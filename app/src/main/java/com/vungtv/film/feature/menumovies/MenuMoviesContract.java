package com.vungtv.film.feature.menumovies;

import android.content.Intent;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.Movie;

import java.util.ArrayList;

/**
 *
 * Created by Mr Cuong on 2/24/2017.
 */
public interface MenuMoviesContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(boolean show, String err);

        void setToolbarTitle(String title);

        void addAdView(int position);

        void addRowMoviesView(int position, String title, ArrayList<Movie> movies, String urlMore);

        void addFooterView();

        void openActFilterMovies(String url);

        void openActSearch();

        void openActMovieDetail(int movId);

        void removeAllViews();
    }

    interface Presenter extends BasePresenter {

        void getIntent(Intent intent);

        void loadContent();

        void reLoadContent();
    }
}
