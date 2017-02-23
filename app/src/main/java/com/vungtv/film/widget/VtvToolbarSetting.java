package com.vungtv.film.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.vungtv.film.R;

public class VtvToolbarSetting extends RelativeLayout {

    private View btnBack;
    private VtvTextView textTitle;

    private String title;

    private OnToolbarListener onToolbarListener;

    public VtvToolbarSetting(Context context) {
        super(context);
        init(null);
    }

    public VtvToolbarSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VtvToolbarSetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvToolbarSetting(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.VtvToolbar, 0, 0);
            try {
                title = a.getString(R.styleable.VtvToolbar_textTitle);
            } finally {
                a.recycle();
            }
        }
        retrieverViews();
        registerListener();
        if (title != null) {
            textTitle.setText(title);
        }
    }

    protected void retrieverViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_toolbar_setting, this);
        btnBack = findViewById(R.id.toolbar_btn_back);
        textTitle = (VtvTextView) findViewById(R.id.toolbar_title);
    }

    protected void registerListener() {
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onToolbarListener != null){
                    onToolbarListener.onBtnBackClick();
                };
            }
        });
    }

    public void setTitleVisibility(boolean show) {
        textTitle.setVisibility(show ? VISIBLE : GONE);
    }

    public void setTitle(String text) {
        if (text == null) return;
        title = text;
        textTitle.setText(text);
    }

    public void setOnToolbarListener(OnToolbarListener onToolbarListener) {
        this.onToolbarListener = onToolbarListener;
    }

    public interface OnToolbarListener {
        void onBtnBackClick();
    }
}
