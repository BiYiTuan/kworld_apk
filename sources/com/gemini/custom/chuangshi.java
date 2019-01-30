package com.gemini.custom;

import android.os.Handler;
import com.gemini.play.MGplayer;
import java.util.Calendar;
import java.util.Date;

public class chuangshi {
    private static int chuangshi_sofs = -1;
    private static String index = "";

    public static void Thread_Chuangshi_Init() {
        if (MGplayer.custom().equals("chuangshi") || MGplayer.custom().equals("chuangshi-v2") || MGplayer.custom().equals("chuangshivgo")) {
            final Handler Chuangshisofs_Hander = new Handler();
            Chuangshisofs_Hander.postDelayed(new Runnable() {

                /* renamed from: com.gemini.custom.chuangshi$1$1 */
                class C01961 extends Thread {
                    C01961() {
                    }

                    public void run() {
                        if (MGplayer.custom().equals("chuangshi") || MGplayer.custom().equals("chuangshi-v2")) {
                            chuangshi.index = MGplayer.sendServerCmd("http://an.iptvcs2.com/sofs.php", 1000).trim();
                        } else if (MGplayer.custom().equals("chuangshivgo")) {
                            chuangshi.index = MGplayer.sendServerCmd("http://an.gohdtv.info/sofs.php", 1000).trim();
                        }
                    }
                }

                public void run() {
                    MGplayer.MyPrintln("Thread_Chuangshi_Init 1");
                    new C01961().start();
                    MGplayer.MyPrintln("Thread_Chuangshi_Init 2 " + chuangshi.index);
                    if (MGplayer.isNumeric(chuangshi.index)) {
                        MGplayer.MyPrintln("Thread_Chuangshi_Init 3 " + chuangshi.chuangshi_sofs);
                        if (chuangshi.chuangshi_sofs == -1) {
                            chuangshi.chuangshi_sofs = Integer.parseInt(chuangshi.index);
                            if (chuangshi.chuangshi_sofs == 1 || chuangshi.chuangshi_sofs == 2 || chuangshi.chuangshi_sofs == 3 || chuangshi.chuangshi_sofs == 4) {
                                MGplayer.MyPrintln("Thread_Chuangshi_Init 4 " + chuangshi.chuangshi_sofs);
                                chuangshi.chuangshi_send();
                            }
                        } else {
                            chuangshi.chuangshi_sofs = Integer.parseInt(chuangshi.index);
                        }
                    } else {
                        MGplayer.MyPrintln("Thread_Chuangshi_Init 5 " + chuangshi.chuangshi_sofs);
                        chuangshi.chuangshi_send();
                    }
                    Chuangshisofs_Hander.postDelayed(this, 600000);
                }
            }, 1000);
        }
        if (chuangshi_sofs != 2 && chuangshi_sofs != 4) {
            return;
        }
        if (MGplayer.custom().equals("chuangshi") || MGplayer.custom().equals("chuangshi-v2") || MGplayer.custom().equals("chuangshivgo")) {
            final Handler ChuangshiHander = new Handler();
            ChuangshiHander.postDelayed(new Runnable() {
                public void run() {
                    chuangshi.chuangshi_send();
                    ChuangshiHander.postDelayed(this, 600000);
                }
            }, 1000);
        }
    }

    public static void chuangshi_send2() {
        if (chuangshi_sofs != 3 && chuangshi_sofs != 4) {
            return;
        }
        if (MGplayer.custom().equals("chuangshi") || MGplayer.custom().equals("chuangshi-v2") || MGplayer.custom().equals("chuangshivgo")) {
            chuangshi_send();
        }
    }

    public static void chuangshi_send() {
        if (MGplayer.custom().equals("chuangshi") || MGplayer.custom().equals("chuangshi-v2") || MGplayer.custom().equals("chuangshivgo")) {
            String cmd;
            Calendar calendar = Calendar.getInstance();
            if (MGplayer.seconds_prc > 0) {
                calendar.setTime(new Date(MGplayer.seconds_prc));
            }
            int year = calendar.get(1);
            int month = calendar.get(2) + 1;
            int day = calendar.get(5);
            int hour = calendar.get(11);
            int minute = calendar.get(12);
            int second = calendar.get(13);
            String send_time = year + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(month)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(day)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(hour)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(minute)}) + "gemini" + String.format("%02d", new Object[]{Integer.valueOf(second)});
            if (MGplayer.custom().equals("chuangshi-v2")) {
                cmd = "|" + MGplayer.tv.GetMac() + "|" + MGplayer.j1(MGplayer.tv.getCpuID() + "$" + MGplayer.tv.GetMac() + "$" + send_time + "$2") + "|2|";
            } else if (MGplayer.custom().equals("chuangshivgo")) {
                cmd = "|" + MGplayer.tv.GetMac() + "|" + MGplayer.j1(MGplayer.tv.getCpuID() + "$" + MGplayer.tv.GetMac() + "$" + send_time + "$2") + "|11|";
            } else {
                cmd = "|" + MGplayer.tv.GetMac() + "|" + MGplayer.j1(MGplayer.tv.getCpuID() + "$" + MGplayer.tv.GetMac() + "$" + send_time + "$1") + "|1|";
            }
            MGplayer.MyPrintln("####################send check u:" + cmd + "####################");
            final UdpClient client = new UdpClient(cmd);
            new Thread() {
                public void run() {
                    if (MGplayer.custom().equals("chuangshi")) {
                        client.send("so.iptvcs2.com");
                    } else if (MGplayer.custom().equals("chuangshi-v2")) {
                        client.send("so2.iptvcs2.com");
                    } else if (MGplayer.custom().equals("chuangshivgo")) {
                        client.send("so.gohdtv.info");
                    }
                }
            }.start();
        }
    }
}
