package com.vungtv.film.feature.player;

import android.content.Context;
import android.content.Intent;

import com.vungtv.film.App;
import com.vungtv.film.R;
import com.vungtv.film.data.source.local.MovieRecentManager;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.model.ApiEpisodes;
import com.vungtv.film.data.source.remote.model.ApiMoviePlayer;
import com.vungtv.film.data.source.remote.service.EpisodeServices;
import com.vungtv.film.data.source.remote.service.PlayerServices;
import com.vungtv.film.model.Episode;
import com.vungtv.film.model.MovieRecent;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.util.TimeUtils;

import java.util.ArrayList;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/4/2017.
 * Email: vancuong2941989@gmail.com
 */

public class PlayerPresenter implements PlayerContract.Presenter, PlayerServices.OnPlayerServicesCallback, EpisodeServices.EpisodeResultCallback {

    private static final String TAG = PlayerPresenter.class.getSimpleName();

    private final Context context;

    private final PlayerContract.View playerView;

    private final PlayerServices mPlayerServices;

    private final EpisodeServices episodeServices;

    private final MovieRecentManager movieRecentManager;

    private ApiMoviePlayer.Data videoData;

    private ArrayList<Episode> listEps;

    private int movId;

    private String movName;

    private String epsHash;

    private boolean isRecent = false;

    private boolean isLoadDrive = true;

    private int watchVideoVer = 0;

    public PlayerPresenter(Context context, PlayerContract.View playerView) {
        this.context = context;
        this.playerView = playerView;
        this.playerView.setPresenter(this);

        mPlayerServices = new PlayerServices(context.getApplicationContext());
        mPlayerServices.setOnPlayerServicesCallback(this);

        episodeServices = new EpisodeServices(context);
        episodeServices.setEpisodeResultCallback(this);

        movieRecentManager = new MovieRecentManager();
    }

    @Override
    public void getIntent(Intent intent) {
        if (intent == null) {
            playerView.showMsgError(true, context.getString(R.string.player_error_msg_no_data));
            return;
        }

        movId = intent.getIntExtra(PlayerActivity.INTENT_MOV_ID, -1);
        movName = intent.getStringExtra(PlayerActivity.INTENT_MOV_NAME);
        epsHash = intent.getStringExtra(PlayerActivity.INTENT_EPS_HASH);
        isRecent = intent.getBooleanExtra(PlayerActivity.INTENT_RECENT, false);
        if (isRecent) {
            App.getInstance().trackEvent("Player", "Play movie", "Play movie from recent.");
        } else {
            App.getInstance().trackEvent("Player", "Play movie", "Play movie from list episodes.");
        }
    }

    @Override
    public void loadEpisodeInfo() {
        if (movId < 0 || StringUtils.isEmpty(epsHash)) {
            playerView.showMsgError(true, context.getString(R.string.player_error_msg_no_data));
            return;
        }

        playerView.showLoading(true);
        mPlayerServices.loadEpisodeInfo(movId, epsHash);
    }

    @Override
    public void reloadEpisodeInfo() {
        loadEpisodeInfo();
    }

    @Override
    public void retryPlayWithHLS() {
        if (videoData != null) {
            isLoadDrive = false;

            if (videoData.player.get(watchVideoVer) != null) {
                setMediaSource(watchVideoVer);
            } else {
                setMediaSource(0);
            }
            playerView.initPlayer();
        }
    }

    @Override
    public void retryPlayer() {
        playerView.initPlayer();
    }

    @Override
    public void nextEpisode() {
        playerView.releasePlayer();
        playerView.clearResumePosition();
        epsHash = videoData.next;
        playerView.showLoading(true);
        mPlayerServices.loadEpisodeInfo(movId, epsHash);
    }

    @Override
    public void prevEpisode() {
        playerView.releasePlayer();
        playerView.clearResumePosition();
        epsHash = videoData.previous;
        playerView.showLoading(true);
        mPlayerServices.loadEpisodeInfo(movId, epsHash);
    }

    @Override
    public void openPopupSelectVersion() {
        if (videoData == null || videoData.player == null || videoData.player.size() == 0) {
            playerView.showMsgToast(context.getString(R.string.player_error_msg_no_data));
            return;
        }

        String[] labels = new String[videoData.player.size()];
        for (int i = 0; i < videoData.player.size(); i++) {
            labels[i] = videoData.player.get(i).label;
        }

        playerView.showPopupSelectVersion(labels, labels[watchVideoVer] != null ? watchVideoVer : 0);
    }

    @Override
    public void openPopupListEpisodes() {
        if (movId <= 0) return;

        if (listEps != null) {
            playerView.showPopupListEpisodes(listEps, videoData != null ? videoData.epsTitle : "");
            return;
        }
        playerView.showLoading(true);
        episodeServices.loadListEpisodes(movId);
    }

    @Override
    public void openPopupClearAds() {
        if (UserSessionManager.isLogin(context.getApplicationContext())) {
            playerView.showPopupBuyVip();
        } else {
            playerView.showPopupLogin();
        }
    }

    @Override
    public void playFromTheLast() {
        MovieRecent movieRecent = movieRecentManager.get(movId);
        if (movieRecent != null && StringUtils.isNotEmpty(movieRecent.getMovEpsHash())) {
            epsHash = movieRecent.getMovEpsHash();
            playerView.setResumePosition(movieRecent.getMovLastPlay());
        }
        loadEpisodeInfo();
    }

    @Override
    public void playFromTheBeginning() {
        loadEpisodeInfo();
    }

    @Override
    public void selectedVersion(int position) {
        if (position != watchVideoVer) {
            playerView.releasePlayer();
            watchVideoVer = position;
            setMediaSource(watchVideoVer);
            playerView.initPlayer();
        }
    }

    @Override
    public void selectedItemListEps(String epsHash1) {
        LogUtils.d(TAG, "selectedItemListEps: epsHash = " + epsHash);
        this.epsHash = epsHash1;

        playerView.releasePlayer();
        playerView.clearResumePosition();
        playerView.showLoading(true);
        mPlayerServices.loadEpisodeInfo(movId, epsHash);
    }

    @Override
    public void accountModify() {
        if (UserSessionManager.isVIP(context.getApplicationContext())) {
            playerView.setBtnClearAdsEnable(false);
        } else {
            playerView.setBtnClearAdsEnable(true);
        }
    }

    @Override
    public void saveMovieRecent(long position, long duration) {
        MovieRecent movieRecent = new MovieRecent(
                movId, movName, null, epsHash, duration, position, TimeUtils.getCurrentTimeMillis()
        );
        movieRecentManager.addOrUpdate(movieRecent);
    }

    @Override
    public void shareEpisode() {
        if (videoData == null || StringUtils.isEmpty(videoData.shareLink)) {
            return;
        }
        playerView.showPopupShare(videoData.shareLink);
    }

    @Override
    public void countPlayed() {
        mPlayerServices.countPlay(movId,
                UserSessionManager.getAccessToken(context.getApplicationContext()));
    }

    @Override
    public void start() {
        if (UserSessionManager.isVIP(context.getApplicationContext())) {
            playerView.setBtnClearAdsEnable(false);
        } else {
            playerView.setBtnClearAdsEnable(true);
        }

        if (isRecent) {
            MovieRecent movieRecent = movieRecentManager.get(movId);
            if (movieRecent != null && movieRecent.getMovLastPlay() > 0) {
                playerView.showPopupRecent();
                return;
            }
        }
        loadEpisodeInfo();
    }

    @Override
    public void onDestroy() {
        mPlayerServices.cancel();
        movieRecentManager.close();
    }

    @Override
    public void onFailure(int code, String error) {
        playerView.showLoading(false);
        playerView.showMsgError(true, error);
    }

    @Override
    public void onLoadEpisodeInfoSuccess(ApiMoviePlayer.Data data) {
        playerView.showLoading(false);

        videoData = data;

        if (videoData.player == null || videoData.player.size() == 0) {
            playerView.showMsgError(true, context.getString(R.string.player_error_msg_no_data));
            return;
        }

        if (videoData.player.get(watchVideoVer) != null) {
            setMediaSource(watchVideoVer);
        } else {
            setMediaSource(0);
        }

        playerView.initPlayer();
        playerView.setVideoName(
                String.format(context.getString(R.string.player_text_video_name),
                        videoData.epsTitle,
                        movName
                ));

        boolean isNext = StringUtils.isNotEmpty(videoData.next);
        boolean isPrev = StringUtils.isNotEmpty(videoData.previous);

        playerView.setBtnNextPrevEnable(isNext, isPrev);
        playerView.setBtnPlaylistEnable(isNext || isPrev);
        playerView.setBtnVersionEnable(videoData.player.size() > 1);
    }

    @Override
    public void onEpisodeResultSuccess(ApiEpisodes.Data data) {
        playerView.showLoading(false);
        if (data.getEpisodes() != null && data.getEpisodes().size() > 0 && videoData != null) {
            listEps = data.getEpisodes();
            playerView.showPopupListEpisodes(listEps, videoData.epsTitle);
        } else {
            playerView.showMsgToast(context.getString(R.string.player_error_msg_no_data));
        }
    }

    @Override
    public void onEpisodeResultFailed(String msg) {
        playerView.showLoading(false);
        playerView.showMsgToast(msg);
    }

    /**
     * Build the video source from data;
     *
     * @param pos the index of list;
     */
    private void setMediaSource(int pos) {

        if (isLoadDrive && videoData.player.get(pos).files.size() > 1) {
            // Play video MP4
            LogUtils.d(TAG, videoData.player.get(pos).label + " - MP4");
            playerView.setMediaSource(
                    videoData.player.get(pos).files.get(1).getUri(),
                    videoData.player.get(pos).files.get(1).getExtension());
        } else {
            // Play video HLS
            LogUtils.d(TAG, videoData.player.get(pos).label + " - HLS");
            playerView.setMediaSource(
                    videoData.player.get(pos).files.get(0).getUri(),
                    videoData.player.get(pos).files.get(0).getExtension());
        }
    }
}
