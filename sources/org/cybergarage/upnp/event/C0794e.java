package org.cybergarage.upnp.event;

import org.cybergarage.upnp.C0795g;

/* renamed from: org.cybergarage.upnp.event.e */
public class C0794e {
    /* renamed from: a */
    public static final long m289a(String str) {
        long j = -1;
        try {
            j = Long.parseLong(str.substring(str.indexOf(45) + 1, str.length()));
        } catch (Exception e) {
        }
        return j;
    }

    /* renamed from: a */
    public static final String m290a() {
        return C0795g.m297b();
    }

    /* renamed from: a */
    public static final String m291a(long j) {
        return j == -1 ? "infinite" : "Second-" + Long.toString(j);
    }

    /* renamed from: b */
    public static final String m292b(String str) {
        return "uuid:" + str;
    }

    /* renamed from: c */
    public static final String m293c(String str) {
        return str == null ? "" : str.startsWith("uuid:") ? str.substring("uuid:".length(), str.length()) : str;
    }
}
