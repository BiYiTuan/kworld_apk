package org.cybergarage.upnp;

import java.util.Vector;

public class ActionList extends Vector {
    public static final String ELEM_NAME = "actionList";

    public C0777a getAction(int i) {
        return (C0777a) get(i);
    }
}
