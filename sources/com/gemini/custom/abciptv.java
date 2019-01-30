package com.gemini.custom;

import com.gemini.play.MGplayer;

public class abciptv {
    public static void start() {
        if (MGplayer.custom().equals("abciptv")) {
            MGplayer.start_tvbus = 1;
        }
    }
}
