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
 *
 * Created by pc on 3/10/2017.
 */

public class RequestServices extends BaseApiServices {

    private Call<ApiModel> call;

    private ResultCallback resultCallback;

    public RequestServices(Context context) {
        super(context);
    }

    public void setResultCallback(ResultCallback resultCallback) {
        this.resultCallback = resultCallback;
    }

    /**
     * Gửi yêu cầu;
     *
     * @param content noi dung yeu cau
     * @param token token đăng nhập
     */
    public void sendRequest(String content, String token) {
        if (isInternetTurnOff(resultCallback)) {
            return;
        }

        ServicesInterface services = retrofit.create(ServicesInterface.class);
        call = services.sendRequest(content, token, "android");
        call.enqueue(new Callback<ApiModel>() {
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

                resultCallback.onRequestResultSuccess(apiModel.getMessage());
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
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }

    private interface ServicesInterface {
        @FormUrlEncoded
        @POST("home/request")
        Call<ApiModel> sendRequest(@Field("req_content") String content,
                                      @Field("token") String token,
                                      @Field("src") String src);
    }

    public interface ResultCallback extends ApiResultCallback {

        void onRequestResultSuccess(String msg);
    }
}
