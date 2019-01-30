package com.gemini.play;

import android.content.res.AssetManager;

class gcustom {
    public native void cc();

    public native void cd();

    public native int ci();

    public native void cm(String str, String str2);

    public native void cu();

    public native String custom();

    public native int debug();

    public native int ds(String str, String str2);

    public native String epg();

    public native boolean existVideoId(int i);

    public native String ff0();

    public native String ff1();

    public native String ff2();

    public native void forcetvcache(int i);

    public native String getIntroductionID(int i);

    public native int getVideoCurrentiid();

    public native String getVideoCurrentiimage();

    public native String getVideoCurrentttile();

    public native String getVideoImage(int i);

    public native String getVideoIntroduction(int i);

    public native String getVideoIntroductions(int i, int i2);

    public native String getVideoName(int i);

    public native String getVideoNameForward(int i);

    public native String getVideoNameNext(int i);

    public native int getVideoNum(int i);

    public native String getVideoPassword(int i);

    public native String getVideoSource(int i);

    public native String getVideoUrl(int i);

    public native String getg();

    public native String getinfo2index(String str, String str2);

    public native void gl();

    public native String glocal();

    public native String gm();

    public native int gnb0();

    public native int gnb1();

    public native int gnb2();

    public native String gscheck(String str);

    public native String gu();

    public native int hlsplugin();

    public native int idGet(int i);

    public native String imageGet(int i);

    public native String introductionGet(int i);

    public native String j1(String str);

    public native String j1no(String str);

    public native String j2(String str);

    public native String j2no(String str);

    public native String j3(String str, String str2);

    public native String ju(String str);

    public native String key(String str);

    public native String nameGet(int i);

    public native String passwordGet(int i);

    public native int playlistkey(String str);

    public native int port();

    public native void qu();

    public native String s0(String str, String str2, String str3, int i);

    public native String s1(String str, String str2, String str3, String str4, int i);

    public native String s2(String str);

    public native String s3(String str);

    public native String s4(String str);

    public native String s5();

    public native void selectlan(int i);

    public native void setVideoIntroduction(int i, String str);

    public native void setVideoIntroductions(int i, int i2, String str);

    public native String sourceGet(int i);

    public native void ssb();

    public native void sstringfromjni();

    public native String string3();

    /* renamed from: t */
    public native void m15t();

    public native String t3(AssetManager assetManager, String str, String str2);

    public native int trytimes();

    public native String typeGet(int i);

    /* renamed from: u */
    public native String m16u();

    public native void urlClear();

    public native String urlGet(int i);

    public native void urlPush(int i, String str);

    public native boolean urlPush1(int i, String str, String str2, String str3);

    public native boolean urlPush2(int i, String str, String str2, String str3, String str4, String str5, String str6);

    public native boolean urlPush3(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7);

    public native boolean urlPush4(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8);

    public native int urlSize();

    gcustom() {
    }

    static {
        System.loadLibrary("custom3");
    }
}
