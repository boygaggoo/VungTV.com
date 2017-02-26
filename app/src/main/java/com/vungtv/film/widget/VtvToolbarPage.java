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

public class VtvToolbarPage extends RelativeLayout {

    private View btnBack, btnSearch, btnFilter;

    private VtvTextView textTitle;

    private String title;

    private OnToolbarPageListener onToolbarPageListener;

    public VtvToolbarPage(Context context) {
        super(context);
        init(null);
    }

    public VtvToolbarPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VtvToolbarPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvToolbarPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.VtvToolbar, 0, 0);
            try {
                title = a.getString(R.styleable.VtvToolbar_textTitle);
            } finally {
                a.recycle();
            }
        }
        LayoutInflater.from(getContext()).inflate(R.layout.widget_toolbar_page, this);
        retrieverViews();
        registerListener();
        if (title != null) {
            textTitle.setText(title);
        }
    }

    private void retrieverViews() {
        btnBack = findViewById(R.id.toolbar_btn_back);
        btnSearch = findViewById(R.id.toolbar_btn_search);
        btnFilter = findViewById(R.id.toolbar_btn_filter);

        textTitle = (VtvTextView) findViewById(R.id.toolbar_title);
    }

    private void registerListener() {
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onToolbarPageListener != null){
                    onToolbarPageListener.onBtnBackClick();
                };
            }
        });
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onToolbarPageListener != null){
                    onToolbarPageListener.onSearchClick();
                };
            }
        });
        btnFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onToolbarPageListener != null){
                    onToolbarPageListener.onBtnFilterClick();
                };
            }
        });
    }

    public void setTitleVisibility(boolean show) {
        textTitle.setVisibility(show ? VISIBLE : GONE);
    }

    public void setTitle(String text) {
        if (text == null) return;
        this.title = text;
        textTitle.setText(text);
    }

    public void setOnToolbarPageListener(OnToolbarPageListener onToolbarPageListener) {
        this.onToolbarPageListener = onToolbarPageListener;
    }

    public interface OnToolbarPageListener {
        void onBtnBackClick();
        void onSearchClick();
        void onBtnFilterClick();
    }
}
