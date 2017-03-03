package com.vungtv.film.feature.moviedetail;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.Episode;
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

        void showMsgToast(String msg);

        void showPopupRating(float star);

        void showPopupLogin(String textContent);

        void showPopupVip(String textContent);

        void setMovieInfo(Movie movie);

        void setListEpisodes(ArrayList<Episode> listEpisodes);

        void setRelateMovies(ArrayList<Movie> movies);

        void setRatingInfo(int total, float avg);

        void changeStatusLike(boolean isLiked);

        void changeStatusFollow(boolean isFollow);

        void changeBtnClearAdsVisible(boolean visible);

        void addAdsBanner();

        void openActPlayer();

        void openActPlayerYoutube(String videoId);

        void openActSearch();

        void openActBuyVip();

        void openActLogin();
    }

    interface Presenter extends BasePresenter {

        void startLoadDetail(int movId);

        void reLoadDeatail(int movId);

        void watchMovie();

        void watchPreviewEpisode(String urlVideo);

        void resumeWatchMovie();

        void playTrailer();

        void ratingMovie(int point);

        void likeMovie();

        void followMovie();

        void downloadMovie();

        void shareMovie();

        void clearAds();
    }
}
