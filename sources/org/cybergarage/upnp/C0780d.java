package org.cybergarage.upnp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.d */
public class C0780d {
    /* renamed from: a */
    private C0802b f72a;
    /* renamed from: b */
    private Object f73b;
    /* renamed from: c */
    private byte[] f74c;

    public C0780d() {
        this(new C0802b("icon"));
    }

    public C0780d(C0802b c0802b) {
        this.f73b = null;
        this.f74c = null;
        this.f72a = c0802b;
    }

    /* renamed from: a */
    public static boolean m197a(C0802b c0802b) {
        return "icon".equals(c0802b.m377c());
    }

    /* renamed from: a */
    public C0802b m198a() {
        return this.f72a;
    }

    /* renamed from: a */
    public void m199a(byte[] bArr) {
        this.f74c = bArr;
    }

    /* renamed from: a */
    public boolean m200a(String str) {
        if (str == null) {
            return false;
        }
        String d = m203d();
        return d != null ? d.equals(str) : false;
    }

    /* renamed from: b */
    public String m201b() {
        return m198a().m398j("mimetype");
    }

    /* renamed from: c */
    public int m202c() {
        try {
            return Integer.parseInt(m198a().m398j("width"));
        } catch (Exception e) {
            return 0;
        }
    }

    /* renamed from: d */
    public String m203d() {
        return m198a().m398j("url");
    }

    /* renamed from: e */
    public boolean m204e() {
        String d = m203d();
        return d != null && d.length() > 0;
    }

    /* renamed from: f */
    public boolean m205f() {
        return this.f74c != null ? true : m204e() ? C0780d.class.getResourceAsStream(m203d()) != null : false;
    }

    /* renamed from: g */
    public byte[] m206g() {
        if (this.f74c == null && m204e()) {
            try {
                InputStream resourceAsStream = C0780d.class.getResourceAsStream(m203d());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int read = resourceAsStream.read();
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(read);
                }
                resourceAsStream.close();
                this.f74c = byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
            }
        }
        return this.f74c;
    }
}
