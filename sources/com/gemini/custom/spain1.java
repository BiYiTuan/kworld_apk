package com.gemini.custom;

import android.os.Environment;
import android.os.Handler;
import com.gemini.play.LIVEplayer;
import com.gemini.play.MGplayer;
import java.io.File;
import java.util.Calendar;

public class spain1 {
    static Handler mHandler = new Handler();
    public static float rate = 1.0f;
    public static String sswwtv_url = "http://shishangtv.shishanggrupo.com/sswwtv.dat";

    /* renamed from: com.gemini.custom.spain1$1 */
    static class C02111 extends Thread {
        C02111() {
        }

        public void run() {
            if (MGplayer.udpIP != null && MGplayer.udpIP.length() >= 7 && MGplayer.udpPort != null && MGplayer.isNumeric(MGplayer.udpPort)) {
                spain1.spain1_send();
                MGplayer.sleep(10000);
                spain1.spain1_send();
            }
        }
    }

    public static void start() {
        if (MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spainglobal") || MGplayer.custom().equals("spain1cn")) {
            LIVEplayer.show_ps_playlist = true;
            MGplayer.MyPrintln("LIVEplayer.show_ps_playlist = " + LIVEplayer.show_ps_playlist);
        }
        if (MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spain1cn")) {
            MGplayer.start_tvbus = 1;
        }
    }

    public static void Thread_spain1_Init() {
        if (MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spainglobal") || MGplayer.custom().equals("spain1cn")) {
            new C02111().start();
        }
    }

    public static void spain1_send() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        int second = calendar.get(13);
        UdpClient client = new UdpClient(MGplayer.tv.GetMac() + "|" + MGplayer.tv.getCpuID() + "|" + (year + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(month)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(day)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(hour)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(minute)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(second)})));
        MGplayer.MyPrintln("MGplayer.udpIP" + MGplayer.udpIP + " MGplayer.udpPort=" + MGplayer.udpPort);
        client.send(MGplayer.udpIP, Integer.parseInt(MGplayer.udpPort));
    }

    public static void rm_mi_txt() {
        new File(Environment.getExternalStorageDirectory() + "/mi.txt").delete();
        new File(MGplayer._this.getFilesDir() + "/sswwtv.dat").delete();
    }
}
