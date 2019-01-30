package org.cybergarage.upnp.event;

import com.google.android.exoplayer.MediaFormat;
import java.net.URL;

/* renamed from: org.cybergarage.upnp.event.d */
public class C0793d {
    /* renamed from: a */
    private String f80a = null;
    /* renamed from: b */
    private String f81b = "";
    /* renamed from: c */
    private String f82c = "";
    /* renamed from: d */
    private String f83d = "";
    /* renamed from: e */
    private String f84e = "";
    /* renamed from: f */
    private int f85f = 0;
    /* renamed from: g */
    private long f86g = 0;
    /* renamed from: h */
    private long f87h = 0;
    /* renamed from: i */
    private long f88i = 0;

    public C0793d() {
        m288k();
    }

    /* renamed from: a */
    public String m273a() {
        return this.f80a;
    }

    /* renamed from: a */
    public void m274a(int i) {
        this.f88i = (long) i;
    }

    /* renamed from: a */
    public void m275a(long j) {
        this.f86g = j;
    }

    /* renamed from: a */
    public void m276a(String str) {
        this.f80a = str;
    }

    /* renamed from: b */
    public String m277b() {
        return this.f82c;
    }

    /* renamed from: b */
    public void m278b(long j) {
        this.f87h = j;
    }

    /* renamed from: b */
    public void m279b(String str) {
        this.f82c = str;
        try {
            URL url = new URL(str);
            this.f83d = url.getHost();
            this.f84e = url.getPath();
            this.f85f = url.getPort();
        } catch (Exception e) {
        }
    }

    /* renamed from: c */
    public String m280c() {
        return this.f83d;
    }

    /* renamed from: d */
    public String m281d() {
        return this.f84e;
    }

    /* renamed from: e */
    public int m282e() {
        return this.f85f;
    }

    /* renamed from: f */
    public long m283f() {
        return this.f86g;
    }

    /* renamed from: g */
    public boolean m284g() {
        return this.f86g != -1 && m285h() + (m283f() * 1000) < System.currentTimeMillis();
    }

    /* renamed from: h */
    public long m285h() {
        return this.f87h;
    }

    /* renamed from: i */
    public long m286i() {
        return this.f88i;
    }

    /* renamed from: j */
    public void m287j() {
        if (this.f88i == MediaFormat.OFFSET_SAMPLE_RELATIVE) {
            this.f88i = 1;
        } else {
            this.f88i++;
        }
    }

    /* renamed from: k */
    public void m288k() {
        m278b(System.currentTimeMillis());
        m274a(0);
    }
}
