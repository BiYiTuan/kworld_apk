package com.memo.sdk;

import android.text.TextUtils;
import com.memo.cable.IController;
import com.memo.cable.MediaInfo;
import org.cybergarage.upnp.C0777a;
import org.cybergarage.upnp.C0790e;
import org.cybergarage.upnp.Device;

public class MemoTVCastController implements IController {
    private static final String AVTransport1 = "urn:schemas-upnp-org:service:AVTransport:1";
    private static final String Play = "Play";
    private static final String RenderingControl = "urn:schemas-upnp-org:service:RenderingControl:1";
    private static final String SetAVTransportURI = "SetAVTransportURI";

    public String getCurrentURI(Device device) {
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return null;
        }
        C0777a d = service.m246d("GetMediaInfo");
        if (d == null) {
            return null;
        }
        d.m173a("InstanceID", "0");
        return d.m176a(device.encryption) ? d.m177b("CurrentURI") : null;
    }

    public int getMaxVolumeValue(Device device) {
        Object volumeDbRange = getVolumeDbRange(device, "MaxValue");
        return TextUtils.isEmpty(volumeDbRange) ? 100 : Integer.parseInt(volumeDbRange);
    }

    public String getMediaDuration(Device device) {
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return null;
        }
        C0777a d = service.m246d("GetMediaInfo");
        if (d == null) {
            return null;
        }
        d.m173a("InstanceID", "0");
        return d.m176a(device.encryption) ? d.m177b("MediaDuration") : null;
    }

    public MediaInfo getMediaInfo(Device device) {
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return null;
        }
        C0777a d = service.m246d("GetMediaInfo");
        if (d == null) {
            return null;
        }
        d.m173a("InstanceID", "0");
        if (!d.m176a(device.encryption)) {
            return null;
        }
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.duration = d.m177b("MediaDuration");
        mediaInfo.name = d.m177b("CurrentURIMetaData");
        return mediaInfo;
    }

    public int getMinVolumeValue(Device device) {
        Object volumeDbRange = getVolumeDbRange(device, "MinValue");
        return TextUtils.isEmpty(volumeDbRange) ? 0 : Integer.parseInt(volumeDbRange);
    }

    public String getMute(Device device) {
        C0790e service = device.getService(RenderingControl);
        if (service == null) {
            return null;
        }
        C0777a d = service.m246d("GetMute");
        if (d == null) {
            return null;
        }
        d.m173a("InstanceID", "0");
        d.m173a("Channel", "Master");
        d.m176a(device.encryption);
        return d.m177b("CurrentMute");
    }

    public String getPositionInfo(Device device) {
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return null;
        }
        C0777a d = service.m246d("GetPositionInfo");
        if (d == null) {
            return null;
        }
        d.m173a("InstanceID", "0");
        return d.m176a(device.encryption) ? d.m177b("AbsTime") : null;
    }

    public String getTransportState(Device device) {
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return null;
        }
        C0777a d = service.m246d("GetTransportInfo");
        if (d == null) {
            return null;
        }
        d.m173a("InstanceID", "0");
        return d.m176a(device.encryption) ? d.m177b("CurrentTransportState") : null;
    }

    public int getVoice(Device device) {
        if (device == null) {
            return -1;
        }
        C0790e service = device.getService(RenderingControl);
        if (service == null) {
            return -1;
        }
        C0777a d = service.m246d("GetVolume");
        if (d == null) {
            return -1;
        }
        d.m173a("InstanceID", "0");
        d.m173a("Channel", "Master");
        return d.m176a(device.encryption) ? d.m179c("CurrentVolume") : -1;
    }

    public String getVolumeDbRange(Device device, String str) {
        C0790e service = device.getService(RenderingControl);
        if (service == null) {
            return null;
        }
        C0777a d = service.m246d("GetVolumeDBRange");
        if (d == null) {
            return null;
        }
        d.m173a("InstanceID", "0");
        d.m173a("Channel", "Master");
        return d.m176a(device.encryption) ? d.m177b(str) : null;
    }

    public boolean goon(Device device, String str) {
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return false;
        }
        C0777a d = service.m246d("Seek");
        if (d == null) {
            return false;
        }
        d.m173a("InstanceID", "0");
        d.m173a("Unit", "ABS_TIME");
        d.m173a("Target", str);
        d.m176a(device.encryption);
        C0777a d2 = service.m246d(Play);
        if (d2 == null) {
            return false;
        }
        d2.m172a("InstanceID", 0);
        d2.m173a("Speed", "1");
        return d2.m176a(device.encryption);
    }

    public boolean pause(Device device) {
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return false;
        }
        C0777a d = service.m246d("Pause");
        if (d == null) {
            return false;
        }
        d.m172a("InstanceID", 0);
        return d.m176a(device.encryption);
    }

    public boolean play(Device device, String str, String str2) {
        if (device == null) {
            return false;
        }
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return false;
        }
        C0777a d = service.m246d(SetAVTransportURI);
        if (d == null) {
            return false;
        }
        C0777a d2 = service.m246d(Play);
        if (d2 == null || TextUtils.isEmpty(str)) {
            return false;
        }
        d.m172a("InstanceID", 0);
        d.m173a("CurrentURI", str);
        d.m173a("CurrentURIMetaData", str2);
        if (!d.m176a(device.encryption)) {
            return false;
        }
        d2.m172a("InstanceID", 0);
        d2.m173a("Speed", "1");
        return d2.m176a(device.encryption);
    }

    public boolean seek(Device device, String str) {
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return false;
        }
        C0777a d = service.m246d("Seek");
        if (d == null) {
            return false;
        }
        d.m173a("InstanceID", "0");
        d.m173a("Unit", "ABS_TIME");
        d.m173a("Target", str);
        boolean a = d.m176a(device.encryption);
        if (a) {
            return a;
        }
        d.m173a("Unit", "REL_TIME");
        d.m173a("Target", str);
        return d.m176a(device.encryption);
    }

    public boolean setMute(Device device, String str) {
        C0790e service = device.getService(RenderingControl);
        if (service == null) {
            return false;
        }
        C0777a d = service.m246d("SetMute");
        if (d == null) {
            return false;
        }
        d.m173a("InstanceID", "0");
        d.m173a("Channel", "Master");
        d.m173a("DesiredMute", str);
        return d.m176a(device.encryption);
    }

    public boolean setVoice(Device device, int i) {
        C0790e service = device.getService(RenderingControl);
        if (service == null) {
            return false;
        }
        C0777a d = service.m246d("SetVolume");
        if (d == null) {
            return false;
        }
        d.m173a("InstanceID", "0");
        d.m173a("Channel", "Master");
        d.m172a("DesiredVolume", i);
        return d.m176a(device.encryption);
    }

    public boolean setWifi(Device device, String str) {
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return false;
        }
        C0777a d = service.m246d("SetWifi");
        if (d == null) {
            return false;
        }
        d.m172a("InstanceID", 0);
        d.m173a("Parameters", str);
        return d.m176a(device.encryption);
    }

    public boolean stop(Device device, boolean z) {
        int i = 0;
        C0790e service = device.getService(AVTransport1);
        if (service == null) {
            return false;
        }
        C0777a d = service.m246d("Stop");
        if (d == null) {
            return false;
        }
        String str = "InstanceID";
        if (z) {
            i = 1;
        }
        d.m172a(str, i);
        return d.m176a(device.encryption);
    }
}
