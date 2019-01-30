package com.gemini.custom;

import com.gemini.play.MGplayer;

public class doudouzi {
    public static void doudouzi_start() {
        if (MGplayer.custom().equals("doudouzi")) {
            MGplayer.start_tvbus = 1;
        }
    }
}
