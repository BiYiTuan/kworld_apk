package com.gemini.custom;

import com.gemini.play.MGplayer;

public class dongsentv {
    public static void start() {
        if (MGplayer.custom().equals("dongsentv")) {
            MGplayer.start_tvbus = 1;
        }
    }
}
