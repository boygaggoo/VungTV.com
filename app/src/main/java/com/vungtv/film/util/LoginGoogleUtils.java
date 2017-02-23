package com.vungtv.film.util;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.interfaces.OnSocialLoginListener;

public class LoginGoogleUtils {
    private static final String TAG = LoginGoogleUtils.class.getSimpleName();
    private static final int RC_SIGN_IN_GG = 9001;

    private FragmentActivity activity;
    private GoogleApiClient mGoogleApiClient;
    private OnSocialLoginListener onLoginListener;

    public LoginGoogleUtils(FragmentActivity activity) {
        this.activity = activity;
        getGoogleLogin();
    }

    public void setOnSocialLoginListener(OnSocialLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    /**
     * Start login with GG
     */
    public void login() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN_GG);
    }

    /**
     * Logout;
     */
    public void logout() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    if(mGoogleApiClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) {
                                    UserSessionManager.clearUserSession(activity);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {
                    LogUtils.d(TAG, "Google API Client Connection Suspended");
                    UserSessionManager.clearUserSession(activity);
                }
            });

            return;
        }

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

                if (status.isSuccess()) {
                    // Remove session sign out
                    UserSessionManager.clearUserSession(activity);
                }
            }
        });
    }

    /**
     * set data reusult callback from google login;
     *
     * @param requestCode int
     * @param data intent
     */
    public void setResultGoogleLogin(int requestCode, Intent data) {
        if (onLoginListener == null) return;

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GG) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            LogUtils.d(TAG, "GG login sccess = " + result.isSuccess());
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                if (account != null) {
                    onLoginListener.onSuccess(account.getIdToken());
                    LogUtils.e(TAG, "ServerAuthCode: " + new Gson().toJson(account));
                    return;
                }
            }

            onLoginListener.onFailure();
        }
    }

    public void disconect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(activity);
            mGoogleApiClient.disconnect();
        }
    }

    private void getGoogleLogin() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity.getApplication().getApplicationContext())
                .enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        if (onLoginListener != null)
                            onLoginListener.onFailure();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

}
