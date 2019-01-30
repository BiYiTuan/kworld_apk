package org.cybergarage.upnp.p002a;

import org.cybergarage.upnp.C0964f;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.a.h */
public class C1004h extends C0996e {
    /* renamed from: m */
    private C0802b m670m(String str) {
        C0802b c0802b = new C0802b();
        c0802b.m368a("u", "QueryStateVariableResponse");
        c0802b.m385d("u", "urn:schemas-upnp-org:control-1-0");
        C0802b c0802b2 = new C0802b();
        c0802b2.m366a("return");
        c0802b2.m374b(str);
        c0802b.m381c(c0802b2);
        return c0802b;
    }

    /* renamed from: a */
    public void m671a(C0964f c0964f) {
        String h = c0964f.m573h();
        m457d(200);
        m609G().m381c(m670m(h));
        m611b(m608F());
    }
}
