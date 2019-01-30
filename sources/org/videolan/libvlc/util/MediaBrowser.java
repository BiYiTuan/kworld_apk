package org.videolan.libvlc.util;

import android.net.Uri;
import android.support.annotation.MainThread;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaDiscoverer;
import org.videolan.libvlc.MediaDiscoverer.Description;
import org.videolan.libvlc.MediaList;
import org.videolan.libvlc.MediaList.Event;

public class MediaBrowser {
    private static final String IGNORE_LIST_OPTION = ":ignore-filetypes=";
    private static final String TAG = "MediaBrowser";
    private boolean mAlive;
    private MediaList mBrowserMediaList;
    private final org.videolan.libvlc.MediaList.EventListener mBrowserMediaListEventListener = new C09911();
    private final ArrayList<Media> mDiscovererMediaArray = new ArrayList();
    private final org.videolan.libvlc.MediaList.EventListener mDiscovererMediaListEventListener = new C09922();
    private EventListener mEventListener;
    private String mIgnoreList = "db,nfo,ini,jpg,jpeg,ljpg,gif,png,pgm,pgmyuv,pbm,pam,tga,bmp,pnm,xpm,xcf,pcx,tif,tiff,lbm,sfv,txt,sub,idx,srt,cue,ssa";
    private final LibVLC mLibVlc;
    private Media mMedia;
    private final ArrayList<MediaDiscoverer> mMediaDiscoverers = new ArrayList();

    public interface EventListener {
        void onBrowseEnd();

        void onMediaAdded(int i, Media media);

        void onMediaRemoved(int i, Media media);
    }

    public static class Flag {
        public static final int Interact = 1;
        public static final int NoSlavesAutodetect = 2;
    }

    /* renamed from: org.videolan.libvlc.util.MediaBrowser$1 */
    class C09911 implements org.videolan.libvlc.MediaList.EventListener {
        C09911() {
        }

        public void onEvent(Event event) {
            if (MediaBrowser.this.mEventListener != null) {
                Event mlEvent = event;
                switch (mlEvent.type) {
                    case 512:
                        MediaBrowser.this.mEventListener.onMediaAdded(mlEvent.index, mlEvent.media);
                        return;
                    case Event.ItemDeleted /*514*/:
                        MediaBrowser.this.mEventListener.onMediaRemoved(mlEvent.index, mlEvent.media);
                        return;
                    case Event.EndReached /*516*/:
                        MediaBrowser.this.mEventListener.onBrowseEnd();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* renamed from: org.videolan.libvlc.util.MediaBrowser$2 */
    class C09922 implements org.videolan.libvlc.MediaList.EventListener {
        C09922() {
        }

        public void onEvent(Event event) {
            if (MediaBrowser.this.mEventListener != null) {
                Event mlEvent = event;
                int index = -1;
                switch (mlEvent.type) {
                    case 512:
                        boolean found = false;
                        Iterator it = MediaBrowser.this.mDiscovererMediaArray.iterator();
                        while (it.hasNext()) {
                            if (((Media) it.next()).getUri().toString().equalsIgnoreCase(mlEvent.media.getUri().toString())) {
                                found = true;
                                if (!found) {
                                    MediaBrowser.this.mDiscovererMediaArray.add(mlEvent.media);
                                    index = MediaBrowser.this.mDiscovererMediaArray.size() - 1;
                                }
                                if (index != -1) {
                                    MediaBrowser.this.mEventListener.onMediaAdded(index, mlEvent.media);
                                    return;
                                }
                                return;
                            }
                        }
                        if (found) {
                            MediaBrowser.this.mDiscovererMediaArray.add(mlEvent.media);
                            index = MediaBrowser.this.mDiscovererMediaArray.size() - 1;
                        }
                        if (index != -1) {
                            MediaBrowser.this.mEventListener.onMediaAdded(index, mlEvent.media);
                            return;
                        }
                        return;
                    case Event.ItemDeleted /*514*/:
                        index = MediaBrowser.this.mDiscovererMediaArray.indexOf(mlEvent.media);
                        if (index != -1) {
                            MediaBrowser.this.mDiscovererMediaArray.remove(index);
                        }
                        if (index != -1) {
                            MediaBrowser.this.mEventListener.onMediaRemoved(index, mlEvent.media);
                            return;
                        }
                        return;
                    case Event.EndReached /*516*/:
                        MediaBrowser.this.mEventListener.onBrowseEnd();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public MediaBrowser(LibVLC libvlc, EventListener listener) {
        this.mLibVlc = libvlc;
        this.mLibVlc.retain();
        this.mEventListener = listener;
        this.mAlive = true;
    }

    private void reset() {
        Iterator it = this.mMediaDiscoverers.iterator();
        while (it.hasNext()) {
            ((MediaDiscoverer) it.next()).release();
        }
        this.mMediaDiscoverers.clear();
        this.mDiscovererMediaArray.clear();
        if (this.mMedia != null) {
            this.mMedia.release();
            this.mMedia = null;
        }
        if (this.mBrowserMediaList != null) {
            this.mBrowserMediaList.release();
            this.mBrowserMediaList = null;
        }
    }

    @MainThread
    public void release() {
        reset();
        if (this.mAlive) {
            this.mLibVlc.release();
            this.mAlive = false;
            return;
        }
        throw new IllegalStateException("MediaBrowser released more than one time");
    }

    @MainThread
    public void changeEventListener(EventListener eventListener) {
        reset();
        this.mEventListener = eventListener;
    }

    private void startMediaDiscoverer(String discovererName) {
        MediaDiscoverer md = new MediaDiscoverer(this.mLibVlc, discovererName);
        this.mMediaDiscoverers.add(md);
        MediaList ml = md.getMediaList();
        ml.setEventListener(this.mDiscovererMediaListEventListener);
        ml.release();
        md.start();
    }

    @MainThread
    public void discoverNetworkShares() {
        reset();
        Description[] descriptions = MediaDiscoverer.list(this.mLibVlc, 1);
        if (descriptions != null) {
            for (Description description : descriptions) {
                Log.i(TAG, "starting " + description.name + " discover (" + description.longName + ")");
                startMediaDiscoverer(description.name);
            }
        }
    }

    @MainThread
    public void discoverNetworkShares(String serviceName) {
        reset();
        startMediaDiscoverer(serviceName);
    }

    @MainThread
    public void browse(String path, int flags) {
        Media media = new Media(this.mLibVlc, path);
        browse(media, flags);
        media.release();
    }

    @MainThread
    public void browse(Uri uri, int flags) {
        Media media = new Media(this.mLibVlc, uri);
        browse(media, flags);
        media.release();
    }

    @MainThread
    public void browse(Media media, int flags) {
        media.retain();
        media.addOption(IGNORE_LIST_OPTION + this.mIgnoreList);
        if ((flags & 2) != 0) {
            media.addOption(":no-sub-autodetect-file");
        }
        int mediaFlags = 1;
        if ((flags & 1) != 0) {
            mediaFlags = 1 | 8;
        }
        reset();
        this.mBrowserMediaList = media.subItems();
        this.mBrowserMediaList.setEventListener(this.mBrowserMediaListEventListener);
        media.parseAsync(mediaFlags, 0);
        this.mMedia = media;
    }

    @MainThread
    public int getMediaCount() {
        return this.mBrowserMediaList != null ? this.mBrowserMediaList.getCount() : this.mDiscovererMediaArray.size();
    }

    @MainThread
    public Media getMediaAt(int index) {
        if (index < 0 || index >= getMediaCount()) {
            throw new IndexOutOfBoundsException();
        }
        Media media;
        if (this.mBrowserMediaList != null) {
            media = this.mBrowserMediaList.getMediaAt(index);
        } else {
            media = (Media) this.mDiscovererMediaArray.get(index);
        }
        media.retain();
        return media;
    }

    @MainThread
    public void setIgnoreFileTypes(String list) {
        this.mIgnoreList = list;
    }
}
