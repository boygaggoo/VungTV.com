package com.vungtv.film.feature.usermovies;

import android.content.Context;
import android.content.Intent;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.model.ApiUserMovies;
import com.vungtv.film.data.source.remote.service.UserMoviesServices;
import com.vungtv.film.feature.filtermovies.FilterMoviesPresenter;
import com.vungtv.film.util.DensityUtils;

import static com.vungtv.film.feature.usermovies.UserMoviesActivity.INTENT_PAGE;
import static com.vungtv.film.feature.usermovies.UserMoviesActivity.PAGE_FAVORITE;
import static com.vungtv.film.feature.usermovies.UserMoviesActivity.PAGE_FOLLOW;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 2/24/2017.
 * Email: vancuong2941989@gmail.com
 */

public class UserMoviesPresenter implements UserMoviesContract.Presenter, UserMoviesServices.ResultListener {
    private static final String TAG = FilterMoviesPresenter.class.getSimpleName();

    private final Context context;

    private final UserMoviesContract.View activityView;

    private final UserMoviesServices userMoviesServices;

    private int pageType = PAGE_FAVORITE;

    private int columNumber = 3;

    private int rowAdsNumber = 8;

    private boolean isLoadmore = false;

    /* Contructor */
    public UserMoviesPresenter(Context context, UserMoviesContract.View activityView) {
        this.context = context;
        this.activityView = activityView;

        this.activityView.setPresenter(this);

        userMoviesServices = new UserMoviesServices(context);
        userMoviesServices.setAccessToken(UserSessionManager.getAccessToken(context));
        userMoviesServices.setResultListener(this);
    }

    @Override
    public void getIntent(Intent intent) {
        if (intent != null) {
            pageType = intent.getIntExtra(INTENT_PAGE, PAGE_FAVORITE);

            if (pageType == PAGE_FAVORITE) {
                activityView.setTitlePage(context.getResources().getString(R.string.home_text_phim_yeu_thich));
            } else if (pageType == PAGE_FOLLOW){
                activityView.setTitlePage(context.getResources().getString(R.string.home_text_phim_theo_doi));
            }
        }
    }

    @Override
    public void loadData() {
        activityView.showLoading(true);
        activityView.showMsgError(false, null);

        userMoviesServices.cancel();
        userMoviesServices.loadMovies(pageType);
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
        userMoviesServices.setOffset(0);
        loadData();
    }

    @Override
    public void configChange(boolean isScreenLand) {
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

        isLoadmore = true;
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
        userMoviesServices.cancel();
    }

    @Override
    public void onUserMoviesResultSuccess(ApiUserMovies.Data data) {
        activityView.showLoading(false);

        if (data.getMovies().size() == 0) {
            if (data.getOffset() == 0) {
                activityView.showMsgError(true, context.getString(R.string.user_movies_error_no_movie));
            }
            return;
        }

        activityView.addItemMovie(data.getMovies());
        if (data.getMovies().size() >= userMoviesServices.getLimit()) {
            userMoviesServices.setOffset(data.getOffset() + userMoviesServices.getLimit());
            isLoadmore = true;
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
