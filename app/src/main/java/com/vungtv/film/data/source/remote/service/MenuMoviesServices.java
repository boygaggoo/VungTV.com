package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiMenuMovies;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *
 * Created by pc on 3/7/2017.
 */

public class MenuMoviesServices extends BaseApiServices {

    private Call<ApiMenuMovies> call;

    private MenuMoviesResultCallback menuMoviesResultCallback;

    public MenuMoviesServices(Context context) {
        super(context);
    }

    public void setMenuMoviesResultCallback(MenuMoviesResultCallback menuMoviesResultCallback) {
        this.menuMoviesResultCallback = menuMoviesResultCallback;
    }

    public void loadMovies(String path) {
        if (isInternetTurnOff(menuMoviesResultCallback)) {
            return;
        }

        InterfaceServices service = retrofit.create(InterfaceServices.class);
        call = service.listMovies(path);
        call.enqueue(new Callback<ApiMenuMovies>() {
            @Override
            public void onResponse(Call<ApiMenuMovies> call, Response<ApiMenuMovies> response) {
                if (menuMoviesResultCallback == null) return;

                if (!response.isSuccessful()) {
                    menuMoviesResultCallback.onFailure(response.code(),
                            ApiError.toString(context, ApiError.SERVICE_ERROR));
                    return;
                }

                ApiMenuMovies apiData = response.body();
                if (!apiData.getSuccess()) {
                    menuMoviesResultCallback.onFailure(apiData.getCode(), apiData.getMessage());
                    return;
                }

                menuMoviesResultCallback.onMenuMoviesResultSuccess(apiData.data);
            }

            @Override
            public void onFailure(Call<ApiMenuMovies> call, Throwable t) {
                if (menuMoviesResultCallback != null) {
                    menuMoviesResultCallback.onFailure(0,
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

    private interface InterfaceServices {

        @GET("page/{path}/?src=android&limit=10&offset=0")
        Call<ApiMenuMovies> listMovies(@Path("path") String path);

    }

    public interface MenuMoviesResultCallback extends ApiResultCallback {

        void onMenuMoviesResultSuccess(ApiMenuMovies.Data data);
    }
}
