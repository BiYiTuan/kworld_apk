package com.memo;

import android.content.Context;
import android.provider.Settings.Secure;

public class ManifestUtils {
    public static String getAnAndroidIDdroidID(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public static int getVersionCode(Context context) {
        int i = 0;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }
}
