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

        void addAdView(int position);

        void addRowMoviesView(int position, String title, ArrayList<Movie> movies);

        void addFooterView();

        void openActFilterMovies();

        void openActSearch();
    }

    interface Presenter extends BasePresenter {

        void loadContent();

        void reLoadContent();

        void openActFilterMovies();

        void openActSearch();

        void getIntent(Intent intent);
    }
}
