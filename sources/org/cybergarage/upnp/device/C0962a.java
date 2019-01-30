package org.cybergarage.upnp.device;

import org.cybergarage.upnp.Device;
import org.cybergarage.util.ThreadCore;

/* renamed from: org.cybergarage.upnp.device.a */
public class C0962a extends ThreadCore {
    /* renamed from: a */
    private Device f172a;

    public C0962a(Device device) {
        m553a(device);
    }

    /* renamed from: a */
    public Device m552a() {
        return this.f172a;
    }

    /* renamed from: a */
    public void m553a(Device device) {
        this.f172a = device;
    }

    public void run() {
        Device a = m552a();
        long leaseTime = (long) a.getLeaseTime();
        while (isRunnable()) {
            try {
                Thread.sleep(((leaseTime / 4) + ((long) (((double) ((float) leaseTime)) * (Math.random() * 0.25d)))) * 1000);
            } catch (InterruptedException e) {
            }
            a.announce();
        }
    }
}
