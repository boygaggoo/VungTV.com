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

        void showRecyclerView(int columNumber, int rowAdsNumber, float itemWidth, int itemSpace);

        void addItemMovie(ArrayList<Movie> movies);

        void setListAdapter(ArrayList<Object> list);

        void clearSearchView();

        void clearListMovies();

        void selectSearchTypeFilmName();

        void selectSearchTypeActorName();

        void openActMovieDetail(int movieId);
    }

    interface Presenter extends BasePresenter {

        void startSearch(String query);

        void loadMore();

        void clearSearchView();

        void changeSearchType(int searchType);

        void openActMovieDetail(int movieId);

        void configChange(boolean isScreenLand, ArrayList<Object> list);
    }
}
