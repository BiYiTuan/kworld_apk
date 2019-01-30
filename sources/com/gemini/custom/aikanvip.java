package com.gemini.custom;

import com.gemini.play.MGplayer;

public class aikanvip {
    public static void start() {
        if (MGplayer.custom().equals("aikanvip")) {
            MGplayer.start_tvbus = 1;
        }
    }
}
