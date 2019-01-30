package org.videolan.libvlc;

import android.view.Surface;

public abstract class AWindowNativeHandler {
    protected abstract Surface getSubtitlesSurface();

    protected abstract Surface getVideoSurface();

    protected abstract void nativeOnMouseEvent(long j, int i, int i2, int i3, int i4);

    protected abstract void nativeOnWindowSize(long j, int i, int i2);

    protected abstract void sendHardwareAccelerationError();

    protected abstract boolean setBuffersGeometry(Surface surface, int i, int i2, int i3);

    protected abstract boolean setCallback(long j);

    protected abstract void setWindowLayout(int i, int i2, int i3, int i4, int i5, int i6);
}
