package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.RetrofitBuild;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiEpisodes;
import com.vungtv.film.data.source.remote.model.ApiModel;
import com.vungtv.film.data.source.remote.model.ApiMovieDetail;
import com.vungtv.film.data.source.remote.model.ApiRating;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 2/25/2017.
 * Email: vancuong2941989@gmail.com
 */

public class MovieDetailServices {
    private static final String TAG = MovieDetailServices.class.getSimpleName();

    private static final String SRC = "android";

    private final Context context;

    private final Retrofit retrofit;

    private Call<ApiMovieDetail> callMovie;

    private Call<ApiEpisodes> callEps;

    private Call<ApiModel> callAction;

    private Call<ApiRating> callRating;

    private MovieDetailResultCallback movieDetailResultCallback;

    private int movId = 0;


    public MovieDetailServices(Context context) {
        this.context = context;
        retrofit = RetrofitBuild.build();
    }

    public void setMovieDetailResultCallback(MovieDetailResultCallback movieDetailResultCallback) {
        this.movieDetailResultCallback = movieDetailResultCallback;
    }

    public void setMovId(int movId) {
        this.movId = movId;
    }

    public int getMovId() {
        return movId;
    }

    /**
     * Get thông tin phim
     */
    public void loadInfo(String token) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (movieDetailResultCallback != null)
                movieDetailResultCallback.onFailure(0,
                        ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadInfo: error: turn off internet!");
            return;
        }

        MovieDetailInterface service = retrofit.create(MovieDetailInterface.class);
        callMovie = service.loadInfo(movId, token);
        callMovie.enqueue(new Callback<ApiMovieDetail>() {
            @Override
            public void onResponse(Call<ApiMovieDetail> call, Response<ApiMovieDetail> response) {
                if (movieDetailResultCallback == null) return;

                if (!response.isSuccessful()) {
                    movieDetailResultCallback.onFailure(response.code(),
                            ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadInfo: error: server down!");
                    return;
                }

                ApiMovieDetail apiMovieDetail = response.body();
                if (!apiMovieDetail.getSuccess()) {
                    movieDetailResultCallback.onFailure(
                            apiMovieDetail.getCode(), apiMovieDetail.getMessage());
                    LogUtils.e(TAG, "loadInfo: error: No data!");
                    return;
                }

                movieDetailResultCallback.onMovieInfoResultSuccess(apiMovieDetail.getData());
            }

            @Override
            public void onFailure(Call<ApiMovieDetail> call, Throwable t) {
                if (movieDetailResultCallback != null) {
                    movieDetailResultCallback.onFailure(0,
                            ApiError.toString(context, ApiError.NO_INTERNET));
                }
                t.printStackTrace();
            }
        });
    }

    /**
     * Get ds tap
     */
    public void loadListEpisodes() {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (movieDetailResultCallback != null)
                movieDetailResultCallback.onActionChangeFailed(
                        ApiError.toString(context, ApiError.OFF_INTERNET));
            return;
        }

        MovieDetailInterface service = retrofit.create(MovieDetailInterface.class);
        callEps = service.loadEpisodes(movId);
        callEps.enqueue(new Callback<ApiEpisodes>() {
            @Override
            public void onResponse(Call<ApiEpisodes> call, Response<ApiEpisodes> response) {
                if (movieDetailResultCallback == null) return;

                if (!response.isSuccessful()) {
                    movieDetailResultCallback.onActionChangeFailed(
                            ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                ApiEpisodes apiEpisodes = response.body();
                if (!apiEpisodes.getSuccess()) {
                    movieDetailResultCallback.onActionChangeFailed(apiEpisodes.getMessage());
                    return;
                }

                movieDetailResultCallback.onEpisodeResultSuccess(apiEpisodes.getData());
            }

            @Override
            public void onFailure(Call<ApiEpisodes> call, Throwable t) {
                if (movieDetailResultCallback != null) {
                    movieDetailResultCallback.onFailure(0,
                            ApiError.toString(context, ApiError.NO_INTERNET));
                }
                t.printStackTrace();
            }
        });
    }

    /**
     * Thich / bỏ thích phim
     *
     * @param action like / unlike
     * @param token token đang nhap
     */
    public void likeMovie(String action, String token) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (movieDetailResultCallback != null)
                movieDetailResultCallback.onActionChangeFailed(
                        ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadHomeData: error: turn off internet!");
            return;
        }

        MovieDetailInterface service = retrofit.create(MovieDetailInterface.class);
        callAction = null;
        callAction = service.likeMovie(action, movId, token, SRC);
        callAction.enqueue(new Callback<ApiModel>() {
            @Override
            public void onResponse(Call<ApiModel> call, Response<ApiModel> response) {

                if (!response.isSuccessful()) {
                    movieDetailResultCallback.onActionChangeFailed(
                            ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                if (!response.body().getSuccess()) {
                    movieDetailResultCallback.onActionChangeFailed(
                            response.body().getMessage()
                    );
                    return;
                }

                movieDetailResultCallback.onLikeMovieSuccess(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ApiModel> call, Throwable t) {
                if (movieDetailResultCallback != null) {
                    movieDetailResultCallback.onActionChangeFailed(
                            ApiError.toString(context, ApiError.NO_INTERNET));
                }
                t.printStackTrace();
            }
        });
    }

    /**
     * Theo dõi phim
     *
     * @param action follow / unfollow
     * @param token token đang nhap
     */
    public void followMovie(String action, String token) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (movieDetailResultCallback != null)
                movieDetailResultCallback.onActionChangeFailed(
                        ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadHomeData: error: turn off internet!");
            return;
        }

        MovieDetailInterface service = retrofit.create(MovieDetailInterface.class);
        callAction = null;
        callAction = service.followMovie(action, movId, token, SRC);
        callAction.enqueue(new Callback<ApiModel>() {
            @Override
            public void onResponse(Call<ApiModel> call, Response<ApiModel> response) {

                if (!response.isSuccessful()) {
                    movieDetailResultCallback.onActionChangeFailed(
                            ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                if (!response.body().getSuccess()) {
                    movieDetailResultCallback.onActionChangeFailed(
                            response.body().getMessage()
                    );
                    return;
                }

                movieDetailResultCallback.onFollowMovieSuccess(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ApiModel> call, Throwable t) {
                if (movieDetailResultCallback != null) {
                    movieDetailResultCallback.onActionChangeFailed(
                            ApiError.toString(context, ApiError.NO_INTERNET));
                }
                t.printStackTrace();
            }
        });
    }

    /**
     * Đánh giá phim;
     *
     * @param star sao
     */
    public void ratingMovie(int star) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (movieDetailResultCallback != null)
                movieDetailResultCallback.onActionChangeFailed(
                        ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadHomeData: error: turn off internet!");
            return;
        }

        MovieDetailInterface service = retrofit.create(MovieDetailInterface.class);

        callRating = service.rating(star, movId, SRC);
        callRating.enqueue(new Callback<ApiRating>() {
            @Override
            public void onResponse(Call<ApiRating> call, Response<ApiRating> response) {

                if (!response.isSuccessful()) {
                    movieDetailResultCallback.onActionChangeFailed(
                            ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                if (!response.body().getSuccess()) {
                    movieDetailResultCallback.onActionChangeFailed(
                            response.body().getMessage()
                    );
                    return;
                }

                movieDetailResultCallback.onRatingMovieSuccess(
                        response.body().getMessage(),
                        response.body().getData().getRating().total,
                        response.body().getData().getRating().avg
                );
            }

            @Override
            public void onFailure(Call<ApiRating> call, Throwable t) {
                if (movieDetailResultCallback != null) {
                    movieDetailResultCallback.onActionChangeFailed(ApiError.toString(context, ApiError.NO_INTERNET));
                }
                t.printStackTrace();
            }
        });
    }

    public void cancel() {
        if (callMovie != null && callMovie.isExecuted()) {
            callMovie.cancel();
        }

        if (callEps != null && callEps.isExecuted()) {
            callEps.cancel();
        }

        if (callAction != null && callAction.isExecuted()) {
            callAction.cancel();
        }
    }

    private interface MovieDetailInterface {

        @GET("phim/detail?src=android")
        Call<ApiMovieDetail> loadInfo(@Query("mov_id") int movId, @Query("token") String token);

        @GET("phim/episodes?src=android")
        Call<ApiEpisodes> loadEpisodes(@Query("mov_id") int movId);

        @FormUrlEncoded
        @POST("phim/like_phim")
        Call<ApiModel> likeMovie(
                @Field("action") String action,
                @Field("mov_id") int movId,
                @Field("token") String token,
                @Field("src") String src);

        @FormUrlEncoded
        @POST("phim/follow_film")
        Call<ApiModel> followMovie(
                @Field("action") String action,
                @Field("mov_id") int movId,
                @Field("token") String token,
                @Field("src") String src);

        @FormUrlEncoded
        @POST("phim/rating_movie")
        Call<ApiRating> rating(
                @Field("star") int star,
                @Field("mov_id") int movId,
                @Field("src") String src);
    }

    public interface MovieDetailResultCallback extends ApiResultCallback {

        void onMovieInfoResultSuccess(ApiMovieDetail.Data data);

        void onEpisodeResultSuccess(ApiEpisodes.Data data);

        void onLikeMovieSuccess(String mes);

        void onFollowMovieSuccess(String mes);

        void onRatingMovieSuccess(String msg, int total, float avg);

        void onActionChangeFailed(String mes);
    }

}
