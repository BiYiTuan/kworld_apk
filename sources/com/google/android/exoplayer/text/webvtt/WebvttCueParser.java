package com.google.android.exoplayer.text.webvtt;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import java.util.Stack;

final class WebvttCueParser {
    private static final char CHAR_AMPERSAND = '&';
    private static final char CHAR_GREATER_THAN = '>';
    private static final char CHAR_LESS_THAN = '<';
    private static final char CHAR_SEMI_COLON = ';';
    private static final char CHAR_SLASH = '/';
    private static final char CHAR_SPACE = ' ';
    private static final String ENTITY_AMPERSAND = "amp";
    private static final String ENTITY_GREATER_THAN = "gt";
    private static final String ENTITY_LESS_THAN = "lt";
    private static final String ENTITY_NON_BREAK_SPACE = "nbsp";
    private static final String SPACE = " ";
    private static final int STYLE_BOLD = 1;
    private static final int STYLE_ITALIC = 2;
    private static final String TAG = "WebvttCueParser";
    private static final String TAG_BOLD = "b";
    private static final String TAG_CLASS = "c";
    private static final String TAG_ITALIC = "i";
    private static final String TAG_LANG = "lang";
    private static final String TAG_UNDERLINE = "u";
    private static final String TAG_VOICE = "v";

    private static final class StartTag {
        public final String name;
        public final int position;

        public StartTag(String name, int position) {
            this.position = position;
            this.name = name;
        }
    }

    WebvttCueParser() {
    }

    public Spanned parse(String markup) {
        SpannableStringBuilder spannedText = new SpannableStringBuilder();
        Stack<StartTag> startTagStack = new Stack();
        int pos = 0;
        while (pos < markup.length()) {
            char curr = markup.charAt(pos);
            switch (curr) {
                case '&':
                    int semiColonEnd = markup.indexOf(59, pos + 1);
                    int spaceEnd = markup.indexOf(32, pos + 1);
                    int entityEnd = semiColonEnd == -1 ? spaceEnd : spaceEnd == -1 ? semiColonEnd : Math.min(semiColonEnd, spaceEnd);
                    if (entityEnd == -1) {
                        spannedText.append(curr);
                        pos++;
                        break;
                    }
                    applyEntity(markup.substring(pos + 1, entityEnd), spannedText);
                    if (entityEnd == spaceEnd) {
                        spannedText.append(SPACE);
                    }
                    pos = entityEnd + 1;
                    break;
                case '<':
                    if (pos + 1 < markup.length()) {
                        int i;
                        int ltPos = pos;
                        boolean isClosingTag = markup.charAt(ltPos + 1) == CHAR_SLASH;
                        pos = findEndOfTag(markup, ltPos + 1);
                        boolean isVoidTag = markup.charAt(pos + -2) == CHAR_SLASH;
                        int i2 = ltPos + (isClosingTag ? 2 : 1);
                        if (isVoidTag) {
                            i = pos - 2;
                        } else {
                            i = pos - 1;
                        }
                        String[] tagTokens = tokenizeTag(markup.substring(i2, i));
                        if (tagTokens != null && isSupportedTag(tagTokens[0])) {
                            if (!isClosingTag) {
                                if (!isVoidTag) {
                                    startTagStack.push(new StartTag(tagTokens[0], spannedText.length()));
                                    break;
                                }
                                break;
                            }
                            while (!startTagStack.isEmpty()) {
                                StartTag startTag = (StartTag) startTagStack.pop();
                                applySpansForTag(startTag, spannedText);
                                if (startTag.name.equals(tagTokens[0])) {
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    pos++;
                    break;
                default:
                    spannedText.append(curr);
                    pos++;
                    break;
            }
        }
        while (!startTagStack.isEmpty()) {
            applySpansForTag((StartTag) startTagStack.pop(), spannedText);
        }
        return spannedText;
    }

    private int findEndOfTag(String markup, int startPos) {
        int idx = markup.indexOf(62, startPos);
        return idx == -1 ? markup.length() : idx + 1;
    }

    private void applyEntity(String entity, SpannableStringBuilder spannedText) {
        Object obj = -1;
        switch (entity.hashCode()) {
            case 3309:
                if (entity.equals(ENTITY_GREATER_THAN)) {
                    obj = 1;
                    break;
                }
                break;
            case 3464:
                if (entity.equals(ENTITY_LESS_THAN)) {
                    obj = null;
                    break;
                }
                break;
            case 96708:
                if (entity.equals(ENTITY_AMPERSAND)) {
                    obj = 3;
                    break;
                }
                break;
            case 3374865:
                if (entity.equals(ENTITY_NON_BREAK_SPACE)) {
                    obj = 2;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                spannedText.append(CHAR_LESS_THAN);
                return;
            case 1:
                spannedText.append(CHAR_GREATER_THAN);
                return;
            case 2:
                spannedText.append(CHAR_SPACE);
                return;
            case 3:
                spannedText.append(CHAR_AMPERSAND);
                return;
            default:
                Log.w(TAG, "ignoring unsupported entity: '&" + entity + ";'");
                return;
        }
    }

    private boolean isSupportedTag(String tagName) {
        boolean z = true;
        switch (tagName.hashCode()) {
            case 98:
                if (tagName.equals(TAG_BOLD)) {
                    z = false;
                    break;
                }
                break;
            case 99:
                if (tagName.equals(TAG_CLASS)) {
                    z = true;
                    break;
                }
                break;
            case 105:
                if (tagName.equals(TAG_ITALIC)) {
                    z = true;
                    break;
                }
                break;
            case 117:
                if (tagName.equals(TAG_UNDERLINE)) {
                    z = true;
                    break;
                }
                break;
            case 118:
                if (tagName.equals(TAG_VOICE)) {
                    z = true;
                    break;
                }
                break;
            case 3314158:
                if (tagName.equals(TAG_LANG)) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
            case true:
            case true:
            case true:
            case true:
            case true:
                return true;
            default:
                return false;
        }
    }

    private void applySpansForTag(StartTag startTag, SpannableStringBuilder spannedText) {
        String str = startTag.name;
        int i = -1;
        switch (str.hashCode()) {
            case 98:
                if (str.equals(TAG_BOLD)) {
                    i = 0;
                    break;
                }
                break;
            case 105:
                if (str.equals(TAG_ITALIC)) {
                    i = 1;
                    break;
                }
                break;
            case 117:
                if (str.equals(TAG_UNDERLINE)) {
                    i = 2;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                spannedText.setSpan(new StyleSpan(1), startTag.position, spannedText.length(), 33);
                return;
            case 1:
                spannedText.setSpan(new StyleSpan(2), startTag.position, spannedText.length(), 33);
                return;
            case 2:
                spannedText.setSpan(new UnderlineSpan(), startTag.position, spannedText.length(), 33);
                return;
            default:
                return;
        }
    }

    private String[] tokenizeTag(String fullTagExpression) {
        fullTagExpression = fullTagExpression.replace("\\s+", SPACE).trim();
        if (fullTagExpression.length() == 0) {
            return null;
        }
        if (fullTagExpression.contains(SPACE)) {
            fullTagExpression = fullTagExpression.substring(0, fullTagExpression.indexOf(SPACE));
        }
        return fullTagExpression.split("\\.");
    }
}
