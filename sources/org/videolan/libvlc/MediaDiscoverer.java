package org.videolan.libvlc;

import android.support.annotation.Nullable;
import org.videolan.libvlc.VLCEvent.Listener;

public class MediaDiscoverer extends VLCObject<Event> {
    private static final String TAG = "LibVLC/MediaDiscoverer";
    private MediaList mMediaList = null;

    public static class Description {
        public final int category;
        public final String longName;
        public final String name;

        public static class Category {
            public static final int Devices = 0;
            public static final int Lan = 1;
            public static final int LocalDirs = 3;
            public static final int Podcasts = 2;
        }

        private Description(String name, String longName, int category) {
            this.name = name;
            this.longName = longName;
            this.category = category;
        }
    }

    public static class Event extends VLCEvent {
        public static final int Ended = 1281;
        public static final int Started = 1280;

        protected Event(int type) {
            super(type);
        }
    }

    public interface EventListener extends Listener<Event> {
    }

    private static native Description[] nativeList(LibVLC libVLC, int i);

    private native void nativeNew(LibVLC libVLC, String str);

    private native void nativeRelease();

    private native boolean nativeStart();

    private native void nativeStop();

    public /* bridge */ /* synthetic */ boolean isReleased() {
        return super.isReleased();
    }

    private static Description createDescriptionFromNative(String name, String longName, int category) {
        return new Description(name, longName, category);
    }

    public MediaDiscoverer(LibVLC libVLC, String name) {
        nativeNew(libVLC, name);
    }

    public boolean start() {
        if (!isReleased()) {
            return nativeStart();
        }
        throw new IllegalStateException("MediaDiscoverer is released");
    }

    public void stop() {
        if (isReleased()) {
            throw new IllegalStateException("MediaDiscoverer is released");
        }
        nativeStop();
    }

    public void setEventListener(EventListener listener) {
        super.setEventListener(listener);
    }

    protected Event onEventNative(int eventType, long arg1, float arg2) {
        switch (eventType) {
            case Event.Started /*1280*/:
            case Event.Ended /*1281*/:
                return new Event(eventType);
            default:
                return null;
        }
    }

    public MediaList getMediaList() {
        MediaList mediaList;
        synchronized (this) {
            if (this.mMediaList != null) {
                this.mMediaList.retain();
                mediaList = this.mMediaList;
            } else {
                MediaList mediaList2 = new MediaList(this);
                synchronized (this) {
                    this.mMediaList = mediaList2;
                    this.mMediaList.retain();
                    mediaList = this.mMediaList;
                }
            }
        }
        return mediaList;
    }

    protected void onReleaseNative() {
        if (this.mMediaList != null) {
            this.mMediaList.release();
        }
        nativeRelease();
    }

    @Nullable
    public static Description[] list(LibVLC libVLC, int category) {
        return nativeList(libVLC, category);
    }
}
