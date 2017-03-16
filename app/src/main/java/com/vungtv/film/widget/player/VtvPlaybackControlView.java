package com.vungtv.film.widget.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.Util;
import com.vungtv.film.R;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.widget.VtvTextView;

import java.util.Formatter;
import java.util.Locale;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/5/2017.
 * Email: vancuong2941989@gmail.com
 */

public class VtvPlaybackControlView extends FrameLayout {
    private static final String TAG = VtvPlaybackControlView.class.getSimpleName();

    /**
     * ResultListener to be notified about changes of the visibility of the UI control.
     */
    public interface VisibilityListener {

        /**
         * Called when the visibility changes.
         *
         * @param visibility The new visibility. Either {@link View#VISIBLE} or {@link View#GONE}.
         */
        void onVisibilityChange(int visibility);

    }

    /**
     * Dispatches seek operations to the player.
     */
    public interface SeekDispatcher {

        /**
         * @param player The player to seek.
         * @param windowIndex The index of the window.
         * @param positionMs The seek position in the specified window, or {@link C#TIME_UNSET} to seek
         *     to the window's default position.
         * @return True if the seek was dispatched. False otherwise.
         */
        boolean dispatchSeek(ExoPlayer player, int windowIndex, long positionMs);

    }

    /**
     * ResultListener to be notified about click the buttons;
     */
    public interface OnControlButtonClickListener {

        void onButtonQualityClick(View v);

        void onButtonSubtitleClick(View v);

        void onButtonVersionClick(View v);

        void onButtonCommentClick();

        void onButtonClearAdsClick();

        void onButtonPlaylistClick();

        void onButtonShareClick();

        void onButtonFinishClick();
    }

    public interface OnControlButtonNextPrevClickListener {

        void onButtonNextClick();

        void onButtonPrevClick();
    }

    /**
     * Default {@link SeekDispatcher} that dispatches seeks to the player without modification.
     */
    public static final SeekDispatcher DEFAULT_SEEK_DISPATCHER = new SeekDispatcher() {
        @Override
        public boolean dispatchSeek(ExoPlayer player, int windowIndex, long positionMs) {
            LogUtils.i(TAG, "public boolean dispatchSeek(ExoPlayer player, int windowIndex, long positionMs)");
            player.seekTo(windowIndex, positionMs);
            return true;
        }
    };

    public static final int DEFAULT_FAST_FORWARD_MS = 15000;
    public static final int DEFAULT_REWIND_MS = 5000;
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;

    private static final int PROGRESS_BAR_MAX = 1000;
    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;

    private final ComponentListener componentListener;
    private final View previousButton;
    private final View nextButton;
    private final View playButton;
    private final View pauseButton;
    private final TextView title;
    private final TextView durationView;
    private final TextView positionView;
    private final SeekBar progressBar;
    private final View btnClearAds;
    private final View btnQuality;
    private final View btnSubtitle;
    private final View btnVersion;
    private final View btnComment;
    private final View btnPlaylist;
    private final View btnShare;
    private final View btnFinish;

    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private final Timeline.Window currentWindow;

    private ExoPlayer player;
    private SeekDispatcher seekDispatcher;
    private VisibilityListener visibilityListener;
    private OnControlButtonClickListener onButtonClickListener;
    private OnControlButtonNextPrevClickListener buttonNextPrevClickListener;

    private boolean isAttachedToWindow;
    private boolean dragging;
    private int rewindMs;
    private int fastForwardMs;
    private int showTimeoutMs;
    private long hideAtMs;

    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private final Runnable hideAction = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Contructor
     *
     * @param context context;
     */
    public VtvPlaybackControlView(@NonNull Context context) {
        this(context, null);
    }

    public VtvPlaybackControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VtvPlaybackControlView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int controllerLayoutId = R.layout.widget_vtv_playback_control_view;
        rewindMs = DEFAULT_REWIND_MS;
        fastForwardMs = DEFAULT_FAST_FORWARD_MS;
        showTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.PlaybackControlView, 0, 0);
            try {
                rewindMs = a.getInt(R.styleable.PlaybackControlView_rewind_increment, rewindMs);
                fastForwardMs = a.getInt(R.styleable.PlaybackControlView_fastforward_increment,
                        fastForwardMs);
                showTimeoutMs = a.getInt(R.styleable.PlaybackControlView_show_timeout, showTimeoutMs);
                controllerLayoutId = a.getResourceId(R.styleable.PlaybackControlView_controller_layout_id,
                        controllerLayoutId);
            } finally {
                a.recycle();
            }
        }

        currentWindow = new Timeline.Window();
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        componentListener = new ComponentListener();
        seekDispatcher = DEFAULT_SEEK_DISPATCHER;

        LayoutInflater.from(context).inflate(controllerLayoutId, this);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        title = (VtvTextView) findViewById(R.id.control_tv_title);
        durationView = (TextView) findViewById(R.id.control_tv_duration);
        positionView = (TextView) findViewById(R.id.control_tv_time);
        progressBar = (SeekBar) findViewById(R.id.control_seekbar);
        if (progressBar != null) {
            progressBar.setOnSeekBarChangeListener(componentListener);
            progressBar.setMax(PROGRESS_BAR_MAX);
        }
        playButton = findViewById(R.id.control_btn_play);
        if (playButton != null) {
            playButton.setOnClickListener(componentListener);
        }
        pauseButton = findViewById(R.id.control_btn_pause);
        if (pauseButton != null) {
            pauseButton.setOnClickListener(componentListener);
        }
        previousButton = findViewById(R.id.control_btn_prev);
        if (previousButton != null) {
            previousButton.setOnClickListener(componentListener);
        }
        nextButton = findViewById(R.id.control_btn_next);
        if (nextButton != null) {
            nextButton.setOnClickListener(componentListener);
        }
        btnClearAds = findViewById(R.id.control_btn_vip);
        if (btnClearAds != null) {
            btnClearAds.setOnClickListener(componentListener);
        }
        btnPlaylist = findViewById(R.id.control_btn_playlist);
        if (btnPlaylist != null) {
            btnPlaylist.setOnClickListener(componentListener);
        }
        btnShare = findViewById(R.id.control_btn_share);
        if (btnShare != null) {
            btnShare.setOnClickListener(componentListener);
        }
        btnQuality = findViewById(R.id.control_btn_quality);
        if (btnQuality != null) {
            btnQuality.setOnClickListener(componentListener);
        }
        btnSubtitle = findViewById(R.id.control_btn_subtitle);
        if (btnSubtitle != null) {
            btnSubtitle.setOnClickListener(componentListener);
        }
        btnVersion = findViewById(R.id.control_btn_version);
        if (btnVersion != null) {
            btnVersion.setOnClickListener(componentListener);
        }
        btnComment = findViewById(R.id.control_btn_comment);
        if (btnComment != null) {
            btnComment.setOnClickListener(componentListener);
        }
        btnFinish = findViewById(R.id.control_btn_exit);
        if (btnFinish != null) {
            btnFinish.setOnClickListener(componentListener);
        }
    }

    /**
     * Returns the player currently being controlled by this view, or null if no player is set.
     */
    public ExoPlayer getPlayer() {
        return player;
    }

    /**
     * Sets the {@link ExoPlayer} to control.
     *
     * @param player the {@code ExoPlayer} to control.
     */
    public void setPlayer(ExoPlayer player) {
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.removeListener(componentListener);
        }
        this.player = player;
        if (player != null) {
            player.addListener(componentListener);
        }
        updateAll();
    }

    /**
     * Sets the {@link VisibilityListener}.
     *
     * @param listener The listener to be notified about visibility changes.
     */
    public void setVisibilityListener(VisibilityListener listener) {
        this.visibilityListener = listener;
    }

    /**
     * Sets the {@link SeekDispatcher}.
     *
     * @param seekDispatcher The {@link SeekDispatcher}, or null to use
     *     {@link #DEFAULT_SEEK_DISPATCHER}.
     */
    public void setSeekDispatcher(SeekDispatcher seekDispatcher) {
        this.seekDispatcher = seekDispatcher == null ? DEFAULT_SEEK_DISPATCHER : seekDispatcher;
    }

    /**
     * Sets the {@link OnControlButtonClickListener}.
     *
     * @param onButtonClickListener event click the buttons
     */
    public void setOnControlButtonClickListener(OnControlButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    /**
     * Sets the {@link OnControlButtonNextPrevClickListener}.
     *
     * @param buttonNextPrevClickListener event click the buttons
     */
    public void setOnControlButtonNextPrevClickListener(OnControlButtonNextPrevClickListener buttonNextPrevClickListener) {
        this.buttonNextPrevClickListener = buttonNextPrevClickListener;
    }

    /**
     * Sets the rewind increment in milliseconds.
     *
     * @param rewindMs The rewind increment in milliseconds. A non-positive value will cause the
     *     rewind button to be disabled.
     */
    public void setRewindIncrementMs(int rewindMs) {
        this.rewindMs = rewindMs;
        updateNavigation();
    }

    /**
     * Sets the fast forward increment in milliseconds.
     *
     * @param fastForwardMs The fast forward increment in milliseconds. A non-positive value will
     *     cause the fast forward button to be disabled.
     */
    public void setFastForwardIncrementMs(int fastForwardMs) {
        this.fastForwardMs = fastForwardMs;
        updateNavigation();
    }

    /**
     * Returns the playback controls timeout. The playback controls are automatically hidden after
     * this duration of time has elapsed without user input.
     *
     * @return The duration in milliseconds. A non-positive value indicates that the controls will
     *     remain visible indefinitely.
     */
    public int getShowTimeoutMs() {
        return showTimeoutMs;
    }

    /**
     * Sets the playback controls timeout. The playback controls are automatically hidden after this
     * duration of time has elapsed without user input.
     *
     * @param showTimeoutMs The duration in milliseconds. A non-positive value will cause the controls
     *     to remain visible indefinitely.
     */
    public void setShowTimeoutMs(int showTimeoutMs) {
        this.showTimeoutMs = showTimeoutMs;
    }

    /**
     * set title of video;
     *
     * @param text string title
     */
    public void setTitle(String text) {
        if (text == null) {
            title.setText("");
            return;
        }
        title.setText(text);
    }

    public void setNextButtonVisible(boolean visible) {
        nextButton.setAlpha(visible ? 1f : 0f);
        nextButton.setEnabled(visible);
    }

    public void setPreviousButtonVisible(boolean visible) {
        previousButton.setAlpha(visible ? 1f : 0f);
        previousButton.setEnabled(visible);
    }

    /**
     * Ẩn hiện nút chọn đọ phân giải;
     *
     * @param enabled hiện nếu có > 1 độ phân giải;
     */
    public void setBtnQualityEnabled(boolean enabled) {
        btnQuality.setVisibility(enabled ? VISIBLE : GONE);
    }

    /**
     * Set Tag độ phân giải đang chạy;
     *
     * @param i tag id;
     */
    public void setTagBtnQuality(int i) {
        btnQuality.setTag(i);
    }

    /**
     * GET Tag độ phân giải đang chạy;
     *
     * @return TAG id
     */
    public int getTagBtnQuality() {
        return (int) btnQuality.getTag();
    }

    /**
     * Ẩn / hiện nút chọn phụ đề;
     *
     * @param enabled hiện nếu video có phụ đề;
     */
    public void setBtnSubtitleEnabled(boolean enabled) {
        btnSubtitle.setVisibility(enabled ? VISIBLE : GONE);
    }

    /**
     * Set Tag Phụ đề đang chạy;
     *
     * @param i tag id;
     */
    public void setTagBtnSubtitle(int i) {
        btnSubtitle.setTag(i);
    }

    /**
     * GET Tag Phụ đề đang chạy;
     *
     * @return TAG id
     */
    public int getTagBtnSubtitle() {
        return (int) btnSubtitle.getTag();
    }

    public void setBtnVersionEnabled(boolean enabled) {
        btnVersion.setVisibility(enabled ? VISIBLE : GONE);
    }

    /**
     * Ẩn / Hiện nút hiện thị danh sách phát;
     *
     * @param enabled hiện nếu film có > 1 tập;
     */
    public void setBtnPlaylistEnabled(boolean enabled) {
        btnPlaylist.setVisibility(enabled ? VISIBLE : GONE);
    }

    public void setBtnClearAdsEnabled(boolean enabled) {
        btnClearAds.setVisibility(enabled ? VISIBLE : GONE);
    }

    /**
     * Shows the playback controls. If {@link #getShowTimeoutMs()} is positive then the controls will
     * be automatically hidden after this duration of time has elapsed without user input.
     */
    public void show() {
        if (!isVisible()) {
            setVisibility(VISIBLE);
            if (visibilityListener != null) {
                visibilityListener.onVisibilityChange(getVisibility());
            }
            updateAll();
            requestPlayPauseFocus();
        }
        // Call hideAfterTimeout even if already visible to reset the timeout.
        hideAfterTimeout();
    }

    /**
     * Hides the controller.
     */
    public void hide() {
        if (isVisible()) {
            setVisibility(GONE);
            if (visibilityListener != null) {
                visibilityListener.onVisibilityChange(getVisibility());
            }
            removeCallbacks(updateProgressAction);
            removeCallbacks(hideAction);
            hideAtMs = C.TIME_UNSET;
        }
    }

    /**
     * Returns whether the controller is currently visible.
     */
    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    private void hideAfterTimeout() {
        removeCallbacks(hideAction);
        if (showTimeoutMs > 0) {
            hideAtMs = SystemClock.uptimeMillis() + showTimeoutMs;
            if (isAttachedToWindow) {
                postDelayed(hideAction, showTimeoutMs);
            }
        } else {
            hideAtMs = C.TIME_UNSET;
        }
    }

    private void updateAll() {
        updatePlayPauseButton();
        updateNavigation();
        updateProgress();
    }

    private void updatePlayPauseButton() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }
        boolean requestPlayPauseFocus = false;
        boolean playing = player != null && player.getPlayWhenReady();
        if (playButton != null) {
            requestPlayPauseFocus |= playing && playButton.isFocused();
            playButton.setVisibility(playing ? View.GONE : View.VISIBLE);
        }
        if (pauseButton != null) {
            requestPlayPauseFocus |= !playing && pauseButton.isFocused();
            pauseButton.setVisibility(!playing ? View.GONE : View.VISIBLE);
        }
        if (requestPlayPauseFocus) {
            requestPlayPauseFocus();
        }
    }

    private void updateNavigation() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }
        Timeline currentTimeline = player != null ? player.getCurrentTimeline() : null;
        boolean haveNonEmptyTimeline = currentTimeline != null && !currentTimeline.isEmpty();
        boolean isSeekable = false;
        boolean enablePrevious = false;
        boolean enableNext = false;
        if (haveNonEmptyTimeline) {
            int currentWindowIndex = player.getCurrentWindowIndex();
            currentTimeline.getWindow(currentWindowIndex, currentWindow);
            isSeekable = currentWindow.isSeekable;
            enablePrevious = currentWindowIndex > 0 || isSeekable || !currentWindow.isDynamic;
            enableNext = (currentWindowIndex < currentTimeline.getWindowCount() - 1)
                    || currentWindow.isDynamic;
        }

        if (buttonNextPrevClickListener == null) {
            setButtonEnabled(enablePrevious , previousButton);
            setButtonEnabled(enableNext, nextButton);
        }

        if (progressBar != null) {
            progressBar.setEnabled(isSeekable);
        }
    }

    private void updateProgress() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }
        long duration = player == null ? 0 : player.getDuration();
        long position = player == null ? 0 : player.getCurrentPosition();
        if (durationView != null) {
            durationView.setText(stringForTime(duration));
        }
        if (positionView != null && !dragging) {
            positionView.setText(stringForTime(position));
        }

        if (progressBar != null) {
            if (!dragging) {
                progressBar.setProgress(progressBarValue(position));
            }
            long bufferedPosition = player == null ? 0 : player.getBufferedPosition();
            progressBar.setSecondaryProgress(progressBarValue(bufferedPosition));
            // Remove scheduled updates.
        }
        removeCallbacks(updateProgressAction);
        // Schedule an update if necessary.
        int playbackState = player == null ? ExoPlayer.STATE_IDLE : player.getPlaybackState();
        if (playbackState != ExoPlayer.STATE_IDLE && playbackState != ExoPlayer.STATE_ENDED) {
            long delayMs;
            if (player.getPlayWhenReady() && playbackState == ExoPlayer.STATE_READY) {
                delayMs = 1000 - (position % 1000);
                if (delayMs < 200) {
                    delayMs += 1000;
                }
            } else {
                delayMs = 1000;
            }
            postDelayed(updateProgressAction, delayMs);
        }
    }

    private void requestPlayPauseFocus() {
        boolean playing = player != null && player.getPlayWhenReady();
        if (!playing && playButton != null) {
            playButton.requestFocus();
        } else if (playing && pauseButton != null) {
            pauseButton.requestFocus();
        }
    }

    private void setButtonEnabled(boolean enabled, View view) {
        if (view == null) {
            return;
        }
        view.setEnabled(enabled);
        if (Util.SDK_INT >= 11) {
            setViewAlphaV11(view, enabled ? 1f : 0.3f);
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(enabled ? VISIBLE : INVISIBLE);
        }
    }

    @TargetApi(11)
    private void setViewAlphaV11(View view, float alpha) {
        view.setAlpha(alpha);
    }

    public String stringForTime(long timeMs) {
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0;
        }
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }

    private int progressBarValue(long position) {
        long duration = player == null ? C.TIME_UNSET : player.getDuration();
        return duration == C.TIME_UNSET || duration == 0 ? 0
                : (int) ((position * PROGRESS_BAR_MAX) / duration);
    }

    private long positionValue(int progress) {
        long duration = player == null ? C.TIME_UNSET : player.getDuration();
        return duration == C.TIME_UNSET ? 0 : ((duration * progress) / PROGRESS_BAR_MAX);
    }

    private void previous() {
        Timeline currentTimeline = player.getCurrentTimeline();
        if (currentTimeline.isEmpty()) {
            return;
        }
        int currentWindowIndex = player.getCurrentWindowIndex();
        currentTimeline.getWindow(currentWindowIndex, currentWindow);
        if (currentWindowIndex > 0 && (player.getCurrentPosition() <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS
                || (currentWindow.isDynamic && !currentWindow.isSeekable))) {
            seekTo(currentWindowIndex - 1, C.TIME_UNSET);
        } else {
            seekTo(0);
        }
    }

    private void next() {
        Timeline currentTimeline = player.getCurrentTimeline();
        if (currentTimeline.isEmpty()) {
            return;
        }
        int currentWindowIndex = player.getCurrentWindowIndex();
        if (currentWindowIndex < currentTimeline.getWindowCount() - 1) {
            seekTo(currentWindowIndex + 1, C.TIME_UNSET);
        } else if (currentTimeline.getWindow(currentWindowIndex, currentWindow, false).isDynamic) {
            seekTo(currentWindowIndex, C.TIME_UNSET);
        }
    }

    private void rewind() {
        if (rewindMs <= 0) {
            return;
        }
        seekTo(Math.max(player.getCurrentPosition() - rewindMs, 0));
    }

    private void fastForward() {
        if (fastForwardMs <= 0) {
            return;
        }
        seekTo(Math.min(player.getCurrentPosition() + fastForwardMs, player.getDuration()));
    }

    private void seekTo(long positionMs) {
        LogUtils.d(TAG, "private void seekTo(long positionMs)");
        seekTo(player.getCurrentWindowIndex(), positionMs);
    }

    private void seekTo(int windowIndex, long positionMs) {
        LogUtils.d(TAG, "private void seekTo(int windowIndex, long positionMs)");
        boolean dispatched = seekDispatcher.dispatchSeek(player, windowIndex, positionMs);
        if (!dispatched) {
            // The seek wasn't dispatched. If the progress bar was dragged by the user to perform the
            // seek then it'll now be in the wrong position. Trigger a progress update to snap it back.
            updateProgress();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        if (hideAtMs != C.TIME_UNSET) {
            long delayMs = hideAtMs - SystemClock.uptimeMillis();
            if (delayMs <= 0) {
                hide();
            } else {
                postDelayed(hideAction, delayMs);
            }
        }
        updateAll();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handled = dispatchMediaKeyEvent(event) || super.dispatchKeyEvent(event);
        if (handled) {
            show();
        }
        return handled;
    }

    /**
     * Called to process media key events. Any {@link KeyEvent} can be passed but only media key
     * events will be handled.
     *
     * @param event A key event.
     * @return Whether the key event was handled.
     */
    public boolean dispatchMediaKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (player == null || !isHandledMediaKey(keyCode)) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    fastForward();
                    break;
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    rewind();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    player.setPlayWhenReady(!player.getPlayWhenReady());
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    player.setPlayWhenReady(true);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    player.setPlayWhenReady(false);
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    next();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    previous();
                    break;
                default:
                    break;
            }
        }
        show();
        return true;
    }

    private static boolean isHandledMediaKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
                || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
                || keyCode == KeyEvent.KEYCODE_MEDIA_NEXT
                || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS;
    }

    /**
     * Class event handler;
     */
    private final class ComponentListener implements ExoPlayer.EventListener,
            SeekBar.OnSeekBarChangeListener, OnClickListener {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            removeCallbacks(hideAction);
            dragging = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                long position = positionValue(progress);
                if (positionView != null) {
                    positionView.setText(stringForTime(position));
                }
                if (player != null && !dragging) {
                    LogUtils.i(TAG, "onProgressChanged: player != null && !dragging");
                    seekTo(position);
                }
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            dragging = false;
            if (player != null) {
                LogUtils.i(TAG, "onStopTrackingTouch: if (player != null)");
                seekTo(positionValue(seekBar.getProgress()));
            }
            hideAfterTimeout();
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            updatePlayPauseButton();
            updateProgress();
        }

        @Override
        public void onPositionDiscontinuity() {
            updateNavigation();
            updateProgress();
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            updateNavigation();
            updateProgress();
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            // Do nothing.
        }

        @Override
        public void onTracksChanged(TrackGroupArray tracks, TrackSelectionArray selections) {
            // Do nothing.
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            // Do nothing.
        }

        @Override
        public void onClick(View view) {
            if (player != null) {
                if (nextButton == view) {
                    if (buttonNextPrevClickListener != null) {
                        buttonNextPrevClickListener.onButtonNextClick();
                    } else {
                        next();
                    }
                } else if (previousButton == view) {
                    if (buttonNextPrevClickListener != null) {
                        buttonNextPrevClickListener.onButtonPrevClick();
                    } else {
                        previous();
                    }
                } else if (playButton == view) {
                    player.setPlayWhenReady(true);
                } else if (pauseButton == view) {
                    player.setPlayWhenReady(false);
                } else if (btnQuality == view) {
                    if (onButtonClickListener != null){
                        onButtonClickListener.onButtonQualityClick(view);
                    }
                } else if (btnSubtitle == view) {
                    if (onButtonClickListener != null){
                        onButtonClickListener.onButtonSubtitleClick(view);
                    }
                } else if (btnVersion == view) {
                    if (onButtonClickListener != null){
                        onButtonClickListener.onButtonVersionClick(view);
                    }
                } else if (btnComment == view) {
                    if (onButtonClickListener != null){
                        onButtonClickListener.onButtonCommentClick();
                    }
                } else if (btnClearAds == view) {
                    if (onButtonClickListener != null){
                        onButtonClickListener.onButtonClearAdsClick();
                    }
                } else if (btnPlaylist == view) {
                    if (onButtonClickListener != null){
                        onButtonClickListener.onButtonPlaylistClick();
                    }
                } else if (btnShare == view) {
                    if (onButtonClickListener != null){
                        onButtonClickListener.onButtonShareClick();
                    }
                } else if (btnFinish == view) {
                    if (onButtonClickListener != null){
                        onButtonClickListener.onButtonFinishClick();
                    }
                }
            }
            hideAfterTimeout();
        }
    }
}
