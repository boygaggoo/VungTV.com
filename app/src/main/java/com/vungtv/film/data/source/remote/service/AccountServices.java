package com.vungtv.film.data.source.remote.service;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiAccount;
import com.vungtv.film.data.source.remote.model.ApiModel;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.MCrypt;
import com.vungtv.film.util.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

public class AccountServices extends BaseApiServices{
    private static final String TAG = AccountServices.class.getSimpleName();
    private static final String SECRET_KEY = ":secret*3nCrypt0r_S3cr3t_K3y";
    private static final String SRC = "android";

    private final ServicesInterface service;

    private Call<ApiAccount> call;

    private Call<ApiModel> callLogout;

    private OnLoginResulListener onLoginResulListener;

    private OnAccountChangeResultListener onAccountChangeResultListener;

    private OnRefreshTokenListener onRefreshTokenListener;

    private OnLogoutListener onLogoutListener;

    public AccountServices(Context context) {
        super(context);
        service = retrofit.create(ServicesInterface.class);
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

    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
    }

    /**
     * Kiểm tra trạng thái đăng nhập;
     *
     * @param token token login;
     */
    public void checkAccountInfo(String token) {
        if (isInternetTurnOff(onLoginResulListener)) {
            return;
        }

        call = null;
        call = service.checkAccountInfo(token, SRC);

        call.enqueue(new Callback<ApiAccount>() {
            @Override
            public void onResponse(Call<ApiAccount> call, Response<ApiAccount> response) {
                if (onLoginResulListener == null) return;

                if (!response.isSuccessful()) {
                    onLoginResulListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                ApiAccount apiData = response.body();
                if (apiData.getCode() == -96 && StringUtils.isEmpty(apiData.getData().getRefreshToken()) && onRefreshTokenListener != null) {
                    // refresh token;
                    onRefreshTokenListener.onRefreshToken(apiData.getData().getRefreshToken());
                    return;
                }

                if (!apiData.getSuccess()) {
                    onLoginResulListener.onFailure(apiData.getCode(), apiData.getMessage());
                    return;
                }

                onLoginResulListener.onSuccess(apiData.getData().getUser(), apiData.getData().getToken());
            }

            @Override
            public void onFailure(Call<ApiAccount> call, Throwable t) {
                if (onLoginResulListener != null)
                    onLoginResulListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                t.printStackTrace();
            }
        });
    }

    /**
     * Đăng nhập = email & pass;
     *
     * @param email email
     * @param pass pass
     */
    public void loginWithEmail(String email, String pass) {
        if (isInternetTurnOff(onLoginResulListener)) {
            return;
        }

        try {
            MCrypt mCrypt = new MCrypt();
            String emailCrypt = MCrypt.bytesToHex(mCrypt.encrypt(email + SECRET_KEY));
            String passCrypt = MCrypt.bytesToHex(mCrypt.encrypt(pass + SECRET_KEY));

            emailCrypt = Base64.encodeToString(emailCrypt.getBytes("UTF-8"), Base64.NO_WRAP);
            passCrypt = Base64.encodeToString(passCrypt.getBytes("UTF-8"), Base64.NO_WRAP);

            call = service.login(emailCrypt, passCrypt, SRC);

            call.enqueue(new Callback<ApiAccount>() {
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
     * Đăng nhập = facebook, google;
     *
     * @param accessToken token nhận từ facebook / google
     */
    public void loginWithSocial(String provider, String accessToken) {
        if (isInternetTurnOff(onLoginResulListener)) {
            return;
        }

        call = null;
        switch (provider) {
            case UserSessionManager.PROVIDER_FACE:
                call = service.loginWithFacebook(accessToken, SRC);
                break;

            case UserSessionManager.PROVIDER_GOOGLE:
                call = service.loginWithGoogle(accessToken, SRC);
                break;
        }

        if (call == null) return;
        call.enqueue(new Callback<ApiAccount>() {
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
     * Đăng ký tài khoản
     *
     * @param email email
     * @param pass pass
     * @param rePass repass
     */
    public void register(String email, String pass, String rePass) {
        if (isInternetTurnOff(onLoginResulListener)) {
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

            call = null;
            call = service.register(emailCrypt, passCrypt, rePassCrypt, SRC);
            call.enqueue(new Callback<ApiAccount>() {
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
     * Đổi tên hiển thị;
     *
     * @param newName tên mới
     */
    public void changeDisplayName(String token, String newName) {
        if (isInternetTurnOff(onLoginResulListener)) {
            return;
        }

        call = null;
        call = service.changeName(newName, token, SRC);
        call.enqueue(new Callback<ApiAccount>() {
            @Override
            public void onResponse(Call<ApiAccount> call, Response<ApiAccount> response) {
                if (onAccountChangeResultListener == null) return;

                if (!response.isSuccessful()) {
                    onAccountChangeResultListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                if (!response.body().getSuccess()) {
                    onAccountChangeResultListener.onFailure(response.body().getCode(), response.body().getMessage());
                    return;
                }

                onAccountChangeResultListener.onAccountChangeSuccess(response.body().getData().getToken());
            }

            @Override
            public void onFailure(Call<ApiAccount> call, Throwable t) {
                if (onAccountChangeResultListener != null) {
                    onAccountChangeResultListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                }
                t.printStackTrace();
            }
        });
    }

    /**
     * Đổi pass
     *
     * @param token token login
     * @param pass array [ oldPass, newPass, reNewPass ];
     */
    public void changePassword(String token, String[] pass) {
        if (isInternetTurnOff(onLoginResulListener)) {
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

            call = null;
            call = service.changePass(oldPassCrypt, newPassCrypt, reNewPassCrypt, token, SRC);
            call.enqueue(new Callback<ApiAccount>() {
                @Override
                public void onResponse(Call<ApiAccount> call, Response<ApiAccount> response) {
                    if (onAccountChangeResultListener == null) return;

                    if (!response.isSuccessful()) {
                        onAccountChangeResultListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                        return;
                    }

                    if (!response.body().getSuccess()) {
                        onAccountChangeResultListener.onFailure(response.body().getCode(), response.body().getMessage());
                        return;
                    }

                    onAccountChangeResultListener.onAccountChangeSuccess(response.body().getData().getToken());
                }

                @Override
                public void onFailure(Call<ApiAccount> call, Throwable t) {
                    if (onAccountChangeResultListener != null) {
                        onAccountChangeResultListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Đăng xuất tài khoản;
     *
     * @param token token đăng nhập;
     */
    public void logout(String token) {
        if (isInternetTurnOff(onLogoutListener)) {
            return;
        }

        callLogout = service.logout(token, SRC);
        callLogout.enqueue(new Callback<ApiModel>() {
            @Override
            public void onResponse(Call<ApiModel> call, Response<ApiModel> response) {
                if (onLogoutListener == null) return;

                if (response.isSuccessful() && response.body().getSuccess()) {
                    onLogoutListener.onLogoutSuccess();
                } else {
                    int code = response.isSuccessful() ? response.body().getCode() : response.code();
                    String mes = "";
                    if (response.isSuccessful()) {
                        response.body().getMessage();
                    } else {
                        mes = ApiError.toString(context, ApiError.NO_INTERNET);
                    }
                    onLogoutListener.onFailure(code, mes);
                }
            }

            @Override
            public void onFailure(Call<ApiModel> call, Throwable t) {
                if (onLogoutListener != null)
                    onLogoutListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                t.printStackTrace();
            }
        });
    }

    /**
     * Cancel loading;
     */
    public void cancel() {
        if (call != null && call.isExecuted())
            call.cancel();

        if (callLogout != null && callLogout.isExecuted())
            callLogout.cancel();
    }

    /**
     * interface service;
     */
    public interface ServicesInterface {
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
        Call<ApiAccount> changePass(
                @Field("old_password") String pass,
                @Field("password") String newPass,
                @Field("re_password") String reNewPass,
                @Field("token") String token,
                @Field("src") String src);

        @FormUrlEncoded
        @POST("user/change_name")
        Call<ApiAccount> changeName(
                @Field("name") String newName,
                @Field("token") String token,
                @Field("src") String src);

        @GET("user/account_info")
        Call<ApiAccount> checkAccountInfo (
                @Query("token") String token,
                @Query("src") String src);
        
        @FormUrlEncoded
        @POST("user/logout")
        Call<ApiModel> logout(
                @Field("token") String accessToken,
                @Field("src") String src);
    }

    public interface OnLoginResulListener extends ApiResultCallback {

        void onSuccess(User user, String token);
    }

    public interface OnRefreshTokenListener {

        void onRefreshToken(String token);
    }

    public interface OnAccountChangeResultListener extends ApiResultCallback{
        void onAccountChangeSuccess(String token);
    }

    public interface OnLogoutListener extends ApiResultCallback {
        void onLogoutSuccess();
    }
}
