package com.gemini.custom;

import com.gemini.play.MGplayer;
import java.util.Calendar;
import java.util.Date;

public class kvod {
    public static void Thread_kvod_Init() {
        kvod_send();
    }

    public static void kvod_send() {
        if (MGplayer.custom().equals("kvod")) {
            Calendar calendar = Calendar.getInstance();
            if (MGplayer.seconds_prc > 0) {
                calendar.setTime(new Date(MGplayer.seconds_prc));
            }
            int year = calendar.get(1);
            int month = calendar.get(2) + 1;
            int day = calendar.get(5);
            int hour = calendar.get(11);
            int minute = calendar.get(12);
            int second = calendar.get(13);
            String cmd = "|" + MGplayer.tv.GetMac() + "|" + MGplayer.j1(MGplayer.tv.getCpuID() + "$" + MGplayer.tv.GetMac() + "$" + (year + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(month)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(day)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(hour)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(minute)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(second)})) + "$1") + "|1|";
            MGplayer.MyPrintln("####################" + MGplayer.udpIP + ":" + MGplayer.udpPort + " send check u:" + cmd + "####################");
            final UdpClient client = new UdpClient(cmd);
            new Thread() {
                public void run() {
                    client.send(MGplayer.udpIP, Integer.parseInt(MGplayer.udpPort));
                }
            }.start();
        }
    }
}
