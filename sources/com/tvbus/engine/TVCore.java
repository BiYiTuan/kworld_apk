package com.tvbus.engine;

import android.content.Context;

public class TVCore {
    static final int RM_CLIENT = 2;
    static final int RM_MASTER = 1;
    static final int RM_STANDALONE = 0;
    private static TVCore inst;
    private static long nativeHandle;
    private String mkBroker = null;
    private TVListener tvListener = null;

    private native String description(long j);

    private native void diagnose(long j);

    private native String err2String(long j, int i);

    private native int init(long j, Context context);

    private native long initialise();

    private native void quit(long j);

    private native int run(long j);

    private native void setAuthItems(long j, String str, String str2, String str3);

    private native void setAuthUrl(long j, String str);

    private native void setDomainSuffix(long j, String str);

    private native void setListener(long j, TVListener tVListener);

    private native void setMKBroker(long j, String str);

    private native void setPassword(long j, String str);

    private native void setPlayPort(long j, int i);

    private native void setRunningMode(long j, int i);

    private native void setServPort(long j, int i);

    private native void setUsername(long j, String str);

    private native void start(long j, String str);

    private native void stop(long j);

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized com.tvbus.engine.TVCore getInstance() {
        /*
        r1 = 0;
        r2 = com.tvbus.engine.TVCore.class;
        monitor-enter(r2);
        r3 = inst;	 Catch:{ all -> 0x0026 }
        if (r3 != 0) goto L_0x0023;
    L_0x0008:
        r3 = new com.tvbus.engine.TVCore;	 Catch:{ all -> 0x0026 }
        r3.<init>();	 Catch:{ all -> 0x0026 }
        inst = r3;	 Catch:{ all -> 0x0026 }
        r3 = inst;	 Catch:{ Throwable -> 0x0021 }
        r4 = r3.initialise();	 Catch:{ Throwable -> 0x0021 }
        nativeHandle = r4;	 Catch:{ Throwable -> 0x0021 }
        r4 = nativeHandle;	 Catch:{ all -> 0x0026 }
        r6 = 0;
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 != 0) goto L_0x0023;
    L_0x001f:
        monitor-exit(r2);
        return r1;
    L_0x0021:
        r0 = move-exception;
        goto L_0x001f;
    L_0x0023:
        r1 = inst;	 Catch:{ all -> 0x0026 }
        goto L_0x001f;
    L_0x0026:
        r1 = move-exception;
        monitor-exit(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvbus.engine.TVCore.getInstance():com.tvbus.engine.TVCore");
    }

    public void setTVListener(TVListener listener) {
        this.tvListener = listener;
        try {
            setListener(nativeHandle, listener);
        } catch (Throwable th) {
        }
    }

    public void setPlayPort(int iPort) {
        try {
            setPlayPort(nativeHandle, iPort);
        } catch (Throwable th) {
        }
    }

    public void setServPort(int iPort) {
        try {
            setServPort(nativeHandle, iPort);
        } catch (Throwable th) {
        }
    }

    public void setAuthUrl(String url) {
        try {
            setAuthUrl(nativeHandle, url);
        } catch (Throwable th) {
        }
    }

    public void setUsername(String username) {
        try {
            setUsername(nativeHandle, username);
        } catch (Throwable th) {
        }
    }

    public void setPassword(String password) {
        try {
            setPassword(nativeHandle, password);
        } catch (Throwable th) {
        }
    }

    public void setAuthItems(String arg0, String arg1, String arg2) {
        try {
            setAuthItems(nativeHandle, arg0, arg1, arg2);
        } catch (Throwable th) {
        }
    }

    public void setDomainSuffix(String domainSuffix) {
        try {
            setDomainSuffix(nativeHandle, domainSuffix);
        } catch (Throwable th) {
        }
    }

    public void setMKBroker(String mkBroker) {
        try {
            setMKBroker(nativeHandle, mkBroker);
        } catch (Throwable th) {
        }
    }

    public void start(String url) {
        try {
            start(nativeHandle, url);
        } catch (Throwable th) {
        }
    }

    public void stop() {
        try {
            stop(nativeHandle);
        } catch (Throwable th) {
        }
    }

    public void diagnose() {
        try {
            diagnose(nativeHandle);
        } catch (Throwable th) {
        }
    }

    public String description() {
        try {
            return description(nativeHandle);
        } catch (Throwable th) {
            return "";
        }
    }

    public String err2String(int errorno) {
        try {
            return err2String(nativeHandle, errorno);
        } catch (Throwable th) {
            return "";
        }
    }

    int init(Context context) {
        try {
            return init(nativeHandle, context);
        } catch (Throwable th) {
            return -1;
        }
    }

    int run() {
        try {
            return run(nativeHandle);
        } catch (Throwable th) {
            return -1;
        }
    }

    void quit() {
        try {
            quit(nativeHandle);
        } catch (Throwable th) {
        }
    }

    void setRunningMode(int rm) {
        try {
            setRunningMode(nativeHandle, rm);
        } catch (Throwable th) {
        }
    }

    static {
        try {
            System.loadLibrary("tvcore");
        } catch (Throwable th) {
        }
    }
}
