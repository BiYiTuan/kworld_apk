package org.cybergarage.upnp.ssdp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.cybergarage.http.C0953e;
import org.cybergarage.upnp.C0795g;
import org.cybergarage.util.Debug;

/* renamed from: org.cybergarage.upnp.ssdp.a */
public class C0797a {
    /* renamed from: a */
    private InetSocketAddress f93a = null;
    /* renamed from: b */
    private MulticastSocket f94b = null;
    /* renamed from: c */
    private NetworkInterface f95c = null;

    /* renamed from: a */
    public String m309a() {
        if (this.f93a == null || this.f95c == null) {
            return "";
        }
        InetAddress address = this.f93a.getAddress();
        Enumeration inetAddresses = this.f95c.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
            if ((address instanceof Inet6Address) && (inetAddress instanceof Inet6Address)) {
                return inetAddress.getHostAddress();
            }
            if ((address instanceof Inet4Address) && (inetAddress instanceof Inet4Address)) {
                return inetAddress.getHostAddress();
            }
        }
        return "";
    }

    /* renamed from: a */
    public boolean m310a(String str, int i, String str2) {
        try {
            return m311a(str, i, InetAddress.getByName(str2));
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    /* renamed from: a */
    public boolean m311a(String str, int i, InetAddress inetAddress) {
        try {
            this.f94b = new MulticastSocket(null);
            this.f94b.setReuseAddress(true);
            this.f94b.bind(new InetSocketAddress(i));
            this.f93a = new InetSocketAddress(InetAddress.getByName(str), i);
            this.f95c = NetworkInterface.getByInetAddress(inetAddress);
            this.f94b.joinGroup(this.f93a, this.f95c);
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    /* renamed from: a */
    public boolean m312a(String str, String str2, int i) {
        MulticastSocket multicastSocket;
        if (str2 == null || i <= 0) {
            multicastSocket = new MulticastSocket();
        } else {
            try {
                multicastSocket = new MulticastSocket(null);
                multicastSocket.bind(new InetSocketAddress(str2, i));
            } catch (Exception e) {
                Debug.warning(e);
                return false;
            }
        }
        DatagramPacket datagramPacket = new DatagramPacket(str.getBytes(), str.length(), this.f93a);
        multicastSocket.setTimeToLive(C0795g.m301e());
        multicastSocket.send(datagramPacket);
        multicastSocket.close();
        return true;
    }

    /* renamed from: a */
    public boolean m313a(C0953e c0953e) {
        return m312a(c0953e.toString(), null, -1);
    }

    /* renamed from: b */
    public int m314b() {
        return this.f93a.getPort();
    }

    /* renamed from: c */
    public int m315c() {
        return this.f94b.getLocalPort();
    }

    /* renamed from: d */
    public InetAddress m316d() {
        return this.f93a.getAddress();
    }

    /* renamed from: e */
    public String m317e() {
        return m316d().getHostAddress();
    }

    /* renamed from: f */
    public boolean m318f() {
        if (this.f94b == null) {
            return true;
        }
        try {
            this.f94b.leaveGroup(this.f93a, this.f95c);
            this.f94b.close();
            this.f94b = null;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void finalize() {
        m318f();
    }

    /* renamed from: g */
    public C0800f m319g() {
        byte[] bArr = new byte[1024];
        C0800f c0800f = new C0800f(bArr, bArr.length);
        c0800f.m333a(m309a());
        if (this.f94b != null) {
            this.f94b.receive(c0800f.m331a());
            c0800f.m332a(System.currentTimeMillis());
            return c0800f;
        }
        throw new IOException("Multicast socket has already been closed.");
    }
}
