package com.vungtv.film.feature.filtermovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.model.ApiFilterMovies;
import com.vungtv.film.data.source.remote.service.FilterMoviesServices;
import com.vungtv.film.util.DensityUtils;
import com.vungtv.film.util.LogUtils;

import java.util.ArrayList;

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

    public FilterMoviesPresenter(Context context, FilterMoviesContract.View activityView, FilterMoviesServices filterMoviesServices) {
        this.context = checkNotNull(context);
        this.activityView = checkNotNull(activityView);
        this.filterMoviesServices = checkNotNull(filterMoviesServices);

        activityView.setPresenter(this);
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
        filterMoviesServices.setSapXep(bundle.getString(INTENT_SAPXEP, ""));
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
    public void configChange(boolean isScreenLand, ArrayList<Object> list) {
        ArrayList<Object> listCop = new ArrayList<>();
        listCop.addAll(list);

        isLoadmore = false;
        activityView.clearData();

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

        activityView.showRecyclerView(columNumber, rowAdsNumber, itemWidth, itemSpace);

        activityView.setListAdapter(listCop);

        isLoadmore = true;
    }

    @Override
    public void openMovieDetails(int movieId) {
        activityView.openActMovieDetail(movieId);
        LogUtils.e(TAG, "openActMovieDetail movId 3 = " + movieId);
    }

    @Override
    public void sapXepMoviesClick(View view) {
        activityView.showPopupSort(view);
    }

    @Override
    public void sapXepMoviesSubmit(String sapXep) {
        filterMoviesServices.setSapXep(sapXep);
        reloadData();
    }

    @Override
    public void filterMoviesClick() {

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
        filterMoviesServices.setPageResultCallback(new FilterMoviesServices.PageResultCallback() {
            @Override
            public void onSuccess(ApiFilterMovies.DataPage dataPage) {

                activityView.showLoading(false);
                activityView.showToolbarTitle(dataPage.getTitle());

                if (dataPage.getMovies().size() > 0) {
                    // add ads;
                    activityView.addAdsNative();
                }
                activityView.addItemMovie(dataPage.getMovies());
                if (dataPage.getMovies().size() > 0) {
                    isLoadmore = true;
                    filterMoviesServices.setOffset(dataPage.getOffset() + filterMoviesServices.getLimit());
                }
            }

            @Override
            public void onFailure(int code, String error) {
                activityView.showLoading(false);
                activityView.showMsgError(true, error);
            }
        });
    }
}
