package com.vungtv.film.feature.player;

import android.content.Context;
import android.content.Intent;

import com.vungtv.film.data.source.remote.model.ApiMoviePlayer;
import com.vungtv.film.data.source.remote.service.PlayerServices;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.StringUtils;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/4/2017.
 * Email: vancuong2941989@gmail.com
 */

public class PlayerPresenter implements PlayerContract.Presenter, PlayerServices.OnPlayerServicesCallback {
    private static final String TAG = PlayerPresenter.class.getSimpleName();

    private static final int WATCH_PHUDE = 0;

    private static final int WATCH_THUYETMINH = 1;

    private final Context context;

    private final PlayerContract.View playerView;

    private final PlayerServices mPlayerServices;

    private ApiMoviePlayer.Data videoData;

    private int movId;

    private String movName;

    private String epsHash;

    private int watchVideoVer = WATCH_PHUDE;

    public PlayerPresenter(Context context, PlayerContract.View playerView) {
        this.context = context;
        this.playerView = playerView;
        this.playerView.setPresenter(this);

        mPlayerServices = new PlayerServices(context.getApplicationContext());
        mPlayerServices.setOnPlayerServicesCallback(this);
    }

    @Override
    public void getIntent(Intent intent) {
        if (intent == null) {
            playerView.showMsgError(true, "Ko có dữ liệu.");
            return;
        }

        movId = intent.getIntExtra(PlayerActivity.INTENT_MOV_ID, -1);
        movName = intent.getStringExtra(PlayerActivity.INTENT_MOV_NAME);
        epsHash = intent.getStringExtra(PlayerActivity.INTENT_EPS_HASH);
    }

    @Override
    public void loadEpisodeInfo() {
        if (movId < 0 || StringUtils.isEmpty(epsHash)) {
            playerView.showMsgError(true, "Ko có dữ liệu.");
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
    public void retryPlayer() {
        playerView.initPlayer();
    }

    @Override
    public void start() {

    }

    @Override
    public void onDestroy() {
        mPlayerServices.cancel();
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

        if (watchVideoVer == WATCH_THUYETMINH
                && videoData.player.thuyetMinh != null && videoData.player.thuyetMinh.size() > 0) {
            // Play video pthuet minh
            if (videoData.player.thuyetMinh.size() > 1) {
                LogUtils.d(TAG, "Play thuyet minh - MP4");
                playerView.setMediaSource(
                        videoData.player.thuyetMinh.get(1).getUri(),
                        videoData.player.thuyetMinh.get(1).getExtension());
            } else {
                LogUtils.d(TAG, "Play thuyet minh - HLS");
                playerView.setMediaSource(
                        videoData.player.thuyetMinh.get(0).getUri(),
                        videoData.player.thuyetMinh.get(0).getExtension());
            }
        } else {
            // Play video phu de
            if (videoData.player.phuDe.size() > 1) {
                LogUtils.d(TAG, "Play PHU DE - MP4");
                playerView.setMediaSource(
                        videoData.player.phuDe.get(1).getUri(),
                        videoData.player.phuDe.get(1).getExtension());
            } else {
                LogUtils.d(TAG, "Play PHU DE - HLS");
                playerView.setMediaSource(
                        videoData.player.phuDe.get(0).getUri(),
                        videoData.player.phuDe.get(0).getExtension());
            }
        }

        playerView.initPlayer();
        playerView.setVideoName(movName);
    }
}
