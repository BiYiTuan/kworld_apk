package io.vov.vitamio;

import android.annotation.SuppressLint;
import android.content.Context;
import io.vov.vitamio.utils.ContextUtils;
import net.lingala.zip4j.util.InternalZipConstants;

public class Vitamio {
    private static String browserlibraryPath;
    private static String vitamioDataPath;
    private static String vitamioLibraryPath;
    private static String vitamioPackage;

    @SuppressLint({"NewApi"})
    public static boolean isInitialized(Context ctx) {
        vitamioPackage = ctx.getPackageName();
        vitamioLibraryPath = ctx.getApplicationInfo().nativeLibraryDir + InternalZipConstants.ZIP_FILE_SEPARATOR;
        vitamioDataPath = ContextUtils.getDataDir(ctx) + "lib/";
        browserlibraryPath = ctx.getApplicationContext().getDir("libs", 0).getPath();
        return true;
    }

    public static String getVitamioPackage() {
        return vitamioPackage;
    }

    public static final String getLibraryPath() {
        return vitamioLibraryPath;
    }

    public static final String getDataPath() {
        return vitamioDataPath;
    }

    public static final String getBrowserLibraryPath() {
        return browserlibraryPath;
    }
}
