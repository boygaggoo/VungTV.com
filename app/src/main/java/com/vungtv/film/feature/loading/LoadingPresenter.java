package com.vungtv.film.feature.loading;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.vungtv.film.BuildConfig;
import com.vungtv.film.data.source.local.RemoteConfigManager;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.data.source.remote.service.ConfigServices;
import com.vungtv.film.feature.home.HomeActivity;
import com.vungtv.film.model.Config;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LoginGoogleUtils;
import com.vungtv.film.util.StringUtils;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;
import static com.vungtv.film.feature.loading.LoadingActivity.INTENT_MOV_ID_NOTIFY;


public class LoadingPresenter implements LoadingContract.Presenter{

    private final Context context;

    private final LoadingContract.View activityView;

    private final AccountServices accountServices;

    private final ConfigServices configServices;

    private LoginGoogleUtils loginGoogleUtils;

    private boolean isGetConfigSuccess = false;

    private boolean isCheckLoginSuccess = false;

    private int movId = -1;

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

        if (HomeActivity.isCreated) {
            activityView.resumeActHome(movId);
        } else {
            checkAccountInfo();
            getAppConfig();
        }
    }

    @Override
    public void onDestroy() {
        accountServices.cancel();
        configServices.cancel();
        loginGoogleUtils.disconect();
    }

    @Override
    public void getIntent(Intent intent) {
        if (intent == null) return;

        if (intent.hasExtra(INTENT_MOV_ID_NOTIFY)) {
            // Intent frome notification;
            movId = intent.getIntExtra(INTENT_MOV_ID_NOTIFY, -1);
            return;
        }

        // intent frome deep link
        Uri uri = intent.getData();
        if (uri == null) return;

        List<String> paths = uri.getPathSegments();
        if (paths.size() > 1) {
            String s = paths.get(0);
            if (s.equalsIgnoreCase("xemphim")) {
                String[] ss = paths.get(1).split("-");

                movId = Integer.parseInt(ss[ss.length-1]);
            } else if (s.equalsIgnoreCase("phim")) {
                String ss = uri.getQueryParameter("mov_id");
                if (StringUtils.isNotEmpty(ss)) {
                    movId = Integer.parseInt(ss);
                }
            }
        }
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
            activityView.openActHome(movId);
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
