package com.vungtv.film.feature.loading;

import android.content.Context;

import com.vungtv.film.BuildConfig;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LoginGoogleUtils;
import com.vungtv.film.util.StringUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class LoadingPresenter implements LoadingContract.Presenter{

    private final Context context;
    private final LoadingContract.View activityView;

    private LoginGoogleUtils loginGoogleUtils;

    private final AccountServices accountServices;


    public LoadingPresenter(Context context, LoadingContract.View loadingView, LoginGoogleUtils loginGoogleUtils) {
        this.context = context;
        this.activityView = checkNotNull(loadingView);
        this.loginGoogleUtils = loginGoogleUtils;

        accountServices = new AccountServices(context.getApplicationContext());

        loadingView.setPresenter(this);
        accountServiceResponse();
    }

    @Override
    public void start() {
        activityView.setVersionName(String.format("V %s", BuildConfig.VERSION_NAME));
        checkAccountInfo();
    }

    @Override
    public void onDestroy() {
        accountServices.cancel();
        loginGoogleUtils.disconect();
    }

    @Override
    public void checkAccountInfo() {

        if (!StringUtils.isEmpty(UserSessionManager.getAccessToken(context))) {
            accountServices.checkAccountInfo(UserSessionManager.getAccessToken(context));
        } else {
            activityView.startHomePage();
        }
    }

    @Override
    public void logoutAccount() {
        UserSessionManager.logout(context, loginGoogleUtils);
    }

    @Override
    public void refreshAccessToken(String token) {
        UserSessionManager.setAccessToken(context, token);
    }

    private void accountServiceResponse() {
        accountServices.setOnRefreshTokenListener(new AccountServices.OnRefreshTokenListener() {
            @Override
            public void onRefreshToken(String token) {
                UserSessionManager.setAccessToken(context, token);
                checkAccountInfo();
            }
        });

        accountServices.setOnLoginResulListener(new AccountServices.OnLoginResulListener() {
            @Override
            public void onSuccess(User user, String token) {
                UserSessionManager.updateUserSession(context, user);
                activityView.startHomePage();
            }

            @Override
            public void onFailure(int code, String error) {
                if (code == -999) {
                    logoutAccount();
                }

                activityView.startHomePage();
            }
        });
    }
}
