package com.vungtv.film.feature.moviedetail;

import android.content.Context;

import com.vungtv.film.data.source.remote.model.ApiMovieDetail;
import com.vungtv.film.data.source.remote.service.MovieDetailServices;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/2/2017.
 * Email: vancuong2941989@gmail.com
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter, MovieDetailServices.MovieDetailResultCallback {
    private static final String TAG = MovieDetailPresenter.class.getSimpleName();

    private final Context context;

    private final MovieDetailContract.View activityView;

    private final MovieDetailServices movieDetailServices;


    public MovieDetailPresenter(Context context, MovieDetailContract.View activityView) {
        this.context = context;
        this.activityView = activityView;
        this.activityView.setPresenter(this);

        movieDetailServices = new MovieDetailServices(context);

        movieDetailServices.setMovieDetailResultCallback(this);
    }

    @Override
    public void startLoadDetail(int movId) {
        activityView.showLoadding(true);
        movieDetailServices.loadInfo(movId);
    }

    @Override
    public void reLoadDeatail(int movId) {

    }

    @Override
    public void watchMovie() {

    }

    @Override
    public void playTrailer() {

    }

    @Override
    public void ratingMovie(int point) {

    }

    @Override
    public void likeMovie() {

    }

    @Override
    public void unLikeMovie() {

    }

    @Override
    public void followMovie() {

    }

    @Override
    public void unFollowMovie() {

    }

    @Override
    public void shareMovie() {

    }

    @Override
    public void closeAds() {

    }

    @Override
    public void start() {

    }

    @Override
    public void onDestroy() {
        movieDetailServices.cancel();
    }

    @Override
    public void onMovieInfoResultSuccess(ApiMovieDetail.Data data) {
        activityView.showLoadding(false);
        activityView.setMovieInfo(data.movie, data.rating.total, data.rating.avg);
    }

    @Override
    public void onEpisodeResultSuccess() {

    }

    @Override
    public void onFailure(int code, String error) {
        activityView.showLoadding(false);
    }
}
