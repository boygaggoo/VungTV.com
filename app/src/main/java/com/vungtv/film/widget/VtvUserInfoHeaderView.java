package com.vungtv.film.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.vungtv.film.R;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.TextUtils;
import com.vungtv.film.util.TimeUtils;

public class VtvUserInfoHeaderView extends LinearLayout {

    private static final String TAG = VtvUserInfoHeaderView.class.getSimpleName();

    private static final int AVATAR_DEFAULT = R.drawable.icon_human2;

    private VtvTextView textUserName;

    private VtvTextView textVipExpired;

    private VtvTextView textVungPoint;

    private VtvCircularImageView imgAvatar;

    private View btnChangeAvatar;

    private View textLogin;

    private OnUserPageHeaderListener onUserPageHeaderListener;

    private boolean isLogin = false;

    public VtvUserInfoHeaderView(Context context) {
        super(context);
        init();
    }

    public VtvUserInfoHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VtvUserInfoHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvUserInfoHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void init() {
        retrieverViews();
        registerListener();
        setLayoutOfflineOrOnline();
    }

    protected void retrieverViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_user_info_view, this);
        textUserName = (VtvTextView) findViewById(R.id.userpage_tv_username);
        textVipExpired = (VtvTextView) findViewById(R.id.userpage_tv_vip);
        textVungPoint = (VtvTextView) findViewById(R.id.userpage_tv_vung);
        textLogin = findViewById(R.id.userpage_tv_offline_login);
        imgAvatar = (VtvCircularImageView) findViewById(R.id.userpage_img_avatar);
        btnChangeAvatar = findViewById(R.id.userpage_btn_change_avatar);
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(User user) {
        this.isLogin = user != null;
        setLayoutOfflineOrOnline();
        setUserInfo(user);
    }

    public void setLogout() {
        isLogin = false;
        setLayoutOfflineOrOnline();
        setUserInfo(null);
    }

    public void setUserInfo(User user) {
        if (user == null) {
            // Chưa đăng nhập;
            setImgAvatar(AVATAR_DEFAULT);
            setTextUserName("Tài khoản");
            setTextVipExpired("0");
            setTextVungPoint(0);
            return;
        };

        // Đã đăng nhập
        setImgAvatar(user.getUserPhoto());

        String uname = user.getUserDisplayName();
        if (uname == null || uname.length() < 2) {
            uname = user.getUserEmail();
        }
        setTextUserName(uname);

        setTextVipExpired(user.getUserVipDate());

        setTextVungPoint(user.getUserBalance());
    }

    public void setImgAvatar(@DrawableRes int iconRes) {
        imgAvatar.setImageResource(iconRes);
    }

    public void setImgAvatar(String url) {
        if (url == null || url.length() < 5) return;
        Picasso.with(getContext())
                .load(url.replace("", ""))
                .placeholder(AVATAR_DEFAULT)
                .error(AVATAR_DEFAULT)
                .fit()
                .into(imgAvatar);
    }

    public void setTextUserName(String userName) {
        if (userName == null) return;
        textUserName.setText(userName);
    }

    public void setTextVipExpired(String timeStampStr) {
        LogUtils.d(TAG, "setTextVipExpired: timeStampStr = " + timeStampStr);

        long timeStamp = 0;
        try {
            if (timeStampStr != null) {
                timeStampStr = timeStampStr.replace(" ", "");
            }
            timeStamp = Long.parseLong(timeStampStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (timeStamp == 0) {
            textVipExpired.setText(String.format(
                    getContext().getResources().getString(R.string.personal_text_vip_expired),
                    "--/--/----"));
        } else {
            textVipExpired.setText(String.format(
                    getContext().getResources().getString(R.string.personal_text_vip_expired),
                    TimeUtils.convertTimeStampToDate(timeStamp)));
        }
    }

    public void setTextVungPoint(int point) {
        String text = String.format(
                getContext().getResources().getString(R.string.personal_text_vung_con),
                String.valueOf(point)
        );
        textVungPoint.setText(TextUtils.styleTextHtml(text));
    }

    public void setOnUserPageHeaderListener(OnUserPageHeaderListener onUserPageHeaderListener) {
        this.onUserPageHeaderListener = onUserPageHeaderListener;
    }

    private void setLayoutOfflineOrOnline() {
        int visibleOnline = isLogin ? VISIBLE : GONE;
        int visibleOffline = isLogin ? GONE : VISIBLE;
        textUserName.setVisibility(visibleOnline);
        textVipExpired.setVisibility(visibleOnline);
        textVungPoint.setVisibility(visibleOnline);
        btnChangeAvatar.setVisibility(visibleOnline);

        textLogin.setVisibility(visibleOffline);
    }

    private void registerListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogin && onUserPageHeaderListener != null) {
                    onUserPageHeaderListener.onBtnLoginClick();
                }
            }
        });
        btnChangeAvatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin && onUserPageHeaderListener != null) {
                    onUserPageHeaderListener.onBtnChangeAvatar();
                }
            }
        });
    }

    public interface OnUserPageHeaderListener {
        void onBtnLoginClick();

        void onBtnChangeAvatar();
    }
}
