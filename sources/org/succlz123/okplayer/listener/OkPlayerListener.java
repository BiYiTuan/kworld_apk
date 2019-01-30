package org.succlz123.okplayer.listener;

public interface OkPlayerListener {
    void onError(Exception exception);

    void onStateChanged(boolean z, int i);

    void onVideoSizeChanged(int i, int i2, int i3, float f);
}
