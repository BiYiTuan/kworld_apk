package org.cybergarage.upnp;

import cz.msebera.android.httpclient.HttpStatus;
import org.cybergarage.http.C0774k;

/* renamed from: org.cybergarage.upnp.h */
public class C0796h {
    /* renamed from: a */
    private int f91a;
    /* renamed from: b */
    private String f92b;

    public C0796h() {
        m308b(0);
        m306a("");
    }

    /* renamed from: a */
    public static final String m304a(int i) {
        switch (i) {
            case HttpStatus.SC_UNAUTHORIZED /*401*/:
                return "Invalid Action";
            case HttpStatus.SC_PAYMENT_REQUIRED /*402*/:
                return "Invalid Args";
            case HttpStatus.SC_FORBIDDEN /*403*/:
                return "Out of Sync";
            case HttpStatus.SC_NOT_FOUND /*404*/:
                return "Invalid Var";
            case HttpStatus.SC_PRECONDITION_FAILED /*412*/:
                return "Precondition Failed";
            case HttpStatus.SC_NOT_IMPLEMENTED /*501*/:
                return "Action Failed";
            default:
                return C0774k.m154a(i);
        }
    }

    /* renamed from: a */
    public int m305a() {
        return this.f91a;
    }

    /* renamed from: a */
    public void m306a(String str) {
        this.f92b = str;
    }

    /* renamed from: b */
    public String m307b() {
        return this.f92b;
    }

    /* renamed from: b */
    public void m308b(int i) {
        this.f91a = i;
    }
}
