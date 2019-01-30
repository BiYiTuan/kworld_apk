package org.videolan.libvlc.util;

import android.net.Uri;
import android.os.Build.VERSION;
import java.io.File;

public class AndroidUtil {
    public static boolean isFroyoOrLater() {
        return VERSION.SDK_INT >= 8;
    }

    public static boolean isGingerbreadOrLater() {
        return VERSION.SDK_INT >= 9;
    }

    public static boolean isHoneycombOrLater() {
        return VERSION.SDK_INT >= 11;
    }

    public static boolean isHoneycombMr1OrLater() {
        return VERSION.SDK_INT >= 12;
    }

    public static boolean isHoneycombMr2OrLater() {
        return VERSION.SDK_INT >= 13;
    }

    public static boolean isICSOrLater() {
        return VERSION.SDK_INT >= 14;
    }

    public static boolean isJellyBeanOrLater() {
        return VERSION.SDK_INT >= 16;
    }

    public static boolean isJellyBeanMR1OrLater() {
        return VERSION.SDK_INT >= 17;
    }

    public static boolean isJellyBeanMR2OrLater() {
        return VERSION.SDK_INT >= 18;
    }

    public static boolean isKitKatOrLater() {
        return VERSION.SDK_INT >= 19;
    }

    public static boolean isLolliPopOrLater() {
        return VERSION.SDK_INT >= 21;
    }

    public static boolean isMarshMallowOrLater() {
        return VERSION.SDK_INT >= 23;
    }

    public static File UriToFile(Uri uri) {
        return new File(uri.getPath().replaceFirst("file://", ""));
    }

    public static Uri PathToUri(String path) {
        return Uri.fromFile(new File(path));
    }

    public static Uri LocationToUri(String location) {
        Uri uri = Uri.parse(location);
        if (uri.getScheme() != null) {
            return uri;
        }
        throw new IllegalArgumentException("location has no scheme");
    }

    public static Uri FileToUri(File file) {
        return Uri.fromFile(file);
    }
}
