package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import java.util.List;

public final class SingleSampleChunkSource implements ChunkSource {
    private final DataSource dataSource;
    private final DataSpec dataSpec;
    private final long durationUs;
    private final Format format;
    private final MediaFormat mediaFormat;

    public SingleSampleChunkSource(DataSource dataSource, DataSpec dataSpec, Format format, long durationUs, MediaFormat mediaFormat) {
        this.dataSource = dataSource;
        this.dataSpec = dataSpec;
        this.format = format;
        this.durationUs = durationUs;
        this.mediaFormat = mediaFormat;
    }

    public boolean prepare() {
        return true;
    }

    public int getTrackCount() {
        return 1;
    }

    public MediaFormat getFormat(int track) {
        return this.mediaFormat;
    }

    public void enable(int track) {
    }

    public void continueBuffering(long playbackPositionUs) {
    }

    public void getChunkOperation(List<? extends MediaChunk> queue, long playbackPositionUs, ChunkOperationHolder out) {
        if (queue.isEmpty()) {
            out.chunk = initChunk();
        } else {
            out.endOfStream = true;
        }
    }

    public void disable(List<? extends MediaChunk> list) {
    }

    public void maybeThrowError() {
    }

    public void onChunkLoadCompleted(Chunk chunk) {
    }

    public void onChunkLoadError(Chunk chunk, Exception e) {
    }

    private SingleSampleMediaChunk initChunk() {
        return new SingleSampleMediaChunk(this.dataSource, this.dataSpec, 0, this.format, 0, this.durationUs, 0, this.mediaFormat, null, -1);
    }
}
