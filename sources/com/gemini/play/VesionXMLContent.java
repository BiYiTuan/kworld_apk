package com.gemini.play;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class VesionXMLContent extends DefaultHandler {
    private String Version;
    private String preTag;

    public String getVersion() {
        return this.Version;
    }

    public void startDocument() throws SAXException {
        this.Version = new String();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String data = new String(ch, start, length);
        if ("string".equals(this.preTag)) {
            this.Version = data;
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.preTag = localName;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.preTag = null;
    }
}
