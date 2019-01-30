package org.cybergarage.xml;

/* renamed from: org.cybergarage.xml.a */
public class C0801a {
    /* renamed from: a */
    private String f103a;
    /* renamed from: b */
    private String f104b;

    public C0801a() {
        this.f103a = new String();
        this.f104b = new String();
    }

    public C0801a(String str, String str2) {
        this();
        m355a(str);
        m358b(str2);
    }

    public C0801a(C0801a c0801a) {
        this();
        m356a(c0801a);
    }

    /* renamed from: a */
    public String m354a() {
        return this.f103a;
    }

    /* renamed from: a */
    public void m355a(String str) {
        this.f103a = str;
    }

    /* renamed from: a */
    public void m356a(C0801a c0801a) {
        m355a(c0801a.m354a());
        m358b(c0801a.m357b());
    }

    /* renamed from: b */
    public String m357b() {
        return this.f104b;
    }

    /* renamed from: b */
    public void m358b(String str) {
        this.f104b = str;
    }
}
