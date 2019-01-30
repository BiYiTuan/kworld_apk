package com.gemini.play;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements UncaughtExceptionHandler {
    public static final boolean DEBUG = true;
    private static CrashHandler INSTANCE;
    private UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    public void init(Context ctx) {
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (handleException(ex) || this.mDefaultHandler == null) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            Process.killProcess(Process.myPid());
            System.exit(10);
            return;
        }
        this.mDefaultHandler.uncaughtException(thread, ex);
    }

    private boolean handleException(Throwable ex) {
        if (ex != null) {
            StackTraceElement[] stack = ex.getStackTrace();
            final String message = ex.getMessage();
            new Thread() {
                public void run() {
                    Looper.prepare();
                    MGplayer.MyPrintln("crash message 0:" + message);
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
                    final String fileName = "debug-" + MGplayer.tv.GetMac().replace(":", "") + "-" + sdf.format(d) + ".txt";
                    final File file = new File(Environment.getExternalStorageDirectory(), "debug-" + MGplayer.tv.GetMac().replace(":", "") + "-" + sdff.format(d) + ".txt");
                    if (file.exists()) {
                        file.delete();
                    }
                    new Thread(new Runnable() {
                        public void run() {
                            Exception e;
                            try {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(new String[]{"logcat"}).getInputStream()));
                                BufferedReader bufferedReader;
                                try {
                                    FileOutputStream fos = new FileOutputStream(file, true);
                                    fos.write((fileName + "\r\n").getBytes());
                                    while (true) {
                                        String line = reader.readLine();
                                        if (line != null) {
                                            fos.write((line + "\r\n").getBytes());
                                        } else {
                                            fos.flush();
                                            fos.close();
                                            bufferedReader = reader;
                                            return;
                                        }
                                    }
                                } catch (Exception e2) {
                                    e = e2;
                                    bufferedReader = reader;
                                    e.printStackTrace();
                                }
                            } catch (Exception e3) {
                                e = e3;
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }.start();
        }
        return false;
    }
}
