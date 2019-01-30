package org.cybergarage.upnp.p002a;

import java.net.MalformedURLException;
import java.net.URL;
import org.cybergarage.http.C0767b;
import org.cybergarage.p001b.C0984b;
import org.cybergarage.upnp.C0790e;

/* renamed from: org.cybergarage.upnp.a.d */
public class C0995d extends C0984b {
    /* renamed from: a */
    protected void m645a(C0790e c0790e) {
        String i = c0790e.m255i();
        String uRLBase = c0790e.m243c().getURLBase();
        if (uRLBase != null && uRLBase.length() > 0) {
            try {
                uRLBase = new URL(uRLBase).getPath();
                int length = uRLBase.length();
                if (length > 0 && (1 < length || uRLBase.charAt(0) != '/')) {
                    i = uRLBase + i;
                }
            } catch (MalformedURLException e) {
            }
        }
        m444b(i, true);
        uRLBase = "";
        if (!C0767b.m55a(i)) {
            i = uRLBase;
        }
        if (i == null || i.length() <= 0) {
            i = c0790e.m243c().getURLBase();
        }
        if (i == null || i.length() <= 0) {
            i = c0790e.m243c().getLocation();
        }
        uRLBase = C0767b.m56b(i);
        int c = C0767b.m57c(i);
        m99c(uRLBase, c);
        m451p(uRLBase);
        m446d(c);
    }

    public boolean ab() {
        return m604r("urn:schemas-upnp-org:control-1-0#QueryStateVariable");
    }
}
