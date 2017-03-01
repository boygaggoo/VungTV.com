package com.vungtv.film.feature.moviedetail;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.Movie;

import java.util.ArrayList;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/1/2017.
 * Email: vancuong2941989@gmail.com
 */

public interface MovieDetailContract {

    interface View extends BaseView<Presenter> {

        void showLoadding(boolean show);

        void showMsgError(boolean show, String error);

        void setMovieInfo(Movie movie, int totalRating, double avgRating);

        void setRelateMovies(ArrayList<Movie> movies);

        void addAdsBanner();

        void openActPlayer();

        void openActPlayerYoutube();

        void openActSearch();
    }

    interface Presenter extends BasePresenter {

        void startLoadDetail(int movId);

        void reLoadDeatail(int movId);

        void watchMovie();

        void playTrailer();

        void ratingMovie(int point);

        void likeMovie();

        void unLikeMovie();

        void followMovie();

        void unFollowMovie();

        void shareMovie();

        void closeAds();
    }
}
