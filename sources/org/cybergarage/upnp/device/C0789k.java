package org.cybergarage.upnp.device;

import org.cybergarage.upnp.Device;

/* renamed from: org.cybergarage.upnp.device.k */
public class C0789k {
    /* renamed from: a */
    public static final boolean m218a(String str) {
        return str == null ? false : str.endsWith(Device.UPNP_ROOTDEVICE);
    }

    /* renamed from: b */
    public static final String m219b(String str) {
        if (str == null) {
            return "";
        }
        int indexOf = str.indexOf("::");
        return indexOf < 0 ? str.trim() : new String(str.getBytes(), 0, indexOf).trim();
    }
}
