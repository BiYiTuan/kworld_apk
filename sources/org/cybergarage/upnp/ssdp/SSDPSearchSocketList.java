package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.p000a.C0763a;
import org.cybergarage.upnp.device.C0787i;

public class SSDPSearchSocketList extends Vector {
    private InetAddress[] binds = null;
    private String multicastIPv4 = "239.255.255.250";
    private String multicastIPv6 = C0799c.m328a();
    private int port = 1900;

    public SSDPSearchSocketList(InetAddress[] inetAddressArr) {
        this.binds = inetAddressArr;
    }

    public SSDPSearchSocketList(InetAddress[] inetAddressArr, int i, String str, String str2) {
        this.binds = inetAddressArr;
        this.port = i;
        this.multicastIPv4 = str;
        this.multicastIPv6 = str2;
    }

    public void addSearchListener(C0787i c0787i) {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPSearchSocket(i).m587a(c0787i);
        }
    }

    public void close() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPSearchSocket(i).m318f();
        }
        clear();
    }

    public C0967l getSSDPSearchSocket(int i) {
        return (C0967l) get(i);
    }

    public boolean open() {
        String[] strArr;
        int i = 0;
        InetAddress[] inetAddressArr = this.binds;
        if (inetAddressArr != null) {
            String[] strArr2 = new String[inetAddressArr.length];
            for (int i2 = 0; i2 < inetAddressArr.length; i2++) {
                strArr2[i2] = inetAddressArr[i2].getHostAddress();
            }
            strArr = strArr2;
        } else {
            int b = C0763a.m40b();
            strArr = new String[b];
            for (int i3 = 0; i3 < b; i3++) {
                strArr[i3] = C0763a.m36a(i3);
            }
        }
        while (i < strArr.length) {
            if (strArr[i] != null) {
                add(C0763a.m38a(strArr[i]) ? new C0967l(strArr[i], this.port, this.multicastIPv6) : new C0967l(strArr[i], this.port, this.multicastIPv4));
            }
            i++;
        }
        return true;
    }

    public void start() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPSearchSocket(i).m590h();
        }
    }

    public void stop() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPSearchSocket(i).m591i();
        }
    }
}
