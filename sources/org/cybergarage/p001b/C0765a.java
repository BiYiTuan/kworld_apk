package org.cybergarage.p001b;

import org.cybergarage.xml.C0802b;
import org.cybergarage.xml.C0803c;

/* renamed from: org.cybergarage.b.a */
public class C0765a {
    /* renamed from: a */
    private static C0803c f35a;

    /* renamed from: a */
    public static final C0802b m43a() {
        C0802b c0802b = new C0802b("s:Envelope");
        c0802b.m380c("xmlns:s", "http://schemas.xmlsoap.org/soap/envelope/");
        c0802b.m380c("s:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
        c0802b.m381c(new C0802b("s:Body"));
        return c0802b;
    }

    /* renamed from: a */
    public static final void m44a(C0803c c0803c) {
        f35a = c0803c;
    }

    /* renamed from: b */
    public static final C0803c m45b() {
        return f35a;
    }
}
