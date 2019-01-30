package org.cybergarage.http;

/* renamed from: org.cybergarage.http.g */
public class C0954g extends C0769d {
    /* renamed from: b */
    private int f124b = 0;

    public C0954g() {
        m75a("1.1");
        m110h("text/html; charset=\"utf-8\"");
        m117k(C0771h.m134a());
        m109g("");
    }

    public C0954g(C0954g c0954g) {
        m83a((C0769d) c0954g);
    }

    public C0954g(boolean z) {
        super(z);
    }

    /* renamed from: A */
    public int m452A() {
        return this.f124b != 0 ? this.f124b : new C0774k(m97c()).m156a();
    }

    /* renamed from: B */
    public boolean m453B() {
        return C0774k.m155c(m452A());
    }

    /* renamed from: C */
    public String m454C() {
        return "HTTP/" + m89b() + " " + m452A() + " " + C0774k.m154a(this.f124b) + "\r\n";
    }

    /* renamed from: D */
    public String mo2463D() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(m454C());
        stringBuffer.append(m108g());
        return stringBuffer.toString();
    }

    /* renamed from: E */
    public void mo2462E() {
        System.out.println(toString());
    }

    /* renamed from: d */
    public void m457d(int i) {
        this.f124b = i;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(m454C());
        stringBuffer.append(m108g());
        stringBuffer.append("\r\n");
        stringBuffer.append(m112i());
        return stringBuffer.toString();
    }
}
