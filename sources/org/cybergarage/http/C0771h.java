package org.cybergarage.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import net.lingala.zip4j.util.InternalZipConstants;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ListenerList;

/* renamed from: org.cybergarage.http.h */
public class C0771h implements Runnable {
    /* renamed from: a */
    protected int f49a;
    /* renamed from: b */
    private ServerSocket f50b;
    /* renamed from: c */
    private InetAddress f51c;
    /* renamed from: d */
    private int f52d;
    /* renamed from: e */
    private ListenerList f53e;
    /* renamed from: f */
    private Thread f54f;

    public C0771h() {
        this.f50b = null;
        this.f51c = null;
        this.f52d = 0;
        this.f49a = 80000;
        this.f53e = new ListenerList();
        this.f54f = null;
        this.f50b = null;
    }

    /* renamed from: a */
    public static String m134a() {
        String property = System.getProperty("os.name");
        return property + InternalZipConstants.ZIP_FILE_SEPARATOR + System.getProperty("os.version") + " " + "CyberHTTP" + InternalZipConstants.ZIP_FILE_SEPARATOR + "1.0";
    }

    /* renamed from: a */
    public void m135a(C0953e c0953e) {
        int size = this.f53e.size();
        for (int i = 0; i < size; i++) {
            ((C0770f) this.f53e.get(i)).httpRequestRecieved(c0953e);
        }
    }

    /* renamed from: a */
    public void m136a(C0770f c0770f) {
        this.f53e.add(c0770f);
    }

    /* renamed from: a */
    public boolean m137a(String str, int i) {
        if (this.f50b != null) {
            return true;
        }
        try {
            this.f51c = InetAddress.getByName(str);
            this.f52d = i;
            this.f50b = new ServerSocket(this.f52d, 0, this.f51c);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /* renamed from: b */
    public synchronized int m138b() {
        return this.f49a;
    }

    /* renamed from: c */
    public boolean m139c() {
        if (this.f50b == null) {
            return true;
        }
        try {
            this.f50b.close();
            this.f50b = null;
            this.f51c = null;
            this.f52d = 0;
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    /* renamed from: d */
    public Socket m140d() {
        if (this.f50b == null) {
            return null;
        }
        try {
            Socket accept = this.f50b.accept();
            accept.setSoTimeout(m138b());
            return accept;
        } catch (Exception e) {
            return null;
        }
    }

    /* renamed from: e */
    public boolean m141e() {
        return this.f50b != null;
    }

    /* renamed from: f */
    public boolean m142f() {
        StringBuffer stringBuffer = new StringBuffer("Cyber.HTTPServer/");
        stringBuffer.append(this.f50b.getLocalSocketAddress());
        this.f54f = new Thread(this, stringBuffer.toString());
        this.f54f.start();
        return true;
    }

    /* renamed from: g */
    public boolean m143g() {
        this.f54f = null;
        return true;
    }

    public void run() {
        if (m141e()) {
            Thread currentThread = Thread.currentThread();
            while (this.f54f == currentThread) {
                Thread.yield();
                try {
                    Debug.message("accept ...");
                    Socket d = m140d();
                    if (d != null) {
                        Debug.message("sock = " + d.getRemoteSocketAddress());
                    }
                    new C0772i(this, d).start();
                    Debug.message("httpServThread ...");
                } catch (Exception e) {
                    Debug.warning(e);
                    return;
                }
            }
        }
    }
}
