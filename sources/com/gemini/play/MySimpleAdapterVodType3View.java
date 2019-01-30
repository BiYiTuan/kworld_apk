package com.gemini.play;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.util.List;
import java.util.Map;

class MySimpleAdapterVodType3View extends SimpleAdapter {
    private Context _this;
    private int current_index = 0;
    private float rate;
    private int selectedPosition = -1;
    private Typeface typeFace;

    public MySimpleAdapterVodType3View(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
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

    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    private void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        LinearLayout il = (LinearLayout) v.findViewById(C0216R.id.ItemLayout);
        LayoutParams layoutParams0 = (LayoutParams) il.getLayoutParams();
        layoutParams0.height = MGplayer.screenHeight / 9;
        il.setLayoutParams(layoutParams0);
        TextView vh = (TextView) v.findViewById(C0216R.id.ItemTitle);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vh.getLayoutParams();
        layoutParams.width = (int) (this.rate * 15.0f);
        layoutParams.height = MGplayer.screenHeight / 9;
        vh.setLayoutParams(layoutParams);
        vh.setTextSize(this.rate * 8.0f);
        vh.setTypeface(this.typeFace);
        vh.setTextColor(Color.rgb(255, 255, 255));
        if (this.selectedPosition == position) {
            vh.setBackgroundResource(C0216R.drawable.gradient9);
        } else {
            vh.setBackgroundResource(C0216R.color.transparent);
        }
        return v;
    }
}
