package org.cybergarage.upnp;

import android.support.v4.view.ViewCompat;
import net.lingala.zip4j.util.InternalZipConstants;
import org.cybergarage.p001b.C0765a;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.C0803c;

/* renamed from: org.cybergarage.upnp.g */
public class C0795g {
    /* renamed from: a */
    private static C0803c f89a;
    /* renamed from: b */
    private static int f90b = 4;

    static {
        C0795g.m296a(4);
    }

    /* renamed from: a */
    public static final int m294a(String str) {
        int i = 0;
        if (str != null) {
            int length = str.length();
            for (int i2 = 0; i2 < length; i2++) {
                i += str.codePointAt(i2);
                if (i >= ViewCompat.MEASURED_SIZE_MASK) {
                    i %= ViewCompat.MEASURED_SIZE_MASK;
                }
            }
        }
        return i;
    }

    /* renamed from: a */
    public static final String m295a() {
        String property = System.getProperty("os.name");
        return property + InternalZipConstants.ZIP_FILE_SEPARATOR + System.getProperty("os.version") + " UPnP/1.0 " + "CyberLinkJava" + InternalZipConstants.ZIP_FILE_SEPARATOR + "3.0";
    }

    /* renamed from: a */
    public static final void m296a(int i) {
        f90b = i;
    }

    /* renamed from: b */
    public static final String m297b() {
        long currentTimeMillis = System.currentTimeMillis();
        long currentTimeMillis2 = (long) (((double) System.currentTimeMillis()) * Math.random());
        return C0795g.m298b((int) (currentTimeMillis & 65535)) + "-" + C0795g.m298b(((int) ((currentTimeMillis >> 32) | 40960)) & 65535) + "-" + C0795g.m298b((int) (currentTimeMillis2 & 65535)) + "-" + C0795g.m298b(((int) ((currentTimeMillis2 >> 32) | 57344)) & 65535);
    }

    /* renamed from: b */
    private static final String m298b(int i) {
        String num = Integer.toString(65535 & i, 16);
        int length = num.length();
        String str = "";
        for (int i2 = 0; i2 < 4 - length; i2++) {
            str = str + "0";
        }
        return str + num;
    }

    /* renamed from: c */
    public static final int m299c() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /* renamed from: d */
    public static final C0803c m300d() {
        if (f89a == null) {
            f89a = C0795g.m303g();
            if (f89a == null) {
                throw new RuntimeException("No XML parser defined. And unable to laod any. \nTry to invoke UPnP.setXMLParser before UPnP.getXMLParser");
            }
            C0765a.m44a(f89a);
        }
        return f89a;
    }

    /* renamed from: e */
    public static final int m301e() {
        return f90b;
    }

    /* renamed from: f */
    public static final void m302f() {
    }

    /* renamed from: g */
    private static C0803c m303g() {
        String[] strArr = new String[]{System.getProperty("cyberlink.upnp.xml.parser"), "org.cybergarage.xml.parser.XmlPullParser", "org.cybergarage.xml.parser.JaxpParser", "org.cybergarage.xml.parser.kXML2Parser", "org.cybergarage.xml.parser.XercesParser"};
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] != null) {
                try {
                    return (C0803c) Class.forName(strArr[i]).newInstance();
                } catch (Throwable th) {
                    Debug.warning("Unable to load " + strArr[i] + " as XMLParser due to " + th);
                }
            }
        }
        return null;
    }
}
