package com.gemini.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;

public class MACUtils {
    public static String getMac2() {
        IOException e;
        String mac = "";
        try {
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
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return mac.toLowerCase();
        }
        return mac.toLowerCase();
    }

    public static String getMac() {
        IOException e;
        String str = "";
        try {
            if (!new File("/sys/class/net/eth0/address").exists()) {
                return getWifiMac().toLowerCase();
            }
            FileInputStream localFileInputStream = new FileInputStream("/sys/class/net/eth0/address");
            byte[] arrayOfByte = new byte[17];
            localFileInputStream.read(arrayOfByte, 0, 17);
            String str2 = new String(arrayOfByte);
            try {
                localFileInputStream.close();
                str = str2;
            } catch (IOException e2) {
                e = e2;
                str = str2;
                e.printStackTrace();
                str = getWifiMac();
                str = getWifiMac();
                if (str.contains(":")) {
                    str = str.replace(":", "").trim();
                }
                if (str.contains("-")) {
                    str = str.replace("-", "").trim();
                }
                return str.toLowerCase();
            }
            if (str.equals("") || str == "") {
                str = getWifiMac();
            }
            if (str.contains(":")) {
                str = str.replace(":", "").trim();
            }
            if (str.contains("-")) {
                str = str.replace("-", "").trim();
            }
            return str.toLowerCase();
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            str = getWifiMac();
            str = getWifiMac();
            if (str.contains(":")) {
                str = str.replace(":", "").trim();
            }
            if (str.contains("-")) {
                str = str.replace("-", "").trim();
            }
            return str.toLowerCase();
        }
    }

    public static String getWifiMac() {
        String str = "";
        String macSerial = "";
        try {
            LineNumberReader input = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
            while (str != null) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                macSerial = loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return macSerial;
    }

    public static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    public static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }
}
