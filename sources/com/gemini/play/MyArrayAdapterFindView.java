package com.gemini.play;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import io.vov.vitamio.ThumbnailUtils;
import java.util.ArrayList;

public class MyArrayAdapterFindView extends ArrayAdapter {
    private Context _this;
    private String[] mStringArray;
    private ArrayList<String> mStringArray2;
    private float rate;
    private Typeface typeFace;
    private int used = 0;

    public MyArrayAdapterFindView(Context context, String[] stringArray) {
        super(context, 17367048, stringArray);
        this._this = context;
        this.used = 0;
        this.mStringArray = stringArray;
        init();
    }

    public MyArrayAdapterFindView(Context context, ArrayList<String> stringArray) {
        super(context, 17367048, stringArray);
        this._this = context;
        this.used = 1;
        this.mStringArray2 = stringArray;
        init();
    }

    private void init() {
        this.typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        this.rate = (float) (MGplayer.screenWidth / ThumbnailUtils.TARGET_SIZE_MINI_THUMBNAIL_HEIGHT);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this._this).inflate(17367049, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(16908308);
        if (this.used == 0) {
            tv.setText(this.mStringArray[position]);
        } else {
            tv.setText((CharSequence) this.mStringArray2.get(position));
        }
        tv.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        tv.setTextSize((float) ((int) (((double) this.rate) * 6.5d)));
        tv.setTypeface(this.typeFace);
        return convertView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this._this).inflate(17367048, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(16908308);
        if (this.used == 0) {
            tv.setText(this.mStringArray[position]);
        } else {
            tv.setText((CharSequence) this.mStringArray2.get(position));
        }
        tv.setTextSize((float) ((int) (((double) this.rate) * 6.5d)));
        tv.setTypeface(this.typeFace);
        tv.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        return convertView;
    }
}
