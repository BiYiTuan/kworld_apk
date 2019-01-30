package io.vov.vitamio.utils;

import java.util.MissingFormatArgumentException;

public class Log {
    public static final String TAG = "Vitamio[Player]";

    /* renamed from: i */
    public static void m34i(String msg, Object... args) {
    }

    /* renamed from: d */
    public static void m31d(String msg, Object... args) {
    }

    /* renamed from: e */
    public static void m33e(String msg, Object... args) {
        try {
            android.util.Log.e(TAG, String.format(msg, args));
        } catch (MissingFormatArgumentException e) {
            android.util.Log.e(TAG, "vitamio.Log", e);
            android.util.Log.e(TAG, msg);
        }
    }

    /* renamed from: e */
    public static void m32e(String msg, Throwable t) {
        android.util.Log.e(TAG, msg, t);
    }
}
