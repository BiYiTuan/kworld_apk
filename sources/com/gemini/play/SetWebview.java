package com.gemini.play;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.gemini.custom.quanxing;
import com.gemini.kvod2.C0216R;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class SetWebview extends Activity {
    private WebSettings mWebSettings = null;
    private WebView mWebView = null;

    /* renamed from: com.gemini.play.SetWebview$1 */
    class C05661 extends WebChromeClient {
        C05661() {
        }

        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            new Builder(SetWebview.this).setTitle(SetWebview.this.getString(C0216R.string.validate_text1)).setMessage(message).setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setCancelable(false).create().show();
            return true;
        }

        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new Builder(SetWebview.this).setTitle(SetWebview.this.getString(C0216R.string.validate_text1)).setMessage(message).setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setNegativeButton(17039360, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).create().show();
            return true;
        }

        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            new Builder(SetWebview.this).setTitle(SetWebview.this.getString(C0216R.string.validate_text1)).setMessage(message).setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    result.confirm();
                }
            }).setNegativeButton(17039360, new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    result.cancel();
                }
            }).setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            }).show();
            return true;
        }

        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress <= 99) {
                System.out.println("onProgressChanged:" + newProgress);
                super.onProgressChanged(view, newProgress);
            } else {
                System.out.println("onProgressChanged:" + newProgress);
                super.onProgressChanged(view, newProgress);
            }
        }
    }

    public class MyWebViewClient extends WebViewClient {
        public void onProgressChanged(WebView view, int newProgress) {
            System.out.println("onProgressChanged:" + newProgress);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("shouldOverrideUrlLoading:" + url);
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            System.out.println("onPageStarted:" + url);
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            Log.d("onPageFinished", "timeout...........");
            super.onPageFinished(view, url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            SetWebview.this.mWebView.loadUrl("file:///android_asset/set.html");
        }
    }

    public class PluginInterface {
        public void CTCSetConfig(String key, String value) {
            Editor editor = MGplayer.MyGetSharedPreferences(SetWebview.this, "data", 0).edit();
            editor.putString(key, value);
            editor.commit();
        }

        public String CTCGetConfig(String key) {
            String value = MGplayer.MyGetSharedPreferences(SetWebview.this, "data", 0).getString(key, "");
            System.out.println("Value: " + value);
            return value;
        }

        public String getMac() throws Exception {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                byte[] mac = ((NetworkInterface) e.nextElement()).getHardwareAddress();
                if (mac != null) {
                    System.out.println(displayMac(mac));
                    return displayMac(mac);
                }
            }
            return null;
        }

        public String getMac_file() {
            String str2 = "00:00:00:00:00:00";
            try {
                str2 = new BufferedReader(new FileReader("/etc/system.mkey"), 8192).readLine();
                if (str2 != null) {
                    return str2;
                }
            } catch (IOException e) {
            }
            return str2;
        }

        public String CTCGetMac() {
            return MGplayer.tv.GetMac();
        }

        private String displayMac(byte[] mac) {
            String macs = "";
            for (int i = 0; i < mac.length; i++) {
                int intValue;
                byte b = mac[i];
                if (b >= (byte) 0) {
                    intValue = b;
                } else {
                    intValue = b + 256;
                }
                System.out.print(Integer.toHexString(intValue));
                macs = macs + String.format("%02x", new Object[]{Integer.valueOf(intValue)});
                if (i != mac.length - 1) {
                    System.out.print(":");
                    macs = macs + ":";
                }
            }
            System.out.println();
            return macs;
        }

        public void CTCSetUpdate(String url) {
            Editor editor = SetWebview.this.getSharedPreferences("myconfig123", 0).edit();
            editor.putString("update", url);
            editor.commit();
        }

        public int CTCGetVersion() {
            return quanxing.version;
        }

        public String CTCGetWifiMac() {
            return "00:00:00:00:00:00";
        }

        public void CTCLoaded() {
            quanxing.load_timeout = 0;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(C0216R.layout.setwebview);
        this.mWebView = (WebView) findViewById(C0216R.id.webView);
        this.mWebSettings = this.mWebView.getSettings();
        this.mWebSettings.setJavaScriptEnabled(true);
        this.mWebView.setWebViewClient(new MyWebViewClient());
        this.mWebView.addJavascriptInterface(new PluginInterface(), "Authentication");
        this.mWebSettings.setCacheMode(2);
        this.mWebView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.mWebView.setWebChromeClient(new C05661());
        String url = MGplayer.MyGetSharedPreferences(this, "data", 0).getString("setwebview", null);
        if (url == null || url.length() <= 7) {
            this.mWebView.loadUrl("file:///android_asset/set.html");
            return;
        }
        this.mWebView.loadUrl(url);
        Toast.makeText(this, getString(C0216R.string.setwebview_text1).toString(), 0).show();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case 4:
                finish();
                break;
        }
        return super.dispatchKeyEvent(event);
    }
}
