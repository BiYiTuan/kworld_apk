package com.gemini.play;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInfo {
    private String IMSI;
    private Context cxt;
    private TelephonyManager telephonyManager;

    public PhoneInfo(Context context) {
        this.cxt = context;
        this.telephonyManager = (TelephonyManager) context.getSystemService("phone");
    }

    public String getNativePhoneNumber() {
        return this.telephonyManager.getLine1Number();
    }

    public String getProvidersName() {
        String ProvidersName = "N/A";
        try {
            this.IMSI = this.telephonyManager.getSubscriberId();
            MGplayer.MyPrintln("IMSI:" + this.IMSI);
            if (this.IMSI == null) {
                return ProvidersName;
            }
            if (this.IMSI.startsWith("46000") || this.IMSI.startsWith("46002")) {
                ProvidersName = "chinamobile";
                return ProvidersName;
            }
            if (this.IMSI.startsWith("46001")) {
                ProvidersName = "chinaunicom";
            } else if (this.IMSI.startsWith("46003")) {
                ProvidersName = "chinatelecommunications";
            } else {
                ProvidersName = this.IMSI;
            }
            return ProvidersName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSubscriberId() {
        return this.telephonyManager.getLine1Number();
    }

    public String getDeviceId() {
        return this.telephonyManager.getDeviceId();
    }

    public String getPhoneInfo() {
        TelephonyManager tm = (TelephonyManager) this.cxt.getSystemService("phone");
        StringBuilder sb = new StringBuilder();
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
        return sb.toString();
    }
}
