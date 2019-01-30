package org.videolan.vlc;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import com.gemini.play.MGplayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.vlc.listener.MediaListenerEvent;
import org.videolan.vlc.listener.MediaPlayerControl;
import org.videolan.vlc.listener.VideoSizeChange;
import org.videolan.vlc.util.LogUtils;

public class VlcVideoView extends TextureView implements MediaPlayerControl, SurfaceTextureListener, VideoSizeChange {
    private boolean isRotation;
    private boolean isStopTimeout;
    private int mVideoHeight;
    private int mVideoWidth;
    private boolean mirror;
    private int playing_time;
    private final String tag;
    private Runnable task;
    private Handler timehandler;
    private VlcVideoPlayer videoMediaLogic;

    /* renamed from: org.videolan.vlc.VlcVideoView$1 */
    class C08271 implements Runnable {
        C08271() {
        }

        public void run() {
            MGplayer.MyPrintln("playing_time:" + VlcVideoView.this.playing_time);
            if (VlcVideoView.this.isPlaying()) {
                VlcVideoView.this.playing_time = VlcVideoView.this.playing_time + 1;
            }
            if (!VlcVideoView.this.isStopTimeout) {
                VlcVideoView.this.timehandler.postDelayed(this, 1000);
            }
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoView$2 */
    class C08282 implements Runnable {
        C08282() {
        }

        public void run() {
            VlcVideoView.this.adjustAspectRatio(VlcVideoView.this.mVideoWidth, VlcVideoView.this.mVideoHeight);
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoView$3 */
    class C08293 implements Runnable {
        C08293() {
        }

        public void run() {
            VlcVideoView.this.adjustAspectRatio(VlcVideoView.this.mVideoWidth, VlcVideoView.this.mVideoHeight);
        }
    }

    public VlcVideoView(Context context) {
        this(context, null);
    }

    public VlcVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VlcVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.tag = "VideoView";
        this.timehandler = new Handler();
        this.task = null;
        this.playing_time = 0;
        this.isStopTimeout = false;
        this.mirror = false;
        this.isRotation = true;
        if (!isInEditMode()) {
            init(context);
        }
    }

    public void setMediaListenerEvent(MediaListenerEvent mediaListenerEvent) {
        this.videoMediaLogic.setMediaListenerEvent(mediaListenerEvent);
    }

    public boolean canControl() {
        return this.videoMediaLogic.canControl();
    }

    public void onStop() {
        this.videoMediaLogic.onStop();
    }

    public void onDestory() {
        if (this.videoMediaLogic != null) {
            this.videoMediaLogic.onDestory();
        }
        this.playing_time = 0;
        LogUtils.m402i("VideoView", "onDestory");
    }

    public void init(Context context) {
        this.videoMediaLogic = new VlcVideoPlayer(context);
        this.videoMediaLogic.setVideoSizeChange(this);
        setSurfaceTextureListener(this);
        this.task = new C08271();
    }

    public void setMediaPlayer(LibVLC libVLC) {
        this.videoMediaLogic.setMediaPlayer(libVLC);
    }

    public void setMedia(Media media) {
        this.videoMediaLogic.setMedia(media);
    }

    public boolean isPrepare() {
        return this.videoMediaLogic.isPrepare();
    }

    public void startPlay(String path) {
        this.playing_time = 0;
        this.isStopTimeout = false;
        this.timehandler.post(this.task);
        this.videoMediaLogic.startPlay(path);
    }

    public void stopTimeout() {
        this.isStopTimeout = true;
    }

    public void saveState() {
        this.videoMediaLogic.saveState();
    }

    public void start() {
        this.videoMediaLogic.start();
    }

    public void pause() {
        this.videoMediaLogic.pause();
    }

    public long getDuration() {
        return this.videoMediaLogic.getDuration();
    }

    public long getCurrentPosition() {
        return this.videoMediaLogic.getCurrentPosition();
    }

    public long getCurrentPosition2() {
        return ((long) this.playing_time) * 1000;
    }

    public void seekTo(long pos) {
        this.videoMediaLogic.seekTo(pos);
    }

    public boolean isPlaying() {
        return this.videoMediaLogic.isPlaying();
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
        if (mirror) {
            setScaleX(-1.0f);
        } else {
            setScaleX(1.0f);
        }
    }

    public boolean getMirror() {
        return this.mirror;
    }

    public int getBufferPercentage() {
        return this.videoMediaLogic.getBufferPercentage();
    }

    public boolean setPlaybackSpeedMedia(float speed) {
        return this.videoMediaLogic.setPlaybackSpeedMedia(speed);
    }

    public float getPlaybackSpeed() {
        return this.videoMediaLogic.getPlaybackSpeed();
    }

    public void setLoop(boolean isLoop) {
        this.videoMediaLogic.setLoop(isLoop);
    }

    public boolean isLoop() {
        return this.videoMediaLogic.isLoop();
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtils.m402i("VideoView", "onSurfaceTextureAvailable");
        this.videoMediaLogic.setSurface(surface);
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtils.m402i("VideoView", "onSurfaceTextureSizeChanged");
        post(new C08282());
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtils.m402i("VideoView", "onSurfaceTextureDestroyed");
        this.videoMediaLogic.onSurfaceTextureDestroyed();
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtils.m402i("VideoView", "onAttachedToWindow");
        if (!isInEditMode()) {
            setKeepScreenOn(true);
            if (this.videoMediaLogic != null) {
                this.videoMediaLogic.onAttachedToWindow(true);
            }
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.m402i("VideoView", "onDetachedFromWindow");
        if (!isInEditMode()) {
            setKeepScreenOn(false);
            if (this.videoMediaLogic != null) {
                this.videoMediaLogic.onAttachedToWindow(false);
            }
        }
    }

    public boolean isRotation() {
        return this.isRotation;
    }

    private void adjustAspectRatio(int videoWidth, int videoHeight) {
        if (videoWidth * videoHeight != 0) {
            int newWidth;
            int newHeight;
            if (videoWidth > videoHeight) {
                this.isRotation = true;
            } else {
                this.isRotation = false;
            }
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            double videoRatio = ((double) viewWidth) / ((double) viewHeight);
            double aspectRatio = ((double) videoWidth) / ((double) videoHeight);
            if (videoWidth <= videoHeight) {
                newWidth = (int) (((double) viewHeight) * aspectRatio);
                newHeight = viewHeight;
            } else if (videoRatio > aspectRatio) {
                newWidth = viewWidth;
                newHeight = viewHeight;
            } else {
                newWidth = viewWidth;
                newHeight = viewHeight;
            }
            float xoff = ((float) (viewWidth - newWidth)) / 2.0f;
            float yoff = ((float) (viewHeight - newHeight)) / 2.0f;
            Matrix txform = new Matrix();
            getTransform(txform);
            txform.setScale(((float) newWidth) / ((float) viewWidth), ((float) newHeight) / ((float) viewHeight));
            txform.postTranslate(xoff, yoff);
            setTransform(txform);
            LogUtils.m402i("VideoView", "video=" + videoWidth + "x" + videoHeight + " view=" + viewWidth + "x" + viewHeight + " newView=" + newWidth + "x" + newHeight + " off=" + xoff + "," + yoff);
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            adjustAspectRatio(this.mVideoWidth, this.mVideoHeight);
        }
    }

    public void onVideoSizeChanged(int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        LogUtils.m402i("VideoView", "onVideoSizeChanged   video=" + width + "x" + width + " visible=" + visibleWidth + "x" + visibleHeight + "   sarNum=" + sarNum + "x" + sarDen);
        if (width * height != 0) {
            this.mVideoWidth = visibleWidth;
            this.mVideoHeight = visibleHeight;
            post(new C08293());
        }
    }
}
