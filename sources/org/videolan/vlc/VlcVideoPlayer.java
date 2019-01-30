package org.videolan.vlc;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import com.gemini.play.MGplayer;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.Media.EventListener;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.MediaPlayer.Event;
import org.videolan.vlc.listener.MediaListenerEvent;
import org.videolan.vlc.listener.MediaPlayerControl;
import org.videolan.vlc.listener.VideoSizeChange;
import org.videolan.vlc.util.LogUtils;
import org.videolan.vlc.util.VLCInstance;
import org.videolan.vlc.util.VLCOptions;

public class VlcVideoPlayer implements MediaPlayerControl, Callback, IVLCVout.Callback {
    private static final int MSG_START = 8;
    private static final int MSG_STOP = 9;
    private static boolean isInstance;
    private static boolean isSaveState;
    private static final HandlerThread sThread = new HandlerThread("VlcVideoPlayThread");
    private static MediaPlayer staticMediaPlayer;
    private String TAG = "VideoMediaLogic";
    private long abTimeEnd;
    private long abTimeStart;
    private boolean canInfo;
    private boolean canPause;
    private boolean canSeek;
    private boolean isABLoop;
    private boolean isAttachSurface;
    private boolean isAttached;
    private boolean isInitPlay;
    private boolean isInitStart;
    private boolean isLoop = true;
    private boolean isPlayError;
    private boolean isSeeking;
    private boolean isSufaceDelayerPlay;
    private boolean isViewLife;
    private LibVLC libVLC;
    private Context mContext;
    private final EventListener mMediaListener = new C09944();
    private MediaPlayer mMediaPlayer;
    private Handler mVideoHandler;
    private Handler mainHandler;
    private MediaListenerEvent mediaListenerEvent;
    private boolean othereMedia;
    private String path;
    private float speed = 1.0f;
    private SurfaceTexture surface;
    private final String tag = "VideoMediaLogic";
    private VideoSizeChange videoSizeChange;

    /* renamed from: org.videolan.vlc.VlcVideoPlayer$1 */
    class C08201 implements Runnable {
        C08201() {
        }

        public void run() {
            if (VlcVideoPlayer.this.isViewLife && VlcVideoPlayer.this.mediaListenerEvent != null) {
                VlcVideoPlayer.this.mediaListenerEvent.eventPlayInit(false);
            }
            VlcVideoPlayer.this.isViewLife = false;
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoPlayer$2 */
    class C08212 implements Runnable {
        C08212() {
        }

        public void run() {
            if (VlcVideoPlayer.this.isViewLife && VlcVideoPlayer.this.mediaListenerEvent != null) {
                VlcVideoPlayer.this.mediaListenerEvent.eventError(2, true);
            }
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoPlayer$5 */
    class C08225 implements Runnable {
        C08225() {
        }

        public void run() {
            VlcVideoPlayer.this.isViewLife = true;
            if (VlcVideoPlayer.this.isViewLife && VlcVideoPlayer.this.mediaListenerEvent != null) {
                VlcVideoPlayer.this.mediaListenerEvent.eventPlayInit(true);
            }
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoPlayer$6 */
    class C08236 implements Runnable {
        C08236() {
        }

        public void run() {
            if (VlcVideoPlayer.this.isViewLife && VlcVideoPlayer.this.mediaListenerEvent != null) {
                VlcVideoPlayer.this.mediaListenerEvent.eventStop(VlcVideoPlayer.this.isPlayError);
            }
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoPlayer$7 */
    class C08247 implements Runnable {
        C08247() {
        }

        public void run() {
            if (VlcVideoPlayer.this.isViewLife && VlcVideoPlayer.this.mediaListenerEvent != null) {
                VlcVideoPlayer.this.mediaListenerEvent.eventError(1, true);
            }
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoPlayer$8 */
    class C08258 implements Runnable {
        C08258() {
        }

        public void run() {
            if (VlcVideoPlayer.this.isViewLife && VlcVideoPlayer.this.mediaListenerEvent != null) {
                VlcVideoPlayer.this.mediaListenerEvent.eventPlay(true);
            }
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoPlayer$9 */
    class C08269 implements Runnable {
        C08269() {
        }

        public void run() {
            if (VlcVideoPlayer.this.isViewLife && VlcVideoPlayer.this.mediaListenerEvent != null) {
                VlcVideoPlayer.this.mediaListenerEvent.eventPlay(false);
            }
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoPlayer$3 */
    class C09933 implements MediaPlayer.EventListener {
        C09933() {
        }

        public void onEvent(Event event) {
            VlcVideoPlayer.this.onEventNative(event);
        }
    }

    /* renamed from: org.videolan.vlc.VlcVideoPlayer$4 */
    class C09944 implements EventListener {
        C09944() {
        }

        public void onEvent(Media.Event event) {
            boolean update = true;
            switch (event.type) {
                case 0:
                    LogUtils.m402i(VlcVideoPlayer.this.TAG, "Media.Event.MetaChanged:  =" + event.getMetaId());
                    break;
                case 3:
                    LogUtils.m402i(VlcVideoPlayer.this.TAG, "Media.Event.ParsedChanged  =" + event.getMetaId());
                    break;
                case 5:
                    LogUtils.m402i(VlcVideoPlayer.this.TAG, "StateChanged   =" + event.getMetaId());
                    break;
                default:
                    LogUtils.m402i(VlcVideoPlayer.this.TAG, "Media.Event.type=" + event.type + "   eventgetParsedStatus=" + event.getParsedStatus());
                    update = false;
                    break;
            }
            if (!update) {
            }
        }
    }

    static {
        sThread.start();
    }

    private static MediaPlayer getMediaPlayer(Context context) {
        if (!isInstance) {
            return new MediaPlayer(VLCInstance.get(context));
        }
        if (staticMediaPlayer == null) {
            staticMediaPlayer = new MediaPlayer(VLCInstance.get(context));
        }
        return staticMediaPlayer;
    }

    public void setInstance(boolean isInstance) {
        isInstance = isInstance;
    }

    public boolean handleMessage(Message msg) {
        synchronized (VlcVideoPlayer.class) {
            switch (msg.what) {
                case 8:
                    LogUtils.m402i(this.TAG, "-----HandlerThread init 1=" + this.isInitStart);
                    if (this.isInitStart) {
                        opendVideo();
                    }
                    LogUtils.m402i(this.TAG, "-----HandlerThread init 2=" + this.isInitStart);
                    break;
                case 9:
                    LogUtils.m402i(this.TAG, "-----HandlerThread stop 3=" + this.isInitStart);
                    release();
                    LogUtils.m402i(this.TAG, "-----HandlerThread stop 4=" + this.isInitStart);
                    break;
            }
        }
        return true;
    }

    public VlcVideoPlayer(Context context) {
        this.mContext = context.getApplicationContext();
        this.mVideoHandler = new Handler(sThread.getLooper(), this);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void setMediaPlayer(LibVLC libVLC) {
        this.mMediaPlayer = new MediaPlayer(libVLC);
    }

    public void setMedia(Media media) {
        this.othereMedia = true;
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = getMediaPlayer(this.mContext);
        }
        this.mMediaPlayer.setMedia(media);
    }

    public void setSurface(SurfaceTexture surface) {
        this.isAttached = true;
        this.surface = surface;
        if (this.isSufaceDelayerPlay) {
            this.isSufaceDelayerPlay = false;
            startPlay(this.path);
        }
    }

    public void onSurfaceTextureDestroyed() {
        this.isAttached = false;
        if (this.isAttachSurface) {
            this.isAttachSurface = false;
            pause();
            this.mMediaPlayer.getVLCVout().detachViews();
        }
    }

    public void onAttachedToWindow(boolean isViewLife) {
        this.isViewLife = isViewLife;
    }

    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public boolean isLoop() {
        return this.isLoop;
    }

    public void onStop() {
        this.mainHandler.post(new C08201());
        this.mVideoHandler.obtainMessage(9).sendToTarget();
    }

    private void opendVideo() {
        if (this.path == null || "".equals(this.path.trim()) || this.mContext == null || !this.isAttached) {
            this.mainHandler.post(new C08212());
            return;
        }
        this.isSufaceDelayerPlay = false;
        this.canSeek = false;
        this.canPause = false;
        this.isPlayError = false;
        if (this.libVLC == null) {
            this.libVLC = VLCInstance.get(this.mContext);
        }
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = getMediaPlayer(this.mContext);
        }
        this.mMediaPlayer.setAudioOutput(VLCOptions.getAout(PreferenceManager.getDefaultSharedPreferences(this.mContext)));
        this.mMediaPlayer.setEqualizer(VLCOptions.getEqualizer(this.mContext));
        if (!this.isAttachSurface && this.mMediaPlayer.getVLCVout().areViewsAttached()) {
            this.mMediaPlayer.getVLCVout().detachViews();
        }
        if (!this.othereMedia) {
            loadMedia();
        }
        if (!(this.mMediaPlayer.getVLCVout().areViewsAttached() || !this.isAttached || this.surface == null)) {
            this.isAttachSurface = true;
            this.mMediaPlayer.getVLCVout().setVideoSurface(this.surface);
            this.mMediaPlayer.getVLCVout().addCallback(this);
            this.mMediaPlayer.getVLCVout().attachViews();
            this.mMediaPlayer.setVideoTitleDisplay(-1, 0);
            LogUtils.m402i("VideoMediaLogic", "setVideoSurface   attachViews");
        }
        this.mMediaPlayer.setEventListener(new C09933());
        this.isInitPlay = true;
        this.othereMedia = false;
        LogUtils.m402i("VideoMediaLogic", "isAttached=" + this.isAttached + " isInitStart=" + this.isInitStart);
        if (this.isAttached && this.isInitStart && this.isAttachSurface) {
            this.mMediaPlayer.play();
        }
    }

    private void loadMedia() {
        Media media;
        if (isSaveState) {
            isSaveState = false;
            media = this.mMediaPlayer.getMedia();
            if (!(media == null || media.isReleased())) {
                this.canSeek = true;
                this.canPause = true;
                this.canInfo = true;
                return;
            }
        }
        if (this.path.contains("://")) {
            media = new Media(this.libVLC, Uri.parse(this.path));
            media.setHWDecoderEnabled(false, false);
            media.setEventListener(this.mMediaListener);
            this.mMediaPlayer.setMedia(media);
            media.release();
            return;
        }
        media = new Media(this.libVLC, this.path);
        media.setEventListener(this.mMediaListener);
        this.mMediaPlayer.setMedia(media);
        media.release();
    }

    public void saveState() {
        if (this.isInitPlay) {
            isSaveState = true;
            onStop();
        }
    }

    public void startPlay(String path) {
        this.path = path;
        this.isInitStart = true;
        this.mainHandler.post(new C08225());
        if (this.isAttached) {
            this.mVideoHandler.obtainMessage(8).sendToTarget();
        } else {
            this.isSufaceDelayerPlay = true;
        }
    }

    private void reStartPlay() {
        if (this.isAttached && this.isLoop && isPrepare()) {
            LogUtils.m402i("VideoMediaLogic", "reStartPlay setMedia");
            this.mMediaPlayer.setMedia(this.mMediaPlayer.getMedia());
            if (this.isAttached) {
                this.mMediaPlayer.play();
                return;
            }
            return;
        }
        this.mainHandler.post(new C08236());
    }

    private void release() {
        LogUtils.m402i("VideoMediaLogic", "release");
        this.canSeek = false;
        if (this.mMediaPlayer != null && this.isInitPlay) {
            LogUtils.m402i("VideoMediaLogic", "release SaveState  isAttachSurface=" + this.isAttachSurface);
            this.isInitPlay = false;
            if (this.isAttachSurface) {
                this.isAttachSurface = false;
                this.mMediaPlayer.getVLCVout().detachViews();
            }
            if (!isSaveState) {
                Media media = this.mMediaPlayer.getMedia();
                if (media != null) {
                    media.setEventListener(null);
                    this.mMediaPlayer.stop();
                    LogUtils.m402i("VideoMediaLogic", "release setMedia null");
                    this.mMediaPlayer.setMedia(null);
                    media.release();
                    isSaveState = false;
                }
            } else if (this.canPause) {
                this.mMediaPlayer.pause();
            }
            LogUtils.m402i("VideoMediaLogic", "release over");
        }
        this.canPause = false;
        this.isInitStart = false;
    }

    public void onDestory() {
        this.videoSizeChange = null;
        release();
        if (this.mMediaPlayer != null && !this.mMediaPlayer.isReleased()) {
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    private void onEventNative(final Event event) {
        switch (event.type) {
            case 256:
                LogUtils.m402i("VideoMediaLogic", "MediaChanged=" + event.getEsChangedType());
                return;
            case Event.Opening /*258*/:
                LogUtils.m402i("VideoMediaLogic", "Opening");
                this.canInfo = true;
                this.speed = 1.0f;
                this.mMediaPlayer.setRate(1.0f);
                return;
            case Event.Buffering /*259*/:
                if (event.getBuffering() == 100.0f) {
                    LogUtils.m402i(this.TAG, "MediaPlayer.Event.Buffering" + event.getBuffering());
                }
                this.mainHandler.post(new Runnable() {
                    public void run() {
                        if (VlcVideoPlayer.this.isViewLife && VlcVideoPlayer.this.mediaListenerEvent != null) {
                            VlcVideoPlayer.this.mediaListenerEvent.eventBuffing(event.getBuffering(), event.getBuffering() < 100.0f);
                        }
                    }
                });
                return;
            case Event.Playing /*260*/:
                LogUtils.m402i("VideoMediaLogic", "Playing");
                this.canInfo = true;
                this.mainHandler.post(new C08258());
                if (!this.isAttached || !this.mMediaPlayer.getVLCVout().areViewsAttached()) {
                    LogUtils.m402i("VideoMediaLogic", "---多线程出错----没有surface  禁止双线程后台运行");
                    if (this.mMediaPlayer != null) {
                        this.mMediaPlayer.pause();
                        return;
                    }
                    return;
                }
                return;
            case Event.Paused /*261*/:
                this.mainHandler.post(new C08269());
                LogUtils.m402i("VideoMediaLogic", "Paused");
                return;
            case Event.Stopped /*262*/:
                this.canInfo = false;
                this.canSeek = false;
                this.canPause = false;
                LogUtils.m402i("VideoMediaLogic", "Stopped  isLoop=" + this.isLoop + "  ");
                reStartPlay();
                return;
            case Event.EndReached /*265*/:
                LogUtils.m402i("VideoMediaLogic", "EndReached");
                return;
            case Event.EncounteredError /*266*/:
                this.isPlayError = true;
                this.canInfo = false;
                LogUtils.m402i("VideoMediaLogic", "EncounteredError");
                this.mainHandler.post(new C08247());
                return;
            case Event.TimeChanged /*267*/:
            case Event.PositionChanged /*268*/:
                return;
            case Event.SeekableChanged /*269*/:
                this.canSeek = event.getSeekable();
                LogUtils.m402i("VideoMediaLogic", "SeekableChanged=" + this.canSeek);
                return;
            case Event.PausableChanged /*270*/:
                this.canPause = event.getPausable();
                LogUtils.m402i("VideoMediaLogic", "PausableChanged=" + this.canPause);
                return;
            case Event.Vout /*274*/:
                LogUtils.m402i("VideoMediaLogic", "Vout" + event.getVoutCount());
                return;
            case Event.ESAdded /*276*/:
                LogUtils.m402i("VideoMediaLogic", "ESAdded");
                return;
            case Event.ESDeleted /*277*/:
                LogUtils.m402i("VideoMediaLogic", "ESDeleted");
                return;
            default:
                LogUtils.m402i("VideoMediaLogic", "event.type=" + event.type);
                return;
        }
    }

    public boolean isPrepare() {
        return (this.mMediaPlayer == null || !this.isInitPlay || this.isPlayError) ? false : true;
    }

    public boolean canControl() {
        return this.canPause && this.canInfo && this.canSeek;
    }

    public void start() {
        LogUtils.m402i("VideoMediaLogic", TtmlNode.START);
        if (isPrepare()) {
            this.mMediaPlayer.play();
        }
    }

    public void pause() {
        if (isPrepare() && this.canPause) {
            this.mMediaPlayer.pause();
        }
    }

    public long getDuration() {
        if (isPrepare() && this.canInfo) {
            return this.mMediaPlayer.getLength();
        }
        return 0;
    }

    public long getCurrentPosition() {
        if (isPrepare() && this.canInfo) {
            return (long) (((float) getDuration()) * this.mMediaPlayer.getPosition());
        }
        return 0;
    }

    public void seekTo(long pos) {
        if (isPrepare() && this.canSeek && !this.isSeeking) {
            this.isSeeking = true;
            MGplayer.MyPrintln("seekTo:" + this.mMediaPlayer.setTime(pos));
            this.isSeeking = false;
        }
    }

    public void setABLoop(boolean isABLoop) {
        this.isABLoop = isABLoop;
    }

    public boolean setABLoop(long abTimeA, long abTimeB) {
        if (!this.isABLoop || !isPrepare() || !this.canSeek) {
            this.abTimeStart = 0;
            this.abTimeEnd = 0;
            return false;
        } else if (abTimeB - abTimeA < 1000) {
            LogUtils.m402i("VideoMediaLogic", "时间少于1秒");
            return false;
        } else {
            this.abTimeStart = abTimeA;
            this.abTimeEnd = abTimeB;
            this.mMediaPlayer.setTime(abTimeA);
            return true;
        }
    }

    public boolean isPlaying() {
        if (isPrepare()) {
            return this.mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void setMirror(boolean mirror) {
    }

    public boolean getMirror() {
        return false;
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean setPlaybackSpeedMedia(float speed) {
        if (isPrepare() && this.canSeek) {
            this.speed = speed;
            this.mMediaPlayer.setRate(speed);
            seekTo(getCurrentPosition());
        }
        return true;
    }

    public float getPlaybackSpeed() {
        if (isPrepare() && this.canSeek) {
            return this.mMediaPlayer.getRate();
        }
        return this.speed;
    }

    public void setPause(boolean playing) {
        if (playing) {
            pause();
        }
    }

    public void setMediaListenerEvent(MediaListenerEvent mediaListenerEvent) {
        this.mediaListenerEvent = mediaListenerEvent;
    }

    public void setVideoSizeChange(VideoSizeChange videoSizeChange) {
        this.videoSizeChange = videoSizeChange;
    }

    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        if (this.videoSizeChange != null) {
            this.videoSizeChange.onVideoSizeChanged(width, height, visibleWidth, visibleHeight, sarNum, sarDen);
        }
    }

    public void onHardwareAccelerationError(IVLCVout vlcVout) {
    }

    public void onSurfacesCreated(IVLCVout vlcVout) {
    }

    public void onSurfacesDestroyed(IVLCVout vlcVout) {
    }
}
