package org.cybergarage.upnp.ssdp;

import cz.msebera.android.httpclient.HttpHeaders;
import org.cybergarage.http.C0954g;

/* renamed from: org.cybergarage.upnp.ssdp.h */
public class C0989h extends C0954g {
    public C0989h() {
        m75a("1.1");
    }

    /* renamed from: D */
    public String mo2463D() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(m454C());
        stringBuffer.append(m108g());
        stringBuffer.append("\r\n");
        return stringBuffer.toString();
    }

    /* renamed from: e */
    public void m639e(int i) {
        m94b("Cache-Control", "max-age=" + Integer.toString(i));
    }

    /* renamed from: f */
    public void m640f(int i) {
        m76a("BOOTID.UPNP.ORG", i);
    }

    /* renamed from: m */
    public void mo2100m(String str) {
        m94b("ST", str);
    }

    /* renamed from: n */
    public void m642n(String str) {
        m94b(HttpHeaders.LOCATION, str);
    }

    /* renamed from: o */
    public void m643o(String str) {
        m94b("USN", str);
    }

    /* renamed from: p */
    public void m644p(String str) {
        m94b("MYNAME", str);
    }
}
