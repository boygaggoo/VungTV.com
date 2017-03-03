package com.vungtv.film.feature.moviedetail;

import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.model.ApiEpisodes;
import com.vungtv.film.data.source.remote.model.ApiMovieDetail;
import com.vungtv.film.data.source.remote.service.MovieDetailServices;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.util.UriPaser;

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

    private boolean isFollowed = false;

    private String trailerVideoId;


    public MovieDetailPresenter(Context context, MovieDetailContract.View activityView) {
        this.context = context;
        this.activityView = activityView;
        this.activityView.setPresenter(this);

        movieDetailServices = new MovieDetailServices(context);

        movieDetailServices.setMovieDetailResultCallback(this);
    }

    @Override
    public void startLoadDetail(int movId) {
        String token = UserSessionManager.getAccessToken(context.getApplicationContext());
        if (token == null) token = "";
        activityView.showLoadding(true);
        movieDetailServices.setMovId(movId);
        movieDetailServices.loadInfo(token);
        movieDetailServices.loadListEpisodes();
    }

    @Override
    public void reLoadDeatail(int movId) {

    }

    @Override
    public void watchMovie() {

    }

    @Override
    public void watchPreviewEpisode(String urlVideo) {
        String videoId = UriPaser.getYoutubeVideoId(urlVideo);
        if (videoId == null) {
            activityView.showMsgToast(context.getString(R.string.movie_details_error_preview));
            return;
        }
        activityView.openActPlayerYoutube(videoId);
    }

    @Override
    public void resumeWatchMovie() {

    }

    @Override
    public void playTrailer() {
        if (trailerVideoId == null) {
            activityView.showMsgToast(context.getString(R.string.movie_details_error_trailer));
            return;
        }

        activityView.openActPlayerYoutube(trailerVideoId);
    }

    @Override
    public void ratingMovie(int point) {
        activityView.showLoadding(true);
        movieDetailServices.ratingMovie(point);
    }

    @Override
    public void likeMovie() {
        String token = UserSessionManager.getAccessToken(context.getApplicationContext());
        if (StringUtils.isEmpty(token)) {
            activityView.showPopupLogin(context.getResources().getString(R.string.movie_details_error_like));
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
            activityView.showPopupLogin(context.getResources().getString(R.string.movie_details_error_follow));
            return;
        }

        activityView.showLoadding(true);
        if (isFollowed) {
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
        if (!UserSessionManager.isLogin(context.getApplicationContext())) {
            activityView.showPopupLogin(context.getResources().getString(R.string.movie_details_error_login));
        } else {
            activityView.showPopupVip(context.getResources().getString(R.string.movie_details_text_clear_ads));
        }
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
        activityView.setMovieInfo(data.movie);
        activityView.setRatingInfo(data.rating.total, data.rating.avg);
        activityView.setRelateMovies(data.relateMovies);

        trailerVideoId = UriPaser.getYoutubeVideoId(data.movie.getMovTrailer());

        if (data.movieUserStatus != null) {
            isLiked = data.movieUserStatus.like;
            isFollowed = data.movieUserStatus.follow;
        }
        activityView.changeStatusLike(isLiked);
        activityView.changeStatusFollow(isFollowed);

        activityView.changeBtnClearAdsVisible(
                !UserSessionManager.isVIP(context.getApplicationContext()));
    }

    @Override
    public void onEpisodeResultSuccess(ApiEpisodes.Data data) {
        activityView.showLoadding(false);

        if (data.getEpisodes() != null && data.getEpisodes().size() > 0) {
            activityView.setListEpisodes(data.getEpisodes());
        }
    }

    @Override
    public void onLikeMovieSuccess(String mes) {
        activityView.showLoadding(false);
        activityView.showMsgToast(mes);

        isLiked = !isLiked;
        activityView.changeStatusLike(isLiked);
    }

    @Override
    public void onFollowMovieSuccess(String mes) {
        activityView.showLoadding(false);
        activityView.showMsgToast(mes);

        isFollowed = !isFollowed;
        activityView.changeStatusFollow(isFollowed);
    }

    @Override
    public void onRatingMovieSuccess(String msg, int total, float avg) {
        activityView.showLoadding(false);
        activityView.showMsgToast(msg);
        activityView.setRatingInfo(total, avg);
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
