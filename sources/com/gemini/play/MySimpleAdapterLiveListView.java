package com.gemini.play;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.gemini.custom.spain1;
import com.gemini.kvod2.C0216R;
import java.util.List;
import java.util.Map;

class MySimpleAdapterLiveListView extends SimpleAdapter {
    private Context _this;
    private int current_index = 0;
    OnHoverListener oListener = new C04511();
    private float rate;
    private int selectedPosition = -1;
    private Typeface typeFace;

    /* renamed from: com.gemini.play.MySimpleAdapterLiveListView$1 */
    class C04511 implements OnHoverListener {
        C04511() {
        }

        public boolean onHover(View v, MotionEvent event) {
            switch (event.getAction()) {
                case 9:
                    v.setBackgroundResource(C0216R.drawable.gradient7);
                    break;
                case 10:
                    v.setBackgroundResource(C0216R.color.transparent);
                    break;
            }
            return false;
        }
    }

    public MySimpleAdapterLiveListView(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
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
            this.rate *= spain1.rate;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        v.setOnHoverListener(this.oListener);
        ImageView vi = (ImageView) v.findViewById(C0216R.id.ItemView);
        ImageView vi2 = (ImageView) v.findViewById(C0216R.id.ItemView2);
        TextView vh = (TextView) v.findViewById(C0216R.id.ItemId);
        TextView vt = (TextView) v.findViewById(C0216R.id.ItemTitle);
        vh.setTextSize(this.rate * 7.0f);
        vh.setTypeface(this.typeFace);
        vt.setTextSize(this.rate * 8.0f);
        vt.setTypeface(this.typeFace);
        if (LIVEplayer.show_playlist_image) {
            if (MGplayer.getDensityDpi() == 480) {
            }
        } else if (MGplayer.getDensityDpi() == 480) {
            vi.setVisibility(8);
        } else {
            vi.setVisibility(8);
        }
        int height = v.getMeasuredHeight();
        LayoutParams params = v.getLayoutParams();
        if (MGplayer.custom().equals("simba")) {
            params.height = MGplayer.screenHeight / 6;
        } else {
            params.height = MGplayer.screenHeight / 12;
        }
        v.setLayoutParams(params);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vt.getLayoutParams();
        lp.height = params.height - 20;
        lp.width = params.height - 10;
        vt.setLayoutParams(lp);
        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) vh.getLayoutParams();
        lp2.height = params.height - 20;
        vh.setLayoutParams(lp2);
        MGplayer.MyPrintln("vt:" + (this.rate * 8.0f) + " height:" + height);
        vh.setTextColor(Color.rgb(255, 255, 255));
        vt.setTextColor(Color.rgb(255, 255, 255));
        return v;
    }
}
