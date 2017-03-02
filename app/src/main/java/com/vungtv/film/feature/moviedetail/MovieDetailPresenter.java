package com.vungtv.film.feature.moviedetail;

import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.model.ApiMovieDetail;
import com.vungtv.film.data.source.remote.service.MovieDetailServices;
import com.vungtv.film.util.StringUtils;

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

    private boolean isLiked = false;

    private boolean isFollow = false;


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
        movieDetailServices.setMovId(movId);
        movieDetailServices.loadInfo();
    }

    @Override
    public void reLoadDeatail(int movId) {

    }

    @Override
    public void watchMovie() {

    }

    @Override
    public void resumeWatchMovie() {

    }

    @Override
    public void playTrailer() {

    }

    @Override
    public void ratingMovie(int point) {

    }

    @Override
    public void likeMovie() {
        String token = UserSessionManager.getAccessToken(context.getApplicationContext());
        if (StringUtils.isEmpty(token)) {
            activityView.showMsgToast(context.getResources().getString(R.string.movie_details_error_like));
            return;
        }

        activityView.showLoadding(true);
        if (isLiked) {
            movieDetailServices.likeMovie("unlike", token);
        } else {
            movieDetailServices.likeMovie("like", token);
        }
    }

    @Override
    public void followMovie() {
        String token = UserSessionManager.getAccessToken(context.getApplicationContext());
        if (StringUtils.isEmpty(token)) {
            activityView.showMsgToast(context.getResources().getString(R.string.movie_details_error_follow));
            return;
        }

        activityView.showLoadding(true);
        if (isFollow) {
            movieDetailServices.followMovie("unfollow", token);
        } else {
            movieDetailServices.followMovie("follow", token);
        }
    }

    @Override
    public void downloadMovie() {
        activityView.showMsgToast(context.getResources().getString(R.string.movie_details_error_download));
    }

    @Override
    public void shareMovie() {

    }

    @Override
    public void clearAds() {

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
        activityView.setRelateMovies(data.relateMovies);
    }

    @Override
    public void onEpisodeResultSuccess() {

    }

    @Override
    public void onLikeMovieSuccess(boolean sucess, String mes) {
        activityView.showLoadding(false);
        activityView.showMsgToast(mes);

        if (sucess) {
            isLiked = !isLiked;
            activityView.changeStatusLike(isLiked);
        }
    }

    @Override
    public void onFollowMovieSuccess(boolean sucess, String mes) {
        activityView.showLoadding(false);
        activityView.showMsgToast(mes);

        if (sucess) {
            isFollow = !isFollow;
            activityView.changeStatusFollow(isFollow);
        }
    }

    @Override
    public void onActionChangeFailed(String mes) {
        activityView.showLoadding(false);
        activityView.showMsgToast(mes);
    }

    @Override
    public void onFailure(int code, String error) {
        activityView.showLoadding(false);
    }
}
