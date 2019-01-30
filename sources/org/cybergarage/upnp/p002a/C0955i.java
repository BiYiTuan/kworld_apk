package org.cybergarage.upnp.p002a;

import org.cybergarage.upnp.C0961c;
import org.cybergarage.util.ThreadCore;

/* renamed from: org.cybergarage.upnp.a.i */
public class C0955i extends ThreadCore {
    /* renamed from: a */
    private C0961c f125a;

    public C0955i(C0961c c0961c) {
        m459a(c0961c);
    }

    /* renamed from: a */
    public C0961c m458a() {
        return this.f125a;
    }

    /* renamed from: a */
    public void m459a(C0961c c0961c) {
        this.f125a = c0961c;
    }

    public void run() {
        C0961c a = m458a();
        while (isRunnable()) {
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
            }
            a.m540c(false);
        }
    }
}
