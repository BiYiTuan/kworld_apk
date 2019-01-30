package com.gemini.play;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.gemini.kvod2.C0216R;

public class MyVodImageView extends LinearLayout {
    private Context _this = null;
    private ListViewInterface iface = null;
    private ImageView image = null;

    /* renamed from: com.gemini.play.MyVodImageView$1 */
    class C05191 implements Runnable {
        C05191() {
        }

        public void run() {
            MyVodImageView.this.hideView();
        }
    }

    public MyVodImageView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodimageview, this, true);
        init();
    }

    public MyVodImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodimageview, this, true);
        init();
    }

    public MyVodImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodimageview, this, true);
        init();
    }

    public void init() {
        this.image = (ImageView) findViewById(C0216R.id.image);
    }

    public void playImage() {
        this.image.setImageResource(C0216R.mipmap.playf2);
        showView();
        new Handler().postDelayed(new C05191(), 1000);
    }

    public void pauseImage() {
        this.image.setImageResource(C0216R.mipmap.pausef2);
        showView();
    }

    public void showView() {
        setVisibility(0);
    }

    public void hideView() {
        setVisibility(8);
    }
}
