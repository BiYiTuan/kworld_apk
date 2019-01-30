package org.cybergarage.xml;

public class ParserException extends Exception {
    public ParserException(Exception exception) {
        super(exception);
    }

    public ParserException(String str) {
        super(str);
    }
}
