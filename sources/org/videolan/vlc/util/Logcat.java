package org.videolan.vlc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Logcat implements Runnable {
    public static final String TAG = "VLC/UiTools/Logcat";
    private Callback mCallback = null;
    private Process mProcess = null;
    private boolean mRun = false;
    private Thread mThread = null;

    public interface Callback {
        void onLog(String str);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r8 = this;
        r6 = 3;
        r0 = new java.lang.String[r6];
        r6 = 0;
        r7 = "logcat";
        r0[r6] = r7;
        r6 = 1;
        r7 = "-v";
        r0[r6] = r7;
        r6 = 2;
        r7 = "time";
        r0[r6] = r7;
        r3 = 0;
        r1 = 0;
        monitor-enter(r8);	 Catch:{ IOException -> 0x0055, all -> 0x0060 }
        r6 = r8.mRun;	 Catch:{ all -> 0x0052 }
        if (r6 != 0) goto L_0x0021;
    L_0x0019:
        monitor-exit(r8);	 Catch:{ all -> 0x0052 }
        org.videolan.vlc.util.Util.close(r3);
        org.videolan.vlc.util.Util.close(r1);
    L_0x0020:
        return;
    L_0x0021:
        r6 = java.lang.Runtime.getRuntime();	 Catch:{ all -> 0x0052 }
        r6 = r6.exec(r0);	 Catch:{ all -> 0x0052 }
        r8.mProcess = r6;	 Catch:{ all -> 0x0052 }
        r4 = new java.io.InputStreamReader;	 Catch:{ all -> 0x0052 }
        r6 = r8.mProcess;	 Catch:{ all -> 0x0052 }
        r6 = r6.getInputStream();	 Catch:{ all -> 0x0052 }
        r4.<init>(r6);	 Catch:{ all -> 0x0052 }
        monitor-exit(r8);	 Catch:{ all -> 0x0072 }
        r2 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x006f, all -> 0x0068 }
        r2.<init>(r4);	 Catch:{ IOException -> 0x006f, all -> 0x0068 }
    L_0x003c:
        r5 = r2.readLine();	 Catch:{ IOException -> 0x0048, all -> 0x006b }
        if (r5 == 0) goto L_0x0057;
    L_0x0042:
        r6 = r8.mCallback;	 Catch:{ IOException -> 0x0048, all -> 0x006b }
        r6.onLog(r5);	 Catch:{ IOException -> 0x0048, all -> 0x006b }
        goto L_0x003c;
    L_0x0048:
        r6 = move-exception;
        r1 = r2;
        r3 = r4;
    L_0x004b:
        org.videolan.vlc.util.Util.close(r3);
        org.videolan.vlc.util.Util.close(r1);
        goto L_0x0020;
    L_0x0052:
        r6 = move-exception;
    L_0x0053:
        monitor-exit(r8);	 Catch:{ all -> 0x0052 }
        throw r6;	 Catch:{ IOException -> 0x0055, all -> 0x0060 }
    L_0x0055:
        r6 = move-exception;
        goto L_0x004b;
    L_0x0057:
        org.videolan.vlc.util.Util.close(r4);
        org.videolan.vlc.util.Util.close(r2);
        r1 = r2;
        r3 = r4;
        goto L_0x0020;
    L_0x0060:
        r6 = move-exception;
    L_0x0061:
        org.videolan.vlc.util.Util.close(r3);
        org.videolan.vlc.util.Util.close(r1);
        throw r6;
    L_0x0068:
        r6 = move-exception;
        r3 = r4;
        goto L_0x0061;
    L_0x006b:
        r6 = move-exception;
        r1 = r2;
        r3 = r4;
        goto L_0x0061;
    L_0x006f:
        r6 = move-exception;
        r3 = r4;
        goto L_0x004b;
    L_0x0072:
        r6 = move-exception;
        r3 = r4;
        goto L_0x0053;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.videolan.vlc.util.Logcat.run():void");
    }

    public synchronized void start(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback should not be null");
        } else if (this.mThread == null && this.mProcess == null) {
            this.mCallback = callback;
            this.mRun = true;
            this.mThread = new Thread(this);
            this.mThread.start();
        } else {
            throw new IllegalStateException("logcat is already started");
        }
    }

    public synchronized void stop() {
        this.mRun = false;
        if (this.mProcess != null) {
            this.mProcess.destroy();
            this.mProcess = null;
        }
        try {
            this.mThread.join();
        } catch (InterruptedException e) {
        }
        this.mThread = null;
        this.mCallback = null;
    }

    public static void writeLogcat(String filename) throws IOException {
        InputStreamReader input = new InputStreamReader(Runtime.getRuntime().exec(new String[]{"logcat", "-v", "time", "-d"}).getInputStream());
        try {
            OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(filename));
            BufferedReader br = new BufferedReader(input);
            BufferedWriter bw = new BufferedWriter(output);
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    bw.write(line);
                    bw.newLine();
                } catch (Exception e) {
                } finally {
                    Util.close(bw);
                    Util.close(output);
                    Util.close(br);
                    Util.close(input);
                }
            }
        } catch (FileNotFoundException e2) {
        }
    }

    public static String getLogcat() throws IOException {
        InputStreamReader input = new InputStreamReader(Runtime.getRuntime().exec(new String[]{"logcat", "-v", "time", "-d", "-t", "500"}).getInputStream());
        BufferedReader br = new BufferedReader(input);
        StringBuilder log = new StringBuilder();
        while (true) {
            String line = br.readLine();
            if (line != null) {
                log.append(line + "\n");
            } else {
                Util.close(br);
                Util.close(input);
                return log.toString();
            }
        }
    }
}
