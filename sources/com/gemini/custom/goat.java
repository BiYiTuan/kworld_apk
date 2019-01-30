package com.gemini.custom;

import com.gemini.play.MGplayer;

public class goat {
    public static void start() {
        if (MGplayer.custom().equals("goat")) {
            MGplayer.start_tvbus = 1;
            MGplayer.start_tvbus_vod = 1;
        }
    }
}
