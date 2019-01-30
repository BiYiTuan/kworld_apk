package com.google.android.exoplayer.extractor.mp3;

import android.support.v4.media.TransportMediator;
import android.support.v4.media.session.PlaybackStateCompat;
import com.google.android.exoplayer.C0665C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.io.EOFException;
import java.io.IOException;

public final class Mp3Extractor implements Extractor {
    private static final int HEADER_MASK = -128000;
    private static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private static final int INFO_HEADER = Util.getIntegerCodeForString("Info");
    private static final int MAX_SNIFF_BYTES = 4096;
    private static final int MAX_SYNC_BYTES = 131072;
    private static final int VBRI_HEADER = Util.getIntegerCodeForString("VBRI");
    private static final int XING_HEADER = Util.getIntegerCodeForString("Xing");
    private long basisTimeUs;
    private ExtractorOutput extractorOutput;
    private final long forcedFirstSampleTimestampUs;
    private final BufferingInput inputBuffer;
    private int sampleBytesRemaining;
    private int samplesRead;
    private final ParsableByteArray scratch;
    private Seeker seeker;
    private final MpegAudioHeader synchronizedHeader;
    private int synchronizedHeaderData;
    private TrackOutput trackOutput;

    interface Seeker extends SeekMap {
        long getDurationUs();

        long getTimeUs(long j);
    }

    public Mp3Extractor() {
        this(-1);
    }

    public Mp3Extractor(long forcedFirstSampleTimestampUs) {
        this.forcedFirstSampleTimestampUs = forcedFirstSampleTimestampUs;
        this.inputBuffer = new BufferingInput(12288);
        this.scratch = new ParsableByteArray(4);
        this.synchronizedHeader = new MpegAudioHeader();
        this.basisTimeUs = -1;
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        ParsableByteArray scratch = new ParsableByteArray(4);
        int startPosition = 0;
        while (true) {
            input.peekFully(scratch.data, 0, 3);
            scratch.setPosition(0);
            if (scratch.readUnsignedInt24() != ID3_TAG) {
                break;
            }
            input.advancePeekPosition(3);
            input.peekFully(scratch.data, 0, 4);
            int headerLength = ((((scratch.data[0] & TransportMediator.KEYCODE_MEDIA_PAUSE) << 21) | ((scratch.data[1] & TransportMediator.KEYCODE_MEDIA_PAUSE) << 14)) | ((scratch.data[2] & TransportMediator.KEYCODE_MEDIA_PAUSE) << 7)) | (scratch.data[3] & TransportMediator.KEYCODE_MEDIA_PAUSE);
            input.advancePeekPosition(headerLength);
            startPosition += headerLength + 10;
        }
        input.resetPeekPosition();
        input.advancePeekPosition(startPosition);
        int headerPosition = startPosition;
        int validFrameCount = 0;
        int candidateSynchronizedHeaderData = 0;
        while (headerPosition - startPosition < 4096) {
            input.peekFully(scratch.data, 0, 4);
            scratch.setPosition(0);
            int headerData = scratch.readInt();
            if (candidateSynchronizedHeaderData == 0 || (HEADER_MASK & headerData) == (HEADER_MASK & candidateSynchronizedHeaderData)) {
                int frameSize = MpegAudioHeader.getFrameSize(headerData);
                if (frameSize != -1) {
                    if (validFrameCount == 0) {
                        candidateSynchronizedHeaderData = headerData;
                    }
                    validFrameCount++;
                    if (validFrameCount == 4) {
                        return true;
                    }
                    input.advancePeekPosition(frameSize - 4);
                }
            }
            validFrameCount = 0;
            candidateSynchronizedHeaderData = 0;
            input.resetPeekPosition();
            headerPosition++;
            input.advancePeekPosition(headerPosition);
        }
        return false;
    }

    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = extractorOutput.track(0);
        extractorOutput.endTracks();
    }

    public void seek() {
        this.synchronizedHeaderData = 0;
        this.samplesRead = 0;
        this.basisTimeUs = -1;
        this.sampleBytesRemaining = 0;
        this.inputBuffer.reset();
    }

    public int read(ExtractorInput extractorInput, PositionHolder seekPosition) throws IOException, InterruptedException {
        if (this.synchronizedHeaderData == 0 && synchronizeCatchingEndOfInput(extractorInput) == -1) {
            return -1;
        }
        return readSample(extractorInput);
    }

    private int readSample(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.sampleBytesRemaining == 0) {
            if (maybeResynchronize(extractorInput) == -1) {
                return -1;
            }
            if (this.basisTimeUs == -1) {
                this.basisTimeUs = this.seeker.getTimeUs(getPosition(extractorInput, this.inputBuffer));
                if (this.forcedFirstSampleTimestampUs != -1) {
                    this.basisTimeUs += this.forcedFirstSampleTimestampUs - this.seeker.getTimeUs(0);
                }
            }
            this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
        }
        long timeUs = this.basisTimeUs + ((((long) this.samplesRead) * C0665C.MICROS_PER_SECOND) / ((long) this.synchronizedHeader.sampleRate));
        this.sampleBytesRemaining -= this.inputBuffer.drainToOutput(this.trackOutput, this.sampleBytesRemaining);
        if (this.sampleBytesRemaining > 0) {
            this.inputBuffer.mark();
            int bytesAppended = this.trackOutput.sampleData(extractorInput, this.sampleBytesRemaining, true);
            if (bytesAppended == -1) {
                return -1;
            }
            this.sampleBytesRemaining -= bytesAppended;
            if (this.sampleBytesRemaining > 0) {
                return 0;
            }
        }
        this.trackOutput.sampleMetadata(timeUs, 1, this.synchronizedHeader.frameSize, 0, null);
        this.samplesRead += this.synchronizedHeader.samplesPerFrame;
        this.sampleBytesRemaining = 0;
        return 0;
    }

    private long maybeResynchronize(ExtractorInput extractorInput) throws IOException, InterruptedException {
        this.inputBuffer.mark();
        if (!this.inputBuffer.readAllowingEndOfInput(extractorInput, this.scratch.data, 0, 4)) {
            return -1;
        }
        this.inputBuffer.returnToMark();
        this.scratch.setPosition(0);
        int sampleHeaderData = this.scratch.readInt();
        if ((sampleHeaderData & HEADER_MASK) != (this.synchronizedHeaderData & HEADER_MASK) || MpegAudioHeader.getFrameSize(sampleHeaderData) == -1) {
            this.synchronizedHeaderData = 0;
            this.inputBuffer.skip(extractorInput, 1);
            return synchronizeCatchingEndOfInput(extractorInput);
        }
        MpegAudioHeader.populateHeader(sampleHeaderData, this.synchronizedHeader);
        return 0;
    }

    private long synchronizeCatchingEndOfInput(ExtractorInput extractorInput) throws IOException, InterruptedException {
        try {
            return synchronize(extractorInput);
        } catch (EOFException e) {
            return -1;
        }
    }

    private long synchronize(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (extractorInput.getPosition() == 0) {
            this.inputBuffer.reset();
        } else {
            this.inputBuffer.returnToMark();
        }
        long startPosition = getPosition(extractorInput, this.inputBuffer);
        if (startPosition == 0) {
            while (true) {
                this.inputBuffer.read(extractorInput, this.scratch.data, 0, 3);
                this.scratch.setPosition(0);
                if (this.scratch.readUnsignedInt24() != ID3_TAG) {
                    break;
                }
                extractorInput.skipFully(3);
                extractorInput.readFully(this.scratch.data, 0, 4);
                extractorInput.skipFully(((((this.scratch.data[0] & TransportMediator.KEYCODE_MEDIA_PAUSE) << 21) | ((this.scratch.data[1] & TransportMediator.KEYCODE_MEDIA_PAUSE) << 14)) | ((this.scratch.data[2] & TransportMediator.KEYCODE_MEDIA_PAUSE) << 7)) | (this.scratch.data[3] & TransportMediator.KEYCODE_MEDIA_PAUSE));
                this.inputBuffer.reset();
                startPosition = getPosition(extractorInput, this.inputBuffer);
            }
            this.inputBuffer.returnToMark();
        }
        this.inputBuffer.mark();
        long headerPosition = startPosition;
        int validFrameCount = 0;
        int candidateSynchronizedHeaderData = 0;
        while (headerPosition - startPosition < PlaybackStateCompat.ACTION_PREPARE_FROM_URI) {
            if (!this.inputBuffer.readAllowingEndOfInput(extractorInput, this.scratch.data, 0, 4)) {
                return -1;
            }
            this.scratch.setPosition(0);
            int headerData = this.scratch.readInt();
            if (candidateSynchronizedHeaderData == 0 || (HEADER_MASK & headerData) == (HEADER_MASK & candidateSynchronizedHeaderData)) {
                int frameSize = MpegAudioHeader.getFrameSize(headerData);
                if (frameSize != -1) {
                    if (validFrameCount == 0) {
                        MpegAudioHeader.populateHeader(headerData, this.synchronizedHeader);
                        candidateSynchronizedHeaderData = headerData;
                    }
                    validFrameCount++;
                    if (validFrameCount == 4) {
                        this.inputBuffer.returnToMark();
                        this.synchronizedHeaderData = candidateSynchronizedHeaderData;
                        if (this.seeker != null) {
                            return headerPosition;
                        }
                        setupSeeker(extractorInput, headerPosition);
                        this.extractorOutput.seekMap(this.seeker);
                        this.trackOutput.format(MediaFormat.createAudioFormat(null, this.synchronizedHeader.mimeType, -1, 4096, this.seeker.getDurationUs(), this.synchronizedHeader.channels, this.synchronizedHeader.sampleRate, null, null));
                        return headerPosition;
                    }
                    this.inputBuffer.skip(extractorInput, frameSize - 4);
                }
            }
            validFrameCount = 0;
            candidateSynchronizedHeaderData = 0;
            this.inputBuffer.returnToMark();
            this.inputBuffer.skip(extractorInput, 1);
            this.inputBuffer.mark();
            headerPosition++;
        }
        throw new ParserException("Searched too many bytes while resynchronizing.");
    }

    private void setupSeeker(ExtractorInput extractorInput, long headerPosition) throws IOException, InterruptedException {
        if (parseSeekerFrame(extractorInput, headerPosition, extractorInput.getLength())) {
            this.inputBuffer.mark();
            if (this.seeker == null) {
                this.inputBuffer.read(extractorInput, this.scratch.data, 0, 4);
                this.scratch.setPosition(0);
                headerPosition += (long) this.synchronizedHeader.frameSize;
                MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
            } else {
                return;
            }
        }
        this.inputBuffer.returnToMark();
        this.seeker = new ConstantBitrateSeeker(headerPosition, this.synchronizedHeader.bitrate * 1000, extractorInput.getLength());
    }

    private boolean parseSeekerFrame(ExtractorInput extractorInput, long headerPosition, long inputLength) throws IOException, InterruptedException {
        int xingBase;
        this.inputBuffer.mark();
        this.seeker = null;
        ParsableByteArray frame = this.inputBuffer.getParsableByteArray(extractorInput, this.synchronizedHeader.frameSize);
        if ((this.synchronizedHeader.version & 1) == 1) {
            if (this.synchronizedHeader.channels != 1) {
                xingBase = 32;
            } else {
                xingBase = 17;
            }
        } else if (this.synchronizedHeader.channels != 1) {
            xingBase = 17;
        } else {
            xingBase = 9;
        }
        frame.setPosition(xingBase + 4);
        int headerData = frame.readInt();
        if (headerData == XING_HEADER || headerData == INFO_HEADER) {
            this.seeker = XingSeeker.create(this.synchronizedHeader, frame, headerPosition, inputLength);
            return true;
        }
        frame.setPosition(36);
        if (frame.readInt() != VBRI_HEADER) {
            return false;
        }
        this.seeker = VbriSeeker.create(this.synchronizedHeader, frame, headerPosition);
        return true;
    }

    private static long getPosition(ExtractorInput extractorInput, BufferingInput bufferingInput) {
        return extractorInput.getPosition() - ((long) bufferingInput.getAvailableByteCount());
    }
}
