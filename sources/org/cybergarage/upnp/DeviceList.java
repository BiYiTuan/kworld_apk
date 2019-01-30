package org.cybergarage.upnp;

import java.util.Vector;

public class DeviceList extends Vector {
    public static final String ELEM_NAME = "deviceList";

    public Device getDevice(int i) {
        return (Device) get(i);
    }
}
