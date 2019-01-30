package org.cybergarage.util;

import com.google.android.exoplayer.ExoPlayer.Factory;
import com.memo.TestXlog;
import cz.msebera.android.httpclient.protocol.HttpRequestExecutor;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static String getContent(URL url) {
        String host = url.getHost();
        if (url.getPort() == -1) {
            url.getPath();
        } else {
            url.getPath();
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(HttpRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE);
            httpURLConnection.setReadTimeout(Factory.DEFAULT_MIN_REBUFFER_MS);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Length", "0");
            if (host != null) {
                httpURLConnection.setRequestProperty("HOST", host);
            }
            InputStream inputStream = httpURLConnection.getInputStream();
            byte[] bArr = new byte[4096];
            StringBuilder stringBuilder = new StringBuilder();
            Object obj = 1;
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    if (bArr[0] == (byte) 60) {
                        obj = null;
                    }
                    if (obj != null) {
                        stringBuilder.append(new String(bArr, 0, read, "ISO-8859-1"));
                    } else {
                        stringBuilder.append(new String(bArr, 0, read));
                    }
                } else {
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TestXlog.m29i("#### getContent " + url + "  Exception " + e.getMessage() + "\n stack " + getStackTrace(e));
            return "HttpError " + e.getMessage();
        }
    }

    public static String getStackTrace(Exception exception) {
        PrintWriter printWriter;
        Throwable th;
        Writer writer;
        StringWriter stringWriter = null;
        try {
            Writer stringWriter2 = new StringWriter();
            try {
                printWriter = new PrintWriter(stringWriter2);
            } catch (Throwable th2) {
                th = th2;
                printWriter = null;
                writer = stringWriter2;
                if (stringWriter != null) {
                    try {
                        stringWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (printWriter != null) {
                    printWriter.close();
                }
                throw th;
            }
            try {
                exception.printStackTrace(printWriter);
                printWriter.flush();
                stringWriter2.flush();
                if (stringWriter2 != null) {
                    try {
                        stringWriter2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (printWriter != null) {
                    printWriter.close();
                }
                return stringWriter2.toString();
            } catch (Throwable th3) {
                th = th3;
                writer = stringWriter2;
                if (stringWriter != null) {
                    stringWriter.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            printWriter = null;
            if (stringWriter != null) {
                stringWriter.close();
            }
            if (printWriter != null) {
                printWriter.close();
            }
            throw th;
        }
    }
}
