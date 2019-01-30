package org.cybergarage.upnp.p003b;

import java.io.File;
import java.net.InetAddress;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.C0962a;
import org.cybergarage.upnp.ssdp.C0799c;
import org.cybergarage.upnp.ssdp.C0800f;
import org.cybergarage.upnp.ssdp.SSDPSearchSocketList;
import org.cybergarage.util.ListenerList;

/* renamed from: org.cybergarage.upnp.b.c */
public class C0958c extends C0778d {
    /* renamed from: a */
    private String f129a = null;
    /* renamed from: b */
    private File f130b = null;
    /* renamed from: c */
    private String f131c = "";
    /* renamed from: d */
    private int f132d = Device.DEFAULT_LEASE_TIME;
    /* renamed from: e */
    private HTTPServerList f133e = null;
    /* renamed from: f */
    private InetAddress[] f134f = null;
    /* renamed from: g */
    private int f135g = Device.HTTP_DEFAULT_PORT;
    /* renamed from: h */
    private ListenerList f136h = new ListenerList();
    /* renamed from: i */
    private SSDPSearchSocketList f137i = null;
    /* renamed from: j */
    private String f138j = "239.255.255.250";
    /* renamed from: k */
    private String f139k = C0799c.m328a();
    /* renamed from: l */
    private int f140l = 1900;
    /* renamed from: m */
    private InetAddress[] f141m = null;
    /* renamed from: n */
    private C0800f f142n = null;
    /* renamed from: o */
    private C0962a f143o = null;

    /* renamed from: a */
    public File m465a() {
        return this.f130b;
    }

    /* renamed from: a */
    public void m466a(int i) {
        this.f132d = i;
    }

    /* renamed from: a */
    public void m467a(File file) {
        this.f130b = file;
    }

    /* renamed from: a */
    public void m468a(String str) {
        this.f129a = str;
    }

    /* renamed from: a */
    public void m469a(C0962a c0962a) {
        this.f143o = c0962a;
    }

    /* renamed from: a */
    public void m470a(C0800f c0800f) {
        this.f142n = c0800f;
    }

    /* renamed from: a */
    public void m471a(InetAddress[] inetAddressArr) {
        this.f134f = inetAddressArr;
    }

    /* renamed from: b */
    public String m472b() {
        return this.f129a;
    }

    /* renamed from: b */
    public void m473b(int i) {
        this.f135g = i;
    }

    /* renamed from: b */
    public void m474b(String str) {
        this.f131c = str;
    }

    /* renamed from: b */
    public void m475b(InetAddress[] inetAddressArr) {
        this.f141m = inetAddressArr;
    }

    /* renamed from: c */
    public String m476c() {
        return this.f131c;
    }

    /* renamed from: c */
    public void m477c(int i) {
        this.f140l = i;
    }

    /* renamed from: c */
    public void m478c(String str) {
        this.f138j = str;
    }

    /* renamed from: d */
    public int m479d() {
        return this.f132d;
    }

    /* renamed from: d */
    public void m480d(String str) {
        this.f139k = str;
    }

    /* renamed from: e */
    public HTTPServerList m481e() {
        if (this.f133e == null) {
            this.f133e = new HTTPServerList(this.f134f, this.f135g);
        }
        return this.f133e;
    }

    /* renamed from: f */
    public InetAddress[] m482f() {
        return this.f134f;
    }

    /* renamed from: g */
    public int m483g() {
        return this.f135g;
    }

    /* renamed from: h */
    public SSDPSearchSocketList m484h() {
        if (this.f137i == null) {
            this.f137i = new SSDPSearchSocketList(this.f141m, this.f140l, this.f138j, this.f139k);
        }
        return this.f137i;
    }

    /* renamed from: i */
    public int m485i() {
        return this.f140l;
    }

    /* renamed from: j */
    public InetAddress[] m486j() {
        return this.f141m;
    }

    /* renamed from: k */
    public String m487k() {
        return this.f138j;
    }

    /* renamed from: l */
    public String m488l() {
        return this.f139k;
    }

    /* renamed from: m */
    public C0800f m489m() {
        return this.f142n;
    }

    /* renamed from: n */
    public C0962a m490n() {
        return this.f143o;
    }
}
