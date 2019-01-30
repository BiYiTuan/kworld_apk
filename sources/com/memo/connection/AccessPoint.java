package com.memo.connection;

import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

public class AccessPoint {
    public static final int INVALID_NETWORK_ID = -1;
    private static final String KEY_CONFIG = "key_config";
    private static final String KEY_DETAILEDSTATE = "key_detailedstate";
    private static final String KEY_SCANRESULT = "key_scanresult";
    private static final String KEY_WIFIINFO = "key_wifiinfo";
    static final int SECURITY_EAP = 3;
    static final int SECURITY_NONE = 0;
    static final int SECURITY_PSK = 2;
    static final int SECURITY_WEP = 1;
    private static final int[] STATE_NONE = new int[0];
    static final String TAG = "Settings.AccessPoint";
    public String bssid;
    private WifiConfiguration mConfig;
    private WifiInfo mInfo;
    private int mRssi;
    ScanResult mScanResult;
    private DetailedState mState;
    public int networkId;
    PskType pskType = PskType.UNKNOWN;
    public int security;
    public String ssid;
    boolean wpsAvailable = false;

    enum PskType {
        UNKNOWN,
        WPA,
        WPA2,
        WPA_WPA2
    }

    public AccessPoint(ScanResult scanResult) {
        loadResult(scanResult);
        refresh();
    }

    AccessPoint(WifiConfiguration wifiConfiguration) {
        loadConfig(wifiConfiguration);
        refresh();
    }

    static String convertToQuotedString(String str) {
        return "\"" + str + "\"";
    }

    private static PskType getPskType(ScanResult scanResult) {
        boolean contains = scanResult.capabilities.contains("WPA-PSK");
        boolean contains2 = scanResult.capabilities.contains("WPA2-PSK");
        return (contains2 && contains) ? PskType.WPA_WPA2 : contains2 ? PskType.WPA2 : contains ? PskType.WPA : PskType.UNKNOWN;
    }

    private static int getSecurity(ScanResult scanResult) {
        return scanResult.capabilities.contains("WEP") ? 1 : scanResult.capabilities.contains("PSK") ? 2 : scanResult.capabilities.contains("EAP") ? 3 : 0;
    }

    static int getSecurity(WifiConfiguration wifiConfiguration) {
        return wifiConfiguration.allowedKeyManagement.get(1) ? 2 : (wifiConfiguration.allowedKeyManagement.get(2) || wifiConfiguration.allowedKeyManagement.get(3)) ? 3 : wifiConfiguration.wepKeys[0] == null ? 0 : 1;
    }

    private void loadConfig(WifiConfiguration wifiConfiguration) {
        this.ssid = wifiConfiguration.SSID == null ? "" : removeDoubleQuotes(wifiConfiguration.SSID);
        this.bssid = wifiConfiguration.BSSID;
        this.security = getSecurity(wifiConfiguration);
        this.networkId = wifiConfiguration.networkId;
        this.mRssi = Integer.MAX_VALUE;
        this.mConfig = wifiConfiguration;
    }

    private void loadResult(ScanResult scanResult) {
        this.ssid = scanResult.SSID;
        this.bssid = scanResult.BSSID;
        this.security = getSecurity(scanResult);
        boolean z = this.security != 3 && scanResult.capabilities.contains("WPS");
        this.wpsAvailable = z;
        if (this.security == 2) {
            this.pskType = getPskType(scanResult);
        }
        this.networkId = -1;
        this.mRssi = scanResult.level;
        this.mScanResult = scanResult;
    }

    private void refresh() {
        if (this.mState != null || this.mRssi == Integer.MAX_VALUE || this.mConfig == null || this.mConfig.status != 1) {
        }
    }

    static String removeDoubleQuotes(String str) {
        int length = str.length();
        return (length > 1 && str.charAt(0) == '\"' && str.charAt(length - 1) == '\"') ? str.substring(1, length - 1) : str;
    }

    protected void generateOpenNetworkConfig() {
        if (this.security != 0) {
            throw new IllegalStateException();
        } else if (this.mConfig == null) {
            this.mConfig = new WifiConfiguration();
            this.mConfig.SSID = convertToQuotedString(this.ssid);
            this.mConfig.allowedKeyManagement.set(0);
        }
    }

    WifiConfiguration getConfig() {
        return this.mConfig;
    }

    WifiInfo getInfo() {
        return this.mInfo;
    }

    int getLevel() {
        return this.mRssi == Integer.MAX_VALUE ? -1 : WifiManager.calculateSignalLevel(this.mRssi, 4);
    }

    DetailedState getState() {
        return this.mState;
    }

    public int getStrength() {
        int i = this.mRssi;
        return (i > 0 || i < -50) ? (i >= -50 || i < -70) ? (i >= -70 || i < -80) ? (i >= -80 || i < -100) ? 5 : 4 : 3 : 2 : 1;
    }

    public boolean isavaliable() {
        return this.mRssi != Integer.MAX_VALUE;
    }

    public void saveWifiState(Bundle bundle) {
        bundle.putParcelable(KEY_CONFIG, this.mConfig);
        bundle.putParcelable(KEY_SCANRESULT, this.mScanResult);
        bundle.putParcelable(KEY_WIFIINFO, this.mInfo);
        if (this.mState != null) {
            bundle.putString(KEY_DETAILEDSTATE, this.mState.toString());
        }
    }

    void update(WifiInfo wifiInfo, DetailedState detailedState) {
        if (wifiInfo != null && this.networkId != -1 && this.networkId == wifiInfo.getNetworkId()) {
            if (this.mInfo == null) {
                this.mRssi = wifiInfo.getRssi();
                this.mInfo = wifiInfo;
                this.mState = detailedState;
                refresh();
            } else {
                this.mRssi = wifiInfo.getRssi();
                this.mInfo = wifiInfo;
                this.mState = detailedState;
                refresh();
            }
        } else if (this.mInfo != null) {
            this.mInfo = null;
            this.mState = null;
            refresh();
        }
    }

    public boolean update(ScanResult scanResult) {
        if (!this.ssid.equals(scanResult.SSID) || this.security != getSecurity(scanResult)) {
            return false;
        }
        if (WifiManager.compareSignalLevel(scanResult.level, this.mRssi) > 0) {
            this.mRssi = scanResult.level;
        }
        if (this.security == 2) {
            this.pskType = getPskType(scanResult);
        }
        refresh();
        return true;
    }
}
