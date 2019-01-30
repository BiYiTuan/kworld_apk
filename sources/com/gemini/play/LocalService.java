package com.gemini.play;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocalService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());
        MGplayer.timeView();
        MGplayer.MyPrintln("Server onCreate");
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        MGplayer.MyPrintln("Server onStart");
    }

    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        MGplayer.MyPrintln("Server onDestroy");
    }
}
