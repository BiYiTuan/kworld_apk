package org.videolan.vlc.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.util.VLCUtil;

public class VLCInstance {
    public static final String TAG = "VLC/UiTools/VLCInstance";
    private static LibVLC sLibVLC = null;

    public static synchronized LibVLC get(Context getApplicationContext) throws IllegalStateException {
        LibVLC libVLC;
        synchronized (VLCInstance.class) {
            if (sLibVLC == null) {
                if (!VLCUtil.hasCompatibleCPU(getApplicationContext)) {
                    Log.e(TAG, VLCUtil.getErrorMsg());
                }
                sLibVLC = new LibVLC(getApplicationContext, VLCOptions.getLibOptions(getApplicationContext));
            }
            libVLC = sLibVLC;
        }
        return libVLC;
    }

    public static synchronized void restart(Context getApplicationContext) throws IllegalStateException {
        synchronized (VLCInstance.class) {
            if (sLibVLC != null) {
                sLibVLC.release();
                sLibVLC = new LibVLC(getApplicationContext, VLCOptions.getLibOptions(getApplicationContext));
            }
        }
    }

    public static synchronized boolean testCompatibleCPU(Context context) {
        boolean z;
        synchronized (VLCInstance.class) {
            if (sLibVLC != null || VLCUtil.hasCompatibleCPU(context)) {
                z = true;
            } else {
                z = context instanceof Activity ? false : false;
            }
        }
        return z;
    }
}
