package com.gemini.custom;

import com.gemini.play.MGplayer;

public class boliwu {
    public static void start() {
        if (MGplayer.custom().equals("boliwu")) {
            MGplayer.start_tvbus = 1;
        }
    }
}
