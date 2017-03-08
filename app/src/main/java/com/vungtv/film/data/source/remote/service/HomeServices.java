package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.RetrofitBuild;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiHome;
import com.vungtv.film.data.source.remote.model.ApiHomeMenu;
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

    private static final int LIMIT = 20;

    private final Context context;

    private final Retrofit retrofit;

    private Call<ApiHome> callHomeData;

    private Call<ApiHomeMenu> callMenu;

    private HomeResultCallback homeResultCallback;

    private HomeMenuResultCallback homeMenuResultCallback;

    public HomeServices(Context context) {
        this.context = context;
        retrofit = RetrofitBuild.build();
    }

    public void setHomeResultCallback(HomeResultCallback homeResultCallback) {
        this.homeResultCallback = homeResultCallback;
    }

    public void setHomeMenuResultCallback(HomeMenuResultCallback homeMenuResultCallback) {
        this.homeMenuResultCallback = homeMenuResultCallback;
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
     * Load data from server;
     */
    public void loadHomeMenu() {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            LogUtils.e(TAG, "loadHomeData: error: turn off internet!");
            return;
        }

        HomeInterface service = retrofit.create(HomeInterface.class);
        callMenu = service.loadMenus();
        callMenu.enqueue(new Callback<ApiHomeMenu>() {
            @Override
            public void onResponse(Call<ApiHomeMenu> call, Response<ApiHomeMenu> response) {

                if (response.isSuccessful() && response.body().getSuccess()) {

                    homeMenuResultCallback.onHomeMenuResultSuccess(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiHomeMenu> call, Throwable t) {
                t.printStackTrace();
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

    public void cancelLoadMenu() {
        if (callMenu != null && callMenu.isExecuted())
            callMenu.cancel();
    }
    
    interface HomeInterface {
        @GET("page/home?src=android")
        Call<ApiHome> load(@Query("limit") int limit);

        @GET("home/menu?src=android")
        Call<ApiHomeMenu> loadMenus();
    }

    public interface HomeResultCallback extends ApiResultCallback {
        void onSuccess(ApiHome.DataHome dataHomeHome);
    }

    public interface HomeMenuResultCallback {
        void onHomeMenuResultSuccess(ApiHomeMenu.Data data);
    }
}
