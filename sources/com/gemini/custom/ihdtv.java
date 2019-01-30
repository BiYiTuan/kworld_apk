package com.gemini.custom;

import android.util.Base64;
import com.gemini.play.CookieStatus;
import com.gemini.play.MGplayer;
import com.google.android.exoplayer.ExoPlayer.Factory;
import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.HttpStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.lingala.zip4j.util.InternalZipConstants;

public class ihdtv {
    public static String playlist_context = null;
    public static String playlist_pw = null;
    public static String playlist_url = null;
    public static int port = 8090;

    /* renamed from: com.gemini.custom.ihdtv$1 */
    static class C02051 implements Runnable {
        ServerSocket server;
        Socket socket;

        C02051() {
        }

        public void run() {
            try {
                this.server = new ServerSocket(ihdtv.port);
                while (true) {
                    if (this.server != null) {
                        this.socket = this.server.accept();
                    }
                    if (this.socket != null && ihdtv.playlist_url != null && ihdtv.playlist_pw != null) {
                        try {
                            int ii;
                            OutputStream output = this.socket.getOutputStream();
                            String[] playlist_urls = ihdtv.playlist_url.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
                            String playlist_ip = "";
                            for (ii = 0; ii < playlist_urls.length - 1; ii++) {
                                playlist_ip = playlist_ip + playlist_urls[ii] + InternalZipConstants.ZIP_FILE_SEPARATOR;
                            }
                            String playlist_context = MGplayer.sendServerCmd(ihdtv.geturl(ihdtv.playlist_url, ihdtv.playlist_pw));
                            if (playlist_context.length() < 5) {
                                output.flush();
                                output.close();
                                this.socket.close();
                                this.socket = null;
                            } else {
                                String[] playlist_contexts = playlist_context.split("\n");
                                playlist_context = "";
                                ii = 0;
                                while (ii < playlist_contexts.length) {
                                    if (!playlist_contexts[ii].contains(".ts") || playlist_contexts[ii].contains("http://")) {
                                        playlist_context = playlist_context + playlist_contexts[ii] + "\n";
                                    } else {
                                        playlist_contexts[ii] = playlist_ip + playlist_contexts[ii];
                                        playlist_context = playlist_context + ihdtv.geturl(playlist_contexts[ii], ihdtv.playlist_pw) + "\n";
                                    }
                                    ii++;
                                }
                                output.write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + playlist_context.length() + "\r\n\r\n").getBytes());
                                output.write(playlist_context.getBytes());
                                output.flush();
                                output.close();
                                this.socket.close();
                                this.socket = null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (this.socket != null) {
                        this.socket.close();
                        this.socket = null;
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static String geturl(String path, String pw) {
        String secret = pw;
        String e = String.valueOf((MGplayer.seconds_prc / 1000) + 300);
        String md5_hex = MD5(secret + e);
        int len = md5_hex.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(md5_hex.charAt(i), 16) << 4) + Character.digit(md5_hex.charAt(i + 1), 16));
        }
        return path + "?st=" + new String(Base64.encode(data, 9)).trim() + "&e=" + e;
    }

    public static String MD5_2(String string) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 255) < 16) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 255));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e2);
        }
    }

    public static final String MD5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            char[] str = new char[(j * 2)];
            int k = 0;
            for (byte byte0 : mdInst.digest()) {
                int i = k + 1;
                str[k] = hexDigits[(byte0 >>> 4) & 15];
                k = i + 1;
                str[i] = hexDigits[byte0 & 15];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createHttpServerForHuidixing() {
        new Thread(new C02051()).start();
    }

    public static void setPlaylist_url_pw(String url, String pw) {
        playlist_url = url;
        playlist_pw = pw;
    }

    public static String get_rtmp_url(String url, String key) {
        String time = String.valueOf(MGplayer.seconds / 1000);
        return url + "?k=" + MGplayer.MD5(key + time).toLowerCase() + "&t=" + time;
    }

    public static String SendServerCmd_Location(String httpUrl) {
        URL url = null;
        String resultData = "";
        CookieStatus cookier = new CookieStatus();
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        MGplayer.MyPrintln("sendServerCmd Url:" + httpUrl);
        if (url != null) {
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(Factory.DEFAULT_MIN_REBUFFER_MS);
                urlConn.setReadTimeout(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                urlConn.setRequestMethod("GET");
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                MGplayer.MyPrintln("sendServerCmd result :" + resultData);
                String location = urlConn.getHeaderField(HttpHeaders.LOCATION);
                in.close();
                urlConn.disconnect();
                return location;
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
}
