package com.memo.sdk;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import com.memo.OnScanResultListener;
import com.memo.TestXlog;
import com.memo.cable.DeviceContainer;
import com.memo.cable.DeviceContainer.DeviceChangeListener;
import com.memo.cable.MemoDeviceServiceHelper;
import com.memo.cable.MemoStatusPacket;
import com.memo.cable.SearchThread;
import com.memo.connection.ConnectInfoSender;
import com.memo.connection.MemoWifiManager;
import com.memo.connection.MemoWifiManager.IConnectWifiListener;
import com.memo.connection.MemoWifiManager.OnNetworkStateChangedListener;
import com.memo.connection.WifiAdmin;
import com.memo.connection.WifiAdmin.WifiCipherType;
import com.memo.connection.WifiHotspotManager;
import com.memo.connection.WifiScanner;
import com.memo.connection.WifiStepsConfig;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.upnp.C0961c;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.ssdp.C0800f;

public class MemoTVCastSDK {
    static SoftReference<ISetTvWifiListener> mISetTvWifiListener;
    private static Context sContext = null;
    private static SoftReference<C0961c> sControlPoint = null;
    private static DeviceChangeListener sDeviceChangeListener = new C09221();
    private static boolean sInited = false;
    private static MemoWifiManager sMemoWifiManager;
    private static List<IMemoAplistener> sTubiApListeners = new ArrayList();
    private static List<IMemoDeviceListener> sTubiDeviceListeners = new ArrayList();
    static WifiAdmin sWifiAdmin;
    private static WifiHotspotManager sWifiHotspotManager;
    static WifiScanner sWifiScanner;

    public interface ISetTvWifiListener {
        void onApStateChanged(MemoStatusPacket memoStatusPacket, boolean z);

        void onSendFail();
    }

    /* renamed from: com.memo.sdk.MemoTVCastSDK$1 */
    static class C09221 implements DeviceChangeListener {
        C09221() {
        }

        public void onDeviceAdd(Device device) {
            for (IMemoDeviceListener onDeviceAdd : MemoTVCastSDK.getDeviceListeners()) {
                onDeviceAdd.onDeviceAdd(device);
            }
        }

        public void onDeviceRemove(Device device) {
            for (IMemoDeviceListener onDeviceRemove : MemoTVCastSDK.getDeviceListeners()) {
                onDeviceRemove.onDeviceRemove(device);
            }
        }
    }

    /* renamed from: com.memo.sdk.MemoTVCastSDK$2 */
    static class C09232 implements org.cybergarage.upnp.device.DeviceChangeListener {
        C09232() {
        }

        public void deviceAdded(Device device) {
            DeviceContainer.getInstance().addDevice(device);
        }

        public void deviceRemoved(Device device) {
        }
    }

    public static void addOnNetworkStateChangedListener(OnNetworkStateChangedListener onNetworkStateChangedListener) {
        getWifiScanner().addOnNetworkStateChangedListener(onNetworkStateChangedListener);
    }

    public static void connectAp(Context context, String str, String str2, int i, IConnectWifiListener iConnectWifiListener) {
        getWifiManager().connectWifi(context, str, str2, i, iConnectWifiListener);
    }

    public static void exit() {
        sInited = false;
        MemoDeviceServiceHelper.getInstance().stopMemoDeviceService();
        DeviceContainer.getInstance().unRegistDeviceChangeListener(sDeviceChangeListener);
        ConnectInfoSender.getInstance().exit();
        sContext = null;
    }

    public static WifiConfiguration get4GWifiAp() {
        return getWifiHotspotManager().getAPConfiguration();
    }

    public static List<IMemoAplistener> getApListeners() {
        if (sTubiApListeners == null) {
            sTubiApListeners = new ArrayList();
        }
        return sTubiApListeners;
    }

    public static WifiCipherType getCipherType(Context context, String str) {
        for (ScanResult scanResult : ((WifiManager) context.getSystemService("wifi")).getScanResults()) {
            if (!TextUtils.isEmpty(scanResult.SSID) && scanResult.SSID.contains(str)) {
                String str2 = scanResult.capabilities;
                if (!TextUtils.isEmpty(str2)) {
                    return (str2.contains("WPA") || str2.contains("wpa")) ? WifiCipherType.WIFICIPHER_WPA : (str2.contains("WEP") || str2.contains("wep")) ? WifiCipherType.WIFICIPHER_WEP : WifiCipherType.WIFICIPHER_NOPASS;
                }
            }
        }
        return WifiCipherType.WIFICIPHER_INVALID;
    }

    public static C0961c getControlPoint() {
        if (sControlPoint == null) {
            C0961c c0961c = new C0961c();
            c0961c.m522a(new C09232());
            sControlPoint = new SoftReference(c0961c);
        }
        return (C0961c) sControlPoint.get();
    }

    public static List<IMemoDeviceListener> getDeviceListeners() {
        if (sTubiDeviceListeners == null) {
            sTubiDeviceListeners = new ArrayList();
        }
        return sTubiDeviceListeners;
    }

    public static List<Device> getDevices() {
        return DeviceContainer.getInstance().getDevices();
    }

    public static ISetTvWifiListener getISetTvWifiListener() {
        return mISetTvWifiListener == null ? null : (ISetTvWifiListener) mISetTvWifiListener.get();
    }

    public static Context getInstance() {
        return sContext;
    }

    public static String getSSID() {
        return getWifiManager().getConnectionInfo().getSSID();
    }

    public static List<ScanResult> getScanResults() {
        return getWifiManager().getScanResults();
    }

    public static Device getSelectedDevice() {
        return DeviceContainer.getInstance().getSelectedDevice();
    }

    private static WifiHotspotManager getWifiHotspotManager() {
        if (sWifiHotspotManager == null) {
            sWifiHotspotManager = WifiHotspotManager.getInstance(getInstance());
        }
        return sWifiHotspotManager;
    }

    public static MemoWifiManager getWifiManager() {
        if (sMemoWifiManager == null) {
            sMemoWifiManager = new MemoWifiManager(getInstance());
        }
        return sMemoWifiManager;
    }

    private static WifiScanner getWifiScanner() {
        if (sWifiScanner == null) {
            sWifiScanner = new WifiScanner(getInstance());
        }
        return sWifiScanner;
    }

    private static WifiAdmin getWifidmin() {
        if (sWifiAdmin == null) {
            sWifiAdmin = new WifiAdmin(getInstance());
        }
        return sWifiAdmin;
    }

    public static void init(Context context) {
        sContext = context;
        DeviceContainer.getInstance().registDeviceChangeListener(sDeviceChangeListener);
        sInited = true;
    }

    public static void onPause(Context context) {
    }

    public static void onResume(Context context) {
        if (context != null && sContext == null) {
            sContext = context.getApplicationContext();
        }
    }

    public static void openWifi() {
        getWifidmin().openWifi();
    }

    public static void registerDeviceListener(IMemoDeviceListener iMemoDeviceListener) {
        if (sTubiDeviceListeners != null && !sTubiDeviceListeners.contains(iMemoDeviceListener)) {
            sTubiDeviceListeners.add(iMemoDeviceListener);
        }
    }

    public static void registerScanReceiver(OnScanResultListener onScanResultListener) {
        getWifiScanner().registerReceiver();
        getWifiScanner().setOnScanResultListener(onScanResultListener);
    }

    public static void removeOnNetworkStateChangedListener(OnNetworkStateChangedListener onNetworkStateChangedListener) {
        getWifiScanner().removeOnNetworkStateChangedListener(onNetworkStateChangedListener);
    }

    public static void searchDeviceFromIp(final String str, final String str2) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("HTTP/1.1 200 OK\n").append("Cache-Control: max-age=5\n").append("Date: Thu, 01 Jan 1970 02:00:07 GMT\n").append("ST: upnp:rootdevice\n").append("                                                EXT:\n").append("SERVER: Platform 1.0 CyberLinkC/2.4 UPnP/1.0 DLNADOC/1.50\n").append("USN: uuid:").append(str2).append("::upnp:rootdevice\n").append("Location: http://").append(str).append(":38400/description.xml\n");
                byte[] bytes = stringBuilder.toString().getBytes();
                MemoTVCastSDK.getControlPoint().m543d(new C0800f(bytes, bytes.length));
            }
        }).start();
    }

    public static void sendHelpIpInfo(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            TestXlog.m29i("###########User Input Ip " + str + "##############");
            SearchThread.addRootDeviceIp(str);
            searchDeviceFromIp(str, "000004fecc54abad");
        }
    }

    public static void sendHomeLiveWifi(Context context, String str, String str2, String str3, boolean z, ISetTvWifiListener iSetTvWifiListener) {
        setISetWifiListener(iSetTvWifiListener);
        Log.d("gggl", "sendHomeLiveWifi");
        if (!WifiStepsConfig.isPureSearch()) {
            ConnectInfoSender.getInstance().interActiveCase1(str, str2, str3, z ? 1 : 0);
        }
    }

    public static void setControlPoint(C0961c c0961c) {
        sControlPoint = new SoftReference(c0961c);
    }

    public static void setISetWifiListener(ISetTvWifiListener iSetTvWifiListener) {
        mISetTvWifiListener = new SoftReference(iSetTvWifiListener);
    }

    public static void setSelectedDevice(Device device) {
        DeviceContainer.getInstance().setSelectedDevice(device);
    }

    public static boolean setSoftApEnable() {
        if (get4GWifiAp() == null) {
            return false;
        }
        getWifiHotspotManager().setSoftapEnabled(get4GWifiAp());
        return true;
    }

    public static void setWifiEnabled(boolean z) {
        getWifiManager().setWifiEnabled(z);
    }

    public static void startDeviceApWork() {
        if (!WifiStepsConfig.isPureSearch()) {
            TestXlog.i2("MemoTVCastSDK startDeviceApWork");
            ConnectInfoSender.getInstance().startDeviceApWork();
        }
    }

    public static void startScan() {
        getWifiScanner().start();
    }

    public static void startSearchLiveDevice() {
        MemoDeviceServiceHelper.getInstance().startFindDevice();
    }

    public static void unRegisterDeviceListener(IMemoDeviceListener iMemoDeviceListener) {
        if (sTubiDeviceListeners != null && sTubiDeviceListeners.contains(iMemoDeviceListener)) {
            sTubiDeviceListeners.remove(iMemoDeviceListener);
        }
    }

    public static void unregisterScanReceiver() {
        getWifiScanner().unregisterReceiver();
        getWifiScanner().setOnScanResultListener(null);
    }
}
