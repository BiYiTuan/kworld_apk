package org.cybergarage.upnp;

import cz.msebera.android.httpclient.HttpStatus;
import org.cybergarage.http.C0954g;
import org.cybergarage.upnp.p002a.C0776f;
import org.cybergarage.upnp.p002a.C1003g;
import org.cybergarage.upnp.p002a.C1004h;
import org.cybergarage.upnp.p003b.C0778d;
import org.cybergarage.upnp.p003b.C0960f;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.f */
public class C0964f extends C0778d {
    /* renamed from: a */
    private C0802b f174a;
    /* renamed from: b */
    private C0802b f175b;
    /* renamed from: c */
    private C0796h f176c;
    /* renamed from: d */
    private Object f177d;

    public C0964f() {
        this.f176c = new C0796h();
        this.f177d = null;
        this.f175b = null;
        this.f174a = new C0802b("stateVariable");
    }

    public C0964f(C0802b c0802b, C0802b c0802b2) {
        this.f176c = new C0796h();
        this.f177d = null;
        this.f175b = c0802b;
        this.f174a = c0802b2;
    }

    /* renamed from: a */
    public static boolean m556a(C0802b c0802b) {
        return "stateVariable".equals(c0802b.m377c());
    }

    /* renamed from: a */
    public C0802b m557a() {
        return this.f175b;
    }

    /* renamed from: a */
    public void m558a(int i) {
        m559a(i, C0796h.m304a(i));
    }

    /* renamed from: a */
    public void m559a(int i, String str) {
        this.f176c.m308b(i);
        this.f176c.m306a(str);
    }

    /* renamed from: a */
    public void m560a(String str) {
        m568c().m388e("name", str);
    }

    /* renamed from: a */
    public void m561a(String str, boolean z) {
        String a = m572g().m497a();
        if (a == null || !a.equals(str)) {
            m572g().m498a(str);
            C0790e b = m566b();
            if (b != null && m571f()) {
                b.m236a(this, z);
            }
        }
    }

    /* renamed from: a */
    public void m562a(C0776f c0776f) {
        m572g().m499a(c0776f);
    }

    /* renamed from: a */
    public void m563a(C0964f c0964f, boolean z) {
        m560a(c0964f.m569d());
        m561a(c0964f.m573h(), z);
        m567b(c0964f.m570e());
        m564a(c0964f.m571f());
    }

    /* renamed from: a */
    public void m564a(boolean z) {
        m568c().m380c("sendEvents", z ? "yes" : "no");
    }

    /* renamed from: a */
    public boolean m565a(C1003g c1003g, boolean z) {
        C0776f i = m574i();
        if (i == null) {
            return false;
        }
        C0954g c1004h = new C1004h();
        C0964f c0964f = new C0964f();
        c0964f.m563a(this, z);
        c0964f.m561a("", z);
        c0964f.m558a((int) HttpStatus.SC_NOT_FOUND);
        if (i.m162a(c0964f)) {
            c1004h.m671a(c0964f);
        } else {
            C0796h j = c0964f.m575j();
            c1004h.m647a(j.m305a(), j.m307b());
        }
        c1003g.m443a(c1004h);
        return true;
    }

    /* renamed from: b */
    public C0790e m566b() {
        C0802b a = m557a();
        return a == null ? null : new C0790e(a);
    }

    /* renamed from: b */
    public void m567b(String str) {
        m568c().m388e("dataType", str);
    }

    /* renamed from: c */
    public C0802b m568c() {
        return this.f174a;
    }

    /* renamed from: d */
    public String m569d() {
        return m568c().m398j("name");
    }

    /* renamed from: e */
    public String m570e() {
        return m568c().m398j("dataType");
    }

    /* renamed from: f */
    public boolean m571f() {
        String e = m568c().m387e("sendEvents");
        return e != null && e.equalsIgnoreCase("yes");
    }

    /* renamed from: g */
    public C0960f m572g() {
        C0802b c = m568c();
        C0960f c0960f = (C0960f) c.m397j();
        if (c0960f != null) {
            return c0960f;
        }
        Object c0960f2 = new C0960f();
        c.m365a(c0960f2);
        c0960f2.m185b(c);
        return c0960f2;
    }

    /* renamed from: h */
    public String m573h() {
        return m572g().m497a();
    }

    /* renamed from: i */
    public C0776f m574i() {
        return m572g().m500b();
    }

    /* renamed from: j */
    public C0796h m575j() {
        return this.f176c;
    }
}
