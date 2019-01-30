package com.gemini.application;

import android.app.Application;
import com.gemini.play.CrashHandler;

public class GeminiApplication extends Application {
    public static int memoryCacheSize;

    public void onCreate() {
        super.onCreate();
        memoryCacheSize = getMemoryCacheSize();
        CrashHandler.getInstance().init(getApplicationContext());
    }

    public int getMemoryCacheSize() {
        return ((int) Runtime.getRuntime().maxMemory()) / 4;
    }
}
