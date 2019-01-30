package org.cybergarage.p001b;

import cz.msebera.android.httpclient.client.methods.HttpPost;
import java.io.ByteArrayInputStream;
import org.cybergarage.http.C0953e;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.b.b */
public class C0984b extends C0953e {
    /* renamed from: b */
    private C0802b f187b;

    public C0984b() {
        m110h("text/xml; charset=\"utf-8\"");
        mo2100m(HttpPost.METHOD_NAME);
    }

    /* renamed from: Y */
    private synchronized C0802b mo2566Y() {
        C0802b c0802b;
        if (this.f187b != null) {
            c0802b = this.f187b;
        } else {
            try {
                this.f187b = C0765a.m45b().parse(new ByteArrayInputStream(m111h()));
            } catch (Exception e) {
                Debug.warning(e);
            }
            c0802b = this.f187b;
        }
        return c0802b;
    }

    /* renamed from: c */
    private void m595c(C0802b c0802b) {
        this.f187b = c0802b;
    }

    /* renamed from: U */
    public void mo2461U() {
        Debug.message(toString());
        if (!m115j()) {
            C0802b Y = mo2566Y();
            if (Y != null) {
                Debug.message(Y.toString());
            }
        }
    }

    /* renamed from: V */
    public String m597V() {
        return m105e("SOAPACTION");
    }

    /* renamed from: W */
    public C0802b m598W() {
        return mo2566Y();
    }

    /* renamed from: X */
    public C0802b m599X() {
        C0802b W = m598W();
        return (W != null && W.m395i()) ? W.m378c(0) : null;
    }

    /* renamed from: a */
    public void m600a(C0802b c0802b) {
        m595c(c0802b);
    }

    /* renamed from: b */
    public C0985c m601b(String str, int i, boolean z) {
        C0985c c0985c = new C0985c(m440a(str, i, z));
        byte[] h = c0985c.m111h();
        if (h.length > 0) {
            try {
                c0985c.m610a(C0765a.m45b().parse(new ByteArrayInputStream(h)));
            } catch (Exception e) {
                Debug.warning(e);
            }
        }
        return c0985c;
    }

    /* renamed from: b */
    public void m602b(C0802b c0802b) {
        m109g((("" + "<?xml version=\"1.0\" encoding=\"utf-8\"?>") + "\n") + c0802b.toString());
    }

    /* renamed from: q */
    public void m603q(String str) {
        m100c("SOAPACTION", str);
    }

    /* renamed from: r */
    public boolean m604r(String str) {
        String d = m102d("SOAPACTION");
        if (d == null) {
            return false;
        }
        if (d.equals(str)) {
            return true;
        }
        String V = m597V();
        return V != null ? V.equals(str) : false;
    }
}
