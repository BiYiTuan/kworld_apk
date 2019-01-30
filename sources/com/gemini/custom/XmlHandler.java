package com.gemini.custom;

import io.vov.vitamio.MediaMetadataRetriever;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* compiled from: quanxing */
class XmlHandler extends DefaultHandler {
    private String date = null;
    private String id = null;
    private List<PreviewInfo> infos = null;
    private String week = null;

    XmlHandler() {
    }

    public List<PreviewInfo> getInfos() {
        return this.infos;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    public void startDocument() throws SAXException {
        super.startDocument();
        this.infos = new ArrayList();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("Epg".equals(localName)) {
            this.id = attributes.getValue("ID");
        }
        if ("Week1".equals(localName) || "Week2".equals(localName) || "Week3".equals(localName) || "Week4".equals(localName) || "Week5".equals(localName) || "Week6".equals(localName) || "Week7".equals(localName)) {
            this.week = localName;
        }
        if (MediaMetadataRetriever.METADATA_KEY_DATE.equals(localName)) {
            this.date = attributes.getValue("day");
        }
        if ("Item".equals(localName)) {
            PreviewInfo info = new PreviewInfo();
            info.setId(this.id);
            info.setWeek(this.week);
            info.setDate(this.date);
            info.setTime(attributes.getValue("Time"));
            info.setName(attributes.getValue("Name"));
            this.infos.add(info);
        }
    }
}
