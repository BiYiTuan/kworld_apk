package com.gemini.play;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.util.List;
import java.util.Map;

public class MySimpleAdapterLiveLineView extends SimpleAdapter {
    private Context _this;
    private float rate;
    private Typeface typeFace;

    public MySimpleAdapterLiveLineView(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this._this = context;
        init();
    }

    private void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView vt = (TextView) v.findViewById(C0216R.id.ItemTitle);
        vt.setTextSize(this.rate * 8.0f);
        vt.setTypeface(this.typeFace);
        return v;
    }
}
