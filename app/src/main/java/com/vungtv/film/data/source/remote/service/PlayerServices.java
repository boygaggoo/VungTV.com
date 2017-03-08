package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiModel;
import com.vungtv.film.data.source.remote.model.ApiMoviePlayer;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/4/2017.
 * Email: vancuong2941989@gmail.com
 */

public class PlayerServices extends BaseApiServices{

    private static final String TAG = PlayerServices.class.getSimpleName();

    private Call<ApiMoviePlayer> call;

    private Call<ApiModel> callCounter;

    private PlayerInterface service;

    private OnPlayerServicesCallback onPlayerServicesCallback;

    public PlayerServices(Context context) {
        super(context);
        service = retrofit.create(PlayerInterface.class);
    }

    public void loadEpisodeInfo(int movId, String epsHash) {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            if (onPlayerServicesCallback != null)
                onPlayerServicesCallback.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadEpisodeInfo: error: turn off internet!");
            return;
        }

        call = service.epsInfo(movId, epsHash);
        call.enqueue(new Callback<ApiMoviePlayer>() {
            @Override
            public void onResponse(Call<ApiMoviePlayer> call, Response<ApiMoviePlayer> response) {
                if (onPlayerServicesCallback == null) return;

                if (!response.isSuccessful()) {
                    onPlayerServicesCallback.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadEpisodeInfo: error: server down!");
                    return;
                }

                ApiMoviePlayer apiData = response.body();

                if (!apiData.getSuccess()) {
                    onPlayerServicesCallback.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "loadEpisodeInfo: error: " + apiData.getMessage());
                    return;
                }

                onPlayerServicesCallback.onLoadEpisodeInfoSuccess(apiData.data);
            }

            @Override
            public void onFailure(Call<ApiMoviePlayer> call, Throwable t) {
                if (onPlayerServicesCallback != null)
                    onPlayerServicesCallback.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                t.printStackTrace();
            }
        });
    }

    public void countPlay(int movId, String token) {
        if (isInternetTurnOff(null)) {
            return;
        }

        callCounter = service.countView(movId, token);
        callCounter.enqueue(new Callback<ApiModel>() {
            @Override
            public void onResponse(Call<ApiModel> call, Response<ApiModel> response) {
                LogUtils.d(TAG, "counter : " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<ApiModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void setOnPlayerServicesCallback(OnPlayerServicesCallback onPlayerServicesCallback) {
        this.onPlayerServicesCallback = onPlayerServicesCallback;
    }

    public void cancel() {
        if (call != null && call.isExecuted()) {
            call.cancel();
        }

        if (callCounter != null && callCounter.isExecuted()) {
            callCounter.cancel();
        }
    }

    private interface PlayerInterface {

        @GET("phim/player/{mov_id}/{eps_hash}?src=android")
        Call<ApiMoviePlayer> epsInfo(@Path("mov_id") int movId, @Path("eps_hash") String epsHash);

        @GET("phim/counter?src=android")
        Call<ApiModel> countView(@Query("mov_id")int movId, @Query("token")String token);
    }

    public interface OnPlayerServicesCallback extends ApiResultCallback{
        void onLoadEpisodeInfoSuccess(ApiMoviePlayer.Data data);
    }
}
