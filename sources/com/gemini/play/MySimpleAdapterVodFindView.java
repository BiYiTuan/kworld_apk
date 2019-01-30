package com.gemini.play;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.util.List;
import java.util.Map;

public class MySimpleAdapterVodFindView extends SimpleAdapter {
    private Context _this;
    private int clickTemp = -1;
    private int current_index = 0;
    private float rate;
    private int selectedPosition = -1;
    private Typeface typeFace;
    private int vtheight = 0;

    public MySimpleAdapterVodFindView(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this._this = context;
        init();
    }

    private void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
    }

    public void setSeclection(int position) {
        this.clickTemp = position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView vt = (TextView) v.findViewById(C0216R.id.ItemChar);
        if (vt.getHeight() > 0) {
            this.vtheight = vt.getHeight();
        }
        LayoutParams layoutParams;
        if (position >= 37) {
            layoutParams = (LayoutParams) vt.getLayoutParams();
            layoutParams.height = (vt.getWidth() / 5) * 4;
            vt.setLayoutParams(layoutParams);
            vt.setTextSize(this.rate * 7.0f);
        } else {
            vt.setTextSize(this.rate * 9.0f);
            layoutParams = (LayoutParams) vt.getLayoutParams();
            layoutParams.height = (vt.getWidth() / 10) * 9;
            vt.setLayoutParams(layoutParams);
        }
        vt.setTypeface(this.typeFace);
        vt.setTextColor(Color.rgb(255, 255, 255));
        if (this.clickTemp == position) {
            v.setBackgroundResource(C0216R.drawable.gradient8);
        } else {
            v.setBackgroundResource(C0216R.drawable.gradient3);
        }
        return v;
    }
}
