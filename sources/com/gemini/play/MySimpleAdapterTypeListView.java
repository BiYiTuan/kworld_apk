package com.gemini.play;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.util.List;
import java.util.Map;

class MySimpleAdapterTypeListView extends SimpleAdapter {
    private Context _this;
    private int current_index = 0;
    private float rate;
    private int selectedPosition = -1;
    private Typeface typeFace;

    public MySimpleAdapterTypeListView(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
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
        vh.setTextSize(this.rate * 7.0f);
        vh.setTypeface(this.typeFace);
        ImageView vi = (ImageView) v.findViewById(C0216R.id.ItemIcon);
        LayoutParams layoutParams = (LayoutParams) vi.getLayoutParams();
        layoutParams.width = (int) (this.rate * 15.0f);
        layoutParams.height = (int) (this.rate * 15.0f);
        vi.setLayoutParams(layoutParams);
        vh.setTextColor(Color.rgb(255, 255, 255));
        return v;
    }
}
