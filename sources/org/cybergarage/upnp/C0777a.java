package org.cybergarage.upnp;

import cz.msebera.android.httpclient.HttpStatus;
import org.cybergarage.http.C0954g;
import org.cybergarage.upnp.p002a.C0775a;
import org.cybergarage.upnp.p002a.C0996e;
import org.cybergarage.upnp.p002a.C1001b;
import org.cybergarage.upnp.p002a.C1002c;
import org.cybergarage.upnp.p003b.C0956a;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.a */
public class C0777a {
    /* renamed from: a */
    private C0802b f63a;
    /* renamed from: b */
    private C0802b f64b;
    /* renamed from: c */
    private Mutex f65c = new Mutex();
    /* renamed from: d */
    private C0796h f66d = new C0796h();
    /* renamed from: e */
    private Object f67e = null;

    public C0777a(C0802b c0802b, C0802b c0802b2) {
        this.f63a = c0802b;
        this.f64b = c0802b2;
    }

    /* renamed from: a */
    private void m163a(C0996e c0996e) {
        m167j().m462a(c0996e);
    }

    /* renamed from: a */
    public static boolean m164a(C0802b c0802b) {
        return "action".equals(c0802b.m377c());
    }

    /* renamed from: h */
    private C0802b m165h() {
        return this.f63a;
    }

    /* renamed from: i */
    private void m166i() {
        ArgumentList d = m181d();
        int size = d.size();
        for (int i = 0; i < size; i++) {
            C0779b argument = d.getArgument(i);
            if (argument.m194e()) {
                argument.m191b("");
            }
        }
    }

    /* renamed from: j */
    private C0956a m167j() {
        C0802b b = m178b();
        C0956a c0956a = (C0956a) b.m397j();
        if (c0956a != null) {
            return c0956a;
        }
        Object c0956a2 = new C0956a();
        b.m365a(c0956a2);
        c0956a2.m185b(b);
        return c0956a2;
    }

    /* renamed from: a */
    public C0779b m168a(String str) {
        ArgumentList d = m181d();
        int size = d.size();
        for (int i = 0; i < size; i++) {
            C0779b argument = d.getArgument(i);
            String b = argument.m190b();
            if (b != null && str.equals(b)) {
                return argument;
            }
        }
        return null;
    }

    /* renamed from: a */
    public C0790e m169a() {
        return new C0790e(m165h());
    }

    /* renamed from: a */
    public void m170a(int i) {
        m171a(i, C0796h.m304a(i));
    }

    /* renamed from: a */
    public void m171a(int i, String str) {
        this.f66d.m308b(i);
        this.f66d.m306a(str);
    }

    /* renamed from: a */
    public void m172a(String str, int i) {
        m173a(str, Integer.toString(i));
    }

    /* renamed from: a */
    public void m173a(String str, String str2) {
        C0779b a = m168a(str);
        if (a != null) {
            a.m191b(str2);
        }
    }

    /* renamed from: a */
    public void m174a(C0775a c0775a) {
        m167j().m461a(c0775a);
    }

    /* renamed from: a */
    public boolean m175a(C1001b c1001b) {
        C0775a f = m183f();
        if (f == null) {
            return false;
        }
        C0954g c1002c = new C1002c();
        m170a((int) HttpStatus.SC_UNAUTHORIZED);
        m166i();
        if (f.m161a(this)) {
            c1002c.m667a(this);
        } else {
            C0796h g = m184g();
            c1002c.m647a(g.m305a(), g.m307b());
        }
        if (Debug.isOn()) {
            c1002c.mo2462E();
        }
        c1001b.m443a(c1002c);
        return true;
    }

    /* renamed from: a */
    public boolean m176a(boolean z) {
        ArgumentList d = m181d();
        ArgumentList e = m182e();
        C1001b c1001b = new C1001b();
        c1001b.m663a(this, e);
        if (Debug.isOn()) {
            c1001b.mo2461U();
        }
        C0996e a = c1001b.m662a(z);
        if (Debug.isOn()) {
            a.mo2462E();
        }
        m163a(a);
        m170a(a.m452A());
        if (!a.m453B()) {
            return false;
        }
        try {
            d.setResArgs(a.mo2620H());
            return true;
        } catch (IllegalArgumentException e2) {
            m171a((int) HttpStatus.SC_PAYMENT_REQUIRED, "Action succesfully delivered but invalid arguments returned.");
            return false;
        }
    }

    /* renamed from: b */
    public String m177b(String str) {
        C0779b a = m168a(str);
        return a == null ? "" : a.m195f();
    }

    /* renamed from: b */
    public C0802b m178b() {
        return this.f64b;
    }

    /* renamed from: c */
    public int m179c(String str) {
        C0779b a = m168a(str);
        return a == null ? 0 : a.m196g();
    }

    /* renamed from: c */
    public String m180c() {
        return m178b().m398j("name");
    }

    /* renamed from: d */
    public ArgumentList m181d() {
        ArgumentList argumentList = new ArgumentList();
        C0802b g = m178b().m392g(ArgumentList.ELEM_NAME);
        if (g != null) {
            int g2 = g.m391g();
            for (int i = 0; i < g2; i++) {
                C0802b c = g.m378c(i);
                if (C0779b.m186a(c)) {
                    argumentList.add(new C0779b(m165h(), c));
                }
            }
        }
        return argumentList;
    }

    /* renamed from: e */
    public ArgumentList m182e() {
        ArgumentList d = m181d();
        int size = d.size();
        ArgumentList argumentList = new ArgumentList();
        for (int i = 0; i < size; i++) {
            C0779b argument = d.getArgument(i);
            if (argument.m193d()) {
                argumentList.add(argument);
            }
        }
        return argumentList;
    }

    /* renamed from: f */
    public C0775a m183f() {
        return m167j().m460a();
    }

    /* renamed from: g */
    public C0796h m184g() {
        return this.f66d;
    }
}
