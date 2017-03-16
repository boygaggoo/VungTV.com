package com.vungtv.film.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.interfaces.OnSocialLoginListener;

import java.util.Arrays;
import java.util.List;


public class LoginFacebookUtils {
    private static final String TAG = LoginFacebookUtils.class.getSimpleName();
    private Activity context;
    private CallbackManager mCallbackManager;
    private OnSocialLoginListener onLoginListener;

    public LoginFacebookUtils(Activity context) {
        this.context = context;
        this.mCallbackManager = CallbackManager.Factory.create();
    }

    public void setOnSocialLoginListener(OnSocialLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    public void login(){
        List<String> permissionNeeds= Arrays.asList("email", "public_profile");

        LoginManager.getInstance().logInWithReadPermissions(context, permissionNeeds);
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResults) {
                        LogUtils.d(TAG, "FB login onAccountChangeSuccess: " + loginResults.getAccessToken().getToken());
                        if (onLoginListener != null)
                            onLoginListener.onSuccess(loginResults.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {
                        LogUtils.d(TAG, "FB login onCancel");
                        if (onLoginListener != null)
                            onLoginListener.onFailure();
                    }

                    @Override
                    public void onError(FacebookException e) {
                        LogUtils.d(TAG, "FB login onError:" + e.getMessage());
                        if (onLoginListener != null)
                            onLoginListener.onFailure();
                    }
                });
    }

    public static void logout(Context context) {
        // Facebook sign out
        LoginManager.getInstance().logOut();
        // Remove session sign out
        UserSessionManager.clearUserSession(context);
    }

    public void setResultFacebookLogin(int requestCode, int resultCode, Intent data){
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
