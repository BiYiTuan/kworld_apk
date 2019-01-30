package org.cybergarage.upnp.p002a;

import org.cybergarage.p001b.C0985c;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.C0777a;
import org.cybergarage.upnp.C0779b;
import org.cybergarage.upnp.C0790e;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.a.c */
public class C1002c extends C0996e {
    public C1002c() {
        m94b("EXT", "");
    }

    public C1002c(C0985c c0985c) {
        super(c0985c);
        m94b("EXT", "");
    }

    /* renamed from: I */
    private C0802b m664I() {
        C0802b G = m609G();
        return (G == null || !G.m395i()) ? null : G.m378c(0);
    }

    /* renamed from: b */
    private C0802b m665b(C0777a c0777a) {
        C0802b c0802b = new C0802b("u:" + c0777a.m180c() + "Response");
        C0790e a = c0777a.m169a();
        if (a != null) {
            c0802b.m380c("xmlns:u", a.m245d());
        }
        ArgumentList d = c0777a.m181d();
        int size = d.size();
        for (int i = 0; i < size; i++) {
            C0779b argument = d.getArgument(i);
            if (argument.m194e()) {
                C0802b c0802b2 = new C0802b();
                c0802b2.m366a(argument.m190b());
                c0802b2.m374b(argument.m195f());
                c0802b.m381c(c0802b2);
            }
        }
        return c0802b;
    }

    /* renamed from: H */
    public ArgumentList mo2620H() {
        ArgumentList argumentList = new ArgumentList();
        C0802b I = m664I();
        if (I != null) {
            int g = I.m391g();
            for (int i = 0; i < g; i++) {
                C0802b c = I.m378c(i);
                argumentList.add(new C0779b(c.m377c(), c.m382d()));
            }
        }
        return argumentList;
    }

    /* renamed from: a */
    public void m667a(C0777a c0777a) {
        m457d(200);
        m609G().m381c(m665b(c0777a));
        m611b(m608F());
    }
}
