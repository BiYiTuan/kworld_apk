package com.google.android.exoplayer.text.ttml;

import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan.Standard;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import java.util.Map;

final class TtmlRenderUtil {
    private static final StrikethroughSpan STRIKETHROUGH_SPAN = new StrikethroughSpan();
    private static final StyleSpan[] STYLE_SPANS = new StyleSpan[]{new StyleSpan(0), new StyleSpan(1), new StyleSpan(2), new StyleSpan(3)};
    private static final UnderlineSpan UNDERLINE_SPAN = new UnderlineSpan();

    public static TtmlStyle resolveStyle(TtmlStyle style, String[] styleIds, Map<String, TtmlStyle> globalStyles) {
        if (style == null && styleIds == null) {
            return null;
        }
        if (style == null && styleIds.length == 1) {
            return (TtmlStyle) globalStyles.get(styleIds[0]);
        }
        if (style == null && styleIds.length > 1) {
            TtmlStyle chainedStyle = new TtmlStyle();
            for (Object obj : styleIds) {
                chainedStyle.chain((TtmlStyle) globalStyles.get(obj));
            }
            return chainedStyle;
        } else if (style != null && styleIds != null && styleIds.length == 1) {
            return style.chain((TtmlStyle) globalStyles.get(styleIds[0]));
        } else {
            if (style == null || styleIds == null || styleIds.length <= 1) {
                return style;
            }
            for (Object obj2 : styleIds) {
                style.chain((TtmlStyle) globalStyles.get(obj2));
            }
            return style;
        }
    }

    public static void applyStylesToSpan(SpannableStringBuilder builder, int start, int end, TtmlStyle style) {
        if (style.getStyle() != (short) -1) {
            builder.setSpan(STYLE_SPANS[style.getStyle()], start, end, 33);
        }
        if (style.isLinethrough()) {
            builder.setSpan(STRIKETHROUGH_SPAN, start, end, 33);
        }
        if (style.isUnderline()) {
            builder.setSpan(UNDERLINE_SPAN, start, end, 33);
        }
        if (style.hasColorSpecified()) {
            builder.setSpan(new ForegroundColorSpan(style.getColor()), start, end, 33);
        }
        if (style.hasBackgroundColorSpecified()) {
            builder.setSpan(new BackgroundColorSpan(style.getBackgroundColor()), start, end, 33);
        }
        if (style.getFontFamily() != null) {
            builder.setSpan(new TypefaceSpan(style.getFontFamily()), start, end, 33);
        }
        if (style.getTextAlign() != null) {
            builder.setSpan(new Standard(style.getTextAlign()), start, end, 33);
        }
        if (style.getFontSizeUnit() != (short) -1) {
            switch (style.getFontSizeUnit()) {
                case (short) 1:
                    builder.setSpan(new AbsoluteSizeSpan((int) style.getFontSize(), true), start, end, 33);
                    return;
                case (short) 2:
                    builder.setSpan(new RelativeSizeSpan(style.getFontSize()), start, end, 33);
                    return;
                case (short) 3:
                    builder.setSpan(new RelativeSizeSpan(style.getFontSize() / 100.0f), start, end, 33);
                    return;
                default:
                    return;
            }
        }
    }

    static void endParagraph(SpannableStringBuilder builder) {
        int position = builder.length() - 1;
        while (position >= 0 && builder.charAt(position) == ' ') {
            position--;
        }
        if (position >= 0 && builder.charAt(position) != '\n') {
            builder.append('\n');
        }
    }

    static String applyTextElementSpacePolicy(String in) {
        return in.replaceAll("\r\n", "\n").replaceAll(" *\n *", "\n").replaceAll("\n", " ").replaceAll("[ \t\\x0B\f\r]+", " ");
    }

    private TtmlRenderUtil() {
    }
}
