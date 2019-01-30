package org.cybergarage.upnp.p002a;

import org.cybergarage.http.C0953e;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.a.g */
public class C1003g extends C0995d {
    public C1003g(C0953e c0953e) {
        m442a(c0953e);
    }

    /* renamed from: Z */
    private C0802b m668Z() {
        C0802b X = m599X();
        if (X == null || !X.m395i()) {
            return null;
        }
        X = X.m378c(0);
        return (X == null || !X.m395i()) ? null : X.m378c(0);
    }

    /* renamed from: Y */
    public String mo2566Y() {
        C0802b Z = m668Z();
        return Z == null ? "" : Z.m382d();
    }
}
