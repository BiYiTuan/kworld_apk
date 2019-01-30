package org.cybergarage.upnp.ssdp;

import org.cybergarage.p000a.C0763a;
import org.cybergarage.upnp.Device;

/* renamed from: org.cybergarage.upnp.ssdp.i */
public class C0999i extends C0988g {
    public C0999i() {
        this(Device.UPNP_ROOTDEVICE);
    }

    public C0999i(String str) {
        this(str, 3);
    }

    public C0999i(String str, int i) {
        mo2100m("M-SEARCH");
        m450o("*");
        m94b("ST", str);
        m94b("MX", Integer.toString(i));
        m94b("MAN", "\"ssdp:discover\"");
    }

    /* renamed from: u */
    public void m658u(String str) {
        String str2 = "239.255.255.250";
        if (C0763a.m38a(str)) {
            str2 = C0799c.m328a();
        }
        m99c(str2, 1900);
    }
}
