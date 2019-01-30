package com.memo.sdk;

import org.cybergarage.upnp.Device;

public interface IMemoDeviceListener {
    void onDeviceAdd(Device device);

    void onDeviceCheated(String str, String str2);

    void onDeviceRemove(Device device);
}
