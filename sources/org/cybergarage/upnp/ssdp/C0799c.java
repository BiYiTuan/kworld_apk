package org.cybergarage.upnp.ssdp;

import org.cybergarage.util.Debug;

/* renamed from: org.cybergarage.upnp.ssdp.c */
public class C0799c {
    /* renamed from: a */
    private static String f98a;

    static {
        C0799c.m329a("FF02::C");
    }

    /* renamed from: a */
    public static final String m328a() {
        return f98a;
    }

    /* renamed from: a */
    public static final void m329a(String str) {
        f98a = str;
    }

    /* renamed from: b */
    public static final int m330b(String str) {
        int i = 0;
        int indexOf = str.indexOf("max-age");
        if (indexOf >= 0) {
            int indexOf2 = str.indexOf(44, indexOf);
            if (indexOf2 < 0) {
                indexOf2 = str.length();
            }
            try {
                i = Integer.parseInt(str.substring(str.indexOf("=", indexOf) + 1, indexOf2).trim());
            } catch (Exception e) {
                Debug.warning(e);
            }
        }
        return i;
    }
}
