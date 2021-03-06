package io.vov.vitamio.utils;

import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.Closeable;

public class IOUtils {
    private static final String TAG = "IOUtils";

    public static void closeSilently(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable t) {
                Log.w(TAG, "fail to close", t);
            }
        }
    }

    public static void closeSilently(ParcelFileDescriptor c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable t) {
                Log.w(TAG, "fail to close", t);
            }
        }
    }

    public static void closeSilently(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable t) {
                Log.w(TAG, "fail to close", t);
            }
        }
    }
}
