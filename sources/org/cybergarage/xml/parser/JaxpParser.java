package org.cybergarage.xml.parser;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.cybergarage.xml.C0802b;
import org.cybergarage.xml.C0803c;
import org.cybergarage.xml.ParserException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class JaxpParser extends C0803c {
    public C0802b parse(InputStream inputStream) {
        C0802b c0802b = null;
        try {
            Node documentElement = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(inputStream)).getDocumentElement();
            if (documentElement != null) {
                c0802b = parse(null, documentElement);
            }
            return c0802b;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    public C0802b parse(C0802b c0802b, Node node) {
        return parse(c0802b, node, 0);
    }

    public C0802b parse(C0802b c0802b, Node node, int i) {
        short nodeType = node.getNodeType();
        String nodeName = node.getNodeName();
        String nodeValue = node.getNodeValue();
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            attributes.getLength();
        }
        if (nodeType == (short) 3) {
            c0802b.m379c(nodeValue);
            return c0802b;
        } else if (nodeType != (short) 1) {
            return c0802b;
        } else {
            C0802b c0802b2 = new C0802b();
            c0802b2.m366a(nodeName);
            c0802b2.m374b(nodeValue);
            if (c0802b != null) {
                c0802b.m381c(c0802b2);
            }
            NamedNodeMap attributes2 = node.getAttributes();
            if (attributes2 != null) {
                int length = attributes2.getLength();
                for (int i2 = 0; i2 < length; i2++) {
                    Node item = attributes2.item(i2);
                    c0802b2.m380c(item.getNodeName(), item.getNodeValue());
                }
            }
            Node firstChild = node.getFirstChild();
            if (firstChild == null) {
                c0802b2.m374b("");
                return c0802b2;
            }
            do {
                parse(c0802b2, firstChild, i + 1);
                firstChild = firstChild.getNextSibling();
            } while (firstChild != null);
            return c0802b2;
        }
    }
}
