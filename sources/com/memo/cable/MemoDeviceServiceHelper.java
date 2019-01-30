package com.memo.cable;

import android.content.Intent;
import android.util.Log;
import com.memo.sdk.MemoTVCastSDK;
import java.util.List;
import org.cybergarage.upnp.Device;

public class MemoDeviceServiceHelper {
    private static MemoDeviceServiceHelper instance;
    private List<Device> mDevices;

    private MemoDeviceServiceHelper() {
        startMemoDeviceService();
    }

    public static MemoDeviceServiceHelper getInstance() {
        if (instance == null) {
            instance = new MemoDeviceServiceHelper();
        }
        return instance;
    }

    private void startMemoDeviceService() {
        if (MemoTVCastSDK.getInstance() != null) {
            MemoTVCastSDK.getInstance().startService(new Intent(MemoTVCastSDK.getInstance(), MemoDeviceService.class));
        }
    }

    public List<Device> getDevices() {
        return DeviceContainer.getInstance().getDevices();
    }

    public Device getSelectedDevice() {
        return DeviceContainer.getInstance().getSelectedDevice();
    }

    public void selectDevice(int i) {
        DeviceContainer.getInstance().setSelectedDevice((Device) getInstance().getDevices().get(i));
    }

    public void startFindDevice() {
        if (MemoTVCastSDK.getInstance() == null) {
            Log.i("NOTFOUND", "MemoTVCastSDK.getInstance()==null");
            return;
        }
        MemoTVCastSDK.getInstance().startService(new Intent(MemoTVCastSDK.getInstance(), MemoDeviceService.class));
    }

    public void stopMemoDeviceService() {
        if (MemoTVCastSDK.getInstance() != null) {
            MemoTVCastSDK.getInstance().stopService(new Intent(MemoTVCastSDK.getInstance(), MemoDeviceService.class));
        }
    }
}
