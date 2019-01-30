package com.gemini.play;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;
import com.gemini.custom.chuangshi;
import com.gemini.custom.ihdtv;
import com.gemini.custom.itv;
import com.gemini.custom.lookiptv;
import com.gemini.custom.quanxing;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.nagasoft.player.UrlChanged;
import com.nagasoft.player.VJPlayer;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.protocol.HttpRequestExecutor;
import io.vov.vitamio.widget.VideoView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import net.lingala.zip4j.util.InternalZipConstants;
import org.succlz123.okplayer.utils.OkPlayerUtils;
import org.videolan.vlc.VlcVideoView;

public class LIVEplayer {
    public static boolean SoftOrHard = true;
    public static VideoView VideoViewH = null;
    public static ExoPlayerView VideoViewH2 = null;
    public static VlcVideoView VideoViewS = null;
    public static VideoView VideoViewS2 = null;
    public static Context _this;
    public static ArrayList<String> adimageArray = new ArrayList();
    public static long check_currentposition = -1;
    public static int check_isplaying = 0;
    public static String check_playing_currentid = null;
    public static int check_playing_line = 0;
    public static boolean check_playing_running = false;
    public static int check_playing_times = 0;
    public static String currentID = null;
    public static int currentLine = 0;
    public static int currentSeek = 0;
    public static String currentType = null;
    public static String currentURL = null;
    public static boolean currentUseHlsPlugin = false;
    public static int enablelsplugin = 1;
    public static Thread geminipassword9_thread = null;
    public static boolean isstop_checkVideo = false;
    public static String p2p_password = null;
    public static ArrayList<TypePasswords> passwordsArray = new ArrayList();
    public static boolean player_isexit = false;
    public static boolean show_playlist_image = true;
    public static boolean show_ps_playlist = false;
    public static boolean show_type_find = true;
    public static int speed_nothings_times = 0;
    public static ArrayList<Type2Status> type2Array = new ArrayList();
    public static ArrayList<TypeStatus> typeArray = new ArrayList();
    public static boolean typePasswordOK = false;
    public static ArrayList<UrlStatus> urlArray = new ArrayList();
    public static int urlss_num = 0;
    public static VJPlayer vjms = null;
    public static UrlChanged vjmsinterface = new UrlChanged() {
        public void onUrlChanged(String paramString, Handler pHandler) {
            Message msg4 = new Message();
            Bundle data = new Bundle();
            data.putString("url", paramString);
            msg4.setData(data);
            msg4.what = 92;
            pHandler.sendMessage(msg4);
        }
    };
    public static String watermask = null;

    /* renamed from: com.gemini.play.LIVEplayer$1 */
    static class C02791 extends Thread {
        C02791() {
        }

        public void run() {
            int ii;
            for (ii = 0; ii < LIVEplayer.urlSize(); ii++) {
                String image = LIVEplayer.imageGet(ii, true);
                String watermark = LIVEplayer.watermarkGet(ii);
                String filename = image;
                if (image != null && image.startsWith("http://")) {
                    String[] paths = image.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
                    filename = paths[paths.length - 1];
                }
                if (!(filename == null || MGplayer.fileIsExists(MGplayer.images_icon + filename) || !LIVEplayer.show_playlist_image)) {
                    if (image != null) {
                        try {
                            if ((image.endsWith("png") || image.endsWith("jpg") || image.endsWith("jpeg") || image.endsWith("gif")) && image.startsWith("http://")) {
                                MGplayer.donwFile(image, MGplayer.images_icon + filename);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (image != null) {
                        if (image.endsWith("png") || image.endsWith("jpg") || image.endsWith("jpeg") || image.endsWith("gif")) {
                            MGplayer.donwFile(MGplayer.tv.gete() + "/images/livepic/" + image, MGplayer.images_icon + image);
                        }
                    }
                }
                if (!(watermark == null || MGplayer.fileIsExists(MGplayer.images_icon + watermark) || watermark == null)) {
                    try {
                        if (watermark.endsWith("png") || watermark.endsWith("jpg") || watermark.endsWith("jpeg") || watermark.endsWith("gif")) {
                            MGplayer.MyPrintln("download live icon " + watermark);
                            MGplayer.donwFile(MGplayer.tv.gete() + "/images/livepic/" + watermark, MGplayer.images_icon + watermark);
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            for (ii = 0; ii < LIVEplayer.adimageSize(); ii++) {
                image = LIVEplayer.adimageGet(ii);
                MGplayer.MyPrintln("download live ad " + image);
                if (!(image == null || MGplayer.fileIsExists(MGplayer.images_ad + image) || image == null)) {
                    try {
                        if (image.endsWith("png") || image.endsWith("jpg") || image.endsWith("jpeg") || image.endsWith("gif")) {
                            MGplayer.donwFile(MGplayer.tv.gete() + "/images/background/" + image, MGplayer.images_ad + image);
                        }
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
            }
        }
    }

    public static void setContext(Context t) {
        _this = t;
        if (MGplayer.custom().equals("doudouzi") || MGplayer.custom().equals("aikanvip") || MGplayer.custom().equals("xtvants") || MGplayer.custom().equals("goat") || MGplayer.custom().equals("vipiptv") || MGplayer.custom().equals("liveline") || MGplayer.custom().equals("boliwu' ")) {
            vjms = new VJPlayer(vjmsinterface);
        }
    }

    public static boolean urlPush(int id, String name, String image, String url, String password, String type, String introduction, String source, String introid) {
        UrlStatus status = new UrlStatus();
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
        if (existVideoId(id)) {
            return true;
        }
        return urlArray.add(status);
    }

    public static boolean urlPush(int id, String name, String image, String url, String password, String type, String introduction, String source, String introid, String watermark) {
        UrlStatus status = new UrlStatus();
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
        status.watermark = watermark;
        if (existVideoId(id)) {
            return true;
        }
        return urlArray.add(status);
    }

    public static boolean urlPush(int id, String name, String image, String url, String password, String type, String source, String introid) {
        UrlStatus status = new UrlStatus();
        status.name = name;
        status.image = image;
        status.url = url;
        status.id = id;
        status.type = type;
        status.password = password;
        status.source = source;
        status.introid = introid;
        if (existVideoId(id)) {
            return true;
        }
        return urlArray.add(status);
    }

    public static void adimagepush(String name) {
        adimageArray.add(name);
    }

    public static void downloadImage_thread() {
        new C02791().start();
    }

    public static boolean existVideoId(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            if (statusGet(i).id == id) {
                return true;
            }
        }
        return false;
    }

    public static void urlClear() {
        if (!urlArray.isEmpty()) {
            urlArray.clear();
        }
    }

    public static int urlSize() {
        return urlArray.size();
    }

    public static String imageGet(int index) {
        return imageGet(index, false);
    }

    public static String imageGet(int index, boolean ishttp) {
        String path = ((UrlStatus) urlArray.get(index)).image;
        if (path == null || !path.startsWith("http://") || ishttp || (!path.endsWith("png") && !path.endsWith("jpg") && !path.endsWith("jpeg") && !path.endsWith("gif"))) {
            return path;
        }
        String[] paths = path.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
        return paths[paths.length - 1];
    }

    public static String watermarkGet(int index) {
        return ((UrlStatus) urlArray.get(index)).watermark;
    }

    public static Bitmap imagebitGet(int index) {
        return ((UrlStatus) urlArray.get(index)).imagebit;
    }

    public static void imagebitSet(int index, Bitmap b) {
        ((UrlStatus) urlArray.get(index)).imagebit = b;
    }

    public static String nameGet(int index) {
        return ((UrlStatus) urlArray.get(index)).name;
    }

    public static int idGet(int index) {
        return ((UrlStatus) urlArray.get(index)).id;
    }

    public static UrlStatus statusGet(int index) {
        return (UrlStatus) urlArray.get(index);
    }

    public static String typeGet(int index) {
        return ((UrlStatus) urlArray.get(index)).type;
    }

    public static String introductionGet(int index) {
        return ((UrlStatus) urlArray.get(index)).introduction;
    }

    public static String introductionSet(int index, String preview) {
        ((UrlStatus) urlArray.get(index)).introduction = preview;
        return preview;
    }

    public static String passwordGet(int index) {
        return ((UrlStatus) urlArray.get(index)).password;
    }

    public static String sourceGet(int index) {
        return ((UrlStatus) urlArray.get(index)).source;
    }

    public static String introidGet(int index) {
        return ((UrlStatus) urlArray.get(index)).introid;
    }

    public static UrlStatus getStatus(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status;
            }
        }
        return null;
    }

    public static String getVideoUrl(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status.url;
            }
        }
        return null;
    }

    public static String getVideoName(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status.name;
            }
        }
        return null;
    }

    public static String getIntroductionID(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status.introid;
            }
        }
        return null;
    }

    public static String getVideoNameNext(int id) {
        return getVideoNameNext(id, false);
    }

    public static String getVideoNameForward(int id) {
        return getVideoNameForward(id, false);
    }

    public static UrlStatus getVideoStatusNext(int id, boolean checkps) {
        if (urlArray.size() <= 0) {
            return null;
        }
        int i;
        UrlStatus status;
        if (checkps) {
            boolean checkok = false;
            ArrayList<UrlStatus> urlArrayPs = new ArrayList();
            for (i = 0; i < urlArray.size(); i++) {
                status = statusGet(i);
                MGplayer.MyPrintln("name id = " + id + " status.id = " + status.id);
                String[] ids = status.type.split("\\|");
                boolean isneedps = false;
                for (String typeNeedpsGet : ids) {
                    if (typeNeedpsGet(typeNeedpsGet).equals("1")) {
                        isneedps = true;
                        break;
                    }
                }
                if (!isneedps) {
                    if (checkok) {
                        return status;
                    }
                    if (status.id == id) {
                        checkok = true;
                    }
                    urlArrayPs.add(status);
                }
            }
            if (urlArrayPs.size() <= 0) {
                return null;
            }
            return (UrlStatus) urlArrayPs.get(urlArrayPs.size() - 1);
        }
        i = 0;
        while (i < urlArray.size()) {
            status = statusGet(i);
            MGplayer.MyPrintln("name id = " + id + " status.id = " + status.id);
            if (status.id != id || checkps) {
                i++;
            } else {
                int k = i + 1;
                if (k >= urlArray.size()) {
                    return statusGet(0);
                }
                return statusGet(k);
            }
        }
        return statusGet(0);
    }

    public static String getVideoNameNext(int id, boolean checkps) {
        if (urlArray.size() <= 0) {
            return "";
        }
        int i;
        UrlStatus status;
        int k;
        if (checkps) {
            ArrayList<UrlStatus> urlArrayPs = new ArrayList();
            for (i = 0; i < urlArray.size(); i++) {
                status = statusGet(i);
                if (!isVideoTypePs(status.id)) {
                    urlArrayPs.add(status);
                }
            }
            if (urlArrayPs.size() <= 0) {
                return "";
            }
            for (i = 0; i < urlArrayPs.size(); i++) {
                status = (UrlStatus) urlArrayPs.get(i);
                MGplayer.MyPrintln("name id = " + id + " status.id = " + status.id);
                if (status.id == id) {
                    k = i + 1;
                    if (k >= urlArrayPs.size()) {
                        return ((UrlStatus) urlArrayPs.get(0)).name;
                    }
                    return ((UrlStatus) urlArrayPs.get(k)).name;
                }
            }
            return ((UrlStatus) urlArrayPs.get(0)).name;
        }
        i = 0;
        while (i < urlArray.size()) {
            status = statusGet(i);
            MGplayer.MyPrintln("name id = " + id + " status.id = " + status.id);
            if (status.id != id || checkps) {
                i++;
            } else {
                k = i + 1;
                if (k >= urlArray.size()) {
                    return statusGet(0).name;
                }
                return statusGet(k).name;
            }
        }
        return statusGet(0).name;
    }

    public static int getVideoIdNext(int id, boolean checkps) {
        if (urlArray.size() <= 0) {
            return -1;
        }
        int i;
        UrlStatus status;
        int k;
        if (checkps) {
            ArrayList<UrlStatus> urlArrayPs = new ArrayList();
            for (i = 0; i < urlArray.size(); i++) {
                status = statusGet(i);
                if (!isVideoTypePs(status.id)) {
                    urlArrayPs.add(status);
                }
            }
            if (urlArrayPs.size() <= 0) {
                return -1;
            }
            for (i = 0; i < urlArrayPs.size(); i++) {
                status = (UrlStatus) urlArrayPs.get(i);
                MGplayer.MyPrintln("ID id = " + id + " status.id = " + status.id);
                if (status.id == id) {
                    k = i + 1;
                    if (k >= urlArrayPs.size()) {
                        return ((UrlStatus) urlArrayPs.get(0)).id;
                    }
                    return ((UrlStatus) urlArrayPs.get(k)).id;
                }
            }
            return ((UrlStatus) urlArrayPs.get(0)).id;
        }
        for (i = 0; i < urlArray.size(); i++) {
            status = statusGet(i);
            MGplayer.MyPrintln("ID id = " + id + " status.id = " + status.id);
            if (status.id == id) {
                k = i + 1;
                if (k >= urlArray.size()) {
                    return statusGet(0).id;
                }
                return statusGet(k).id;
            }
        }
        return statusGet(0).id;
    }

    public static UrlStatus getVideoStatusForward(int id, boolean checkps) {
        if (urlArray.size() <= 0) {
            return null;
        }
        int i;
        UrlStatus status;
        if (checkps) {
            ArrayList<UrlStatus> urlArrayPs = new ArrayList();
            int i_forward = 0;
            for (i = 0; i < urlArray.size(); i++) {
                status = statusGet(i);
                String[] ids = status.type.split("\\|");
                boolean isneedps = false;
                for (String typeNeedpsGet : ids) {
                    if (typeNeedpsGet(typeNeedpsGet).equals("1")) {
                        isneedps = true;
                        break;
                    }
                }
                if (!isneedps) {
                    if (status.id == id) {
                        return (UrlStatus) urlArray.get(i_forward);
                    }
                    urlArrayPs.add(status);
                    i_forward = i;
                }
            }
            return (UrlStatus) urlArrayPs.get(urlArrayPs.size() - 1);
        }
        for (i = 0; i < urlArray.size(); i++) {
            status = statusGet(i);
            MGplayer.MyPrintln("name = " + id + " status.id = " + status.id);
            if (status.id == id) {
                int k = i - 1;
                if (k < 0) {
                    return statusGet(urlArray.size() - 1);
                }
                return statusGet(k);
            }
        }
        return statusGet(urlArray.size() - 1);
    }

    public static String getVideoNameForward(int id, boolean checkps) {
        if (urlArray.size() <= 0) {
            return "";
        }
        int i;
        UrlStatus status;
        int k;
        if (checkps) {
            ArrayList<UrlStatus> urlArrayPs = new ArrayList();
            for (i = 0; i < urlArray.size(); i++) {
                status = statusGet(i);
                if (!isVideoTypePs(status.id)) {
                    urlArrayPs.add(status);
                }
            }
            if (urlArrayPs.size() <= 0) {
                return "";
            }
            for (i = 0; i < urlArrayPs.size(); i++) {
                status = (UrlStatus) urlArrayPs.get(i);
                MGplayer.MyPrintln("name id = " + id + " status.id = " + status.id);
                if (status.id == id) {
                    k = i - 1;
                    if (k < 0) {
                        return ((UrlStatus) urlArrayPs.get(urlArrayPs.size() - 1)).name;
                    }
                    return ((UrlStatus) urlArrayPs.get(k)).name;
                }
            }
            return ((UrlStatus) urlArrayPs.get(urlArrayPs.size() - 1)).name;
        }
        for (i = 0; i < urlArray.size(); i++) {
            status = statusGet(i);
            MGplayer.MyPrintln("name = " + id + " status.id = " + status.id);
            if (status.id == id) {
                k = i - 1;
                if (k < 0) {
                    return statusGet(urlArray.size() - 1).name;
                }
                return statusGet(k).name;
            }
        }
        return statusGet(urlArray.size() - 1).name;
    }

    public static int getVideoIdForward(int id, boolean checkps) {
        if (urlArray.size() <= 0) {
            return -1;
        }
        int i;
        UrlStatus status;
        int k;
        if (checkps) {
            ArrayList<UrlStatus> urlArrayPs = new ArrayList();
            int i_forward = 0;
            for (i = 0; i < urlArray.size(); i++) {
                status = statusGet(i);
                if (!isVideoTypePs(status.id)) {
                    if (status.id == id) {
                        return ((UrlStatus) urlArray.get(i_forward)).id;
                    }
                    urlArrayPs.add(status);
                    i_forward = i;
                }
            }
            if (urlArrayPs.size() <= 0) {
                return -1;
            }
            for (i = 0; i < urlArrayPs.size(); i++) {
                if (((UrlStatus) urlArrayPs.get(i)).id == id) {
                    k = i - 1;
                    if (k < 0) {
                        return ((UrlStatus) urlArrayPs.get(urlArrayPs.size() - 1)).id;
                    }
                    return ((UrlStatus) urlArrayPs.get(k)).id;
                }
            }
            return ((UrlStatus) urlArrayPs.get(urlArrayPs.size() - 1)).id;
        }
        for (i = 0; i < urlArray.size(); i++) {
            status = statusGet(i);
            if (status.id == id) {
                MGplayer.MyPrintln("ID id = " + id + " status.id = " + status.id);
                k = i - 1;
                if (k < 0) {
                    return statusGet(urlArray.size() - 1).id;
                }
                return statusGet(k).id;
            }
        }
        return statusGet(urlArray.size() - 1).id;
    }

    public static String getVideoIntroduction(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status.introduction;
            }
        }
        return null;
    }

    public static void setVideoIntroduction(int id, String preview) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                status.introduction = preview;
            }
        }
    }

    public static String getVideoImage(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                String path = status.image;
                if (path == null || !path.startsWith("http://")) {
                    return path;
                }
                String[] paths = path.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
                return paths[paths.length - 1];
            }
        }
        return null;
    }

    public static String getVideoWaterMark(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status.watermark;
            }
        }
        return null;
    }

    public static String getVideoPassword(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status.password;
            }
        }
        return null;
    }

    public static String getVideoSource(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status.source;
            }
        }
        return null;
    }

    public static String getVideoType(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status.type;
            }
        }
        return null;
    }

    public static boolean isVideoTypePs(int id) {
        String type = null;
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                type = status.type;
            }
        }
        if (type == null) {
            return false;
        }
        String[] ids = type.split("\\|");
        for (String typeNeedpsGet : ids) {
            if (typeNeedpsGet(typeNeedpsGet).equals("1")) {
                return true;
            }
        }
        return false;
    }

    public static int getVideoNoNeedpsNum() {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (typeNeedpsGet(status.type).equals("0")) {
                return status.id;
            }
        }
        return -1;
    }

    public static int getVideoNum(int id) {
        for (int i = 0; i < urlArray.size(); i++) {
            if (statusGet(i).id == id) {
                return i;
            }
        }
        return -1;
    }

    public static int getVideoNum(int id, boolean checkps) {
        int i;
        if (checkps) {
            int needps_num = 0;
            for (i = 0; i < urlArray.size(); i++) {
                UrlStatus status = statusGet(i);
                if (status == null) {
                    break;
                }
                boolean isneedps = false;
                String[] ids = status.type.split("\\|");
                if (ids.length > 1) {
                    for (String typeNeedpsGet : ids) {
                        if (typeNeedpsGet(typeNeedpsGet).equals("1")) {
                            isneedps = true;
                            needps_num++;
                            break;
                        }
                    }
                } else if (typeNeedpsGet(status.type).equals("1")) {
                    isneedps = true;
                    needps_num++;
                }
                if (!isneedps && status.id == id) {
                    return i - needps_num;
                }
            }
            return -1;
        }
        for (i = 0; i < urlArray.size(); i++) {
            if (statusGet(i).id == id) {
                return i;
            }
        }
        return -1;
    }

    public static int getVideoNum(int id, String type, boolean checkps) {
        ArrayList<UrlStatus> urlArrayPs;
        int i;
        UrlStatus status;
        if (checkps) {
            urlArrayPs = new ArrayList();
            for (i = 0; i < urlArray.size(); i++) {
                status = statusGet(i);
                if (status.type.indexOf(type) >= 0) {
                    urlArrayPs.add(status);
                }
            }
            int needps_num = 0;
            i = 0;
            while (i < urlArrayPs.size()) {
                boolean isneedps = false;
                status = (UrlStatus) urlArrayPs.get(i);
                String[] ids = status.type.split("\\|");
                for (String typeNeedpsGet : ids) {
                    if (typeNeedpsGet(typeNeedpsGet).equals("1")) {
                        isneedps = true;
                        needps_num++;
                        break;
                    }
                }
                if (status.id != id) {
                    i++;
                } else if (isneedps || status.type.indexOf(type) < 0) {
                    return -1;
                } else {
                    return i - needps_num;
                }
            }
            return -1;
        }
        urlArrayPs = new ArrayList();
        for (i = 0; i < urlArray.size(); i++) {
            status = statusGet(i);
            if (status.type.indexOf(type) >= 0 || type.equals("1")) {
                urlArrayPs.add(status);
            }
        }
        for (i = 0; i < urlArrayPs.size(); i++) {
            if (((UrlStatus) urlArrayPs.get(i)).id == id) {
                return i;
            }
        }
        return -1;
    }

    public static String getVideoIntroductions(int id, int index) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                return status.introductions[index];
            }
        }
        return null;
    }

    public static void setVideoIntroductions(int id, int index, String preview) {
        for (int i = 0; i < urlArray.size(); i++) {
            UrlStatus status = statusGet(i);
            if (status.id == id) {
                status.introductions[index] = preview;
            }
        }
    }

    public static boolean typePush(String id, String type) {
        TypeStatus status = new TypeStatus();
        status.id = id;
        status.type = type;
        return typeArray.add(status);
    }

    public static boolean typePush(String id, String type, String key) {
        TypeStatus status = new TypeStatus();
        status.id = id;
        status.type = type;
        status.key = key;
        return typeArray.add(status);
    }

    public static boolean type2Push(String id, String needps, String ps) {
        Type2Status status = new Type2Status();
        status.id = id;
        status.needps = needps;
        status.ps = ps;
        return type2Array.add(status);
    }

    public static int typeSize() {
        return typeArray.size();
    }

    public static String typeNameGet(int index) {
        if (index >= typeArray.size() || index < 0) {
            return "";
        }
        return ((TypeStatus) typeArray.get(index)).type;
    }

    public static String typeIdGet(int index) {
        if (index >= typeArray.size() || index < 0) {
            return null;
        }
        return ((TypeStatus) typeArray.get(index)).id;
    }

    public static int type2Size() {
        return type2Array.size();
    }

    public static String typeNeedpsGet(String id) {
        for (int i = 0; i < type2Array.size(); i++) {
            if (((Type2Status) type2Array.get(i)).id.equals(id)) {
                return ((Type2Status) type2Array.get(i)).needps;
            }
        }
        return "0";
    }

    public static String typePasswordGet(String id) {
        for (int i = 0; i < type2Array.size(); i++) {
            if (((Type2Status) type2Array.get(i)).id.equals(id)) {
                return ((Type2Status) type2Array.get(i)).ps;
            }
        }
        return null;
    }

    public static String typeNameGetFromId(String id) {
        for (int i = 0; i < typeArray.size(); i++) {
            if (((TypeStatus) typeArray.get(i)).id.equals(id)) {
                return ((TypeStatus) typeArray.get(i)).type;
            }
        }
        return null;
    }

    public static int typeIndexGetFormId(String id) {
        for (int i = 0; i < typeArray.size(); i++) {
            if (((TypeStatus) typeArray.get(i)).id.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public static void typeClear() {
        if (!typeArray.isEmpty()) {
            typeArray.clear();
        }
    }

    public static int adimageSize() {
        return adimageArray.size();
    }

    public static String adimageGet(int ii) {
        return (String) adimageArray.get(ii);
    }

    public static void stopVideoForSoft2(VideoView mVideoView) {
        player_isexit = true;
        MGplayer.mediaplayerunload();
        Ghttp.stop();
        MGplayer.s5();
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        }
        if (MGplayer.start_tvbus == 1) {
            MGplayer.tvbuser.stopChannel();
        }
        if (vjms != null && VJPlayer.gbload) {
            vjms.stop();
        }
    }

    public static void stopVideoForSoft(VlcVideoView mVideoView) {
        player_isexit = true;
        if (currentURL != null && isUseHlsPlugin(currentURL)) {
            MGplayer.mediaplayerunload();
        }
        Ghttp.stop();
        MGplayer.s5();
        mVideoView.pause();
        mVideoView.onDestory();
        if (MGplayer.start_tvbus == 1) {
            MGplayer.tvbuser.stopChannel();
        }
        if (vjms != null && VJPlayer.gbload) {
            vjms.stop();
        }
    }

    public static void stopVideoForHard(VideoView mVideoView) {
        player_isexit = true;
        MGplayer.mediaplayerunload();
        Ghttp.stop();
        MGplayer.s5();
        mVideoView.pause();
        mVideoView.stopPlayback();
        if (MGplayer.start_tvbus == 1) {
            MGplayer.tvbuser.stopChannel();
        }
        if (vjms != null && VJPlayer.gbload) {
            vjms.stop();
        }
    }

    public static void stopVideoForHard2(ExoPlayerView mVideoView) {
        player_isexit = true;
        MGplayer.mediaplayerunload();
        Ghttp.stop();
        MGplayer.s5();
        mVideoView.pause();
        mVideoView.onDestroy();
        if (MGplayer.start_tvbus == 1) {
            MGplayer.tvbuser.stopChannel();
        }
        if (vjms != null && VJPlayer.gbload) {
            vjms.stop();
        }
    }

    public static void playVideoForSoft2(final VideoView mVideoView, final String id, int line, int seek, String nurl, String npassword) {
        mVideoView.pause();
        stopStatus();
        Ghttp.stop();
        if (currentURL != null && currentUseHlsPlugin) {
            MGplayer.mediaplayerunload();
            MGplayer.sleep(1);
        }
        String url = nurl;
        String password = npassword;
        if (url == null && password == null) {
            url = getVideoUrl(Integer.parseInt(id));
            password = getVideoPassword(Integer.parseInt(id));
            if (url != null) {
                if (MGplayer.custom().equals("quanxing")) {
                    MGplayer.MyPrintln("video play:" + url + " password:" + quanxing.urlpassword);
                    url = MGplayer.j2(url, quanxing.urlpassword);
                    password = quanxing.hotlink;
                } else {
                    String urlss = MGplayer.ju(url);
                    String passwordss = MGplayer.j2(password);
                    url = getVideoUrlFromUrlss(urlss, line);
                    password = getVideoPassFromPassTmpss(passwordss, line);
                }
                MGplayer.MyPrintln("video play:" + url + " password:" + password);
            } else {
                return;
            }
        }
        final Handler playHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        String murl = msg.getData().getString("murl");
                        String id = msg.getData().getString(TtmlNode.ATTR_ID);
                        if (murl != null) {
                            mVideoView.pause();
                            mVideoView.setVideoPath(murl);
                            mVideoView.start();
                        }
                        LIVEplayer.currentID = id;
                        LIVEplayer.saveCurrentID(id);
                        return;
                    default:
                        return;
                }
            }
        };
        playVideoMessage(url, password, id, new Handler() {
            public void handleMessage(Message msg) {
                String murl = null;
                switch (msg.what) {
                    case 90:
                        murl = LIVEplayer.playVideo_p2p(msg.getData().getString("url"), msg.getData().getString("password"), msg.getData().getInt("needrestart"), playHandler);
                        break;
                    case 91:
                    case 92:
                        murl = msg.getData().getString("url");
                        break;
                    case 96:
                        Toast.makeText(MGplayer._this.getApplicationContext(), MGplayer._this.getString(C0216R.string.liveplayer_text10).toString(), 0).show();
                        break;
                    case 97:
                        LIVEplayer.currentID = id;
                        LIVEplayer.saveCurrentID(id);
                        return;
                }
                if (murl != null) {
                    MGplayer.MyPrintln("video play:" + murl);
                    mVideoView.pause();
                    mVideoView.setVideoPath(murl);
                    mVideoView.start();
                }
                LIVEplayer.currentID = id;
                LIVEplayer.saveCurrentID(id);
            }
        }, seek);
    }

    public static void playVideoForSoft(final VlcVideoView mVideoView, final String id, int line, int seek, String nurl, String npassword) {
        mVideoView.pause();
        stopStatus();
        Ghttp.stop();
        if (currentURL != null && currentUseHlsPlugin) {
            MGplayer.mediaplayerunload();
            MGplayer.sleep(1);
        }
        String url = nurl;
        String password = npassword;
        if (url == null && password == null) {
            url = getVideoUrl(Integer.parseInt(id));
            password = getVideoPassword(Integer.parseInt(id));
            if (url == null) {
                MGplayer.MyPrintln("playVideoForHard null");
                return;
            } else if (MGplayer.custom().equals("quanxing")) {
                url = MGplayer.j2(url, quanxing.urlpassword);
                password = quanxing.hotlink;
                MGplayer.MyPrintln("video play:" + url + " password:" + password);
            } else {
                String urlss = MGplayer.ju(url);
                String passwordss = MGplayer.j2(password);
                MGplayer.MyPrintln("url=" + urlss + " psss=" + passwordss);
                url = getVideoUrlFromUrlss(urlss, line);
                password = getVideoPassFromPassTmpss(passwordss, line);
                MGplayer.MyPrintln("url=" + url + " ps=" + password);
            }
        }
        final Handler playHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        String murl = msg.getData().getString("murl");
                        String id = msg.getData().getString(TtmlNode.ATTR_ID);
                        if (murl != null) {
                            mVideoView.startPlay(murl);
                        }
                        LIVEplayer.currentID = id;
                        LIVEplayer.saveCurrentID(id);
                        return;
                    default:
                        return;
                }
            }
        };
        playVideoMessage(url, password, id, new Handler() {
            public void handleMessage(Message msg) {
                String murl = null;
                switch (msg.what) {
                    case 90:
                        murl = LIVEplayer.playVideo_p2p(msg.getData().getString("url"), msg.getData().getString("password"), msg.getData().getInt("needrestart"), playHandler);
                        break;
                    case 91:
                    case 92:
                        murl = msg.getData().getString("url");
                        break;
                    case 96:
                        Toast.makeText(MGplayer._this.getApplicationContext(), MGplayer._this.getString(C0216R.string.liveplayer_text10).toString(), 0).show();
                        break;
                    case 97:
                        LIVEplayer.currentID = id;
                        LIVEplayer.saveCurrentID(id);
                        return;
                }
                if (murl != null) {
                    MGplayer.MyPrintln("video play:" + murl);
                    mVideoView.startPlay(murl);
                    final String murl_tmp = murl;
                    final String currenturl_tmp = LIVEplayer.currentURL;
                    final Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (currenturl_tmp != null && currenturl_tmp.equals(LIVEplayer.currentURL) && LIVEplayer.currentURL.startsWith("gp2p://") && mVideoView != null && !mVideoView.isPlaying()) {
                                MGplayer.MyPrintln("vlc videoview startPlay is false " + MGplayer.mediareceivecount());
                                mVideoView.startPlay(murl_tmp);
                                mHandler.postDelayed(this, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                            }
                        }
                    }, 3000);
                }
                LIVEplayer.currentID = id;
                LIVEplayer.saveCurrentID(id);
            }
        }, seek);
    }

    public static void playVideoForHard(final VideoView videoViewH, final String id, int line, int seek, String nurl, String npassword) {
        stopStatus();
        if (videoViewH.isPlaying()) {
            videoViewH.pause();
            videoViewH.reset();
        }
        Ghttp.stop();
        if (currentURL != null && currentUseHlsPlugin) {
            MGplayer.mediaplayerunload();
            MGplayer.sleep(1);
        }
        String url = nurl;
        String password = npassword;
        if (url == null && password == null) {
            url = getVideoUrl(Integer.parseInt(id));
            password = getVideoPassword(Integer.parseInt(id));
            if (url == null) {
                MGplayer.MyPrintln("playVideoForHard null");
                return;
            } else if (url.contains("://")) {
                password = null;
            } else if (MGplayer.custom().equals("quanxing")) {
                url = MGplayer.j2(url, quanxing.urlpassword);
                password = quanxing.hotlink;
                MGplayer.MyPrintln("video play:" + url + " password:" + password);
            } else {
                String urlss = MGplayer.ju(url);
                String passwordss = MGplayer.j2(password);
                MGplayer.MyPrintln("url=" + urlss + " psss=" + passwordss);
                url = getVideoUrlFromUrlss(urlss, line);
                password = getVideoPassFromPassTmpss(passwordss, line);
                MGplayer.MyPrintln("url=" + url + " ps=" + password);
            }
        }
        MGplayer.MyPrintln("video play:" + url + " password:" + password);
        if (url != null) {
            final Handler playHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            String murl = msg.getData().getString("murl");
                            MGplayer.MyPrintln("video murl:" + murl);
                            if (murl != null) {
                                videoViewH.setVideoPath(murl);
                                videoViewH.start();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                }
            };
            playVideoMessage(url, password, id, new Handler() {
                public void handleMessage(Message msg) {
                    String murl = null;
                    switch (msg.what) {
                        case 90:
                            murl = LIVEplayer.playVideo_p2p(msg.getData().getString("url"), msg.getData().getString("password"), msg.getData().getInt("needrestart"), playHandler);
                            break;
                        case 91:
                        case 92:
                            murl = msg.getData().getString("url");
                            break;
                        case 96:
                            Toast.makeText(MGplayer._this.getApplicationContext(), MGplayer._this.getString(C0216R.string.liveplayer_text10).toString(), 0).show();
                            break;
                        case 97:
                            LIVEplayer.currentID = id;
                            LIVEplayer.saveCurrentID(id);
                            return;
                    }
                    if (murl != null) {
                        videoViewH.setVideoPath(murl);
                        if (LIVEplayer.currentURL.startsWith("gp2p://") && MGplayer.getDecode() == 0) {
                            final String murl_tmp = murl;
                            final String currenturl_tmp = LIVEplayer.currentURL;
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (MGplayer.httpdstart == 1) {
                                        if (currenturl_tmp != null && currenturl_tmp.equals(LIVEplayer.currentURL) && LIVEplayer.currentURL.startsWith("gp2p://") && videoViewH != null && !videoViewH.isPlaying()) {
                                            videoViewH.pause();
                                            videoViewH.setVideoPath(murl_tmp);
                                        }
                                    } else if (currenturl_tmp != null && currenturl_tmp.equals(LIVEplayer.currentURL) && LIVEplayer.currentURL.startsWith("gp2p://") && MGplayer.mediareceivecount() <= 0 && videoViewH != null && !videoViewH.isPlaying()) {
                                        videoViewH.pause();
                                        videoViewH.setVideoPath(murl_tmp);
                                    }
                                }
                            }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                        }
                    }
                    LIVEplayer.currentID = id;
                    LIVEplayer.saveCurrentID(id);
                }
            }, seek);
        }
    }

    public static void playVideoForHard2(final ExoPlayerView videoViewH, final String id, int line, int seek, String nurl, String npassword) {
        stopStatus();
        videoViewH.pause();
        videoViewH.release();
        Ghttp.stop();
        if (currentURL != null && currentUseHlsPlugin) {
            MGplayer.mediaplayerunload();
            MGplayer.sleep(1);
        }
        String url = nurl;
        String password = npassword;
        if (url == null && password == null) {
            url = getVideoUrl(Integer.parseInt(id));
            password = getVideoPassword(Integer.parseInt(id));
            if (url == null) {
                MGplayer.MyPrintln("playVideoForHard null");
                return;
            } else if (MGplayer.custom().equals("quanxing")) {
                url = MGplayer.j2(url, quanxing.urlpassword);
                password = quanxing.hotlink;
                MGplayer.MyPrintln("video play:" + url + " password:" + password);
            } else {
                String urlss = MGplayer.ju(url);
                String passwordss = MGplayer.j2(password);
                MGplayer.MyPrintln("url=" + urlss + " psss=" + passwordss);
                url = getVideoUrlFromUrlss(urlss, line);
                password = getVideoPassFromPassTmpss(passwordss, line);
                MGplayer.MyPrintln("url=" + url + " ps=" + password);
            }
        }
        if (url != null) {
            final Handler playHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            String murl = msg.getData().getString("murl");
                            MGplayer.MyPrintln("video murl:" + murl);
                            if (murl != null) {
                                videoViewH.setRendererContentType(2);
                                videoViewH.setVideoUri(Uri.parse(murl));
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                }
            };
            playVideoMessage(url, password, id, new Handler() {
                public void handleMessage(Message msg) {
                    String murl = null;
                    switch (msg.what) {
                        case 90:
                            murl = LIVEplayer.playVideo_p2p(msg.getData().getString("url"), msg.getData().getString("password"), msg.getData().getInt("needrestart"), playHandler);
                            break;
                        case 91:
                        case 92:
                            murl = msg.getData().getString("url");
                            break;
                        case 96:
                            Toast.makeText(MGplayer._this.getApplicationContext(), MGplayer._this.getString(C0216R.string.liveplayer_text10).toString(), 0).show();
                            break;
                        case 97:
                            LIVEplayer.currentID = id;
                            LIVEplayer.saveCurrentID(id);
                            return;
                    }
                    if (murl != null) {
                        MGplayer.MyPrintln("murl = " + murl);
                        videoViewH.setRendererContentType(2);
                        videoViewH.setVideoUri(Uri.parse(murl));
                    }
                    LIVEplayer.currentID = id;
                    LIVEplayer.saveCurrentID(id);
                }
            }, seek);
        }
    }

    public static void playVideoMessage(final String url, final String password, final String urlid, final Handler pHandler, int seek) {
        chuangshi.chuangshi_send2();
        lookiptv.lookiptv_send();
        player_isexit = false;
        check_playing_times = 0;
        speed_nothings_times = 0;
        check_currentposition = -1;
        int p2p_needrestart_tmp = 1;
        if (currentURL != null && ((currentURL.startsWith("p2p://") || currentURL.startsWith("forcetv://")) && (url.startsWith("p2p://") || url.startsWith("forcetv://")))) {
            p2p_needrestart_tmp = 0;
        } else if (currentURL != null && ((currentURL.startsWith("p2p://") || currentURL.startsWith("forcetv://")) && !url.startsWith("p2p://") && !url.startsWith("forcetv://"))) {
            MGplayer.s5();
        } else if (currentURL == null || !currentURL.startsWith("tvbus://")) {
            if (currentURL != null && currentURL.startsWith("vjms://") && VJPlayer.gbload) {
                vjms.stop();
            }
        } else if (MGplayer.start_tvbus == 1) {
            MGplayer.tvbuser.stopChannel();
        }
        if (geminipassword9_thread != null) {
            geminipassword9_thread.interrupt();
            geminipassword9_thread = null;
        }
        final int p2p_needrestart = p2p_needrestart_tmp;
        currentURL = url;
        currentUseHlsPlugin = false;
        if (url.startsWith("vjms://")) {
            new Thread(new Runnable() {
                public void run() {
                    if (VJPlayer.gbload) {
                        LIVEplayer.vjms.setURL(url);
                        LIVEplayer.vjms.start();
                        LIVEplayer.vjms.setHandler(pHandler);
                    }
                }
            }).start();
        } else if (url.startsWith("tvbus://")) {
            new Thread(new Runnable() {
                public void run() {
                    if (MGplayer.start_tvbus == 1) {
                        MGplayer.tvbuser.startChannel(url, pHandler);
                        Message msg = new Message();
                        msg.what = 97;
                        pHandler.sendMessage(msg);
                    }
                }
            }).start();
        } else if (url.startsWith("p2p://") || url.startsWith("forcetv://")) {
            MGplayer.MyPrintln("p2p_needrestart = " + p2p_needrestart);
            new Thread(new Runnable() {
                public void run() {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    String str = url;
                    data.putString("url", str);
                    if (MGplayer.custom().equals("asqgp3") || MGplayer.custom().equals("goat")) {
                        if (str.contains(MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/magic", false))) {
                            data.putString("password", MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/magic.php", false).split("\\|")[0]);
                        } else if (str.contains(MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/lb", false))) {
                            data.putString("password", MGplayer.executeHttpGet01(MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/lb_key_server", false), false).split("\\|")[0]);
                        } else if (str.contains(MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/myeytv", false))) {
                            data.putString("password", MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/myeytv.php", false).split("\\|")[0]);
                        } else if (str.contains(MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/iwhole", false))) {
                            data.putString("password", MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/iwhole.php", false).split("\\|")[0]);
                        } else if (str.contains(MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/ym", false))) {
                            data.putString("password", MGplayer.executeHttpGet01(MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/ym_key_server", false), false).split("\\|")[0]);
                        } else if (str.contains(MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/tb103", false))) {
                            data.putString("password", MGplayer.executeHttpGet01("http://goat.iaarc.com/link_server/goat_paid/tb103_key", false).split("\\|")[0]);
                        } else {
                            data.putString("password", password);
                        }
                    } else if (password == null || !password.contains("geminipassword4")) {
                        data.putString("password", password);
                    } else {
                        String ps = "";
                        String userid = "";
                        if (password != null && password.indexOf("@PWUSERID@") > 0) {
                            String[] pwuserid = password.split("@PWUSERID@");
                            if (pwuserid.length >= 2 && pwuserid[0].length() > 0 && pwuserid[1].length() > 0) {
                                userid = pwuserid[1];
                            }
                        }
                        String ps_all = MGplayer.sendServerCmd(userid);
                        if (ps_all != null && ps_all.length() > 0 && ps_all.contains("|")) {
                            TypePasswords password_status = new TypePasswords();
                            String[] ps_alls = ps_all.split("\\|");
                            if (ps_alls.length >= 2) {
                                password_status.time = MGplayer.seconds_prc + ((long) Integer.parseInt(ps_alls[1].trim()));
                                password_status.url = userid;
                                password_status.ps = ps_alls[0];
                                ps = ps_alls[0];
                            }
                            MGplayer.MyPrintln("ps:" + ps + " ps_all:" + ps_all);
                            data.putString("password", ps);
                        }
                    }
                    data.putInt("needrestart", p2p_needrestart);
                    msg.setData(data);
                    msg.what = 90;
                    if (pHandler.hasMessages(90)) {
                        pHandler.removeMessages(90);
                    }
                    pHandler.sendMessage(msg);
                }
            }).start();
        } else if (url != null && url.startsWith("ghttp://")) {
            new Thread(new Runnable() {
                public void run() {
                    String key_url = Ghttp.play(url);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("url", key_url);
                    msg.setData(data);
                    msg.what = 91;
                    if (pHandler.hasMessages(91)) {
                        pHandler.removeMessages(91);
                    }
                    pHandler.sendMessage(msg);
                }
            }).start();
        } else if (url != null && url.startsWith("http://") && password != null && password.contains("homepassword")) {
            new Thread(new Runnable() {
                public void run() {
                    String userid = null;
                    String sgin = null;
                    String key_url2 = url;
                    if (password != null && password.indexOf("@PWUSERID@") > 0) {
                        String[] pwuserid = password.split("@PWUSERID@");
                        if (pwuserid.length >= 2 && pwuserid[0].length() > 0 && pwuserid[1].length() > 0) {
                            userid = pwuserid[1];
                        }
                    }
                    MGplayer.MyPrintln("userid = " + userid);
                    if (userid.startsWith("http://")) {
                        sgin = MGplayer.sendServerCmd(userid + MGplayer.ip);
                        sgin = sgin.substring(sgin.indexOf("<br>") + 4);
                    }
                    if (url.indexOf("?") > 0) {
                        key_url2 = url + sgin;
                    } else {
                        key_url2 = url + sgin;
                    }
                    String key_url = key_url2;
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("url", key_url);
                    msg.setData(data);
                    msg.what = 91;
                    if (pHandler.hasMessages(91)) {
                        pHandler.removeMessages(91);
                    }
                    pHandler.sendMessage(msg);
                }
            }).start();
        } else if (url != null && url.startsWith("http://") && password != null && password.equals("geminiusertoken")) {
            new Thread(new Runnable() {
                public void run() {
                    String murl = null;
                    String[] urls = url.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
                    if (urls.length >= 3) {
                        MGplayer.proxy_http_setip(urls[2]);
                        murl = "http://127.0.0.1:9080/";
                        for (int ii = 3; ii < urls.length; ii++) {
                            murl = murl + InternalZipConstants.ZIP_FILE_SEPARATOR + urls[ii];
                        }
                    }
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("url", murl);
                    msg.setData(data);
                    msg.what = 91;
                    if (pHandler.hasMessages(91)) {
                        pHandler.removeMessages(91);
                    }
                    pHandler.sendMessageDelayed(msg, 100);
                }
            }).start();
        } else if (url != null && url.startsWith("http://") && url.endsWith("&token=key") && password != null && password.equals("geminiankotoken") && MGplayer.set_token == 1 && MGplayer.set_token_value != null) {
            new Thread(new Runnable() {
                public void run() {
                    String key_url2 = url.replace("&token=key", "&token=") + MGplayer.set_token_value;
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("url", key_url2);
                    msg.setData(data);
                    msg.what = 91;
                    if (pHandler.hasMessages(91)) {
                        pHandler.removeMessages(91);
                    }
                    pHandler.sendMessage(msg);
                }
            }).start();
        } else if (url != null && url.startsWith("rtmp://") && password != null && password.equals("geminipassword")) {
            new Thread(new Runnable() {
                public void run() {
                    MGplayer.sendServerCmd(url.replace("rtmp://", "http://") + "/playlist.m3u8?uidgemini=" + MGplayer.key(String.valueOf(MGplayer.seconds + ((long) new Random().nextInt(60)))), HttpRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("url", url);
                    msg.setData(data);
                    msg.what = 91;
                    if (pHandler.hasMessages(91)) {
                        pHandler.removeMessages(91);
                    }
                    pHandler.sendMessageDelayed(msg, 100);
                }
            }).start();
        } else if (!isUseHlsPlugin(url) && (password == null || (password != null && !password.equals("geminipassword2") && !password.equals("geminipassword3")))) {
            String key_url2 = url;
            String[] passwords;
            if ((MGplayer.custom().equals("gqhd") || MGplayer.custom().equals("iworld") || MGplayer.custom().equals("itv178") || MGplayer.custom().equals("huidixing") || MGplayer.custom().equals("woini")) && password != null && url != null && password.contains("geminipassword") && url.startsWith("http://") && url.endsWith(OkPlayerUtils.EXT_HLS)) {
                passwords = password.split("gjinghaog");
                if (passwords.length >= 2) {
                    ihdtv.setPlaylist_url_pw(url, passwords[1]);
                    key_url2 = "http://127.0.0.1:" + ihdtv.port + "/playlist.m3u8";
                } else {
                    ihdtv.setPlaylist_url_pw(url, "ihdtv.top");
                    key_url2 = "http://127.0.0.1:" + ihdtv.port + "/playlist.m3u8";
                }
            } else if ((MGplayer.custom().equals("gqhd") || MGplayer.custom().equals("iworld") || MGplayer.custom().equals("itv178") || MGplayer.custom().equals("huidixing") || MGplayer.custom().equals("woini")) && password != null && url != null && password.contains("geminipassword") && url.startsWith("http://")) {
                passwords = password.split("gjinghaog");
                key_url2 = passwords.length >= 2 ? ihdtv.geturl(url, passwords[1]) : ihdtv.geturl(url, "ihdtv.top");
            } else if ((MGplayer.custom().equals("gqhd") || MGplayer.custom().equals("iworld") || MGplayer.custom().equals("huidixing") || MGplayer.custom().equals("woini")) && password != null && url != null && url.startsWith("rtmp://")) {
                key_url2 = ihdtv.get_rtmp_url(url, password);
            } else if ((MGplayer.custom().equals("gqhd") || MGplayer.custom().equals("iworld") || MGplayer.custom().equals("itv178")) && password != null && url != null && url.startsWith("rtmp://")) {
                key_url2 = itv.get_rtmp_url(url, password);
            } else if (password != null && password.equals("geminipassword")) {
                key_url2 = url.indexOf("?") > 0 ? url + "&uidgemini=" + MGplayer.key(String.valueOf(MGplayer.seconds)) : url + "?uidgemini=" + MGplayer.key(String.valueOf(MGplayer.seconds));
            }
            final String key_url = key_url2;
            new Thread(new Runnable() {
                public void run() {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("url", key_url);
                    msg.setData(data);
                    msg.what = 91;
                    if (pHandler.hasMessages(91)) {
                        pHandler.removeMessages(91);
                    }
                    pHandler.sendMessage(msg);
                }
            }).start();
        } else if (isUseHlsPlugin(url) || (password != null && (password.equals("geminipassword2") || password.equals("geminipassword3")))) {
            final String str = urlid;
            final String str2 = url;
            final String str3 = password;
            final int i = seek;
            final Handler handler = pHandler;
            new Thread(new Runnable() {
                public void run() {
                    LIVEplayer.currentID = str;
                    LIVEplayer.saveCurrentID(str);
                    LIVEplayer.currentUseHlsPlugin = true;
                    String cpuinfo = MGplayer.getCpuName();
                    String key_url2 = str2;
                    if ((MGplayer.custom().equals("gqhd") || MGplayer.custom().equals("iworld") || MGplayer.custom().equals("itv178") || MGplayer.custom().equals("huidixing") || MGplayer.custom().equals("woini")) && str3 != null && str3.contains("geminipassword")) {
                        String[] passwords = str3.split("gjinghaog");
                        key_url2 = passwords.length >= 2 ? ihdtv.geturl(str2, passwords[1]).trim() : ihdtv.geturl(str2, "ihdtv.top").trim();
                    } else if (str3 != null && ((str3.equals("geminipassword") || str3.equals("geminipassword3")) && str2.endsWith("playlist.m3u8"))) {
                        key_url2 = str2.indexOf("?") > 0 ? str2 + "&uidgemini=" + MGplayer.key(String.valueOf(MGplayer.seconds)) : str2 + "?uidgemini=" + MGplayer.key(String.valueOf(MGplayer.seconds));
                    }
                    MGplayer.MyPrintln("seek = " + i);
                    int gplayer_port = MGplayer.mediaplayerload(key_url2, 10, 0, i);
                    MGplayer.MyPrintln("#################### port: " + gplayer_port + "####################");
                    if (gplayer_port < 0) {
                        MGplayer.mediaplayerunload();
                        Message msg = new Message();
                        msg.what = 94;
                        if (handler.hasMessages(94)) {
                            handler.removeMessages(94);
                        }
                        handler.sendMessage(msg);
                        return;
                    }
                    int inx = MGplayer.MyGetSharedPreferences(MGplayer._this, "data", 0).getInt("decode", 0);
                    Message msg4 = new Message();
                    Bundle data = new Bundle();
                    if (inx == 1) {
                        if (!str2.startsWith("gp2p://")) {
                            data.putString("url", "http://127.0.0.1:" + gplayer_port);
                        } else if (MGplayer.httpdstart == 1) {
                            data.putString("url", "http://127.0.0.1:23456/playlist.m3u8");
                        } else {
                            data.putString("url", "http://127.0.0.1:" + gplayer_port);
                        }
                    } else if (cpuinfo.equals("HI3716M") || cpuinfo.equals("HIK3V2")) {
                        if (!str2.startsWith("gp2p://")) {
                            data.putString("url", "http://127.0.0.1:" + gplayer_port);
                        } else if (MGplayer.httpdstart == 1) {
                            data.putString("url", "http://127.0.0.1:23456/playlist.m3u8");
                        } else {
                            data.putString("url", LIVEplayer.createPlaylist(gplayer_port));
                        }
                    } else if (!str2.startsWith("gp2p://")) {
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
                    if (handler.hasMessages(92)) {
                        handler.removeMessages(92);
                    }
                    if (str2.startsWith("gp2p://")) {
                        int ii = 0;
                        while (ii < 120 && str2.equals(LIVEplayer.currentURL) && !LIVEplayer.player_isexit) {
                            int cachecount = 5;
                            if (MGplayer.gp2pwaitcachecount >= 0) {
                                cachecount = MGplayer.gp2pwaitcachecount;
                            }
                            if (str3 != null && str3.equals("passwordexo") && MGplayer.httpdstart == 1) {
                                cachecount = 1;
                            } else if (MGplayer.httpdstart == 0) {
                                if (MGplayer.gp2pwaitcachecount >= 0) {
                                    cachecount = MGplayer.gp2pwaitcachecount;
                                } else {
                                    cachecount = 10;
                                }
                            }
                            int mediaplayercache = MGplayer.mediaplayercache();
                            MGplayer.MyPrintln("mediaplayercache:" + mediaplayercache);
                            if (mediaplayercache == -1) {
                                handler.sendMessageDelayed(msg4, 200);
                                return;
                            } else if (mediaplayercache < 0 || mediaplayercache < cachecount) {
                                MGplayer.sleep(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                                ii++;
                            } else {
                                handler.sendMessageDelayed(msg4, 200);
                                return;
                            }
                        }
                        return;
                    }
                    handler.sendMessage(msg4);
                }
            }).start();
        } else if (url.startsWith("wow://")) {
            new Thread(new Runnable() {
                public void run() {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("url", url);
                    data.putString("urlid", urlid);
                    msg.setData(data);
                    msg.what = 95;
                    if (pHandler.hasMessages(95)) {
                        pHandler.removeMessages(95);
                    }
                    pHandler.sendMessage(msg);
                }
            }).start();
        }
    }

    public static void playCollectVideoForHard(Context _this, VideoView videoViewH, String id, int line, int seek, String nurl, String npassword) {
        String url = null;
        String password = null;
        if (nurl == null && npassword == null) {
            UrlStatus s = new MyLiveCollectView().get(_this, id);
            if (s != null) {
                if (currentURL != null && currentUseHlsPlugin) {
                    MGplayer.mediaplayerunload();
                    MGplayer.sleep(1);
                }
                url = s.url;
                password = s.password;
                if (url == null) {
                    return;
                }
                if (MGplayer.custom().equals("quanxing")) {
                    url = MGplayer.j2(url, quanxing.urlpassword);
                    password = quanxing.hotlink;
                } else {
                    String urlss = MGplayer.ju(url);
                    String passwordss = MGplayer.j2(password);
                    url = getVideoUrlFromUrlss(urlss, line);
                    password = getVideoPassFromPassTmpss(passwordss, currentLine);
                }
            } else {
                return;
            }
        }
        MGplayer.MyPrintln("collect url:" + url + " password:" + password);
        playVideoForHard(videoViewH, id, line, seek, url, password);
    }

    public static void playCollectVideoForHard2(Context _this, ExoPlayerView videoViewH, String id, int line, int seek, String nurl, String npassword) {
        String url = null;
        String password = null;
        if (nurl == null && npassword == null) {
            UrlStatus s = new MyLiveCollectView().get(_this, id);
            if (s != null) {
                if (currentURL != null && currentUseHlsPlugin) {
                    MGplayer.mediaplayerunload();
                    MGplayer.sleep(1);
                }
                url = s.url;
                password = s.password;
                if (url == null) {
                    return;
                }
                if (MGplayer.custom().equals("quanxing")) {
                    url = MGplayer.j2(url, quanxing.urlpassword);
                    password = quanxing.hotlink;
                } else {
                    String urlss = MGplayer.ju(url);
                    String passwordss = MGplayer.j2(password);
                    url = getVideoUrlFromUrlss(urlss, line);
                    password = getVideoPassFromPassTmpss(passwordss, currentLine);
                }
            } else {
                return;
            }
        }
        playVideoForHard2(videoViewH, id, line, seek, url, password);
    }

    public static void playCollectVideoForSoft2(Context _this, VideoView mVideoView, String id, int line, int seek, String nurl, String npassword) {
        String url = null;
        String password = null;
        if (nurl == null && npassword == null) {
            UrlStatus s = new MyLiveCollectView().get(_this, id);
            if (s != null) {
                if (currentURL != null && currentUseHlsPlugin) {
                    MGplayer.mediaplayerunload();
                    MGplayer.sleep(1);
                }
                url = s.url;
                password = s.password;
                if (url == null) {
                    return;
                }
                if (MGplayer.custom().equals("quanxing")) {
                    url = MGplayer.j2(url, quanxing.urlpassword);
                    password = quanxing.hotlink;
                } else {
                    String urlss = MGplayer.ju(url);
                    String passwordss = MGplayer.j2(password);
                    url = getVideoUrlFromUrlss(urlss, line);
                    password = getVideoPassFromPassTmpss(passwordss, currentLine);
                }
            } else {
                return;
            }
        }
        playVideoForSoft2(mVideoView, id, line, seek, url, password);
    }

    public static void playCollectVideoForSoft(Context _this, VlcVideoView mVideoView, String id, int line, int seek, String nurl, String npassword) {
        String url = null;
        String password = null;
        if (nurl == null && npassword == null) {
            UrlStatus s = new MyLiveCollectView().get(_this, id);
            if (s != null) {
                if (currentURL != null && currentUseHlsPlugin) {
                    MGplayer.mediaplayerunload();
                    MGplayer.sleep(1);
                }
                url = s.url;
                password = s.password;
                if (url == null) {
                    return;
                }
                if (MGplayer.custom().equals("quanxing")) {
                    url = MGplayer.j2(url, quanxing.urlpassword);
                    password = quanxing.hotlink;
                } else {
                    String urlss = MGplayer.ju(url);
                    String passwordss = MGplayer.j2(password);
                    url = getVideoUrlFromUrlss(urlss, line);
                    password = getVideoPassFromPassTmpss(passwordss, currentLine);
                }
            } else {
                return;
            }
        }
        playVideoForSoft(mVideoView, id, line, seek, url, password);
    }

    public static void playWaterMark(String id, ImageView watermarkImage) {
        if (MGplayer.isNumeric(id)) {
            String watermark = getVideoWaterMark(Integer.parseInt(id));
            MGplayer.MyPrintln("watermark = " + watermark);
            if (watermark != null && watermark.length() > 4 && MGplayer.watermark_site >= 0) {
                watermarkImage.setVisibility(0);
                watermarkImage.setImageBitmap(BitmapFactory.decodeFile(MGplayer._this.getFilesDir() + "/icon/" + watermark));
            } else if (MGplayer.watermark_site >= 0) {
                watermarkImage.setVisibility(0);
                watermarkImage.setImageBitmap(BitmapFactory.decodeFile(MGplayer._this.getFilesDir() + "/icon/" + watermask));
            } else {
                watermarkImage.setVisibility(8);
            }
        }
    }

    public static boolean isUseHlsPlugin(String url) {
        if (url == null) {
            return false;
        }
        String cpuinfo = MGplayer.getCpuName();
        MGplayer.MyPrintln("isUseHlsPlugin:" + enablelsplugin + " CPU:" + cpuinfo);
        if (url.startsWith("rtsp://") || url.startsWith("p2p://") || url.startsWith("forcetv://")) {
            return false;
        }
        if (url.startsWith("gemini://") || url.startsWith("gp2p://")) {
            return true;
        }
        if (enablelsplugin == 0) {
            return false;
        }
        if (enablelsplugin == 1) {
            if (cpuinfo == null || !cpuinfo.equals("S805")) {
                if (cpuinfo == null || !cpuinfo.equals("RK3128")) {
                    if (cpuinfo == null || !cpuinfo.equals("HIK3V2")) {
                        if (SoftOrHard || url.startsWith("http://")) {
                            return false;
                        }
                        return true;
                    } else if (url.startsWith("rtmp://") || url.startsWith("udp://")) {
                        return false;
                    } else {
                        if (url.startsWith("http://") && url.contains(":1935") && url.contains("playlist.m3u8")) {
                            return false;
                        }
                        if (url.startsWith("http://") && !url.contains("playlist.m3u8")) {
                            return false;
                        }
                        if (url.startsWith("http://") && !url.contains(":1935") && url.contains("playlist.m3u8")) {
                            return false;
                        }
                        return true;
                    }
                } else if (url.startsWith("rtmp://") || url.startsWith("udp://")) {
                    return false;
                } else {
                    return ((url.startsWith("http://") && url.contains(":1935") && url.contains("playlist.m3u8")) || !url.startsWith("http://") || url.contains("playlist.m3u8")) ? false : false;
                }
            } else if (url.startsWith("rtmp://") || url.startsWith("udp://")) {
                return false;
            } else {
                if (url.startsWith("http://") && url.contains(":1935") && url.contains("playlist.m3u8")) {
                    return true;
                }
                return (!url.startsWith("http://") || url.contains("playlist.m3u8")) ? false : false;
            }
        } else if (enablelsplugin == 2) {
            return true;
        } else {
            return false;
        }
    }

    private static void createPlaylist(String id, int port) {
        try {
            FileOutputStream outStream = MGplayer._this.openFileOutput("playlist.m3u8", 1);
            try {
                outStream.write(("#EXTM3U\n#EXT-X-TARGETDURATION:1\n#EXT-X-VERSION:2\n#EXT-X-DISCONTINUITY\n#EXTINF:10,\nhttp://127.0.0.1:" + Integer.toString(port) + InternalZipConstants.ZIP_FILE_SEPARATOR + id + "\n#EXT-X-ENDLIST\n").getBytes());
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    private static String getHttpdPlaylist() {
        return MGplayer._this.getFilesDir() + "/httpd/playlist.m3u8";
    }

    private static String createPlaylist(int gplayer_port) {
        try {
            FileOutputStream outStream = MGplayer._this.openFileOutput("playlist.m3u8", 1);
            String text = "#EXTM3U\n#EXT-X-ALLOW-CACHE:YES\n#EXT-X-TARGETDURATION:72000\n#EXT-X-MEDIA-SEQUENCE:1\n#EXTINF:70000,\nhttp://127.0.0.1:" + Integer.toString(gplayer_port) + "\n";
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String playVideo_p2p(java.lang.String r26, java.lang.String r27, int r28, android.os.Handler r29) {
        /*
        r25 = 0;
        r21 = 0;
        r4 = "/";
        r0 = r26;
        r9 = r0.split(r4);
        r4 = 3;
        r4 = r9[r4];
        r10 = "\\.";
        r8 = r4.split(r10);
        r4 = r9.length;
        r10 = 4;
        if (r4 >= r10) goto L_0x001b;
    L_0x0019:
        r4 = 0;
    L_0x001a:
        return r4;
    L_0x001b:
        if (r27 == 0) goto L_0x004d;
    L_0x001d:
        r4 = "@PWUSERID@";
        r0 = r27;
        r4 = r0.indexOf(r4);
        if (r4 <= 0) goto L_0x004d;
    L_0x0027:
        r4 = "@PWUSERID@";
        r0 = r27;
        r22 = r0.split(r4);
        r0 = r22;
        r4 = r0.length;
        r10 = 2;
        if (r4 < r10) goto L_0x004d;
    L_0x0035:
        r4 = 0;
        r4 = r22[r4];
        r4 = r4.length();
        if (r4 <= 0) goto L_0x004d;
    L_0x003e:
        r4 = 1;
        r4 = r22[r4];
        r4 = r4.length();
        if (r4 <= 0) goto L_0x004d;
    L_0x0047:
        r4 = 0;
        r27 = r22[r4];
        r4 = 1;
        r25 = r22[r4];
    L_0x004d:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "ps:";
        r4 = r4.append(r10);
        r0 = r27;
        r4 = r4.append(r0);
        r10 = " userid:";
        r4 = r4.append(r10);
        r0 = r25;
        r4 = r4.append(r0);
        r4 = r4.toString();
        com.gemini.play.MGplayer.MyPrintln(r4);
        if (r27 == 0) goto L_0x00b8;
    L_0x0073:
        r4 = "geminipassword7";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x009b;
    L_0x007d:
        r4 = "geminipassword8";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x009b;
    L_0x0087:
        r4 = "geminipassword5";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x009b;
    L_0x0091:
        r4 = "geminipassword9";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x00b8;
    L_0x009b:
        r23 = 0;
        if (r27 == 0) goto L_0x02b3;
    L_0x009f:
        r4 = "geminipasswordlocalkey";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x02b3;
    L_0x00a9:
        r4 = new com.gemini.play.LIVEplayer$21;
        r0 = r28;
        r1 = r29;
        r4.<init>(r8, r9, r0, r1);
        r4.start();
        r4 = 0;
        goto L_0x001a;
    L_0x00b8:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "szysx";
        r4 = r4.equals(r10);
        if (r4 != 0) goto L_0x0100;
    L_0x00c4:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "dhtv";
        r4 = r4.equals(r10);
        if (r4 != 0) goto L_0x0100;
    L_0x00d0:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "familytv";
        r4 = r4.equals(r10);
        if (r4 != 0) goto L_0x0100;
    L_0x00dc:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "smallseven";
        r4 = r4.equals(r10);
        if (r4 != 0) goto L_0x0100;
    L_0x00e8:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "turbotv";
        r4 = r4.equals(r10);
        if (r4 != 0) goto L_0x0100;
    L_0x00f4:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "anko";
        r4 = r4.equals(r10);
        if (r4 == 0) goto L_0x0146;
    L_0x0100:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "$user=$mac=";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.tv;
        r10 = r10.GetMac();
        r4 = r4.append(r10);
        r10 = "$playkey=$username=$channelid=$columnid=$vodid=$key=";
        r4 = r4.append(r10);
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r12 = com.gemini.play.MGplayer.tv;
        r12 = r12.GetMac();
        r10 = r10.append(r12);
        r12 = com.gemini.play.MGplayer.tv;
        r12 = r12.getCpuID();
        r10 = r10.append(r12);
        r10 = r10.toString();
        r10 = com.gemini.play.MGplayer.MD5(r10);
        r4 = r4.append(r10);
        r25 = r4.toString();
        goto L_0x009b;
    L_0x0146:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "huanqiu";
        r4 = r4.equals(r10);
        if (r4 != 0) goto L_0x015e;
    L_0x0152:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "jptv";
        r4 = r4.equals(r10);
        if (r4 == 0) goto L_0x01be;
    L_0x015e:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "$user=$mac=";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.tv;
        r10 = r10.GetMac();
        r4 = r4.append(r10);
        r10 = "$username=$channelid=$columnid=$vodid=$key=";
        r4 = r4.append(r10);
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r12 = com.gemini.play.MGplayer.tv;
        r12 = r12.GetMac();
        r10 = r10.append(r12);
        r12 = com.gemini.play.MGplayer.tv;
        r12 = r12.getCpuID();
        r10 = r10.append(r12);
        r12 = com.gemini.play.MGplayer.ip;
        r10 = r10.append(r12);
        r10 = r10.toString();
        r10 = com.gemini.play.MGplayer.MD5(r10);
        r4 = r4.append(r10);
        r10 = "$playkey=";
        r4 = r4.append(r10);
        r12 = com.gemini.play.MGplayer.seconds;
        r10 = java.lang.String.valueOf(r12);
        r10 = com.gemini.play.MGplayer.key(r10);
        r4 = r4.append(r10);
        r25 = r4.toString();
        goto L_0x009b;
    L_0x01be:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "52home";
        r4 = r4.equals(r10);
        if (r4 == 0) goto L_0x020a;
    L_0x01ca:
        if (r27 == 0) goto L_0x009b;
    L_0x01cc:
        r4 = "geminipassword6";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x009b;
    L_0x01d6:
        r4 = r8.length;
        r10 = 2;
        if (r4 < r10) goto L_0x01f2;
    L_0x01da:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r25;
        r4 = r4.append(r0);
        r10 = 0;
        r10 = r8[r10];
        r4 = r4.append(r10);
        r25 = r4.toString();
        goto L_0x009b;
    L_0x01f2:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r25;
        r4 = r4.append(r0);
        r10 = 3;
        r10 = r9[r10];
        r4 = r4.append(r10);
        r25 = r4.toString();
        goto L_0x009b;
    L_0x020a:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "lookiptv";
        r4 = r4.equals(r10);
        r10 = 1;
        if (r4 == r10) goto L_0x023d;
    L_0x0217:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "xiaoqi";
        r4 = r4.equals(r10);
        r10 = 1;
        if (r4 == r10) goto L_0x023d;
    L_0x0224:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "aikanvip";
        r4 = r4.equals(r10);
        r10 = 1;
        if (r4 == r10) goto L_0x023d;
    L_0x0231:
        r4 = com.gemini.play.MGplayer.custom();
        r10 = "huaren";
        r4 = r4.equals(r10);
        if (r4 == 0) goto L_0x009b;
    L_0x023d:
        r4 = "&userid=$username=";
        r0 = r26;
        r4 = r0.indexOf(r4);
        if (r4 >= 0) goto L_0x009b;
    L_0x0247:
        r4 = com.gemini.play.MGplayer.tv;
        r20 = r4.GetMac();
        r4 = com.gemini.play.MGplayer.tv;
        r18 = r4.getCpuID();
        r4 = com.gemini.custom.lookiptv.mac;
        if (r4 == 0) goto L_0x0259;
    L_0x0257:
        r20 = com.gemini.custom.lookiptv.mac;
    L_0x0259:
        r4 = com.gemini.custom.lookiptv.cpuid;
        if (r4 == 0) goto L_0x025f;
    L_0x025d:
        r18 = com.gemini.custom.lookiptv.cpuid;
    L_0x025f:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "$user=$mac=";
        r4 = r4.append(r10);
        r0 = r20;
        r4 = r4.append(r0);
        r10 = "$username=$channelid=$columnid=$vodid=$key=";
        r4 = r4.append(r10);
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r0 = r20;
        r10 = r10.append(r0);
        r0 = r18;
        r10 = r10.append(r0);
        r12 = com.gemini.play.MGplayer.ip;
        r10 = r10.append(r12);
        r10 = r10.toString();
        r10 = com.gemini.play.MGplayer.MD5(r10);
        r4 = r4.append(r10);
        r10 = "$playkey=";
        r4 = r4.append(r10);
        r12 = com.gemini.play.MGplayer.seconds;
        r10 = java.lang.String.valueOf(r12);
        r10 = com.gemini.play.MGplayer.key(r10);
        r4 = r4.append(r10);
        r25 = r4.toString();
        goto L_0x009b;
    L_0x02b3:
        if (r27 == 0) goto L_0x02ef;
    L_0x02b5:
        r4 = "geminipassword4";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x02ef;
    L_0x02bf:
        if (r25 == 0) goto L_0x02ef;
    L_0x02c1:
        r4 = "http://";
        r0 = r25;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x02d5;
    L_0x02cb:
        r4 = "https://";
        r0 = r25;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x02ef;
    L_0x02d5:
        r4 = "@";
        r0 = r25;
        r5 = r0.split(r4);
        r4 = r5.length;
        r10 = 2;
        if (r4 != r10) goto L_0x02ec;
    L_0x02e1:
        r4 = 0;
        p2p_password = r4;
        r4 = new com.gemini.play.LIVEplayer$22;
        r4.<init>(r5);
        r4.start();
    L_0x02ec:
        r4 = 0;
        goto L_0x001a;
    L_0x02ef:
        if (r27 == 0) goto L_0x0346;
    L_0x02f1:
        r4 = "geminipassword5";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0346;
    L_0x02fb:
        if (r25 == 0) goto L_0x0346;
    L_0x02fd:
        r4 = "http://";
        r0 = r25;
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0311;
    L_0x0307:
        r4 = "https://";
        r0 = r25;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0346;
    L_0x0311:
        r4 = "@";
        r0 = r25;
        r5 = r0.split(r4);
        r24 = 0;
        r19 = 0;
        r4 = r5.length;
        r10 = 3;
        if (r4 < r10) goto L_0x032f;
    L_0x0321:
        r4 = r5.length;
        r10 = 4;
        if (r4 < r10) goto L_0x0328;
    L_0x0325:
        r4 = 3;
        r24 = r5[r4];
    L_0x0328:
        r4 = r5.length;
        r10 = 5;
        if (r4 < r10) goto L_0x032f;
    L_0x032c:
        r4 = 4;
        r19 = r5[r4];
    L_0x032f:
        r7 = r19;
        r4 = r5.length;
        r10 = 3;
        if (r4 < r10) goto L_0x0343;
    L_0x0335:
        r6 = r24;
        r4 = new com.gemini.play.LIVEplayer$23;
        r10 = r28;
        r11 = r29;
        r4.<init>(r5, r6, r7, r8, r9, r10, r11);
        r4.start();
    L_0x0343:
        r4 = 0;
        goto L_0x001a;
    L_0x0346:
        if (r27 == 0) goto L_0x0365;
    L_0x0348:
        r4 = "geminipassword7";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0365;
    L_0x0352:
        r11 = r25;
        r10 = new com.gemini.play.LIVEplayer$24;
        r12 = r8;
        r13 = r9;
        r14 = r28;
        r15 = r29;
        r10.<init>(r11, r12, r13, r14, r15);
        r10.start();
        r4 = 0;
        goto L_0x001a;
    L_0x0365:
        if (r27 == 0) goto L_0x03aa;
    L_0x0367:
        r4 = "geminipassword8";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x03aa;
    L_0x0371:
        r4 = "@";
        r0 = r25;
        r5 = r0.split(r4);
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "userid = ";
        r4 = r4.append(r10);
        r0 = r25;
        r4 = r4.append(r0);
        r4 = r4.toString();
        com.gemini.play.MGplayer.MyPrintln(r4);
        r4 = r5.length;
        r10 = 2;
        if (r4 >= r10) goto L_0x0398;
    L_0x0395:
        r4 = 0;
        goto L_0x001a;
    L_0x0398:
        r12 = new com.gemini.play.LIVEplayer$25;
        r13 = r5;
        r14 = r8;
        r15 = r9;
        r16 = r28;
        r17 = r29;
        r12.<init>(r13, r14, r15, r16, r17);
        r12.start();
        r4 = 0;
        goto L_0x001a;
    L_0x03aa:
        if (r27 == 0) goto L_0x03d3;
    L_0x03ac:
        r4 = "geminipassword9";
        r0 = r27;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x03d3;
    L_0x03b6:
        r4 = "@";
        r0 = r25;
        r5 = r0.split(r4);
        r4 = r5.length;
        r10 = 3;
        if (r4 < r10) goto L_0x03d0;
    L_0x03c2:
        r4 = new com.gemini.play.LIVEplayer$26;
        r0 = r29;
        r4.<init>(r5, r8, r9, r0);
        geminipassword9_thread = r4;
        r4 = geminipassword9_thread;
        r4.start();
    L_0x03d0:
        r4 = 0;
        goto L_0x001a;
    L_0x03d3:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "mac:";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.tv;
        r10 = r10.GetMac();
        r4 = r4.append(r10);
        r10 = "cpuid:";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.tv;
        r10 = r10.getCpuID();
        r4 = r4.append(r10);
        r10 = "ip:";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.ip;
        r4 = r4.append(r10);
        r10 = " playVideo_p2p = ";
        r4 = r4.append(r10);
        r0 = r25;
        r4 = r4.append(r0);
        r4 = r4.toString();
        com.gemini.play.MGplayer.MyPrintln(r4);
        if (r27 == 0) goto L_0x0427;
    L_0x0419:
        r4 = "";
        r0 = r27;
        if (r0 == r4) goto L_0x0427;
    L_0x041f:
        if (r27 == 0) goto L_0x04b3;
    L_0x0421:
        r4 = r27.length();
        if (r4 > 0) goto L_0x04b3;
    L_0x0427:
        if (r25 != 0) goto L_0x04a4;
    L_0x0429:
        r4 = 0;
        r4 = r8[r4];
        r10 = 2;
        r10 = r9[r10];
        r12 = com.gemini.play.MGplayer.tv;
        r12 = r12.GetMac();
        r0 = r28;
        r23 = com.gemini.play.MGplayer.s0(r4, r10, r12, r0);
    L_0x043b:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "playvideo password ";
        r4 = r4.append(r10);
        r0 = r27;
        r4 = r4.append(r0);
        r10 = " p2p ret:";
        r4 = r4.append(r10);
        r0 = r23;
        r4 = r4.append(r0);
        r4 = r4.toString();
        com.gemini.play.MGplayer.MyPrintln(r4);
        r4 = com.gemini.play.MGplayer.getCpuName();
        r10 = "AML8726";
        r4 = r4.equals(r10);
        if (r4 == 0) goto L_0x0502;
    L_0x046b:
        r4 = r8.length;
        r10 = 2;
        if (r4 < r10) goto L_0x04dd;
    L_0x046f:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "http://127.0.0.1:";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.port();
        r4 = r4.append(r10);
        r10 = "/";
        r4 = r4.append(r10);
        r10 = 0;
        r10 = r8[r10];
        r4 = r4.append(r10);
        r10 = ".";
        r4 = r4.append(r10);
        r10 = 1;
        r10 = r8[r10];
        r4 = r4.append(r10);
        r21 = r4.toString();
    L_0x04a0:
        r4 = r21;
        goto L_0x001a;
    L_0x04a4:
        r4 = 0;
        r4 = r8[r4];
        r10 = 2;
        r10 = r9[r10];
        r0 = r25;
        r1 = r28;
        r23 = com.gemini.play.MGplayer.s0(r4, r10, r0, r1);
        goto L_0x043b;
    L_0x04b3:
        if (r25 != 0) goto L_0x04cb;
    L_0x04b5:
        r4 = 0;
        r4 = r8[r4];
        r10 = 2;
        r10 = r9[r10];
        r12 = com.gemini.play.MGplayer.tv;
        r12 = r12.GetMac();
        r0 = r27;
        r1 = r28;
        r23 = com.gemini.play.MGplayer.s1(r4, r10, r0, r12, r1);
        goto L_0x043b;
    L_0x04cb:
        r4 = 0;
        r4 = r8[r4];
        r10 = 2;
        r10 = r9[r10];
        r0 = r27;
        r1 = r25;
        r2 = r28;
        r23 = com.gemini.play.MGplayer.s1(r4, r10, r0, r1, r2);
        goto L_0x043b;
    L_0x04dd:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "http://127.0.0.1:";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.port();
        r4 = r4.append(r10);
        r10 = "/";
        r4 = r4.append(r10);
        r10 = 3;
        r10 = r9[r10];
        r4 = r4.append(r10);
        r21 = r4.toString();
        goto L_0x04a0;
    L_0x0502:
        r4 = com.gemini.play.MGplayer.getCpuName();
        r10 = "HI3716M";
        r4 = r4.equals(r10);
        if (r4 == 0) goto L_0x056b;
    L_0x050e:
        r4 = r8.length;
        r10 = 2;
        if (r4 < r10) goto L_0x0545;
    L_0x0512:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "http://127.0.0.1:";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.port();
        r4 = r4.append(r10);
        r10 = "/";
        r4 = r4.append(r10);
        r10 = 0;
        r10 = r8[r10];
        r4 = r4.append(r10);
        r10 = ".";
        r4 = r4.append(r10);
        r10 = 1;
        r10 = r8[r10];
        r4 = r4.append(r10);
        r21 = r4.toString();
        goto L_0x04a0;
    L_0x0545:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "http://127.0.0.1:";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.port();
        r4 = r4.append(r10);
        r10 = "/";
        r4 = r4.append(r10);
        r10 = 3;
        r10 = r9[r10];
        r4 = r4.append(r10);
        r21 = r4.toString();
        goto L_0x04a0;
    L_0x056b:
        r4 = r8.length;
        r10 = 2;
        if (r4 < r10) goto L_0x05a2;
    L_0x056f:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "http://127.0.0.1:";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.port();
        r4 = r4.append(r10);
        r10 = "/";
        r4 = r4.append(r10);
        r10 = 0;
        r10 = r8[r10];
        r4 = r4.append(r10);
        r10 = ".";
        r4 = r4.append(r10);
        r10 = 1;
        r10 = r8[r10];
        r4 = r4.append(r10);
        r21 = r4.toString();
        goto L_0x04a0;
    L_0x05a2:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r10 = "http://127.0.0.1:";
        r4 = r4.append(r10);
        r10 = com.gemini.play.MGplayer.port();
        r4 = r4.append(r10);
        r10 = "/";
        r4 = r4.append(r10);
        r10 = 3;
        r10 = r9[r10];
        r4 = r4.append(r10);
        r21 = r4.toString();
        goto L_0x04a0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gemini.play.LIVEplayer.playVideo_p2p(java.lang.String, java.lang.String, int, android.os.Handler):java.lang.String");
    }

    private static void saveCurrentID(String id) {
        if (id != null) {
            Editor editor = MGplayer.MyGetSharedPreferences(MGplayer._this, "data", 0).edit();
            editor.putString("current_id", id);
            editor.commit();
        }
    }

    public static String getCurrentID() {
        return MGplayer.MyGetSharedPreferences(MGplayer._this, "data", 0).getString("current_id", null);
    }

    public static String[] getMergeArray(String[] al, String[] bl) {
        String[] a = al;
        String[] b = bl;
        String[] c = new String[(a.length + b.length)];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static int getVideoUrlHighCount(String urlss) {
        String[] urls = urlss.split("geminihighlowgemini");
        if (urls.length < 1) {
            MGplayer.MyPrintln("getVideoUrlHighCount 1");
            return 1;
        }
        String[] high_urls = urls[0].split("\\|");
        MGplayer.MyPrintln("getVideoUrlHighCount " + high_urls.length);
        return high_urls.length;
    }

    public static int getVideoUrlLowCount(String urlss) {
        String[] urls = urlss.split("geminihighlowgemini");
        if (urls.length < 2) {
            return 0;
        }
        String[] low_urls = urls[1].split("\\|");
        MGplayer.MyPrintln("getVideoUrlLowCount " + low_urls.length);
        return low_urls.length;
    }

    public static String getVideoUrlFromUrlss(String urlss, int line) {
        String[] urls = urlss.split("geminihighlowgemini");
        ArrayList<String> url = new ArrayList();
        if (urls.length < 1) {
            MGplayer.MyPrintln("getVideoUrlHighCount 1");
            url.add(urlss);
        } else {
            int ii;
            String[] high_urls = urls[0].split("\\|");
            MGplayer.MyPrintln("getVideoUrlHighCount " + high_urls.length);
            for (ii = 0; ii < high_urls.length; ii++) {
                if (high_urls[ii].length() > 7) {
                    url.add(high_urls[ii]);
                }
            }
            if (urls.length >= 2) {
                String[] low_urls = urls[1].split("\\|");
                MGplayer.MyPrintln("getVideoUrllowCount " + low_urls.length);
                for (ii = 0; ii < low_urls.length; ii++) {
                    if (low_urls[ii].length() > 7) {
                        url.add(low_urls[ii]);
                    }
                }
            }
        }
        MGplayer.MyPrintln("getVideoUrl " + url.size());
        String[] u;
        if (url.size() > 0 && url.size() > line && line >= 0) {
            currentLine = line;
            u = ((String) url.get(currentLine)).split("#");
            if (u.length >= 2) {
                return u[1];
            }
            return (String) url.get(0);
        } else if (url.size() > 0 && line < 0) {
            currentLine = url.size() - 1;
            u = ((String) url.get(currentLine)).split("#");
            if (u.length >= 2) {
                return u[1];
            }
            return (String) url.get(0);
        } else if (url.size() <= 0 || url.size() - 1 >= line) {
            return null;
        } else {
            currentLine = 0;
            u = ((String) url.get(currentLine)).split("#");
            if (u.length >= 2) {
                return u[1];
            }
            return (String) url.get(0);
        }
    }

    public static String getVideoPassFromPassTmpss(String password_tmpss, int line) {
        String[] passwords = password_tmpss.split("geminihighlowgemini");
        ArrayList<String> password = new ArrayList();
        if (passwords.length < 1) {
            MGplayer.MyPrintln("getVideoUrlHighCount 1");
            password.add(password_tmpss);
        } else {
            String[] high_passwords = passwords[0].split("\\|");
            MGplayer.MyPrintln("getVideoUrlHighCount " + high_passwords.length);
            for (Object add : high_passwords) {
                password.add(add);
            }
            if (passwords.length >= 2) {
                String[] low_passwords = passwords[1].split("\\|");
                MGplayer.MyPrintln("getVideoUrllowCount " + low_passwords.length);
                for (Object add2 : low_passwords) {
                    password.add(add2);
                }
            }
        }
        String[] u;
        if (password.size() > line) {
            u = ((String) password.get(line)).split("#");
            if (u.length >= 2) {
                return u[1];
            }
            return null;
        }
        u = ((String) password.get(0)).split("#");
        return u.length >= 2 ? u[1] : null;
    }

    public static int getVideoLineCount(String id) {
        if (!MGplayer.isNumeric(id)) {
            return 0;
        }
        String urlss = MGplayer.ju(getVideoUrl(Integer.parseInt(id)));
        return getVideoUrlHighCount(urlss) + getVideoUrlLowCount(urlss);
    }

    public static boolean isPlaying() {
        if (MGplayer.getDecode() == 0 && VideoViewH != null) {
            return VideoViewH.isPlaying();
        }
        if (MGplayer.getDecode() == 3 && VideoViewH2 != null) {
            return VideoViewH2.isPlaying();
        }
        if (MGplayer.getDecode() == 1 && VideoViewS != null) {
            return VideoViewS.isPlaying();
        }
        if (MGplayer.getDecode() != 2 || VideoViewS2 == null) {
            return false;
        }
        return VideoViewS2.isPlaying();
    }

    public static boolean isBackplayUrl(String url) {
        if (url == null) {
            return false;
        }
        if (url != null && !url.startsWith("gemini://") && !url.startsWith("gp2p://")) {
            return false;
        }
        if (!url.startsWith("gemini://") && !url.startsWith("gp2p://")) {
            return false;
        }
        String[] v1 = url.split("\\?");
        if (v1.length < 2) {
            return false;
        }
        String[] v2 = v1[1].split("&");
        if (v2.length < 2) {
            return false;
        }
        String[] v3 = v2[0].split("=");
        if (v3.length < 2 || !MGplayer.isNumeric(v3[1]) || Integer.parseInt(v3[1]) <= 0) {
            return false;
        }
        return true;
    }

    public static void stopCheckVideo() {
        check_playing_running = false;
        MGplayer.MyPrintln("live check_playing_running false");
    }

    public static void stopStatus() {
        check_isplaying = 0;
    }

    public static int get_next_line(String currentID, int currentLine) {
        if (MGplayer.liveplaytimeout <= 0) {
            return currentLine;
        }
        int line = currentLine + 1;
        if (line >= getVideoLineCount(currentID)) {
            line = 0;
        }
        MGplayer.MyPrintln("live get_next_line " + line);
        return line;
    }

    public static void checkVideo() {
        if (!check_playing_running) {
            check_playing_running = true;
            check_playing_currentid = null;
            check_playing_line = 0;
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (LIVEplayer.check_playing_running) {
                        MGplayer.MyPrintln("live check_playing_running true " + LIVEplayer.currentURL);
                        if (!(LIVEplayer.currentURL == null || LIVEplayer.currentURL.startsWith("p2p://") || LIVEplayer.currentURL.startsWith("forcetv://") || !LIVEplayer.isUseHlsPlugin(LIVEplayer.currentURL))) {
                            int seek = MGplayer.mediaplayerreopen();
                            if (seek >= 0) {
                                MGplayer.MyPrintln("=============mediaplayerreopen============ = " + seek);
                            }
                            if (seek >= 1) {
                                if (MGplayer.getDecode() == 0) {
                                    LIVEplayer.playVideoForHard(LIVEplayer.VideoViewH, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), seek, null, null);
                                } else if (MGplayer.getDecode() == 3) {
                                    LIVEplayer.playVideoForHard2(LIVEplayer.VideoViewH2, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), seek, null, null);
                                } else if (MGplayer.getDecode() == 1) {
                                    if (LIVEplayer.VideoViewS != null) {
                                        LIVEplayer.playVideoForSoft(LIVEplayer.VideoViewS, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), seek, null, null);
                                    }
                                } else if (MGplayer.getDecode() == 2 && LIVEplayer.VideoViewS2 != null) {
                                    LIVEplayer.playVideoForSoft2(LIVEplayer.VideoViewS2, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), seek, null, null);
                                }
                            }
                        }
                        if (LIVEplayer.currentURL != null) {
                            long speed_v;
                            long currentPosition;
                            if (MGplayer.getDecode() == 0) {
                                MGplayer.MyPrintln("BufferPercentage:" + LIVEplayer.VideoViewH.getBufferPercentage());
                                if (LIVEplayer.VideoViewH.isPlaying() || LIVEplayer.check_playing_currentid == null || !LIVEplayer.check_playing_currentid.equals(LIVEplayer.currentID) || LIVEplayer.check_playing_line != LIVEplayer.currentLine) {
                                    LIVEplayer.check_playing_currentid = LIVEplayer.currentID;
                                    LIVEplayer.check_playing_line = LIVEplayer.currentLine;
                                    LIVEplayer.check_playing_times = 0;
                                    if ((LIVEplayer.currentURL.startsWith("http://") || LIVEplayer.currentURL.startsWith("rtsp://")) && LIVEplayer.VideoViewH.isPlaying() && MGplayer.liveplaytimeout > 5 && LIVEplayer.check_playing_currentid != null && LIVEplayer.check_playing_currentid.equals(LIVEplayer.currentID) && LIVEplayer.check_playing_line == LIVEplayer.currentLine) {
                                        speed_v = 0;
                                        currentPosition = 0;
                                        if (LIVEplayer.VideoViewH.isPlaying()) {
                                            speed_v = MGplayer.getUidRxBytes();
                                            currentPosition = (long) LIVEplayer.VideoViewH.getCurrentPosition();
                                        }
                                        MGplayer.MyPrintln("MGplayer.getUidRxBytes = " + speed_v + " speed_nothings_times=" + LIVEplayer.speed_nothings_times);
                                        if (LIVEplayer.check_currentposition == -1) {
                                            LIVEplayer.check_currentposition = currentPosition;
                                        }
                                        if (speed_v >= 15 || LIVEplayer.check_currentposition != currentPosition) {
                                            LIVEplayer.check_currentposition = currentPosition;
                                            LIVEplayer.speed_nothings_times = 0;
                                        } else {
                                            LIVEplayer.speed_nothings_times++;
                                        }
                                        if (LIVEplayer.speed_nothings_times > 6) {
                                            MGplayer.MyPrintln("MGplayer.getUidRxBytes Reset Play");
                                            LIVEplayer.playVideoForHard(LIVEplayer.VideoViewH, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                                            MGplayer.MyPrintln("MGplayer.getUidRxBytes Reset Play 2");
                                            LIVEplayer.speed_nothings_times = 0;
                                            LIVEplayer.check_currentposition = -1;
                                        }
                                    }
                                } else {
                                    LIVEplayer.speed_nothings_times = 0;
                                    int check_playing_timeout = 12;
                                    if (MGplayer.liveplaytimeout > 5 && !LIVEplayer.currentURL.startsWith("gp2p://")) {
                                        check_playing_timeout = MGplayer.liveplaytimeout / 5;
                                    }
                                    LIVEplayer.check_playing_times++;
                                    if (LIVEplayer.check_playing_times >= check_playing_timeout) {
                                        LIVEplayer.check_currentposition = -1;
                                        LIVEplayer.check_playing_times = 0;
                                        MGplayer.MyPrintln("live check_playing_running timeout " + LIVEplayer.currentURL);
                                        LIVEplayer.playVideoForHard(LIVEplayer.VideoViewH, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                                    }
                                }
                            } else if (MGplayer.getDecode() != 3) {
                                if (MGplayer.getDecode() == 1) {
                                    if (LIVEplayer.VideoViewS.isPlaying() || LIVEplayer.check_playing_currentid == null || !LIVEplayer.check_playing_currentid.equals(LIVEplayer.currentID) || LIVEplayer.check_playing_line != LIVEplayer.currentLine) {
                                        LIVEplayer.check_playing_currentid = LIVEplayer.currentID;
                                        LIVEplayer.check_playing_line = LIVEplayer.currentLine;
                                        LIVEplayer.check_playing_times = 0;
                                        if ((LIVEplayer.currentURL.startsWith("http://") || LIVEplayer.currentURL.startsWith("rtsp://")) && LIVEplayer.VideoViewS.isPlaying() && MGplayer.liveplaytimeout > 5) {
                                            speed_v = MGplayer.getUidRxBytes();
                                            currentPosition = LIVEplayer.VideoViewS.getCurrentPosition();
                                            MGplayer.MyPrintln("currentPosition = " + currentPosition + " " + LIVEplayer.VideoViewS.getBufferPercentage());
                                            if (LIVEplayer.check_currentposition == -1) {
                                                LIVEplayer.check_currentposition = currentPosition;
                                            }
                                            if (speed_v >= 15 || LIVEplayer.check_currentposition != currentPosition) {
                                                LIVEplayer.check_currentposition = currentPosition;
                                                LIVEplayer.speed_nothings_times = 0;
                                            } else {
                                                LIVEplayer.speed_nothings_times++;
                                            }
                                            if (LIVEplayer.speed_nothings_times > 18) {
                                                MGplayer.MyPrintln("soft Reset Play2 ");
                                                LIVEplayer.playVideoForSoft(LIVEplayer.VideoViewS, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                                                LIVEplayer.speed_nothings_times = 0;
                                                LIVEplayer.check_currentposition = -1;
                                            }
                                        }
                                    } else {
                                        LIVEplayer.speed_nothings_times = 0;
                                        MGplayer.MyPrintln("check_playing_times:" + LIVEplayer.check_playing_times);
                                        LIVEplayer.check_playing_times++;
                                        if (LIVEplayer.check_playing_times > 18) {
                                            LIVEplayer.check_currentposition = -1;
                                            LIVEplayer.check_playing_times = 0;
                                            MGplayer.MyPrintln("soft Reset Play1 ");
                                            LIVEplayer.playVideoForSoft(LIVEplayer.VideoViewS, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                                        }
                                    }
                                } else if (MGplayer.getDecode() == 2) {
                                    if (LIVEplayer.VideoViewS2.isPlaying() || LIVEplayer.check_playing_currentid == null || !LIVEplayer.check_playing_currentid.equals(LIVEplayer.currentID) || LIVEplayer.check_playing_line != LIVEplayer.currentLine) {
                                        LIVEplayer.check_playing_currentid = LIVEplayer.currentID;
                                        LIVEplayer.check_playing_line = LIVEplayer.currentLine;
                                        LIVEplayer.check_playing_times = 0;
                                        if ((LIVEplayer.currentURL.startsWith("http://") || LIVEplayer.currentURL.startsWith("rtsp://")) && LIVEplayer.VideoViewS2.isPlaying() && MGplayer.liveplaytimeout > 5) {
                                            speed_v = MGplayer.getUidRxBytes();
                                            currentPosition = LIVEplayer.VideoViewS2.getCurrentPosition();
                                            MGplayer.MyPrintln("currentPosition = " + currentPosition + " " + LIVEplayer.VideoViewS2.getBufferPercentage());
                                            if (LIVEplayer.check_currentposition == -1) {
                                                LIVEplayer.check_currentposition = currentPosition;
                                            }
                                            if (speed_v >= 15 || LIVEplayer.check_currentposition != currentPosition) {
                                                LIVEplayer.check_currentposition = currentPosition;
                                                LIVEplayer.speed_nothings_times = 0;
                                            } else {
                                                LIVEplayer.speed_nothings_times++;
                                            }
                                            if (LIVEplayer.speed_nothings_times > 18) {
                                                MGplayer.MyPrintln("soft Reset Play2 ");
                                                LIVEplayer.playVideoForSoft2(LIVEplayer.VideoViewS2, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                                                LIVEplayer.speed_nothings_times = 0;
                                                LIVEplayer.check_currentposition = -1;
                                            }
                                        }
                                    } else {
                                        LIVEplayer.speed_nothings_times = 0;
                                        MGplayer.MyPrintln("check_playing_times:" + LIVEplayer.check_playing_times);
                                        LIVEplayer.check_playing_times++;
                                        if (LIVEplayer.check_playing_times > 18) {
                                            LIVEplayer.check_currentposition = -1;
                                            LIVEplayer.check_playing_times = 0;
                                            MGplayer.MyPrintln("soft Reset Play1 ");
                                            LIVEplayer.playVideoForSoft2(LIVEplayer.VideoViewS2, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                                        }
                                    }
                                }
                            }
                        }
                        if (LIVEplayer.check_playing_running) {
                            mHandler.postDelayed(this, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                        }
                    }
                }
            }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
        }
    }

    public static int selectDecode(String url, String password) {
        MGplayer.MyPrintln("getDecode:" + MGplayer.getDecode() + " getPreDecode:" + MGplayer.getPreDecode());
        if (MGplayer.getDecode() == 3) {
            stopVideoForHard2(VideoViewH2);
            if (MGplayer.getPreDecode() == 3) {
                MGplayer.setDecode(0);
                MGplayer.setPreDecode(0);
            } else if (MGplayer.getPreDecode() >= 0) {
                MGplayer.setDecode(MGplayer.getPreDecode());
            }
        } else if (MGplayer.getPreDecode() == 3) {
            MGplayer.setPreDecode(MGplayer.getDecode());
        } else if (MGplayer.getDecode() == -1 && MGplayer.getPreDecode() == -1) {
            MGplayer.setDecode(0);
        }
        if ((url == null || !url.startsWith("http://") || password == null || !password.equals("passwordexo")) && (url == null || !url.startsWith("gp2p://") || password == null || !password.equals("passwordexo"))) {
            int inx = MGplayer.getPreDecode();
            if (inx < 0) {
                return MGplayer.getDecode();
            }
            MGplayer.setDecode(inx);
            return inx;
        }
        if (MGplayer.getDecode() != 3) {
            MGplayer.setPreDecode(MGplayer.getDecode());
        } else {
            MGplayer.setPreDecode(0);
        }
        MGplayer.setDecode(3);
        return 3;
    }

    public static void resumeDecode() {
        if (MGplayer.getDecode() == 3) {
            stopVideoForHard2(VideoViewH2);
            if (MGplayer.getPreDecode() == 3) {
                MGplayer.setDecode(0);
                MGplayer.setPreDecode(0);
            } else if (MGplayer.getPreDecode() >= 0) {
                MGplayer.setDecode(MGplayer.getPreDecode());
            }
        }
        MGplayer.setPreDecode(-1);
    }
}
