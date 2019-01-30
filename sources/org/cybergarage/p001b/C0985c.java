package org.cybergarage.p001b;

import org.cybergarage.http.C0954g;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.b.c */
public class C0985c extends C0954g {
    /* renamed from: b */
    private C0802b f188b;

    public C0985c() {
        m606c(C0765a.m43a());
        m110h("text/xml; charset=\"utf-8\"");
    }

    public C0985c(C0985c c0985c) {
        super((C0954g) c0985c);
        m610a(c0985c.m608F());
        m110h("text/xml; charset=\"utf-8\"");
    }

    public C0985c(C0954g c0954g) {
        super(c0954g);
        m606c(C0765a.m43a());
        m110h("text/xml; charset=\"utf-8\"");
    }

    /* renamed from: H */
    private C0802b mo2620H() {
        return this.f188b;
    }

    /* renamed from: c */
    private void m606c(C0802b c0802b) {
        this.f188b = c0802b;
    }

    /* renamed from: E */
    public void mo2462E() {
        Debug.message(toString());
        if (!m115j()) {
            C0802b H = mo2620H();
            if (H != null) {
                Debug.message(H.toString());
            }
        }
    }

    /* renamed from: F */
    public C0802b m608F() {
        return mo2620H();
    }

    /* renamed from: G */
    public C0802b m609G() {
        C0802b F = m608F();
        return F == null ? null : F.m393h("Body");
    }

    /* renamed from: a */
    public void m610a(C0802b c0802b) {
        m606c(c0802b);
    }

    /* renamed from: b */
    public void m611b(C0802b c0802b) {
        m109g((("" + "<?xml version=\"1.0\" encoding=\"utf-8\"?>") + "\n") + c0802b.toString());
    }
}
