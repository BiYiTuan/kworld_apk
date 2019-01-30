package com.gemini.play;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;

public class MyVodTop3View extends LinearLayout {
    private Context _this = null;
    private ListViewInterface iface = null;
    private ListView listview = null;
    private float rate;
    private Typeface typeFace;
    private TextView vh = null;
    private TextView vh1 = null;

    /* renamed from: com.gemini.play.MyVodTop3View$1 */
    class C05351 implements OnClickListener {
        C05351() {
        }

        public void onClick(View v) {
            MenuView.gridMenuInit(MyVodTop3View.this._this);
            MenuView.showAlertDialog(MyVodTop3View.this._this);
        }
    }

    /* renamed from: com.gemini.play.MyVodTop3View$2 */
    class C05362 implements OnClickListener {
        C05362() {
        }

        public void onClick(View v) {
            MyVodTop3View.this.iface.callback(0, null);
        }
    }

    public MyVodTop3View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtopview3, this, true);
        init();
    }

    public MyVodTop3View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtopview3, this, true);
        init();
    }

    public MyVodTop3View(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtopview3, this, true);
        init();
    }

    void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
        this.vh = (TextView) findViewById(C0216R.id.TopTip);
        this.vh.setTextSize(this.rate * 7.0f);
        this.vh.setTypeface(this.typeFace);
        this.vh1 = (TextView) findViewById(C0216R.id.TopCenterTip);
        this.vh1.setTextSize(this.rate * 8.0f);
        this.vh1.setTypeface(this.typeFace);
        ((ImageView) findViewById(C0216R.id.options)).setOnClickListener(new C05351());
        ((ImageView) findViewById(C0216R.id.back)).setOnClickListener(new C05362());
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public void set_vod_top_text(String t) {
        if (MGplayer.vod_showpage == 1) {
            this.vh.setText(t);
        } else {
            this.vh.setText("");
        }
    }

    public void set_vod_top_center_text(String t) {
        this.vh1.setText(t);
    }
}
