package org.cybergarage.upnp.ssdp;

import com.memo.TestXlog;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.cybergarage.util.Debug;

/* renamed from: org.cybergarage.upnp.ssdp.b */
public class C0798b {
    /* renamed from: a */
    private DatagramSocket f96a = null;
    /* renamed from: b */
    private String f97b = "";

    public C0798b() {
        m325c();
    }

    public C0798b(String str, int i) {
        m322a(str, i);
    }

    /* renamed from: a */
    public DatagramSocket m320a() {
        return this.f96a;
    }

    /* renamed from: a */
    public void m321a(String str) {
        this.f97b = str;
    }

    /* renamed from: a */
    public boolean m322a(String str, int i) {
        m326d();
        try {
            this.f96a = new DatagramSocket(new InetSocketAddress(InetAddress.getByName(str), i));
            m321a(str);
            return true;
        } catch (Exception e) {
            TestXlog.m29i("#### HTTPUSockety BindException ");
            Debug.warning(e);
            throw e;
        } catch (Exception e2) {
            TestXlog.m29i("#### HTTPUSockety exception ");
            Debug.warning(e2);
            return false;
        }
    }

    /* renamed from: a */
    public boolean m323a(String str, int i, String str2) {
        try {
            this.f96a.send(new DatagramPacket(str2.getBytes(), str2.length(), InetAddress.getByName(str), i));
            return true;
        } catch (Exception e) {
            TestXlog.m29i("#### HTTPUSockety post exception:" + e.getMessage());
            Debug.warning(e);
            if (this.f96a != null) {
                Debug.warning("addr = " + this.f96a.getLocalAddress().getHostName());
                Debug.warning("port = " + this.f96a.getLocalPort());
            }
            return false;
        }
    }

    /* renamed from: b */
    public String m324b() {
        return this.f97b.length() > 0 ? this.f97b : this.f96a.getLocalAddress().getHostAddress();
    }

    /* renamed from: c */
    public boolean m325c() {
        m326d();
        try {
            this.f96a = new DatagramSocket();
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    /* renamed from: d */
    public boolean m326d() {
        if (this.f96a == null) {
            return true;
        }
        try {
            this.f96a.close();
            this.f96a = null;
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    /* renamed from: e */
    public C0800f m327e() {
        byte[] bArr = new byte[1024];
        C0800f c0800f = new C0800f(bArr, bArr.length);
        c0800f.m333a(m324b());
        try {
            this.f96a.receive(c0800f.m331a());
            c0800f.m332a(System.currentTimeMillis());
            return c0800f;
        } catch (Exception e) {
            return null;
        }
    }

    protected void finalize() {
        m326d();
    }
}
