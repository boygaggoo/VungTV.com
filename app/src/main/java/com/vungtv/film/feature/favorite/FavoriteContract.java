package com.vungtv.film.feature.favorite;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.Movie;

import java.util.ArrayList;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface FavoriteContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(boolean show, String error);

        void showRecyclerView(int columNumber, int rowAdsNumber, float itemWidth, int itemSpace);

        void showActMovieDetails(int movieId);

        void addItemMovie(ArrayList<Movie> movies);

        void setListAdapter(ArrayList<Object> list);

        void addAdsNative();

        void clearData();

        void disableRefresing();

        void logOutAccount();
    }

    interface Presenter extends BasePresenter {

        void loadData();

        void loadMore();

        void reloadData();

        void configChange(boolean isScreenLand, ArrayList<Object> list);

        void openMovieDetails(int movieId);
    }
}
