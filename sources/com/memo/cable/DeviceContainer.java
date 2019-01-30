package com.memo.cable;

import android.text.TextUtils;
import com.memo.TestXlog;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.upnp.Device;

public class DeviceContainer {
    private static final String TAG = "DeviceContainer";
    private static final DeviceContainer mDeviceContainer = new DeviceContainer();
    private List<DeviceChangeListener> mDeviceChangeListenerList = new ArrayList();
    private List<Device> mDevices = new ArrayList();
    private Device mSelectedDevice;

    public interface DeviceChangeListener {
        void onDeviceAdd(Device device);

        void onDeviceRemove(Device device);
    }

    private DeviceContainer() {
    }

    public static DeviceContainer getInstance() {
        return mDeviceContainer;
    }

    public synchronized void addDevice(Device device) {
        if (DeviceUtil.isMediaRenderDevice(device)) {
            if (device != null) {
                TestXlog.m27d("addDevice = " + device.getFriendlyName());
            }
            TestXlog.m29i("####addDevice Chipid is " + device.getChipId() + "######");
            if (ChipidChecker.getInstace().checkChipid(device.getChipId()) != -1) {
                int size = this.mDevices.size();
                int i = 0;
                while (i < size) {
                    if (!device.getUDN().equalsIgnoreCase(((Device) this.mDevices.get(i)).getUDN())) {
                        i++;
                    } else if (!TextUtils.equals(((Device) this.mDevices.get(i)).getFriendlyName(), device.getFriendlyName())) {
                        this.mDevices.remove(i);
                        if (this.mDevices.isEmpty()) {
                            setSelectedDevice(device);
                        }
                        this.mDevices.add(device);
                        for (DeviceChangeListener onDeviceAdd : this.mDeviceChangeListenerList) {
                            onDeviceAdd.onDeviceAdd(device);
                        }
                    }
                }
                if (this.mDevices.isEmpty()) {
                    setSelectedDevice(device);
                }
                this.mDevices.add(device);
                while (r1.hasNext()) {
                    onDeviceAdd.onDeviceAdd(device);
                }
            }
        }
    }

    public synchronized void clear() {
        if (this.mDevices != null) {
            this.mDevices.clear();
            this.mSelectedDevice = null;
        }
    }

    public List<Device> getDevices() {
        return this.mDevices;
    }

    public Device getSelectedDevice() {
        return this.mSelectedDevice;
    }

    public void registDeviceChangeListener(DeviceChangeListener deviceChangeListener) {
        if (!this.mDeviceChangeListenerList.contains(deviceChangeListener)) {
            this.mDeviceChangeListenerList.add(deviceChangeListener);
        }
    }

    public synchronized Device removeDevice(String str) {
        Device device;
        String str2 = "uuid:" + str;
        int size = this.mDevices.size();
        int i = 0;
        while (i < size) {
            if (str2.equalsIgnoreCase(((Device) this.mDevices.get(i)).getUDN())) {
                device = (Device) this.mDevices.remove(i);
                if (this.mSelectedDevice != null ? this.mSelectedDevice.getUDN().equalsIgnoreCase(device.getUDN()) : false) {
                    this.mSelectedDevice = null;
                }
                for (DeviceChangeListener onDeviceRemove : this.mDeviceChangeListenerList) {
                    onDeviceRemove.onDeviceRemove(device);
                }
            } else {
                i++;
            }
        }
        device = null;
        return device;
    }

    public synchronized void removeDevice(Device device) {
        if (DeviceUtil.isMediaRenderDevice(device)) {
            if (device != null) {
                TestXlog.m27d("removeDevice = " + device.getFriendlyName());
            }
            int size = this.mDevices.size();
            int i = 0;
            while (i < size) {
                if (device.getUDN().equalsIgnoreCase(((Device) this.mDevices.get(i)).getUDN())) {
                    if (this.mSelectedDevice != null ? this.mSelectedDevice.getUDN().equalsIgnoreCase(((Device) this.mDevices.remove(i)).getUDN()) : false) {
                        this.mSelectedDevice = null;
                    }
                    for (DeviceChangeListener onDeviceRemove : this.mDeviceChangeListenerList) {
                        onDeviceRemove.onDeviceRemove(device);
                    }
                } else {
                    i++;
                }
            }
        }
    }

    public void setSelectedDevice(Device device) {
        this.mSelectedDevice = device;
    }

    public void unRegistDeviceChangeListener(DeviceChangeListener deviceChangeListener) {
        if (this.mDeviceChangeListenerList.contains(deviceChangeListener)) {
            this.mDeviceChangeListenerList.remove(deviceChangeListener);
        }
    }
}
