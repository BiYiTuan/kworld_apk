package com.memo.cable;

import org.cybergarage.upnp.Device;

public interface IController {
    String getCurrentURI(Device device);

    int getMaxVolumeValue(Device device);

    String getMediaDuration(Device device);

    MediaInfo getMediaInfo(Device device);

    int getMinVolumeValue(Device device);

    String getMute(Device device);

    String getPositionInfo(Device device);

    String getTransportState(Device device);

    int getVoice(Device device);

    boolean goon(Device device, String str);

    boolean pause(Device device);

    boolean play(Device device, String str, String str2);

    boolean seek(Device device, String str);

    boolean setMute(Device device, String str);

    boolean setVoice(Device device, int i);

    boolean setWifi(Device device, String str);

    boolean stop(Device device, boolean z);
}
