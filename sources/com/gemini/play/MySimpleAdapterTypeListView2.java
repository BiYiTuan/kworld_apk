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

class MySimpleAdapterTypeListView2 extends SimpleAdapter {
    private Context _this;
    private int current_index = 0;
    private float rate;
    private int selectedPosition = -1;
    private Typeface typeFace;

    public MySimpleAdapterTypeListView2(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
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
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView vh = (TextView) v.findViewById(C0216R.id.ItemName);
        vh.setTextSize(this.rate * 5.0f);
        vh.setTypeface(this.typeFace);
        if (position == this.current_index) {
            vh.setBackgroundResource(C0216R.mipmap.se4);
        } else {
            vh.setBackgroundColor(0);
        }
        vh.setTextColor(Color.rgb(255, 255, 255));
        return v;
    }
}
