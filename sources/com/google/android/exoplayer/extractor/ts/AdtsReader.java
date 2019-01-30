package com.google.android.exoplayer.extractor.ts;

import android.util.Pair;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.Collections;

final class AdtsReader extends ElementaryStreamReader {
    private static final int CRC_SIZE = 2;
    private static final int HEADER_SIZE = 5;
    private static final int STATE_FINDING_SYNC = 0;
    private static final int STATE_READING_HEADER = 1;
    private static final int STATE_READING_SAMPLE = 2;
    private final ParsableBitArray adtsScratch = new ParsableBitArray(new byte[7]);
    private int bytesRead;
    private boolean hasCrc;
    private boolean hasOutputFormat;
    private boolean lastByteWasFF;
    private long sampleDurationUs;
    private int sampleSize;
    private int state = 0;
    private long timeUs;

    public AdtsReader(TrackOutput output) {
        super(output);
    }

    public void seek() {
        this.state = 0;
        this.bytesRead = 0;
        this.lastByteWasFF = false;
    }

    public void consume(ParsableByteArray data, long pesTimeUs, boolean startOfPacket) {
        if (startOfPacket) {
            this.timeUs = pesTimeUs;
        }
        while (data.bytesLeft() > 0) {
            switch (this.state) {
                case 0:
                    if (!skipToNextSync(data)) {
                        break;
                    }
                    this.bytesRead = 0;
                    this.state = 1;
                    break;
                case 1:
                    if (!continueRead(data, this.adtsScratch.data, this.hasCrc ? 7 : 5)) {
                        break;
                    }
                    parseHeader();
                    this.bytesRead = 0;
                    this.state = 2;
                    break;
                case 2:
                    int bytesToRead = Math.min(data.bytesLeft(), this.sampleSize - this.bytesRead);
                    this.output.sampleData(data, bytesToRead);
                    this.bytesRead += bytesToRead;
                    if (this.bytesRead != this.sampleSize) {
                        break;
                    }
                    this.output.sampleMetadata(this.timeUs, 1, this.sampleSize, 0, null);
                    this.timeUs += this.sampleDurationUs;
                    this.bytesRead = 0;
                    this.state = 0;
                    break;
                default:
                    break;
            }
        }
    }

    public void packetFinished() {
    }

    private boolean continueRead(ParsableByteArray source, byte[] target, int targetLength) {
        int bytesToRead = Math.min(source.bytesLeft(), targetLength - this.bytesRead);
        source.readBytes(target, this.bytesRead, bytesToRead);
        this.bytesRead += bytesToRead;
        return this.bytesRead == targetLength;
    }

    private boolean skipToNextSync(ParsableByteArray pesBuffer) {
        byte[] adtsData = pesBuffer.data;
        int startOffset = pesBuffer.getPosition();
        int endOffset = pesBuffer.limit();
        int i = startOffset;
        while (i < endOffset) {
            boolean found;
            boolean byteIsFF = (adtsData[i] & 255) == 255;
            if (this.lastByteWasFF && !byteIsFF && (adtsData[i] & 240) == 240) {
                found = true;
            } else {
                found = false;
            }
            this.lastByteWasFF = byteIsFF;
            if (found) {
                boolean z;
                if ((adtsData[i] & 1) == 0) {
                    z = true;
                } else {
                    z = false;
                }
                this.hasCrc = z;
                pesBuffer.setPosition(i + 1);
                this.lastByteWasFF = false;
                return true;
            }
            i++;
        }
        pesBuffer.setPosition(endOffset);
        return false;
    }

    private void parseHeader() {
        this.adtsScratch.setPosition(0);
        if (this.hasOutputFormat) {
            this.adtsScratch.skipBits(10);
        } else {
            int audioObjectType = this.adtsScratch.readBits(2) + 1;
            int sampleRateIndex = this.adtsScratch.readBits(4);
            this.adtsScratch.skipBits(1);
            byte[] audioSpecificConfig = CodecSpecificDataUtil.buildAacAudioSpecificConfig(audioObjectType, sampleRateIndex, this.adtsScratch.readBits(3));
            Pair<Integer, Integer> audioParams = CodecSpecificDataUtil.parseAacAudioSpecificConfig(audioSpecificConfig);
            MediaFormat mediaFormat = MediaFormat.createAudioFormat(null, MimeTypes.AUDIO_AAC, -1, -1, -1, ((Integer) audioParams.second).intValue(), ((Integer) audioParams.first).intValue(), Collections.singletonList(audioSpecificConfig), null);
            this.sampleDurationUs = 1024000000 / ((long) mediaFormat.sampleRate);
            this.output.format(mediaFormat);
            this.hasOutputFormat = true;
        }
        this.adtsScratch.skipBits(4);
        this.sampleSize = (this.adtsScratch.readBits(13) - 2) - 5;
        if (this.hasCrc) {
            this.sampleSize -= 2;
        }
    }
}
