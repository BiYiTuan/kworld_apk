package org.cybergarage.upnp.ssdp;

import java.io.IOException;
import org.cybergarage.p000a.C0763a;
import org.cybergarage.upnp.device.C0787i;
import org.cybergarage.util.ListenerList;

/* renamed from: org.cybergarage.upnp.ssdp.l */
public class C0967l extends C0797a implements Runnable {
    /* renamed from: a */
    private boolean f183a;
    /* renamed from: b */
    private ListenerList f184b = new ListenerList();
    /* renamed from: c */
    private Thread f185c = null;

    public C0967l(String str, int i, String str2) {
        m589a(str, str2);
    }

    /* renamed from: a */
    public void m587a(C0787i c0787i) {
        this.f184b.add(c0787i);
    }

    /* renamed from: a */
    public void m588a(C0800f c0800f) {
        int size = this.f184b.size();
        for (int i = 0; i < size; i++) {
            ((C0787i) this.f184b.get(i)).deviceSearchReceived(c0800f);
        }
    }

    /* renamed from: a */
    public boolean m589a(String str, String str2) {
        if (C0763a.m38a(str) && C0763a.m38a(str2)) {
            this.f183a = true;
        } else if (C0763a.m41b(str) && C0763a.m41b(str2)) {
            this.f183a = false;
        } else {
            throw new IllegalArgumentException("Cannot open a UDP Socket for IPv6 address on IPv4 interface or viceversa");
        }
        return m310a(str2, 1900, str);
    }

    /* renamed from: h */
    public void m590h() {
        StringBuffer stringBuffer = new StringBuffer("Cyber.SSDPSearchSocket/");
        String a = m309a();
        if (a != null && a.length() > 0) {
            stringBuffer.append(m309a()).append(':');
            stringBuffer.append(m315c()).append(" -> ");
            stringBuffer.append(m317e()).append(':');
            stringBuffer.append(m314b());
        }
        this.f185c = new Thread(this, stringBuffer.toString());
        this.f185c.start();
    }

    /* renamed from: i */
    public void m591i() {
        m318f();
        this.f185c = null;
    }

    public void run() {
        Thread currentThread = Thread.currentThread();
        while (this.f185c == currentThread) {
            Thread.yield();
            try {
                C0800f g = m319g();
                if (g != null && g.m350r()) {
                    m588a(g);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
