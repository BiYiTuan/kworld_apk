package io.vov.vitamio.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import net.lingala.zip4j.util.InternalZipConstants;

public class ContextUtils {
    public static int getVersionCode(Context ctx) {
        int version = 0;
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getApplicationInfo().packageName, 0).versionCode;
        } catch (Throwable e) {
            Log.m32e("getVersionInt", e);
            return version;
        }
    }

    public static String getDataDir(Context ctx) {
        ApplicationInfo ai = ctx.getApplicationInfo();
        if (ai.dataDir != null) {
            return fixLastSlash(ai.dataDir);
        }
        return "/data/data/" + ai.packageName + InternalZipConstants.ZIP_FILE_SEPARATOR;
    }

    public static String fixLastSlash(String str) {
        String res = str == null ? InternalZipConstants.ZIP_FILE_SEPARATOR : str.trim() + InternalZipConstants.ZIP_FILE_SEPARATOR;
        if (res.length() <= 2 || res.charAt(res.length() - 2) != '/') {
            return res;
        }
        return res.substring(0, res.length() - 1);
    }
}
