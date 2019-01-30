package org.cybergarage.upnp.event;

import java.util.Vector;

public class PropertyList extends Vector {
    public static final String ELEM_NAME = "PropertyList";

    public C0792c getProperty(int i) {
        return (C0792c) get(i);
    }
}
