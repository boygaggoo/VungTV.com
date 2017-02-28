package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.RetrofitBuild;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiSearch;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by pc on 2/28/2017.
 */

public class SearchServices {
    private static final String TAG = SearchServices.class.getSimpleName();
    private static final int LIMIT = 10;

    private final Context context;

    private final Retrofit retrofit;

    private Call<ApiSearch> call;

    private SearchResultCallback searchResultCallback;

    private int offset = 0;

    private String query = "";

    private String mode = "movie";


    public SearchServices(Context context) {
        this.context = context;
        retrofit = RetrofitBuild.build();
    }

    public void setSearchResultCallback(SearchResultCallback searchResultCallback) {
        this.searchResultCallback = searchResultCallback;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setModeMovie() {
        mode = "movie";
    }

    public void setModeActor() {
        mode = "actor";
    }

    /**
     * Start request search
     */
    public void enqueueSearch() {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            searchResultCallback.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadFilterMovies: error: turn off internet!");
            return;
        }

        Retrofit retrofit = RetrofitBuild.build();
        SearchInterface services = retrofit.create(SearchInterface.class);
        call = services.load(query, mode, offset, LIMIT);
        call.enqueue(new Callback<ApiSearch>() {
            @Override
            public void onResponse(Call<ApiSearch> call, Response<ApiSearch> response) {

                if (!response.isSuccessful()) {
                    searchResultCallback.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadFilterMovies: error: server down!");
                    return;
                }

                ApiSearch apiData = response.body();
                if (!apiData.getSuccess()) {
                    searchResultCallback.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "loadFilterMovies: error: No data!");
                    return;
                }

                searchResultCallback.onSearchResultSuccess(apiData.getData());
            }

            @Override
            public void onFailure(Call<ApiSearch> call, Throwable t) {
                searchResultCallback.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                t.printStackTrace();
            }
        });
    }

    public void cancel() {
        if (call != null && call.isExecuted())
            call.cancel();
    }

    private interface SearchInterface {
        @GET("phim/search?src=android")
        Call<ApiSearch> load(
                @Query("query") String query,
                @Query("mode") String mode,
                @Query("offset") int offset,
                @Query("limit") int limit);
    }

    public interface SearchResultCallback extends ApiResultCallback {
        void onSearchResultSuccess(ApiSearch.Data data);
    }
}
