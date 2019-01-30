package org.videolan.libvlc;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import org.videolan.libvlc.util.HWDecoderUtil;
import org.videolan.libvlc.util.HWDecoderUtil.AudioOutput;

public class LibVLC extends VLCObject<Event> {
    private static final String TAG = "VLC/LibVLC";
    private static boolean sLoaded = false;
    private static OnNativeCrashListener sOnNativeCrashListener;

    public interface OnNativeCrashListener {
        void onNativeCrash();
    }

    public static class Event extends VLCEvent {
        protected Event(int type) {
            super(type);
        }
    }

    private native void nativeNew(String[] strArr, String str);

    private native void nativeRelease();

    private native void nativeSetUserAgent(String str, String str2);

    public native String changeset();

    public native String compiler();

    public native String version();

    public /* bridge */ /* synthetic */ boolean isReleased() {
        return super.isReleased();
    }

    public LibVLC(Context context, ArrayList<String> options) {
        loadLibraries();
        boolean setAout = true;
        boolean setChroma = true;
        if (options != null) {
            Iterator it = options.iterator();
            while (it.hasNext()) {
                String option = (String) it.next();
                if (option.startsWith("--aout=")) {
                    setAout = false;
                }
                if (option.startsWith("--androidwindow-chroma")) {
                    setChroma = false;
                }
                if (!setAout && !setChroma) {
                    break;
                }
            }
        }
        if (setAout || setChroma) {
            if (options == null) {
                options = new ArrayList();
            }
            if (setAout) {
                if (HWDecoderUtil.getAudioOutputFromDevice() == AudioOutput.OPENSLES) {
                    options.add("--aout=opensles");
                } else {
                    options.add("--aout=android_audiotrack");
                }
            }
            if (setChroma) {
                options.add("--androidwindow-chroma");
                options.add("RV32");
            }
        }
        nativeNew((String[]) options.toArray(new String[options.size()]), context.getDir("vlc", 0).getAbsolutePath());
    }

    public LibVLC(Context context) {
        this(context, null);
    }

    protected Event onEventNative(int eventType, long arg1, float arg2) {
        return null;
    }

    protected void onReleaseNative() {
        nativeRelease();
    }

    public static void setOnNativeCrashListener(OnNativeCrashListener l) {
        sOnNativeCrashListener = l;
    }

    private static void onNativeCrash() {
        if (sOnNativeCrashListener != null) {
            sOnNativeCrashListener.onNativeCrash();
        }
    }

    public void setUserAgent(String name, String http) {
        nativeSetUserAgent(name, http);
    }

    static synchronized void loadLibraries() {
        synchronized (LibVLC.class) {
            if (!sLoaded) {
                sLoaded = true;
                if (VERSION.SDK_INT >= 10) {
                    try {
                        if (VERSION.SDK_INT <= 12) {
                            System.loadLibrary("anw.10");
                        } else if (VERSION.SDK_INT <= 13) {
                            System.loadLibrary("anw.13");
                        } else if (VERSION.SDK_INT <= 17) {
                            System.loadLibrary("anw.14");
                        } else if (VERSION.SDK_INT <= 20) {
                            System.loadLibrary("anw.18");
                        } else {
                            System.loadLibrary("anw.21");
                        }
                    } catch (Throwable th) {
                        Log.d(TAG, "anw library not loaded");
                    }
                    try {
                        if (VERSION.SDK_INT <= 10) {
                            System.loadLibrary("iomx.10");
                        } else if (VERSION.SDK_INT <= 13) {
                            System.loadLibrary("iomx.13");
                        } else if (VERSION.SDK_INT <= 17) {
                            System.loadLibrary("iomx.14");
                        } else if (VERSION.SDK_INT <= 18) {
                            System.loadLibrary("iomx.18");
                        } else if (VERSION.SDK_INT <= 19) {
                            System.loadLibrary("iomx.19");
                        }
                    } catch (Throwable t) {
                        if (VERSION.SDK_INT <= 15) {
                            Log.w(TAG, "Unable to load the iomx library: " + t);
                        }
                    }
                }
                try {
                    System.loadLibrary("compat.7");
                } catch (Throwable th2) {
                }
                try {
                    System.loadLibrary("vlc");
                    System.loadLibrary("vlcjni");
                } catch (UnsatisfiedLinkError ule) {
                    Log.e(TAG, "Can't load vlcjni library: " + ule);
                    System.exit(1);
                } catch (SecurityException se) {
                    Log.e(TAG, "Encountered a security issue when loading vlcjni library: " + se);
                    System.exit(1);
                }
            }
        }
    }
}
