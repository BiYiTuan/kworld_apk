package org.videolan.libvlc;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.support.annotation.MainThread;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

public interface IVLCVout {

    public interface Callback {
        @MainThread
        void onHardwareAccelerationError(IVLCVout iVLCVout);

        @MainThread
        void onNewLayout(IVLCVout iVLCVout, int i, int i2, int i3, int i4, int i5, int i6);

        @MainThread
        void onSurfacesCreated(IVLCVout iVLCVout);

        @MainThread
        void onSurfacesDestroyed(IVLCVout iVLCVout);
    }

    @MainThread
    void addCallback(Callback callback);

    @MainThread
    boolean areViewsAttached();

    @MainThread
    void attachViews();

    @MainThread
    void detachViews();

    @MainThread
    void removeCallback(Callback callback);

    @MainThread
    void sendMouseEvent(int i, int i2, int i3, int i4);

    @TargetApi(14)
    @MainThread
    void setSubtitlesSurface(SurfaceTexture surfaceTexture);

    @MainThread
    void setSubtitlesSurface(Surface surface, SurfaceHolder surfaceHolder);

    @MainThread
    void setSubtitlesView(SurfaceView surfaceView);

    @TargetApi(14)
    @MainThread
    void setSubtitlesView(TextureView textureView);

    @TargetApi(14)
    @MainThread
    void setVideoSurface(SurfaceTexture surfaceTexture);

    @MainThread
    void setVideoSurface(Surface surface, SurfaceHolder surfaceHolder);

    @MainThread
    void setVideoView(SurfaceView surfaceView);

    @TargetApi(14)
    @MainThread
    void setVideoView(TextureView textureView);

    @MainThread
    void setWindowSize(int i, int i2);
}
