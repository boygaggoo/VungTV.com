package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiConfig;
import com.vungtv.film.model.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/13/2017.
 * Email: vancuong2941989@gmail.com
 */

public class ConfigServices extends BaseApiServices {

    private Call<ApiConfig> callConfig;

    private ServicesInterface servicesInterface;

    private ResultCallback resultCallback;

    public ConfigServices(Context context) {
        super(context);
        servicesInterface = retrofit.create(ServicesInterface.class);
    }

    public void setResultCallback(ResultCallback resultCallback) {
        this.resultCallback = resultCallback;
    }

    public void loadConfig () {
        if (isInternetTurnOff(resultCallback)) {
            return;
        }

        callConfig = servicesInterface.getConfig();
        callConfig.enqueue(new Callback<ApiConfig>() {
            @Override
            public void onResponse(Call<ApiConfig> call, Response<ApiConfig> response) {
                if (resultCallback == null) return;

                if (!response.isSuccessful()) {
                    resultCallback.onFailure(response.code(),
                            ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                ApiConfig apiModel = response.body();
                if (!apiModel.getSuccess()) {
                    resultCallback.onFailure(apiModel.getCode(), apiModel.getMessage());
                    return;
                }

                resultCallback.onGetConfigSuccess(apiModel.getData().getConfig());
            }

            @Override
            public void onFailure(Call<ApiConfig> call, Throwable t) {
                if (resultCallback != null) {
                    resultCallback.onFailure(0,
                            ApiError.toString(context, ApiError.NO_INTERNET));
                }
                t.printStackTrace();
            }
        });
    }

    @Override
    public void cancel() {
        if (callConfig != null && callConfig.isExecuted()) {
            callConfig.cancel();
        }
    }


    private interface ServicesInterface {

        @GET("home/config?src=android")
        Call<ApiConfig> getConfig();
    }

    public interface ResultCallback extends ApiResultCallback {

        void onGetConfigSuccess(Config config);
    }
}
