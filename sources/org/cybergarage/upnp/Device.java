package org.cybergarage.upnp;

import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import cz.msebera.android.httpclient.HttpStatus;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.lingala.zip4j.util.InternalZipConstants;
import org.cybergarage.http.C0767b;
import org.cybergarage.http.C0770f;
import org.cybergarage.http.C0953e;
import org.cybergarage.http.C0954g;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.p000a.C0763a;
import org.cybergarage.p001b.C0985c;
import org.cybergarage.upnp.device.C0785g;
import org.cybergarage.upnp.device.C0786h;
import org.cybergarage.upnp.device.C0787i;
import org.cybergarage.upnp.device.C0962a;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.event.C0793d;
import org.cybergarage.upnp.event.C0794e;
import org.cybergarage.upnp.event.C0986f;
import org.cybergarage.upnp.event.C0987g;
import org.cybergarage.upnp.p002a.C0775a;
import org.cybergarage.upnp.p002a.C0776f;
import org.cybergarage.upnp.p002a.C0995d;
import org.cybergarage.upnp.p002a.C1001b;
import org.cybergarage.upnp.p002a.C1002c;
import org.cybergarage.upnp.p002a.C1003g;
import org.cybergarage.upnp.p003b.C0958c;
import org.cybergarage.upnp.ssdp.C0800f;
import org.cybergarage.upnp.ssdp.C0965e;
import org.cybergarage.upnp.ssdp.C0966k;
import org.cybergarage.upnp.ssdp.C0998d;
import org.cybergarage.upnp.ssdp.C1000j;
import org.cybergarage.upnp.ssdp.SSDPSearchSocketList;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;
import org.cybergarage.util.StringUtil;
import org.cybergarage.util.TimerUtil;
import org.cybergarage.xml.C0802b;
import org.cybergarage.xml.C0803c;
import org.cybergarage.xml.ParserException;

public class Device implements C0770f, C0787i {
    private static final String CONFIG_ID = "configId";
    public static final String DEFAULT_DESCRIPTION_URI = "/description.xml";
    public static final int DEFAULT_DISCOVERY_WAIT_TIME = 300;
    public static final int DEFAULT_LEASE_TIME = 1800;
    public static final String DEFAULT_PRESENTATION_URI = "/presentation";
    public static final int DEFAULT_STARTUP_WAIT_TIME = 1000;
    private static final String DEVICE_TYPE = "deviceType";
    public static final String ELEM_NAME = "device";
    private static final String FRIENDLY_NAME = "friendlyName";
    public static final int HTTP_DEFAULT_PORT = 4004;
    private static final String MANUFACTURE = "manufacturer";
    private static final String MANUFACTURE_URL = "manufacturerURL";
    private static final String MODEL_DESCRIPTION = "modelDescription";
    private static final String MODEL_NAME = "modelName";
    private static final String MODEL_NUMBER = "modelNumber";
    private static final String MODEL_URL = "modelURL";
    private static final String NAME = "Name";
    private static final String SERIAL_NUMBER = "serialNumber";
    public static final String SERVICE_LIST_STR = "<serviceList>\n         <service>\n            <serviceType>urn:schemas-upnp-org:service:RenderingControl:1</serviceType>\n            <serviceId>urn:upnp-org:serviceId:RenderingControl</serviceId>\n            <SCPDURL>/service/RenderingControl1.xml</SCPDURL>\n            <controlURL>/service/RenderingControl_control</controlURL>\n            <eventSubURL>/service/RenderingControl_event</eventSubURL>\n         </service>\n         <service>\n            <serviceType>urn:schemas-upnp-org:service:ConnectionManager:1</serviceType>\n            <serviceId>urn:upnp-org:serviceId:ConnectionManager</serviceId>\n            <SCPDURL>/service/ConnectionManager1.xml</SCPDURL>\n            <controlURL>/service/ConnectionManager_control</controlURL>\n            <eventSubURL>/service/ConnectionManager_event</eventSubURL>\n         </service>\n         <service>\n            <serviceType>urn:schemas-upnp-org:service:AVTransport:1</serviceType>\n            <serviceId>urn:upnp-org:serviceId:AVTransport</serviceId>\n            <SCPDURL>/service/AVTransport1.xml</SCPDURL>\n            <controlURL>/service/AVTransport_control</controlURL>\n            <eventSubURL>/service/AVTransport_event</eventSubURL>\n         </service>\n      </serviceList>";
    private static final String UDN = "UDN";
    private static final String UPC = "UPC";
    public static final String UPNP_ROOTDEVICE = "upnp:rootdevice";
    private static final String URLBASE_NAME = "URLBase";
    private static Calendar cal = Calendar.getInstance();
    private static final String presentationURL = "presentationURL";
    private int bootId;
    private String devUUID;
    private C0802b deviceNode;
    public boolean encryption;
    private HashMap<String, byte[]> iconBytesMap;
    private Mutex mutex;
    private C0785g presentationListener;
    private C0802b rootNode;
    private Object userData;
    private boolean wirelessMode;

    static {
        C0795g.m302f();
    }

    public Device() {
        this(null, null);
    }

    public Device(File file) {
        this(null, null);
        loadDescription(file);
    }

    public Device(InputStream inputStream) {
        this(null, null);
        loadDescription(inputStream);
    }

    public Device(String str) {
        this(new File(str));
    }

    public Device(C0802b c0802b) {
        this(null, c0802b);
    }

    public Device(C0802b c0802b, C0802b c0802b2) {
        this.encryption = false;
        this.mutex = new Mutex();
        this.iconBytesMap = new HashMap();
        this.userData = null;
        this.rootNode = c0802b;
        this.deviceNode = c0802b2;
        C0802b g = this.deviceNode.m392g(ServiceList.ELEM_NAME);
        if (g == null) {
            try {
                g = C0795g.m300d().parse(SERVICE_LIST_STR);
            } catch (ParserException e) {
                e.printStackTrace();
            }
            this.deviceNode.m381c(g);
        }
        setUUID(C0795g.m297b());
        setWirelessMode(false);
    }

    private void deviceActionControlRecieved(C1001b c1001b, C0790e c0790e) {
        if (Debug.isOn()) {
            c1001b.mo2461U();
        }
        C0777a d = c0790e.m246d(c1001b.m661Z());
        if (d == null) {
            invalidActionControlRecieved(c1001b);
            return;
        }
        try {
            d.m181d().setReqArgs(c1001b.aa());
            if (!d.m175a(c1001b)) {
                invalidActionControlRecieved(c1001b);
            }
        } catch (IllegalArgumentException e) {
            invalidArgumentsControlRecieved(c1001b);
        }
    }

    private void deviceControlRequestRecieved(C0995d c0995d, C0790e c0790e) {
        if (c0995d.ab()) {
            deviceQueryControlRecieved(new C1003g(c0995d), c0790e);
        } else {
            deviceActionControlRecieved(new C1001b(c0995d), c0790e);
        }
    }

    private void deviceEventNewSubscriptionRecieved(C0790e c0790e, C0986f c0986f) {
        String V = c0986f.m613V();
        try {
            URL url = new URL(V);
            long Z = c0986f.m617Z();
            String a = C0794e.m290a();
            C0793d c0793d = new C0793d();
            c0793d.m279b(V);
            c0793d.m275a(Z);
            c0793d.m276a(a);
            c0790e.m235a(c0793d);
            C0987g c0987g = new C0987g();
            c0987g.m457d(200);
            c0987g.mo2100m(a);
            c0987g.m629b(Z);
            if (Debug.isOn()) {
                c0987g.mo2462E();
            }
            c0986f.m621a(c0987g);
            if (Debug.isOn()) {
                c0987g.mo2462E();
            }
            c0790e.m237a(this.encryption);
        } catch (Exception e) {
            upnpBadSubscriptionRecieved(c0986f, HttpStatus.SC_PRECONDITION_FAILED);
        }
    }

    private void deviceEventRenewSubscriptionRecieved(C0790e c0790e, C0986f c0986f) {
        String X = c0986f.m615X();
        C0793d j = c0790e.m258j(X);
        if (j == null) {
            upnpBadSubscriptionRecieved(c0986f, HttpStatus.SC_PRECONDITION_FAILED);
            return;
        }
        long Z = c0986f.m617Z();
        j.m275a(Z);
        j.m288k();
        C0987g c0987g = new C0987g();
        c0987g.m457d(200);
        c0987g.mo2100m(X);
        c0987g.m629b(Z);
        c0986f.m621a(c0987g);
        if (Debug.isOn()) {
            c0987g.mo2462E();
        }
    }

    private void deviceEventSubscriptionRecieved(C0986f c0986f) {
        C0790e serviceByEventSubURL = getServiceByEventSubURL(c0986f.m426H());
        if (serviceByEventSubURL == null) {
            c0986f.m438T();
        } else if (!c0986f.m614W() && !c0986f.m616Y()) {
            upnpBadSubscriptionRecieved(c0986f, HttpStatus.SC_PRECONDITION_FAILED);
        } else if (c0986f.m424F()) {
            deviceEventUnsubscriptionRecieved(serviceByEventSubURL, c0986f);
        } else if (c0986f.m614W()) {
            deviceEventNewSubscriptionRecieved(serviceByEventSubURL, c0986f);
        } else if (c0986f.m616Y()) {
            deviceEventRenewSubscriptionRecieved(serviceByEventSubURL, c0986f);
        } else {
            upnpBadSubscriptionRecieved(c0986f, HttpStatus.SC_PRECONDITION_FAILED);
        }
    }

    private void deviceEventUnsubscriptionRecieved(C0790e c0790e, C0986f c0986f) {
        C0793d j = c0790e.m258j(c0986f.m615X());
        if (j == null) {
            upnpBadSubscriptionRecieved(c0986f, HttpStatus.SC_PRECONDITION_FAILED);
            return;
        }
        c0790e.m241b(j);
        C0987g c0987g = new C0987g();
        c0987g.m457d(200);
        c0986f.m621a(c0987g);
        if (Debug.isOn()) {
            c0987g.mo2462E();
        }
    }

    private void deviceQueryControlRecieved(C1003g c1003g, C0790e c0790e) {
        if (Debug.isOn()) {
            c1003g.mo2461U();
        }
        String Y = c1003g.mo2566Y();
        if (!c0790e.m250f(Y)) {
            invalidActionControlRecieved(c1003g);
        } else if (!getStateVariable(Y).m565a(c1003g, this.encryption)) {
            invalidActionControlRecieved(c1003g);
        }
    }

    private C0962a getAdvertiser() {
        return getDeviceData().m490n();
    }

    private synchronized byte[] getDescriptionData(String str) {
        byte[] bArr;
        if (!isNMPRMode()) {
            updateURLBase(str);
        }
        C0802b rootNode = getRootNode();
        if (rootNode == null) {
            bArr = new byte[0];
        } else {
            bArr = (((new String() + "<?xml version=\"1.0\" encoding=\"utf-8\"?>") + "\n") + rootNode.toString()).getBytes();
        }
        return bArr;
    }

    private String getDescriptionURI() {
        return getDeviceData().m472b();
    }

    private C0958c getDeviceData() {
        C0802b deviceNode = getDeviceNode();
        C0958c c0958c = (C0958c) deviceNode.m397j();
        if (c0958c != null) {
            return c0958c;
        }
        Object c0958c2 = new C0958c();
        deviceNode.m365a(c0958c2);
        c0958c2.m185b(deviceNode);
        return c0958c2;
    }

    private HTTPServerList getHTTPServerList() {
        return getDeviceData().m481e();
    }

    private String getNotifyDeviceNT() {
        return !isRootDevice() ? getUDN() : UPNP_ROOTDEVICE;
    }

    private String getNotifyDeviceTypeNT() {
        return getDeviceType();
    }

    private String getNotifyDeviceTypeUSN() {
        return getUDN() + "::" + getDeviceType();
    }

    private String getNotifyDeviceUSN() {
        return !isRootDevice() ? getUDN() : getUDN() + "::" + UPNP_ROOTDEVICE;
    }

    private SSDPSearchSocketList getSSDPSearchSocketList() {
        return getDeviceData().m484h();
    }

    private void httpGetRequestRecieved(C0953e c0953e) {
        String str = null;
        String H = c0953e.m426H();
        Debug.message("httpGetRequestRecieved = " + H);
        if (H == null) {
            c0953e.m438T();
            return;
        }
        String M;
        byte[] descriptionData;
        byte[] bArr = new byte[0];
        if (isDescriptionURI(H)) {
            M = c0953e.m431M();
            if (M == null || M.length() <= 0) {
                M = C0763a.m35a();
            }
            str = "en";
            descriptionData = getDescriptionData(M);
            M = "text/xml; charset=\"utf-8\"";
        } else {
            Device deviceByDescriptionURI = getDeviceByDescriptionURI(H);
            if (deviceByDescriptionURI != null) {
                M = "text/xml; charset=\"utf-8\"";
                str = "en";
                descriptionData = deviceByDescriptionURI.getDescriptionData(c0953e.m431M());
            } else {
                C0790e serviceBySCPDURL = getServiceBySCPDURL(H);
                if (serviceBySCPDURL != null) {
                    M = "text/xml; charset=\"utf-8\"";
                    str = "en";
                    descriptionData = serviceBySCPDURL.m260k();
                } else if (isIconBytesURI(H)) {
                    C0780d iconByURI = getIconByURI(H);
                    if (iconByURI != null) {
                        M = iconByURI.m201b();
                        descriptionData = iconByURI.m206g();
                    } else {
                        descriptionData = bArr;
                        M = null;
                    }
                } else {
                    c0953e.m438T();
                    return;
                }
            }
        }
        C0954g c0954g = new C0954g();
        c0954g.m457d(200);
        if (M != null) {
            c0954g.m110h(M);
        }
        if (str != null) {
            c0954g.m113i(str);
        }
        c0954g.m84a(descriptionData);
        c0953e.m443a(c0954g);
    }

    private void httpPostRequestRecieved(C0953e c0953e) {
        if (c0953e.m427I()) {
            soapActionRecieved(c0953e);
        } else {
            c0953e.m438T();
        }
    }

    private boolean initializeLoadedDescription() {
        setDescriptionURI(DEFAULT_DESCRIPTION_URI);
        setLeaseTime(DEFAULT_LEASE_TIME);
        setHTTPPort(HTTP_DEFAULT_PORT);
        if (!hasUDN()) {
            updateUDN();
        }
        return true;
    }

    private void invalidActionControlRecieved(C0995d c0995d) {
        C0954g c1002c = new C1002c();
        c1002c.m648e(HttpStatus.SC_UNAUTHORIZED);
        c0995d.m443a(c1002c);
    }

    private void invalidArgumentsControlRecieved(C0995d c0995d) {
        C0954g c1002c = new C1002c();
        c1002c.m648e(HttpStatus.SC_PAYMENT_REQUIRED);
        c0995d.m443a(c1002c);
    }

    private boolean isDescriptionURI(String str) {
        String descriptionURI = getDescriptionURI();
        return (str == null || descriptionURI == null) ? false : descriptionURI.equals(str);
    }

    public static boolean isDeviceNode(C0802b c0802b) {
        return ELEM_NAME.equals(c0802b.m377c());
    }

    private boolean isPresentationRequest(C0953e c0953e) {
        if (!c0953e.m420B()) {
            return false;
        }
        String H = c0953e.m426H();
        if (H == null) {
            return false;
        }
        String presentationURL = getPresentationURL();
        return presentationURL != null ? H.startsWith(presentationURL) : false;
    }

    public static final void notifyWait() {
        TimerUtil.waitRandom(300);
    }

    private void setAdvertiser(C0962a c0962a) {
        getDeviceData().m469a(c0962a);
    }

    private void setDescriptionFile(File file) {
        getDeviceData().m467a(file);
    }

    private void setDescriptionURI(String str) {
        getDeviceData().m468a(str);
    }

    private void setURLBase(String str) {
        if (isRootDevice()) {
            C0802b g = getRootNode().m392g(URLBASE_NAME);
            if (g != null) {
                g.m374b(str);
                return;
            }
            g = new C0802b(URLBASE_NAME);
            g.m374b(str);
            if (getRootNode().m395i()) {
                getRootNode().m371a(g, 1);
            } else {
                getRootNode().m371a(g, 1);
            }
        }
    }

    private void setUUID(String str) {
        this.devUUID = str;
    }

    private void soapActionRecieved(C0953e c0953e) {
        C0790e serviceByControlURL = getServiceByControlURL(c0953e.m426H());
        if (serviceByControlURL != null) {
            deviceControlRequestRecieved(new C1001b(c0953e), serviceByControlURL);
        } else {
            soapBadActionRecieved(c0953e);
        }
    }

    private void soapBadActionRecieved(C0953e c0953e) {
        C0954g c0985c = new C0985c();
        c0985c.m457d(400);
        c0953e.m443a(c0985c);
    }

    private boolean stop(boolean z) {
        if (z) {
            byebye();
        }
        HTTPServerList hTTPServerList = getHTTPServerList();
        hTTPServerList.stop();
        hTTPServerList.close();
        hTTPServerList.clear();
        SSDPSearchSocketList sSDPSearchSocketList = getSSDPSearchSocketList();
        sSDPSearchSocketList.stop();
        sSDPSearchSocketList.close();
        sSDPSearchSocketList.clear();
        C0962a advertiser = getAdvertiser();
        if (advertiser != null) {
            advertiser.stop();
            setAdvertiser(null);
        }
        return true;
    }

    private void updateBootId() {
        this.bootId = C0795g.m299c();
    }

    private void updateConfigId(Device device) {
        int i = 0;
        DeviceList deviceList = device.getDeviceList();
        int size = deviceList.size();
        int i2 = 0;
        int i3 = 0;
        while (i2 < size) {
            Device device2 = deviceList.getDevice(i2);
            updateConfigId(device2);
            i2++;
            i3 = (i3 + device2.getConfigId()) & ViewCompat.MEASURED_SIZE_MASK;
        }
        ServiceList serviceList = device.getServiceList();
        int size2 = serviceList.size();
        while (i < size2) {
            C0790e service = serviceList.getService(i);
            service.m249f();
            i3 = (i3 + service.m251g()) & ViewCompat.MEASURED_SIZE_MASK;
            i++;
        }
        C0802b deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.m367a(CONFIG_ID, (i3 + C0795g.m294a(deviceNode.toString())) & ViewCompat.MEASURED_SIZE_MASK);
        }
    }

    private void updateUDN() {
        setUDN("uuid:" + getUUID());
    }

    private void updateURLBase(String str) {
        setURLBase(C0763a.m37a(str, getHTTPPort(), ""));
    }

    private void upnpBadSubscriptionRecieved(C0986f c0986f, int i) {
        C0987g c0987g = new C0987g();
        c0987g.m630e(i);
        c0986f.m621a(c0987g);
    }

    public void addDevice(Device device) {
        C0802b g = getDeviceNode().m392g(DeviceList.ELEM_NAME);
        if (g == null) {
            g = new C0802b(DeviceList.ELEM_NAME);
            getDeviceNode().m381c(g);
        }
        g.m381c(device.getDeviceNode());
        device.setRootNode(null);
        if (getRootNode() == null) {
            g = new C0802b("root");
            g.m385d("", "urn:schemas-upnp-org:device-1-0");
            C0802b c0802b = new C0802b("specVersion");
            C0802b c0802b2 = new C0802b("major");
            c0802b2.m374b("1");
            C0802b c0802b3 = new C0802b("minor");
            c0802b3.m374b("0");
            c0802b.m381c(c0802b2);
            c0802b.m381c(c0802b3);
            g.m381c(c0802b);
            setRootNode(g);
        }
    }

    public boolean addIcon(C0780d c0780d) {
        C0802b deviceNode = getDeviceNode();
        if (deviceNode == null) {
            return false;
        }
        C0802b g = deviceNode.m392g(IconList.ELEM_NAME);
        if (g == null) {
            g = new C0802b(IconList.ELEM_NAME);
            deviceNode.m381c(g);
        }
        deviceNode = new C0802b("icon");
        if (c0780d.m198a() != null) {
            deviceNode.m376b(c0780d.m198a());
        }
        g.m381c(deviceNode);
        if (c0780d.m204e() && c0780d.m205f()) {
            this.iconBytesMap.put(c0780d.m203d(), c0780d.m206g());
        }
        return true;
    }

    public void addService(C0790e c0790e) {
        C0802b g = getDeviceNode().m392g(ServiceList.ELEM_NAME);
        if (g == null) {
            g = new C0802b(ServiceList.ELEM_NAME);
            getDeviceNode().m381c(g);
        }
        g.m381c(c0790e.m231a());
    }

    public void announce() {
        String[] strArr;
        int i;
        notifyWait();
        InetAddress[] f = getDeviceData().m482f();
        if (f != null) {
            String[] strArr2 = new String[f.length];
            for (int i2 = 0; i2 < f.length; i2++) {
                strArr2[i2] = f[i2].getHostAddress();
            }
            strArr = strArr2;
        } else {
            int b;
            b = C0763a.m40b();
            strArr = new String[b];
            for (i = 0; i < b; i++) {
                strArr[i] = C0763a.m36a(i);
            }
        }
        i = 0;
        while (i < strArr.length) {
            if (!(strArr[i] == null || strArr[i].length() == 0)) {
                int sSDPAnnounceCount = getSSDPAnnounceCount();
                for (b = 0; b < sSDPAnnounceCount; b++) {
                    announce(strArr[i]);
                }
            }
            i++;
        }
    }

    public void announce(String str) {
        String notifyDeviceUSN;
        int i = 0;
        String locationURL = getLocationURL(str);
        C0965e c0965e = new C0965e(str);
        C0998d c0998d = new C0998d();
        c0998d.m117k(C0795g.m295a());
        c0998d.m632f(getLeaseTime());
        c0998d.m636s(locationURL);
        c0998d.m635r("ssdp:alive");
        c0998d.m633g(getBootId());
        if (isRootDevice()) {
            locationURL = getNotifyDeviceNT();
            notifyDeviceUSN = getNotifyDeviceUSN();
            c0998d.m634q(locationURL);
            c0998d.m637t(notifyDeviceUSN);
            c0965e.m577a(c0998d);
            locationURL = getUDN();
            c0998d.m634q(locationURL);
            c0998d.m637t(locationURL);
            c0965e.m577a(c0998d);
        }
        locationURL = getNotifyDeviceTypeNT();
        notifyDeviceUSN = getNotifyDeviceTypeUSN();
        c0998d.m634q(locationURL);
        c0998d.m637t(notifyDeviceUSN);
        c0965e.m577a(c0998d);
        c0965e.m318f();
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            serviceList.getService(i2).m254h(str);
        }
        DeviceList deviceList = getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            deviceList.getDevice(i).announce(str);
            i++;
        }
    }

    public void byebye() {
        String[] strArr;
        int i;
        InetAddress[] f = getDeviceData().m482f();
        if (f != null) {
            String[] strArr2 = new String[f.length];
            for (int i2 = 0; i2 < f.length; i2++) {
                strArr2[i2] = f[i2].getHostAddress();
            }
            strArr = strArr2;
        } else {
            int b;
            b = C0763a.m40b();
            strArr = new String[b];
            for (i = 0; i < b; i++) {
                strArr[i] = C0763a.m36a(i);
            }
        }
        i = 0;
        while (i < strArr.length) {
            if (strArr[i] != null && strArr[i].length() > 0) {
                int sSDPAnnounceCount = getSSDPAnnounceCount();
                for (b = 0; b < sSDPAnnounceCount; b++) {
                    byebye(strArr[i]);
                }
            }
            i++;
        }
    }

    public void byebye(String str) {
        String notifyDeviceNT;
        String notifyDeviceUSN;
        int i = 0;
        C0965e c0965e = new C0965e(str);
        C0998d c0998d = new C0998d();
        c0998d.m635r("ssdp:byebye");
        if (isRootDevice()) {
            notifyDeviceNT = getNotifyDeviceNT();
            notifyDeviceUSN = getNotifyDeviceUSN();
            c0998d.m634q(notifyDeviceNT);
            c0998d.m637t(notifyDeviceUSN);
            c0965e.m577a(c0998d);
        }
        notifyDeviceNT = getNotifyDeviceTypeNT();
        notifyDeviceUSN = getNotifyDeviceTypeUSN();
        c0998d.m634q(notifyDeviceNT);
        c0998d.m637t(notifyDeviceUSN);
        c0965e.m577a(c0998d);
        c0965e.m318f();
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            serviceList.getService(i2).m256i(str);
        }
        DeviceList deviceList = getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            deviceList.getDevice(i).byebye(str);
            i++;
        }
    }

    public void deviceSearchReceived(C0800f c0800f) {
        deviceSearchResponse(c0800f);
    }

    public void deviceSearchResponse(C0800f c0800f) {
        String k = c0800f.m343k();
        if (k != null) {
            int i;
            int i2;
            boolean isRootDevice = isRootDevice();
            String udn = getUDN();
            if (isRootDevice) {
                udn = udn + "::upnp:rootdevice";
            }
            if (C0786h.m213a(k)) {
                String notifyDeviceNT = getNotifyDeviceNT();
                int i3 = isRootDevice ? 3 : 2;
                for (i = 0; i < i3; i++) {
                    postSearchResponse(c0800f, notifyDeviceNT, udn);
                }
            } else if (C0786h.m214b(k)) {
                if (isRootDevice) {
                    postSearchResponse(c0800f, UPNP_ROOTDEVICE, udn);
                }
            } else if (C0786h.m215c(k)) {
                String udn2 = getUDN();
                if (k.equals(udn2)) {
                    postSearchResponse(c0800f, udn2, udn);
                }
            } else if (C0786h.m216d(k)) {
                udn = getDeviceType();
                if (k.equals(udn)) {
                    postSearchResponse(c0800f, udn, getUDN() + "::" + udn);
                }
            }
            ServiceList serviceList = getServiceList();
            i = serviceList.size();
            for (i2 = 0; i2 < i; i2++) {
                serviceList.getService(i2).m239a(c0800f);
            }
            DeviceList deviceList = getDeviceList();
            i = deviceList.size();
            for (i2 = 0; i2 < i; i2++) {
                deviceList.getDevice(i2).deviceSearchResponse(c0800f);
            }
        }
    }

    public String getAbsoluteURL(String str) {
        String uRLBase;
        String str2 = null;
        Device rootDevice = getRootDevice();
        if (rootDevice != null) {
            uRLBase = rootDevice.getURLBase();
            str2 = rootDevice.getLocation();
        } else {
            uRLBase = null;
        }
        return getAbsoluteURL(str, uRLBase, str2);
    }

    public String getAbsoluteURL(String str, String str2, String str3) {
        if (str == null || str.length() <= 0) {
            return "";
        }
        try {
            return new URL(str).toString();
        } catch (Exception e) {
            if ((str2 == null || str2.length() <= 0) && str3 != null && str3.length() > 0) {
                if (str3.endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR) && str.startsWith(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
                    try {
                        return new URL(str3 + str.substring(1)).toString();
                    } catch (Exception e2) {
                        return new URL(C0767b.m53a(str3, str)).toString();
                    }
                }
                try {
                    return new URL(str3 + str).toString();
                } catch (Exception e3) {
                    try {
                        return new URL(C0767b.m53a(str3, str)).toString();
                    } catch (Exception e4) {
                        Device rootDevice = getRootDevice();
                        if (rootDevice != null) {
                            String location = rootDevice.getLocation();
                            str2 = C0767b.m52a(C0767b.m56b(location), C0767b.m57c(location));
                        }
                        if (str2 == null && str2.length() > 0) {
                            if (str2.endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR) && str.startsWith(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
                                try {
                                    return new URL(str2 + str.substring(1)).toString();
                                } catch (Exception e5) {
                                    return new URL(C0767b.m53a(str2, str)).toString();
                                }
                            }
                            try {
                                return new URL(str2 + str).toString();
                            } catch (Exception e6) {
                                try {
                                    return new URL(C0767b.m53a(str2, str)).toString();
                                } catch (Exception e7) {
                                    return str;
                                }
                            }
                        }
                    }
                }
            }
            return str2 == null ? str : str;
        }
    }

    public C0777a getAction(String str) {
        int i;
        int i2 = 0;
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (i = 0; i < size; i++) {
            ActionList l = serviceList.getService(i).m261l();
            int size2 = l.size();
            for (int i3 = 0; i3 < size2; i3++) {
                C0777a action = l.getAction(i3);
                String c = action.m180c();
                if (c != null && c.equals(str)) {
                    return action;
                }
            }
        }
        DeviceList deviceList = getDeviceList();
        i = deviceList.size();
        while (i2 < i) {
            action = deviceList.getDevice(i2).getAction(str);
            if (action != null) {
                return action;
            }
            i2++;
        }
        return null;
    }

    public int getBootId() {
        return this.bootId;
    }

    public String getChipId() {
        String str = "";
        String[] split = getUDN().split(":");
        return split.length == 2 ? split[1] : str;
    }

    public int getConfigId() {
        C0802b deviceNode = getDeviceNode();
        return deviceNode == null ? 0 : deviceNode.m389f(CONFIG_ID);
    }

    public File getDescriptionFile() {
        return getDeviceData().m465a();
    }

    public String getDescriptionFilePath() {
        File descriptionFile = getDescriptionFile();
        return descriptionFile == null ? "" : descriptionFile.getAbsoluteFile().getParent();
    }

    public Device getDevice(String str) {
        DeviceList deviceList = getDeviceList();
        int size = deviceList.size();
        for (int i = 0; i < size; i++) {
            Device device = deviceList.getDevice(i);
            if (device.isDevice(str)) {
                return device;
            }
            device = device.getDevice(str);
            if (device != null) {
                return device;
            }
        }
        return null;
    }

    public Device getDeviceByDescriptionURI(String str) {
        DeviceList deviceList = getDeviceList();
        int size = deviceList.size();
        for (int i = 0; i < size; i++) {
            Device device = deviceList.getDevice(i);
            if (device.isDescriptionURI(str)) {
                return device;
            }
            device = device.getDeviceByDescriptionURI(str);
            if (device != null) {
                return device;
            }
        }
        return null;
    }

    public DeviceList getDeviceList() {
        DeviceList deviceList = new DeviceList();
        C0802b g = getDeviceNode().m392g(DeviceList.ELEM_NAME);
        if (g != null) {
            int g2 = g.m391g();
            for (int i = 0; i < g2; i++) {
                C0802b c = g.m378c(i);
                if (isDeviceNode(c)) {
                    deviceList.add(new Device(c));
                }
            }
        }
        return deviceList;
    }

    public C0802b getDeviceNode() {
        return this.deviceNode;
    }

    public String getDeviceType() {
        return getDeviceNode().m398j(DEVICE_TYPE);
    }

    public long getElapsedTime() {
        return (System.currentTimeMillis() - getTimeStamp()) / 1000;
    }

    public String getFriendlyName() {
        String str = "";
        Object j = getDeviceNode().m398j(NAME);
        return TextUtils.isEmpty(j) ? getDeviceNode().m398j(FRIENDLY_NAME) : j;
    }

    public InetAddress[] getHTTPBindAddress() {
        return getDeviceData().m482f();
    }

    public int getHTTPPort() {
        return getDeviceData().m483g();
    }

    public C0780d getIcon(int i) {
        IconList iconList = getIconList();
        return (i >= 0 || iconList.size() - 1 >= i) ? iconList.getIcon(i) : null;
    }

    public C0780d getIconByURI(String str) {
        IconList iconList = getIconList();
        if (iconList.size() <= 0) {
            return null;
        }
        int size = iconList.size();
        for (int i = 0; i < size; i++) {
            C0780d icon = iconList.getIcon(i);
            if (icon.m200a(str)) {
                return icon;
            }
        }
        return null;
    }

    public IconList getIconList() {
        IconList iconList = new IconList();
        C0802b g = getDeviceNode().m392g(IconList.ELEM_NAME);
        if (g == null) {
            return iconList;
        }
        int g2 = g.m391g();
        for (int i = 0; i < g2; i++) {
            C0802b c = g.m378c(i);
            if (C0780d.m197a(c)) {
                C0780d c0780d = new C0780d(c);
                if (c0780d.m204e()) {
                    byte[] bArr = (byte[]) this.iconBytesMap.get(c0780d.m203d());
                    if (bArr != null) {
                        c0780d.m199a(bArr);
                    }
                }
                iconList.add(c0780d);
            }
        }
        return iconList;
    }

    public String getInterfaceAddress() {
        C0800f sSDPPacket = getSSDPPacket();
        return sSDPPacket == null ? "" : sSDPPacket.m334b();
    }

    public int getLeaseTime() {
        C0800f sSDPPacket = getSSDPPacket();
        return sSDPPacket != null ? sSDPPacket.m353u() : getDeviceData().m479d();
    }

    public String getLocation() {
        C0800f sSDPPacket = getSSDPPacket();
        return sSDPPacket != null ? sSDPPacket.m341i() : getDeviceData().m476c();
    }

    public String getLocationURL(String str) {
        return C0763a.m37a(str, getHTTPPort(), getDescriptionURI());
    }

    public String getManufacture() {
        return getDeviceNode().m398j(MANUFACTURE);
    }

    public String getManufactureURL() {
        return getDeviceNode().m398j(MANUFACTURE_URL);
    }

    public String getModelDescription() {
        return getDeviceNode().m398j(MODEL_DESCRIPTION);
    }

    public String getModelName() {
        return getDeviceNode().m398j(MODEL_NAME);
    }

    public String getModelNumber() {
        return getDeviceNode().m398j(MODEL_NUMBER);
    }

    public String getModelURL() {
        return getDeviceNode().m398j(MODEL_URL);
    }

    public String getMulticastIPv4Address() {
        return getDeviceData().m487k();
    }

    public String getMulticastIPv6Address() {
        return getDeviceData().m488l();
    }

    public Device getParentDevice() {
        return isRootDevice() ? null : new Device(getDeviceNode().m361a().m361a());
    }

    public C0785g getPresentationListener() {
        return this.presentationListener;
    }

    public String getPresentationURL() {
        return getDeviceNode().m398j(presentationURL);
    }

    public Device getRootDevice() {
        C0802b rootNode = getRootNode();
        if (rootNode == null) {
            return null;
        }
        C0802b g = rootNode.m392g(ELEM_NAME);
        return g != null ? new Device(rootNode, g) : null;
    }

    public C0802b getRootNode() {
        return this.rootNode != null ? this.rootNode : this.deviceNode == null ? null : this.deviceNode.m373b();
    }

    public int getSSDPAnnounceCount() {
        return (isNMPRMode() && isWirelessMode()) ? 4 : 1;
    }

    public InetAddress[] getSSDPBindAddress() {
        return getDeviceData().m486j();
    }

    public String getSSDPIPv4MulticastAddress() {
        return getDeviceData().m487k();
    }

    public void getSSDPIPv4MulticastAddress(String str) {
        getDeviceData().m478c(str);
    }

    public String getSSDPIPv6MulticastAddress() {
        return getDeviceData().m488l();
    }

    public void getSSDPIPv6MulticastAddress(String str) {
        getDeviceData().m480d(str);
    }

    public C0800f getSSDPPacket() {
        return !isRootDevice() ? null : getDeviceData().m489m();
    }

    public int getSSDPPort() {
        return getDeviceData().m485i();
    }

    public String getSerialNumber() {
        return getDeviceNode().m398j(SERIAL_NUMBER);
    }

    public C0790e getService(String str) {
        int i = 0;
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0790e service = serviceList.getService(i2);
            if (service.m252g(str)) {
                return service;
            }
        }
        DeviceList deviceList = getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            service = deviceList.getDevice(i).getService(str);
            if (service != null) {
                return service;
            }
            i++;
        }
        return null;
    }

    public C0790e getServiceByControlURL(String str) {
        int i = 0;
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0790e service = serviceList.getService(i2);
            if (service.m242b(str)) {
                return service;
            }
        }
        DeviceList deviceList = getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            service = deviceList.getDevice(i).getServiceByControlURL(str);
            if (service != null) {
                return service;
            }
            i++;
        }
        return null;
    }

    public C0790e getServiceByEventSubURL(String str) {
        int i = 0;
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0790e service = serviceList.getService(i2);
            if (service.m244c(str)) {
                return service;
            }
        }
        DeviceList deviceList = getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            service = deviceList.getDevice(i).getServiceByEventSubURL(str);
            if (service != null) {
                return service;
            }
            i++;
        }
        return null;
    }

    public C0790e getServiceBySCPDURL(String str) {
        int i = 0;
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0790e service = serviceList.getService(i2);
            if (service.m238a(str)) {
                return service;
            }
        }
        DeviceList deviceList = getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            service = deviceList.getDevice(i).getServiceBySCPDURL(str);
            if (service != null) {
                return service;
            }
            i++;
        }
        return null;
    }

    public ServiceList getServiceList() {
        ServiceList serviceList = new ServiceList();
        C0802b g = getDeviceNode().m392g(ServiceList.ELEM_NAME);
        if (g != null) {
            int g2 = g.m391g();
            for (int i = 0; i < g2; i++) {
                C0802b c = g.m378c(i);
                if (C0790e.m224a(c)) {
                    serviceList.add(new C0790e(c));
                }
            }
        }
        return serviceList;
    }

    public C0780d getSmallestIcon() {
        C0780d c0780d = null;
        IconList iconList = getIconList();
        int size = iconList.size();
        int i = 0;
        while (i < size) {
            C0780d icon = iconList.getIcon(i);
            if (c0780d != null && icon.m202c() >= c0780d.m202c()) {
                icon = c0780d;
            }
            i++;
            c0780d = icon;
        }
        return c0780d;
    }

    public C0964f getStateVariable(String str) {
        return getStateVariable(null, str);
    }

    public C0964f getStateVariable(String str, String str2) {
        int i = 0;
        if (str == null && str2 == null) {
            return null;
        }
        C0964f e;
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0790e service = serviceList.getService(i2);
            if (str == null || service.m245d().equals(str)) {
                e = service.m248e(str2);
                if (e != null) {
                    return e;
                }
            }
        }
        DeviceList deviceList = getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            e = deviceList.getDevice(i).getStateVariable(str, str2);
            if (e != null) {
                return e;
            }
            i++;
        }
        return null;
    }

    public C0790e getSubscriberService(String str) {
        int i = 0;
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0790e service = serviceList.getService(i2);
            if (str.equals(service.m264o())) {
                return service;
            }
        }
        DeviceList deviceList = getDeviceList();
        int size2 = deviceList.size();
        while (i < size2) {
            service = deviceList.getDevice(i).getSubscriberService(str);
            if (service != null) {
                return service;
            }
            i++;
        }
        return null;
    }

    public long getTimeStamp() {
        C0800f sSDPPacket = getSSDPPacket();
        return sSDPPacket != null ? sSDPPacket.m335c() : 0;
    }

    public String getUDN() {
        return getDeviceNode().m398j(UDN);
    }

    public String getUPC() {
        return getDeviceNode().m398j(UPC);
    }

    public String getURLBase() {
        if (!isRootDevice()) {
            return "";
        }
        String j = getRootNode().m398j(URLBASE_NAME);
        if (!TextUtils.isEmpty(j)) {
            return j;
        }
        Matcher matcher = Pattern.compile("(http://\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d+).*").matcher(getLocation());
        return matcher.find() ? matcher.group(1) : j;
    }

    public String getUUID() {
        return this.devUUID;
    }

    public Object getUserData() {
        return this.userData;
    }

    public boolean hasPresentationListener() {
        return this.presentationListener != null;
    }

    public boolean hasUDN() {
        String udn = getUDN();
        return udn != null && udn.length() > 0;
    }

    public void httpRequestRecieved(C0953e c0953e) {
        if (Debug.isOn()) {
            c0953e.mo2461U();
        }
        if (hasPresentationListener() && isPresentationRequest(c0953e)) {
            getPresentationListener().m212a(c0953e);
        } else if (c0953e.m420B() || c0953e.m422D()) {
            httpGetRequestRecieved(c0953e);
        } else if (c0953e.m421C()) {
            httpPostRequestRecieved(c0953e);
        } else if (c0953e.m423E() || c0953e.m424F()) {
            deviceEventSubscriptionRecieved(new C0986f(c0953e));
        } else {
            c0953e.m438T();
        }
    }

    public boolean isDevice(String str) {
        return str == null ? false : str.endsWith(getUDN()) ? true : str.equals(getFriendlyName()) ? true : str.endsWith(getDeviceType());
    }

    public boolean isDeviceType(String str) {
        return str == null ? false : str.equals(getDeviceType());
    }

    public boolean isExpired() {
        return ((long) (getLeaseTime() + 30)) < getElapsedTime();
    }

    public boolean isIconBytesURI(String str) {
        if (((byte[]) this.iconBytesMap.get(str)) != null) {
            return true;
        }
        C0780d iconByURI = getIconByURI(str);
        return iconByURI != null ? iconByURI.m205f() : false;
    }

    public boolean isNMPRMode() {
        C0802b deviceNode = getDeviceNode();
        return (deviceNode == null || deviceNode.m392g("INMPR03") == null) ? false : true;
    }

    public boolean isRootDevice() {
        return getRootNode().m392g(ELEM_NAME).m398j(UDN).equals(getUDN());
    }

    public boolean isRunning() {
        return getAdvertiser() != null;
    }

    public boolean isWirelessMode() {
        return this.wirelessMode;
    }

    public boolean loadDescription(File file) {
        try {
            this.rootNode = C0795g.m300d().parse(file);
            if (this.rootNode == null) {
                throw new InvalidDescriptionException("Couldn't find a root node", file);
            }
            this.deviceNode = this.rootNode.m392g(ELEM_NAME);
            if (this.deviceNode == null) {
                throw new InvalidDescriptionException("Couldn't find a root device node", file);
            } else if (!initializeLoadedDescription()) {
                return false;
            } else {
                setDescriptionFile(file);
                return true;
            }
        } catch (Exception e) {
            throw new InvalidDescriptionException(e);
        }
    }

    public boolean loadDescription(InputStream inputStream) {
        try {
            C0803c d = C0795g.m300d();
            byte[] bArr = new byte[4096];
            StringBuilder stringBuilder = new StringBuilder();
            if (-1 != inputStream.read(bArr)) {
                stringBuilder.append(new String(bArr, 0, -1, "ISO-8859-1"));
            }
            this.rootNode = d.parse(StringUtil.eds(stringBuilder.toString()));
            if (this.rootNode == null) {
                throw new InvalidDescriptionException("Couldn't find a root node");
            }
            this.deviceNode = this.rootNode.m392g(ELEM_NAME);
            if (this.deviceNode == null) {
                throw new InvalidDescriptionException("Couldn't find a root device node");
            }
            if (!initializeLoadedDescription()) {
                return false;
            }
            setDescriptionFile(null);
            return true;
        } catch (Exception e) {
            throw new InvalidDescriptionException(e);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public boolean loadDescription(String str) {
        try {
            this.rootNode = C0795g.m300d().parse(str);
            if (this.rootNode == null) {
                throw new InvalidDescriptionException("Couldn't find a root node");
            }
            this.deviceNode = this.rootNode.m392g(ELEM_NAME);
            if (this.deviceNode == null) {
                throw new InvalidDescriptionException("Couldn't find a root device node");
            } else if (!initializeLoadedDescription()) {
                return false;
            } else {
                setDescriptionFile(null);
                return true;
            }
        } catch (Exception e) {
            throw new InvalidDescriptionException(e);
        }
    }

    public void lock() {
        this.mutex.lock();
    }

    public boolean postSearchResponse(C0800f c0800f, String str, String str2) {
        String locationURL = getRootDevice().getLocationURL(c0800f.m334b());
        C1000j c1000j = new C1000j();
        c1000j.m639e(getLeaseTime());
        c1000j.m81a(cal);
        c1000j.mo2100m(str);
        c1000j.m643o(str2);
        c1000j.m642n(locationURL);
        c1000j.m640f(getBootId());
        c1000j.m644p(getFriendlyName());
        TimerUtil.waitRandom(c0800f.m347o() * 1000);
        String d = c0800f.m336d();
        int e = c0800f.m337e();
        C0966k c0966k = new C0966k();
        if (Debug.isOn()) {
            c1000j.mo2462E();
        }
        int sSDPAnnounceCount = getSSDPAnnounceCount();
        for (int i = 0; i < sSDPAnnounceCount; i++) {
            c0966k.m583a(d, e, c1000j);
        }
        return true;
    }

    public boolean removePresentationURL() {
        return getDeviceNode().m396i(presentationURL);
    }

    public void setActionListener(C0775a c0775a) {
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            serviceList.getService(i).m233a(c0775a);
        }
    }

    public void setActionListener(C0775a c0775a, boolean z) {
        setActionListener(c0775a);
        if (z) {
            DeviceList deviceList = getDeviceList();
            int size = deviceList.size();
            for (int i = 0; i < size; i++) {
                deviceList.getDevice(i).setActionListener(c0775a, true);
            }
        }
    }

    public void setDeviceNode(C0802b c0802b) {
        this.deviceNode = c0802b;
    }

    public void setDeviceType(String str) {
        getDeviceNode().m388e(DEVICE_TYPE, str);
    }

    public void setFriendlyName(String str) {
        getDeviceNode().m388e(FRIENDLY_NAME, str);
    }

    public void setHTTPBindAddress(InetAddress[] inetAddressArr) {
        getDeviceData().m471a(inetAddressArr);
    }

    public void setHTTPPort(int i) {
        getDeviceData().m473b(i);
    }

    public void setLeaseTime(int i) {
        getDeviceData().m466a(i);
        C0962a advertiser = getAdvertiser();
        if (advertiser != null) {
            announce();
            advertiser.restart();
        }
    }

    public void setLocation(String str) {
        getDeviceData().m474b(str);
    }

    public void setManufacture(String str) {
        getDeviceNode().m388e(MANUFACTURE, str);
    }

    public void setManufactureURL(String str) {
        getDeviceNode().m388e(MANUFACTURE_URL, str);
    }

    public void setModelDescription(String str) {
        getDeviceNode().m388e(MODEL_DESCRIPTION, str);
    }

    public void setModelName(String str) {
        getDeviceNode().m388e(MODEL_NAME, str);
    }

    public void setModelNumber(String str) {
        getDeviceNode().m388e(MODEL_NUMBER, str);
    }

    public void setModelURL(String str) {
        getDeviceNode().m388e(MODEL_URL, str);
    }

    public void setMulticastIPv4Address(String str) {
        getDeviceData().m478c(str);
    }

    public void setMulticastIPv6Address(String str) {
        getDeviceData().m480d(str);
    }

    public void setNMPRMode(boolean z) {
        C0802b deviceNode = getDeviceNode();
        if (deviceNode != null) {
            if (z) {
                deviceNode.m388e("INMPR03", "1.0");
                deviceNode.m396i(URLBASE_NAME);
                return;
            }
            deviceNode.m396i("INMPR03");
        }
    }

    public void setPresentationListener(C0785g c0785g) {
        this.presentationListener = c0785g;
        if (c0785g != null) {
            setPresentationURL(DEFAULT_PRESENTATION_URI);
        } else {
            removePresentationURL();
        }
    }

    public void setPresentationURL(String str) {
        getDeviceNode().m388e(presentationURL, str);
    }

    public void setQueryListener(C0776f c0776f) {
        ServiceList serviceList = getServiceList();
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            serviceList.getService(i).m234a(c0776f);
        }
    }

    public void setQueryListener(C0776f c0776f, boolean z) {
        setQueryListener(c0776f);
        if (z) {
            DeviceList deviceList = getDeviceList();
            int size = deviceList.size();
            for (int i = 0; i < size; i++) {
                deviceList.getDevice(i).setQueryListener(c0776f, true);
            }
        }
    }

    public void setRootNode(C0802b c0802b) {
        this.rootNode = c0802b;
    }

    public void setSSDPBindAddress(InetAddress[] inetAddressArr) {
        getDeviceData().m475b(inetAddressArr);
    }

    public void setSSDPPacket(C0800f c0800f) {
        getDeviceData().m470a(c0800f);
    }

    public void setSSDPPort(int i) {
        getDeviceData().m477c(i);
    }

    public void setSerialNumber(String str) {
        getDeviceNode().m388e(SERIAL_NUMBER, str);
    }

    public void setUDN(String str) {
        getDeviceNode().m388e(UDN, str);
    }

    public void setUPC(String str) {
        getDeviceNode().m388e(UPC, str);
    }

    public void setUserData(Object obj) {
        this.userData = obj;
    }

    public void setWirelessMode(boolean z) {
        this.wirelessMode = z;
    }

    public boolean start() {
        stop(true);
        int hTTPPort = getHTTPPort();
        HTTPServerList hTTPServerList = getHTTPServerList();
        int i = 0;
        while (!hTTPServerList.open(hTTPPort)) {
            i++;
            if (100 < i) {
                return false;
            }
            setHTTPPort(hTTPPort + 1);
            hTTPPort = getHTTPPort();
        }
        hTTPServerList.addRequestListener(this);
        hTTPServerList.start();
        SSDPSearchSocketList sSDPSearchSocketList = getSSDPSearchSocketList();
        if (!sSDPSearchSocketList.open()) {
            return false;
        }
        sSDPSearchSocketList.addSearchListener(this);
        sSDPSearchSocketList.start();
        updateBootId();
        updateConfigId();
        announce();
        C0962a c0962a = new C0962a(this);
        setAdvertiser(c0962a);
        c0962a.start();
        return true;
    }

    public boolean stop() {
        return stop(true);
    }

    public void unlock() {
        this.mutex.unlock();
    }

    public void updateConfigId() {
        updateConfigId(this);
    }
}
