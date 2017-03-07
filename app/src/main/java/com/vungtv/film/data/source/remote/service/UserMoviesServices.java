package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiUserMovies;
import com.vungtv.film.feature.usermovies.UserMoviesActivity;
import com.vungtv.film.util.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 2/24/2017.
 * Email: vancuong2941989@gmail.com
 */

public class UserMoviesServices extends BaseApiServices {
    private static final String TAG = BillingServices.class.getSimpleName();

    private String accessToken;

    private int limit = 24;

    private int offset = 0;

    private Call<ApiUserMovies> call;

    private ResultListener resultListener;

    public UserMoviesServices(Context context) {
        super(context);
    }

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void loadMovies(int pageType) {
        if (isInternetTurnOff(resultListener)) {
            return;
        }

        InterfaceServices service = retrofit.create(InterfaceServices.class);
        if (pageType == UserMoviesActivity.PAGE_FAVORITE) {
            call = service.favoriteMovies(limit, offset, accessToken);
        } else {
            call = service.followMovies(limit, offset, accessToken);
        }

        call.enqueue(new Callback<ApiUserMovies>() {
            @Override
            public void onResponse(Call<ApiUserMovies> call, Response<ApiUserMovies> response) {

                if (!response.isSuccessful()) {
                    resultListener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadMovies: error: server down!");
                    return;
                }
                ApiUserMovies apiData = response.body();
                if (!apiData.getSuccess()) {
                    resultListener.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "loadMovies: error: No data!");
                    return;
                }

                resultListener.onUserMoviesResultSuccess(apiData.getData());
            }

            @Override
            public void onFailure(Call<ApiUserMovies> call, Throwable t) {

                resultListener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                t.printStackTrace();
            }
        });
    }

    public void cancel() {
        if (call != null &&  call.isExecuted())
            call.cancel();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /* interface */
    private interface InterfaceServices {

        @GET("page/phim_favorite?src=android")
        Call<ApiUserMovies> favoriteMovies (
                @Query("limit") int limit,
                @Query("offset") int offset,
                @Query("token") String token);

        @GET("page/phim_follow?src=android")
        Call<ApiUserMovies> followMovies (
                @Query("limit") int limit,
                @Query("offset") int offset,
                @Query("token") String token);
    }

    /* Callback */
    public interface ResultListener extends ApiResultCallback {

        void onUserMoviesResultSuccess(ApiUserMovies.Data data);
    }
}
