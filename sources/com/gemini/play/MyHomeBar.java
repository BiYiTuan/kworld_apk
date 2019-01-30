package com.gemini.play;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;

public class MyHomeBar extends LinearLayout {
    private Context _this;
    private TranslateAnimation animation;
    private int barindex;
    private LauncherInterface iface;
    private LinearLayout mlayout;
    private Handler rHandler;
    /* renamed from: t */
    private TextView[] f13t;

    /* renamed from: com.gemini.play.MyHomeBar$2 */
    class C03862 extends Handler {
        C03862() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyHomeBar.this.selectIndex(MyHomeBar.this.barindex, 0);
                    return;
                case 1:
                    MyHomeBar.this.selectIndex(MyHomeBar.this.barindex, 1);
                    return;
                default:
                    return;
            }
        }
    }

    public MyHomeBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.f13t = new TextView[]{null, null, null, null, null};
        this.barindex = 0;
        this.iface = null;
        this.rHandler = new C03862();
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.homebar, this, true);
        init();
    }

    public MyHomeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.f13t = new TextView[]{null, null, null, null, null};
        this.barindex = 0;
        this.iface = null;
        this.rHandler = new C03862();
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.homebar, this, true);
        init();
    }

    public MyHomeBar(Context context) {
        super(context);
        this.f13t = new TextView[]{null, null, null, null, null};
        this.barindex = 0;
        this.iface = null;
        this.rHandler = new C03862();
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.homebar, this, true);
        init();
    }

    private void init() {
        this.mlayout = (LinearLayout) findViewById(C0216R.id.layout);
        this.f13t[0] = (TextView) findViewById(C0216R.id.t1);
        this.f13t[1] = (TextView) findViewById(C0216R.id.t2);
        this.f13t[2] = (TextView) findViewById(C0216R.id.t3);
        this.f13t[3] = (TextView) findViewById(C0216R.id.t4);
        this.f13t[4] = (TextView) findViewById(C0216R.id.t5);
        Typeface typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        MGplayer.MyPrintln("dm.density " + MGplayer.screenHeight + "x" + MGplayer.screenWidth);
        int rate = (int) (10.0f * MGplayer.getFontsRate());
        for (int ii = 0; ii < this.f13t.length; ii++) {
            final int kk = ii;
            this.f13t[ii].setTextSize((float) rate);
            this.f13t[ii].setTypeface(typeFace);
            this.f13t[ii].setTextColor(Color.rgb(128, 128, 128));
            this.f13t[ii].setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (MyHomeBar.this.barindex - kk > 0) {
                        MyHomeBar.this.selectIndex(kk, 1, Math.abs(MyHomeBar.this.barindex - kk));
                    } else {
                        MyHomeBar.this.selectIndex(kk, 0, Math.abs(MyHomeBar.this.barindex - kk));
                    }
                    MyHomeBar.this.iface.callback(kk);
                }
            });
        }
    }

    public void selectafter() {
        selectafter(false);
    }

    public void selectafter(boolean delay) {
        this.barindex++;
        if (this.barindex > 4) {
            this.barindex = 4;
            return;
        }
        selectTextView(this.barindex);
        Message msg = new Message();
        msg.setData(new Bundle());
        msg.what = 0;
        this.rHandler.removeMessages(0);
        if (delay) {
            this.rHandler.sendMessageDelayed(msg, 100);
        } else {
            this.rHandler.sendMessage(msg);
        }
    }

    public void selectpre() {
        selectpre(false);
    }

    public void selectpre(boolean delay) {
        this.barindex--;
        if (this.barindex < 0) {
            this.barindex = 0;
            return;
        }
        selectTextView(this.barindex);
        Message msg = new Message();
        msg.setData(new Bundle());
        msg.what = 1;
        this.rHandler.removeMessages(1);
        if (delay) {
            this.rHandler.sendMessageDelayed(msg, 100);
        } else {
            this.rHandler.sendMessage(msg);
        }
    }

    public void selectIndex(int index, int direct) {
        selectIndex(index, direct, 1);
    }

    public void selectIndex(int index, int direct, int step) {
        MGplayer.MyPrintln("selectIndex =" + index);
        final int len = MGplayer.screenWidth / 5;
        this.barindex = index;
        final int index2 = index - 2;
        if (direct == 1) {
            this.animation = new TranslateAnimation(0.0f, (float) (step * len), 0.0f, 0.0f);
        } else if (direct == 0) {
            this.animation = new TranslateAnimation(0.0f, (float) (0 - (step * len)), 0.0f, 0.0f);
        }
        if (direct == 1 || direct == 0) {
            this.animation.setDuration(100);
            this.animation.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    int left = (0 - index2) * len;
                    int top = MyHomeBar.this.mlayout.getTop();
                    int width = MyHomeBar.this.mlayout.getWidth();
                    int height = MyHomeBar.this.mlayout.getHeight();
                    MyHomeBar.this.mlayout.clearAnimation();
                    MyHomeBar.this.mlayout.layout(left, top, left + width, top + height);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            this.mlayout.setFocusable(true);
            this.mlayout.startAnimation(this.animation);
        } else if (direct == -1) {
            int left = index2 * len;
            int top = this.mlayout.getTop();
            int width = this.mlayout.getWidth();
            int height = this.mlayout.getHeight();
            this.mlayout.clearAnimation();
            this.mlayout.layout(left, top, left + width, top + height);
        }
        selectTextView(index);
    }

    private void selectTextView(int index) {
        for (TextView textColor : this.f13t) {
            textColor.setTextColor(Color.rgb(128, 128, 128));
        }
        this.f13t[index].setTextColor(Color.rgb(0, 128, 255));
    }

    public void flash() {
        int left = (0 - (this.barindex - 2)) * (MGplayer.screenWidth / 5);
        int top = this.mlayout.getTop();
        int width = this.mlayout.getWidth();
        int height = this.mlayout.getHeight();
        this.mlayout.clearAnimation();
        this.mlayout.layout(left, top, left + width, top + height);
    }

    public int getBarindex() {
        return this.barindex;
    }

    public void setInterface(LauncherInterface l) {
        this.iface = l;
    }
}
