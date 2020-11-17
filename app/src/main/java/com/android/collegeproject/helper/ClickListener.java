package com.android.collegeproject.helper;

import android.view.View;

public abstract class ClickListener implements View.OnClickListener {

    //Double click time-gap 300 milliseconds
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;

    long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            onDoubleClick(v);
            lastClickTime = 0;
        } else {
            onSingleClick(v);
        }
        lastClickTime = clickTime;
    }

    public abstract void onSingleClick(View v);
    public abstract void onDoubleClick(View v);
}
