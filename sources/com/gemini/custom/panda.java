package com.gemini.custom;

import com.gemini.play.MGplayer;

public class panda {
    public static void start() {
        if (MGplayer.custom().equals("pandaiptv") || MGplayer.custom().equals("meuiptv") || MGplayer.custom().equals("alphaiptv")) {
            MGplayer.start_tvbus = 1;
        }
    }
}
