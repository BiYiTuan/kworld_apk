package org.cybergarage.upnp.event;

import org.cybergarage.http.C0767b;
import org.cybergarage.http.C0953e;
import org.cybergarage.http.C0954g;
import org.cybergarage.upnp.C0790e;
import org.cybergarage.upnp.Device;

/* renamed from: org.cybergarage.upnp.event.f */
public class C0986f extends C0953e {
    public C0986f() {
        m73a(0);
    }

    public C0986f(C0953e c0953e) {
        this();
        m442a(c0953e);
    }

    /* renamed from: b */
    private void m612b(C0790e c0790e) {
        String j = c0790e.m257j();
        m444b(j, true);
        String str = "";
        Device b = c0790e.m240b();
        if (b != null) {
            str = b.getURLBase();
        }
        if (str == null || str.length() <= 0) {
            b = c0790e.m243c();
            if (b != null) {
                str = b.getURLBase();
            }
        }
        if (str == null || str.length() <= 0) {
            b = c0790e.m243c();
            if (b != null) {
                str = b.getLocation();
            }
        }
        if ((str == null || str.length() <= 0) && C0767b.m55a(j)) {
            str = j;
        }
        j = C0767b.m56b(str);
        int c = C0767b.m57c(str);
        m99c(j, c);
        m451p(j);
        m446d(c);
    }

    /* renamed from: V */
    public String m613V() {
        return m71a("CALLBACK", "<", ">");
    }

    /* renamed from: W */
    public boolean m614W() {
        String V = m613V();
        return V != null && V.length() > 0;
    }

    /* renamed from: X */
    public String m615X() {
        String c = C0794e.m293c(m102d("SID"));
        return c == null ? "" : c;
    }

    /* renamed from: Y */
    public boolean m616Y() {
        String X = m615X();
        return X != null && X.length() > 0;
    }

    /* renamed from: Z */
    public long m617Z() {
        return C0794e.m289a(m102d("TIMEOUT"));
    }

    /* renamed from: a */
    public C0987g m618a(boolean z) {
        return new C0987g(m440a(m428J(), m429K(), z));
    }

    /* renamed from: a */
    public void m619a(C0790e c0790e) {
        mo2100m("UNSUBSCRIBE");
        m612b(c0790e);
        m626s(c0790e.m264o());
    }

    /* renamed from: a */
    public void m620a(C0790e c0790e, String str, long j) {
        mo2100m("SUBSCRIBE");
        m612b(c0790e);
        m625r(str);
        m624q("upnp:event");
        m622b(j);
    }

    /* renamed from: a */
    public void m621a(C0987g c0987g) {
        super.m443a((C0954g) c0987g);
    }

    /* renamed from: b */
    public final void m622b(long j) {
        m94b("TIMEOUT", C0794e.m291a(j));
    }

    /* renamed from: b */
    public void m623b(C0790e c0790e, String str, long j) {
        mo2100m("SUBSCRIBE");
        m612b(c0790e);
        m626s(str);
        m622b(j);
    }

    /* renamed from: q */
    public void m624q(String str) {
        m94b("NT", str);
    }

    /* renamed from: r */
    public void m625r(String str) {
        m79a("CALLBACK", str, "<", ">");
    }

    /* renamed from: s */
    public void m626s(String str) {
        m94b("SID", C0794e.m292b(str));
    }
}
