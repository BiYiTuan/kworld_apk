package com.google.android.exoplayer.extractor.mp4;

import android.support.v4.media.TransportMediator;
import android.util.Pair;
import com.google.android.exoplayer.C0665C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.util.Ac3Util;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.videolan.libvlc.MediaPlayer.Event;

final class AtomParsers {

    private static final class AvcCData {
        public final List<byte[]> initializationData;
        public final int nalUnitLengthFieldLength;
        public final float pixelWidthAspectRatio;

        public AvcCData(List<byte[]> initializationData, int nalUnitLengthFieldLength, float pixelWidthAspectRatio) {
            this.initializationData = initializationData;
            this.nalUnitLengthFieldLength = nalUnitLengthFieldLength;
            this.pixelWidthAspectRatio = pixelWidthAspectRatio;
        }
    }

    private static final class StsdData {
        public MediaFormat mediaFormat;
        public int nalUnitLengthFieldLength = -1;
        public final TrackEncryptionBox[] trackEncryptionBoxes;

        public StsdData(int numberOfEntries) {
            this.trackEncryptionBoxes = new TrackEncryptionBox[numberOfEntries];
        }
    }

    private static final class TkhdData {
        private final long duration;
        private final int id;
        private final int rotationDegrees;

        public TkhdData(int id, long duration, int rotationDegrees) {
            this.id = id;
            this.duration = duration;
            this.rotationDegrees = rotationDegrees;
        }
    }

    public static Track parseTrak(ContainerAtom trak, LeafAtom mvhd, boolean isQuickTime) {
        ContainerAtom mdia = trak.getContainerAtomOfType(Atom.TYPE_mdia);
        int trackType = parseHdlr(mdia.getLeafAtomOfType(Atom.TYPE_hdlr).data);
        if (trackType != Track.TYPE_soun && trackType != Track.TYPE_vide && trackType != Track.TYPE_text && trackType != Track.TYPE_sbtl && trackType != Track.TYPE_subt) {
            return null;
        }
        long durationUs;
        TkhdData tkhdData = parseTkhd(trak.getLeafAtomOfType(Atom.TYPE_tkhd).data);
        long duration = tkhdData.duration;
        long movieTimescale = parseMvhd(mvhd.data);
        if (duration == -1) {
            durationUs = -1;
        } else {
            durationUs = Util.scaleLargeTimestamp(duration, C0665C.MICROS_PER_SECOND, movieTimescale);
        }
        ContainerAtom stbl = mdia.getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
        Pair<Long, String> mdhdData = parseMdhd(mdia.getLeafAtomOfType(Atom.TYPE_mdhd).data);
        StsdData stsdData = parseStsd(stbl.getLeafAtomOfType(Atom.TYPE_stsd).data, tkhdData.id, durationUs, tkhdData.rotationDegrees, (String) mdhdData.second, isQuickTime);
        Pair<long[], long[]> edtsData = parseEdts(trak.getContainerAtomOfType(Atom.TYPE_edts));
        if (stsdData.mediaFormat == null) {
            return null;
        }
        return new Track(tkhdData.id, trackType, ((Long) mdhdData.first).longValue(), movieTimescale, durationUs, stsdData.mediaFormat, stsdData.trackEncryptionBoxes, stsdData.nalUnitLengthFieldLength, (long[]) edtsData.first, (long[]) edtsData.second);
    }

    public static TrackSampleTable parseStbl(Track track, ContainerAtom stblAtom) {
        ParsableByteArray stsz = stblAtom.getLeafAtomOfType(Atom.TYPE_stsz).data;
        LeafAtom chunkOffsetsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_stco);
        if (chunkOffsetsAtom == null) {
            chunkOffsetsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_co64);
        }
        ParsableByteArray chunkOffsets = chunkOffsetsAtom.data;
        ParsableByteArray stsc = stblAtom.getLeafAtomOfType(Atom.TYPE_stsc).data;
        ParsableByteArray stts = stblAtom.getLeafAtomOfType(Atom.TYPE_stts).data;
        LeafAtom stssAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_stss);
        ParsableByteArray stss = stssAtom != null ? stssAtom.data : null;
        LeafAtom cttsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_ctts);
        ParsableByteArray ctts = cttsAtom != null ? cttsAtom.data : null;
        stsz.setPosition(12);
        int fixedSampleSize = stsz.readUnsignedIntToInt();
        int sampleCount = stsz.readUnsignedIntToInt();
        Object offsets = new long[sampleCount];
        Object sizes = new int[sampleCount];
        int maximumSize = 0;
        long[] timestamps = new long[sampleCount];
        Object flags = new int[sampleCount];
        if (sampleCount == 0) {
            return new TrackSampleTable(offsets, sizes, 0, timestamps, flags);
        }
        long offsetBytes;
        int i;
        chunkOffsets.setPosition(12);
        int chunkCount = chunkOffsets.readUnsignedIntToInt();
        stsc.setPosition(12);
        int remainingSamplesPerChunkChanges = stsc.readUnsignedIntToInt() - 1;
        Assertions.checkState(stsc.readInt() == 1, "stsc first chunk must be 1");
        int samplesPerChunk = stsc.readUnsignedIntToInt();
        stsc.skipBytes(4);
        int nextSamplesPerChunkChangeChunkIndex = -1;
        if (remainingSamplesPerChunkChanges > 0) {
            nextSamplesPerChunkChangeChunkIndex = stsc.readUnsignedIntToInt() - 1;
        }
        int chunkIndex = 0;
        int remainingSamplesInChunk = samplesPerChunk;
        stts.setPosition(12);
        int remainingTimestampDeltaChanges = stts.readUnsignedIntToInt() - 1;
        int remainingSamplesAtTimestampDelta = stts.readUnsignedIntToInt();
        int timestampDeltaInTimeUnits = stts.readUnsignedIntToInt();
        int remainingSamplesAtTimestampOffset = 0;
        int remainingTimestampOffsetChanges = 0;
        int timestampOffset = 0;
        if (ctts != null) {
            ctts.setPosition(12);
            remainingTimestampOffsetChanges = ctts.readUnsignedIntToInt() - 1;
            remainingSamplesAtTimestampOffset = ctts.readUnsignedIntToInt();
            timestampOffset = ctts.readInt();
        }
        int nextSynchronizationSampleIndex = -1;
        int remainingSynchronizationSamples = 0;
        if (stss != null) {
            stss.setPosition(12);
            remainingSynchronizationSamples = stss.readUnsignedIntToInt();
            nextSynchronizationSampleIndex = stss.readUnsignedIntToInt() - 1;
        }
        if (chunkOffsetsAtom.type == Atom.TYPE_stco) {
            offsetBytes = chunkOffsets.readUnsignedInt();
        } else {
            offsetBytes = chunkOffsets.readUnsignedLongToLong();
        }
        long timestampTimeUnits = 0;
        for (i = 0; i < sampleCount; i++) {
            int readUnsignedIntToInt;
            offsets[i] = offsetBytes;
            if (fixedSampleSize == 0) {
                readUnsignedIntToInt = stsz.readUnsignedIntToInt();
            } else {
                readUnsignedIntToInt = fixedSampleSize;
            }
            sizes[i] = readUnsignedIntToInt;
            if (sizes[i] > maximumSize) {
                maximumSize = sizes[i];
            }
            timestamps[i] = ((long) timestampOffset) + timestampTimeUnits;
            flags[i] = stss == null ? 1 : 0;
            if (i == nextSynchronizationSampleIndex) {
                flags[i] = 1;
                remainingSynchronizationSamples--;
                if (remainingSynchronizationSamples > 0) {
                    nextSynchronizationSampleIndex = stss.readUnsignedIntToInt() - 1;
                }
            }
            timestampTimeUnits += (long) timestampDeltaInTimeUnits;
            remainingSamplesAtTimestampDelta--;
            if (remainingSamplesAtTimestampDelta == 0 && remainingTimestampDeltaChanges > 0) {
                remainingSamplesAtTimestampDelta = stts.readUnsignedIntToInt();
                timestampDeltaInTimeUnits = stts.readUnsignedIntToInt();
                remainingTimestampDeltaChanges--;
            }
            if (ctts != null) {
                remainingSamplesAtTimestampOffset--;
                if (remainingSamplesAtTimestampOffset == 0 && remainingTimestampOffsetChanges > 0) {
                    remainingSamplesAtTimestampOffset = ctts.readUnsignedIntToInt();
                    timestampOffset = ctts.readInt();
                    remainingTimestampOffsetChanges--;
                }
            }
            remainingSamplesInChunk--;
            if (remainingSamplesInChunk == 0) {
                chunkIndex++;
                if (chunkIndex < chunkCount) {
                    if (chunkOffsetsAtom.type == Atom.TYPE_stco) {
                        offsetBytes = chunkOffsets.readUnsignedInt();
                    } else {
                        offsetBytes = chunkOffsets.readUnsignedLongToLong();
                    }
                }
                if (chunkIndex == nextSamplesPerChunkChangeChunkIndex) {
                    samplesPerChunk = stsc.readUnsignedIntToInt();
                    stsc.skipBytes(4);
                    remainingSamplesPerChunkChanges--;
                    if (remainingSamplesPerChunkChanges > 0) {
                        nextSamplesPerChunkChangeChunkIndex = stsc.readUnsignedIntToInt() - 1;
                    }
                }
                if (chunkIndex < chunkCount) {
                    remainingSamplesInChunk = samplesPerChunk;
                }
            } else {
                offsetBytes += (long) sizes[i];
            }
        }
        Assertions.checkArgument(remainingSynchronizationSamples == 0);
        Assertions.checkArgument(remainingSamplesAtTimestampDelta == 0);
        Assertions.checkArgument(remainingSamplesInChunk == 0);
        Assertions.checkArgument(remainingTimestampDeltaChanges == 0);
        Assertions.checkArgument(remainingTimestampOffsetChanges == 0);
        if (track.editListDurations == null) {
            Util.scaleLargeTimestampsInPlace(timestamps, C0665C.MICROS_PER_SECOND, track.timescale);
            return new TrackSampleTable(offsets, sizes, maximumSize, timestamps, flags);
        }
        Object editedOffsets;
        Object editedSizes;
        int editedMaximumSize;
        Object editedFlags;
        int editedSampleCount = 0;
        int nextSampleIndex = 0;
        boolean copyMetadata = false;
        for (i = 0; i < track.editListDurations.length; i++) {
            long mediaTime = track.editListMediaTimes[i];
            if (mediaTime != -1) {
                long duration = Util.scaleLargeTimestamp(track.editListDurations[i], track.timescale, track.movieTimescale);
                int startIndex = Util.binarySearchCeil(timestamps, mediaTime, true, true);
                int endIndex = Util.binarySearchCeil(timestamps, mediaTime + duration, true, false);
                editedSampleCount += endIndex - startIndex;
                copyMetadata |= nextSampleIndex != startIndex ? 1 : 0;
                nextSampleIndex = endIndex;
            }
        }
        copyMetadata |= editedSampleCount != sampleCount ? 1 : 0;
        if (copyMetadata) {
            editedOffsets = new long[editedSampleCount];
        } else {
            editedOffsets = offsets;
        }
        if (copyMetadata) {
            editedSizes = new int[editedSampleCount];
        } else {
            editedSizes = sizes;
        }
        if (copyMetadata) {
            editedMaximumSize = 0;
        } else {
            editedMaximumSize = maximumSize;
        }
        if (copyMetadata) {
            editedFlags = new int[editedSampleCount];
        } else {
            editedFlags = flags;
        }
        long[] editedTimestamps = new long[editedSampleCount];
        long pts = 0;
        int sampleIndex = 0;
        for (i = 0; i < track.editListDurations.length; i++) {
            mediaTime = track.editListMediaTimes[i];
            duration = track.editListDurations[i];
            if (mediaTime != -1) {
                long endMediaTime = mediaTime + Util.scaleLargeTimestamp(duration, track.timescale, track.movieTimescale);
                startIndex = Util.binarySearchCeil(timestamps, mediaTime, true, true);
                endIndex = Util.binarySearchCeil(timestamps, endMediaTime, true, false);
                if (copyMetadata) {
                    int count = endIndex - startIndex;
                    System.arraycopy(offsets, startIndex, editedOffsets, sampleIndex, count);
                    System.arraycopy(sizes, startIndex, editedSizes, sampleIndex, count);
                    System.arraycopy(flags, startIndex, editedFlags, sampleIndex, count);
                }
                for (int j = startIndex; j < endIndex; j++) {
                    editedTimestamps[sampleIndex] = Util.scaleLargeTimestamp(pts, C0665C.MICROS_PER_SECOND, track.movieTimescale) + Util.scaleLargeTimestamp(timestamps[j] - mediaTime, C0665C.MICROS_PER_SECOND, track.timescale);
                    if (copyMetadata && editedSizes[sampleIndex] > editedMaximumSize) {
                        editedMaximumSize = sizes[j];
                    }
                    sampleIndex++;
                }
            }
            pts += duration;
        }
        return new TrackSampleTable(editedOffsets, editedSizes, editedMaximumSize, editedTimestamps, editedFlags);
    }

    private static long parseMvhd(ParsableByteArray mvhd) {
        int i = 8;
        mvhd.setPosition(8);
        if (Atom.parseFullAtomVersion(mvhd.readInt()) != 0) {
            i = 16;
        }
        mvhd.skipBytes(i);
        return mvhd.readUnsignedInt();
    }

    private static TkhdData parseTkhd(ParsableByteArray tkhd) {
        long duration;
        int rotationDegrees;
        tkhd.setPosition(8);
        int version = Atom.parseFullAtomVersion(tkhd.readInt());
        tkhd.skipBytes(version == 0 ? 8 : 16);
        int trackId = tkhd.readInt();
        tkhd.skipBytes(4);
        boolean durationUnknown = true;
        int durationPosition = tkhd.getPosition();
        int durationByteCount = version == 0 ? 4 : 8;
        for (int i = 0; i < durationByteCount; i++) {
            if (tkhd.data[durationPosition + i] != (byte) -1) {
                durationUnknown = false;
                break;
            }
        }
        if (durationUnknown) {
            tkhd.skipBytes(durationByteCount);
            duration = -1;
        } else {
            duration = version == 0 ? tkhd.readUnsignedInt() : tkhd.readUnsignedLongToLong();
        }
        tkhd.skipBytes(16);
        int a00 = tkhd.readInt();
        int a01 = tkhd.readInt();
        tkhd.skipBytes(4);
        int a10 = tkhd.readInt();
        int a11 = tkhd.readInt();
        if (a00 == 0 && a01 == 65536 && a10 == (-65536) && a11 == 0) {
            rotationDegrees = 90;
        } else if (a00 == 0 && a01 == (-65536) && a10 == 65536 && a11 == 0) {
            rotationDegrees = Event.PausableChanged;
        } else if (a00 == (-65536) && a01 == 0 && a10 == 0 && a11 == (-65536)) {
            rotationDegrees = 180;
        } else {
            rotationDegrees = 0;
        }
        return new TkhdData(trackId, duration, rotationDegrees);
    }

    private static int parseHdlr(ParsableByteArray hdlr) {
        hdlr.setPosition(16);
        return hdlr.readInt();
    }

    private static Pair<Long, String> parseMdhd(ParsableByteArray mdhd) {
        int i = 8;
        mdhd.setPosition(8);
        int version = Atom.parseFullAtomVersion(mdhd.readInt());
        mdhd.skipBytes(version == 0 ? 8 : 16);
        long timescale = mdhd.readUnsignedInt();
        if (version == 0) {
            i = 4;
        }
        mdhd.skipBytes(i);
        int languageCode = mdhd.readUnsignedShort();
        return Pair.create(Long.valueOf(timescale), "" + ((char) (((languageCode >> 10) & 31) + 96)) + ((char) (((languageCode >> 5) & 31) + 96)) + ((char) ((languageCode & 31) + 96)));
    }

    private static StsdData parseStsd(ParsableByteArray stsd, int trackId, long durationUs, int rotationDegrees, String language, boolean isQuickTime) {
        stsd.setPosition(12);
        int numberOfEntries = stsd.readInt();
        StsdData out = new StsdData(numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++) {
            int childStartPosition = stsd.getPosition();
            int childAtomSize = stsd.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = stsd.readInt();
            if (childAtomType == Atom.TYPE_avc1 || childAtomType == Atom.TYPE_avc3 || childAtomType == Atom.TYPE_encv || childAtomType == Atom.TYPE_mp4v || childAtomType == Atom.TYPE_hvc1 || childAtomType == Atom.TYPE_hev1 || childAtomType == Atom.TYPE_s263) {
                parseVideoSampleEntry(stsd, childStartPosition, childAtomSize, trackId, durationUs, rotationDegrees, out, i);
            } else if (childAtomType == Atom.TYPE_mp4a || childAtomType == Atom.TYPE_enca || childAtomType == Atom.TYPE_ac_3 || childAtomType == Atom.TYPE_ec_3 || childAtomType == Atom.TYPE_dtsc || childAtomType == Atom.TYPE_dtse || childAtomType == Atom.TYPE_dtsh || childAtomType == Atom.TYPE_dtsl || childAtomType == Atom.TYPE_samr || childAtomType == Atom.TYPE_sawb) {
                parseAudioSampleEntry(stsd, childAtomType, childStartPosition, childAtomSize, trackId, durationUs, language, isQuickTime, out, i);
            } else if (childAtomType == Atom.TYPE_TTML) {
                out.mediaFormat = MediaFormat.createTextFormat(Integer.toString(trackId), MimeTypes.APPLICATION_TTML, -1, durationUs, language);
            } else if (childAtomType == Atom.TYPE_tx3g) {
                out.mediaFormat = MediaFormat.createTextFormat(Integer.toString(trackId), MimeTypes.APPLICATION_TX3G, -1, durationUs, language);
            } else if (childAtomType == Atom.TYPE_stpp) {
                out.mediaFormat = MediaFormat.createTextFormat(Integer.toString(trackId), MimeTypes.APPLICATION_TTML, -1, durationUs, language, 0);
            }
            stsd.setPosition(childStartPosition + childAtomSize);
        }
        return out;
    }

    private static void parseVideoSampleEntry(ParsableByteArray parent, int position, int size, int trackId, long durationUs, int rotationDegrees, StsdData out, int entryIndex) {
        parent.setPosition(position + 8);
        parent.skipBytes(24);
        int width = parent.readUnsignedShort();
        int height = parent.readUnsignedShort();
        boolean pixelWidthHeightRatioFromPasp = false;
        float pixelWidthHeightRatio = 1.0f;
        parent.skipBytes(50);
        List<byte[]> initializationData = null;
        int childPosition = parent.getPosition();
        String mimeType = null;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childStartPosition = parent.getPosition();
            int childAtomSize = parent.readInt();
            if (childAtomSize == 0 && parent.getPosition() - position == size) {
                break;
            }
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_avcC) {
                Assertions.checkState(mimeType == null);
                mimeType = MimeTypes.VIDEO_H264;
                AvcCData avcCData = parseAvcCFromParent(parent, childStartPosition);
                initializationData = avcCData.initializationData;
                out.nalUnitLengthFieldLength = avcCData.nalUnitLengthFieldLength;
                if (!pixelWidthHeightRatioFromPasp) {
                    pixelWidthHeightRatio = avcCData.pixelWidthAspectRatio;
                }
            } else if (childAtomType == Atom.TYPE_hvcC) {
                Assertions.checkState(mimeType == null);
                mimeType = MimeTypes.VIDEO_H265;
                Pair<List<byte[]>, Integer> hvcCData = parseHvcCFromParent(parent, childStartPosition);
                initializationData = hvcCData.first;
                out.nalUnitLengthFieldLength = ((Integer) hvcCData.second).intValue();
            } else if (childAtomType == Atom.TYPE_d263) {
                Assertions.checkState(mimeType == null);
                mimeType = MimeTypes.VIDEO_H263;
            } else if (childAtomType == Atom.TYPE_esds) {
                Assertions.checkState(mimeType == null);
                Pair<String, byte[]> mimeTypeAndInitializationData = parseEsdsFromParent(parent, childStartPosition);
                mimeType = mimeTypeAndInitializationData.first;
                initializationData = Collections.singletonList(mimeTypeAndInitializationData.second);
            } else if (childAtomType == Atom.TYPE_sinf) {
                out.trackEncryptionBoxes[entryIndex] = parseSinfFromParent(parent, childStartPosition, childAtomSize);
            } else if (childAtomType == Atom.TYPE_pasp) {
                pixelWidthHeightRatio = parsePaspFromParent(parent, childStartPosition);
                pixelWidthHeightRatioFromPasp = true;
            }
            childPosition += childAtomSize;
        }
        if (mimeType != null) {
            out.mediaFormat = MediaFormat.createVideoFormat(Integer.toString(trackId), mimeType, -1, -1, durationUs, width, height, initializationData, rotationDegrees, pixelWidthHeightRatio);
        }
    }

    private static AvcCData parseAvcCFromParent(ParsableByteArray parent, int position) {
        parent.setPosition((position + 8) + 4);
        int nalUnitLengthFieldLength = (parent.readUnsignedByte() & 3) + 1;
        if (nalUnitLengthFieldLength == 3) {
            throw new IllegalStateException();
        }
        int j;
        List<byte[]> initializationData = new ArrayList();
        float pixelWidthAspectRatio = 1.0f;
        int numSequenceParameterSets = parent.readUnsignedByte() & 31;
        for (j = 0; j < numSequenceParameterSets; j++) {
            initializationData.add(NalUnitUtil.parseChildNalUnit(parent));
        }
        int numPictureParameterSets = parent.readUnsignedByte();
        for (j = 0; j < numPictureParameterSets; j++) {
            initializationData.add(NalUnitUtil.parseChildNalUnit(parent));
        }
        if (numSequenceParameterSets > 0) {
            ParsableBitArray spsDataBitArray = new ParsableBitArray((byte[]) initializationData.get(0));
            spsDataBitArray.setPosition((nalUnitLengthFieldLength + 1) * 8);
            pixelWidthAspectRatio = CodecSpecificDataUtil.parseSpsNalUnit(spsDataBitArray).pixelWidthAspectRatio;
        }
        return new AvcCData(initializationData, nalUnitLengthFieldLength, pixelWidthAspectRatio);
    }

    private static Pair<List<byte[]>, Integer> parseHvcCFromParent(ParsableByteArray parent, int position) {
        int i;
        parent.setPosition((position + 8) + 21);
        int lengthSizeMinusOne = parent.readUnsignedByte() & 3;
        int numberOfArrays = parent.readUnsignedByte();
        int csdLength = 0;
        int csdStartPosition = parent.getPosition();
        for (i = 0; i < numberOfArrays; i++) {
            int j;
            parent.skipBytes(1);
            int numberOfNalUnits = parent.readUnsignedShort();
            for (j = 0; j < numberOfNalUnits; j++) {
                int nalUnitLength = parent.readUnsignedShort();
                csdLength += nalUnitLength + 4;
                parent.skipBytes(nalUnitLength);
            }
        }
        parent.setPosition(csdStartPosition);
        byte[] buffer = new byte[csdLength];
        int bufferPosition = 0;
        for (i = 0; i < numberOfArrays; i++) {
            parent.skipBytes(1);
            numberOfNalUnits = parent.readUnsignedShort();
            for (j = 0; j < numberOfNalUnits; j++) {
                nalUnitLength = parent.readUnsignedShort();
                System.arraycopy(NalUnitUtil.NAL_START_CODE, 0, buffer, bufferPosition, NalUnitUtil.NAL_START_CODE.length);
                bufferPosition += NalUnitUtil.NAL_START_CODE.length;
                System.arraycopy(parent.data, parent.getPosition(), buffer, bufferPosition, nalUnitLength);
                bufferPosition += nalUnitLength;
                parent.skipBytes(nalUnitLength);
            }
        }
        return Pair.create(csdLength == 0 ? null : Collections.singletonList(buffer), Integer.valueOf(lengthSizeMinusOne + 1));
    }

    private static Pair<long[], long[]> parseEdts(ContainerAtom edtsAtom) {
        if (edtsAtom != null) {
            LeafAtom elst = edtsAtom.getLeafAtomOfType(Atom.TYPE_elst);
            if (elst != null) {
                ParsableByteArray elstData = elst.data;
                elstData.setPosition(8);
                int version = Atom.parseFullAtomVersion(elstData.readInt());
                int entryCount = elstData.readUnsignedIntToInt();
                long[] editListDurations = new long[entryCount];
                long[] editListMediaTimes = new long[entryCount];
                for (int i = 0; i < entryCount; i++) {
                    editListDurations[i] = version == 1 ? elstData.readUnsignedLongToLong() : elstData.readUnsignedInt();
                    editListMediaTimes[i] = version == 1 ? elstData.readLong() : (long) elstData.readInt();
                    if (elstData.readShort() != 1) {
                        throw new IllegalArgumentException("Unsupported media rate.");
                    }
                    elstData.skipBytes(2);
                }
                return Pair.create(editListDurations, editListMediaTimes);
            }
        }
        return Pair.create(null, null);
    }

    private static TrackEncryptionBox parseSinfFromParent(ParsableByteArray parent, int position, int size) {
        int childPosition = position + 8;
        TrackEncryptionBox trackEncryptionBox = null;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_frma) {
                parent.readInt();
            } else if (childAtomType == Atom.TYPE_schm) {
                parent.skipBytes(4);
                parent.readInt();
                parent.readInt();
            } else if (childAtomType == Atom.TYPE_schi) {
                trackEncryptionBox = parseSchiFromParent(parent, childPosition, childAtomSize);
            }
            childPosition += childAtomSize;
        }
        return trackEncryptionBox;
    }

    private static float parsePaspFromParent(ParsableByteArray parent, int position) {
        parent.setPosition(position + 8);
        return ((float) parent.readUnsignedIntToInt()) / ((float) parent.readUnsignedIntToInt());
    }

    private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray parent, int position, int size) {
        boolean defaultIsEncrypted = true;
        int childPosition = position + 8;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            if (parent.readInt() == Atom.TYPE_tenc) {
                parent.skipBytes(4);
                int firstInt = parent.readInt();
                if ((firstInt >> 8) != 1) {
                    defaultIsEncrypted = false;
                }
                int defaultInitVectorSize = firstInt & 255;
                byte[] defaultKeyId = new byte[16];
                parent.readBytes(defaultKeyId, 0, defaultKeyId.length);
                return new TrackEncryptionBox(defaultIsEncrypted, defaultInitVectorSize, defaultKeyId);
            }
            childPosition += childAtomSize;
        }
        return null;
    }

    private static void parseAudioSampleEntry(ParsableByteArray parent, int atomType, int position, int size, int trackId, long durationUs, String language, boolean isQuickTime, StsdData out, int entryIndex) {
        parent.setPosition(position + 8);
        int quickTimeSoundDescriptionVersion = 0;
        if (isQuickTime) {
            parent.skipBytes(8);
            quickTimeSoundDescriptionVersion = parent.readUnsignedShort();
            parent.skipBytes(6);
        } else {
            parent.skipBytes(16);
        }
        int channelCount = parent.readUnsignedShort();
        int sampleSize = parent.readUnsignedShort();
        parent.skipBytes(4);
        int sampleRate = parent.readUnsignedFixedPoint1616();
        if (quickTimeSoundDescriptionVersion > 0) {
            parent.skipBytes(16);
            if (quickTimeSoundDescriptionVersion == 2) {
                parent.skipBytes(20);
            }
        }
        String mimeType = null;
        if (atomType == Atom.TYPE_ac_3) {
            mimeType = MimeTypes.AUDIO_AC3;
        } else if (atomType == Atom.TYPE_ec_3) {
            mimeType = MimeTypes.AUDIO_E_AC3;
        } else if (atomType == Atom.TYPE_dtsc || atomType == Atom.TYPE_dtse) {
            mimeType = MimeTypes.AUDIO_DTS;
        } else if (atomType == Atom.TYPE_dtsh || atomType == Atom.TYPE_dtsl) {
            mimeType = MimeTypes.AUDIO_DTS_HD;
        } else if (atomType == Atom.TYPE_samr) {
            mimeType = MimeTypes.AUDIO_AMR_NB;
        } else if (atomType == Atom.TYPE_sawb) {
            mimeType = MimeTypes.AUDIO_AMR_WB;
        }
        byte[] initializationData = null;
        int childAtomPosition = parent.getPosition();
        while (childAtomPosition - position < size) {
            parent.setPosition(childAtomPosition);
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = parent.readInt();
            if (atomType == Atom.TYPE_mp4a || atomType == Atom.TYPE_enca) {
                int esdsAtomPosition = -1;
                if (childAtomType == Atom.TYPE_esds) {
                    esdsAtomPosition = childAtomPosition;
                } else if (isQuickTime && childAtomType == Atom.TYPE_wave) {
                    esdsAtomPosition = findEsdsPosition(parent, childAtomPosition, childAtomSize);
                }
                if (esdsAtomPosition != -1) {
                    Pair<String, byte[]> mimeTypeAndInitializationData = parseEsdsFromParent(parent, esdsAtomPosition);
                    mimeType = mimeTypeAndInitializationData.first;
                    initializationData = (byte[]) mimeTypeAndInitializationData.second;
                    if (MimeTypes.AUDIO_AAC.equals(mimeType)) {
                        Pair<Integer, Integer> audioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(initializationData);
                        sampleRate = ((Integer) audioSpecificConfig.first).intValue();
                        channelCount = ((Integer) audioSpecificConfig.second).intValue();
                    }
                } else if (childAtomType == Atom.TYPE_sinf) {
                    out.trackEncryptionBoxes[entryIndex] = parseSinfFromParent(parent, childAtomPosition, childAtomSize);
                }
            } else if (atomType == Atom.TYPE_ac_3 && childAtomType == Atom.TYPE_dac3) {
                parent.setPosition(childAtomPosition + 8);
                out.mediaFormat = Ac3Util.parseAc3AnnexFFormat(parent, Integer.toString(trackId), durationUs, language);
                return;
            } else if (atomType == Atom.TYPE_ec_3 && childAtomType == Atom.TYPE_dec3) {
                parent.setPosition(childAtomPosition + 8);
                out.mediaFormat = Ac3Util.parseEAc3AnnexFFormat(parent, Integer.toString(trackId), durationUs, language);
                return;
            } else if ((atomType == Atom.TYPE_dtsc || atomType == Atom.TYPE_dtse || atomType == Atom.TYPE_dtsh || atomType == Atom.TYPE_dtsl) && childAtomType == Atom.TYPE_ddts) {
                out.mediaFormat = MediaFormat.createAudioFormat(Integer.toString(trackId), mimeType, -1, -1, durationUs, channelCount, sampleRate, null, language);
                return;
            }
            childAtomPosition += childAtomSize;
        }
        if (mimeType != null) {
            out.mediaFormat = MediaFormat.createAudioFormat(Integer.toString(trackId), mimeType, -1, sampleSize, durationUs, channelCount, sampleRate, initializationData == null ? null : Collections.singletonList(initializationData), language);
        }
    }

    private static int findEsdsPosition(ParsableByteArray parent, int position, int size) {
        int childAtomPosition = parent.getPosition();
        while (childAtomPosition - position < size) {
            parent.setPosition(childAtomPosition);
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            if (parent.readInt() == Atom.TYPE_esds) {
                return childAtomPosition;
            }
            childAtomPosition += childAtomSize;
        }
        return -1;
    }

    private static Pair<String, byte[]> parseEsdsFromParent(ParsableByteArray parent, int position) {
        String mimeType;
        parent.setPosition((position + 8) + 4);
        parent.skipBytes(1);
        int varIntByte = parent.readUnsignedByte();
        while (varIntByte > TransportMediator.KEYCODE_MEDIA_PAUSE) {
            varIntByte = parent.readUnsignedByte();
        }
        parent.skipBytes(2);
        int flags = parent.readUnsignedByte();
        if ((flags & 128) != 0) {
            parent.skipBytes(2);
        }
        if ((flags & 64) != 0) {
            parent.skipBytes(parent.readUnsignedShort());
        }
        if ((flags & 32) != 0) {
            parent.skipBytes(2);
        }
        parent.skipBytes(1);
        varIntByte = parent.readUnsignedByte();
        while (varIntByte > TransportMediator.KEYCODE_MEDIA_PAUSE) {
            varIntByte = parent.readUnsignedByte();
        }
        switch (parent.readUnsignedByte()) {
            case 32:
                mimeType = MimeTypes.VIDEO_MP4V;
                break;
            case 33:
                mimeType = MimeTypes.VIDEO_H264;
                break;
            case 35:
                mimeType = MimeTypes.VIDEO_H265;
                break;
            case 64:
            case 102:
            case 103:
            case 104:
                mimeType = MimeTypes.AUDIO_AAC;
                break;
            case 107:
                return Pair.create(MimeTypes.AUDIO_MPEG, null);
            case 165:
                mimeType = MimeTypes.AUDIO_AC3;
                break;
            case 166:
                mimeType = MimeTypes.AUDIO_E_AC3;
                break;
            case 169:
            case 172:
                return Pair.create(MimeTypes.AUDIO_DTS, null);
            case 170:
            case 171:
                return Pair.create(MimeTypes.AUDIO_DTS_HD, null);
            default:
                mimeType = null;
                break;
        }
        parent.skipBytes(12);
        parent.skipBytes(1);
        varIntByte = parent.readUnsignedByte();
        int varInt = varIntByte & TransportMediator.KEYCODE_MEDIA_PAUSE;
        while (varIntByte > TransportMediator.KEYCODE_MEDIA_PAUSE) {
            varIntByte = parent.readUnsignedByte();
            varInt = (varInt << 8) | (varIntByte & TransportMediator.KEYCODE_MEDIA_PAUSE);
        }
        byte[] initializationData = new byte[varInt];
        parent.readBytes(initializationData, 0, varInt);
        return Pair.create(mimeType, initializationData);
    }

    private AtomParsers() {
    }
}
