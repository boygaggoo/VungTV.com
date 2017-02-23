package com.vungtv.film.data.source.remote.service;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.RetrofitBuild;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiAccount;
import com.vungtv.film.data.source.remote.model.ApiGetRecharge;
import com.vungtv.film.data.source.remote.model.ApiPackageVip;
import com.vungtv.film.model.CardType;
import com.vungtv.film.model.CardValue;
import com.vungtv.film.model.PackageVip;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.MCrypt;
import com.vungtv.film.util.NetworkUtils;

import java.util.ArrayList;

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
 * Created by pc on 2/21/2017.
 */

public class BillingServices {
    private static final String TAG = BillingServices.class.getSimpleName();
    private static final String SECRET_KEY = ":secret*3nCrypt0r_S3cr3t_K3y";
    private static final String SRC = "android";

    private final Context context;

    private final Retrofit retrofit;

    private Call<ApiPackageVip> callPackageVip;

    private Call<ApiAccount> callBuyVip;

    private Call<ApiGetRecharge> callRechargeInfo;

    private Call<ApiAccount> callRecharge;

    private OnBillingListener onBillingListener;

    public BillingServices(Context context) {
        this.context = context;
        retrofit = RetrofitBuild.build();
    }


    public void setOnBillingListener(OnBillingListener onBillingListener) {
        this.onBillingListener = onBillingListener;
    }

    /**
     * Mua goi VIP
     *
     * @param token token login;
     * @param packagePos ps
     */
    public void buyVip(String token, int packagePos) {

        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onBillingListener != null)
                onBillingListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));

            LogUtils.e(TAG, "loadPackagesVip: error: turn off internet!");
            return;
        }

        BillingInterface service = retrofit.create(BillingInterface.class);
        callBuyVip = service.buyVip(packagePos, token, SRC);

        callBuyVip.enqueue(new Callback<ApiAccount>() {
            @Override
            public void onResponse(Call<ApiAccount> call, Response<ApiAccount> response) {

                if (onBillingListener == null) return;

                if (!response.isSuccessful()) {
                    onBillingListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));

                    LogUtils.e(TAG, "buyVip: error: server down! status code = " + response.code());
                    return;
                }

                ApiAccount apiData = response.body();

                if (!apiData.getSuccess()) {
                    onBillingListener.onFailure(apiData.getCode(), apiData.getMessage());

                    LogUtils.e(TAG, "buyVip: error: " + apiData.getMessage());
                    return;
                }

                onBillingListener.onBuyVipSuccess(
                        apiData.getMessage(),
                        apiData.getData().getUser(),
                        apiData.getData().getToken()
                );
            }

            @Override
            public void onFailure(Call<ApiAccount> call, Throwable t) {
                if (onBillingListener != null)
                    onBillingListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));

                LogUtils.e(TAG, "buyVip: onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    /**
     * lay thong tin cac gois vip;
     *
     * @param token token login;
     */
    public void loadPackagesVip(String token) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onBillingListener != null)
                onBillingListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadPackagesVip: error: turn off internet!");
            return;
        }

        BillingInterface service = retrofit.create(BillingInterface.class);

        callPackageVip = service.packagesVip(token, SRC);
        LogUtils.d(TAG, "loadPackagesVip request: " + new Gson().toJson(callPackageVip.request().body()));

        callPackageVip.enqueue(new Callback<ApiPackageVip>() {
            @Override
            public void onResponse(Call<ApiPackageVip> call, Response<ApiPackageVip> response) {
                if (onBillingListener == null) return;

                if (!response.isSuccessful()) {
                    onBillingListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadPackagesVip: error: server down!");
                    return;
                }

                ApiPackageVip apiData = response.body();
                LogUtils.d(TAG, "checkAccountInfo: dataJson: " + new Gson().toJson(apiData));
                if (!apiData.getSuccess()) {
                    onBillingListener.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "loadPackagesVip: error: " + apiData.getMessage());
                    return;
                }

                onBillingListener.onLoadPackagesVipSuccess(
                        apiData.getData().getUserBalance(), apiData.getData().getPackages());
            }

            @Override
            public void onFailure(Call<ApiPackageVip> call, Throwable t) {
                if (onBillingListener != null)
                    onBillingListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                LogUtils.e(TAG, "loadPackagesVip: error: " + t.getMessage());
            }
        });
    }

    /**
     * Nạp vừng = thẻ cào;
     *
     * @param token access tokent
     * @param cardInfo thông tin thẻ nạp;
     */
    public void recharge(String token, String[] cardInfo) {

        if (!NetworkUtils.isInternetTurnOn(context)) {

            if (onBillingListener != null)
                onBillingListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));

            LogUtils.e(TAG, "recharge: error: turn off internet!");
            return;
        }

        try {

            MCrypt mCrypt = new MCrypt();
            String cardTypeCrypt = MCrypt.bytesToHex(mCrypt.encrypt(cardInfo[0] + SECRET_KEY));
            String cardNumberCrypt = MCrypt.bytesToHex(mCrypt.encrypt(cardInfo[1] + SECRET_KEY));
            String cardSerialCrypt = MCrypt.bytesToHex(mCrypt.encrypt(cardInfo[2] + SECRET_KEY));

            cardTypeCrypt = Base64.encodeToString(cardTypeCrypt.getBytes("UTF-8"), Base64.NO_WRAP);
            cardNumberCrypt = Base64.encodeToString(cardNumberCrypt.getBytes("UTF-8"), Base64.NO_WRAP);
            cardSerialCrypt = Base64.encodeToString(cardSerialCrypt.getBytes("UTF-8"), Base64.NO_WRAP);

            BillingInterface service = retrofit.create(BillingInterface.class);
            callRecharge = service.recharge(cardTypeCrypt, cardNumberCrypt, cardSerialCrypt, token, SRC, true);
            LogUtils.d(TAG, "recharge request: " + new Gson().toJson(callRecharge.request().body()));

            callRecharge.enqueue(new Callback<ApiAccount>() {
                @Override
                public void onResponse(Call<ApiAccount> call, Response<ApiAccount> response) {
                    if (onBillingListener == null) return;

                    if (!response.isSuccessful()) {

                        onBillingListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));

                        LogUtils.e(TAG, "recharge: error: server down!");
                        return;
                    }

                    ApiAccount apiData = response.body();
                    LogUtils.d(TAG, "recharge: dataJson: " + new Gson().toJson(apiData));

                    if (!apiData.getSuccess()) {

                        onBillingListener.onFailure(apiData.getCode(), apiData.getMessage());

                        LogUtils.e(TAG, "recharge: error: " + apiData.getMessage());
                        return;
                    }

                    onBillingListener.onRechargeSuccess(
                            apiData.getMessage(),
                            apiData.getData().getUser(),
                            apiData.getData().getToken()
                    );
                }

                @Override
                public void onFailure(Call<ApiAccount> call, Throwable t) {
                    if (onBillingListener != null)
                        onBillingListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                    LogUtils.e(TAG, "recharge: error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, "recharge error: " + e.getMessage());
        }
    }

    /**
     * lay thong tin cac gois vip;
     *
     * @param token token login;
     */
    public void loadRechargeInfo(String token) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onBillingListener != null)
                onBillingListener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadPackagesVip: error: turn off internet!");
            return;
        }

        BillingInterface service = retrofit.create(BillingInterface.class);

        callRechargeInfo = service.getRecharge(token, SRC);
        LogUtils.d(TAG, "loadPackagesVip request: " + new Gson().toJson(callRechargeInfo.request().body()));

        callRechargeInfo.enqueue(new Callback<ApiGetRecharge>() {
            @Override
            public void onResponse(Call<ApiGetRecharge> call, Response<ApiGetRecharge> response) {
                if (onBillingListener == null) return;

                if (!response.isSuccessful()) {
                    onBillingListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadPackagesVip: error: server down!");
                    return;
                }

                ApiGetRecharge apiData = response.body();
                LogUtils.d(TAG, "checkAccountInfo: dataJson: " + new Gson().toJson(apiData));
                if (!apiData.getSuccess()) {
                    onBillingListener.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "loadPackagesVip: error: " + apiData.getMessage());
                    return;
                }

                onBillingListener.onLoadRechargeInfo(
                        apiData.getData().getCardTypes(), apiData.getData().getCardValues());
            }

            @Override
            public void onFailure(Call<ApiGetRecharge> call, Throwable t) {
                if (onBillingListener != null)
                    onBillingListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                LogUtils.e(TAG, "loadPackagesVip: error: " + t.getMessage());
            }
        });
    }

    public void cancel() {
        if (callPackageVip != null && callPackageVip.isExecuted())
            callPackageVip.cancel();

        if (callBuyVip != null && callBuyVip.isExecuted())
            callBuyVip.cancel();

        if (callRechargeInfo != null && callRechargeInfo.isExecuted())
            callRechargeInfo.cancel();

        if (callRecharge != null && callRecharge.isExecuted())
            callRecharge.cancel();
    }

    interface BillingInterface {

        @GET("user/recharge")
        Call<ApiPackageVip> packagesVip (
                @Query("token") String token,
                @Query("src") String src);

        @GET("user/topup_card")
        Call<ApiGetRecharge> getRecharge (
                @Query("token") String token,
                @Query("src") String src);

        @FormUrlEncoded
        @POST("user/buy")
        Call<ApiAccount> buyVip(
                @Field("package") int packageVipPos,
                @Field("token") String token,
                @Field("src") String src);

        @FormUrlEncoded
        @POST("user/topup_card")
        Call<ApiAccount> recharge(
                @Field("card_type") String cardType,
                @Field("card_number") String cardNumber,
                @Field("card_serial") String cardSerial,
                @Field("token") String token,
                @Field("src") String src,
                @Field("is_fake") boolean isFake);
    }

    public interface OnBillingListener extends ApiResultCallback{

        void onLoadPackagesVipSuccess(int vungPoint, ArrayList<PackageVip> list);

        void onBuyVipSuccess(String msg, User user, String token);

        void onLoadRechargeInfo(ArrayList<CardType> cardTypes, ArrayList<CardValue> cardValues);

        void onRechargeSuccess(String msg, User user, String token);
    }
}
