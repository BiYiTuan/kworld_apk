package org.videolan.libvlc;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.videolan.libvlc.IVLCVout.Callback;
import org.videolan.libvlc.util.AndroidUtil;

public class AWindow implements IVLCVout {
    private static final int ID_MAX = 2;
    private static final int ID_SUBTITLES = 1;
    private static final int ID_VIDEO = 0;
    private static final int SURFACE_STATE_ATTACHED = 1;
    private static final int SURFACE_STATE_INIT = 0;
    private static final int SURFACE_STATE_READY = 2;
    private static final String TAG = "AWindow";
    private final AWindowNativeHandler mAWindowNativeHandler = new C09681();
    private final BuffersGeometryCond mBuffersGeometryCond = new BuffersGeometryCond();
    private long mCallbackNativeHandle = 0;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private ArrayList<Callback> mIVLCVoutCallbacks = new ArrayList();
    private int mMouseAction = -1;
    private int mMouseButton = -1;
    private int mMouseX = -1;
    private int mMouseY = -1;
    private final Object mNativeLock = new Object();
    private final SurfaceCallback mSurfaceCallback;
    private final SurfaceHelper[] mSurfaceHelpers;
    private final Surface[] mSurfaces;
    private final AtomicInteger mSurfacesState = new AtomicInteger(0);
    private int mWindowHeight = -1;
    private int mWindowWidth = -1;

    private static class BuffersGeometryCond {
        private boolean abort;
        private boolean configured;

        private BuffersGeometryCond() {
            this.configured = false;
            this.abort = false;
        }
    }

    public interface SurfaceCallback {
        @MainThread
        void onSurfacesCreated(AWindow aWindow);

        @MainThread
        void onSurfacesDestroyed(AWindow aWindow);
    }

    private class SurfaceHelper {
        private final int mId;
        private Surface mSurface;
        private final SurfaceHolder mSurfaceHolder;
        private final SurfaceHolder.Callback mSurfaceHolderCallback;
        private final SurfaceTextureListener mSurfaceTextureListener;
        private final SurfaceView mSurfaceView;
        private final TextureView mTextureView;

        /* renamed from: org.videolan.libvlc.AWindow$SurfaceHelper$1 */
        class C08091 implements SurfaceHolder.Callback {
            C08091() {
            }

            public void surfaceCreated(SurfaceHolder holder) {
                if (holder != SurfaceHelper.this.mSurfaceHolder) {
                    throw new IllegalStateException("holders are different");
                }
                SurfaceHelper.this.setSurface(holder.getSurface());
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                AWindow.this.onSurfaceDestroyed();
            }
        }

        /* renamed from: org.videolan.libvlc.AWindow$SurfaceHelper$2 */
        class C08102 implements SurfaceTextureListener {
            C08102() {
            }

            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                SurfaceHelper.this.setSurface(new Surface(surfaceTexture));
            }

            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                AWindow.this.onSurfaceDestroyed();
                return true;
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        }

        private SurfaceHelper(int id, SurfaceView surfaceView) {
            SurfaceTextureListener createSurfaceTextureListener;
            this.mSurfaceHolderCallback = new C08091();
            if (AndroidUtil.isICSOrLater()) {
                createSurfaceTextureListener = createSurfaceTextureListener();
            } else {
                createSurfaceTextureListener = null;
            }
            this.mSurfaceTextureListener = createSurfaceTextureListener;
            this.mId = id;
            this.mTextureView = null;
            this.mSurfaceView = surfaceView;
            this.mSurfaceHolder = this.mSurfaceView.getHolder();
        }

        private SurfaceHelper(int id, TextureView textureView) {
            SurfaceTextureListener createSurfaceTextureListener;
            this.mSurfaceHolderCallback = new C08091();
            if (AndroidUtil.isICSOrLater()) {
                createSurfaceTextureListener = createSurfaceTextureListener();
            } else {
                createSurfaceTextureListener = null;
            }
            this.mSurfaceTextureListener = createSurfaceTextureListener;
            this.mId = id;
            this.mSurfaceView = null;
            this.mSurfaceHolder = null;
            this.mTextureView = textureView;
        }

        private SurfaceHelper(int id, Surface surface, SurfaceHolder surfaceHolder) {
            SurfaceTextureListener createSurfaceTextureListener;
            this.mSurfaceHolderCallback = new C08091();
            if (AndroidUtil.isICSOrLater()) {
                createSurfaceTextureListener = createSurfaceTextureListener();
            } else {
                createSurfaceTextureListener = null;
            }
            this.mSurfaceTextureListener = createSurfaceTextureListener;
            this.mId = id;
            this.mSurfaceView = null;
            this.mTextureView = null;
            this.mSurfaceHolder = surfaceHolder;
            this.mSurface = surface;
        }

        private void setSurface(Surface surface) {
            if (surface.isValid() && AWindow.this.getNativeSurface(this.mId) == null) {
                this.mSurface = surface;
                AWindow.this.setNativeSurface(this.mId, this.mSurface);
                AWindow.this.onSurfaceCreated();
            }
        }

        private void attachSurfaceView() {
            this.mSurfaceHolder.addCallback(this.mSurfaceHolderCallback);
            setSurface(this.mSurfaceHolder.getSurface());
        }

        @TargetApi(14)
        private void attachTextureView() {
            this.mTextureView.setSurfaceTextureListener(this.mSurfaceTextureListener);
            setSurface(new Surface(this.mTextureView.getSurfaceTexture()));
        }

        private void attachSurface() {
            if (this.mSurfaceHolder != null) {
                this.mSurfaceHolder.addCallback(this.mSurfaceHolderCallback);
            }
            setSurface(this.mSurface);
        }

        public void attach() {
            if (this.mSurfaceView != null) {
                attachSurfaceView();
            } else if (this.mTextureView != null) {
                attachTextureView();
            } else if (this.mSurface != null) {
                attachSurface();
            } else {
                throw new IllegalStateException();
            }
        }

        @TargetApi(14)
        private void releaseSurfaceTexture() {
            if (this.mTextureView != null) {
                this.mTextureView.setSurfaceTextureListener(null);
            }
        }

        public void release() {
            this.mSurface = null;
            AWindow.this.setNativeSurface(this.mId, null);
            if (this.mSurfaceHolder != null) {
                this.mSurfaceHolder.removeCallback(this.mSurfaceHolderCallback);
            }
            releaseSurfaceTexture();
        }

        public boolean isReady() {
            return this.mSurfaceView == null || this.mSurface != null;
        }

        public Surface getSurface() {
            return this.mSurface;
        }

        public SurfaceHolder getSurfaceHolder() {
            return this.mSurfaceHolder;
        }

        @TargetApi(14)
        private SurfaceTextureListener createSurfaceTextureListener() {
            return new C08102();
        }
    }

    /* renamed from: org.videolan.libvlc.AWindow$1 */
    class C09681 extends AWindowNativeHandler {

        /* renamed from: org.videolan.libvlc.AWindow$1$3 */
        class C08083 implements Runnable {
            C08083() {
            }

            public void run() {
                Iterator it = AWindow.this.mIVLCVoutCallbacks.iterator();
                while (it.hasNext()) {
                    ((Callback) it.next()).onHardwareAccelerationError(AWindow.this);
                }
            }
        }

        protected native void nativeOnMouseEvent(long j, int i, int i2, int i3, int i4);

        protected native void nativeOnWindowSize(long j, int i, int i2);

        C09681() {
        }

        public Surface getVideoSurface() {
            return AWindow.this.getNativeSurface(0);
        }

        public Surface getSubtitlesSurface() {
            return AWindow.this.getNativeSurface(1);
        }

        public boolean setCallback(long nativeHandle) {
            synchronized (AWindow.this.mNativeLock) {
                if (AWindow.this.mCallbackNativeHandle == 0 || nativeHandle == 0) {
                    AWindow.this.mCallbackNativeHandle = nativeHandle;
                    if (AWindow.this.mCallbackNativeHandle != 0) {
                        if (AWindow.this.mMouseAction != -1) {
                            nativeOnMouseEvent(AWindow.this.mCallbackNativeHandle, AWindow.this.mMouseAction, AWindow.this.mMouseButton, AWindow.this.mMouseX, AWindow.this.mMouseY);
                        }
                        if (!(AWindow.this.mWindowWidth == -1 || AWindow.this.mWindowHeight == -1)) {
                            nativeOnWindowSize(AWindow.this.mCallbackNativeHandle, AWindow.this.mWindowWidth, AWindow.this.mWindowHeight);
                        }
                    }
                    AWindow.this.mMouseAction = AWindow.this.mMouseButton = AWindow.this.mMouseX = AWindow.this.mMouseY = -1;
                    AWindow.this.mWindowWidth = AWindow.this.mWindowHeight = -1;
                    return true;
                }
                return false;
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean setBuffersGeometry(android.view.Surface r10, int r11, int r12, int r13) {
            /*
            r9 = this;
            r7 = 0;
            r0 = org.videolan.libvlc.util.AndroidUtil.isICSOrLater();
            if (r0 == 0) goto L_0x0009;
        L_0x0007:
            r0 = r7;
        L_0x0008:
            return r0;
        L_0x0009:
            r0 = r11 * r12;
            if (r0 != 0) goto L_0x000f;
        L_0x000d:
            r0 = r7;
            goto L_0x0008;
        L_0x000f:
            r0 = "AWindow";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "configureSurface: ";
            r1 = r1.append(r2);
            r1 = r1.append(r11);
            r2 = "x";
            r1 = r1.append(r2);
            r1 = r1.append(r12);
            r1 = r1.toString();
            android.util.Log.d(r0, r1);
            r0 = org.videolan.libvlc.AWindow.this;
            r1 = r0.mBuffersGeometryCond;
            monitor-enter(r1);
            r0 = org.videolan.libvlc.AWindow.this;	 Catch:{ all -> 0x0097 }
            r0 = r0.mBuffersGeometryCond;	 Catch:{ all -> 0x0097 }
            r0 = r0.configured;	 Catch:{ all -> 0x0097 }
            if (r0 != 0) goto L_0x0050;
        L_0x0044:
            r0 = org.videolan.libvlc.AWindow.this;	 Catch:{ all -> 0x0097 }
            r0 = r0.mBuffersGeometryCond;	 Catch:{ all -> 0x0097 }
            r0 = r0.abort;	 Catch:{ all -> 0x0097 }
            if (r0 == 0) goto L_0x0053;
        L_0x0050:
            monitor-exit(r1);	 Catch:{ all -> 0x0097 }
            r0 = r7;
            goto L_0x0008;
        L_0x0053:
            monitor-exit(r1);	 Catch:{ all -> 0x0097 }
            r0 = org.videolan.libvlc.AWindow.this;
            r8 = r0.mHandler;
            r0 = new org.videolan.libvlc.AWindow$1$1;
            r1 = r9;
            r2 = r10;
            r3 = r13;
            r4 = r11;
            r5 = r12;
            r0.<init>(r2, r3, r4, r5);
            r8.post(r0);
            r0 = org.videolan.libvlc.AWindow.this;	 Catch:{ InterruptedException -> 0x0093 }
            r1 = r0.mBuffersGeometryCond;	 Catch:{ InterruptedException -> 0x0093 }
            monitor-enter(r1);	 Catch:{ InterruptedException -> 0x0093 }
        L_0x006e:
            r0 = org.videolan.libvlc.AWindow.this;	 Catch:{ all -> 0x0090 }
            r0 = r0.mBuffersGeometryCond;	 Catch:{ all -> 0x0090 }
            r0 = r0.configured;	 Catch:{ all -> 0x0090 }
            if (r0 != 0) goto L_0x009a;
        L_0x007a:
            r0 = org.videolan.libvlc.AWindow.this;	 Catch:{ all -> 0x0090 }
            r0 = r0.mBuffersGeometryCond;	 Catch:{ all -> 0x0090 }
            r0 = r0.abort;	 Catch:{ all -> 0x0090 }
            if (r0 != 0) goto L_0x009a;
        L_0x0086:
            r0 = org.videolan.libvlc.AWindow.this;	 Catch:{ all -> 0x0090 }
            r0 = r0.mBuffersGeometryCond;	 Catch:{ all -> 0x0090 }
            r0.wait();	 Catch:{ all -> 0x0090 }
            goto L_0x006e;
        L_0x0090:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0090 }
            throw r0;	 Catch:{ InterruptedException -> 0x0093 }
        L_0x0093:
            r6 = move-exception;
            r0 = r7;
            goto L_0x0008;
        L_0x0097:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0097 }
            throw r0;
        L_0x009a:
            r0 = org.videolan.libvlc.AWindow.this;	 Catch:{ all -> 0x0090 }
            r0 = r0.mBuffersGeometryCond;	 Catch:{ all -> 0x0090 }
            r2 = 0;
            r0.configured = r2;	 Catch:{ all -> 0x0090 }
            monitor-exit(r1);	 Catch:{ all -> 0x0090 }
            r0 = 1;
            goto L_0x0008;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.videolan.libvlc.AWindow.1.setBuffersGeometry(android.view.Surface, int, int, int):boolean");
        }

        public void setWindowLayout(int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
            final int i = width;
            final int i2 = height;
            final int i3 = visibleWidth;
            final int i4 = visibleHeight;
            final int i5 = sarNum;
            final int i6 = sarDen;
            AWindow.this.mHandler.post(new Runnable() {
                public void run() {
                    Iterator it = AWindow.this.mIVLCVoutCallbacks.iterator();
                    while (it.hasNext()) {
                        ((Callback) it.next()).onNewLayout(AWindow.this, i, i2, i3, i4, i5, i6);
                    }
                }
            });
        }

        public void sendHardwareAccelerationError() {
            AWindow.this.mHandler.post(new C08083());
        }
    }

    public AWindow(SurfaceCallback surfaceCallback) {
        this.mSurfaceCallback = surfaceCallback;
        this.mSurfaceHelpers = new SurfaceHelper[2];
        this.mSurfaceHelpers[0] = null;
        this.mSurfaceHelpers[1] = null;
        this.mSurfaces = new Surface[2];
        this.mSurfaces[0] = null;
        this.mSurfaces[1] = null;
    }

    private void ensureInitState() throws IllegalStateException {
        if (this.mSurfacesState.get() != 0) {
            throw new IllegalStateException("Can't set view when already attached. Current state: " + this.mSurfacesState.get() + ", mSurfaces[ID_VIDEO]: " + this.mSurfaceHelpers[0] + " / " + this.mSurfaces[0] + ", mSurfaces[ID_SUBTITLES]: " + this.mSurfaceHelpers[1] + " / " + this.mSurfaces[1]);
        }
    }

    private void setView(int id, SurfaceView view) {
        ensureInitState();
        if (view == null) {
            throw new NullPointerException("view is null");
        }
        SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
        if (surfaceHelper != null) {
            surfaceHelper.release();
        }
        this.mSurfaceHelpers[id] = new SurfaceHelper(id, view);
    }

    private void setView(int id, TextureView view) {
        if (AndroidUtil.isICSOrLater()) {
            ensureInitState();
            if (view == null) {
                throw new NullPointerException("view is null");
            }
            SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
            if (surfaceHelper != null) {
                surfaceHelper.release();
            }
            this.mSurfaceHelpers[id] = new SurfaceHelper(id, view);
            return;
        }
        throw new IllegalArgumentException("TextureView not implemented in this android version");
    }

    private void setSurface(int id, Surface surface, SurfaceHolder surfaceHolder) {
        ensureInitState();
        if (surface.isValid() || surfaceHolder != null) {
            SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
            if (surfaceHelper != null) {
                surfaceHelper.release();
            }
            this.mSurfaceHelpers[id] = new SurfaceHelper(id, surface, surfaceHolder);
            return;
        }
        throw new IllegalStateException("surface is not attached and holder is null");
    }

    @MainThread
    public void setVideoView(SurfaceView videoSurfaceView) {
        setView(0, videoSurfaceView);
    }

    @MainThread
    public void setVideoView(TextureView videoTextureView) {
        setView(0, videoTextureView);
    }

    public void setVideoSurface(Surface videoSurface, SurfaceHolder surfaceHolder) {
        setSurface(0, videoSurface, surfaceHolder);
    }

    @TargetApi(14)
    public void setVideoSurface(SurfaceTexture videoSurfaceTexture) {
        setSurface(0, new Surface(videoSurfaceTexture), null);
    }

    @MainThread
    public void setSubtitlesView(SurfaceView subtitlesSurfaceView) {
        setView(1, subtitlesSurfaceView);
    }

    @MainThread
    public void setSubtitlesView(TextureView subtitlesTextureView) {
        setView(1, subtitlesTextureView);
    }

    public void setSubtitlesSurface(Surface subtitlesSurface, SurfaceHolder surfaceHolder) {
        setSurface(1, subtitlesSurface, surfaceHolder);
    }

    @TargetApi(14)
    public void setSubtitlesSurface(SurfaceTexture subtitlesSurfaceTexture) {
        setSurface(1, new Surface(subtitlesSurfaceTexture), null);
    }

    @MainThread
    public void attachViews() {
        if (this.mSurfacesState.get() != 0 || this.mSurfaceHelpers[0] == null) {
            throw new IllegalStateException("already attached or video view not configured");
        }
        this.mSurfacesState.set(1);
        synchronized (this.mBuffersGeometryCond) {
            this.mBuffersGeometryCond.configured = false;
            this.mBuffersGeometryCond.abort = false;
        }
        for (int id = 0; id < 2; id++) {
            SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
            if (surfaceHelper != null) {
                surfaceHelper.attach();
            }
        }
    }

    @MainThread
    public void detachViews() {
        if (this.mSurfacesState.get() != 0) {
            this.mSurfacesState.set(0);
            this.mHandler.removeCallbacksAndMessages(null);
            synchronized (this.mBuffersGeometryCond) {
                this.mBuffersGeometryCond.abort = true;
                this.mBuffersGeometryCond.notifyAll();
            }
            for (int id = 0; id < 2; id++) {
                SurfaceHelper surfaceHelper = this.mSurfaceHelpers[id];
                if (surfaceHelper != null) {
                    surfaceHelper.release();
                }
                this.mSurfaceHelpers[id] = null;
            }
            Iterator it = this.mIVLCVoutCallbacks.iterator();
            while (it.hasNext()) {
                ((Callback) it.next()).onSurfacesDestroyed(this);
            }
            if (this.mSurfaceCallback != null) {
                this.mSurfaceCallback.onSurfacesDestroyed(this);
            }
        }
    }

    @MainThread
    public boolean areViewsAttached() {
        return this.mSurfacesState.get() != 0;
    }

    @MainThread
    private void onSurfaceCreated() {
        if (this.mSurfacesState.get() != 1) {
            throw new IllegalArgumentException("invalid state");
        }
        SurfaceHelper videoHelper = this.mSurfaceHelpers[0];
        SurfaceHelper subtitlesHelper = this.mSurfaceHelpers[1];
        if (videoHelper == null) {
            throw new NullPointerException("videoHelper shouldn't be null here");
        } else if (!videoHelper.isReady()) {
        } else {
            if (subtitlesHelper == null || subtitlesHelper.isReady()) {
                this.mSurfacesState.set(2);
                Iterator it = this.mIVLCVoutCallbacks.iterator();
                while (it.hasNext()) {
                    ((Callback) it.next()).onSurfacesCreated(this);
                }
                if (this.mSurfaceCallback != null) {
                    this.mSurfaceCallback.onSurfacesCreated(this);
                }
            }
        }
    }

    @MainThread
    private void onSurfaceDestroyed() {
        detachViews();
    }

    protected boolean areSurfacesWaiting() {
        return this.mSurfacesState.get() == 1;
    }

    public void sendMouseEvent(int action, int button, int x, int y) {
        synchronized (this.mNativeLock) {
            if (this.mCallbackNativeHandle != 0) {
                this.mAWindowNativeHandler.nativeOnMouseEvent(this.mCallbackNativeHandle, action, button, x, y);
            } else {
                this.mMouseAction = action;
                this.mMouseButton = button;
                this.mMouseX = x;
                this.mMouseY = y;
            }
        }
    }

    public void setWindowSize(int width, int height) {
        synchronized (this.mNativeLock) {
            if (this.mCallbackNativeHandle != 0) {
                this.mAWindowNativeHandler.nativeOnWindowSize(this.mCallbackNativeHandle, width, height);
            } else {
                this.mWindowWidth = width;
                this.mWindowHeight = height;
            }
        }
    }

    private void setNativeSurface(int id, Surface surface) {
        synchronized (this.mNativeLock) {
            this.mSurfaces[id] = surface;
        }
    }

    private Surface getNativeSurface(int id) {
        Surface surface;
        synchronized (this.mNativeLock) {
            surface = this.mSurfaces[id];
        }
        return surface;
    }

    public void addCallback(Callback callback) {
        if (!this.mIVLCVoutCallbacks.contains(callback)) {
            this.mIVLCVoutCallbacks.add(callback);
        }
    }

    public void removeCallback(Callback callback) {
        this.mIVLCVoutCallbacks.remove(callback);
    }

    public AWindowNativeHandler getNativeHandler() {
        return this.mAWindowNativeHandler;
    }
}
