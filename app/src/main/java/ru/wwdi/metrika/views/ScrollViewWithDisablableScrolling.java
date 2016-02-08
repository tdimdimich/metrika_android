package ru.wwdi.metrika.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by ryashentsev on 29.04.14.
 */
public class ScrollViewWithDisablableScrolling extends ScrollView {

    private boolean mScrollingEnabled = false;

    public ScrollViewWithDisablableScrolling(Context context) {
        super(context);
    }

    public ScrollViewWithDisablableScrolling(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewWithDisablableScrolling(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollingEnabled(boolean enabled){
        mScrollingEnabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mScrollingEnabled) return super.onInterceptTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mScrollingEnabled) return super.onTouchEvent(ev);
        return false;
    }

}

