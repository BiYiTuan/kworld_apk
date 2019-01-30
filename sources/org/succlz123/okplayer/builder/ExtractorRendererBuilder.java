package org.succlz123.okplayer.builder;

import android.content.Context;
import android.net.Uri;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.upstream.TransferListener;
import org.succlz123.okplayer.OkPlayer;

public class ExtractorRendererBuilder implements RendererBuilder {
    private static final int BUFFER_SEGMENT_COUNT = 256;
    private static final int BUFFER_SEGMENT_SIZE = 65536;
    private final Context context;
    private final Uri uri;
    private final String userAgent;

    public ExtractorRendererBuilder(Context context, String userAgent, Uri uri) {
        this.context = context;
        this.userAgent = userAgent;
        this.uri = uri;
    }

    public void buildRenderers(OkPlayer player) {
        Allocator allocator = new DefaultAllocator(65536);
        TransferListener bandwidthMeter = new DefaultBandwidthMeter(player.getMainHandler(), player);
        ExtractorSampleSource sampleSource = new ExtractorSampleSource(this.uri, new DefaultUriDataSource(this.context, bandwidthMeter, this.userAgent), allocator, 16777216, new Extractor[0]);
        MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(this.context, sampleSource, 1, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS, player.getMainHandler(), player, 50);
        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, null, true, player.getMainHandler(), player, AudioCapabilities.getCapabilities(this.context));
        TrackRenderer[] renderers = new TrackRenderer[4];
        renderers[0] = videoRenderer;
        renderers[1] = audioRenderer;
        player.onRenderers(renderers, bandwidthMeter);
    }

    public void cancel() {
    }
}
