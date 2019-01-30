package com.gemini.play;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;

public class LauncherActivity extends Activity implements OnGestureListener {
    private int barindex = 0;
    private GestureDetector detector = null;
    private MyHomeBar homeblade = null;
    private RelativeLayout mLayout = null;
    public LauncherInterface onPressed = new C09083();
    private Handler rHandler = new C02972();

    /* renamed from: com.gemini.play.LauncherActivity$1 */
    class C02961 implements Runnable {
        C02961() {
        }

        public void run() {
            MGplayer.MyPrintln("time now = " + MGplayer.time_now);
            TextView timetext = (TextView) LauncherActivity.this.findViewById(C0216R.id.timetext);
            timetext.setTextSize((float) ((int) (7.0f * MGplayer.getFontsRate())));
            timetext.setText(MGplayer.time_now);
            LauncherActivity.this.homeblade.flash();
        }
    }

    /* renamed from: com.gemini.play.LauncherActivity$2 */
    class C02972 extends Handler {
        C02972() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
            }
        }
    }

    /* renamed from: com.gemini.play.LauncherActivity$3 */
    class C09083 implements LauncherInterface {
        C09083() {
        }

        public void callback(int data) {
            MGplayer.MyPrintln("data = " + data);
            switch (data) {
                case 0:
                    MGplayer.openActivity(LauncherActivity.this, LivePlayerActivity.class);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.launcher);
        getWindow().setFlags(1024, 1024);
        this.detector = new GestureDetector(this, this);
        this.mLayout = (RelativeLayout) findViewById(C0216R.id.panal);
        this.mLayout.setBackgroundDrawable(getResources().getDrawable(C0216R.mipmap.bg));
        init();
    }

    private void init() {
        int bottom = MGplayer.screenHeight / 5;
        int height = MGplayer.screenHeight / 10;
        this.homeblade = (MyHomeBar) findViewById(C0216R.id.homeblade);
        LayoutParams layoutParams = (LayoutParams) this.homeblade.getLayoutParams();
        layoutParams.bottomMargin = bottom;
        layoutParams.height = height;
        this.homeblade.setLayoutParams(layoutParams);
        this.homeblade.setInterface(this.onPressed);
        this.homeblade.selectIndex(2, -1);
        ScrollTextView scroller = (ScrollTextView) findViewById(C0216R.id.scrolltext);
        scroller.init(getWindowManager());
        if (MGplayer.scrolltext != null) {
            MGplayer.MyPrintln("start scrolltext");
            scroller.setText("");
            scroller.start(this, MGplayer.scrolltext, 0, 0, 0, 0, 2.5f, 25, "FFFFFF");
        }
        timeView();
        if (MGplayer.updatetip_show != 1) {
        }
    }

    public void timeView() {
        new Handler().postDelayed(new C02961(), 1000);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 4:
                    this.homeblade.selectafter(true);
                    return super.dispatchKeyEvent(event);
                case 66:
                    switch (this.homeblade.getBarindex()) {
                        case 0:
                            MGplayer.openActivity(this, LivePlayerActivity.class);
                            break;
                        case 1:
                            break;
                        default:
                            break;
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {
        return false;
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
