package com.gemini.play;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.util.List;
import java.util.Map;

class MySimpleAdapterLiveGridView extends SimpleAdapter {
    private Context _this;
    private int current_index = 0;
    private float rate;
    private int selectedPosition = -1;
    private Typeface typeFace;

    public MySimpleAdapterLiveGridView(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this._this = context;
        init();
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public void setCurrentIndex(int index) {
        this.current_index = index;
    }

    private void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
        if (MGplayer.custom().equals("spain1") || MGplayer.custom().equals("spainglobal")) {
            this.rate = (float) (((double) this.rate) * 0.8d);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView vh = (TextView) v.findViewById(C0216R.id.ItemId);
        TextView vt = (TextView) v.findViewById(C0216R.id.ItemText);
        vh.setTextSize(this.rate * 7.0f);
        vh.setTypeface(this.typeFace);
        vt.setTextSize(this.rate * 8.0f);
        vt.setTypeface(this.typeFace);
        vh.setTextColor(Color.rgb(255, 255, 255));
        vt.setTextColor(Color.rgb(255, 255, 255));
        return v;
    }
}
