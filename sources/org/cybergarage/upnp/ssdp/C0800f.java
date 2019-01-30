package org.cybergarage.upnp.ssdp;

import cz.msebera.android.httpclient.HttpHeaders;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.cybergarage.http.C0768c;
import org.cybergarage.upnp.device.C0781c;
import org.cybergarage.upnp.device.C0782d;
import org.cybergarage.upnp.device.C0783e;
import org.cybergarage.upnp.device.C0786h;
import org.cybergarage.upnp.device.C0789k;

/* renamed from: org.cybergarage.upnp.ssdp.f */
public class C0800f {
    /* renamed from: a */
    public byte[] f99a = null;
    /* renamed from: b */
    private DatagramPacket f100b = null;
    /* renamed from: c */
    private String f101c = "";
    /* renamed from: d */
    private long f102d;

    public C0800f(byte[] bArr, int i) {
        this.f100b = new DatagramPacket(bArr, i);
    }

    /* renamed from: a */
    public DatagramPacket m331a() {
        return this.f100b;
    }

    /* renamed from: a */
    public void m332a(long j) {
        this.f102d = j;
    }

    /* renamed from: a */
    public void m333a(String str) {
        this.f101c = str;
    }

    /* renamed from: b */
    public String m334b() {
        return this.f101c;
    }

    /* renamed from: c */
    public long m335c() {
        return this.f102d;
    }

    /* renamed from: d */
    public String m336d() {
        return m331a().getAddress().getHostAddress();
    }

    /* renamed from: e */
    public int m337e() {
        return m331a().getPort();
    }

    /* renamed from: f */
    public byte[] m338f() {
        if (this.f99a != null) {
            return this.f99a;
        }
        DatagramPacket a = m331a();
        this.f99a = new String(a.getData(), 0, a.getLength()).getBytes();
        return this.f99a;
    }

    /* renamed from: g */
    public String m339g() {
        return C0768c.m61a(m338f(), "HOST");
    }

    /* renamed from: h */
    public String m340h() {
        return C0768c.m61a(m338f(), "Cache-Control");
    }

    /* renamed from: i */
    public String m341i() {
        return C0768c.m61a(m338f(), HttpHeaders.LOCATION);
    }

    /* renamed from: j */
    public String m342j() {
        return C0768c.m61a(m338f(), "MAN");
    }

    /* renamed from: k */
    public String m343k() {
        return C0768c.m61a(m338f(), "ST");
    }

    /* renamed from: l */
    public String m344l() {
        return C0768c.m61a(m338f(), "NT");
    }

    /* renamed from: m */
    public String m345m() {
        return C0768c.m61a(m338f(), "NTS");
    }

    /* renamed from: n */
    public String m346n() {
        return C0768c.m61a(m338f(), "USN");
    }

    /* renamed from: o */
    public int m347o() {
        return C0768c.m62b(m338f(), "MX");
    }

    /* renamed from: p */
    public InetAddress m348p() {
        String str = "127.0.0.1";
        String g = m339g();
        int lastIndexOf = g.lastIndexOf(":");
        if (lastIndexOf >= 0) {
            str = g.substring(0, lastIndexOf);
            if (str.charAt(0) == '[') {
                str = str.substring(1, str.length());
            }
            if (str.charAt(str.length() - 1) == ']') {
                str = str.substring(0, str.length() - 1);
            }
        }
        return new InetSocketAddress(str, 0).getAddress();
    }

    /* renamed from: q */
    public boolean m349q() {
        return (C0782d.m208a(m344l()) || C0786h.m214b(m343k())) ? true : C0789k.m218a(m346n());
    }

    /* renamed from: r */
    public boolean m350r() {
        return C0781c.m207a(m342j());
    }

    /* renamed from: s */
    public boolean m351s() {
        return C0783e.m209a(m345m());
    }

    /* renamed from: t */
    public boolean m352t() {
        return C0783e.m210b(m345m());
    }

    public String toString() {
        return new String(m338f());
    }

    /* renamed from: u */
    public int m353u() {
        return C0799c.m330b(m340h());
    }
}
