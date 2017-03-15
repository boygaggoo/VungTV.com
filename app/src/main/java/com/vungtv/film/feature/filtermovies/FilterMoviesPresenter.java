package com.vungtv.film.feature.filtermovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.ApiQuery;
import com.vungtv.film.data.source.remote.model.ApiFilterMovies;
import com.vungtv.film.data.source.remote.model.ApiPopupFilterMovies;
import com.vungtv.film.data.source.remote.service.FilterMoviesServices;
import com.vungtv.film.util.DensityUtils;
import com.vungtv.film.util.LogUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;
import static com.vungtv.film.feature.filtermovies.FilterMoviesActivity.INTENT_DANHMUC;
import static com.vungtv.film.feature.filtermovies.FilterMoviesActivity.INTENT_NAM;
import static com.vungtv.film.feature.filtermovies.FilterMoviesActivity.INTENT_QUOCGIA;
import static com.vungtv.film.feature.filtermovies.FilterMoviesActivity.INTENT_SAPXEP;
import static com.vungtv.film.feature.filtermovies.FilterMoviesActivity.INTENT_THELOAI;
import static com.vungtv.film.feature.filtermovies.FilterMoviesActivity.INTENT_TOP;

/**
 *
 * Created by Mr Cuong on 2/3/2017.
 */

public class FilterMoviesPresenter implements FilterMoviesContract.Presenter {

    private static final String TAG = FilterMoviesPresenter.class.getSimpleName();

    private final Context context;

    private final FilterMoviesContract.View activityView;

    private final FilterMoviesServices filterMoviesServices;

    private boolean isLoadmore = false;

    private int columNumber = 3;

    private int rowAdsNumber = 8;

    public FilterMoviesPresenter(Context context, FilterMoviesContract.View activityView) {
        this.context = checkNotNull(context);
        this.activityView = checkNotNull(activityView);

        this.activityView.setPresenter(this);

        filterMoviesServices = new FilterMoviesServices(context);
        filterMoviesServicesResponse();
    }

    @Override
    public void start() {
        float itemWidth = DensityUtils.getWidthInPx(context);
        int itemSpace = context.getResources().getDimensionPixelSize(R.dimen.space_3);
        if (context.getResources().getBoolean(R.bool.isTabletLand)) {
            columNumber = 6;
            rowAdsNumber = 4;
        }

        itemWidth = itemWidth - itemSpace*(columNumber + 1);
        itemWidth = itemWidth / columNumber;

        activityView.showRecyclerView(columNumber, rowAdsNumber, itemWidth, itemSpace);
    }

    @Override
    public void onDestroy() {
        filterMoviesServices.cancel();
    }

    @Override
    public void getIntentData(Intent intent) {
        if (intent == null) return;
        Bundle bundle = intent.getExtras();
        if (bundle == null) return;

        filterMoviesServices.setDanhMuc(bundle.getString(INTENT_DANHMUC, ""));
        filterMoviesServices.setTheLoai(bundle.getString(INTENT_THELOAI, ""));
        filterMoviesServices.setQuocGia(bundle.getString(INTENT_QUOCGIA, ""));
        filterMoviesServices.setNam(bundle.getString(INTENT_NAM, ""));
        filterMoviesServices.setSapXep(bundle.getString(INTENT_SAPXEP, ApiQuery.P_MOVIES_SORT[0]));
        filterMoviesServices.setTop(bundle.getString(INTENT_TOP, ""));
    }

    @Override
    public void loadData() {
        filterMoviesServices.cancel();
        filterMoviesServices.loadFilterMovies();
        activityView.showLoading(true);
        activityView.showMsgError(false, null);
    }

    @Override
    public void loadMore() {
        if (isLoadmore) {
            isLoadmore = false;
            loadData();
            LogUtils.d(TAG, "onLoadmore");
        }
    }

    @Override
    public void reloadData() {
        isLoadmore = false;
        activityView.clearData();
        activityView.disableRefresing();
        filterMoviesServices.setOffset(0);
        loadData();
    }

    @Override
    public void configChange(boolean isScreenLand) {
        boolean isLoadmoreCur = isLoadmore;
        isLoadmore = false;

        if (isScreenLand) {
            columNumber = 6;
            rowAdsNumber = 4;
        } else {
            columNumber = 3;
            rowAdsNumber = 8;
        }

        float itemWidth = DensityUtils.getWidthInPx(context);
        int itemSpace = context.getResources().getDimensionPixelSize(R.dimen.space_3);

        itemWidth = itemWidth - itemSpace * (columNumber + 1);
        itemWidth = itemWidth / columNumber;

        activityView.updateRecyclerView(columNumber, rowAdsNumber, itemWidth);

        isLoadmore = isLoadmoreCur;
    }

    @Override
    public void openMovieDetails(int movieId) {
        activityView.openActMovieDetail(movieId);
        LogUtils.e(TAG, "openActMovieDetail movId 3 = " + movieId);
    }

    @Override
    public void openPopupSort(View view) {
        activityView.showPopupSort(view);
    }

    @Override
    public void sortMoviesSubmit(String sapXep) {
        filterMoviesServices.setSapXep(sapXep);
        reloadData();
    }

    @Override
    public void openPopupFilter() {
        activityView.showLoading(true);
        filterMoviesServices.loadFilterData();
    }

    @Override
    public void filterMoviesSubmit(String sapXep, String danhMuc, String quocGia, String theLoai, String nam) {
        filterMoviesServices.setSapXep(sapXep);
        filterMoviesServices.setDanhMuc(danhMuc);
        filterMoviesServices.setQuocGia(quocGia);
        filterMoviesServices.setTheLoai(theLoai);
        filterMoviesServices.setNam(nam);

        reloadData();
    }

    private void filterMoviesServicesResponse() {
        filterMoviesServices.setOnPageResultCallback(new FilterMoviesServices.OnPageResultCallback() {
            @Override
            public void onPageMoviesResultSuccess(ApiFilterMovies.DataPage dataPage) {

                activityView.showLoading(false);
                activityView.showToolbarTitle(dataPage.getTitle());

                if (dataPage.getMovies().size() > 0) {
                    // add ads;
                    activityView.addAdsNative();
                }
                activityView.addItemMovie(dataPage.getMovies());
                if (dataPage.getMovies().size() >= filterMoviesServices.getLimit()) {
                    isLoadmore = true;
                    filterMoviesServices.setOffset(dataPage.getOffset() + filterMoviesServices.getLimit());
                }
            }

            @Override
            public void onPageFilterResultSuccess(ApiPopupFilterMovies.Data data) {
                activityView.showLoading(false);
                activityView.showPopupFilter(true, data);
            }

            @Override
            public void onPageFilterResultFailed(String msg) {
                activityView.showLoading(false);
                activityView.showMsgToast(msg);
            }

            @Override
            public void onFailure(int code, String error) {
                activityView.showLoading(false);
                activityView.showMsgError(true, error);
            }
        });
    }
}
