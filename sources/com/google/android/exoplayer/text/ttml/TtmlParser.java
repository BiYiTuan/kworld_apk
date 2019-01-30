package com.google.android.exoplayer.text.ttml;

import android.util.Log;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.text.SubtitleParser;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParserUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public final class TtmlParser implements SubtitleParser {
    private static final String ATTR_BEGIN = "begin";
    private static final String ATTR_DURATION = "dur";
    private static final String ATTR_END = "end";
    private static final String ATTR_STYLE = "style";
    private static final Pattern CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
    private static final int DEFAULT_FRAMERATE = 30;
    private static final int DEFAULT_SUBFRAMERATE = 1;
    private static final int DEFAULT_TICKRATE = 1;
    private static final Pattern FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
    private static final Pattern OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
    private static final String TAG = "TtmlParser";
    private final XmlPullParserFactory xmlParserFactory;

    public TtmlParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    public boolean canParse(String mimeType) {
        return MimeTypes.APPLICATION_TTML.equals(mimeType);
    }

    public Subtitle parse(InputStream inputStream) throws IOException {
        try {
            XmlPullParser xmlParser = this.xmlParserFactory.newPullParser();
            Map<String, TtmlStyle> globalStyles = new HashMap();
            xmlParser.setInput(inputStream, null);
            TtmlSubtitle ttmlSubtitle = null;
            LinkedList<TtmlNode> nodeStack = new LinkedList();
            int unsupportedNodeDepth = 0;
            for (int eventType = xmlParser.getEventType(); eventType != 1; eventType = xmlParser.getEventType()) {
                TtmlNode parent = (TtmlNode) nodeStack.peekLast();
                if (unsupportedNodeDepth == 0) {
                    String name = xmlParser.getName();
                    if (eventType == 2) {
                        if (!isSupportedTag(name)) {
                            Log.i(TAG, "Ignoring unsupported tag: " + xmlParser.getName());
                            unsupportedNodeDepth++;
                        } else if (TtmlNode.TAG_HEAD.equals(name)) {
                            parseHeader(xmlParser, globalStyles);
                        } else {
                            try {
                                TtmlNode node = parseNode(xmlParser, parent);
                                nodeStack.addLast(node);
                                if (parent != null) {
                                    parent.addChild(node);
                                }
                            } catch (ParserException e) {
                                Log.w(TAG, "Suppressing parser error", e);
                                unsupportedNodeDepth++;
                            }
                        }
                    } else if (eventType == 4) {
                        parent.addChild(TtmlNode.buildTextNode(xmlParser.getText()));
                    } else if (eventType == 3) {
                        if (xmlParser.getName().equals(TtmlNode.TAG_TT)) {
                            ttmlSubtitle = new TtmlSubtitle((TtmlNode) nodeStack.getLast(), globalStyles);
                        }
                        nodeStack.removeLast();
                    }
                } else if (eventType == 2) {
                    unsupportedNodeDepth++;
                } else if (eventType == 3) {
                    unsupportedNodeDepth--;
                }
                xmlParser.next();
            }
            return ttmlSubtitle;
        } catch (XmlPullParserException xppe) {
            throw new ParserException("Unable to parse source", xppe);
        }
    }

    private Map<String, TtmlStyle> parseHeader(XmlPullParser xmlParser, Map<String, TtmlStyle> globalStyles) throws IOException, XmlPullParserException {
        do {
            xmlParser.next();
            if (ParserUtil.isStartTag(xmlParser, "style")) {
                String parentStyleId = xmlParser.getAttributeValue(null, "style");
                TtmlStyle style = parseStyleAttributes(xmlParser, new TtmlStyle());
                if (parentStyleId != null) {
                    String[] ids = parseStyleIds(parentStyleId);
                    for (Object obj : ids) {
                        style.chain((TtmlStyle) globalStyles.get(obj));
                    }
                }
                if (style.getId() != null) {
                    globalStyles.put(style.getId(), style);
                }
            }
        } while (!ParserUtil.isEndTag(xmlParser, TtmlNode.TAG_HEAD));
        return globalStyles;
    }

    private String[] parseStyleIds(String parentStyleIds) {
        return parentStyleIds.split("\\s+");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.google.android.exoplayer.text.ttml.TtmlStyle parseStyleAttributes(org.xmlpull.v1.XmlPullParser r14, com.google.android.exoplayer.text.ttml.TtmlStyle r15) {
        /*
        r13 = this;
        r10 = 3;
        r9 = 2;
        r7 = -1;
        r8 = 1;
        r6 = 0;
        r0 = r14.getAttributeCount();
        r4 = 0;
    L_0x000a:
        if (r4 >= r0) goto L_0x0225;
    L_0x000c:
        r1 = r14.getAttributeName(r4);
        r2 = r14.getAttributeValue(r4);
        r5 = com.google.android.exoplayer.util.ParserUtil.removeNamespacePrefix(r1);
        r11 = r5.hashCode();
        switch(r11) {
            case -1550943582: goto L_0x0062;
            case -1224696685: goto L_0x0044;
            case -1065511464: goto L_0x006c;
            case -879295043: goto L_0x0076;
            case -734428249: goto L_0x0058;
            case 3355: goto L_0x0026;
            case 94842723: goto L_0x003a;
            case 365601008: goto L_0x004e;
            case 1287124693: goto L_0x0030;
            default: goto L_0x001f;
        };
    L_0x001f:
        r5 = r7;
    L_0x0020:
        switch(r5) {
            case 0: goto L_0x0081;
            case 1: goto L_0x0096;
            case 2: goto L_0x00c3;
            case 3: goto L_0x00f1;
            case 4: goto L_0x00fb;
            case 5: goto L_0x0125;
            case 6: goto L_0x0135;
            case 7: goto L_0x0145;
            case 8: goto L_0x01c4;
            default: goto L_0x0023;
        };
    L_0x0023:
        r4 = r4 + 1;
        goto L_0x000a;
    L_0x0026:
        r11 = "id";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x001f;
    L_0x002e:
        r5 = r6;
        goto L_0x0020;
    L_0x0030:
        r11 = "backgroundColor";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x001f;
    L_0x0038:
        r5 = r8;
        goto L_0x0020;
    L_0x003a:
        r11 = "color";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x001f;
    L_0x0042:
        r5 = r9;
        goto L_0x0020;
    L_0x0044:
        r11 = "fontFamily";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x001f;
    L_0x004c:
        r5 = r10;
        goto L_0x0020;
    L_0x004e:
        r11 = "fontSize";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x001f;
    L_0x0056:
        r5 = 4;
        goto L_0x0020;
    L_0x0058:
        r11 = "fontWeight";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x001f;
    L_0x0060:
        r5 = 5;
        goto L_0x0020;
    L_0x0062:
        r11 = "fontStyle";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x001f;
    L_0x006a:
        r5 = 6;
        goto L_0x0020;
    L_0x006c:
        r11 = "textAlign";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x001f;
    L_0x0074:
        r5 = 7;
        goto L_0x0020;
    L_0x0076:
        r11 = "textDecoration";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x001f;
    L_0x007e:
        r5 = 8;
        goto L_0x0020;
    L_0x0081:
        r5 = "style";
        r11 = r14.getName();
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x0023;
    L_0x008d:
        r5 = r13.createIfNull(r15);
        r15 = r5.setId(r2);
        goto L_0x0023;
    L_0x0096:
        r15 = r13.createIfNull(r15);
        r5 = com.google.android.exoplayer.text.ttml.TtmlColorParser.parseColor(r2);	 Catch:{ IllegalArgumentException -> 0x00a2 }
        r15.setBackgroundColor(r5);	 Catch:{ IllegalArgumentException -> 0x00a2 }
        goto L_0x0023;
    L_0x00a2:
        r3 = move-exception;
        r5 = "TtmlParser";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "failed parsing background value: '";
        r11 = r11.append(r12);
        r11 = r11.append(r2);
        r12 = "'";
        r11 = r11.append(r12);
        r11 = r11.toString();
        android.util.Log.w(r5, r11);
        goto L_0x0023;
    L_0x00c3:
        r15 = r13.createIfNull(r15);
        r5 = com.google.android.exoplayer.text.ttml.TtmlColorParser.parseColor(r2);	 Catch:{ IllegalArgumentException -> 0x00d0 }
        r15.setColor(r5);	 Catch:{ IllegalArgumentException -> 0x00d0 }
        goto L_0x0023;
    L_0x00d0:
        r3 = move-exception;
        r5 = "TtmlParser";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "failed parsing color value: '";
        r11 = r11.append(r12);
        r11 = r11.append(r2);
        r12 = "'";
        r11 = r11.append(r12);
        r11 = r11.toString();
        android.util.Log.w(r5, r11);
        goto L_0x0023;
    L_0x00f1:
        r5 = r13.createIfNull(r15);
        r15 = r5.setFontFamily(r2);
        goto L_0x0023;
    L_0x00fb:
        r15 = r13.createIfNull(r15);	 Catch:{ ParserException -> 0x0104 }
        parseFontSize(r2, r15);	 Catch:{ ParserException -> 0x0104 }
        goto L_0x0023;
    L_0x0104:
        r3 = move-exception;
        r5 = "TtmlParser";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "failed parsing fontSize value: '";
        r11 = r11.append(r12);
        r11 = r11.append(r2);
        r12 = "'";
        r11 = r11.append(r12);
        r11 = r11.toString();
        android.util.Log.w(r5, r11);
        goto L_0x0023;
    L_0x0125:
        r5 = r13.createIfNull(r15);
        r11 = "bold";
        r11 = r11.equalsIgnoreCase(r2);
        r15 = r5.setBold(r11);
        goto L_0x0023;
    L_0x0135:
        r5 = r13.createIfNull(r15);
        r11 = "italic";
        r11 = r11.equalsIgnoreCase(r2);
        r15 = r5.setItalic(r11);
        goto L_0x0023;
    L_0x0145:
        r5 = com.google.android.exoplayer.util.Util.toLowerInvariant(r2);
        r11 = r5.hashCode();
        switch(r11) {
            case -1364013995: goto L_0x018a;
            case 100571: goto L_0x0180;
            case 3317767: goto L_0x0162;
            case 108511772: goto L_0x0176;
            case 109757538: goto L_0x016c;
            default: goto L_0x0150;
        };
    L_0x0150:
        r5 = r7;
    L_0x0151:
        switch(r5) {
            case 0: goto L_0x0156;
            case 1: goto L_0x0194;
            case 2: goto L_0x01a0;
            case 3: goto L_0x01ac;
            case 4: goto L_0x01b8;
            default: goto L_0x0154;
        };
    L_0x0154:
        goto L_0x0023;
    L_0x0156:
        r5 = r13.createIfNull(r15);
        r11 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r15 = r5.setTextAlign(r11);
        goto L_0x0023;
    L_0x0162:
        r11 = "left";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x0150;
    L_0x016a:
        r5 = r6;
        goto L_0x0151;
    L_0x016c:
        r11 = "start";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x0150;
    L_0x0174:
        r5 = r8;
        goto L_0x0151;
    L_0x0176:
        r11 = "right";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x0150;
    L_0x017e:
        r5 = r9;
        goto L_0x0151;
    L_0x0180:
        r11 = "end";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x0150;
    L_0x0188:
        r5 = r10;
        goto L_0x0151;
    L_0x018a:
        r11 = "center";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x0150;
    L_0x0192:
        r5 = 4;
        goto L_0x0151;
    L_0x0194:
        r5 = r13.createIfNull(r15);
        r11 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r15 = r5.setTextAlign(r11);
        goto L_0x0023;
    L_0x01a0:
        r5 = r13.createIfNull(r15);
        r11 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        r15 = r5.setTextAlign(r11);
        goto L_0x0023;
    L_0x01ac:
        r5 = r13.createIfNull(r15);
        r11 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        r15 = r5.setTextAlign(r11);
        goto L_0x0023;
    L_0x01b8:
        r5 = r13.createIfNull(r15);
        r11 = android.text.Layout.Alignment.ALIGN_CENTER;
        r15 = r5.setTextAlign(r11);
        goto L_0x0023;
    L_0x01c4:
        r5 = com.google.android.exoplayer.util.Util.toLowerInvariant(r2);
        r11 = r5.hashCode();
        switch(r11) {
            case -1461280213: goto L_0x01fd;
            case -1026963764: goto L_0x01f3;
            case 913457136: goto L_0x01e9;
            case 1679736913: goto L_0x01df;
            default: goto L_0x01cf;
        };
    L_0x01cf:
        r5 = r7;
    L_0x01d0:
        switch(r5) {
            case 0: goto L_0x01d5;
            case 1: goto L_0x0207;
            case 2: goto L_0x0211;
            case 3: goto L_0x021b;
            default: goto L_0x01d3;
        };
    L_0x01d3:
        goto L_0x0023;
    L_0x01d5:
        r5 = r13.createIfNull(r15);
        r15 = r5.setLinethrough(r8);
        goto L_0x0023;
    L_0x01df:
        r11 = "linethrough";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x01cf;
    L_0x01e7:
        r5 = r6;
        goto L_0x01d0;
    L_0x01e9:
        r11 = "nolinethrough";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x01cf;
    L_0x01f1:
        r5 = r8;
        goto L_0x01d0;
    L_0x01f3:
        r11 = "underline";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x01cf;
    L_0x01fb:
        r5 = r9;
        goto L_0x01d0;
    L_0x01fd:
        r11 = "nounderline";
        r5 = r5.equals(r11);
        if (r5 == 0) goto L_0x01cf;
    L_0x0205:
        r5 = r10;
        goto L_0x01d0;
    L_0x0207:
        r5 = r13.createIfNull(r15);
        r15 = r5.setLinethrough(r6);
        goto L_0x0023;
    L_0x0211:
        r5 = r13.createIfNull(r15);
        r15 = r5.setUnderline(r8);
        goto L_0x0023;
    L_0x021b:
        r5 = r13.createIfNull(r15);
        r15 = r5.setUnderline(r6);
        goto L_0x0023;
    L_0x0225:
        return r15;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.text.ttml.TtmlParser.parseStyleAttributes(org.xmlpull.v1.XmlPullParser, com.google.android.exoplayer.text.ttml.TtmlStyle):com.google.android.exoplayer.text.ttml.TtmlStyle");
    }

    private TtmlStyle createIfNull(TtmlStyle style) {
        return style == null ? new TtmlStyle() : style;
    }

    private TtmlNode parseNode(XmlPullParser parser, TtmlNode parent) throws ParserException {
        long duration = 0;
        long startTime = -1;
        long endTime = -1;
        String[] styleIds = null;
        int attributeCount = parser.getAttributeCount();
        TtmlStyle style = parseStyleAttributes(parser, null);
        for (int i = 0; i < attributeCount; i++) {
            String attr = ParserUtil.removeNamespacePrefix(parser.getAttributeName(i));
            String value = parser.getAttributeValue(i);
            if (attr.equals(ATTR_BEGIN)) {
                startTime = parseTimeExpression(value, 30, 1, 1);
            } else if (attr.equals("end")) {
                endTime = parseTimeExpression(value, 30, 1, 1);
            } else if (attr.equals(ATTR_DURATION)) {
                duration = parseTimeExpression(value, 30, 1, 1);
            } else if (attr.equals("style")) {
                String[] ids = parseStyleIds(value);
                if (ids.length > 0) {
                    styleIds = ids;
                }
            }
        }
        if (!(parent == null || parent.startTimeUs == -1)) {
            if (startTime != -1) {
                startTime += parent.startTimeUs;
            }
            if (endTime != -1) {
                endTime += parent.startTimeUs;
            }
        }
        if (endTime == -1) {
            if (duration > 0) {
                endTime = startTime + duration;
            } else if (!(parent == null || parent.endTimeUs == -1)) {
                endTime = parent.endTimeUs;
            }
        }
        return TtmlNode.buildNode(parser.getName(), startTime, endTime, style, styleIds);
    }

    private static boolean isSupportedTag(String tag) {
        if (tag.equals(TtmlNode.TAG_TT) || tag.equals(TtmlNode.TAG_HEAD) || tag.equals(TtmlNode.TAG_BODY) || tag.equals(TtmlNode.TAG_DIV) || tag.equals(TtmlNode.TAG_P) || tag.equals(TtmlNode.TAG_SPAN) || tag.equals(TtmlNode.TAG_BR) || tag.equals("style") || tag.equals(TtmlNode.TAG_STYLING) || tag.equals(TtmlNode.TAG_LAYOUT) || tag.equals(TtmlNode.TAG_REGION) || tag.equals(TtmlNode.TAG_METADATA) || tag.equals(TtmlNode.TAG_SMPTE_IMAGE) || tag.equals(TtmlNode.TAG_SMPTE_DATA) || tag.equals(TtmlNode.TAG_SMPTE_INFORMATION)) {
            return true;
        }
        return false;
    }

    private static void parseFontSize(String expression, TtmlStyle out) throws ParserException {
        Matcher matcher;
        String[] expressions = expression.split("\\s+");
        if (expressions.length == 1) {
            matcher = FONT_SIZE.matcher(expression);
        } else if (expressions.length == 2) {
            matcher = FONT_SIZE.matcher(expressions[1]);
            Log.w(TAG, "multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
        } else {
            throw new ParserException();
        }
        if (matcher.matches()) {
            String unit = matcher.group(3);
            short s = (short) -1;
            switch (unit.hashCode()) {
                case 37:
                    if (unit.equals("%")) {
                        s = (short) 2;
                        break;
                    }
                    break;
                case 3240:
                    if (unit.equals("em")) {
                        s = (short) 1;
                        break;
                    }
                    break;
                case 3592:
                    if (unit.equals("px")) {
                        s = (short) 0;
                        break;
                    }
                    break;
            }
            switch (s) {
                case (short) 0:
                    out.setFontSizeUnit((short) 1);
                    break;
                case (short) 1:
                    out.setFontSizeUnit((short) 2);
                    break;
                case (short) 2:
                    out.setFontSizeUnit((short) 3);
                    break;
                default:
                    throw new ParserException();
            }
            out.setFontSize(Float.valueOf(matcher.group(1)).floatValue());
            return;
        }
        throw new ParserException();
    }

    private static long parseTimeExpression(String time, int frameRate, int subframeRate, int tickRate) throws ParserException {
        Matcher matcher = CLOCK_TIME.matcher(time);
        if (matcher.matches()) {
            double durationSeconds = (((double) (Long.parseLong(matcher.group(1)) * 3600)) + ((double) (Long.parseLong(matcher.group(2)) * 60))) + ((double) Long.parseLong(matcher.group(3)));
            String fraction = matcher.group(4);
            durationSeconds += fraction != null ? Double.parseDouble(fraction) : 0.0d;
            String frames = matcher.group(5);
            durationSeconds += frames != null ? ((double) Long.parseLong(frames)) / ((double) frameRate) : 0.0d;
            String subframes = matcher.group(6);
            return (long) (1000000.0d * (durationSeconds + (subframes != null ? (((double) Long.parseLong(subframes)) / ((double) subframeRate)) / ((double) frameRate) : 0.0d)));
        }
        matcher = OFFSET_TIME.matcher(time);
        if (matcher.matches()) {
            double offsetSeconds = Double.parseDouble(matcher.group(1));
            String unit = matcher.group(2);
            if (unit.equals("h")) {
                offsetSeconds *= 3600.0d;
            } else if (unit.equals("m")) {
                offsetSeconds *= 60.0d;
            } else if (!unit.equals("s")) {
                if (unit.equals("ms")) {
                    offsetSeconds /= 1000.0d;
                } else if (unit.equals("f")) {
                    offsetSeconds /= (double) frameRate;
                } else if (unit.equals("t")) {
                    offsetSeconds /= (double) tickRate;
                }
            }
            return (long) (1000000.0d * offsetSeconds);
        }
        throw new ParserException("Malformed time expression: " + time);
    }
}
