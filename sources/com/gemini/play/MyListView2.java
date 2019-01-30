package com.gemini.play;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;

public class MyListView2 extends LinearLayout {
    public static String currentID = null;
    private Context _this = null;
    private MySimpleAdapterLiveGridView adapter = null;
    private GridView gridview;
    private ListViewInterface iface = null;
    private boolean isShow = false;
    private ArrayList<HashMap<String, Object>> list = new ArrayList();
    public Handler rHandler = new C04234();

    /* renamed from: com.gemini.play.MyListView2$1 */
    class C04201 implements OnItemClickListener {
        C04201() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            String id = (String) ((HashMap) MyListView2.this.gridview.getItemAtPosition(arg2)).get("ItemId");
            MGplayer.MyPrintln("list click Itemid =" + id);
            if (id != null) {
                MyListView2.currentID = id;
                MyListView2.this.iface.callback(0, id);
                MGplayer.MyPrintln("list click id =" + id);
                MyListView2.this.showViewTimeout();
            }
        }
    }

    /* renamed from: com.gemini.play.MyListView2$2 */
    class C04212 implements OnItemLongClickListener {
        C04212() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyListView2.this.iface.callback(3, (String) ((HashMap) MyListView2.this.gridview.getItemAtPosition(arg2)).get("ItemId"));
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyListView2$4 */
    class C04234 extends Handler {
        C04234() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyListView2.this.hidePlayList();
                    return;
                default:
                    return;
            }
        }
    }

    public MyListView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.listview2, this, true);
        init();
    }

    public MyListView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.listview2, this, true);
        init();
    }

    public MyListView2(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.listview2, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = MGplayer.getFontsType(this._this);
        float rate = MGplayer.getFontsRate();
        this.gridview = (GridView) findViewById(C0216R.id.gridview);
        set_init();
        this.gridview.setOnItemClickListener(new C04201());
        this.gridview.setOnItemLongClickListener(new C04212());
    }

    public void set_ad_Image(ImageView ad) {
    }

    private void bitmapcache_init() {
    }

    public void set_init() {
        this.adapter = new MySimpleAdapterLiveGridView(this._this, this.list, C0216R.layout.listitem2, new String[]{"ItemId", "ItemText"}, new int[]{C0216R.id.ItemId, C0216R.id.ItemText});
        this.gridview.setAdapter(this.adapter);
    }

    public void set_list(String type) {
        set_list();
    }

    public void set_list() {
        this.list.clear();
        int size = LIVEplayer.urlSize();
        MGplayer.MyPrintln("set_list size = " + size);
        for (int i = 0; i < size; i++) {
            int id = LIVEplayer.idGet(i);
            HashMap<String, Object> map = new HashMap();
            map.put("ItemText", LIVEplayer.nameGet(i));
            map.put("ItemId", String.format("%03d", new Object[]{Integer.valueOf(id)}));
            this.list.add(map);
        }
        set_list_notifyDataSetChanged();
    }

    public void collect_list() {
    }

    public void set_list_notifyDataSetChanged() {
        boolean z = false;
        this.adapter.notifyDataSetChanged();
        if (MGplayer.isNumeric(LIVEplayer.currentID)) {
            int num;
            if (LIVEplayer.show_ps_playlist) {
                num = LIVEplayer.getVideoNum(Integer.parseInt(LIVEplayer.currentID), false);
            } else {
                int parseInt = Integer.parseInt(LIVEplayer.currentID);
                if (!LIVEplayer.typePasswordOK) {
                    z = true;
                }
                num = LIVEplayer.getVideoNum(parseInt, z);
            }
            MGplayer.MyPrintln("set_list currentid = " + LIVEplayer.currentID + " num=" + num);
            this.gridview.setSelection(num);
        }
    }

    public void setSelection(int index) {
        this.gridview.setSelection(index);
    }

    public ListView getListView() {
        return null;
    }

    public void showPlayList(String type) {
        showPlayList(type, false);
    }

    public void showPlayList(String type, boolean check_type_selection) {
        boolean z = true;
        if (!isShown()) {
            set_list();
            showViewTimeout();
            this.isShow = true;
            listFocus();
            if (MGplayer.isNumeric(LIVEplayer.getCurrentID())) {
                int i;
                if (LIVEplayer.show_ps_playlist) {
                    i = LIVEplayer.getVideoNum(Integer.parseInt(LIVEplayer.getCurrentID()), type, false);
                    MGplayer.MyPrintln("show playlist type = " + type + " LIVEplayer.getCurrentID():" + LIVEplayer.getCurrentID() + " Selection:" + i);
                } else {
                    int parseInt = Integer.parseInt(LIVEplayer.getCurrentID());
                    if (LIVEplayer.typePasswordOK) {
                        z = false;
                    }
                    i = LIVEplayer.getVideoNum(parseInt, type, z);
                    MGplayer.MyPrintln("no show playlist type = " + type + " LIVEplayer.getCurrentID():" + LIVEplayer.getCurrentID() + " Selection:" + i);
                }
                setSelection(i);
            } else {
                setSelection(0);
            }
            setVisibility(0);
            listFocus();
        }
    }

    public void showPlayList(int type) {
        boolean z = true;
        if (!isShown()) {
            set_list();
            showViewTimeout();
            this.isShow = true;
            listFocus();
            setVisibility(0);
            if (MGplayer.isNumeric(LIVEplayer.getCurrentID()) && type == 0) {
                int num;
                if (LIVEplayer.show_ps_playlist) {
                    num = LIVEplayer.getVideoNum(Integer.parseInt(LIVEplayer.getCurrentID()), false);
                } else {
                    int parseInt = Integer.parseInt(LIVEplayer.getCurrentID());
                    if (LIVEplayer.typePasswordOK) {
                        z = false;
                    }
                    num = LIVEplayer.getVideoNum(parseInt, z);
                }
                final int i = num;
                MGplayer.MyPrintln("1 LIVEplayer.getCurrentID():" + LIVEplayer.getCurrentID() + "setSelection:" + i);
                this.gridview.post(new Runnable() {
                    public void run() {
                        MyListView2.this.gridview.requestFocusFromTouch();
                        MyListView2.this.gridview.setSelection(i);
                    }
                });
            } else if (MGplayer.isNumeric(LIVEplayer.getCurrentID()) && type == 1) {
                setSelection(0);
            }
            setVisibility(0);
            listFocus();
        }
    }

    public void hidePlayList() {
        setVisibility(8);
        this.isShow = false;
    }

    public void show_ad_image() {
    }

    public void hide_ad_image() {
    }

    public void hideViewTimeout() {
        Message msg = new Message();
        msg.what = 0;
        this.rHandler.sendMessageDelayed(msg, 30000);
    }

    public void showViewTimeout() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        hideViewTimeout();
    }

    public void showViewNoTimeout() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
    }

    public void listFocus() {
        this.gridview.setFocusable(true);
        this.gridview.setFocusableInTouchMode(true);
        this.gridview.requestFocus();
        this.gridview.requestFocusFromTouch();
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public String getCurrentID() {
        if (MGplayer.isNumeric(currentID)) {
            return currentID;
        }
        return "0";
    }

    public void setCurrentID(int id) {
        currentID = String.valueOf(id);
    }

    public void setCurrentID(String id) {
        currentID = id;
    }

    public boolean isShown() {
        return this.isShow;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                case 20:
                    MGplayer.MyPrintln("list dispatchKeyEvent");
                    super.dispatchKeyEvent(event);
                    listFocus();
                    return true;
                case 21:
                    super.dispatchKeyEvent(event);
                    listFocus();
                    return true;
                case 22:
                    super.dispatchKeyEvent(event);
                    listFocus();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
