package com.loopj.android.http;

public interface LogInterface {
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    public static final int WTF = 8;

    /* renamed from: d */
    void mo1728d(String str, String str2);

    /* renamed from: d */
    void mo1729d(String str, String str2, Throwable th);

    /* renamed from: e */
    void mo1730e(String str, String str2);

    /* renamed from: e */
    void mo1731e(String str, String str2, Throwable th);

    int getLoggingLevel();

    /* renamed from: i */
    void mo1733i(String str, String str2);

    /* renamed from: i */
    void mo1734i(String str, String str2, Throwable th);

    boolean isLoggingEnabled();

    void setLoggingEnabled(boolean z);

    void setLoggingLevel(int i);

    boolean shouldLog(int i);

    /* renamed from: v */
    void mo1739v(String str, String str2);

    /* renamed from: v */
    void mo1740v(String str, String str2, Throwable th);

    /* renamed from: w */
    void mo1741w(String str, String str2);

    /* renamed from: w */
    void mo1742w(String str, String str2, Throwable th);

    void wtf(String str, String str2);

    void wtf(String str, String str2, Throwable th);
}
