package com.google.android.exoplayer.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ParserUtil {
    private ParserUtil() {
    }

    public static boolean isEndTag(XmlPullParser xpp, String name) throws XmlPullParserException {
        return xpp.getEventType() == 3 && name.equals(xpp.getName());
    }

    public static boolean isStartTag(XmlPullParser xpp, String name) throws XmlPullParserException {
        return xpp.getEventType() == 2 && name.equals(xpp.getName());
    }

    public static boolean isStartTag(XmlPullParser xpp) throws XmlPullParserException {
        return xpp.getEventType() == 2;
    }

    public static String removeNamespacePrefix(String attributeName) {
        return attributeName.replaceFirst("^.*:", "");
    }
}
