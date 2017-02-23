package com.vungtv.film.feature.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.util.ActivityUtils;
import com.vungtv.film.util.LoginFacebookUtils;
import com.vungtv.film.util.LoginGoogleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);

        Fragment fragment = new LoginFragment();
        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(), fragment, R.id.login_framelayout);

        loginPresenter = new LoginPresenter(this, (LoginFragment) fragment,
                new AccountServices(this), new LoginFacebookUtils(this), new LoginGoogleUtils(this));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        loginPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginPresenter.setActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void eventLoginSuccess(AccountModifyEvent event) {
        finish();
    }

    @OnClick(R.id.login_btn_exit)
    public void exit() {
        finish();
    }
}
