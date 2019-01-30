package com.google.android.exoplayer.extractor.ts;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

public final class TsExtractor implements Extractor {
    private static final long AC3_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("AC-3"));
    private static final long E_AC3_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("EAC3"));
    private static final long HEVC_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("HEVC"));
    private static final String TAG = "TsExtractor";
    private static final int TS_PACKET_SIZE = 188;
    private static final int TS_PAT_PID = 0;
    private static final int TS_STREAM_TYPE_AAC = 15;
    private static final int TS_STREAM_TYPE_AC3 = 129;
    private static final int TS_STREAM_TYPE_EIA608 = 256;
    private static final int TS_STREAM_TYPE_E_AC3 = 135;
    private static final int TS_STREAM_TYPE_H262 = 2;
    private static final int TS_STREAM_TYPE_H264 = 27;
    private static final int TS_STREAM_TYPE_H265 = 36;
    private static final int TS_STREAM_TYPE_ID3 = 21;
    private static final int TS_STREAM_TYPE_MPA = 3;
    private static final int TS_STREAM_TYPE_MPA_LSF = 4;
    private static final int TS_SYNC_BYTE = 71;
    Id3Reader id3Reader;
    private final boolean idrKeyframesOnly;
    private ExtractorOutput output;
    private final PtsTimestampAdjuster ptsTimestampAdjuster;
    final SparseBooleanArray streamTypes;
    private final ParsableByteArray tsPacketBuffer;
    final SparseArray<TsPayloadReader> tsPayloadReaders;
    private final ParsableBitArray tsScratch;

    private static abstract class TsPayloadReader {
        public abstract void consume(ParsableByteArray parsableByteArray, boolean z, ExtractorOutput extractorOutput);

        public abstract void seek();

        private TsPayloadReader() {
        }
    }

    private class PatReader extends TsPayloadReader {
        private final ParsableBitArray patScratch = new ParsableBitArray(new byte[4]);

        public PatReader() {
            super();
        }

        public void seek() {
        }

        public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator, ExtractorOutput output) {
            if (payloadUnitStartIndicator) {
                data.skipBytes(data.readUnsignedByte());
            }
            data.readBytes(this.patScratch, 3);
            this.patScratch.skipBits(12);
            int sectionLength = this.patScratch.readBits(12);
            data.skipBytes(5);
            int programCount = (sectionLength - 9) / 4;
            for (int i = 0; i < programCount; i++) {
                data.readBytes(this.patScratch, 4);
                this.patScratch.skipBits(19);
                TsExtractor.this.tsPayloadReaders.put(this.patScratch.readBits(13), new PmtReader());
            }
        }
    }

    private class PesReader extends TsPayloadReader {
        private static final int HEADER_SIZE = 9;
        private static final int MAX_HEADER_EXTENSION_SIZE = 10;
        private static final int PES_SCRATCH_SIZE = 10;
        private static final int STATE_FINDING_HEADER = 0;
        private static final int STATE_READING_BODY = 3;
        private static final int STATE_READING_HEADER = 1;
        private static final int STATE_READING_HEADER_EXTENSION = 2;
        private boolean bodyStarted;
        private int bytesRead;
        private boolean dtsFlag;
        private int extendedHeaderLength;
        private int payloadSize;
        private final ElementaryStreamReader pesPayloadReader;
        private final ParsableBitArray pesScratch = new ParsableBitArray(new byte[10]);
        private boolean ptsFlag;
        private boolean seenFirstDts;
        private int state = 0;
        private long timeUs;

        public PesReader(ElementaryStreamReader pesPayloadReader) {
            super();
            this.pesPayloadReader = pesPayloadReader;
        }

        public void seek() {
            this.state = 0;
            this.bytesRead = 0;
            this.bodyStarted = false;
            this.seenFirstDts = false;
            this.pesPayloadReader.seek();
        }

        public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator, ExtractorOutput output) {
            if (payloadUnitStartIndicator) {
                switch (this.state) {
                    case 2:
                        Log.w(TsExtractor.TAG, "Unexpected start indicator reading extended header");
                        break;
                    case 3:
                        if (this.payloadSize != -1) {
                            Log.w(TsExtractor.TAG, "Unexpected start indicator: expected " + this.payloadSize + " more bytes");
                        }
                        if (this.bodyStarted) {
                            this.pesPayloadReader.packetFinished();
                            break;
                        }
                        break;
                }
                setState(1);
            }
            while (data.bytesLeft() > 0) {
                switch (this.state) {
                    case 0:
                        data.skipBytes(data.bytesLeft());
                        break;
                    case 1:
                        if (!continueRead(data, this.pesScratch.data, 9)) {
                            break;
                        }
                        setState(parseHeader() ? 2 : 0);
                        break;
                    case 2:
                        if (continueRead(data, this.pesScratch.data, Math.min(10, this.extendedHeaderLength)) && continueRead(data, null, this.extendedHeaderLength)) {
                            parseHeaderExtension();
                            this.bodyStarted = false;
                            setState(3);
                            break;
                        }
                    case 3:
                        boolean z;
                        int readLength = data.bytesLeft();
                        int padding = this.payloadSize == -1 ? 0 : readLength - this.payloadSize;
                        if (padding > 0) {
                            readLength -= padding;
                            data.setLimit(data.getPosition() + readLength);
                        }
                        ElementaryStreamReader elementaryStreamReader = this.pesPayloadReader;
                        long j = this.timeUs;
                        if (this.bodyStarted) {
                            z = false;
                        } else {
                            z = true;
                        }
                        elementaryStreamReader.consume(data, j, z);
                        this.bodyStarted = true;
                        if (this.payloadSize == -1) {
                            break;
                        }
                        this.payloadSize -= readLength;
                        if (this.payloadSize != 0) {
                            break;
                        }
                        this.pesPayloadReader.packetFinished();
                        setState(1);
                        break;
                    default:
                        break;
                }
            }
        }

        private void setState(int state) {
            this.state = state;
            this.bytesRead = 0;
        }

        private boolean continueRead(ParsableByteArray source, byte[] target, int targetLength) {
            int bytesToRead = Math.min(source.bytesLeft(), targetLength - this.bytesRead);
            if (bytesToRead <= 0) {
                return true;
            }
            if (target == null) {
                source.skipBytes(bytesToRead);
            } else {
                source.readBytes(target, this.bytesRead, bytesToRead);
            }
            this.bytesRead += bytesToRead;
            if (this.bytesRead != targetLength) {
                return false;
            }
            return true;
        }

        private boolean parseHeader() {
            this.pesScratch.setPosition(0);
            int startCodePrefix = this.pesScratch.readBits(24);
            if (startCodePrefix != 1) {
                Log.w(TsExtractor.TAG, "Unexpected start code prefix: " + startCodePrefix);
                this.payloadSize = -1;
                return false;
            }
            this.pesScratch.skipBits(8);
            int packetLength = this.pesScratch.readBits(16);
            this.pesScratch.skipBits(8);
            this.ptsFlag = this.pesScratch.readBit();
            this.dtsFlag = this.pesScratch.readBit();
            this.pesScratch.skipBits(6);
            this.extendedHeaderLength = this.pesScratch.readBits(8);
            if (packetLength == 0) {
                this.payloadSize = -1;
            } else {
                this.payloadSize = ((packetLength + 6) - 9) - this.extendedHeaderLength;
            }
            return true;
        }

        private void parseHeaderExtension() {
            this.pesScratch.setPosition(0);
            this.timeUs = 0;
            if (this.ptsFlag) {
                this.pesScratch.skipBits(4);
                long pts = ((long) this.pesScratch.readBits(3)) << 30;
                this.pesScratch.skipBits(1);
                pts |= (long) (this.pesScratch.readBits(15) << 15);
                this.pesScratch.skipBits(1);
                pts |= (long) this.pesScratch.readBits(15);
                this.pesScratch.skipBits(1);
                if (!this.seenFirstDts && this.dtsFlag) {
                    this.pesScratch.skipBits(4);
                    long dts = ((long) this.pesScratch.readBits(3)) << 30;
                    this.pesScratch.skipBits(1);
                    dts |= (long) (this.pesScratch.readBits(15) << 15);
                    this.pesScratch.skipBits(1);
                    dts |= (long) this.pesScratch.readBits(15);
                    this.pesScratch.skipBits(1);
                    TsExtractor.this.ptsTimestampAdjuster.adjustTimestamp(dts);
                    this.seenFirstDts = true;
                }
                this.timeUs = TsExtractor.this.ptsTimestampAdjuster.adjustTimestamp(pts);
            }
        }
    }

    private class PmtReader extends TsPayloadReader {
        private final ParsableBitArray pmtScratch = new ParsableBitArray(new byte[5]);
        private int sectionBytesRead;
        private final ParsableByteArray sectionData = new ParsableByteArray();
        private int sectionLength;

        public PmtReader() {
            super();
        }

        public void seek() {
        }

        public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator, ExtractorOutput output) {
            if (payloadUnitStartIndicator) {
                data.skipBytes(data.readUnsignedByte());
                data.readBytes(this.pmtScratch, 3);
                this.pmtScratch.skipBits(12);
                this.sectionLength = this.pmtScratch.readBits(12);
                if (this.sectionData.capacity() < this.sectionLength) {
                    this.sectionData.reset(new byte[this.sectionLength], this.sectionLength);
                } else {
                    this.sectionData.reset();
                    this.sectionData.setLimit(this.sectionLength);
                }
            }
            int bytesToRead = Math.min(data.bytesLeft(), this.sectionLength - this.sectionBytesRead);
            data.readBytes(this.sectionData.data, this.sectionBytesRead, bytesToRead);
            this.sectionBytesRead += bytesToRead;
            if (this.sectionBytesRead >= this.sectionLength) {
                this.sectionData.skipBytes(7);
                this.sectionData.readBytes(this.pmtScratch, 2);
                this.pmtScratch.skipBits(4);
                int programInfoLength = this.pmtScratch.readBits(12);
                this.sectionData.skipBytes(programInfoLength);
                if (TsExtractor.this.id3Reader == null) {
                    TsExtractor.this.id3Reader = new Id3Reader(output.track(21));
                }
                int remainingEntriesLength = ((this.sectionLength - 9) - programInfoLength) - 4;
                while (remainingEntriesLength > 0) {
                    this.sectionData.readBytes(this.pmtScratch, 5);
                    int streamType = this.pmtScratch.readBits(8);
                    this.pmtScratch.skipBits(3);
                    int elementaryPid = this.pmtScratch.readBits(13);
                    this.pmtScratch.skipBits(4);
                    int esInfoLength = this.pmtScratch.readBits(12);
                    if (streamType == 6) {
                        streamType = readPrivateDataStreamType(this.sectionData, esInfoLength);
                    } else {
                        this.sectionData.skipBytes(esInfoLength);
                    }
                    remainingEntriesLength -= esInfoLength + 5;
                    if (!TsExtractor.this.streamTypes.get(streamType)) {
                        ElementaryStreamReader pesPayloadReader = null;
                        switch (streamType) {
                            case 2:
                                pesPayloadReader = new H262Reader(output.track(2));
                                break;
                            case 3:
                                pesPayloadReader = new MpegAudioReader(output.track(3));
                                break;
                            case 4:
                                pesPayloadReader = new MpegAudioReader(output.track(4));
                                break;
                            case 15:
                                pesPayloadReader = new AdtsReader(output.track(15));
                                break;
                            case 21:
                                pesPayloadReader = TsExtractor.this.id3Reader;
                                break;
                            case 27:
                                pesPayloadReader = new H264Reader(output.track(27), new SeiReader(output.track(256)), TsExtractor.this.idrKeyframesOnly);
                                break;
                            case 36:
                                pesPayloadReader = new H265Reader(output.track(36), new SeiReader(output.track(256)));
                                break;
                            case TsExtractor.TS_STREAM_TYPE_AC3 /*129*/:
                                pesPayloadReader = new Ac3Reader(output.track(TsExtractor.TS_STREAM_TYPE_AC3), false);
                                break;
                            case TsExtractor.TS_STREAM_TYPE_E_AC3 /*135*/:
                                pesPayloadReader = new Ac3Reader(output.track(TsExtractor.TS_STREAM_TYPE_E_AC3), true);
                                break;
                        }
                        if (pesPayloadReader != null) {
                            TsExtractor.this.streamTypes.put(streamType, true);
                            TsExtractor.this.tsPayloadReaders.put(elementaryPid, new PesReader(pesPayloadReader));
                        }
                    }
                }
                output.endTracks();
            }
        }

        private int readPrivateDataStreamType(ParsableByteArray data, int length) {
            int streamType = -1;
            int descriptorsEndPosition = data.getPosition() + length;
            while (data.getPosition() < descriptorsEndPosition) {
                int descriptorTag = data.readUnsignedByte();
                int descriptorLength = data.readUnsignedByte();
                if (descriptorTag == 5) {
                    long formatIdentifier = data.readUnsignedInt();
                    if (formatIdentifier == TsExtractor.AC3_FORMAT_IDENTIFIER) {
                        streamType = TsExtractor.TS_STREAM_TYPE_AC3;
                    } else if (formatIdentifier == TsExtractor.E_AC3_FORMAT_IDENTIFIER) {
                        streamType = TsExtractor.TS_STREAM_TYPE_E_AC3;
                    } else if (formatIdentifier == TsExtractor.HEVC_FORMAT_IDENTIFIER) {
                        streamType = 36;
                    }
                    data.setPosition(descriptorsEndPosition);
                    return streamType;
                }
                data.skipBytes(descriptorLength);
            }
            data.setPosition(descriptorsEndPosition);
            return streamType;
        }
    }

    public TsExtractor() {
        this(new PtsTimestampAdjuster(0));
    }

    public TsExtractor(PtsTimestampAdjuster ptsTimestampAdjuster) {
        this(ptsTimestampAdjuster, true);
    }

    public TsExtractor(PtsTimestampAdjuster ptsTimestampAdjuster, boolean idrKeyframesOnly) {
        this.ptsTimestampAdjuster = ptsTimestampAdjuster;
        this.idrKeyframesOnly = idrKeyframesOnly;
        this.tsPacketBuffer = new ParsableByteArray((int) TS_PACKET_SIZE);
        this.tsScratch = new ParsableBitArray(new byte[3]);
        this.tsPayloadReaders = new SparseArray();
        this.tsPayloadReaders.put(0, new PatReader());
        this.streamTypes = new SparseBooleanArray();
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        byte[] scratch = new byte[1];
        for (int i = 0; i < 5; i++) {
            input.peekFully(scratch, 0, 1);
            if ((scratch[0] & 255) != 71) {
                return false;
            }
            input.advancePeekPosition(187);
        }
        return true;
    }

    public void init(ExtractorOutput output) {
        this.output = output;
        output.seekMap(SeekMap.UNSEEKABLE);
    }

    public void seek() {
        this.ptsTimestampAdjuster.reset();
        for (int i = 0; i < this.tsPayloadReaders.size(); i++) {
            ((TsPayloadReader) this.tsPayloadReaders.valueAt(i)).seek();
        }
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        if (!input.readFully(this.tsPacketBuffer.data, 0, TS_PACKET_SIZE, true)) {
            return -1;
        }
        this.tsPacketBuffer.setPosition(0);
        this.tsPacketBuffer.setLimit(TS_PACKET_SIZE);
        if (this.tsPacketBuffer.readUnsignedByte() != 71) {
            return 0;
        }
        this.tsPacketBuffer.readBytes(this.tsScratch, 3);
        this.tsScratch.skipBits(1);
        boolean payloadUnitStartIndicator = this.tsScratch.readBit();
        this.tsScratch.skipBits(1);
        int pid = this.tsScratch.readBits(13);
        this.tsScratch.skipBits(2);
        boolean adaptationFieldExists = this.tsScratch.readBit();
        boolean payloadExists = this.tsScratch.readBit();
        if (adaptationFieldExists) {
            this.tsPacketBuffer.skipBytes(this.tsPacketBuffer.readUnsignedByte());
        }
        if (!payloadExists) {
            return 0;
        }
        TsPayloadReader payloadReader = (TsPayloadReader) this.tsPayloadReaders.get(pid);
        if (payloadReader == null) {
            return 0;
        }
        payloadReader.consume(this.tsPacketBuffer, payloadUnitStartIndicator, this.output);
        return 0;
    }
}
