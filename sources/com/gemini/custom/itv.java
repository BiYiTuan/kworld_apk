package com.gemini.custom;

import com.gemini.play.MGplayer;

public class itv {
    public static String get_rtmp_url(String url, String key) {
        String time = String.valueOf(MGplayer.seconds / 1000) + 180;
        return url + "?st=" + MGplayer.MD5(key + time).toLowerCase() + "&e=" + time;
    }
}
