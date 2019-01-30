package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.gemini.custom.lookiptv;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.ExoPlayer.Factory;
import com.google.android.exoplayer.hls.HlsChunkSource;
import cz.msebera.android.httpclient.HttpStatus;
import io.vov.vitamio.provider.MediaStore.Audio.AudioColumns;
import io.vov.vitamio.widget.VideoView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.lingala.zip4j.util.InternalZipConstants;
import org.videolan.vlc.VlcVideoView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class VODplayer {
    public static VideoView VideoViewH = null;
    public static ExoPlayerView VideoViewH2 = null;
    public static VlcVideoView VideoViewS = null;
    public static VideoView VideoViewS2 = null;
    public static ArrayList<VodListStatus> VodListArray0 = new ArrayList();
    public static ArrayList<VodListStatus> VodListArray1 = new ArrayList();
    public static ArrayList<VodListStatus> VodListArray2 = new ArrayList();
    public static ArrayList<VodListStatus> VodListArray3 = new ArrayList();
    private static ArrayList<HashMap<String, Object>> VodListImageListArray = new ArrayList();
    public static ArrayList<VodListUrlListStatus> VodListUrlListArray = new ArrayList();
    public static VodTypeStatus VodType0 = new VodTypeStatus();
    public static VodTypeStatus VodType1 = new VodTypeStatus();
    public static VodTypeStatus VodType2 = new VodTypeStatus();
    public static VodTypeStatus VodType3 = new VodTypeStatus();
    private static boolean check_playing_running = false;
    private static int check_playing_times = 0;
    public static ColumnStatus[] columner = null;
    public static int[] columner_needps = null;
    public static String findtype = "&findtype=0";
    public static boolean is_reload = false;
    public static ArrayList<HashMap<String, Object>> list = new ArrayList();
    public static LruBitmapCache listbitmapCache = new LruBitmapCache();
    public static boolean mIsPause = false;
    public static int p2pCurrentDuration = 0;
    public static String p2pCurrentUrl = null;
    public static int page = 0;
    public static int psize = 10;
    public static int ptotal = 0;
    static Handler seekHandler = new C05711();
    public static boolean show_all_type = true;
    public static String type = "0";
    public static String url_param = null;
    public static int video_current = 0;
    public static String video_pw = null;
    public static int video_seek = 0;
    public static String video_selections = null;
    public static String video_url = null;
    public static String vod_epg = null;
    private static boolean vod_inited = false;
    public static int[] vod_number = new int[]{0, 0, 0, 0};

    /* renamed from: com.gemini.play.VODplayer$1 */
    static class C05711 extends Handler {
        C05711() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    VODplayer.seekVideo(VODplayer.video_url, VODplayer.video_current);
                    break;
                case 1:
                    VODplayer.seekVideo(VODplayer.video_url, VODplayer.video_current);
                    break;
                case 2:
                    VODplayer.seekVideo(VODplayer.video_url, VODplayer.video_current);
                    break;
            }
            VODplayer.video_current = 0;
        }
    }

    static /* synthetic */ int access$408() {
        int i = check_playing_times;
        check_playing_times = i + 1;
        return i;
    }

    public static String gete() {
        if (vod_epg == null && MGplayer.tv != null) {
            vod_epg = MGplayer.tv.gete();
        }
        return vod_epg;
    }

    public static void listClear() {
        list.clear();
        VodListArray0.clear();
        VodListArray1.clear();
        VodListArray2.clear();
        VodListArray3.clear();
    }

    public static ArrayList<VodListStatus> listGet(int index) {
        if (index == 0) {
            return VodListArray0;
        }
        if (index == 1) {
            return VodListArray1;
        }
        if (index == 2) {
            return VodListArray2;
        }
        if (index == 3) {
            return VodListArray3;
        }
        return null;
    }

    public static ArrayList<VodListStatus> listGet(String type) {
        if (!MGplayer.isNumeric(type)) {
            return null;
        }
        int index = Integer.parseInt(type);
        if (index == 0) {
            return VodListArray0;
        }
        if (index == 1) {
            return VodListArray1;
        }
        if (index == 2) {
            return VodListArray2;
        }
        if (index == 3) {
            return VodListArray3;
        }
        return null;
    }

    public static VodTypeStatus typeGet(int index) {
        if (index == 0) {
            return VodType0;
        }
        if (index == 1) {
            return VodType1;
        }
        if (index == 2) {
            return VodType2;
        }
        if (index == 3) {
            return VodType3;
        }
        return null;
    }

    public static VodListStatus parseCollectXML(String info_type, int vodid) {
        MainVodDB mainVodDB = new MainVodDB(MGplayer._this);
        VodListStatus s = new VodListStatus();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            String collecturl = gete() + "/vod_collect.php?type=" + info_type + "&id=" + vodid + MGplayer.get_key_value();
            MGplayer.MyPrintln("url:" + collecturl);
            URLConnection conn = new URL(collecturl).openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            conn.connect();
            Document doc = factory.newDocumentBuilder().parse(conn.getInputStream());
            NodeList nl = doc.getElementsByTagName("item");
            for (int i = 0; i < nl.getLength(); i++) {
                String id = null;
                if (doc.getElementsByTagName(TtmlNode.ATTR_ID).item(i) != null) {
                    id = doc.getElementsByTagName(TtmlNode.ATTR_ID).item(i).getFirstChild().getNodeValue();
                }
                if (id != null && MGplayer.isNumeric(id)) {
                    s.id = Integer.parseInt(id);
                    Node name = doc.getElementsByTagName("name").item(i).getFirstChild();
                    if (name != null) {
                        s.name = name.getNodeValue();
                    }
                    Node image = doc.getElementsByTagName("image").item(i).getFirstChild();
                    if (image != null) {
                        s.image = image.getNodeValue();
                        if (s.image.startsWith("http://")) {
                            s.imagebit = MGplayer.getHttpBitmap(s.image);
                        }
                    }
                    Node url = doc.getElementsByTagName("url").item(i).getFirstChild();
                    if (url != null) {
                        s.url = url.getNodeValue();
                    }
                    Node area = doc.getElementsByTagName("area").item(i).getFirstChild();
                    if (area != null) {
                        s.area = area.getNodeValue();
                    }
                    Node year = doc.getElementsByTagName(AudioColumns.YEAR).item(i).getFirstChild();
                    if (year != null) {
                        s.year = year.getNodeValue();
                    }
                    Node xtype = doc.getElementsByTagName("type").item(i).getFirstChild();
                    if (xtype != null) {
                        s.type = xtype.getNodeValue();
                    }
                    Node intro1 = doc.getElementsByTagName("intro1").item(i).getFirstChild();
                    if (intro1 != null) {
                        s.intro1 = intro1.getNodeValue();
                    }
                    Node intro2 = doc.getElementsByTagName("intro2").item(i).getFirstChild();
                    if (intro2 != null) {
                        s.intro2 = intro2.getNodeValue();
                    }
                    Node intro3 = doc.getElementsByTagName("intro3").item(i).getFirstChild();
                    if (intro3 != null) {
                        s.intro3 = intro3.getNodeValue();
                    }
                    Node intro4 = doc.getElementsByTagName("intro4").item(i).getFirstChild();
                    if (intro4 != null) {
                        s.intro4 = intro4.getNodeValue();
                    }
                    if (doc.getElementsByTagName("clickrate").item(i).getFirstChild() != null) {
                        String clickratev = doc.getElementsByTagName("clickrate").item(i).getFirstChild().getNodeValue();
                        if (MGplayer.isNumeric(clickratev)) {
                            s.clickrate = Integer.parseInt(clickratev);
                        } else {
                            s.clickrate = 0;
                        }
                    } else {
                        s.clickrate = 0;
                    }
                    if (doc.getElementsByTagName("recommend").item(i).getFirstChild() != null) {
                        String recommendv = doc.getElementsByTagName("recommend").item(i).getFirstChild().getNodeValue();
                        if (MGplayer.isNumeric(recommendv)) {
                            s.recommend = Integer.parseInt(recommendv);
                        } else {
                            s.recommend = 0;
                        }
                    } else {
                        s.recommend = 0;
                    }
                    if (doc.getElementsByTagName("chage").item(i).getFirstChild() != null) {
                        String chagev = doc.getElementsByTagName("chage").item(i).getFirstChild().getNodeValue();
                        if (MGplayer.isNumeric(chagev)) {
                            s.chage = (float) Integer.parseInt(chagev);
                        } else {
                            s.chage = 0.0f;
                        }
                    } else {
                        s.chage = 0.0f;
                    }
                    if (doc.getElementsByTagName("updatetime").item(i).getFirstChild() != null) {
                        String updatetimev = doc.getElementsByTagName("updatetime").item(i).getFirstChild().getNodeValue();
                        if (MGplayer.isNumeric(updatetimev)) {
                            s.updatetime = Integer.parseInt(updatetimev);
                        } else {
                            s.updatetime = 0;
                        }
                    } else {
                        s.updatetime = 0;
                    }
                    if (MGplayer.isNumeric(info_type)) {
                        mainVodDB.insert(s, s.imagebit, Integer.parseInt(info_type));
                    }
                }
            }
            return s;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ParserConfigurationException e3) {
            e3.printStackTrace();
        }
        return null;
    }

    public static ArrayList<VodListStatus> parseMainDB() {
        VodListArray0.clear();
        ArrayList<VodListStatus> ss = new MainVodDB(MGplayer._this).parseAll();
        VodListArray0 = ss;
        return ss;
    }

    public static ArrayList<VodListStatus> parseMainXML() {
        ArrayList<VodListStatus> VodListArray = new ArrayList();
        MainVodDB mainVodDB = new MainVodDB(MGplayer._this);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            MGplayer.MyPrintln("vod_main:" + gete() + "/vod_main.php?type=" + type + MGplayer.get_key_value());
            URLConnection conn = new URL(gete() + "/vod_main.php?type=" + type + MGplayer.get_key_value()).openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            conn.connect();
            InputStream is = conn.getInputStream();
            if (is == null) {
                return null;
            }
            Document doc = factory.newDocumentBuilder().parse(is);
            if (doc == null) {
                return null;
            }
            NodeList nl = doc.getElementsByTagName("item");
            if (nl == null) {
                return null;
            }
            int ii;
            for (int i = 0; i < nl.getLength(); i++) {
                String id = null;
                if (doc.getElementsByTagName(TtmlNode.ATTR_ID).item(i) != null) {
                    id = doc.getElementsByTagName(TtmlNode.ATTR_ID).item(i).getFirstChild().getNodeValue();
                }
                if (id != null && MGplayer.isNumeric(id)) {
                    VodListStatus s = new VodListStatus();
                    s.id = Integer.parseInt(id);
                    Node name = doc.getElementsByTagName("name").item(i).getFirstChild();
                    if (name != null) {
                        s.name = name.getNodeValue();
                    }
                    Node image = doc.getElementsByTagName("image").item(i).getFirstChild();
                    if (image != null) {
                        s.image = image.getNodeValue();
                        if (!s.image.startsWith("http://")) {
                            for (ii = 0; ii < 10; ii++) {
                                s.imagebit = MGplayer.getHttpBitmap(gete() + "/images/vodpic/" + s.image, 10000);
                                if (s.imagebit != null) {
                                    break;
                                }
                            }
                        } else {
                            for (ii = 0; ii < 10; ii++) {
                                s.imagebit = MGplayer.getHttpBitmap(s.image, 10000);
                                if (s.imagebit != null) {
                                    break;
                                }
                            }
                        }
                    }
                    Node url = doc.getElementsByTagName("url").item(i).getFirstChild();
                    if (url != null) {
                        s.url = url.getNodeValue();
                    }
                    Node area = doc.getElementsByTagName("area").item(i).getFirstChild();
                    if (area != null) {
                        s.area = area.getNodeValue();
                    }
                    Node year = doc.getElementsByTagName(AudioColumns.YEAR).item(i).getFirstChild();
                    if (year != null) {
                        s.year = year.getNodeValue();
                    }
                    Node xtype = doc.getElementsByTagName("type").item(i).getFirstChild();
                    if (xtype != null) {
                        s.type = xtype.getNodeValue();
                    }
                    Node intro1 = doc.getElementsByTagName("intro1").item(i).getFirstChild();
                    if (intro1 != null) {
                        s.intro1 = intro1.getNodeValue();
                    }
                    Node intro2 = doc.getElementsByTagName("intro2").item(i).getFirstChild();
                    if (intro2 != null) {
                        s.intro2 = intro2.getNodeValue();
                    }
                    Node intro3 = doc.getElementsByTagName("intro3").item(i).getFirstChild();
                    if (intro3 != null) {
                        s.intro3 = intro3.getNodeValue();
                    }
                    Node intro4 = doc.getElementsByTagName("intro4").item(i).getFirstChild();
                    if (intro4 != null) {
                        s.intro4 = intro4.getNodeValue();
                    }
                    if (doc.getElementsByTagName("clickrate").item(i).getFirstChild() != null) {
                        String clickratev = doc.getElementsByTagName("clickrate").item(i).getFirstChild().getNodeValue();
                        if (MGplayer.isNumeric(clickratev)) {
                            s.clickrate = Integer.parseInt(clickratev);
                        } else {
                            s.clickrate = 0;
                        }
                    } else {
                        s.clickrate = 0;
                    }
                    if (doc.getElementsByTagName("recommend").item(i).getFirstChild() != null) {
                        String recommendv = doc.getElementsByTagName("recommend").item(i).getFirstChild().getNodeValue();
                        if (MGplayer.isNumeric(recommendv)) {
                            s.recommend = Integer.parseInt(recommendv);
                        } else {
                            s.recommend = 0;
                        }
                    } else {
                        s.recommend = 0;
                    }
                    if (doc.getElementsByTagName("chage").item(i).getFirstChild() != null) {
                        String chagev = doc.getElementsByTagName("chage").item(i).getFirstChild().getNodeValue();
                        if (MGplayer.isNumeric(chagev)) {
                            s.chage = (float) Integer.parseInt(chagev);
                        } else {
                            s.chage = 0.0f;
                        }
                    } else {
                        s.chage = 0.0f;
                    }
                    if (doc.getElementsByTagName("updatetime").item(i).getFirstChild() != null) {
                        String updatetimev = doc.getElementsByTagName("updatetime").item(i).getFirstChild().getNodeValue();
                        if (MGplayer.isNumeric(updatetimev)) {
                            s.updatetime = Integer.parseInt(updatetimev);
                        } else {
                            s.updatetime = 0;
                        }
                    } else {
                        s.updatetime = 0;
                    }
                    if (s.id >= 0 && s.name.length() > 0 && s.url.length() > 7) {
                        VodListArray.add(s);
                        VodListArray0.add(s);
                    }
                }
            }
            if (VodListArray.size() < 5) {
                return VodListArray;
            }
            mainVodDB.clear();
            for (ii = 0; ii < VodListArray.size(); ii++) {
                mainVodDB.insert((VodListStatus) VodListArray.get(ii), ((VodListStatus) VodListArray.get(ii)).imagebit, 0);
            }
            return VodListArray;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ParserConfigurationException e3) {
            e3.printStackTrace();
        }
        return null;
    }

    public static void parseTypeMem(String type) {
        if (columner == null) {
            return;
        }
        if (columner[0].name != null || columner[0].type_type != null || columner[0].type_year != null || columner[0].type_area != null) {
            VodTypeStatus s = new VodTypeStatus();
            MGplayer.MyPrintln("GET VOD TYPE FOR MEN");
            if (columner[0].type_type != null) {
                s.types = columner[0].type_type.split("\\|");
            }
            if (columner[0].type_year != null) {
                s.years = columner[0].type_year.split("\\|");
            }
            if (columner[0].type_area != null) {
                s.areas = columner[0].type_area.split("\\|");
            }
            if (null == null) {
                VodType0 = s;
            }
            if (0 == 1) {
                VodType1 = s;
            }
            if (0 == 2) {
                VodType2 = s;
            }
            if (0 == 3) {
                VodType3 = s;
            }
        }
    }

    public static void parseTypeXML(String type) {
        if (type != null && MGplayer.isNumeric(type)) {
            int index = Integer.parseInt(type);
            if (columner == null || index <= columner.length - 1) {
                VodTypeStatus s = new VodTypeStatus();
                if (columner == null || (columner[index].name == null && columner[index].type_type == null && columner[index].type_year == null && columner[index].type_area == null)) {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    try {
                        URLConnection conn = new URL(gete() + "/vod_type_xml.php?type=" + type + MGplayer.get_key_value()).openConnection();
                        conn.setConnectTimeout(2000);
                        conn.setReadTimeout(2000);
                        conn.connect();
                        Document doc = factory.newDocumentBuilder().parse(conn.getInputStream());
                        NodeList nl = doc.getElementsByTagName("item");
                        for (int i = 0; i < nl.getLength(); i++) {
                            if (doc.getElementsByTagName("type").item(i) != null) {
                                Node xtype = doc.getElementsByTagName("type").item(i).getFirstChild();
                                if (xtype != null) {
                                    s.types = xtype.getNodeValue().split("\\|");
                                }
                            }
                            if (doc.getElementsByTagName(AudioColumns.YEAR).item(i) != null) {
                                Node year = doc.getElementsByTagName(AudioColumns.YEAR).item(i).getFirstChild();
                                if (year != null) {
                                    s.years = year.getNodeValue().split("\\|");
                                }
                            }
                            if (doc.getElementsByTagName("area").item(i) != null) {
                                Node area = doc.getElementsByTagName("area").item(i).getFirstChild();
                                if (area != null) {
                                    s.areas = area.getNodeValue().split("\\|");
                                }
                            }
                        }
                        if (index == 0) {
                            VodType0 = s;
                        }
                        if (index == 1) {
                            VodType1 = s;
                        }
                        if (index == 2) {
                            VodType2 = s;
                        }
                        if (index == 3) {
                            VodType3 = s;
                            return;
                        }
                        return;
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                        return;
                    } catch (SAXException e2) {
                        e2.printStackTrace();
                        return;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return;
                    }
                }
                MGplayer.MyPrintln("GET VOD TYPE FOR MEN");
                if (columner[index].type_type != null) {
                    s.types = columner[index].type_type.split("\\|");
                }
                if (columner[index].type_year != null) {
                    s.years = columner[index].type_year.split("\\|");
                }
                if (columner[index].type_area != null) {
                    s.areas = columner[index].type_area.split("\\|");
                }
                if (index == 0) {
                    VodType0 = s;
                }
                if (index == 1) {
                    VodType1 = s;
                }
                if (index == 2) {
                    VodType2 = s;
                }
                if (index == 3) {
                    VodType3 = s;
                }
            }
        }
    }

    public static ArrayList<VodListStatus> parseXML(String type) {
        return parseXML(type, null);
    }

    public static ArrayList<VodListStatus> parseGzipXML(String type, String gurl) {
        String purl;
        if (gurl == null) {
            purl = gete() + "/vod_gzip.php?type=" + type + "&page=" + page;
        } else {
            purl = gurl;
        }
        String xml = MGplayer.sendServerCmd(purl, Factory.DEFAULT_MIN_REBUFFER_MS);
        if (xml != null) {
            MGplayer.MyPrintln(MGplayer.uncompress(xml));
        }
        return null;
    }

    public static ArrayList<VodListStatus> parseXML(String type, String gurl) {
        int index;
        if (MGplayer.isNumeric(type)) {
            index = Integer.parseInt(type);
            if (index > 3) {
                return null;
            }
        } else if (!type.equals("find") && !type.equals("hot") && !type.equals("collect")) {
            return null;
        } else {
            index = 0;
        }
        ArrayList<VodListStatus> VodListArray = new ArrayList();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        if (gurl == null) {
            try {
                String purl = gete() + "/vod_xml.php?type=" + type + "&page=" + page + MGplayer.get_key_value();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return null;
            } catch (SAXException e2) {
                e2.printStackTrace();
                return null;
            } catch (IOException e3) {
                e3.printStackTrace();
                return null;
            }
        }
        purl = gurl;
        if (!purl.startsWith("http://") && !purl.startsWith("https://")) {
            return null;
        }
        MGplayer.MyPrintln(purl);
        HttpURLConnection conn = (HttpURLConnection) new URL(purl).openConnection();
        conn.setConnectTimeout(Factory.DEFAULT_MIN_REBUFFER_MS);
        conn.setReadTimeout(Factory.DEFAULT_MIN_REBUFFER_MS);
        conn.connect();
        for (int ii = 0; ii < 5; ii++) {
            MGplayer.MyPrintln("connect = " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                break;
            }
            conn.connect();
        }
        if (conn.getResponseCode() != 200) {
            return null;
        }
        InputStream is = conn.getInputStream();
        if (is == null) {
            return null;
        }
        Document doc = factory.newDocumentBuilder().parse(is);
        if (doc == null) {
            return null;
        }
        NodeList nl = doc.getElementsByTagName("item");
        if (nl == null) {
            return null;
        }
        int i = 0;
        while (i < nl.getLength()) {
            if (doc == null || doc.getElementsByTagName(TtmlNode.ATTR_ID) == null || doc.getElementsByTagName(TtmlNode.ATTR_ID).item(i) == null || doc.getElementsByTagName(TtmlNode.ATTR_ID).item(i).getFirstChild().getNodeValue() == null) {
                return null;
            }
            String id = doc.getElementsByTagName(TtmlNode.ATTR_ID).item(i).getFirstChild().getNodeValue();
            if (MGplayer.isNumeric(id)) {
                VodListStatus s = new VodListStatus();
                s.id = Integer.parseInt(id);
                Node name = doc.getElementsByTagName("name").item(i).getFirstChild();
                if (name != null) {
                    s.name = name.getNodeValue();
                }
                Node image = doc.getElementsByTagName("image").item(i).getFirstChild();
                if (image != null) {
                    s.image = image.getNodeValue();
                }
                Node url = doc.getElementsByTagName("url").item(i).getFirstChild();
                if (url != null) {
                    s.url = url.getNodeValue();
                }
                Node area = doc.getElementsByTagName("area").item(i).getFirstChild();
                if (area != null) {
                    s.area = area.getNodeValue();
                }
                Node year = doc.getElementsByTagName(AudioColumns.YEAR).item(i).getFirstChild();
                if (year != null) {
                    s.year = year.getNodeValue();
                }
                Node xtype = doc.getElementsByTagName("type").item(i).getFirstChild();
                if (xtype != null) {
                    s.type = xtype.getNodeValue();
                }
                Node intro1 = doc.getElementsByTagName("intro1").item(i).getFirstChild();
                if (intro1 != null) {
                    s.intro1 = intro1.getNodeValue();
                }
                Node intro2 = doc.getElementsByTagName("intro2").item(i).getFirstChild();
                if (intro2 != null) {
                    s.intro2 = intro2.getNodeValue();
                }
                Node intro3 = doc.getElementsByTagName("intro3").item(i).getFirstChild();
                if (intro3 != null) {
                    s.intro3 = intro3.getNodeValue();
                }
                Node intro4 = doc.getElementsByTagName("intro4").item(i).getFirstChild();
                if (intro4 != null) {
                    s.intro4 = intro4.getNodeValue();
                }
                if (doc.getElementsByTagName("clickrate").item(i).getFirstChild() != null) {
                    String clickratev = doc.getElementsByTagName("clickrate").item(i).getFirstChild().getNodeValue();
                    if (MGplayer.isNumeric(clickratev)) {
                        s.clickrate = Integer.parseInt(clickratev);
                    } else {
                        s.clickrate = 0;
                    }
                } else {
                    s.clickrate = 0;
                }
                if (doc.getElementsByTagName("recommend").item(i).getFirstChild() != null) {
                    String recommendv = doc.getElementsByTagName("recommend").item(i).getFirstChild().getNodeValue();
                    if (MGplayer.isNumeric(recommendv)) {
                        s.recommend = Integer.parseInt(recommendv);
                    } else {
                        s.recommend = 0;
                    }
                } else {
                    s.recommend = 0;
                }
                if (doc.getElementsByTagName("chage").item(i).getFirstChild() != null) {
                    String chagev = doc.getElementsByTagName("chage").item(i).getFirstChild().getNodeValue();
                    if (MGplayer.isNumeric(chagev)) {
                        s.chage = (float) Integer.parseInt(chagev);
                    } else {
                        s.chage = 0.0f;
                    }
                } else {
                    s.chage = 0.0f;
                }
                if (doc.getElementsByTagName("updatetime").item(i).getFirstChild() != null) {
                    String updatetimev = doc.getElementsByTagName("updatetime").item(i).getFirstChild().getNodeValue();
                    if (MGplayer.isNumeric(updatetimev)) {
                        s.updatetime = Integer.parseInt(updatetimev);
                    } else {
                        s.updatetime = 0;
                    }
                } else {
                    s.updatetime = 0;
                }
                if (!(doc.getElementsByTagName("total") == null || doc.getElementsByTagName("total").item(i) == null)) {
                    if (doc.getElementsByTagName("total").item(i).getFirstChild() != null) {
                        String totalev = doc.getElementsByTagName("total").item(i).getFirstChild().getNodeValue();
                        if (MGplayer.isNumeric(totalev)) {
                            s.total = Integer.parseInt(totalev);
                        } else {
                            s.total = 0;
                        }
                    } else {
                        s.total = 0;
                    }
                }
                if (!(doc.getElementsByTagName("findtype") == null || doc.getElementsByTagName("findtype").item(i) == null)) {
                    if (doc.getElementsByTagName("findtype").item(i).getFirstChild() != null) {
                        String findtypeev = doc.getElementsByTagName("findtype").item(i).getFirstChild().getNodeValue();
                        if (!MGplayer.isNumeric(findtypeev)) {
                            s.findtype = -1;
                        } else if (Integer.parseInt(findtypeev) >= 0) {
                            s.findtype = Integer.parseInt(findtypeev);
                        } else {
                            s.findtype = -1;
                        }
                    } else {
                        s.findtype = -1;
                    }
                }
                VodListArray.add(s);
                if (index == 0) {
                    VodListArray0.add(s);
                }
                if (index == 1) {
                    VodListArray1.add(s);
                }
                if (index == 2) {
                    VodListArray2.add(s);
                }
                if (index == 3) {
                    VodListArray3.add(s);
                }
            }
            i++;
        }
        return VodListArray;
    }

    public static String secondToData(long seconds) {
        Calendar calendar = Calendar.getInstance();
        if (seconds > 0) {
            calendar.setTime(new Date(1000 * seconds));
        }
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        int hour = calendar.get(11);
        return year + "-" + month + "-" + day + " " + hour + ":" + calendar.get(12);
    }

    public static String secondToData2(long seconds) {
        Calendar calendar = Calendar.getInstance();
        if (seconds > 0) {
            calendar.setTime(new Date(1000 * seconds));
        }
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        return year + "-" + month + "-" + day;
    }

    public static VodListStatus getVodListStatus(String id, String type) {
        if (!MGplayer.isNumeric(type) || !MGplayer.isNumeric(id) || Integer.parseInt(type) > 3) {
            return null;
        }
        int index = Integer.parseInt(type);
        VodListStatus ss = null;
        for (int ii = 0; ii < listGet(index).size(); ii++) {
            VodListStatus t = (VodListStatus) listGet(index).get(ii);
            if (t.id == Integer.parseInt(id)) {
                ss = t;
            }
        }
        return ss;
    }

    public static void playVideo(Context _this, String url, String pw) {
        playVideo(_this, url, 0, pw);
    }

    public static void playVideo(int inx, Context _this, String url, int seek, String pw) {
        if (inx == 0) {
            VideoViewH.setVisibility(0);
            VideoViewH2.setVisibility(8);
            VideoViewS.setVisibility(8);
            VideoViewS2.setVisibility(8);
            playVideoForHard(_this, VideoViewH, url, seek, pw);
        } else if (inx == 3) {
            VideoViewH.setVisibility(8);
            VideoViewH2.setVisibility(0);
            VideoViewS.setVisibility(8);
            VideoViewS2.setVisibility(8);
            playVideoForHard2(_this, VideoViewH2, url, seek, pw);
        } else if (inx == 1) {
            VideoViewH.setVisibility(8);
            VideoViewH2.setVisibility(8);
            VideoViewS.setVisibility(0);
            VideoViewS2.setVisibility(8);
            playVideoForSoft(_this, VideoViewS, url, seek, pw);
        } else if (inx == 2) {
            VideoViewH.setVisibility(8);
            VideoViewH2.setVisibility(8);
            VideoViewS.setVisibility(8);
            VideoViewS2.setVisibility(0);
            playVideoForSoft2(_this, VideoViewS2, url, seek, pw);
        }
        video_seek = seek;
    }

    public static void playVideo(Context _this, String url, int seek, String pw) {
        int inx = selectDecode(url, pw);
        MGplayer.MyPrintln("vod inx = " + inx);
        playVideo(inx, _this, url, seek, pw);
    }

    public static int forward(String url, Handler rHandler) {
        if (video_current == 0) {
            video_current = getProgress(url);
        }
        int total = getTotal(url);
        MGplayer.MyPrintln("vod info view key down :" + video_current);
        Message msg;
        if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://"))) {
            video_current += 10000;
            if (video_current > total) {
                video_current = total;
            }
            MGplayer.MyPrintln("vod info view key down :" + video_current);
            msg = new Message();
            msg.what = 0;
            if (seekHandler.hasMessages(0)) {
                seekHandler.removeMessages(0);
            }
            seekHandler.sendMessageDelayed(msg, 1000);
        } else if (url != null && (url.startsWith("p2p://") || url.startsWith("forcetv://"))) {
            video_current += 10000;
            if (video_current > total) {
                video_current = total;
            }
            msg = new Message();
            msg.what = 1;
            if (seekHandler.hasMessages(1)) {
                seekHandler.removeMessages(1);
            }
            seekHandler.sendMessageDelayed(msg, 1000);
        } else if (url != null && url.startsWith("gp2p://")) {
            video_current += 10000;
            if (video_current > total) {
                video_current = total;
            }
            msg = new Message();
            Bundle data = new Bundle();
            data.putInt("currentTime", video_current);
            msg.setData(data);
            msg.what = 1;
            if (rHandler.hasMessages(1)) {
                rHandler.removeMessages(1);
            }
            rHandler.sendMessageDelayed(msg, 1000);
        }
        return video_current;
    }

    public static int backward(String url, Handler rHandler) {
        if (video_current == 0) {
            video_current = getProgress(url);
        }
        MGplayer.MyPrintln("vod info view key down :" + video_current);
        Message msg;
        if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://"))) {
            video_current -= 10000;
            if (video_current < 0) {
                video_current = 0;
            }
            MGplayer.MyPrintln("vod info view key down :" + video_current);
            msg = new Message();
            msg.what = 0;
            if (seekHandler.hasMessages(0)) {
                seekHandler.removeMessages(0);
            }
            seekHandler.sendMessageDelayed(msg, 1000);
        } else if (url != null && (url.startsWith("p2p://") || url.startsWith("forcetv://"))) {
            video_current -= 10000;
            if (video_current < 0) {
                video_current = 0;
            }
            msg = new Message();
            msg.what = 1;
            if (seekHandler.hasMessages(1)) {
                seekHandler.removeMessages(1);
            }
            seekHandler.sendMessageDelayed(msg, 1000);
        } else if (url != null && url.startsWith("gp2p://")) {
            video_current -= 10000;
            if (video_current < 0) {
                video_current = 0;
            }
            msg = new Message();
            Bundle data = new Bundle();
            data.putInt("currentTime", video_current);
            msg.setData(data);
            msg.what = 1;
            if (rHandler.hasMessages(1)) {
                rHandler.removeMessages(1);
            }
            rHandler.sendMessageDelayed(msg, 1000);
        }
        return video_current;
    }

    public static void seekVideo(String url, int seek) {
        if (MGplayer.getDecode() == 0) {
            seekVideoForHard(VideoViewH, url, seek);
        } else if (MGplayer.getDecode() == 1) {
            seekVideoForSoft(VideoViewS, url, seek);
        } else if (MGplayer.getDecode() == 2) {
            seekVideoForSoft2(VideoViewS2, url, seek);
        } else if (MGplayer.getDecode() == 3) {
            seekVideoForHard2(VideoViewH2, url, seek);
        }
    }

    public static void seekVideoForHard(VideoView mVideoView, String url, int seek) {
        MGplayer.MyPrintln("seek = " + seek);
        if ((url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://")) && seek > 0) {
            mVideoView.seekTo(seek);
        } else if (url.startsWith("p2p://") || url.startsWith("forcetv://")) {
            mVideoView.seekTo(seek);
        }
    }

    public static void seekVideoForHard2(ExoPlayerView mVideoView, String url, int seek) {
        MGplayer.MyPrintln("seek = " + seek);
        if ((url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://")) && seek > 0) {
            mVideoView.seekTo((long) seek);
        } else if (url.startsWith("p2p://") || url.startsWith("forcetv://")) {
            mVideoView.seekTo((long) seek);
        }
    }

    public static void seekVideoForSoft(VlcVideoView mVideoView, String url, int seek) {
        if ((url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://")) && seek > 0) {
            mVideoView.seekTo((long) seek);
        } else if (url.startsWith("p2p://") || url.startsWith("forcetv://")) {
            mVideoView.seekTo((long) seek);
        } else if (!url.startsWith("gp2p://")) {
        }
    }

    public static void seekVideoForSoft2(VideoView mVideoView, String url, int seek) {
        if ((url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://")) && seek > 0) {
            mVideoView.seekTo((long) seek);
        } else if (url.startsWith("p2p://") || url.startsWith("forcetv://")) {
            mVideoView.seekTo((long) seek);
        } else if (!url.startsWith("gp2p://")) {
        }
    }

    public static void playVideoForSoft(final Context _this, final VlcVideoView mVideoView, final String url, final int seek, String pw) {
        mVideoView.pause();
        MGplayer.mediaplayerunload();
        if (url != null) {
            playVideoMessage(_this, url.trim(), new Handler() {
                public void handleMessage(Message msg) {
                    String murl = null;
                    switch (msg.what) {
                        case 90:
                            murl = VODplayer.playVideo_p2p(_this, msg.getData().getString("url"), msg.getData().getString("pw"));
                            break;
                        case 91:
                        case 92:
                            murl = msg.getData().getString("url");
                            break;
                    }
                    MGplayer.MyPrintln("playVideoForSoft:" + murl);
                    if (murl != null) {
                        mVideoView.startPlay(murl);
                        if (!(url.startsWith("http://") && url.endsWith(".bsbt")) && ((url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://")) && seek > 0)) {
                            mVideoView.seekTo((long) seek);
                        }
                        mVideoView.start();
                        if (VODplayer.video_url.startsWith("gp2p://")) {
                            final String murl_tmp = murl;
                            final String currenturl_tmp = VODplayer.video_url;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (MGplayer.httpdstart == 1) {
                                        if (currenturl_tmp != null && currenturl_tmp.equals(VODplayer.video_url) && VODplayer.video_url.startsWith("gp2p://") && mVideoView != null && !mVideoView.isPlaying()) {
                                            mVideoView.startPlay(murl_tmp);
                                            mVideoView.start();
                                        }
                                    } else if (currenturl_tmp != null && currenturl_tmp.equals(VODplayer.video_url) && VODplayer.video_url.startsWith("gp2p://") && MGplayer.mediareceivecount() <= 0 && mVideoView != null && !mVideoView.isPlaying()) {
                                        mVideoView.startPlay(murl_tmp);
                                        mVideoView.start();
                                    }
                                }
                            }, 10000);
                        }
                    }
                }
            }, seek, pw);
        }
    }

    public static void playVideoForSoft2(final Context _this, final VideoView mVideoView, final String url, final int seek, String pw) {
        mVideoView.pause();
        MGplayer.mediaplayerunload();
        if (url != null) {
            playVideoMessage(_this, url.trim(), new Handler() {
                public void handleMessage(Message msg) {
                    String murl = null;
                    switch (msg.what) {
                        case 90:
                            murl = VODplayer.playVideo_p2p(_this, msg.getData().getString("url"), msg.getData().getString("pw"));
                            break;
                        case 91:
                        case 92:
                            murl = msg.getData().getString("url");
                            break;
                    }
                    if (murl != null) {
                        mVideoView.setVideoPath(murl);
                        if (!(url.startsWith("http://") && url.endsWith(".bsbt"))) {
                            if ((url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://")) && seek > 0) {
                                mVideoView.seekTo((long) seek);
                            } else if ((url.startsWith("p2p://") || url.startsWith("forcetv://")) && seek > 0) {
                                VODplayer.seekVideo(url, seek);
                            }
                        }
                        mVideoView.start();
                    }
                }
            }, seek, pw);
        }
    }

    public static void playVideoForHard(final Context _this, final VideoView mVideoView, final String url, final int seek, String pw) {
        mVideoView.pause();
        mVideoView.reset();
        MGplayer.mediaplayerunload();
        MGplayer.sleep(1);
        if (url != null) {
            playVideoMessage(_this, url.trim(), new Handler() {
                public void handleMessage(Message msg) {
                    String murl = null;
                    switch (msg.what) {
                        case 90:
                            murl = VODplayer.playVideo_p2p(_this, msg.getData().getString("url"), msg.getData().getString("pw"));
                            break;
                        case 91:
                        case 92:
                            murl = msg.getData().getString("url");
                            break;
                    }
                    if (murl != null) {
                        mVideoView.setVideoPath(murl);
                        if (!(url.startsWith("http://") && url.endsWith(".bsbt"))) {
                            if ((url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://")) && seek > 0) {
                                mVideoView.seekTo(seek);
                            } else if ((url.startsWith("p2p://") || url.startsWith("forcetv://")) && seek > 0) {
                                VODplayer.seekVideo(url, seek);
                            }
                        }
                        if (VODplayer.video_url.startsWith("gp2p://") && MGplayer.getDecode() == 0) {
                            final String murl_tmp = murl;
                            final String currenturl_tmp = VODplayer.video_url;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (MGplayer.httpdstart == 1) {
                                        if (currenturl_tmp != null && currenturl_tmp.equals(VODplayer.video_url) && VODplayer.video_url.startsWith("gp2p://") && mVideoView != null && !mVideoView.isPlaying()) {
                                            mVideoView.pause();
                                            mVideoView.setVideoPath(murl_tmp);
                                        }
                                    } else if (currenturl_tmp != null && currenturl_tmp.equals(VODplayer.video_url) && VODplayer.video_url.startsWith("gp2p://") && MGplayer.mediareceivecount() <= 0 && mVideoView != null && !mVideoView.isPlaying()) {
                                        mVideoView.pause();
                                        mVideoView.setVideoPath(murl_tmp);
                                    }
                                }
                            }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                        }
                    }
                }
            }, seek, pw);
        }
    }

    public static void playVideoForHard2(final Context _this, final ExoPlayerView mVideoView, final String url, final int seek, String pw) {
        mVideoView.pause();
        mVideoView.release();
        MGplayer.mediaplayerunload();
        if (url != null) {
            playVideoMessage(_this, url.trim(), new Handler() {
                public void handleMessage(Message msg) {
                    String murl = null;
                    switch (msg.what) {
                        case 90:
                            murl = VODplayer.playVideo_p2p(_this, msg.getData().getString("url"), msg.getData().getString("pw"));
                            break;
                        case 91:
                        case 92:
                            murl = msg.getData().getString("url");
                            break;
                    }
                    if (murl != null) {
                        mVideoView.setRendererContentType(2);
                        mVideoView.setVideoUri(Uri.parse(murl));
                        if ((url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://")) && seek > 0) {
                            mVideoView.seekTo((long) seek);
                        } else if ((url.startsWith("p2p://") || url.startsWith("forcetv://")) && seek > 0) {
                            VODplayer.seekVideo(url, seek);
                        }
                    }
                }
            }, seek, pw);
        }
    }

    public static void playVideoMessage(final Context _this, final String url, final Handler pHandler, final int seek, final String pw) {
        if (url != null) {
            if (!url.startsWith("http://") || !url.endsWith(".bsbt")) {
                if (url.startsWith("p2p://") || url.startsWith("forcetv://")) {
                    new Thread(new Runnable() {
                        public void run() {
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putString("url", url);
                            data.putString("pw", pw);
                            data.putString("ourl", url);
                            data.putInt("seek", seek);
                            msg.setData(data);
                            msg.what = 90;
                            if (pHandler.hasMessages(90)) {
                                pHandler.removeMessages(90);
                            }
                            pHandler.sendMessage(msg);
                        }
                    }).start();
                } else if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://")) {
                    new Thread(new Runnable() {
                        public void run() {
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putString("url", url);
                            data.putString("ourl", url);
                            data.putInt("seek", seek);
                            msg.setData(data);
                            msg.what = 91;
                            if (pHandler.hasMessages(91)) {
                                pHandler.removeMessages(91);
                            }
                            pHandler.sendMessage(msg);
                        }
                    }).start();
                } else if (url.startsWith("youku@")) {
                    final String gurl = url;
                    new Thread(new Runnable() {
                        public void run() {
                            String cmd = VODplayer.gete() + "/admin/youku_get.php?url=" + gurl.split("@")[1];
                            for (int ii = 0; ii < 5; ii++) {
                                String path = MGplayer.sendServerCmd(cmd, Factory.DEFAULT_MIN_REBUFFER_MS).trim();
                                if (path.length() > 16) {
                                    Message msg;
                                    Bundle data;
                                    if (url.indexOf("tudou") >= 7) {
                                        msg = new Message();
                                        data = new Bundle();
                                        data.putString("url", path);
                                        data.putString("ourl", url);
                                        data.putInt("seek", seek);
                                        msg.setData(data);
                                        msg.what = 91;
                                        if (pHandler.hasMessages(91)) {
                                            pHandler.removeMessages(91);
                                        }
                                        pHandler.sendMessage(msg);
                                        return;
                                    } else if (url.indexOf("youku") >= 7) {
                                        MGplayer.Ghttp_playlist_text = path;
                                        msg = new Message();
                                        data = new Bundle();
                                        data.putString("url", "http://127.0.0.1:" + MGplayer.http_server_port + "/playlist.m3u8");
                                        data.putString("ourl", url);
                                        data.putInt("seek", seek);
                                        msg.setData(data);
                                        msg.what = 91;
                                        if (pHandler.hasMessages(91)) {
                                            pHandler.removeMessages(91);
                                        }
                                        pHandler.sendMessage(msg);
                                        return;
                                    }
                                }
                            }
                        }
                    }).start();
                } else if (url.startsWith("file://")) {
                    new Thread(new Runnable() {
                        public void run() {
                            String path = url.replace("file://", VODplayer.gete() + "/movie/");
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putString("url", path);
                            data.putString("ourl", url);
                            data.putInt("seek", seek);
                            msg.setData(data);
                            msg.what = 91;
                            if (pHandler.hasMessages(91)) {
                                pHandler.removeMessages(91);
                            }
                            pHandler.sendMessage(msg);
                        }
                    }).start();
                } else if (url.startsWith("gp2p://")) {
                    new Thread(new Runnable() {
                        public void run() {
                            int gplayer_port = MGplayer.mediaplayerload(url, 10, 0, seek / 1000);
                            MGplayer.MyPrintln("#################### port: " + gplayer_port + " seek: " + seek + "####################");
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            if (MGplayer.httpdstart == 1) {
                                data.putString("url", "http://127.0.0.1:23456/playlist.m3u8");
                            } else {
                                data.putString("url", "http://127.0.0.1:" + gplayer_port);
                            }
                            data.putString("ourl", url);
                            data.putInt("seek", seek);
                            msg.setData(data);
                            msg.what = 92;
                            if (pHandler.hasMessages(92)) {
                                pHandler.removeMessages(92);
                            }
                            if (url.startsWith("gp2p://")) {
                                int ii = 0;
                                while (ii < 120 && url.equals(VODplayer.video_url)) {
                                    int cachecount = 5;
                                    if (MGplayer.gp2pwaitcachecount >= 0) {
                                        cachecount = MGplayer.gp2pwaitcachecount;
                                    }
                                    if (MGplayer.httpdstart == 0) {
                                        if (MGplayer.gp2pwaitcachecount >= 0) {
                                            cachecount = MGplayer.gp2pwaitcachecount;
                                        } else {
                                            cachecount = 10;
                                        }
                                    }
                                    int mediaplayercache = MGplayer.mediaplayercache();
                                    MGplayer.MyPrintln("mediaplayercache:" + mediaplayercache);
                                    if (mediaplayercache == -1) {
                                        pHandler.sendMessageDelayed(msg, 200);
                                        return;
                                    } else if (mediaplayercache < 0 || mediaplayercache < cachecount) {
                                        MGplayer.sleep(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                                        ii++;
                                    } else {
                                        pHandler.sendMessage(msg);
                                        return;
                                    }
                                }
                                return;
                            }
                            pHandler.sendMessage(msg);
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        public void run() {
                            String cpuinfo = MGplayer.getCpuName();
                            int gplayer_port = MGplayer.mediaplayerload(url, 10, 0, seek);
                            MGplayer.MyPrintln("#################### port: " + gplayer_port + "####################");
                            if (gplayer_port < 0) {
                                MGplayer.mediaplayerunload();
                                Message msg = new Message();
                                msg.what = 94;
                                if (pHandler.hasMessages(94)) {
                                    pHandler.removeMessages(94);
                                }
                                pHandler.sendMessage(msg);
                                return;
                            }
                            String murl = VODplayer.createPlaylist(_this, gplayer_port);
                            Message msg4 = new Message();
                            Bundle data = new Bundle();
                            if (cpuinfo.equals("AML8726")) {
                                data.putString("url", murl);
                            } else if (cpuinfo.equals("HI3716M") || cpuinfo.equals("HIK3V2")) {
                                if (url.startsWith("udp://")) {
                                    data.putString("url", murl);
                                } else if (MGplayer.custom().equals("koreayh")) {
                                    data.putString("url", murl);
                                } else {
                                    data.putString("url", "http://127.0.0.1:8084");
                                }
                            } else if (cpuinfo.equals("RK3128") || cpuinfo.equals("S805")) {
                                data.putString("url", "http://127.0.0.1:8084");
                            } else {
                                data.putString("url", "http://127.0.0.1:8084");
                            }
                            msg4.setData(data);
                            msg4.what = 92;
                            pHandler.removeMessages(92);
                            pHandler.sendMessageDelayed(msg4, 2000);
                        }
                    }).start();
                }
            }
        }
    }

    public static void resumeVideo(String url) {
        if (url != null && url.startsWith("gp2p://")) {
            MGplayer.mediaplayerstop(0);
        }
        if (MGplayer.getDecode() == 0) {
            VideoViewH.start();
        } else if (MGplayer.getDecode() == 1) {
            VideoViewS.start();
        } else if (MGplayer.getDecode() == 2) {
            VideoViewS2.start();
        } else if (MGplayer.getDecode() == 3) {
            VideoViewH2.start();
        }
    }

    public static void pauseVideo(String url) {
        if (url != null && url.startsWith("gp2p://")) {
            MGplayer.mediaplayerstop(1);
        }
        if (MGplayer.getDecode() == 0) {
            VideoViewH.pause();
        } else if (MGplayer.getDecode() == 1) {
            VideoViewS.pause();
        } else if (MGplayer.getDecode() == 2) {
            VideoViewS2.pause();
        } else if (MGplayer.getDecode() == 3) {
            VideoViewH2.pause();
        }
    }

    public static int ppVideo(String url) {
        if (MGplayer.getDecode() == 0) {
            if (VideoViewH.isPlaying()) {
                pauseVideo(url);
                mIsPause = true;
                return 0;
            }
            resumeVideo(url);
            mIsPause = false;
            return 1;
        } else if (MGplayer.getDecode() == 1) {
            if (VideoViewS.isPlaying()) {
                pauseVideo(url);
                mIsPause = true;
                return 0;
            }
            resumeVideo(url);
            mIsPause = false;
            return 1;
        } else if (MGplayer.getDecode() == 2) {
            if (VideoViewS2.isPlaying()) {
                pauseVideo(url);
                mIsPause = true;
                return 0;
            }
            resumeVideo(url);
            mIsPause = false;
            return 1;
        } else if (MGplayer.getDecode() != 3) {
            return 0;
        } else {
            if (VideoViewH2.isPlaying()) {
                pauseVideo(url);
                mIsPause = true;
                return 0;
            }
            resumeVideo(url);
            mIsPause = false;
            return 1;
        }
    }

    public static void stopVideo() {
        stopVideo(MGplayer.getDecode());
    }

    public static void stopVideo(int inx) {
        if (inx == 0) {
            stopVideoForHard(VideoViewH);
        } else if (inx == 3) {
            stopVideoForHard2(VideoViewH2);
        } else if (inx == 1) {
            stopVideoForSoft(VideoViewS);
        } else if (inx == 2) {
            stopVideoForSoft2(VideoViewS2);
        }
        resumeDecode();
    }

    public static void stopVideoForSoft(VlcVideoView mVideoView) {
        MGplayer.mediaplayerunload();
        if (video_url.startsWith("p2p://")) {
            MGplayer.s5();
        }
        mVideoView.pause();
        mVideoView.stopTimeout();
    }

    public static void stopVideoForSoft2(VideoView mVideoView) {
        MGplayer.mediaplayerunload();
        if (video_url.startsWith("p2p://")) {
            MGplayer.s5();
        }
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        }
    }

    public static void stopVideoForHard(VideoView mVideoView) {
        MGplayer.mediaplayerunload();
        if (video_url.startsWith("p2p://")) {
            MGplayer.s5();
        }
        if (mVideoView != null) {
            mVideoView.pause();
            mVideoView.reset();
        }
    }

    public static void stopVideoForHard2(ExoPlayerView mVideoView) {
        MGplayer.mediaplayerunload();
        if (video_url.startsWith("p2p://")) {
            MGplayer.s5();
        }
        mVideoView.pause();
        mVideoView.onDestroy();
    }

    public static boolean isPlaying() {
        if (MGplayer.getDecode() == 0 && VideoViewH != null) {
            return VideoViewH.isPlaying();
        }
        if (MGplayer.getDecode() == 1 && VideoViewS != null) {
            return VideoViewS.isPlaying();
        }
        if (MGplayer.getDecode() == 2 && VideoViewS2 != null) {
            return VideoViewS2.isPlaying();
        }
        if (MGplayer.getDecode() != 3 || VideoViewH2 == null) {
            return false;
        }
        return VideoViewH2.isPlaying();
    }

    public static int getProgress(String url) {
        if (url == null || !(url.startsWith("p2p://") || url.startsWith("forcetv://"))) {
            if (url != null && url.startsWith("gp2p://")) {
                String cpuname = MGplayer.getCpuName();
                int total = getTotal(url);
                int pos;
                if (MGplayer.getDecode() == 0) {
                    if (!cpuname.equals("S805")) {
                        pos = VideoViewH.getCurrentPosition();
                        if (pos <= 0 || pos > total) {
                            return video_seek;
                        }
                        return video_seek + pos;
                    } else if (MGplayer.httpdstart == 1) {
                        pos = VideoViewH.getCurrentPosition();
                        if (pos <= 0 || pos > total) {
                            return video_seek;
                        }
                        return video_seek + pos;
                    } else {
                        int pos2 = (int) VideoViewH.getCurrentPosition2();
                        if (pos2 <= 0 || pos2 > total) {
                            return video_seek;
                        }
                        return video_seek + pos2;
                    }
                } else if (MGplayer.getDecode() == 1) {
                    pos = (int) VideoViewS.getCurrentPosition2();
                    if (pos <= 0 || pos > total) {
                        return video_seek;
                    }
                    return video_seek + pos;
                } else if (MGplayer.getDecode() == 2) {
                    pos = (int) VideoViewS2.getCurrentPosition();
                    if (pos <= 0 || pos > total) {
                        return video_seek;
                    }
                    return video_seek + pos;
                } else if (MGplayer.getDecode() == 3) {
                    pos = (int) VideoViewH2.getCurrentPosition();
                    if (pos <= 0 || pos > total) {
                        return video_seek;
                    }
                    return video_seek + pos;
                }
            } else if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://"))) {
                if (MGplayer.getDecode() == 0) {
                    return VideoViewH.getCurrentPosition();
                }
                if (MGplayer.getDecode() == 1) {
                    return (int) VideoViewS.getCurrentPosition();
                }
                if (MGplayer.getDecode() == 2) {
                    return (int) VideoViewS2.getCurrentPosition();
                }
                if (MGplayer.getDecode() == 3) {
                    return (int) VideoViewH2.getCurrentPosition();
                }
            }
        } else if (MGplayer.getDecode() == 0) {
            return VideoViewH.getCurrentPosition();
        } else {
            if (MGplayer.getDecode() == 1) {
                return (int) VideoViewS.getCurrentPosition();
            }
            if (MGplayer.getDecode() == 2) {
                return (int) VideoViewS2.getCurrentPosition();
            }
            if (MGplayer.getDecode() == 3) {
                return (int) VideoViewH2.getCurrentPosition();
            }
        }
        return 0;
    }

    public static int getTotal(String url) {
        if (url == null || !(url.startsWith("p2p://") || url.startsWith("forcetv://"))) {
            if (url != null && url.startsWith("gp2p://")) {
                String statue = MGplayer.mediaplayerstatue("get");
                MGplayer.MyPrintln("mediaplayerstatue:" + statue);
                if (statue != null && statue.length() > 9) {
                    String[] statues = statue.split("#");
                    if (statues.length >= 4 && MGplayer.isNumeric(statues[3])) {
                        return Integer.parseInt(statues[3]) * 1000;
                    }
                }
            } else if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://") || url.startsWith("youku@") || url.startsWith("file://"))) {
                if (MGplayer.getDecode() == 0) {
                    return VideoViewH.getDuration();
                }
                if (MGplayer.getDecode() == 1) {
                    return (int) VideoViewS.getDuration();
                }
                if (MGplayer.getDecode() == 2) {
                    return (int) VideoViewS2.getDuration();
                }
                if (MGplayer.getDecode() == 3) {
                    return (int) VideoViewH2.getDuration();
                }
            }
        } else if (MGplayer.getDecode() == 0) {
            return VideoViewH.getDuration();
        } else {
            if (MGplayer.getDecode() == 1) {
                return (int) VideoViewS.getDuration();
            }
            if (MGplayer.getDecode() == 2) {
                return (int) VideoViewS2.getDuration();
            }
            if (MGplayer.getDecode() == 3) {
                return (int) VideoViewH2.getDuration();
            }
        }
        return 0;
    }

    public static void createPlaylist(Context _this, String id, int port) {
        try {
            FileOutputStream outStream = _this.openFileOutput("playlist.m3u8", 1);
            try {
                outStream.write(("#EXTM3U\n#EXT-X-TARGETDURATION:3600\n#EXT-X-VERSION:2\n#EXT-X-DISCONTINUITY\n#EXTINF:3600,\nhttp://127.0.0.1:" + Integer.toString(port) + InternalZipConstants.ZIP_FILE_SEPARATOR + id + "\n#EXT-X-ENDLIST\n").getBytes());
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    private static String createPlaylist(Context _this, int gplayer_port) {
        try {
            FileOutputStream outStream = _this.openFileOutput("playlist.m3u8", 1);
            try {
                outStream.write(("#EXTM3U\n#EXT-X-ALLOW-CACHE:YES\n#EXT-X-TARGETDURATION:72000\n#EXT-X-MEDIA-SEQUENCE:110236\n#EXTINF:1,\nhttp://127.0.0.1:" + Integer.toString(gplayer_port) + "/video.ts\n").getBytes());
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        return _this.getFilesDir() + "/playlist.m3u8";
    }

    private static String playVideo_p2p(Context _this, String url, String pw) {
        String[] arrs = url.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
        String[] opts = arrs[3].split("\\.");
        if (arrs.length < 4) {
            return null;
        }
        String murl;
        MGplayer.MyPrintln("p2p video = " + url + " pw = " + pw);
        String userid = MGplayer.tv.GetMac();
        if (MGplayer.custom().equals("szysx") || MGplayer.custom().equals("dhtv") || MGplayer.custom().equals("familytv") || MGplayer.custom().equals("turbotv") || MGplayer.custom().equals("anko")) {
            userid = "$user=$mac=" + MGplayer.tv.GetMac() + "$playkey=$username=$channelid=$columnid=$vodid=$key=" + MGplayer.MD5(MGplayer.tv.GetMac() + MGplayer.tv.getCpuID());
        } else if (MGplayer.custom().equals("huanqiu")) {
            userid = "$user=$mac=" + MGplayer.tv.GetMac() + "$playkey=$username=$channelid=$columnid=$vodid=$key=" + MGplayer.MD5(MGplayer.tv.GetMac() + MGplayer.tv.getCpuID() + "$time=" + MGplayer.key(String.valueOf(MGplayer.seconds)));
        }
        if (MGplayer.custom().equals("lookiptv") || MGplayer.custom().equals("xiaoqi") || MGplayer.custom().equals("aikanvip") || MGplayer.custom().equals("52home")) {
            String mac = MGplayer.tv.GetMac();
            String cpuid = MGplayer.tv.getCpuID();
            if (lookiptv.mac != null) {
                mac = lookiptv.mac;
            }
            if (lookiptv.cpuid != null) {
                cpuid = lookiptv.cpuid;
            }
            userid = "$user=$mac=" + mac + "$username=$channelid=$columnid=$vodid=$key=" + MGplayer.MD5(mac + cpuid + MGplayer.ip) + "$playkey=" + MGplayer.key(String.valueOf(MGplayer.seconds));
        }
        if (pw == null || pw == "" || (pw != null && pw.length() <= 0)) {
            MGplayer.s0(opts[0], arrs[2], userid, 0);
        } else {
            MGplayer.s1(opts[0], arrs[2], pw, userid, 0);
        }
        if (MGplayer.getCpuName().equals("AML8726")) {
            if (opts.length >= 2) {
                murl = "http://127.0.0.1:" + MGplayer.port() + InternalZipConstants.ZIP_FILE_SEPARATOR + opts[0] + "." + opts[1];
            } else {
                murl = "http://127.0.0.1:" + MGplayer.port() + InternalZipConstants.ZIP_FILE_SEPARATOR + arrs[3];
            }
        } else if (opts.length >= 2) {
            murl = "http://127.0.0.1:" + MGplayer.port() + InternalZipConstants.ZIP_FILE_SEPARATOR + opts[0] + "." + opts[1];
        } else {
            murl = "http://127.0.0.1:" + MGplayer.port() + InternalZipConstants.ZIP_FILE_SEPARATOR + arrs[3];
        }
        MGplayer.MyPrintln("playVideo_p2p = " + murl);
        return murl;
    }

    public static String sendHttpRequesttotal(String channelId, int port) {
        try {
            URL url = new URL("http://127.0.0.1:" + port + "/cmd.xml?cmd=query_chan_data_info&id=" + channelId);
            URL url2;
            try {
                StringBuffer sb = new StringBuffer();
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setConnectTimeout(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                huc.setReadTimeout(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                huc.setAllowUserInteraction(false);
                BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                int ii = 0;
                while (true) {
                    String line = br.readLine();
                    if (line == null || ii > 10) {
                        url2 = url;
                    } else {
                        ii++;
                        sb.append(line).append("\n");
                    }
                }
                url2 = url;
                return sb.toString();
            } catch (IOException e) {
                url2 = url;
                return "";
            }
        } catch (IOException e2) {
            return "";
        }
    }

    public static void init_Column() {
        if (MGplayer.vodcolumn != null) {
            MGplayer.MyPrintln("MGplayer.vodcolumn = " + MGplayer.vodcolumn);
            String[] column = MGplayer.vodcolumn.split("&");
            columner = new ColumnStatus[column.length];
            columner_needps = new int[column.length];
            for (int kk = 0; kk < column.length; kk++) {
                columner[kk] = new ColumnStatus();
                String[] columnkk = column[kk].split("@");
                if (columnkk.length >= 1) {
                    String[] columnkk_name = columnkk[0].split("\\|");
                    if (columnkk_name.length >= 1) {
                        columner[kk].id = Integer.parseInt(columnkk_name[0]);
                    }
                    if (columnkk_name.length >= 2) {
                        columner[kk].name = columnkk_name[1];
                    }
                    if (columnkk_name.length >= 3) {
                        ColumnStatus columnStatus = columner[kk];
                        int[] iArr = columner_needps;
                        int parseInt = Integer.parseInt(columnkk_name[2]);
                        iArr[kk] = parseInt;
                        columnStatus.needps = parseInt;
                    }
                    if (columnkk_name.length >= 4) {
                        columner[kk].password = columnkk_name[3];
                    }
                }
                if (columnkk.length >= 4) {
                    for (int ii = 1; ii < columnkk.length; ii++) {
                        if (columnkk[ii].startsWith("type#")) {
                            columner[kk].type_type = columnkk[ii].substring(5);
                        } else if (columnkk[ii].startsWith("area#")) {
                            columner[kk].type_area = columnkk[ii].substring(5);
                        } else if (columnkk[ii].startsWith("year#")) {
                            columner[kk].type_year = columnkk[ii].substring(5);
                        }
                    }
                }
                MGplayer.MyPrintln("columner[" + kk + "].name = " + columner[kk].name);
                MGplayer.MyPrintln("columner[" + kk + "].needps = " + columner[kk].needps);
                MGplayer.MyPrintln("columner[" + kk + "].password = " + columner[kk].password);
                MGplayer.MyPrintln("columner[" + kk + "].type_type = " + columner[kk].type_type);
                MGplayer.MyPrintln("columner[" + kk + "].type_area = " + columner[kk].type_area);
                MGplayer.MyPrintln("columner[" + kk + "].type_year = " + columner[kk].type_year);
            }
        }
    }

    private static Bitmap DrawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
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

    public static VodListUrlListStatus getVodListUrlListBitmap(String url) {
        for (int ii = 0; ii < VodListUrlListArray.size(); ii++) {
            VodListUrlListStatus VodListUrlLister = (VodListUrlListStatus) VodListUrlListArray.get(ii);
            if (url.equals(VodListUrlLister.url)) {
                return VodListUrlLister;
            }
        }
        return null;
    }

    public static void putVodListUrlList(String url) {
        VodListUrlListStatus VodListUrlLister2 = new VodListUrlListStatus();
        VodListUrlLister2.url = url;
        VodListUrlListArray.add(VodListUrlLister2);
        MGplayer.MyPrintln("putVodListUrlList url = " + url);
        if (VodListUrlListArray.size() > 100) {
            VodListUrlListArray.remove(0);
        }
    }

    public static void init(final Handler rHandler) {
        if (!vod_inited) {
            vod_inited = true;
            new Thread() {
                public void run() {
                    while (true) {
                        if (VODplayer.VodListUrlListArray.size() > 0) {
                            for (int ii = VODplayer.VodListUrlListArray.size() - 1; ii >= 0; ii--) {
                                VodListUrlListStatus VodListUrlLister = (VodListUrlListStatus) VODplayer.VodListUrlListArray.get(ii);
                                if (VodListUrlLister.f17b == null) {
                                    VodListUrlListStatus VodListUrlLister2 = VodListUrlLister;
                                    Bitmap bitmap = MGplayer.getHttpBitmap(VodListUrlLister.url);
                                    if (bitmap == null) {
                                        VodListUrlLister2.f17b = VODplayer.DrawableToBitmap(MGplayer._this.getResources().getDrawable(C0216R.mipmap.vdef));
                                    } else {
                                        if (MGplayer.resize_vod_image > 1) {
                                            VodListUrlLister2.f17b = MGplayer.resizeImage(bitmap, ((int) MGplayer.getFontsRate()) * 90, ((int) MGplayer.getFontsRate()) * 180);
                                        } else {
                                            VodListUrlLister2.f17b = bitmap;
                                        }
                                        if (rHandler.hasMessages(6)) {
                                            rHandler.removeMessages(6);
                                        }
                                        Message msg = new Message();
                                        msg.what = 6;
                                        rHandler.sendMessageDelayed(msg, 500);
                                    }
                                    VODplayer.VodListUrlListArray.remove(ii);
                                    VODplayer.VodListUrlListArray.add(VodListUrlLister2);
                                }
                            }
                        } else {
                            MGplayer.sleep(2000);
                        }
                        MGplayer.sleep(2000);
                    }
                }
            }.start();
        }
    }

    public static void checkVideo(final Context _this) {
        if (!check_playing_running) {
            check_playing_running = true;
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (VODplayer.check_playing_running) {
                        if (!(VODplayer.video_url == null || !VODplayer.video_url.startsWith("gp2p://") || VODplayer.mIsPause)) {
                            int seek = MGplayer.mediaplayerreopen();
                            if (seek >= 0) {
                                MGplayer.MyPrintln("=============mediaplayerreopen============ = " + seek);
                            }
                            if (seek >= 1) {
                                if (MGplayer.getDecode() == 0) {
                                    VODplayer.playVideoForHard(_this, VODplayer.VideoViewH, VODplayer.video_url, seek, VODplayer.video_pw);
                                } else if (MGplayer.getDecode() == 1) {
                                    if (VODplayer.VideoViewS != null) {
                                        VODplayer.playVideoForSoft(_this, VODplayer.VideoViewS, VODplayer.video_url, seek, VODplayer.video_pw);
                                    }
                                } else if (MGplayer.getDecode() == 2 && VODplayer.VideoViewS2 != null) {
                                    VODplayer.playVideoForSoft2(_this, VODplayer.VideoViewS2, VODplayer.video_url, seek, VODplayer.video_pw);
                                }
                            }
                        }
                        if (!(VODplayer.video_url == null || VODplayer.mIsPause)) {
                            int progress = VODplayer.getProgress(VODplayer.video_url);
                            int check_playing_timeout;
                            if (MGplayer.getDecode() == 0) {
                                if (VODplayer.VideoViewH.isPlaying()) {
                                    VODplayer.check_playing_times = 0;
                                } else {
                                    check_playing_timeout = 12;
                                    if (VODplayer.video_url.startsWith("gp2p://") && MGplayer.mediaplayercache() > 0) {
                                        check_playing_timeout = 12;
                                    }
                                    VODplayer.access$408();
                                    if (VODplayer.check_playing_times > check_playing_timeout) {
                                        VODplayer.check_playing_times = 0;
                                        MGplayer.MyPrintln("vod check_playing_running timeout " + VODplayer.video_url + " progress=" + progress);
                                        VODplayer.playVideoForHard(_this, VODplayer.VideoViewH, VODplayer.video_url, progress, VODplayer.video_pw);
                                    }
                                }
                            } else if (MGplayer.getDecode() == 1) {
                                if (VODplayer.VideoViewS.isPlaying()) {
                                    VODplayer.check_playing_times = 0;
                                } else {
                                    check_playing_timeout = 15;
                                    if (VODplayer.video_url.startsWith("gp2p://") && MGplayer.mediaplayercache() > 0) {
                                        check_playing_timeout = 10;
                                    }
                                    VODplayer.access$408();
                                    if (VODplayer.check_playing_times > check_playing_timeout) {
                                        VODplayer.check_playing_times = 0;
                                        VODplayer.playVideoForSoft(_this, VODplayer.VideoViewS, VODplayer.video_url, progress, VODplayer.video_pw);
                                    }
                                }
                            } else if (MGplayer.getDecode() == 2) {
                                if (VODplayer.VideoViewS2.isPlaying()) {
                                    VODplayer.check_playing_times = 0;
                                } else {
                                    check_playing_timeout = 15;
                                    if (VODplayer.video_url.startsWith("gp2p://") && MGplayer.mediaplayercache() > 0) {
                                        check_playing_timeout = 10;
                                    }
                                    VODplayer.access$408();
                                    if (VODplayer.check_playing_times > check_playing_timeout) {
                                        VODplayer.check_playing_times = 0;
                                        VODplayer.playVideoForSoft2(_this, VODplayer.VideoViewS2, VODplayer.video_url, progress, VODplayer.video_pw);
                                    }
                                }
                            }
                        }
                        mHandler.postDelayed(this, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                    }
                }
            }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
        }
    }

    public static int selectDecode(String url, String password) {
        MGplayer.MyPrintln("getDecode:" + MGplayer.getDecode() + " getPreDecode:" + MGplayer.getPreDecode());
        if (MGplayer.getDecode() == 3) {
            stopVideoForHard2(LIVEplayer.VideoViewH2);
            if (MGplayer.getPreDecode() == 3) {
                MGplayer.setDecode(0);
                MGplayer.setPreDecode(0);
            } else if (MGplayer.getPreDecode() >= 0) {
                MGplayer.setDecode(MGplayer.getPreDecode());
            }
        } else if (MGplayer.getDecode() == 2) {
            if (MGplayer.getPreDecode() != 2 && MGplayer.getPreDecode() >= 0) {
                MGplayer.setDecode(MGplayer.getPreDecode());
            }
        } else if (MGplayer.getPreDecode() == 3 || MGplayer.getPreDecode() == 2) {
            MGplayer.setPreDecode(MGplayer.getDecode());
        }
        if (url.startsWith("http://") && password != null && password.equals("passwordexo")) {
            if (MGplayer.getDecode() != 3) {
                MGplayer.setPreDecode(MGplayer.getDecode());
            } else {
                MGplayer.setPreDecode(0);
            }
            MGplayer.setDecode(3);
            return 3;
        } else if (url != null && url.startsWith("p2p://") && (MGplayer.getDecode() == 1 || MGplayer.getDecode() == 2)) {
            MGplayer.setPreDecode(MGplayer.getDecode());
            MGplayer.setDecode(2);
            return 2;
        } else {
            int inx = MGplayer.getPreDecode();
            if (inx < 0) {
                return MGplayer.getDecode();
            }
            MGplayer.setDecode(inx);
            return inx;
        }
    }

    public static void resumeDecode() {
        if (MGplayer.getDecode() == 3) {
            LIVEplayer.stopVideoForHard2(LIVEplayer.VideoViewH2);
            if (MGplayer.getPreDecode() == 3) {
                MGplayer.setDecode(0);
                MGplayer.setPreDecode(0);
            } else if (MGplayer.getPreDecode() >= 0) {
                MGplayer.setDecode(MGplayer.getPreDecode());
            }
        } else if (MGplayer.getDecode() == 2) {
            if (MGplayer.getPreDecode() != 2 && MGplayer.getPreDecode() >= 0) {
                MGplayer.setDecode(MGplayer.getPreDecode());
            }
        } else if (MGplayer.getDecode() == 1 && MGplayer.getPreDecode() != 1 && MGplayer.getPreDecode() >= 0) {
            MGplayer.setDecode(MGplayer.getPreDecode());
        }
        MGplayer.setPreDecode(-1);
    }
}
