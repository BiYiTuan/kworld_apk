package org.cybergarage.http;

import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.protocol.HTTP;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;
import net.lingala.zip4j.util.InternalZipConstants;
import org.cybergarage.p000a.C0763a;
import org.cybergarage.util.Debug;
import org.cybergarage.util.StringUtil;

/* renamed from: org.cybergarage.http.d */
public class C0769d {
    /* renamed from: a */
    protected boolean f43a;
    /* renamed from: b */
    private String f44b;
    /* renamed from: c */
    private String f45c;
    /* renamed from: d */
    private Vector f46d;
    /* renamed from: e */
    private byte[] f47e;
    /* renamed from: f */
    private InputStream f48f;

    public C0769d() {
        this.f43a = false;
        this.f45c = "";
        this.f46d = new Vector();
        this.f47e = new byte[0];
        this.f48f = null;
        m75a("1.1");
        m92b(null);
    }

    public C0769d(boolean z) {
        this.f43a = false;
        this.f45c = "";
        this.f46d = new Vector();
        this.f47e = new byte[0];
        this.f48f = null;
        this.f43a = z;
    }

    /* renamed from: a */
    private String m68a(BufferedInputStream bufferedInputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1];
        try {
            int read = bufferedInputStream.read(bArr);
            while (read > 0 && bArr[0] != (byte) 10) {
                if (bArr[0] != (byte) 13) {
                    byteArrayOutputStream.write(bArr[0]);
                }
                read = bufferedInputStream.read(bArr);
            }
        } catch (InterruptedIOException e) {
        } catch (Exception e2) {
            Debug.warning(e2);
        }
        return byteArrayOutputStream.toString();
    }

    /* renamed from: m */
    private void mo2100m(String str) {
        this.f45c = str;
    }

    /* renamed from: a */
    protected String m70a(int i) {
        StringTokenizer stringTokenizer = new StringTokenizer(this.f45c, " ");
        String str = "";
        int i2 = 0;
        while (i2 <= i) {
            if (!stringTokenizer.hasMoreTokens()) {
                return "";
            }
            i2++;
            str = stringTokenizer.nextToken();
        }
        return str;
    }

    /* renamed from: a */
    public String m71a(String str, String str2, String str3) {
        String d = m102d(str);
        if (d.startsWith(str2)) {
            d = d.substring(1, d.length());
        }
        return d.endsWith(str3) ? d.substring(0, d.length() - 1) : d;
    }

    /* renamed from: a */
    public void m72a() {
        mo2100m("");
        m107f();
        m85a(new byte[0], false);
        m92b(null);
    }

    /* renamed from: a */
    public void m73a(long j) {
        m77a("Content-Length", j);
    }

    /* renamed from: a */
    public void m74a(long j, long j2, long j3) {
        m94b("Content-Range", ((("" + "bytes ") + Long.toString(j) + "-") + Long.toString(j2) + InternalZipConstants.ZIP_FILE_SEPARATOR) + (0 < j3 ? Long.toString(j3) : "*"));
    }

    /* renamed from: a */
    public void m75a(String str) {
        this.f44b = str;
    }

    /* renamed from: a */
    public void m76a(String str, int i) {
        m94b(str, Integer.toString(i));
    }

    /* renamed from: a */
    public void m77a(String str, long j) {
        m94b(str, Long.toString(j));
    }

    /* renamed from: a */
    public void m78a(String str, String str2) {
        this.f46d.add(new C0768c(str, str2));
    }

    /* renamed from: a */
    public void m79a(String str, String str2, String str3, String str4) {
        if (!str2.startsWith(str3)) {
            str2 = str3 + str2;
        }
        if (!str2.endsWith(str4)) {
            str2 = str2 + str4;
        }
        m94b(str, str2);
    }

    /* renamed from: a */
    public void m80a(String str, boolean z) {
        m85a(str.getBytes(), z);
    }

    /* renamed from: a */
    public void m81a(Calendar calendar) {
        m94b("Date", new C0766a(calendar).m50b());
    }

    /* renamed from: a */
    public void m82a(C0768c c0768c) {
        this.f46d.add(c0768c);
    }

    /* renamed from: a */
    protected void m83a(C0769d c0769d) {
        mo2100m(c0769d.m97c());
        m107f();
        int e = c0769d.m104e();
        for (int i = 0; i < e; i++) {
            m82a(c0769d.m90b(i));
        }
        m84a(c0769d.m111h());
    }

    /* renamed from: a */
    public void m84a(byte[] bArr) {
        m85a(bArr, true);
    }

    /* renamed from: a */
    public void m85a(byte[] bArr, boolean z) {
        this.f47e = bArr;
        if (z) {
            m73a((long) bArr.length);
        }
    }

    /* renamed from: a */
    protected boolean m86a(InputStream inputStream) {
        return m87a(inputStream, false);
    }

    /* renamed from: a */
    protected boolean m87a(InputStream inputStream, boolean z) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            String a = m68a(bufferedInputStream);
            if (a == null || a.length() <= 0) {
                return false;
            }
            C0768c c0768c;
            mo2100m(a);
            if (new C0774k(a).m156a() == 100) {
                a = m68a(bufferedInputStream);
                while (a != null && a.length() > 0) {
                    c0768c = new C0768c(a);
                    if (c0768c.m67c()) {
                        m95b(c0768c);
                    }
                    a = m68a(bufferedInputStream);
                }
                a = m68a(bufferedInputStream);
                if (a == null || a.length() <= 0) {
                    return true;
                }
                mo2100m(a);
            }
            a = m68a(bufferedInputStream);
            while (a != null && a.length() > 0) {
                c0768c = new C0768c(a);
                if (c0768c.m67c()) {
                    m95b(c0768c);
                }
                a = m68a(bufferedInputStream);
            }
            if (z) {
                m80a("", false);
                return true;
            }
            boolean z2 = m133z();
            long j = 0;
            if (z2) {
                try {
                    a = m68a(bufferedInputStream);
                    j = a != null ? Long.parseLong(a.trim(), 16) : 0;
                } catch (Exception e) {
                }
            } else {
                j = m122o();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (0 < j) {
                long j2;
                int a2 = C0767b.m51a();
                byte[] bArr = new byte[((int) (j > ((long) a2) ? (long) a2 : j))];
                long j3 = 0;
                while (j3 < j) {
                    j2 = j - j3;
                    if (((long) a2) < j2) {
                        j2 = (long) a2;
                    }
                    int read = bufferedInputStream.read(bArr, 0, (int) j2);
                    if (read < 0) {
                        break;
                    }
                    try {
                        byteArrayOutputStream.write(bArr, 0, read);
                        j3 = ((long) read) + j3;
                    } catch (Exception e2) {
                        Debug.warning(e2);
                    }
                }
                if (z2) {
                    j2 = 0;
                    do {
                        j3 = bufferedInputStream.skip(((long) "\r\n".length()) - j2);
                        if (j3 >= 0) {
                            j2 += j3;
                        }
                        break;
                    } while (j2 < ((long) "\r\n".length()));
                    try {
                        a = m68a(bufferedInputStream);
                        j2 = Long.parseLong(new String(a.getBytes(), 0, a.length() - 2), 16);
                    } catch (Exception e3) {
                        j2 = 0;
                    }
                } else {
                    j2 = 0;
                }
                j = j2;
            }
            if (this.f43a) {
                m85a(StringUtil.eds(byteArrayOutputStream.toByteArray()), false);
            } else {
                m85a(byteArrayOutputStream.toByteArray(), false);
            }
            return true;
        } catch (Exception e22) {
            Debug.warning(e22);
            return false;
        }
    }

    /* renamed from: a */
    protected boolean m88a(C0773j c0773j) {
        return m86a(c0773j.m151c());
    }

    /* renamed from: b */
    public String m89b() {
        return this.f44b;
    }

    /* renamed from: b */
    public C0768c m90b(int i) {
        return (C0768c) this.f46d.get(i);
    }

    /* renamed from: b */
    public C0768c m91b(String str) {
        int e = m104e();
        for (int i = 0; i < e; i++) {
            C0768c b = m90b(i);
            if (b.m63a().equalsIgnoreCase(str)) {
                return b;
            }
        }
        return null;
    }

    /* renamed from: b */
    public void m92b(InputStream inputStream) {
        this.f48f = inputStream;
    }

    /* renamed from: b */
    public void m93b(String str, int i) {
        m94b("Cache-Control", str + "=" + Integer.toString(i));
    }

    /* renamed from: b */
    public void m94b(String str, String str2) {
        C0768c b = m91b(str);
        if (b != null) {
            b.m66b(str2);
        } else {
            m78a(str, str2);
        }
    }

    /* renamed from: b */
    public void m95b(C0768c c0768c) {
        m94b(c0768c.m63a(), c0768c.m65b());
    }

    /* renamed from: b */
    public boolean m96b(C0773j c0773j) {
        m72a();
        return m88a(c0773j);
    }

    /* renamed from: c */
    protected String m97c() {
        return this.f45c;
    }

    /* renamed from: c */
    public void m98c(int i) {
        m93b("max-age", i);
    }

    /* renamed from: c */
    public void m99c(String str, int i) {
        if (C0763a.m38a(str)) {
            str = "[" + str + "]";
        }
        m94b("HOST", str + ":" + Integer.toString(i));
    }

    /* renamed from: c */
    public void m100c(String str, String str2) {
        m79a(str, str2, "\"", "\"");
    }

    /* renamed from: c */
    public boolean m101c(String str) {
        return m91b(str) != null;
    }

    /* renamed from: d */
    public String m102d(String str) {
        C0768c b = m91b(str);
        return b == null ? "" : b.m65b();
    }

    /* renamed from: d */
    public boolean m103d() {
        return this.f45c.length() > 0;
    }

    /* renamed from: e */
    public int m104e() {
        return this.f46d.size();
    }

    /* renamed from: e */
    public String m105e(String str) {
        return m71a(str, "\"", "\"");
    }

    /* renamed from: f */
    public long m106f(String str) {
        C0768c b = m91b(str);
        return b == null ? 0 : StringUtil.toLong(b.m65b());
    }

    /* renamed from: f */
    public void m107f() {
        this.f46d.clear();
        this.f46d = new Vector();
    }

    /* renamed from: g */
    public String m108g() {
        StringBuffer stringBuffer = new StringBuffer();
        int e = m104e();
        for (int i = 0; i < e; i++) {
            C0768c b = m90b(i);
            stringBuffer.append(b.m63a() + ": " + b.m65b() + "\r\n");
        }
        return stringBuffer.toString();
    }

    /* renamed from: g */
    public void m109g(String str) {
        m80a(str, true);
    }

    /* renamed from: h */
    public void m110h(String str) {
        m94b("Content-Type", str);
    }

    /* renamed from: h */
    public byte[] m111h() {
        return this.f47e;
    }

    /* renamed from: i */
    public String m112i() {
        String n = m121n();
        if (n == null || n.length() <= 0) {
            return new String(this.f47e);
        }
        try {
            return new String(this.f47e, n);
        } catch (Exception e) {
            Debug.warning(e);
            return new String(this.f47e);
        }
    }

    /* renamed from: i */
    public void m113i(String str) {
        m94b(HttpHeaders.CONTENT_LANGUAGE, str);
    }

    /* renamed from: j */
    public void m114j(String str) {
        m94b("Connection", str);
    }

    /* renamed from: j */
    public boolean m115j() {
        return this.f47e.length > 0;
    }

    /* renamed from: k */
    public InputStream m116k() {
        return this.f48f;
    }

    /* renamed from: k */
    public void m117k(String str) {
        m94b("Server", str);
    }

    /* renamed from: l */
    public void m118l(String str) {
        if (C0763a.m38a(str)) {
            str = "[" + str + "]";
        }
        m94b("HOST", str);
    }

    /* renamed from: l */
    public boolean m119l() {
        return this.f48f != null;
    }

    /* renamed from: m */
    public String m120m() {
        return m102d("Content-Type");
    }

    /* renamed from: n */
    public String m121n() {
        String m = m120m();
        if (m == null) {
            return "";
        }
        String toLowerCase = m.toLowerCase();
        int indexOf = toLowerCase.indexOf("charset");
        if (indexOf < 0) {
            return "";
        }
        int length = (indexOf + "charset".length()) + 1;
        m = new String(toLowerCase.getBytes(), length, toLowerCase.length() - length);
        if (m.length() < 0) {
            return "";
        }
        if (m.charAt(0) == '\"') {
            m = m.substring(1, m.length() - 1);
        }
        return m.length() < 0 ? "" : m.charAt(m.length() + -1) == '\"' ? m.substring(0, m.length() - 1) : m;
    }

    /* renamed from: o */
    public long m122o() {
        return m106f("Content-Length");
    }

    /* renamed from: p */
    public boolean m123p() {
        return m101c("Connection");
    }

    /* renamed from: q */
    public String m124q() {
        return m102d("Connection");
    }

    /* renamed from: r */
    public boolean m125r() {
        if (!m123p()) {
            return false;
        }
        String q = m124q();
        return q != null ? q.equalsIgnoreCase("close") : false;
    }

    /* renamed from: s */
    public boolean m126s() {
        if (!m123p()) {
            return false;
        }
        String q = m124q();
        return q != null ? q.equalsIgnoreCase(HTTP.CONN_KEEP_ALIVE) : false;
    }

    /* renamed from: t */
    public boolean m127t() {
        return m101c("Content-Range") || m101c("Range");
    }

    /* renamed from: u */
    public long[] m128u() {
        long[] jArr = new long[]{0, 0, 0};
        if (!m127t()) {
            return jArr;
        }
        String d = m102d("Content-Range");
        if (d.length() <= 0) {
            d = m102d("Range");
        }
        if (d.length() <= 0) {
            return jArr;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(d, " =");
        if (!stringTokenizer.hasMoreTokens()) {
            return jArr;
        }
        stringTokenizer.nextToken(" ");
        if (!stringTokenizer.hasMoreTokens()) {
            return jArr;
        }
        try {
            jArr[0] = Long.parseLong(stringTokenizer.nextToken(" -"));
        } catch (NumberFormatException e) {
        }
        if (!stringTokenizer.hasMoreTokens()) {
            return jArr;
        }
        try {
            jArr[1] = Long.parseLong(stringTokenizer.nextToken("-/"));
        } catch (NumberFormatException e2) {
        }
        if (!stringTokenizer.hasMoreTokens()) {
            return jArr;
        }
        try {
            jArr[2] = Long.parseLong(stringTokenizer.nextToken(InternalZipConstants.ZIP_FILE_SEPARATOR));
        } catch (NumberFormatException e3) {
        }
        return jArr;
    }

    /* renamed from: v */
    public long m129v() {
        return m128u()[0];
    }

    /* renamed from: w */
    public long m130w() {
        return m128u()[1];
    }

    /* renamed from: x */
    public boolean m131x() {
        return m101c("Transfer-Encoding");
    }

    /* renamed from: y */
    public String m132y() {
        return m102d("Transfer-Encoding");
    }

    /* renamed from: z */
    public boolean m133z() {
        if (!m131x()) {
            return false;
        }
        String y = m132y();
        return y != null ? y.equalsIgnoreCase("Chunked") : false;
    }
}
