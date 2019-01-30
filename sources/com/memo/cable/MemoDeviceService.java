package com.memo.cable;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import com.memo.TestXlog;
import com.memo.connection.ConnectInfoSender;
import com.memo.sdk.MemoTVCastSDK;
import org.cybergarage.upnp.C0961c;
import org.cybergarage.upnp.device.C0788j;
import org.cybergarage.upnp.ssdp.C0800f;

public class MemoDeviceService extends Service {
    private static final String TAG = "gggl";
    private C0961c mControlPoint;
    private SearchThread mSearchThread;
    private WifiManager mWifiManager;
    private WifiStateReceiver mWifiStateReceiver;

    private class WifiStateReceiver extends BroadcastReceiver {
        private WifiStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("wifi_state", 0);
                Log.d(MemoDeviceService.TAG, intent.getAction() + "   " + intExtra);
                switch (intExtra) {
                    case 1:
                        Log.e(MemoDeviceService.TAG, "wifi disabled");
                        return;
                    case 3:
                        TestXlog.m29i("###########Device Service WifiManager.WIFI_STATE_ENABLED #####");
                        MemoDeviceServiceHelper.getInstance().startFindDevice();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* renamed from: com.memo.cable.MemoDeviceService$1 */
    class C09201 implements C0788j {
        C09201() {
        }

        public void deviceSearchResponseReceived(C0800f c0800f) {
            c0800f.m335c();
        }
    }

    private void init() {
        TestXlog.i2("###########Device Service init #####");
        this.mControlPoint = new C0961c();
        MemoTVCastSDK.setControlPoint(this.mControlPoint);
        this.mSearchThread = new SearchThread(this.mControlPoint);
        this.mControlPoint.m524a(new C09201());
        MemoTVCastSDK.startDeviceApWork();
    }

    private void registerWifiStateReceiver() {
        if (this.mWifiStateReceiver == null) {
            this.mWifiStateReceiver = new WifiStateReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.STATE_CHANGE");
            intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(this.mWifiStateReceiver, new IntentFilter(intentFilter));
        }
    }

    private void startThread() {
        if (this.mSearchThread != null) {
            TestXlog.m29i("thread is not null");
            this.mSearchThread.setLongInternal(false);
        } else {
            TestXlog.m29i("thread is null, create a new thread");
            this.mSearchThread = new SearchThread(this.mControlPoint);
        }
        if (this.mSearchThread.isAlive()) {
            TestXlog.m29i("thread is alive");
            this.mSearchThread.awake();
        } else {
            TestXlog.m29i("start the thread");
            this.mSearchThread.start();
        }
        TestXlog.m29i("###########Device Service startThread #####");
    }

    private void stopThread() {
        if (this.mSearchThread != null) {
            this.mSearchThread.stopThread();
            this.mControlPoint.m551l();
            this.mSearchThread = null;
            this.mControlPoint = null;
            Log.w(TAG, "stop Device service");
            TestXlog.m29i("###########Device Service stop Device service#####");
        }
    }

    private void unInit() {
        Log.d(TAG, "unInit");
        TestXlog.i2("###########Device Service unInit #####");
        stopThread();
        unregisterWifiStateReceiver();
        ConnectInfoSender.getInstance().exit();
    }

    private void unregisterWifiStateReceiver() {
        if (this.mWifiStateReceiver != null) {
            unregisterReceiver(this.mWifiStateReceiver);
            this.mWifiStateReceiver = null;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        MemoTVCastSDK.init(this);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        init();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        unInit();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(TAG, "onStartCommand");
        if (intent == null) {
            stopSelf();
            return 0;
        }
        registerWifiStateReceiver();
        Log.d(TAG, "onStartCommand start thread");
        this.mSearchThread.reInit();
        startThread();
        return super.onStartCommand(intent, i, i2);
    }
}
