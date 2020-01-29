package com.a1573595.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public abstract class Footer extends LinearLayout{
    protected View view;

    public Footer(Context context) {
        super(context);
    }

    public Footer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected abstract void setState(@RefreshListView.ListViewState int state);

    void setBottomMargin(int height){
        LayoutParams lp = (LayoutParams)view.getLayoutParams();
        lp.bottomMargin = height < 0 ? 0 : height;
        view.setLayoutParams(lp);
    }

    int getBottomMargin(){
        LayoutParams lp = (LayoutParams)view.getLayoutParams();
        return lp.bottomMargin;
    }
}
