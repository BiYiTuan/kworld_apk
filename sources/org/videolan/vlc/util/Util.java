package org.videolan.vlc.util;

import android.support.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;

public class Util {
    public static final String TAG = "VLC/Util";

    public static boolean close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return true;
            } catch (IOException e) {
            }
        }
        return false;
    }

    public static boolean isListEmpty(@Nullable Collection collection) {
        return collection == null || collection.isEmpty();
    }
}
