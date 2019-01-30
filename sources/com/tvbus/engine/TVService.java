package com.tvbus.engine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.gemini.play.MGplayer;

public class TVService extends Service {
    static final String TAG = "TVBusService";
    public static boolean bInited = false;

    private class TVServer implements Runnable {
        private static final String TAG = "TVBusServer";
        TVCore tvcore;

        private TVServer() {
            this.tvcore = TVCore.getInstance();
        }

        public void run() {
            if (MGplayer.custom().equals("jingjimu") || MGplayer.custom().equals("jingjimudev")) {
                this.tvcore.setPlayPort(8902);
                this.tvcore.setServPort(0);
            } else {
                this.tvcore.setPlayPort(18902);
                this.tvcore.setServPort(0);
            }
            this.tvcore.setRunningMode(1);
            int retv = this.tvcore.init(TVService.this.getApplicationContext());
            MGplayer.MyPrintln("TVService TVCore init result:" + retv);
            TVService.bInited = true;
            MGplayer.MyPrintln("TVService desc:" + this.tvcore.description());
            if (retv == 0) {
                retv = this.tvcore.run();
            }
            MGplayer.MyPrintln("TVService TVCore exited thread: " + retv);
            if (retv == -1) {
                MGplayer.StartTVBusOK = false;
            } else {
                MGplayer.StartTVBusOK = true;
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        MGplayer.MyPrintln("TVService onCreate() executed ++++++++++++!!!!!!!!!!!!!");
        Thread thread = new Thread(new TVServer());
        thread.setName("tvcore");
        thread.start();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        MGplayer.MyPrintln("TVService onStartCommand() executed");
        return 2;
    }

    public void onDestroy() {
        MGplayer.MyPrintln("TVService onDestroy() executed");
        TVCore.getInstance().quit();
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        MGplayer.MyPrintln("TVService onBind() executed");
        return null;
    }
}
