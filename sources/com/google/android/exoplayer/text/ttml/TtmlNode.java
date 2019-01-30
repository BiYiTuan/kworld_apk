package com.google.android.exoplayer.text.ttml;

import android.text.SpannableStringBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

final class TtmlNode {
    public static final String ATTR_ID = "id";
    public static final String ATTR_TTS_BACKGROUND_COLOR = "backgroundColor";
    public static final String ATTR_TTS_COLOR = "color";
    public static final String ATTR_TTS_FONT_FAMILY = "fontFamily";
    public static final String ATTR_TTS_FONT_SIZE = "fontSize";
    public static final String ATTR_TTS_FONT_STYLE = "fontStyle";
    public static final String ATTR_TTS_FONT_WEIGHT = "fontWeight";
    public static final String ATTR_TTS_TEXT_ALIGN = "textAlign";
    public static final String ATTR_TTS_TEXT_DECORATION = "textDecoration";
    public static final String BOLD = "bold";
    public static final String CENTER = "center";
    public static final String END = "end";
    public static final String ITALIC = "italic";
    public static final String LEFT = "left";
    public static final String LINETHROUGH = "linethrough";
    public static final String NO_LINETHROUGH = "nolinethrough";
    public static final String NO_UNDERLINE = "nounderline";
    public static final String RIGHT = "right";
    public static final String START = "start";
    public static final String TAG_BODY = "body";
    public static final String TAG_BR = "br";
    public static final String TAG_DIV = "div";
    public static final String TAG_HEAD = "head";
    public static final String TAG_LAYOUT = "layout";
    public static final String TAG_METADATA = "metadata";
    public static final String TAG_P = "p";
    public static final String TAG_REGION = "region";
    public static final String TAG_SMPTE_DATA = "smpte:data";
    public static final String TAG_SMPTE_IMAGE = "smpte:image";
    public static final String TAG_SMPTE_INFORMATION = "smpte:information";
    public static final String TAG_SPAN = "span";
    public static final String TAG_STYLE = "style";
    public static final String TAG_STYLING = "styling";
    public static final String TAG_TT = "tt";
    public static final long UNDEFINED_TIME = -1;
    public static final String UNDERLINE = "underline";
    private List<TtmlNode> children;
    private int end;
    public final long endTimeUs;
    public final boolean isTextNode;
    private int start;
    public final long startTimeUs;
    public final TtmlStyle style;
    private String[] styleIds;
    public final String tag;
    public final String text;

    public static TtmlNode buildTextNode(String text) {
        return new TtmlNode(null, TtmlRenderUtil.applyTextElementSpacePolicy(text), -1, -1, null, null);
    }

    public static TtmlNode buildNode(String tag, long startTimeUs, long endTimeUs, TtmlStyle style, String[] styleIds) {
        return new TtmlNode(tag, null, startTimeUs, endTimeUs, style, styleIds);
    }

    private TtmlNode(String tag, String text, long startTimeUs, long endTimeUs, TtmlStyle style, String[] styleIds) {
        this.tag = tag;
        this.text = text;
        this.style = style;
        this.styleIds = styleIds;
        this.isTextNode = text != null;
        this.startTimeUs = startTimeUs;
        this.endTimeUs = endTimeUs;
    }

    public boolean isActive(long timeUs) {
        return (this.startTimeUs == -1 && this.endTimeUs == -1) || ((this.startTimeUs <= timeUs && this.endTimeUs == -1) || ((this.startTimeUs == -1 && timeUs < this.endTimeUs) || (this.startTimeUs <= timeUs && timeUs < this.endTimeUs)));
    }

    public void addChild(TtmlNode child) {
        if (this.children == null) {
            this.children = new ArrayList();
        }
        this.children.add(child);
    }

    public TtmlNode getChild(int index) {
        if (this.children != null) {
            return (TtmlNode) this.children.get(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public int getChildCount() {
        return this.children == null ? 0 : this.children.size();
    }

    public long[] getEventTimesUs() {
        TreeSet<Long> eventTimeSet = new TreeSet();
        getEventTimes(eventTimeSet, false);
        long[] eventTimes = new long[eventTimeSet.size()];
        Iterator<Long> eventTimeIterator = eventTimeSet.iterator();
        int i = 0;
        while (eventTimeIterator.hasNext()) {
            int i2 = i + 1;
            eventTimes[i] = ((Long) eventTimeIterator.next()).longValue();
            i = i2;
        }
        return eventTimes;
    }

    private void getEventTimes(TreeSet<Long> out, boolean descendsPNode) {
        boolean isPNode = TAG_P.equals(this.tag);
        if (descendsPNode || isPNode) {
            if (this.startTimeUs != -1) {
                out.add(Long.valueOf(this.startTimeUs));
            }
            if (this.endTimeUs != -1) {
                out.add(Long.valueOf(this.endTimeUs));
            }
        }
        if (this.children != null) {
            for (int i = 0; i < this.children.size(); i++) {
                TtmlNode ttmlNode = (TtmlNode) this.children.get(i);
                boolean z = descendsPNode || isPNode;
                ttmlNode.getEventTimes(out, z);
            }
        }
    }

    public String[] getStyleIds() {
        return this.styleIds;
    }

    public CharSequence getText(long timeUs, Map<String, TtmlStyle> globalStyles) {
        int i;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        traverseForText(timeUs, builder, false);
        traverseForStyle(builder, globalStyles);
        int builderLength = builder.length();
        for (i = 0; i < builderLength; i++) {
            if (builder.charAt(i) == ' ') {
                int j = i + 1;
                while (j < builder.length() && builder.charAt(j) == ' ') {
                    j++;
                }
                int spacesToDelete = j - (i + 1);
                if (spacesToDelete > 0) {
                    builder.delete(i, i + spacesToDelete);
                    builderLength -= spacesToDelete;
                }
            }
        }
        if (builderLength > 0 && builder.charAt(0) == ' ') {
            builder.delete(0, 1);
            builderLength--;
        }
        i = 0;
        while (i < builderLength - 1) {
            if (builder.charAt(i) == '\n' && builder.charAt(i + 1) == ' ') {
                builder.delete(i + 1, i + 2);
                builderLength--;
            }
            i++;
        }
        if (builderLength > 0 && builder.charAt(builderLength - 1) == ' ') {
            builder.delete(builderLength - 1, builderLength);
            builderLength--;
        }
        i = 0;
        while (i < builderLength - 1) {
            if (builder.charAt(i) == ' ' && builder.charAt(i + 1) == '\n') {
                builder.delete(i, i + 1);
                builderLength--;
            }
            i++;
        }
        if (builderLength > 0 && builder.charAt(builderLength - 1) == '\n') {
            builder.delete(builderLength - 1, builderLength);
        }
        return builder;
    }

    private SpannableStringBuilder traverseForText(long timeUs, SpannableStringBuilder builder, boolean descendsPNode) {
        this.start = builder.length();
        this.end = this.start;
        if (this.isTextNode && descendsPNode) {
            builder.append(this.text);
        } else if (TAG_BR.equals(this.tag) && descendsPNode) {
            builder.append('\n');
        } else if (!TAG_METADATA.equals(this.tag) && isActive(timeUs)) {
            boolean isPNode = TAG_P.equals(this.tag);
            for (int i = 0; i < getChildCount(); i++) {
                TtmlNode child = getChild(i);
                boolean z = descendsPNode || isPNode;
                child.traverseForText(timeUs, builder, z);
            }
            if (isPNode) {
                TtmlRenderUtil.endParagraph(builder);
            }
            this.end = builder.length();
        }
        return builder;
    }

    private void traverseForStyle(SpannableStringBuilder builder, Map<String, TtmlStyle> globalStyles) {
        if (this.start != this.end) {
            TtmlStyle resolvedStyle = TtmlRenderUtil.resolveStyle(this.style, this.styleIds, globalStyles);
            if (resolvedStyle != null) {
                TtmlRenderUtil.applyStylesToSpan(builder, this.start, this.end, resolvedStyle);
            }
            for (int i = 0; i < getChildCount(); i++) {
                getChild(i).traverseForStyle(builder, globalStyles);
            }
        }
    }
}
