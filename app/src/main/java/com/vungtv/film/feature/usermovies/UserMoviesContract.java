package com.vungtv.film.feature.usermovies;

import android.content.Intent;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.Movie;

import java.util.ArrayList;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface UserMoviesContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(boolean show, String error);

        void showRecyclerView(int columNumber, int rowAdsNumber, float itemWidth, int itemSpace);

        void addItemMovie(ArrayList<Movie> movies);

        void setListAdapter(ArrayList<Object> list);

        void setTitlePage(String title);

        void addAdsNative();

        void openMovieDetails(int movId);

        void clearData();

        void disableRefresing();

        void logOutAccount();
    }

    interface Presenter extends BasePresenter {

        void getIntent(Intent intent);

        void loadData();

        void loadMore();

        void reloadData();

        void configChange(boolean isScreenLand, ArrayList<Object> list);
    }
}
