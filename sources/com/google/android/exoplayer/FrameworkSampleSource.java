package com.google.android.exoplayer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import com.google.android.exoplayer.SampleSource.SampleSourceReader;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.drm.DrmInitData.Mapped;
import com.google.android.exoplayer.drm.DrmInitData.SchemeInitData;
import com.google.android.exoplayer.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.Util;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@TargetApi(16)
@Deprecated
public final class FrameworkSampleSource implements SampleSource, SampleSourceReader {
    private static final int ALLOWED_FLAGS_MASK = 3;
    private static final int TRACK_STATE_DISABLED = 0;
    private static final int TRACK_STATE_ENABLED = 1;
    private static final int TRACK_STATE_FORMAT_SENT = 2;
    private final Context context;
    private MediaExtractor extractor;
    private final FileDescriptor fileDescriptor;
    private final long fileDescriptorLength;
    private final long fileDescriptorOffset;
    private final Map<String, String> headers;
    private boolean[] pendingDiscontinuities;
    private IOException preparationError;
    private boolean prepared;
    private int remainingReleaseCount;
    private long seekPositionUs;
    private MediaFormat[] trackFormats;
    private int[] trackStates;
    private final Uri uri;

    public FrameworkSampleSource(Context context, Uri uri, Map<String, String> headers) {
        Assertions.checkState(Util.SDK_INT >= 16);
        this.context = (Context) Assertions.checkNotNull(context);
        this.uri = (Uri) Assertions.checkNotNull(uri);
        this.headers = headers;
        this.fileDescriptor = null;
        this.fileDescriptorOffset = 0;
        this.fileDescriptorLength = 0;
    }

    public FrameworkSampleSource(FileDescriptor fileDescriptor, long fileDescriptorOffset, long fileDescriptorLength) {
        Assertions.checkState(Util.SDK_INT >= 16);
        this.fileDescriptor = (FileDescriptor) Assertions.checkNotNull(fileDescriptor);
        this.fileDescriptorOffset = fileDescriptorOffset;
        this.fileDescriptorLength = fileDescriptorLength;
        this.context = null;
        this.uri = null;
        this.headers = null;
    }

    public SampleSourceReader register() {
        this.remainingReleaseCount++;
        return this;
    }

    public boolean prepare(long positionUs) {
        if (!this.prepared) {
            if (this.preparationError != null) {
                return false;
            }
            this.extractor = new MediaExtractor();
            try {
                if (this.context != null) {
                    this.extractor.setDataSource(this.context, this.uri, this.headers);
                } else {
                    this.extractor.setDataSource(this.fileDescriptor, this.fileDescriptorOffset, this.fileDescriptorLength);
                }
                this.trackStates = new int[this.extractor.getTrackCount()];
                this.pendingDiscontinuities = new boolean[this.trackStates.length];
                this.trackFormats = new MediaFormat[this.trackStates.length];
                for (int i = 0; i < this.trackStates.length; i++) {
                    this.trackFormats[i] = createMediaFormat(this.extractor.getTrackFormat(i));
                }
                this.prepared = true;
            } catch (IOException e) {
                this.preparationError = e;
                return false;
            }
        }
        return true;
    }

    public int getTrackCount() {
        Assertions.checkState(this.prepared);
        return this.trackStates.length;
    }

    public MediaFormat getFormat(int track) {
        Assertions.checkState(this.prepared);
        return this.trackFormats[track];
    }

    public void enable(int track, long positionUs) {
        boolean z;
        boolean z2 = true;
        Assertions.checkState(this.prepared);
        if (this.trackStates[track] == 0) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        this.trackStates[track] = 1;
        this.extractor.selectTrack(track);
        if (positionUs == 0) {
            z2 = false;
        }
        seekToUsInternal(positionUs, z2);
    }

    public boolean continueBuffering(int track, long positionUs) {
        return true;
    }

    public int readData(int track, long positionUs, MediaFormatHolder formatHolder, SampleHolder sampleHolder, boolean onlyReadDiscontinuity) {
        Assertions.checkState(this.prepared);
        Assertions.checkState(this.trackStates[track] != 0);
        if (this.pendingDiscontinuities[track]) {
            this.pendingDiscontinuities[track] = false;
            return -5;
        } else if (onlyReadDiscontinuity) {
            return -2;
        } else {
            if (this.trackStates[track] != 2) {
                formatHolder.format = this.trackFormats[track];
                formatHolder.drmInitData = Util.SDK_INT >= 18 ? getDrmInitDataV18() : null;
                this.trackStates[track] = 2;
                return -4;
            }
            int extractorTrackIndex = this.extractor.getSampleTrackIndex();
            if (extractorTrackIndex == track) {
                if (sampleHolder.data != null) {
                    int offset = sampleHolder.data.position();
                    sampleHolder.size = this.extractor.readSampleData(sampleHolder.data, offset);
                    sampleHolder.data.position(sampleHolder.size + offset);
                } else {
                    sampleHolder.size = 0;
                }
                sampleHolder.timeUs = this.extractor.getSampleTime();
                sampleHolder.flags = this.extractor.getSampleFlags() & 3;
                if (sampleHolder.isEncrypted()) {
                    sampleHolder.cryptoInfo.setFromExtractorV16(this.extractor);
                }
                this.seekPositionUs = -1;
                this.extractor.advance();
                return -3;
            }
            return extractorTrackIndex < 0 ? -1 : -2;
        }
    }

    public void disable(int track) {
        boolean z;
        Assertions.checkState(this.prepared);
        if (this.trackStates[track] != 0) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        this.extractor.unselectTrack(track);
        this.pendingDiscontinuities[track] = false;
        this.trackStates[track] = 0;
    }

    public void maybeThrowError() throws IOException {
        if (this.preparationError != null) {
            throw this.preparationError;
        }
    }

    public void seekToUs(long positionUs) {
        Assertions.checkState(this.prepared);
        seekToUsInternal(positionUs, false);
    }

    public long getBufferedPositionUs() {
        Assertions.checkState(this.prepared);
        long bufferedDurationUs = this.extractor.getCachedDuration();
        if (bufferedDurationUs == -1) {
            return -1;
        }
        long sampleTime = this.extractor.getSampleTime();
        return sampleTime == -1 ? -3 : sampleTime + bufferedDurationUs;
    }

    public void release() {
        Assertions.checkState(this.remainingReleaseCount > 0);
        int i = this.remainingReleaseCount - 1;
        this.remainingReleaseCount = i;
        if (i == 0 && this.extractor != null) {
            this.extractor.release();
            this.extractor = null;
        }
    }

    @TargetApi(18)
    private DrmInitData getDrmInitDataV18() {
        Map<UUID, byte[]> psshInfo = this.extractor.getPsshInfo();
        if (psshInfo == null || psshInfo.isEmpty()) {
            return null;
        }
        DrmInitData drmInitData = new Mapped();
        for (UUID uuid : psshInfo.keySet()) {
            drmInitData.put(uuid, new SchemeInitData(MimeTypes.VIDEO_MP4, PsshAtomUtil.buildPsshAtom(uuid, (byte[]) psshInfo.get(uuid))));
        }
        return drmInitData;
    }

    private void seekToUsInternal(long positionUs, boolean force) {
        if (force || this.seekPositionUs != positionUs) {
            this.seekPositionUs = positionUs;
            this.extractor.seekTo(positionUs, 0);
            for (int i = 0; i < this.trackStates.length; i++) {
                if (this.trackStates[i] != 0) {
                    this.pendingDiscontinuities[i] = true;
                }
            }
        }
    }

    @SuppressLint({"InlinedApi"})
    private static MediaFormat createMediaFormat(MediaFormat format) {
        long durationUs;
        String mimeType = format.getString(io.vov.vitamio.MediaFormat.KEY_MIME);
        String language = getOptionalStringV16(format, "language");
        int maxInputSize = getOptionalIntegerV16(format, io.vov.vitamio.MediaFormat.KEY_MAX_INPUT_SIZE);
        int width = getOptionalIntegerV16(format, "width");
        int height = getOptionalIntegerV16(format, "height");
        int rotationDegrees = getOptionalIntegerV16(format, "rotation-degrees");
        int channelCount = getOptionalIntegerV16(format, io.vov.vitamio.MediaFormat.KEY_CHANNEL_COUNT);
        int sampleRate = getOptionalIntegerV16(format, io.vov.vitamio.MediaFormat.KEY_SAMPLE_RATE);
        ArrayList<byte[]> initializationData = new ArrayList();
        int i = 0;
        while (true) {
            if (!format.containsKey("csd-" + i)) {
                break;
            }
            ByteBuffer buffer = format.getByteBuffer("csd-" + i);
            Object data = new byte[buffer.limit()];
            buffer.get(data);
            initializationData.add(data);
            buffer.flip();
            i++;
        }
        if (format.containsKey(io.vov.vitamio.MediaFormat.KEY_DURATION)) {
            durationUs = format.getLong(io.vov.vitamio.MediaFormat.KEY_DURATION);
        } else {
            durationUs = -1;
        }
        MediaFormat mediaFormat = new MediaFormat(null, mimeType, -1, maxInputSize, durationUs, width, height, rotationDegrees, -1.0f, channelCount, sampleRate, language, MediaFormat.OFFSET_SAMPLE_RELATIVE, initializationData, false, -1, -1);
        mediaFormat.setFrameworkFormatV16(format);
        return mediaFormat;
    }

    @TargetApi(16)
    private static final String getOptionalStringV16(MediaFormat format, String key) {
        return format.containsKey(key) ? format.getString(key) : null;
    }

    @TargetApi(16)
    private static final int getOptionalIntegerV16(MediaFormat format, String key) {
        return format.containsKey(key) ? format.getInteger(key) : -1;
    }
}
