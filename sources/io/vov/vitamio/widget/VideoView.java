package io.vov.vitamio.widget;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.media.TransportMediator;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import io.vov.vitamio.MediaFormat;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.MediaPlayer.OnTimedTextListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController.MediaPlayerControl;
import java.util.List;
import java.util.Map;

public class VideoView extends SurfaceView implements MediaPlayerControl {
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_RESUME = 7;
    private static final int STATE_SUSPEND = 6;
    private static final int STATE_SUSPEND_UNSUPPORTED = 8;
    public static final int VIDEO_LAYOUT_FIT_PARENT = 4;
    public static final int VIDEO_LAYOUT_ORIGIN = 0;
    public static final int VIDEO_LAYOUT_SCALE = 1;
    public static final int VIDEO_LAYOUT_STRETCH = 2;
    public static final int VIDEO_LAYOUT_ZOOM = 3;
    private float mAspectRatio;
    private int mBufSize;
    private OnBufferingUpdateListener mBufferingUpdateListener;
    private OnCompletionListener mCompletionListener;
    private Context mContext;
    private int mCurrentBufferPercentage;
    private int mCurrentState;
    private long mDuration;
    private OnErrorListener mErrorListener;
    private boolean mHardwareDecoder;
    private Map<String, String> mHeaders;
    private OnInfoListener mInfoListener;
    private View mMediaBufferingIndicator;
    private MediaController mMediaController;
    private MediaPlayer mMediaPlayer;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPreparedListener mOnPreparedListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnTimedTextListener mOnTimedTextListener;
    OnPreparedListener mPreparedListener;
    Callback mSHCallback;
    private OnSeekCompleteListener mSeekCompleteListener;
    private long mSeekWhenPrepared;
    OnVideoSizeChangedListener mSizeChangedListener;
    private int mSurfaceHeight;
    private SurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private OnTimedTextListener mTimedTextListener;
    private Uri mUri;
    private float mVideoAspectRatio;
    private int mVideoChroma;
    private int mVideoHeight;
    private int mVideoLayout;
    private int mVideoWidth;

    /* renamed from: io.vov.vitamio.widget.VideoView$3 */
    class C07563 implements Callback {
        C07563() {
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            VideoView.this.mSurfaceWidth = w;
            VideoView.this.mSurfaceHeight = h;
            boolean isValidState;
            if (VideoView.this.mTargetState == 3) {
                isValidState = true;
            } else {
                isValidState = false;
            }
            boolean hasValidSize;
            if (VideoView.this.mVideoWidth == w && VideoView.this.mVideoHeight == h) {
                hasValidSize = true;
            } else {
                hasValidSize = false;
            }
            if (VideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                if (VideoView.this.mSeekWhenPrepared != 0) {
                    VideoView.this.seekTo(VideoView.this.mSeekWhenPrepared);
                }
                VideoView.this.start();
                if (VideoView.this.mMediaController != null) {
                    if (VideoView.this.mMediaController.isShowing()) {
                        VideoView.this.mMediaController.hide();
                    }
                    VideoView.this.mMediaController.show();
                }
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            VideoView.this.mSurfaceHolder = holder;
            if (VideoView.this.mMediaPlayer != null && VideoView.this.mCurrentState == 6 && VideoView.this.mTargetState == 7) {
                VideoView.this.mMediaPlayer.setDisplay(VideoView.this.mSurfaceHolder);
                VideoView.this.resume();
                return;
            }
            VideoView.this.openVideo();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            VideoView.this.mSurfaceHolder = null;
            if (VideoView.this.mMediaController != null) {
                VideoView.this.mMediaController.hide();
            }
            VideoView.this.release(true);
        }
    }

    /* renamed from: io.vov.vitamio.widget.VideoView$1 */
    class C09451 implements OnVideoSizeChangedListener {
        C09451() {
        }

        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            Log.m31d("onVideoSizeChanged: (%dx%d)", Integer.valueOf(width), Integer.valueOf(height));
            VideoView.this.mVideoWidth = mp.getVideoWidth();
            VideoView.this.mVideoHeight = mp.getVideoHeight();
            VideoView.this.mVideoAspectRatio = mp.getVideoAspectRatio();
            if (VideoView.this.mVideoWidth != 0 && VideoView.this.mVideoHeight != 0) {
                VideoView.this.setVideoLayout(VideoView.this.mVideoLayout, VideoView.this.mAspectRatio);
            }
        }
    }

    /* renamed from: io.vov.vitamio.widget.VideoView$2 */
    class C09462 implements OnPreparedListener {
        C09462() {
        }

        public void onPrepared(MediaPlayer mp) {
            Log.m31d("onPrepared", new Object[0]);
            VideoView.this.mCurrentState = 2;
            if (VideoView.this.mOnPreparedListener != null) {
                VideoView.this.mOnPreparedListener.onPrepared(VideoView.this.mMediaPlayer);
            }
            if (VideoView.this.mMediaController != null) {
                VideoView.this.mMediaController.setEnabled(true);
            }
            VideoView.this.mVideoWidth = mp.getVideoWidth();
            VideoView.this.mVideoHeight = mp.getVideoHeight();
            VideoView.this.mVideoAspectRatio = mp.getVideoAspectRatio();
            long seekToPosition = VideoView.this.mSeekWhenPrepared;
            if (seekToPosition != 0) {
                VideoView.this.seekTo(seekToPosition);
            }
            if (VideoView.this.mVideoWidth != 0 && VideoView.this.mVideoHeight != 0) {
                VideoView.this.setVideoLayout(VideoView.this.mVideoLayout, VideoView.this.mAspectRatio);
                if (VideoView.this.mSurfaceWidth != VideoView.this.mVideoWidth || VideoView.this.mSurfaceHeight != VideoView.this.mVideoHeight) {
                    return;
                }
                if (VideoView.this.mTargetState == 3) {
                    VideoView.this.start();
                    if (VideoView.this.mMediaController != null) {
                        VideoView.this.mMediaController.show();
                    }
                } else if (!VideoView.this.isPlaying()) {
                    if ((seekToPosition != 0 || VideoView.this.getCurrentPosition() > 0) && VideoView.this.mMediaController != null) {
                        VideoView.this.mMediaController.show(0);
                    }
                }
            } else if (VideoView.this.mTargetState == 3) {
                VideoView.this.start();
            }
        }
    }

    /* renamed from: io.vov.vitamio.widget.VideoView$4 */
    class C09474 implements OnCompletionListener {
        C09474() {
        }

        public void onCompletion(MediaPlayer mp) {
            Log.m31d("onCompletion", new Object[0]);
            VideoView.this.mCurrentState = 5;
            VideoView.this.mTargetState = 5;
            if (VideoView.this.mMediaController != null) {
                VideoView.this.mMediaController.hide();
            }
            if (VideoView.this.mOnCompletionListener != null) {
                VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
            }
        }
    }

    /* renamed from: io.vov.vitamio.widget.VideoView$5 */
    class C09485 implements OnErrorListener {

        /* renamed from: io.vov.vitamio.widget.VideoView$5$1 */
        class C07571 implements OnClickListener {
            C07571() {
            }

            public void onClick(DialogInterface dialog, int whichButton) {
                if (VideoView.this.mOnCompletionListener != null) {
                    VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
                }
            }
        }

        C09485() {
        }

        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Log.m31d("Error: %d, %d", Integer.valueOf(framework_err), Integer.valueOf(impl_err));
            VideoView.this.mCurrentState = -1;
            VideoView.this.mTargetState = -1;
            if (VideoView.this.mMediaController != null) {
                VideoView.this.mMediaController.hide();
            }
            if ((VideoView.this.mOnErrorListener == null || !VideoView.this.mOnErrorListener.onError(VideoView.this.mMediaPlayer, framework_err, impl_err)) && VideoView.this.getWindowToken() != null) {
                new Builder(VideoView.this.mContext).setTitle(VideoView.this.getResources().getIdentifier("VideoView_error_title", "string", VideoView.this.mContext.getPackageName())).setMessage(framework_err == 200 ? VideoView.this.getResources().getIdentifier("VideoView_error_text_invalid_progressive_playback", "string", VideoView.this.mContext.getPackageName()) : VideoView.this.getResources().getIdentifier("VideoView_error_text_unknown", "string", VideoView.this.mContext.getPackageName())).setPositiveButton(VideoView.this.getResources().getIdentifier("VideoView_error_button", "string", VideoView.this.mContext.getPackageName()), new C07571()).setCancelable(false).show();
            }
            return true;
        }
    }

    /* renamed from: io.vov.vitamio.widget.VideoView$6 */
    class C09496 implements OnBufferingUpdateListener {
        C09496() {
        }

        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            VideoView.this.mCurrentBufferPercentage = percent;
            if (VideoView.this.mOnBufferingUpdateListener != null) {
                VideoView.this.mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
            }
        }
    }

    /* renamed from: io.vov.vitamio.widget.VideoView$7 */
    class C09507 implements OnInfoListener {
        C09507() {
        }

        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.m31d("onInfo: (%d, %d)", Integer.valueOf(what), Integer.valueOf(extra));
            if (1001 == what) {
                Log.m33e(" VITAMIO--TYPE_CHECK  stype  not include  onInfo mediaplayer unknow type ", new Object[0]);
            }
            if (704 == what) {
                VideoView.this.mMediaPlayer.audioInitedOk((long) VideoView.this.mMediaPlayer.audioTrackInit());
            }
            Log.m31d("onInfo: (%d, %d)", Integer.valueOf(what), Integer.valueOf(extra));
            if (VideoView.this.mOnInfoListener != null) {
                VideoView.this.mOnInfoListener.onInfo(mp, what, extra);
            } else if (VideoView.this.mMediaPlayer != null) {
                if (what == 701) {
                    VideoView.this.mMediaPlayer.pause();
                    if (VideoView.this.mMediaBufferingIndicator != null) {
                        VideoView.this.mMediaBufferingIndicator.setVisibility(0);
                    }
                } else if (what == 702) {
                    VideoView.this.mMediaPlayer.start();
                    if (VideoView.this.mMediaBufferingIndicator != null) {
                        VideoView.this.mMediaBufferingIndicator.setVisibility(8);
                    }
                }
            }
            return true;
        }
    }

    /* renamed from: io.vov.vitamio.widget.VideoView$8 */
    class C09518 implements OnSeekCompleteListener {
        C09518() {
        }

        public void onSeekComplete(MediaPlayer mp) {
            Log.m31d("onSeekComplete", new Object[0]);
            if (VideoView.this.mOnSeekCompleteListener != null) {
                VideoView.this.mOnSeekCompleteListener.onSeekComplete(mp);
            }
        }
    }

    /* renamed from: io.vov.vitamio.widget.VideoView$9 */
    class C09529 implements OnTimedTextListener {
        C09529() {
        }

        public void onTimedTextUpdate(byte[] pixels, int width, int height) {
            Log.m34i("onSubtitleUpdate: bitmap subtitle, %dx%d", Integer.valueOf(width), Integer.valueOf(height));
            if (VideoView.this.mOnTimedTextListener != null) {
                VideoView.this.mOnTimedTextListener.onTimedTextUpdate(pixels, width, height);
            }
        }

        public void onTimedText(String text) {
            Log.m34i("onSubtitleUpdate: %s", text);
            if (VideoView.this.mOnTimedTextListener != null) {
                VideoView.this.mOnTimedTextListener.onTimedText(text);
            }
        }
    }

    public VideoView(Context context) {
        super(context);
        this.mSizeChangedListener = new C09451();
        this.mPreparedListener = new C09462();
        this.mSHCallback = new C07563();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mAspectRatio = 0.0f;
        this.mVideoLayout = 1;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mVideoChroma = 1;
        this.mHardwareDecoder = false;
        this.mCompletionListener = new C09474();
        this.mErrorListener = new C09485();
        this.mBufferingUpdateListener = new C09496();
        this.mInfoListener = new C09507();
        this.mSeekCompleteListener = new C09518();
        this.mTimedTextListener = new C09529();
        initVideoView(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initVideoView(context);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSizeChangedListener = new C09451();
        this.mPreparedListener = new C09462();
        this.mSHCallback = new C07563();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mAspectRatio = 0.0f;
        this.mVideoLayout = 1;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mVideoChroma = 1;
        this.mHardwareDecoder = false;
        this.mCompletionListener = new C09474();
        this.mErrorListener = new C09485();
        this.mBufferingUpdateListener = new C09496();
        this.mInfoListener = new C09507();
        this.mSeekCompleteListener = new C09518();
        this.mTimedTextListener = new C09529();
        initVideoView(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(this.mVideoWidth, widthMeasureSpec), getDefaultSize(this.mVideoHeight, heightMeasureSpec));
    }

    public void setVideoLayout(int layout, float aspectRatio) {
        LayoutParams lp = getLayoutParams();
        lp.width = this.mSurfaceWidth;
        lp.height = this.mSurfaceHeight;
        setLayoutParams(lp);
        getHolder().setFixedSize(this.mSurfaceWidth, this.mSurfaceHeight);
        this.mVideoLayout = layout;
        this.mAspectRatio = aspectRatio;
    }

    private void initVideoView(Context ctx) {
        this.mContext = ctx;
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        getHolder().setFormat(1);
        getHolder().addCallback(this.mSHCallback);
        if (VERSION.SDK_INT < 11 && this.mHardwareDecoder) {
            getHolder().setType(3);
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        if (ctx instanceof Activity) {
            ((Activity) ctx).setVolumeControlStream(3);
        }
    }

    public boolean isValid() {
        return this.mSurfaceHolder != null && this.mSurfaceHolder.getSurface().isValid();
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    public void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
        }
    }

    private void openVideo() {
        if (this.mUri != null && this.mSurfaceHolder != null) {
            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "pause");
            this.mContext.sendBroadcast(i);
            release(false);
            try {
                int i2;
                this.mDuration = -1;
                this.mCurrentBufferPercentage = 0;
                this.mMediaPlayer = new MediaPlayer(this.mContext, this.mHardwareDecoder);
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
                this.mMediaPlayer.setOnTimedTextListener(this.mTimedTextListener);
                this.mMediaPlayer.setDataSource(this.mContext, this.mUri, this.mHeaders);
                this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
                this.mMediaPlayer.setBufferSize((long) this.mBufSize);
                MediaPlayer mediaPlayer = this.mMediaPlayer;
                if (this.mVideoChroma == 0) {
                    i2 = 0;
                } else {
                    i2 = 1;
                }
                mediaPlayer.setVideoChroma(i2);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync();
                this.mCurrentState = 1;
                attachMediaController();
            } catch (Throwable ex) {
                Log.m32e("Unable to open content: " + this.mUri, ex);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } catch (Throwable ex2) {
                Log.m32e("Unable to open content: " + this.mUri, ex2);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }
        }
    }

    public void setMediaController(MediaController controller) {
        if (this.mMediaController != null) {
            this.mMediaController.hide();
        }
        this.mMediaController = controller;
        attachMediaController();
    }

    public void setMediaBufferingIndicator(View mediaBufferingIndicator) {
        if (this.mMediaBufferingIndicator != null) {
            this.mMediaBufferingIndicator.setVisibility(8);
        }
        this.mMediaBufferingIndicator = mediaBufferingIndicator;
    }

    private void attachMediaController() {
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            this.mMediaController.setMediaPlayer(this);
            this.mMediaController.setAnchorView(getParent() instanceof View ? (View) getParent() : this);
            this.mMediaController.setEnabled(isInPlaybackState());
            if (this.mUri != null) {
                List<String> paths = this.mUri.getPathSegments();
                String name = (paths == null || paths.isEmpty()) ? "null" : (String) paths.get(paths.size() - 1);
                this.mMediaController.setFileName(name);
            }
        }
    }

    public void setOnPreparedListener(OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
        this.mOnBufferingUpdateListener = l;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener l) {
        this.mOnSeekCompleteListener = l;
    }

    public void setOnTimedTextListener(OnTimedTextListener l) {
        this.mOnTimedTextListener = l;
    }

    public void setOnInfoListener(OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    private void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && isKeyCodeSupported && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85 || keyCode == 62) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode == TransportMediator.KEYCODE_MEDIA_PLAY) {
                if (this.mMediaPlayer.isPlaying()) {
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode != 86 && keyCode != TransportMediator.KEYCODE_MEDIA_PAUSE) {
                toggleMediaControlsVisiblity();
            } else if (!this.mMediaPlayer.isPlaying()) {
                return true;
            } else {
                pause();
                this.mMediaController.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    public void start() {
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public void suspend() {
        if (isInPlaybackState()) {
            release(false);
            this.mCurrentState = 8;
            Log.m31d("Unable to suspend video. Release MediaPlayer.", new Object[0]);
        }
    }

    public void resume() {
        if (this.mSurfaceHolder == null && this.mCurrentState == 6) {
            this.mTargetState = 7;
        } else if (this.mCurrentState == 8) {
            openVideo();
        }
    }

    public long getDuration() {
        if (!isInPlaybackState()) {
            this.mDuration = -1;
            return this.mDuration;
        } else if (this.mDuration > 0) {
            return this.mDuration;
        } else {
            this.mDuration = this.mMediaPlayer.getDuration();
            return this.mDuration;
        }
    }

    public long getCurrentPosition() {
        if (isInPlaybackState()) {
            return this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(long msec) {
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo(msec);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = msec;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public float getVideoAspectRatio() {
        return this.mVideoAspectRatio;
    }

    public void setVideoChroma(int chroma) {
        getHolder().setFormat(chroma == 0 ? 4 : 1);
        this.mVideoChroma = chroma;
    }

    public void setHardwareDecoder(boolean hardware) {
        this.mHardwareDecoder = hardware;
    }

    public void setVideoQuality(int quality) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setVideoQuality(quality);
        }
    }

    public void setBufferSize(int bufSize) {
        this.mBufSize = bufSize;
    }

    public boolean isBuffering() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.isBuffering();
        }
        return false;
    }

    public String getMetaEncoding() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getMetaEncoding();
        }
        return null;
    }

    public void setMetaEncoding(String encoding) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setMetaEncoding(encoding);
        }
    }

    public SparseArray<MediaFormat> getAudioTrackMap(String encoding) {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.findTrackFromTrackInfo(2, this.mMediaPlayer.getTrackInfo(encoding));
        }
        return null;
    }

    public int getAudioTrack() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getAudioTrack();
        }
        return -1;
    }

    public void setAudioTrack(int audioIndex) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.selectTrack(audioIndex);
        }
    }

    public void setTimedTextShown(boolean shown) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setTimedTextShown(shown);
        }
    }

    public void setTimedTextEncoding(String encoding) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setTimedTextEncoding(encoding);
        }
    }

    public int getTimedTextLocation() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getTimedTextLocation();
        }
        return -1;
    }

    public void addTimedTextSource(String subPath) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.addTimedTextSource(subPath);
        }
    }

    public String getTimedTextPath() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getTimedTextPath();
        }
        return null;
    }

    public void setSubTrack(int trackId) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.selectTrack(trackId);
        }
    }

    public int getTimedTextTrack() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getTimedTextTrack();
        }
        return -1;
    }

    public SparseArray<MediaFormat> getSubTrackMap(String encoding) {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.findTrackFromTrackInfo(3, this.mMediaPlayer.getTrackInfo(encoding));
        }
        return null;
    }

    protected boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }
}
