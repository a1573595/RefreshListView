package com.a1573595.refreshlistview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RefreshListView extends SwipeRefreshLayout implements AbsListView.OnScrollListener, View.OnTouchListener{
    // footer state
    static final int STATE_NORMAL = 0;
    static final int STATE_READY = 1;
    static final int STATE_LOADING = 2;

    @IntDef({STATE_NORMAL, STATE_READY, STATE_LOADING})
    @Retention(RetentionPolicy.SOURCE)
    @interface ListViewState { }

    private static final int SCROLL_DURATION = 400; // scroll back duration
    private static final int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
    private static final float OFFSET_RADIO = 1.8f; // support iOS like pull

    private float mLastY = -1f; // save event y
    private Scroller mScroller = new Scroller(getContext(), new DecelerateInterpolator()); // used for scroll back
    private int mTotalItemCount = 0; // total list items, used to detect is at the bottom of listView.

    private boolean enableLoadMore = true;  // enable load more
    private boolean isLoading = false;    // is loading
    private boolean loadingLock = false; // avoid resetFooter trigger loading twice
    private long REST_TIME = 10000; // reset countdown

    private View root;
    private ListView listView;
    private Footer footer;

    private OnUpdateListener updateListener = null;
    private OnFailedListener onFailedListener = null;

    public interface OnUpdateListener {
        void onRefresh();
        void onLoadMore();
    }

    public interface OnFailedListener {
        void onRefreshFailed();
        void onLoadMoreFailed();
    }

    private Handler failedHandler = new Handler();

    private Runnable stopRefreshRunnable = () -> {
        stopRefresh();

        if(onFailedListener != null)
            onFailedListener.onRefreshFailed();
    };

    private Runnable stopLoadMoreRunnable = () -> {
        stopLoadMore();

        if(onFailedListener != null)
            onFailedListener.onLoadMoreFailed();
    };

    public RefreshListView(@NonNull Context context) {
        super(context);

        setup();
    }

    public RefreshListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setup();
    }

    private void setup() {
        setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        root = View.inflate(getContext(), R.layout.listview, null);

        listView = root.findViewById(R.id.listView);

        footer = new Footer(getContext());
        listView.addFooterView(footer);
        listView.setFooterDividersEnabled(false);

        this.addView(root);

        setOnRefreshListener(this::startRefresh);
        listView.setOnScrollListener(this);
        listView.setOnTouchListener(this);
    }

    public void setResetTime(long milliseconds) {
        REST_TIME = milliseconds;
    }

    public void setRefreshEnable(boolean enable) {
        setEnabled(enable);
        setRefreshing(enable);

        stopRefresh();
    }

    public void setLoadMoreEnable(boolean enable) {
        enableLoadMore = enable;

        footer.setVisibility(enable? VISIBLE : GONE);

        stopLoadMore();
    }

    private void startRefresh(){
        stopLoadMore();

        if(REST_TIME > 0) {
            failedHandler.postDelayed(stopRefreshRunnable, REST_TIME);
        }

        if(updateListener != null) {
            updateListener.onRefresh();
        }
    }

    private void startLoadMore() {
        isLoading = true;
        loadingLock = true;
        stopRefresh();

        footer.setState(STATE_LOADING);

        if(REST_TIME > 0) {
            failedHandler.postDelayed(stopLoadMoreRunnable, REST_TIME);
        }

        if(updateListener != null) {
            updateListener.onLoadMore();
        }
    }

    public void stopRefresh() {
        failedHandler.removeCallbacksAndMessages(null);

        if(isRefreshing()) {
            setRefreshing(false);
        }
    }

    public void stopLoadMore() {
        failedHandler.removeCallbacksAndMessages(null);

        if (isLoading) {
            isLoading = false;
            footer.setState(STATE_NORMAL);
        }
    }

    public void setListViewPadding(int start, int top, int end, int bottom) {
        int density = (int)getContext().getResources().getDisplayMetrics().density;

        listView.setPaddingRelative(start * density, top * density,
                end * density, bottom * density);
    }

    public void setHeaderDividersEnabled(boolean headerDividersEnabled) {
        listView.setHeaderDividersEnabled(headerDividersEnabled);
    }

    public void setFooterDividersEnabled(boolean footerDividersEnabled) {
        listView.setFooterDividersEnabled(footerDividersEnabled);
    }

    public void setDividerHeight(int height) {
        listView.setDividerHeight(height);
    }

    public void setDivider(@Nullable Drawable divider) {
        listView.setDivider(divider);
    }

    public void addHeaderView(View header) {
        listView.addHeaderView(header);
    }

    public void removeHeaderView(View header) {
        listView.removeHeaderView(header);
    }

//    public void addFooterView(Footer footer) {
//        listView.removeFooterView(this.footer);
//
//        this.footer = footer;
//        listView.addFooterView(footer);
//    }

    public void setAdapter(ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public void setSelection(int position) {
        listView.setSelection(position);
    }

    public void setProgressBarColor(int color) {
        footer.setIndeterminateTintList(ColorStateList.valueOf(color));
    }

    public void setUpdateListener(OnUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void setFailedListener(OnFailedListener onFailedListener) {
        this.onFailedListener = onFailedListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) { }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        mTotalItemCount = totalItemCount;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            footer.setBottomMargin(mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (mLastY == -1f)
            mLastY = ev.getRawY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (listView.getLastVisiblePosition() == mTotalItemCount - 1 &&
                        (footer.getBottomMargin() > 0 || deltaY < 0) && enableLoadMore) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                    if (!isLoading && !loadingLock){
                        startLoadMore();
                    }
                }
                break;
            default:
                mLastY = -1f; // reset
                if (listView.getLastVisiblePosition() == mTotalItemCount - 1) {
                    resetFooterHeight();
                }
                loadingLock = false;
                break;
        }

        return false;
    }

    private void updateFooterHeight(float delta) {
        int height = footer.getBottomMargin() +  Math.round(delta);
        if(enableLoadMore && !isLoading){
            if (height > PULL_LOAD_MORE_DELTA) {
                footer.setState(STATE_READY);
            } else {
                footer.setState(STATE_NORMAL);
            }
        }
        footer.setBottomMargin(height);
    }

    private void resetFooterHeight() {
        int bottomMargin = footer.getBottomMargin();
        if (bottomMargin > 0) {
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }
}
