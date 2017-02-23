package com.vungtv.film.feature.logout;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.util.LoginGoogleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 *
 * Created by pc on 2/22/2017.
 */

public class LogOutActivity extends BaseActivity {
    public static final int INTENT_RC = 111;

    private LoginGoogleUtils loginGoogleUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        EventBus.getDefault().register(this);

        loginGoogleUtils = new LoginGoogleUtils(this);

        UserSessionManager.logout(this, loginGoogleUtils);
        popupLoading.show();
    }

    @Override
    protected void onDestroy() {
        if (loginGoogleUtils != null) {
            loginGoogleUtils.disconect();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void accountModifyEvent(AccountModifyEvent event) {
        popupLoading.dismiss();
        finish();
    }
}
