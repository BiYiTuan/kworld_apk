package org.cybergarage.http;

import cz.msebera.android.httpclient.HttpStatus;
import java.util.StringTokenizer;
import org.cybergarage.util.Debug;

/* renamed from: org.cybergarage.http.k */
public class C0774k {
    /* renamed from: a */
    private String f60a = "";
    /* renamed from: b */
    private int f61b = 0;
    /* renamed from: c */
    private String f62c = "";

    public C0774k() {
        m157a("");
        m158b(0);
        m159b("");
    }

    public C0774k(String str) {
        m160c(str);
    }

    /* renamed from: a */
    public static final String m154a(int i) {
        switch (i) {
            case 100:
                return "Continue";
            case 200:
                return "OK";
            case HttpStatus.SC_PARTIAL_CONTENT /*206*/:
                return "Partial Content";
            case 400:
                return "Bad Request";
            case HttpStatus.SC_NOT_FOUND /*404*/:
                return "Not Found";
            case HttpStatus.SC_PRECONDITION_FAILED /*412*/:
                return "Precondition Failed";
            case HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE /*416*/:
                return "Invalid Range";
            case HttpStatus.SC_INTERNAL_SERVER_ERROR /*500*/:
                return "Internal Server Error";
            default:
                return "";
        }
    }

    /* renamed from: c */
    public static final boolean m155c(int i) {
        return 200 <= i && i < 300;
    }

    /* renamed from: a */
    public int m156a() {
        return this.f61b;
    }

    /* renamed from: a */
    public void m157a(String str) {
        this.f60a = str;
    }

    /* renamed from: b */
    public void m158b(int i) {
        this.f61b = i;
    }

    /* renamed from: b */
    public void m159b(String str) {
        this.f62c = str;
    }

    /* renamed from: c */
    public void m160c(String str) {
        if (str == null) {
            m157a("1.1");
            m158b((int) HttpStatus.SC_INTERNAL_SERVER_ERROR);
            m159b(C0774k.m154a((int) HttpStatus.SC_INTERNAL_SERVER_ERROR));
            return;
        }
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(str, " ");
            if (stringTokenizer.hasMoreTokens()) {
                m157a(stringTokenizer.nextToken().trim());
                if (stringTokenizer.hasMoreTokens()) {
                    int i = 0;
                    try {
                        i = Integer.parseInt(stringTokenizer.nextToken());
                    } catch (Exception e) {
                    }
                    m158b(i);
                    String str2 = "";
                    while (stringTokenizer.hasMoreTokens()) {
                        if (str2.length() >= 0) {
                            str2 = str2 + " ";
                        }
                        str2 = str2 + stringTokenizer.nextToken();
                    }
                    m159b(str2.trim());
                }
            }
        } catch (Exception e2) {
            Debug.warning(e2);
        }
    }
}
