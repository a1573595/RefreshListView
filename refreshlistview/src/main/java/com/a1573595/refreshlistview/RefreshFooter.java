package com.a1573595.refreshlistview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import static com.a1573595.refreshlistview.RefreshListView.STATE_LOADING;

public class RefreshFooter extends Footer {
    private ProgressBar progressBar;

    public RefreshFooter(Context context) {
        super(context);
        init();
    }

    public RefreshFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        view = View.inflate(getContext(), R.layout.footer, null);
        progressBar = view.findViewById(R.id.progressBar);

        setVisibility(false);
        addView(view);
    }

    private void setVisibility(boolean visibility){
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                visibility? LayoutParams.WRAP_CONTENT : 0));
    }

    @Override
    protected void setState(@RefreshListView.ListViewState int state){
        switch (state){
            case STATE_LOADING:
                setVisibility(true);
                break;
            default:
                setVisibility(false);
                break;
        }
    }

    public void setIndeterminateTintList(int color) {
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(color));
    }
}
