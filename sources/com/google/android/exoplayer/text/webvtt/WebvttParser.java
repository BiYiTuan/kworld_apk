package com.google.android.exoplayer.text.webvtt;

import android.text.Layout.Alignment;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.util.MimeTypes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebvttParser implements SubtitleParser {
    private static final Pattern CUE_SETTING = Pattern.compile("(\\S+?):(\\S+)");
    private static final String TAG = "WebvttParser";
    private final WebvttCueParser cueParser = new WebvttCueParser();
    private final PositionHolder positionHolder = new PositionHolder();
    private final StringBuilder textBuilder = new StringBuilder();

    /* renamed from: com.google.android.exoplayer.text.webvtt.WebvttParser$1 */
    static /* synthetic */ class C07031 {
        static final /* synthetic */ int[] $SwitchMap$android$text$Layout$Alignment = new int[Alignment.values().length];

        static {
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$text$Layout$Alignment[Alignment.ALIGN_OPPOSITE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private static final class PositionHolder {
        public int lineType;
        public float position;
        public int positionAnchor;

        private PositionHolder() {
        }
    }

    public final boolean canParse(String mimeType) {
        return MimeTypes.TEXT_VTT.equals(mimeType);
    }

    public final WebvttSubtitle parse(InputStream inputStream) throws IOException {
        BufferedReader webvttData = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        WebvttParserUtil.validateWebvttHeaderLine(webvttData);
        do {
        } while (!TextUtils.isEmpty(webvttData.readLine()));
        ArrayList<WebvttCue> subtitles = new ArrayList();
        while (true) {
            Matcher cueHeaderMatcher = WebvttParserUtil.findNextCueHeader(webvttData);
            if (cueHeaderMatcher == null) {
                return new WebvttSubtitle(subtitles);
            }
            try {
                long cueStartTime = WebvttParserUtil.parseTimestampUs(cueHeaderMatcher.group(1));
                long cueEndTime = WebvttParserUtil.parseTimestampUs(cueHeaderMatcher.group(2));
                Alignment cueTextAlignment = null;
                float cueLine = Float.MIN_VALUE;
                int cueLineType = Integer.MIN_VALUE;
                int cueLineAnchor = Integer.MIN_VALUE;
                float cuePosition = Float.MIN_VALUE;
                int cuePositionAnchor = Integer.MIN_VALUE;
                float cueWidth = Float.MIN_VALUE;
                Matcher cueSettingMatcher = CUE_SETTING.matcher(cueHeaderMatcher.group(3));
                while (cueSettingMatcher.find()) {
                    String name = cueSettingMatcher.group(1);
                    String value = cueSettingMatcher.group(2);
                    try {
                        if ("line".equals(name)) {
                            parseLineAttribute(value, this.positionHolder);
                            cueLine = this.positionHolder.position;
                            cueLineType = this.positionHolder.lineType;
                            cueLineAnchor = this.positionHolder.positionAnchor;
                        } else if ("align".equals(name)) {
                            cueTextAlignment = parseTextAlignment(value);
                        } else if ("position".equals(name)) {
                            parsePositionAttribute(value, this.positionHolder);
                            cuePosition = this.positionHolder.position;
                            cuePositionAnchor = this.positionHolder.positionAnchor;
                        } else if ("size".equals(name)) {
                            cueWidth = parsePercentage(value);
                        } else {
                            Log.w(TAG, "Unknown cue setting " + name + ":" + value);
                        }
                    } catch (NumberFormatException e) {
                        Log.w(TAG, "Skipping bad cue setting: " + cueSettingMatcher.group());
                    }
                }
                if (cuePosition != Float.MIN_VALUE && cuePositionAnchor == Integer.MIN_VALUE) {
                    cuePositionAnchor = alignmentToAnchor(cueTextAlignment);
                }
                this.textBuilder.setLength(0);
                while (true) {
                    String line = webvttData.readLine();
                    if (line == null || line.isEmpty()) {
                        subtitles.add(new WebvttCue(cueStartTime, cueEndTime, this.cueParser.parse(this.textBuilder.toString()), cueTextAlignment, cueLine, cueLineType, cueLineAnchor, cuePosition, cuePositionAnchor, cueWidth));
                    } else {
                        if (this.textBuilder.length() > 0) {
                            this.textBuilder.append("\n");
                        }
                        this.textBuilder.append(line.trim());
                    }
                }
                subtitles.add(new WebvttCue(cueStartTime, cueEndTime, this.cueParser.parse(this.textBuilder.toString()), cueTextAlignment, cueLine, cueLineType, cueLineAnchor, cuePosition, cuePositionAnchor, cueWidth));
            } catch (NumberFormatException e2) {
                Log.w(TAG, "Skipping cue with bad header: " + cueHeaderMatcher.group());
            }
        }
    }

    private static void parseLineAttribute(String s, PositionHolder out) throws NumberFormatException {
        int lineAnchor;
        float line;
        int lineType;
        int commaPosition = s.indexOf(",");
        if (commaPosition != -1) {
            lineAnchor = parsePositionAnchor(s.substring(commaPosition + 1));
            s = s.substring(0, commaPosition);
        } else {
            lineAnchor = Integer.MIN_VALUE;
        }
        if (s.endsWith("%")) {
            line = parsePercentage(s);
            lineType = 0;
        } else {
            line = (float) Integer.parseInt(s);
            lineType = 1;
        }
        out.position = line;
        out.positionAnchor = lineAnchor;
        out.lineType = lineType;
    }

    private static void parsePositionAttribute(String s, PositionHolder out) throws NumberFormatException {
        int positionAnchor;
        int commaPosition = s.indexOf(",");
        if (commaPosition != -1) {
            positionAnchor = parsePositionAnchor(s.substring(commaPosition + 1));
            s = s.substring(0, commaPosition);
        } else {
            positionAnchor = Integer.MIN_VALUE;
        }
        out.position = parsePercentage(s);
        out.positionAnchor = positionAnchor;
        out.lineType = Integer.MIN_VALUE;
    }

    private static float parsePercentage(String s) throws NumberFormatException {
        if (s.endsWith("%")) {
            return Float.parseFloat(s.substring(0, s.length() - 1)) / 100.0f;
        }
        throw new NumberFormatException("Percentages must end with %");
    }

    private static int parsePositionAnchor(String s) {
        int i = -1;
        switch (s.hashCode()) {
            case -1074341483:
                if (s.equals("middle")) {
                    i = 1;
                    break;
                }
                break;
            case 100571:
                if (s.equals(TtmlNode.END)) {
                    i = 2;
                    break;
                }
                break;
            case 109757538:
                if (s.equals(TtmlNode.START)) {
                    i = 0;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                Log.w(TAG, "Invalid anchor value: " + s);
                return Integer.MIN_VALUE;
        }
    }

    private static Alignment parseTextAlignment(String s) {
        Object obj = -1;
        switch (s.hashCode()) {
            case -1074341483:
                if (s.equals("middle")) {
                    obj = 2;
                    break;
                }
                break;
            case 100571:
                if (s.equals(TtmlNode.END)) {
                    obj = 3;
                    break;
                }
                break;
            case 3317767:
                if (s.equals(TtmlNode.LEFT)) {
                    obj = 1;
                    break;
                }
                break;
            case 108511772:
                if (s.equals(TtmlNode.RIGHT)) {
                    obj = 4;
                    break;
                }
                break;
            case 109757538:
                if (s.equals(TtmlNode.START)) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
            case 1:
                return Alignment.ALIGN_NORMAL;
            case 2:
                return Alignment.ALIGN_CENTER;
            case 3:
            case 4:
                return Alignment.ALIGN_OPPOSITE;
            default:
                Log.w(TAG, "Invalid alignment value: " + s);
                return null;
        }
    }

    private static int alignmentToAnchor(Alignment alignment) {
        if (alignment == null) {
            return Integer.MIN_VALUE;
        }
        switch (C07031.$SwitchMap$android$text$Layout$Alignment[alignment.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                Log.w(TAG, "Unrecognized alignment: " + alignment);
                return 0;
        }
    }
}
