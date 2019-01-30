package org.cybergarage.upnp.ssdp;

import com.memo.TestXlog;
import java.io.IOException;
import java.net.InetAddress;
import org.cybergarage.p000a.C0763a;
import org.cybergarage.upnp.C0961c;
import org.cybergarage.util.Debug;

/* renamed from: org.cybergarage.upnp.ssdp.e */
public class C0965e extends C0797a implements Runnable {
    /* renamed from: a */
    private boolean f178a;
    /* renamed from: b */
    private C0961c f179b = null;
    /* renamed from: c */
    private Thread f180c = null;

    public C0965e(String str) {
        String str2 = "239.255.255.250";
        this.f178a = false;
        if (C0763a.m38a(str)) {
            str2 = C0799c.m328a();
            this.f178a = true;
        }
        m310a(str2, 1900, str);
        m576a(null);
    }

    /* renamed from: a */
    public void m576a(C0961c c0961c) {
        this.f179b = c0961c;
    }

    /* renamed from: a */
    public boolean m577a(C0998d c0998d) {
        String str = "239.255.255.250";
        if (this.f178a) {
            str = C0799c.m328a();
        }
        c0998d.m99c(str, 1900);
        return m313a(c0998d);
    }

    /* renamed from: h */
    public C0961c m578h() {
        return this.f179b;
    }

    /* renamed from: i */
    public void m579i() {
        StringBuffer stringBuffer = new StringBuffer("Cyber.SSDPNotifySocket/");
        String a = m309a();
        if (a != null && a.length() > 0) {
            stringBuffer.append(m309a()).append(':');
            stringBuffer.append(m315c()).append(" -> ");
            stringBuffer.append(m317e()).append(':');
            stringBuffer.append(m314b());
        }
        TestXlog.m27d("DeviceService deviceNotifyThread start");
        this.f180c = new Thread(this, stringBuffer.toString());
        this.f180c.start();
    }

    /* renamed from: j */
    public void m580j() {
        m318f();
        this.f180c = null;
    }

    public void run() {
        Thread currentThread = Thread.currentThread();
        C0961c h = m578h();
        while (this.f180c == currentThread) {
            Thread.yield();
            try {
                C0800f g = m319g();
                if (g != null) {
                    InetAddress d = m316d();
                    InetAddress p = g.m348p();
                    if (d.equals(p)) {
                        if (new String(g.m338f()).startsWith("NOTIFY")) {
                            TestXlog.m29i("####Socket Notify package ######");
                        }
                        if (h != null) {
                            h.m539c(g);
                        }
                    } else {
                        Debug.warning("Invalidate Multicast Received from IP " + d + " on " + p);
                        TestXlog.m29i("#### Error Invalidate Multicast Received from IP " + d + " on " + p);
                    }
                }
            } catch (IOException e) {
                return;
            }
        }
    }
}
