package org.cybergarage.upnp;

import android.text.TextUtils;
import android.util.Log;
import com.memo.TestXlog;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.cybergarage.http.C0770f;
import org.cybergarage.http.C0953e;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.p000a.C0763a;
import org.cybergarage.upnp.device.C0784f;
import org.cybergarage.upnp.device.C0788j;
import org.cybergarage.upnp.device.C0789k;
import org.cybergarage.upnp.device.C0963b;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.event.C0791a;
import org.cybergarage.upnp.event.C0792c;
import org.cybergarage.upnp.event.C0986f;
import org.cybergarage.upnp.event.C0987g;
import org.cybergarage.upnp.event.C0997b;
import org.cybergarage.upnp.event.PropertyList;
import org.cybergarage.upnp.p002a.C0955i;
import org.cybergarage.upnp.ssdp.C0800f;
import org.cybergarage.upnp.ssdp.C0999i;
import org.cybergarage.upnp.ssdp.SSDPNotifySocketList;
import org.cybergarage.upnp.ssdp.SSDPSearchResponseSocketList;
import org.cybergarage.util.Debug;
import org.cybergarage.util.HttpUtil;
import org.cybergarage.util.ListenerList;
import org.cybergarage.util.Mutex;
import org.cybergarage.util.StringUtil;
import org.cybergarage.xml.C0802b;
import org.cybergarage.xml.C0803c;
import org.cybergarage.xml.NodeList;

/* renamed from: org.cybergarage.upnp.c */
public class C0961c implements C0770f {
    /* renamed from: a */
    ListenerList f153a;
    /* renamed from: b */
    private SSDPNotifySocketList f154b;
    /* renamed from: c */
    private SSDPSearchResponseSocketList f155c;
    /* renamed from: d */
    private Mutex f156d;
    /* renamed from: e */
    private int f157e;
    /* renamed from: f */
    private int f158f;
    /* renamed from: g */
    private boolean f159g;
    /* renamed from: h */
    private final NodeList f160h;
    /* renamed from: i */
    private final ReentrantReadWriteLock f161i;
    /* renamed from: j */
    private C0963b f162j;
    /* renamed from: k */
    private long f163k;
    /* renamed from: l */
    private ListenerList f164l;
    /* renamed from: m */
    private ListenerList f165m;
    /* renamed from: n */
    private int f166n;
    /* renamed from: o */
    private HTTPServerList f167o;
    /* renamed from: p */
    private ListenerList f168p;
    /* renamed from: q */
    private String f169q;
    /* renamed from: r */
    private C0955i f170r;
    /* renamed from: s */
    private Object f171s;

    static {
        C0795g.m302f();
    }

    public C0961c() {
        this(8008, 8058);
    }

    public C0961c(int i, int i2) {
        this(i, i2, null);
    }

    public C0961c(int i, int i2, InetAddress[] inetAddressArr) {
        this.f156d = new Mutex();
        this.f157e = 0;
        this.f158f = 0;
        this.f160h = new NodeList();
        this.f161i = new ReentrantReadWriteLock();
        this.f164l = new ListenerList();
        this.f165m = new ListenerList();
        this.f153a = new ListenerList();
        this.f166n = 3;
        this.f167o = new HTTPServerList();
        this.f168p = new ListenerList();
        this.f169q = "/evetSub";
        this.f171s = null;
        this.f154b = new SSDPNotifySocketList(inetAddressArr);
        this.f155c = new SSDPSearchResponseSocketList(inetAddressArr);
        m512a(i);
        m531b(i2);
        m523a(null);
        m513a(60);
        m521a(null);
        m526a(false);
        m521a(null);
    }

    /* renamed from: a */
    private void m501a(C0802b c0802b) {
        this.f161i.writeLock().lock();
        try {
            this.f160h.add(c0802b);
        } finally {
            this.f161i.writeLock().unlock();
        }
    }

    /* renamed from: b */
    private Device m502b(C0802b c0802b) {
        if (c0802b == null) {
            return null;
        }
        C0802b g = c0802b.m392g(Device.ELEM_NAME);
        return g != null ? new Device(c0802b, g) : null;
    }

    /* renamed from: c */
    private void m503c(C0802b c0802b) {
        Device b = m502b(c0802b);
        if (b != null && b.isRootDevice()) {
            m538c(b);
        }
        this.f161i.writeLock().lock();
        try {
            this.f160h.remove(c0802b);
        } finally {
            this.f161i.writeLock().unlock();
        }
    }

    /* renamed from: d */
    private String m504d(String str) {
        return C0763a.m37a(str, m530b(), m548i());
    }

    /* renamed from: e */
    private synchronized void m505e(C0800f c0800f) {
        if (!c0800f.m349q()) {
            TestXlog.m29i("####addDevice return not root device ######");
        } else if (!TextUtils.equals("tubicast", "tubicast") || c0800f.m341i().contains("description.xml") || c0800f.m341i().contains("description_dmr.xml")) {
            TestXlog.m29i(c0800f.toString());
            Device a = m511a(C0789k.m219b(c0800f.m346n()));
            if (a != null) {
                TestXlog.m29i("#### addDevice dev != null,AND return now######");
                a.setSSDPPacket(c0800f);
            } else {
                String i = c0800f.m341i();
                TestXlog.m29i("#### description location is " + i + " ######");
                try {
                    String str;
                    URL url = new URL(i);
                    C0803c d = C0795g.m300d();
                    String content = HttpUtil.getContent(url);
                    boolean z = false;
                    if (content.startsWith("<")) {
                        str = content;
                    } else {
                        try {
                            str = new String(StringUtil.eds(content.getBytes("ISO-8859-1")));
                            z = true;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            z = true;
                            str = content;
                        }
                    }
                    TestXlog.m29i("#### description " + i + "HttpUtil.getContent is " + str + " ######");
                    if (TextUtils.isEmpty(str) || str.startsWith("HttpError")) {
                        TestXlog.m29i("#### Error " + i + "get description empty");
                    } else {
                        TestXlog.m29i("#### control point parse description over######");
                    }
                    C0802b parse = d.parse(str);
                    Device b = m502b(parse);
                    if (b != null) {
                        b.encryption = z;
                        b.setSSDPPacket(c0800f);
                        m501a(parse);
                        TestXlog.m29i("#### control point addDevice over######");
                        m533b(b);
                    }
                } catch (Exception e2) {
                    TestXlog.m29i("#### Error MalformedURLException");
                    Debug.warning(c0800f.toString());
                    Debug.warning(e2);
                } catch (Exception e22) {
                    TestXlog.m29i("#### Error ParserException pe");
                    Debug.warning(c0800f.toString());
                    Debug.warning(e22);
                }
            }
        } else {
            TestXlog.m29i("####addDevice return not 38400 ######");
        }
    }

    /* renamed from: f */
    private void m506f(C0800f c0800f) {
        if (c0800f.m352t()) {
            m532b(C0789k.m219b(c0800f.m346n()));
        }
    }

    /* renamed from: m */
    private SSDPNotifySocketList m507m() {
        return this.f154b;
    }

    /* renamed from: n */
    private SSDPSearchResponseSocketList m508n() {
        return this.f155c;
    }

    /* renamed from: o */
    private HTTPServerList m509o() {
        return this.f167o;
    }

    /* renamed from: a */
    public int m510a() {
        return this.f157e;
    }

    /* renamed from: a */
    public Device m511a(String str) {
        this.f161i.readLock().lock();
        try {
            int size = this.f160h.size();
            for (int i = 0; i < size; i++) {
                Device b = m502b(this.f160h.getNode(i));
                if (b != null) {
                    if (b.isDevice(str)) {
                        this.f161i.readLock().unlock();
                        return b;
                    }
                    b = b.getDevice(str);
                    if (b != null) {
                        return b;
                    }
                }
            }
            this.f161i.readLock().unlock();
            return null;
        } finally {
            this.f161i.readLock().unlock();
        }
    }

    /* renamed from: a */
    public void m512a(int i) {
        this.f157e = i;
    }

    /* renamed from: a */
    public void m513a(long j) {
        this.f163k = j;
    }

    /* renamed from: a */
    public void m514a(long j, boolean z) {
        DeviceList d = m542d();
        int size = d.size();
        for (int i = 0; i < size; i++) {
            m519a(d.getDevice(i), j, z);
        }
    }

    /* renamed from: a */
    public void m515a(String str, int i) {
        C0999i c0999i = new C0999i(str, i);
        SSDPSearchResponseSocketList n = m508n();
        for (int i2 = 0; i2 < n.size(); i2++) {
            Log.d("gggl", "search " + n.getSSDPSearchResponseSocket(i2).m324b());
        }
        n.post(c0999i);
    }

    /* renamed from: a */
    public void m516a(String str, int i, String str2) {
        m508n().post(new C0999i(str, i), str2);
    }

    /* renamed from: a */
    public void m517a(String str, long j, String str2, String str3) {
        int size = this.f168p.size();
        for (int i = 0; i < size; i++) {
            ((C0791a) this.f168p.get(i)).m268a(str, j, str2, str3);
        }
    }

    /* renamed from: a */
    protected void m518a(Device device) {
        if (device != null) {
            m503c(device.getRootNode());
        }
    }

    /* renamed from: a */
    public void m519a(Device device, long j, boolean z) {
        int i = 0;
        ServiceList serviceList = device.getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0790e service = serviceList.getService(i2);
            if (service.m267r() && !m528a(service, service.m264o(), j, z)) {
                m527a(service, j, z);
            }
        }
        DeviceList deviceList = device.getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            m519a(deviceList.getDevice(i), j, z);
            i++;
        }
    }

    /* renamed from: a */
    public void m520a(Device device, boolean z) {
        int i = 0;
        ServiceList serviceList = device.getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0790e service = serviceList.getService(i2);
            if (service.m266q()) {
                m529a(service, z);
            }
        }
        DeviceList deviceList = device.getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            m520a(deviceList.getDevice(i), z);
            i++;
        }
    }

    /* renamed from: a */
    public void m521a(C0955i c0955i) {
        this.f170r = c0955i;
    }

    /* renamed from: a */
    public void m522a(DeviceChangeListener deviceChangeListener) {
        this.f153a.add(deviceChangeListener);
    }

    /* renamed from: a */
    public void m523a(C0963b c0963b) {
        this.f162j = c0963b;
    }

    /* renamed from: a */
    public void m524a(C0788j c0788j) {
        this.f165m.add(c0788j);
    }

    /* renamed from: a */
    public void m525a(C0800f c0800f) {
        int size = this.f164l.size();
        for (int i = 0; i < size; i++) {
            try {
                ((C0784f) this.f164l.get(i)).m211a(c0800f);
            } catch (Exception e) {
                Debug.warning("NotifyListener returned an error:", e);
            }
        }
    }

    /* renamed from: a */
    public void m526a(boolean z) {
        this.f159g = z;
    }

    /* renamed from: a */
    public boolean m527a(C0790e c0790e, long j, boolean z) {
        if (c0790e.m267r()) {
            return m528a(c0790e, c0790e.m264o(), j, z);
        }
        Device c = c0790e.m243c();
        if (c == null) {
            return false;
        }
        String interfaceAddress = c.getInterfaceAddress();
        C0986f c0986f = new C0986f();
        c0986f.m620a(c0790e, m504d(interfaceAddress), j);
        C0987g a = c0986f.m618a(c.encryption);
        if (a.m453B()) {
            c0790e.m259k(a.m627F());
            c0790e.m232a(a.m628G());
            return true;
        }
        c0790e.m265p();
        return false;
    }

    /* renamed from: a */
    public boolean m528a(C0790e c0790e, String str, long j, boolean z) {
        C0986f c0986f = new C0986f();
        c0986f.m623b(c0790e, str, j);
        if (Debug.isOn()) {
            c0986f.mo2461U();
        }
        C0987g a = c0986f.m618a(z);
        if (Debug.isOn()) {
            a.mo2462E();
        }
        if (a.m453B()) {
            c0790e.m259k(a.m627F());
            c0790e.m232a(a.m628G());
            return true;
        }
        c0790e.m265p();
        return false;
    }

    /* renamed from: a */
    public boolean m529a(C0790e c0790e, boolean z) {
        C0986f c0986f = new C0986f();
        c0986f.m619a(c0790e);
        if (!c0986f.m618a(z).m453B()) {
            return false;
        }
        c0790e.m265p();
        return true;
    }

    /* renamed from: b */
    public int m530b() {
        return this.f158f;
    }

    /* renamed from: b */
    public void m531b(int i) {
        this.f158f = i;
    }

    /* renamed from: b */
    protected void m532b(String str) {
        m518a(m511a(str));
    }

    /* renamed from: b */
    public void m533b(Device device) {
        int size = this.f153a.size();
        for (int i = 0; i < size; i++) {
            ((DeviceChangeListener) this.f153a.get(i)).deviceAdded(device);
        }
    }

    /* renamed from: b */
    public void m534b(C0800f c0800f) {
        int size = this.f165m.size();
        for (int i = 0; i < size; i++) {
            try {
                ((C0788j) this.f165m.get(i)).deviceSearchResponseReceived(c0800f);
            } catch (Exception e) {
                Debug.warning("SearchResponseListener returned an error:", e);
            }
        }
    }

    /* renamed from: b */
    public void m535b(boolean z) {
        DeviceList d = m542d();
        int size = d.size();
        for (int i = 0; i < size; i++) {
            m520a(d.getDevice(i), z);
        }
    }

    /* renamed from: b */
    public boolean m536b(String str, int i) {
        Log.d("gggl", "ControlPoint start!");
        m551l();
        int b = m530b();
        HTTPServerList o = m509o();
        int i2 = 0;
        while (!o.open(b)) {
            i2++;
            if (100 < i2) {
                return false;
            }
            m531b(b + 1);
            b = m530b();
        }
        o.addRequestListener(this);
        o.start();
        SSDPNotifySocketList m = m507m();
        if (!m.open()) {
            return false;
        }
        m.setControlPoint(this);
        m.start();
        b = m510a();
        SSDPSearchResponseSocketList n = m508n();
        i2 = 0;
        while (!n.open(b)) {
            i2++;
            if (100 < i2) {
                return false;
            }
            m512a(b + 1);
            b = m510a();
        }
        n.setControlPoint(this);
        n.start();
        m515a(str, i);
        C0963b c0963b = new C0963b(this);
        m523a(c0963b);
        c0963b.start();
        if (m541c()) {
            C0955i c0955i = new C0955i(this);
            m521a(c0955i);
            c0955i.start();
        }
        return true;
    }

    /* renamed from: c */
    public void m537c(String str) {
        m516a(Device.UPNP_ROOTDEVICE, 3, str);
    }

    /* renamed from: c */
    public void m538c(Device device) {
        int size = this.f153a.size();
        for (int i = 0; i < size; i++) {
            ((DeviceChangeListener) this.f153a.get(i)).deviceRemoved(device);
        }
    }

    /* renamed from: c */
    public void m539c(C0800f c0800f) {
        if (c0800f.m349q()) {
            if (c0800f.m351s()) {
                m505e(c0800f);
            } else if (c0800f.m352t()) {
                TestXlog.m29i("####notifyReceived packet.isByeBye,and remove device ######");
                m506f(c0800f);
            }
        }
        m525a(c0800f);
    }

    /* renamed from: c */
    public void m540c(boolean z) {
        m514a(-1, z);
    }

    /* renamed from: c */
    public boolean m541c() {
        return this.f159g;
    }

    /* renamed from: d */
    public DeviceList m542d() {
        DeviceList deviceList;
        this.f161i.readLock().lock();
        try {
            deviceList = new DeviceList();
            int size = this.f160h.size();
            for (int i = 0; i < size; i++) {
                Device b = m502b(this.f160h.getNode(i));
                if (b != null) {
                    deviceList.add(b);
                }
            }
            return deviceList;
        } finally {
            deviceList = this.f161i.readLock();
            deviceList.unlock();
        }
    }

    /* renamed from: d */
    public void m543d(C0800f c0800f) {
        if (c0800f.m349q()) {
            m505e(c0800f);
        }
        m534b(c0800f);
    }

    /* renamed from: e */
    public void m544e() {
        int i = 0;
        DeviceList d = m542d();
        int size = d.size();
        Device[] deviceArr = new Device[size];
        for (int i2 = 0; i2 < size; i2++) {
            deviceArr[i2] = d.getDevice(i2);
        }
        while (i < size) {
            if (deviceArr[i].isExpired()) {
                Debug.message("Expired device = " + deviceArr[i].getFriendlyName());
                m518a(deviceArr[i]);
            }
            i++;
        }
    }

    /* renamed from: f */
    public long m545f() {
        return this.f163k;
    }

    public void finalize() {
        m551l();
    }

    /* renamed from: g */
    public C0963b m546g() {
        return this.f162j;
    }

    /* renamed from: h */
    public void m547h() {
        m515a(Device.UPNP_ROOTDEVICE, 3);
    }

    public void httpRequestRecieved(C0953e c0953e) {
        if (Debug.isOn()) {
            c0953e.mo2461U();
        }
        if (c0953e.m425G()) {
            C0997b c0997b = new C0997b(c0953e);
            String Y = c0997b.mo2566Y();
            long Z = c0997b.m652Z();
            PropertyList aa = c0997b.aa();
            int size = aa.size();
            for (int i = 0; i < size; i++) {
                C0792c property = aa.getProperty(i);
                m517a(Y, Z, property.m269a(), property.m271b());
            }
            c0953e.m437S();
            return;
        }
        c0953e.m438T();
    }

    /* renamed from: i */
    public String m548i() {
        return this.f169q;
    }

    /* renamed from: j */
    public C0955i m549j() {
        return this.f170r;
    }

    /* renamed from: k */
    public boolean m550k() {
        return m536b(Device.UPNP_ROOTDEVICE, 3);
    }

    /* renamed from: l */
    public boolean m551l() {
        m535b(false);
        SSDPNotifySocketList m = m507m();
        m.stop();
        m.close();
        m.clear();
        SSDPSearchResponseSocketList n = m508n();
        n.stop();
        n.close();
        n.clear();
        HTTPServerList o = m509o();
        o.stop();
        o.close();
        o.clear();
        C0963b g = m546g();
        if (g != null) {
            g.stop();
            m523a(null);
        }
        C0955i j = m549j();
        if (j != null) {
            j.stop();
            m521a(null);
        }
        return true;
    }
}
