package org.videolan.vlc.util;

import android.util.Log;

public class LogUtils {
    private static final boolean debug = true;

    /* renamed from: i */
    public static void m402i(String tag, String msg) {
        Log.i(tag, msg);
    }

    /* renamed from: i */
    public static void m401i(String msg) {
        Log.i("vlc", msg);
    }
}
