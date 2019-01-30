package org.cybergarage.upnp.p002a;

import cz.msebera.android.httpclient.HttpStatus;
import org.cybergarage.p001b.C0985c;
import org.cybergarage.upnp.C0795g;
import org.cybergarage.upnp.C0796h;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.a.e */
public class C0996e extends C0985c {
    /* renamed from: b */
    private C0796h f189b = new C0796h();

    public C0996e() {
        m117k(C0795g.m295a());
    }

    public C0996e(C0985c c0985c) {
        super(c0985c);
    }

    /* renamed from: b */
    private C0802b m646b(int i, String str) {
        C0802b c0802b = new C0802b("s:Fault");
        C0802b c0802b2 = new C0802b("faultcode");
        c0802b2.m374b("s:Client");
        c0802b.m381c(c0802b2);
        c0802b2 = new C0802b("faultstring");
        c0802b2.m374b("UPnPError");
        c0802b.m381c(c0802b2);
        c0802b2 = new C0802b("detail");
        c0802b.m381c(c0802b2);
        C0802b c0802b3 = new C0802b("UPnPError");
        c0802b3.m380c("xmlns", "urn:schemas-upnp-org:control-1-0");
        c0802b2.m381c(c0802b3);
        c0802b2 = new C0802b("errorCode");
        c0802b2.m362a(i);
        c0802b3.m381c(c0802b2);
        c0802b2 = new C0802b("errorDescription");
        c0802b2.m374b(str);
        c0802b3.m381c(c0802b2);
        return c0802b;
    }

    /* renamed from: a */
    public void m647a(int i, String str) {
        m457d(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        m609G().m381c(m646b(i, str));
        m611b(m608F());
    }

    /* renamed from: e */
    public void m648e(int i) {
        m647a(i, C0796h.m304a(i));
    }
}
