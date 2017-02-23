package com.vungtv.film.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vungtv.film.R;

public class VtvDrawerLayout extends DrawerLayout {
    private static final String TAG = VtvDrawerLayout.class.getSimpleName();
    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;

    protected boolean isLockOpen = false;

    public VtvDrawerLayout(Context context) {
        super(context);
        //setLockOpenMode(context, null);
    }

    public VtvDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setLockOpenMode(context, attrs);
    }

    public VtvDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //setLockOpenMode(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        View drawer = getChildAt(1);

        return !(getDrawerLockMode(drawer) == LOCK_MODE_LOCKED_OPEN
                && ev.getRawX() > drawer.getWidth()) && super.onInterceptTouchEvent(ev);
    }

    private void setLockOpenMode(Context context, AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.VtvDrawerLayout);

        isLockOpen = attributeArray.getBoolean(R.styleable.VtvDrawerLayout_lock_open, false);

        if (isLockOpen) {
            setDrawerLockMode(LOCK_MODE_LOCKED_OPEN);
            setScrimColor(Color.TRANSPARENT);
        }
        attributeArray.recycle();
    }

    public void setModeLockOpen(View draw) {
        isLockOpen = true;
        setDrawerLockMode(LOCK_MODE_LOCKED_OPEN);
        setScrimColor(Color.TRANSPARENT);
        if (draw != null) {
            openDrawer(draw, false);
        }
    }

    public void disableModeLockOpen(View draw) {
        isLockOpen = false;
        setDrawerLockMode(LOCK_MODE_UNLOCKED);
        setScrimColor(DEFAULT_SCRIM_COLOR);
        if (draw != null) {
            closeDrawer(draw, false);
        }
    }

    public boolean isLockOpen() {
        return isLockOpen;
    }
}
