package org.cybergarage.upnp;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.cybergarage.http.C0767b;
import org.cybergarage.upnp.device.C0786h;
import org.cybergarage.upnp.event.C0793d;
import org.cybergarage.upnp.event.C0997b;
import org.cybergarage.upnp.event.SubscriberList;
import org.cybergarage.upnp.p002a.C0775a;
import org.cybergarage.upnp.p002a.C0776f;
import org.cybergarage.upnp.p003b.C0959e;
import org.cybergarage.upnp.ssdp.C0800f;
import org.cybergarage.upnp.ssdp.C0965e;
import org.cybergarage.upnp.ssdp.C0998d;
import org.cybergarage.util.Debug;
import org.cybergarage.util.HttpUtil;
import org.cybergarage.util.Mutex;
import org.cybergarage.util.StringUtil;
import org.cybergarage.xml.C0802b;
import org.cybergarage.xml.C0803c;
import org.cybergarage.xml.ParserException;

/* renamed from: org.cybergarage.upnp.e */
public class C0790e {
    /* renamed from: a */
    private C0802b f75a;
    /* renamed from: b */
    private Mutex f76b;
    /* renamed from: c */
    private Object f77c;

    public C0790e() {
        this(new C0802b("service"));
        C0802b c0802b = new C0802b("specVersion");
        C0802b c0802b2 = new C0802b("major");
        c0802b2.m374b("1");
        c0802b.m381c(c0802b2);
        c0802b2 = new C0802b("minor");
        c0802b2.m374b("0");
        c0802b.m381c(c0802b2);
        c0802b2 = new C0802b("scpd");
        c0802b2.m375b("xmlns", "urn:schemas-upnp-org:service-1-0");
        c0802b2.m381c(c0802b);
        m228v().m494a(c0802b2);
    }

    public C0790e(C0802b c0802b) {
        this.f76b = new Mutex();
        this.f77c = null;
        this.f75a = c0802b;
    }

    /* renamed from: a */
    private C0802b m220a(File file) {
        return C0795g.m300d().parse(file);
    }

    /* renamed from: a */
    private C0802b m221a(URL url) {
        String str;
        C0803c d = C0795g.m300d();
        String content = HttpUtil.getContent(url);
        if (content.startsWith("<")) {
            str = content;
        } else {
            byte[] bArr = new byte[0];
            try {
                bArr = StringUtil.edsReturnBuffer(content.toString().getBytes("ISO-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            str = new String(bArr);
        }
        return d.parse(str);
    }

    /* renamed from: a */
    private boolean m222a(String str, String str2) {
        return (str == null || str2 == null) ? false : str2.equals(str) || str2.equals(C0767b.m54a(str, false));
    }

    /* renamed from: a */
    private boolean m223a(C0793d c0793d, C0964f c0964f, boolean z) {
        String d = c0964f.m569d();
        String h = c0964f.m573h();
        String c = c0793d.m280c();
        int e = c0793d.m282e();
        C0997b c0997b = new C0997b();
        c0997b.m653a(c0793d, d, h);
        if (!c0997b.m440a(c, e, z).m453B()) {
            return false;
        }
        c0793d.m287j();
        return true;
    }

    /* renamed from: a */
    public static boolean m224a(C0802b c0802b) {
        return "service".equals(c0802b.m377c());
    }

    /* renamed from: s */
    private C0802b m225s() {
        C0802b a = m231a().m361a();
        return a == null ? null : a.m361a();
    }

    /* renamed from: t */
    private C0802b m226t() {
        return m231a().m373b();
    }

    /* renamed from: u */
    private C0802b m227u() {
        C0959e v = m228v();
        C0802b a = v.m491a();
        if (a != null) {
            return a;
        }
        Device c = m243c();
        if (c == null) {
            return null;
        }
        String h = m253h();
        String descriptionFilePath = c.getDescriptionFilePath();
        if (descriptionFilePath != null) {
            File file = new File(descriptionFilePath.concat(h));
            if (file.exists()) {
                try {
                    a = m220a(file);
                } catch (ParserException e) {
                    e.printStackTrace();
                }
                if (a != null) {
                    v.m494a(a);
                    return a;
                }
            }
        }
        try {
            a = m221a(new URL(c.getAbsoluteURL(h)));
            if (a != null) {
                v.m494a(a);
                return a;
            }
        } catch (Exception e2) {
        }
        try {
            return m220a(new File(c.getDescriptionFilePath() + C0767b.m58d(h)));
        } catch (Exception e3) {
            Debug.warning(e3);
            return null;
        }
    }

    /* renamed from: v */
    private C0959e m228v() {
        C0802b a = m231a();
        C0959e c0959e = (C0959e) a.m397j();
        if (c0959e != null) {
            return c0959e;
        }
        Object c0959e2 = new C0959e();
        a.m365a(c0959e2);
        c0959e2.m185b(a);
        return c0959e2;
    }

    /* renamed from: w */
    private String m229w() {
        return m245d();
    }

    /* renamed from: x */
    private String m230x() {
        return m240b().getUDN() + "::" + m245d();
    }

    /* renamed from: a */
    public C0802b m231a() {
        return this.f75a;
    }

    /* renamed from: a */
    public void m232a(long j) {
        m228v().m492a(j);
    }

    /* renamed from: a */
    public void m233a(C0775a c0775a) {
        ActionList l = m261l();
        int size = l.size();
        for (int i = 0; i < size; i++) {
            l.getAction(i).m174a(c0775a);
        }
    }

    /* renamed from: a */
    public void m234a(C0776f c0776f) {
        ServiceStateTable m = m262m();
        int size = m.size();
        for (int i = 0; i < size; i++) {
            m.getStateVariable(i).m562a(c0776f);
        }
    }

    /* renamed from: a */
    public void m235a(C0793d c0793d) {
        m263n().add(c0793d);
    }

    /* renamed from: a */
    public void m236a(C0964f c0964f, boolean z) {
        int i;
        int i2 = 0;
        SubscriberList n = m263n();
        int size = n.size();
        C0793d[] c0793dArr = new C0793d[size];
        for (i = 0; i < size; i++) {
            c0793dArr[i] = n.getSubscriber(i);
        }
        for (i = 0; i < size; i++) {
            C0793d c0793d = c0793dArr[i];
            if (c0793d != null && c0793d.m284g()) {
                m241b(c0793d);
            }
        }
        size = n.size();
        c0793dArr = new C0793d[size];
        for (i = 0; i < size; i++) {
            c0793dArr[i] = n.getSubscriber(i);
        }
        while (i2 < size) {
            C0793d c0793d2 = c0793dArr[i2];
            if (!(c0793d2 == null || m223a(c0793d2, c0964f, z))) {
            }
            i2++;
        }
    }

    /* renamed from: a */
    public void m237a(boolean z) {
        ServiceStateTable m = m262m();
        int size = m.size();
        for (int i = 0; i < size; i++) {
            C0964f stateVariable = m.getStateVariable(i);
            if (stateVariable.m571f()) {
                m236a(stateVariable, z);
            }
        }
    }

    /* renamed from: a */
    public boolean m238a(String str) {
        return m222a(m253h(), str);
    }

    /* renamed from: a */
    public boolean m239a(C0800f c0800f) {
        String k = c0800f.m343k();
        if (k == null) {
            return false;
        }
        Device b = m240b();
        String w = m229w();
        String x = m230x();
        if (C0786h.m213a(k)) {
            b.postSearchResponse(c0800f, w, x);
            return true;
        } else if (!C0786h.m217e(k)) {
            return true;
        } else {
            w = m245d();
            if (!k.equals(w)) {
                return true;
            }
            b.postSearchResponse(c0800f, w, x);
            return true;
        }
    }

    /* renamed from: b */
    public Device m240b() {
        return new Device(m226t(), m225s());
    }

    /* renamed from: b */
    public void m241b(C0793d c0793d) {
        m263n().remove(c0793d);
    }

    /* renamed from: b */
    public boolean m242b(String str) {
        return m222a(m255i(), str);
    }

    /* renamed from: c */
    public Device m243c() {
        return m240b().getRootDevice();
    }

    /* renamed from: c */
    public boolean m244c(String str) {
        return m222a(m257j(), str);
    }

    /* renamed from: d */
    public String m245d() {
        return m231a().m398j("serviceType");
    }

    /* renamed from: d */
    public C0777a m246d(String str) {
        ActionList l = m261l();
        int size = l.size();
        for (int i = 0; i < size; i++) {
            C0777a action = l.getAction(i);
            String c = action.m180c();
            if (c != null && c.equals(str)) {
                return action;
            }
        }
        return null;
    }

    /* renamed from: e */
    public String m247e() {
        return m231a().m398j("serviceId");
    }

    /* renamed from: e */
    public C0964f m248e(String str) {
        ServiceStateTable m = m262m();
        int size = m.size();
        for (int i = 0; i < size; i++) {
            C0964f stateVariable = m.getStateVariable(i);
            String d = stateVariable.m569d();
            if (d != null && d.equals(str)) {
                return stateVariable;
            }
        }
        return null;
    }

    /* renamed from: f */
    public void m249f() {
        C0802b u = m227u();
        if (u != null) {
            u.m367a("configId", C0795g.m294a(u.toString()));
        }
    }

    /* renamed from: f */
    public boolean m250f(String str) {
        return m248e(str) != null;
    }

    /* renamed from: g */
    public int m251g() {
        C0802b u = m227u();
        return u == null ? 0 : u.m389f("configId");
    }

    /* renamed from: g */
    public boolean m252g(String str) {
        return str == null ? false : str.endsWith(m245d()) ? true : str.endsWith(m247e());
    }

    /* renamed from: h */
    public String m253h() {
        return m231a().m398j("SCPDURL");
    }

    /* renamed from: h */
    public void m254h(String str) {
        String locationURL = m243c().getLocationURL(str);
        String w = m229w();
        String x = m230x();
        Device b = m240b();
        C0998d c0998d = new C0998d();
        c0998d.m117k(C0795g.m295a());
        c0998d.m632f(b.getLeaseTime());
        c0998d.m636s(locationURL);
        c0998d.m635r("ssdp:alive");
        c0998d.m634q(w);
        c0998d.m637t(x);
        C0965e c0965e = new C0965e(str);
        Device.notifyWait();
        c0965e.m577a(c0998d);
    }

    /* renamed from: i */
    public String m255i() {
        return m231a().m398j("controlURL");
    }

    /* renamed from: i */
    public void m256i(String str) {
        String w = m229w();
        String x = m230x();
        C0998d c0998d = new C0998d();
        c0998d.m635r("ssdp:byebye");
        c0998d.m634q(w);
        c0998d.m637t(x);
        C0965e c0965e = new C0965e(str);
        Device.notifyWait();
        c0965e.m577a(c0998d);
    }

    /* renamed from: j */
    public String m257j() {
        return m231a().m398j("eventSubURL");
    }

    /* renamed from: j */
    public C0793d m258j(String str) {
        SubscriberList n = m263n();
        int size = n.size();
        for (int i = 0; i < size; i++) {
            C0793d subscriber = n.getSubscriber(i);
            if (subscriber != null) {
                String a = subscriber.m273a();
                if (a != null && a.equals(str)) {
                    return subscriber;
                }
            }
        }
        return null;
    }

    /* renamed from: k */
    public void m259k(String str) {
        m228v().m493a(str);
    }

    /* renamed from: k */
    public byte[] m260k() {
        C0802b u = m227u();
        if (u == null) {
            return new byte[0];
        }
        return (((new String() + "<?xml version=\"1.0\" encoding=\"utf-8\"?>") + "\n") + u.toString()).getBytes();
    }

    /* renamed from: l */
    public ActionList m261l() {
        ActionList actionList = new ActionList();
        C0802b u = m227u();
        if (u != null) {
            C0802b g = u.m392g(ActionList.ELEM_NAME);
            if (g != null) {
                int g2 = g.m391g();
                for (int i = 0; i < g2; i++) {
                    C0802b c = g.m378c(i);
                    if (C0777a.m164a(c)) {
                        actionList.add(new C0777a(this.f75a, c));
                    }
                }
            }
        }
        return actionList;
    }

    /* renamed from: m */
    public ServiceStateTable m262m() {
        ServiceStateTable serviceStateTable = new ServiceStateTable();
        C0802b g = m227u().m392g(ServiceStateTable.ELEM_NAME);
        if (g != null) {
            C0802b a = m231a();
            int g2 = g.m391g();
            for (int i = 0; i < g2; i++) {
                C0802b c = g.m378c(i);
                if (C0964f.m556a(c)) {
                    serviceStateTable.add(new C0964f(a, c));
                }
            }
        }
        return serviceStateTable;
    }

    /* renamed from: n */
    public SubscriberList m263n() {
        return m228v().m495b();
    }

    /* renamed from: o */
    public String m264o() {
        return m228v().m496c();
    }

    /* renamed from: p */
    public void m265p() {
        m259k("");
        m232a(0);
    }

    /* renamed from: q */
    public boolean m266q() {
        return StringUtil.hasData(m264o());
    }

    /* renamed from: r */
    public boolean m267r() {
        return m266q();
    }
}
