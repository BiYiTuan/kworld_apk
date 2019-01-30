package com.gemini.custom;

import android.os.Handler;
import com.gemini.play.MGplayer;
import com.google.android.exoplayer.hls.HlsChunkSource;

public class lookiptv {
    public static String cpuid = null;
    public static int lookiptvSendEvery = 0;
    public static int lookiptvSendTimeOut = 0;
    public static String lookiptvSendUrl = null;
    public static boolean lookiptvloaded = false;
    public static String mac = null;
    public static String token = null;
    public static String userkey = null;
    public static String username = null;

    public static void lookiptv_start() {
        if (MGplayer.custom().equals("lookiptv") || MGplayer.custom().equals("xiaoqi") || MGplayer.custom().equals("aikanvip") || MGplayer.custom().equals("52home") || MGplayer.custom().equals("huaren") || MGplayer.custom().equals("ying") || MGplayer.custom().equals("xtvants") || MGplayer.custom().equals("blue885") || MGplayer.custom().equals("anko") || MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spainglobal") || MGplayer.custom().equals("spain1cn") || MGplayer.custom().equals("goat") || MGplayer.custom().equals("coolsee")) {
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    int timeout = lookiptv.lookiptvSendTimeOut;
                    if (lookiptv.lookiptvSendUrl != null && lookiptv.lookiptvSendTimeOut > 0 && lookiptv.lookiptvSendUrl.length() > 7 && lookiptv.lookiptvSendUrl.startsWith("http://") && lookiptv.lookiptvloaded) {
                        if (lookiptv.lookiptvSendUrl.contains("#@#")) {
                            String[] lookiptvSendUrls = lookiptv.lookiptvSendUrl.split("#@#");
                            for (int ii = 0; ii < lookiptvSendUrls.length; ii++) {
                                if (MGplayer.mWebView != null) {
                                    MGplayer.mWebView.loadUrl(lookiptvSendUrls[ii]);
                                }
                                MGplayer.MyPrintln("lookiptvSendUrl:" + lookiptvSendUrls[ii] + MGplayer.seconds_prc);
                                MGplayer.sleep(1000);
                            }
                        } else if (MGplayer.mWebView != null) {
                            MGplayer.mWebView.loadUrl(lookiptv.lookiptvSendUrl);
                        }
                    }
                    if (lookiptv.lookiptvloaded) {
                        mHandler.postDelayed(this, (long) timeout);
                    } else {
                        mHandler.postDelayed(this, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                    }
                }
            }, (long) lookiptvSendTimeOut);
        }
    }

    public static void lookiptv_send() {
        if ((!MGplayer.custom().equals("lookiptv") && !MGplayer.custom().equals("xiaoqi") && !MGplayer.custom().equals("aikanvip") && !MGplayer.custom().equals("52home") && !MGplayer.custom().equals("huaren") && !MGplayer.custom().equals("ying") && !MGplayer.custom().equals("xtvants") && !MGplayer.custom().equals("blue885") && !MGplayer.custom().equals("anko") && !MGplayer.custom().equals("goat") && !MGplayer.custom().equals("coolsee")) || lookiptvSendUrl == null || lookiptvSendEvery <= 0 || lookiptvSendUrl.length() <= 7 || !lookiptvSendUrl.startsWith("http://")) {
            return;
        }
        if (lookiptvSendUrl.contains("#@#")) {
            String[] lookiptvSendUrls = lookiptvSendUrl.split("#@#");
            for (String loadUrl : lookiptvSendUrls) {
                if (MGplayer.mWebView != null) {
                    MGplayer.mWebView.loadUrl(loadUrl);
                }
            }
        } else if (MGplayer.mWebView != null) {
            MGplayer.mWebView.loadUrl(lookiptvSendUrl);
        }
    }

    public static String lookiptv_userid(String puserid) {
        String[] userids = puserid.split("@");
        if (userids.length < 3) {
            return null;
        }
        get_xml_item("http://211.23.22.16:8080/forcetech/login?mac=" + userids[2] + "&sn=" + userids[2] + "&username=&type=1&key=" + MGplayer.MD5(userids[2] + "forcetech").toLowerCase());
        String t = puserid;
        if (!(puserid == null || username == null || token == null)) {
            t = "$username=" + username + "$channelid=" + userids[0] + "$columnid=36$vodid=" + userids[1] + "$key=" + MGplayer.MD5("forcetech" + token).toLowerCase();
        }
        String str2 = MGplayer.sendServerCmd("http://211.23.22.16:8080/forcetech/member" + "?username=" + username + "&key=" + MGplayer.MD5("forcetech" + token).toLowerCase());
        String str3 = MGplayer.sendServerCmd("http://211.23.22.16:8080/forcetech/p2plink" + "?username=" + username + "&key=" + MGplayer.MD5("forcetech" + token).toLowerCase());
        MGplayer.MyPrintln("t:" + t + "str2:" + str2 + "str3:" + str3 + "str4:" + MGplayer.sendServerCmd("http://211.23.22.16:8080/forcetech/myproduct" + "?username=" + username + "&key=" + MGplayer.MD5("forcetech" + token).toLowerCase()));
        return t;
    }

    public static void get_xml_item(String purl) {
        String str = MGplayer.sendServerCmd(purl);
        if (str != null && str.contains("<username>") && str.contains("<token>")) {
            String items = "<username>";
            username = str.substring(str.indexOf(items) + items.length(), str.indexOf("</username>"));
            items = "<token>";
            token = str.substring(str.indexOf(items) + items.length(), str.indexOf("</token>"));
            MGplayer.MyPrintln("username:" + username + " token:" + token);
        }
    }
}
