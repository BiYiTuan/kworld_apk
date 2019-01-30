package org.cybergarage.upnp.event;

import org.cybergarage.http.C0953e;
import org.cybergarage.p001b.C0984b;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.event.b */
public class C0997b extends C0984b {
    public C0997b(C0953e c0953e) {
        m442a(c0953e);
    }

    /* renamed from: c */
    private C0792c m649c(C0802b c0802b) {
        C0792c c0792c = new C0792c();
        if (c0802b == null) {
            return c0792c;
        }
        String c = c0802b.m377c();
        int lastIndexOf = c.lastIndexOf(58);
        if (lastIndexOf != -1) {
            c = c.substring(lastIndexOf + 1);
        }
        c0792c.m270a(c);
        c0792c.m272b(c0802b.m382d());
        return c0792c;
    }

    /* renamed from: d */
    private C0802b m650d(String str, String str2) {
        C0802b c0802b = new C0802b("propertyset");
        c0802b.m385d("e", "urn:schemas-upnp-org:event-1-0");
        C0802b c0802b2 = new C0802b("property");
        c0802b.m381c(c0802b2);
        C0802b c0802b3 = new C0802b(str);
        c0802b3.m374b(str2);
        c0802b2.m381c(c0802b3);
        return c0802b;
    }

    /* renamed from: Y */
    public String mo2566Y() {
        return C0794e.m293c(m102d("SID"));
    }

    /* renamed from: Z */
    public long m652Z() {
        return m106f("SEQ");
    }

    /* renamed from: a */
    public boolean m653a(C0793d c0793d, String str, String str2) {
        c0793d.m277b();
        String a = c0793d.m273a();
        long i = c0793d.m286i();
        String c = c0793d.m280c();
        String d = c0793d.m281d();
        int e = c0793d.m282e();
        mo2100m("NOTIFY");
        m450o(d);
        m99c(c, e);
        m655s("upnp:event");
        m656t("upnp:propchange");
        m657u(a);
        m654b(i);
        m110h("text/xml; charset=\"utf-8\"");
        m602b(m650d(str, str2));
        return true;
    }

    public PropertyList aa() {
        PropertyList propertyList = new PropertyList();
        C0802b W = m598W();
        for (int i = 0; i < W.m391g(); i++) {
            C0802b c = W.m378c(i);
            if (c != null) {
                propertyList.add(m649c(c.m378c(0)));
            }
        }
        return propertyList;
    }

    /* renamed from: b */
    public void m654b(long j) {
        m94b("SEQ", Long.toString(j));
    }

    /* renamed from: s */
    public void m655s(String str) {
        m94b("NT", str);
    }

    /* renamed from: t */
    public void m656t(String str) {
        m94b("NTS", str);
    }

    /* renamed from: u */
    public void m657u(String str) {
        m94b("SID", C0794e.m292b(str));
    }
}
