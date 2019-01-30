package org.cybergarage.xml.parser;

import java.io.InputStream;
import org.cybergarage.xml.C0802b;
import org.cybergarage.xml.C0803c;
import org.cybergarage.xml.ParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlPullParser extends C0803c {
    public C0802b parse(InputStream inputStream) {
        try {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            return parse(newInstance.newPullParser(), inputStream);
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    public C0802b parse(org.xmlpull.v1.XmlPullParser xmlPullParser, InputStream inputStream) {
        try {
            xmlPullParser.setInput(inputStream, null);
            C0802b c0802b = null;
            C0802b c0802b2 = null;
            int eventType = xmlPullParser.getEventType();
            while (eventType != 1) {
                C0802b c0802b3;
                C0802b c0802b4;
                switch (eventType) {
                    case 2:
                        c0802b3 = new C0802b();
                        String prefix = xmlPullParser.getPrefix();
                        String name = xmlPullParser.getName();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (prefix != null && prefix.length() > 0) {
                            stringBuffer.append(prefix);
                            stringBuffer.append(":");
                        }
                        if (name != null && name.length() > 0) {
                            stringBuffer.append(name);
                        }
                        c0802b3.m366a(stringBuffer.toString());
                        int attributeCount = xmlPullParser.getAttributeCount();
                        for (int i = 0; i < attributeCount; i++) {
                            c0802b3.m380c(xmlPullParser.getAttributeName(i), xmlPullParser.getAttributeValue(i));
                        }
                        if (c0802b != null) {
                            c0802b.m381c(c0802b3);
                        }
                        if (c0802b2 == null) {
                            c0802b2 = c0802b3;
                        }
                        c0802b4 = c0802b2;
                        break;
                    case 3:
                        c0802b3 = c0802b.m361a();
                        c0802b4 = c0802b2;
                        break;
                    case 4:
                        String text = xmlPullParser.getText();
                        if (!(text == null || c0802b == null)) {
                            c0802b.m374b(text);
                        }
                        c0802b3 = c0802b;
                        c0802b4 = c0802b2;
                        break;
                    default:
                        c0802b3 = c0802b;
                        c0802b4 = c0802b2;
                        break;
                }
                c0802b = c0802b3;
                eventType = xmlPullParser.next();
                c0802b2 = c0802b4;
            }
            return c0802b2;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
