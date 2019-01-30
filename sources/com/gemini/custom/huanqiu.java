package com.gemini.custom;

import com.gemini.play.MGplayer;

public class huanqiu {
    private static boolean ss = false;

    /* renamed from: com.gemini.custom.huanqiu$1 */
    static class C02021 extends Thread {
        C02021() {
        }

        public void run() {
            String addr = MGplayer.fu();
            MGplayer.MyPrintln("huanqiu addr = " + addr);
            String url = MGplayer.get_ip(addr);
            if (url != null) {
                int pu = huanqiu.rp(MGplayer.getSingInfo(), 65535);
                int pt = huanqiu.rp(String.valueOf(pu), 65535);
                MGplayer.fs(url, pu, pt, MGplayer.tv.GetMacXml());
                while (!huanqiu.ss) {
                    MGplayer.sleep(1800000);
                    MGplayer.MyPrintln("huanqiu s2");
                    MGplayer.fs(url, pu, pt, MGplayer.tv.GetMacXml());
                }
            }
        }
    }

    /* renamed from: com.gemini.custom.huanqiu$2 */
    static class C02032 extends Thread {
        C02032() {
        }

        public void run() {
            String url = MGplayer.get_ip(MGplayer.fu());
            if (url != null) {
                int pu = huanqiu.rp(MGplayer.getSingInfo(), 65535);
                MGplayer.fs(url, pu, huanqiu.rp(String.valueOf(pu), 65535), MGplayer.tv.GetMacXml());
            }
        }
    }

    public static void init() {
        if (MGplayer.custom().equals("huanqiu")) {
            MGplayer.start_tvbus = 1;
        }
    }

    public static void s1() {
        new C02021().start();
    }

    public static void ss() {
        new C02032().start();
    }

    public static void s2() {
        String url = MGplayer.get_ip(MGplayer.fu());
        if (url != null) {
            MGplayer.fe(url, MGplayer.tv.GetMacXml());
            ss = true;
        }
    }

    private static int rp(String key, int prime) {
        int hash = key.length();
        for (int i = 0; i < key.length(); i++) {
            hash = ((hash << 4) ^ (hash >> 28)) ^ key.charAt(i);
        }
        return hash % prime;
    }
}
