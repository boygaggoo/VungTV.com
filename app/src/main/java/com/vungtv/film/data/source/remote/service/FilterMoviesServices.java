package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.ApiQuery;
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

public class FilterMoviesServices {
    private static final String TAG = FilterMoviesServices.class.getSimpleName();
    private static final String SRC = "android";
    private Context context;
    private String danhMuc = "";
    private String theLoai = "";
    private String quocGia = "";
    private String nam = "";
    private String sapXep = "";
    private String top = "";
    private int limit = 24;
    private int offset = 0;
    private Call<ApiFilterMovies> call;
    private PageResultCallback pageResultCallback;

    public FilterMoviesServices(Context context) {
        this.context = context;
    }

    public void setPageResultCallback(PageResultCallback pageResultCallback) {
        this.pageResultCallback = pageResultCallback;
    }

    /**
     * Load page data from server;
     */
    public void loadFilterMovies() {
        if (!NetworkUtils.isInternetTurnOn(context)) {
            pageResultCallback.onFailure(0, ApiError.toString(context, ApiError.OFF_INTERNET));
            LogUtils.e(TAG, "loadFilterMovies: error: turn off internet!");
            return;
        }

        Retrofit retrofit = RetrofitBuild.build();
        FilterMoviesInterface homeService = retrofit.create(FilterMoviesInterface.class);
        call = homeService.loadPage(danhMuc, theLoai, quocGia, nam, sapXep, top, limit, offset, SRC);
        LogUtils.d(TAG, "loadFilterMovies: " + call.request().url());

        call.enqueue(new Callback<ApiFilterMovies>() {
            @Override
            public void onResponse(Call<ApiFilterMovies> call, Response<ApiFilterMovies> response) {

                if (!response.isSuccessful()) {
                    pageResultCallback.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadFilterMovies: error: server down!");
                    return;
                }
                ApiFilterMovies apiData = response.body();
                if (!apiData.getSuccess()) {
                    pageResultCallback.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "loadFilterMovies: error: No data!");
                    return;
                }

                pageResultCallback.onSuccess(apiData.getDataPage());
            }

            @Override
            public void onFailure(Call<ApiFilterMovies> call, Throwable t) {

                pageResultCallback.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                LogUtils.e(TAG, "loadFilterMovies: onFailure: " + t.getMessage());
            }
        });
    }

    /**
     * Cancel loading;
     */
    public void cancel() {
        if (call != null && call.isExecuted())
            call.cancel();
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public void setQuocGia(String quocGia) {
        this.quocGia = quocGia;
    }

    public void setNam(String nam) {
        this.nam = nam;
    }

    public void setSapXep(String sapXep) {
        this.sapXep = sapXep;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public String getQuocGia() {
        return quocGia;
    }

    public String getNam() {
        return nam;
    }

    public String getSapXep() {
        return sapXep;
    }

    public String getTop() {
        return top;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
    
    public interface FilterMoviesInterface {
        @GET("page/index")
        Call<ApiFilterMovies> loadPage(@Query(ApiQuery.Q_DANH_MUC) String danhMuc,
                                       @Query(ApiQuery.Q_THE_LOAI) String theLoai,
                                       @Query(ApiQuery.Q_QUOC_GIA) String quocGia,
                                       @Query(ApiQuery.Q_NAM) String nam,
                                       @Query(ApiQuery.Q_SAP_XEP) String sapXep,
                                       @Query(ApiQuery.Q_TOP) String top,
                                       @Query("limit") int limit,
                                       @Query("offset") int offset,
                                       @Query("src") String src);
    }

    public interface PageResultCallback extends ApiResultCallback {
        void onSuccess(ApiFilterMovies.DataPage dataPage);
    }
}
