package org.videolan.libvlc;

import android.net.Uri;
import android.support.annotation.Nullable;
import java.io.FileDescriptor;
import org.videolan.libvlc.VLCEvent.Listener;
import org.videolan.libvlc.util.AndroidUtil;
import org.videolan.libvlc.util.HWDecoderUtil;
import org.videolan.libvlc.util.HWDecoderUtil.Decoder;

public class Media extends VLCObject<Event> {
    private static final int PARSE_STATUS_INIT = 0;
    private static final int PARSE_STATUS_PARSED = 2;
    private static final int PARSE_STATUS_PARSING = 1;
    private static final String TAG = "LibVLC/Media";
    private static final String URI_AUTHORIZED_CHARS = "!'()*";
    private boolean mCodecOptionSet;
    private long mDuration;
    private final String[] mNativeMetas;
    private Track[] mNativeTracks;
    private int mParseStatus;
    private int mState;
    private MediaList mSubItems;
    private int mType;
    private Uri mUri;

    public static class Meta {
        public static final int Actors = 22;
        public static final int Album = 4;
        public static final int AlbumArtist = 23;
        public static final int Artist = 1;
        public static final int ArtworkURL = 15;
        public static final int Copyright = 3;
        public static final int Date = 8;
        public static final int Description = 6;
        public static final int Director = 18;
        public static final int DiscNumber = 24;
        public static final int EncodedBy = 14;
        public static final int Episode = 20;
        public static final int Genre = 2;
        public static final int Language = 11;
        public static final int MAX = 25;
        public static final int NowPlaying = 12;
        public static final int Publisher = 13;
        public static final int Rating = 7;
        public static final int Season = 19;
        public static final int Setting = 9;
        public static final int ShowName = 21;
        public static final int Title = 0;
        public static final int TrackID = 16;
        public static final int TrackNumber = 5;
        public static final int TrackTotal = 17;
        public static final int URL = 10;
    }

    public static class Parse {
        public static final int DoInteract = 8;
        public static final int FetchLocal = 2;
        public static final int FetchNetwork = 4;
        public static final int ParseLocal = 0;
        public static final int ParseNetwork = 1;
    }

    public static class ParsedStatus {
        public static final int Done = 4;
        public static final int Failed = 2;
        public static final int Skipped = 1;
        public static final int Timeout = 3;
    }

    public static class Slave {
        public final int priority;
        public final int type;
        public final String uri;

        public static class Type {
            public static final int Audio = 1;
            public static final int Subtitle = 0;
        }

        public Slave(int type, int priority, String uri) {
            this.type = type;
            this.priority = priority;
            this.uri = uri;
        }
    }

    public static class State {
        public static final int Ended = 6;
        public static final int Error = 7;
        public static final int MAX = 8;
        public static final int NothingSpecial = 0;
        public static final int Opening = 1;
        public static final int Paused = 4;
        public static final int Playing = 3;
        public static final int Stopped = 5;
    }

    public static abstract class Track {
        public final int bitrate;
        public final String codec;
        public final String description;
        public final int id;
        public final String language;
        public final int level;
        public final String originalCodec;
        public final int profile;
        public final int type;

        public static class Type {
            public static final int Audio = 0;
            public static final int Text = 2;
            public static final int Unknown = -1;
            public static final int Video = 1;
        }

        private Track(int type, String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description) {
            this.type = type;
            this.codec = codec;
            this.originalCodec = originalCodec;
            this.id = id;
            this.profile = profile;
            this.level = level;
            this.bitrate = bitrate;
            this.language = language;
            this.description = description;
        }
    }

    public static class Type {
        public static final int Directory = 2;
        public static final int Disc = 3;
        public static final int File = 1;
        public static final int Playlist = 5;
        public static final int Stream = 4;
        public static final int Unknown = 0;
    }

    public static class AudioTrack extends Track {
        public final int channels;
        public final int rate;

        private AudioTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int channels, int rate) {
            super(0, codec, originalCodec, id, profile, level, bitrate, language, description);
            this.channels = channels;
            this.rate = rate;
        }
    }

    public static class Event extends VLCEvent {
        public static final int DurationChanged = 2;
        public static final int MetaChanged = 0;
        public static final int ParsedChanged = 3;
        public static final int StateChanged = 5;
        public static final int SubItemAdded = 1;
        public static final int SubItemTreeAdded = 6;

        protected Event(int type) {
            super(type);
        }

        protected Event(int type, long arg1) {
            super(type, arg1);
        }

        public int getMetaId() {
            return (int) this.arg1;
        }

        public int getParsedStatus() {
            return (int) this.arg1;
        }
    }

    public interface EventListener extends Listener<Event> {
    }

    public static class SubtitleTrack extends Track {
        public final String encoding;

        private SubtitleTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, String encoding) {
            super(2, codec, originalCodec, id, profile, level, bitrate, language, description);
            this.encoding = encoding;
        }
    }

    public static class UnknownTrack extends Track {
        private UnknownTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description) {
            super(-1, codec, originalCodec, id, profile, level, bitrate, language, description);
        }
    }

    public static class VideoTrack extends Track {
        public final int frameRateDen;
        public final int frameRateNum;
        public final int height;
        public final int sarDen;
        public final int sarNum;
        public final int width;

        private VideoTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int height, int width, int sarNum, int sarDen, int frameRateNum, int frameRateDen) {
            super(1, codec, originalCodec, id, profile, level, bitrate, language, description);
            this.height = height;
            this.width = width;
            this.sarNum = sarNum;
            this.sarDen = sarDen;
            this.frameRateNum = frameRateNum;
            this.frameRateDen = frameRateDen;
        }
    }

    private native void nativeAddOption(String str);

    private native void nativeAddSlave(int i, int i2, String str);

    private native void nativeClearSlaves();

    private native long nativeGetDuration();

    private native String nativeGetMeta(int i);

    private native String nativeGetMrl();

    private native Slave[] nativeGetSlaves();

    private native int nativeGetState();

    private native Track[] nativeGetTracks();

    private native int nativeGetType();

    private native void nativeNewFromFd(LibVLC libVLC, FileDescriptor fileDescriptor);

    private native void nativeNewFromLocation(LibVLC libVLC, String str);

    private native void nativeNewFromMediaList(MediaList mediaList, int i);

    private native void nativeNewFromPath(LibVLC libVLC, String str);

    private native boolean nativeParse(int i);

    private native boolean nativeParseAsync(int i, int i2);

    private native void nativeRelease();

    public /* bridge */ /* synthetic */ boolean isReleased() {
        return super.isReleased();
    }

    private static Track createAudioTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int channels, int rate) {
        return new AudioTrack(codec, originalCodec, id, profile, level, bitrate, language, description, channels, rate);
    }

    private static Track createVideoTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int height, int width, int sarNum, int sarDen, int frameRateNum, int frameRateDen) {
        return new VideoTrack(codec, originalCodec, id, profile, level, bitrate, language, description, height, width, sarNum, sarDen, frameRateNum, frameRateDen);
    }

    private static Track createSubtitleTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, String encoding) {
        return new SubtitleTrack(codec, originalCodec, id, profile, level, bitrate, language, description, encoding);
    }

    private static Track createUnknownTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description) {
        return new UnknownTrack(codec, originalCodec, id, profile, level, bitrate, language, description);
    }

    private static Slave createSlaveFromNative(int type, int priority, String uri) {
        return new Slave(type, priority, uri);
    }

    public Media(LibVLC libVLC, String path) {
        this.mUri = null;
        this.mSubItems = null;
        this.mParseStatus = 0;
        this.mNativeMetas = new String[25];
        this.mNativeTracks = null;
        this.mDuration = -1;
        this.mState = -1;
        this.mType = -1;
        this.mCodecOptionSet = false;
        nativeNewFromPath(libVLC, path);
        this.mUri = UriFromMrl(nativeGetMrl());
    }

    public Media(LibVLC libVLC, Uri uri) {
        this.mUri = null;
        this.mSubItems = null;
        this.mParseStatus = 0;
        this.mNativeMetas = new String[25];
        this.mNativeTracks = null;
        this.mDuration = -1;
        this.mState = -1;
        this.mType = -1;
        this.mCodecOptionSet = false;
        nativeNewFromLocation(libVLC, locationFromUri(uri));
        this.mUri = uri;
    }

    public Media(LibVLC libVLC, FileDescriptor fd) {
        this.mUri = null;
        this.mSubItems = null;
        this.mParseStatus = 0;
        this.mNativeMetas = new String[25];
        this.mNativeTracks = null;
        this.mDuration = -1;
        this.mState = -1;
        this.mType = -1;
        this.mCodecOptionSet = false;
        nativeNewFromFd(libVLC, fd);
        this.mUri = UriFromMrl(nativeGetMrl());
    }

    protected Media(MediaList ml, int index) {
        this.mUri = null;
        this.mSubItems = null;
        this.mParseStatus = 0;
        this.mNativeMetas = new String[25];
        this.mNativeTracks = null;
        this.mDuration = -1;
        this.mState = -1;
        this.mType = -1;
        this.mCodecOptionSet = false;
        if (ml == null || ml.isReleased()) {
            throw new IllegalArgumentException("MediaList is null or released");
        } else if (ml.isLocked()) {
            nativeNewFromMediaList(ml, index);
            this.mUri = UriFromMrl(nativeGetMrl());
        } else {
            throw new IllegalStateException("MediaList should be locked");
        }
    }

    private static Uri UriFromMrl(String mrl) {
        char[] array = mrl.toCharArray();
        StringBuilder sb = new StringBuilder(array.length);
        int i = 0;
        while (i < array.length) {
            char c = array[i];
            if (c == '%' && array.length - i >= 3) {
                try {
                    int hex = Integer.parseInt(new String(array, i + 1, 2), 16);
                    if (URI_AUTHORIZED_CHARS.indexOf(hex) != -1) {
                        sb.append((char) hex);
                        i += 2;
                        i++;
                    }
                } catch (NumberFormatException e) {
                }
            }
            sb.append(c);
            i++;
        }
        return Uri.parse(sb.toString());
    }

    protected static String locationFromUri(Uri uri) {
        char[] array = uri.toString().toCharArray();
        StringBuilder sb = new StringBuilder(array.length * 2);
        for (char c : array) {
            if (URI_AUTHORIZED_CHARS.indexOf(c) != -1) {
                sb.append("%").append(Integer.toHexString(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void setEventListener(EventListener listener) {
        super.setEventListener(listener);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected synchronized org.videolan.libvlc.Media.Event onEventNative(int r5, long r6, float r8) {
        /*
        r4 = this;
        monitor-enter(r4);
        switch(r5) {
            case 0: goto L_0x000b;
            case 1: goto L_0x0004;
            case 2: goto L_0x0020;
            case 3: goto L_0x0025;
            case 4: goto L_0x0004;
            case 5: goto L_0x002e;
            default: goto L_0x0004;
        };
    L_0x0004:
        r1 = new org.videolan.libvlc.Media$Event;	 Catch:{ all -> 0x001d }
        r1.<init>(r5);	 Catch:{ all -> 0x001d }
    L_0x0009:
        monitor-exit(r4);
        return r1;
    L_0x000b:
        r0 = (int) r6;
        if (r0 < 0) goto L_0x0017;
    L_0x000e:
        r1 = 25;
        if (r0 >= r1) goto L_0x0017;
    L_0x0012:
        r1 = r4.mNativeMetas;	 Catch:{ all -> 0x001d }
        r2 = 0;
        r1[r0] = r2;	 Catch:{ all -> 0x001d }
    L_0x0017:
        r1 = new org.videolan.libvlc.Media$Event;	 Catch:{ all -> 0x001d }
        r1.<init>(r5, r6);	 Catch:{ all -> 0x001d }
        goto L_0x0009;
    L_0x001d:
        r1 = move-exception;
        monitor-exit(r4);
        throw r1;
    L_0x0020:
        r2 = -1;
        r4.mDuration = r2;	 Catch:{ all -> 0x001d }
        goto L_0x0004;
    L_0x0025:
        r4.postParse();	 Catch:{ all -> 0x001d }
        r1 = new org.videolan.libvlc.Media$Event;	 Catch:{ all -> 0x001d }
        r1.<init>(r5, r6);	 Catch:{ all -> 0x001d }
        goto L_0x0009;
    L_0x002e:
        r1 = -1;
        r4.mState = r1;	 Catch:{ all -> 0x001d }
        goto L_0x0004;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.videolan.libvlc.Media.onEventNative(int, long, float):org.videolan.libvlc.Media$Event");
    }

    public synchronized Uri getUri() {
        return this.mUri;
    }

    public long getDuration() {
        long j;
        synchronized (this) {
            if (this.mDuration != -1) {
                j = this.mDuration;
            } else if (isReleased()) {
                j = 0;
            } else {
                long duration = nativeGetDuration();
                synchronized (this) {
                    this.mDuration = duration;
                    j = this.mDuration;
                }
            }
        }
        return j;
    }

    public int getState() {
        int i;
        synchronized (this) {
            if (this.mState != -1) {
                i = this.mState;
            } else if (isReleased()) {
                i = 7;
            } else {
                int state = nativeGetState();
                synchronized (this) {
                    this.mState = state;
                    i = this.mState;
                }
            }
        }
        return i;
    }

    public MediaList subItems() {
        MediaList mediaList;
        synchronized (this) {
            if (this.mSubItems != null) {
                this.mSubItems.retain();
                mediaList = this.mSubItems;
            } else {
                MediaList subItems = new MediaList(this);
                synchronized (this) {
                    this.mSubItems = subItems;
                    this.mSubItems.retain();
                    mediaList = this.mSubItems;
                }
            }
        }
        return mediaList;
    }

    private synchronized void postParse() {
        if ((this.mParseStatus & 2) == 0) {
            this.mParseStatus &= -2;
            this.mParseStatus |= 2;
            this.mNativeTracks = null;
            this.mDuration = -1;
            this.mState = -1;
            this.mType = -1;
        }
    }

    public boolean parse(int flags) {
        boolean parse = false;
        synchronized (this) {
            if ((this.mParseStatus & 3) == 0) {
                this.mParseStatus |= 1;
                parse = true;
            }
        }
        if (!parse || !nativeParse(flags)) {
            return false;
        }
        postParse();
        return true;
    }

    public boolean parse() {
        return parse(2);
    }

    public boolean parseAsync(int flags, int timeout) {
        boolean parse = false;
        synchronized (this) {
            if ((this.mParseStatus & 3) == 0) {
                this.mParseStatus |= 1;
                parse = true;
            }
        }
        return parse && nativeParseAsync(flags, timeout);
    }

    public boolean parseAsync(int flags) {
        return parseAsync(flags, -1);
    }

    public boolean parseAsync() {
        return parseAsync(2);
    }

    public synchronized boolean isParsed() {
        return (this.mParseStatus & 2) != 0;
    }

    public int getType() {
        int i;
        synchronized (this) {
            if (this.mType != -1) {
                i = this.mType;
            } else if (isReleased()) {
                i = 0;
            } else {
                int type = nativeGetType();
                synchronized (this) {
                    this.mType = type;
                    i = this.mType;
                }
            }
        }
        return i;
    }

    private Track[] getTracks() {
        Track[] trackArr;
        synchronized (this) {
            if (this.mNativeTracks != null) {
                trackArr = this.mNativeTracks;
            } else if (isReleased()) {
                trackArr = null;
            } else {
                Track[] tracks = nativeGetTracks();
                synchronized (this) {
                    this.mNativeTracks = tracks;
                    trackArr = this.mNativeTracks;
                }
            }
        }
        return trackArr;
    }

    public int getTrackCount() {
        Track[] tracks = getTracks();
        return tracks != null ? tracks.length : 0;
    }

    public Track getTrack(int idx) {
        Track[] tracks = getTracks();
        if (tracks == null || idx < 0 || idx >= tracks.length) {
            return null;
        }
        return tracks[idx];
    }

    public String getMeta(int id) {
        String str = null;
        if (id >= 0 && id < 25) {
            synchronized (this) {
                if (this.mNativeMetas[id] != null) {
                    str = this.mNativeMetas[id];
                } else if (isReleased()) {
                } else {
                    str = nativeGetMeta(id);
                    synchronized (this) {
                        this.mNativeMetas[id] = str;
                    }
                }
            }
        }
        return str;
    }

    private static String getMediaCodecModule() {
        return AndroidUtil.isLolliPopOrLater() ? "mediacodec_ndk" : "mediacodec_jni";
    }

    public void setHWDecoderEnabled(boolean enabled, boolean force) {
        Decoder decoder = enabled ? HWDecoderUtil.getDecoderFromDevice() : Decoder.NONE;
        if (decoder == Decoder.NONE || (decoder == Decoder.UNKNOWN && !force)) {
            addOption(":codec=all");
            return;
        }
        addOption(":file-caching=1500");
        addOption(":network-caching=1500");
        StringBuilder sb = new StringBuilder(":codec=");
        if (decoder == Decoder.MEDIACODEC) {
            sb.append(getMediaCodecModule()).append(",");
        } else if (decoder == Decoder.OMX) {
            sb.append("iomx,");
        } else {
            sb.append(getMediaCodecModule()).append(",iomx,");
        }
        sb.append("all");
        addOption(sb.toString());
    }

    protected void setDefaultMediaPlayerOptions() {
        synchronized (this) {
            boolean codecOptionSet = this.mCodecOptionSet;
            this.mCodecOptionSet = true;
        }
        if (!codecOptionSet) {
            setHWDecoderEnabled(true, false);
        }
    }

    public void addOption(String option) {
        synchronized (this) {
            if (!this.mCodecOptionSet && option.startsWith(":codec=")) {
                this.mCodecOptionSet = true;
            }
        }
        nativeAddOption(option);
    }

    public void addSlave(Slave slave) {
        nativeAddSlave(slave.type, slave.priority, slave.uri);
    }

    public void clearSlaves() {
        nativeClearSlaves();
    }

    @Nullable
    public Slave[] getSlaves() {
        return nativeGetSlaves();
    }

    protected void onReleaseNative() {
        if (this.mSubItems != null) {
            this.mSubItems.release();
        }
        nativeRelease();
    }
}
