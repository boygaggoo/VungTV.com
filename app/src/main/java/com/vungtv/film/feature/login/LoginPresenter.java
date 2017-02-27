package com.vungtv.film.feature.login;


import android.content.Context;
import android.content.Intent;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.interfaces.OnSocialLoginListener;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.LoginFacebookUtils;
import com.vungtv.film.util.LoginGoogleUtils;

import org.greenrobot.eventbus.EventBus;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link LoginFragment}) && ({@link RegisterFragment}),
 * retrieves the data and updates the
 * UI as required.
 */
public class LoginPresenter implements LoginContract.Presenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();

    private Context context;

    private final LoginFacebookUtils loginFacebookUtils;

    private final LoginGoogleUtils loginGoogleUtils;

    private final AccountServices accountService;

    private LoginContract.View fragmentView;

    private String userProvider = null;

    public LoginPresenter(Context context, LoginContract.View fragmentView, AccountServices accountService, LoginFacebookUtils loginFacebookUtils, LoginGoogleUtils loginGoogleUtils) {
        this.context = context;
        this.fragmentView = checkNotNull(fragmentView);
        this.accountService = checkNotNull(accountService);
        this.loginFacebookUtils = checkNotNull(loginFacebookUtils);
        this.loginGoogleUtils = checkNotNull(loginGoogleUtils);

        this.fragmentView.setPresenter(this);

        accountServiceResponse();
        setLoginFacebookGoogleEventcallback();
    }

    @Override
    public void start() {
        //start load home data;

    }

    @Override
    public void onDestroy() {
        accountService.cancel();
    }

    @Override
    public void loginWithEmail(String email, String pass) {
        if (email.length() < 2) {
            fragmentView.showMsgError(context.getString(R.string.login_error_msg_email));
            return;
        }

        if (pass.length() < 6) {
            fragmentView.showMsgError(context.getString(R.string.login_error_msg_pass));
            return;
        }

        fragmentView.showLoading(true);
        accountService.loginWithEmail(email, pass);
    }

    @Override
    public void loginWithFacebook() {
        fragmentView.showLoading(true);
        loginFacebookUtils.login();
    }

    @Override
    public void loginWithGoogle() {
        fragmentView.showLoading(true);
        loginGoogleUtils.login();
    }

    @Override
    public void setActivityResult(int requestCode, int resultCode, Intent data) {
        loginFacebookUtils.setResultFacebookLogin(requestCode, resultCode, data);
        loginGoogleUtils.setResultGoogleLogin(requestCode, data);
    }

    @Override
    public void registerAcc(String email, String pass, String rePass) {
        fragmentView.showLoading(true);
        accountService.register(email, pass, rePass);
    }

    @Override
    public void fogetPass() {
        fragmentView.showMsgError("Quên pass");
    }

    @Override
    public void openFragLogin(LoginContract.View view) {
        this.fragmentView = checkNotNull(view);
        this.fragmentView.setPresenter(this);
    }

    @Override
    public void openFragRegister(LoginContract.View view) {
        this.fragmentView = checkNotNull(view);
        this.fragmentView.setPresenter(this);
    }

    private void accountServiceResponse() {
        accountService.setOnLoginResulListener(new AccountServices.OnLoginResulListener() {
            @Override
            public void onSuccess(User user, String token) {
                fragmentView.showLoading(false);

                user.setProvider(userProvider);
                UserSessionManager.setUserSession(context, user, token);
                EventBus.getDefault().post(new AccountModifyEvent());
                userProvider = null;
            }

            @Override
            public void onFailure(int code, String error) {
                fragmentView.showLoading(false);
                fragmentView.showMsgError(error);
            }
        });
    }

    private void setLoginFacebookGoogleEventcallback() {
        loginFacebookUtils.setOnSocialLoginListener(new OnSocialLoginListener() {
            @Override
            public void onSuccess(String token) {
                LogUtils.d(TAG, "facebook token: " + token);
                userProvider = UserSessionManager.PROVIDER_FACE;
                accountService.loginWithSocial(userProvider, token);
            }

            @Override
            public void onFailure() {
                fragmentView.showLoading(false);
                fragmentView.showMsgError(context.getResources().getString(R.string.login_error_msg_login_failed));
            }
        });

        loginGoogleUtils.setOnSocialLoginListener(new OnSocialLoginListener() {
            @Override
            public void onSuccess(String token) {
                LogUtils.d(TAG, "Google token: " + token);
                userProvider = UserSessionManager.PROVIDER_GOOGLE;
                accountService.loginWithSocial(userProvider, token);
            }

            @Override
            public void onFailure() {
                fragmentView.showLoading(false);
                fragmentView.showMsgError(context.getResources().getString(R.string.login_error_msg_login_failed));
            }
        });
    }
}
