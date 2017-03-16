package com.vungtv.film.widget.player;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.Assertions;
import com.vungtv.film.R;
import com.vungtv.film.util.DensityUtils;
import com.vungtv.film.widget.VtvTextView;

import java.util.List;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/5/2017.
 * Email: vancuong2941989@gmail.com
 */
@TargetApi(16)
public class VtvPlayerView extends FrameLayout {
    public interface OnScreenBrightControl {

        void onTouchLightDown(float delatY, float height);

        void onTouchLightUp(float delatY, float height);
    }
    private static final String TAG = VtvPlayerView.class.getSimpleName();
    private static final int OPTION_FORWARD = 0;
    private static final int OPTION_REWIND = 1;
    private static final int OPTION_VOLUME = 2;
    private static final int OPTION_LIGHT = 3;

    private static final int SURFACE_TYPE_NONE = 0;
    private static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    private static final int SURFACE_TYPE_TEXTURE_VIEW = 2;

    private final AspectRatioFrameLayout contentFrame;
    private final View shutterView;
    private final View surfaceView;
    private final ImageView artworkView;
    private final SubtitleView subtitleView;
    private final VtvPlaybackControlView controller;
    private final VtvTextView textOption;
    private final ImageView btnLockController;
    private final ProgressBar loadingView;

    private final ComponentListener componentListener;
    private final FrameLayout overlayFrameLayout;
    private AudioManager mAudioManager;
    private OnScreenBrightControl onScreenBrightControl;

    private SimpleExoPlayer player;
    private boolean useArtwork;
    private Bitmap defaultArtwork;
    private int controllerShowTimeoutMs;
    private boolean isLockController = false;
    private int orginalLight;
    private int playbackState;

    /* Ontouch event */
    private float mLastMotionX, mLastMotionY;
    private float absDeltaXs, mLastMotionXs;
    private int startX, startY;
    private int typeVideoFast = 0;
    private long time;
    private boolean isClick = true;
    private int threshold;
    private float width;
    private float height;

    public VtvPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public VtvPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VtvPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        threshold = DensityUtils.dip2px(context, 18);
        width = context.getResources().getDisplayMetrics().heightPixels;
        height = context.getResources().getDisplayMetrics().widthPixels;
        int playerLayoutId = R.layout.widget_vtv_player_view;
        boolean useArtwork = true;
        int defaultArtworkId = 0;
        boolean useController = true;
        int surfaceType = SURFACE_TYPE_SURFACE_VIEW;
        int resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
        int controllerShowTimeoutMs = PlaybackControlView.DEFAULT_SHOW_TIMEOUT_MS;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.SimpleExoPlayerView, 0, 0);
            try {
                playerLayoutId = a.getResourceId(R.styleable.SimpleExoPlayerView_player_layout_id,
                        playerLayoutId);
                useArtwork = a.getBoolean(R.styleable.SimpleExoPlayerView_use_artwork, useArtwork);
                defaultArtworkId = a.getResourceId(R.styleable.SimpleExoPlayerView_default_artwork,
                        defaultArtworkId);
                useController = a.getBoolean(R.styleable.SimpleExoPlayerView_use_controller, useController);
                surfaceType = a.getInt(R.styleable.SimpleExoPlayerView_surface_type, surfaceType);
                resizeMode = a.getInt(R.styleable.SimpleExoPlayerView_resize_mode, resizeMode);
                controllerShowTimeoutMs = a.getInt(R.styleable.SimpleExoPlayerView_show_timeout,
                        controllerShowTimeoutMs);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(playerLayoutId, this);
        componentListener = new ComponentListener();
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        // Content frame.
        contentFrame = (AspectRatioFrameLayout) findViewById(R.id.vtv_player_content_frame);
        if (contentFrame != null) {
            setResizeModeRaw(contentFrame, resizeMode);
        }

        // Shutter view.
        shutterView = findViewById(R.id.vtv_player_shutter);

        // Create a surface view and insert it into the content frame, if there is one.
        if (contentFrame != null && surfaceType != SURFACE_TYPE_NONE) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            surfaceView = surfaceType == SURFACE_TYPE_TEXTURE_VIEW ? new TextureView(context)
                    : new SurfaceView(context);
            surfaceView.setLayoutParams(params);
            contentFrame.addView(surfaceView, 0);
        } else {
            surfaceView = null;
        }

        // Overlay frame layout.
        overlayFrameLayout = (FrameLayout) findViewById(R.id.vtv_player_overlay);

        // Artwork view.
        artworkView = (ImageView) findViewById(R.id.vtv_player_artwork);
        this.useArtwork = useArtwork && artworkView != null;
        if (defaultArtworkId != 0) {
            defaultArtwork = BitmapFactory.decodeResource(context.getResources(), defaultArtworkId);
        }

        // Subtitle view.
        subtitleView = (SubtitleView) findViewById(R.id.vtv_player_subtitles);
        if (subtitleView != null) {
            subtitleView.setUserDefaultStyle();
            subtitleView.setUserDefaultTextSize();
        }

        // Playback control view.
        controller = (VtvPlaybackControlView) findViewById(R.id.vtv_player_controller);
        if (controller != null) {
            controller.setVisibilityListener(componentListener);
        }
        this.controllerShowTimeoutMs = controllerShowTimeoutMs;

        textOption = (VtvTextView) findViewById(R.id.vtv_player_tv_option);
        loadingView = (ProgressBar) findViewById(R.id.vtv_player_progressBar);
        btnLockController = (ImageView) findViewById(R.id.vtv_player_btn_lock);
        if (btnLockController != null) {
            btnLockController.setOnClickListener(componentListener);
        }

        hideController();
    }

    /**
     * Returns the player currently set on this view, or null if no player is set.
     */
    public SimpleExoPlayer getPlayer() {
        return player;
    }

    /**
     * Set the {@link SimpleExoPlayer} to use. The {@link SimpleExoPlayer#setTextOutput} and
     * {@link SimpleExoPlayer#setVideoListener} method of the player will be called and previous
     * assignments are overridden.
     *
     * @param player The {@link SimpleExoPlayer} to use.
     */
    public void setPlayer(SimpleExoPlayer player) {
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.setTextOutput(null);
            this.player.setVideoListener(null);
            this.player.removeListener(componentListener);
            this.player.setVideoSurface(null);
        }

        this.player = player;

        controller.setPlayer(player);

        if (shutterView != null) {
            shutterView.setVisibility(VISIBLE);
        }
        if (player != null) {
            if (surfaceView instanceof TextureView) {
                player.setVideoTextureView((TextureView) surfaceView);
            } else if (surfaceView instanceof SurfaceView) {
                player.setVideoSurfaceView((SurfaceView) surfaceView);
            }
            player.setVideoListener(componentListener);
            player.addListener(componentListener);
            player.setTextOutput(componentListener);
            maybeShowController(false);
            updateForCurrentTrackSelections();
        } else {
            hideController();
            hideArtwork();
        }
    }

    public void setAudioManager(AudioManager mAudioManager) {
        this.mAudioManager = mAudioManager;
    }

    /**
     * Sets the resize mode.
     *
     * @param resizeMode The resize mode.
     */
    public void setResizeMode(@AspectRatioFrameLayout.ResizeMode int resizeMode) {
        Assertions.checkState(contentFrame != null);
        contentFrame.setResizeMode(resizeMode);
    }

    /**
     * Returns whether artwork is displayed if present in the media.
     */
    public boolean getUseArtwork() {
        return useArtwork;
    }

    /**
     * Sets whether artwork is displayed if present in the media.
     *
     * @param useArtwork Whether artwork is displayed.
     */
    public void setUseArtwork(boolean useArtwork) {
        Assertions.checkState(!useArtwork || artworkView != null);
        if (this.useArtwork != useArtwork) {
            this.useArtwork = useArtwork;
            updateForCurrentTrackSelections();
        }
    }

    /**
     * Returns the default artwork to display.
     */
    public Bitmap getDefaultArtwork() {
        return defaultArtwork;
    }

    /**
     * Sets the default artwork to display if {@code useArtwork} is {@code true} and no artwork is
     * present in the media.
     *
     * @param defaultArtwork the default artwork to display.
     */
    public void setDefaultArtwork(Bitmap defaultArtwork) {
        if (this.defaultArtwork != defaultArtwork) {
            this.defaultArtwork = defaultArtwork;
            updateForCurrentTrackSelections();
        }
    }

    public VtvPlaybackControlView getController() {
        return controller;
    }

    /**
     * Called to process media key events. Any {@link KeyEvent} can be passed but only media key
     * events will be handled. Does nothing if playback controls are disabled.
     *
     * @param event A key event.
     * @return Whether the key event was handled.
     */
    public boolean dispatchMediaKeyEvent(KeyEvent event) {
        return controller.dispatchMediaKeyEvent(event);
    }

    /**
     * Shows the playback controls. Does nothing if playback controls are disabled.
     */
    public void showController() {
        maybeShowController(true);
    }

    private void maybeShowController(boolean isForced) {
        if (player == null) {
            return;
        }

        int playbackState = player.getPlaybackState();
        boolean showIndefinitely = playbackState == ExoPlayer.STATE_IDLE
                || playbackState == ExoPlayer.STATE_ENDED || !player.getPlayWhenReady();

        boolean wasShowingIndefinitely = controller.isVisible() && controller.getShowTimeoutMs() <= 0;
        controller.setShowTimeoutMs(showIndefinitely ? 0 : controllerShowTimeoutMs);
        if (isForced || showIndefinitely || wasShowingIndefinitely) {
            controller.show();
            showBtnLockController(true);
        }
    }

    /**
     * Hides the playback controls. Does nothing if playback controls are disabled.
     */
    public void hideController() {
        if (controller != null) {
            controller.hide();
            showBtnLockController(false);
        }
    }

    /**
     * Show / Hide btn lock;
     *
     * @param show bool
     */
    public void showBtnLockController(boolean show) {
        btnLockController.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * Show progress loading view;
     *
     * @param show bool
     */
    public void showLoading(boolean show) {
        loadingView.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * Show text option when touch on screen;
     * content is text control volume, bright, rewind / forward video
     *
     * @param show bool
     */
    public void showTextOption(boolean show) {
        textOption.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * Returns the playback controls timeout. The playback controls are automatically hidden after
     * this duration of time has elapsed without user input and with playback or buffering in
     * progress.
     *
     * @return The timeout in milliseconds. A non-positive value will cause the controller to remain
     *     visible indefinitely.
     */
    public int getControllerShowTimeoutMs() {
        return controllerShowTimeoutMs;
    }

    /**
     * Sets the playback controls timeout. The playback controls are automatically hidden after this
     * duration of time has elapsed without user input and with playback or buffering in progress.
     *
     * @param controllerShowTimeoutMs The timeout in milliseconds. A non-positive value will cause
     *     the controller to remain visible indefinitely.
     */
    public void setControllerShowTimeoutMs(int controllerShowTimeoutMs) {
        Assertions.checkState(controller != null);
        this.controllerShowTimeoutMs = controllerShowTimeoutMs;
    }

    /**
     * Set the {@link PlaybackControlView.VisibilityListener}.
     *
     * @param listener The listener to be notified about visibility changes.
     */
    public void setControllerVisibilityListener(VtvPlaybackControlView.VisibilityListener listener) {
        Assertions.checkState(controller != null);
        controller.setVisibilityListener(listener);
    }

    public void setOnScreenBrightControl(OnScreenBrightControl onScreenBrightControl) {
        this.onScreenBrightControl = onScreenBrightControl;
    }

    /**
     * Sets the {@link PlaybackControlView.SeekDispatcher}.
     *
     * @param seekDispatcher The {@link PlaybackControlView.SeekDispatcher}, or null to use
     *     {@link PlaybackControlView#DEFAULT_SEEK_DISPATCHER}.
     */
    public void setSeekDispatcher(VtvPlaybackControlView.SeekDispatcher seekDispatcher) {
        Assertions.checkState(controller != null);
        controller.setSeekDispatcher(seekDispatcher);
    }

    /**
     * Sets the rewind increment in milliseconds.
     *
     * @param rewindMs The rewind increment in milliseconds.
     */
    public void setRewindIncrementMs(int rewindMs) {
        Assertions.checkState(controller != null);
        controller.setRewindIncrementMs(rewindMs);
    }

    /**
     * Sets the fast forward increment in milliseconds.
     *
     * @param fastForwardMs The fast forward increment in milliseconds.
     */
    public void setFastForwardIncrementMs(int fastForwardMs) {
        Assertions.checkState(controller != null);
        controller.setFastForwardIncrementMs(fastForwardMs);
    }

    public void onTouchVolumeUp(float delatY, float height) {
        showTextOption(true);
        if (mAudioManager == null) return;

        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int up = (int) ((delatY / height) * max * 3);
        int volume = Math.min(current + up, max);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        updateTextOption(OPTION_VOLUME, transformatVolume);
    }

    public void onTouchVolumeDown(float delatY, float height) {
        showTextOption(true);
        if (mAudioManager == null) return;

        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int down = (int) (delatY / height * max * 3);
        int volume = Math.max(current - down, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        updateTextOption(OPTION_VOLUME, transformatVolume);
    }

    public void onTouchLightDown(Activity context, float delatY, float height) {
        showTextOption(true);
        int down = (int) (delatY / height * 100);
        orginalLight = orginalLight - down;
        if (orginalLight < 10) {
            orginalLight = 10;
        }
        if (orginalLight > 100) {
            orginalLight = 100;
        }
        Log.e(TAG, "onTouchLightDown: orginalLight = " + orginalLight);
        LightnessController.setScreenBright(context, orginalLight);
        updateTextOption(OPTION_LIGHT, orginalLight);
    }

    public void onTouchLightUp(Activity context, float delatY, float height) {
        showTextOption(true);
        int up = (int) (delatY / height * 100);
        orginalLight = orginalLight + up;
        if (orginalLight < 10) {
            orginalLight = 10;
        }
        if (orginalLight > 100) {
            orginalLight = 100;
        }
        Log.e(TAG, "onTouchLightUp: orginalLight = " + orginalLight);
        LightnessController.setScreenBright(context, orginalLight);
        updateTextOption(OPTION_LIGHT, orginalLight);
    }

    /* ham tua time video */
    public void onTouchFastForward(float delataX, long time) {

        int forwardTime = (int) delataX * 25;
        long currentTime = time + forwardTime;

        if (player != null) {
            player.seekTo(Math.min(currentTime, player.getDuration()));
        }
    }

    /* ham tua time video */
    public void onTouchRewind(float delataX, long time) {

        if (player != null) {
            int backwardTime = (int) delataX * 25;
            long currentTime = time - backwardTime;

            player.seekTo(Math.max(currentTime, 0));
        }
    }

    public void onTouchRewindForwardOptionShow(float delataX, long time) {
        Log.d(TAG, "onTouchRewindForwardOptionShow: delataX = " + delataX + "\ntime = " + time);
        if (player == null) {
            return;
        }
        showTextOption(true);
        long currentTime = (long) (time + delataX * 25);
        if (currentTime > player.getDuration()) {
            currentTime = player.getDuration();
        } else if (currentTime < 0) {
            currentTime = 0;
        }

        updateTextOption(delataX > 0 ? OPTION_FORWARD : OPTION_REWIND, (int) currentTime);
    }

    private void updateTextOption(int option, int value) {
        int resIcon = 0;
        String text = null;
        switch (option) {
            case OPTION_FORWARD:
                resIcon = R.drawable.ic_control_forward;
                text = controller.stringForTime(value);
                break;
            case OPTION_REWIND:
                resIcon = R.drawable.ic_control_rewind;
                text = controller.stringForTime(value);
                break;
            case OPTION_VOLUME:
                resIcon = R.drawable.ic_control_volum;
                text = String.valueOf(value) + "%";
                break;
            case OPTION_LIGHT:
                resIcon = R.drawable.ic_control_light;
                text = String.valueOf(value) + "%";
                break;
        }
        textOption.setText(text);
        textOption.setCompoundDrawablesWithIntrinsicBounds(resIcon,0,0,0);
    }

    private void updateLockMode() {
        isLockController = !isLockController;
        if (isLockController) {
            hideController();
            btnLockController.setImageResource(R.drawable.ic_control_lock);
        } else {
            showController();
            btnLockController.setImageResource(R.drawable.ic_control_unlock);
        }
    }

    /**
     * Gets the view onto which video is rendered. This is either a {@link SurfaceView} (default)
     * or a {@link TextureView} if the {@code use_texture_view} view attribute has been set to true.
     *
     * @return Either a {@link SurfaceView} or a {@link TextureView}.
     */
    public View getVideoSurfaceView() {
        return surfaceView;
    }

    /**
     * Gets the overlay {@link FrameLayout}, which can be populated with UI elements to show on top of
     * the player.
     *
     * @return The overlay {@link FrameLayout}, or {@code null} if the layout has been customized and
     *     the overlay is not present.
     */
    public FrameLayout getOverlayFrameLayout() {
        return overlayFrameLayout;
    }

    /**
     * Gets the {@link SubtitleView}.
     *
     * @return The {@link SubtitleView}, or {@code null} if the layout has been customized and the
     *     subtitle view is not present.
     */
    public SubtitleView getSubtitleView() {
        return subtitleView;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (player == null) {
            return false;
        }
        maybeShowController(true);
        return true;
    }

    private void updateForCurrentTrackSelections() {
        if (player == null) {
            return;
        }
        TrackSelectionArray selections = player.getCurrentTrackSelections();
        for (int i = 0; i < selections.length; i++) {
            if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO && selections.get(i) != null) {
                // Video enabled so artwork must be hidden. If the shutter is closed, it will be opened in
                // onRenderedFirstFrame().
                hideArtwork();
                return;
            }
        }
        // Video disabled so the shutter must be closed.
        if (shutterView != null) {
            shutterView.setVisibility(VISIBLE);
        }
        // Display artwork if enabled and available, else hide it.
        if (useArtwork) {
            for (int i = 0; i < selections.length; i++) {
                TrackSelection selection = selections.get(i);
                if (selection != null) {
                    for (int j = 0; j < selection.length(); j++) {
                        Metadata metadata = selection.getFormat(j).metadata;
                        if (metadata != null && setArtworkFromMetadata(metadata)) {
                            return;
                        }
                    }
                }
            }
            if (setArtworkFromBitmap(defaultArtwork)) {
                return;
            }
        }
        // Artwork disabled or unavailable.
        hideArtwork();
    }

    private boolean setArtworkFromMetadata(Metadata metadata) {
        for (int i = 0; i < metadata.length(); i++) {
            Metadata.Entry metadataEntry = metadata.get(i);
            if (metadataEntry instanceof ApicFrame) {
                byte[] bitmapData = ((ApicFrame) metadataEntry).pictureData;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
                return setArtworkFromBitmap(bitmap);
            }
        }
        return false;
    }

    private boolean setArtworkFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            if (bitmapWidth > 0 && bitmapHeight > 0) {
                if (contentFrame != null) {
                    contentFrame.setAspectRatio((float) bitmapWidth / bitmapHeight);
                }
                artworkView.setImageBitmap(bitmap);
                artworkView.setVisibility(VISIBLE);
                return true;
            }
        }
        return false;
    }

    private void hideArtwork() {
        if (artworkView != null) {
            artworkView.setImageResource(android.R.color.transparent); // Clears any bitmap reference.
            artworkView.setVisibility(INVISIBLE);
        }
    }

    @SuppressWarnings("ResourceType")
    private static void setResizeModeRaw(AspectRatioFrameLayout aspectRatioFrame, int resizeMode) {
        aspectRatioFrame.setResizeMode(resizeMode);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (player == null) {
            return false;
        }

        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                mLastMotionXs = x;
                startX = (int) x;
                startY = (int) y;
                time = player.getCurrentPosition();
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = x - mLastMotionX;
                float deltaY = y - mLastMotionY;
                float deltaXs = x - mLastMotionXs;
                float absDeltaX = Math.abs(deltaX);
                absDeltaXs = Math.abs(deltaXs);
                float absDeltaY = Math.abs(deltaY);

                boolean isAdjustAudio;

                if (absDeltaX > threshold && absDeltaY > threshold) {
                    isAdjustAudio = absDeltaX < absDeltaY;
                } else if (absDeltaX < threshold && absDeltaY > threshold) {
                    isAdjustAudio = true;
                } else if (absDeltaX > threshold && absDeltaY < threshold) {
                    isAdjustAudio = false;
                } else {
                    return true;
                }

                if (isAdjustAudio) {
                    if (x < width / 3) {
                        // Dieu chinh do sang
                        if (onScreenBrightControl != null) {
                            if (deltaY > 0) {
                                onScreenBrightControl.onTouchLightDown(absDeltaY, height);
                            } else if (deltaY < 0) {
                                onScreenBrightControl.onTouchLightUp(absDeltaY, height);
                            }
                        }

                    } else if(x > (width * 2 / 3)){
                        // Dieu chinh am thanh
                        if (deltaY > 0) {
                            onTouchVolumeDown(absDeltaY, height);
                        } else if (deltaY < 0) {
                            onTouchVolumeUp(absDeltaY, height);
                        }
                    }

                } else {
                    // Tua video
                    onTouchRewindForwardOptionShow(deltaXs, time);

                    if (deltaXs > 0) {
                        typeVideoFast = 1;
                    } else if (deltaXs < 0) {
                        typeVideoFast = 2;
                    }
                }
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (typeVideoFast == 1) {
                    onTouchFastForward(absDeltaXs, time);
                } else if (typeVideoFast == 2) {
                    onTouchRewind(absDeltaXs, time);
                }
                typeVideoFast = 0;
                if (Math.abs(x - startX) > threshold
                        || Math.abs(y - startY) > threshold) {
                    isClick = false;
                }
                mLastMotionX = 0;
                mLastMotionY = 0;
                startX = 0;
                if (isClick) {
                    // Show / hide controller
                    if (isLockController) {
                        if (btnLockController.getVisibility() == VISIBLE) {
                            showBtnLockController(false);
                        } else {
                            showBtnLockController(true);
                        }
                    } else if (!controller.isVisible()) {
                        showController();
                    } else {
                        hideController();
                    }
                }
                isClick = true;
                showTextOption(false);
                break;
            default:
                break;
        }

        return true;
    }


    private final class ComponentListener implements SimpleExoPlayer.VideoListener,
            TextRenderer.Output, ExoPlayer.EventListener, OnClickListener, VtvPlaybackControlView.VisibilityListener {

        // TextRenderer.Output implementation

        @Override
        public void onCues(List<Cue> cues) {
            if (subtitleView != null) {
                subtitleView.onCues(cues);
            }
        }

        // SimpleExoPlayer.VideoListener implementation

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                       float pixelWidthHeightRatio) {
            if (contentFrame != null) {
                float aspectRatio = height == 0 ? 1 : (width * pixelWidthHeightRatio) / height;
                contentFrame.setAspectRatio(aspectRatio);
            }
        }

        @Override
        public void onRenderedFirstFrame() {
            if (shutterView != null) {
                shutterView.setVisibility(INVISIBLE);
            }
        }

        @Override
        public void onTracksChanged(TrackGroupArray tracks, TrackSelectionArray selections) {
            updateForCurrentTrackSelections();
        }

        // ExoPlayer.EventListener implementation

        @Override
        public void onLoadingChanged(boolean isLoading) {
            if (playbackState == 3 && isLoading) {
                return;
            }
            showLoading(isLoading);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState1) {
            playbackState = playbackState1;
            if (playbackState == ExoPlayer.STATE_READY) {
                showLoading(false);
            } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
                showLoading(true);
            }
            maybeShowController(false);
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            // Do nothing.
        }

        @Override
        public void onPositionDiscontinuity() {
            // Do nothing.
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            // Do nothing.
        }

        @Override
        public void onClick(View view) {

            if (view == btnLockController) {
                updateLockMode();
            }
        }

        @Override
        public void onVisibilityChange(int visibility) {
            if (visibility == GONE) {
                showBtnLockController(false);
            }
        }
    }
}
