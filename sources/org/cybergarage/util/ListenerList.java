package org.cybergarage.util;

import java.util.Vector;

public class ListenerList extends Vector {
    public boolean add(Object obj) {
        return indexOf(obj) >= 0 ? false : super.add(obj);
    }
}
