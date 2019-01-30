package org.cybergarage.http;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.p000a.C0763a;
import org.cybergarage.upnp.Device;

public class HTTPServerList extends Vector {
    private InetAddress[] binds = null;
    private int port = Device.HTTP_DEFAULT_PORT;

    public HTTPServerList(InetAddress[] inetAddressArr, int i) {
        this.binds = inetAddressArr;
        this.port = i;
    }

    public void addRequestListener(C0770f c0770f) {
        int size = size();
        for (int i = 0; i < size; i++) {
            getHTTPServer(i).m136a(c0770f);
        }
    }

    public void close() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getHTTPServer(i).m139c();
        }
    }

    public C0771h getHTTPServer(int i) {
        return (C0771h) get(i);
    }

    public int open() {
        String[] strArr;
        int i;
        int i2 = 0;
        InetAddress[] inetAddressArr = this.binds;
        if (inetAddressArr != null) {
            String[] strArr2 = new String[inetAddressArr.length];
            for (int i3 = 0; i3 < inetAddressArr.length; i3++) {
                strArr2[i3] = inetAddressArr[i3].getHostAddress();
            }
            strArr = strArr2;
        } else {
            int b = C0763a.m40b();
            strArr = new String[b];
            for (i = 0; i < b; i++) {
                strArr[i] = C0763a.m36a(i);
            }
        }
        i = 0;
        while (i2 < strArr.length) {
            C0771h c0771h = new C0771h();
            if (strArr[i2] == null || !c0771h.m137a(strArr[i2], this.port)) {
                close();
                clear();
            } else {
                add(c0771h);
                i++;
            }
            i2++;
        }
        return i;
    }

    public boolean open(int i) {
        this.port = i;
        return open() != 0;
    }

    public void start() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getHTTPServer(i).m142f();
        }
    }

    public void stop() {
        int size = size();
        for (int i = 0; i < size; i++) {
            getHTTPServer(i).m143g();
        }
    }
}
