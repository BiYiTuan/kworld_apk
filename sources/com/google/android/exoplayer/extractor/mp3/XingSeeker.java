package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.C0665C;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;

final class XingSeeker implements Seeker {
    private final long durationUs;
    private final long firstFramePosition;
    private final long inputLength;
    private final long sizeBytes;
    private final long[] tableOfContents;

    public static XingSeeker create(MpegAudioHeader mpegAudioHeader, ParsableByteArray frame, long position, long inputLength) {
        int samplesPerFrame = mpegAudioHeader.samplesPerFrame;
        int sampleRate = mpegAudioHeader.sampleRate;
        long firstFramePosition = position + ((long) mpegAudioHeader.frameSize);
        int flags = frame.readInt();
        if ((flags & 1) == 1) {
            int frameCount = frame.readUnsignedIntToInt();
            if (frameCount != 0) {
                long durationUs = Util.scaleLargeTimestamp((long) frameCount, ((long) samplesPerFrame) * C0665C.MICROS_PER_SECOND, (long) sampleRate);
                if ((flags & 6) != 6) {
                    return new XingSeeker(inputLength, firstFramePosition, durationUs);
                }
                long sizeBytes = (long) frame.readUnsignedIntToInt();
                frame.skipBytes(1);
                long[] tableOfContents = new long[99];
                for (int i = 0; i < 99; i++) {
                    tableOfContents[i] = (long) frame.readUnsignedByte();
                }
                return new XingSeeker(inputLength, firstFramePosition, durationUs, tableOfContents, sizeBytes);
            }
        }
        return null;
    }

    private XingSeeker(long inputLength, long firstFramePosition, long durationUs) {
        this(inputLength, firstFramePosition, durationUs, null, 0);
    }

    private XingSeeker(long inputLength, long firstFramePosition, long durationUs, long[] tableOfContents, long sizeBytes) {
        this.tableOfContents = tableOfContents;
        this.firstFramePosition = firstFramePosition;
        this.sizeBytes = sizeBytes;
        this.durationUs = durationUs;
        this.inputLength = inputLength;
    }

    public boolean isSeekable() {
        return this.tableOfContents != null;
    }

    public long getPosition(long timeUs) {
        if (!isSeekable()) {
            return this.firstFramePosition;
        }
        float fx;
        float percent = (((float) timeUs) * 100.0f) / ((float) this.durationUs);
        if (percent <= 0.0f) {
            fx = 0.0f;
        } else if (percent >= 100.0f) {
            fx = 256.0f;
        } else {
            float fa;
            float fb;
            int a = (int) percent;
            if (a == 0) {
                fa = 0.0f;
            } else {
                fa = (float) this.tableOfContents[a - 1];
            }
            if (a < 99) {
                fb = (float) this.tableOfContents[a];
            } else {
                fb = 256.0f;
            }
            fx = fa + ((fb - fa) * (percent - ((float) a)));
        }
        long position = ((long) ((0.00390625d * ((double) fx)) * ((double) this.sizeBytes))) + this.firstFramePosition;
        if (this.inputLength != -1) {
            return Math.min(position, this.inputLength - 1);
        }
        return position;
    }

    public long getTimeUs(long position) {
        if (!isSeekable()) {
            return 0;
        }
        double offsetByte = (256.0d * ((double) (position - this.firstFramePosition))) / ((double) this.sizeBytes);
        int previousIndex = Util.binarySearchFloor(this.tableOfContents, (long) offsetByte, true, false);
        long previousTime = getTimeUsForTocIndex(previousIndex);
        if (previousIndex == 98) {
            return previousTime;
        }
        long previousByte = previousIndex == -1 ? 0 : this.tableOfContents[previousIndex];
        long nextByte = this.tableOfContents[previousIndex + 1];
        return previousTime + (nextByte == previousByte ? 0 : (long) ((((double) (getTimeUsForTocIndex(previousIndex + 1) - previousTime)) * (offsetByte - ((double) previousByte))) / ((double) (nextByte - previousByte))));
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    private long getTimeUsForTocIndex(int tocIndex) {
        return (this.durationUs * ((long) (tocIndex + 1))) / 100;
    }
}
