package org.cybergarage.http;

import java.io.LineNumberReader;
import java.io.StringReader;
import org.cybergarage.util.Debug;

/* renamed from: org.cybergarage.http.c */
public class C0768c {
    /* renamed from: a */
    private static int f40a = 1024;
    /* renamed from: b */
    private String f41b;
    /* renamed from: c */
    private String f42c;

    public C0768c(String str) {
        m64a("");
        m66b("");
        if (str != null) {
            int indexOf = str.indexOf(58);
            if (indexOf >= 0) {
                String str2 = new String(str.getBytes(), 0, indexOf);
                String str3 = new String(str.getBytes(), indexOf + 1, (str.length() - indexOf) - 1);
                m64a(str2.trim());
                m66b(str3.trim());
            }
        }
    }

    public C0768c(String str, String str2) {
        m64a(str);
        m66b(str2);
    }

    /* renamed from: a */
    public static final String m59a(LineNumberReader lineNumberReader, String str) {
        String toUpperCase = str.toUpperCase();
        try {
            String readLine = lineNumberReader.readLine();
            while (readLine != null && readLine.length() > 0) {
                C0768c c0768c = new C0768c(readLine);
                if (!c0768c.m67c()) {
                    readLine = lineNumberReader.readLine();
                } else if (c0768c.m63a().toUpperCase().equals(toUpperCase)) {
                    return c0768c.m65b();
                } else {
                    readLine = lineNumberReader.readLine();
                }
            }
            return "";
        } catch (Exception e) {
            Debug.warning(e);
            return "";
        }
    }

    /* renamed from: a */
    public static final String m60a(String str, String str2) {
        return C0768c.m59a(new LineNumberReader(new StringReader(str), Math.min(str.length(), f40a)), str2);
    }

    /* renamed from: a */
    public static final String m61a(byte[] bArr, String str) {
        return C0768c.m60a(new String(bArr), str);
    }

    /* renamed from: b */
    public static final int m62b(byte[] bArr, String str) {
        try {
            return Integer.parseInt(C0768c.m61a(bArr, str));
        } catch (Exception e) {
            return 0;
        }
    }

    /* renamed from: a */
    public String m63a() {
        return this.f41b;
    }

    /* renamed from: a */
    public void m64a(String str) {
        this.f41b = str;
    }

    /* renamed from: b */
    public String m65b() {
        return this.f42c;
    }

    /* renamed from: b */
    public void m66b(String str) {
        this.f42c = str;
    }

    /* renamed from: c */
    public boolean m67c() {
        return this.f41b != null && this.f41b.length() > 0;
    }
}
