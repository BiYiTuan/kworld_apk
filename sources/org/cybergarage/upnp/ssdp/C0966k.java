package org.cybergarage.upnp.ssdp;

import com.memo.TestXlog;
import java.net.DatagramSocket;
import org.cybergarage.upnp.C0961c;

/* renamed from: org.cybergarage.upnp.ssdp.k */
public class C0966k extends C0798b implements Runnable {
    /* renamed from: a */
    private C0961c f181a = null;
    /* renamed from: b */
    private Thread f182b = null;

    public C0966k() {
        m581a(null);
    }

    public C0966k(String str, int i) {
        super(str, i);
        m581a(null);
    }

    /* renamed from: a */
    public void m581a(C0961c c0961c) {
        this.f181a = c0961c;
    }

    /* renamed from: a */
    public boolean m582a(String str, int i, C0999i c0999i) {
        TestXlog.m29i("#### SSDPSearchResponseSocket post host:" + str + ",port is " + i);
        return m323a(str, i, c0999i.toString());
    }

    /* renamed from: a */
    public boolean m583a(String str, int i, C1000j c1000j) {
        return m323a(str, i, c1000j.mo2463D());
    }

    /* renamed from: f */
    public C0961c m584f() {
        return this.f181a;
    }

    /* renamed from: g */
    public void m585g() {
        StringBuffer stringBuffer = new StringBuffer("Cyber.SSDPSearchResponseSocket/");
        DatagramSocket a = m320a();
        if (a.getLocalAddress() != null) {
            stringBuffer.append(a.getLocalAddress()).append(':');
            stringBuffer.append(a.getLocalPort());
        }
        TestXlog.m27d("new deviceSearchResponseThread,name is " + stringBuffer.toString());
        this.f182b = new Thread(this, stringBuffer.toString());
        this.f182b.start();
    }

    /* renamed from: h */
    public void m586h() {
        this.f182b = null;
    }

    public void run() {
        Thread currentThread = Thread.currentThread();
        C0961c f = m584f();
        while (this.f182b == currentThread) {
            Thread.yield();
            C0800f e = m327e();
            if (e != null) {
                if (f != null) {
                    f.m543d(e);
                }
                TestXlog.m29i("#### Socket searchResponseReceived ######,search response packet ");
            }
        }
    }
}
