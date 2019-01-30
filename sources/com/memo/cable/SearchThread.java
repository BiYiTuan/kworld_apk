package com.memo.cable;

import com.memo.TestXlog;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;
import net.lingala.zip4j.util.InternalZipConstants;
import org.cybergarage.upnp.C0961c;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.DeviceChangeListener;

public class SearchThread extends Thread {
    private static final String TAG = "SearchThread";
    private static final int mFastInternalTime = 2000;
    private static final int mNormalInternalTime = 13000;
    private static ConcurrentSkipListSet<String> sDestIpList = new ConcurrentSkipListSet();
    private boolean flag = true;
    private boolean isLongInternalTime = false;
    private C0961c mControlPoint;
    private DeviceChangeListener mDeviceChangeListener = new C09211();
    private boolean mStartComplete;

    /* renamed from: com.memo.cable.SearchThread$1 */
    class C09211 implements DeviceChangeListener {
        C09211() {
        }

        public void deviceAdded(Device device) {
            TestXlog.m29i("####Device control point add a device..." + device.getDeviceType() + device.getFriendlyName());
            String[] split = device.getSSDPPacket().m341i().split(InternalZipConstants.ZIP_FILE_SEPARATOR);
            if (split.length > 3) {
                SearchThread.addRootDeviceIp(split[2].split(":")[0]);
            }
            SearchThread.this.isLongInternalTime = true;
            DeviceContainer.getInstance().addDevice(device);
        }

        public void deviceRemoved(Device device) {
            TestXlog.m29i("####Device control point remove a device######");
            DeviceContainer.getInstance().removeDevice(device);
            if (DeviceContainer.getInstance().getDevices() != null && DeviceContainer.getInstance().getDevices().size() == 0) {
                SearchThread.this.isLongInternalTime = false;
            }
        }
    }

    public SearchThread(C0961c c0961c) {
        this.mControlPoint = c0961c;
        this.mControlPoint.m522a(this.mDeviceChangeListener);
    }

    public static void addRootDeviceIp(String str) {
        if (sDestIpList != null && !sDestIpList.contains(str)) {
            sDestIpList.add(str);
        }
    }

    private void searchDevices() {
        try {
            if (this.mStartComplete) {
                if (!sDestIpList.isEmpty()) {
                    Iterator it = sDestIpList.iterator();
                    while (it.hasNext()) {
                        this.mControlPoint.m537c((String) it.next());
                    }
                }
                this.mControlPoint.m547h();
                TestXlog.m29i("####device controlpoint search... ######");
            } else {
                this.mControlPoint.m551l();
                boolean k = this.mControlPoint.m550k();
                TestXlog.m29i("####Device controlpoint start:" + k + "######");
                if (k) {
                    this.mStartComplete = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        synchronized (this) {
            try {
                if (!this.mControlPoint.m542d().isEmpty()) {
                    this.isLongInternalTime = true;
                }
                if (this.isLongInternalTime) {
                    wait(13000);
                } else {
                    wait(2000);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void awake() {
        synchronized (this) {
            notifyAll();
        }
    }

    public void reInit() {
        this.mStartComplete = false;
        this.isLongInternalTime = false;
    }

    public void run() {
        while (this.flag && this.mControlPoint != null) {
            searchDevices();
        }
    }

    public void setLongInternal(boolean z) {
        this.isLongInternalTime = z;
    }

    public void stopThread() {
        this.flag = false;
        awake();
    }
}
