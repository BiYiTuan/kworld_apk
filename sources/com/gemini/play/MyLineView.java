package com.gemini.play;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;

public class MyLineView {
    private MySimpleAdapterLiveLineView adapter = null;
    private AlertDialog dialog = null;
    private ListViewInterface iface = null;
    private LayoutInflater layoutInflater = null;
    private View lineActivity = null;
    private ArrayList<HashMap<String, Object>> list = new ArrayList();
    private ListView listview;
    private Context mContext = null;

    /* renamed from: com.gemini.play.MyLineView$1 */
    class C04061 implements OnClickListener {
        C04061() {
        }

        public void onClick(View arg0) {
            MyLineView.this.dialog.hide();
        }
    }

    /* renamed from: com.gemini.play.MyLineView$2 */
    class C04072 implements OnFocusChangeListener {
        C04072() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyLineView$3 */
    class C04083 implements OnItemClickListener {
        C04083() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyLineView.this.iface.callback(0, ((HashMap) MyLineView.this.listview.getItemAtPosition(arg2)).get("ItemLine").toString());
            if (MyLineView.this.dialog != null) {
                MyLineView.this.dialog.hide();
            }
        }
    }

    /* renamed from: com.gemini.play.MyLineView$5 */
    class C04105 implements ViewBinder {
        C04105() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    public void showView(Context _this, String id) {
        showView(_this, id, 0);
    }

    public void initView(final Context _this) {
        this.mContext = _this;
        this.layoutInflater = LayoutInflater.from(_this);
        this.lineActivity = this.layoutInflater.inflate(C0216R.layout.liveline, null);
        this.listview = (ListView) this.lineActivity.findViewById(C0216R.id.line_listView);
        this.listview.setSelector(new ColorDrawable(0));
        Button cancelbutton = (Button) this.lineActivity.findViewById(C0216R.id.line_cancel);
        cancelbutton.setOnClickListener(new C04061());
        cancelbutton.setOnFocusChangeListener(new C04072());
        Typeface typeFace = MGplayer.getFontsType(_this);
        cancelbutton.setTextSize(7.0f * MGplayer.getFontsRate());
        cancelbutton.setTypeface(typeFace);
        this.listview.setOnItemClickListener(new C04083());
        this.listview.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MyLineView.this.listview.setSelector(ContextCompat.getDrawable(_this, C0216R.drawable.gradient9));
                    return;
                }
                MGplayer.MyPrintln("listview onfocus");
                MyLineView.this.listview.setSelector(new ColorDrawable(0));
            }
        });
        this.dialog = new Builder(_this).setView(this.lineActivity).create();
        this.dialog.getWindow().setLayout((MGplayer.screenWidth / 5) * 2, MGplayer.screenHeight);
        init_list();
    }

    public void showView(Context _this, String id, int line) {
        show_list(id, line);
        this.dialog.show();
    }

    public void init_list() {
        this.adapter = new MySimpleAdapterLiveLineView(this.mContext, this.list, C0216R.layout.lineitem, new String[]{"ItemTitle"}, new int[]{C0216R.id.ItemTitle});
        this.listview.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C04105());
    }

    public void hide() {
        if (this.dialog != null) {
            this.dialog.hide();
        }
    }

    private void show_list(String id, int line) {
        if (MGplayer.isNumeric(id)) {
            int ii;
            HashMap<String, Object> map;
            this.list.clear();
            int high_count = 0;
            int low_count = 0;
            if (!(LIVEplayer.currentType == null || LIVEplayer.currentType.equals("1"))) {
                if (MGplayer.custom().equals("quanxing")) {
                    high_count = 1;
                    low_count = 0;
                } else {
                    String urlss = MGplayer.ju(LIVEplayer.getVideoUrl(Integer.parseInt(id)));
                    high_count = LIVEplayer.getVideoUrlHighCount(urlss);
                    low_count = LIVEplayer.getVideoUrlLowCount(urlss);
                }
            }
            for (ii = 0; ii < high_count; ii++) {
                map = new HashMap();
                map.put("ItemTitle", this.mContext.getString(C0216R.string.liveline_text2) + " " + this.mContext.getString(C0216R.string.liveline_text1) + "-" + (ii + 1));
                map.put("ItemLine", String.valueOf(ii));
                this.list.add(map);
            }
            for (ii = high_count; ii < high_count + low_count; ii++) {
                map = new HashMap();
                map.put("ItemTitle", this.mContext.getString(C0216R.string.liveline_text3) + " " + this.mContext.getString(C0216R.string.liveline_text1) + "-" + (ii + 1));
                map.put("ItemLine", String.valueOf(ii));
                this.list.add(map);
            }
            this.adapter.notifyDataSetChanged();
            this.listview.setSelection(line);
        }
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }
}
