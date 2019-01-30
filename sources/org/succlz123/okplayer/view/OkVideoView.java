package org.succlz123.okplayer.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.MediaController;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlayer.Factory;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver.Listener;
import com.google.android.exoplayer.text.Cue;
import java.util.List;
import org.succlz123.okplayer.C0805R;
import org.succlz123.okplayer.OkPlayer;
import org.succlz123.okplayer.listener.CaptionListener;
import org.succlz123.okplayer.listener.OkPlayerListener;
import org.succlz123.okplayer.utils.OkPlayerUtils;

public class OkVideoView extends FrameLayout implements OkPlayerListener, CaptionListener, Callback, Listener {
    private AudioCapabilitiesReceiver audioCapabilitiesReceiver;
    private MediaController mediaController;
    private OkPlayer okPlayer;
    private long playerPosition;
    private int rendererContentType = 3;
    private SurfaceView surfaceView;
    private Uri uri;
    private AspectRatioFrameLayout videoFrame;

    public class CustomTouchListener extends SimpleOnGestureListener implements OnTouchListener {
        private GestureDetector gestureDetector;

        public CustomTouchListener(Context context) {
            this.gestureDetector = new GestureDetector(context, this);
        }

        public boolean onTouch(View v, MotionEvent event) {
            this.gestureDetector.onTouchEvent(event);
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (OkVideoView.this.mediaController == null) {
                return false;
            }
            if (OkVideoView.this.mediaController.isShowing()) {
                OkVideoView.this.mediaController.hide();
            } else {
                OkVideoView.this.mediaController.show(Factory.DEFAULT_MIN_REBUFFER_MS);
            }
            return true;
        }
    }

    public OkVideoView(Context context) {
        super(context);
        setup(context, null);
    }

    public OkVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public OkVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    @TargetApi(21)
    public OkVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context, attrs);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    private void setup(Context context, AttributeSet attrs) {
        if (context != null) {
            initView(context);
            readAttributes(context, attrs);
        }
    }

    private void initView(Context context) {
        View.inflate(context, C0805R.layout.ok_video_view, this);
        this.videoFrame = (AspectRatioFrameLayout) findViewById(C0805R.id.video_frame);
        this.surfaceView = (SurfaceView) findViewById(C0805R.id.surface_view);
        if (this.surfaceView != null) {
            initExoPlayer();
        }
    }

    private void initExoPlayer() {
        this.audioCapabilitiesReceiver = new AudioCapabilitiesReceiver(getContext().getApplicationContext(), this);
        this.audioCapabilitiesReceiver.register();
        this.okPlayer = new OkPlayer(null);
        this.okPlayer.addListener(this);
        this.okPlayer.setId3MetadataListener(null);
        this.okPlayer.setSurface(this.surfaceView.getHolder().getSurface());
        this.surfaceView.getHolder().addCallback(this);
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        if (attrs != null && !isInEditMode()) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, C0805R.styleable.OkExoPlayerVideoView);
            if (typedArray != null) {
                setDefaultControlsEnabled(typedArray.getBoolean(C0805R.styleable.OkExoPlayerVideoView_ControlsEnabled, false));
                typedArray.recycle();
            }
        }
    }

    public void setDefaultControlsEnabled(boolean enabled) {
        if (enabled) {
            this.mediaController = new MediaController(getContext());
            this.mediaController.setAnchorView(this.videoFrame);
            this.mediaController.setMediaPlayer(this.okPlayer.getPlayerControl());
            this.mediaController.setEnabled(true);
        }
        CustomTouchListener listener = new CustomTouchListener(getContext());
        if (!enabled) {
            listener = null;
        }
        setOnTouchListener(listener);
    }

    public Uri getVideoUri() {
        return this.uri;
    }

    public void setVideoUri(Uri videoUri) {
        setVideoUri(videoUri, true);
    }

    public void setVideoUri(Uri videoUri, boolean defaultPlay) {
        this.uri = videoUri;
        if (this.uri != null) {
            if (this.okPlayer == null) {
                initExoPlayer();
            }
            this.okPlayer.replaceRenderBuilder(OkPlayerUtils.getRendererBuilder(getContext(), this.uri, this.rendererContentType));
            this.okPlayer.prepare();
            this.okPlayer.pushSurface(true);
            this.okPlayer.seekTo(0);
            this.okPlayer.setPlayWhenReady(defaultPlay);
        }
    }

    public void onNewIntent() {
        release();
    }

    public void onResume(Uri uri) {
        if (this.okPlayer == null) {
            setVideoUri(uri);
        } else {
            this.okPlayer.setPlayWhenReady(true);
        }
    }

    public void onPause() {
        pause();
    }

    public void onDestroy() {
        release();
    }

    public void pause() {
        if (this.okPlayer != null) {
            this.okPlayer.setPlayWhenReady(false);
        }
    }

    public void release() {
        if (this.okPlayer != null) {
            this.okPlayer.release();
            this.okPlayer = null;
        }
        this.playerPosition = 0;
        if (this.audioCapabilitiesReceiver != null) {
            this.audioCapabilitiesReceiver.unregister();
            this.audioCapabilitiesReceiver = null;
        }
    }

    public void addListener(OkPlayerListener listener) {
        if (this.okPlayer != null) {
            this.okPlayer.addListener(listener);
        }
    }

    public void setRendererContentType(int rendererContentType) {
        this.rendererContentType = rendererContentType;
    }

    public int getPlaybackState() {
        if (this.okPlayer == null) {
            return 0;
        }
        return this.okPlayer.getPlaybackState();
    }

    public boolean getPlayWhenReady() {
        if (this.okPlayer == null) {
            return false;
        }
        return this.okPlayer.getPlayWhenReady();
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        if (this.okPlayer != null) {
            this.okPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    public void togglePlayback() {
        if (this.okPlayer == null || this.okPlayer.getPlaybackState() != 4) {
            return;
        }
        if (this.okPlayer.getPlayWhenReady()) {
            this.okPlayer.setPlayWhenReady(false);
        } else {
            this.okPlayer.setPlayWhenReady(true);
        }
    }

    public long getDuration() {
        if (this.okPlayer == null) {
            return 0;
        }
        return this.okPlayer.getDuration();
    }

    public long getCurrentPosition() {
        if (this.okPlayer == null) {
            return 0;
        }
        return this.okPlayer.getCurrentPosition();
    }

    public void seekTo(long positionMs) {
        if (this.okPlayer != null) {
            this.okPlayer.seekTo(positionMs);
        }
    }

    public long getBufferedPosition() {
        if (this.okPlayer == null) {
            return 0;
        }
        return this.okPlayer.getBufferedPosition();
    }

    public int getBufferedPercentage() {
        if (this.okPlayer == null) {
            return 0;
        }
        return this.okPlayer.getBufferedPercentage();
    }

    public void onStateChanged(boolean playWhenReady, int playbackState) {
    }

    public void onError(Exception e) {
    }

    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (this.okPlayer != null) {
            this.okPlayer.setSurface(holder.getSurface());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (this.okPlayer != null) {
            this.okPlayer.blockingClearSurface();
        }
    }

    public void onCues(List<Cue> list) {
    }

    public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
        if (this.okPlayer != null) {
            boolean backgrounded = this.okPlayer.getBackgrounded();
            boolean playWhenReady = this.okPlayer.getPlayWhenReady();
            this.okPlayer.setBackgrounded(backgrounded);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
