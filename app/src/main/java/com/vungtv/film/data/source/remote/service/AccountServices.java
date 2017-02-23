package com.vungtv.film.data.source.remote.service;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.RetrofitBuild;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiAccount;
import com.vungtv.film.data.source.remote.model.ApiModel;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.MCrypt;
import com.vungtv.film.util.NetworkUtils;
import com.vungtv.film.util.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *
 *
 * Created by pc on 2/3/2017.
 */

public class AccountServices {
    private static final String TAG = AccountServices.class.getSimpleName();
    private static final String SECRET_KEY = ":secret*3nCrypt0r_S3cr3t_K3y";
    private static final String SRC = "android";

    private final Context context;

    private final Retrofit retrofit;

    private Call<ApiAccount> callLogin;

    private Call<ApiModel> callChangeInfo;

    private OnLoginResulListener onLoginResulListener;

    private OnAccountChangeResultListener onAccountChangeResultListener;

    private OnRefreshTokenListener onRefreshTokenListener;

    public AccountServices(Context context) {
        this.context = context;
        retrofit = RetrofitBuild.build();
    }

    public void setOnLoginResulListener(OnLoginResulListener onLoginResulListener) {
        this.onLoginResulListener = onLoginResulListener;
    }

    public void setOnAccountChangeResultListener(OnAccountChangeResultListener onAccountChangeResultListener) {
        this.onAccountChangeResultListener = onAccountChangeResultListener;
    }

    public void setOnRefreshTokenListener(OnRefreshTokenListener onRefreshTokenListener) {
        this.onRefreshTokenListener = onRefreshTokenListener;
    }

    /**
     * Check account info;
     *
     * @param token token login;
     */
    public void checkAccountInfo(String token) {

        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onLoginResulListener != null)
                onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "checkAccountInfo: error: turn off internet!");
            return;
        }

        AccountInterface service = retrofit.create(AccountInterface.class);
        callLogin = null;
        callLogin = service.checkAccountInfo(token, SRC);
        LogUtils.d(TAG, "checkAccountInfo request: " + new Gson().toJson(callLogin.request().body()));

        callLogin.enqueue(new Callback<ApiAccount>() {
            @Override
            public void onResponse(Call<ApiAccount> call, Response<ApiAccount> response) {
                if (onLoginResulListener == null) return;

                if (!response.isSuccessful()) {
                    onLoginResulListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "checkAccountInfo: error: server down!");
                    return;
                }

                ApiAccount apiData = response.body();
                LogUtils.e(TAG, "checkAccountInfo: response json: " + new Gson().toJson(apiData));
                if (apiData.getCode() == -96 && StringUtils.isEmpty(apiData.getData().getRefreshToken()) && onRefreshTokenListener != null) {
                    // refresh token;
                    onRefreshTokenListener.onRefreshToken(apiData.getData().getRefreshToken());

                    return;
                }

                if (!apiData.getSuccess()) {
                    onLoginResulListener.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "checkAccountInfo: error: " + apiData.getMessage());

                    return;
                }

                onLoginResulListener.onSuccess(apiData.getData().getUser(), apiData.getData().getToken());
            }

            @Override
            public void onFailure(Call<ApiAccount> call, Throwable t) {
                if (onLoginResulListener != null)
                    onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                LogUtils.e(TAG, "loginWithEmail: error: " + t.getMessage());
            }
        });
    }

    /**
     * User login with email & pass;
     *
     * @param email email
     * @param pass pass
     */
    public void loginWithEmail(String email, String pass) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onLoginResulListener != null)
                onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loginWithEmail: error: turn off internet!");
            return;
        }

        try {
            MCrypt mCrypt = new MCrypt();
            String emailCrypt = MCrypt.bytesToHex(mCrypt.encrypt(email + SECRET_KEY));
            String passCrypt = MCrypt.bytesToHex(mCrypt.encrypt(pass + SECRET_KEY));

            emailCrypt = Base64.encodeToString(emailCrypt.getBytes("UTF-8"), Base64.NO_WRAP);
            passCrypt = Base64.encodeToString(passCrypt.getBytes("UTF-8"), Base64.NO_WRAP);

            AccountInterface service = retrofit.create(AccountInterface.class);
            callLogin = service.login(emailCrypt, passCrypt, SRC);

            callLogin.enqueue(new Callback<ApiAccount>() {
                @Override
                public void onResponse(Call<ApiAccount> call, Response<ApiAccount> response) {
                    if (onLoginResulListener == null) return;

                    if (!response.isSuccessful()) {
                        onLoginResulListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                        LogUtils.e(TAG, "loginWithEmail: error: server down!");
                        return;
                    }

                    ApiAccount apiData = response.body();
                    LogUtils.d(TAG, "loginWithEmail: dataJson: " + new Gson().toJson(apiData));
                    if (!apiData.getSuccess()) {
                        onLoginResulListener.onFailure(apiData.getCode(), apiData.getMessage());
                        LogUtils.e(TAG, "loginWithEmail: error: " + apiData.getMessage());
                        return;
                    }

                    onLoginResulListener.onSuccess(apiData.getData().getUser(), apiData.getData().getToken());
                }

                @Override
                public void onFailure(Call<ApiAccount> call, Throwable t) {
                    if (onLoginResulListener != null)
                        onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                    LogUtils.e(TAG, "loginWithEmail: error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, "loginWithEmail error: email + pass null");
        }
    }

    /**
     * Login with facebook, google;
     *
     * @param accessToken /
     */
    public void loginWithSocial(String provider, String accessToken) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onLoginResulListener != null)
                onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loginWithFacebook: error: turn off internet!");
            return;
        }

        AccountInterface service = retrofit.create(AccountInterface.class);

        callLogin = null;
        switch (provider) {
            case UserSessionManager.PROVIDER_FACE:
                callLogin = service.loginWithFacebook(accessToken, SRC);
                break;

            case UserSessionManager.PROVIDER_GOOGLE:
                callLogin = service.loginWithGoogle(accessToken, SRC);
                break;
        }

        if (callLogin == null) return;
        callLogin.enqueue(new Callback<ApiAccount>() {
            @Override
            public void onResponse(Call<ApiAccount> call, Response<ApiAccount> response) {
                if (onLoginResulListener == null) return;

                if (!response.isSuccessful()) {
                    onLoginResulListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loginWithSocial: error: server down: " + response.code());
                    return;
                }

                ApiAccount apiData = response.body();
                LogUtils.d(TAG, "loginWithSocial: dataJson: " + new Gson().toJson(apiData));
                if (!apiData.getSuccess()) {
                    onLoginResulListener.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "loginWithSocial: error: " + apiData.getMessage());
                    return;
                }


                if (onLoginResulListener != null) {
                    onLoginResulListener.onSuccess(apiData.getData().getUser(), apiData.getData().getToken());
                }
            }

            @Override
            public void onFailure(Call<ApiAccount> call, Throwable t) {
                if (onLoginResulListener != null)
                    onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                LogUtils.e(TAG, "loginWithSocial: error: " + t.getMessage());
            }
        });
    }

    /**
     * User login with email & pass;
     *
     * @param email email
     * @param pass pass
     */
    public void register(String email, String pass, String rePass) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onLoginResulListener != null)
                onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadHomeData: error: turn off internet!");
            return;
        }

        try {
            MCrypt mCrypt = new MCrypt();
            String emailCrypt = MCrypt.bytesToHex(mCrypt.encrypt(email + SECRET_KEY));
            String passCrypt = MCrypt.bytesToHex(mCrypt.encrypt(pass + SECRET_KEY));
            String rePassCrypt = MCrypt.bytesToHex(mCrypt.encrypt(rePass + SECRET_KEY));

            emailCrypt = Base64.encodeToString(emailCrypt.getBytes("UTF-8"), Base64.NO_WRAP);
            passCrypt = Base64.encodeToString(passCrypt.getBytes("UTF-8"), Base64.NO_WRAP);
            rePassCrypt = Base64.encodeToString(rePassCrypt.getBytes("UTF-8"), Base64.NO_WRAP);

            AccountInterface service = retrofit.create(AccountInterface.class);
            callLogin = null;
            callLogin = service.register(emailCrypt, passCrypt, rePassCrypt, SRC);
            callLogin.enqueue(new Callback<ApiAccount>() {
                @Override
                public void onResponse(Call<ApiAccount> call, Response<ApiAccount> response) {
                    if (onLoginResulListener == null) return;

                    LogUtils.d(TAG, "register: dataJson: " + new Gson().toJson(response));

                    if (!response.isSuccessful()) {
                        onLoginResulListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                        LogUtils.e(TAG, "register: error: server down!");
                        return;
                    }

                    ApiAccount apiData = response.body();
                    if (!apiData.getSuccess()) {
                        onLoginResulListener.onFailure(apiData.getCode(), apiData.getMessage());
                        LogUtils.e(TAG, "register: error: " + apiData.getMessage());
                        return;
                    }

                    if (onLoginResulListener != null) {
                        onLoginResulListener.onSuccess(apiData.getData().getUser(), apiData.getData().getToken());
                    }
                }

                @Override
                public void onFailure(Call<ApiAccount> call, Throwable t) {
                    if (onLoginResulListener != null)
                        onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                    LogUtils.e(TAG, "register: error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, "register error: email + pass null");
        }
    }

    /**
     * Change display name;
     *
     * @param newName s
     */
    public void changeDisplayName(String token, String newName) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onLoginResulListener != null)
                onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "changeDisplayName: error: turn off internet!");
            return;
        }

        AccountInterface service = retrofit.create(AccountInterface.class);
        callChangeInfo = null;
        callChangeInfo = service.changeName(newName, token, SRC);
        LogUtils.d(TAG, "changeDisplayName request: " + new Gson().toJson(callChangeInfo.request().body()));
        callChangeInfo.enqueue(new Callback<ApiModel>() {
            @Override
            public void onResponse(Call<ApiModel> call, Response<ApiModel> response) {
                if (onAccountChangeResultListener == null) return;
                LogUtils.d(TAG, "changeDisplayName json: " + new Gson().toJson(response));

                if (!response.isSuccessful()) {
                    onAccountChangeResultListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "changeDisplayName: error: server down!");
                    return;
                }

                if (!response.body().getSuccess()) {
                    onAccountChangeResultListener.onFailure(response.body().getCode(), response.body().getMessage());
                    LogUtils.e(TAG, "changeDisplayName: error msg:" + response.body().getMessage() + "\nCode: " + response.body().getCode());
                    return;
                }

                onAccountChangeResultListener.onSuccess();
            }

            @Override
            public void onFailure(Call<ApiModel> call, Throwable t) {
                if (onAccountChangeResultListener != null)
                    onAccountChangeResultListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));

                LogUtils.e(TAG, "changeDisplayName: onFailure: " + t.getMessage());
            }
        });
    }

    /**
     * Change password;
     *
     * @param token token login
     * @param pass array pass;
     */
    public void changePassword(String token, String[] pass) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onLoginResulListener != null)
                onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "changePassword: error: turn off internet!");
            return;
        }

        try {
            MCrypt mCrypt = new MCrypt();
            String oldPassCrypt = MCrypt.bytesToHex(mCrypt.encrypt(pass[0] + SECRET_KEY));
            String newPassCrypt = MCrypt.bytesToHex(mCrypt.encrypt(pass[1] + SECRET_KEY));
            String reNewPassCrypt = MCrypt.bytesToHex(mCrypt.encrypt(pass[2] + SECRET_KEY));

            oldPassCrypt = Base64.encodeToString(oldPassCrypt.getBytes("UTF-8"), Base64.NO_WRAP);
            newPassCrypt = Base64.encodeToString(newPassCrypt.getBytes("UTF-8"), Base64.NO_WRAP);
            reNewPassCrypt = Base64.encodeToString(reNewPassCrypt.getBytes("UTF-8"), Base64.NO_WRAP);

            AccountInterface service = retrofit.create(AccountInterface.class);
            callChangeInfo = null;
            callChangeInfo = service.changePass(oldPassCrypt, newPassCrypt, reNewPassCrypt, token, SRC);
            LogUtils.d(TAG, "changePassword request: " + new Gson().toJson(callChangeInfo.request().body()));
            callChangeInfo.enqueue(new Callback<ApiModel>() {
                @Override
                public void onResponse(Call<ApiModel> call, Response<ApiModel> response) {
                    if (onAccountChangeResultListener == null) return;
                    LogUtils.d(TAG, "changePassword json: " + new Gson().toJson(response));

                    if (!response.isSuccessful()) {
                        onAccountChangeResultListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                        LogUtils.e(TAG, "changePassword: error: server down!");
                        return;
                    }

                    if (!response.body().getSuccess()) {
                        onAccountChangeResultListener.onFailure(response.body().getCode(), response.body().getMessage());
                        LogUtils.e(TAG, "changePassword: error msg:" + response.body().getMessage() + "\nCode: " + response.body().getCode());
                        return;
                    }

                    onAccountChangeResultListener.onSuccess();
                }

                @Override
                public void onFailure(Call<ApiModel> call, Throwable t) {
                    if (onAccountChangeResultListener != null)
                        onAccountChangeResultListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));

                    LogUtils.e(TAG, "changePassword: onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, "changePassword error: email + pass null");
        }
    }

    /**
     * Cancel loading;
     */
    public void cancel() {
        if (callLogin != null && callLogin.isExecuted())
            callLogin.cancel();

        if (callChangeInfo != null && callChangeInfo.isExecuted())
            callChangeInfo.cancel();
    }

    /**
     * interface service;
     */
    public interface AccountInterface {
        @FormUrlEncoded
        @POST("login")
        Call<ApiAccount> login(
                @Field("user_email") String email,
                @Field("password") String pass,
                @Field("src") String src);

        @FormUrlEncoded
        @POST("login/facebook")
        Call<ApiAccount> loginWithFacebook(
                @Field("access_token") String accessToken,
                @Field("src") String src);

        @FormUrlEncoded
        @POST("login/google")
        Call<ApiAccount> loginWithGoogle(
                @Field("access_token") String accessToken,
                @Field("src") String src);

        @FormUrlEncoded
        @POST("user/register")
        Call<ApiAccount> register(
                @Field("user_email") String email,
                @Field("password") String pass,
                @Field("re_password") String rePass,
                @Field("src") String src);

        @FormUrlEncoded
        @POST("user/change_password")
        Call<ApiModel> changePass(
                @Field("old_password") String pass,
                @Field("password") String newPass,
                @Field("re_password") String reNewPass,
                @Field("token") String token,
                @Field("src") String src);

        @FormUrlEncoded
        @POST("user/change_name")
        Call<ApiModel> changeName(
                @Field("name") String newName,
                @Field("token") String token,
                @Field("src") String src);

        @GET("user/account_info")
        Call<ApiAccount> checkAccountInfo (
                @Query("token") String token,
                @Query("src") String src);
    }

    public interface OnLoginResulListener extends ApiResultCallback {

        void onSuccess(User user, String token);
    }

    public interface OnRefreshTokenListener {

        void onRefreshToken(String token);
    }

    public interface OnAccountChangeResultListener extends ApiResultCallback{
        void onSuccess();
    }
}
