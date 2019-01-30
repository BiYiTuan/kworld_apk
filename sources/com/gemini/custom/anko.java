package com.gemini.custom;

import com.gemini.play.MGplayer;

public class anko {
    public static void start() {
        if (MGplayer.custom().equals("anko")) {
            MGplayer.set_token = 1;
        }
    }
}
