package com.memo.cable;

import org.cybergarage.upnp.Device;

public class DeviceUtil {
    private static final String MEDIARENDER = "tubicast";
    private static final String MEDIARENDER_DLNA = "urn:schemas-upnp-org:device:MediaRenderer:1";

    public static boolean isMediaRenderDevice(Device device) {
        return (device != null && device.encryption && MEDIARENDER_DLNA.equalsIgnoreCase(device.getDeviceType())) ? true : (device == null || device.encryption || !MEDIARENDER.equalsIgnoreCase(device.getDeviceType())) ? false : true;
    }
}
