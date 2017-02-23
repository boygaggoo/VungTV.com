package com.vungtv.film.widget.sliderview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.vungtv.film.R;
import com.vungtv.film.model.Slider;
import com.vungtv.film.widget.PagerIndicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Slider image with indicator;
 */
public class VtvSliderView extends LinearLayout {
    private static final String TAG = VtvSliderView.class.getSimpleName();
    private static final float RATIO_COVER_HW = 0.404296875f;
    private static final float TABLET_LAND_PAGER_W = 0.8f;
    private static final int TIME_SLIDER = 3000; // mili second

    protected SliderAdapter adapter;
    protected ViewPager viewPager;
    protected CirclePageIndicator pageIndicator;

    private int currentItem = 0;
    private boolean runLeftToRight = true;
    private Handler handlerRunSlider = new Handler();
    private Runnable runnableRunSlider = new Runnable() {
        @Override
        public void run() {
            if (viewPager != null && adapter != null && adapter.getCount() > 1) {
                int itemCount = adapter.getCount();
                if (getContext().getResources().getBoolean(R.bool.isTabletLand)) {
                    itemCount -= 1;
                }
                if (currentItem < itemCount - 1) {
                    if (runLeftToRight) {
                        currentItem++;
                    } else {
                        currentItem -= 1;
                    }
                    if (currentItem == 0) runLeftToRight = true;
                } else {
                    runLeftToRight = false;
                    currentItem -= 1;
                }
                //LogUtil.d(TAG, "currentItem = " + currentItem);
                //LogUtil.d(TAG, "runLeftToRight = " + runLeftToRight);
                viewPager.setCurrentItem(currentItem, true);

                handlerRunSlider.postDelayed(runnableRunSlider, TIME_SLIDER);
            }
        }
    };

    public VtvSliderView(Context context) {
        super(context);
        init();
    }

    public VtvSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VtvSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvSliderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void init() {
        retrieverViews();
        changeConfigOrientation();
    }

    protected void retrieverViews() {
        View.inflate(getContext(), R.layout.widget_slider_view, this);
        viewPager = (ViewPager) findViewById(R.id.home_row_viewPager);
        pageIndicator = (CirclePageIndicator) findViewById(R.id.home_row_pagerIndicator);
    }

    public void setupAdapter(FragmentManager fm) {
        viewPager.setOffscreenPageLimit(2);
        adapter = new SliderAdapter(getContext(), fm);
        viewPager.setAdapter(adapter);
        pageIndicator.setViewPager(viewPager);
    }

    public void setData(ArrayList<Slider> listSlider) {
        if (adapter != null) adapter.addMultiItem(listSlider);
    }

    public void changeConfigOrientation() {

        // Device is tablet
        if (getContext().getResources().getBoolean(R.bool.isTabletLand)) {
            int w = getContext().getResources().getDisplayMetrics().widthPixels;
            int margin = getContext().getResources().getDimensionPixelSize(R.dimen.margin);
            int navW = getContext().getResources().getDimensionPixelSize(R.dimen.home_nav_size_w);
            int pagerW = w - navW;
            int padding = (int) (pagerW * (1 - TABLET_LAND_PAGER_W));
            viewPager.getLayoutParams().height = (int) (pagerW * TABLET_LAND_PAGER_W * RATIO_COVER_HW);
            viewPager.setClipToPadding(false);
            viewPager.setPageMargin(margin);
            viewPager.setPadding(padding / 2 + margin * 2, 0, 0, 0);
        } else {
            int w = getContext().getResources().getDisplayMetrics().widthPixels;
            viewPager.getLayoutParams().height = (int) (w * RATIO_COVER_HW);
            viewPager.setClipToPadding(true);
            viewPager.setPageMargin(0);
        }
    }

    public void startSlider() {
        handlerRunSlider.removeCallbacks(runnableRunSlider);
        handlerRunSlider.postDelayed(runnableRunSlider, TIME_SLIDER);
    }

    public void stopSlider() {
        handlerRunSlider.removeCallbacks(runnableRunSlider);
    }
}
