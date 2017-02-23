package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.RetrofitBuild;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiHome;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class HomeServices {
    private static final String TAG = HomeServices.class.getSimpleName();
    private static final int LIMIT = 10;
    private Context context;
    private Call<ApiHome> callHomeData;
    private HomeResultCallback homeResultCallback;

    public HomeServices(Context context) {
        this.context = context;
    }

    public void setHomeResultCallback(HomeResultCallback homeResultCallback) {
        this.homeResultCallback = homeResultCallback;
    }

    /**
     * Load data from server;
     */
    public void loadHomeData() {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            homeResultCallback.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadHomeData: error: turn off internet!");
            return;
        }

        final Retrofit retrofit = RetrofitBuild.build();
        HomeInterface service = retrofit.create(HomeInterface.class);
        callHomeData = service.load(LIMIT);
        LogUtils.d(TAG, "loadHomeData: " + callHomeData.request().url());
        callHomeData.enqueue(new Callback<ApiHome>() {
            @Override
            public void onResponse(Call<ApiHome> call, Response<ApiHome> response) {

                if (!response.isSuccessful()) {
                    homeResultCallback.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadHomeData: error: server down!");
                    return;
                }

                ApiHome apiHome = response.body();
                if (!apiHome.getSuccess()) {
                    homeResultCallback.onFailure(apiHome.getCode(), apiHome.getMessage());
                    LogUtils.e(TAG, "loadHomeData: error: No data!");
                    return;
                }

                homeResultCallback.onSuccess(apiHome.getDataHome());
            }

            @Override
            public void onFailure(Call<ApiHome> call, Throwable t) {
                homeResultCallback.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                LogUtils.e(TAG, "loadHomeData: error: " + t.getMessage());
            }
        });
    }
    
    /**
     * Cancel loading;
     */
    public void cancel() {
        if (callHomeData != null && callHomeData.isExecuted())
            callHomeData.cancel();
    }
    
    interface HomeInterface {
        @GET("page/home?src=android")
        Call<ApiHome> load(@Query("limit") int limit);
    }

    public interface HomeResultCallback extends ApiResultCallback {
        void onSuccess(ApiHome.DataHome dataHomeHome);
    }
}
