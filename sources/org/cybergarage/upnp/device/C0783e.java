package org.cybergarage.upnp.device;

/* renamed from: org.cybergarage.upnp.device.e */
public class C0783e {
    /* renamed from: a */
    public static final boolean m209a(String str) {
        return str == null ? false : str.startsWith("ssdp:alive");
    }

    /* renamed from: b */
    public static final boolean m210b(String str) {
        return str == null ? false : str.startsWith("ssdp:byebye");
    }
}
