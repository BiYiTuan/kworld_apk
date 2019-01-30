package com.nagasoft.player;

import android.os.Handler;
import com.gemini.play.MGplayer;
import java.lang.ref.WeakReference;

public class VJPlayer {
    public static final int PLAYER_NOTIFY_URL = 4113;
    protected static final String TAG = "NGPlayer";
    public static boolean gbload;
    private int mNativeListener = 0;
    private int mNativePlayer = 0;
    private String mStrVideoURL;
    private UrlChanged mUCCallback = null;
    private OnVJMSErrorListener mVELCallback = null;
    private Handler pHandler;

    public static final native void native_init();

    public native void _release();

    public native long getPlayBackDuration();

    public native boolean isLiveStream();

    public native boolean isPlayBackStream();

    public native boolean isVodFile();

    public final native void native_finalize();

    public final native void native_setup(Object obj);

    public native boolean seekPlayBack(long j);

    public native void setURL(String str);

    public native void setVJMSBufferTimeout(int i);

    public native boolean start();

    public native void stop();

    static {
        gbload = false;
        try {
            System.loadLibrary("p2pcore");
            System.loadLibrary("vjplayer_jni");
            gbload = true;
            native_init();
        } catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
            gbload = false;
            MGplayer.MyPrintln("loadLibrary libvjplayer_jni error: " + localUnsatisfiedLinkError.toString());
        }
    }

    public VJPlayer(UrlChanged paramUrlChanged) {
        if (gbload) {
            native_setup(new WeakReference(this));
        }
        this.mUCCallback = paramUrlChanged;
    }

    public String getVideoURL() {
        return this.mStrVideoURL;
    }

    public void notifyError(int paramInt) {
        MGplayer.MyPrintln("from native error is " + paramInt);
        notifyUIError(paramInt);
    }

    public void notifyPlayURL(String paramString) {
        MGplayer.MyPrintln("vjms native string is " + paramString);
        this.mStrVideoURL = paramString;
        notifyUI(paramString);
    }

    protected void notifyUI(String paramString) {
        if (this.mUCCallback != null) {
            this.mUCCallback.onUrlChanged(paramString, this.pHandler);
        }
    }

    protected void notifyUIError(int paramInt) {
        if (this.mVELCallback != null) {
            this.mVELCallback.onVJMSError(paramInt);
        }
    }

    public void setOnVJMSErrorListener(OnVJMSErrorListener paramOnVJMSErrorListener) {
        this.mVELCallback = paramOnVJMSErrorListener;
    }

    public void setHandler(Handler p) {
        this.pHandler = p;
    }
}
