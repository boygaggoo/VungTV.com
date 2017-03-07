package com.vungtv.film.feature.player;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.vungtv.film.App;
import com.vungtv.film.R;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.feature.buyvip.BuyVipActivity;
import com.vungtv.film.feature.login.LoginActivity;
import com.vungtv.film.model.DrmSession;
import com.vungtv.film.model.Episode;
import com.vungtv.film.popup.PopupListEpisode;
import com.vungtv.film.popup.PopupMessenger;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.widget.player.TrackSelectionHelper;
import com.vungtv.film.widget.player.VersionSelectionHelper;
import com.vungtv.film.widget.player.VtvPlaybackControlView;
import com.vungtv.film.widget.player.VtvPlayerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;

public class PlayerActivity extends AppCompatActivity implements PlayerContract.View, ExoPlayer.EventListener, VtvPlaybackControlView.VisibilityListener, VtvPlayerView.OnScreenBrightControl, VtvPlaybackControlView.OnControlButtonClickListener, VtvPlaybackControlView.OnControlButtonNextPrevClickListener {

    private static final String TAG = PlayerActivity.class.getSimpleName();
    public static final String INTENT_MOV_ID = "INTENT_MOV_ID";
    public static final String INTENT_MOV_NAME = "INTENT_MOV_NAME";
    public static final String INTENT_EPS_HASH = "INTENT_EPS_HASH";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    @BindView(R.id.player_view)
    VtvPlayerView vtvPlayerView;

    @BindView(R.id.player_btn_retry)
    TextView btnRetry;

    private PlayerContract.Presenter presenter;

    private View mDecorView;

    private VtvPlaybackControlView vtvControlView;
    private Handler mainHandler;
    private EventLogger eventLogger;

    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private TrackSelectionHelper trackSelectionHelper;
    private VersionSelectionHelper versionSelectionHelper;

    private PopupListEpisode popupListEpisode;

    private MediaSource mediaSource;

    private boolean playerNeedsSource;
    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;

    private DrmSession drmSession;

    public static Intent buildIntent(Context context, int movId, String movName, String epsHash) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(INTENT_MOV_ID, movId);
        intent.putExtra(INTENT_MOV_NAME, movName);
        intent.putExtra(INTENT_EPS_HASH, epsHash);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        hideSystemUI();

        shouldAutoPlay = true;
        clearResumePosition();
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        vtvPlayerView.setAudioManager((AudioManager) getSystemService(Context.AUDIO_SERVICE));
        vtvPlayerView.setControllerVisibilityListener(this);
        vtvPlayerView.setOnScreenBrightControl(this);
        vtvPlayerView.requestFocus();
        vtvControlView = vtvPlayerView.getController();
        vtvControlView.setVisibilityListener(this);
        vtvControlView.setOnControlButtonClickListener(this);
        vtvControlView.setOnControlButtonNextPrevClickListener(this);

        new PlayerPresenter(this, this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        shouldAutoPlay = true;
        clearResumePosition();
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mediaSource == null) {
            presenter.getIntent(getIntent());
            presenter.loadEpisodeInfo();
        }else if (Util.SDK_INT > 23) {
            initPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaSource == null) return;
        if ((Util.SDK_INT <= 23 || player == null)) {
            initPlayer();
        }
    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.player_root)
    public void onRootViewClick() {

    }

    @OnClick(R.id.player_btn_retry)
    public void onBtnRetryClick() {
        if (mediaSource != null) {
            initPlayer();
        } else {
            presenter.reloadEpisodeInfo();
        }
    }

    @Subscribe
    public void accountModifyEvent(AccountModifyEvent event) {
        // User update info
        // If user buy vip success => hide button clear vip and remove all ads;
        presenter.accountModify();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initPlayer();
        } else {
            showToast(R.string.storage_permission_denied);
            finish();
        }
    }

    @Override
    public void setPresenter(PlayerContract.Presenter Presenter) {
        presenter = checkNotNull(Presenter);
    }

    @Override
    public void showLoading(boolean show) {
        vtvPlayerView.showLoading(show);
    }

    @Override
    public void showMsgError(boolean show, String msg) {
        if (show) {
            btnRetry.setText(msg);
        }
        btnRetry.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMsgToast(String msg) {
        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPopupSelectVersion(String[] labels, int curSelected) {
        if (versionSelectionHelper == null) {
            versionSelectionHelper = new VersionSelectionHelper(this);
            versionSelectionHelper.setOnItemSelectedListener(new VersionSelectionHelper.OnItemSelectedListener() {
                @Override
                public void onItemVersionSelected(int pos) {
                    presenter.selectedVersion(pos);
                }
            });
        }

        versionSelectionHelper.show(labels, curSelected);
    }

    @Override
    public void showPopupListEpisodes(ArrayList<Episode> list, String epsTitle) {
        if (popupListEpisode == null) {
            popupListEpisode = new PopupListEpisode(this);
            popupListEpisode.setOnPopupListEpisodeListener(new PopupListEpisode.OnPopupListEpisodeListener() {
                @Override
                public void onItemPopupEpisodeClicked(String epsHash) {
                    presenter.selectedItemListEps(epsHash);
                    LogUtils.d(TAG, "showPopupListEpisodes: epsHash = " + epsHash);
                }
            });
        }

        popupListEpisode.setListEpisodes(list, epsTitle);
        popupListEpisode.show();
    }

    @Override
    public void showPopupLogin() {
        PopupMessenger popupMessenger = new PopupMessenger(this);
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
        popupMessenger.setTextContent(getString(R.string.movie_details_error_login));
        popupMessenger.setTextBtnConfirm(getString(R.string.popup_action_login));
    }

    @Override
    public void showPopupBuyVip() {
        PopupMessenger popupMessenger = new PopupMessenger(this);
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
        popupMessenger.setTextContent(getString(R.string.movie_details_text_clear_ads));
        popupMessenger.setTextBtnConfirm(getString(R.string.popup_action_buy_vip));
    }

    @Override
    public void setMediaSource(Uri uri, String extension) {
        mediaSource = buildMediaSource(uri, extension);
        LogUtils.d(TAG, "setMediaSource: uri : " + uri + "\nextension : " + extension );
    }

    @Override
    public void setDrmSession(DrmSession drmSession) {
        this.drmSession = drmSession;
    }

    @Override
    public void setVideoName(String text) {
        vtvControlView.setTitle(text);
    }

    @Override
    public void setBtnNextPrevEnable(boolean nextEnable, boolean prevEnable) {
        vtvControlView.setNextButtonVisible(nextEnable);
        vtvControlView.setPreviousButtonVisible(prevEnable);
    }

    @Override
    public void setBtnVersionEnable(boolean enable) {
        vtvControlView.setBtnVersionEnabled(enable);
    }

    @Override
    public void setBtnPlaylistEnable(boolean enable) {
        vtvControlView.setBtnPlaylistEnabled(enable);
    }

    @Override
    public void setBtnClearAdsEnable(boolean enable) {
        vtvControlView.setBtnClearAdsEnabled(enable);
    }

    @Override
    public void initPlayer() {
        LogUtils.d(TAG, "initPlayer.");
        if (player == null) {
            boolean preferExtensionDecoders =
                    drmSession != null && drmSession.isPreferExtensionDecoders();
            UUID drmSchemeUuid =
                    drmSession != null ? UUID.fromString(drmSession.getDrmSchemeUUIDExtra()) : null;
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;

            if (drmSchemeUuid != null) {
                Map<String, String> keyRequestProperties;

                if (drmSession.getDrmKeyRequestProperties() == null
                        || drmSession.getDrmKeyRequestProperties().length < 2) {
                    keyRequestProperties = null;
                } else {
                    keyRequestProperties = new HashMap<>();
                    for (int i = 0; i < drmSession.getDrmKeyRequestProperties().length - 1; i += 2) {
                        keyRequestProperties.put(drmSession.getDrmKeyRequestProperties()[i],
                                drmSession.getDrmKeyRequestProperties()[i + 1]);
                    }
                }

                try {
                    drmSessionManager = buildDrmSessionManager(
                            drmSchemeUuid, drmSession.getDrmLicenseUrl(), keyRequestProperties);
                } catch (UnsupportedDrmException e) {

                    int errorStringId = Util.SDK_INT < 18 ? R.string.error_drm_not_supported
                            : (e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                            ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown);

                    showToast(errorStringId);
                    return;
                }
            }

            @SimpleExoPlayer.ExtensionRendererMode
            int extensionRendererMode =
                    ((App) getApplication()).useExtensionRenderers()
                            ? (preferExtensionDecoders ? SimpleExoPlayer.EXTENSION_RENDERER_MODE_PREFER
                            : SimpleExoPlayer.EXTENSION_RENDERER_MODE_ON)
                            : SimpleExoPlayer.EXTENSION_RENDERER_MODE_OFF;

            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);

            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(),
                    drmSessionManager, extensionRendererMode);
            player.addListener(this);

            eventLogger = new EventLogger(trackSelector);
            player.addListener(eventLogger);
            player.setAudioDebugListener(eventLogger);
            player.setVideoDebugListener(eventLogger);
            player.setMetadataOutput(eventLogger);

            vtvPlayerView.setPlayer(player);
            player.setPlayWhenReady(shouldAutoPlay);
            playerNeedsSource = true;
        }

        if (playerNeedsSource) {
            //if (Util.maybeRequestReadExternalStoragePermission(this, uris)) {
                //If video in storage;
                // The player will be reinitialized if the permission is granted.
                //return;
            //}
            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                player.seekTo(resumeWindow, resumePosition);
            }
            player.prepare(mediaSource, !haveResumePosition, false);
            playerNeedsSource = false;
            updateButtonsVisible();
        }
    }

    @Override
    public void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
            trackSelectionHelper = null;
            eventLogger = null;
        }
    }

    @Override
    public void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = player.isCurrentWindowSeekable() ? Math.max(0, player.getCurrentPosition())
                : C.TIME_UNSET;
    }

    @Override
    public void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    @Override
    public void updateButtonsVisible() {

        btnRetry.setVisibility(playerNeedsSource ? View.VISIBLE : View.GONE);

        if (player == null) {
            return;
        }

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }

        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                switch (player.getRendererType(i)) {
                    case C.TRACK_TYPE_VIDEO:
                        // Show btn select quality;
                        vtvControlView.setBtnQualityEnabled(true);
                        vtvControlView.setTagBtnQuality(i);
                        break;
                    case C.TRACK_TYPE_TEXT:
                        // Show button select subtitles;
                        vtvControlView.setBtnSubtitleEnabled(true);
                        vtvControlView.setTagBtnSubtitle(i);
                        break;
                }
            }
        }
    }

    @Override
    public MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension
                : uri.getLastPathSegment());

        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mainHandler, eventLogger);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    @Override
    public DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(UUID uuid, String licenseUrl, Map<String, String> keyRequestProperties) throws UnsupportedDrmException {
        if (Util.SDK_INT < 18) {
            return null;
        }
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false), keyRequestProperties);
        return new DefaultDrmSessionManager<>(uuid,
                FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, eventLogger);
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *     DataSource factory.
     * @return A new DataSource factory.
     */
    @Override
    public DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((App) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    /**
     * Returns a new HttpDataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *     DataSource factory.
     * @return A new HttpDataSource factory.
     */
    @Override
    public HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ((App) getApplication())
                .buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    @Override
    public void openActLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void openActBuyVip() {
        startActivity(new Intent(this, BuyVipActivity.class));
    }

    // Activity input

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Show the controls on any key event.
        vtvPlayerView.showController();
        // If the event was not handled then see if the player view can handle it as a media key event.
        return super.dispatchKeyEvent(event) || vtvPlayerView.dispatchMediaKeyEvent(event);
    }

    @Override
    public void onVisibilityChange(int visibility) {
        if (visibility == View.GONE) {
            hideSystemUI();
        }
    }

    @Override
    public void onButtonNextClick() {
        presenter.nextEpisode();
    }

    @Override
    public void onButtonPrevClick() {
        presenter.prevEpisode();
    }

    @Override
    public void onButtonQualityClick(View v) {
        trackSelectionHelper.showSelectionDialog(PlayerActivity.this, getString(R.string.player_text_quality),
                trackSelector.getCurrentMappedTrackInfo(), vtvControlView.getTagBtnQuality());
    }

    @Override
    public void onButtonSubtitleClick(View v) {
        trackSelectionHelper.showSelectionDialog(PlayerActivity.this, getString(R.string.player_text_subtutle),
                trackSelector.getCurrentMappedTrackInfo(), vtvControlView.getTagBtnSubtitle());
    }

    @Override
    public void onButtonVersionClick(View v) {
        presenter.openPopupSelectVersion();
    }

    @Override
    public void onButtonCommentClick() {
        showMsgToast("onButtonCommentClick");
    }

    @Override
    public void onButtonClearAdsClick() {
        presenter.openPopupClearAds();
    }

    @Override
    public void onButtonPlaylistClick() {
        presenter.openPopupListEpisodes();
    }

    @Override
    public void onButtonShareClick() {
        showMsgToast("onButtonShareClick");
    }

    @Override
    public void onButtonFinishClick() {
        finish();
    }

    @Override
    public void onTouchLightDown(float delatY, float height) {
        vtvPlayerView.onTouchLightDown(PlayerActivity.this, delatY, height);
    }

    @Override
    public void onTouchLightUp(float delatY, float height) {
        vtvPlayerView.onTouchLightUp(PlayerActivity.this, delatY, height);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        updateButtonsVisible();
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                showToast(R.string.error_unsupported_video);
            }
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                showToast(R.string.error_unsupported_audio);
            }
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            vtvPlayerView.showController();
        }
        updateButtonsVisible();
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }

        if (errorString != null) {
            showMsgToast(errorString);
        }

        playerNeedsSource = true;

        if (isBehindLiveWindow(e)) {
            clearResumePosition();
            initPlayer();
        } else {
            updateResumePosition();
            updateButtonsVisible();
        }
    }

    @Override
    public void onPositionDiscontinuity() {
        if (playerNeedsSource) {
            // This will only occur if the user has performed a seek whilst in the error state. Update the
            // resume position so that if the user then retries, playback will resume from the position to
            // which they seeked.
            updateResumePosition();
        }
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    protected void showToast(@StringRes int messageId) {
        showMsgToast(getString(messageId));
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        if (mDecorView == null) {
            mDecorView = getWindow().getDecorView();
        }
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN );
        }
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
