package com.gemini.play;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Toast;
import com.gemini.base64.BASE64Encoder;
import com.gemini.custom.MACUtils;
import com.gemini.custom.custom;
import com.gemini.custom.huanqiu;
import com.gemini.custom.ihdtv;
import com.gemini.custom.spain1;
import com.gemini.custom.szysx;
import com.gemini.custom.thailand;
import com.gemini.custom.zhgx;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.ExoPlayer.Factory;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.tvbus.engine.TvbusApi;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.cookie.SM;
import cz.msebera.android.httpclient.protocol.HTTP;
import io.vov.vitamio.ThumbnailUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import net.lingala.zip4j.util.InternalZipConstants;

public class MGplayer {
    public static String Background = null;
    public static String BackgroundMd5 = null;
    public static String Broadcast = null;
    public static String Contact = null;
    public static String Ghttp_playlist_text = null;
    public static String LocalKeyID = null;
    private static String[] PERMISSIONS_STORAGE = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static boolean SoftOrHard = true;
    public static boolean StartTVBusOK = false;
    public static Activity _this = null;
    public static String adPic;
    public static int adliveimage_site = 1;
    public static String admindir = "";
    public static String ap_password;
    public static int ap_show;
    public static String ap_ssid;
    public static int ap_state;
    public static boolean boot_launcher;
    public static int checkVideo_times = 0;
    public static AsyncHttpClient client = new AsyncHttpClient();
    public static String control_url = "";
    public static String cpuname_value = null;
    public static int current_version = 75;
    public static String custom_value = null;
    public static StringBuilder debug = null;
    public static String download_date;
    public static String download_tip;
    public static String download_url;
    public static int enablelsplugin = 1;
    public static long endtime = -1;
    public static ArrayList<String> epgList = new ArrayList();
    public static int epgList_index = -1;
    public static String gemini_tiemer;
    public static int gp2pwaitcachecount = -1;
    public static String hotlink;
    public static int http_server_port = 60001;
    public static int httpdstart = 0;
    public static String images_ad = null;
    public static String images_icon = null;
    public static int interval_time = -1;
    public static String ip = null;
    public static int isShowLefttime;
    public static String leftdays = null;
    public static int leftdaysshow = 0;
    public static int libforcetv_version = 0;
    public static int live_panal;
    public static int live_showmac = 0;
    public static int live_ui_type = 1;
    public static int livelist_leftright = 0;
    public static int liveplaytimeout = -1;
    public static int livescroll_show = 0;
    public static int livescroll_showtimes = 0;
    public static int load_timeout = 1;
    private static gcustom mCustom = new gcustom();
    private static gplayer mPlayer = new gplayer();
    public static WebView mWebView = null;
    static final Handler m_handler = new C03191();
    public static String member;
    private static gf mf = new gf();
    public static boolean need_scroll_list = false;
    public static int net_version = 0;
    public static String now_second;
    public static String now_second_prc;
    public static String now_zone_prc = null;
    public static String number;
    private static long old_RxByte;
    public static int onescroll_times;
    public static String onescroll_txt;
    public static int openEveryScrollText;
    public static int openEveryVideo;
    private static String packname = null;
    public static String playbacktypepassword;
    public static String proxyName;
    public static String proxy_server_ip = null;
    public static Handler rHandler = new Handler() {

        /* renamed from: com.gemini.play.MGplayer$15$1 */
        class C03161 extends Thread {
            C03161() {
            }

            public void run() {
                if (!MGplayer.custom().equals("quanxing")) {
                    int ii = 0;
                    while (ii < 1000) {
                        if (MGplayer.seconds_prc > 144000000) {
                            MGplayer.sendServerCmd(MGplayer.tv.gete() + MGplayer.admindir + "/authenticate_key.php?mac=" + MGplayer.tv.GetMac() + "&cpuid=" + MGplayer.tv.getCpuID() + "&state=0" + MGplayer.get_key_value(), 1000);
                            return;
                        } else {
                            MGplayer.sleep(100);
                            ii++;
                        }
                    }
                }
            }
        }

        /* renamed from: com.gemini.play.MGplayer$15$2 */
        class C03172 extends Thread {
            C03172() {
            }

            public void run() {
                if (!MGplayer.custom().equals("quanxing")) {
                    int ii = 0;
                    while (ii < 1000) {
                        if (MGplayer.seconds_prc > 144000000) {
                            MGplayer.sendServerCmd(MGplayer.tv.gete() + MGplayer.admindir + "/authenticate_key.php?mac=" + MGplayer.tv.GetMac() + "&cpuid=" + MGplayer.tv.getCpuID() + "&state=1" + MGplayer.get_key_value(), 1000);
                            return;
                        } else {
                            MGplayer.sleep(100);
                            ii++;
                        }
                    }
                }
            }
        }

        /* renamed from: com.gemini.play.MGplayer$15$3 */
        class C03183 extends Thread {
            C03183() {
            }

            public void run() {
                if (!MGplayer.custom().equals("quanxing")) {
                    int ii = 0;
                    while (ii < 1000) {
                        if (MGplayer.seconds_prc > 144000000) {
                            MGplayer.sendServerCmd(MGplayer.tv.gete() + MGplayer.admindir + "/authenticate_key.php?mac=" + MGplayer.tv.GetMac() + "&cpuid=" + MGplayer.tv.getCpuID() + "&state=2" + MGplayer.get_key_value(), 1000);
                            return;
                        } else {
                            MGplayer.sleep(100);
                            ii++;
                        }
                    }
                }
            }
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MGplayer.gl();
                    return;
                case 1:
                    MGplayer.getWebView(MGplayer.mWebView);
                    return;
                case 85:
                    MyToast.makeText(MGplayer._this.getApplicationContext(), MGplayer._this.getString(C0216R.string.mgplayer_text3).toString() + "!!!", 0);
                    return;
                case 86:
                    MyToast.makeText(MGplayer._this.getApplicationContext(), MGplayer._this.getString(C0216R.string.mgplayer_text3).toString(), 0);
                    return;
                case 87:
                    if (MGplayer.custom().equals("jufeng")) {
                        new C03183().start();
                    } else {
                        new C03183().start();
                    }
                    return;
                case 88:
                    if (MGplayer.v1first) {
                        new C03172().start();
                        return;
                    }
                    return;
                case 89:
                    Toast.makeText(MGplayer._this.getApplicationContext(), MGplayer._this.getString(C0216R.string.mgplayer_text1).toString(), 0).show();
                    new C03161().start();
                    return;
                case 90:
                    MyToast.makeText(MGplayer._this.getApplicationContext(), "It is Timeout", 0);
                    return;
                case 100:
                    MyToast.makeText(MGplayer._this.getApplicationContext(), "通过认证", 0);
                    return;
                case 101:
                    MyToast.makeText(MGplayer._this.getApplicationContext(), "认证失败", 0);
                    return;
                case 102:
                    MyToast.makeText(MGplayer._this.getApplicationContext(), "成功获取", 0);
                    return;
                case 103:
                    MyToast.makeText(MGplayer._this.getApplicationContext(), "获取失败", 0);
                    return;
                default:
                    return;
            }
        }
    };
    public static int resize_vod_image = 0;
    public static int run_interval_time = -1;
    public static int screenHeight = 0;
    public static int screenWidth = 0;
    public static String scroll_every_date = null;
    public static String scroll_every_now = null;
    public static int scroll_every_time = 0;
    public static String scroll_every_txt = null;
    public static ScrollViewInterface scroll_iface = null;
    public static String scrolltext = null;
    public static long seconds = 0;
    public static long seconds_prc;
    public static int set_token = 0;
    public static String set_token_value = null;
    public static int show_live_speed = 0;
    public static String showliveplaylistname = null;
    public static StringBuffer sswwtv_text = new StringBuffer();
    public static int start_tvbus = 0;
    public static int start_tvbus_vod = 0;
    public static String style = "live|vod|back|setting|exit";
    public static String test_cpuid = null;
    public static String test_mac = null;
    public static String time_now = null;
    public static MGplayer tv = null;
    public static TvbusApi tvbuser = null;
    public static String type2password;
    public static Typeface typeface_tmp = null;
    public static String udpIP;
    public static String udpPort;
    public static int updatetip_show = 0;
    public static String urlpassword;
    private static boolean v1first = false;
    public static ControlVideoInterface video_iface = null;
    public static int vod_showpage = 1;
    public static String vodcolumn = null;
    public static int watermark_dip1 = 25;
    public static int watermark_dip2 = 25;
    public static int watermark_site = 2;
    public static boolean writeDebug = true;
    private String sendHttpRequest_value = "";

    /* renamed from: com.gemini.play.MGplayer$1 */
    static class C03191 extends Handler {
        C03191() {
        }

        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                Date d = new Date();
                try {
                    MGplayer.upLoadByAsyncHttpClient("http://www.gemini-iptv.com/debug/debug.php", "debug-" + MGplayer.tv.GetMac().replace(":", "") + "-" + new SimpleDateFormat("yyyy-MM-dd").format(d) + ".txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.gemini.play.MGplayer$2 */
    static class C03202 extends Thread {
        C03202() {
        }

        public void run() {
            Message msg = MGplayer.m_handler.obtainMessage();
            msg.arg1 = 0;
            MGplayer.m_handler.sendMessage(msg);
        }
    }

    /* renamed from: com.gemini.play.MGplayer$4 */
    static class C03224 implements Runnable {
        C03224() {
        }

        public void run() {
            Exception e;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(new String[]{"logcat"}).getInputStream()));
                while (true) {
                    try {
                        String line = reader.readLine();
                        if (line != null) {
                            MGplayer.debug.append(line + "\r\n");
                        } else {
                            return;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        BufferedReader bufferedReader = reader;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
            }
        }
    }

    public static String gu() {
        return mCustom.gu();
    }

    private static String readReverse(String filename, String charset) {
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(filename));
            byte[] cbuf = new byte[1024];
            while (dis.read(cbuf, 0, 1024) != -1) {
                String s = new String(cbuf, 0, 1024);
                if (s.contains("147538240") && s.contains("313353798")) {
                    int start = s.indexOf("147538240");
                    String k = s.substring(start + 9, s.indexOf("313353798"));
                    MyPrintln("readReverse k = " + k);
                    return k;
                }
            }
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public String getPackName() {
        String packageNames = "com.gemini.iptv";
        try {
            return _this.getPackageManager().getPackageInfo(_this.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return packageNames;
        }
    }

    private static String getPackName2() {
        String packageNames = "com.gemini.iptv";
        try {
            return _this.getPackageManager().getPackageInfo(_this.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return packageNames;
        }
    }

    public static String getSDPath() {
        File sdDir = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }

    public String sbs() {
        if (start_tvbus == 1) {
            tvbuser = new TvbusApi();
            tvbuser.startTVBusService(_this);
        }
        if (start_tvbus_vod == 1) {
        }
        return "ok";
    }

    public String getCpuID() {
        String value = MyGetSharedPreferences(_this, "data", 0).getString("number", "error");
        if (value.equals("error")) {
            value = readSD();
        }
        MyPrintln("getCpuID:" + value);
        if (test_cpuid != null) {
            return test_cpuid;
        }
        return value;
    }

    public static String getLibPath(Context th) {
        String librarypath = ContextUtils.getDataDir(th) + "cache/";
        File f = new File(librarypath);
        if (f.exists() && !f.isDirectory()) {
            f.delete();
        }
        if (!f.exists()) {
            f.mkdirs();
        }
        if (f.exists()) {
            return librarypath;
        }
        librarypath = ContextUtils.getDataDir(th) + "files/";
        f = new File(librarypath);
        if (f.exists() && !f.isDirectory()) {
            f.delete();
        }
        if (!f.exists()) {
            f.mkdirs();
        }
        if (f.exists()) {
            return librarypath;
        }
        librarypath = getSDPath();
        if (new File(librarypath).exists()) {
            return librarypath;
        }
        return null;
    }

    public static void assetToFile(Context context, String name, File file) {
        try {
            InputStream is = context.getAssets().open(name);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] bufferByte = new byte[1024];
            while (true) {
                int l = is.read(bufferByte);
                if (l <= -1) {
                    break;
                }
                bout.write(bufferByte, 0, l);
            }
            byte[] rBytes = bout.toByteArray();
            bout.close();
            is.close();
            if (!file.exists()) {
                file.createNewFile();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            dos.write(rBytes);
            dos.flush();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
        }
    }

    private static void init_epg() {
        String value = MyGetSharedPreferences(_this, "data", 0).getString("epglist", null);
        if (value != null) {
            String[] epgs = value.split("\\|");
            if (epgs.length > 0) {
                for (Object add : epgs) {
                    epgList.add(add);
                }
            }
            if (epgList.size() > 0) {
                return;
            }
        }
        if (custom().equals("thailand")) {
            for (Object add2 : thailand.epgList) {
                epgList.add(add2);
            }
        }
    }

    private static String getSign(Context context) {
        for (PackageInfo packageinfo : context.getPackageManager().getInstalledPackages(64)) {
            if (packageinfo.packageName.equals(getPackName2())) {
                return packageinfo.signatures[0].toCharsString();
            }
        }
        return null;
    }

    public static String getSingInfo() {
        try {
            Signature sign = _this.getPackageManager().getPackageInfo(getPackName2(), 64).signatures[0];
            if (custom().equals("huanqiu")) {
                return MD5(parseSignature(sign.toByteArray()));
            }
            return parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String gsi() {
        try {
            return MD5(parseSignature(_this.getPackageManager().getPackageInfo(getPackName2(), 64).signatures[0].toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parseSignature(byte[] signature) {
        try {
            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
            MyPrintln("signName:" + cert.getSigAlgName());
            MyPrintln("pubKey:" + pubKey);
            MyPrintln("signNumber:" + signNumber);
            MyPrintln("subjectDN:" + cert.getSubjectDN().toString());
            return pubKey;
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String gaid() {
        try {
            return String.valueOf(_this.getPackageManager().getApplicationInfo(tv.getPackName(), 128).uid);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getLanguage() {
        String lan = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
        MyPrintln("getLanguage:" + lan);
        return lan.toLowerCase();
    }

    public static boolean load_forcetv(Activity th, String sofile) {
        if (MyGetSharedPreferences(th, "data", 0).getString("libforcetv_version", "0").equals("0")) {
            return false;
        }
        try {
            String localPath = th.getFilesDir() + InternalZipConstants.ZIP_FILE_SEPARATOR + sofile + ".so";
            MyPrintln("BandingLib localPath:" + localPath);
            File inFile = new File(localPath);
            if (!inFile.exists()) {
                return false;
            }
            FileInputStream fis = new FileInputStream(inFile);
            File soFile = new File(th.getDir("libs", 0), sofile + ".so");
            MyPrintln("soFile dir:" + soFile.getParent());
            if (!new File(soFile.getParent()).isDirectory()) {
                return false;
            }
            if (!soFile.exists()) {
                MyPrintln("### " + soFile.getAbsolutePath() + " is not exists");
                FileOutputStream fos = new FileOutputStream(soFile);
                MyPrintln("FileOutputStream:" + fos.toString() + ",tokens:" + sofile + ".so");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = fis.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    baos.write(buffer, 0, len);
                }
                fos.write(baos.toByteArray());
                baos.close();
                fos.close();
            }
            fis.close();
            if (soFile.exists()) {
                MyPrintln("soFile " + soFile.getAbsolutePath() + " length = " + soFile.length());
                System.load(soFile.getAbsolutePath());
                MyPrintln("### System.load End");
                return true;
            }
            MyPrintln("soFile " + soFile.getAbsolutePath() + " no exists");
            return false;
        } catch (Exception e) {
            MyPrintln("Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* renamed from: t */
    public static int m13t(Activity th) {
        if (load_forcetv(th, "libforcetv")) {
            MyPrintln("load_forcetv:true");
        } else {
            System.loadLibrary("forcetv");
            MyPrintln("load_forcetv:false");
        }
        _this = th;
        tv = new MGplayer();
        debug = new StringBuilder();
        SharedPreferences settings = MyGetSharedPreferences(_this, "data", 0);
        packname = tv.getPackName();
        String librarypath = getLibPath(th);
        if (librarypath == null) {
            return 1;
        }
        mCustom.ds(packname, librarypath);
        String fn = mCustom.t3(th.getAssets(), "error", librarypath);
        try {
            String localPath = librarypath + fn;
            MyPrintln("BandingLib localPath:" + localPath);
            File file = new File(localPath);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                file = new File(th.getDir("libs", 0), fn);
                MyPrintln("soFile dir:" + file.getParent());
                if (!new File(file.getParent()).isDirectory()) {
                    return 3;
                }
                if (!file.exists()) {
                    MyPrintln("### " + file.getAbsolutePath() + " is not exists");
                    FileOutputStream fos = new FileOutputStream(file);
                    MyPrintln("FileOutputStream:" + fos.toString() + ",tokens:" + fn);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int len = fis.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        baos.write(buffer, 0, len);
                    }
                    fos.write(baos.toByteArray());
                    baos.close();
                    fos.close();
                }
                fis.close();
                MyPrintln("### System.load start");
                if (file.exists()) {
                    MyPrintln("soFile " + file.getAbsolutePath() + " length = " + file.length());
                    System.load(file.getAbsolutePath());
                } else {
                    MyPrintln("soFile " + file.getAbsolutePath() + " no exists");
                }
                MyPrintln("### System.load End");
                mCustom.cm(readReverse(localPath, null), librarypath);
                String cpuinfo = getCpuName();
                String httpd_path = settings.getString("httpd", "0");
                MyPrintln("httpd_path =" + httpd_path + " getDecode = " + getDecode() + "systemID = " + getSystemID());
                if (getDecode() == 1 || httpd_path == "1000") {
                    gp2pwaitcachecount = 16;
                    setParam(0, 2, 1);
                    httpdstart = 0;
                } else {
                    gp2pwaitcachecount = 2;
                    setParam(0, 2, 1);
                    httpdstart = 1;
                    String httpd_save_path = _this.getFilesDir() + "/httpd/";
                    MyPrintln("httpd_save_path = " + httpd_save_path);
                    File httpd_save_dir = new File(httpd_save_path);
                    if (!httpd_save_dir.exists()) {
                        httpd_save_dir.mkdir();
                    }
                    mPlayer.httpdpath(httpd_save_path);
                }
                mCustom.m15t();
                if (custom().equals("74839547") || custom().equals("szntv") || custom().equals("fszj") || custom().equals("spain1") || custom().equals("smallseven") || custom().equals("spainglobal") || custom().equals("spain1cn") || custom().equals("p2p1") || custom().equals("szysx") || custom().equals("turbotv") || custom().equals("zdiptv") || custom().equals("auditv") || custom().equals("zuzhuang") || custom().equals("familytv") || custom().equals("vibetv") || custom().equals("huanqiu") || custom().equals("andy") || custom().equals("pandaiptv") || custom().equals("meuiptv") || ((custom().equals("jingjimu") && tv.gete().equals("http://www.wordtv.com.tw/gemini-iptv/")) || (custom().equals("jingjimuvod") && tv.gete().equals("http://www.fafa.net.tw/gemini-iptv/")))) {
                    selectlan(1);
                }
                DisplayMetrics dm = new DisplayMetrics();
                _this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                screenWidth = dm.widthPixels;
                screenHeight = dm.heightPixels;
                images_icon = _this.getFilesDir() + "/icon/";
                File destDir = new File(images_icon);
                if (destDir.isFile()) {
                    destDir.delete();
                } else if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                images_ad = _this.getFilesDir() + "/ad/";
                File adDir = new File(images_ad);
                if (adDir.isFile()) {
                    adDir.delete();
                } else if (!adDir.exists()) {
                    adDir.mkdirs();
                }
                String v1 = settings.getString("v1", "error");
                MyPrintln("settings.getString v1:" + v1);
                if (v1 == "error" || v1 == string3() || v1.equals("00:00:00:00:00:00")) {
                    v1first = true;
                    Toast.makeText(_this, _this.getString(C0216R.string.mgplayer_text2).toString(), 0).show();
                }
                createCpuID();
                init_epg();
                int mem = ((ActivityManager) _this.getSystemService("activity")).getMemoryClass();
                MyPrintln("mem = " + mem);
                int inx = MyGetSharedPreferences(_this, "data", 0).getInt("decode", 0);
                int forcetvcache = MyGetSharedPreferences(_this, "data", 0).getInt("forcetvcache", 20);
                if (custom().equals("zhibo")) {
                    forcetvcache = 0;
                }
                if (inx == 1) {
                    if (mem >= 48) {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    } else {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    }
                } else if (cpuinfo.equals("RK3128")) {
                    if (mem >= 48) {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    } else {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    }
                } else if (cpuinfo.equals("AWH3")) {
                    if (mem >= 48) {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    } else {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    }
                } else if (cpuinfo.equals("A20")) {
                    if (mem >= 48) {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    } else {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    }
                } else if (cpuinfo.equals("HIK3V2")) {
                    if (mem >= 48) {
                        mediaplayerheadertype(1);
                        gp2ppthreadnum(1, 1, 1);
                    } else {
                        mediaplayerheadertype(1);
                        gp2ppthreadnum(1, 1, 1);
                    }
                } else if (cpuinfo.equals("RK3229")) {
                    if (mem >= 48) {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    } else {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    }
                } else if (cpuinfo.equals("AML905")) {
                    if (mem >= 48) {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    } else {
                        mediaplayerheadertype(0);
                        gp2ppthreadnum(2, 2, 2);
                    }
                } else if (mem >= 48) {
                    mediaplayerheadertype(0);
                    gp2ppthreadnum(2, 2, 2);
                } else {
                    mediaplayerheadertype(0);
                    gp2ppthreadnum(2, 2, 2);
                }
                if (!custom().equals("quanxing")) {
                    int ver = getSofeVersion();
                    if (ver > 0) {
                        current_version = ver;
                    }
                }
                forcetv(forcetvcache);
                Thread_Custom_Init();
                LIVEplayer.urlClear();
                LIVEplayer.typeClear();
                BACKplayer.playbackClear();
                BACKplayer.playbackTypeClear();
                custom.chuangshi_start();
                custom.jjm_send();
                custom.chaoyang_start();
                custom.newlive_start();
                custom.spain1_start();
                custom.szysx_start(_this);
                custom.lookiptv_start();
                custom.huanqiu_start();
                custom.panda_start();
                custom.goat_start();
                custom.boliwu_start();
                custom.turbo_start();
                custom.jufeng_start();
                custom.doudouzi_start();
                custom.aikanvip_start();
                custom.msiptv_start();
                custom.abciptv_start();
                custom.dongsentv_start();
                custom.anko_start();
                setPreDecode(-1);
                if (custom().equals("huidixing") || custom().equals("woini") || custom().equals("badatv") || custom().equals("itv178") || custom().equals("iworld") || custom().equals("gqhd")) {
                    ihdtv.createHttpServerForHuidixing();
                }
                if (custom().equals("spain1") || custom().equals("spainglobal") || custom().equals("spain1cn")) {
                    createHttpServer();
                    proxy_https_server(9080);
                }
                if (custom().equals("coolsee")) {
                    proxy_https_server(9080);
                }
                ssb();
                PostDebug();
                return 0;
            }
            MyPrintln(file.getAbsolutePath() + " is not fond!");
            return 2;
        } catch (Exception e) {
            MyPrintln("Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void PostDebug() {
        new C03202().start();
    }

    public String GetMacXml() {
        return MyGetSharedPreferences(_this, "data", 0).getString("geminisetmac", "null");
    }

    public String gete(boolean f) {
        String epg;
        if (epgList_index < 0 || epgList.size() <= 0 || epgList.size() <= epgList_index) {
            epg = mCustom.epg();
            if (custom().equals("zhgxnet")) {
                epg = epg + zhgx.epg_dir;
            }
            if (epg == null || epg.length() < 14) {
                if (!custom().equals("yunfou")) {
                    return mCustom.epg();
                }
                Process.killProcess(Process.myPid());
                exit_app();
            }
            if ((epg.endsWith(".html") || epg.endsWith(".php") || epg.endsWith(".asp")) && !f) {
                return epg.substring(0, epg.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR));
            }
            return epg;
        }
        epg = (String) epgList.get(epgList_index);
        if (epg == null || epg.length() < 7) {
            return mCustom.epg();
        }
        if (custom().equals("zeus") || custom().equals("quanxing") || custom().equals("quanxing2")) {
            return epg;
        }
        if ((epg.endsWith(".html") || epg.endsWith(".php") || epg.endsWith(".asp")) && !f) {
            return epg.substring(0, epg.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR));
        }
        return epg;
    }

    public String gete() {
        if (custom().equals("liaoling")) {
            return "rtmp://22.59.49.10/live/livestream";
        }
        return gete(false);
    }

    public String GetSDPath() {
        return Environment.getExternalStorageDirectory().toString();
    }

    public void ShowSDCM(String v) {
        MyPrintln("ShowSDCM v = " + v);
        Message msg;
        if (v.equals("ok")) {
            msg = new Message();
            msg.what = 100;
            rHandler.sendMessage(msg);
        } else if (v.equals("no")) {
            msg = new Message();
            msg.what = 101;
            rHandler.sendMessage(msg);
        } else if (v.equals("get")) {
            msg = new Message();
            msg.what = 102;
            rHandler.sendMessage(msg);
        } else if (v.equals("noget")) {
            msg = new Message();
            msg.what = 103;
            rHandler.sendMessage(msg);
        }
    }

    public void SetSDCM(String file, String t) {
        File f = new File(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + file);
        f.delete();
        try {
            FileOutputStream fileOS = new FileOutputStream(f);
            fileOS.write(t.getBytes());
            fileOS.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public String GetMac() {
        String mac = null;
        try {
            String value = MyGetSharedPreferences(_this, "data", 0).getString("geminisetmac", "null");
            if ((value == null || !value.equals("null")) && value.length() > 1) {
                mac = value;
            } else {
                Editor editor;
                if ((value.equals("null") || value.length() <= 1) && (custom().equals("szysx") || custom().equals("dhtv") || custom().equals("familytv"))) {
                    mac = szysx.getMac();
                    if (mac != null && mac.length() >= 17) {
                        MyPrintln("szysx.getMac:" + szysx.getMac());
                        editor = MyGetSharedPreferences(_this, "data", 0).edit();
                        editor.putString("geminisetmac", mac);
                        editor.commit();
                    }
                }
                if (mac == null || mac.length() < 17) {
                    mac = mCustom.gm();
                    if (mac.length() <= 1) {
                        mac = getLocalMacAddress();
                    }
                    if (mac.equals("02:00:00:00:00:00")) {
                        mac = szysx.getMacAddrWifi7().toLowerCase();
                        MyPrintln("getMacAddrWifi7:" + mac);
                    }
                    editor = MyGetSharedPreferences(_this, "data", 0).edit();
                    editor.putString("geminisetmac", mac);
                    editor.commit();
                }
            }
            MyPrintln("GetMac:" + mac);
            if (test_mac != null) {
                return test_mac.toLowerCase();
            }
            return mac.toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String sendHttpRequest(String param) {
        final String url = param;
        new Thread() {
            public void run() {
                MGplayer.this.sendHttpRequest_value = MGplayer.sendServerCmd(url);
            }
        }.start();
        for (int ii = 0; ii < 1000; ii++) {
            if (this.sendHttpRequest_value.length() > 16) {
                return this.sendHttpRequest_value.trim();
            }
            sleep(1);
        }
        return "";
    }

    public static int getVersion() {
        return current_version;
    }

    public static void MyPrintln(String str) {
        System.out.println(str);
    }

    public static void MyPrintln(int str) {
        System.out.println(str);
    }

    public static void writeDebug(String filename) {
        File f = new File(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + filename);
        f.delete();
        try {
            FileOutputStream fileOS = new FileOutputStream(f);
            fileOS.write(debug.toString().getBytes());
            fileOS.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        debug.setLength(0);
    }

    public static void runLogcat(Context t) {
        new Thread(new C03224()).start();
    }

    public static String week(int id) {
        if (_this == null) {
            return null;
        }
        switch (id) {
            case 1:
                return _this.getString(C0216R.string.sunday).toString();
            case 2:
                return _this.getString(C0216R.string.monday).toString();
            case 3:
                return _this.getString(C0216R.string.tuesday).toString();
            case 4:
                return _this.getString(C0216R.string.wednesday).toString();
            case 5:
                return _this.getString(C0216R.string.thursday).toString();
            case 6:
                return _this.getString(C0216R.string.friday).toString();
            case 7:
                return _this.getString(C0216R.string.saturday).toString();
            default:
                return null;
        }
    }

    public static void timeView() {
        final Handler timeHander = new Handler();
        timeHander.postDelayed(new Runnable() {
            public void run() {
                Calendar calendar = Calendar.getInstance();
                if (MGplayer.seconds_prc > 0) {
                    calendar.setTime(new Date(MGplayer.seconds_prc));
                }
                if (MGplayer.now_zone_prc != null) {
                    calendar.setTimeZone(TimeZone.getTimeZone(MGplayer.now_zone_prc));
                }
                int year = calendar.get(1);
                int month = calendar.get(2) + 1;
                int day = calendar.get(5);
                int hour = calendar.get(11);
                int minute = calendar.get(12);
                int week = calendar.get(7);
                MGplayer.time_now = year + "-" + String.format("%02d", new Object[]{Integer.valueOf(month)}) + "-" + String.format("%02d", new Object[]{Integer.valueOf(day)}) + " | " + MGplayer.week(week) + " | " + String.format("%02d", new Object[]{Integer.valueOf(hour)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minute)});
                timeHander.postDelayed(this, HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS);
            }
        }, 1000);
    }

    public static void check_timeout(final int time) {
        final Handler timeHander = new Handler();
        timeHander.postDelayed(new Runnable() {

            /* renamed from: com.gemini.play.MGplayer$6$1 */
            class C03241 extends Thread {
                C03241() {
                }

                public void run() {
                    if (MGplayer.sendServerCmd(MGplayer.tv.gete() + "/admin/unbundling_send.php?mac=" + MGplayer.tv.GetMac() + "&cpuid=" + MGplayer.tv.getCpuID()).indexOf("no") >= 0) {
                        MGplayer._this.stopService(new Intent(MGplayer._this, LocalService.class));
                        Process.killProcess(Process.myPid());
                        System.exit(0);
                    }
                }
            }

            public void run() {
                new C03241().start();
                timeHander.postDelayed(this, (long) (time * 1000));
            }
        }, (long) (time * 1000));
    }

    public static void openActivity(Activity v1, Class<?> cls) {
        v1.startActivity(new Intent(v1, cls));
    }

    public static void getWebView(WebView webview) {
        mWebView = webview;
        mCustom.gl();
    }

    public void wl(String u) {
        if (mWebView == null) {
            return;
        }
        if (u.startsWith("http://")) {
            mWebView.loadUrl(u);
            return;
        }
        MyPrintln("wl = " + tv.gete() + u);
        mWebView.loadUrl(tv.gete() + u);
    }

    public static void gl() {
        mCustom.gl();
    }

    public static String key(String value) {
        return mCustom.key(value);
    }

    public static String getLocalMacAddress() {
        return ((WifiManager) _this.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }

    public static boolean fileIsExists(String path) {
        File f = new File(path);
        if (!f.exists() || f.isDirectory()) {
            return false;
        }
        return true;
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        if (!file.exists()) {
            return "";
        }
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            value = new BigInteger(1, md5.digest()).toString(16);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
        return value;
    }

    public static String file_md5(String path) {
        String v = null;
        try {
            v = getMd5ByFile(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return v;
    }

    public static void donwFileSswwtv(String str, String save) throws IOException {
        MyPrintln("str:" + str + " !save:" + save);
        String save2 = "";
        if (save.startsWith("file://")) {
            save2 = save.substring(7);
        } else {
            save2 = save;
        }
        String[] pasts = save2.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
        String fileName = "";
        String path = "";
        for (int i = 0; i < pasts.length - 1; i++) {
            path = path + pasts[i] + InternalZipConstants.ZIP_FILE_SEPARATOR;
        }
        fileName = pasts[pasts.length - 1];
        MyPrintln("path:" + path + "#fileName:" + fileName);
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        MyPrintln("str:" + str);
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(str).openConnection();
            conn.setConnectTimeout(180000);
            conn.setReadTimeout(180000);
            conn.connect();
            if (conn.getResponseCode() == 200) {
                int fileSize = conn.getContentLength();
                if (fileSize > 0) {
                    MyPrintln("donwFileSswwtv filesize = " + fileSize);
                    InputStream is = conn.getInputStream();
                    if (is == null) {
                        MyPrintln("WGET DOWN NULL");
                        return;
                    }
                    FileOutputStream fos = new FileOutputStream(new File(path + fileName));
                    byte[] buf = new byte[1024];
                    int total = 0;
                    while (true) {
                        int numread = is.read(buf);
                        if (numread <= 0 && fileSize <= total) {
                            break;
                        }
                        fos.write(buf, 0, numread);
                        total += numread;
                        MyPrintln("donwFileSswwtv total = " + total);
                    }
                    fos.flush();
                    fos.close();
                    MyPrintln("WGET 1");
                }
            }
        } catch (MalformedURLException e) {
            MyPrintln("WGET DOWN NULL");
        }
    }

    public static void donwFile(String str, String save) throws IOException {
        MyPrintln("str:" + str + " !save:" + save);
        String save2 = "";
        if (save.startsWith("file://")) {
            save2 = save.substring(7);
        } else {
            save2 = save;
        }
        String[] pasts = save2.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
        String fileName = "";
        String path = "";
        for (int i = 0; i < pasts.length - 1; i++) {
            path = path + pasts[i] + InternalZipConstants.ZIP_FILE_SEPARATOR;
        }
        fileName = pasts[pasts.length - 1];
        MyPrintln("path:" + path + "#fileName:" + fileName);
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        MyPrintln("str:" + str);
        if (!str.endsWith(".png") && !str.endsWith(".jpg") && !str.endsWith(".gif") && !str.endsWith(".bmp") && !str.endsWith(".jpeg") && !str.endsWith(".PNG") && !str.endsWith(".JPG") && !str.endsWith(".GIF") && !str.endsWith(".BMP") && !str.endsWith(".JPEG")) {
            return;
        }
        if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif") || fileName.endsWith(".bmp") || fileName.endsWith(".jpeg") || fileName.endsWith(".PNG") || fileName.endsWith(".JPG") || fileName.endsWith(".GIF") || fileName.endsWith(".BMP") || fileName.endsWith(".JPEG")) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(str).openConnection();
                conn.setConnectTimeout(2000);
                conn.setReadTimeout(2000);
                conn.connect();
                if (conn.getResponseCode() == 200 && conn.getContentLength() > 0) {
                    InputStream is = conn.getInputStream();
                    if (is == null) {
                        MyPrintln("WGET DOWN NULL");
                        return;
                    }
                    FileOutputStream fos = new FileOutputStream(new File(path + fileName));
                    byte[] buf = new byte[1024];
                    while (true) {
                        int numread = is.read(buf);
                        if (numread <= 0) {
                            break;
                        }
                        fos.write(buf, 0, numread);
                    }
                    fos.flush();
                    fos.close();
                    MyPrintln("WGET 1");
                }
            } catch (MalformedURLException e) {
                MyPrintln("WGET DOWN NULL");
            }
        }
    }

    public String jsendServerCmd(String httpUrl) {
        return sendServerCmd(httpUrl, Factory.DEFAULT_MIN_REBUFFER_MS);
    }

    public static String sendServerCmd(String httpUrl) {
        return sendServerCmd(httpUrl, Factory.DEFAULT_MIN_REBUFFER_MS);
    }

    public static StringBuffer sendServerCmdButter(String httpUrl, int timeout) {
        StringBuffer resultData = new StringBuffer();
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        MyPrintln("sendServerCmdButter Url:" + httpUrl);
        if (url != null) {
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(timeout);
                urlConn.setReadTimeout(timeout);
                urlConn.setRequestMethod("GET");
                MyPrintln("sendServerCmdButter fileSize:" + urlConn.getContentLength());
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                while (true) {
                    String inputLine = buffer.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    resultData.append(inputLine);
                }
                in.close();
                urlConn.disconnect();
                MyPrintln("sendServerCmdButter result len:" + resultData.length());
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return resultData;
    }

    public static String sendServerCmd(String httpUrl, int timeout) {
        return sendServerCmd(httpUrl, Factory.DEFAULT_MIN_REBUFFER_MS, null, null);
    }

    public static CookieStatus SendServerCmd_cookies(String httpUrl, int timeout, String cookie, String UserAgent) {
        URL url = null;
        String resultData = "";
        CookieStatus cookier = new CookieStatus();
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        MyPrintln("sendServerCmd Url:" + httpUrl);
        if (url != null) {
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(timeout);
                urlConn.setReadTimeout(timeout);
                urlConn.setRequestMethod("GET");
                if (UserAgent == null || UserAgent.length() <= 0) {
                    urlConn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                } else {
                    urlConn.setRequestProperty("User-agent", UserAgent);
                }
                if (cookie != null && cookie.length() > 0) {
                    urlConn.setRequestProperty(SM.COOKIE, cookie);
                }
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                while (true) {
                    String inputLine = buffer.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    resultData = resultData + inputLine + "\n";
                }
                MyPrintln("sendServerCmd result :" + resultData);
                cookier.cookie = urlConn.getHeaderField("set-cookie");
                cookier.ret = resultData;
                in.close();
                urlConn.disconnect();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return cookier;
    }

    public static CookieStatus SendHttpsServerCmd_cookies(String httpUrl, int timeout, String cookie, String UserAgent) {
        URL url = null;
        String resultData = "";
        CookieStatus cookier = new CookieStatus();
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        MyPrintln("sendServerCmd Url:" + httpUrl);
        if (url != null) {
            try {
                HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
                urlConn.setConnectTimeout(timeout);
                urlConn.setReadTimeout(timeout);
                urlConn.setRequestMethod("GET");
                if (UserAgent == null || UserAgent.length() <= 0) {
                    urlConn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                } else {
                    urlConn.setRequestProperty("User-agent", UserAgent);
                }
                if (cookie != null && cookie.length() > 0) {
                    urlConn.setRequestProperty(SM.COOKIE, cookie);
                }
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                while (true) {
                    String inputLine = buffer.readLine();
                    if (inputLine == null) {
                        break;
                    }
                    resultData = resultData + inputLine + "\n";
                }
                MyPrintln("sendServerCmd result :" + resultData);
                cookier.cookie = urlConn.getHeaderField("set-cookie");
                cookier.ret = resultData;
                in.close();
                urlConn.disconnect();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return cookier;
    }

    public static String sendServerCookie(String httpUrl, int timeout, String cookie, String UserAgent) {
        URL url = null;
        String resultData = "";
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        MyPrintln("sendServerCmd Url:" + httpUrl);
        if (url != null) {
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(timeout);
                urlConn.setReadTimeout(timeout);
                urlConn.setRequestMethod("GET");
                if (UserAgent == null || UserAgent.length() <= 0) {
                    urlConn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                } else {
                    urlConn.setRequestProperty("User-agent", UserAgent);
                }
                if (cookie != null && cookie.length() > 0) {
                    urlConn.setRequestProperty(SM.COOKIE, cookie);
                }
                urlConn.getInputStream();
                cookie = urlConn.getHeaderField("set-cookie");
                urlConn.disconnect();
                return cookie;
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return cookie;
    }

    public static String sendHttpsServerCookie(String httpUrl, int timeout, String cookie, String UserAgent) {
        URL url = null;
        String resultData = "";
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        MyPrintln("sendServerCmd Url:" + httpUrl);
        if (url != null) {
            try {
                HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
                urlConn.setConnectTimeout(timeout);
                urlConn.setReadTimeout(timeout);
                urlConn.setRequestMethod("GET");
                if (UserAgent == null || UserAgent.length() <= 0) {
                    urlConn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                } else {
                    urlConn.setRequestProperty("User-agent", UserAgent);
                }
                if (cookie != null && cookie.length() > 0) {
                    urlConn.setRequestProperty(SM.COOKIE, cookie);
                }
                urlConn.getInputStream();
                cookie = urlConn.getHeaderField("set-cookie");
                urlConn.disconnect();
                return cookie;
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return cookie;
    }

    public static String sendServerCmd(String httpUrl, int timeout, String cookies, String UserAgent) {
        String resultData = "";
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        MyPrintln("sendServerCmd Url:" + httpUrl);
        if (url != null) {
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(timeout);
                urlConn.setReadTimeout(timeout);
                urlConn.setRequestMethod("GET");
                if (UserAgent != null && UserAgent.length() > 0) {
                    urlConn.setRequestProperty("User-agent", UserAgent);
                } else if (httpUrl.contains("get_prc.php")) {
                    urlConn.setRequestProperty("User-agent", gu());
                } else {
                    urlConn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                }
                if (cookies != null && cookies.length() > 0) {
                    urlConn.setRequestProperty(SM.COOKIE, cookies);
                }
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                while (true) {
                    String inputLine = buffer.readLine();
                    if (inputLine != null) {
                        resultData = resultData + inputLine + "\n";
                    } else {
                        in.close();
                        urlConn.disconnect();
                        MyPrintln("sendServerCmd result :" + resultData);
                        return resultData;
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return resultData;
    }

    public static String sendHttpsServerCmd(String httpUrl, int timeout, String cookies, String UserAgent) {
        String resultData = "";
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        MyPrintln("sendServerCmd Url:" + httpUrl);
        if (url != null) {
            try {
                HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
                urlConn.setConnectTimeout(timeout);
                urlConn.setReadTimeout(timeout);
                urlConn.setRequestMethod("GET");
                if (UserAgent == null || UserAgent.length() <= 0) {
                    urlConn.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                } else {
                    urlConn.setRequestProperty("User-agent", UserAgent);
                }
                if (cookies != null && cookies.length() > 0) {
                    urlConn.setRequestProperty(SM.COOKIE, cookies);
                }
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader buffer = new BufferedReader(in);
                while (true) {
                    String inputLine = buffer.readLine();
                    if (inputLine != null) {
                        resultData = resultData + inputLine + "\n";
                    } else {
                        in.close();
                        urlConn.disconnect();
                        MyPrintln("sendServerCmd result :" + resultData);
                        return resultData;
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return resultData;
    }

    public static String sendServerCmd_https(String url, int timeout, String cookiet, String useragent) {
        return new GHttps().GetHttps(url, timeout, cookiet, useragent);
    }

    public static String sendServerCmd_https(String url) {
        return new GHttps().GetHttps(url, Factory.DEFAULT_MIN_REBUFFER_MS);
    }

    public static String j1(String url) {
        return mCustom.j1(url);
    }

    public static String j2(String url) {
        return mCustom.j2(url);
    }

    public static String j2(String url, String password) {
        return mCustom.j3(url, password);
    }

    public static String j2no(String url) {
        return mCustom.j2no(url);
    }

    public static String j1no(String url) {
        return mCustom.j1no(url);
    }

    public static String ju(String url) {
        return mCustom.ju(url);
    }

    public static String custom_init(String text) {
        return mCustom.gscheck(text);
    }

    public static void forcetv(int cache) {
        if (!custom().equals("jingjimudev")) {
            mCustom.forcetvcache(cache);
        }
    }

    public static void custon_uninit() {
        mCustom.sstringfromjni();
    }

    public static String getg() {
        return mCustom.getg();
    }

    public static String s0(String param0, String param1, String param2, int param3) {
        deleteForcetvFiles();
        MyPrintln("p2p:s0 " + param0 + "#" + param1 + "#" + param2 + "#" + param3);
        return mCustom.s0(param0, param1, param2, param3);
    }

    public static String s1(String param0, String param1, String param2, String param3, int param4) {
        deleteForcetvFiles();
        MyPrintln("p2p:s1 " + param0 + "#" + param1 + "#" + param2 + "#" + param3 + "#" + param4);
        return mCustom.s1(param0, param1, param2, param3, param4);
    }

    public static String s2(String param0) {
        return mCustom.s2(param0);
    }

    public static void s4(String param0) {
        mCustom.s4(param0);
    }

    public static void s5() {
        mCustom.s5();
    }

    /* renamed from: u */
    public static String m14u() {
        return mCustom.m16u();
    }

    public static String custom() {
        if (custom_value == null) {
            custom_value = mCustom.custom();
        }
        return custom_value;
    }

    public static String ff0() {
        return mCustom.ff0();
    }

    public static String ff1() {
        return mCustom.ff1();
    }

    public static String ff2() {
        return mCustom.ff2();
    }

    public static String getinfo2index(String param1, String param2) {
        return mCustom.getinfo2index(param1, param2);
    }

    public static String string3() {
        return mCustom.string3();
    }

    public static int port() {
        return mCustom.port();
    }

    public static int trytimes() {
        return mCustom.trytimes();
    }

    public static int hlsplugin() {
        return mCustom.hlsplugin();
    }

    public static int debug() {
        return mCustom.debug();
    }

    public static int playlistkey(String key) {
        return mCustom.playlistkey(key);
    }

    public static void ssb() {
        mCustom.ssb();
    }

    public static void selectlan(int select) {
        mCustom.selectlan(select);
    }

    public static void mediaplayerinit() {
        mPlayer.mediaplayerinit();
    }

    public static int mediaplayerreopen() {
        return mPlayer.mediaplayerreopen();
    }

    public static int percen() {
        return mPlayer.percen();
    }

    public static int mediaplayerload(String url, int retry, int ffmpeg, int seek) {
        return mPlayer.mediaplayerload(url, retry, ffmpeg, seek);
    }

    public static void httpdinit() {
        mPlayer.httpdinit();
    }

    public static void httpdpath(String path) {
        mPlayer.httpdpath(path);
    }

    public static String mediaplayerstatue(String url) {
        return mPlayer.mediaplayerstatue(url);
    }

    public static void mediaplayerheadertype(int type) {
        mPlayer.mediaplayerheadertype(type);
    }

    public static int mediaplayerload(String url, int retry) {
        return mPlayer.mediaplayerload(url, retry, 0, 0);
    }

    public static void mediaplayerunload() {
        mPlayer.mediaplayerunload();
    }

    public static void mediaplayerexit() {
        mPlayer.mediaplayerexit();
    }

    public static void mediaplayerstop(int play) {
        mPlayer.mediaplayerstop(play);
    }

    public static int mediareceivecount() {
        return mPlayer.mediareceivecount();
    }

    public static void setParam(int param0, int param1, int param2) {
        mPlayer.setParam(param0, param1, param2);
    }

    public static String mediaplayerurl(String url, String password) {
        return mPlayer.mediaplayerurl(url, password);
    }

    public static void gp2ppthreadnum(int all, int start, int end) {
        mPlayer.gp2ppthreadnum(all, start, end);
    }

    public static void mediaplayervodheader(int vod) {
        mPlayer.mediaplayervodheader(vod);
    }

    public static int mediaplayercache() {
        return mPlayer.mediaplayercache();
    }

    public static void fs(String p, int pu, int pt, String m) {
        mf.start(p, pu, pt, m);
    }

    public static void fe(String p, String m) {
        mf.end(p, m);
    }

    public static String fu() {
        return mf.url();
    }

    public static void upnp(final String port, final String pol) {
        String value = MyGetSharedPreferences(_this, "data", 0).getString("upnp", "1");
        if (value != null && !value.equals("0")) {
            new Thread() {
                public void run() {
                    try {
                        String ip = MGplayer.getLocalIpAddress();
                        if (ip != null) {
                            MGplayer.mPlayer.upnp(ip, port, port, pol);
                        }
                    } catch (Exception e) {
                        Editor editor = MGplayer.MyGetSharedPreferences(MGplayer._this, "data", 0).edit();
                        editor.putString("upnp", "0");
                        editor.commit();
                        MGplayer.MyPrintln("upnp error");
                    }
                }
            }.start();
        }
    }

    public static void deleteForcetvFiles() {
    }

    public static void deleteFolderFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File absolutePath : files) {
                    deleteFolderFile(absolutePath.getAbsolutePath(), true);
                }
            }
            if (!deleteThisPath) {
                return;
            }
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.listFiles().length == 0) {
                file.delete();
            }
        }
    }

    public static void Thread_LoadImage(final String image, final String editorName, final String md5) {
        new Thread() {
            public void run() {
                String Loadground = image;
                if (Loadground != null) {
                    String imagePath = MGplayer._this.getFilesDir() + "/background/" + Loadground;
                    MGplayer.MyPrintln("Loadingground 1:" + imagePath);
                    if (!MGplayer.fileIsExists(imagePath)) {
                        MGplayer.MyPrintln("Background:" + MGplayer.tv.gete() + "/images/background/" + Loadground);
                        try {
                            MGplayer.donwFile(MGplayer.tv.gete() + "/images/background/" + Loadground, imagePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!MGplayer.fileIsExists(imagePath)) {
                        return;
                    }
                    if (md5 == null || MGplayer.file_md5(imagePath).equals(md5)) {
                        Editor editor = MGplayer.MyGetSharedPreferences(MGplayer._this, "data", 0).edit();
                        editor.putString(editorName, image);
                        editor.commit();
                        return;
                    }
                    MGplayer.deleteFile(imagePath);
                }
            }
        }.start();
    }

    public static void Thread_LoadImage(String image, String editorName) {
        Thread_LoadImage(image, editorName, null);
    }

    public static void runTime() {
        if (seconds == 0 && now_second != null) {
            Date date = null;
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            inputFormat.setTimeZone(TimeZone.getTimeZone("GMT-0"));
            try {
                date = inputFormat.parse(now_second);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (date == null) {
                seconds = -1;
            }
            seconds = date.getTime();
        }
        final Handler timeHander = new Handler();
        timeHander.post(new Runnable() {
            public void run() {
                MGplayer.seconds += HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS;
                timeHander.postDelayed(this, HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS);
            }
        });
    }

    public static void runTimePRC() {
        if (seconds_prc == 0 && now_second_prc != null) {
            Date date = null;
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (now_zone_prc != null) {
                inputFormat.setTimeZone(TimeZone.getTimeZone(now_zone_prc));
            }
            try {
                date = inputFormat.parse(now_second_prc);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (date == null) {
                seconds_prc = -1;
            }
            seconds_prc = date.getTime();
        }
        final Handler timeHander = new Handler();
        timeHander.post(new Runnable() {
            public void run() {
                MGplayer.seconds_prc += HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS;
                MGplayer.MyPrintln("seconds:" + MGplayer.seconds_prc + " endtime:" + MGplayer.endtime + " tw:" + (MGplayer.endtime - (MGplayer.seconds_prc / 1000)));
                if (MGplayer.endtime > 1440000000 && MGplayer.seconds_prc / 1000 > MGplayer.endtime) {
                    if (MGplayer.rHandler != null) {
                        Message msg = new Message();
                        msg.what = 90;
                        MGplayer.rHandler.sendMessage(msg);
                    }
                    Process.killProcess(Process.myPid());
                    MGplayer.exit_app();
                }
                timeHander.postDelayed(this, HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS);
            }
        });
    }

    public static long fromDateStringToLong(String inVal) {
        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (now_zone_prc != null) {
            inputFormat.setTimeZone(TimeZone.getTimeZone(now_zone_prc));
        }
        try {
            date = inputFormat.parse(inVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date == null) {
            return -1;
        }
        return date.getTime();
    }

    public static void runTimeExit(final Activity _this) {
        final Handler timeHander = new Handler();
        timeHander.post(new Runnable() {

            /* renamed from: com.gemini.play.MGplayer$11$1 */
            class C03151 implements Runnable {
                C03151() {
                }

                public void run() {
                    _this.stopService(new Intent(_this, LocalService.class));
                    _this.finish();
                    Process.killProcess(Process.myPid());
                    MGplayer.exit_app();
                }
            }

            public void run() {
                if (MGplayer.interval_time > 0 && MGplayer.run_interval_time > MGplayer.interval_time) {
                    MGplayer.MyPrintln("runTimeExit");
                    MyToast.makeText(_this.getApplicationContext(), _this.getString(C0216R.string.mgplayer_text4), 0);
                    new Handler().postDelayed(new C03151(), HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                }
                MGplayer.run_interval_time += 60;
                timeHander.postDelayed(this, HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS);
            }
        });
    }

    private static String fromLongToYearString(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (now_zone_prc != null) {
            formatter.setTimeZone(TimeZone.getTimeZone(now_zone_prc));
        }
        return formatter.format(currentTime);
    }

    public static void networkActivity() {
        _this.startActivity(new Intent("android.settings.SETTINGS"));
    }

    public static String getCpuId_Hi3716() {
        String customerId = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop").getInputStream()));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                } else if (line.startsWith("[dev.boxid]")) {
                    customerId = StringUtil.getAfterColon(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customerId;
    }

    public static String getCpuId_HiK3V2() {
        String customerId = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop").getInputStream()));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                } else if (line.startsWith("[persist.sys.instructions.id]")) {
                    customerId = StringUtil.getAfterColon(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customerId;
    }

    public static String writeCpuName() {
        String alltext = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String str = "";
            while (true) {
                str = br.readLine();
                if (str != null) {
                    alltext = alltext + str;
                } else {
                    MyPrintln("");
                    return new BASE64Encoder().encode(alltext.getBytes());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static boolean isAmlogic() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String str = "";
            do {
                str = br.readLine();
                if (str == null) {
                    return false;
                }
            } while (!str.contains("Amlogic"));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static String getCpuName() {
        if (cpuname_value != null) {
            return cpuname_value;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String str = "";
            do {
                str = br.readLine();
                if (str == null) {
                    cpuname_value = "OTHER";
                    return cpuname_value;
                } else if (str.contains("8726") && str.contains("AMLOGIC")) {
                    cpuname_value = "AML8726";
                    return cpuname_value;
                } else if (str.contains("godbox")) {
                    cpuname_value = "HI3716M";
                    return cpuname_value;
                } else if (str.contains("bigfish")) {
                    cpuname_value = "HIK3V2";
                    return cpuname_value;
                } else if (str.contains("MT8580")) {
                    cpuname_value = "MT8580";
                    return cpuname_value;
                } else if (str.contains("Meson8")) {
                    cpuname_value = "S805";
                    return cpuname_value;
                } else if (str.contains("Rockchip") && str.contains("RK3128")) {
                    cpuname_value = "RK3128";
                    return cpuname_value;
                } else if (str.contains("sun7i")) {
                    cpuname_value = "A20";
                    return cpuname_value;
                } else if (str.contains("sun6i")) {
                    cpuname_value = "A31";
                    return cpuname_value;
                } else if (str.contains("sun8i")) {
                    cpuname_value = "AWH3";
                    return cpuname_value;
                } else if (str.contains("Rockchip") && str.contains("RK3229")) {
                    cpuname_value = "RK3229";
                    return cpuname_value;
                } else if (str.toLowerCase().contains("rockchip") && str.toLowerCase().contains("rk3368")) {
                    cpuname_value = "RK3368";
                    return cpuname_value;
                }
            } while (!str.contains("Amlogic"));
            BufferedReader br2 = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String text2 = "";
            int processor = 0;
            while (true) {
                text2 = br2.readLine();
                if (text2 == null) {
                    break;
                }
                MyPrintln("text2:" + text2);
                if (text2.contains("processor")) {
                    processor++;
                }
                if (text2.contains("aarch64") && text2.contains("AArch64")) {
                    cpuname_value = "AML905x64";
                    return cpuname_value;
                }
            }
            MyPrintln("processor:" + processor);
            if (processor >= 8) {
                cpuname_value = "AML912";
                return cpuname_value;
            }
            cpuname_value = "AML905";
            return cpuname_value;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            cpuname_value = "OTHER";
            return cpuname_value;
        } catch (IOException e2) {
            e2.printStackTrace();
            cpuname_value = "OTHER";
            return cpuname_value;
        }
    }

    public static void createCpuID() {
        String value = MyGetSharedPreferences(_this, "data", 0).getString("number", "error");
        String cpuid;
        if (value.equals("error")) {
            cpuid = null;
            if (custom().equals("szysx") || custom().equals("dhtv") || custom().equals("familytv")) {
                if (MACUtils.getMac() == null) {
                    Builder builder = new Builder(_this);
                    builder.setMessage(_this.getString(C0216R.string.validate_text14).toString());
                    builder.setPositiveButton(_this.getString(C0216R.string.ok).toString(), new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MGplayer._this.startActivity(new Intent("android.settings.SETTINGS"));
                            MGplayer._this.stopService(new Intent(MGplayer._this, LocalService.class));
                            MGplayer._this.finish();
                            Process.killProcess(Process.myPid());
                            System.exit(0);
                        }
                    });
                    builder.setNegativeButton(_this.getString(C0216R.string.cancel).toString(), new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MGplayer._this.stopService(new Intent(MGplayer._this, LocalService.class));
                            MGplayer._this.finish();
                            Process.killProcess(Process.myPid());
                            System.exit(0);
                        }
                    });
                    builder.create().show();
                    cpuid = "error";
                } else {
                    cpuid = szysx.GetCpuByfileNull();
                }
            }
            if (getCpuName().equals("HI3716M")) {
                cpuid = getCpuId_Hi3716();
            } else if (getCpuName().equals("HIK3V2")) {
                cpuid = getCpuId_HiK3V2();
            }
            if (cpuid == null || cpuid.length() < 4) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
                    String str = "";
                    while (true) {
                        str = br.readLine();
                        if (str == null) {
                            break;
                        }
                        Log.d("CPU INFO", str);
                        if (str.contains("Serial")) {
                            int index = str.indexOf(":");
                            Log.d("CPU ID", str.substring(index + 2));
                            cpuid = str.substring(index + 2);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                if (cpuid == null || cpuid.startsWith("00000") || cpuid.length() <= 4) {
                    cpuid = readSD();
                    MyPrintln("cpuid numbersd = " + cpuid);
                }
                if (cpuid == null || cpuid.startsWith("00000") || cpuid.length() <= 4) {
                    if (cpuid == null || !cpuid.startsWith("GEMINI") || cpuid.length() <= 4) {
                        cpuid = readSD();
                        MyPrintln("cpuid numbersd = " + cpuid);
                        if (cpuid == null || !cpuid.startsWith("GEMINI") || cpuid.length() <= 4) {
                            cpuid = Number();
                            writeSD(cpuid);
                            MyPrintln("cpuid number = " + cpuid);
                        }
                    }
                    MyPrintln("cpuid = " + cpuid);
                }
            }
            if (!cpuid.equals("error")) {
                MyPrintln("cpuid = " + cpuid);
                Editor editor = MyGetSharedPreferences(_this, "data", 0).edit();
                editor.putString("number", cpuid);
                editor.commit();
                return;
            }
            return;
        }
        cpuid = value;
        if (!cpuid.equals("error") && !fileIsExists(Environment.getExternalStorageDirectory() + "/sysinfo.dat") && cpuid.startsWith("GEMINI")) {
            writeSD(cpuid);
        }
    }

    public static void writeSD(String val) {
        writeSD("sysinfo.dat", val);
    }

    public static String readSD() {
        return readSD("sysinfo.dat");
    }

    public static void writeSD(String filename, String val) {
        if (ContextCompat.checkSelfPermission(_this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(_this, PERMISSIONS_STORAGE, 1);
        }
        File f = new File(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + filename);
        f.delete();
        String str = j1no(val);
        try {
            FileOutputStream fileOS = new FileOutputStream(f);
            fileOS.write(str.getBytes());
            fileOS.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static String readSD(String filename) {
        if (ContextCompat.checkSelfPermission(_this, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(_this, PERMISSIONS_STORAGE, 1);
        }
        File f = new File(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + filename);
        if (!f.exists()) {
            return null;
        }
        String val = "";
        try {
            FileInputStream fileIS = new FileInputStream(f.getAbsolutePath());
            BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
            String str = new String();
            while (true) {
                str = buf.readLine();
                if (str == null) {
                    break;
                }
                val = val + str;
            }
            fileIS.close();
            MyPrintln("1 readSD val = " + val);
            if (val.length() <= 4) {
                return null;
            }
            String v = j2no(val);
            MyPrintln("2 readSD val = " + v);
            return v;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String Number() {
        String str = "";
        for (int i = 1; i <= 16; i++) {
            str = str + ((char) ((int) ((Math.random() * 10.0d) + 48.0d))) + "";
        }
        return getg() + str;
    }

    public static void Thread_Custom_Init() {
        new Thread() {
            public void run() {
                String check = "";
                check = MGplayer.custom_init(MGplayer.tv.GetMac());
                MGplayer.MyPrintln("Thread_Custom_Init = " + check);
                Message msg;
                if (check.equals("NO")) {
                    msg = new Message();
                    msg.what = 89;
                    MGplayer.rHandler.sendMessage(msg);
                } else if (check.equals("NOOK")) {
                    msg = new Message();
                    msg.what = 87;
                    MGplayer.rHandler.sendMessage(msg);
                } else if (check.equals("NOTEST")) {
                    msg = new Message();
                    msg.what = 85;
                    MGplayer.rHandler.sendMessage(msg);
                } else if (check.equals("TEST")) {
                    msg = new Message();
                    msg.what = 86;
                    MGplayer.rHandler.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.what = 88;
                    MGplayer.rHandler.sendMessage(msg);
                }
                MGplayer.MyPrintln("check:" + check);
                if (MGplayer.custom().equals("coolsee")) {
                    MGplayer.getLocalKey();
                }
            }
        }.start();
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep((long) ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Bitmap getHttpBitmap(String url) {
        return getHttpBitmap(url, 1000);
    }

    public static Bitmap getHttpBitmap(String url, int timeout) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            InputStream is = conn.getInputStream();
            if (is == null) {
                MyPrintln("getHttpBitmap is null");
                return null;
            }
            Bitmap bitmap;
            Options options = new Options();
            options.inJustDecodeBounds = true;
            byte[] data = inputStream2ByteArr(is);
            if (resize_vod_image > 1) {
                options.inSampleSize = resize_vod_image;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            } else {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
            is.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] inputStream2ByteArr(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        while (true) {
            int len = inputStream.read(buff);
            if (len != -1) {
                outputStream.write(buff, 0, len);
            } else {
                inputStream.close();
                outputStream.close();
                return outputStream.toByteArray();
            }
        }
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        float scaleWidth = ((float) w) / ((float) width);
        float scaleHeight = ((float) h) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
    }

    public static int getDecode() {
        return MyGetSharedPreferences(_this, "data", 0).getInt("decode", 0);
    }

    public static void setDecode(int decode) {
        Editor editor = MyGetSharedPreferences(_this, "data", 0).edit();
        editor.putInt("decode", decode);
        editor.commit();
    }

    public static int getPreDecode() {
        return MyGetSharedPreferences(_this, "data", 0).getInt("predecode", -1);
    }

    public static void setPreDecode(int decode) {
        Editor editor = MyGetSharedPreferences(_this, "data", 0).edit();
        editor.putInt("predecode", decode);
        editor.commit();
    }

    public static void getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
    }

    public static String fromLongToDateString(long time, String format) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (now_zone_prc != null) {
            formatter.setTimeZone(TimeZone.getTimeZone(now_zone_prc));
        }
        return formatter.format(currentTime);
    }

    public static float getFontsRate() {
        if (_this == null) {
            return 100.0f;
        }
        DisplayMetrics metric = new DisplayMetrics();
        _this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int densityDpi = metric.densityDpi;
        float density = metric.density;
        MyPrintln("densityDpi =" + densityDpi + "density=" + density);
        float rate = ((float) (screenWidth / ThumbnailUtils.TARGET_SIZE_MINI_THUMBNAIL_HEIGHT)) / density;
        if (densityDpi >= 480) {
            rate = ((float) (screenWidth / 480)) / density;
        }
        return (((float) MyGetSharedPreferences(_this, "data", 0).getInt("fontsize", 100)) / 100.0f) * rate;
    }

    public static int getDensityDpi() {
        if (_this == null) {
            return ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL_HEIGHT;
        }
        DisplayMetrics metric = new DisplayMetrics();
        _this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int densityDpi = metric.densityDpi;
        MyPrintln("densityDpi = " + densityDpi);
        return densityDpi;
    }

    public static Typeface getFontsType(Context _this) {
        if (typeface_tmp == null) {
            typeface_tmp = Typeface.createFromAsset(_this.getAssets(), "fonts/Roboto-Bold.ttf");
        }
        return typeface_tmp;
    }

    public static long getUidRxBytes() {
        long j = 0;
        ApplicationInfo ai = null;
        try {
            ai = _this.getPackageManager().getApplicationInfo(packname, 128);
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }
        if (old_RxByte == 0 && ai != null) {
            old_RxByte = TrafficStats.getUidRxBytes(ai.uid) == -1 ? 0 : TrafficStats.getTotalRxBytes() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
            return 0;
        } else if (ai == null) {
            return 0;
        } else {
            long RxByte = TrafficStats.getUidRxBytes(ai.uid) == -1 ? 0 : (TrafficStats.getTotalRxBytes() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) - old_RxByte;
            if (TrafficStats.getUidRxBytes(ai.uid) != -1) {
                j = TrafficStats.getTotalRxBytes() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
            }
            old_RxByte = j;
            return RxByte;
        }
    }

    public static String submitPostData(String strUrlPath, String params, String useragent, String cookie, int timeout, int type) {
        return submitPostData(strUrlPath, params, useragent, cookie, timeout, "application/x-www-form-urlencoded; charset=UTF-8", type);
    }

    public static String submitPostData(String strUrlPath, String params, String useragent, String cookie, int timeout, String contenttype, int type) {
        byte[] data = params.getBytes();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strUrlPath).openConnection();
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", contenttype);
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            httpURLConnection.setRequestProperty("User-Agent", useragent);
            if (cookie != null && cookie.length() > 0) {
                httpURLConnection.setRequestProperty(SM.COOKIE, cookie);
            }
            httpURLConnection.getOutputStream().write(data);
            if (httpURLConnection.getResponseCode() != 200) {
                return "-1";
            }
            InputStream inptStream = httpURLConnection.getInputStream();
            if (type == 0) {
                return httpURLConnection.getHeaderField("set-cookie");
            }
            if (type == 1) {
                return dealResponseResult(inptStream);
            }
            return httpURLConnection.getHeaderField("set-cookie") + "@#@" + dealResponseResult(inptStream);
        } catch (IOException e) {
            return "err: " + e.getMessage().toString();
        }
    }

    public static String submitHttpsPostData(String strUrlPath, String params, String useragent, String cookie, int timeout, int type) {
        byte[] data = params.getBytes();
        try {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(strUrlPath).openConnection();
            httpsURLConnection.setConnectTimeout(timeout);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            httpsURLConnection.setRequestProperty("User-Agent", useragent);
            if (cookie != null && cookie.length() > 0) {
                httpsURLConnection.setRequestProperty(SM.COOKIE, cookie);
            }
            httpsURLConnection.getOutputStream().write(data);
            if (httpsURLConnection.getResponseCode() != 200) {
                return "-1";
            }
            InputStream inptStream = httpsURLConnection.getInputStream();
            if (type == 0) {
                return httpsURLConnection.getHeaderField("set-cookie");
            }
            if (type == 1) {
                return dealResponseResult(inptStream);
            }
            return httpsURLConnection.getHeaderField("set-cookie") + "@#@" + dealResponseResult(inptStream);
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
        String resultData = new String(byteArrayOutputStream.toByteArray());
        MyPrintln(resultData);
        return resultData;
    }

    public static String sendHttpRequestpercent(String channelId, int port) {
        URL url;
        try {
            String urlInfo = "http://127.0.0.1:" + port + "/cmd.xml?cmd=query_chan_p2p_info&id=" + channelId;
            MyPrintln("&&&&&&&&&&&&&" + urlInfo + "**************");
            URL url2 = new URL(urlInfo);
            try {
                StringBuffer sb = new StringBuffer();
                HttpURLConnection huc = (HttpURLConnection) url2.openConnection();
                huc.setAllowUserInteraction(false);
                BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
                while (true) {
                    String line = br.readLine();
                    if (line != null) {
                        sb.append(line).append("\n");
                    } else {
                        String tempStr = sb.toString();
                        MyPrintln("&&&&&&&&&&&&&" + tempStr + "**************");
                        url = url2;
                        return tempStr;
                    }
                }
            } catch (IOException e) {
                url = url2;
                return "";
            }
        } catch (IOException e2) {
            return "";
        }
    }

    public static void scroll_every_interface(ScrollViewInterface l) {
        scroll_iface = l;
    }

    public static void video_every_interface(ControlVideoInterface l) {
        video_iface = l;
    }

    public static void video_every_init() {
        new Thread() {
            public void run() {
                while (true) {
                    String cpuid = MGplayer.tv.getCpuID();
                    String url = MGplayer.sendServerCmd(MGplayer.tv.gete() + "/admin/control_get.php?cpu=" + cpuid + "&mac=" + MGplayer.tv.GetMac());
                    if (MGplayer.video_iface != null && url != null && url.length() > 6) {
                        MGplayer.MyPrintln("video_every_init:url:" + url);
                        MGplayer.video_iface.callback(0, url);
                    } else if (MGplayer.video_iface != null) {
                        MGplayer.video_iface.callback(1, null);
                    }
                    MGplayer.sleep(10000);
                }
            }
        }.start();
    }

    public static void scroll_every_init() {
        new Thread() {
            public void run() {
                while (true) {
                    String cpuid = MGplayer.tv.getCpuID();
                    String[] texts = MGplayer.sendServerCmd(MGplayer.tv.gete() + "/admin/scroll_every_get.php?cpuid=" + cpuid + "&mac=" + MGplayer.tv.GetMac()).split("geminiscroll");
                    if (texts.length >= 4) {
                        String every_date = texts[0].toString().trim();
                        String every_txt = texts[1].toString().trim();
                        int every_time = 0;
                        if (MGplayer.isNumeric(texts[2].toString().trim())) {
                            every_time = Integer.parseInt(texts[2].toString().trim());
                        }
                        String every_now = texts[3].toString().trim();
                        long date = 0;
                        long now = 0;
                        if (every_date.length() > 0 && every_now.length() > 0) {
                            date = MGplayer.fromDateStringToLong(every_date);
                            now = MGplayer.fromDateStringToLong(every_now);
                        }
                        if (now > date && !every_date.equals(MGplayer.scroll_every_date)) {
                            MGplayer.MyPrintln("====start scroll every====");
                            MGplayer.scroll_every_date = every_date;
                            if (MGplayer.scroll_iface != null) {
                                MGplayer.scroll_iface.callback(0, every_txt, every_time);
                            }
                        }
                    }
                    MGplayer.sleep(10000);
                }
            }
        }.start();
    }

    public static int getSofeVersion() {
        try {
            String version = _this.getPackageManager().getPackageInfo(_this.getPackageName(), 0).versionName;
            MyPrintln("versionName:" + version);
            return (int) (Double.parseDouble(version) * 10.0d);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void createHttpServer() {
        new Thread(new Runnable() {
            ServerSocket server;
            Socket socket;

            public void run() {
                try {
                    MGplayer.MyPrintln("==start server " + MGplayer.http_server_port + "==");
                    this.server = new ServerSocket(MGplayer.http_server_port);
                    while (true) {
                        if (this.server != null) {
                            this.socket = this.server.accept();
                        }
                        MGplayer.MyPrintln("==start server 2==");
                        String line = new BufferedReader(new InputStreamReader(this.socket.getInputStream())).readLine();
                        MGplayer.MyPrintln("==start server line==" + line);
                        OutputStream output;
                        String msg;
                        if (this.socket != null && line != null && line.contains("GET /sswwtv.php")) {
                            try {
                                String imagePath = MGplayer._this.getFilesDir() + "/sswwtv.dat";
                                File file = new File(imagePath);
                                if (!file.exists()) {
                                    MGplayer.donwFileSswwtv(spain1.sswwtv_url, imagePath);
                                }
                                FileInputStream inputStream = new FileInputStream(imagePath);
                                output = this.socket.getOutputStream();
                                msg = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + file.length() + "\r\n\r\n";
                                output.write(msg.getBytes());
                                MGplayer.MyPrintln("==start server 3==" + msg + " length:" + file.length());
                                byte[] temp = new byte[((int) file.length())];
                                int totallen = 0;
                                while (true) {
                                    int len = inputStream.read(temp);
                                    if (len <= 0) {
                                        break;
                                    }
                                    output.write(new String(temp, 0, len).getBytes());
                                    totallen += len;
                                }
                                MGplayer.sleep(1000);
                                output.flush();
                                output.close();
                                inputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            this.socket.close();
                            this.socket = null;
                        } else if (this.socket != null && MGplayer.Ghttp_playlist_text != null && line != null && line.contains("GET /playlist.m3u8")) {
                            try {
                                output = this.socket.getOutputStream();
                                msg = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + MGplayer.Ghttp_playlist_text.length() + "\r\n\r\n";
                                output.write(msg.getBytes());
                                MGplayer.MyPrintln("==start server 3==" + msg);
                                MGplayer.MyPrintln("==start server 4==" + MGplayer.Ghttp_playlist_text);
                                output.write(MGplayer.Ghttp_playlist_text.getBytes());
                                output.flush();
                                output.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            this.socket.close();
                            this.socket = null;
                        } else if (this.socket != null) {
                            this.socket.close();
                            this.socket = null;
                        }
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }).start();
    }

    public static final String MD5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            char[] str = new char[(j * 2)];
            int k = 0;
            for (byte byte0 : mdInst.digest()) {
                int i = k + 1;
                str[k] = hexDigits[(byte0 >>> 4) & 15];
                k = i + 1;
                str[i] = hexDigits[byte0 & 15];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String get_ip(String name) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(name);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (address == null) {
            return null;
        }
        MyPrintln("IP:" + address.getHostAddress().toString());
        return address.getHostAddress().toString();
    }

    public static void exit_app() {
        if (custom().equals("huanqiu")) {
            huanqiu.s2();
        }
        if (start_tvbus == 1 && tvbuser != null) {
            tvbuser.stopTvBusService();
        }
        System.exit(0);
    }

    public static String uncompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream gunzip = null;
        try {
            gunzip = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[256];
        while (true) {
            try {
                int n = gunzip.read(buffer);
                if (n < 0) {
                    break;
                }
                out.write(buffer, 0, n);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return out.toString();
    }

    public String getMyUUID() {
        TelephonyManager tm = null;
        if (ContextCompat.checkSelfPermission(_this, "android.permission.READ_PHONE_STATE") == 0) {
            tm = (TelephonyManager) _this.getBaseContext().getSystemService("phone");
        }
        String tmDevice = null;
        String tmSerial = null;
        String androidId = null;
        if (tm != null) {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + Secure.getString(_this.getContentResolver(), "android_id");
        }
        UUID deviceUuid = null;
        if (!(tm == null || tmDevice == null || tmSerial == null || androidId == null)) {
            deviceUuid = new UUID((long) androidId.hashCode(), (((long) tmDevice.hashCode()) << 32) | ((long) tmSerial.hashCode()));
        }
        String uniqueId = null;
        if (deviceUuid != null) {
            uniqueId = deviceUuid.toString();
        }
        Log.d("debug", "uuid=" + uniqueId);
        return uniqueId != null ? uniqueId : "";
    }

    public static String get_key_value() {
        String key_value = "";
        if (seconds_prc <= 1440000000) {
            return key_value;
        }
        return "&key=" + key("gemini#time#" + (seconds_prc / 1000) + "#time&" + (((int) (1.0d + (Math.random() * 10.0d))) * 1000) + "#gemini") + "&version=" + current_version;
    }

    public static void showUpdateActivity(Context _this) {
        Intent intent = new Intent();
        intent.setClass(_this, UpdateActivity.class);
        _this.startActivity(intent);
    }

    public static SharedPreferences MyGetSharedPreferences(Context th, String data, int t) {
        return th.getSharedPreferences(data, t);
    }

    public static boolean isApkInDebug(Context context) {
        try {
            if ((context.getApplicationInfo().flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void RunControlPlayer(final int time) {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                String url = MGplayer.sendServerCmd(MGplayer.tv.gete() + "/control_get.php?mac=" + MGplayer.tv.GetMac() + "&cpuid=" + MGplayer.tv.getCpuID() + MGplayer.get_key_value());
                MGplayer.MyPrintln("control url = " + url);
                int pos = url.indexOf("gemini:");
                if (url.length() > 7) {
                    url = url.substring(pos + 7);
                    if (url == null || (url != null && url.length() < 7)) {
                        if (ControlPlayerActivity.instance != null) {
                            ControlPlayerActivity.instance.finish();
                        }
                        MGplayer.control_url = "";
                    } else if (!(url == null || MGplayer.control_url.equals(url) || (!url.startsWith("http://") && !url.startsWith("rtsp://") && !url.startsWith("rtmp://") && !url.startsWith("udp://")))) {
                        if (ControlPlayerActivity.instance != null) {
                            ControlPlayerActivity.instance.finish();
                        }
                        Intent intent = new Intent();
                        intent.setClass(MGplayer._this, ControlPlayerActivity.class);
                        intent.putExtra("vod_url", url);
                        MGplayer._this.startActivity(intent);
                        MGplayer.control_url = url;
                    }
                    mHandler.postDelayed(this, (long) time);
                }
            }
        }, (long) time);
    }

    public static String executeHttpGet01(String httpurl, boolean brk) {
        Log.e("x---", httpurl);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(httpurl).openConnection();
            connection.getResponseCode();
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                strBuffer.append(line);
            }
            String result = strBuffer.toString();
            if (connection != null) {
                connection.disconnect();
            }
            if (in == null) {
                return result;
            }
            try {
                in.close();
                return result;
            } catch (IOException e) {
                Log.e("error", Log.getStackTraceString(e));
                return result;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (brk) {
                connection.setConnectTimeout(20000);
            }
            return null;
        }
    }

    public static String sendPostData(String strUrlPath, String params) {
        byte[] data = null;
        if (params != null) {
            data = params.getBytes();
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strUrlPath).openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            httpURLConnection.getOutputStream().write(data);
            httpURLConnection.getResponseCode();
            return "-1";
        } catch (IOException e) {
            return "err: " + e.getMessage().toString();
        }
    }

    public static void uploadFile(String uploadUrl, String filename) {
        String uploadFile = Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + filename;
        String srcPath = Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + filename;
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(uploadUrl).openConnection();
            httpURLConnection.setConnectTimeout(DefaultLoadControl.DEFAULT_HIGH_WATERMARK_MS);
            httpURLConnection.setReadTimeout(DefaultLoadControl.DEFAULT_HIGH_WATERMARK_MS);
            httpURLConnection.setChunkedStreamingMode(131072);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod(HttpPost.METHOD_NAME);
            httpURLConnection.setRequestProperty("Connection", HTTP.CONN_KEEP_ALIVE);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\"" + srcPath.substring(srcPath.lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR) + 1) + "\"" + end);
            dos.writeBytes(end);
            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192];
            while (true) {
                int count = fis.read(buffer);
                if (count != -1) {
                    dos.write(buffer, 0, count);
                } else {
                    fis.close();
                    dos.writeBytes(end);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    dos.flush();
                    InputStream is = httpURLConnection.getInputStream();
                    String result = new BufferedReader(new InputStreamReader(is, "utf-8")).readLine();
                    dos.close();
                    is.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void upLoadByAsyncHttpClient(String uploadUrl, String filename) throws FileNotFoundException {
        final File f = new File(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + filename);
        RequestParams params = new RequestParams();
        params.put("uploadedfile", f);
        RequestHandle post = client.post(uploadUrl, params, new AsyncHttpResponseHandler() {
            public void onSuccess(int var1, Header[] var2, byte[] var3) {
                MGplayer.MyPrintln("Post debug Success");
                f.delete();
            }

            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                MGplayer.MyPrintln("Post debug Fail");
            }
        });
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (NetworkInfo state : networkInfo) {
                    if (state.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWiFi(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.getType() != 1) {
            return false;
        }
        return true;
    }

    public static boolean isMobile(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.getType() != 0) {
            return false;
        }
        return true;
    }

    public static void restart(Activity t) {
        Intent i = t.getBaseContext().getPackageManager().getLaunchIntentForPackage(t.getBaseContext().getPackageName());
        i.addFlags(67108864);
        t.startActivity(i);
    }

    public String getAppName(String packageName) throws NameNotFoundException {
        return null;
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("getLocalIpAddress", ex.toString());
        }
        return null;
    }

    public static String getSystemID() {
        return MD5(Build.BRAND + "#" + Build.PRODUCT + "#" + Build.CPU_ABI + "#" + Build.TAGS + "#" + 1 + "#" + Build.DEVICE + "#" + Build.DISPLAY + "#" + Build.ID).toLowerCase();
    }

    public static String getLocalKey() {
        String[] b = sendServerCmd("http://127.0.0.1:" + port() + "/api?func=get_local_key").split("localkey val=");
        if (b.length == 2) {
            LocalKeyID = b[1].substring(0, b[1].indexOf("/>")).replace("\"", "").trim();
        }
        return LocalKeyID;
    }

    public static void proxy_http_setip(String ip) {
        proxy_server_ip = ip;
    }

    public static void proxy_https_server(final int port) {
        new Thread(new Runnable() {
            ServerSocket server;
            Socket socket;

            public void run() {
                try {
                    this.server = new ServerSocket(port);
                    while (true) {
                        if (this.server != null) {
                            this.socket = this.server.accept();
                        }
                        if (this.socket != null) {
                            try {
                                OutputStream output = this.socket.getOutputStream();
                                String line = new BufferedReader(new InputStreamReader(this.socket.getInputStream())).readLine();
                                String context = line.substring(line.indexOf("GET ") + 4, line.indexOf(" HTTP"));
                                MGplayer.MyPrintln("line = " + line + " context = " + context);
                                String strPath = "http://" + MGplayer.proxy_server_ip + context;
                                if (URLUtil.isNetworkUrl(strPath)) {
                                    URLConnection conn = new URL(strPath).openConnection();
                                    SharedPreferences settings = MGplayer.MyGetSharedPreferences(MGplayer._this, "data", 0);
                                    String userid = settings.getString("userid", null);
                                    String usertoken = settings.getString("usertoken", null);
                                    MGplayer.MyPrintln("userid:" + userid + " usertoken:" + usertoken);
                                    if (!(userid == null || usertoken == null)) {
                                        conn.setRequestProperty("userid", userid);
                                        conn.setRequestProperty("usertoken", usertoken);
                                    }
                                    conn.connect();
                                    int fileSize = conn.getContentLength();
                                    if (fileSize <= 0) {
                                        MGplayer.MyPrintln("UpdateActivity APK 0KB ");
                                        return;
                                    }
                                    InputStream is = conn.getInputStream();
                                    if (is == null) {
                                        MGplayer.MyPrintln("UpdateActivity APK NULL ");
                                    }
                                    output.write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + fileSize + "\r\n\r\n").getBytes());
                                    byte[] buf = new byte[1024];
                                    while (true) {
                                        int numread = is.read(buf);
                                        if (numread <= 0) {
                                            try {
                                                break;
                                            } catch (Exception e) {
                                                Log.d("Tag", "error");
                                            }
                                        } else {
                                            output.write(buf, 0, numread);
                                        }
                                    }
                                    output.flush();
                                    output.close();
                                    is.close();
                                    MGplayer.MyPrintln("UpdateActivity Download Finish");
                                } else {
                                    MGplayer.MyPrintln("url error");
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            this.socket.close();
                            this.socket = null;
                        } else if (this.socket != null) {
                            this.socket.close();
                            this.socket = null;
                        }
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }).start();
    }

    public static void postDebug(String f) {
        try {
            upLoadByAsyncHttpClient("http://www.gemini-iptv.com/debug/debug.php", f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
