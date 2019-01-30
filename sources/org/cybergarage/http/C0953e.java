package org.cybergarage.http;

import com.memo.TestXlog;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.protocol.HTTP;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import org.cybergarage.util.Debug;
import org.cybergarage.util.StringUtil;

/* renamed from: org.cybergarage.http.e */
public class C0953e extends C0769d {
    /* renamed from: b */
    private String f118b = null;
    /* renamed from: c */
    private String f119c = null;
    /* renamed from: d */
    private String f120d = "";
    /* renamed from: e */
    private int f121e = -1;
    /* renamed from: f */
    private C0773j f122f = null;
    /* renamed from: g */
    private Socket f123g = null;

    public C0953e() {
        m75a("1.0");
    }

    /* renamed from: A */
    public String m419A() {
        return this.f118b != null ? this.f118b : m70a(0);
    }

    /* renamed from: B */
    public boolean m420B() {
        return m449n("GET");
    }

    /* renamed from: C */
    public boolean m421C() {
        return m449n(HttpPost.METHOD_NAME);
    }

    /* renamed from: D */
    public boolean m422D() {
        return m449n("HEAD");
    }

    /* renamed from: E */
    public boolean m423E() {
        return m449n("SUBSCRIBE");
    }

    /* renamed from: F */
    public boolean m424F() {
        return m449n("UNSUBSCRIBE");
    }

    /* renamed from: G */
    public boolean m425G() {
        return m449n("NOTIFY");
    }

    /* renamed from: H */
    public String m426H() {
        return this.f119c != null ? this.f119c : m70a(1);
    }

    /* renamed from: I */
    public boolean m427I() {
        return m101c("SOAPACTION");
    }

    /* renamed from: J */
    public String m428J() {
        return this.f120d;
    }

    /* renamed from: K */
    public int m429K() {
        return this.f121e;
    }

    /* renamed from: L */
    public C0773j m430L() {
        return this.f122f;
    }

    /* renamed from: M */
    public String m431M() {
        return m430L().m150b();
    }

    /* renamed from: N */
    public String m432N() {
        return m103d() ? m70a(2) : "HTTP/" + super.m89b();
    }

    /* renamed from: O */
    public String m433O() {
        return m419A() + " " + m426H() + " " + m432N() + "\r\n";
    }

    /* renamed from: P */
    public String m434P() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(m433O());
        stringBuffer.append(m108g());
        return stringBuffer.toString();
    }

    /* renamed from: Q */
    public boolean m435Q() {
        if (m125r()) {
            return false;
        }
        if (m126s()) {
            return true;
        }
        return !(m432N().indexOf("1.0") > 0);
    }

    /* renamed from: R */
    public boolean m436R() {
        return super.m96b(m430L());
    }

    /* renamed from: S */
    public boolean m437S() {
        return m447e(200);
    }

    /* renamed from: T */
    public boolean m438T() {
        return m447e(400);
    }

    /* renamed from: U */
    public void mo2461U() {
        System.out.println(toString());
    }

    /* renamed from: a */
    public C0954g m440a(String str, int i, boolean z) {
        return m441a(str, i, false, z);
    }

    /* renamed from: a */
    public C0954g m441a(String str, int i, boolean z, boolean z2) {
        OutputStream outputStream;
        InputStream inputStream;
        Exception e;
        Throwable th;
        C0954g c0954g = new C0954g(z2);
        m118l(str);
        m114j(z ? HTTP.CONN_KEEP_ALIVE : "close");
        boolean D = m422D();
        try {
            if (this.f123g == null) {
                this.f123g = new Socket();
                this.f123g.connect(new InetSocketAddress(str, i), 80000);
            }
            outputStream = this.f123g.getOutputStream();
            try {
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(m434P());
                printStream.print("\r\n");
                boolean z3 = m133z();
                String i2 = m112i();
                TestXlog.i2(i2);
                byte[] edsReturnBuffer = z2 ? StringUtil.edsReturnBuffer(new String(i2.getBytes(), "UTF-8")) : i2.getBytes();
                int i3 = 0;
                if (edsReturnBuffer != null) {
                    i3 = edsReturnBuffer.length;
                }
                if (i3 > 0) {
                    if (z3) {
                        printStream.print(Long.toHexString((long) i3));
                        printStream.print("\r\n");
                    }
                    printStream.write(edsReturnBuffer);
                    if (z3) {
                        printStream.print("\r\n");
                    }
                }
                if (z3) {
                    printStream.print("0");
                    printStream.print("\r\n");
                }
                printStream.flush();
                inputStream = this.f123g.getInputStream();
                try {
                    c0954g.m87a(inputStream, D);
                    if (!z) {
                        try {
                            inputStream.close();
                        } catch (Exception e2) {
                        }
                        if (inputStream != null) {
                            try {
                                outputStream.close();
                            } catch (Exception e3) {
                            }
                        }
                        if (outputStream != null) {
                            try {
                                this.f123g.close();
                            } catch (Exception e4) {
                            }
                        }
                        this.f123g = null;
                    }
                } catch (SocketException e5) {
                    e = e5;
                    try {
                        c0954g.m457d(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                        Debug.warning(e);
                        if (!z) {
                            try {
                                inputStream.close();
                            } catch (Exception e6) {
                            }
                            if (inputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (Exception e7) {
                                }
                            }
                            if (outputStream != null) {
                                try {
                                    this.f123g.close();
                                } catch (Exception e8) {
                                }
                            }
                            this.f123g = null;
                        }
                        return c0954g;
                    } catch (Throwable th2) {
                        th = th2;
                        if (!z) {
                            try {
                                inputStream.close();
                            } catch (Exception e9) {
                            }
                            if (inputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (Exception e10) {
                                }
                            }
                            if (outputStream != null) {
                                try {
                                    this.f123g.close();
                                } catch (Exception e11) {
                                }
                            }
                            this.f123g = null;
                        }
                        throw th;
                    }
                } catch (IOException e12) {
                    e = e12;
                    c0954g.m457d(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                    Debug.warning(e);
                    if (!z) {
                        try {
                            inputStream.close();
                        } catch (Exception e13) {
                        }
                        if (inputStream != null) {
                            try {
                                outputStream.close();
                            } catch (Exception e14) {
                            }
                        }
                        if (outputStream != null) {
                            try {
                                this.f123g.close();
                            } catch (Exception e15) {
                            }
                        }
                        this.f123g = null;
                    }
                    return c0954g;
                }
            } catch (SocketException e16) {
                e = e16;
                inputStream = null;
                c0954g.m457d(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                Debug.warning(e);
                if (z) {
                    inputStream.close();
                    if (inputStream != null) {
                        outputStream.close();
                    }
                    if (outputStream != null) {
                        this.f123g.close();
                    }
                    this.f123g = null;
                }
                return c0954g;
            } catch (IOException e17) {
                e = e17;
                inputStream = null;
                c0954g.m457d(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                Debug.warning(e);
                if (z) {
                    inputStream.close();
                    if (inputStream != null) {
                        outputStream.close();
                    }
                    if (outputStream != null) {
                        this.f123g.close();
                    }
                    this.f123g = null;
                }
                return c0954g;
            } catch (Throwable th3) {
                th = th3;
                inputStream = null;
                if (z) {
                    inputStream.close();
                    if (inputStream != null) {
                        outputStream.close();
                    }
                    if (outputStream != null) {
                        this.f123g.close();
                    }
                    this.f123g = null;
                }
                throw th;
            }
        } catch (SocketException e18) {
            e = e18;
            inputStream = null;
            outputStream = null;
            c0954g.m457d(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            Debug.warning(e);
            if (z) {
                inputStream.close();
                if (inputStream != null) {
                    outputStream.close();
                }
                if (outputStream != null) {
                    this.f123g.close();
                }
                this.f123g = null;
            }
            return c0954g;
        } catch (IOException e19) {
            e = e19;
            inputStream = null;
            outputStream = null;
            c0954g.m457d(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            Debug.warning(e);
            if (z) {
                inputStream.close();
                if (inputStream != null) {
                    outputStream.close();
                }
                if (outputStream != null) {
                    this.f123g.close();
                }
                this.f123g = null;
            }
            return c0954g;
        } catch (Throwable th4) {
            th = th4;
            inputStream = null;
            outputStream = null;
            if (z) {
                inputStream.close();
                if (inputStream != null) {
                    outputStream.close();
                }
                if (outputStream != null) {
                    this.f123g.close();
                }
                this.f123g = null;
            }
            throw th;
        }
        return c0954g;
    }

    /* renamed from: a */
    public void m442a(C0953e c0953e) {
        m83a((C0769d) c0953e);
        m445c(c0953e.m430L());
    }

    /* renamed from: a */
    public boolean m443a(C0954g c0954g) {
        long v;
        long j;
        C0773j L = m430L();
        long o = c0954g.m122o();
        if (m127t()) {
            v = m129v();
            long w = m130w();
            j = w <= 0 ? o - 1 : w;
            if (v > o || j > o) {
                return m447e(HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            }
            c0954g.m74a(v, j, o);
            c0954g.m457d(HttpStatus.SC_PARTIAL_CONTENT);
            j = (j - v) + 1;
        } else {
            v = 0;
            j = o;
        }
        return L.m149a(c0954g, v, j, m422D());
    }

    /* renamed from: b */
    public void m444b(String str, boolean z) {
        this.f119c = str;
        if (z) {
            this.f119c = C0767b.m58d(this.f119c);
        }
    }

    /* renamed from: c */
    public void m445c(C0773j c0773j) {
        this.f122f = c0773j;
    }

    /* renamed from: d */
    public void m446d(int i) {
        this.f121e = i;
    }

    /* renamed from: e */
    public boolean m447e(int i) {
        C0954g c0954g = new C0954g();
        c0954g.m457d(i);
        c0954g.m73a(0);
        return m443a(c0954g);
    }

    /* renamed from: m */
    public void mo2100m(String str) {
        this.f118b = str;
    }

    /* renamed from: n */
    public boolean m449n(String str) {
        String A = m419A();
        return A == null ? false : A.equalsIgnoreCase(str);
    }

    /* renamed from: o */
    public void m450o(String str) {
        m444b(str, false);
    }

    /* renamed from: p */
    public void m451p(String str) {
        this.f120d = str;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(m434P());
        stringBuffer.append("\r\n");
        stringBuffer.append(m112i());
        return stringBuffer.toString();
    }
}
