package com.vungtv.film.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.vungtv.film.R;
import com.vungtv.film.util.FontUtils;


public class VtvButton extends Button {
    public VtvButton(Context context) {
        super(context);
        init(null);
    }

    public VtvButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VtvButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        int font = FontUtils.FONT_ROBOTO;
        int style = FontUtils.NORMAL;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.VtvFont);
            try {
                font = a.getInt(R.styleable.VtvFont_font, FontUtils.FONT_ROBOTO);
                style = a.getInt(R.styleable.VtvFont_textStyle, FontUtils.NORMAL);
            } finally {
                a.recycle();
            }
        }
        setTypeface(FontUtils.selectTypeface(getContext(), font, style));
    }
}
