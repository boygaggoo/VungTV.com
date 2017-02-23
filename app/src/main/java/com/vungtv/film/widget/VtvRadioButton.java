package com.vungtv.film.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.vungtv.film.util.FontUtils;

/**
 * Custom font for app
 *
 * Created by pc on 1/18/2017.
 */

public class VtvRadioButton extends RadioButton {

    public VtvRadioButton(Context context) {
        super(context);
        setTypeface(FontUtils.selectTypeface(context, null));
    }

    public VtvRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(FontUtils.selectTypeface(context, attrs));
    }

    public VtvRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(FontUtils.selectTypeface(context, attrs));
    }

    public void setTextStyle(int style) {
        setTypeface(FontUtils.selectTypeface(getContext(), style));
    }
}
