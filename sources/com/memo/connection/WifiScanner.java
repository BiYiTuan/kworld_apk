package com.memo.connection;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.memo.OnScanResultListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WifiScanner extends MemoWifiManager {
    private static final int WIFI_RESCAN_INTERVAL_MS = 2000;
    private static String apPrefix;
    private List<AccessPoint> mAccessPoints = Collections.synchronizedList(new ArrayList());
    private OnScanResultListener mOnScanResultListener;
    private Scanner mScanner = new Scanner();

    private class Multimap<K, V> {
        private HashMap<K, List<V>> store;

        private Multimap() {
            this.store = new HashMap();
        }

        List<V> getAll(K k) {
            List<V> list = (List) this.store.get(k);
            return list != null ? list : Collections.emptyList();
        }

        void put(K k, V v) {
            List list = (List) this.store.get(k);
            if (list == null) {
                list = new ArrayList(3);
                this.store.put(k, list);
            }
            list.add(v);
        }
    }

    private class Scanner extends Handler {
        private int mRetry;

        private Scanner() {
            this.mRetry = 0;
        }

        void forceScan() {
            removeMessages(0);
            sendEmptyMessage(0);
        }

        public void handleMessage(Message message) {
            if (WifiScanner.this.mWifiManager.startScan()) {
                this.mRetry = 0;
            } else {
                int i = this.mRetry + 1;
                this.mRetry = i;
                if (i >= 3) {
                    this.mRetry = 0;
                    if (WifiScanner.this.mOnScanResultListener != null) {
                        WifiScanner.this.mOnScanResultListener.onScanErrorOccurs();
                        return;
                    }
                    return;
                }
            }
            sendEmptyMessageDelayed(0, 2000);
        }

        void pause() {
            this.mRetry = 0;
            removeMessages(0);
        }

        void resume() {
            if (!hasMessages(0)) {
                sendEmptyMessage(0);
            }
        }
    }

    static {
        if (TextUtils.equals("tubicast", "azcast")) {
            apPrefix = "LOLLIPOP";
        } else {
            apPrefix = "tubicast";
        }
    }

    public WifiScanner(Context context) {
        super(context);
    }

    public synchronized List<AccessPoint> constructAccessPoints() {
        List arrayList;
        arrayList = new ArrayList();
        Multimap multimap = new Multimap();
        List<ScanResult> scanResults = this.mWifiManager.getScanResults();
        if (scanResults != null) {
            for (ScanResult scanResult : scanResults) {
                if (!(TextUtils.isEmpty(scanResult.SSID) || scanResult.capabilities.contains("[IBSS]") || !scanResult.SSID.contains(apPrefix))) {
                    AccessPoint accessPoint;
                    Object obj = null;
                    for (AccessPoint accessPoint2 : multimap.getAll(scanResult.SSID)) {
                        obj = accessPoint2.update(scanResult) ? 1 : obj;
                    }
                    if (obj == null) {
                        accessPoint2 = new AccessPoint(scanResult);
                        arrayList.add(accessPoint2);
                        multimap.put(accessPoint2.ssid, accessPoint2);
                    }
                }
            }
        }
        return arrayList;
    }

    public void pause() {
        this.mScanner.pause();
    }

    public void resume() {
        this.mScanner.resume();
    }

    public void setOnScanResultListener(OnScanResultListener onScanResultListener) {
        this.mOnScanResultListener = onScanResultListener;
    }

    public void start() {
        this.mScanner.forceScan();
    }

    protected synchronized void updateAccessPoints() {
        super.updateAccessPoints();
        this.mAccessPoints.clear();
        this.mAccessPoints.addAll(constructAccessPoints());
        if (this.mOnScanResultListener != null) {
            this.mOnScanResultListener.onScanResultUpdateChanged(this.mAccessPoints);
        }
    }

    protected void updateWifiState(int i) {
        super.updateWifiState(i);
    }
}
