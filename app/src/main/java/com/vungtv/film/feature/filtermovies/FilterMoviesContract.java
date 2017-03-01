package com.vungtv.film.feature.filtermovies;

import android.content.Intent;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.Movie;

import java.util.ArrayList;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface FilterMoviesContract {

    interface View extends BaseView<Presenter> {
        void showLoading(boolean show);

        void showMsgError(boolean show, String error);

        void showToolbarTitle(String title);

        void showRecyclerView(int columNumber, int rowAdsNumber, float itemWidth, int itemSpace);

        void showPopupFilter(boolean show);

        void showPopupSort(android.view.View view);

        void openActMovieDetails(int movieId);

        void addItemMovie(ArrayList<Movie> movies);

        void setListAdapter(ArrayList<Object> list);

        void addAdsNative();

        void addAdsNative(int pos);

        void clearData();

        void disableRefresing();
    }

    interface Presenter extends BasePresenter {

        void getIntentData(Intent intent);

        void loadData();

        void loadMore();

        void reloadData();

        void configChange(boolean isScreenLand, ArrayList<Object> list);

        void openMovieDetails(int movieId);

        void sapXepMoviesClick(android.view.View view);

        void sapXepMoviesSubmit(String sapXep);

        void filterMoviesClick();

        void filterMoviesSubmit(String sapXep, String danhMuc, String quocGia, String theLoai, String nam);
    }
}
