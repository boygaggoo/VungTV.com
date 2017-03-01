package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.RetrofitBuild;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiMovieDetail;
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
 * Created by Mr Cuong on 2/25/2017.
 * Email: vancuong2941989@gmail.com
 */

public class MovieDetailServices {
    private static final String TAG = MovieDetailServices.class.getSimpleName();

    private final Context context;

    private final Retrofit retrofit;

    private Call<ApiMovieDetail> callMovie;

    private MovieDetailResultCallback movieDetailResultCallback;


    public MovieDetailServices(Context context) {
        this.context = context;
        retrofit = RetrofitBuild.build();
    }

    public void setMovieDetailResultCallback(MovieDetailResultCallback movieDetailResultCallback) {
        this.movieDetailResultCallback = movieDetailResultCallback;
    }

    public void loadInfo(int movId) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            movieDetailResultCallback.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadHomeData: error: turn off internet!");
            return;
        }

        MovieDetailInterface service = retrofit.create(MovieDetailInterface.class);
        callMovie = service.loadInfo(movId);
        callMovie.enqueue(new Callback<ApiMovieDetail>() {
            @Override
            public void onResponse(Call<ApiMovieDetail> call, Response<ApiMovieDetail> response) {
                if (!response.isSuccessful()) {
                    movieDetailResultCallback.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadHomeData: error: server down!");
                    return;
                }

                ApiMovieDetail apiMovieDetail = response.body();
                if (!apiMovieDetail.getSuccess()) {
                    movieDetailResultCallback.onFailure(apiMovieDetail.getCode(), apiMovieDetail.getMessage());
                    LogUtils.e(TAG, "loadHomeData: error: No data!");
                    return;
                }

                movieDetailResultCallback.onMovieInfoResultSuccess(apiMovieDetail.getData());
            }

            @Override
            public void onFailure(Call<ApiMovieDetail> call, Throwable t) {

            }
        });
    }

    public void loadEpisode(int movId) {

    }

    public void cancel() {

    }

    private interface MovieDetailInterface {

        @GET("phim/detail?src=android")
        Call<ApiMovieDetail> loadInfo(@Query("mov_id") int movId);
    }

    public interface MovieDetailResultCallback extends ApiResultCallback {

        void onMovieInfoResultSuccess(ApiMovieDetail.Data data);

        void onEpisodeResultSuccess();
    }

}
