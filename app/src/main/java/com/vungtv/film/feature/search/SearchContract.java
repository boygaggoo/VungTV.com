package com.vungtv.film.feature.search;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.Movie;

import java.util.ArrayList;

/**
 *
 * Created by pc on 2/8/2017.
 */

public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showLoadding(boolean show);

        void showMsgError(boolean show, String error);

        void clearSearchView();

        void setRecyclerView(int columNumber, float itemWidth, int itemSpace);

        void showListMovies(boolean show, ArrayList<Movie> listMovies);

        void clearListMovies();

        void selectSearchTypeFilmName();

        void selectSearchTypeActorName();

        void openActMovieDetail(int movieId);
    }

    interface Presenter extends BasePresenter {

        void loadData(int searchType, String query);

        void clearSearchView();

        void changeSearchType(int searchType);

        void openActMovieDetail(int movieId);
    }
}
