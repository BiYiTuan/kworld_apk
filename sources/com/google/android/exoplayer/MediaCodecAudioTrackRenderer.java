package com.google.android.exoplayer;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.PlaybackParams;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.audio.AudioTrack.InitializationException;
import com.google.android.exoplayer.audio.AudioTrack.WriteException;
import com.google.android.exoplayer.drm.DrmSessionManager;
import com.google.android.exoplayer.util.MimeTypes;
import java.nio.ByteBuffer;

@TargetApi(16)
public class MediaCodecAudioTrackRenderer extends MediaCodecTrackRenderer implements MediaClock {
    public static final int MSG_SET_PLAYBACK_PARAMS = 2;
    public static final int MSG_SET_VOLUME = 1;
    private static final String RAW_DECODER_NAME = "OMX.google.raw.decoder";
    private boolean allowPositionDiscontinuity;
    private int audioSessionId;
    private final AudioTrack audioTrack;
    private boolean audioTrackHasData;
    private long currentPositionUs;
    private final EventListener eventListener;
    private long lastFeedElapsedRealtimeMs;
    private MediaFormat passthroughMediaFormat;

    public interface EventListener extends com.google.android.exoplayer.MediaCodecTrackRenderer.EventListener {
        void onAudioTrackInitializationError(InitializationException initializationException);

        void onAudioTrackUnderrun(int i, long j, long j2);

        void onAudioTrackWriteError(WriteException writeException);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source) {
        this(source, null, true);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys) {
        this(source, drmSessionManager, playClearSamplesWithoutKeys, null, null);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, Handler eventHandler, EventListener eventListener) {
        this(source, null, true, eventHandler, eventListener);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener) {
        this(source, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener, null);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener, AudioCapabilities audioCapabilities) {
        this(source, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener, audioCapabilities, 3);
    }

    public MediaCodecAudioTrackRenderer(SampleSource source, DrmSessionManager drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, EventListener eventListener, AudioCapabilities audioCapabilities, int streamType) {
        super(source, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener);
        this.eventListener = eventListener;
        this.audioSessionId = 0;
        this.audioTrack = new AudioTrack(audioCapabilities, streamType);
    }

    protected DecoderInfo getDecoderInfo(String mimeType, boolean requiresSecureDecoder) throws DecoderQueryException {
        return allowPassthrough(mimeType) ? new DecoderInfo(RAW_DECODER_NAME, true) : super.getDecoderInfo(mimeType, requiresSecureDecoder);
    }

    protected boolean allowPassthrough(String mimeType) {
        return this.audioTrack.isPassthroughSupported(mimeType);
    }

    protected void configureCodec(MediaCodec codec, String codecName, boolean codecIsAdaptive, MediaFormat format, MediaCrypto crypto) {
        String mimeType = format.getString(io.vov.vitamio.MediaFormat.KEY_MIME);
        if (!RAW_DECODER_NAME.equals(codecName) || MimeTypes.AUDIO_RAW.equals(mimeType)) {
            codec.configure(format, null, crypto, 0);
            this.passthroughMediaFormat = null;
            return;
        }
        format.setString(io.vov.vitamio.MediaFormat.KEY_MIME, MimeTypes.AUDIO_RAW);
        codec.configure(format, null, crypto, 0);
        format.setString(io.vov.vitamio.MediaFormat.KEY_MIME, mimeType);
        this.passthroughMediaFormat = format;
    }

    protected MediaClock getMediaClock() {
        return this;
    }

    protected boolean handlesTrack(MediaFormat mediaFormat) throws DecoderQueryException {
        String mimeType = mediaFormat.mimeType;
        if (!MimeTypes.isAudio(mimeType)) {
            return false;
        }
        if (MimeTypes.AUDIO_UNKNOWN.equals(mimeType) || allowPassthrough(mimeType) || MediaCodecUtil.getDecoderInfo(mimeType, false) != null) {
            return true;
        }
        return false;
    }

    protected void onEnabled(int track, long positionUs, boolean joining) throws ExoPlaybackException {
        super.onEnabled(track, positionUs, joining);
        seekToInternal(positionUs);
    }

    protected void onOutputFormatChanged(MediaFormat outputFormat) {
        boolean passthrough = this.passthroughMediaFormat != null;
        AudioTrack audioTrack = this.audioTrack;
        if (passthrough) {
            outputFormat = this.passthroughMediaFormat;
        }
        audioTrack.configure(outputFormat, passthrough);
    }

    protected void onAudioSessionId(int audioSessionId) {
    }

    protected void onStarted() {
        super.onStarted();
        this.audioTrack.play();
    }

    protected void onStopped() {
        this.audioTrack.pause();
        super.onStopped();
    }

    protected boolean isEnded() {
        return super.isEnded() && !this.audioTrack.hasPendingData();
    }

    protected boolean isReady() {
        return this.audioTrack.hasPendingData() || super.isReady();
    }

    public long getPositionUs() {
        long newCurrentPositionUs = this.audioTrack.getCurrentPositionUs(isEnded());
        if (newCurrentPositionUs != Long.MIN_VALUE) {
            if (!this.allowPositionDiscontinuity) {
                newCurrentPositionUs = Math.max(this.currentPositionUs, newCurrentPositionUs);
            }
            this.currentPositionUs = newCurrentPositionUs;
            this.allowPositionDiscontinuity = false;
        }
        return this.currentPositionUs;
    }

    protected void onDisabled() throws ExoPlaybackException {
        this.audioSessionId = 0;
        try {
            this.audioTrack.release();
        } finally {
            super.onDisabled();
        }
    }

    protected void seekTo(long positionUs) throws ExoPlaybackException {
        super.seekTo(positionUs);
        seekToInternal(positionUs);
    }

    private void seekToInternal(long positionUs) {
        this.audioTrack.reset();
        this.currentPositionUs = positionUs;
        this.allowPositionDiscontinuity = true;
    }

    protected boolean processOutputBuffer(long positionUs, long elapsedRealtimeUs, MediaCodec codec, ByteBuffer buffer, BufferInfo bufferInfo, int bufferIndex, boolean shouldSkip) throws ExoPlaybackException {
        if (shouldSkip) {
            codec.releaseOutputBuffer(bufferIndex, false);
            CodecCounters codecCounters = this.codecCounters;
            codecCounters.skippedOutputBufferCount++;
            this.audioTrack.handleDiscontinuity();
            return true;
        }
        if (this.audioTrack.isInitialized()) {
            boolean audioTrackHadData = this.audioTrackHasData;
            this.audioTrackHasData = this.audioTrack.hasPendingData();
            if (audioTrackHadData && !this.audioTrackHasData && getState() == 3) {
                long elapsedSinceLastFeedMs = SystemClock.elapsedRealtime() - this.lastFeedElapsedRealtimeMs;
                long bufferSizeUs = this.audioTrack.getBufferSizeUs();
                notifyAudioTrackUnderrun(this.audioTrack.getBufferSize(), bufferSizeUs == -1 ? -1 : bufferSizeUs / 1000, elapsedSinceLastFeedMs);
            }
        } else {
            try {
                if (this.audioSessionId != 0) {
                    this.audioTrack.initialize(this.audioSessionId);
                } else {
                    this.audioSessionId = this.audioTrack.initialize();
                    onAudioSessionId(this.audioSessionId);
                }
                this.audioTrackHasData = false;
                if (getState() == 3) {
                    this.audioTrack.play();
                }
            } catch (Throwable e) {
                notifyAudioTrackInitializationError(e);
                throw new ExoPlaybackException(e);
            }
        }
        try {
            int handleBufferResult = this.audioTrack.handleBuffer(buffer, bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs);
            this.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
            if ((handleBufferResult & 1) != 0) {
                handleDiscontinuity();
                this.allowPositionDiscontinuity = true;
            }
            if ((handleBufferResult & 2) == 0) {
                return false;
            }
            codec.releaseOutputBuffer(bufferIndex, false);
            codecCounters = this.codecCounters;
            codecCounters.renderedOutputBufferCount++;
            return true;
        } catch (Throwable e2) {
            notifyAudioTrackWriteError(e2);
            throw new ExoPlaybackException(e2);
        }
    }

    protected void onOutputStreamEnded() {
        this.audioTrack.handleEndOfStream();
    }

    protected void handleDiscontinuity() {
    }

    public void handleMessage(int messageType, Object message) throws ExoPlaybackException {
        switch (messageType) {
            case 1:
                this.audioTrack.setVolume(((Float) message).floatValue());
                return;
            case 2:
                this.audioTrack.setPlaybackParams((PlaybackParams) message);
                return;
            default:
                super.handleMessage(messageType, message);
                return;
        }
    }

    private void notifyAudioTrackInitializationError(final InitializationException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackInitializationError(e);
                }
            });
        }
    }

    private void notifyAudioTrackWriteError(final WriteException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackWriteError(e);
                }
            });
        }
    }

    private void notifyAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        if (this.eventHandler != null && this.eventListener != null) {
            final int i = bufferSize;
            final long j = bufferSizeMs;
            final long j2 = elapsedSinceLastFeedMs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    MediaCodecAudioTrackRenderer.this.eventListener.onAudioTrackUnderrun(i, j, j2);
                }
            });
        }
    }
}
