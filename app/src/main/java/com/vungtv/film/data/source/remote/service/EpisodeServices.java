package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiEpisodes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pc on 3/6/2017.
 */

public class EpisodeServices extends BaseApiServices {

    private Call<ApiEpisodes> callEps;

    private EpisodeResultCallback episodeResultCallback;

    public EpisodeServices(Context context) {
        super(context);
    }

    public void setEpisodeResultCallback(EpisodeResultCallback episodeResultCallback) {
        this.episodeResultCallback = episodeResultCallback;
    }

    /**
     * Get ds tap
     */
    public void loadListEpisodes(int movId) {
        if (isInternetTurnOff(episodeResultCallback)) {
            return;
        }

        EpisodeServicesInterface service = retrofit.create(EpisodeServicesInterface.class);
        callEps = service.loadEpisodes(movId);
        callEps.enqueue(new Callback<ApiEpisodes>() {
            @Override
            public void onResponse(Call<ApiEpisodes> call, Response<ApiEpisodes> response) {
                if (episodeResultCallback == null) return;

                if (!response.isSuccessful()) {
                    episodeResultCallback.onEpisodeResultFailed(
                            ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                ApiEpisodes apiEpisodes = response.body();
                if (!apiEpisodes.getSuccess()) {
                    episodeResultCallback.onEpisodeResultFailed(apiEpisodes.getMessage());
                    return;
                }

                episodeResultCallback.onEpisodeResultSuccess(apiEpisodes.getData());
            }

            @Override
            public void onFailure(Call<ApiEpisodes> call, Throwable t) {
                if (episodeResultCallback != null) {
                    episodeResultCallback.onFailure(0,
                            ApiError.toString(context, ApiError.NO_INTERNET));
                }
                t.printStackTrace();
            }
        });
    }

    @Override
    public void cancel() {
        if (callEps != null && callEps.isExecuted()) {
            callEps.cancel();
        }
    }

    public interface EpisodeResultCallback extends ApiResultCallback {

        void onEpisodeResultSuccess(ApiEpisodes.Data data);

        void onEpisodeResultFailed(String msg);
    }

    private interface EpisodeServicesInterface {

        @GET("phim/episodes?src=android")
        Call<ApiEpisodes> loadEpisodes(@Query("mov_id") int movId);
    }
}
