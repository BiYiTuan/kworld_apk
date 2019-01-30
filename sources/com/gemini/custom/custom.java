package com.gemini.custom;

import android.content.Context;

public class custom {

    /* renamed from: com.gemini.custom.custom$1 */
    static class C02001 extends Thread {
        C02001() {
        }

        public void run() {
            jufeng.Thread_jufeng_Init();
            kvod.Thread_kvod_Init();
            home.Thread_home_Init();
            huaren.Thread_huaren_Init();
        }
    }

    public static void start() {
        new C02001().start();
    }

    public static void jjm_send() {
        jjm.send_checkip();
    }

    public static void huanqiu_start() {
        huanqiu.init();
    }

    public static void chuangshi_start() {
        chuangshi.Thread_Chuangshi_Init();
    }

    public static void chaoyang_start() {
        chaoyang.http_send();
    }

    public static void newlive_start() {
        newlive.http_send();
    }

    public static void spain1_start() {
        spain1.start();
    }

    public static void szysx_start(Context _this) {
        szysx.start(_this);
    }

    public static void lookiptv_start() {
        lookiptv.lookiptv_start();
    }

    public static void panda_start() {
        panda.start();
    }

    public static void goat_start() {
        goat.start();
    }

    public static void boliwu_start() {
        boliwu.start();
    }

    public static void turbo_start() {
        turbo.start();
    }

    public static void jufeng_start() {
        jufeng.jufeng_start();
    }

    public static void doudouzi_start() {
        doudouzi.doudouzi_start();
    }

    public static void aikanvip_start() {
        aikanvip.start();
    }

    public static void msiptv_start() {
        msiptv.msiptv_start();
    }

    public static void abciptv_start() {
        abciptv.start();
    }

    public static void dongsentv_start() {
        dongsentv.start();
    }

    public static void anko_start() {
        anko.start();
    }
}
