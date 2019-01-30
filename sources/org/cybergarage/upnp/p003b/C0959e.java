package org.cybergarage.upnp.p003b;

import org.cybergarage.upnp.event.SubscriberList;
import org.cybergarage.util.ListenerList;
import org.cybergarage.xml.C0802b;

/* renamed from: org.cybergarage.upnp.b.e */
public class C0959e extends C0778d {
    /* renamed from: a */
    private ListenerList f144a = new ListenerList();
    /* renamed from: b */
    private C0802b f145b = null;
    /* renamed from: c */
    private SubscriberList f146c = new SubscriberList();
    /* renamed from: d */
    private String f147d = "";
    /* renamed from: e */
    private String f148e = "";
    /* renamed from: f */
    private long f149f = 0;

    /* renamed from: a */
    public C0802b m491a() {
        return this.f145b;
    }

    /* renamed from: a */
    public void m492a(long j) {
        this.f149f = j;
    }

    /* renamed from: a */
    public void m493a(String str) {
        this.f148e = str;
    }

    /* renamed from: a */
    public void m494a(C0802b c0802b) {
        this.f145b = c0802b;
    }

    /* renamed from: b */
    public SubscriberList m495b() {
        return this.f146c;
    }

    /* renamed from: c */
    public String m496c() {
        return this.f148e;
    }
}
