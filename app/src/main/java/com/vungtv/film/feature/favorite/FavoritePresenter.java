package com.vungtv.film.feature.favorite;

import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.model.ApiFilterMovies;
import com.vungtv.film.data.source.remote.service.FavoriteServices;
import com.vungtv.film.feature.filtermovies.FilterMoviesPresenter;
import com.vungtv.film.util.DensityUtils;

import java.util.ArrayList;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 2/24/2017.
 * Email: vancuong2941989@gmail.com
 */

public class FavoritePresenter implements FavoriteContract.Presenter, FavoriteServices.OnFavoriteServicesListener {
    private static final String TAG = FilterMoviesPresenter.class.getSimpleName();

    private final Context context;

    private final FavoriteContract.View activityView;

    private final FavoriteServices favoriteServices;

    private int columNumber = 3;

    private int rowAdsNumber = 8;

    private boolean isLoadmore = false;

    /* Contructor */
    public FavoritePresenter(Context context, FavoriteContract.View activityView) {
        this.context = context;
        this.activityView = activityView;

        this.activityView.setPresenter(this);

        favoriteServices = new FavoriteServices(context);
        favoriteServices.setAccessToken(UserSessionManager.getAccessToken(context));
        favoriteServices.setOnFavoriteServicesListener(this);
    }

    @Override
    public void loadData() {
        favoriteServices.cancel();
        favoriteServices.loadFavorite();
        activityView.showLoading(true);
        activityView.showMsgError(false, null);
    }

    @Override
    public void loadMore() {
        if (isLoadmore) {
            isLoadmore = false;
            loadData();
        }
    }

    @Override
    public void reloadData() {
        isLoadmore = false;
        activityView.clearData();
        activityView.disableRefresing();
        favoriteServices.setOffset(0);
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
        activityView.showActMovieDetails(movieId);
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

        loadData();
    }

    @Override
    public void onDestroy() {
        favoriteServices.cancel();
    }

    @Override
    public void onFavoriteLoadSuccess(ApiFilterMovies.DataPage dataPage) {
        activityView.showLoading(false);

        if (dataPage.getMovies().size() > 0) {
            // add ads;
            activityView.addAdsNative();
        }

        activityView.addItemMovie(dataPage.getMovies());

        if (dataPage.getMovies().size() > 0) {
            isLoadmore = true;
            favoriteServices.setOffset(dataPage.getOffset() + favoriteServices.getLimit());
        }
    }

    @Override
    public void onFailure(int code, String error) {

        activityView.showLoading(false);
        activityView.showMsgError(true, error);

        if (code == -999) {
            activityView.logOutAccount();
        }
    }
}
