package com.gemini.custom;

import com.gemini.play.MGplayer;

public class msiptv {
    public static void msiptv_start() {
        if (MGplayer.custom().equals("msiptv")) {
            MGplayer.start_tvbus = 1;
        }
    }
}
