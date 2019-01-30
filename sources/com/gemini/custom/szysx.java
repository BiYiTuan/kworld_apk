package com.gemini.custom;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import com.gemini.play.MGplayer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

public class szysx {
    public static void start(Context _this) {
        if (MGplayer.custom().equals("szysx")) {
            int size = MGplayer.MyGetSharedPreferences(_this, "data", 0).getInt("fontsize", 70);
            Editor editor = MGplayer.MyGetSharedPreferences(_this, "data", 0).edit();
            editor.putInt("fontsize", size);
            editor.commit();
        }
    }

    public static String GetCpuByfileNull() {
        String cpuid = "";
        String m_szDevIDShort = "";
        try {
            m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.ID.length() % 10) + (Build.USER.length() % 10);
        } catch (Exception e) {
        }
        String m_szLongID = m_szDevIDShort + MACUtils.getMac();
        MGplayer.MyPrintln("m_szLongID:" + m_szLongID);
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        byte[] p_md5Data = m.digest();
        String m_szUniqueID = new String();
        for (byte b : p_md5Data) {
            int b2 = b & 255;
            if (b2 <= 15) {
                m_szUniqueID = m_szUniqueID + "0";
            }
            m_szUniqueID = m_szUniqueID + Integer.toHexString(b2);
        }
        return m_szUniqueID.toUpperCase();
    }

    public static String getMac() {
        IOException e;
        String mac = "";
        try {
            if (!new File("/sys/class/net/eth0/address").exists()) {
                return null;
            }
            FileInputStream localFileInputStream = new FileInputStream("/sys/class/net/eth0/address");
            byte[] arrayOfByte = new byte[17];
            localFileInputStream.read(arrayOfByte, 0, 17);
            String mac2 = new String(arrayOfByte);
            try {
                localFileInputStream.close();
                mac = mac2;
            } catch (IOException e2) {
                e = e2;
                mac = mac2;
                e.printStackTrace();
                return mac.toLowerCase();
            }
            return mac.toLowerCase();
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return mac.toLowerCase();
        }
    }

    public static String getMacAddrWifi7() {
        try {
            for (NetworkInterface nif : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }
                    StringBuilder res1 = new StringBuilder();
                    int length = macBytes.length;
                    for (int i = 0; i < length; i++) {
                        res1.append(String.format("%02X:", new Object[]{Byte.valueOf(macBytes[i])}));
                    }
                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}
