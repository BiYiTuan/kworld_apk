package com.memo.connection;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import com.memo.ManifestUtils;
import java.lang.reflect.InvocationTargetException;

public class WifiHotspotManager extends MemoWifiManager {
    private static WifiHotspotManager sInstance = null;
    private OnHotspotStateChangedListener mOnHotspotStateChangedListener;
    private WifiConfiguration oldConfig;

    public interface OnHotspotStateChangedListener {
        void onWifiApStateChanged(int i);
    }

    private WifiHotspotManager(Context context) {
        super(context);
    }

    public static WifiHotspotManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WifiHotspotManager(context);
        }
        return sInstance;
    }

    public static String getSoftAPName(Context context) {
        return "Tubicast_" + ManifestUtils.getAnAndroidIDdroidID(context);
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

    public int getWifiApState() {
        int i = WIFI_AP_STATE_FAILED;
        try {
            return ((Integer) this.mWifiManager.getClass().getMethod("getWifiApState", new Class[0]).invoke(this.mWifiManager, new Object[0])).intValue();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return i;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return i;
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            return i;
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            return i;
        }
    }

    public void setHotspotStateChangedListener(OnHotspotStateChangedListener onHotspotStateChangedListener) {
        this.mOnHotspotStateChangedListener = onHotspotStateChangedListener;
    }

    public void setSoftapEnabled(WifiConfiguration wifiConfiguration) {
        this.oldConfig = getAPConfiguration();
        saveDataPreference();
        setWifiEnabled(false);
        MobileDataManager.setMobileDataEnabled(this.mContext, false);
        if (wifiConfiguration == null) {
            wifiConfiguration = MemoWifiManager.generateWifiSoftAPConfig(getSoftAPName(this.mContext));
        }
        if (wifiConfiguration != null) {
            setWifiApEnabled(wifiConfiguration, true);
        }
        setFrequencyBand(1, false);
    }

    protected void updateWifiAPState(int i) {
        if (this.mOnHotspotStateChangedListener != null) {
            this.mOnHotspotStateChangedListener.onWifiApStateChanged(i);
        }
    }
}
