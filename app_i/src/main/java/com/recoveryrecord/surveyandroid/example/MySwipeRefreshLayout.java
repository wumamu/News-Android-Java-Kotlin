package com.recoveryrecord.surveyandroid.example;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

class MySwipeRefreshLayout extends SwipeRefreshLayout {
    private int mTouchSlop;
    private float mPrevx;
    private float mPrevy;
    public MySwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevx = MotionEvent.obtain(ev).getX();
                mPrevy = MotionEvent.obtain(ev).getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float evX = ev.getX();
                final float evy = ev.getY();
                float xDiff = Math.abs(evX - mPrevx);
                float yDiff = Math.abs(evy - mPrevy);
                if (xDiff > mTouchSlop && xDiff > yDiff) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
