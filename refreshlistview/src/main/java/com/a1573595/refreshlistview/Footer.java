package com.a1573595.refreshlistview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import static com.a1573595.refreshlistview.RefreshListView.STATE_LOADING;

class Footer extends LinearLayout {
    private View view = View.inflate(getContext(), R.layout.footer, null);

    private ProgressBar progressBar;

    public Footer(Context context) {
        super(context);
        init();
    }

    public Footer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(
                getContext().getResources().getColor(R.color.colorAccent)));

        setVisibility(false);
        addView(view);
    }

    private void setVisibility(boolean visibility){
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                visibility? LayoutParams.WRAP_CONTENT : 0));
    }

    void setState(@RefreshListView.ListViewState int state){
        switch (state){
            case STATE_LOADING:
                setVisibility(true);
                break;
            default:
                setVisibility(false);
                break;
        }
    }

    void setBottomMargin(int height){
        LayoutParams lp = (LayoutParams)view.getLayoutParams();
        lp.bottomMargin = height < 0 ? 0 : height;
        view.setLayoutParams(lp);
    }

    int getBottomMargin(){
        LayoutParams lp = (LayoutParams)view.getLayoutParams();
        return lp.bottomMargin;
    }

    void setIndeterminateTintList(ColorStateList tint) {
        progressBar.setIndeterminateTintList(tint);
    }
}
