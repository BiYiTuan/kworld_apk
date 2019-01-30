package org.cybergarage.upnp.device;

import org.cybergarage.upnp.Device;

/* renamed from: org.cybergarage.upnp.device.h */
public class C0786h {
    /* renamed from: a */
    public static final boolean m213a(String str) {
        return str == null ? false : !str.equals("ssdp:all") ? str.equals("\"ssdp:all\"") : true;
    }

    /* renamed from: b */
    public static final boolean m214b(String str) {
        return str == null ? false : !str.equals(Device.UPNP_ROOTDEVICE) ? str.equals("\"upnp:rootdevice\"") : true;
    }

    /* renamed from: c */
    public static final boolean m215c(String str) {
        return str == null ? false : !str.startsWith("uuid") ? str.startsWith("\"uuid") : true;
    }

    /* renamed from: d */
    public static final boolean m216d(String str) {
        return str == null ? false : !str.startsWith("urn:schemas-upnp-org:device:") ? str.startsWith("\"urn:schemas-upnp-org:device:") : true;
    }

    /* renamed from: e */
    public static final boolean m217e(String str) {
        return str == null ? false : !str.startsWith("urn:schemas-upnp-org:service:") ? str.startsWith("\"urn:schemas-upnp-org:service:") : true;
    }
}
