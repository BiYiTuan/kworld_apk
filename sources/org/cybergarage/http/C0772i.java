package org.cybergarage.http;

import java.net.Socket;

/* renamed from: org.cybergarage.http.i */
public class C0772i extends Thread {
    /* renamed from: a */
    private C0771h f55a;
    /* renamed from: b */
    private Socket f56b;

    public C0772i(C0771h c0771h, Socket socket) {
        super("Cyber.HTTPServerThread");
        this.f55a = c0771h;
        this.f56b = socket;
    }

    public void run() {
        C0773j c0773j = new C0773j(this.f56b);
        if (c0773j.m152d()) {
            C0953e c0953e = new C0953e();
            c0953e.m445c(c0773j);
            while (c0953e.m436R()) {
                this.f55a.m135a(c0953e);
                if (!c0953e.m435Q()) {
                    break;
                }
            }
            c0773j.m153e();
        }
    }
}
