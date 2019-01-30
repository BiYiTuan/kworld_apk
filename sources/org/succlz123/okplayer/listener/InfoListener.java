package org.succlz123.okplayer.listener;

import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.chunk.Format;

public interface InfoListener {
    void onAudioFormatEnabled(Format format, int i, long j);

    void onAvailableRangeChanged(TimeRange timeRange);

    void onBandwidthSample(int i, long j, long j2);

    void onDecoderInitialized(String str, long j, long j2);

    void onDroppedFrames(int i, long j);

    void onLoadCompleted(int i, long j, int i2, int i3, Format format, long j2, long j3, long j4, long j5);

    void onLoadStarted(int i, long j, int i2, int i3, Format format, long j2, long j3);

    void onVideoFormatEnabled(Format format, int i, long j);
}
