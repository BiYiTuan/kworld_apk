package com.google.android.exoplayer.hls;

import android.text.TextUtils;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import com.google.android.exoplayer.text.webvtt.WebvttParserUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class WebvttExtractor implements Extractor {
    private static final Pattern LOCAL_TIMESTAMP = Pattern.compile("LOCAL:([^,]+)");
    private static final Pattern MEDIA_TIMESTAMP = Pattern.compile("MPEGTS:(\\d+)");
    private ExtractorOutput output;
    private final PtsTimestampAdjuster ptsTimestampAdjuster;
    private byte[] sampleData = new byte[1024];
    private final ParsableByteArray sampleDataWrapper = new ParsableByteArray();
    private int sampleSize;

    public WebvttExtractor(PtsTimestampAdjuster ptsTimestampAdjuster) {
        this.ptsTimestampAdjuster = ptsTimestampAdjuster;
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        throw new IllegalStateException();
    }

    public void init(ExtractorOutput output) {
        this.output = output;
        output.seekMap(SeekMap.UNSEEKABLE);
    }

    public void seek() {
        throw new IllegalStateException();
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        int currentFileSize = (int) input.getLength();
        if (this.sampleSize == this.sampleData.length) {
            int i;
            byte[] bArr = this.sampleData;
            if (currentFileSize != -1) {
                i = currentFileSize;
            } else {
                i = this.sampleData.length;
            }
            this.sampleData = Arrays.copyOf(bArr, (i * 3) / 2);
        }
        int bytesRead = input.read(this.sampleData, this.sampleSize, this.sampleData.length - this.sampleSize);
        if (bytesRead != -1) {
            this.sampleSize += bytesRead;
            if (currentFileSize == -1 || this.sampleSize != currentFileSize) {
                return 0;
            }
        }
        processSample();
        return -1;
    }

    private void processSample() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.sampleData), "UTF-8"));
        WebvttParserUtil.validateWebvttHeaderLine(bufferedReader);
        long vttTimestampUs = 0;
        long tsTimestampUs = 0;
        while (true) {
            String line = bufferedReader.readLine();
            if (TextUtils.isEmpty(line)) {
                break;
            } else if (line.startsWith("X-TIMESTAMP-MAP")) {
                Matcher localTimestampMatcher = LOCAL_TIMESTAMP.matcher(line);
                if (localTimestampMatcher.find()) {
                    Matcher mediaTimestampMatcher = MEDIA_TIMESTAMP.matcher(line);
                    if (mediaTimestampMatcher.find()) {
                        vttTimestampUs = WebvttParserUtil.parseTimestampUs(localTimestampMatcher.group(1));
                        tsTimestampUs = PtsTimestampAdjuster.ptsToUs(Long.parseLong(mediaTimestampMatcher.group(1)));
                    } else {
                        throw new ParserException("X-TIMESTAMP-MAP doesn't contain media timestamp: " + line);
                    }
                }
                throw new ParserException("X-TIMESTAMP-MAP doesn't contain local timestamp: " + line);
            }
        }
        Matcher cueHeaderMatcher = WebvttParserUtil.findNextCueHeader(bufferedReader);
        if (cueHeaderMatcher == null) {
            buildTrackOutput(0);
            return;
        }
        long firstCueTimeUs = WebvttParserUtil.parseTimestampUs(cueHeaderMatcher.group(1));
        long sampleTimeUs = this.ptsTimestampAdjuster.adjustTimestamp(PtsTimestampAdjuster.usToPts((firstCueTimeUs + tsTimestampUs) - vttTimestampUs));
        TrackOutput trackOutput = buildTrackOutput(sampleTimeUs - firstCueTimeUs);
        this.sampleDataWrapper.reset(this.sampleData, this.sampleSize);
        trackOutput.sampleData(this.sampleDataWrapper, this.sampleSize);
        trackOutput.sampleMetadata(sampleTimeUs, 1, this.sampleSize, 0, null);
    }

    private TrackOutput buildTrackOutput(long subsampleOffsetUs) {
        TrackOutput trackOutput = this.output.track(0);
        trackOutput.format(MediaFormat.createTextFormat(TtmlNode.ATTR_ID, MimeTypes.TEXT_VTT, -1, -1, "en", subsampleOffsetUs));
        this.output.endTracks();
        return trackOutput;
    }
}
