package com.gemini.play;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;

public class MyVodTopView extends LinearLayout {
    private Context _this = null;
    private ListViewInterface iface = null;
    private ListView listview = null;
    private float rate;
    private Typeface typeFace;

    /* renamed from: com.gemini.play.MyVodTopView$1 */
    class C05371 implements OnClickListener {
        C05371() {
        }

        public void onClick(View v) {
            MyVodTopView.this.iface.callback(0, null);
        }
    }

    /* renamed from: com.gemini.play.MyVodTopView$2 */
    class C05382 implements OnClickListener {
        C05382() {
        }

        public void onClick(View v) {
            MenuView.gridMenuInit(MyVodTopView.this._this);
            MenuView.showAlertDialog(MyVodTopView.this._this);
        }
    }

    public MyVodTopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtopview, this, true);
        init();
    }

    public MyVodTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtopview, this, true);
        init();
    }

    public MyVodTopView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtopview, this, true);
        init();
    }

    void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
        TextView vh = (TextView) findViewById(C0216R.id.TopTip);
        vh.setTextSize(this.rate * 7.0f);
        vh.setTypeface(this.typeFace);
        TextView vh1 = (TextView) findViewById(C0216R.id.TopCenterTip);
        vh1.setTextSize(this.rate * 6.0f);
        vh1.setTypeface(this.typeFace);
        TextView vh2 = (TextView) findViewById(C0216R.id.TopLeftTip);
        vh2.setTextSize(this.rate * 6.0f);
        vh2.setTypeface(this.typeFace);
        vh2.setOnClickListener(new C05371());
        setFlickerAnimation((ImageView) findViewById(C0216R.id.timage));
        ((ImageView) findViewById(C0216R.id.options)).setOnClickListener(new C05382());
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public void set_vod_top_text(String t) {
        ((TextView) findViewById(C0216R.id.TopTip)).setText(t);
    }

    public void set_vod_top_center_text(String t) {
        ((TextView) findViewById(C0216R.id.TopCenterTip)).setText(t);
    }

    public void set_vod_top_left_text(String t) {
        ((TextView) findViewById(C0216R.id.TopLeftTip)).setText(t);
    }

    private void setFlickerAnimation(ImageView iv_chat_head) {
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);
        animation.setRepeatMode(2);
        iv_chat_head.setAnimation(animation);
    }
}
