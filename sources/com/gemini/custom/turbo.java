package com.gemini.custom;

import com.gemini.play.MGplayer;

public class turbo {
    public static int isvod = 0;

    public static void start() {
        if (MGplayer.custom().equals("turbotv")) {
            MGplayer.start_tvbus = 1;
        }
    }
}
