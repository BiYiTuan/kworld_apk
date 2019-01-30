package com.gemini.play;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class MyVScrollText extends TextView {
    private Context _this;
    private ListViewInterface iface = null;
    private String[] texts;

    /* renamed from: com.gemini.play.MyVScrollText$1 */
    class C04661 implements AnimationListener {
        C04661() {
        }

        public void onAnimationEnd(Animation animation) {
            MyVScrollText.this.clearAnimation();
            MyVScrollText.this.listFocus();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    public MyVScrollText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        init();
    }

    public MyVScrollText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        init();
    }

    public MyVScrollText(Context context) {
        super(context);
        this._this = context;
        init();
    }

    private void init() {
    }

    public void setTexts(String[] t) {
        this.texts = t;
    }

    public void scrollRun() {
        setVisibility(0);
        TranslateAnimation animation = new TranslateAnimation((float) ((-MGplayer.screenWidth) / 3), 0.0f, 0.0f, 0.0f);
        animation.setDuration(300);
        animation.setAnimationListener(new C04661());
        setFocusable(true);
        startAnimation(animation);
    }

    public void listFocus() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        requestFocusFromTouch();
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }
}
