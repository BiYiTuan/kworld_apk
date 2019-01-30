package org.succlz123.okplayer.builder;

import android.content.Context;
import android.os.Handler;
import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.hls.HlsMasterPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.upstream.TransferListener;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.ManifestFetcher.ManifestCallback;
import java.io.IOException;
import org.succlz123.okplayer.OkPlayer;

public class HlsRendererBuilder implements RendererBuilder {
    private static final int BUFFER_SEGMENTS = 256;
    private static final int BUFFER_SEGMENT_SIZE = 65536;
    private final Context context;
    private AsyncRendererBuilder currentAsyncBuilder;
    private final String url;
    private final String userAgent;

    private static final class AsyncRendererBuilder implements ManifestCallback<HlsPlaylist> {
        private boolean canceled;
        private final Context context;
        private final OkPlayer player;
        private final ManifestFetcher<HlsPlaylist> playlistFetcher;
        private final String url;
        private final String userAgent;

        public AsyncRendererBuilder(Context context, String userAgent, String url, OkPlayer player) {
            this.context = context;
            this.userAgent = userAgent;
            this.url = url;
            this.player = player;
            this.playlistFetcher = new ManifestFetcher(url, new DefaultUriDataSource(context, userAgent), new HlsPlaylistParser());
        }

        public void init() {
            this.playlistFetcher.singleLoad(this.player.getMainHandler().getLooper(), this);
        }

        public void cancel() {
            this.canceled = true;
        }

        public void onSingleManifestError(IOException e) {
            if (!this.canceled) {
                this.player.onRenderersError(e);
            }
        }

        public void onSingleManifest(HlsPlaylist manifest) {
            if (!this.canceled) {
                Handler mainHandler = this.player.getMainHandler();
                LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(65536));
                TransferListener bandwidthMeter = new DefaultBandwidthMeter();
                int[] variantIndices = null;
                if (manifest instanceof HlsMasterPlaylist) {
                    try {
                        variantIndices = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(this.context, ((HlsMasterPlaylist) manifest).variants, null, false);
                        if (variantIndices.length == 0) {
                            this.player.onRenderersError(new IllegalStateException("No variants selected."));
                            return;
                        }
                    } catch (Exception e) {
                        this.player.onRenderersError(e);
                        return;
                    }
                }
                HlsSampleSource sampleSource = new HlsSampleSource(new HlsChunkSource(new DefaultUriDataSource(this.context, bandwidthMeter, this.userAgent), this.url, manifest, bandwidthMeter, variantIndices, 1), loadControl, 16777216, mainHandler, this.player, 0);
                MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(this.context, sampleSource, 1, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS, mainHandler, this.player, 50);
                MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, null, true, this.player.getMainHandler(), this.player, AudioCapabilities.getCapabilities(this.context));
                TrackRenderer[] renderers = new TrackRenderer[4];
                renderers[0] = videoRenderer;
                renderers[1] = audioRenderer;
                this.player.onRenderers(renderers, bandwidthMeter);
            }
        }
    }

    public HlsRendererBuilder(Context context, String userAgent, String url) {
        this.context = context;
        this.userAgent = userAgent;
        this.url = url;
    }

    public void buildRenderers(OkPlayer player) {
        this.currentAsyncBuilder = new AsyncRendererBuilder(this.context, this.userAgent, this.url, player);
        this.currentAsyncBuilder.init();
    }

    public void cancel() {
        if (this.currentAsyncBuilder != null) {
            this.currentAsyncBuilder.cancel();
            this.currentAsyncBuilder = null;
        }
    }
}
