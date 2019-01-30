package com.gemini.custom;

import com.gemini.play.MGplayer;
import java.util.Calendar;

public class jufeng {
    public static void jufeng_start() {
        if (MGplayer.custom().equals("jufeng") || MGplayer.custom().equals("jufengv2") || MGplayer.custom().equals("jufengv3") || MGplayer.custom().equals("ksiptv")) {
            MGplayer.start_tvbus = 1;
        }
    }

    public static void Thread_jufeng_Init() {
        if (MGplayer.custom().equals("jufeng") || MGplayer.custom().equals("jufengv2") || MGplayer.custom().equals("jufengv3") || MGplayer.custom().equals("ksiptv")) {
            jufeng_send();
        }
    }

    public static void jufeng_send() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        int second = calendar.get(13);
        new UdpClient(MGplayer.tv.GetMac() + "|" + MGplayer.tv.getCpuID() + "|" + (year + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(month)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(day)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(hour)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(minute)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(second)}))).send(MGplayer.udpIP, Integer.parseInt(MGplayer.udpPort));
    }
}
