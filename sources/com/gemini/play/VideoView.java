package com.gemini.play;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VideoView extends SurfaceView implements MediaPlayerControl {
    private String TAG;
    private boolean isStopTimeout;
    private String key1;
    private String key2;
    private String keyvalue1;
    private String keyvalue2;
    private OnBufferingUpdateListener mBufferingUpdateListener;
    private OnCompletionListener mCompletionListener;
    private Context mContext;
    private int mCurrentBufferPercentage;
    private int mDuration;
    private OnErrorListener mErrorListener;
    private OnInfoListener mInfoListener;
    private boolean mIsPause;
    private boolean mIsPrepared;
    public MediaController mMediaController;
    private MediaPlayer mMediaPlayer;
    private MySizeChangeLinstener mMyChangeLinstener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPreparedListener mOnPreparedListener;
    OnPreparedListener mPreparedListener;
    Callback mSHCallback;
    private OnSeekCompleteListener mSeekCompleteListener;
    private int mSeekWhenPrepared;
    OnVideoSizeChangedListener mSizeChangedListener;
    private boolean mStartWhenPrepared;
    private int mSurfaceHeight;
    private SurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoWidth;
    private boolean needChanged;
    private String path;
    private int playing_time;
    private boolean seekComplete;
    private boolean setLoop;
    private Runnable task;
    private Handler timehandler;
    private boolean v3orv2;

    /* renamed from: com.gemini.play.VideoView$1 */
    class C05821 extends Handler {
        C05821() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MGplayer.MyPrintln("playing_time:" + VideoView.this.playing_time);
                    if (VideoView.this.isPlaying()) {
                        VideoView.this.playing_time = VideoView.this.playing_time + 1;
                    }
                    if (!VideoView.this.isStopTimeout) {
                        Message msg2 = new Message();
                        msg2.what = 0;
                        if (VideoView.this.timehandler.hasMessages(0)) {
                            VideoView.this.timehandler.removeMessages(0);
                        }
                        VideoView.this.timehandler.sendMessageDelayed(msg2, 1000);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.VideoView$2 */
    class C05832 implements OnVideoSizeChangedListener {
        C05832() {
        }

        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            VideoView.this.mVideoWidth = mp.getVideoWidth();
            VideoView.this.mVideoHeight = mp.getVideoHeight();
            if (VideoView.this.mMyChangeLinstener != null) {
                VideoView.this.mMyChangeLinstener.doMyThings();
            }
            if (VideoView.this.mVideoWidth != 0 && VideoView.this.mVideoHeight != 0) {
                VideoView.this.getHolder().setFixedSize(VideoView.this.mVideoWidth, VideoView.this.mVideoHeight);
            }
        }
    }

    /* renamed from: com.gemini.play.VideoView$3 */
    class C05843 implements OnPreparedListener {
        C05843() {
        }

        public void onPrepared(MediaPlayer mp) {
            VideoView.this.mIsPrepared = true;
            if (VideoView.this.mOnPreparedListener != null) {
                VideoView.this.mOnPreparedListener.onPrepared(VideoView.this.mMediaPlayer);
            }
            if (VideoView.this.mMediaController != null) {
                VideoView.this.mMediaController.setEnabled(true);
            }
            VideoView.this.mVideoWidth = mp.getVideoWidth();
            VideoView.this.mVideoHeight = mp.getVideoHeight();
            if (VideoView.this.mVideoWidth == 0 || VideoView.this.mVideoHeight == 0) {
                if (VideoView.this.mSeekWhenPrepared != 0) {
                    VideoView.this.mMediaPlayer.seekTo(VideoView.this.mSeekWhenPrepared);
                    VideoView.this.mSeekWhenPrepared = 0;
                }
                if (VideoView.this.mStartWhenPrepared) {
                    VideoView.this.mMediaPlayer.start();
                    VideoView.this.mStartWhenPrepared = false;
                    return;
                }
                return;
            }
            VideoView.this.getHolder().setFixedSize(VideoView.this.mVideoWidth, VideoView.this.mVideoHeight);
            if (VideoView.this.mSurfaceWidth == VideoView.this.mVideoWidth && VideoView.this.mSurfaceHeight == VideoView.this.mVideoHeight) {
                if (VideoView.this.mSeekWhenPrepared != 0) {
                    VideoView.this.mMediaPlayer.seekTo(VideoView.this.mSeekWhenPrepared);
                    VideoView.this.mSeekWhenPrepared = 0;
                }
                if (VideoView.this.mStartWhenPrepared) {
                    VideoView.this.mMediaPlayer.start();
                    VideoView.this.mStartWhenPrepared = false;
                    if (VideoView.this.mMediaController != null) {
                        VideoView.this.mMediaController.show();
                    }
                } else if (!VideoView.this.isPlaying()) {
                    if ((VideoView.this.mSeekWhenPrepared != 0 || VideoView.this.getCurrentPosition() > 0) && VideoView.this.mMediaController != null) {
                        VideoView.this.mMediaController.show(0);
                    }
                }
            }
        }
    }

    /* renamed from: com.gemini.play.VideoView$4 */
    class C05854 implements OnCompletionListener {
        C05854() {
        }

        public void onCompletion(MediaPlayer mp) {
            Log.d(VideoView.this.TAG, "onCompletion 1");
            if (VideoView.this.mMediaController != null) {
                VideoView.this.mMediaController.hide();
            }
            if (VideoView.this.mOnCompletionListener != null) {
                Log.d(VideoView.this.TAG, "onCompletion 2");
                VideoView.this.mOnCompletionListener.onCompletion(VideoView.this.mMediaPlayer);
            }
        }
    }

    /* renamed from: com.gemini.play.VideoView$5 */
    class C05865 implements OnErrorListener {
        C05865() {
        }

        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Log.d(VideoView.this.TAG, "Error: " + framework_err + "," + impl_err);
            if (VideoView.this.mMediaController != null) {
                VideoView.this.mMediaController.hide();
            }
            if (VideoView.this.mOnErrorListener != null) {
                if (VideoView.this.mOnErrorListener.onError(VideoView.this.mMediaPlayer, framework_err, impl_err)) {
                    Log.v("play error+++++", "Error: " + framework_err + "," + impl_err);
                    MGplayer.MyPrintln("statr open video+++++++" + framework_err + "," + impl_err);
                }
                if (VideoView.this.getWindowToken() != null) {
                    VideoView.this.mContext.getResources();
                }
            } else {
                MGplayer.MyPrintln("error it's null++++++++++++++++++++++++++++=================");
                if (VideoView.this.getWindowToken() != null) {
                    VideoView.this.mContext.getResources();
                }
            }
            return true;
        }
    }

    /* renamed from: com.gemini.play.VideoView$6 */
    class C05876 implements OnBufferingUpdateListener {
        C05876() {
        }

        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            VideoView.this.mCurrentBufferPercentage = percent;
        }
    }

    /* renamed from: com.gemini.play.VideoView$7 */
    class C05887 implements OnSeekCompleteListener {
        C05887() {
        }

        public void onSeekComplete(MediaPlayer mp) {
            VideoView.this.seekComplete = true;
        }
    }

    /* renamed from: com.gemini.play.VideoView$8 */
    class C05898 implements OnInfoListener {
        C05898() {
        }

        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return VideoView.this.mOnInfoListener.onInfo(mp, what, extra);
        }
    }

    /* renamed from: com.gemini.play.VideoView$9 */
    class C05909 implements Callback {
        C05909() {
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            MGplayer.MyPrintln("surfaceChanged++++++++++++++++++++++++++++surfaceChanged");
            VideoView.this.mSurfaceWidth = w;
            VideoView.this.mSurfaceHeight = h;
            if (VideoView.this.mMediaPlayer != null && VideoView.this.mIsPrepared) {
                MGplayer.MyPrintln("surfaceChanged2++++++++++++++++++++++++++++surfaceChanged2");
                if (VideoView.this.mSeekWhenPrepared != 0) {
                    VideoView.this.mMediaPlayer.seekTo(VideoView.this.mSeekWhenPrepared);
                    VideoView.this.mSeekWhenPrepared = 0;
                }
                VideoView.this.mMediaPlayer.start();
                if (VideoView.this.mMediaController != null) {
                    VideoView.this.mMediaController.show();
                }
            }
            VideoView.this.setSurface(false);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            MGplayer.MyPrintln("surfaceCreated++++++++++++++++++++++++++++surfaceCreated");
            VideoView.this.mSurfaceHolder = holder;
            VideoView.this.openVideo();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            MGplayer.MyPrintln("surfaceDestroyed++++++++++++++++++++++++++++surfaceDestroyed");
            VideoView.this.mSurfaceHolder = null;
            if (VideoView.this.mMediaController != null) {
                VideoView.this.mMediaController.hide();
            }
            if (VideoView.this.mMediaPlayer != null) {
                VideoView.this.mMediaPlayer.reset();
                VideoView.this.mMediaPlayer.release();
                VideoView.this.mMediaPlayer = null;
            }
        }
    }

    public interface MySizeChangeLinstener {
        void doMyThings();
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public void setVideoScale(int width, int height) {
        LayoutParams lp = getLayoutParams();
        lp.height = height;
        lp.width = width;
        setLayoutParams(lp);
    }

    public void setMySizeChangeLinstener(MySizeChangeLinstener l) {
        this.mMyChangeLinstener = l;
    }

    public VideoView(Context context) {
        super(context);
        this.TAG = "VideoView";
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.path = null;
        this.v3orv2 = false;
        this.needChanged = false;
        this.seekComplete = true;
        this.setLoop = false;
        this.timehandler = null;
        this.task = null;
        this.playing_time = 0;
        this.isStopTimeout = false;
        this.key1 = null;
        this.key2 = null;
        this.keyvalue1 = null;
        this.keyvalue2 = null;
        this.mSizeChangedListener = new C05832();
        this.mPreparedListener = new C05843();
        this.mCompletionListener = new C05854();
        this.mErrorListener = new C05865();
        this.mBufferingUpdateListener = new C05876();
        this.mSeekCompleteListener = new C05887();
        this.mInfoListener = new C05898();
        this.mSHCallback = new C05909();
        this.mContext = context;
        initVideoView();
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
        initVideoView();
    }

    public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.TAG = "VideoView";
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.path = null;
        this.v3orv2 = false;
        this.needChanged = false;
        this.seekComplete = true;
        this.setLoop = false;
        this.timehandler = null;
        this.task = null;
        this.playing_time = 0;
        this.isStopTimeout = false;
        this.key1 = null;
        this.key2 = null;
        this.keyvalue1 = null;
        this.keyvalue2 = null;
        this.mSizeChangedListener = new C05832();
        this.mPreparedListener = new C05843();
        this.mCompletionListener = new C05854();
        this.mErrorListener = new C05865();
        this.mBufferingUpdateListener = new C05876();
        this.mSeekCompleteListener = new C05887();
        this.mInfoListener = new C05898();
        this.mSHCallback = new C05909();
        this.mContext = context;
        initVideoView();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(this.mVideoWidth, widthMeasureSpec), getDefaultSize(this.mVideoHeight, heightMeasureSpec));
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case Integer.MIN_VALUE:
                return Math.min(desiredSize, specSize);
            case 0:
                return desiredSize;
            case 1073741824:
                return specSize;
            default:
                return result;
        }
    }

    private void initVideoView() {
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        getHolder().addCallback(this.mSHCallback);
        getHolder().setType(3);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.timehandler = new C05821();
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoUrl(String path1) {
        this.playing_time = 0;
        this.path = path1;
        this.mStartWhenPrepared = false;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void setVideoURI(Uri uri) {
        this.playing_time = 0;
        this.mUri = uri;
        this.mStartWhenPrepared = false;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            this.playing_time = 0;
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    private void openVideo() {
        MGplayer.MyPrintln("statr open video+++++++");
        if ((this.mUri != null && this.mSurfaceHolder != null) || (this.path != null && this.mSurfaceHolder != null)) {
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.reset();
            } else {
                this.mMediaPlayer = new MediaPlayer();
            }
            try {
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mIsPrepared = false;
                this.mDuration = -1;
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
                this.mCurrentBufferPercentage = 0;
                if (this.mUri == null) {
                    this.mMediaPlayer.setDataSource(this.path);
                } else if (this.key1 == null && this.key2 == null) {
                    this.mMediaPlayer.setDataSource(this.mContext, this.mUri);
                } else {
                    Map<String, String> headers = new HashMap();
                    headers.put(this.key1, this.keyvalue1);
                    headers.put(this.key2, this.keyvalue2);
                    this.mMediaPlayer.setDataSource(this.mContext, this.mUri, headers);
                }
                this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync();
                this.mMediaPlayer.setLooping(this.setLoop);
                attachMediaController();
            } catch (IOException ex) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex);
            } catch (IllegalArgumentException ex2) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex2);
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

    private void attachMediaController() {
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            View anchorView;
            this.mMediaController.setMediaPlayer(this);
            if (getParent() instanceof View) {
                anchorView = (View) getParent();
            } else {
                anchorView = this;
            }
            this.mMediaController.setAnchorView(anchorView);
            this.mMediaController.setEnabled(this.mIsPrepared);
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

    public void setOnInfoListener(OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    public void setSurface(boolean need) {
        this.needChanged = true;
    }

    public void setSurfaceResetVideo() {
        openVideo();
        this.mMediaPlayer.start();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!(!this.mIsPrepared || this.mMediaPlayer == null || this.mMediaController == null)) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        if (!(!this.mIsPrepared || this.mMediaPlayer == null || this.mMediaController == null)) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!(!this.mIsPrepared || keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 82 || keyCode == 5 || keyCode == 6 || this.mMediaPlayer == null || this.mMediaController == null)) {
            if (keyCode == 79 || keyCode == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                } else {
                    start();
                    this.mMediaController.hide();
                }
                return true;
            } else if (keyCode == 86 && this.mMediaPlayer.isPlaying()) {
                pause();
                this.mMediaController.show();
            } else {
                toggleMediaControlsVisiblity();
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
        if (this.mMediaPlayer == null || !this.mIsPrepared) {
            this.mStartWhenPrepared = true;
        } else {
            this.mMediaPlayer.start();
            this.mStartWhenPrepared = false;
        }
        this.mIsPause = false;
        this.isStopTimeout = false;
        Message msg2 = new Message();
        msg2.what = 0;
        if (this.timehandler.hasMessages(0)) {
            this.timehandler.removeMessages(0);
        }
        this.timehandler.sendMessageDelayed(msg2, 1000);
    }

    public void pause() {
        if (this.mMediaPlayer != null && this.mIsPrepared && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
        }
        this.mStartWhenPrepared = false;
        this.mIsPause = true;
    }

    public boolean isPause() {
        return this.mIsPause;
    }

    public void stop() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
        }
        this.isStopTimeout = true;
    }

    public void reset() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
        }
        this.isStopTimeout = true;
    }

    public void release() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
        }
        this.isStopTimeout = true;
    }

    public void setLooping(boolean loop) {
        this.setLoop = loop;
    }

    public int getDuration() {
        if (this.mMediaPlayer == null || !this.mIsPrepared) {
            this.mDuration = -1;
            return this.mDuration;
        } else if (this.mDuration > 0) {
            return this.mDuration;
        } else {
            this.mDuration = this.mMediaPlayer.getDuration();
            return this.mDuration;
        }
    }

    public int getCurrentPosition() {
        if (this.mMediaPlayer == null || !this.mIsPrepared) {
            return 0;
        }
        return this.mMediaPlayer.getCurrentPosition();
    }

    public long getCurrentPosition2() {
        return ((long) this.playing_time) * 1000;
    }

    public void seekTo(int msec) {
        if (this.mMediaPlayer == null || !this.mIsPrepared) {
            this.mSeekWhenPrepared = msec;
        } else {
            this.seekComplete = false;
            this.mMediaPlayer.seekTo(msec);
        }
        this.playing_time = 0;
    }

    public boolean isPlaying() {
        if (this.mMediaPlayer != null && this.mIsPrepared) {
            try {
                return this.mMediaPlayer.isPlaying();
            } catch (Exception e) {
            }
        }
        return false;
    }

    public boolean getSeekComplete() {
        return this.seekComplete;
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public boolean canPause() {
        return true;
    }

    public void v3_v2(boolean bool) {
        this.v3orv2 = bool;
        this.v3orv2 = false;
    }

    public void setKeyValue(String k1, String v1, String k2, String v2) {
        this.key1 = k1;
        this.key2 = k2;
        this.keyvalue1 = v1;
        this.keyvalue2 = v2;
    }

    public int getAudioSessionId() {
        return 0;
    }
}
