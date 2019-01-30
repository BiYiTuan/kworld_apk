package com.gemini.custom;

import com.gemini.play.MGplayer;
import java.util.Calendar;

public class home {

    /* renamed from: com.gemini.custom.home$1 */
    static class C02011 extends Thread {
        C02011() {
        }

        public void run() {
            if (MGplayer.udpIP != null && MGplayer.udpIP.length() >= 7 && MGplayer.udpPort != null && MGplayer.isNumeric(MGplayer.udpPort)) {
                home.home_send();
                MGplayer.sleep(10000);
                home.home_send();
            }
        }
    }

    public static void start() {
    }

    public static void Thread_home_Init() {
        if (MGplayer.custom().equals("52home")) {
            new C02011().start();
        }
    }

    public static void home_send() {
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
}
