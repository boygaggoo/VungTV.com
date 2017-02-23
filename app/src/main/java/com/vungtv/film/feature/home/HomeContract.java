package com.vungtv.film.feature.home;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
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

        void addReCentView(int style, int itemType, int iconRes, String title, ArrayList<Object> list);

        void addHotTopicView(int style, int itemType, String url, ArrayList<Object> list);

        void addMoviesView(int style, int itemType, int iconRes, String title, String url, ArrayList<Object> list);

        void addFooterView();

        void removeAllViews();

        void openActMovieDetails(int movieId);

        void openActFilterMovies(String url);
    }

    interface Presenter extends BasePresenter {

        void loadData();

        void reloadData();

        void configChange();

        void openActMovieDetails(int movieId);

        void openActFilterMovies(String url);
    }
}
