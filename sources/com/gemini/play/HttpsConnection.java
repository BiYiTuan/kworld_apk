package com.gemini.play;

import cz.msebera.android.httpclient.HttpHeaders;
import io.vov.vitamio.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.lingala.zip4j.util.InternalZipConstants;

public class HttpsConnection {
    private static final String DEFAULT_CHARSET = "utf-8";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    /* renamed from: com.gemini.play.HttpsConnection$1 */
    static class C02771 implements HostnameVerifier {
        C02771() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /* renamed from: com.gemini.play.HttpsConnection$2 */
    static class C02782 implements HostnameVerifier {
        C02782() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class DefaultTrustManager implements X509TrustManager {
        private DefaultTrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public static String doPost(String url, String params, String charset, int connectTimeout, int readTimeout, boolean getLocation) throws Exception {
        String ctype = "text/html;charset=" + charset;
        byte[] content = new byte[0];
        if (params != null) {
            content = params.getBytes(charset);
        }
        return doPost(url, ctype, content, connectTimeout, readTimeout, getLocation);
    }

    public static String doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout, boolean getLocation) throws Exception {
        HttpsURLConnection conn = null;
        OutputStream out = null;
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);
            conn = getConnection(new URL(url), "POST", ctype);
            conn.setRequestMethod("POST");
            conn.setHostnameVerifier(new C02771());
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            out = conn.getOutputStream();
            out.write(content);
            String responseAsString = getResponseAsString(conn);
            if (responseAsString != null && getLocation) {
                String location = conn.getHeaderField(HttpHeaders.LOCATION);
                if (location != null) {
                    responseAsString = location.substring(location.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR) + 1, location.length());
                    if (out != null) {
                        out.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                    return responseAsString;
                }
            }
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
            return responseAsString;
        } catch (Throwable e) {
            Log.m32e("doPost REQUEST_RESPONSE_ERROR, URL = " + url, e);
            throw e;
        } catch (Throwable e2) {
            Log.m32e("doPost GET_CONNECTOIN_ERROR, URL = " + url, e2);
            throw e2;
        } catch (Throwable th) {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String doGet(String url, String ctype, int connectTimeout, int readTimeout) throws Exception {
        HttpsURLConnection conn = null;
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);
            conn = getConnection(new URL(url), "GET", ctype);
            conn.setHostnameVerifier(new C02782());
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            String rsp = getResponseAsString(conn);
            if (conn != null) {
                conn.disconnect();
            }
            return rsp;
        } catch (Throwable e) {
            Log.m32e("doGet REQUEST_RESPONSE_ERROR, URL = " + url, e);
            throw e;
        } catch (Throwable e2) {
            Log.m32e("doGet GET_CONNECTOIN_ERROR, URL = " + url, e2);
            throw e2;
        } catch (Throwable th) {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static HttpsURLConnection getConnection(URL JNurl, String method, String ctype) throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection) JNurl.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
    }

    protected static String getResponseAsString(HttpsURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        }
        String msg = getStreamAsString(es, charset);
        if (msg == null) {
            throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
        }
        throw new IOException(msg);
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();
            char[] chars = new char[256];
            while (true) {
                int count = reader.read(chars);
                if (count <= 0) {
                    break;
                }
                writer.write(chars, 0, count);
            }
            String stringWriter = writer.toString();
            return stringWriter;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;
        if (ctype == null) {
            return charset;
        }
        for (String param : ctype.split(";")) {
            String param2 = param2.trim();
            if (param2.startsWith("charset")) {
                String[] pair = param2.split("=", 2);
                if (pair.length != 2 || pair[1] == null) {
                    return charset;
                }
                return pair[1].trim();
            }
        }
        return charset;
    }
}
