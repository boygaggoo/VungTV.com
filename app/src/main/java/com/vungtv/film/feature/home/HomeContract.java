package com.vungtv.film.feature.home;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.MovieRecent;
import com.vungtv.film.model.Slider;

import java.util.ArrayList;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface HomeContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(boolean show, String error);

        void addSliderView(ArrayList<Slider> listSlider);

        void addReCentView(ArrayList<Object> list);

        void addHotTopicView(int style, int itemType, String url, ArrayList<Object> list);

        void addMoviesView(int style, int itemType, int iconRes, String title, String url, ArrayList<Object> list);

        void addFooterView();

        void updateRecentView(ArrayList<Object> list);

        void removeAllViews();

        void openActMovieDetail(int movieId);

        void openActFilterMovies(String url);

        void openActRecentMovies();

        void openActPlayer(MovieRecent movieRecent);
    }

    interface Presenter extends BasePresenter {

        void loadData();

        void reloadData();

        void reloadRecentMovies();

        void configChange();

        void playRecentMovies(int pos);
    }
}
