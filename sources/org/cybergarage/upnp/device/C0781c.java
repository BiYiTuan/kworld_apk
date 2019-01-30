package org.cybergarage.upnp.device;

/* renamed from: org.cybergarage.upnp.device.c */
public class C0781c {
    /* renamed from: a */
    public static final boolean m207a(String str) {
        return str == null ? false : !str.equals("ssdp:discover") ? str.equals("\"ssdp:discover\"") : true;
    }
}
