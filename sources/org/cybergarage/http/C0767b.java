package org.cybergarage.http;

import java.net.URL;
import net.lingala.zip4j.util.InternalZipConstants;

/* renamed from: org.cybergarage.http.b */
public class C0767b {
    /* renamed from: a */
    private static int f39a = 524288;

    /* renamed from: a */
    public static final int m51a() {
        return f39a;
    }

    /* renamed from: a */
    public static final String m52a(String str, int i) {
        return "http://" + str + ":" + i;
    }

    /* renamed from: a */
    public static final String m53a(String str, String str2) {
        try {
            URL url = new URL(str);
            return url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + C0767b.m58d(str2);
        } catch (Exception e) {
            return "";
        }
    }

    /* renamed from: a */
    public static final String m54a(String str, boolean z) {
        String query;
        if (!C0767b.m55a(str)) {
            return (str.length() <= 0 || str.charAt(0) == '/') ? str : InternalZipConstants.ZIP_FILE_SEPARATOR + str;
        } else {
            try {
                URL url = new URL(str);
                str = url.getPath();
                if (z) {
                    query = url.getQuery();
                    if (!query.equals("")) {
                        query = str + "?" + query;
                        if (query.endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
                            query = query.substring(0, query.length() - 1);
                        }
                        return query;
                    }
                }
                query = str;
                try {
                    if (query.endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
                        query = query.substring(0, query.length() - 1);
                    }
                    return query;
                } catch (Exception e) {
                    return query;
                }
            } catch (Exception e2) {
                return str;
            }
        }
    }

    /* renamed from: a */
    public static final boolean m55a(String str) {
        try {
            URL url = new URL(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* renamed from: b */
    public static final String m56b(String str) {
        try {
            return new URL(str).getHost();
        } catch (Exception e) {
            return "";
        }
    }

    /* renamed from: c */
    public static final int m57c(String str) {
        try {
            int port = new URL(str).getPort();
            return port <= 0 ? 80 : port;
        } catch (Exception e) {
            return 80;
        }
    }

    /* renamed from: d */
    public static final String m58d(String str) {
        return C0767b.m54a(str, true);
    }
}
