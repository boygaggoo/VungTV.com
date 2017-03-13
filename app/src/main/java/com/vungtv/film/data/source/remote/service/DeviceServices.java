package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/13/2017.
 * Email: vancuong2941989@gmail.com
 */

public class DeviceServices extends BaseApiServices {

    private Call<ApiModel> callDevice;

    private ResultCallback resultCallback;

    public DeviceServices(Context context) {
        super(context);
    }

    public void setResultCallback(ResultCallback resultCallback) {
        this.resultCallback = resultCallback;
    }

    public void registerDevice(String devId, String devFcmKey) {
        if (isInternetTurnOff(null)) {
            return;
        }
        ServicesInterface services = retrofit.create(ServicesInterface.class);
        callDevice = services.registerDevice(devId, devFcmKey, "android");
        callDevice.enqueue(new Callback<ApiModel>() {
            @Override
            public void onResponse(Call<ApiModel> call, Response<ApiModel> response) {
                if (resultCallback == null) return;

                if (!response.isSuccessful()) {
                    resultCallback.onFailure(response.code(),
                            ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                ApiModel apiModel = response.body();
                if (!apiModel.getSuccess()) {
                    resultCallback.onFailure(apiModel.getCode(), apiModel.getMessage());
                    return;
                }

                resultCallback.onRegisterDeviceSuccess();
            }

            @Override
            public void onFailure(Call<ApiModel> call, Throwable t) {
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
        if (callDevice != null && callDevice.isExecuted()) {
            callDevice.cancel();
        }
    }

    private interface ServicesInterface {

        @FormUrlEncoded
        @POST("home/device_register")
        Call<ApiModel> registerDevice(
                @Field("dev_id") String devId,
                @Field("dev_fcm_key") String fcmKey,
                @Field("src") String src);
    }

    public interface ResultCallback extends ApiResultCallback {

        void onRegisterDeviceSuccess();
    }
}
