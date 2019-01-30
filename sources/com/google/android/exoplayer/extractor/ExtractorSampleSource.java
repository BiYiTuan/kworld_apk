package com.google.android.exoplayer.extractor;

import android.net.Uri;
import android.os.SystemClock;
import android.util.SparseArray;
import com.google.android.exoplayer.C0665C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.MediaFormatHolder;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.SampleSource.SampleSourceReader;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.upstream.Loader.Callback;
import com.google.android.exoplayer.upstream.Loader.Loadable;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ExtractorSampleSource implements SampleSource, SampleSourceReader, ExtractorOutput, Callback {
    private static final List<Class<? extends Extractor>> DEFAULT_EXTRACTOR_CLASSES = new ArrayList();
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT_LIVE = 6;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT_ON_DEMAND = 3;
    private static final int MIN_RETRY_COUNT_DEFAULT_FOR_MEDIA = -1;
    private static final long NO_RESET_PENDING = Long.MIN_VALUE;
    private final Allocator allocator;
    private IOException currentLoadableException;
    private int currentLoadableExceptionCount;
    private long currentLoadableExceptionTimestamp;
    private final DataSource dataSource;
    private long downstreamPositionUs;
    private volatile DrmInitData drmInitData;
    private int enabledTrackCount;
    private int extractedSampleCount;
    private int extractedSampleCountAtStartOfLoad;
    private final ExtractorHolder extractorHolder;
    private boolean havePendingNextSampleUs;
    private long lastSeekPositionUs;
    private ExtractingLoadable loadable;
    private Loader loader;
    private boolean loadingFinished;
    private long maxTrackDurationUs;
    private MediaFormat[] mediaFormats;
    private final int minLoadableRetryCount;
    private boolean[] pendingDiscontinuities;
    private boolean[] pendingMediaFormat;
    private long pendingNextSampleUs;
    private long pendingResetPositionUs;
    private boolean prepared;
    private int remainingReleaseCount;
    private final int requestedBufferSize;
    private final SparseArray<InternalTrackOutput> sampleQueues;
    private long sampleTimeOffsetUs;
    private volatile SeekMap seekMap;
    private boolean[] trackEnabledStates;
    private volatile boolean tracksBuilt;
    private final Uri uri;

    private static final class ExtractorHolder {
        private Extractor extractor;
        private final ExtractorOutput extractorOutput;
        private final Extractor[] extractors;

        public ExtractorHolder(Extractor[] extractors, ExtractorOutput extractorOutput) {
            this.extractors = extractors;
            this.extractorOutput = extractorOutput;
        }

        public Extractor selectExtractor(ExtractorInput input) throws UnrecognizedInputFormatException, IOException, InterruptedException {
            if (this.extractor != null) {
                return this.extractor;
            }
            Extractor[] arr$ = this.extractors;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Extractor extractor = arr$[i$];
                try {
                    if (extractor.sniff(input)) {
                        this.extractor = extractor;
                        break;
                    }
                    input.resetPeekPosition();
                    i$++;
                } catch (EOFException e) {
                }
            }
            if (this.extractor == null) {
                throw new UnrecognizedInputFormatException(this.extractors);
            }
            this.extractor.init(this.extractorOutput);
            return this.extractor;
        }
    }

    private static class ExtractingLoadable implements Loadable {
        private final Allocator allocator;
        private final DataSource dataSource;
        private final ExtractorHolder extractorHolder;
        private volatile boolean loadCanceled;
        private boolean pendingExtractorSeek;
        private final PositionHolder positionHolder = new PositionHolder();
        private final int requestedBufferSize;
        private final Uri uri;

        public ExtractingLoadable(Uri uri, DataSource dataSource, ExtractorHolder extractorHolder, Allocator allocator, int requestedBufferSize, long position) {
            this.uri = (Uri) Assertions.checkNotNull(uri);
            this.dataSource = (DataSource) Assertions.checkNotNull(dataSource);
            this.extractorHolder = (ExtractorHolder) Assertions.checkNotNull(extractorHolder);
            this.allocator = (Allocator) Assertions.checkNotNull(allocator);
            this.requestedBufferSize = requestedBufferSize;
            this.positionHolder.position = position;
            this.pendingExtractorSeek = true;
        }

        public void cancelLoad() {
            this.loadCanceled = true;
        }

        public boolean isLoadCanceled() {
            return this.loadCanceled;
        }

        public void load() throws IOException, InterruptedException {
            Throwable th;
            int result = 0;
            while (result == 0 && !this.loadCanceled) {
                ExtractorInput input;
                try {
                    long position = this.positionHolder.position;
                    long length = this.dataSource.open(new DataSpec(this.uri, position, -1, null));
                    if (length != -1) {
                        length += position;
                    }
                    input = new DefaultExtractorInput(this.dataSource, position, length);
                    try {
                        Extractor extractor = this.extractorHolder.selectExtractor(input);
                        if (this.pendingExtractorSeek) {
                            extractor.seek();
                            this.pendingExtractorSeek = false;
                        }
                        while (result == 0 && !this.loadCanceled) {
                            this.allocator.blockWhileTotalBytesAllocatedExceeds(this.requestedBufferSize);
                            result = extractor.read(input, this.positionHolder);
                        }
                        if (result == 1) {
                            result = 0;
                        } else if (input != null) {
                            this.positionHolder.position = input.getPosition();
                        }
                        this.dataSource.close();
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    input = null;
                }
            }
            return;
            if (result != 1) {
                if (input != null) {
                    this.positionHolder.position = input.getPosition();
                }
            }
            this.dataSource.close();
            throw th;
        }
    }

    public static final class UnrecognizedInputFormatException extends ParserException {
        public UnrecognizedInputFormatException(Extractor[] extractors) {
            super("None of the available extractors (" + Util.getCommaDelimitedSimpleClassNames(extractors) + ") could read the stream.");
        }
    }

    private class InternalTrackOutput extends DefaultTrackOutput {
        public InternalTrackOutput(Allocator allocator) {
            super(allocator);
        }

        public void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
            super.sampleMetadata(timeUs, flags, size, offset, encryptionKey);
            ExtractorSampleSource.this.extractedSampleCount = ExtractorSampleSource.this.extractedSampleCount + 1;
        }
    }

    static {
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.webm.WebmExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.mp4.FragmentedMp4Extractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e2) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.mp4.Mp4Extractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e3) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.mp3.Mp3Extractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e4) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.ts.AdtsExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e5) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.ts.TsExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e6) {
        }
        try {
            DEFAULT_EXTRACTOR_CLASSES.add(Class.forName("com.google.android.exoplayer.extractor.flv.FlvExtractor").asSubclass(Extractor.class));
        } catch (ClassNotFoundException e7) {
        }
    }

    @Deprecated
    public ExtractorSampleSource(Uri uri, DataSource dataSource, int requestedBufferSize, Extractor... extractors) {
        this(uri, dataSource, new DefaultAllocator(65536), requestedBufferSize, extractors);
    }

    public ExtractorSampleSource(Uri uri, DataSource dataSource, Allocator allocator, int requestedBufferSize, Extractor... extractors) {
        this(uri, dataSource, allocator, requestedBufferSize, -1, extractors);
    }

    @Deprecated
    public ExtractorSampleSource(Uri uri, DataSource dataSource, int requestedBufferSize, int minLoadableRetryCount, Extractor... extractors) {
        this(uri, dataSource, new DefaultAllocator(65536), requestedBufferSize, minLoadableRetryCount, extractors);
    }

    public ExtractorSampleSource(Uri uri, DataSource dataSource, Allocator allocator, int requestedBufferSize, int minLoadableRetryCount, Extractor... extractors) {
        this.uri = uri;
        this.dataSource = dataSource;
        this.allocator = allocator;
        this.requestedBufferSize = requestedBufferSize;
        this.minLoadableRetryCount = minLoadableRetryCount;
        if (extractors == null || extractors.length == 0) {
            extractors = new Extractor[DEFAULT_EXTRACTOR_CLASSES.size()];
            int i = 0;
            while (i < extractors.length) {
                try {
                    extractors[i] = (Extractor) ((Class) DEFAULT_EXTRACTOR_CLASSES.get(i)).newInstance();
                    i++;
                } catch (InstantiationException e) {
                    throw new IllegalStateException("Unexpected error creating default extractor", e);
                } catch (IllegalAccessException e2) {
                    throw new IllegalStateException("Unexpected error creating default extractor", e2);
                }
            }
        }
        this.extractorHolder = new ExtractorHolder(extractors, this);
        this.sampleQueues = new SparseArray();
        this.pendingResetPositionUs = Long.MIN_VALUE;
    }

    public SampleSourceReader register() {
        this.remainingReleaseCount++;
        return this;
    }

    public boolean prepare(long positionUs) {
        if (this.prepared) {
            return true;
        }
        if (this.loader == null) {
            this.loader = new Loader("Loader:ExtractorSampleSource");
        }
        maybeStartLoading();
        if (this.seekMap == null || !this.tracksBuilt || !haveFormatsForAllTracks()) {
            return false;
        }
        int trackCount = this.sampleQueues.size();
        this.trackEnabledStates = new boolean[trackCount];
        this.pendingDiscontinuities = new boolean[trackCount];
        this.pendingMediaFormat = new boolean[trackCount];
        this.mediaFormats = new MediaFormat[trackCount];
        this.maxTrackDurationUs = -1;
        for (int i = 0; i < trackCount; i++) {
            MediaFormat format = ((InternalTrackOutput) this.sampleQueues.valueAt(i)).getFormat();
            this.mediaFormats[i] = format;
            if (format.durationUs != -1 && format.durationUs > this.maxTrackDurationUs) {
                this.maxTrackDurationUs = format.durationUs;
            }
        }
        this.prepared = true;
        return true;
    }

    public int getTrackCount() {
        return this.sampleQueues.size();
    }

    public MediaFormat getFormat(int track) {
        Assertions.checkState(this.prepared);
        return this.mediaFormats[track];
    }

    public void enable(int track, long positionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(!this.trackEnabledStates[track]);
        this.enabledTrackCount++;
        this.trackEnabledStates[track] = true;
        this.pendingMediaFormat[track] = true;
        this.pendingDiscontinuities[track] = false;
        if (this.enabledTrackCount == 1) {
            if (!this.seekMap.isSeekable()) {
                positionUs = 0;
            }
            this.downstreamPositionUs = positionUs;
            this.lastSeekPositionUs = positionUs;
            restartFrom(positionUs);
        }
    }

    public void disable(int track) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[track]);
        this.enabledTrackCount--;
        this.trackEnabledStates[track] = false;
        if (this.enabledTrackCount == 0) {
            this.downstreamPositionUs = Long.MIN_VALUE;
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
                return;
            }
            clearState();
            this.allocator.trim(0);
        }
    }

    public boolean continueBuffering(int track, long playbackPositionUs) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackEnabledStates[track]);
        this.downstreamPositionUs = playbackPositionUs;
        discardSamplesForDisabledTracks(this.downstreamPositionUs);
        if (this.loadingFinished) {
            return true;
        }
        maybeStartLoading();
        if (isPendingReset()) {
            return false;
        }
        return !((InternalTrackOutput) this.sampleQueues.valueAt(track)).isEmpty();
    }

    public int readData(int track, long playbackPositionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder, boolean onlyReadDiscontinuity) {
        this.downstreamPositionUs = playbackPositionUs;
        if (this.pendingDiscontinuities[track]) {
            this.pendingDiscontinuities[track] = false;
            return -5;
        } else if (onlyReadDiscontinuity || isPendingReset()) {
            return -2;
        } else {
            InternalTrackOutput sampleQueue = (InternalTrackOutput) this.sampleQueues.valueAt(track);
            if (this.pendingMediaFormat[track]) {
                formatHolder.format = sampleQueue.getFormat();
                formatHolder.drmInitData = this.drmInitData;
                this.pendingMediaFormat[track] = false;
                return -4;
            } else if (!sampleQueue.getSample(sampleHolder)) {
                return this.loadingFinished ? -1 : -2;
            } else {
                boolean decodeOnly;
                int i;
                if (sampleHolder.timeUs < this.lastSeekPositionUs) {
                    decodeOnly = true;
                } else {
                    decodeOnly = false;
                }
                int i2 = sampleHolder.flags;
                if (decodeOnly) {
                    i = C0665C.SAMPLE_FLAG_DECODE_ONLY;
                } else {
                    i = 0;
                }
                sampleHolder.flags = i | i2;
                if (this.havePendingNextSampleUs) {
                    this.sampleTimeOffsetUs = this.pendingNextSampleUs - sampleHolder.timeUs;
                    this.havePendingNextSampleUs = false;
                }
                sampleHolder.timeUs += this.sampleTimeOffsetUs;
                return -3;
            }
        }
    }

    public void maybeThrowError() throws IOException {
        if (this.currentLoadableException != null) {
            if (isCurrentLoadableExceptionFatal()) {
                throw this.currentLoadableException;
            }
            int minLoadableRetryCountForMedia;
            if (this.minLoadableRetryCount != -1) {
                minLoadableRetryCountForMedia = this.minLoadableRetryCount;
            } else {
                minLoadableRetryCountForMedia = (this.seekMap == null || this.seekMap.isSeekable()) ? 3 : 6;
            }
            if (this.currentLoadableExceptionCount > minLoadableRetryCountForMedia) {
                throw this.currentLoadableException;
            }
        }
    }

    public void seekToUs(long positionUs) {
        boolean z;
        Assertions.checkState(this.prepared);
        if (this.enabledTrackCount > 0) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!this.seekMap.isSeekable()) {
            positionUs = 0;
        }
        long currentPositionUs = isPendingReset() ? this.pendingResetPositionUs : this.downstreamPositionUs;
        this.downstreamPositionUs = positionUs;
        this.lastSeekPositionUs = positionUs;
        if (currentPositionUs != positionUs) {
            boolean seekInsideBuffer;
            if (isPendingReset()) {
                seekInsideBuffer = false;
            } else {
                seekInsideBuffer = true;
            }
            int i = 0;
            while (seekInsideBuffer && i < this.sampleQueues.size()) {
                seekInsideBuffer &= ((InternalTrackOutput) this.sampleQueues.valueAt(i)).skipToKeyframeBefore(positionUs);
                i++;
            }
            if (!seekInsideBuffer) {
                restartFrom(positionUs);
            }
            for (i = 0; i < this.pendingDiscontinuities.length; i++) {
                this.pendingDiscontinuities[i] = true;
            }
        }
    }

    public long getBufferedPositionUs() {
        if (this.loadingFinished) {
            return -3;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long largestParsedTimestampUs = Long.MIN_VALUE;
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            largestParsedTimestampUs = Math.max(largestParsedTimestampUs, ((InternalTrackOutput) this.sampleQueues.valueAt(i)).getLargestParsedTimestampUs());
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
        this.loadingFinished = true;
    }

    public void onLoadCanceled(Loadable loadable) {
        if (this.enabledTrackCount > 0) {
            restartFrom(this.pendingResetPositionUs);
            return;
        }
        clearState();
        this.allocator.trim(0);
    }

    public void onLoadError(Loadable ignored, IOException e) {
        this.currentLoadableException = e;
        this.currentLoadableExceptionCount = this.extractedSampleCount > this.extractedSampleCountAtStartOfLoad ? 1 : this.currentLoadableExceptionCount + 1;
        this.currentLoadableExceptionTimestamp = SystemClock.elapsedRealtime();
        maybeStartLoading();
    }

    public TrackOutput track(int id) {
        InternalTrackOutput sampleQueue = (InternalTrackOutput) this.sampleQueues.get(id);
        if (sampleQueue != null) {
            return sampleQueue;
        }
        sampleQueue = new InternalTrackOutput(this.allocator);
        this.sampleQueues.put(id, sampleQueue);
        return sampleQueue;
    }

    public void endTracks() {
        this.tracksBuilt = true;
    }

    public void seekMap(SeekMap seekMap) {
        this.seekMap = seekMap;
    }

    public void drmInitData(DrmInitData drmInitData) {
        this.drmInitData = drmInitData;
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

    private void maybeStartLoading() {
        boolean z = false;
        if (!this.loadingFinished && !this.loader.isLoading()) {
            if (this.currentLoadableException == null) {
                this.sampleTimeOffsetUs = 0;
                this.havePendingNextSampleUs = false;
                if (this.prepared) {
                    Assertions.checkState(isPendingReset());
                    if (this.maxTrackDurationUs == -1 || this.pendingResetPositionUs < this.maxTrackDurationUs) {
                        this.loadable = createLoadableFromPositionUs(this.pendingResetPositionUs);
                        this.pendingResetPositionUs = Long.MIN_VALUE;
                    } else {
                        this.loadingFinished = true;
                        this.pendingResetPositionUs = Long.MIN_VALUE;
                        return;
                    }
                }
                this.loadable = createLoadableFromStart();
                this.extractedSampleCountAtStartOfLoad = this.extractedSampleCount;
                this.loader.startLoading(this.loadable, this);
            } else if (!isCurrentLoadableExceptionFatal()) {
                if (this.loadable != null) {
                    z = true;
                }
                Assertions.checkState(z);
                if (SystemClock.elapsedRealtime() - this.currentLoadableExceptionTimestamp >= getRetryDelayMillis((long) this.currentLoadableExceptionCount)) {
                    this.currentLoadableException = null;
                    int i;
                    if (!this.prepared) {
                        for (i = 0; i < this.sampleQueues.size(); i++) {
                            ((InternalTrackOutput) this.sampleQueues.valueAt(i)).clear();
                        }
                        this.loadable = createLoadableFromStart();
                    } else if (!this.seekMap.isSeekable() && this.maxTrackDurationUs == -1) {
                        for (i = 0; i < this.sampleQueues.size(); i++) {
                            ((InternalTrackOutput) this.sampleQueues.valueAt(i)).clear();
                        }
                        this.loadable = createLoadableFromStart();
                        this.pendingNextSampleUs = this.downstreamPositionUs;
                        this.havePendingNextSampleUs = true;
                    }
                    this.extractedSampleCountAtStartOfLoad = this.extractedSampleCount;
                    this.loader.startLoading(this.loadable, this);
                }
            }
        }
    }

    private ExtractingLoadable createLoadableFromStart() {
        return new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this.allocator, this.requestedBufferSize, 0);
    }

    private ExtractingLoadable createLoadableFromPositionUs(long positionUs) {
        return new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this.allocator, this.requestedBufferSize, this.seekMap.getPosition(positionUs));
    }

    private boolean haveFormatsForAllTracks() {
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            if (!((InternalTrackOutput) this.sampleQueues.valueAt(i)).hasFormat()) {
                return false;
            }
        }
        return true;
    }

    private void discardSamplesForDisabledTracks(long timeUs) {
        for (int i = 0; i < this.trackEnabledStates.length; i++) {
            if (!this.trackEnabledStates[i]) {
                ((InternalTrackOutput) this.sampleQueues.valueAt(i)).discardUntil(timeUs);
            }
        }
    }

    private void clearState() {
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            ((InternalTrackOutput) this.sampleQueues.valueAt(i)).clear();
        }
        this.loadable = null;
        this.currentLoadableException = null;
        this.currentLoadableExceptionCount = 0;
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != Long.MIN_VALUE;
    }

    private boolean isCurrentLoadableExceptionFatal() {
        return this.currentLoadableException instanceof UnrecognizedInputFormatException;
    }

    private long getRetryDelayMillis(long errorCount) {
        return Math.min((errorCount - 1) * 1000, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
    }
}
