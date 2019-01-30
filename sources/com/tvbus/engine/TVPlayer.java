package com.tvbus.engine;

import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TVPlayer {
    public static String submitPostData(String strUrlPath, String username, String password, int timeout) {
        byte[] data = ("{\"from\": \"sc\",\"username\": \"" + username + "\",\"password\": \"" + password + "\"}").getBytes();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strUrlPath).openConnection();
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", RequestParams.APPLICATION_JSON);
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            httpURLConnection.getOutputStream().write(data);
            if (httpURLConnection.getResponseCode() == 200) {
                return dealResponseResult(httpURLConnection.getInputStream());
            }
            return "-1";
        } catch (IOException e) {
            return "err: " + e.getMessage().toString();
        }
    }

    public static String dealResponseResult(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        while (true) {
            try {
                int len = inputStream.read(data);
                if (len == -1) {
                    break;
                }
                byteArrayOutputStream.write(data, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new String(byteArrayOutputStream.toByteArray());
    }
}
