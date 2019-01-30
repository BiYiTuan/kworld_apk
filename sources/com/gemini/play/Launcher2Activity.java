package com.gemini.play;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.io.IOException;

public class Launcher2Activity extends Activity implements OnGestureListener {
    static Activity _this;
    static TextView timeouttext = null;
    private GestureDetector detector = null;
    private int[] hb = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    private MyHomeBar2 homeblade = null;
    private boolean isexit = false;
    private FrameLayout mLayout = null;
    public ControlVideoInterface onControlVideo = new C09076();
    public LauncherInterface onPressed = new C09054();
    public ScrollViewInterface onScrollView = new C09065();
    private ScrollTextView onescroller = null;
    public Handler rHandler = new C02912();

    /* renamed from: com.gemini.play.Launcher2Activity$2 */
    class C02912 extends Handler {
        C02912() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String imagePath = msg.getData().getString("imagePath");
                    new Options().inSampleSize = 4;
                    Launcher2Activity.this.mLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(imagePath)));
                    return;
                case 1:
                    String content = msg.getData().getString("content");
                    int times = msg.getData().getInt("times");
                    Launcher2Activity.this.onescroller.setText("");
                    Launcher2Activity.this.onescroller.setVisibility(0);
                    Launcher2Activity.this.onescroller.start_every(Launcher2Activity.this, content, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", times);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.Launcher2Activity$7 */
    class C02937 implements Runnable {
        C02937() {
        }

        public void run() {
            Launcher2Activity.this.isexit = false;
        }
    }

    /* renamed from: com.gemini.play.Launcher2Activity$8 */
    class C02948 implements OnClickListener {
        C02948() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Launcher2Activity.this.stopService(new Intent(Launcher2Activity.this, LocalService.class));
            Launcher2Activity.this.finish();
            Process.killProcess(Process.myPid());
            MGplayer.exit_app();
        }
    }

    /* renamed from: com.gemini.play.Launcher2Activity$9 */
    class C02959 implements OnClickListener {
        C02959() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    /* renamed from: com.gemini.play.Launcher2Activity$4 */
    class C09054 implements LauncherInterface {
        C09054() {
        }

        public void callback(int data) {
            MGplayer.MyPrintln("data = " + data);
            switch (data) {
                case 0:
                    Intent intent = new Intent(Launcher2Activity.this, LivePlayerActivity.class);
                    intent.putExtra("ui_type", MGplayer.live_ui_type);
                    Launcher2Activity.this.startActivity(intent);
                    return;
                case 1:
                    MGplayer.openActivity(Launcher2Activity.this, VodPlayerMainActivity.class);
                    return;
                case 2:
                    MGplayer.openActivity(Launcher2Activity.this, BackPlayer2Activity.class);
                    return;
                case 3:
                    MenuView.gridMenuInit(Launcher2Activity.this);
                    MenuView.showAlertDialog(Launcher2Activity.this);
                    return;
                case 4:
                    Launcher2Activity.this.exitActivity();
                    return;
                case 5:
                    MGplayer.openActivity(Launcher2Activity.this, AppsActivity.class);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.Launcher2Activity$5 */
    class C09065 implements ScrollViewInterface {
        C09065() {
        }

        public void callback(int cmd, String content, int times) {
            Message msg;
            switch (cmd) {
                case 0:
                    msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("content", content);
                    data.putInt("times", times);
                    msg.setData(data);
                    msg.what = 1;
                    Launcher2Activity.this.rHandler.sendMessage(msg);
                    return;
                case 1:
                    if (MGplayer.onescroll_txt != null && MGplayer.onescroll_txt.length() > 0) {
                        msg = new Message();
                        msg.setData(new Bundle());
                        msg.what = 2;
                        Launcher2Activity.this.rHandler.sendMessage(msg);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.Launcher2Activity$6 */
    class C09076 implements ControlVideoInterface {
        C09076() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(Launcher2Activity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    Launcher2Activity.this.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.launcher2);
        getWindow().setFlags(1024, 1024);
        this.detector = new GestureDetector(this, this);
        this.onescroller = (ScrollTextView) findViewById(C0216R.id.onescrolltext);
        this.mLayout = (FrameLayout) findViewById(C0216R.id.panal);
        Resources resources = getResources();
        Drawable drawable = null;
        MGplayer.video_every_interface(this.onControlVideo);
        _this = this;
        if (MGplayer.Background != null) {
            final String imagePath = getFilesDir() + "/background/" + MGplayer.Background;
            String file_md5 = MGplayer.file_md5(imagePath);
            if (MGplayer.fileIsExists(imagePath) && file_md5 != null && file_md5.equals(MGplayer.BackgroundMd5)) {
                new Options().inSampleSize = 4;
                this.mLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(imagePath)));
            } else {
                this.hb[0] = C0216R.mipmap.hb0;
                this.hb[1] = C0216R.mipmap.hb1;
                this.hb[2] = C0216R.mipmap.hb2;
                this.hb[3] = C0216R.mipmap.hb3;
                this.hb[4] = C0216R.mipmap.hb4;
                this.hb[5] = C0216R.mipmap.hb5;
                this.hb[6] = C0216R.mipmap.hb6;
                this.hb[7] = C0216R.mipmap.hb7;
                this.hb[8] = C0216R.mipmap.hb8;
                double id = Math.random();
                if (8.0d * id <= 8.0d) {
                    drawable = resources.getDrawable(this.hb[(int) (8.0d * id)]);
                }
                this.mLayout.setBackgroundDrawable(drawable);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            MGplayer.donwFile(MGplayer.tv.gete() + "/images/background/" + MGplayer.Background, imagePath);
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putString("imagePath", imagePath);
                            msg.setData(data);
                            msg.what = 0;
                            Launcher2Activity.this.rHandler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } else {
            this.hb[0] = C0216R.mipmap.hb0;
            this.hb[1] = C0216R.mipmap.hb1;
            this.hb[2] = C0216R.mipmap.hb2;
            this.hb[3] = C0216R.mipmap.hb3;
            this.hb[4] = C0216R.mipmap.hb4;
            this.hb[5] = C0216R.mipmap.hb5;
            this.hb[6] = C0216R.mipmap.hb6;
            this.hb[7] = C0216R.mipmap.hb7;
            this.hb[8] = C0216R.mipmap.hb8;
            this.mLayout.setBackgroundDrawable(resources.getDrawable(this.hb[(int) (8.0d * Math.random())]));
        }
        init();
    }

    private void init() {
        int bottom;
        if (MGplayer.custom().equals("cxiptv")) {
            bottom = MGplayer.screenHeight / 6;
        } else {
            bottom = MGplayer.screenHeight / 5;
        }
        int height = MGplayer.screenHeight / 8;
        this.homeblade = (MyHomeBar2) findViewById(C0216R.id.homeblade);
        LayoutParams layoutParams = (LayoutParams) this.homeblade.getLayoutParams();
        layoutParams.bottomMargin = bottom;
        layoutParams.height = height;
        this.homeblade.setInterface(this.onPressed);
        this.homeblade.setLayoutParams(layoutParams);
        ScrollTextView scroller = (ScrollTextView) findViewById(C0216R.id.scrolltext);
        scroller.init(getWindowManager());
        MGplayer.scroll_every_interface(this.onScrollView);
        if (MGplayer.scrolltext != null) {
            MGplayer.MyPrintln("start scrolltext");
            scroller.setText("");
            scroller.start(this, MGplayer.scrolltext, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF");
        }
        if (MGplayer.onescroll_txt != null && MGplayer.onescroll_txt.length() > 0) {
            this.onescroller.init(getWindowManager());
            MGplayer.MyPrintln("start onescroll_txt");
            this.onescroller.setText("");
            this.onescroller.setVisibility(0);
            this.onescroller.start(this, MGplayer.onescroll_txt, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", MGplayer.onescroll_times);
        }
        timeView();
        leftView();
        if (MGplayer.updatetip_show == 1) {
            MGplayer.showUpdateActivity(this);
        }
    }

    public void leftView() {
        if (MGplayer.custom().equals("quanxing")) {
            MGplayer.isShowLefttime = MGplayer.leftdaysshow;
        }
        if (MGplayer.isShowLefttime == 1) {
            timeouttext = (TextView) findViewById(C0216R.id.lefttext);
            timeouttext.setTextSize((float) ((int) (7.0f * MGplayer.getFontsRate())));
            timeouttext.setTypeface(MGplayer.getFontsType(this));
            timeouttext.setText("");
            String timeout = "";
            if (Integer.parseInt(MGplayer.leftdays) == -1) {
                timeout = timeout + "    " + getString(C0216R.string.myhomebar_text7).toString() + ":" + getString(C0216R.string.myhomebar_text9).toString();
            } else {
                timeout = timeout + "    " + getString(C0216R.string.myhomebar_text7).toString() + ":" + MGplayer.leftdays + getString(C0216R.string.myhomebar_text8).toString();
            }
            timeouttext.setText(timeout);
        }
    }

    public static void setLeftView() {
        if (MGplayer.isShowLefttime == 1) {
            TextView timeouttext = (TextView) _this.findViewById(C0216R.id.lefttext);
            timeouttext.setText("");
            String timeout = "";
            if (Integer.parseInt(MGplayer.leftdays) == -1) {
                timeout = timeout + "    " + _this.getString(C0216R.string.myhomebar_text7).toString() + ":" + _this.getString(C0216R.string.myhomebar_text9).toString();
            } else {
                timeout = timeout + "    " + _this.getString(C0216R.string.myhomebar_text7).toString() + ":" + MGplayer.leftdays + _this.getString(C0216R.string.myhomebar_text8).toString();
            }
            timeouttext.setText(timeout);
        }
    }

    public void timeView() {
        final Handler timeHander = new Handler();
        timeHander.postDelayed(new Runnable() {
            public void run() {
                MGplayer.MyPrintln("time now = " + MGplayer.time_now);
                TextView timetext = (TextView) Launcher2Activity.this.findViewById(C0216R.id.timetext);
                timetext.setTextSize((float) ((int) (7.0f * MGplayer.getFontsRate())));
                timetext.setTypeface(MGplayer.getFontsType(Launcher2Activity.this));
                timetext.setText(MGplayer.time_now);
                timeHander.postDelayed(this, HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS);
            }
        }, 1000);
    }

    protected void onResume() {
        MGplayer.video_every_interface(this.onControlVideo);
        super.onResume();
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MGplayer.MyPrintln("onKeyUp:" + keyCode);
        switch (keyCode) {
            case 4:
                if (this.isexit) {
                    stopService(new Intent(this, LocalService.class));
                    finish();
                    Process.killProcess(Process.myPid());
                    MGplayer.exit_app();
                    break;
                }
                this.isexit = true;
                MyToast.makeText(this, getString(C0216R.string.isexit).toString(), 0);
                new Handler().postDelayed(new C02937(), HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                return true;
            case 21:
                this.homeblade.selectafter();
                break;
            case 22:
                this.homeblade.selectpre();
                break;
            case 23:
            case 66:
                this.homeblade.selectenter();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exitActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.myhomebar_text6).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new C02948());
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new C02959());
        builder.create().show();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 82:
                MenuView.gridMenuInit(this);
                MenuView.showAlertDialog(this);
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        MGplayer.MyPrintln("onFling");
        if (e1.getX() - e2.getX() > 20.0f) {
            MGplayer.MyPrintln("onFling 1");
            this.homeblade.selectafter();
        } else if (e2.getX() - e1.getX() > 20.0f) {
            MGplayer.MyPrintln("onFling 2");
            this.homeblade.selectpre();
        }
        return true;
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
