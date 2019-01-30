package org.cybergarage.upnp;

import java.util.Vector;

public class ServiceList extends Vector {
    public static final String ELEM_NAME = "serviceList";

    public C0790e getService(int i) {
        Object obj = null;
        try {
            obj = get(i);
        } catch (Exception e) {
        }
        return (C0790e) obj;
    }
}
