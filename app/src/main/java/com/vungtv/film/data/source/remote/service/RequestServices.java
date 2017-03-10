package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiModel;

import retrofit2.Call;

/**
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

    public void sendRequest(String title, String content) {

    }

    @Override
    public void cancel() {
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }

    public interface ResultCallback extends ApiResultCallback {

        void onRequestResultSuccess(String msg);
    }
}
