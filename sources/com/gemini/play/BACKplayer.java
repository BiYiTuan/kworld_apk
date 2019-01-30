package com.gemini.play;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.gemini.custom.quanxing;
import com.google.android.exoplayer.hls.HlsChunkSource;
import cz.msebera.android.httpclient.HttpStatus;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import net.lingala.zip4j.util.InternalZipConstants;
import org.videolan.vlc.VlcVideoView;

public class BACKplayer {
    public static VideoView VideoViewH = null;
    public static VlcVideoView VideoViewS = null;
    public static String check_playing_currentid = null;
    private static boolean check_playing_running = false;
    private static int check_playing_times = 0;
    public static String check_playing_url = null;
    public static String currentID = null;
    public static String currentURL = null;
    public static ArrayList<PlaybackStatus> playbackArray = new ArrayList();
    public static ArrayList<PlayBackTypeStatus> playbackTypeArray = new ArrayList();
    public static boolean player_isexit = false;
    private static boolean preivewok = false;
    private static String[] previewdates = new String[]{null, null, null, null, null, null, null};
    private static int previewindex = 0;
    private static String[] previewweeks = new String[]{null, null, null, null, null, null, null};
    public static boolean typePasswordOK = false;
    public static int urlss_num = 0;

    static /* synthetic */ int access$308() {
        int i = check_playing_times;
        check_playing_times = i + 1;
        return i;
    }

    public static void playbackClear() {
        if (!playbackArray.isEmpty()) {
            playbackArray.clear();
        }
    }

    public static int playbackSize() {
        return playbackArray.size();
    }

    public static String playbackGet(int index) {
        return ((PlaybackStatus) playbackArray.get(index)).url;
    }

    public static String playbackImageGet(int index) {
        return ((PlaybackStatus) playbackArray.get(index)).image;
    }

    public static String playbackNameGet(int index) {
        return ((PlaybackStatus) playbackArray.get(index)).name;
    }

    public static int playbackIdGet(int index) {
        return ((PlaybackStatus) playbackArray.get(index)).id;
    }

    public static PlaybackStatus playbackStatusGet(int index) {
        return (PlaybackStatus) playbackArray.get(index);
    }

    public static String playbackTypeGet(int index) {
        return ((PlaybackStatus) playbackArray.get(index)).type;
    }

    public static String playbackIntroductionGet(int index) {
        return ((PlaybackStatus) playbackArray.get(index)).introduction;
    }

    public static String playbackPasswordGet(int index) {
        return ((PlaybackStatus) playbackArray.get(index)).password;
    }

    public static String playbackSourceGet(int index) {
        return ((PlaybackStatus) playbackArray.get(index)).source;
    }

    public static String playbackIntroidGet(int index) {
        return ((PlaybackStatus) playbackArray.get(index)).introid;
    }

    public static String playbackGetVideoUrl(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                return status.url;
            }
        }
        return null;
    }

    public static String playbackGetVideoName(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                return status.name;
            }
        }
        return null;
    }

    public static String playbackGetIntroductionID(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                return status.introid;
            }
        }
        return null;
    }

    public static String playbackGetVideoNameNext(int id) {
        return playbackGetVideoNameNext(id, false);
    }

    public static String playbackGetVideoNameForward(int id) {
        return playbackGetVideoNameForward(id, false);
    }

    public static String playbackGetVideoNameNext(int id, boolean checkps) {
        int i;
        int k;
        if (checkps) {
            ArrayList<PlaybackStatus> urlArrayPs = new ArrayList();
            for (i = 0; i < playbackArray.size(); i++) {
                PlaybackStatus status = playbackStatusGet(i);
                if (!playbackIsVideoTypePs(status.id)) {
                    urlArrayPs.add(status);
                }
            }
            for (i = 0; i < urlArrayPs.size(); i++) {
                if (((PlaybackStatus) urlArrayPs.get(i)).id == id) {
                    k = i + 1;
                    if (k >= urlArrayPs.size()) {
                        return "";
                    }
                    return ((PlaybackStatus) urlArrayPs.get(k)).name;
                }
            }
            return "";
        }
        i = 0;
        while (i < playbackArray.size()) {
            if (playbackStatusGet(i).id != id || checkps) {
                i++;
            } else {
                k = i + 1;
                if (k >= playbackArray.size()) {
                    return "";
                }
                return playbackStatusGet(k).name;
            }
        }
        return "";
    }

    public static String playbackGetVideoNameForward(int id, boolean checkps) {
        int i;
        int k;
        if (checkps) {
            ArrayList<PlaybackStatus> urlArrayPs = new ArrayList();
            for (i = 0; i < playbackArray.size(); i++) {
                PlaybackStatus status = playbackStatusGet(i);
                if (!playbackIsVideoTypePs(status.id)) {
                    urlArrayPs.add(status);
                }
            }
            for (i = 0; i < urlArrayPs.size(); i++) {
                if (((PlaybackStatus) urlArrayPs.get(i)).id == id) {
                    k = i - 1;
                    if (k < 0) {
                        return "";
                    }
                    return ((PlaybackStatus) urlArrayPs.get(k)).name;
                }
            }
            return "";
        }
        for (i = 0; i < playbackArray.size(); i++) {
            if (playbackStatusGet(i).id == id) {
                k = i - 1;
                if (k < 0) {
                    return "";
                }
                return playbackStatusGet(k).name;
            }
        }
        return "";
    }

    public static String playbackGetVideoIntroduction(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                return status.introduction;
            }
        }
        return null;
    }

    public static void playbackSetVideoIntroduction(int id, String preview) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                status.introduction = preview;
            }
        }
    }

    public static String playbackGetVideoImage(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                return status.image;
            }
        }
        return null;
    }

    public static String playbackGetVideoPassword(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                return status.password;
            }
        }
        return null;
    }

    public static String playbackGetVideoSource(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                return status.source;
            }
        }
        return null;
    }

    public static String playbackGetVideoType(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                return status.type;
            }
        }
        return null;
    }

    public static boolean playbackIsVideoTypePs(int id) {
        String type = null;
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                type = status.type;
            }
        }
        MGplayer.MyPrintln("type:" + type);
        if (type == null) {
            return false;
        }
        String[] ids = type.split("\\|");
        for (int ii = 0; ii < ids.length; ii++) {
            MGplayer.MyPrintln("type need ps:" + playbackTypeNeedpsGet(ids[ii]));
            if (playbackTypeNeedpsGet(ids[ii]).equals("1")) {
                return true;
            }
        }
        return false;
    }

    public static boolean playbackExistVideoId(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            if (playbackStatusGet(i).id == id) {
                return true;
            }
        }
        return false;
    }

    public static int playbackGetVideoNum(int id) {
        for (int i = 0; i < playbackArray.size(); i++) {
            if (playbackStatusGet(i).id == id) {
                return i;
            }
        }
        return -1;
    }

    public static int playbackGetVideoNum(int id, boolean checkps) {
        int i;
        if (checkps) {
            ArrayList<PlaybackStatus> urlArrayPs = new ArrayList();
            for (i = 0; i < playbackArray.size(); i++) {
                PlaybackStatus status = playbackStatusGet(i);
                if (!playbackIsVideoTypePs(status.id)) {
                    urlArrayPs.add(status);
                }
            }
            for (i = 0; i < urlArrayPs.size(); i++) {
                if (((PlaybackStatus) urlArrayPs.get(i)).id == id) {
                    return i;
                }
            }
            return -1;
        }
        for (i = 0; i < playbackArray.size(); i++) {
            if (playbackStatusGet(i).id == id) {
                return i;
            }
        }
        return -1;
    }

    public static String playbackGetVideoIntroductions(int id, int index) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                return status.introductions[index];
            }
        }
        return null;
    }

    public static void playbackSetVideoIntroductions(int id, int index, String preview) {
        for (int i = 0; i < playbackArray.size(); i++) {
            PlaybackStatus status = playbackStatusGet(i);
            if (status.id == id) {
                status.introductions[index] = preview;
            }
        }
    }

    public static boolean playbackPush(int id, String name, String image, String url, String password, String type, String introduction, String source, String introid) {
        PlaybackStatus status = new PlaybackStatus();
        status.name = name;
        status.image = image;
        status.url = url;
        status.id = id;
        status.type = type;
        status.password = password;
        status.introduction = introduction;
        status.introductions[0] = introduction;
        status.source = source;
        status.introid = introid;
        if (!(image == null || MGplayer.fileIsExists(MGplayer.images_icon + image))) {
            try {
                MGplayer.donwFile(MGplayer.tv.gete() + "/images/livepic/" + image, MGplayer.images_icon + image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (playbackExistVideoId(id)) {
            return true;
        }
        return playbackArray.add(status);
    }

    public static boolean playbackPush2(int id, String name, String image, String url, String password, String type, String introduction, String source, String introid) {
        int kk;
        previewDate();
        PlaybackStatus status = new PlaybackStatus();
        status.name = name;
        status.image = image;
        status.url = url;
        status.id = id;
        status.type = type;
        status.password = password;
        status.source = source;
        status.introid = introid;
        for (String str : previewdates) {
            MGplayer.MyPrintln("2 previewdates[kk]:" + str);
        }
        if (introduction.length() > 16) {
            String[] introductions = introduction.trim().split("geminipreview");
            for (String split : introductions) {
                MGplayer.MyPrintln("playbackPush2 introductions length:" + introductions.length);
                String[] item = split.split("geminidate");
                if (item.length >= 2) {
                    item[0] = item[0].replace("#$", "");
                    item[0] = item[0].replace("$#", "");
                    item[1] = item[1].replace("#$", "");
                    item[1] = item[1].replace("$#", "");
                    MGplayer.MyPrintln("item[0]:" + item[0]);
                    MGplayer.MyPrintln("item[1]:" + item[1]);
                    MGplayer.MyPrintln("playbackPush2 previewdates length:" + previewdates.length);
                    kk = 0;
                    while (kk < previewdates.length) {
                        MGplayer.MyPrintln("previewdates[kk]:" + previewdates[kk]);
                        if (previewdates[kk] == null || previewdates[kk].indexOf(item[0]) < 0 || kk >= 7) {
                            kk++;
                        } else {
                            status.introductions[kk] = item[1];
                            MGplayer.MyPrintln("playbackPush2 introductions[" + kk + "]:" + status.introductions[kk]);
                            if (kk == 0) {
                                status.introduction = item[1];
                            }
                        }
                    }
                }
            }
        }
        if (!(image == null || MGplayer.fileIsExists(MGplayer.images_icon + image))) {
            try {
                MGplayer.donwFile(MGplayer.tv.gete() + "/images/livepic/" + image, MGplayer.images_icon + image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (playbackExistVideoId(id)) {
            return true;
        }
        MGplayer.MyPrintln("playback no exist video push id " + id);
        return playbackArray.add(status);
    }

    public static boolean playbackTypePush(String id, String needps, String ps, String key, String type) {
        PlayBackTypeStatus status = new PlayBackTypeStatus();
        status.id = id;
        status.needps = needps;
        status.type = type;
        status.key = key;
        status.ps = ps;
        return playbackTypeArray.add(status);
    }

    public static String playbackTypeNameGet(int index) {
        if (index >= playbackTypeArray.size() || index < 0) {
            return "";
        }
        return ((PlayBackTypeStatus) playbackTypeArray.get(index)).type;
    }

    public static String playbackTypeIdGet(int index) {
        if (index >= playbackTypeArray.size() || index < 0) {
            return null;
        }
        return ((PlayBackTypeStatus) playbackTypeArray.get(index)).id;
    }

    public static int playbackTypeSize() {
        return playbackTypeArray.size();
    }

    public static String playbackTypeNeedpsGet(String id) {
        for (int i = 0; i < playbackTypeArray.size(); i++) {
            if (((PlayBackTypeStatus) playbackTypeArray.get(i)).id.equals(id)) {
                return ((PlayBackTypeStatus) playbackTypeArray.get(i)).needps;
            }
        }
        return "0";
    }

    public static String playbackTypePasswordGet(String id) {
        for (int i = 0; i < playbackTypeArray.size(); i++) {
            if (((PlayBackTypeStatus) playbackTypeArray.get(i)).id.equals(id)) {
                return ((PlayBackTypeStatus) playbackTypeArray.get(i)).ps;
            }
        }
        return null;
    }

    public static String playbackTypeNameGetFromId(String id) {
        for (int i = 0; i < playbackTypeArray.size(); i++) {
            if (((PlayBackTypeStatus) playbackTypeArray.get(i)).id.equals(id)) {
                return ((PlayBackTypeStatus) playbackTypeArray.get(i)).type;
            }
        }
        return null;
    }

    public static void playbackTypeClear() {
        if (!playbackTypeArray.isEmpty()) {
            playbackTypeArray.clear();
        }
    }

    private static void saveCurrentID(String id) {
        if (id != null) {
            Editor editor = MGplayer._this.getSharedPreferences("back", 0).edit();
            editor.putString("current_id", id);
            editor.commit();
        }
    }

    public static String getCurrentID() {
        return MGplayer._this.getSharedPreferences("back", 0).getString("current_id", "0");
    }

    public static void playVideoForSoft(final Context _this, final VlcVideoView mVideoView, final String id, int seek, boolean next) {
        player_isexit = false;
        MGplayer.mediaplayerunload();
        String url = playbackGetVideoUrl(Integer.parseInt(id));
        String password = playbackGetVideoPassword(Integer.parseInt(id));
        if (url != null) {
            if (MGplayer.custom().equals("quanxing")) {
                MGplayer.MyPrintln("video play:" + url + " password:" + quanxing.urlpassword);
                url = MGplayer.j2(url, quanxing.urlpassword);
                currentURL = url;
                password = quanxing.hotlink;
            } else {
                String urlss = MGplayer.ju(url);
                String passwordss = MGplayer.j2(password);
                url = getVideoUrlFromUrlss(urlss, next);
                currentURL = url;
                password = getVideoPassFromPassTmpss(passwordss, next);
            }
            mVideoView.pause();
            MGplayer.MyPrintln("back url 1 = " + url);
            if (url != null) {
                playVideoMessage(_this, url, seek, new Handler() {
                    public void handleMessage(Message msg) {
                        String murl = null;
                        switch (msg.what) {
                            case 90:
                                murl = BACKplayer.playVideo_p2p(_this, msg.getData().getString("url"));
                                break;
                            case 91:
                            case 92:
                                murl = msg.getData().getString("url");
                                break;
                        }
                        if (murl != null) {
                            mVideoView.startPlay(murl);
                            BACKplayer.saveCurrentID(id);
                        }
                    }
                });
            }
        }
    }

    public static void playVideoForHard(final Context _this, final VideoView mVideoView, final String id, int seek, boolean next) {
        player_isexit = false;
        mVideoView.pause();
        mVideoView.reset();
        Ghttp.stop();
        if (currentURL != null) {
            MGplayer.mediaplayerunload();
            MGplayer.sleep(1);
        }
        if (id != null && MGplayer.isNumeric(id)) {
            String url = playbackGetVideoUrl(Integer.parseInt(id));
            String password = playbackGetVideoPassword(Integer.parseInt(id));
            if (url != null) {
                if (MGplayer.custom().equals("quanxing")) {
                    MGplayer.MyPrintln("video play:" + url + " password:" + quanxing.urlpassword);
                    url = MGplayer.j2(url, quanxing.urlpassword);
                    currentURL = url;
                    password = quanxing.hotlink;
                } else {
                    String urlss = MGplayer.ju(url);
                    String passwordss = MGplayer.j2(password);
                    url = getVideoUrlFromUrlss(urlss, next);
                    currentURL = url;
                    password = getVideoPassFromPassTmpss(passwordss, next);
                }
                MGplayer.MyPrintln("back url 1 = " + url);
                if (url != null) {
                    playVideoMessage(_this, url, seek, new Handler() {
                        public void handleMessage(Message msg) {
                            String murl = null;
                            switch (msg.what) {
                                case 90:
                                    murl = BACKplayer.playVideo_p2p(_this, msg.getData().getString("url"));
                                    break;
                                case 91:
                                case 92:
                                    murl = msg.getData().getString("url");
                                    break;
                                case 97:
                                    BACKplayer.saveCurrentID(id);
                                    break;
                            }
                            if (murl != null) {
                                mVideoView.setVideoPath(murl);
                                if (BACKplayer.currentURL.startsWith("gp2p://") && MGplayer.getDecode() == 0) {
                                    final String murl_tmp = murl;
                                    final String currenturl_tmp = BACKplayer.currentURL;
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            if (MGplayer.httpdstart == 1) {
                                                if (currenturl_tmp != null && currenturl_tmp.equals(BACKplayer.currentURL) && BACKplayer.currentURL.startsWith("gp2p://") && mVideoView != null && !mVideoView.isPlaying()) {
                                                    mVideoView.pause();
                                                    mVideoView.setVideoPath(murl_tmp);
                                                }
                                            } else if (currenturl_tmp != null && currenturl_tmp.equals(BACKplayer.currentURL) && BACKplayer.currentURL.startsWith("gp2p://") && MGplayer.mediareceivecount() <= 0 && mVideoView != null && !mVideoView.isPlaying()) {
                                                mVideoView.pause();
                                                mVideoView.setVideoPath(murl_tmp);
                                            }
                                        }
                                    }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                                }
                            }
                            BACKplayer.saveCurrentID(id);
                        }
                    });
                }
            }
        }
    }

    public static void playVideoMessage(Context _this, final String url, final int seek, final Handler pHandler) {
        if (url != null) {
            player_isexit = false;
            if (url.startsWith("p2p://")) {
                new Thread(new Runnable() {
                    public void run() {
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putString("url", url);
                        msg.setData(data);
                        msg.what = 90;
                        if (pHandler.hasMessages(90)) {
                            pHandler.removeMessages(90);
                        }
                        pHandler.sendMessage(msg);
                    }
                }).start();
            } else if (url.startsWith("http://") || url.startsWith("rtsp://")) {
                MGplayer.MyPrintln("back url 2 = " + url);
                new Thread(new Runnable() {
                    public void run() {
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putString("url", url);
                        msg.setData(data);
                        msg.what = 91;
                        if (pHandler.hasMessages(91)) {
                            pHandler.removeMessages(91);
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
                        int inx = MGplayer.MyGetSharedPreferences(MGplayer._this, "data", 0).getInt("decode", 0);
                        Message msg4 = new Message();
                        Bundle data = new Bundle();
                        if (inx == 1) {
                            if (!url.startsWith("gp2p://")) {
                                data.putString("url", "http://127.0.0.1:" + gplayer_port);
                            } else if (MGplayer.httpdstart == 1) {
                                data.putString("url", "http://127.0.0.1:23456/playlist.m3u8");
                            } else {
                                data.putString("url", "http://127.0.0.1:" + gplayer_port);
                            }
                        } else if (!url.startsWith("gp2p://")) {
                            data.putString("url", "http://127.0.0.1:" + gplayer_port);
                        } else if (MGplayer.httpdstart == 1) {
                            data.putString("url", "http://127.0.0.1:23456/playlist.m3u8");
                        } else if (MGplayer.isAmlogic()) {
                            data.putString("url", "http://127.0.0.1:" + gplayer_port);
                        } else {
                            data.putString("url", "http://127.0.0.1:" + gplayer_port);
                        }
                        msg4.setData(data);
                        msg4.what = 92;
                        if (pHandler.hasMessages(92)) {
                            pHandler.removeMessages(92);
                        }
                        if (url.startsWith("gp2p://")) {
                            boolean send_ok = false;
                            int ii = 0;
                            while (ii < 120) {
                                if (url.equals(BACKplayer.currentURL) && !BACKplayer.player_isexit) {
                                    int cachecount = 5;
                                    if (MGplayer.gp2pwaitcachecount >= 0) {
                                        cachecount = MGplayer.gp2pwaitcachecount;
                                    } else if (MGplayer.httpdstart == 0) {
                                        cachecount = 10;
                                    }
                                    int mediaplayercache = MGplayer.mediaplayercache();
                                    MGplayer.MyPrintln("mediaplayercache:" + mediaplayercache);
                                    if (mediaplayercache != -1) {
                                        if (mediaplayercache >= 0 && mediaplayercache >= cachecount) {
                                            send_ok = true;
                                            pHandler.sendMessageDelayed(msg4, 200);
                                            break;
                                        }
                                        MGplayer.sleep(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                                        ii++;
                                    } else {
                                        send_ok = true;
                                        pHandler.sendMessageDelayed(msg4, 200);
                                        break;
                                    }
                                }
                                return;
                            }
                            if (!send_ok && url.equals(BACKplayer.currentURL)) {
                                return;
                            }
                            return;
                        }
                        pHandler.sendMessage(msg4);
                    }
                }).start();
            }
        }
    }

    public static void stopVideoForHard(VideoView mVideoView) {
        player_isexit = true;
        MGplayer.mediaplayerunload();
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        }
    }

    public static void stopVideoForSoft(VlcVideoView mVideoView) {
        player_isexit = true;
        MGplayer.mediaplayerunload();
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        } else {
            mVideoView.pause();
        }
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

    private static String playVideo_p2p(Context _this, String url) {
        String[] arrs = url.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
        String[] opts = arrs[3].split("\\.");
        if (arrs.length < 4) {
            return null;
        }
        String murl;
        MGplayer.s0(opts[0], arrs[2], MGplayer.tv.GetMac(), 0);
        if (MGplayer.getCpuName().equals("AML8726")) {
            if (opts.length >= 2) {
                murl = "http://127.0.0.1:" + MGplayer.port() + InternalZipConstants.ZIP_FILE_SEPARATOR + opts[0] + "." + opts[1];
            } else {
                murl = "http://127.0.0.1:" + MGplayer.port() + InternalZipConstants.ZIP_FILE_SEPARATOR + arrs[3];
            }
        } else if (MGplayer.getCpuName().equals("HI3716M")) {
            if (opts.length >= 2) {
                createPlaylist(_this, opts[0] + "." + opts[1], MGplayer.port());
            } else {
                createPlaylist(_this, arrs[3], MGplayer.port());
            }
            murl = _this.getFilesDir() + "/playlist.m3u8";
        } else if (opts.length >= 2) {
            murl = "http://127.0.0.1:" + MGplayer.port() + InternalZipConstants.ZIP_FILE_SEPARATOR + opts[0] + "." + opts[1];
        } else {
            murl = "http://127.0.0.1:" + MGplayer.port() + InternalZipConstants.ZIP_FILE_SEPARATOR + arrs[3];
        }
        return murl;
    }

    public static String[] getMergeArray(String[] al, String[] bl) {
        String[] a = al;
        String[] b = bl;
        String[] c = new String[(a.length + b.length)];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static String getVideoUrlFromUrlss(String urlss, boolean next) {
        String[] urls = urlss.split("geminihighlowgemini");
        MGplayer.MyPrintln("url length:" + urls.length);
        String[] high_urls;
        String[] high_url;
        if (urls.length < 2) {
            if (urls.length < 1) {
                String url = urlss;
                urlss_num = 0;
                return url;
            } else if (urls.length != 1) {
                return null;
            } else {
                if (urls[0].length() <= 1) {
                    return null;
                }
                high_urls = urls[0].split("\\|");
                if (next) {
                    urlss_num++;
                    if (urlss_num >= high_urls.length) {
                        urlss_num = 0;
                    }
                }
                high_url = high_urls[urlss_num].split("#");
                if (high_url.length >= 2) {
                    return high_url[1];
                }
                return urls[0];
            }
        } else if (next) {
            urlss_num++;
            String[] all_urls = getMergeArray(urls[0].split("\\|"), urls[1].split("\\|"));
            if (urlss_num >= all_urls.length) {
                urlss_num = 0;
            }
            String[] all_url = all_urls[urlss_num].split("#");
            if (all_url.length >= 2) {
                return all_url[1];
            }
            return null;
        } else if (urls[0].length() > 1) {
            high_urls = urls[0].split("\\|");
            if (high_urls.length < 1) {
                return null;
            }
            high_url = high_urls[0].split("#");
            if (high_url.length >= 2) {
                return high_url[1];
            }
            return null;
        } else if (urls[1].length() <= 1) {
            return null;
        } else {
            String[] low_urls = urls[1].split("\\|");
            if (low_urls.length < 1) {
                return null;
            }
            String[] low_url = low_urls[0].split("#");
            if (low_url.length >= 2) {
                return low_url[1];
            }
            return null;
        }
    }

    public static String getVideoPassFromPassTmpss(String password_tmpss, boolean next) {
        String[] password_tmps = password_tmpss.split("geminihighlowgemini");
        String[] high_password_tmp;
        if (password_tmps.length < 2) {
            if (password_tmps.length < 1) {
                return password_tmpss;
            }
            if (password_tmps.length != 1) {
                return null;
            }
            if (password_tmps[0].length() <= 1) {
                return null;
            }
            high_password_tmp = password_tmps[0].split("\\|")[urlss_num].split("#");
            if (high_password_tmp.length >= 2) {
                return high_password_tmp[1];
            }
            if (high_password_tmp.length == 1) {
                return null;
            }
            return password_tmps[0];
        } else if (next) {
            String[] all_pws = getMergeArray(password_tmps[0].split("\\|"), password_tmps[1].split("\\|"));
            if (urlss_num >= all_pws.length) {
                urlss_num = 0;
            }
            String[] all_pw = all_pws[urlss_num].split("#");
            if (all_pw.length >= 2) {
                return all_pw[1];
            }
            return null;
        } else if (password_tmps[0].length() > 1) {
            String[] high_password_tmps = password_tmps[0].split("\\|");
            if (high_password_tmps.length < 1) {
                return null;
            }
            high_password_tmp = high_password_tmps[0].split("#");
            if (high_password_tmp.length >= 2) {
                return high_password_tmp[1];
            }
            return null;
        } else if (password_tmps[1].length() <= 1) {
            return null;
        } else {
            String[] low_password_tmps = password_tmps[1].split("\\|");
            if (low_password_tmps.length < 1) {
                return null;
            }
            String[] low_password_tmp = low_password_tmps[0].split("#");
            if (low_password_tmp.length >= 2) {
                return low_password_tmp[1];
            }
            return null;
        }
    }

    public static void playVideoFull(Activity _this, String time) {
        Intent intent = new Intent(_this, BackPlayerVideoActivity.class);
        intent.putExtra(TtmlNode.ATTR_ID, currentID);
        intent.putExtra("time", time);
        _this.startActivityForResult(intent, 0);
    }

    public static void playVideoMini() {
    }

    public static long fromDateStringToLong(String inVal) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date == null) {
            return -1;
        }
        return date.getTime();
    }

    private static void previewDate() {
        if (!preivewok) {
            for (int ii = 0; ii < 7; ii++) {
                Calendar calendar = Calendar.getInstance();
                if (MGplayer.seconds_prc > 0) {
                    calendar.setTime(new Date(MGplayer.seconds_prc));
                }
                calendar.set(5, calendar.get(5) - ii);
                int week = calendar.get(7);
                previewdates[ii] = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                previewweeks[ii] = MGplayer.week(week);
                MGplayer.MyPrintln("MGplayer.seconds_prc" + MGplayer.seconds_prc + " previewdates[ii]" + previewdates[ii]);
            }
            preivewok = true;
        }
    }

    private static String createPlaylist(int gplayer_port) {
        try {
            FileOutputStream outStream = MGplayer._this.openFileOutput("playlist.m3u8", 1);
            String text = "#EXTM3U\n#EXT-X-ALLOW-CACHE:YES\n#EXT-X-TARGETDURATION:72000\n#EXT-X-MEDIA-SEQUENCE:1\n#EXTINF:70000,\nhttp://127.0.0.1:" + Integer.toString(gplayer_port) + "/video.ts\n";
            MGplayer.Ghttp_playlist_text = text;
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

    public static void playVideo(Context _this, String id, boolean next, int seek) {
        if (MGplayer.getDecode() == 0) {
            VideoViewH.setVisibility(0);
            VideoViewS.setVisibility(8);
            playVideoForHard(_this, VideoViewH, id, seek, next);
        } else {
            VideoViewS.setVisibility(0);
            VideoViewH.setVisibility(8);
            playVideoForSoft(_this, VideoViewS, id, seek, next);
        }
        check_playing_times = 0;
    }

    public static boolean isPlaying() {
        if (MGplayer.getDecode() == 0 && VideoViewH != null) {
            return VideoViewH.isPlaying();
        }
        if (MGplayer.getDecode() != 1 || VideoViewS == null) {
            return false;
        }
        return VideoViewS.isPlaying();
    }

    public static void stopCheckVideo() {
        check_playing_running = false;
        MGplayer.MyPrintln("back check_playing_running false");
    }

    public static void checkVideo(final Context _this) {
        if (!check_playing_running) {
            check_playing_running = true;
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (BACKplayer.check_playing_running) {
                        MGplayer.MyPrintln("back check_playing_running true " + BACKplayer.currentURL);
                        if (BACKplayer.currentURL != null && BACKplayer.currentURL.startsWith("gp2p://")) {
                            int seek = MGplayer.mediaplayerreopen();
                            if (seek >= 0) {
                                MGplayer.MyPrintln("=============mediaplayerreopen============ = " + seek);
                            }
                            if (seek >= 1) {
                                BACKplayer.playVideo(_this, BACKplayer.currentID, false, seek);
                            }
                        }
                        if (BACKplayer.currentURL == null || BACKplayer.isPlaying()) {
                            BACKplayer.check_playing_times = 0;
                        } else {
                            BACKplayer.access$308();
                            if (BACKplayer.check_playing_times > 12) {
                                BACKplayer.check_playing_times = 0;
                                String statues = MGplayer.mediaplayerstatue("no").trim();
                                if (statues.equals("ERROR")) {
                                    BACKplayer.check_playing_times = 0;
                                    BACKplayer.playVideo(_this, BACKplayer.currentID, false, 0);
                                    return;
                                }
                                String[] statue = statues.split("#");
                                if (statue.length >= 3) {
                                    BACKplayer.check_playing_times = 0;
                                    BACKplayer.playVideo(_this, BACKplayer.currentID, false, Integer.parseInt(statue[1]));
                                } else {
                                    BACKplayer.check_playing_times = 0;
                                    BACKplayer.playVideo(_this, BACKplayer.currentID, false, 0);
                                }
                            }
                        }
                        mHandler.postDelayed(this, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                    }
                }
            }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
        }
    }
}
