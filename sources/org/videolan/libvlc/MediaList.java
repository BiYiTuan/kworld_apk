package org.videolan.libvlc;

import android.util.SparseArray;
import org.videolan.libvlc.VLCEvent.Listener;

public class MediaList extends VLCObject<Event> {
    private static final String TAG = "LibVLC/MediaList";
    private int mCount = 0;
    private boolean mLocked = false;
    private final SparseArray<Media> mMediaArray = new SparseArray();

    public static class Event extends VLCEvent {
        public static final int EndReached = 516;
        public static final int ItemAdded = 512;
        public static final int ItemDeleted = 514;
        public final int index;
        public final Media media;

        protected Event(int type, Media media, int index) {
            super(type);
            this.media = media;
            this.index = index;
        }
    }

    public interface EventListener extends Listener<Event> {
    }

    private native int nativeGetCount();

    private native void nativeLock();

    private native void nativeNewFromLibVlc(LibVLC libVLC);

    private native void nativeNewFromMedia(Media media);

    private native void nativeNewFromMediaDiscoverer(MediaDiscoverer mediaDiscoverer);

    private native void nativeRelease();

    private native void nativeUnlock();

    public /* bridge */ /* synthetic */ boolean isReleased() {
        return super.isReleased();
    }

    private void init() {
        lock();
        this.mCount = nativeGetCount();
        for (int i = 0; i < this.mCount; i++) {
            this.mMediaArray.put(i, new Media(this, i));
        }
        unlock();
    }

    public MediaList(LibVLC libVLC) {
        nativeNewFromLibVlc(libVLC);
        init();
    }

    protected MediaList(MediaDiscoverer md) {
        nativeNewFromMediaDiscoverer(md);
        init();
    }

    protected MediaList(Media m) {
        nativeNewFromMedia(m);
        init();
    }

    private synchronized Media insertMediaFromEvent(int index) {
        Media media;
        this.mCount++;
        for (int i = this.mCount - 1; i >= index; i--) {
            this.mMediaArray.put(i + 1, this.mMediaArray.valueAt(i));
        }
        media = new Media(this, index);
        this.mMediaArray.put(index, media);
        return media;
    }

    private synchronized Media removeMediaFromEvent(int index) {
        Media media;
        this.mCount--;
        media = (Media) this.mMediaArray.get(index);
        if (media != null) {
            media.release();
        }
        for (int i = index; i < this.mCount; i++) {
            this.mMediaArray.put(i, this.mMediaArray.valueAt(i + 1));
        }
        return media;
    }

    public void setEventListener(EventListener listener) {
        super.setEventListener(listener);
    }

    protected synchronized Event onEventNative(int eventType, long arg1, float arg2) {
        Event event;
        if (this.mLocked) {
            throw new IllegalStateException("already locked from event callback");
        }
        this.mLocked = true;
        event = null;
        int index;
        switch (eventType) {
            case 512:
                index = (int) arg1;
                if (index != -1) {
                    event = new Event(eventType, insertMediaFromEvent(index), index);
                    break;
                }
                break;
            case Event.ItemDeleted /*514*/:
                index = (int) arg1;
                if (index != -1) {
                    event = new Event(eventType, removeMediaFromEvent(index), index);
                    break;
                }
                break;
            case Event.EndReached /*516*/:
                event = new Event(eventType, null, -1);
                break;
        }
        this.mLocked = false;
        return event;
    }

    public synchronized int getCount() {
        return this.mCount;
    }

    public synchronized Media getMediaAt(int index) {
        Media media;
        if (index >= 0) {
            if (index < getCount()) {
                media = (Media) this.mMediaArray.get(index);
                media.retain();
            }
        }
        throw new IndexOutOfBoundsException();
        return media;
    }

    public void onReleaseNative() {
        for (int i = 0; i < this.mMediaArray.size(); i++) {
            Media media = (Media) this.mMediaArray.get(i);
            if (media != null) {
                media.release();
            }
        }
        nativeRelease();
    }

    private synchronized void lock() {
        if (this.mLocked) {
            throw new IllegalStateException("already locked");
        }
        this.mLocked = true;
        nativeLock();
    }

    private synchronized void unlock() {
        if (this.mLocked) {
            this.mLocked = false;
            nativeUnlock();
        } else {
            throw new IllegalStateException("not locked");
        }
    }

    protected synchronized boolean isLocked() {
        return this.mLocked;
    }
}
