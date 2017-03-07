package com.vungtv.film.data.source.remote;

import android.content.Context;

import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.NetworkUtils;

import retrofit2.Retrofit;

import static com.facebook.GraphRequest.TAG;

/**
 *
 * Created by pc on 3/6/2017.
 */

public abstract class BaseApiServices {
    protected static final String SRC = "android";

    protected final Context context;

    protected final Retrofit retrofit;

    public BaseApiServices(Context context) {
        this.context = context;
        retrofit = RetrofitBuild.build();
    }

    public abstract void cancel();

    protected boolean isInternetTurnOff(ApiResultCallback apiResultCallback) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (apiResultCallback != null) {
                apiResultCallback.onFailure(0,
                        ApiError.toString(context, ApiError.OFF_INTERNET));
            }
            LogUtils.e(TAG, "checkInternet: internet turn off!");
            return true;
        }

        return false;
    }
}
