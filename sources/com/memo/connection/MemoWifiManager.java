package com.memo.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MemoWifiManager {
    public static final String CONFIGURED_NETWORKS_CHANGED_ACTION = "android.net.wifi.CONFIGURED_NETWORKS_CHANGE";
    public static String EXTRA_PREVIOUS_WIFI_AP_STATE = null;
    public static String EXTRA_WIFI_AP_STATE = null;
    public static final String LINK_CONFIGURATION_CHANGED_ACTION = "android.net.wifi.LINK_CONFIGURATION_CHANGED";
    private static final String TAG = "MemoWifiManager";
    public static String WIFI_AP_STATE_CHANGED_ACTION = null;
    public static int WIFI_AP_STATE_DISABLED = 0;
    public static int WIFI_AP_STATE_DISABLING = 0;
    public static int WIFI_AP_STATE_ENABLED = 0;
    public static int WIFI_AP_STATE_ENABLING = 0;
    public static int WIFI_AP_STATE_FAILED = 0;
    public static final int WIFI_FREQUENCY_BAND_2GHZ = 2;
    public static final int WIFI_FREQUENCY_BAND_5GHZ = 1;
    public static final int WIFI_FREQUENCY_BAND_AUTO = 0;
    public static boolean isWifiEnabled = false;
    public static int networkId = 0;
    private boolean isMobileDataEnabled = false;
    protected Context mContext;
    private IntentFilter mFilter;
    private List<OnNetworkStateChangedListener> mOnNetworkStateChangedListeners = new ArrayList();
    private BroadcastReceiver mReceiver;
    protected WifiManager mWifiManager;

    /* renamed from: com.memo.connection.MemoWifiManager$1 */
    class C07341 extends BroadcastReceiver {
        C07341() {
        }

        public void onReceive(Context context, Intent intent) {
            MemoWifiManager.this.handleEvent(context, intent);
        }
    }

    public interface IConnectWifiListener {
        void connectComlete(String str);

        void connectFaild(String str);
    }

    public interface OnNetworkStateChangedListener {
        void onNetworkStateChanged(boolean z);
    }

    static {
        WIFI_AP_STATE_DISABLING = 10;
        WIFI_AP_STATE_DISABLED = 11;
        WIFI_AP_STATE_ENABLING = 12;
        WIFI_AP_STATE_ENABLED = 13;
        WIFI_AP_STATE_FAILED = 14;
        WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
        EXTRA_WIFI_AP_STATE = "wifi_state";
        EXTRA_PREVIOUS_WIFI_AP_STATE = "previous_wifi_state";
        try {
            Class cls = Class.forName("android.net.wifi.WifiManager");
            Field field = cls.getField("WIFI_AP_STATE_DISABLING");
            Field field2 = cls.getField("WIFI_AP_STATE_DISABLED");
            Field field3 = cls.getField("WIFI_AP_STATE_ENABLING");
            Field field4 = cls.getField("WIFI_AP_STATE_ENABLED");
            Field field5 = cls.getField("WIFI_AP_STATE_FAILED");
            Field field6 = cls.getField("WIFI_AP_STATE_CHANGED_ACTION");
            Field field7 = cls.getField("EXTRA_WIFI_AP_STATE");
            Field field8 = cls.getField("EXTRA_PREVIOUS_WIFI_AP_STATE");
            WIFI_AP_STATE_DISABLING = field.getInt(cls);
            WIFI_AP_STATE_DISABLED = field2.getInt(cls);
            WIFI_AP_STATE_ENABLING = field3.getInt(cls);
            WIFI_AP_STATE_ENABLED = field4.getInt(cls);
            WIFI_AP_STATE_FAILED = field5.getInt(cls);
            WIFI_AP_STATE_CHANGED_ACTION = field6.get(cls).toString();
            EXTRA_WIFI_AP_STATE = field7.get(cls).toString();
            EXTRA_PREVIOUS_WIFI_AP_STATE = field8.get(cls).toString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        }
    }

    public MemoWifiManager(Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        initFilterAndReceiver();
    }

    private int addNetwork(WifiConfiguration wifiConfiguration) {
        return this.mWifiManager.addNetwork(wifiConfiguration);
    }

    public static boolean deleteConfig(WifiManager wifiManager, int i) {
        return wifiManager.disableNetwork(i) && wifiManager.removeNetwork(i) && wifiManager.saveConfiguration();
    }

    public static WifiConfiguration generateWifiSoftAPConfig(String str) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = str;
        wifiConfiguration.preSharedKey = "12345678";
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.set(0);
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        return wifiConfiguration;
    }

    public static WifiConfiguration generateWifiSoftAPConfig2(String str) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + str + "\"";
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.set(0);
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.allowedKeyManagement.set(0);
        return wifiConfiguration;
    }

    public static WifiConfiguration generateWifiSoftAPConfig2(String str, int i, Context context, String str2) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        if (str.startsWith("\"")) {
            wifiConfiguration.SSID = str;
        } else {
            wifiConfiguration.SSID = "\"" + str + "\"";
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        for (WifiConfiguration wifiConfiguration2 : wifiManager.getConfiguredNetworks()) {
            Log.d("gggl", wifiConfiguration2.SSID);
            if (wifiConfiguration2.SSID.equals(wifiConfiguration.SSID)) {
                deleteConfig(wifiManager, wifiConfiguration2.networkId);
            }
        }
        if (i == 1) {
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.allowedKeyManagement.set(0);
        }
        if (i == 2) {
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.wepKeys[0] = "\"" + str2 + "\"";
            wifiConfiguration.allowedAuthAlgorithms.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedGroupCiphers.set(0);
            wifiConfiguration.allowedGroupCiphers.set(1);
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.wepTxKeyIndex = 0;
        }
        if (i == 3) {
            wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.allowedAuthAlgorithms.set(0);
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedKeyManagement.set(1);
            wifiConfiguration.allowedPairwiseCiphers.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedPairwiseCiphers.set(2);
            wifiConfiguration.status = 2;
        }
        return wifiConfiguration;
    }

    private String getCurrentConnection(Context context) {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        Object obj = (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) ? null : 1;
        return (obj == null || !(activeNetworkInfo == null ? false : activeNetworkInfo.getTypeName().equalsIgnoreCase("WIFI"))) ? null : connectionInfo.getSSID();
    }

    public static String getWifiSSID(Context context) {
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        return connectionInfo == null ? "" : connectionInfo.getSSID();
    }

    public static int getWifiStrength(Context context) {
        int rssi = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getRssi();
        return (rssi > 0 || rssi < -50) ? (rssi >= -50 || rssi < -70) ? (rssi >= -70 || rssi < -80) ? (rssi >= -80 || rssi < -100) ? 5 : 4 : 3 : 2 : 1;
    }

    private void handleEvent(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
            updateWifiState(intent.getIntExtra("wifi_state", 4));
        } else if ("android.net.wifi.SCAN_RESULTS".equals(action) || CONFIGURED_NETWORKS_CHANGED_ACTION.equals(action) || LINK_CONFIGURATION_CHANGED_ACTION.equals(action)) {
            if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
                updateAccessPoints();
            } else {
                updateAccessPoints();
            }
        } else if (!"android.net.wifi.supplicant.STATE_CHANGE".equals(action)) {
            if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                for (OnNetworkStateChangedListener onNetworkStateChangedListener : this.mOnNetworkStateChangedListeners) {
                    if (onNetworkStateChangedListener != null) {
                        onNetworkStateChangedListener.onNetworkStateChanged(networkInfo.isConnected());
                    }
                }
            } else if (WIFI_AP_STATE_CHANGED_ACTION.equals(action)) {
                int intExtra = intent.getIntExtra(EXTRA_WIFI_AP_STATE, WIFI_AP_STATE_FAILED);
                getAPConfiguration();
                updateWifiAPState(intExtra);
            }
        }
    }

    private void initFilterAndReceiver() {
        this.mFilter = new IntentFilter();
        this.mFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        this.mFilter.addAction("android.net.wifi.SCAN_RESULTS");
        this.mFilter.addAction("android.net.wifi.NETWORK_IDS_CHANGED");
        this.mFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        this.mFilter.addAction("android.net.wifi.STATE_CHANGE");
        this.mFilter.addAction("android.net.wifi.RSSI_CHANGED");
        this.mFilter.addAction(WIFI_AP_STATE_CHANGED_ACTION);
        this.mReceiver = new C07341();
    }

    private void setWifiApEnabled(WifiConfiguration wifiConfiguration) {
        Field declaredField = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
        declaredField.setAccessible(true);
        Object obj = declaredField.get(wifiConfiguration);
        declaredField.setAccessible(false);
        if (obj != null) {
            declaredField = obj.getClass().getDeclaredField("SSID");
            declaredField.setAccessible(true);
            declaredField.set(obj, wifiConfiguration.SSID);
            declaredField.setAccessible(false);
            declaredField = obj.getClass().getDeclaredField("BSSID");
            declaredField.setAccessible(true);
            declaredField.set(obj, wifiConfiguration.BSSID);
            declaredField.setAccessible(false);
            declaredField = obj.getClass().getDeclaredField("dhcpEnable");
            declaredField.setAccessible(true);
            declaredField.setInt(obj, 1);
            declaredField.setAccessible(false);
            declaredField = obj.getClass().getDeclaredField("secureType");
            declaredField.setAccessible(true);
            declaredField.set(obj, "open");
            declaredField.setAccessible(false);
        }
    }

    public void addOnNetworkStateChangedListener(OnNetworkStateChangedListener onNetworkStateChangedListener) {
        if (onNetworkStateChangedListener != null && !this.mOnNetworkStateChangedListeners.contains(onNetworkStateChangedListener)) {
            this.mOnNetworkStateChangedListeners.add(onNetworkStateChangedListener);
        }
    }

    public void connectWifi(Context context, String str, String str2, int i, IConnectWifiListener iConnectWifiListener) {
        final Context context2 = context;
        final String str3 = str;
        final String str4 = str2;
        final int i2 = i;
        final IConnectWifiListener iConnectWifiListener2 = iConnectWifiListener;
        new Thread("connect wifi " + str) {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r9 = this;
                r1 = 0;
                r8 = 1;
                r4 = new com.memo.connection.WifiAdmin;
                r0 = r3;
                r4.<init>(r0);
                r3 = r1;
            L_0x000a:
                r0 = 2;
                if (r3 >= r0) goto L_0x0059;
            L_0x000d:
                r0 = r4;
                r2 = r5;
                r5 = r6;
                r5 = com.memo.connection.WifiAdmin.WifiCipherType.valueOf(r5);
                r4.connect(r0, r2, r5);
                r0 = r4;
                r0 = r4.isExsits(r0);
                if (r0 != 0) goto L_0x005a;
            L_0x0022:
                r0 = r4;
                r2 = r5;
                r5 = r6;
                r5 = com.memo.connection.WifiAdmin.WifiCipherType.valueOf(r5);
                r0 = r4.createWifiInfo(r0, r2, r5);
                r2 = com.memo.connection.MemoWifiManager.this;
                r2 = r2.mWifiManager;
                r0 = r2.addNetwork(r0);
                r2 = com.memo.connection.MemoWifiManager.this;
                r2 = r2.mWifiManager;
                r0 = r2.enableNetwork(r0, r8);
            L_0x0040:
                if (r0 == 0) goto L_0x0044;
            L_0x0042:
                if (r3 < r8) goto L_0x009d;
            L_0x0044:
                r0 = com.memo.connection.MemoWifiManager.this;
                r0 = r0.mWifiManager;
                r0 = r0.getConfiguredNetworks();
                if (r0 != 0) goto L_0x0065;
            L_0x004e:
                r0 = r7;
                if (r0 == 0) goto L_0x0059;
            L_0x0052:
                r0 = r7;
                r1 = r4;
                r0.connectFaild(r1);
            L_0x0059:
                return;
            L_0x005a:
                r2 = com.memo.connection.MemoWifiManager.this;
                r2 = r2.mWifiManager;
                r0 = r0.networkId;
                r0 = r2.enableNetwork(r0, r8);
                goto L_0x0040;
            L_0x0065:
                r2 = r0.iterator();
            L_0x0069:
                r0 = r2.hasNext();
                if (r0 == 0) goto L_0x009d;
            L_0x006f:
                r0 = r2.next();
                r0 = (android.net.wifi.WifiConfiguration) r0;
                r5 = r0.SSID;
                r6 = r4;
                r5 = r5.contains(r6);
                if (r5 != 0) goto L_0x0089;
            L_0x007f:
                r5 = r4;
                r6 = r0.SSID;
                r5 = r5.contains(r6);
                if (r5 == 0) goto L_0x0093;
            L_0x0089:
                r5 = com.memo.connection.MemoWifiManager.this;
                r5 = r5.mWifiManager;
                r0 = r0.networkId;
                r5.enableNetwork(r0, r8);
                goto L_0x0069;
            L_0x0093:
                r5 = com.memo.connection.MemoWifiManager.this;
                r5 = r5.mWifiManager;
                r0 = r0.networkId;
                r5.disableNetwork(r0);
                goto L_0x0069;
            L_0x009d:
                r0 = r1;
            L_0x009e:
                r2 = r0 + 1;
                r5 = 7;
                if (r0 >= r5) goto L_0x00db;
            L_0x00a3:
                r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
                java.lang.Thread.sleep(r6);	 Catch:{ InterruptedException -> 0x00b8 }
            L_0x00a8:
                r0 = com.memo.connection.MemoWifiManager.this;
                r5 = r3;
                r0 = r0.getCurrentConnection(r5);
                r5 = android.text.TextUtils.isEmpty(r0);
                if (r5 == 0) goto L_0x00bd;
            L_0x00b6:
                r0 = r2;
                goto L_0x009e;
            L_0x00b8:
                r0 = move-exception;
                r0.printStackTrace();
                goto L_0x00a8;
            L_0x00bd:
                r5 = "\"";
                r6 = "";
                r0 = r0.replace(r5, r6);
                r5 = r4;
                r0 = android.text.TextUtils.equals(r5, r0);
                if (r0 == 0) goto L_0x00d9;
            L_0x00cd:
                r0 = r7;
                if (r0 == 0) goto L_0x0059;
            L_0x00d1:
                r0 = r7;
                r1 = r4;
                r0.connectComlete(r1);
                goto L_0x0059;
            L_0x00d9:
                r0 = r2;
                goto L_0x009e;
            L_0x00db:
                if (r8 != r3) goto L_0x00e8;
            L_0x00dd:
                r0 = r7;
                if (r0 == 0) goto L_0x00e8;
            L_0x00e1:
                r0 = r7;
                r2 = r4;
                r0.connectFaild(r2);
            L_0x00e8:
                r0 = r3 + 1;
                r3 = r0;
                goto L_0x000a;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.memo.connection.MemoWifiManager.2.run():void");
            }
        }.start();
    }

    public boolean disconnect() {
        return this.mWifiManager.disconnect();
    }

    public boolean enableNetwork(int i, boolean z) {
        return this.mWifiManager.enableNetwork(i, z);
    }

    public WifiConfiguration getAPConfiguration() {
        try {
            return (WifiConfiguration) this.mWifiManager.getClass().getMethod("getWifiApConfiguration", new Class[0]).invoke(this.mWifiManager, (Object[]) null);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
        return null;
    }

    public WifiInfo getConnectionInfo() {
        return this.mWifiManager.getConnectionInfo();
    }

    public int getCurrentConfiguration() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        return connectionInfo == null ? -1 : connectionInfo.getNetworkId();
    }

    public DhcpInfo getDhcpInfo() {
        return this.mWifiManager.getDhcpInfo();
    }

    public List<ScanResult> getScanResults() {
        return this.mWifiManager.getScanResults();
    }

    public boolean isWifiEnabled() {
        return this.mWifiManager.isWifiEnabled();
    }

    public boolean reconnect() {
        return this.mWifiManager.reconnect();
    }

    public void registerReceiver() {
        this.mContext.registerReceiver(this.mReceiver, this.mFilter);
    }

    public void removeOnNetworkStateChangedListener(OnNetworkStateChangedListener onNetworkStateChangedListener) {
        if (onNetworkStateChangedListener != null && this.mOnNetworkStateChangedListeners.contains(onNetworkStateChangedListener)) {
            this.mOnNetworkStateChangedListeners.remove(onNetworkStateChangedListener);
        }
    }

    public void restoreDataPreference() {
        if (isWifiEnabled) {
            setWifiEnabled(true);
            enableNetwork(networkId, true);
        } else {
            setWifiEnabled(false);
        }
        MobileDataManager.setMobileDataEnabled(this.mContext, this.isMobileDataEnabled);
    }

    public void saveDataPreference() {
        this.isMobileDataEnabled = MobileDataManager.getMobileDataEnabled(this.mContext);
        isWifiEnabled = isWifiEnabled();
        if (isWifiEnabled) {
            networkId = getCurrentConfiguration();
        }
    }

    public void setFrequencyBand(int i, boolean z) {
        try {
            this.mWifiManager.getClass().getMethod("setFrequencyBand", new Class[]{Integer.TYPE, Boolean.TYPE}).invoke(this.mWifiManager, new Object[]{Integer.valueOf(i), Boolean.valueOf(z)});
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e2) {
        } catch (IllegalArgumentException e3) {
        } catch (IllegalAccessException e4) {
        } catch (InvocationTargetException e5) {
        }
    }

    public boolean setWifiApConfiguration(WifiConfiguration wifiConfiguration) {
        try {
            return ((Boolean) this.mWifiManager.getClass().getMethod("setWifiApConfiguration", new Class[]{WifiConfiguration.class}).invoke(this.mWifiManager, new Object[]{wifiConfiguration})).booleanValue();
        } catch (SecurityException e) {
            return false;
        } catch (NoSuchMethodException e2) {
            return false;
        } catch (IllegalArgumentException e3) {
            return false;
        } catch (IllegalAccessException e4) {
            return false;
        } catch (InvocationTargetException e5) {
            return false;
        }
    }

    public boolean setWifiApEnabled(WifiConfiguration wifiConfiguration, boolean z) {
        try {
            if (Build.MODEL.toLowerCase(Locale.US).contains("htc")) {
                setWifiApEnabled(wifiConfiguration);
            }
            return ((Boolean) this.mWifiManager.getClass().getMethod("setWifiApEnabled", new Class[]{WifiConfiguration.class, Boolean.TYPE}).invoke(this.mWifiManager, new Object[]{wifiConfiguration, Boolean.valueOf(z)})).booleanValue();
        } catch (SecurityException e) {
            return false;
        } catch (NoSuchMethodException e2) {
            return false;
        } catch (IllegalArgumentException e3) {
            return false;
        } catch (IllegalAccessException e4) {
            return false;
        } catch (InvocationTargetException e5) {
            return false;
        } catch (NoSuchFieldException e6) {
            e6.printStackTrace();
            return false;
        }
    }

    public boolean setWifiEnabled(boolean z) {
        return this.mWifiManager.setWifiEnabled(z);
    }

    public void unregisterReceiver() {
        if (this.mReceiver != null) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
    }

    protected synchronized void updateAccessPoints() {
    }

    protected void updateWifiAPState(int i) {
    }

    protected void updateWifiNetworkState() {
    }

    protected void updateWifiState(int i) {
        switch (i) {
            case 0:
            case 1:
                for (OnNetworkStateChangedListener onNetworkStateChangedListener : this.mOnNetworkStateChangedListeners) {
                    if (onNetworkStateChangedListener != null) {
                        onNetworkStateChangedListener.onNetworkStateChanged(false);
                    }
                }
                return;
            default:
                return;
        }
    }
}
