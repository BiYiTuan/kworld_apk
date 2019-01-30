package org.cybergarage.upnp;

import org.cybergarage.upnp.p003b.C0957b;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.b */
public class C0779b {
    /* renamed from: a */
    private C0802b f69a;
    /* renamed from: b */
    private C0802b f70b;
    /* renamed from: c */
    private Object f71c;

    public C0779b() {
        this.f71c = null;
        this.f69a = new C0802b("argument");
        this.f70b = null;
    }

    public C0779b(String str, String str2) {
        this();
        m189a(str);
        m191b(str2);
    }

    public C0779b(C0802b c0802b, C0802b c0802b2) {
        this.f71c = null;
        this.f70b = c0802b;
        this.f69a = c0802b2;
    }

    /* renamed from: a */
    public static boolean m186a(C0802b c0802b) {
        return "argument".equals(c0802b.m377c());
    }

    /* renamed from: h */
    private C0957b m187h() {
        C0802b a = m188a();
        C0957b c0957b = (C0957b) a.m397j();
        if (c0957b != null) {
            return c0957b;
        }
        Object c0957b2 = new C0957b();
        a.m365a(c0957b2);
        c0957b2.m185b(a);
        return c0957b2;
    }

    /* renamed from: a */
    public C0802b m188a() {
        return this.f69a;
    }

    /* renamed from: a */
    public void m189a(String str) {
        m188a().m388e("name", str);
    }

    /* renamed from: b */
    public String m190b() {
        return m188a().m398j("name");
    }

    /* renamed from: b */
    public void m191b(String str) {
        m187h().m464a(str);
    }

    /* renamed from: c */
    public String m192c() {
        return m188a().m398j("direction");
    }

    /* renamed from: d */
    public boolean m193d() {
        String c = m192c();
        return c == null ? false : c.equalsIgnoreCase("in");
    }

    /* renamed from: e */
    public boolean m194e() {
        return !m193d();
    }

    /* renamed from: f */
    public String m195f() {
        return m187h().m463a();
    }

    /* renamed from: g */
    public int m196g() {
        try {
            return Integer.parseInt(m195f());
        } catch (Exception e) {
            return 0;
        }
    }
}
