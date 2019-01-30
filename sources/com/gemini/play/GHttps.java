package com.gemini.play;

import cz.msebera.android.httpclient.cookie.SM;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class GHttps {

    private class MyHostnameVerifier implements HostnameVerifier {
        private MyHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private class MyTrustManager implements X509TrustManager {
        private MyTrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public String GetHttps(String https, int timeout) {
        return GetHttps(https, timeout, null, null);
    }

    public String GetHttps(String https, int timeout, String cookie, String UserAgent) {
        String res = "";
        try {
            HttpsURLConnection httpsConn = (HttpsURLConnection) new URL(https).openConnection();
            httpsConn.setConnectTimeout(timeout);
            httpsConn.setReadTimeout(timeout);
            InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
            if (UserAgent == null || UserAgent.length() <= 0) {
                httpsConn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            } else {
                httpsConn.setRequestProperty("User-agent", UserAgent);
            }
            if (cookie != null && cookie.length() > 0) {
                httpsConn.setRequestProperty(SM.COOKIE, cookie);
            }
            int respInt = 0;
            while (respInt != -1) {
                respInt = insr.read();
                res = res + ((char) respInt);
            }
        } catch (Exception e) {
            MGplayer.MyPrintln(e.getMessage());
        }
        return res;
    }
}
