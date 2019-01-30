package com.memo;

import com.memo.sdk.IMemoDeviceListener;
import com.memo.sdk.MemoTVCastSDK;

public class EventDeviceCheat {
    public String deviceChipId;
    public String deviceName;

    public EventDeviceCheat(String str, String str2) {
        this.deviceName = str;
        this.deviceChipId = str2;
    }

    public void throwOut() {
        for (IMemoDeviceListener onDeviceCheated : MemoTVCastSDK.getDeviceListeners()) {
            onDeviceCheated.onDeviceCheated(this.deviceName, this.deviceChipId);
        }
    }
}
