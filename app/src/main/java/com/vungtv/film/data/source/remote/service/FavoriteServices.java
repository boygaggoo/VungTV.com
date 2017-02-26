package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.RetrofitBuild;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiFilterMovies;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 2/24/2017.
 * Email: vancuong2941989@gmail.com
 */

public class FavoriteServices {
    private static final String TAG = BillingServices.class.getSimpleName();
    private static final String SECRET_KEY = ":secret*3nCrypt0r_S3cr3t_K3y";
    private static final String SRC = "android";

    private final Context context;

    private final Retrofit retrofit;

    private String accessToken;

    private int limit = 24;

    private int offset = 0;

    private Call<ApiFilterMovies> call;

    private OnFavoriteServicesListener listener;

    public FavoriteServices(Context context) {
        this.context = context;

        retrofit = RetrofitBuild.build();
    }

    public void setOnFavoriteServicesListener(OnFavoriteServicesListener onFavoriteServicesListener) {
        this.listener = onFavoriteServicesListener;
    }

    public void loadFavorite() {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            listener.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadFilterMovies: error: turn off internet!");
            return;
        }

        FavoriteInterface service = retrofit.create(FavoriteInterface.class);
        call = service.load(limit, offset, accessToken, SRC);

        call.enqueue(new Callback<ApiFilterMovies>() {
            @Override
            public void onResponse(Call<ApiFilterMovies> call, Response<ApiFilterMovies> response) {

                if (!response.isSuccessful()) {
                    listener.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadFavorite: error: server down!");
                    return;
                }
                ApiFilterMovies apiData = response.body();
                if (!apiData.getSuccess()) {
                    listener.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "loadFavorite: error: No data!");
                    return;
                }

                listener.onFavoriteLoadSuccess(apiData.getDataPage());
            }

            @Override
            public void onFailure(Call<ApiFilterMovies> call, Throwable t) {

                listener.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
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
    private interface FavoriteInterface {

        @GET("user/phim_favorite")
        Call<ApiFilterMovies> load (
                @Query("limit") int limit,
                @Query("offset") int offset,
                @Query("token") String token,
                @Query("src") String src);
    }

    /* Callback */
    public interface OnFavoriteServicesListener extends ApiResultCallback {

        void onFavoriteLoadSuccess(ApiFilterMovies.DataPage dataPage);
    }
}
