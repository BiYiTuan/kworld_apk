package com.gemini.play;

import cz.msebera.android.httpclient.protocol.HttpRequestExecutor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Ghttp {
    private static int hls_tag_size = 20;
    private static int mSeq = 1;
    private static boolean playing = false;

    public static String play(String url) {
        mSeq = 0;
        createPlaylist();
        MGplayer.MyPrintln("playlist url = " + url);
        final String http_url_playlist = url.replace("ghttp://", "http://");
        playing = true;
        new Thread() {
            public void run() {
                Ghttp.runGetPlaylist(http_url_playlist);
            }
        }.start();
        String murl = "http://127.0.0.1:" + MGplayer.http_server_port + "/playlist.m3u8";
        for (int ii = 0; ii < 10000; ii++) {
            if (mSeq >= 1) {
                return "http://127.0.0.1:" + MGplayer.http_server_port + "/playlist.m3u8";
            }
            MGplayer.sleep(1);
            if (!playing) {
                return murl;
            }
        }
        return murl;
    }

    private static void runGetPlaylist(String url) {
        ArrayList urls = new ArrayList();
        String playlists_start = "";
        int playlists_num = 0;
        int index_length = 10;
        while (playing) {
            int ii;
            String playlist = sendServerCmd(url + "/list.dat", 2000);
            if (playlist != null && playlist.length() >= 9) {
                playlist = MGplayer.ju(playlist);
                String[] playlists = playlist.trim().split("#@#");
                if (playlists.length > 0) {
                    index_length++;
                    if (index_length > playlists.length) {
                        index_length = playlists.length;
                    }
                    urls.clear();
                    for (ii = 0; ii < index_length; ii++) {
                        if (playlists[ii].length() >= 9) {
                            urls.add(url + "/hls" + playlists[ii].substring(0, 9) + ".ts");
                        }
                    }
                    if (playlists.length > 0 && !(playlists_start.equals(playlists[0]) && playlists_num == playlists.length)) {
                        playlists_start = playlists[0];
                        playlists_num = playlists.length;
                        mSeq++;
                        createPlaylist(urls);
                    }
                }
            }
            MGplayer.MyPrintln("playlist ghttp = " + playlist);
            ii = 0;
            while (ii < HttpRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE) {
                MGplayer.sleep(1);
                if (playing) {
                    ii++;
                } else {
                    return;
                }
            }
        }
    }

    private static String createPlaylist(String text) {
        try {
            FileOutputStream outStream = MGplayer._this.openFileOutput("playlist.m3u8", 1);
            try {
                outStream.write(text.getBytes());
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        return MGplayer._this.getFilesDir() + "/playlist.m3u8";
    }

    private static void createPlaylist() {
        MGplayer.Ghttp_playlist_text = "#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-ALLOW-CACHE:NO\n#EXT-X-TARGETDURATION:10\n#EXT-X-MEDIA-SEQUENCE:" + mSeq + "\n";
    }

    private static void createPlaylist(ArrayList<String> urls) {
        MGplayer.Ghttp_playlist_text = "#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-ALLOW-CACHE:NO\n#EXT-X-TARGETDURATION:10\n#EXT-X-MEDIA-SEQUENCE:" + mSeq + "\n";
        for (int ii = 0; ii < urls.size(); ii++) {
            MGplayer.Ghttp_playlist_text += "#EXTINF:10,\n" + ((String) urls.get(ii)).trim() + "\n";
        }
        MGplayer.MyPrintln("playlist ghttp = " + MGplayer.Ghttp_playlist_text);
    }

    public static String sendServerCmd(String httpUrl, int timeout) {
        String resultData = "";
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        MGplayer.MyPrintln("sendServerCmd Url:" + httpUrl);
        if (url != null) {
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(timeout);
                urlConn.setReadTimeout(timeout);
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                while (true) {
                    String inputLine = buffer.readLine();
                    if (inputLine == null) {
                        in.close();
                        urlConn.disconnect();
                        MGplayer.MyPrintln("sendServerCmd result :" + resultData);
                        return resultData;
                    } else if (!playing) {
                        return null;
                    } else {
                        resultData = resultData + inputLine + "\n";
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return resultData;
    }

    public static void stop() {
        playing = false;
    }
}
