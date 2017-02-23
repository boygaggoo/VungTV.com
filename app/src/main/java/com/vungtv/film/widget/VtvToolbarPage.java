package com.vungtv.film.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.vungtv.film.R;

public class VtvToolbarPage extends RelativeLayout {

    private View btnBack, btnSearch, btnFilter;
    private VtvTextView textTitle;

    private OnToolbarPageListener onToolbarPageListener;

    public VtvToolbarPage(Context context) {
        super(context);
        init();
    }

    public VtvToolbarPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VtvToolbarPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvToolbarPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_toolbar_page, this);
        retrieverViews();
        registerListener();
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
