package com.gemini.kvod2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.gemini.custom.custom;
import com.gemini.custom.huanqiu;
import com.gemini.custom.jhome;
import com.gemini.custom.lookiptv;
import com.gemini.custom.quanxing;
import com.gemini.custom.spain1;
import com.gemini.custom.turbo;
import com.gemini.play.BACKplayer;
import com.gemini.play.BackPlayerActivity;
import com.gemini.play.ControlPlayerActivity;
import com.gemini.play.ControlVideoInterface;
import com.gemini.play.CookieStatus;
import com.gemini.play.GHash;
import com.gemini.play.LIVEplayer;
import com.gemini.play.Launcher2Activity;
import com.gemini.play.LivePlayerActivity;
import com.gemini.play.LoadlibsView;
import com.gemini.play.LocalService;
import com.gemini.play.MGplayer;
import com.gemini.play.MenuView;
import com.gemini.play.MyBackPreviewView;
import com.gemini.play.MyDialog;
import com.gemini.play.MyDialog2.Builder;
import com.gemini.play.MyDialogPrompt;
import com.gemini.play.MyToast;
import com.gemini.play.NumberProgressBar;
import com.gemini.play.PhoneInfo;
import com.gemini.play.UpdateActivity;
import com.gemini.play.VODplayer;
import com.gemini.play.VodPlayerListActivity;
import com.gemini.play.VodPlayerMainActivity;
import com.gemini.play.WifiAP;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.util.MimeTypes;
import com.memo.sdk.MemoTVCastSDK;
import io.vov.vitamio.Vitamio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import net.lingala.zip4j.util.InternalZipConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.succlz123.okplayer.utils.OkPlayerUtils;

public class ValidateActivity extends Activity implements OnRequestPermissionsResultCallback {
    private static String[] PERMISSIONS_STORAGE = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private ArrayList<String> interfaceList = null;
    private boolean is_showed_network_setting = false;
    private NumberProgressBar load_progressbar = null;
    private TextView load_textview = null;
    private boolean loader_finish = false;
    private TextView loading_text = null;
    private TextView loading_text2 = null;
    private PluginInterface mPlugin = null;
    private WebSettings mWebSettings = null;
    private WebView mWebView = null;
    public ControlVideoInterface onControlVideo = new ControlVideoInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(ValidateActivity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    ValidateActivity.this.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    };
    private PhoneInfo siminfo = null;
    private Handler wHandler = new C02287();

    /* renamed from: com.gemini.kvod2.ValidateActivity$1 */
    class C02221 extends WebChromeClient {
        C02221() {
        }

        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            new Builder(ValidateActivity.this).setTitle(ValidateActivity.this.getString(C0216R.string.validate_text1).toString()).setMessage(message).setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                    dialog.dismiss();
                }
            }).create().show();
            return true;
        }

        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new MyDialog.Builder(ValidateActivity.this).setTitle(ValidateActivity.this.getString(C0216R.string.validate_text1).toString()).setMessage(message).setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                    dialog.dismiss();
                }
            }).setNegativeButton(17039360, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                    dialog.dismiss();
                }
            }).create().show();
            return true;
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            final MyDialogPrompt.Builder builder = new MyDialogPrompt.Builder(ValidateActivity.this);
            builder.setTitle(ValidateActivity.this.getString(C0216R.string.validate_text1).toString()).setMessage(message).setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    result.confirm(builder.getContentEditText());
                    dialog.dismiss();
                }
            }).setNegativeButton(17039360, new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    result.cancel();
                    dialog.dismiss();
                }
            }).create().show();
            return true;
        }
    }

    /* renamed from: com.gemini.kvod2.ValidateActivity$5 */
    class C02265 implements OnClickListener {
        C02265() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            MGplayer.getWebView(ValidateActivity.this.mWebView);
        }
    }

    /* renamed from: com.gemini.kvod2.ValidateActivity$6 */
    class C02276 implements OnClickListener {
        C02276() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            ValidateActivity.this.startActivity(new Intent("android.settings.SETTINGS"));
        }
    }

    /* renamed from: com.gemini.kvod2.ValidateActivity$7 */
    class C02287 extends Handler {
        C02287() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ValidateActivity.this.loginActivity();
                    return;
                case 2:
                    TextView t = (TextView) ValidateActivity.this.findViewById(C0216R.id.loadingtext);
                    t.setVisibility(0);
                    t.setText(msg.getData().getString(MimeTypes.BASE_TYPE_TEXT));
                    return;
                case 4:
                    ValidateActivity.this.load_progressbar.setVisibility(0);
                    ValidateActivity.this.loading_text2.setVisibility(0);
                    int num = msg.getData().getInt("progress");
                    String text = msg.getData().getString(MimeTypes.BASE_TYPE_TEXT, null);
                    ValidateActivity.this.load_progressbar.setProgress(num);
                    if (text != null) {
                        ValidateActivity.this.loading_text2.setText(text);
                        return;
                    }
                    return;
                case 5:
                    ValidateActivity.this.load_progressbar.setVisibility(8);
                    ValidateActivity.this.load_textview.setVisibility(8);
                    ValidateActivity.this.loading_text2.setVisibility(8);
                    return;
                case 6:
                    MGplayer.getWebView(ValidateActivity.this.mWebView);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.kvod2.ValidateActivity$8 */
    class C02298 implements OnClickListener {
        C02298() {
        }

        public void onClick(DialogInterface dialog, int which) {
            MGplayer._this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
        }
    }

    /* renamed from: com.gemini.kvod2.ValidateActivity$9 */
    class C02309 implements OnClickListener {
        C02309() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    public class MyWebViewClient extends WebViewClient {

        /* renamed from: com.gemini.kvod2.ValidateActivity$MyWebViewClient$1 */
        class C02311 implements Runnable {
            C02311() {
            }

            public void run() {
                if (!ValidateActivity.this.loader_finish) {
                    MGplayer.epgList_index++;
                    if (MGplayer.epgList_index >= MGplayer.epgList.size()) {
                        MGplayer.epgList_index = -1;
                    }
                    if (ValidateActivity.this.mWebView != null) {
                        ValidateActivity.this.mWebView.setVisibility(0);
                        MGplayer.getWebView(ValidateActivity.this.mWebView);
                    }
                }
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            MGplayer.MyPrintln("load url = " + url);
            ValidateActivity.this.loader_finish = false;
            new Handler().postDelayed(new C02311(), 120000);
            if (url.indexOf("error") >= 0) {
                ValidateActivity.this.load_progressbar.setVisibility(8);
                ValidateActivity.this.load_textview.setVisibility(8);
                ValidateActivity.this.mWebView.setVisibility(0);
            }
            if (ValidateActivity.this.load_progressbar != null) {
                if (url.contains("custom_post")) {
                    if (!ValidateActivity.this.load_progressbar.isShown()) {
                        ValidateActivity.this.load_progressbar.setVisibility(0);
                        ValidateActivity.this.load_textview.setVisibility(0);
                    }
                    ValidateActivity.this.load_progressbar.setProgress(70);
                    if (MGplayer.custom().equals("quanxing")) {
                        ValidateActivity.this.load_progressbar.setVisibility(8);
                        ValidateActivity.this.load_textview.setVisibility(8);
                    } else if (MGplayer.custom().equals("fszj")) {
                        ValidateActivity.this.loading_text.setText(ValidateActivity.this.getString(C0216R.string.validate_text6).toString() + "....");
                    } else {
                        ValidateActivity.this.loading_text2.setText(ValidateActivity.this.getString(C0216R.string.validate_text4).toString());
                    }
                } else if (url.contains("init")) {
                    if (!ValidateActivity.this.load_progressbar.isShown()) {
                        ValidateActivity.this.load_progressbar.setVisibility(0);
                        ValidateActivity.this.load_textview.setVisibility(0);
                    }
                    ValidateActivity.this.load_progressbar.setProgress(90);
                    if (MGplayer.custom().equals("quanxing")) {
                        ValidateActivity.this.load_progressbar.setVisibility(8);
                        ValidateActivity.this.load_textview.setVisibility(8);
                    } else if (MGplayer.custom().equals("fszj")) {
                        ValidateActivity.this.loading_text.setText(ValidateActivity.this.getString(C0216R.string.validate_text6).toString() + "......");
                    } else {
                        ValidateActivity.this.loading_text2.setText(ValidateActivity.this.getString(C0216R.string.validate_text5).toString());
                    }
                } else if (MGplayer.custom().equals("quanxing")) {
                    ValidateActivity.this.load_progressbar.setVisibility(8);
                    ValidateActivity.this.load_textview.setVisibility(8);
                } else if (MGplayer.custom().equals("fszj")) {
                    ValidateActivity.this.loading_text.setText(ValidateActivity.this.getString(C0216R.string.validate_text6).toString() + "..");
                } else {
                    ValidateActivity.this.loading_text2.setText(ValidateActivity.this.getString(C0216R.string.validate_text3).toString());
                }
                if (url.contains("error")) {
                    if (MGplayer.custom().equals("jhome") && jhome.bg_default) {
                        ValidateActivity.this.mWebView.setBackgroundResource(C0216R.mipmap.error_bg_jhome);
                    } else if (MGplayer.custom().equals("badatv")) {
                        ValidateActivity.this.mWebView.setBackgroundResource(C0216R.mipmap.error_bg_badatv);
                    } else {
                        String loadimage = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).getString("accountimage", null);
                        if (loadimage == null || !MGplayer.fileIsExists(ValidateActivity.this.getFilesDir() + "/background/" + loadimage)) {
                            ValidateActivity.this.mWebView.setBackgroundResource(C0216R.mipmap.error_bg);
                        } else {
                            String imagePath = ValidateActivity.this.getFilesDir() + "/background/" + loadimage;
                            new Options().inSampleSize = 4;
                            ValidateActivity.this.mWebView.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(imagePath)));
                            ValidateActivity.this.mWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                        }
                    }
                }
                super.onPageStarted(view, url, favicon);
            }
        }

        public void onPageFinished(WebView view, String url) {
            if (url.indexOf("error.php") >= 0) {
                ValidateActivity.this.mWebView.setVisibility(0);
                if (MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spainglobal") || MGplayer.custom().equals("spain1cn")) {
                    spain1.rm_mi_txt();
                }
            }
            ValidateActivity.this.loader_finish = true;
            super.onPageFinished(view, url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            ValidateActivity.this.loader_finish = true;
            FrameLayout frameLayout;
            if (MGplayer.epgList.size() > 0) {
                MGplayer.epgList_index++;
                if (MGplayer.epgList_index >= MGplayer.epgList.size()) {
                    MGplayer.epgList_index = -1;
                }
                final WebView view2 = view;
                final String failingUrl2 = failingUrl;
                frameLayout = (FrameLayout) ValidateActivity.this.findViewById(C0216R.id.frameLayout);
                if (MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spainglobal")) {
                    frameLayout.setBackgroundResource(C0216R.mipmap.error2);
                    ValidateActivity.this.networkActivity();
                } else if (MGplayer.custom().equals("badatv")) {
                    frameLayout.setBackgroundResource(C0216R.mipmap.badatv_404);
                } else if (MGplayer.getLanguage().equals("zh-cn")) {
                    frameLayout.setBackgroundResource(C0216R.mipmap.error);
                } else {
                    frameLayout.setBackgroundResource(C0216R.mipmap.error_en);
                }
                if (ValidateActivity.this.mWebView != null) {
                    ValidateActivity.this.mWebView.setVisibility(8);
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (ValidateActivity.this.mWebView != null) {
                            ValidateActivity.this.mWebView.setVisibility(0);
                        }
                        MGplayer.MyPrintln("onReceivedError:" + failingUrl2);
                        if (MGplayer.custom().equals("quanxing") || MGplayer.custom().equals("zeus")) {
                            view2.loadUrl(MGplayer.tv.gete());
                        } else {
                            MGplayer.getWebView(ValidateActivity.this.mWebView);
                        }
                        if (ValidateActivity.this.mWebView != null) {
                            ValidateActivity.this.mWebView.setVisibility(8);
                        }
                    }
                }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                return;
            }
            view2 = view;
            failingUrl2 = failingUrl;
            frameLayout = (FrameLayout) ValidateActivity.this.findViewById(C0216R.id.frameLayout);
            if (MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spainglobal")) {
                frameLayout.setBackgroundResource(C0216R.mipmap.error2);
                ValidateActivity.this.networkActivity();
            } else {
                frameLayout.setBackgroundResource(C0216R.mipmap.error);
            }
            if (ValidateActivity.this.mWebView != null) {
                ValidateActivity.this.mWebView.setVisibility(8);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MGplayer.MyPrintln("onReceivedError:" + failingUrl2);
                    if (ValidateActivity.this.mWebView != null) {
                        ValidateActivity.this.mWebView.setVisibility(0);
                    }
                    if (MGplayer.custom().equals("quanxing")) {
                        view2.loadUrl(failingUrl2);
                    } else {
                        MGplayer.getWebView(ValidateActivity.this.mWebView);
                    }
                    if (ValidateActivity.this.mWebView != null) {
                        ValidateActivity.this.mWebView.setVisibility(8);
                    }
                }
            }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
        }
    }

    public class PluginInterface {

        /* renamed from: com.gemini.kvod2.ValidateActivity$PluginInterface$1 */
        class C02341 implements Runnable {
            C02341() {
            }

            public void run() {
                MGplayer.boot_launcher = true;
                MGplayer.openActivity(ValidateActivity.this, Launcher2Activity.class);
                ValidateActivity.this.finish();
                lookiptv.lookiptvloaded = true;
                ValidateActivity.this.init();
            }
        }

        /* renamed from: com.gemini.kvod2.ValidateActivity$PluginInterface$2 */
        class C02352 extends Thread {
            C02352() {
            }

            public void run() {
                try {
                    MGplayer.donwFileSswwtv(spain1.sswwtv_url, ValidateActivity.this.getFilesDir() + "/sswwtv.dat");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /* renamed from: com.gemini.kvod2.ValidateActivity$PluginInterface$3 */
        class C02363 implements Runnable {
            C02363() {
            }

            public void run() {
                MGplayer.boot_launcher = true;
                MGplayer.openActivity(ValidateActivity.this, Launcher2Activity.class);
                ValidateActivity.this.finish();
                lookiptv.lookiptvloaded = true;
                ValidateActivity.this.init();
            }
        }

        /* renamed from: com.gemini.kvod2.ValidateActivity$PluginInterface$4 */
        class C02374 extends Thread {
            C02374() {
            }

            public void run() {
                try {
                    MGplayer.donwFileSswwtv(spain1.sswwtv_url, ValidateActivity.this.getFilesDir() + "/sswwtv.dat");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /* renamed from: com.gemini.kvod2.ValidateActivity$PluginInterface$5 */
        class C02385 implements OnClickListener {
            C02385() {
            }

            public void onClick(DialogInterface dialog, int which) {
                MGplayer.networkActivity();
            }
        }

        @JavascriptInterface
        public void CTCSetConfig(String key, String value) {
            Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
            editor.putString(key, value);
            editor.commit();
        }

        @JavascriptInterface
        public String CTCGetConfig(String key) {
            return MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).getString(key, "");
        }

        @JavascriptInterface
        public void CTCStartApp() {
            if (MGplayer.custom().equals("turbotv") && turbo.isvod == 1) {
                Intent intent = new Intent();
                intent.setClass(ValidateActivity.this, VodPlayerMainActivity.class);
                ValidateActivity.this.startActivity(intent);
                lookiptv.lookiptvloaded = true;
                ValidateActivity.this.init();
                return;
            }
            if (MGplayer.custom().equals("huanqiu")) {
                huanqiu.s1();
            }
            intent = new Intent();
            intent.setClass(ValidateActivity.this, LivePlayerActivity.class);
            intent.putExtra("ui_type", MGplayer.live_ui_type);
            ValidateActivity.this.startActivity(intent);
            lookiptv.lookiptvloaded = true;
            if (MGplayer.custom().equals("quanxing")) {
                if (quanxing.version >= MGplayer.net_version || MGplayer.net_version <= 0) {
                    quanxing.init(ValidateActivity.this);
                    quanxing.unIcon(ValidateActivity.this);
                    quanxing.unXml(ValidateActivity.this);
                    quanxing.previewInit();
                    MGplayer.boot_launcher = false;
                    ValidateActivity.this.finish();
                } else {
                    MGplayer.MyPrintln("CTCVersion:" + MGplayer.net_version + "@" + quanxing.version);
                    ValidateActivity.this.updateActivity();
                }
            }
            ValidateActivity.this.init();
        }

        @JavascriptInterface
        public void CTCStartVod() {
            if (MGplayer.custom().equals("huanqiu")) {
                huanqiu.s1();
            }
            Intent intent = new Intent();
            if (MGplayer.custom().equals("msiptv")) {
                intent.setClass(ValidateActivity.this, VodPlayerListActivity.class);
            } else {
                intent.setClass(ValidateActivity.this, VodPlayerMainActivity.class);
            }
            ValidateActivity.this.startActivity(intent);
            lookiptv.lookiptvloaded = true;
            ValidateActivity.this.init();
        }

        @JavascriptInterface
        public void vodPlay(String url) {
            MGplayer.MyPrintln("VodPlay: " + url);
        }

        @JavascriptInterface
        public void backActivity(String url) {
            ValidateActivity.this.mWebView.loadUrl(MGplayer.tv.gete() + InternalZipConstants.ZIP_FILE_SEPARATOR + url);
        }

        @JavascriptInterface
        public String CTCGetMac() {
            return MGplayer.tv.GetMac();
        }

        @JavascriptInterface
        public void CTCAddUrl(int id, String name, String image, String url, String password, String type, String intro, String source, String introid) {
            LIVEplayer.urlPush(id, name, image, url, password, type, intro, source, introid);
        }

        @JavascriptInterface
        public void CTCAddUrl2(int id, String name, String image, String url, String password, String type, String intro, String source, String introid, String watermark) {
            LIVEplayer.urlPush(id, name, image, url, password, type, intro, source, introid, watermark);
        }

        @JavascriptInterface
        public void CTCAddPlayback(int id, String name, String image, String url, String password, String type, String intro, String source, String introid) {
            BACKplayer.playbackPush(id, name, image, url, password, type, intro, source, introid);
        }

        @JavascriptInterface
        public void CTCAddPlayback2(int id, String name, String image, String url, String password, String type, String intro, String source, String introid) {
            MGplayer.MyPrintln("CTCAddPlayback 2:" + intro);
            BACKplayer.playbackPush2(id, name, image, url, password, type, intro, source, introid);
        }

        @JavascriptInterface
        public void CTCAddType(String id, String type) {
            LIVEplayer.typePush(id, type);
        }

        @JavascriptInterface
        public void CTCAddPlaybackType(String id, String needps, String ps, String key, String type) {
            BACKplayer.playbackTypePush(id, needps, ps, key, type);
        }

        @JavascriptInterface
        public void CTCSetLastTime(String days, int show) {
            MGplayer.leftdays = days;
            MGplayer.leftdaysshow = show;
        }

        @JavascriptInterface
        public void CTCSetLastTime(String days) {
            CTCSetLastTime(days, 0);
        }

        @JavascriptInterface
        public void CTCSetUpdate(String url) {
            Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
            editor.putString("update", url);
            editor.commit();
        }

        @JavascriptInterface
        public void CTCLoaded() {
            MGplayer.load_timeout = 0;
        }

        @JavascriptInterface
        public void CTCVersion(int version) {
            MGplayer.net_version = version;
        }

        @JavascriptInterface
        public int CTCGetVersion() {
            CTCLoaded();
            return MGplayer.current_version;
        }

        @JavascriptInterface
        public void CTCSetHotlink(String hotlink) {
            MGplayer.hotlink = hotlink;
        }

        @JavascriptInterface
        public void CTCSetLivePanal(int val) {
            MGplayer.live_panal = val;
            if (val != 5) {
                Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
                if (val == 6) {
                    editor.putInt("decode", 1);
                } else if (val == 7) {
                    editor.putInt("decode", 2);
                } else if (val == 8) {
                    editor.putInt("decode", 3);
                } else {
                    editor.putInt("decode", 0);
                }
                editor.commit();
            }
        }

        @JavascriptInterface
        public void CTCSetScrollText(String text) {
            MGplayer.scrolltext = text;
        }

        @JavascriptInterface
        public void CTCSetScrollAdPic(String text) {
            MGplayer.adPic = text;
        }

        @JavascriptInterface
        public void CTCSetCloseWebPage() {
            ValidateActivity.this.mWebView = null;
            System.gc();
            ValidateActivity.this.finish();
        }

        @JavascriptInterface
        public void CTChttpRequest(String cmd) {
            MGplayer.sendServerCmd(cmd);
        }

        @JavascriptInterface
        public String CTCgetEpg() {
            return MGplayer.tv.gete();
        }

        @JavascriptInterface
        public void CTClanucherActivity() {
            String custom = MGplayer.custom();
            if (MGplayer.custom().equals("huanqiu")) {
                huanqiu.s1();
            }
            if (custom.equals("turbotv") && turbo.isvod == 1) {
                Intent intent = new Intent();
                intent.setClass(ValidateActivity.this, VodPlayerMainActivity.class);
                ValidateActivity.this.startActivity(intent);
                lookiptv.lookiptvloaded = true;
                ValidateActivity.this.init();
            } else {
                new Handler().post(new C02341());
            }
            if (MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spainglobal") || MGplayer.custom().equals("spain1cn")) {
                new C02352().start();
            }
        }

        @JavascriptInterface
        public void CTClanucherActivity2() {
            String custom = MGplayer.custom();
            if (MGplayer.custom().equals("huanqiu")) {
                huanqiu.s1();
            }
            if (custom.equals("turbotv") && turbo.isvod == 1) {
                Intent intent = new Intent();
                intent.setClass(ValidateActivity.this, VodPlayerMainActivity.class);
                ValidateActivity.this.startActivity(intent);
                lookiptv.lookiptvloaded = true;
                ValidateActivity.this.init();
            } else {
                new Handler().post(new C02363());
            }
            if (MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spainglobal") || MGplayer.custom().equals("spain1cn")) {
                new C02374().start();
            }
        }

        @JavascriptInterface
        public String CTCGetID() {
            return MGplayer.tv.getCpuID();
        }

        @JavascriptInterface
        public String CTCGetCpuName() {
            return MGplayer.getCpuName();
        }

        @JavascriptInterface
        public void CTCProxyName(String value) {
            MGplayer.proxyName = value;
        }

        @JavascriptInterface
        public void CTCSetEveryScrollText(int openEveryScrollText) {
            MGplayer.openEveryScrollText = openEveryScrollText;
            if (MGplayer.openEveryScrollText == 1) {
                MGplayer.scroll_every_init();
            }
        }

        @JavascriptInterface
        public void CTCSetEveryVideo(int openEveryVideo) {
            MGplayer.openEveryVideo = openEveryVideo;
            if (MGplayer.openEveryVideo == 1) {
                MGplayer.video_every_init();
            }
        }

        @JavascriptInterface
        public void CTCSetShowLefttime(int isshow) {
            MGplayer.isShowLefttime = isshow;
        }

        @JavascriptInterface
        public void CTCSetContact(String contact) {
            MGplayer.Contact = contact;
        }

        @JavascriptInterface
        public void CTCSetEpgBackground(String background) {
            MGplayer.Background = background;
        }

        @JavascriptInterface
        public void CTCSetEpgBackground2(String background, String md5) {
            MGplayer.Background = background;
            MGplayer.BackgroundMd5 = md5;
        }

        @JavascriptInterface
        public void CTCSetBroadcast(String broadcast) {
            MGplayer.Broadcast = broadcast;
        }

        @JavascriptInterface
        public void CTCStartBroadcast() {
            MGplayer.openActivity(ValidateActivity.this, ControlPlayerActivity.class);
        }

        @JavascriptInterface
        public boolean CTCIsExistsInterface(String name) {
            return ValidateActivity.this.isExistsInterface(name);
        }

        @JavascriptInterface
        public void CTCSetLoadingImage(String image) {
            if (MGplayer.custom().equals("quanxing")) {
                quanxing.donloadBackground(ValidateActivity.this, image);
            } else if (image.equals("null")) {
                Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
                editor.remove("loadimage");
                editor.commit();
            } else {
                MGplayer.Thread_LoadImage(image, "loadimage");
            }
        }

        @JavascriptInterface
        public void CTCSetLoadingImage2(String image, String md5) {
            if (image.equals("null")) {
                Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
                editor.remove("loadimage");
                editor.commit();
                return;
            }
            MGplayer.Thread_LoadImage(image, "loadimage", md5);
        }

        @JavascriptInterface
        public void CTCSetLoadingAccountImage(String image) {
            if (image.equals("null")) {
                Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
                editor.remove("accountimage");
                editor.commit();
                return;
            }
            MGplayer.Thread_LoadImage(image, "accountimage");
        }

        @JavascriptInterface
        public void CTCSetLoadingAccountImage2(String image, String md5) {
            if (image.equals("null")) {
                Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
                editor.remove("accountimage");
                editor.commit();
                return;
            }
            MGplayer.Thread_LoadImage(image, "accountimage", md5);
        }

        @JavascriptInterface
        public void CTCSetTimeNow(String time) {
            MGplayer.now_second = time;
            MGplayer.runTime();
        }

        @JavascriptInterface
        public void CTCSetTimeNowPRC(String time) {
            MGplayer.now_second_prc = time;
            MGplayer.runTimePRC();
        }

        @JavascriptInterface
        public void CTCSetTimeZonePRC(String zone) {
            MGplayer.now_zone_prc = zone;
        }

        @JavascriptInterface
        public void CTCSetMember(String member) {
            MGplayer.member = member;
        }

        @JavascriptInterface
        public void CTCNetWorkSetting() {
            new AlertDialog.Builder(ValidateActivity.this).setTitle(ValidateActivity.this.getString(C0216R.string.validate_text2).toString()).setIcon(17301659).setPositiveButton(ValidateActivity.this.getString(C0216R.string.ok).toString(), new C02385()).setNegativeButton(ValidateActivity.this.getString(C0216R.string.cancel).toString(), null).show();
        }

        @JavascriptInterface
        public void CTCAddType2(String id, String needps, String ps) {
            MGplayer.MyPrintln("CTCAddType2 id:" + id + " needps:" + needps);
            LIVEplayer.type2Push(id, needps, ps);
        }

        @JavascriptInterface
        public void CTCSetType2Pass(String ps) {
            MGplayer.type2password = ps;
        }

        @JavascriptInterface
        public void CTCSetAP(String ssid, String password, int state, int show) {
            MGplayer.ap_ssid = ssid;
            MGplayer.ap_password = password;
            MGplayer.ap_state = state;
            MGplayer.ap_show = show;
        }

        @JavascriptInterface
        public void CTCSetGeminiTiemer(String times) {
            MGplayer.gemini_tiemer = times;
        }

        @JavascriptInterface
        public void CTCSetDownload(String date, String tip, String url) {
            MGplayer.download_date = date;
            MGplayer.download_tip = tip;
            MGplayer.download_url = url;
        }

        @JavascriptInterface
        public void CTCSetOneScroll(String text, int times) {
            if (text.length() > 1) {
                MGplayer.onescroll_txt = text;
                MGplayer.onescroll_times = times;
            }
        }

        @JavascriptInterface
        public void CTCLoadWebView() {
            Message msg = new Message();
            msg.what = 0;
            if (MGplayer.rHandler.hasMessages(0)) {
                MGplayer.rHandler.removeMessages(0);
            }
            MGplayer.rHandler.sendMessage(msg);
        }

        @JavascriptInterface
        public void CTCEnableHlsPlugIn(int enable) {
            LIVEplayer.enablelsplugin = enable;
        }

        @JavascriptInterface
        public void CTCSetUdp(String ip, String port) {
            MGplayer.udpIP = ip;
            MGplayer.udpPort = port;
            if (MGplayer.udpIP.length() > 7 && MGplayer.udpPort.length() > 2) {
                custom.start();
            }
        }

        @JavascriptInterface
        public int CTCKey(String key) {
            return MGplayer.playlistkey(key);
        }

        @JavascriptInterface
        public void showUpdateUI() {
            ValidateActivity.this.updateActivity();
        }

        @JavascriptInterface
        public void showSource(String html) {
            Log.d("HTML", html);
        }

        @JavascriptInterface
        public void CTCSetNumber(String number) {
            MGplayer.number = number;
        }

        @JavascriptInterface
        public void CTCSetEpgStyle(String style) {
            MGplayer.style = style;
        }

        @JavascriptInterface
        public void CTCSetEpgList(String epgs) {
            Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
            editor.putString("epglist", epgs);
            editor.commit();
        }

        @JavascriptInterface
        public void CTCCheckTimeout(int time) {
            MGplayer.check_timeout(time);
        }

        @JavascriptInterface
        public void CTCLivePlayLeftRight(int value) {
            MGplayer.livelist_leftright = value;
        }

        @JavascriptInterface
        public void CTCLivePlayShowScroll(int value) {
            MGplayer.livescroll_show = value;
        }

        @JavascriptInterface
        public void CTCLivePlayShowScroll2(int value, int times) {
            MGplayer.livescroll_show = value;
            MGplayer.livescroll_showtimes = times;
        }

        @JavascriptInterface
        public void CTCVodSetColumn(String value) {
            MGplayer.vodcolumn = value;
            VODplayer.init_Column();
        }

        @JavascriptInterface
        public void CTCLiveWatermark(String value) {
            if (value != null && value.length() > 4 && !MGplayer.fileIsExists(MGplayer.images_icon + value) && (value.endsWith("png") || value.endsWith("jpg") || value.endsWith("jpeg") || value.endsWith("gif"))) {
                try {
                    MGplayer.MyPrintln("download live icon " + value);
                    MGplayer.donwFile(MGplayer.tv.gete() + "/images/livepic/" + value, MGplayer.images_icon + value);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            LIVEplayer.watermask = value;
        }

        @JavascriptInterface
        public void CTCLiveWatermarkSite(int site) {
            MGplayer.watermark_site = site;
        }

        @JavascriptInterface
        public void CTCLiveWatermarkSite2(int site, int dip1, int dip2) {
            MGplayer.watermark_site = site;
            MGplayer.watermark_dip1 = dip1;
            MGplayer.watermark_dip2 = dip2;
        }

        @JavascriptInterface
        public String CTCGetModel() {
            Build bd = new Build();
            String model = Build.MODEL;
            MGplayer.MyPrintln("model:" + model);
            return model;
        }

        @JavascriptInterface
        public void CTCSetAdLiveImage(String name) {
            LIVEplayer.adimagepush(name);
            MGplayer.MyPrintln("CTCSetAdLiveImage:" + name);
        }

        @JavascriptInterface
        public void CTCSetAdLiveImageSite(int site) {
            MGplayer.adliveimage_site = site;
        }

        @JavascriptInterface
        public int CTCCheckSign(int key) {
            if (key == GHash.rotatingHash(MGplayer.getSingInfo(), 65535)) {
                return 1;
            }
            return 0;
        }

        @JavascriptInterface
        public String CTCGetSign() {
            return MGplayer.getSingInfo();
        }

        @JavascriptInterface
        public void CTCLivePlaylistIcon(int show) {
            if (show > 0) {
                LIVEplayer.show_playlist_image = true;
            } else {
                LIVEplayer.show_playlist_image = false;
            }
        }

        @JavascriptInterface
        public void CTCVodCount(int num0, int num1, int num2, int num3) {
            VODplayer.vod_number[0] = num0;
            VODplayer.vod_number[1] = num1;
            VODplayer.vod_number[2] = num2;
            VODplayer.vod_number[3] = num3;
        }

        @JavascriptInterface
        public void CTCUpdateTip(int show) {
            MGplayer.updatetip_show = show;
        }

        @JavascriptInterface
        public void CTCStartPlayback() {
            Intent intent = new Intent();
            intent.setClass(ValidateActivity.this, BackPlayerActivity.class);
            ValidateActivity.this.startActivity(intent);
            ValidateActivity.this.init();
        }

        @JavascriptInterface
        public void CTCStartForcetv(int cache) {
            Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
            editor.putInt("forcetvcache", cache);
            editor.commit();
        }

        @JavascriptInterface
        public void CTCSetLanguage(int language) {
            if (language == 0) {
                ValidateActivity.this.switchLanguage(Locale.SIMPLIFIED_CHINESE);
            } else if (language == 1) {
                ValidateActivity.this.switchLanguage(Locale.TRADITIONAL_CHINESE);
            } else if (language == 2) {
                ValidateActivity.this.switchLanguage(Locale.JAPAN);
            } else if (language == 4) {
                ValidateActivity.this.switchLanguage(Locale.KOREA);
            } else if (language == 5) {
                ValidateActivity.this.switchLanguage(new Locale("es", "ES"));
            } else if (language == 6) {
                ValidateActivity.this.switchLanguage(new Locale("pt", "BR"));
            } else if (language == 7) {
                ValidateActivity.this.switchLanguage(new Locale("pt", "PT"));
            } else if (language == 8) {
                ValidateActivity.this.switchLanguage(Locale.ENGLISH);
            } else if (language > 0) {
                ValidateActivity.this.switchLanguage(Locale.SIMPLIFIED_CHINESE);
            }
        }

        @JavascriptInterface
        public void CTCOutputKeyBroad() {
            MGplayer.MyPrintln("CTCOutputKeyBroad");
            ((InputMethodManager) ValidateActivity.this.getSystemService("input_method")).toggleSoftInput(0, 2);
        }

        @JavascriptInterface
        public void CTCCheckVideoTimes(int value) {
            MGplayer.checkVideo_times = value;
            MGplayer.MyPrintln("CTCCheckVideoTimes:" + value);
        }

        @JavascriptInterface
        public void CTCSetValue(String filename, String val) {
            MGplayer.writeSD(filename, val);
        }

        @JavascriptInterface
        public String CTCGetValue(String filename) {
            return MGplayer.readSD(filename);
        }

        @JavascriptInterface
        public void CTCSetProgress(int val) {
            if (ValidateActivity.this.load_progressbar == null) {
                return;
            }
            Message msg;
            if (val < 0) {
                if (ValidateActivity.this.load_progressbar.isShown()) {
                    msg = new Message();
                    msg.what = 5;
                    ValidateActivity.this.wHandler.sendMessage(msg);
                }
            } else if (ValidateActivity.this.load_progressbar.isShown()) {
                msg = new Message();
                Bundle data = new Bundle();
                data.putInt("progress", val);
                msg.setData(data);
                msg.what = 4;
                ValidateActivity.this.wHandler.sendMessage(msg);
            }
        }

        @JavascriptInterface
        public void CTCSetProgress2(int val, String text) {
            if (ValidateActivity.this.load_progressbar == null) {
                return;
            }
            Message msg;
            if (val < 0) {
                if (ValidateActivity.this.load_progressbar.isShown()) {
                    msg = new Message();
                    msg.what = 5;
                    ValidateActivity.this.wHandler.sendMessage(msg);
                }
            } else if (ValidateActivity.this.load_progressbar.isShown()) {
                msg = new Message();
                Bundle data = new Bundle();
                data.putInt("progress", val);
                if (text.length() > 0) {
                    data.putString(MimeTypes.BASE_TYPE_TEXT, text);
                }
                msg.setData(data);
                msg.what = 4;
                ValidateActivity.this.wHandler.sendMessage(msg);
            }
        }

        @JavascriptInterface
        public void CTCSetVodEpg(String url) {
            VODplayer.vod_epg = url;
        }

        @JavascriptInterface
        public void CTCSetLiveUiType(int type) {
            MGplayer.live_ui_type = type;
        }

        @JavascriptInterface
        public String CTCGetHttps(String url) {
            return MGplayer.sendServerCmd_https(url);
        }

        @JavascriptInterface
        public void CTCSetLiveShowSpeed(int show) {
            MGplayer.show_live_speed = show;
        }

        @JavascriptInterface
        public void CTCSetLiveShowMAC(int show) {
            MGplayer.live_showmac = show;
        }

        @JavascriptInterface
        public void CTCExitUrl(String url) {
            quanxing.exit_url = url;
        }

        @JavascriptInterface
        public void CTCLoadingTip(String text) {
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString(MimeTypes.BASE_TYPE_TEXT, text);
            msg.setData(data);
            msg.what = 2;
            ValidateActivity.this.wHandler.sendMessage(msg);
        }

        @JavascriptInterface
        public String CTCGetWifiMac() {
            return ((WifiManager) ValidateActivity.this.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getMacAddress();
        }

        @JavascriptInterface
        public void CTCLoadTip(String text0, String text1, String text2) {
            quanxing.tips.clear();
            quanxing.tips.add(text0);
            quanxing.tips.add(text1);
            quanxing.tips.add(text2);
        }

        @JavascriptInterface
        public void CTCLoadTip2(String tip) {
            quanxing.tip_text8 = tip;
            MGplayer.scrolltext = tip;
        }

        @JavascriptInterface
        public void CTCSetXmlPassword(String ps, String ps2) {
            quanxing.xmlpassword = MGplayer.j2(ps, "78120328" + ps2);
            MGplayer.MyPrintln("xmlpassword:" + quanxing.xmlpassword);
        }

        @JavascriptInterface
        public void CTCSetIcon(String url, String password, int version) {
            quanxing.iconurl = url;
            quanxing.iconpassword = password;
            quanxing.iconversion = version;
        }

        @JavascriptInterface
        public void CTCSetPanel(int index) {
            quanxing.panel = index;
        }

        @JavascriptInterface
        public void CTCSetRunCheck(int times, String url) {
            quanxing.checkruntimes = times;
            quanxing.checkrunurl = url;
        }

        @JavascriptInterface
        public void CTCSetRunCheck(int times, String url, int timeout) {
            quanxing.checkruntimes = times;
            quanxing.checkrunurl = url;
            quanxing.checktimeout = timeout;
        }

        @JavascriptInterface
        public String CTCGetLanguage() {
            return (Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry()).toLowerCase();
        }

        @JavascriptInterface
        public void CTCAddUrl(int id, String name, String url, String password, String type, String introduction, String xml) {
            MGplayer.MyPrintln("CTCAddUrl:" + url);
            LIVEplayer.urlPush(id, name, introduction, url, password, type, "hd", xml);
        }

        @JavascriptInterface
        public void CTCSetHotlink(String hotlink, String password) {
            quanxing.hotlink = MGplayer.j2(hotlink, "65827855" + password);
        }

        @JavascriptInterface
        public void CTCSetUrlPassword(String ps) {
            MGplayer.urlpassword = ps;
            quanxing.urlpassword = "60785559" + ps;
        }

        @JavascriptInterface
        public String CTCGetSDCardID() {
            return quanxing.readSDFile(quanxing.qxid);
        }

        @JavascriptInterface
        public String CTCGETtempID() {
            return CTCGetSDCardID();
        }

        @JavascriptInterface
        public void CTCSETtempID(String str) {
            quanxing.saveToSDCard(quanxing.qxid, str);
        }

        @JavascriptInterface
        public void CTCWifiTip(int value) {
            ValidateActivity.this.startActivity(new Intent("android.settings.SETTINGS"));
        }

        @JavascriptInterface
        public void CTCLogin() {
            Message msg = new Message();
            msg.what = 1;
            ValidateActivity.this.wHandler.sendMessage(msg);
        }

        @JavascriptInterface
        public void CTCFinish() {
            ValidateActivity.this.finish();
        }

        @JavascriptInterface
        public void CTCAudoBoot(int value) {
            Editor editor = MGplayer.MyGetSharedPreferences(ValidateActivity.this, "data", 0).edit();
            editor.putInt("autoboot", value);
            editor.commit();
        }

        @JavascriptInterface
        public String CTCGetPhoneNumber() {
            return ValidateActivity.this.siminfo.getNativePhoneNumber();
        }

        @JavascriptInterface
        public String CTCGetProvidersName() {
            return ValidateActivity.this.siminfo.getProvidersName();
        }

        @JavascriptInterface
        public String CTCGetIMSI() {
            return ValidateActivity.this.siminfo.getSubscriberId();
        }

        @JavascriptInterface
        public String CTCGetIMEI() {
            return ValidateActivity.this.siminfo.getDeviceId();
        }

        @JavascriptInterface
        public String CTCGetAppID() {
            return MGplayer.tv.gaid();
        }

        @JavascriptInterface
        public String CTCGetUUID() {
            return MGplayer.tv.getMyUUID();
        }

        @JavascriptInterface
        public void CTCSetShowLivePlaylist(String name) {
            MGplayer.showliveplaylistname = name;
        }

        @JavascriptInterface
        public void CTCSetIP(String ip) {
            MGplayer.ip = ip;
        }

        @JavascriptInterface
        public void CTCShowUpdateDialog(String s) {
            String t;
            if (s.length() > 0) {
                t = s;
            } else {
                t = ValidateActivity.this.getString(C0216R.string.update_text3).toString();
            }
            UpdateActivity.showUpdate(ValidateActivity.this, ValidateActivity.this.wHandler);
        }

        @JavascriptInterface
        public void CTCSetLivePlaytimeout(int timeout) {
            MGplayer.liveplaytimeout = timeout;
            MGplayer.MyPrintln("MGplayer.liveplaytimeout " + MGplayer.liveplaytimeout);
        }

        @JavascriptInterface
        public void CTCSetWifiAP(String ssid, String password, int enable) {
            WifiAP aper = new WifiAP();
            if (enable != 1) {
                aper.setWifiApEnabled(ValidateActivity.this, false, ssid, password);
            } else if (!aper.setWifiApEnabled(ValidateActivity.this, true, ssid, password)) {
                MGplayer.MyPrintln("setWifiApEnabled false");
            }
        }

        @JavascriptInterface
        public void CTCSetControlPlayer(int time) {
            MGplayer.RunControlPlayer(time);
        }

        @JavascriptInterface
        public void CTCPlaybackDaytime(int daytime) {
            if (daytime > 6 || daytime < 0) {
                MyBackPreviewView.backpreviewdaytime = 6;
            } else {
                MyBackPreviewView.backpreviewdaytime = daytime;
            }
        }

        @JavascriptInterface
        public void CTCSetTimeEndPRC(long endtime) {
            MGplayer.endtime = endtime;
        }

        @JavascriptInterface
        public String CTCGetCpuNameInfo() {
            return MGplayer.writeCpuName();
        }

        @JavascriptInterface
        public String CTCHttpsrequest(String url) {
            return MGplayer.sendServerCmd_https(url);
        }

        @JavascriptInterface
        public void CTCMenu() {
            MenuView.gridMenuInit(ValidateActivity.this);
            MenuView.showAlertDialog(ValidateActivity.this);
        }

        @JavascriptInterface
        public void CTCLoadLIbs(String strUrl, String md5, String sofile, int version) {
            MGplayer.libforcetv_version = version;
            LoadlibsView.showDownload(ValidateActivity.this, strUrl, md5, sofile, version);
        }

        @JavascriptInterface
        public void CTCSetVodShowPage(int value) {
            MGplayer.vod_showpage = value;
        }

        @JavascriptInterface
        public void CTCIntervalExit(int seconds) {
            MGplayer.interval_time = seconds;
            MGplayer.runTimeExit(ValidateActivity.this);
        }

        @JavascriptInterface
        public void CTCSetToken(String value) {
            MGplayer.set_token_value = value;
        }

        @JavascriptInterface
        public void CTCSetLookiptv(String name, String mac, String url1, String url2, String url3) {
        }

        @JavascriptInterface
        public String CTCMD5(String t) {
            return MGplayer.MD5(t);
        }

        @JavascriptInterface
        public String CTCJson(String json, String key) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject != null) {
                    return jsonObject.getString(key);
                }
                return "";
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }

        @JavascriptInterface
        public String CTCSendServerCmdCookies2(String cmd, String cookie, int timeout, String useragent) {
            MGplayer.MyPrintln("lookiptv webview loadurl2 " + cmd);
            CookieStatus cookier = MGplayer.SendServerCmd_cookies(cmd, timeout, cookie, useragent);
            return cookier.ret + "@#@" + cookier.cookie;
        }

        @JavascriptInterface
        public String CTCSendServerCmdCookies(String cmd, String cookie, int timeout) {
            MGplayer.MyPrintln("lookiptv webview loadurl2 " + cmd);
            CookieStatus cookier = MGplayer.SendServerCmd_cookies(cmd, timeout, cookie, null);
            return cookier.ret + "@#@" + cookier.cookie;
        }

        @JavascriptInterface
        public String CTCSendServerCmd2(String cmd, String cookie, int timeout, String useragent) {
            return MGplayer.sendServerCmd(cmd, timeout, cookie, useragent);
        }

        @JavascriptInterface
        public String CTCSendServerCmd(String cmd, String cookie, int timeout) {
            return MGplayer.sendServerCmd(cmd, timeout, cookie, null);
        }

        @JavascriptInterface
        public String CTCSendServerCookies2(String cmd, String cookie, int timeout, String useragent) {
            return MGplayer.sendServerCookie(cmd, timeout, cookie, useragent);
        }

        @JavascriptInterface
        public String CTCSendServerCookies(String cmd, String cookie, int timeout) {
            return MGplayer.sendServerCookie(cmd, timeout, cookie, null);
        }

        @JavascriptInterface
        public String CTCPostServerCmdAndCookies(String cmd, String param, String useragent, String cookie, int timeout) {
            return MGplayer.submitPostData(cmd, param, useragent, cookie, timeout, 2);
        }

        @JavascriptInterface
        public String CTCPostServerCmd(String cmd, String param, String useragent, String cookie, int timeout) {
            return MGplayer.submitPostData(cmd, param, useragent, cookie, timeout, 1);
        }

        @JavascriptInterface
        public String CTCPostServerCmd2(String cmd, String param, String useragent, String cookie, int timeout, String contenttype) {
            return MGplayer.submitPostData(cmd, param, useragent, cookie, timeout, contenttype, 1);
        }

        @JavascriptInterface
        public String CTCPostServerCookies(String cmd, String param, String useragent, String cookie, int timeout) {
            return MGplayer.submitPostData(cmd, param, useragent, cookie, timeout, 0);
        }

        @JavascriptInterface
        public String CTCSendHttpsServerCmdCookies2(String cmd, String cookie, int timeout, String useragent) {
            MGplayer.MyPrintln("lookiptv webview loadurl2 " + cmd);
            CookieStatus cookier = MGplayer.SendHttpsServerCmd_cookies(cmd, timeout, cookie, useragent);
            return cookier.ret + "@#@" + cookier.cookie;
        }

        @JavascriptInterface
        public String CTCSendHttpsServerCmdCookies(String cmd, String cookie, int timeout) {
            MGplayer.MyPrintln("lookiptv webview loadurl2 " + cmd);
            CookieStatus cookier = MGplayer.SendHttpsServerCmd_cookies(cmd, timeout, cookie, null);
            return cookier.ret + "@#@" + cookier.cookie;
        }

        @JavascriptInterface
        public String CTCHttpsSendServerCmd2(String cmd, String cookie, int timeout, String useragent) {
            return MGplayer.sendHttpsServerCmd(cmd, timeout, cookie, useragent);
        }

        @JavascriptInterface
        public String CTCHttpsSendServerCmd(String cmd, String cookie, int timeout) {
            return MGplayer.sendHttpsServerCmd(cmd, timeout, cookie, null);
        }

        @JavascriptInterface
        public String CTCHttpsSendServerCookies2(String cmd, String cookie, int timeout, String useragent) {
            return MGplayer.sendHttpsServerCookie(cmd, timeout, cookie, useragent);
        }

        @JavascriptInterface
        public String CTCHttpsSendServerCookies(String cmd, String cookie, int timeout) {
            return MGplayer.sendHttpsServerCookie(cmd, timeout, cookie, null);
        }

        @JavascriptInterface
        public String CTCHttpsPostServerCmdAndCookies(String cmd, String param, String useragent, String cookie, int timeout) {
            return MGplayer.submitHttpsPostData(cmd, param, useragent, cookie, timeout, 2);
        }

        @JavascriptInterface
        public String CTCHttpsPostServerCmd(String cmd, String param, String useragent, String cookie, int timeout) {
            return MGplayer.submitHttpsPostData(cmd, param, useragent, cookie, timeout, 1);
        }

        @JavascriptInterface
        public String CTCHttpsPostServerCookies(String cmd, String param, String useragent, String cookie, int timeout) {
            return MGplayer.submitHttpsPostData(cmd, param, useragent, cookie, timeout, 0);
        }

        @JavascriptInterface
        public void CTCSetLookiptvEverySend2(String url, int timeout, int sendevery) {
            MGplayer.MyPrintln("CTCSetLookiptvEverySend2");
            lookiptv.lookiptvSendUrl = url;
            lookiptv.lookiptvSendTimeOut = timeout;
            lookiptv.lookiptvSendEvery = sendevery;
            lookiptv.lookiptvloaded = false;
        }

        @JavascriptInterface
        public void CTCSetLookiptvMacCpuid(String mac, String cpuid) {
            lookiptv.mac = mac;
            lookiptv.cpuid = cpuid;
        }

        @JavascriptInterface
        public void CTCSetLookiptvUserAndKey(String user, String key) {
            lookiptv.username = user;
            lookiptv.userkey = key;
        }

        @JavascriptInterface
        public String CTCGetLocalKey() {
            if (MGplayer.LocalKeyID == null) {
                return MGplayer.getLocalKey();
            }
            return MGplayer.LocalKeyID;
        }

        @JavascriptInterface
        public void CTCRunDebug() {
        }

        @JavascriptInterface
        public void CTCResizeVodImage(int resize) {
            MGplayer.resize_vod_image = resize;
        }

        @JavascriptInterface
        public String CTCGetSystemID() {
            return MGplayer.getSystemID();
        }

        @JavascriptInterface
        public void CTCUpnp(String port, String pol) {
            MGplayer.upnp(port, pol);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.main);
        getWindow().setFlags(1024, 1024);
        MemoTVCastSDK.init(this);
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_PHONE_STATE"}, 1);
        }
        verifyStoragePermissions(this);
        if (Vitamio.isInitialized(getApplicationContext())) {
            this.siminfo = new PhoneInfo(this);
            ((ActivityManager) getSystemService("activity")).getMemoryClass();
            this.interfaceList = new ArrayList();
            saveInterface();
            this.mWebView = (WebView) findViewById(C0216R.id.webView);
            this.loading_text2 = (TextView) findViewById(C0216R.id.loadingtext2);
            MGplayer.video_every_interface(this.onControlVideo);
            int ret = MGplayer.m13t(this);
            if (ret == 0) {
                double mWebrate;
                if (MGplayer.custom().equals("szysx") || MGplayer.custom().equals("dhtv") || MGplayer.custom().equals("familytv")) {
                    this.load_progressbar = (NumberProgressBar) findViewById(C0216R.id.load_progressBar_szysx);
                    ((NumberProgressBar) findViewById(C0216R.id.load_progressBar)).setVisibility(8);
                } else {
                    this.load_progressbar = (NumberProgressBar) findViewById(C0216R.id.load_progressBar);
                    ((NumberProgressBar) findViewById(C0216R.id.load_progressBar_szysx)).setVisibility(8);
                }
                this.load_textview = (TextView) findViewById(C0216R.id.loadingtext2);
                this.load_progressbar.setProgress(10);
                Typeface typeFace = MGplayer.getFontsType(this);
                float rate = MGplayer.getFontsRate();
                if (MGplayer.custom().equals("fszj")) {
                    this.load_progressbar.setVisibility(8);
                    this.load_textview.setVisibility(8);
                    this.loading_text = (TextView) findViewById(C0216R.id.loadingtext);
                    this.loading_text.setVisibility(0);
                    this.loading_text.setTextSize(10.0f * rate);
                    this.loading_text.setTypeface(typeFace);
                    LayoutParams lp = (FrameLayout.LayoutParams) this.loading_text.getLayoutParams();
                    lp.gravity = 80;
                    lp.setMargins(0, 0, 0, 50);
                    this.loading_text.setLayoutParams(lp);
                }
                if (MGplayer.custom().equals("quanxing")) {
                    this.load_progressbar.setVisibility(8);
                    this.load_textview.setVisibility(8);
                }
                startService(new Intent(this, LocalService.class));
                this.mPlugin = new PluginInterface();
                this.mWebSettings = this.mWebView.getSettings();
                this.mWebSettings.setJavaScriptEnabled(true);
                this.mWebView.setWebViewClient(new MyWebViewClient());
                this.mWebView.addJavascriptInterface(this.mPlugin, "Authentication");
                this.mWebSettings.setCacheMode(2);
                if (!MGplayer.custom().equals("quanxing")) {
                    this.mWebView.getSettings().setUserAgentString(MGplayer.gu());
                }
                this.mWebView.getSettings().setUseWideViewPort(true);
                this.mWebView.getSettings().setSupportZoom(true);
                this.mWebView.getSettings().setBuiltInZoomControls(true);
                this.mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
                this.mWebView.getSettings().setLoadWithOverviewMode(true);
                if (((double) MGplayer.screenWidth) / 1280.0d > ((double) MGplayer.screenHeight) / 720.0d) {
                    mWebrate = ((double) MGplayer.screenHeight) / 720.0d;
                } else {
                    mWebrate = ((double) MGplayer.screenWidth) / 1280.0d;
                }
                MGplayer.MyPrintln("webview rate = " + (100.0d * mWebrate) + "ValidateActivity.screenWidth = " + MGplayer.screenWidth);
                this.mWebView.setInitialScale((int) (100.0d * mWebrate));
                this.mWebSettings.setSupportZoom(true);
                this.mWebView.setWebChromeClient(new C02221());
                String loadimage = MGplayer.MyGetSharedPreferences(this, "data", 0).getString("loadimage", null);
                MGplayer.MyPrintln("loadimage=" + loadimage);
                if (loadimage == null || !MGplayer.fileIsExists(getFilesDir() + "/background/" + loadimage)) {
                    if (MGplayer.custom().equals("jhome") && jhome.bg_default) {
                        this.mWebView.setBackgroundResource(C0216R.mipmap.jhomebg);
                    } else if (MGplayer.custom().equals("quanxing")) {
                        this.mWebView.setBackgroundResource(C0216R.mipmap.qxbg);
                    } else if (MGplayer.custom().equals("badatv")) {
                        this.mWebView.setBackgroundResource(C0216R.mipmap.bada_bg);
                    } else {
                        this.mWebView.setBackgroundResource(C0216R.mipmap.bg);
                    }
                    this.mWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                } else {
                    String imagePath = getFilesDir() + "/background/" + loadimage;
                    new Options().inSampleSize = 4;
                    this.mWebView.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(imagePath)));
                    this.mWebView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                }
                if (MGplayer.tv.gete().length() < 14) {
                    MGplayer.MyPrintln("MGplayer.tv.gete = " + MGplayer.tv.gete());
                    Process.killProcess(Process.myPid());
                    MGplayer.exit_app();
                } else if (!MGplayer.tv.gete().startsWith("http://") || ((MGplayer.tv.gete().startsWith("http://") && MGplayer.tv.gete().endsWith(OkPlayerUtils.EXT_HLS)) || (MGplayer.tv.gete().startsWith("http://") && MGplayer.tv.gete().endsWith("gemini")))) {
                    MGplayer.playlistkey("");
                    LIVEplayer.typePush("0000", "ALL");
                    MGplayer.MyPrintln("MGplayer.tv.gete() = " + MGplayer.tv.gete());
                    if (MGplayer.key(MGplayer.tv.gete()).length() < 14) {
                        MGplayer.MyPrintln("MGplayer.tv.gete = " + MGplayer.tv.gete());
                        Process.killProcess(Process.myPid());
                        MGplayer.exit_app();
                    }
                    LIVEplayer.urlPush(1, "ALL", "", MGplayer.key(MGplayer.tv.gete()), "", "0000", "", "hd", "");
                    Intent intent = new Intent();
                    intent.setClass(this, LivePlayerActivity.class);
                    startActivity(intent);
                    MGplayer.boot_launcher = false;
                } else if (MGplayer.custom().equals("quanxing2")) {
                    MGplayer.current_version = quanxing.version;
                    settings = MGplayer.MyGetSharedPreferences(this, "data", 0);
                    home_page = settings.getString("home_page", "http://192.168.1.2:18006/gemini-iptv1/");
                    this.mWebView.loadUrl(home_page);
                    MGplayer.epgList.add("http://192.168.1.2:18006/gemini-iptv1/");
                    home_page2 = settings.getString("home_page2", null);
                    if (home_page2 != null) {
                        MGplayer.epgList.add(home_page2);
                    }
                    MGplayer.epgList.add(home_page);
                    timeHander = new Handler();
                    r2 = timeHander;
                    timeHander.postDelayed(new Runnable() {
                        public void run() {
                            MGplayer.MyPrintln("MGplayer.load_timeout:" + MGplayer.load_timeout);
                            if (MGplayer.load_timeout == 1) {
                                MGplayer.epgList_index++;
                                if (MGplayer.epgList_index >= MGplayer.epgList.size()) {
                                    MGplayer.epgList_index = -1;
                                }
                                ValidateActivity.this.mWebView.loadUrl(MGplayer.tv.gete());
                                r2.postDelayed(this, 30000);
                            }
                        }
                    }, 30000);
                } else if (MGplayer.custom().equals("quanxing")) {
                    MGplayer.current_version = quanxing.version;
                    settings = MGplayer.MyGetSharedPreferences(this, "data", 0);
                    home_page = settings.getString("home_page", "http://192.168.1.3:18006/gemini-iptv1/");
                    this.mWebView.loadUrl(home_page);
                    MGplayer.epgList.add(home_page);
                    home_page2 = settings.getString("home_page2", null);
                    if (home_page2 != null) {
                        MGplayer.epgList.add(home_page2);
                    }
                    timeHander = new Handler();
                    r2 = timeHander;
                    timeHander.postDelayed(new Runnable() {
                        public void run() {
                            if (MGplayer.load_timeout == -1) {
                                MGplayer.epgList_index++;
                                if (MGplayer.epgList_index >= MGplayer.epgList.size()) {
                                    MGplayer.epgList_index = -1;
                                }
                                ValidateActivity.this.mWebView.loadUrl(MGplayer.tv.gete());
                                r2.postDelayed(this, 30000);
                            }
                        }
                    }, 30000);
                } else if (MGplayer.custom().equals("zeus")) {
                    MGplayer.current_version = quanxing.version;
                    settings = MGplayer.MyGetSharedPreferences(this, "data", 0);
                    home_page = settings.getString("home_page", "http://192.168.1.3:18006/gemini-iptv2/");
                    this.mWebView.loadUrl(home_page);
                    home_page2 = settings.getString("home_page2", null);
                    if (home_page2 != null) {
                        MGplayer.epgList.add("http://192.168.1.3:18006/gemini-iptv3/");
                    }
                    MGplayer.epgList.add(home_page2);
                    MGplayer.epgList.add(home_page);
                    timeHander = new Handler();
                    r2 = timeHander;
                    timeHander.postDelayed(new Runnable() {
                        public void run() {
                            MGplayer.MyPrintln("MGplayer.load_timeout:" + MGplayer.load_timeout);
                            if (MGplayer.load_timeout == 1) {
                                MGplayer.epgList_index++;
                                if (MGplayer.epgList_index >= MGplayer.epgList.size()) {
                                    MGplayer.epgList_index = -1;
                                }
                                ValidateActivity.this.mWebView.loadUrl(MGplayer.tv.gete());
                                r2.postDelayed(this, 30000);
                            }
                        }
                    }, 30000);
                } else if (MGplayer.isMobile(this)) {
                    MyDialog.Builder builder = new MyDialog.Builder(this);
                    builder.setMessage(getString(C0216R.string.validate_text12).toString());
                    builder.setPositiveButton(getString(C0216R.string.ok).toString(), new C02265());
                    builder.setNegativeButton(getString(C0216R.string.validate_text13).toString(), new C02276());
                    builder.create().show();
                } else {
                    MGplayer.getWebView(this.mWebView);
                }
                if (MGplayer.custom().equals("cxiptv") || MGplayer.custom().equals("badatv") || MGplayer.custom().equals("hanatv")) {
                    switchLanguage(Locale.KOREA);
                }
            } else if (ret == 1) {
                MyToast.makeText(getApplicationContext(), getString(C0216R.string.error_text1).toString(), 0);
                this.loading_text2.setText(getString(C0216R.string.error_text1).toString());
            } else if (ret == 2) {
                MyToast.makeText(getApplicationContext(), getString(C0216R.string.error_text2).toString(), 0);
                this.loading_text2.setText(getString(C0216R.string.error_text2).toString());
            } else if (ret == 3) {
                MyToast.makeText(getApplicationContext(), getString(C0216R.string.error_text3).toString(), 0);
                this.loading_text2.setText(getString(C0216R.string.error_text3).toString());
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
        }
        if (ContextCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
        }
    }

    public void switchLanguage(Locale locale) {
        Configuration config = getResources().getConfiguration();
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.locale = locale;
        resources.updateConfiguration(config, dm);
    }

    public void saveInterface() {
        this.interfaceList.add("CTCSetLoadingImage");
        this.interfaceList.add("CTClanucherActivity2");
        this.interfaceList.add("CTCSetTimeNow");
        this.interfaceList.add("CTCSetMember");
        this.interfaceList.add("CTCSetTimeNowPRC");
        this.interfaceList.add("CTCAddType2");
        this.interfaceList.add("CTCSetType2Pass");
        this.interfaceList.add("CTCSetAP");
        this.interfaceList.add("CTCSetGeminiTiemer");
        this.interfaceList.add("CTCSetDownload");
        this.interfaceList.add("CTCSetOneScroll");
        this.interfaceList.add("CTCSetEveryVideo");
        this.interfaceList.add("CTCLoadWebView");
        this.interfaceList.add("CTCEnableHlsPlugIn");
        this.interfaceList.add("CTCAddPlayback");
        this.interfaceList.add("CTCAddPlayback2");
        this.interfaceList.add("CTCAddPlaybackType");
        this.interfaceList.add("CTCSetUdp");
        this.interfaceList.add("CTCKey");
        this.interfaceList.add("CTCSetNumber");
        this.interfaceList.add("CTCSetEpgStyle");
        this.interfaceList.add("CTCAudoBoot");
        this.interfaceList.add("CTCSetEpgList");
        this.interfaceList.add("CTCGetCpuName");
        this.interfaceList.add("CTCCheckTimeout");
        this.interfaceList.add("CTCVodSetColumn");
        this.interfaceList.add("CTCLiveWatermark");
        this.interfaceList.add("CTCLivePlayLeftRight");
        this.interfaceList.add("CTCLivePlayShowScroll");
        this.interfaceList.add("CTCLivePlayShowScroll2");
        this.interfaceList.add("CTCGetModel");
        this.interfaceList.add("CTCSetAdLiveImage");
        this.interfaceList.add("CTCLiveWatermarkSite");
        this.interfaceList.add("CTCLivePlaylistIcon");
        this.interfaceList.add("CTCAddUrl2");
        this.interfaceList.add("CTCVodCount");
        this.interfaceList.add("CTCLiveWatermarkSite2");
        this.interfaceList.add("CTCUpdateTip");
        this.interfaceList.add("CTCStartPlayback");
        this.interfaceList.add("CTCSetTimeZonePRC");
        this.interfaceList.add("CTCSetAdLiveImageSite");
        this.interfaceList.add("CTCStartForcetv");
        this.interfaceList.add("CTCSetLanguage");
        this.interfaceList.add("CTCSetLoadingAccountImage");
        this.interfaceList.add("CTCOutputKeyBroad");
        this.interfaceList.add("CTCCheckVideoTimes");
        this.interfaceList.add("CTCSetValue");
        this.interfaceList.add("CTCGetValue");
        this.interfaceList.add("CTCSetProgress");
        this.interfaceList.add("CTCSetLiveUiType");
        this.interfaceList.add("CTCSetLoadingImage2");
        this.interfaceList.add("CTCSetLoadingAccountImage2");
        this.interfaceList.add("CTCGetPhoneNumber");
        this.interfaceList.add("CTCGetProvidersName");
        this.interfaceList.add("CTCSetEpgBackground2");
        this.interfaceList.add("CTCGetIMSI");
        this.interfaceList.add("CTCGetIMEI");
        this.interfaceList.add("CTCSetLookiptv");
        this.interfaceList.add("CTCMD5");
        this.interfaceList.add("CTCJson");
        this.interfaceList.add("CTCSendServerCmdCookies");
        this.interfaceList.add("CTCSendServerCmd");
        this.interfaceList.add("CTCSendServerCookies");
        this.interfaceList.add("CTCSendServerCmdCookies2");
        this.interfaceList.add("CTCSendServerCmd2");
        this.interfaceList.add("CTCSendServerCookies2");
        this.interfaceList.add("CTCSetLookiptvEverySend2");
        this.interfaceList.add("CTCSetProgress2");
        this.interfaceList.add("CTCSetVodEpg");
        this.interfaceList.add("CTCGetAppID");
        this.interfaceList.add("CTCGetUUID");
        this.interfaceList.add("CTCGetSign");
        this.interfaceList.add("CTCSetShowLivePlaylist");
        this.interfaceList.add("CTCSetIP");
        this.interfaceList.add("CTCShowUpdateDialog");
        this.interfaceList.add("CTCPostServerCmdAndCookies");
        this.interfaceList.add("CTCPostServerCmd");
        this.interfaceList.add("CTCPostServerCmd2");
        this.interfaceList.add("CTCPostServerCookies");
        this.interfaceList.add("CTCSetLivePlaytimeout");
        this.interfaceList.add("CTCSetLookiptvMacCpuid");
        this.interfaceList.add("CTCSetWifiAP");
        this.interfaceList.add("CTCSetControlPlayer");
        this.interfaceList.add("CTCPlaybackDaytime");
        this.interfaceList.add("CTCSetTimeEndPRC");
        this.interfaceList.add("CTCSetLookiptvUserAndKey");
        this.interfaceList.add("CTCGetCpuNameInfo");
        this.interfaceList.add("CTCHttpsrequest");
        this.interfaceList.add("CTCSendHttpsServerCmdCookies2");
        this.interfaceList.add("CTCSendHttpsServerCmdCookies");
        this.interfaceList.add("CTCHttpsSendServerCmd2");
        this.interfaceList.add("CTCHttpsSendServerCmd");
        this.interfaceList.add("CTCHttpsSendServerCookies2");
        this.interfaceList.add("CTCHttpsSendServerCookies");
        this.interfaceList.add("CTCHttpsPostServerCmdAndCookies");
        this.interfaceList.add("CTCHttpsPostServerCmd");
        this.interfaceList.add("CTCHttpsPostServerCookies");
        this.interfaceList.add("CTCMenu");
        this.interfaceList.add("CTCLoadLIbs");
        this.interfaceList.add("CTCGetSystemID");
        this.interfaceList.add("CTCSetVodShowPage");
        this.interfaceList.add("CTCUpnp");
        this.interfaceList.add("CTCSetLiveShowMAC");
        this.interfaceList.add("CTCGetLocalKey");
        this.interfaceList.add("CTCRunDebug");
        this.interfaceList.add("CTCIntervalExit");
        this.interfaceList.add("CTCSetToken");
        this.interfaceList.add("CTCSetLiveShowSpeed");
        this.interfaceList.add("CTCResizeVodImage");
    }

    public boolean isExistsInterface(String name) {
        for (int ii = 0; ii < this.interfaceList.size(); ii++) {
            if (((String) this.interfaceList.get(ii)).equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void loginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivityForResult(intent, 101);
    }

    private void updateActivity() {
        UpdateActivity.showUpdate(this, this.wHandler);
    }

    private void init() {
        LIVEplayer.downloadImage_thread();
    }

    private void networkActivity() {
        if (!this.is_showed_network_setting) {
            this.is_showed_network_setting = true;
            MyDialog.Builder builder = new MyDialog.Builder(this);
            builder.setMessage(getString(C0216R.string.validate_text7).toString());
            builder.setPositiveButton(getString(C0216R.string.ok).toString(), new C02298());
            builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new C02309());
            builder.create().show();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                stopService(new Intent(this, LocalService.class));
                finish();
                Process.killProcess(Process.myPid());
                System.exit(0);
                return true;
            case 82:
                MenuView.gridMenuInit(this);
                MenuView.showAlertDialog(this);
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (20 == resultCode && requestCode == 101) {
            LIVEplayer.urlClear();
            this.mWebView.loadUrl(data.getExtras().getString("url"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == 0) {
                    return;
                }
                return;
            default:
                return;
        }
    }
}
