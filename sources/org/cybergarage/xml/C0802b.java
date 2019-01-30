package org.cybergarage.xml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/* renamed from: org.cybergarage.xml.b */
public class C0802b {
    /* renamed from: a */
    private C0802b f105a;
    /* renamed from: b */
    private String f106b;
    /* renamed from: c */
    private String f107c;
    /* renamed from: d */
    private AttributeList f108d;
    /* renamed from: e */
    private NodeList f109e;
    /* renamed from: f */
    private Object f110f;

    public C0802b() {
        this.f105a = null;
        this.f106b = new String();
        this.f107c = new String();
        this.f108d = new AttributeList();
        this.f109e = new NodeList();
        this.f110f = null;
        m365a(null);
        m370a(null);
    }

    public C0802b(String str) {
        this();
        m366a(str);
    }

    /* renamed from: a */
    public String m359a(int i, String str) {
        StringBuffer stringBuffer = new StringBuffer(str.length() * i);
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    /* renamed from: a */
    public String m360a(String str, boolean z) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
        m364a(printWriter, 0, z);
        printWriter.flush();
        if (str != null) {
            try {
                if (str.length() > 0) {
                    return byteArrayOutputStream.toString(str);
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
        return byteArrayOutputStream.toString();
    }

    /* renamed from: a */
    public C0802b m361a() {
        return this.f105a;
    }

    /* renamed from: a */
    public void m362a(int i) {
        m374b(Integer.toString(i));
    }

    /* renamed from: a */
    public void m363a(PrintWriter printWriter) {
        int e = m386e();
        for (int i = 0; i < e; i++) {
            C0801a b = m372b(i);
            printWriter.print(" " + b.m354a() + "=\"" + C0804d.m399a(b.m357b()) + "\"");
        }
    }

    /* renamed from: a */
    public void m364a(PrintWriter printWriter, int i, boolean z) {
        String d = m383d(i);
        String c = m377c();
        String d2 = m382d();
        if (m395i() && z) {
            printWriter.print(d + "<" + c);
            m363a(printWriter);
            printWriter.println(">");
            int g = m391g();
            for (int i2 = 0; i2 < g; i2++) {
                m378c(i2).m364a(printWriter, i + 1, true);
            }
            printWriter.println(d + "</" + c + ">");
            return;
        }
        printWriter.print(d + "<" + c);
        m363a(printWriter);
        if (d2 == null || d2.length() == 0) {
            printWriter.println("></" + c + ">");
        } else {
            printWriter.println(">" + C0804d.m399a(d2) + "</" + c + ">");
        }
    }

    /* renamed from: a */
    public void m365a(Object obj) {
        this.f110f = obj;
    }

    /* renamed from: a */
    public void m366a(String str) {
        this.f106b = str;
    }

    /* renamed from: a */
    public void m367a(String str, int i) {
        m380c(str, Integer.toString(i));
    }

    /* renamed from: a */
    public void m368a(String str, String str2) {
        this.f106b = str + ":" + str2;
    }

    /* renamed from: a */
    public void m369a(C0801a c0801a) {
        this.f108d.add(c0801a);
    }

    /* renamed from: a */
    public void m370a(C0802b c0802b) {
        this.f105a = c0802b;
    }

    /* renamed from: a */
    public void m371a(C0802b c0802b, int i) {
        c0802b.m370a(this);
        this.f109e.insertElementAt(c0802b, i);
    }

    /* renamed from: b */
    public C0801a m372b(int i) {
        return this.f108d.getAttribute(i);
    }

    /* renamed from: b */
    public C0802b m373b() {
        C0802b c0802b = null;
        for (C0802b a = m361a(); a != null; a = a.m361a()) {
            c0802b = a;
        }
        return c0802b;
    }

    /* renamed from: b */
    public void m374b(String str) {
        this.f107c = str;
    }

    /* renamed from: b */
    public void m375b(String str, String str2) {
        m369a(new C0801a(str, str2));
    }

    /* renamed from: b */
    public boolean m376b(C0802b c0802b) {
        int i = 0;
        if (c0802b == null) {
            return false;
        }
        int i2;
        m366a(c0802b.m377c());
        m374b(c0802b.m382d());
        m390f();
        int e = c0802b.m386e();
        for (i2 = 0; i2 < e; i2++) {
            m369a(new C0801a(c0802b.m372b(i2)));
        }
        m394h();
        i2 = c0802b.m391g();
        while (i < i2) {
            C0802b c = c0802b.m378c(i);
            C0802b c0802b2 = new C0802b();
            c0802b2.m376b(c);
            m381c(c0802b2);
            i++;
        }
        return true;
    }

    /* renamed from: c */
    public String m377c() {
        return this.f106b;
    }

    /* renamed from: c */
    public C0802b m378c(int i) {
        return this.f109e.getNode(i);
    }

    /* renamed from: c */
    public void m379c(String str) {
        if (this.f107c == null) {
            this.f107c = str;
        } else if (str != null) {
            this.f107c += str;
        }
    }

    /* renamed from: c */
    public void m380c(String str, String str2) {
        C0801a d = m384d(str);
        if (d != null) {
            d.m358b(str2);
        } else {
            m369a(new C0801a(str, str2));
        }
    }

    /* renamed from: c */
    public void m381c(C0802b c0802b) {
        c0802b.m370a(this);
        this.f109e.add(c0802b);
    }

    /* renamed from: d */
    public String m382d() {
        return this.f107c;
    }

    /* renamed from: d */
    public String m383d(int i) {
        return m359a(i, "   ");
    }

    /* renamed from: d */
    public C0801a m384d(String str) {
        return this.f108d.getAttribute(str);
    }

    /* renamed from: d */
    public void m385d(String str, String str2) {
        m380c("xmlns:" + str, str2);
    }

    /* renamed from: e */
    public int m386e() {
        return this.f108d.size();
    }

    /* renamed from: e */
    public String m387e(String str) {
        C0801a d = m384d(str);
        return d != null ? d.m357b() : "";
    }

    /* renamed from: e */
    public void m388e(String str, String str2) {
        C0802b g = m392g(str);
        if (g == null) {
            g = new C0802b(str);
            m381c(g);
        }
        g.m374b(str2);
    }

    /* renamed from: f */
    public int m389f(String str) {
        try {
            return Integer.parseInt(m387e(str));
        } catch (Exception e) {
            return 0;
        }
    }

    /* renamed from: f */
    public void m390f() {
        this.f108d.clear();
    }

    /* renamed from: g */
    public int m391g() {
        return this.f109e.size();
    }

    /* renamed from: g */
    public C0802b m392g(String str) {
        return this.f109e.getNode(str);
    }

    /* renamed from: h */
    public C0802b m393h(String str) {
        return this.f109e.getEndsWith(str);
    }

    /* renamed from: h */
    public void m394h() {
        this.f109e.clear();
    }

    /* renamed from: i */
    public boolean m395i() {
        return m391g() > 0;
    }

    /* renamed from: i */
    public boolean m396i(String str) {
        return this.f109e.remove(m392g(str));
    }

    /* renamed from: j */
    public Object m397j() {
        return this.f110f;
    }

    /* renamed from: j */
    public String m398j(String str) {
        C0802b g = m392g(str);
        return g != null ? g.m382d() : "";
    }

    public String toString() {
        return m360a("utf-8", true);
    }
}
