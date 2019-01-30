package org.cybergarage.upnp;

import java.util.Vector;

public class ServiceStateTable extends Vector {
    public static final String ELEM_NAME = "serviceStateTable";

    public C0964f getStateVariable(int i) {
        return (C0964f) get(i);
    }
}
