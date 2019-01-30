package com.gemini.play;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.util.List;
import java.util.Map;

class MySimpleAdapterBackListView extends SimpleAdapter {
    private Context _this;
    private int current_index = 0;
    private float rate;
    private int selectedPosition = -1;
    private Typeface typeFace;

    public MySimpleAdapterBackListView(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
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
        ImageView vi = (ImageView) v.findViewById(C0216R.id.ItemView);
        ImageView vi2 = (ImageView) v.findViewById(C0216R.id.ItemView2);
        TextView vh = (TextView) v.findViewById(C0216R.id.ItemId);
        TextView vt = (TextView) v.findViewById(C0216R.id.ItemTitle);
        vh.setTextSize(this.rate * 7.0f);
        vh.setTypeface(this.typeFace);
        vt.setTextSize(this.rate * 8.0f);
        vt.setTypeface(this.typeFace);
        int height = v.getMeasuredHeight();
        LayoutParams params = v.getLayoutParams();
        params.height = MGplayer.screenHeight / 12;
        v.setLayoutParams(params);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vt.getLayoutParams();
        lp.height = params.height - 20;
        lp.width = params.height - 10;
        vt.setLayoutParams(lp);
        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) vh.getLayoutParams();
        lp2.height = params.height - 20;
        vh.setLayoutParams(lp2);
        vh.setTextColor(Color.rgb(255, 255, 255));
        vt.setTextColor(Color.rgb(255, 255, 255));
        return v;
    }
}
