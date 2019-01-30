package com.google.android.exoplayer.hls;

import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer.C0665C;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.MediaFormatHolder;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.SampleSource.SampleSourceReader;
import com.google.android.exoplayer.chunk.BaseChunkSampleSourceEventListener;
import com.google.android.exoplayer.chunk.Chunk;
import com.google.android.exoplayer.chunk.ChunkOperationHolder;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.upstream.Loader.Callback;
import com.google.android.exoplayer.upstream.Loader.Loadable;
import com.google.android.exoplayer.util.Assertions;
import java.io.IOException;
import java.util.LinkedList;

public final class HlsSampleSource implements SampleSource, SampleSourceReader, Callback {
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private static final long NO_RESET_PENDING = Long.MIN_VALUE;
    private final int bufferSizeContribution;
    private final ChunkOperationHolder chunkOperationHolder;
    private final HlsChunkSource chunkSource;
    private long currentLoadStartTimeMs;
    private Chunk currentLoadable;
    private IOException currentLoadableException;
    private int currentLoadableExceptionCount;
    private long currentLoadableExceptionTimestamp;
    private TsChunk currentTsLoadable;
    private Format downstreamFormat;
    private MediaFormat[] downstreamMediaFormats;
    private long downstreamPositionUs;
    private int enabledTrackCount;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private final int eventSourceId;
    private final LinkedList<HlsExtractorWrapper> extractors;
    private long lastSeekPositionUs;
    private final LoadControl loadControl;
    private boolean loadControlRegistered;
    private Loader loader;
    private boolean loadingFinished;
    private final int minLoadableRetryCount;
    private boolean[] pendingDiscontinuities;
    private long pendingResetPositionUs;
    private boolean prepared;
    private TsChunk previousTsLoadable;
    private int remainingReleaseCount;
    private int trackCount;
    private boolean[] trackEnabledStates;
    private MediaFormat[] trackFormat;

    public interface EventListener extends BaseChunkSampleSourceEventListener {
    }

    public HlsSampleSource(HlsChunkSource chunkSource, LoadControl loadControl, int bufferSizeContribution) {
        this(chunkSource, loadControl, bufferSizeContribution, null, null, 0);
    }

    public HlsSampleSource(HlsChunkSource chunkSource, LoadControl loadControl, int bufferSizeContribution, Handler eventHandler, EventListener eventListener, int eventSourceId) {
        this(chunkSource, loadControl, bufferSizeContribution, eventHandler, eventListener, eventSourceId, 3);
    }

    public HlsSampleSource(HlsChunkSource chunkSource, LoadControl loadControl, int bufferSizeContribution, Handler eventHandler, EventListener eventListener, int eventSourceId, int minLoadableRetryCount) {
        this.chunkSource = chunkSource;
        this.loadControl = loadControl;
        this.bufferSizeContribution = bufferSizeContribution;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.eventSourceId = eventSourceId;
        this.pendingResetPositionUs = Long.MIN_VALUE;
        this.extractors = new LinkedList();
        this.chunkOperationHolder = new ChunkOperationHolder();
    }

    public SampleSourceReader register() {
        this.remainingReleaseCount++;
        return this;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean prepare(long r10) {
        /*
        r9 = this;
        r6 = 1;
        r5 = r9.prepared;
        if (r5 == 0) goto L_0x0007;
    L_0x0005:
        r5 = r6;
    L_0x0006:
        return r5;
    L_0x0007:
        r5 = r9.extractors;
        r5 = r5.isEmpty();
        if (r5 != 0) goto L_0x007a;
    L_0x000f:
        r5 = r9.extractors;
        r2 = r5.getFirst();
        r2 = (com.google.android.exoplayer.hls.HlsExtractorWrapper) r2;
        r5 = r2.isPrepared();
        if (r5 == 0) goto L_0x0066;
    L_0x001d:
        r5 = r2.getTrackCount();
        r9.trackCount = r5;
        r5 = r9.trackCount;
        r5 = new boolean[r5];
        r9.trackEnabledStates = r5;
        r5 = r9.trackCount;
        r5 = new boolean[r5];
        r9.pendingDiscontinuities = r5;
        r5 = r9.trackCount;
        r5 = new com.google.android.exoplayer.MediaFormat[r5];
        r9.downstreamMediaFormats = r5;
        r5 = r9.trackCount;
        r5 = new com.google.android.exoplayer.MediaFormat[r5];
        r9.trackFormat = r5;
        r5 = r9.chunkSource;
        r0 = r5.getDurationUs();
        r4 = 0;
    L_0x0042:
        r5 = r9.trackCount;
        if (r4 >= r5) goto L_0x0062;
    L_0x0046:
        r5 = r2.getMediaFormat(r4);
        r3 = r5.copyWithDurationUs(r0);
        r5 = r3.mimeType;
        r5 = com.google.android.exoplayer.util.MimeTypes.isVideo(r5);
        if (r5 == 0) goto L_0x005b;
    L_0x0056:
        r5 = 0;
        r3 = r3.copyAsAdaptive(r5);
    L_0x005b:
        r5 = r9.trackFormat;
        r5[r4] = r3;
        r4 = r4 + 1;
        goto L_0x0042;
    L_0x0062:
        r9.prepared = r6;
        r5 = r6;
        goto L_0x0006;
    L_0x0066:
        r5 = r9.extractors;
        r5 = r5.size();
        if (r5 <= r6) goto L_0x007a;
    L_0x006e:
        r5 = r9.extractors;
        r5 = r5.removeFirst();
        r5 = (com.google.android.exoplayer.hls.HlsExtractorWrapper) r5;
        r5.clear();
        goto L_0x000f;
    L_0x007a:
        r5 = r9.loader;
        if (r5 != 0) goto L_0x0087;
    L_0x007e:
        r5 = new com.google.android.exoplayer.upstream.Loader;
        r7 = "Loader:HLS";
        r5.<init>(r7);
        r9.loader = r5;
    L_0x0087:
        r5 = r9.loadControlRegistered;
        if (r5 != 0) goto L_0x0094;
    L_0x008b:
        r5 = r9.loadControl;
        r7 = r9.bufferSizeContribution;
        r5.register(r9, r7);
        r9.loadControlRegistered = r6;
    L_0x0094:
        r5 = r9.loader;
        r5 = r5.isLoading();
        if (r5 != 0) goto L_0x00a0;
    L_0x009c:
        r9.pendingResetPositionUs = r10;
        r9.downstreamPositionUs = r10;
    L_0x00a0:
        r9.maybeStartLoading();
        r5 = 0;
        goto L_0x0006;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.hls.HlsSampleSource.prepare(long):boolean");
    }

    public int getTrackCount() {
        Assertions.checkState(this.prepared);
        return this.trackCount;
    }

    public MediaFormat getFormat(int track) {
        Assertions.checkState(this.prepared);
        return this.trackFormat[track];
    }

    public void enable(int track, long positionUs) {
        boolean z;
        Assertions.checkState(this.prepared);
        if (this.trackEnabledStates[track]) {
            z = false;
        } else {
            z = true;
        }
        Assertions.checkState(z);
        this.enabledTrackCount++;
        this.trackEnabledStates[track] = true;
        this.downstreamMediaFormats[track] = null;
        this.pendingDiscontinuities[track] = false;
        this.downstreamFormat = null;
        boolean wasLoadControlRegistered = this.loadControlRegistered;
        if (!this.loadControlRegistered) {
            this.loadControl.register(this, this.bufferSizeContribution);
            this.loadControlRegistered = true;
        }
        if (this.enabledTrackCount == 1) {
            this.lastSeekPositionUs = positionUs;
            if (wasLoadControlRegistered && this.downstreamPositionUs == positionUs) {
                maybeStartLoading();
                return;
            }
            this.downstreamPositionUs = positionUs;
            restartFrom(positionUs);
        }
    }

    public void disable(int track) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[track]);
        this.enabledTrackCount--;
        this.trackEnabledStates[track] = false;
        if (this.enabledTrackCount == 0) {
            this.chunkSource.reset();
            this.downstreamPositionUs = Long.MIN_VALUE;
            if (this.loadControlRegistered) {
                this.loadControl.unregister(this);
                this.loadControlRegistered = false;
            }
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
                return;
            }
            clearState();
            this.loadControl.trimAllocator();
        }
    }

    public boolean continueBuffering(int track, long playbackPositionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[track]);
        this.downstreamPositionUs = playbackPositionUs;
        if (!this.extractors.isEmpty()) {
            discardSamplesForDisabledTracks(getCurrentExtractor(), this.downstreamPositionUs);
        }
        if (this.loadingFinished) {
            return true;
        }
        maybeStartLoading();
        if (isPendingReset() || this.extractors.isEmpty()) {
            return false;
        }
        int extractorIndex = 0;
        while (extractorIndex < this.extractors.size()) {
            HlsExtractorWrapper extractor = (HlsExtractorWrapper) this.extractors.get(extractorIndex);
            if (!extractor.isPrepared()) {
                break;
            } else if (extractor.hasSamples(track)) {
                return true;
            } else {
                extractorIndex++;
            }
        }
        return false;
    }

    public int readData(int track, long playbackPositionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder, boolean onlyReadDiscontinuity) {
        Assertions.checkState(this.prepared);
        this.downstreamPositionUs = playbackPositionUs;
        if (this.pendingDiscontinuities[track]) {
            this.pendingDiscontinuities[track] = false;
            return -5;
        } else if (onlyReadDiscontinuity) {
            return -2;
        } else {
            if (isPendingReset()) {
                return -2;
            }
            HlsExtractorWrapper extractor = getCurrentExtractor();
            if (!extractor.isPrepared()) {
                return -2;
            }
            if (this.downstreamFormat == null || !this.downstreamFormat.equals(extractor.format)) {
                notifyDownstreamFormatChanged(extractor.format, extractor.trigger, extractor.startTimeUs);
                this.downstreamFormat = extractor.format;
            }
            if (this.extractors.size() > 1) {
                extractor.configureSpliceTo((HlsExtractorWrapper) this.extractors.get(1));
            }
            int extractorIndex = 0;
            while (this.extractors.size() > extractorIndex + 1 && !extractor.hasSamples(track)) {
                extractorIndex++;
                extractor = (HlsExtractorWrapper) this.extractors.get(extractorIndex);
                if (!extractor.isPrepared()) {
                    return -2;
                }
            }
            MediaFormat mediaFormat = extractor.getMediaFormat(track);
            if (mediaFormat != null && !mediaFormat.equals(this.downstreamMediaFormats[track])) {
                formatHolder.format = mediaFormat;
                this.downstreamMediaFormats[track] = mediaFormat;
                return -4;
            } else if (extractor.getSample(track, sampleHolder)) {
                boolean decodeOnly = sampleHolder.timeUs < this.lastSeekPositionUs;
                sampleHolder.flags = (decodeOnly ? C0665C.SAMPLE_FLAG_DECODE_ONLY : 0) | sampleHolder.flags;
                return -3;
            } else if (this.loadingFinished) {
                return -1;
            } else {
                return -2;
            }
        }
    }

    public void maybeThrowError() throws IOException {
        if (this.currentLoadableException != null && this.currentLoadableExceptionCount > this.minLoadableRetryCount) {
            throw this.currentLoadableException;
        } else if (this.currentLoadable == null) {
            this.chunkSource.maybeThrowError();
        }
    }

    public void seekToUs(long positionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.enabledTrackCount > 0);
        long currentPositionUs = isPendingReset() ? this.pendingResetPositionUs : this.downstreamPositionUs;
        this.downstreamPositionUs = positionUs;
        this.lastSeekPositionUs = positionUs;
        if (currentPositionUs != positionUs) {
            this.downstreamPositionUs = positionUs;
            for (int i = 0; i < this.pendingDiscontinuities.length; i++) {
                this.pendingDiscontinuities[i] = true;
            }
            restartFrom(positionUs);
        }
    }

    public long getBufferedPositionUs() {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.enabledTrackCount > 0);
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        if (this.loadingFinished) {
            return -3;
        }
        long largestParsedTimestampUs = ((HlsExtractorWrapper) this.extractors.getLast()).getLargestParsedTimestampUs();
        if (this.extractors.size() > 1) {
            largestParsedTimestampUs = Math.max(largestParsedTimestampUs, ((HlsExtractorWrapper) this.extractors.get(this.extractors.size() - 2)).getLargestParsedTimestampUs());
        }
        return largestParsedTimestampUs == Long.MIN_VALUE ? this.downstreamPositionUs : largestParsedTimestampUs;
    }

    public void release() {
        Assertions.checkState(this.remainingReleaseCount > 0);
        int i = this.remainingReleaseCount - 1;
        this.remainingReleaseCount = i;
        if (i == 0 && this.loader != null) {
            this.loader.release();
            this.loader = null;
        }
    }

    public void onLoadCompleted(Loadable loadable) {
        boolean z;
        boolean z2 = true;
        if (loadable == this.currentLoadable) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        long now = SystemClock.elapsedRealtime();
        long loadDurationMs = now - this.currentLoadStartTimeMs;
        this.chunkSource.onChunkLoadCompleted(this.currentLoadable);
        if (isTsChunk(this.currentLoadable)) {
            if (this.currentLoadable != this.currentTsLoadable) {
                z2 = false;
            }
            Assertions.checkState(z2);
            this.previousTsLoadable = this.currentTsLoadable;
            notifyLoadCompleted(this.currentLoadable.bytesLoaded(), this.currentTsLoadable.type, this.currentTsLoadable.trigger, this.currentTsLoadable.format, this.currentTsLoadable.startTimeUs, this.currentTsLoadable.endTimeUs, now, loadDurationMs);
        } else {
            notifyLoadCompleted(this.currentLoadable.bytesLoaded(), this.currentLoadable.type, this.currentLoadable.trigger, this.currentLoadable.format, -1, -1, now, loadDurationMs);
        }
        clearCurrentLoadable();
        if (this.enabledTrackCount > 0 || !this.prepared) {
            maybeStartLoading();
        }
    }

    public void onLoadCanceled(Loadable loadable) {
        notifyLoadCanceled(this.currentLoadable.bytesLoaded());
        if (this.enabledTrackCount > 0) {
            restartFrom(this.pendingResetPositionUs);
            return;
        }
        clearState();
        this.loadControl.trimAllocator();
    }

    public void onLoadError(Loadable loadable, IOException e) {
        if (this.chunkSource.onChunkLoadError(this.currentLoadable, e)) {
            if (this.previousTsLoadable == null && !isPendingReset()) {
                this.pendingResetPositionUs = this.lastSeekPositionUs;
            }
            clearCurrentLoadable();
        } else {
            this.currentLoadableException = e;
            this.currentLoadableExceptionCount++;
            this.currentLoadableExceptionTimestamp = SystemClock.elapsedRealtime();
        }
        notifyLoadError(e);
        maybeStartLoading();
    }

    private HlsExtractorWrapper getCurrentExtractor() {
        HlsExtractorWrapper extractor = (HlsExtractorWrapper) this.extractors.getFirst();
        while (this.extractors.size() > 1 && !haveSamplesForEnabledTracks(extractor)) {
            ((HlsExtractorWrapper) this.extractors.removeFirst()).clear();
            extractor = (HlsExtractorWrapper) this.extractors.getFirst();
        }
        return extractor;
    }

    private void discardSamplesForDisabledTracks(HlsExtractorWrapper extractor, long timeUs) {
        if (extractor.isPrepared()) {
            for (int i = 0; i < this.trackEnabledStates.length; i++) {
                if (!this.trackEnabledStates[i]) {
                    extractor.discardUntil(i, timeUs);
                }
            }
        }
    }

    private boolean haveSamplesForEnabledTracks(HlsExtractorWrapper extractor) {
        if (!extractor.isPrepared()) {
            return false;
        }
        int i = 0;
        while (i < this.trackEnabledStates.length) {
            if (this.trackEnabledStates[i] && extractor.hasSamples(i)) {
                return true;
            }
            i++;
        }
        return false;
    }

    private void restartFrom(long positionUs) {
        this.pendingResetPositionUs = positionUs;
        this.loadingFinished = false;
        if (this.loader.isLoading()) {
            this.loader.cancelLoading();
            return;
        }
        clearState();
        maybeStartLoading();
    }

    private void clearState() {
        for (int i = 0; i < this.extractors.size(); i++) {
            ((HlsExtractorWrapper) this.extractors.get(i)).clear();
        }
        this.extractors.clear();
        clearCurrentLoadable();
        this.previousTsLoadable = null;
    }

    private void clearCurrentLoadable() {
        this.currentTsLoadable = null;
        this.currentLoadable = null;
        this.currentLoadableException = null;
        this.currentLoadableExceptionCount = 0;
    }

    private void maybeStartLoading() {
        long now = SystemClock.elapsedRealtime();
        long nextLoadPositionUs = getNextLoadPositionUs();
        boolean isBackedOff = this.currentLoadableException != null;
        boolean loadingOrBackedOff = this.loader.isLoading() || isBackedOff;
        boolean nextLoader = this.loadControl.update(this, this.downstreamPositionUs, nextLoadPositionUs, loadingOrBackedOff);
        if (isBackedOff) {
            if (now - this.currentLoadableExceptionTimestamp >= getRetryDelayMillis((long) this.currentLoadableExceptionCount)) {
                this.currentLoadableException = null;
                this.loader.startLoading(this.currentLoadable, this);
            }
        } else if (!this.loader.isLoading() && nextLoader) {
            long j;
            HlsChunkSource hlsChunkSource = this.chunkSource;
            TsChunk tsChunk = this.previousTsLoadable;
            if (this.pendingResetPositionUs != Long.MIN_VALUE) {
                j = this.pendingResetPositionUs;
            } else {
                j = this.downstreamPositionUs;
            }
            hlsChunkSource.getChunkOperation(tsChunk, j, this.chunkOperationHolder);
            boolean endOfStream = this.chunkOperationHolder.endOfStream;
            Chunk nextLoadable = this.chunkOperationHolder.chunk;
            this.chunkOperationHolder.clear();
            if (endOfStream) {
                this.loadingFinished = true;
            } else if (nextLoadable != null) {
                this.currentLoadStartTimeMs = now;
                this.currentLoadable = nextLoadable;
                if (isTsChunk(this.currentLoadable)) {
                    TsChunk tsChunk2 = (TsChunk) this.currentLoadable;
                    if (isPendingReset()) {
                        this.pendingResetPositionUs = Long.MIN_VALUE;
                    }
                    HlsExtractorWrapper extractorWrapper = tsChunk2.extractorWrapper;
                    if (this.extractors.isEmpty() || this.extractors.getLast() != extractorWrapper) {
                        extractorWrapper.init(this.loadControl.getAllocator());
                        this.extractors.addLast(extractorWrapper);
                    }
                    notifyLoadStarted(tsChunk2.dataSpec.length, tsChunk2.type, tsChunk2.trigger, tsChunk2.format, tsChunk2.startTimeUs, tsChunk2.endTimeUs);
                    this.currentTsLoadable = tsChunk2;
                } else {
                    notifyLoadStarted(this.currentLoadable.dataSpec.length, this.currentLoadable.type, this.currentLoadable.trigger, this.currentLoadable.format, -1, -1);
                }
                this.loader.startLoading(this.currentLoadable, this);
            }
        }
    }

    private long getNextLoadPositionUs() {
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        if (this.loadingFinished) {
            return -1;
        }
        return this.currentTsLoadable != null ? this.currentTsLoadable.endTimeUs : this.previousTsLoadable.endTimeUs;
    }

    private boolean isTsChunk(Chunk chunk) {
        return chunk instanceof TsChunk;
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != Long.MIN_VALUE;
    }

    private long getRetryDelayMillis(long errorCount) {
        return Math.min((errorCount - 1) * 1000, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
    }

    long usToMs(long timeUs) {
        return timeUs / 1000;
    }

    private void notifyLoadStarted(long length, int type, int trigger, Format format, long mediaStartTimeUs, long mediaEndTimeUs) {
        if (this.eventHandler != null && this.eventListener != null) {
            final long j = length;
            final int i = type;
            final int i2 = trigger;
            final Format format2 = format;
            final long j2 = mediaStartTimeUs;
            final long j3 = mediaEndTimeUs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    HlsSampleSource.this.eventListener.onLoadStarted(HlsSampleSource.this.eventSourceId, j, i, i2, format2, HlsSampleSource.this.usToMs(j2), HlsSampleSource.this.usToMs(j3));
                }
            });
        }
    }

    private void notifyLoadCompleted(long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeUs, long mediaEndTimeUs, long elapsedRealtimeMs, long loadDurationMs) {
        if (this.eventHandler != null && this.eventListener != null) {
            final long j = bytesLoaded;
            final int i = type;
            final int i2 = trigger;
            final Format format2 = format;
            final long j2 = mediaStartTimeUs;
            final long j3 = mediaEndTimeUs;
            final long j4 = elapsedRealtimeMs;
            final long j5 = loadDurationMs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    HlsSampleSource.this.eventListener.onLoadCompleted(HlsSampleSource.this.eventSourceId, j, i, i2, format2, HlsSampleSource.this.usToMs(j2), HlsSampleSource.this.usToMs(j3), j4, j5);
                }
            });
        }
    }

    private void notifyLoadCanceled(final long bytesLoaded) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    HlsSampleSource.this.eventListener.onLoadCanceled(HlsSampleSource.this.eventSourceId, bytesLoaded);
                }
            });
        }
    }

    private void notifyLoadError(final IOException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    HlsSampleSource.this.eventListener.onLoadError(HlsSampleSource.this.eventSourceId, e);
                }
            });
        }
    }

    private void notifyDownstreamFormatChanged(Format format, int trigger, long positionUs) {
        if (this.eventHandler != null && this.eventListener != null) {
            final Format format2 = format;
            final int i = trigger;
            final long j = positionUs;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    HlsSampleSource.this.eventListener.onDownstreamFormatChanged(HlsSampleSource.this.eventSourceId, format2, i, HlsSampleSource.this.usToMs(j));
                }
            });
        }
    }
}
