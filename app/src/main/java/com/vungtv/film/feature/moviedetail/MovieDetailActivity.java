package com.vungtv.film.feature.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.squareup.picasso.Picasso;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.model.Movie;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.util.TimeUtils;
import com.vungtv.film.widget.ExpandableTextView;
import com.vungtv.film.widget.VtvTextView;
import com.vungtv.film.widget.moviesrowview.VtvMoviesRowView;

import java.util.ArrayList;

import butterknife.BindView;

public class MovieDetailActivity extends BaseActivity implements MovieDetailContract.View {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private static final String INTENT_MOVIE_ID = "INTENT_MOVIE_ID";

    @BindView(R.id.activity_movie_details)
    ImageView imgCover;

    @BindView(R.id.mdetails_tv_filmname_1)
    VtvTextView tvMovName1;

    @BindView(R.id.mdetails_tv_filmname_2)
    VtvTextView tvMovName2;

    @BindView(R.id.mdetails_tv_imdb)
    VtvTextView tvIMDB;

    @BindView(R.id.mdetails_tv_imdb_2)
    VtvTextView tvIMDB2;

    @BindView(R.id.mdetails_rating_1)
    RatingBar ratingBar1;

    @BindView(R.id.mdetails_tv_rating_count)
    VtvTextView tvRatingCount;

    @BindView(R.id.mdetails_btn_notify)
    ImageView btnNotify;

    @BindView(R.id.mdetails_btn_like)
    ImageView btnLike;

    @BindView(R.id.mdetails_tv_des)
    ExpandableTextView expanTvDes;

    @BindView(R.id.mdetails_rating_btn)
    RatingBar ratingBar2;

    @BindView(R.id.mdetails_layout_episodes)
    LinearLayout layoutEpisodes;

    @BindView(R.id.mdetails_relate_movies)
    VtvMoviesRowView relateMovies;

    private MovieDetailContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        new MovieDetailPresenter(this, this);
        presenter.startLoadDetail(getIntent().getIntExtra(INTENT_MOVIE_ID, 0));
    }

    /**
     * set intent data;
     *
     * @param intent Intent start activity;
     * @param movieId id of movie;
     * @return Intent;
     */
    public static Intent getIntentData(Intent intent, int movieId) {
        intent.putExtra(INTENT_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    public void showLoadding(boolean show) {
        popupLoading.show(show);
    }

    @Override
    public void showMsgError(boolean show, String error) {

    }

    @Override
    public void setMovieInfo(Movie movie, int totalRating, double avgRating) {
        // set Cover img
        if (StringUtils.isNotEmpty(movie.getMovCover())) {
            Picasso.with(this)
                    .load(movie.getMovCover().replace(" ", ""))
                    .fit()
                    .error(R.drawable.default_poster_land)
                    .into(imgCover);
        }

        // set film name;
        if (StringUtils.isNotEmpty(movie.getMovName())) {
            tvMovName1.setText(movie.getMovName());
        }

        if (StringUtils.isNotEmpty(movie.getMovNameAccent())) {
            tvMovName2.setText(movie.getMovNameAccent());
        }

        // set IMDB
        if (StringUtils.isNotEmpty(movie.getMovScore())) {
            tvMovName2.setText(movie.getMovScore());
        }

        // set rating point
        ratingBar1.setRating((float) avgRating);
        String rateCount = "(" + totalRating + ")";
        tvRatingCount.setText(rateCount);

        // set description;
        expanTvDes.setText(String.format(
                getString(R.string.movie_details_des),
                StringUtils.isNotEmpty(movie.getMovSummary()) ? movie.getMovSummary() : "N/A",
                movie.getMovReleasedDate() > 0 ? TimeUtils.convertTimeStampToDate(movie.getMovReleasedDate()) : "N/A",
                StringUtils.isNotEmpty(movie.getMovDuration()) ? movie.getMovDuration() : "N/A",
                StringUtils.isNotEmpty(movie.getMovWriter()) ? movie.getMovWriter() : "N/A",
                StringUtils.isNotEmpty(movie.getMovDirector()) ? movie.getMovDirector() : "N/A",
                StringUtils.isNotEmpty(movie.getActors()) ? movie.getActors() : "N/A",
                StringUtils.isNotEmpty(movie.getFeatures()) ? movie.getFeatures() : "N/A",
                StringUtils.isNotEmpty(movie.getCountries()) ? movie.getCountries() : "N/A"
                )
        );
    }

    @Override
    public void setRelateMovies(ArrayList<Movie> movies) {
        relateMovies.setDataListView(new ArrayList<Object>(movies));
    }

    @Override
    public void addAdsBanner() {

    }

    @Override
    public void openActPlayer() {

    }

    @Override
    public void openActPlayerYoutube() {

    }

    @Override
    public void openActSearch() {

    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter Presenter) {
        presenter = Presenter;
    }
}
