package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.p000a.C0763a;
import org.cybergarage.upnp.C0961c;

public class SSDPSearchResponseSocketList extends Vector {
    private InetAddress[] binds = null;

    public SSDPSearchResponseSocketList(InetAddress[] inetAddressArr) {
        this.binds = inetAddressArr;
    }

    public void close() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPSearchResponseSocket(i).m326d();
        }
        clear();
    }

    public C0966k getSSDPSearchResponseSocket(int i) {
        return (C0966k) get(i);
    }

    public boolean open() {
        return open(1900);
    }

    public boolean open(int i) {
        String[] strArr;
        int i2;
        InetAddress[] inetAddressArr = this.binds;
        if (inetAddressArr != null) {
            strArr = new String[inetAddressArr.length];
            for (i2 = 0; i2 < inetAddressArr.length; i2++) {
                strArr[i2] = inetAddressArr[i2].getHostAddress();
            }
        } else {
            int b = C0763a.m40b();
            String[] strArr2 = new String[b];
            for (int i3 = 0; i3 < b; i3++) {
                strArr2[i3] = C0763a.m36a(i3);
            }
            strArr = strArr2;
        }
        try {
            for (i2 = strArr.length - 1; i2 >= 0; i2--) {
                add(new C0966k(strArr[i2], i));
            }
            return true;
        } catch (Exception e) {
            stop();
            close();
            clear();
            return false;
        }
    }

    public boolean post(C0999i c0999i) {
        boolean z = true;
        int size = size();
        int i = 0;
        while (i < size) {
            C0966k sSDPSearchResponseSocket = getSSDPSearchResponseSocket(i);
            String b = sSDPSearchResponseSocket.m324b();
            c0999i.m658u(b);
            String str = "239.255.255.250";
            if (C0763a.m38a(b)) {
                str = C0799c.m328a();
            }
            i++;
            z = !sSDPSearchResponseSocket.m582a(str, 1900, c0999i) ? false : z;
        }
        return z;
    }

    public boolean post(C0999i c0999i, String str) {
        boolean z = true;
        int size = size();
        int i = 0;
        while (i < size) {
            C0966k sSDPSearchResponseSocket = getSSDPSearchResponseSocket(i);
            String b = sSDPSearchResponseSocket.m324b();
            c0999i.m658u(b);
            i++;
            z = !sSDPSearchResponseSocket.m582a(C0763a.m38a(b) ? C0799c.m328a() : str, 1900, c0999i) ? false : z;
        }
        return z;
    }

    public void setControlPoint(C0961c c0961c) {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPSearchResponseSocket(i).m581a(c0961c);
        }
    }

    public void start() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPSearchResponseSocket(i).m585g();
        }
    }

    public void stop() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPSearchResponseSocket(i).m586h();
        }
    }
}
