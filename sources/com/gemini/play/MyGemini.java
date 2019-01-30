package com.gemini.play;

import cz.msebera.android.httpclient.protocol.HttpRequestExecutor;
import net.lingala.zip4j.util.InternalZipConstants;

public class MyGemini {
    public static String getStatue(String url) {
        String statue = "ERROR";
        if (!url.startsWith("gemini://") && !url.startsWith("gp2p://")) {
            return statue;
        }
        String[] urls = url.split("\\?");
        if (urls.length < 2) {
            return statue;
        }
        String serverip = "http://" + urls[0].substring(6);
        if (!serverip.endsWith(InternalZipConstants.ZIP_FILE_SEPARATOR)) {
            serverip = serverip + InternalZipConstants.ZIP_FILE_SEPARATOR;
        }
        String u = serverip + getUrlParam(url, "distribution") + "/info.dat";
        MGplayer.MyPrintln("getStatue u:" + u);
        String ret = MGplayer.sendServerCmd(u, HttpRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE);
        if (ret == null) {
            return statue;
        }
        String[] rets = ret.split("#@#");
        if (rets.length >= 3) {
            return rets[0] + "#0#" + rets[1];
        }
        return statue;
    }

    public static String getUrlParam(String url, String param) {
        if (url == null) {
            return null;
        }
        String[] urls = url.split("\\?");
        MGplayer.MyPrintln("getUrlParam:" + url + "# " + urls.length);
        if (urls.length < 2) {
            return null;
        }
        String[] params = urls[1].split("&");
        for (String split : params) {
            String[] values = split.split("=");
            if (values.length >= 2 && values[0].equals(param)) {
                return values[1];
            }
        }
        return null;
    }
}
