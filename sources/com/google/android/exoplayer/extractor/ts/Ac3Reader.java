package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.C0665C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.Ac3Util;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;

final class Ac3Reader extends ElementaryStreamReader {
    private static final int HEADER_SIZE = 8;
    private static final int STATE_FINDING_SYNC = 0;
    private static final int STATE_READING_HEADER = 1;
    private static final int STATE_READING_SAMPLE = 2;
    private int bytesRead;
    private final ParsableBitArray headerScratchBits = new ParsableBitArray(new byte[8]);
    private final ParsableByteArray headerScratchBytes = new ParsableByteArray(this.headerScratchBits.data);
    private final boolean isEac3;
    private boolean lastByteWas0B;
    private MediaFormat mediaFormat;
    private long sampleDurationUs;
    private int sampleSize;
    private int state = 0;
    private long timeUs;

    public Ac3Reader(TrackOutput output, boolean isEac3) {
        super(output);
        this.isEac3 = isEac3;
    }

    public void seek() {
        this.state = 0;
        this.bytesRead = 0;
        this.lastByteWas0B = false;
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
                    this.state = 1;
                    this.headerScratchBytes.data[0] = (byte) 11;
                    this.headerScratchBytes.data[1] = (byte) 119;
                    this.bytesRead = 2;
                    break;
                case 1:
                    if (!continueRead(data, this.headerScratchBytes.data, 8)) {
                        break;
                    }
                    parseHeader();
                    this.headerScratchBytes.setPosition(0);
                    this.output.sampleData(this.headerScratchBytes, 8);
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
        while (pesBuffer.bytesLeft() > 0) {
            if (this.lastByteWas0B) {
                int secondByte = pesBuffer.readUnsignedByte();
                if (secondByte == 119) {
                    this.lastByteWas0B = false;
                    return true;
                }
                this.lastByteWas0B = secondByte == 11;
            } else {
                this.lastByteWas0B = pesBuffer.readUnsignedByte() == 11;
            }
        }
        return false;
    }

    private void parseHeader() {
        if (this.mediaFormat == null) {
            this.mediaFormat = this.isEac3 ? Ac3Util.parseEac3SyncframeFormat(this.headerScratchBits, null, -1, null) : Ac3Util.parseAc3SyncframeFormat(this.headerScratchBits, null, -1, null);
            this.output.format(this.mediaFormat);
        }
        this.sampleSize = this.isEac3 ? Ac3Util.parseEAc3SyncframeSize(this.headerScratchBits.data) : Ac3Util.parseAc3SyncframeSize(this.headerScratchBits.data);
        this.sampleDurationUs = (long) ((int) ((C0665C.MICROS_PER_SECOND * ((long) (this.isEac3 ? Ac3Util.parseEAc3SyncframeAudioSampleCount(this.headerScratchBits.data) : Ac3Util.getAc3SyncframeAudioSampleCount()))) / ((long) this.mediaFormat.sampleRate)));
    }
}
