package com.vungtv.film.feature.search;

import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.model.ApiSearch;
import com.vungtv.film.data.source.remote.service.SearchServices;
import com.vungtv.film.model.Movie;
import com.vungtv.film.util.DensityUtils;

import java.util.ArrayList;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 *
 * Created by pc on 2/8/2017.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private final Context context;

    private final SearchContract.View activityView;

    private final SearchServices searchServices;

    private boolean isLoadmore = false;

    private int columNumber = 3;

    private int rowAdsNumber = 8;

    public SearchPresenter(Context context, final SearchContract.View activityView) {
        this.context = checkNotNull(context);

        this.activityView = checkNotNull(activityView);
        this.activityView.setPresenter(this);

        searchServices = new SearchServices(context);
        searchServices.setSearchResultCallback(new SearchServices.SearchResultCallback() {
            @Override
            public void onSearchResultSuccess(ApiSearch.Data data) {
                activityView.showLoadding(false);
                activityView.showMsgError(false, null);
                activityView.showListMovies(true, (ArrayList<Movie>) data.getMovies());
            }

            @Override
            public void onFailure(int code, String error) {
                activityView.showLoadding(false);
                activityView.showMsgError(true, error);
                activityView.showListMovies(false, null);
            }
        });
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
        searchServices.cancel();
    }

    @Override
    public void loadData(String query) {
        if (query.length() < 3) {
            activityView.showMsgError(true, context.getString(R.string.search_error_msg_short));
            return;
        }

        searchServices.setQuery(query);
        searchServices.enqueueSearch();
        activityView.showLoadding(true);
    }

    @Override
    public void clearSearchView() {
        activityView.clearSearchView();
        searchServices.setOffset(0);
    }

    @Override
    public void changeSearchType(int searchType) {
        if (searchType == 0) {
            searchServices.setModeMovie();
        } else {
            searchServices.setModeActor();
        }

        if (searchServices.getQuery().length() > 2) {
            clearSearchView();
            searchServices.enqueueSearch();
            activityView.showLoadding(true);
        }
    }

    @Override
    public void openActMovieDetail(int movieId) {

    }


}
