package org.succlz123.okplayer.listener;

import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver.Listener;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.upstream.BandwidthMeter.EventListener;
import java.util.List;

public class OkMuxListener implements OkPlayerListener, CaptionListener, EventListener, Listener {
    public void onCues(List<Cue> list) {
    }

    public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
    }

    public void onStateChanged(boolean playWhenReady, int playbackState) {
    }

    public void onError(Exception e) {
    }

    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
    }

    public void onBandwidthSample(int elapsedMs, long bytes, long bitrate) {
    }
}
