package com.vungtv.film.feature.player;

import android.content.Intent;
import android.net.Uri;

import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.DrmSession;
import com.vungtv.film.model.Episode;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * Content class
 * <p>
 * Created by Mr Cuong on 3/4/2017.
 * Email: vancuong2941989@gmail.com
 */
public interface PlayerContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(boolean show, String msg);

        void showMsgToast(String msg);

        void showPopupSelectVersion(String[] labels, int curSelected);

        void showPopupListEpisodes(ArrayList<Episode> list, String epsTitle);

        void setMediaSource(Uri uri, String extension);

        void setDrmSession(DrmSession drmSession);

        void setVideoName(String text);

        void setBtnNextPrevEnable(boolean nextEnable, boolean prevEnable);

        void setBtnVersionEnable(boolean enable);

        void setBtnPlaylistEnable(boolean enable);

        void initPlayer();

        void releasePlayer();

        void updateResumePosition();

        void clearResumePosition();

        void updateButtonsVisible();

        MediaSource buildMediaSource(Uri uri, String overrideExtension);

        DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(
                UUID uuid, String licenseUrl, Map<String, String> keyRequestProperties) throws UnsupportedDrmException;

        DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter);

        HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter);
    }

    interface Presenter extends BasePresenter {

        void getIntent(Intent intent);

        void loadEpisodeInfo();

        void reloadEpisodeInfo();

        void retryPlayer();

        void nextEpisode();

        void prevEpisode();

        void openPopupSelectVersion();

        void openPopupListEpisodes();

        void selectedVersion(int position);
    }
}
