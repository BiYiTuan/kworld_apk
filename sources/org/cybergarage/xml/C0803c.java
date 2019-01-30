package org.cybergarage.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.cybergarage.http.C0953e;
import org.cybergarage.http.C0954g;

/* renamed from: org.cybergarage.xml.c */
public abstract class C0803c {
    public C0802b parse(File file) {
        try {
            InputStream fileInputStream = new FileInputStream(file);
            C0802b parse = parse(fileInputStream);
            fileInputStream.close();
            return parse;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    public abstract C0802b parse(InputStream inputStream);

    public C0802b parse(String str) {
        try {
            return parse(new ByteArrayInputStream(str.getBytes()));
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    public C0802b parse(URL url, boolean z) {
        String host = url.getHost();
        int port = url.getPort();
        int i = port == -1 ? 80 : port;
        String path = url.getPath();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Length", "0");
            if (host != null) {
                httpURLConnection.setRequestProperty("HOST", host);
            }
            InputStream inputStream = httpURLConnection.getInputStream();
            C0802b parse = parse(inputStream);
            inputStream.close();
            httpURLConnection.disconnect();
            return parse;
        } catch (Exception e) {
            C0953e c0953e = new C0953e();
            c0953e.mo2100m("GET");
            c0953e.m450o(path);
            C0954g a = c0953e.m440a(host, i, z);
            if (a.m453B()) {
                return parse(new ByteArrayInputStream(new String(a.m111h()).getBytes()));
            }
            throw new ParserException("HTTP comunication failed: no answer from peer.Unable to retrive resoure -> " + url.toString());
        }
    }
}
