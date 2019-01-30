package com.gemini.play;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.content.FileProvider;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.gemini.custom.cxiptv;
import com.gemini.kvod2.C0216R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;

public class MyHomeBar2 extends LinearLayout {
    private Context _this;
    private TranslateAnimation animation;
    private int barindex;
    private Drawable focus_drawable;
    private LauncherInterface iface;
    private LinearLayout mlayout;
    private ProgressDialog pDialog;
    private String[] styles;
    /* renamed from: t */
    private TextView[] f12t;
    private TextView textfocuse;

    /* renamed from: com.gemini.play.MyHomeBar2$5 */
    class C03925 implements OnClickListener {
        C03925() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
            MyHomeBar2.this.pDialog.cancel();
        }
    }

    public MyHomeBar2(Context context) {
        super(context);
        this.f12t = new TextView[]{null, null, null, null, null};
        this.barindex = 0;
        this.iface = null;
        this.focus_drawable = null;
        this.textfocuse = null;
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.homebar2, this, true);
        init();
    }

    public MyHomeBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.f12t = new TextView[]{null, null, null, null, null};
        this.barindex = 0;
        this.iface = null;
        this.focus_drawable = null;
        this.textfocuse = null;
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.homebar2, this, true);
        init();
    }

    public MyHomeBar2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.f12t = new TextView[]{null, null, null, null, null};
        this.barindex = 0;
        this.iface = null;
        this.focus_drawable = null;
        this.textfocuse = null;
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.homebar2, this, true);
        init();
    }

    private void init() {
        this.mlayout = (LinearLayout) findViewById(C0216R.id.layout);
        this.f12t[0] = (TextView) findViewById(C0216R.id.t1);
        this.f12t[1] = (TextView) findViewById(C0216R.id.t2);
        this.f12t[2] = (TextView) findViewById(C0216R.id.t3);
        this.f12t[3] = (TextView) findViewById(C0216R.id.t4);
        this.f12t[4] = (TextView) findViewById(C0216R.id.t5);
        if (MGplayer.custom().equals("jufeng")) {
            this.f12t[2].setText(this._this.getString(C0216R.string.myhomebar_text10));
        }
        if (MGplayer.style.length() < 8) {
            MGplayer.style = "live|vod|back|setting|exit";
        }
        this.styles = MGplayer.style.split("\\|");
        if (MGplayer.custom().equals("cxiptv") && !cxiptv.need_moatv) {
            this.styles[1] = "app";
        }
        if (this.styles.length >= this.f12t.length) {
            setTextView(this.f12t[0], this.styles[0]);
            setTextView(this.f12t[1], this.styles[1]);
            setTextView(this.f12t[2], this.styles[2]);
            setTextView(this.f12t[3], this.styles[3]);
            setTextView(this.f12t[4], this.styles[4]);
        }
        Typeface typeFace = MGplayer.getFontsType(this._this);
        float rate = MGplayer.getFontsRate();
        for (int ii = 0; ii < this.f12t.length; ii++) {
            final int kk = ii;
            this.f12t[ii].setTextSize(10.0f * rate);
            this.f12t[ii].setTypeface(typeFace);
            this.f12t[ii].setTextColor(Color.rgb(128, 128, 128));
            LayoutParams layoutParams = (LayoutParams) this.f12t[ii].getLayoutParams();
            layoutParams.width = MGplayer.screenWidth / 5;
            layoutParams.height = MGplayer.screenHeight / 8;
            this.f12t[ii].setLayoutParams(layoutParams);
            this.f12t[ii].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (MyHomeBar2.this.barindex - kk > 0) {
                        MyHomeBar2.this.selectIndex(kk);
                    } else {
                        MyHomeBar2.this.selectIndex(kk);
                    }
                    int index = MyHomeBar2.this.getOpenIndex(kk);
                    MGplayer.MyPrintln("index = " + index + "styles[" + kk + "]:" + MyHomeBar2.this.styles[kk]);
                    if (index == 6) {
                        String[] defineds = MyHomeBar2.this.styles[kk].split("\\*");
                        if (defineds.length >= 4) {
                            MyHomeBar2.this.download_defined(defineds[3], defineds[2]);
                            return;
                        }
                        return;
                    }
                    MyHomeBar2.this.iface.callback(index);
                }
            });
        }
        this.focus_drawable = getResources().getDrawable(C0216R.mipmap.se);
        this.textfocuse = (TextView) findViewById(C0216R.id.force);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.textfocuse.getLayoutParams();
        layoutParams2.width = MGplayer.screenWidth / 5;
        layoutParams2.height = MGplayer.screenHeight / 8;
        this.textfocuse.setVisibility(8);
        this.f12t[0].setBackgroundResource(C0216R.mipmap.fo);
        this.f12t[0].setTextColor(Color.rgb(255, 255, 255));
        if (MGplayer.custom().equals("spain1")) {
            this.f12t[0].setText("全球直播");
        }
    }

    public void selectIndex(final int index) {
        int step = index - this.barindex;
        if (step != 0) {
            TranslateAnimation animation;
            int len = MGplayer.screenWidth / 5;
            if (step > 0) {
                animation = new TranslateAnimation((float) (this.barindex * len), (float) (index * len), 0.0f, 0.0f);
            } else {
                animation = new TranslateAnimation((float) (this.barindex * len), (float) (index * len), 0.0f, 0.0f);
            }
            for (int ii = 0; ii < this.f12t.length; ii++) {
                this.f12t[ii].setBackgroundColor(0);
                this.f12t[ii].setTextColor(Color.rgb(128, 128, 128));
            }
            this.textfocuse.setVisibility(0);
            animation.setDuration(500);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    MyHomeBar2.this.textfocuse.setVisibility(8);
                    MyHomeBar2.this.f12t[index].setBackgroundResource(C0216R.mipmap.fo);
                    MyHomeBar2.this.f12t[index].setTextColor(Color.rgb(255, 255, 255));
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            this.textfocuse.setFocusable(true);
            this.textfocuse.startAnimation(animation);
            this.barindex = index;
        }
    }

    public void setInterface(LauncherInterface l) {
        this.iface = l;
    }

    public void selectafter() {
        int index = this.barindex - 1;
        if (index < 0) {
            index = 0;
        }
        selectIndex(index);
    }

    public void selectpre() {
        int index = this.barindex + 1;
        if (index > this.f12t.length - 1) {
            index = this.f12t.length - 1;
        }
        selectIndex(index);
    }

    public void selectenter() {
        int index = getOpenIndex(this.barindex);
        MGplayer.MyPrintln("index = " + index + "styles[" + this.barindex + "]:" + this.styles[this.barindex]);
        if (index == 6) {
            String[] defineds = this.styles[this.barindex].split("\\*");
            if (defineds.length >= 4) {
                download_defined(defineds[3], defineds[2]);
                return;
            }
            return;
        }
        this.iface.callback(index);
    }

    private void setTextView(TextView t, String style) {
        if (style.equals("live")) {
            t.setText(this._this.getString(C0216R.string.myhomebar_text1).toString());
        } else if (style.equals("vod")) {
            t.setText(this._this.getString(C0216R.string.myhomebar_text2).toString());
        } else if (style.equals("back")) {
            t.setText(this._this.getString(C0216R.string.myhomebar_text3).toString());
        } else if (style.equals("app")) {
            if (MGplayer.custom().equals("jhome")) {
                t.setText("응용");
            } else {
                t.setText(this._this.getString(C0216R.string.myhomebar_text10).toString());
            }
        } else if (style.equals("setting")) {
            t.setText(this._this.getString(C0216R.string.myhomebar_text4).toString());
        } else if (style.equals("exit")) {
            t.setText(this._this.getString(C0216R.string.myhomebar_text5).toString());
        } else if (style.startsWith("defined")) {
            String[] defineds = style.split("\\*");
            if (defineds.length >= 4) {
                t.setText(defineds[1]);
            }
        }
    }

    private int getOpenIndex(int barindex) {
        if (this.styles[barindex].equals("live")) {
            return 0;
        }
        if (this.styles[barindex].equals("vod")) {
            return 1;
        }
        if (this.styles[barindex].equals("back")) {
            return 2;
        }
        if (this.styles[barindex].equals("app")) {
            return 5;
        }
        if (this.styles[barindex].equals("setting")) {
            return 3;
        }
        if (this.styles[barindex].equals("exit")) {
            return 4;
        }
        if (this.styles[barindex].startsWith("defined")) {
            return 6;
        }
        return -1;
    }

    private void download_defined(String id, final String url) {
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> mApps = this._this.getPackageManager().queryIntentActivities(intent, 0);
        for (int i = 0; i < mApps.size(); i++) {
            if (((ResolveInfo) mApps.get(i)).activityInfo.packageName.equals(id)) {
                intent = this._this.getPackageManager().getLaunchIntentForPackage(id);
                if (intent != null) {
                    this._this.startActivity(intent);
                    return;
                }
                return;
            }
        }
        final Builder builder = new Builder(this._this);
        builder.setTitle(this._this.getString(C0216R.string.myhomebar_text11));
        builder.setMessage(this._this.getString(C0216R.string.myhomebar_text12));
        builder.setPositiveButton(this._this.getString(C0216R.string.ok), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
                MyHomeBar2.this.showUpdate(URLDecoder.decode(url));
            }
        });
        builder.setNegativeButton(this._this.getString(C0216R.string.cancel).toString(), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().cancel();
            }
        });
        builder.create().show();
    }

    public void showUpdate(final String url) {
        this.pDialog = new ProgressDialog(this._this);
        this.pDialog.setProgressStyle(1);
        this.pDialog.setTitle(this._this.getString(C0216R.string.myhomebar_text13));
        this.pDialog.setProgress(100);
        this.pDialog.setIndeterminate(false);
        this.pDialog.setButton(this._this.getString(C0216R.string.cancel).toString(), new C03925());
        this.pDialog.show();
        MGplayer.MyPrintln("UpdateActivity strPath:" + url);
        new Thread() {
            public void run() {
                try {
                    MyHomeBar2.this.getDataSource(url, "a");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getDataSource(String strPath, String Updatename) throws Exception {
        if (URLUtil.isNetworkUrl(strPath)) {
            URLConnection conn = new URL(strPath).openConnection();
            conn.connect();
            int fileSize = conn.getContentLength();
            if (fileSize <= 0) {
                Log.d("Tag", "error");
                return;
            }
            FileOutputStream fos;
            InputStream is = conn.getInputStream();
            MGplayer.MyPrintln("UpdateActivity strPath:" + strPath);
            File myTempFile = new File(this._this.getFilesDir() + "/a.apk");
            MGplayer.MyPrintln("UpdateActivity APK:" + myTempFile.getAbsolutePath());
            String FileStream = "a.apk";
            Context context;
            Context context2;
            if (VERSION.SDK_INT >= 24) {
                context = this._this;
                context2 = this._this;
                fos = context.openFileOutput(FileStream, 0);
            } else {
                context = this._this;
                context2 = this._this;
                fos = context.openFileOutput(FileStream, 1);
            }
            byte[] buf = new byte[1024];
            int numreads = 0;
            while (true) {
                int numread = is.read(buf);
                if (numread <= 0) {
                    break;
                }
                fos.write(buf, 0, numread);
                numreads += numread;
                this.pDialog.setProgress((int) (100.0d * (((double) numreads) / ((double) fileSize))));
                this.pDialog.show();
            }
            this.pDialog.cancel();
            if (VERSION.SDK_INT >= 24) {
                installN(this._this, FileStream);
            } else {
                installFile(myTempFile, "a");
            }
            try {
                is.close();
            } catch (Exception e) {
                Log.d("Tag", "error");
            }
            MGplayer.MyPrintln("UpdateActivity Download Finish");
            return;
        }
        Log.d("Tag", "error");
    }

    private void installFile(File f, String Updatename) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.parse("file://" + this._this.getFilesDir() + InternalZipConstants.ZIP_FILE_SEPARATOR + Updatename + ".apk"), "application/vnd.android.package-archive");
        this._this.startActivity(intent);
    }

    public static void installN(Context context, String apkfile) {
        Uri apkUri = FileProvider.getUriForFile(context, "comgeminiiptvfileprovider", new File(context.getFilesDir(), apkfile));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.addFlags(1);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
