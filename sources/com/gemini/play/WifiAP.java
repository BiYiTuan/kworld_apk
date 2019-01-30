package com.gemini.play;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiAP {
    private WifiManager wifiManager;

    public boolean setWifiApEnabled(Context _this, boolean enabled, String ssid, String password) {
        this.wifiManager = (WifiManager) _this.getSystemService("wifi");
        if (enabled) {
            this.wifiManager.setWifiEnabled(false);
        }
        try {
            WifiConfiguration apConfig = new WifiConfiguration();
            apConfig.allowedAuthAlgorithms.clear();
            apConfig.allowedGroupCiphers.clear();
            apConfig.allowedKeyManagement.clear();
            apConfig.allowedPairwiseCiphers.clear();
            apConfig.allowedProtocols.clear();
            apConfig.SSID = ssid;
            apConfig.preSharedKey = password;
            apConfig.allowedAuthAlgorithms.set(0);
            apConfig.allowedGroupCiphers.set(2);
            apConfig.allowedKeyManagement.set(1);
            apConfig.allowedPairwiseCiphers.set(1);
            apConfig.allowedGroupCiphers.set(3);
            apConfig.allowedPairwiseCiphers.set(2);
            apConfig.status = 2;
            return ((Boolean) this.wifiManager.getClass().getMethod("setWifiApEnabled", new Class[]{WifiConfiguration.class, Boolean.TYPE}).invoke(this.wifiManager, new Object[]{apConfig, Boolean.valueOf(enabled)})).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }
}
