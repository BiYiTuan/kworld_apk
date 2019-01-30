package org.videolan.vlc.listener;

public interface MediaPlayerControl {
    boolean canControl();

    int getBufferPercentage();

    long getCurrentPosition();

    long getDuration();

    boolean getMirror();

    float getPlaybackSpeed();

    boolean isLoop();

    boolean isPlaying();

    boolean isPrepare();

    void pause();

    void seekTo(long j);

    void setLoop(boolean z);

    void setMirror(boolean z);

    boolean setPlaybackSpeedMedia(float f);

    void start();

    void startPlay(String str);
}
