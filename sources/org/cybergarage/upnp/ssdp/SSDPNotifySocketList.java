package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.p000a.C0763a;
import org.cybergarage.upnp.C0961c;

public class SSDPNotifySocketList extends Vector {
    private InetAddress[] binds = null;

    public SSDPNotifySocketList(InetAddress[] inetAddressArr) {
        this.binds = inetAddressArr;
    }

    public void close() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPNotifySocket(i).m318f();
        }
        clear();
    }

    public C0965e getSSDPNotifySocket(int i) {
        return (C0965e) get(i);
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
                add(new C0965e(strArr[i]));
            }
            i++;
        }
        return true;
    }

    public void setControlPoint(C0961c c0961c) {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPNotifySocket(i).m576a(c0961c);
        }
    }

    public void start() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPNotifySocket(i).m579i();
        }
    }

    public void stop() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getSSDPNotifySocket(i).m580j();
        }
    }
}
