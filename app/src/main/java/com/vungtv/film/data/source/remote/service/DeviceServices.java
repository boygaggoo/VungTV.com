package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.model.ApiModel;

import java.io.IOException;

import retrofit2.Call;
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

    public DeviceServices(Context context) {
        super(context);
    }

    public void registerDevice(String devId, String devFcmKey) {
        if (isInternetTurnOff(null)) {
            return;
        }
        ServicesInterface services = retrofit.create(ServicesInterface.class);
        callDevice = services.registerDevice(devId, devFcmKey, "android");
        try {
            callDevice.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
