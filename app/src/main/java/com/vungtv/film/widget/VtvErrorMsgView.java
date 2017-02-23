package com.vungtv.film.widget;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

import com.vungtv.film.R;
import com.vungtv.film.util.DensityUtils;

/**
 * Text view show error;
 *
 * Created by Mr Cuong on 2/2/2017.
 */

public class VtvErrorMsgView extends VtvTextView {

    public VtvErrorMsgView(Context context) {
        super(context);
        init();
    }

    public VtvErrorMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VtvErrorMsgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VtvErrorMsgView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setTextStyle(R.style.AppTheme_Text);
        setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.text_dark_2, null));
        setGravity(Gravity.CENTER);
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.margin);
        setPadding(padding, padding, padding, padding);
    }

    public void setTextColorBack() {
        setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.text_dark_2, null));
    }

    public void setTextColorWhite() {
        setTextColor(ResourcesCompat.getColor(getContext().getResources(), R.color.text_light_2, null));
    }

    public void setWidthHeight() {
        int w = (int) DensityUtils.getWidthInPx(getContext());
        int h = (int) DensityUtils.getHeightInPx(getContext());
        setLayoutParams(new ViewGroup.LayoutParams(w, h));
    }

    public void setWidthHeight(ViewGroup viewGroup) {
        int w = viewGroup.getWidth();
        int h = viewGroup.getHeight();
        setLayoutParams(new ViewGroup.LayoutParams(w, h));
    }
}
