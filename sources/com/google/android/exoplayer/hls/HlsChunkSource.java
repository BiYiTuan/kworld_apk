package com.google.android.exoplayer.hls;

import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer.BehindLiveWindowException;
import com.google.android.exoplayer.chunk.BaseChunkSampleSourceEventListener;
import com.google.android.exoplayer.chunk.Chunk;
import com.google.android.exoplayer.chunk.ChunkOperationHolder;
import com.google.android.exoplayer.chunk.DataChunk;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.Format.DecreasingBandwidthComparator;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import com.google.android.exoplayer.extractor.ts.TsExtractor;
import com.google.android.exoplayer.hls.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.HttpDataSource.InvalidResponseCodeException;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.UriUtil;
import com.google.android.exoplayer.util.Util;
import cz.msebera.android.httpclient.HttpStatus;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HlsChunkSource {
    private static final String AAC_FILE_EXTENSION = ".aac";
    public static final int ADAPTIVE_MODE_ABRUPT = 3;
    public static final int ADAPTIVE_MODE_NONE = 0;
    public static final int ADAPTIVE_MODE_SPLICE = 1;
    private static final float BANDWIDTH_FRACTION = 0.8f;
    public static final long DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS = 20000;
    public static final long DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS = 5000;
    public static final long DEFAULT_PLAYLIST_BLACKLIST_MS = 60000;
    private static final String MP3_FILE_EXTENSION = ".mp3";
    private static final String TAG = "HlsChunkSource";
    private final int adaptiveMaxHeight;
    private final int adaptiveMaxWidth;
    private final int adaptiveMode;
    private final BandwidthMeter bandwidthMeter;
    private final String baseUri;
    private final DataSource dataSource;
    private long durationUs;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private IOException fatalError;
    private boolean live;
    private final long maxBufferDurationToSwitchDownUs;
    private final long minBufferDurationToSwitchUpUs;
    private final HlsPlaylistParser playlistParser;
    private PtsTimestampAdjuster ptsTimestampAdjuster;
    private byte[] scratchSpace;
    private int selectedVariantIndex;
    private final long[] variantBlacklistTimes;
    private final long[] variantLastPlaylistLoadTimesMs;
    private final HlsMediaPlaylist[] variantPlaylists;
    private final Variant[] variants;

    /* renamed from: com.google.android.exoplayer.hls.HlsChunkSource$1 */
    static class C06971 implements Comparator<Variant> {
        private final Comparator<Format> formatComparator = new DecreasingBandwidthComparator();

        C06971() {
        }

        public int compare(Variant first, Variant second) {
            return this.formatComparator.compare(first.format, second.format);
        }
    }

    public interface EventListener extends BaseChunkSampleSourceEventListener {
    }

    private static class EncryptionKeyChunk extends DataChunk {
        public final String iv;
        private byte[] result;
        public final int variantIndex;

        public EncryptionKeyChunk(DataSource dataSource, DataSpec dataSpec, byte[] scratchSpace, String iv, int variantIndex) {
            super(dataSource, dataSpec, 3, 0, null, -1, scratchSpace);
            this.iv = iv;
            this.variantIndex = variantIndex;
        }

        protected void consume(byte[] data, int limit) throws IOException {
            this.result = Arrays.copyOf(data, limit);
        }

        public byte[] getResult() {
            return this.result;
        }
    }

    private static class MediaPlaylistChunk extends DataChunk {
        private final HlsPlaylistParser playlistParser;
        private final String playlistUrl;
        private HlsMediaPlaylist result;
        public final int variantIndex;

        public MediaPlaylistChunk(DataSource dataSource, DataSpec dataSpec, byte[] scratchSpace, HlsPlaylistParser playlistParser, int variantIndex, String playlistUrl) {
            super(dataSource, dataSpec, 4, 0, null, -1, scratchSpace);
            this.variantIndex = variantIndex;
            this.playlistParser = playlistParser;
            this.playlistUrl = playlistUrl;
        }

        protected void consume(byte[] data, int limit) throws IOException {
            this.result = (HlsMediaPlaylist) this.playlistParser.parse(this.playlistUrl, new ByteArrayInputStream(data, 0, limit));
        }

        public HlsMediaPlaylist getResult() {
            return this.result;
        }
    }

    public HlsChunkSource(DataSource dataSource, String playlistUrl, HlsPlaylist playlist, BandwidthMeter bandwidthMeter, int[] variantIndices, int adaptiveMode) {
        this(dataSource, playlistUrl, playlist, bandwidthMeter, variantIndices, adaptiveMode, DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS, DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS);
    }

    public HlsChunkSource(DataSource dataSource, String playlistUrl, HlsPlaylist playlist, BandwidthMeter bandwidthMeter, int[] variantIndices, int adaptiveMode, long minBufferDurationToSwitchUpMs, long maxBufferDurationToSwitchDownMs) {
        this.dataSource = dataSource;
        this.bandwidthMeter = bandwidthMeter;
        this.adaptiveMode = adaptiveMode;
        this.minBufferDurationToSwitchUpUs = 1000 * minBufferDurationToSwitchUpMs;
        this.maxBufferDurationToSwitchDownUs = 1000 * maxBufferDurationToSwitchDownMs;
        this.baseUri = playlist.baseUri;
        this.playlistParser = new HlsPlaylistParser();
        if (playlist.type == 1) {
            Format format = new Format("0", MimeTypes.APPLICATION_M3U8, -1, -1, -1.0f, -1, -1, -1, null, null);
            this.variants = new Variant[]{new Variant(playlistUrl, format)};
            this.variantPlaylists = new HlsMediaPlaylist[1];
            this.variantLastPlaylistLoadTimesMs = new long[1];
            this.variantBlacklistTimes = new long[1];
            setMediaPlaylist(0, (HlsMediaPlaylist) playlist);
            this.adaptiveMaxWidth = -1;
            this.adaptiveMaxHeight = -1;
            return;
        }
        List<Variant> masterPlaylistVariants = ((HlsMasterPlaylist) playlist).variants;
        this.variants = buildOrderedVariants(masterPlaylistVariants, variantIndices);
        this.variantPlaylists = new HlsMediaPlaylist[this.variants.length];
        this.variantLastPlaylistLoadTimesMs = new long[this.variants.length];
        this.variantBlacklistTimes = new long[this.variants.length];
        int maxWidth = -1;
        int maxHeight = -1;
        int minOriginalVariantIndex = Integer.MAX_VALUE;
        for (int i = 0; i < this.variants.length; i++) {
            int originalVariantIndex = masterPlaylistVariants.indexOf(this.variants[i]);
            if (originalVariantIndex < minOriginalVariantIndex) {
                minOriginalVariantIndex = originalVariantIndex;
                this.selectedVariantIndex = i;
            }
            Format variantFormat = this.variants[i].format;
            maxWidth = Math.max(variantFormat.width, maxWidth);
            maxHeight = Math.max(variantFormat.height, maxHeight);
        }
        if (this.variants.length <= 1 || adaptiveMode == 0) {
            this.adaptiveMaxWidth = -1;
            this.adaptiveMaxHeight = -1;
            return;
        }
        if (maxWidth <= 0) {
            maxWidth = 1920;
        }
        this.adaptiveMaxWidth = maxWidth;
        if (maxHeight <= 0) {
            maxHeight = 1080;
        }
        this.adaptiveMaxHeight = maxHeight;
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        }
    }

    public void getChunkOperation(TsChunk previousTsChunk, long playbackPositionUs, ChunkOperationHolder out) {
        int nextVariantIndex;
        boolean switchingVariantSpliced;
        if (this.adaptiveMode == 0) {
            nextVariantIndex = this.selectedVariantIndex;
            switchingVariantSpliced = false;
        } else {
            nextVariantIndex = getNextVariantIndex(previousTsChunk, playbackPositionUs);
            switchingVariantSpliced = (previousTsChunk == null || this.variants[nextVariantIndex].format.equals(previousTsChunk.format) || this.adaptiveMode != 1) ? false : true;
        }
        HlsMediaPlaylist mediaPlaylist = this.variantPlaylists[nextVariantIndex];
        if (mediaPlaylist == null) {
            out.chunk = newMediaPlaylistChunk(nextVariantIndex);
            return;
        }
        int chunkMediaSequence;
        this.selectedVariantIndex = nextVariantIndex;
        if (this.live) {
            if (previousTsChunk == null) {
                chunkMediaSequence = getLiveStartChunkMediaSequence(nextVariantIndex);
            } else {
                chunkMediaSequence = switchingVariantSpliced ? previousTsChunk.chunkIndex : previousTsChunk.chunkIndex + 1;
                if (chunkMediaSequence < mediaPlaylist.mediaSequence) {
                    this.fatalError = new BehindLiveWindowException();
                    return;
                }
            }
        } else if (previousTsChunk == null) {
            chunkMediaSequence = Util.binarySearchFloor(mediaPlaylist.segments, Long.valueOf(playbackPositionUs), true, true) + mediaPlaylist.mediaSequence;
        } else {
            chunkMediaSequence = switchingVariantSpliced ? previousTsChunk.chunkIndex : previousTsChunk.chunkIndex + 1;
        }
        int chunkIndex = chunkMediaSequence - mediaPlaylist.mediaSequence;
        if (chunkIndex < mediaPlaylist.segments.size()) {
            long startTimeUs;
            HlsExtractorWrapper extractorWrapper;
            Segment segment = (Segment) mediaPlaylist.segments.get(chunkIndex);
            Uri chunkUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.url);
            if (segment.isEncrypted) {
                Uri keyUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.encryptionKeyUri);
                if (!keyUri.equals(this.encryptionKeyUri)) {
                    out.chunk = newEncryptionKeyChunk(keyUri, segment.encryptionIV, this.selectedVariantIndex);
                    return;
                } else if (!Util.areEqual(segment.encryptionIV, this.encryptionIvString)) {
                    setEncryptionData(keyUri, segment.encryptionIV, this.encryptionKey);
                }
            } else {
                clearEncryptionData();
            }
            DataSpec dataSpec = new DataSpec(chunkUri, (long) segment.byterangeOffset, (long) segment.byterangeLength, null);
            if (!this.live) {
                startTimeUs = segment.startTimeUs;
            } else if (previousTsChunk == null) {
                startTimeUs = 0;
            } else if (switchingVariantSpliced) {
                startTimeUs = previousTsChunk.startTimeUs;
            } else {
                startTimeUs = previousTsChunk.endTimeUs;
            }
            long endTimeUs = startTimeUs + ((long) (segment.durationSecs * 1000000.0d));
            Format format = this.variants[this.selectedVariantIndex].format;
            if (chunkUri.getLastPathSegment().endsWith(AAC_FILE_EXTENSION)) {
                extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, new AdtsExtractor(startTimeUs), switchingVariantSpliced, this.adaptiveMaxWidth, this.adaptiveMaxHeight);
            } else if (chunkUri.getLastPathSegment().endsWith(MP3_FILE_EXTENSION)) {
                extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, new Mp3Extractor(startTimeUs), switchingVariantSpliced, this.adaptiveMaxWidth, this.adaptiveMaxHeight);
            } else if (previousTsChunk != null && null == null && previousTsChunk.discontinuitySequenceNumber == segment.discontinuitySequenceNumber && format.equals(previousTsChunk.format)) {
                extractorWrapper = previousTsChunk.extractorWrapper;
            } else {
                if (previousTsChunk == null || null != null || previousTsChunk.discontinuitySequenceNumber != segment.discontinuitySequenceNumber || this.ptsTimestampAdjuster == null) {
                    this.ptsTimestampAdjuster = new PtsTimestampAdjuster(startTimeUs);
                }
                extractorWrapper = new HlsExtractorWrapper(0, format, startTimeUs, new TsExtractor(this.ptsTimestampAdjuster), switchingVariantSpliced, this.adaptiveMaxWidth, this.adaptiveMaxHeight);
            }
            out.chunk = new TsChunk(this.dataSource, dataSpec, 0, format, startTimeUs, endTimeUs, chunkMediaSequence, segment.discontinuitySequenceNumber, extractorWrapper, this.encryptionKey, this.encryptionIv);
        } else if (!mediaPlaylist.live) {
            out.endOfStream = true;
        } else if (shouldRerequestLiveMediaPlaylist(nextVariantIndex)) {
            out.chunk = newMediaPlaylistChunk(nextVariantIndex);
        }
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof MediaPlaylistChunk) {
            MediaPlaylistChunk mediaPlaylistChunk = (MediaPlaylistChunk) chunk;
            this.scratchSpace = mediaPlaylistChunk.getDataHolder();
            setMediaPlaylist(mediaPlaylistChunk.variantIndex, mediaPlaylistChunk.getResult());
        } else if (chunk instanceof EncryptionKeyChunk) {
            EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk) chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.iv, encryptionKeyChunk.getResult());
        }
    }

    public boolean onChunkLoadError(Chunk chunk, IOException e) {
        if (chunk.bytesLoaded() == 0 && (((chunk instanceof TsChunk) || (chunk instanceof MediaPlaylistChunk) || (chunk instanceof EncryptionKeyChunk)) && (e instanceof InvalidResponseCodeException))) {
            int responseCode = ((InvalidResponseCodeException) e).responseCode;
            if (responseCode == HttpStatus.SC_NOT_FOUND || responseCode == HttpStatus.SC_GONE) {
                int variantIndex;
                if (chunk instanceof TsChunk) {
                    variantIndex = getVariantIndex(((TsChunk) chunk).format);
                } else if (chunk instanceof MediaPlaylistChunk) {
                    variantIndex = ((MediaPlaylistChunk) chunk).variantIndex;
                } else {
                    variantIndex = ((EncryptionKeyChunk) chunk).variantIndex;
                }
                boolean alreadyBlacklisted = this.variantBlacklistTimes[variantIndex] != 0;
                this.variantBlacklistTimes[variantIndex] = SystemClock.elapsedRealtime();
                if (alreadyBlacklisted) {
                    Log.w(TAG, "Already blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
                    return false;
                } else if (allVariantsBlacklisted()) {
                    Log.w(TAG, "Final variant not blacklisted (" + responseCode + "): " + chunk.dataSpec.uri);
                    this.variantBlacklistTimes[variantIndex] = 0;
                    return false;
                } else {
                    Log.w(TAG, "Blacklisted variant (" + responseCode + "): " + chunk.dataSpec.uri);
                    return true;
                }
            }
        }
        return false;
    }

    public void reset() {
        this.fatalError = null;
    }

    private int getNextVariantIndex(TsChunk previousTsChunk, long playbackPositionUs) {
        clearStaleBlacklistedVariants();
        long bitrateEstimate = this.bandwidthMeter.getBitrateEstimate();
        if (this.variantBlacklistTimes[this.selectedVariantIndex] != 0) {
            return getVariantIndexForBandwidth(bitrateEstimate);
        }
        if (previousTsChunk == null) {
            return this.selectedVariantIndex;
        }
        if (bitrateEstimate == -1) {
            return this.selectedVariantIndex;
        }
        int idealIndex = getVariantIndexForBandwidth(bitrateEstimate);
        if (idealIndex == this.selectedVariantIndex) {
            return this.selectedVariantIndex;
        }
        long bufferedUs = (this.adaptiveMode == 1 ? previousTsChunk.startTimeUs : previousTsChunk.endTimeUs) - playbackPositionUs;
        if (this.variantBlacklistTimes[this.selectedVariantIndex] != 0) {
            return idealIndex;
        }
        if (idealIndex <= this.selectedVariantIndex || bufferedUs >= this.maxBufferDurationToSwitchDownUs) {
            return (idealIndex >= this.selectedVariantIndex || bufferedUs <= this.minBufferDurationToSwitchUpUs) ? this.selectedVariantIndex : idealIndex;
        } else {
            return idealIndex;
        }
    }

    private int getVariantIndexForBandwidth(long bitrateEstimate) {
        if (bitrateEstimate == -1) {
            bitrateEstimate = 0;
        }
        int effectiveBitrate = (int) (((float) bitrateEstimate) * 0.8f);
        int lowestQualityEnabledVariantIndex = -1;
        for (int i = 0; i < this.variants.length; i++) {
            if (this.variantBlacklistTimes[i] == 0) {
                if (this.variants[i].format.bitrate <= effectiveBitrate) {
                    return i;
                }
                lowestQualityEnabledVariantIndex = i;
            }
        }
        Assertions.checkState(lowestQualityEnabledVariantIndex != -1);
        return lowestQualityEnabledVariantIndex;
    }

    private boolean shouldRerequestLiveMediaPlaylist(int nextVariantIndex) {
        return SystemClock.elapsedRealtime() - this.variantLastPlaylistLoadTimesMs[nextVariantIndex] >= ((long) ((this.variantPlaylists[nextVariantIndex].targetDurationSecs * 1000) / 2));
    }

    private int getLiveStartChunkMediaSequence(int variantIndex) {
        HlsMediaPlaylist mediaPlaylist = this.variantPlaylists[variantIndex];
        return mediaPlaylist.mediaSequence + (mediaPlaylist.segments.size() > 3 ? mediaPlaylist.segments.size() - 3 : 0);
    }

    private MediaPlaylistChunk newMediaPlaylistChunk(int variantIndex) {
        Uri mediaPlaylistUri = UriUtil.resolveToUri(this.baseUri, this.variants[variantIndex].url);
        return new MediaPlaylistChunk(this.dataSource, new DataSpec(mediaPlaylistUri, 0, -1, null, 1), this.scratchSpace, this.playlistParser, variantIndex, mediaPlaylistUri.toString());
    }

    private EncryptionKeyChunk newEncryptionKeyChunk(Uri keyUri, String iv, int variantIndex) {
        return new EncryptionKeyChunk(this.dataSource, new DataSpec(keyUri, 0, -1, null, 1), this.scratchSpace, iv, variantIndex);
    }

    private void setEncryptionData(Uri keyUri, String iv, byte[] secretKey) {
        String trimmedIv;
        if (iv.toLowerCase(Locale.getDefault()).startsWith("0x")) {
            trimmedIv = iv.substring(2);
        } else {
            trimmedIv = iv;
        }
        byte[] ivData = new BigInteger(trimmedIv, 16).toByteArray();
        byte[] ivDataWithPadding = new byte[16];
        int offset = ivData.length > 16 ? ivData.length - 16 : 0;
        System.arraycopy(ivData, offset, ivDataWithPadding, (ivDataWithPadding.length - ivData.length) + offset, ivData.length - offset);
        this.encryptionKeyUri = keyUri;
        this.encryptionKey = secretKey;
        this.encryptionIvString = iv;
        this.encryptionIv = ivDataWithPadding;
    }

    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }

    private void setMediaPlaylist(int variantIndex, HlsMediaPlaylist mediaPlaylist) {
        this.variantLastPlaylistLoadTimesMs[variantIndex] = SystemClock.elapsedRealtime();
        this.variantPlaylists[variantIndex] = mediaPlaylist;
        this.live |= mediaPlaylist.live;
        this.durationUs = this.live ? -1 : mediaPlaylist.durationUs;
    }

    private static Variant[] buildOrderedVariants(List<Variant> originalVariants, int[] originalVariantIndices) {
        int i;
        ArrayList<Variant> enabledVariantList = new ArrayList();
        if (originalVariantIndices != null) {
            for (int i2 : originalVariantIndices) {
                enabledVariantList.add(originalVariants.get(i2));
            }
        } else {
            enabledVariantList.addAll(originalVariants);
        }
        ArrayList<Variant> definiteVideoVariants = new ArrayList();
        ArrayList<Variant> definiteAudioOnlyVariants = new ArrayList();
        for (i = 0; i < enabledVariantList.size(); i++) {
            Variant variant = (Variant) enabledVariantList.get(i);
            if (variant.format.height > 0 || variantHasExplicitCodecWithPrefix(variant, "avc")) {
                definiteVideoVariants.add(variant);
            } else if (variantHasExplicitCodecWithPrefix(variant, "mp4a")) {
                definiteAudioOnlyVariants.add(variant);
            }
        }
        if (!definiteVideoVariants.isEmpty()) {
            enabledVariantList = definiteVideoVariants;
        } else if (definiteAudioOnlyVariants.size() < enabledVariantList.size()) {
            enabledVariantList.removeAll(definiteAudioOnlyVariants);
        }
        Variant[] enabledVariants = new Variant[enabledVariantList.size()];
        enabledVariantList.toArray(enabledVariants);
        Arrays.sort(enabledVariants, new C06971());
        return enabledVariants;
    }

    private static boolean variantHasExplicitCodecWithPrefix(Variant variant, String prefix) {
        String codecs = variant.format.codecs;
        if (TextUtils.isEmpty(codecs)) {
            return false;
        }
        String[] codecArray = codecs.split("(\\s*,\\s*)|(\\s*$)");
        for (String startsWith : codecArray) {
            if (startsWith.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private boolean allVariantsBlacklisted() {
        for (long j : this.variantBlacklistTimes) {
            if (j == 0) {
                return false;
            }
        }
        return true;
    }

    private void clearStaleBlacklistedVariants() {
        long currentTime = SystemClock.elapsedRealtime();
        int i = 0;
        while (i < this.variantBlacklistTimes.length) {
            if (this.variantBlacklistTimes[i] != 0 && currentTime - this.variantBlacklistTimes[i] > DEFAULT_PLAYLIST_BLACKLIST_MS) {
                this.variantBlacklistTimes[i] = 0;
            }
            i++;
        }
    }

    private int getVariantIndex(Format format) {
        for (int i = 0; i < this.variants.length; i++) {
            if (this.variants[i].format.equals(format)) {
                return i;
            }
        }
        throw new IllegalStateException("Invalid format: " + format);
    }
}
