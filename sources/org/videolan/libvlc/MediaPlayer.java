package org.videolan.libvlc;

import android.net.Uri;
import java.io.File;
import org.videolan.libvlc.AWindow.SurfaceCallback;
import org.videolan.libvlc.VLCEvent.Listener;

public class MediaPlayer extends VLCObject<Event> {
    private String mAudioOutput = null;
    private String mAudioOutputDevice = null;
    private boolean mAudioReset = false;
    private Media mMedia = null;
    private boolean mPlayRequested = false;
    private boolean mPlaying = false;
    private int mVoutCount = 0;
    private final AWindow mWindow = new AWindow(new C09691());

    public static class Chapter {
        public final long duration;
        public final String name;
        public final long timeOffset;

        private Chapter(long timeOffset, long duration, String name) {
            this.timeOffset = timeOffset;
            this.duration = duration;
            this.name = name;
        }
    }

    public static class Equalizer {
        private long mInstance;

        private native float nativeGetAmp(int i);

        private static native int nativeGetBandCount();

        private static native float nativeGetBandFrequency(int i);

        private native float nativeGetPreAmp();

        private static native int nativeGetPresetCount();

        private static native String nativeGetPresetName(int i);

        private native void nativeNew();

        private native void nativeNewFromPreset(int i);

        private native void nativeRelease();

        private native boolean nativeSetAmp(int i, float f);

        private native boolean nativeSetPreAmp(float f);

        private Equalizer() {
            nativeNew();
        }

        private Equalizer(int index) {
            nativeNewFromPreset(index);
        }

        protected void finalize() throws Throwable {
            try {
                nativeRelease();
            } finally {
                super.finalize();
            }
        }

        public static Equalizer create() {
            return new Equalizer();
        }

        public static Equalizer createFromPreset(int index) {
            return new Equalizer(index);
        }

        public static int getPresetCount() {
            return nativeGetPresetCount();
        }

        public static String getPresetName(int index) {
            return nativeGetPresetName(index);
        }

        public static int getBandCount() {
            return nativeGetBandCount();
        }

        public static float getBandFrequency(int index) {
            return nativeGetBandFrequency(index);
        }

        public float getPreAmp() {
            return nativeGetPreAmp();
        }

        public boolean setPreAmp(float preamp) {
            return nativeSetPreAmp(preamp);
        }

        public float getAmp(int index) {
            return nativeGetAmp(index);
        }

        public boolean setAmp(int index, float amp) {
            return nativeSetAmp(index, amp);
        }
    }

    public static class Navigate {
        public static final int Activate = 0;
        public static final int Down = 2;
        public static final int Left = 3;
        public static final int Right = 4;
        public static final int Up = 1;
    }

    public static class Position {
        public static final int Bottom = 6;
        public static final int BottomLeft = 7;
        public static final int BottomRight = 8;
        public static final int Center = 0;
        public static final int Disable = -1;
        public static final int Left = 1;
        public static final int Right = 2;
        public static final int Top = 3;
        public static final int TopLeft = 4;
        public static final int TopRight = 5;
    }

    public static class Title {
        public final long duration;
        private final int flags;
        public final String name;

        private static class Flags {
            public static final int INTERACTIVE = 2;
            public static final int MENU = 1;

            private Flags() {
            }
        }

        public Title(long duration, String name, int flags) {
            this.duration = duration;
            this.name = name;
            this.flags = flags;
        }

        public boolean isMenu() {
            return (this.flags & 1) != 0;
        }

        public boolean isInteractive() {
            return (this.flags & 2) != 0;
        }
    }

    public static class TrackDescription {
        public final int id;
        public final String name;

        private TrackDescription(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /* renamed from: org.videolan.libvlc.MediaPlayer$1 */
    class C09691 implements SurfaceCallback {
        C09691() {
        }

        public void onSurfacesCreated(AWindow vout) {
            boolean play = false;
            boolean enableVideo = false;
            synchronized (MediaPlayer.this) {
                if (!MediaPlayer.this.mPlaying && MediaPlayer.this.mPlayRequested) {
                    play = true;
                } else if (MediaPlayer.this.mVoutCount == 0) {
                    enableVideo = true;
                }
            }
            if (play) {
                MediaPlayer.this.play();
            } else if (enableVideo) {
                MediaPlayer.this.setVideoTrackEnabled(true);
            }
        }

        public void onSurfacesDestroyed(AWindow vout) {
            boolean disableVideo = false;
            synchronized (MediaPlayer.this) {
                if (MediaPlayer.this.mVoutCount > 0) {
                    disableVideo = true;
                }
            }
            if (disableVideo) {
                MediaPlayer.this.setVideoTrackEnabled(false);
            }
        }
    }

    public static class Event extends VLCEvent {
        public static final int Buffering = 259;
        public static final int ESAdded = 276;
        public static final int ESDeleted = 277;
        public static final int EncounteredError = 266;
        public static final int EndReached = 265;
        public static final int MediaChanged = 256;
        public static final int Opening = 258;
        public static final int PausableChanged = 270;
        public static final int Paused = 261;
        public static final int Playing = 260;
        public static final int PositionChanged = 268;
        public static final int SeekableChanged = 269;
        public static final int Stopped = 262;
        public static final int TimeChanged = 267;
        public static final int Vout = 274;

        protected Event(int type) {
            super(type);
        }

        protected Event(int type, long arg1) {
            super(type, arg1);
        }

        protected Event(int type, float arg2) {
            super(type, arg2);
        }

        public long getTimeChanged() {
            return this.arg1;
        }

        public float getPositionChanged() {
            return this.arg2;
        }

        public int getVoutCount() {
            return (int) this.arg1;
        }

        public int getEsChangedType() {
            return (int) this.arg1;
        }

        public boolean getPausable() {
            return this.arg1 != 0;
        }

        public boolean getSeekable() {
            return this.arg1 != 0;
        }

        public float getBuffering() {
            return this.arg2;
        }
    }

    public interface EventListener extends Listener<Event> {
    }

    private native boolean nativeAddSlave(int i, String str, boolean z);

    private native long nativeGetAudioDelay();

    private native int nativeGetAudioTrack();

    private native TrackDescription[] nativeGetAudioTracks();

    private native int nativeGetAudioTracksCount();

    private native Chapter[] nativeGetChapters(int i);

    private native long nativeGetSpuDelay();

    private native int nativeGetSpuTrack();

    private native TrackDescription[] nativeGetSpuTracks();

    private native int nativeGetSpuTracksCount();

    private native Title[] nativeGetTitles();

    private native int nativeGetVideoTrack();

    private native TrackDescription[] nativeGetVideoTracks();

    private native int nativeGetVideoTracksCount();

    private native void nativeNewFromLibVlc(LibVLC libVLC, AWindowNativeHandler aWindowNativeHandler);

    private native void nativeNewFromMedia(Media media, AWindowNativeHandler aWindowNativeHandler);

    private native void nativePlay();

    private native void nativeRelease();

    private native boolean nativeSetAudioDelay(long j);

    private native boolean nativeSetAudioOutput(String str);

    private native boolean nativeSetAudioOutputDevice(String str);

    private native boolean nativeSetAudioTrack(int i);

    private native boolean nativeSetEqualizer(Equalizer equalizer);

    private native void nativeSetMedia(Media media);

    private native boolean nativeSetSpuDelay(long j);

    private native boolean nativeSetSpuTrack(int i);

    private native void nativeSetVideoTitleDisplay(int i, int i2);

    private native boolean nativeSetVideoTrack(int i);

    private native void nativeStop();

    public native int getChapter();

    public native long getLength();

    public native int getPlayerState();

    public native float getPosition();

    public native float getRate();

    public native long getTime();

    public native int getTitle();

    public native int getVolume();

    public native boolean isPlaying();

    public native boolean isSeekable();

    public native void navigate(int i);

    public native int nextChapter();

    public native void pause();

    public native int previousChapter();

    public native void setChapter(int i);

    public native void setPosition(float f);

    public native void setRate(float f);

    public native long setTime(long j);

    public native void setTitle(int i);

    public native int setVolume(int i);

    public /* bridge */ /* synthetic */ boolean isReleased() {
        return super.isReleased();
    }

    private static Title createTitleFromNative(long duration, String name, int flags) {
        return new Title(duration, name, flags);
    }

    private static Chapter createChapterFromNative(long timeOffset, long duration, String name) {
        return new Chapter(timeOffset, duration, name);
    }

    private static TrackDescription createTrackDescriptionFromNative(int id, String name) {
        return new TrackDescription(id, name);
    }

    public MediaPlayer(LibVLC libVLC) {
        nativeNewFromLibVlc(libVLC, this.mWindow.getNativeHandler());
    }

    public MediaPlayer(Media media) {
        if (media == null || media.isReleased()) {
            throw new IllegalArgumentException("Media is null or released");
        }
        this.mMedia = media;
        this.mMedia.retain();
        nativeNewFromMedia(this.mMedia, this.mWindow.getNativeHandler());
    }

    public IVLCVout getVLCVout() {
        return this.mWindow;
    }

    public void setMedia(Media media) {
        if (media != null) {
            if (media.isReleased()) {
                throw new IllegalArgumentException("Media is released");
            }
            media.setDefaultMediaPlayerOptions();
        }
        nativeSetMedia(media);
        synchronized (this) {
            if (this.mMedia != null) {
                this.mMedia.release();
            }
            if (media != null) {
                media.retain();
            }
            this.mMedia = media;
        }
    }

    public synchronized Media getMedia() {
        if (this.mMedia != null) {
            this.mMedia.retain();
        }
        return this.mMedia;
    }

    public void play() {
        synchronized (this) {
            if (!this.mPlaying) {
                if (this.mAudioReset) {
                    if (this.mAudioOutput != null) {
                        nativeSetAudioOutput(this.mAudioOutput);
                    }
                    if (this.mAudioOutputDevice != null) {
                        nativeSetAudioOutputDevice(this.mAudioOutputDevice);
                    }
                    this.mAudioReset = false;
                }
                this.mPlayRequested = true;
                if (this.mWindow.areSurfacesWaiting()) {
                    return;
                }
            }
            this.mPlaying = true;
            nativePlay();
        }
    }

    public void stop() {
        synchronized (this) {
            this.mPlayRequested = false;
            this.mPlaying = false;
            this.mAudioReset = true;
        }
        nativeStop();
    }

    public void setVideoTitleDisplay(int position, int timeout) {
        nativeSetVideoTitleDisplay(position, timeout);
    }

    public boolean setAudioOutput(String aout) {
        boolean ret = nativeSetAudioOutput(aout);
        if (ret) {
            synchronized (this) {
                this.mAudioOutput = aout;
            }
        }
        return ret;
    }

    public boolean setAudioOutputDevice(String id) {
        boolean ret = nativeSetAudioOutputDevice(id);
        if (ret) {
            synchronized (this) {
                this.mAudioOutputDevice = id;
            }
        }
        return ret;
    }

    public Title[] getTitles() {
        return nativeGetTitles();
    }

    public Chapter[] getChapters(int title) {
        return nativeGetChapters(title);
    }

    public int getVideoTracksCount() {
        return nativeGetVideoTracksCount();
    }

    public TrackDescription[] getVideoTracks() {
        return nativeGetVideoTracks();
    }

    public int getVideoTrack() {
        return nativeGetVideoTrack();
    }

    public boolean setVideoTrack(int index) {
        if (index == -1 || (this.mWindow.areViewsAttached() && !this.mWindow.areSurfacesWaiting())) {
            return nativeSetVideoTrack(index);
        }
        return false;
    }

    public void setVideoTrackEnabled(boolean enabled) {
        if (!enabled) {
            setVideoTrack(-1);
        } else if (getVideoTrack() == -1) {
            TrackDescription[] tracks = getVideoTracks();
            if (tracks != null) {
                for (TrackDescription track : tracks) {
                    if (track.id != -1) {
                        setVideoTrack(track.id);
                        if (isSeekable()) {
                            long time = getTime();
                            if (time > 0) {
                                setTime(time);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    public int getAudioTracksCount() {
        return nativeGetAudioTracksCount();
    }

    public TrackDescription[] getAudioTracks() {
        return nativeGetAudioTracks();
    }

    public int getAudioTrack() {
        return nativeGetAudioTrack();
    }

    public boolean setAudioTrack(int index) {
        return nativeSetAudioTrack(index);
    }

    public long getAudioDelay() {
        return nativeGetAudioDelay();
    }

    public boolean setAudioDelay(long delay) {
        return nativeSetAudioDelay(delay);
    }

    public int getSpuTracksCount() {
        return nativeGetSpuTracksCount();
    }

    public TrackDescription[] getSpuTracks() {
        return nativeGetSpuTracks();
    }

    public int getSpuTrack() {
        return nativeGetSpuTrack();
    }

    public boolean setSpuTrack(int index) {
        return nativeSetSpuTrack(index);
    }

    public long getSpuDelay() {
        return nativeGetSpuDelay();
    }

    public boolean setSpuDelay(long delay) {
        return nativeSetSpuDelay(delay);
    }

    public boolean setEqualizer(Equalizer equalizer) {
        return nativeSetEqualizer(equalizer);
    }

    public boolean addSlave(int type, Uri uri, boolean select) {
        return nativeAddSlave(type, Media.locationFromUri(uri), select);
    }

    public boolean addSlave(int type, String path, boolean select) {
        return addSlave(type, Uri.fromFile(new File(path)), select);
    }

    public synchronized void setEventListener(EventListener listener) {
        super.setEventListener(listener);
    }

    protected synchronized Event onEventNative(int eventType, long arg1, float arg2) {
        Event event;
        switch (eventType) {
            case 256:
            case Event.Stopped /*262*/:
            case Event.EndReached /*265*/:
            case Event.EncounteredError /*266*/:
                this.mVoutCount = 0;
                notify();
                break;
            case Event.Opening /*258*/:
            case Event.Buffering /*259*/:
                break;
            case Event.Playing /*260*/:
            case Event.Paused /*261*/:
                event = new Event(eventType);
                break;
            case Event.TimeChanged /*267*/:
                event = new Event(eventType, arg1);
                break;
            case Event.PositionChanged /*268*/:
                event = new Event(eventType, arg2);
                break;
            case Event.SeekableChanged /*269*/:
            case Event.PausableChanged /*270*/:
            case Event.ESAdded /*276*/:
            case Event.ESDeleted /*277*/:
                event = new Event(eventType, arg1);
                break;
            case Event.Vout /*274*/:
                this.mVoutCount = (int) arg1;
                notify();
                event = new Event(eventType, arg1);
                break;
            default:
                event = null;
                break;
        }
        event = new Event(eventType, arg2);
        return event;
    }

    protected void onReleaseNative() {
        if (this.mMedia != null) {
            this.mMedia.release();
        }
        nativeRelease();
    }
}
