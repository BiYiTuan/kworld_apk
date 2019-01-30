package org.cybergarage.upnp.device;

import org.cybergarage.upnp.Device;

public interface DeviceChangeListener {
    void deviceAdded(Device device);

    void deviceRemoved(Device device);
}
