package com.vungtv.film.feature.loading;

import android.content.Context;

import com.vungtv.film.BuildConfig;
import com.vungtv.film.data.source.local.RemoteConfigManager;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.data.source.remote.service.ConfigServices;
import com.vungtv.film.model.Config;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LoginGoogleUtils;
import com.vungtv.film.util.StringUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class LoadingPresenter implements LoadingContract.Presenter{

    private final Context context;

    private final LoadingContract.View activityView;

    private final AccountServices accountServices;

    private final ConfigServices configServices;

    private LoginGoogleUtils loginGoogleUtils;

    private boolean isGetConfigSuccess = false;

    private boolean isCheckLoginSuccess = false;

    public LoadingPresenter(Context context, LoadingContract.View loadingView, LoginGoogleUtils loginGoogleUtils) {
        this.context = context;
        this.activityView = checkNotNull(loadingView);
        this.loginGoogleUtils = loginGoogleUtils;
        loadingView.setPresenter(this);

        accountServices = new AccountServices(context.getApplicationContext());
        configServices = new ConfigServices(context.getApplicationContext());

        accountServiceResponse();
        configServicesResponse();
    }

    @Override
    public void start() {
        activityView.setVersionName(String.format("V %s", BuildConfig.VERSION_NAME));
        checkAccountInfo();
        getAppConfig();
    }

    @Override
    public void onDestroy() {
        accountServices.cancel();
        configServices.cancel();
        loginGoogleUtils.disconect();
    }

    @Override
    public void checkAccountInfo() {
        if (!StringUtils.isEmpty(UserSessionManager.getAccessToken(context))) {
            accountServices.checkAccountInfo(UserSessionManager.getAccessToken(context));
        } else {
            isCheckLoginSuccess = true;
        }
    }

    @Override
    public void getAppConfig() {
        configServices.loadConfig();
    }

    private void openActHome() {
        if (isGetConfigSuccess && isCheckLoginSuccess) {
            activityView.openActHome();
        }
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
                isCheckLoginSuccess = true;
                openActHome();
            }

            @Override
            public void onFailure(int code, String error) {
                if (code == -999) {
                    accountServices.logout(UserSessionManager.getAccessToken(context));
                } else {
                    isCheckLoginSuccess = true;
                    openActHome();
                }
            }
        });

        accountServices.setOnLogoutListener(new AccountServices.OnLogoutListener() {
            @Override
            public void onLogoutSuccess() {
                UserSessionManager.logout(context, loginGoogleUtils);

                isCheckLoginSuccess = true;
                openActHome();
            }

            @Override
            public void onFailure(int code, String error) {
                isCheckLoginSuccess = true;
                openActHome();
            }
        });
    }

    private void configServicesResponse() {
        configServices.setResultCallback(new ConfigServices.ResultCallback() {
            @Override
            public void onGetConfigSuccess(Config config) {
                RemoteConfigManager.setConfigs(context, config);

                isGetConfigSuccess = true;
                openActHome();
            }

            @Override
            public void onFailure(int code, String error) {
                isGetConfigSuccess = true;
                openActHome();
            }
        });
    }
}
