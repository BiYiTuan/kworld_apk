package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.Arrays;
import java.util.Collections;

final class H262Reader extends ElementaryStreamReader {
    private static final int START_EXTENSION = 181;
    private static final int START_GROUP = 184;
    private static final int START_PICTURE = 0;
    private static final int START_SEQUENCE_HEADER = 179;
    private final CsdBuffer csdBuffer = new CsdBuffer(128);
    private boolean foundFirstFrameInGroup;
    private long framePosition;
    private long frameTimeUs;
    private boolean hasOutputFormat;
    private boolean isKeyframe;
    private final boolean[] prefixFlags = new boolean[4];
    private long totalBytesWritten;

    private static final class CsdBuffer {
        public byte[] data;
        private boolean isFilling;
        public int length;
        private boolean seenExtensionStartCode;

        public CsdBuffer(int initialCapacity) {
            this.data = new byte[initialCapacity];
        }

        public void reset() {
            this.isFilling = false;
            this.seenExtensionStartCode = false;
            this.length = 0;
        }

        public boolean onStartCode(int startCodeValue, int bytesAlreadyPassed) {
            if (this.isFilling) {
                if (this.seenExtensionStartCode || startCodeValue != H262Reader.START_EXTENSION) {
                    this.length -= bytesAlreadyPassed;
                    this.isFilling = false;
                    return true;
                }
                this.seenExtensionStartCode = true;
            } else if (startCodeValue == H262Reader.START_SEQUENCE_HEADER) {
                this.isFilling = true;
            }
            return false;
        }

        public void onData(byte[] newData, int offset, int limit) {
            if (this.isFilling) {
                int readLength = limit - offset;
                if (this.data.length < this.length + readLength) {
                    this.data = Arrays.copyOf(this.data, (this.length + readLength) * 2);
                }
                System.arraycopy(newData, offset, this.data, this.length, readLength);
                this.length += readLength;
            }
        }
    }

    public H262Reader(TrackOutput output) {
        super(output);
    }

    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.csdBuffer.reset();
        this.foundFirstFrameInGroup = false;
        this.totalBytesWritten = 0;
    }

    public void consume(ParsableByteArray data, long pesTimeUs, boolean startOfPacket) {
        if (data.bytesLeft() > 0) {
            int offset = data.getPosition();
            int limit = data.limit();
            byte[] dataArray = data.data;
            this.totalBytesWritten += (long) data.bytesLeft();
            this.output.sampleData(data, data.bytesLeft());
            int searchOffset = offset;
            while (true) {
                int startCodeOffset = NalUnitUtil.findNalUnit(dataArray, searchOffset, limit, this.prefixFlags);
                if (startCodeOffset == limit) {
                    break;
                }
                int startCodeValue = data.data[startCodeOffset + 3] & 255;
                if (!this.hasOutputFormat) {
                    int lengthToStartCode = startCodeOffset - offset;
                    if (lengthToStartCode > 0) {
                        this.csdBuffer.onData(dataArray, offset, startCodeOffset);
                    }
                    if (this.csdBuffer.onStartCode(startCodeValue, lengthToStartCode < 0 ? -lengthToStartCode : 0)) {
                        this.output.format(parseMediaFormat(this.csdBuffer));
                        this.hasOutputFormat = true;
                    }
                }
                if (this.hasOutputFormat && (startCodeValue == START_GROUP || startCodeValue == 0)) {
                    int bytesWrittenPastStartCode = limit - startCodeOffset;
                    if (this.foundFirstFrameInGroup) {
                        this.output.sampleMetadata(this.frameTimeUs, this.isKeyframe ? 1 : 0, ((int) (this.totalBytesWritten - this.framePosition)) - bytesWrittenPastStartCode, bytesWrittenPastStartCode, null);
                        this.isKeyframe = false;
                    }
                    if (startCodeValue == START_GROUP) {
                        this.foundFirstFrameInGroup = false;
                        this.isKeyframe = true;
                    } else {
                        this.foundFirstFrameInGroup = true;
                        this.frameTimeUs = pesTimeUs;
                        this.framePosition = this.totalBytesWritten - ((long) bytesWrittenPastStartCode);
                    }
                }
                offset = startCodeOffset;
                searchOffset = offset + 3;
            }
            if (!this.hasOutputFormat) {
                this.csdBuffer.onData(dataArray, offset, limit);
            }
        }
    }

    public void packetFinished() {
    }

    private static MediaFormat parseMediaFormat(CsdBuffer csdBuffer) {
        byte[] csdData = Arrays.copyOf(csdBuffer.data, csdBuffer.length);
        int secondByte = csdData[5] & 255;
        int width = ((csdData[4] & 255) << 4) | (secondByte >> 4);
        int height = ((secondByte & 15) << 8) | (csdData[6] & 255);
        float pixelWidthHeightRatio = 1.0f;
        switch ((csdData[7] & 240) >> 4) {
            case 2:
                pixelWidthHeightRatio = ((float) (height * 4)) / ((float) (width * 3));
                break;
            case 3:
                pixelWidthHeightRatio = ((float) (height * 16)) / ((float) (width * 9));
                break;
            case 4:
                pixelWidthHeightRatio = ((float) (height * 121)) / ((float) (width * 100));
                break;
        }
        return MediaFormat.createVideoFormat(null, MimeTypes.VIDEO_MPEG2, -1, -1, -1, width, height, Collections.singletonList(csdData), -1, pixelWidthHeightRatio);
    }
}
