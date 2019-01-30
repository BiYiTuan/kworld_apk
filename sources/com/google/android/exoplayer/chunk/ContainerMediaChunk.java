package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.chunk.ChunkExtractorWrapper.SingleTrackOutput;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.DefaultExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

public class ContainerMediaChunk extends BaseMediaChunk implements SingleTrackOutput {
    private final int adaptiveMaxHeight;
    private final int adaptiveMaxWidth;
    private volatile int bytesLoaded;
    private DrmInitData drmInitData;
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;
    private MediaFormat mediaFormat;
    private final long sampleOffsetUs;

    public ContainerMediaChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, long startTimeUs, long endTimeUs, int chunkIndex, long sampleOffsetUs, ChunkExtractorWrapper extractorWrapper, MediaFormat mediaFormat, int adaptiveMaxWidth, int adaptiveMaxHeight, DrmInitData drmInitData, boolean isMediaFormatFinal, int parentId) {
        super(dataSource, dataSpec, trigger, format, startTimeUs, endTimeUs, chunkIndex, isMediaFormatFinal, parentId);
        this.extractorWrapper = extractorWrapper;
        this.sampleOffsetUs = sampleOffsetUs;
        this.adaptiveMaxWidth = adaptiveMaxWidth;
        this.adaptiveMaxHeight = adaptiveMaxHeight;
        this.mediaFormat = getAdjustedMediaFormat(mediaFormat, sampleOffsetUs, adaptiveMaxWidth, adaptiveMaxHeight);
        this.drmInitData = drmInitData;
    }

    public final long bytesLoaded() {
        return (long) this.bytesLoaded;
    }

    public final MediaFormat getMediaFormat() {
        return this.mediaFormat;
    }

    public final DrmInitData getDrmInitData() {
        return this.drmInitData;
    }

    public final void seekMap(SeekMap seekMap) {
    }

    public final void drmInitData(DrmInitData drmInitData) {
        this.drmInitData = drmInitData;
    }

    public final void format(MediaFormat mediaFormat) {
        this.mediaFormat = getAdjustedMediaFormat(mediaFormat, this.sampleOffsetUs, this.adaptiveMaxWidth, this.adaptiveMaxHeight);
    }

    public final int sampleData(ExtractorInput input, int length, boolean allowEndOfInput) throws IOException, InterruptedException {
        return getOutput().sampleData(input, length, allowEndOfInput);
    }

    public final void sampleData(ParsableByteArray data, int length) {
        getOutput().sampleData(data, length);
    }

    public final void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
        getOutput().sampleMetadata(this.sampleOffsetUs + timeUs, flags, size, offset, encryptionKey);
    }

    public final void cancelLoad() {
        this.loadCanceled = true;
    }

    public final boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    public final void load() throws IOException, InterruptedException {
        DataSpec loadDataSpec = Util.getRemainderDataSpec(this.dataSpec, this.bytesLoaded);
        ExtractorInput input;
        try {
            input = new DefaultExtractorInput(this.dataSource, loadDataSpec.absoluteStreamPosition, this.dataSource.open(loadDataSpec));
            if (this.bytesLoaded == 0) {
                this.extractorWrapper.init(this);
            }
            int result = 0;
            while (result == 0) {
                if (!this.loadCanceled) {
                    result = this.extractorWrapper.read(input);
                }
            }
            this.bytesLoaded = (int) (input.getPosition() - this.dataSpec.absoluteStreamPosition);
            this.dataSource.close();
        } catch (Throwable th) {
            this.dataSource.close();
        }
    }

    private static MediaFormat getAdjustedMediaFormat(MediaFormat format, long sampleOffsetUs, int adaptiveMaxWidth, int adaptiveMaxHeight) {
        if (format == null) {
            return null;
        }
        if (!(sampleOffsetUs == 0 || format.subsampleOffsetUs == MediaFormat.OFFSET_SAMPLE_RELATIVE)) {
            format = format.copyWithSubsampleOffsetUs(format.subsampleOffsetUs + sampleOffsetUs);
        }
        if (adaptiveMaxWidth == -1 && adaptiveMaxHeight == -1) {
            return format;
        }
        return format.copyWithMaxVideoDimensions(adaptiveMaxWidth, adaptiveMaxHeight);
    }
}
