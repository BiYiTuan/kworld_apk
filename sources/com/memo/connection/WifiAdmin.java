package com.memo.connection;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;

public class WifiAdmin {
    private static final String TAG = "WifiAdmin";
    private List<WifiConfiguration> mWifiConfiguration;
    private List<ScanResult> mWifiList;
    WifiLock mWifiLock;
    private WifiManager mWifiManager;

    public enum WifiCipherType {
        WIFICIPHER_NOPASS(0),
        WIFICIPHER_WEP(1),
        WIFICIPHER_WPA(2),
        WIFICIPHER_INVALID(3);
        
        private int value;

        private WifiCipherType(int i) {
            this.value = 0;
            this.value = i;
        }

        public static WifiCipherType valueOf(int i) {
            switch (i) {
                case 0:
                    return WIFICIPHER_NOPASS;
                case 1:
                    return WIFICIPHER_WEP;
                case 2:
                    return WIFICIPHER_WPA;
                case 3:
                    return WIFICIPHER_INVALID;
                default:
                    return null;
            }
        }

        public int value() {
            return this.value;
        }
    }

    public WifiAdmin(Context context) {
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
    }

    public static String getShowPsk(String str) {
        int i = 0;
        if (str == null) {
            return "";
        }
        char charAt;
        char charAt2;
        int length;
        if (str.length() > 2) {
            charAt = str.charAt(0);
            charAt2 = str.charAt(str.length() - 1);
            length = str.length() - 2;
        } else {
            length = str.length();
            charAt2 = '\u0000';
            charAt = '\u0000';
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (charAt != '\u0000') {
            stringBuffer.append(charAt);
        }
        while (i < length) {
            stringBuffer.append("*");
            i++;
        }
        if (charAt2 != '\u0000') {
            stringBuffer.append(charAt2);
        }
        return stringBuffer.toString();
    }

    private static boolean isHex(String str) {
        for (int length = str.length() - 1; length >= 0; length--) {
            char charAt = str.charAt(length);
            if ((charAt < '0' || charAt > '9') && ((charAt < 'A' || charAt > 'F') && (charAt < 'a' || charAt > 'f'))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isHexWepKey(String str) {
        int length = str.length();
        return (length == 10 || length == 26 || length == 58) ? isHex(str) : false;
    }

    public void acquireWifiLock() {
        this.mWifiLock.acquire();
    }

    public boolean addNetwork(WifiConfiguration wifiConfiguration) {
        return this.mWifiManager.enableNetwork(this.mWifiManager.addNetwork(wifiConfiguration), true);
    }

    public int checkState() {
        return this.mWifiManager.getWifiState();
    }

    public void closeWifi() {
        if (this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(false);
        }
    }

    public boolean connect(String str, String str2, WifiCipherType wifiCipherType) {
        WifiConfiguration createWifiInfo = createWifiInfo(str, str2, wifiCipherType);
        return createWifiInfo == null ? false : addNetwork(createWifiInfo);
    }

    public void connectConfiguration(int i) {
        if (i <= this.mWifiConfiguration.size()) {
            this.mWifiManager.enableNetwork(((WifiConfiguration) this.mWifiConfiguration.get(i)).networkId, true);
        }
    }

    public void creatWifiLock() {
        this.mWifiLock = this.mWifiManager.createWifiLock("Test");
    }

    public WifiConfiguration createWifiInfo(String str, String str2, WifiCipherType wifiCipherType) {
        Log.v(TAG, "SSID = " + str + "## Password = " + str2 + "## Type = " + wifiCipherType);
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"" + str + "\"";
        WifiConfiguration isExsits = isExsits(str);
        if (isExsits != null) {
            this.mWifiManager.removeNetwork(isExsits.networkId);
        }
        if (wifiCipherType == WifiCipherType.WIFICIPHER_NOPASS) {
            wifiConfiguration.wepKeys[0] = "";
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.wepTxKeyIndex = 0;
        } else if (wifiCipherType == WifiCipherType.WIFICIPHER_WEP) {
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.wepKeys[0] = "\"" + str2 + "\"";
            wifiConfiguration.allowedAuthAlgorithms.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedGroupCiphers.set(0);
            wifiConfiguration.allowedGroupCiphers.set(1);
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.wepTxKeyIndex = 0;
        } else if (wifiCipherType == WifiCipherType.WIFICIPHER_WPA) {
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

    public boolean delWifi(String str) {
        WifiConfiguration isExsits = isExsits(str);
        if (isExsits == null) {
            return false;
        }
        isExsits.hiddenSSID = true;
        isExsits.wepKeys[0] = "\"1234\"";
        isExsits.allowedAuthAlgorithms.set(1);
        isExsits.allowedGroupCiphers.set(3);
        isExsits.allowedGroupCiphers.set(2);
        isExsits.allowedGroupCiphers.set(0);
        isExsits.allowedGroupCiphers.set(1);
        isExsits.allowedKeyManagement.set(0);
        isExsits.wepTxKeyIndex = 0;
        this.mWifiManager.updateNetwork(isExsits);
        this.mWifiManager.removeNetwork(isExsits.networkId);
        return true;
    }

    public void disconnectWifi(int i) {
        this.mWifiManager.disableNetwork(i);
        this.mWifiManager.disconnect();
    }

    public String getBSSID() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        return connectionInfo == null ? "NULL" : connectionInfo.getBSSID();
    }

    public List<WifiConfiguration> getConfiguration() {
        return this.mWifiConfiguration;
    }

    public int getIPAddress() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        return connectionInfo == null ? 0 : connectionInfo.getIpAddress();
    }

    public List<WifiConfiguration> getLinkList() {
        return this.mWifiManager.getConfiguredNetworks();
    }

    public String getMacAddress() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        return connectionInfo == null ? "NULL" : connectionInfo.getMacAddress();
    }

    public int getNetworkId() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        return connectionInfo == null ? 0 : connectionInfo.getNetworkId();
    }

    public String getSSID() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        return connectionInfo == null ? "NULL" : connectionInfo.getSSID();
    }

    public WifiConfiguration getWifiConfig(String str) {
        for (WifiConfiguration wifiConfiguration : this.mWifiManager.getConfiguredNetworks()) {
            if (wifiConfiguration.SSID.equals(InternalZipConstants.ZIP_FILE_SEPARATOR + str + InternalZipConstants.ZIP_FILE_SEPARATOR)) {
                return wifiConfiguration;
            }
        }
        return null;
    }

    public String getWifiInfo() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        return connectionInfo == null ? "NULL" : connectionInfo.toString();
    }

    public List<ScanResult> getWifiList() {
        return this.mWifiList;
    }

    public WifiManager getWifiManager() {
        return this.mWifiManager;
    }

    public WifiConfiguration isExsits(String str) {
        List<WifiConfiguration> configuredNetworks = this.mWifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                if (wifiConfiguration.SSID.equals("\"" + str + "\"")) {
                    return wifiConfiguration;
                }
            }
        }
        return null;
    }

    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.mWifiList.size(); i++) {
            stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
            stringBuilder.append(((ScanResult) this.mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    public void openWifi() {
        if (!this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(true);
        }
    }

    public void releaseWifiLock() {
        if (this.mWifiLock.isHeld()) {
            this.mWifiLock.acquire();
        }
    }

    public void startScan() {
        this.mWifiManager.startScan();
        this.mWifiList = this.mWifiManager.getScanResults();
        this.mWifiConfiguration = this.mWifiManager.getConfiguredNetworks();
    }
}
