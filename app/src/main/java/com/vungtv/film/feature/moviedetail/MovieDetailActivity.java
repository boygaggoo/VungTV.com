package com.vungtv.film.feature.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;
import com.vungtv.film.App;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.feature.buyvip.BuyVipActivity;
import com.vungtv.film.feature.login.LoginActivity;
import com.vungtv.film.feature.player.PlayerActivity;
import com.vungtv.film.feature.search.SearchActivity;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.Episode;
import com.vungtv.film.model.Movie;
import com.vungtv.film.popup.PopupMessenger;
import com.vungtv.film.popup.PopupRating;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.util.TextUtils;
import com.vungtv.film.util.TimeUtils;
import com.vungtv.film.widget.ExpandableTextView;
import com.vungtv.film.widget.VtvAutofitMarginDecoration;
import com.vungtv.film.widget.VtvTextView;
import com.vungtv.film.widget.moviesrowview.MoviesRowAdapter;
import com.vungtv.film.widget.moviesrowview.VtvMovieRowView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MovieDetailActivity extends BaseActivity implements MovieDetailContract.View, RatingBar.OnRatingBarChangeListener {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    public static final String INTENT_MOVIE_ID = "INTENT_MOVIE_ID";

    private MovieDetailContract.Presenter presenter;

    private EpisodesRecycerAdapter adapter;

    @BindView(R.id.mdetails_img_cover)
    ImageView imgCover;

    @BindView(R.id.mdetails_tv_filmname_1)
    VtvTextView tvMovName1;

    @BindView(R.id.mdetails_tv_filmname_2)
    VtvTextView tvMovName2;

    @BindView(R.id.mdetails_tv_imdb)
    VtvTextView tvIMDB;

    @BindView(R.id.mdetails_tv_imdb_2)
    VtvTextView tvIMDB2;

    @BindView(R.id.mdetails_layout_type_res)
    LinearLayout layoutTypeRes;

    @BindView(R.id.mdetails_rating_1)
    RatingBar ratingBar1;

    @BindView(R.id.mdetails_tv_rating_count)
    VtvTextView tvRatingCount;

    @BindView(R.id.mdetails_btn_notify)
    ImageView btnNotify;

    @BindView(R.id.mdetails_btn_like)
    ImageView btnLike;

    @BindView(R.id.mdetails_btn_clear_ads)
    ImageView btnClearAds;

    @BindView(R.id.mdetails_tv_des)
    ExpandableTextView expanTvDes;

    @BindView(R.id.mdetails_rating_btn)
    RatingBar ratingBar2;

    @BindView(R.id.mdetails_recycler_eps)
    RecyclerView epsRecyclerView;

    @BindView(R.id.mdetails_relate_movies)
    VtvMovieRowView relateMovies;

    private PopupRating popupRating;

    private PopupMessenger popupMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ratingBar2.setOnRatingBarChangeListener(this);

        epsRecyclerView.addItemDecoration(new VtvAutofitMarginDecoration(this, R.dimen.space_4));
        epsRecyclerView.setHasFixedSize(true);
        adapter = new EpisodesRecycerAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (adapter.getEpisode(pos).getEpsPreview()) {
                    presenter.watchPreviewEpisode(adapter.getEpisode(pos).getEpsHash());
                } else {
                    presenter.watchMovie(adapter.getEpisode(pos).getEpsHash());
                }
            }
        });
        epsRecyclerView.setAdapter(adapter);

        new MovieDetailPresenter(this, this);
        presenter.startLoadDetail(getIntent().getIntExtra(INTENT_MOVIE_ID, 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Movie detail Screen");
    }

    /**
     * set intent data;
     *
     * @param packageContext activity;
     * @param movieId id of movie;
     * @return Intent;
     */
    public static Intent buildIntent(Context packageContext, int movieId) {
        Intent intent = new Intent(packageContext, MovieDetailActivity.class);
        intent.putExtra(INTENT_MOVIE_ID, movieId);
        return intent;
    }

    @OnClick(R.id.mdetails_btn_back)
    public void onBtnBackClick() {
        finish();
    }

    @OnClick(R.id.mdetails_btn_search)
    public void onBtnSearchClick() {
        openActSearch();
    }

    @OnClick(R.id.mdetails_btn_share)
    public void onBtnSharekClick() {
        presenter.shareMovie();
    }

    @OnClick(R.id.mdetails_img_cover)
    public void onImgCoverClick() {
        presenter.resumeWatchMovie();
    }

    @OnClick(R.id.mdetails_img_cover_play)
    public void onImgCoverPlayClick() {
        presenter.resumeWatchMovie();
    }

    @OnClick(R.id.mdetails_btn_like)
    public void onBtnLikeClick() {
        presenter.likeMovie();
    }

    @OnClick(R.id.mdetails_btn_notify)
    public void onBtnFollowClick() {
        presenter.followMovie();
    }

    @OnClick(R.id.mdetails_btn_download)
    public void onBtnDownloadClick() {
        presenter.downloadMovie();
    }

    @OnClick(R.id.mdetails_btn_trailer)
    public void onBtnPlayTrailerClick() {
        presenter.playTrailer();
    }

    @OnClick(R.id.mdetails_btn_clear_ads)
    public void onBtnClearAdsClick() {
        presenter.clearAds();
    }

    @Override
    public void showLoadding(boolean show) {
        popupLoading.show(show);
    }

    @Override
    public void showMsgError(boolean show, String error) {

    }

    @Override
    public void showMsgToast(String msg) {
        showToast(msg);
    }

    @Override
    public void showPopupRating(float star) {
        if (popupRating == null) {
            popupRating = new PopupRating(this);
            popupRating.setOnPopupChangeNameListener(new PopupRating.OnPopupRatingListener() {
                @Override
                public void onSendRating(int star) {
                    presenter.ratingMovie(star);
                }
            });
        }

        popupRating.show();
        popupRating.setRating(star);
    }

    @Override
    public void showPopupLogin(String textContent) {
        if (popupMessenger == null) {
            popupMessenger = new PopupMessenger(this);
        }
        popupMessenger.setOnPopupMessengerListener(new PopupMessenger.OnPopupMessengerListener() {
            @Override
            public void onPopupMsgBtnConfirmClick() {
                openActLogin();
            }

            @Override
            public void onPopupMsgBtnCancelClick() {

            }
        });
        popupMessenger.show();
        popupMessenger.setTextContent(textContent);
        popupMessenger.setTextBtnConfirm(getString(R.string.popup_action_login));
    }

    @Override
    public void showPopupVip(String textContent) {
        if (popupMessenger == null) {
            popupMessenger = new PopupMessenger(this);
        }
        popupMessenger.setOnPopupMessengerListener(new PopupMessenger.OnPopupMessengerListener() {
            @Override
            public void onPopupMsgBtnConfirmClick() {
                openActBuyVip();
            }

            @Override
            public void onPopupMsgBtnCancelClick() {

            }
        });
        popupMessenger.show();
        popupMessenger.setTextContent(textContent);
        popupMessenger.setTextBtnConfirm(getString(R.string.popup_action_buy_vip));
    }

    @Override
    public void setMovieInfo(Movie movie) {
        // set Cover img
        if (StringUtils.isNotEmpty(movie.getMovCover())) {
            Picasso.with(this)
                    .load(movie.getMovCover().replace(" ", ""))
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.default_poster_land)
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
            tvIMDB.setText(movie.getMovScore());
        }

        // set Type res
        if (StringUtils.isNotEmpty(movie.getMovTypeRes())) {
            layoutTypeRes.removeAllViews();
            String[] typeRes = movie.getMovTypeRes().split(",");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.space_7);

            for (String type : typeRes) {
                int icon = 0;
                if (type.equalsIgnoreCase("HD")) {
                    icon = R.drawable.icon_hd;
                } else if (type.equalsIgnoreCase("SD")) {
                    icon = R.drawable.icon_sd;
                } else if (type.equalsIgnoreCase("LT")) {
                    icon = R.drawable.icon_lt;
                } else if (type.equalsIgnoreCase("TM")) {
                    icon = R.drawable.icon_tm;
                }

                if (icon != 0) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(icon);
                    layoutTypeRes.addView(imageView, layoutParams);
                }
            }
        }

        // set description;
        String textDes = String.format(
                getString(R.string.movie_details_des),
                StringUtils.isNotEmpty(movie.getMovSummary()) ? movie.getMovSummary() : "N/A",
                movie.getMovReleasedDate() > 0 ? TimeUtils.convertTimeStampToDate(movie.getMovReleasedDate()) : "N/A",
                StringUtils.isNotEmpty(movie.getMovDuration()) ? movie.getMovDuration() : "N/A",
                StringUtils.isNotEmpty(movie.getMovWriter()) ? movie.getMovWriter() : "N/A",
                StringUtils.isNotEmpty(movie.getMovDirector()) ? movie.getMovDirector() : "N/A",
                StringUtils.isNotEmpty(movie.getActors()) ? movie.getActors() : "N/A",
                StringUtils.isNotEmpty(movie.getFeatures()) ? movie.getFeatures() : "N/A",
                StringUtils.isNotEmpty(movie.getCountries()) ? movie.getCountries() : "N/A"
        );

        expanTvDes.setText(TextUtils.styleTextHtml(textDes));
    }

    @Override
    public void setListEpisodes(ArrayList<Episode> listEpisodes) {
        adapter.addAll(listEpisodes);
    }

    @Override
    public void setRelateMovies(ArrayList<Movie> movies) {
        LogUtils.d(TAG, "setRelateMovies: " + movies.size());
        relateMovies.setListAdapter(new ArrayList<Object>(movies));
        relateMovies.setButtonViewMoreVisible(false);
        relateMovies.setOnItemClickListener(new MoviesRowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int movieId) {
                openActMovieDetail(movieId);
            }
        });
    }

    @Override
    public void changeStatusLike(boolean isLiked) {
        if (isLiked) {
            btnLike.setImageResource(R.drawable.icon_heart2);
        } else {
            btnLike.setImageResource(R.drawable.icon_heart1);
        }
    }

    @Override
    public void changeStatusFollow(boolean isFollow) {
        if (isFollow) {
            btnNotify.setImageResource(R.drawable.icon_bell2);
        } else {
            btnNotify.setImageResource(R.drawable.icon_bell1);
        }
    }

    @Override
    public void changeBtnClearAdsVisible(boolean visible) {
        btnClearAds.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRatingInfo(int total, float avg) {
        // set rating point
        ratingBar1.setRating(avg);
        String rateCount = String.format("(%s)", String.valueOf(total));
        tvRatingCount.setText(rateCount);
    }

    @Override
    public void addAdsBanner() {

    }

    @Override
    public void openActMovieDetail(int movId) {
        Intent intent = getIntent();
        intent.putExtra(INTENT_MOVIE_ID, movId);
        finish();
        startActivity(intent);
    }

    @Override
    public void openActPlayer(int movId, String movName, String movCover, String epsHash) {
        Intent intent = PlayerActivity.buildIntent(this, movId, movName, movCover, epsHash);
        startActivity(intent);
    }

    @Override
    public void openActPlayerRecent(int movId, String movName, String movCover, String epsHash) {
        Intent intent = PlayerActivity.buildIntentRecent(this, movId, movName, movCover, epsHash);
        startActivity(intent);
    }

    @Override
    public void openActPlayerYoutube(String videoId) {
        String devKey = getString(R.string.yooutube_dev_key);
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, devKey, videoId);
        startActivity(intent);
    }

    @Override
    public void openActSearch() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void openActBuyVip() {
        startActivity(new Intent(this, BuyVipActivity.class));
    }

    @Override
    public void openActLogin() {
        startActivity(new Intent(MovieDetailActivity.this, LoginActivity.class));
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter Presenter) {
        presenter = Presenter;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        showPopupRating(v);
    }
}
