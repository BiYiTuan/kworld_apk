package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;

public class MyBackListView extends LinearLayout {
    private Context _this = null;
    MySimpleAdapterBackListView adapter = null;
    private String firstID = null;
    private ListViewInterface iface = null;
    ArrayList<HashMap<String, Object>> list = new ArrayList();
    private TextView listtext = null;
    private ListView listview = null;

    /* renamed from: com.gemini.play.MyBackListView$1 */
    class C03491 implements OnClickListener {
        C03491() {
        }

        public void onClick(View v) {
            MyBackListView.this.iface.callback(1, null);
        }
    }

    /* renamed from: com.gemini.play.MyBackListView$2 */
    class C03502 implements OnItemClickListener {
        C03502() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            String id = (String) ((HashMap) MyBackListView.this.listview.getItemAtPosition(arg2)).get("ItemId");
            BACKplayer.currentID = id;
            MyBackListView.this.iface.callback(2, id);
        }
    }

    /* renamed from: com.gemini.play.MyBackListView$3 */
    class C03513 implements OnItemSelectedListener {
        C03513() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyBackListView.this.iface.callback(2, (String) ((HashMap) MyBackListView.this.listview.getItemAtPosition(arg2)).get("ItemId"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyBackListView$4 */
    class C03524 implements OnFocusChangeListener {
        C03524() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                MyBackListView.this.listview.setSelector(ContextCompat.getDrawable(MyBackListView.this._this, C0216R.drawable.gradient9));
                if (BACKplayer.currentID != null && MGplayer.isNumeric(BACKplayer.currentID)) {
                    MyBackListView.this.listview.setSelection(BACKplayer.playbackGetVideoNum(Integer.parseInt(BACKplayer.currentID), false));
                    return;
                }
                return;
            }
            MyBackListView.this.listview.setSelector(ContextCompat.getDrawable(MyBackListView.this._this, C0216R.color.transparent));
        }
    }

    /* renamed from: com.gemini.play.MyBackListView$5 */
    class C03535 implements ViewBinder {
        C03535() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    public MyBackListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backlistview, this, true);
        init();
    }

    public MyBackListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backlistview, this, true);
        init();
    }

    public MyBackListView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backlistview, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        float rate = MGplayer.getFontsRate();
        this.listview = (ListView) findViewById(C0216R.id.listview);
        this.listview.setSelector(ContextCompat.getDrawable(this._this, C0216R.drawable.gradient9));
        this.listtext = (TextView) findViewById(C0216R.id.listtext);
        TextView listtext = (TextView) findViewById(C0216R.id.listtext);
        listtext.setTextSize(8.0f * rate);
        listtext.setTypeface(typeFace);
        listtext.setOnClickListener(new C03491());
        this.listview.setOnItemClickListener(new C03502());
        this.listview.setOnItemSelectedListener(new C03513());
        this.listview.setOnFocusChangeListener(new C03524());
        init_list();
        set_list();
    }

    public void showListView(String type) {
        if (type != null) {
            set_list(type);
            this.iface.callback(2, this.firstID);
            return;
        }
        set_list();
    }

    public void set_list(String type) {
        this.listtext.setText(BACKplayer.playbackTypeNameGetFromId(type));
        this.list.clear();
        this.firstID = null;
        int size = BACKplayer.playbackSize();
        MGplayer.MyPrintln("backplayer size " + size);
        for (int i = 0; i < size; i++) {
            int id = BACKplayer.playbackIdGet(i);
            if (BACKplayer.playbackTypeGet(i).indexOf(type) >= 0 && (BACKplayer.typePasswordOK || !BACKplayer.playbackIsVideoTypePs(id))) {
                HashMap<String, Object> map = new HashMap();
                String imagePath = this._this.getFilesDir() + "/icon/" + BACKplayer.playbackImageGet(i);
                if (MGplayer.fileIsExists(imagePath)) {
                    map.put("ItemView", BitmapFactory.decodeFile(imagePath));
                } else {
                    map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti));
                }
                map.put("ItemTitle", BACKplayer.playbackNameGet(i));
                MGplayer.MyPrintln("ID = " + i + " ItemTitle = " + BACKplayer.playbackGetVideoName(i));
                String ItemId = String.format("%03d", new Object[]{Integer.valueOf(BACKplayer.playbackIdGet(i))});
                if (this.firstID == null) {
                    this.firstID = ItemId;
                }
                map.put("ItemId", ItemId);
                if (BACKplayer.playbackSourceGet(i).equals("hd")) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.h));
                } else if (BACKplayer.playbackSourceGet(i).equals(TtmlNode.TAG_P)) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.p));
                } else {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.s));
                }
                this.list.add(map);
            }
        }
        this.adapter.notifyDataSetChanged();
    }

    public void set_list() {
        this.list.clear();
        int size = BACKplayer.playbackSize();
        MGplayer.MyPrintln("backplayer size all" + size);
        for (int i = 0; i < size; i++) {
            int id = BACKplayer.playbackIdGet(i);
            if (BACKplayer.typePasswordOK || !BACKplayer.playbackIsVideoTypePs(id)) {
                HashMap<String, Object> map = new HashMap();
                String imagePath = this._this.getFilesDir() + "/icon/" + BACKplayer.playbackImageGet(i);
                MGplayer.MyPrintln("imagePath = " + imagePath);
                if (MGplayer.fileIsExists(imagePath)) {
                    map.put("ItemView", BitmapFactory.decodeFile(imagePath));
                } else {
                    map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti));
                }
                map.put("ItemTitle", BACKplayer.playbackNameGet(i));
                map.put("ItemId", String.format("%03d", new Object[]{Integer.valueOf(BACKplayer.playbackIdGet(i))}));
                if (BACKplayer.playbackSourceGet(i).equals("hd")) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.h));
                } else if (BACKplayer.playbackSourceGet(i).equals(TtmlNode.TAG_P)) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.p));
                } else {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.s));
                }
                this.list.add(map);
            }
        }
        this.adapter.notifyDataSetChanged();
    }

    public void init_list() {
        this.adapter = new MySimpleAdapterBackListView(this._this, this.list, C0216R.layout.backlistitem, new String[]{"ItemView", "ItemId", "ItemTitle", "ItemView2"}, new int[]{C0216R.id.ItemView, C0216R.id.ItemId, C0216R.id.ItemTitle, C0216R.id.ItemView2});
        this.listview.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C03535());
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            int pos;
            int end;
            int first;
            switch (event.getKeyCode()) {
                case 19:
                    pos = this.listview.getSelectedItemPosition();
                    end = this.listview.getLastVisiblePosition();
                    first = this.listview.getFirstVisiblePosition();
                    MGplayer.MyPrintln("backlist up " + first + " " + end);
                    if (pos <= first || pos > end) {
                        if (pos < 0) {
                            pos = end;
                        } else {
                            pos--;
                        }
                        this.listview.setSelection(pos);
                        listFocus();
                        return true;
                    }
                    super.dispatchKeyEvent(event);
                    listFocus();
                    return true;
                case 20:
                    pos = this.listview.getSelectedItemPosition();
                    end = this.listview.getLastVisiblePosition();
                    first = this.listview.getFirstVisiblePosition();
                    MGplayer.MyPrintln("backlist down " + first + " " + end);
                    if (pos < first || pos >= end) {
                        if (pos > end) {
                            pos = 0;
                        } else {
                            pos++;
                        }
                        this.listview.setSelection(pos);
                        listFocus();
                        return true;
                    }
                    super.dispatchKeyEvent(event);
                    listFocus();
                    return true;
                case 22:
                    this.iface.callback(3, null);
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setSelection(int index) {
        this.listview.setSelection(index);
    }

    public void listFocus() {
        this.listview.setFocusable(true);
        this.listview.setFocusableInTouchMode(true);
        this.listview.requestFocus();
        this.listview.requestFocusFromTouch();
    }
}
