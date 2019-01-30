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
import cz.msebera.android.httpclient.HttpStatus;
import io.vov.vitamio.ThumbnailUtils;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MySimpleAdapterPreviewView extends SimpleAdapter {
    private Context _this;
    private int current_index = 0;
    private float rate;
    private Typeface typeFace;

    public MySimpleAdapterPreviewView(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this._this = context;
        init();
    }

    private void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
    }

    private int timeOut(String time0, String time1) {
        if (time0 == null || time1 == null) {
            return 0;
        }
        if (time0.equals(time1)) {
            return 1;
        }
        String[] times1 = time1.split(":");
        int nhour = 0;
        int nminute = 0;
        if (times1.length >= 2) {
            nhour = Integer.parseInt(times1[0]);
            nminute = Integer.parseInt(times1[1]);
        }
        String[] times0 = time0.split(":");
        if (times0.length < 2) {
            return 2;
        }
        int hour = Integer.parseInt(times0[0]);
        int minute = Integer.parseInt(times0[1]);
        if (hour < nhour) {
            return 0;
        }
        if (hour == nhour && minute < nminute) {
            return 0;
        }
        if ((hour != nhour || minute <= nminute) && hour <= nhour) {
            return 1;
        }
        return 2;
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        int nhour = calendar.get(11);
        return nhour + ":" + calendar.get(12);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView vh = (TextView) v.findViewById(C0216R.id.ItemTime);
        TextView vt = (TextView) v.findViewById(C0216R.id.ItemPreview);
        vh.setTextSize(this.rate * 8.0f);
        vh.setTypeface(this.typeFace);
        vt.setTextSize(this.rate * 8.0f);
        vt.setTypeface(this.typeFace);
        LayoutParams layoutParams = (LayoutParams) vh.getLayoutParams();
        if (MGplayer.getDensityDpi() == ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL_HEIGHT) {
            layoutParams.width = (int) (this.rate * 15.0f);
            layoutParams.height = (int) (this.rate * 20.0f);
        } else if (MGplayer.getDensityDpi() <= ThumbnailUtils.TARGET_SIZE_MINI_THUMBNAIL_HEIGHT) {
            layoutParams.width = (int) (this.rate * 6.0f);
            layoutParams.height = (int) (this.rate * 30.0f);
        } else {
            layoutParams.width = (int) (this.rate * 6.0f);
            layoutParams.height = (int) ((this.rate * 20.0f) * ((((float) (MGplayer.getDensityDpi() - 320)) / 160.0f) + 1.0f));
        }
        vh.setLayoutParams(layoutParams);
        layoutParams = (LayoutParams) vt.getLayoutParams();
        if (MGplayer.getDensityDpi() == ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL_HEIGHT) {
            layoutParams.width = (int) (this.rate * 15.0f);
            layoutParams.height = (int) (this.rate * 20.0f);
        } else if (MGplayer.getDensityDpi() <= ThumbnailUtils.TARGET_SIZE_MINI_THUMBNAIL_HEIGHT) {
            layoutParams.width = (int) (this.rate * 15.0f);
            layoutParams.height = (int) (this.rate * 30.0f);
        } else {
            layoutParams.width = (int) (this.rate * 15.0f);
            layoutParams.height = (int) ((this.rate * 20.0f) * ((((float) (MGplayer.getDensityDpi() - 320)) / 160.0f) + 1.0f));
        }
        vt.setLayoutParams(layoutParams);
        if (this.current_index == -1) {
            if (v != null) {
                vh.setTextColor(Color.rgb(255, 255, 255));
                vt.setTextColor(Color.rgb(255, 255, 255));
            }
        } else if (position < this.current_index) {
            if (v != null) {
                vh.setTextColor(Color.rgb(255, HttpStatus.SC_CREATED, 14));
                vt.setTextColor(Color.rgb(255, HttpStatus.SC_CREATED, 14));
            }
        } else if (position == this.current_index) {
            if (v != null) {
                vh.setTextColor(Color.rgb(255, 0, 0));
                vt.setTextColor(Color.rgb(255, 0, 0));
            }
        } else if (position > this.current_index && v != null) {
            vh.setTextColor(Color.rgb(255, 255, 255));
            vt.setTextColor(Color.rgb(255, 255, 255));
        }
        return v;
    }

    public void setCurrentIndex(int index) {
        this.current_index = index;
    }
}
