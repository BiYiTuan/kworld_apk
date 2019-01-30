package org.succlz123.okplayer;

import android.media.MediaCodec.CryptoException;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import com.google.android.exoplayer.CodecCounters;
import com.google.android.exoplayer.DummyTrackRenderer;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.ExoPlayer.Factory;
import com.google.android.exoplayer.ExoPlayer.Listener;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer.DecoderInitializationException;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioTrack.InitializationException;
import com.google.android.exoplayer.audio.AudioTrack.WriteException;
import com.google.android.exoplayer.chunk.ChunkSampleSource;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.BandwidthMeter.EventListener;
import com.google.android.exoplayer.util.DebugTextViewHelper.Provider;
import com.google.android.exoplayer.util.PlayerControl;
import com.loopj.android.http.AsyncHttpClient;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.succlz123.okplayer.builder.RendererBuilder;
import org.succlz123.okplayer.listener.CaptionListener;
import org.succlz123.okplayer.listener.Id3MetadataListener;
import org.succlz123.okplayer.listener.InfoListener;
import org.succlz123.okplayer.listener.InternalErrorListener;
import org.succlz123.okplayer.listener.OkMuxListener;
import org.succlz123.okplayer.listener.OkPlayerListener;

public class OkPlayer implements Listener, EventListener, HlsSampleSource.EventListener, ChunkSampleSource.EventListener, MediaCodecVideoTrackRenderer.EventListener, MediaCodecAudioTrackRenderer.EventListener, StreamingDrmSessionManager.EventListener, Provider {
    private static final int RENDERER_BUILDING_STATE_BUILDING = 2;
    private static final int RENDERER_BUILDING_STATE_BUILT = 3;
    private static final int RENDERER_BUILDING_STATE_IDLE = 1;
    public static final int RENDERER_COUNT = 4;
    public static final int STATE_BUFFERING = 3;
    public static final int STATE_ENDED = 5;
    public static final int STATE_IDLE = 1;
    public static final int STATE_PREPARING = 2;
    public static final int STATE_READY = 4;
    public static final int TRACK_DEFAULT = 0;
    public static final int TRACK_DISABLED = -1;
    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_METADATA = 3;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_VIDEO = 0;
    private TrackRenderer audioRenderer;
    private boolean backgrounded;
    private BandwidthMeter bandwidthMeter;
    private CaptionListener captionListener;
    private CodecCounters codecCounters;
    private Id3MetadataListener id3MetadataListener;
    private InfoListener infoListener;
    private InternalErrorListener internalErrorListener;
    private boolean lastReportedPlayWhenReady;
    private int lastReportedPlaybackState;
    private final CopyOnWriteArrayList<OkPlayerListener> listeners;
    private final Handler mainHandler;
    private final ExoPlayer player;
    private final PlayerControl playerControl;
    private long position;
    private boolean prepared = false;
    private RendererBuilder rendererBuilder;
    private int rendererBuildingState;
    private Surface surface;
    private Format videoFormat;
    private TrackRenderer videoRenderer;
    private int videoTrackToRestore;

    public OkPlayer(RendererBuilder rendererBuilder) {
        this.rendererBuilder = rendererBuilder;
        this.player = Factory.newInstance(4, AsyncHttpClient.DEFAULT_RETRY_SLEEP_TIME_MILLIS, Factory.DEFAULT_MIN_REBUFFER_MS);
        this.player.addListener(this);
        this.playerControl = new PlayerControl(this.player);
        this.mainHandler = new Handler();
        this.listeners = new CopyOnWriteArrayList();
        this.lastReportedPlaybackState = 1;
        this.rendererBuildingState = 1;
        this.player.setSelectedTrack(2, -1);
    }

    public void addListener(OkPlayerListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public PlayerControl getPlayerControl() {
        return this.playerControl;
    }

    public Handler getMainHandler() {
        return this.mainHandler;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
        pushSurface(false);
    }

    public Surface getSurface() {
        return this.surface;
    }

    public long getDuration() {
        return this.player.getDuration();
    }

    public long getBufferedPosition() {
        return this.player.getBufferedPosition();
    }

    public int getBufferedPercentage() {
        return this.player.getBufferedPercentage();
    }

    public Looper getPlaybackLooper() {
        return this.player.getPlaybackLooper();
    }

    public int getTrackCount(int rendererIndex) {
        return this.player.getTrackCount(rendererIndex);
    }

    public MediaFormat getTrackFormat(int rendererIndex, int trackIndex) {
        return this.player.getTrackFormat(rendererIndex, trackIndex);
    }

    public Format getFormat() {
        return this.videoFormat;
    }

    public BandwidthMeter getBandwidthMeter() {
        return this.bandwidthMeter;
    }

    public CodecCounters getCodecCounters() {
        return this.codecCounters;
    }

    public long getCurrentPosition() {
        return this.player.getCurrentPosition();
    }

    public void replaceRenderBuilder(RendererBuilder rendererBuilder) {
        this.rendererBuilder = rendererBuilder;
    }

    public boolean getPlayWhenReady() {
        return this.player.getPlayWhenReady();
    }

    public void blockingClearSurface() {
        this.surface = null;
        pushSurface(true);
    }

    public int getSelectedTrack(int type) {
        return this.player.getSelectedTrack(type);
    }

    public void setSelectedTrack(int type, int index) {
        this.player.setSelectedTrack(type, index);
        if (type == 2 && index < 0 && this.captionListener != null) {
            this.captionListener.onCues(Collections.emptyList());
        }
    }

    public boolean getBackgrounded() {
        return this.backgrounded;
    }

    public InternalErrorListener getInternalErrorListener() {
        return this.internalErrorListener;
    }

    public void setInternalErrorListener(InternalErrorListener internalErrorListener) {
        this.internalErrorListener = internalErrorListener;
    }

    public CaptionListener getCaptionListener() {
        return this.captionListener;
    }

    public void setCaptionListener(CaptionListener captionListener) {
        this.captionListener = captionListener;
    }

    public Id3MetadataListener getId3MetadataListener() {
        return this.id3MetadataListener;
    }

    public void setId3MetadataListener(Id3MetadataListener id3MetadataListener) {
        this.id3MetadataListener = id3MetadataListener;
    }

    public InfoListener getInfoListener() {
        return this.infoListener;
    }

    public void setInfoListener(InfoListener infoListener) {
        this.infoListener = infoListener;
    }

    public void setBackgrounded(boolean backgrounded) {
        if (this.backgrounded != backgrounded) {
            this.backgrounded = backgrounded;
            if (backgrounded) {
                this.videoTrackToRestore = getSelectedTrack(0);
                setSelectedTrack(0, -1);
                blockingClearSurface();
                return;
            }
            setSelectedTrack(0, 0);
        }
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.player.setPlayWhenReady(playWhenReady);
    }

    public void prepare() {
        if (!this.prepared && this.rendererBuilder != null) {
            if (this.rendererBuildingState == 3) {
                this.player.stop();
            }
            this.rendererBuilder.cancel();
            this.videoFormat = null;
            this.videoRenderer = null;
            this.rendererBuildingState = 2;
            maybeReportPlayerState();
            this.rendererBuilder.buildRenderers(this);
            this.prepared = true;
        }
    }

    public void seekTo(long positionMs) {
        this.player.seekTo(positionMs);
    }

    public void release() {
        if (this.rendererBuilder != null) {
            this.rendererBuilder.cancel();
        }
        this.rendererBuildingState = 1;
        this.surface = null;
        this.listeners.clear();
        this.player.release();
    }

    public void onRenderers(TrackRenderer[] renderers, BandwidthMeter bandwidthMeter) {
        for (int i = 0; i < 4; i++) {
            if (renderers[i] == null) {
                renderers[i] = new DummyTrackRenderer();
            }
        }
        this.videoRenderer = renderers[0];
        this.audioRenderer = renderers[1];
        pushSurface(false);
        this.player.prepare(renderers);
        this.rendererBuildingState = 3;
    }

    public void pushSurface(boolean blockForSurfacePush) {
        if (this.videoRenderer != null) {
            if (blockForSurfacePush) {
                this.player.blockingSendMessage(this.videoRenderer, 1, this.surface);
            } else {
                this.player.sendMessage(this.videoRenderer, 1, this.surface);
            }
        }
    }

    public void onRenderersError(Exception e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onRendererInitializationError(e);
        }
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((OkPlayerListener) i$.next()).onError(e);
        }
        this.rendererBuildingState = 1;
        maybeReportPlayerState();
    }

    private void maybeReportPlayerState() {
        boolean playWhenReady = this.player.getPlayWhenReady();
        int playbackState = getPlaybackState();
        if (this.lastReportedPlayWhenReady != playWhenReady || this.lastReportedPlaybackState != playbackState) {
            Iterator i$ = this.listeners.iterator();
            while (i$.hasNext()) {
                ((OkPlayerListener) i$.next()).onStateChanged(playWhenReady, playbackState);
            }
            this.lastReportedPlayWhenReady = playWhenReady;
            this.lastReportedPlaybackState = playbackState;
        }
    }

    public int getPlaybackState() {
        if (this.rendererBuildingState == 2) {
            return 2;
        }
        int playerState = this.player.getPlaybackState();
        if (this.rendererBuildingState == 3 && playerState == 1) {
            return 2;
        }
        return playerState;
    }

    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        maybeReportPlayerState();
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((OkPlayerListener) i$.next()).onStateChanged(playWhenReady, playbackState);
        }
    }

    public void onPlayWhenReadyCommitted() {
    }

    public void onPlayerError(ExoPlaybackException error) {
        this.rendererBuildingState = 1;
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((OkPlayerListener) i$.next()).onError(error);
        }
    }

    public void onDecoderInitializationError(DecoderInitializationException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onDecoderInitializationError(e);
        }
    }

    public void onCryptoError(CryptoException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onCryptoError(e);
        }
    }

    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
    }

    public void onDroppedFrames(int count, long elapsed) {
        if (this.infoListener != null) {
            this.infoListener.onDroppedFrames(count, elapsed);
        }
    }

    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((OkPlayerListener) i$.next()).onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    public void onDrawnToSurface(Surface surface) {
    }

    public void onAudioTrackInitializationError(InitializationException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onAudioTrackInitializationError(e);
        }
    }

    public void onAudioTrackWriteError(WriteException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onAudioTrackWriteError(e);
        }
    }

    public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
    }

    public void onLoadStarted(int sourceId, long length, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
    }

    public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
    }

    public void onLoadCanceled(int sourceId, long bytesLoaded) {
    }

    public void onLoadError(int sourceId, IOException e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onLoadError(sourceId, e);
        }
    }

    public void onUpstreamDiscarded(int sourceId, long mediaStartTimeMs, long mediaEndTimeMs) {
    }

    public void onDownstreamFormatChanged(int sourceId, Format format, int trigger, long mediaTimeMs) {
        if (this.infoListener != null) {
            if (sourceId == 0) {
                this.videoFormat = format;
                this.infoListener.onVideoFormatEnabled(format, trigger, mediaTimeMs);
            } else if (sourceId == 1) {
                this.infoListener.onAudioFormatEnabled(format, trigger, mediaTimeMs);
            }
        }
    }

    public void onBandwidthSample(int elapsedMs, long bytes, long bitrate) {
        if (this.infoListener != null) {
            this.infoListener.onBandwidthSample(elapsedMs, bytes, bitrate);
        }
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            OkPlayerListener okPlayerListener = (OkPlayerListener) i$.next();
            if (okPlayerListener instanceof OkMuxListener) {
                ((OkMuxListener) okPlayerListener).onBandwidthSample(elapsedMs, bytes, bitrate);
            }
        }
    }

    public void onDrmKeysLoaded() {
    }

    public void onDrmSessionManagerError(Exception e) {
        if (this.internalErrorListener != null) {
            this.internalErrorListener.onDrmSessionManagerError(e);
        }
    }
}
