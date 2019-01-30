package org.cybergarage.xml;

import java.util.Vector;

public class NodeList extends Vector {
    public synchronized C0802b getEndsWith(String str) {
        C0802b c0802b = null;
        synchronized (this) {
            if (str != null) {
                int size = size();
                for (int i = 0; i < size; i++) {
                    C0802b node = getNode(i);
                    String c = node.m377c();
                    if (c != null && c.endsWith(str)) {
                        c0802b = node;
                        break;
                    }
                }
            }
        }
        return c0802b;
    }

    public C0802b getNode(int i) {
        return (C0802b) get(i);
    }

    public synchronized C0802b getNode(String str) {
        C0802b c0802b = null;
        synchronized (this) {
            if (str != null) {
                int size = size();
                for (int i = 0; i < size; i++) {
                    C0802b node = getNode(i);
                    if (str.compareTo(node.m377c()) == 0) {
                        c0802b = node;
                        break;
                    }
                }
            }
        }
        return c0802b;
    }
}
