package com.memo.cable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.memo.EventDeviceCheat;
import com.memo.ManifestUtils;
import com.memo.sdk.MemoTVCastSDK;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.cybergarage.upnp.Device;
import org.json.JSONException;
import org.json.JSONObject;

public class ChipidChecker {
    public static final int CHIPID_FAILD = -1;
    public static final int CHIPID_NOT_FOUND = 0;
    public static final int CHIPID_SUCCESS = 1;
    private static ChipidChecker instance = null;
    public Map<String, Boolean> mChipidTickets = new HashMap(1);
    private HashMap<String, EventDeviceCheat> mDeviceCheatList = new HashMap();

    private ChipidChecker() {
    }

    private void checkNewChipid(final String str) {
        if (TextUtils.equals(str, "12345678") || TextUtils.equals(str, "0000000000000000")) {
            this.mChipidTickets.put(str, Boolean.valueOf(true));
            return;
        }
        Context instance = MemoTVCastSDK.getInstance();
        final String format = String.format("http://api.memohi.com/tubicast_chipidcheck?chipid=%s&p3=%s&vercode=%s&channel_id=%s&b2=%s", new Object[]{str, instance.getPackageName(), Integer.valueOf(ManifestUtils.getVersionCode(instance)), "999", ManifestUtils.getAnAndroidIDdroidID(instance)});
        new Thread(new Runnable() {
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(format).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        try {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = inputStream.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                byteArrayOutputStream.write(bArr, 0, read);
                            }
                            inputStream.close();
                            try {
                                if (TextUtils.equals("1", new JSONObject(new String(byteArrayOutputStream.toByteArray(), "utf-8")).optString("code"))) {
                                    ChipidChecker.this.mChipidTickets.put(str, Boolean.valueOf(true));
                                    return;
                                }
                                ChipidChecker.this.mChipidTickets.put(str, Boolean.valueOf(false));
                                Device removeDevice = DeviceContainer.getInstance().removeDevice(str);
                                if (removeDevice != null && !ChipidChecker.this.mDeviceCheatList.containsKey(str)) {
                                    final EventDeviceCheat eventDeviceCheat = new EventDeviceCheat(removeDevice.getFriendlyName(), str);
                                    ChipidChecker.this.mDeviceCheatList.put(str, eventDeviceCheat);
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        public void run() {
                                            eventDeviceCheat.throwOut();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }).start();
    }

    public static ChipidChecker getInstace() {
        if (instance == null) {
            instance = new ChipidChecker();
        }
        return instance;
    }

    public int checkChipid(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        if (this.mChipidTickets.containsKey(str)) {
            return ((Boolean) this.mChipidTickets.get(str)).booleanValue() ? 1 : -1;
        } else {
            checkNewChipid(str);
            return 0;
        }
    }

    public List<EventDeviceCheat> getCheatDevices() {
        List arrayList = new ArrayList();
        for (Entry value : this.mDeviceCheatList.entrySet()) {
            arrayList.add((EventDeviceCheat) value.getValue());
        }
        return arrayList;
    }
}
