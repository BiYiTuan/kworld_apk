package org.cybergarage.xml;

import java.util.Vector;

public class AttributeList extends Vector {
    public C0801a getAttribute(int i) {
        return (C0801a) get(i);
    }

    public C0801a getAttribute(String str) {
        if (str == null) {
            return null;
        }
        int size = size();
        for (int i = 0; i < size; i++) {
            C0801a attribute = getAttribute(i);
            if (str.compareTo(attribute.m354a()) == 0) {
                return attribute;
            }
        }
        return null;
    }
}
