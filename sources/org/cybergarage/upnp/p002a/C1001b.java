package org.cybergarage.upnp.p002a;

import org.cybergarage.http.C0953e;
import org.cybergarage.p001b.C0765a;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.C0777a;
import org.cybergarage.upnp.C0779b;
import org.cybergarage.upnp.C0790e;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.a.b */
public class C1001b extends C0995d {
    public C1001b(C0953e c0953e) {
        m442a(c0953e);
    }

    /* renamed from: a */
    private C0802b m659a(C0790e c0790e, C0777a c0777a, ArgumentList argumentList) {
        String c = c0777a.m180c();
        String d = c0790e.m245d();
        C0802b c0802b = new C0802b();
        c0802b.m368a("u", c);
        c0802b.m385d("u", d);
        int size = argumentList.size();
        for (int i = 0; i < size; i++) {
            C0779b argument = argumentList.getArgument(i);
            C0802b c0802b2 = new C0802b();
            c0802b2.m366a(argument.m190b());
            c0802b2.m374b(argument.m195f());
            c0802b.m381c(c0802b2);
        }
        return c0802b;
    }

    /* renamed from: Y */
    public C0802b mo2566Y() {
        C0802b X = m599X();
        return (X != null && X.m395i()) ? X.m378c(0) : null;
    }

    /* renamed from: Z */
    public String m661Z() {
        C0802b Y = mo2566Y();
        if (Y == null) {
            return "";
        }
        String c = Y.m377c();
        if (c == null) {
            return "";
        }
        int indexOf = c.indexOf(":") + 1;
        return indexOf < 0 ? "" : c.substring(indexOf, c.length());
    }

    /* renamed from: a */
    public C1002c m662a(boolean z) {
        return new C1002c(m601b(m428J(), m429K(), z));
    }

    /* renamed from: a */
    public void m663a(C0777a c0777a, ArgumentList argumentList) {
        C0790e a = c0777a.m169a();
        m645a(a);
        m600a(C0765a.m43a());
        C0802b W = m598W();
        m599X().m381c(m659a(a, c0777a, argumentList));
        m602b(W);
        String d = a.m245d();
        m603q("\"" + d + "#" + c0777a.m180c() + "\"");
    }

    public ArgumentList aa() {
        C0802b Y = mo2566Y();
        int g = Y.m391g();
        ArgumentList argumentList = new ArgumentList();
        for (int i = 0; i < g; i++) {
            C0779b c0779b = new C0779b();
            C0802b c = Y.m378c(i);
            c0779b.m189a(c.m377c());
            c0779b.m191b(c.m382d());
            argumentList.add(c0779b);
        }
        return argumentList;
    }
}
