package com.memo.cable;

import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class DeviceFullSearch {
    private static int count = 2;
    private static Object lock = new Object();

    public static void forceSearchDLNADevice(String str) {
        int i = 0;
        if (!TextUtils.isEmpty(str)) {
            ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(count);
            final LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
            final String substring = str.substring(0, str.lastIndexOf(".") + 1);
            while (i < 1) {
                newFixedThreadPool.submit(new Runnable() {
                    public void run() {
                        while (true) {
                            if (linkedBlockingQueue.isEmpty()) {
                                Log.i("concurrent_test", "mPingIps.is empty");
                                try {
                                    synchronized (DeviceFullSearch.lock) {
                                        DeviceFullSearch.lock.wait();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.i("concurrent_test", "latch.await() over");
                            } else {
                                String str = (String) linkedBlockingQueue.poll();
                                Log.i("concurrent_test", "poll result " + str);
                                if (TextUtils.isEmpty(str)) {
                                }
                            }
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                });
                i++;
            }
            newFixedThreadPool.submit(new Runnable() {
                public void run() {
                    for (int i = 0; i < 256; i++) {
                        Log.i("concurrent_test", "exec:" + substring + i);
                        if (ShellUtils.execCmd("ping -c 2 -w 6 " + substring + i, false).result == 0) {
                            String str = substring + i;
                            linkedBlockingQueue.offer(str);
                            Log.i("concurrent_test", "put:" + str);
                            synchronized (DeviceFullSearch.lock) {
                                DeviceFullSearch.lock.notify();
                            }
                        }
                    }
                }
            });
        }
    }
}
