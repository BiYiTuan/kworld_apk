package org.cybergarage.p000a;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.cybergarage.util.Debug;

/* renamed from: org.cybergarage.a.a */
public class C0763a {
    /* renamed from: a */
    public static boolean f31a = false;
    /* renamed from: b */
    public static boolean f32b = true;
    /* renamed from: c */
    public static boolean f33c = false;
    /* renamed from: d */
    private static String f34d = "";

    /* renamed from: a */
    public static final String m35a() {
        return f34d;
    }

    /* renamed from: a */
    public static final String m36a(int i) {
        if (C0763a.m42c()) {
            return C0763a.m35a();
        }
        int i2 = 0;
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (C0763a.m39a(inetAddress)) {
                        if (i2 >= i) {
                            return inetAddress.getHostAddress();
                        }
                        i2++;
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /* renamed from: a */
    public static final String m37a(String str, int i, String str2) {
        if (C0763a.m38a(str)) {
            str = "[" + str + "]";
        }
        return "http://" + str + ":" + Integer.toString(i) + str2;
    }

    /* renamed from: a */
    public static final boolean m38a(String str) {
        try {
            return InetAddress.getByName(str) instanceof Inet6Address;
        } catch (Exception e) {
            return false;
        }
    }

    /* renamed from: a */
    private static final boolean m39a(InetAddress inetAddress) {
        return (f31a || !inetAddress.isLoopbackAddress()) ? (f32b && (inetAddress instanceof Inet6Address)) ? false : (f33c && (inetAddress instanceof Inet4Address)) ? false : true : false;
    }

    /* renamed from: b */
    public static final int m40b() {
        if (C0763a.m42c()) {
            return 1;
        }
        int i = 0;
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    if (C0763a.m39a((InetAddress) inetAddresses.nextElement())) {
                        i++;
                    }
                }
            }
            return i;
        } catch (Exception e) {
            Exception exception = e;
            int i2 = 0;
            Debug.warning(exception);
            return i2;
        }
    }

    /* renamed from: b */
    public static final boolean m41b(String str) {
        try {
            return InetAddress.getByName(str) instanceof Inet4Address;
        } catch (Exception e) {
            return false;
        }
    }

    /* renamed from: c */
    private static final boolean m42c() {
        return f34d.length() > 0;
    }
}
