package com.vungtv.film.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.FollowNotifyManger;
import com.vungtv.film.data.source.local.UserSessionManager;

public class VtvToolbarHome extends RelativeLayout {

    private View btnMenu, btnSearch, btnVip, btnUser;

    private VtvTextView textUserLabel;

    private OnBtnClickListener onBtnClickListener;

    public VtvToolbarHome(Context context) {
        super(context);
        init();
    }

    public VtvToolbarHome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VtvToolbarHome(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvToolbarHome(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_toolbar_home, this);
        retrieverViews();
        registerListener();
        notifyUserlabelChange();
    }

    private void retrieverViews() {
        btnMenu = findViewById(R.id.toolbar_btn_menu);
        btnSearch = findViewById(R.id.toolbar_btn_search);
        btnVip = findViewById(R.id.toolbar_vip);
        btnUser = findViewById(R.id.toolbar_user);
        textUserLabel = (VtvTextView) findViewById(R.id.toolbar_user_label);
    }

    private void registerListener() {
        btnMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBtnClickListener != null){
                    onBtnClickListener.onBtnNavClick();
                };
            }
        });
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBtnClickListener != null){
                    onBtnClickListener.onBtnSearchClick();
                };
            }
        });
        btnVip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBtnClickListener != null){
                    onBtnClickListener.onBtnVipClick();
                };
            }
        });
        btnUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBtnClickListener != null){
                    onBtnClickListener.onBtnUserClick();
                };
            }
        });
    }

    public void setUserLabelVisibility(boolean show) {
        textUserLabel.setVisibility(show ? VISIBLE : GONE);
    }

    public void setTextUserLabel(String text) {
        if (text == null) return;
        textUserLabel.setText(text);
    }

    public void setTextUserLabel(int number) {
        textUserLabel.setText(String.valueOf(number));
    }

    public void notifyUserlabelChange() {
        if (UserSessionManager.isLogin(getContext())) {
            textUserLabel.setVisibility(VISIBLE);
            setTextUserLabel(FollowNotifyManger.get(getContext()));
        } else {
            textUserLabel.setVisibility(GONE);
        }
    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }

    public interface OnBtnClickListener {
        void onBtnNavClick();
        void onBtnSearchClick();
        void onBtnVipClick();
        void onBtnUserClick();
    }
}
