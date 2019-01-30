package com.gemini.play;

/* compiled from: gcustom */
class gf {
    public native void end(String str, String str2);

    public native void start(String str, int i, int i2, String str2);

    public native String url();

    gf() {
    }

    static {
        System.loadLibrary("tcp");
    }
}
