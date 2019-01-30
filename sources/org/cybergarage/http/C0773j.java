package org.cybergarage.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;

/* renamed from: org.cybergarage.http.j */
public class C0773j {
    /* renamed from: a */
    private Socket f57a = null;
    /* renamed from: b */
    private InputStream f58b = null;
    /* renamed from: c */
    private OutputStream f59c = null;

    public C0773j(Socket socket) {
        m144a(socket);
        m152d();
    }

    /* renamed from: a */
    private void m144a(Socket socket) {
        this.f57a = socket;
    }

    /* renamed from: a */
    private boolean m145a(C0954g c0954g, InputStream inputStream, long j, long j2, boolean z) {
        c0954g.m81a(Calendar.getInstance());
        OutputStream f = m147f();
        try {
            c0954g.m73a(j2);
            f.write(c0954g.mo2463D().getBytes());
            f.write("\r\n".getBytes());
            if (z) {
                f.flush();
                return true;
            }
            boolean z2 = c0954g.m133z();
            if (0 < j) {
                inputStream.skip(j);
            }
            int a = C0767b.m51a();
            byte[] bArr = new byte[a];
            long j3 = 0;
            int read = inputStream.read(bArr, 0, (int) (((long) a) < j2 ? (long) a : j2));
            while (read > 0 && j3 < j2) {
                if (z2) {
                    f.write(Long.toHexString((long) read).getBytes());
                    f.write("\r\n".getBytes());
                }
                f.write(bArr, 0, read);
                if (z2) {
                    f.write("\r\n".getBytes());
                }
                j3 += (long) read;
                read = inputStream.read(bArr, 0, (int) (((long) a) < j2 - j3 ? (long) a : j2 - j3));
            }
            if (z2) {
                f.write("0".getBytes());
                f.write("\r\n".getBytes());
            }
            f.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* renamed from: a */
    private boolean m146a(C0954g c0954g, byte[] bArr, long j, long j2, boolean z) {
        c0954g.m81a(Calendar.getInstance());
        OutputStream f = m147f();
        try {
            c0954g.m73a(j2);
            f.write(c0954g.mo2463D().getBytes());
            f.write("\r\n".getBytes());
            if (z) {
                f.flush();
                return true;
            }
            boolean z2 = c0954g.m133z();
            if (z2) {
                f.write(Long.toHexString(j2).getBytes());
                f.write("\r\n".getBytes());
            }
            f.write(bArr, (int) j, (int) j2);
            if (z2) {
                f.write("\r\n".getBytes());
                f.write("0".getBytes());
                f.write("\r\n".getBytes());
            }
            f.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* renamed from: f */
    private OutputStream m147f() {
        return this.f59c;
    }

    /* renamed from: a */
    public Socket m148a() {
        return this.f57a;
    }

    /* renamed from: a */
    public boolean m149a(C0954g c0954g, long j, long j2, boolean z) {
        if (c0954g.m119l()) {
            return m145a(c0954g, c0954g.m116k(), j, j2, z);
        }
        return m146a(c0954g, c0954g.m111h(), j, j2, z);
    }

    /* renamed from: b */
    public String m150b() {
        return m148a().getLocalAddress().getHostAddress();
    }

    /* renamed from: c */
    public InputStream m151c() {
        return this.f58b;
    }

    /* renamed from: d */
    public boolean m152d() {
        Socket a = m148a();
        try {
            this.f58b = a.getInputStream();
            this.f59c = a.getOutputStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* renamed from: e */
    public boolean m153e() {
        try {
            if (this.f58b != null) {
                this.f58b.close();
            }
            if (this.f59c != null) {
                this.f59c.close();
            }
            m148a().close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void finalize() {
        m153e();
    }
}
