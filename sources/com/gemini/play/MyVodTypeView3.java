package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodTypeView3 extends LinearLayout {
    private Context _this = null;
    private MySimpleAdapterVodType3View adapter = null;
    private ListViewInterface iface = null;
    private ArrayList<HashMap<String, Object>> list = new ArrayList();
    private ListView listview = null;
    private float rate;
    private Typeface typeFace;

    /* renamed from: com.gemini.play.MyVodTypeView3$1 */
    class C05541 implements OnItemClickListener {
        C05541() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodTypeView3.this.iface.callback(0, (String) ((HashMap) MyVodTypeView3.this.listview.getItemAtPosition(arg2)).get("ItemId"));
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView3$2 */
    class C05552 implements OnFocusChangeListener {
        C05552() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                MyVodTypeView3.this.listview.setFocusable(true);
                MyVodTypeView3.this.listview.setFocusableInTouchMode(true);
                MyVodTypeView3.this.listview.requestFocus();
                MyVodTypeView3.this.listview.requestFocusFromTouch();
                MyVodTypeView3.this.listview.setSelector(ContextCompat.getDrawable(MyVodTypeView3.this._this, C0216R.drawable.gradient9));
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView3$3 */
    class C05563 implements ViewBinder {
        C05563() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    public MyVodTypeView3(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtypeview3, this, true);
        init();
    }

    public MyVodTypeView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtypeview3, this, true);
        init();
    }

    public MyVodTypeView3(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtypeview3, this, true);
        init();
    }

    private void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
        String timeout = "";
        if (MGplayer.isShowLefttime == 1) {
            if (Integer.parseInt(MGplayer.leftdays) == -1) {
                timeout = timeout + this._this.getString(C0216R.string.myhomebar_text7).toString() + ":" + this._this.getString(C0216R.string.myhomebar_text9).toString();
            } else {
                timeout = timeout + this._this.getString(C0216R.string.myhomebar_text7).toString() + ":" + MGplayer.leftdays + this._this.getString(C0216R.string.myhomebar_text8).toString();
            }
        }
        TextView leftdaytext = (TextView) findViewById(C0216R.id.leftday);
        leftdaytext.setTextSize(this.rate * 6.0f);
        leftdaytext.setTypeface(this.typeFace);
        leftdaytext.setText(timeout);
        this.listview = (ListView) findViewById(C0216R.id.typelist);
        this.listview.setSelector(ContextCompat.getDrawable(this._this, C0216R.drawable.gradient9));
        this.listview.setVerticalScrollBarEnabled(false);
        this.listview.setFastScrollEnabled(false);
        this.listview.setOnItemClickListener(new C05541());
        setOnFocusChangeListener(new C05552());
        this.adapter = new MySimpleAdapterVodType3View(this._this, this.list, C0216R.layout.vodtypeitem3, new String[]{"ItemTitle", "ItemId"}, new int[]{C0216R.id.ItemTitle, C0216R.id.ItemId});
        this.listview.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C05563());
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public void set_init(int type) {
        set_init(type, -1);
    }

    public void set_init(int type, int findtyp) {
        MGplayer.MyPrintln("VODplayer.type:" + VODplayer.type);
        int ii;
        HashMap<String, Object> map;
        if (VODplayer.type.equals("find")) {
            for (ii = 0; ii < 4; ii++) {
                map = new HashMap();
                map.put("ItemTitle", VODplayer.columner[ii].name);
                map.put("ItemId", String.valueOf(ii));
                this.list.add(map);
            }
        } else if (VODplayer.type.equals("hot")) {
            map = new HashMap();
            map.put("ItemTitle", this._this.getString(C0216R.string.vodplayermain_text2));
            map.put("ItemId", "hot");
            this.list.add(map);
        } else if (VODplayer.type.equals("collect")) {
            map = new HashMap();
            map.put("ItemTitle", this._this.getString(C0216R.string.vodplayermain_text3));
            map.put("ItemId", "collect");
            this.list.add(map);
            map2 = new HashMap();
            map2.put("ItemTitle", this._this.getString(C0216R.string.clear));
            map2.put("ItemId", "collectclear");
            this.list.add(map2);
        } else if (VODplayer.type.equals("history")) {
            map = new HashMap();
            map.put("ItemTitle", this._this.getString(C0216R.string.vodplayermain_text5));
            map.put("ItemId", "history");
            this.list.add(map);
            map2 = new HashMap();
            map2.put("ItemTitle", this._this.getString(C0216R.string.clear));
            map2.put("ItemId", "historyclear");
            this.list.add(map2);
        } else if (!(VODplayer.columner == null || VODplayer.columner.length - 1 < type - 1 || VODplayer.columner[type].type_type == null)) {
            if (!MGplayer.custom().equals("turbotv") && VODplayer.show_all_type) {
                HashMap<String, Object> map1 = new HashMap();
                map1.put("ItemTitle", this._this.getString(C0216R.string.vodplayermain_text1));
                map1.put("ItemId", "-1");
                this.list.add(map1);
                HashMap<String, Object> map0 = new HashMap();
                map0.put("ItemTitle", this._this.getString(C0216R.string.vodclassify_text5));
                map0.put("ItemId", String.format("%03d", new Object[]{Integer.valueOf(0)}));
                this.list.add(map0);
            }
            String[] types = VODplayer.columner[type].type_type.split("\\|");
            for (ii = 0; ii < types.length; ii++) {
                map = new HashMap();
                map.put("ItemTitle", types[ii]);
                map.put("ItemId", String.format("%03d", new Object[]{Integer.valueOf(ii + 1)}));
                MGplayer.MyPrintln("ItemTitle:" + types[ii] + "ItemID:" + String.format("%03d", new Object[]{Integer.valueOf(ii)}));
                this.list.add(map);
            }
        }
        this.adapter.notifyDataSetChanged();
        if (VODplayer.type.equals("find") && findtyp >= 0) {
            this.listview.setSelection(findtyp);
            this.iface.callback(0, String.valueOf(findtyp));
        }
    }

    private void selectIndexNoFocus() {
        this.listview.setSelector(ContextCompat.getDrawable(this._this, C0216R.mipmap.se4));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 22:
                selectIndexNoFocus();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
