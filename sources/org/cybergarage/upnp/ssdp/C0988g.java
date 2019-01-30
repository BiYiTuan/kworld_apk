package org.cybergarage.upnp.ssdp;

import cz.msebera.android.httpclient.HttpHeaders;
import org.cybergarage.http.C0953e;

/* renamed from: org.cybergarage.upnp.ssdp.g */
public class C0988g extends C0953e {
    public C0988g() {
        m75a("1.1");
    }

    /* renamed from: f */
    public void m632f(int i) {
        m94b("Cache-Control", "max-age=" + Integer.toString(i));
    }

    /* renamed from: g */
    public void m633g(int i) {
        m76a("BOOTID.UPNP.ORG", i);
    }

    /* renamed from: q */
    public void m634q(String str) {
        m94b("NT", str);
    }

    /* renamed from: r */
    public void m635r(String str) {
        m94b("NTS", str);
    }

    /* renamed from: s */
    public void m636s(String str) {
        m94b(HttpHeaders.LOCATION, str);
    }

    /* renamed from: t */
    public void m637t(String str) {
        m94b("USN", str);
    }
}
