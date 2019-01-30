package org.cybergarage.upnp.device;

import org.cybergarage.upnp.C0961c;
import org.cybergarage.util.ThreadCore;

/* renamed from: org.cybergarage.upnp.device.b */
public class C0963b extends ThreadCore {
    /* renamed from: a */
    private C0961c f173a;

    public C0963b(C0961c c0961c) {
        m555a(c0961c);
    }

    /* renamed from: a */
    public C0961c m554a() {
        return this.f173a;
    }

    /* renamed from: a */
    public void m555a(C0961c c0961c) {
        this.f173a = c0961c;
    }

    public void run() {
        C0961c a = m554a();
        long f = a.m545f() * 1000;
        while (isRunnable()) {
            try {
                Thread.sleep(f);
            } catch (InterruptedException e) {
            }
            a.m544e();
        }
    }
}
