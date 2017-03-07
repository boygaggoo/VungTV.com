package com.vungtv.film.data.source.remote.service;

import android.content.Context;

import com.vungtv.film.data.source.remote.ApiError;
import com.vungtv.film.data.source.remote.ApiQuery;
import com.vungtv.film.data.source.remote.BaseApiServices;
import com.vungtv.film.data.source.remote.interfaces.ApiResultCallback;
import com.vungtv.film.data.source.remote.model.ApiFilterMovies;
import com.vungtv.film.data.source.remote.model.ApiPopupFilterMovies;
import com.vungtv.film.util.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class FilterMoviesServices extends BaseApiServices {
    private static final String TAG = FilterMoviesServices.class.getSimpleName();

    private String danhMuc = "";
    private String theLoai = "";
    private String quocGia = "";
    private String nam = "";
    private String sapXep = "";
    private String top = "";
    private int limit = 24;
    private int offset = 0;

    private Call<ApiFilterMovies> call;

    private Call<ApiPopupFilterMovies> callPopData;

    private OnPageResultCallback onPageResultCallback;

    public FilterMoviesServices(Context context) {
        super(context);
    }

    public void setOnPageResultCallback(OnPageResultCallback onPageResultCallback) {
        this.onPageResultCallback = onPageResultCallback;
    }

    /**
     * Load page data from server;
     */
    public void loadFilterMovies() {
        if (isInternetTurnOff(onPageResultCallback)) {
            return;
        }

        InterfaceServices homeService = retrofit.create(InterfaceServices.class);
        call = homeService.page(danhMuc, theLoai, quocGia, nam, sapXep, top, limit, offset);

        call.enqueue(new Callback<ApiFilterMovies>() {
            @Override
            public void onResponse(Call<ApiFilterMovies> call, Response<ApiFilterMovies> response) {

                if (!response.isSuccessful()) {
                    onPageResultCallback.onFailure(response.code(), ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadFilterMovies: error: server down!");
                    return;
                }
                ApiFilterMovies apiData = response.body();
                if (!apiData.getSuccess()) {
                    onPageResultCallback.onFailure(apiData.getCode(), apiData.getMessage());
                    LogUtils.e(TAG, "loadFilterMovies: error: No data!");
                    return;
                }

                onPageResultCallback.onPageMoviesResultSuccess(apiData.getDataPage());
            }

            @Override
            public void onFailure(Call<ApiFilterMovies> call, Throwable t) {

                onPageResultCallback.onFailure(0, ApiError.toString(context, ApiError.NO_INTERNET));
                t.printStackTrace();
            }
        });
    }

    public void loadFilterData() {
        if (isInternetTurnOff(null)) {
            onPageResultCallback.onPageFilterResultFailed(
                    ApiError.toString(context, ApiError.OFF_INTERNET));
            return;
        }

        InterfaceServices services = retrofit.create(InterfaceServices.class);
        callPopData = services.filter();
        callPopData.enqueue(new Callback<ApiPopupFilterMovies>() {
            @Override
            public void onResponse(Call<ApiPopupFilterMovies> call, Response<ApiPopupFilterMovies> response) {

                if (!response.isSuccessful()) {
                    onPageResultCallback.onPageFilterResultFailed(ApiError.toString(context, ApiError.SERVICE_ERROR));
                    LogUtils.e(TAG, "loadFilterMovies: error: server down!");
                    return;
                }

                ApiPopupFilterMovies apiData = response.body();
                if (!apiData.getSuccess()) {
                    onPageResultCallback.onPageFilterResultFailed(apiData.getMessage());
                    LogUtils.e(TAG, "loadFilterMovies: error: No data!");
                    return;
                }

                onPageResultCallback.onPageFilterResultSuccess(apiData.getData());
            }

            @Override
            public void onFailure(Call<ApiPopupFilterMovies> call, Throwable t) {
                onPageResultCallback.onPageFilterResultFailed(
                        ApiError.toString(context, ApiError.NO_INTERNET));
                t.printStackTrace();
            }
        });
    }

    /**
     * Cancel loading;
     */
    @Override
    public void cancel() {
        if (call != null && call.isExecuted())
            call.cancel();

        if (callPopData != null && callPopData.isExecuted())
            callPopData.cancel();

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
    
    public interface InterfaceServices {
        @GET("page/index?src=android")
        Call<ApiFilterMovies> page(@Query(ApiQuery.Q_DANH_MUC) String danhMuc,
                                   @Query(ApiQuery.Q_THE_LOAI) String theLoai,
                                   @Query(ApiQuery.Q_QUOC_GIA) String quocGia,
                                   @Query(ApiQuery.Q_NAM) String nam,
                                   @Query(ApiQuery.Q_SAP_XEP) String sapXep,
                                   @Query(ApiQuery.Q_TOP) String top,
                                   @Query("limit") int limit,
                                   @Query("offset") int offset);

        @GET("home/filter?src=android")
        Call<ApiPopupFilterMovies> filter();
    }

    public interface OnPageResultCallback extends ApiResultCallback {

        void onPageMoviesResultSuccess(ApiFilterMovies.DataPage dataPage);

        void onPageFilterResultSuccess(ApiPopupFilterMovies.Data data);

        void onPageFilterResultFailed(String msg);
    }
}
