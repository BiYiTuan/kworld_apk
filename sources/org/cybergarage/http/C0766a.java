package org.cybergarage.http;

import java.util.Calendar;

/* renamed from: org.cybergarage.http.a */
public class C0766a {
    /* renamed from: b */
    private static final String[] f36b = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    /* renamed from: c */
    private static final String[] f37c = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    /* renamed from: a */
    private Calendar f38a;

    public C0766a(Calendar calendar) {
        this.f38a = calendar;
    }

    /* renamed from: a */
    public static final String m46a(int i) {
        int i2 = i + 0;
        return (i2 < 0 || i2 >= 12) ? "" : f36b[i2];
    }

    /* renamed from: b */
    public static final String m47b(int i) {
        int i2 = i - 1;
        return (i2 < 0 || i2 >= 7) ? "" : f37c[i2];
    }

    /* renamed from: c */
    public static final String m48c(int i) {
        String str = "";
        if (i < 10) {
            str = str + "0";
        }
        return str + Integer.toString(i);
    }

    /* renamed from: a */
    public Calendar m49a() {
        return this.f38a;
    }

    /* renamed from: b */
    public String m50b() {
        Calendar a = m49a();
        return C0766a.m47b(a.get(7)) + ", " + C0766a.m48c(a.get(5)) + " " + C0766a.m46a(a.get(2)) + " " + Integer.toString(a.get(1)) + " " + C0766a.m48c(a.get(11)) + ":" + C0766a.m48c(a.get(12)) + ":" + C0766a.m48c(a.get(13)) + " GMT";
    }
}
