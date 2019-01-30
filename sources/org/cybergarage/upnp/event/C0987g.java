package org.cybergarage.upnp.event;

import org.cybergarage.http.C0954g;
import org.cybergarage.upnp.C0795g;

/* renamed from: org.cybergarage.upnp.event.g */
public class C0987g extends C0954g {
    public C0987g() {
        m117k(C0795g.m295a());
    }

    public C0987g(C0954g c0954g) {
        super(c0954g);
    }

    /* renamed from: F */
    public String m627F() {
        return C0794e.m293c(m102d("SID"));
    }

    /* renamed from: G */
    public long m628G() {
        return C0794e.m289a(m102d("TIMEOUT"));
    }

    /* renamed from: b */
    public void m629b(long j) {
        m94b("TIMEOUT", C0794e.m291a(j));
    }

    /* renamed from: e */
    public void m630e(int i) {
        m457d(i);
        m73a(0);
    }

    /* renamed from: m */
    public void mo2100m(String str) {
        m94b("SID", C0794e.m292b(str));
    }
}
