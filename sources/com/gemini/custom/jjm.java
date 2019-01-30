package com.gemini.custom;

import com.gemini.play.MGplayer;

public class jjm {
    public static String auth_url = "https://auth.tvgood.taipei/v1/auth";

    public static void send_checkip() {
        if (MGplayer.custom().equals("jingjimu") || MGplayer.custom().equals("jingjimudev") || MGplayer.custom().equals("tvgo") || MGplayer.custom().equals("tvgo2")) {
            MGplayer.start_tvbus = 1;
            MGplayer.start_tvbus_vod = 1;
        }
    }
}
