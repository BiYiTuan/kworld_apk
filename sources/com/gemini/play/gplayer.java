package com.gemini.play;

/* compiled from: gcustom */
class gplayer {
    public native void gp2ppthreadnum(int i, int i2, int i3);

    public native void httpdinit();

    public native void httpdpath(String str);

    public native int mediaplayercache();

    public native void mediaplayerexit();

    public native void mediaplayerheadertype(int i);

    public native void mediaplayerinit();

    public native int mediaplayerload(String str, int i, int i2, int i3);

    public native int mediaplayerreopen();

    public native String mediaplayerstatue(String str);

    public native void mediaplayerstop(int i);

    public native void mediaplayerunload();

    public native String mediaplayerurl(String str, String str2);

    public native void mediaplayervodheader(int i);

    public native int mediareceivecount();

    public native int percen();

    public native void setParam(int i, int i2, int i3);

    public native void upnp(String str, String str2, String str3, String str4);

    gplayer() {
    }

    static {
        System.loadLibrary("ghttp");
        System.loadLibrary("hls");
        System.loadLibrary("gp2p");
        System.loadLibrary("rtmp");
        System.loadLibrary("mms");
        System.loadLibrary("udp");
        System.loadLibrary("gemini");
        System.loadLibrary("miniupnp");
        System.loadLibrary("mediaplayer");
    }
}
