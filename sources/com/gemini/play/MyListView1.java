package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;

public class MyListView1 extends LinearLayout {
    public static String currentID = null;
    private Context _this = null;
    private ImageView ad_image = null;
    private MySimpleAdapterLiveListView adapter = null;
    private ArrayList<HashMap<String, Object>> all_list = new ArrayList();
    private LruBitmapCache bitmapCache = null;
    private String current_type = null;
    private ListViewInterface iface = null;
    private boolean isShow = false;
    private ArrayList<HashMap<String, Object>> list = new ArrayList();
    private Button listdleft = null;
    private Button listdright = null;
    private ArrayList<listStatus> lists_type = new ArrayList();
    private TextView listtext = null;
    private ListView listview = null;
    public Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyListView1.this.hidePlayList();
                    return;
                default:
                    return;
            }
        }
    };

    /* renamed from: com.gemini.play.MyListView1$1 */
    class C04111 implements OnClickListener {
        C04111() {
        }

        public void onClick(View v) {
            MyListView1.this.iface.callback(1, null);
            MyListView1.this.showViewTimeout();
        }
    }

    /* renamed from: com.gemini.play.MyListView1$2 */
    class C04122 implements OnItemClickListener {
        C04122() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            HashMap<String, String> map = (HashMap) MyListView1.this.listview.getItemAtPosition(arg2);
            String url = (String) map.get("ItemUrl");
            MGplayer.MyPrintln("list click ItemUrl =" + url);
            if (url != null) {
                String id = (String) map.get("ItemId");
                MyListView1.currentID = id;
                MyListView1.this.iface.callback(4, id);
                MGplayer.MyPrintln("list click id =" + id);
                MyListView1.this.showViewTimeout();
                return;
            }
            id = (String) map.get("ItemId");
            MyListView1.currentID = id;
            MyListView1.this.iface.callback(0, id);
            MGplayer.MyPrintln("list click id =" + id);
            MyListView1.this.showViewTimeout();
        }
    }

    /* renamed from: com.gemini.play.MyListView1$3 */
    class C04133 implements OnItemSelectedListener {
        C04133() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            String id = (String) ((HashMap) MyListView1.this.listview.getItemAtPosition(arg2)).get("ItemId");
            MyListView1.currentID = id;
            MGplayer.MyPrintln("list click id =" + id);
            MyListView1.this.showViewTimeout();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyListView1$4 */
    class C04144 implements OnItemLongClickListener {
        C04144() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyListView1.this.iface.callback(3, (String) ((HashMap) MyListView1.this.listview.getItemAtPosition(arg2)).get("ItemId"));
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyListView1$5 */
    class C04155 implements OnClickListener {
        C04155() {
        }

        public void onClick(View v) {
            MyListView1.this.iface.callback(1, null);
            MyListView1.this.showViewTimeout();
        }
    }

    /* renamed from: com.gemini.play.MyListView1$6 */
    class C04166 implements OnClickListener {
        C04166() {
        }

        public void onClick(View v) {
            MyListView1.this.hidePlayList();
        }
    }

    /* renamed from: com.gemini.play.MyListView1$7 */
    class C04177 implements ViewBinder {
        C04177() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyListView1$8 */
    class C04188 implements AnimationListener {
        C04188() {
        }

        public void onAnimationEnd(Animation animation) {
            MyListView1.this.clearAnimation();
            MyListView1.this.show_ad_image();
            MyListView1.this.listFocus();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    public MyListView1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        if (MGplayer.custom().equals("simba")) {
            LayoutInflater.from(context).inflate(C0216R.layout.listview_shadow, this, true);
        } else {
            LayoutInflater.from(context).inflate(C0216R.layout.listview1, this, true);
        }
        init();
    }

    public MyListView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        if (MGplayer.custom().equals("simba")) {
            LayoutInflater.from(context).inflate(C0216R.layout.listview_shadow, this, true);
        } else {
            LayoutInflater.from(context).inflate(C0216R.layout.listview1, this, true);
        }
        init();
    }

    public MyListView1(Context context) {
        super(context);
        this._this = context;
        if (!MGplayer.custom().equals("simba")) {
            LayoutInflater.from(context).inflate(C0216R.layout.listview1, this, true);
        }
        init();
    }

    private void init() {
        Typeface typeFace = MGplayer.getFontsType(this._this);
        float rate = MGplayer.getFontsRate();
        this.listview = (ListView) findViewById(C0216R.id.listview);
        this.listtext = (TextView) findViewById(C0216R.id.listtext);
        this.listdleft = (Button) findViewById(C0216R.id.dleft);
        this.listdright = (Button) findViewById(C0216R.id.dright);
        LinearLayout buttonLayout = (LinearLayout) findViewById(C0216R.id.buttonLayout);
        if (MGplayer.custom().equals("szysx") || MGplayer.custom().equals("dhtv") || MGplayer.custom().equals("xtvants") || MGplayer.custom().equals("goat")) {
            buttonLayout.setVisibility(0);
        }
        this.listview.setSelector(ContextCompat.getDrawable(this._this, C0216R.drawable.gradient9));
        this.listtext.setTextSize(8.0f * rate);
        this.listtext.setTypeface(typeFace);
        this.listtext.setOnClickListener(new C04111());
        this.listview.setOnItemClickListener(new C04122());
        this.listview.setOnItemSelectedListener(new C04133());
        this.listview.setOnItemLongClickListener(new C04144());
        this.listdleft.setTextSize(6.0f * rate);
        this.listdleft.setTypeface(typeFace);
        this.listdleft.setText(this._this.getString(C0216R.string.liveplayer_text4).toString());
        this.listdleft.setOnClickListener(new C04155());
        this.listdright.setTextSize(6.0f * rate);
        this.listdright.setTypeface(typeFace);
        this.listdright.setText(this._this.getString(C0216R.string.back2).toString());
        this.listdright.setOnClickListener(new C04166());
        TextView scroller = (TextView) findViewById(C0216R.id.scrolltext);
        if (MGplayer.custom().equals("quanxing")) {
            MGplayer.isShowLefttime = MGplayer.leftdaysshow;
        }
        String timeout = "";
        if (MGplayer.isShowLefttime == 1) {
            if (Integer.parseInt(MGplayer.leftdays) == -1) {
                timeout = timeout + "  " + this._this.getString(C0216R.string.myhomebar_text7).toString() + ":" + this._this.getString(C0216R.string.myhomebar_text9).toString();
            } else {
                timeout = timeout + "  " + this._this.getString(C0216R.string.myhomebar_text7).toString() + ":" + MGplayer.leftdays + this._this.getString(C0216R.string.myhomebar_text8).toString();
            }
        }
        if (MGplayer.showliveplaylistname != null) {
            timeout = timeout + "  " + this._this.getString(C0216R.string.liveplayer_text11).toString() + ":" + MGplayer.showliveplaylistname;
        }
        scroller.setText(timeout);
        scroller.setTextSize(MGplayer.getFontsRate() * 8.0f);
        scroller.setTypeface(MGplayer.getFontsType(this._this));
        set_init();
        bitmapcache_init();
    }

    public void set_ad_Image(ImageView ad) {
        this.ad_image = ad;
    }

    private void bitmapcache_init() {
        int size = LIVEplayer.urlSize();
        String cpuinfo = MGplayer.getCpuName();
        if (!cpuinfo.equals("A20") || (cpuinfo.equals("A20") && size < 100)) {
            this.bitmapCache = new LruBitmapCache();
        }
    }

    public void set_init() {
        if (MGplayer.custom().equals("simba")) {
            this.adapter = new MySimpleAdapterLiveListView(this._this, this.list, C0216R.layout.listitem_shadow, new String[]{"ItemView", "ItemId", "ItemTitle", "ItemView2"}, new int[]{C0216R.id.ItemView, C0216R.id.ItemId, C0216R.id.ItemTitle, C0216R.id.ItemView2});
        } else {
            this.adapter = new MySimpleAdapterLiveListView(this._this, this.list, C0216R.layout.listitem1, new String[]{"ItemView", "ItemId", "ItemTitle", "ItemView2"}, new int[]{C0216R.id.ItemView, C0216R.id.ItemId, C0216R.id.ItemTitle, C0216R.id.ItemView2});
        }
        this.listview.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C04177());
    }

    public void set_list(int tid, String type) {
        if (tid == 0) {
            this.listtext.setText(LIVEplayer.typeNameGetFromId(type));
            this.listview.setAdapter(this.adapter);
        } else {
            this.listtext.setText("Find");
            this.listview.setAdapter(this.adapter);
        }
        this.list.clear();
        String cpuinfo = MGplayer.getCpuName();
        int size = LIVEplayer.urlSize();
        for (int i = 0; i < size; i++) {
            int id = LIVEplayer.idGet(i);
            boolean ret = false;
            if (tid == 0) {
                ret = LIVEplayer.typeGet(i).indexOf(type) >= 0 && (LIVEplayer.typePasswordOK || !LIVEplayer.isVideoTypePs(id) || LIVEplayer.show_ps_playlist);
            } else if (tid == 1) {
                ret = LIVEplayer.nameGet(i).toLowerCase().indexOf(type.toLowerCase()) >= 0 && (LIVEplayer.typePasswordOK || !LIVEplayer.isVideoTypePs(id) || LIVEplayer.show_ps_playlist);
            }
            if (ret) {
                HashMap<String, Object> map = new HashMap();
                if (!cpuinfo.equals("A20") || (cpuinfo.equals("A20") && size < 100)) {
                    String imagePath = "";
                    if (MGplayer.custom().equals("quanxing")) {
                        imagePath = "/data/data/" + MGplayer.tv.getPackName() + "/temp/icon/" + LIVEplayer.imageGet(i);
                    } else {
                        imagePath = this._this.getFilesDir() + "/icon/" + LIVEplayer.imageGet(i);
                    }
                    if (MGplayer.fileIsExists(imagePath)) {
                        if (this.bitmapCache.getBitmap(imagePath) == null) {
                            Bitmap bit = BitmapFactory.decodeFile(imagePath);
                            if (MGplayer.custom().equals("badatv")) {
                                this.bitmapCache.putBitmap(imagePath, bit);
                            } else {
                                this.bitmapCache.putBitmap(imagePath, MGplayer.resizeImage(bit, (MGplayer.screenHeight / 12) - 10, (MGplayer.screenHeight / 12) - 10));
                            }
                        }
                        map.put("ItemView", this.bitmapCache.getBitmap(imagePath));
                    } else if (MGplayer.custom().equals("turbotv")) {
                        map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti_turbo));
                    } else {
                        map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti));
                    }
                }
                map.put("ItemTitle", LIVEplayer.nameGet(i));
                map.put("ItemId", String.format("%03d", new Object[]{Integer.valueOf(LIVEplayer.idGet(i))}));
                if (LIVEplayer.sourceGet(i).equals("hd")) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.h));
                } else if (LIVEplayer.sourceGet(i).equals(TtmlNode.TAG_P)) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.p));
                } else {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.s));
                }
                this.list.add(map);
            }
        }
        set_list_notifyDataSetChanged();
    }

    public void set_list() {
        this.listtext.setText(this._this.getString(C0216R.string.typelist_text1).toString());
        this.list.clear();
        int size = LIVEplayer.urlSize();
        String cpuinfo = MGplayer.getCpuName();
        MGplayer.MyPrintln("set_list size = " + size);
        for (int i = 0; i < size; i++) {
            int id = LIVEplayer.idGet(i);
            if (LIVEplayer.typePasswordOK || !LIVEplayer.isVideoTypePs(id) || LIVEplayer.show_ps_playlist) {
                HashMap<String, Object> map = new HashMap();
                if (!cpuinfo.equals("A20") || (cpuinfo.equals("A20") && size < 100)) {
                    String imagePath = "";
                    if (MGplayer.custom().equals("quanxing")) {
                        imagePath = "/data/data/" + MGplayer.tv.getPackName() + "/temp/icon/" + LIVEplayer.imageGet(i);
                    } else {
                        imagePath = this._this.getFilesDir() + "/icon/" + LIVEplayer.imageGet(i);
                    }
                    if (MGplayer.fileIsExists(imagePath)) {
                        if (this.bitmapCache.getBitmap(imagePath) == null) {
                            Bitmap bit = BitmapFactory.decodeFile(imagePath);
                            if (MGplayer.custom().equals("badatv")) {
                                this.bitmapCache.putBitmap(imagePath, bit);
                            } else {
                                this.bitmapCache.putBitmap(imagePath, MGplayer.resizeImage(bit, (MGplayer.screenHeight / 12) - 10, (MGplayer.screenHeight / 12) - 10));
                            }
                        }
                        map.put("ItemView", this.bitmapCache.getBitmap(imagePath));
                    } else if (MGplayer.custom().equals("turbotv")) {
                        map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti_turbo));
                    } else {
                        map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti));
                    }
                }
                map.put("ItemTitle", LIVEplayer.nameGet(i));
                map.put("ItemId", String.format("%03d", new Object[]{Integer.valueOf(id)}));
                if (LIVEplayer.sourceGet(i).equals("hd")) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.h));
                } else if (LIVEplayer.sourceGet(i).equals(TtmlNode.TAG_P)) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.p));
                } else {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.s));
                }
                this.list.add(map);
            }
        }
        set_list_notifyDataSetChanged();
    }

    public void collect_list() {
        this.listtext.setText(this._this.getString(C0216R.string.typelist_text4).toString());
        ArrayList<UrlStatus> status = new MyLiveCollectView().parseAll(this._this);
        int size = status.size();
        this.listview.setAdapter(this.adapter);
        this.list.clear();
        for (int i = 0; i < size; i++) {
            int id = ((UrlStatus) status.get(i)).id;
            if (LIVEplayer.typePasswordOK || !LIVEplayer.isVideoTypePs(id)) {
                HashMap<String, Object> map = new HashMap();
                if (((UrlStatus) status.get(i)).imagebit != null) {
                    map.put("ItemView", ((UrlStatus) status.get(i)).imagebit);
                } else if (MGplayer.custom().equals("turbotv")) {
                    map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti_turbo));
                } else {
                    map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti));
                }
                map.put("ItemTitle", ((UrlStatus) status.get(i)).name);
                map.put("ItemId", String.format("%03d", new Object[]{Integer.valueOf(id)}));
                map.put("ItemUrl", ((UrlStatus) status.get(i)).url);
                map.put("ItemPassword", ((UrlStatus) status.get(i)).password);
                if (((UrlStatus) status.get(i)).source.equals("hd")) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.h));
                } else if (LIVEplayer.sourceGet(i).equals(TtmlNode.TAG_P)) {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.p));
                } else {
                    map.put("ItemView2", Integer.valueOf(C0216R.mipmap.s));
                }
                this.list.add(map);
            }
        }
        set_list_notifyDataSetChanged();
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
            this.listview.setSelection(num);
        }
    }

    public void setSelection(int index) {
        this.listview.setSelection(index);
    }

    public ListView getListView() {
        return this.listview;
    }

    public void showPlayList(String type) {
        showPlayList(0, type, false);
    }

    public void showPlayList(int tid, String type, boolean check_type_selection) {
        if (MGplayer.isNumeric(LIVEplayer.getCurrentID()) && check_type_selection && !LIVEplayer.getVideoType(Integer.parseInt(LIVEplayer.getCurrentID())).contains(type)) {
            showPlayList(0);
            return;
        }
        LIVEplayer.currentType = type;
        currentID = LIVEplayer.currentID;
        if (this.current_type == null || !(type == null || type.equals(this.current_type))) {
            set_list(tid, type);
        }
        this.current_type = type;
        showViewTimeout();
        if (!isShown()) {
            this.isShow = true;
            listFocus();
            if (MGplayer.isNumeric(LIVEplayer.getCurrentID())) {
                int i;
                if (LIVEplayer.show_ps_playlist) {
                    i = LIVEplayer.getVideoNum(Integer.parseInt(LIVEplayer.getCurrentID()), type, false);
                    MGplayer.MyPrintln("show playlist type = " + type + " LIVEplayer.getCurrentID():" + LIVEplayer.getCurrentID() + " Selection:" + i);
                } else {
                    boolean z;
                    int parseInt = Integer.parseInt(LIVEplayer.getCurrentID());
                    if (LIVEplayer.typePasswordOK) {
                        z = false;
                    } else {
                        z = true;
                    }
                    i = LIVEplayer.getVideoNum(parseInt, type, z);
                    MGplayer.MyPrintln("no show playlist type = " + type + " LIVEplayer.getCurrentID():" + LIVEplayer.getCurrentID() + " Selection:" + i);
                }
                setSelection(i);
            } else {
                setSelection(0);
            }
            setVisibility(0);
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                show_ad_image();
                listFocus();
                setFocusable(true);
                return;
            }
            TranslateAnimation animation = new TranslateAnimation((float) ((-MGplayer.screenWidth) / 3), 0.0f, 0.0f, 0.0f);
            animation.setDuration(300);
            animation.setAnimationListener(new C04188());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void showPlayList(int type) {
        boolean z = false;
        LIVEplayer.currentType = String.valueOf(type);
        currentID = LIVEplayer.currentID;
        if (this.current_type == null || !String.valueOf(type).equals(this.current_type)) {
            if (type == 1) {
                collect_list();
            } else {
                set_list();
            }
        }
        this.current_type = String.valueOf(type);
        showViewTimeout();
        if (!isShown()) {
            this.isShow = true;
            listFocus();
            setVisibility(0);
            if (MGplayer.isNumeric(LIVEplayer.getCurrentID()) && type == 0) {
                int num;
                if (LIVEplayer.show_ps_playlist) {
                    num = LIVEplayer.getVideoNum(Integer.parseInt(LIVEplayer.getCurrentID()), false);
                } else {
                    int parseInt = Integer.parseInt(LIVEplayer.getCurrentID());
                    if (!LIVEplayer.typePasswordOK) {
                        z = true;
                    }
                    num = LIVEplayer.getVideoNum(parseInt, z);
                }
                final int i = num;
                MGplayer.MyPrintln("1 LIVEplayer.getCurrentID():" + LIVEplayer.getCurrentID() + "setSelection:" + i);
                this.listview.post(new Runnable() {
                    public void run() {
                        MyListView1.this.listview.requestFocusFromTouch();
                        MyListView1.this.listview.setSelection(i);
                    }
                });
            } else if (MGplayer.isNumeric(LIVEplayer.getCurrentID()) && type == 1) {
                setSelection(0);
            }
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                show_ad_image();
                listFocus();
                setFocusable(true);
                return;
            }
            TranslateAnimation animation = new TranslateAnimation((float) ((-MGplayer.screenWidth) / 3), 0.0f, 0.0f, 0.0f);
            animation.setDuration(300);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    MyListView1.this.clearAnimation();
                    MyListView1.this.show_ad_image();
                    MyListView1.this.listFocus();
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void hidePlayList() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        if (isShown()) {
            this.isShow = false;
            setCurrentID(LIVEplayer.getCurrentID());
            hide_ad_image();
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setVisibility(8);
                hide_ad_image();
                return;
            }
            TranslateAnimation animation = new TranslateAnimation(0.0f, (float) ((-MGplayer.screenWidth) / 3), 0.0f, 0.0f);
            animation.setDuration(500);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    MyListView1.this.setVisibility(8);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void show_ad_image() {
        if (LIVEplayer.adimageSize() > 0) {
            this.ad_image.setImageBitmap(BitmapFactory.decodeFile(this._this.getFilesDir() + "/ad/" + LIVEplayer.adimageGet((int) (Math.random() * ((double) LIVEplayer.adimageSize())))));
            this.ad_image.setVisibility(0);
        }
    }

    public void hide_ad_image() {
        if (LIVEplayer.adimageSize() > 0) {
            this.ad_image.setVisibility(8);
        }
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
        this.listview.setFocusable(true);
        this.listview.setFocusableInTouchMode(true);
        this.listview.requestFocus();
        this.listview.requestFocusFromTouch();
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

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        MGplayer.MyPrintln("listview onKeyLongPress ");
        return super.onKeyLongPress(keyCode, event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            int pos;
            int end;
            int first;
            switch (event.getKeyCode()) {
                case 19:
                    pos = this.listview.getSelectedItemPosition();
                    end = this.listview.getCount() - 1;
                    first = this.listview.getFirstVisiblePosition();
                    MGplayer.MyPrintln("livelist up " + first + " " + end + " " + pos);
                    if (pos <= first || pos > end) {
                        if (pos < 0) {
                            pos = end;
                        } else {
                            pos--;
                        }
                        if (pos < 0) {
                            pos = end;
                        }
                        MGplayer.MyPrintln("list pos " + pos);
                        this.listview.setSelection(pos);
                        listFocus();
                        return true;
                    }
                    MGplayer.MyPrintln("list dispatchKeyEvent");
                    super.dispatchKeyEvent(event);
                    listFocus();
                    return true;
                case 20:
                    pos = this.listview.getSelectedItemPosition();
                    end = this.listview.getCount() - 1;
                    first = this.listview.getFirstVisiblePosition();
                    MGplayer.MyPrintln("livelist down " + first + " " + end + " " + pos);
                    if (pos < first || pos >= end) {
                        if (pos > end) {
                            pos = 0;
                        } else {
                            pos++;
                        }
                        if (pos > end) {
                            pos = 0;
                        }
                        MGplayer.MyPrintln("list pos " + pos);
                        this.listview.setSelection(pos);
                        listFocus();
                        return true;
                    }
                    MGplayer.MyPrintln("list dispatchKeyEvent");
                    super.dispatchKeyEvent(event);
                    listFocus();
                    return true;
                case 24:
                    if (MGplayer.custom().equals("simba") && isShown()) {
                        pos = this.listview.getSelectedItemPosition();
                        end = this.listview.getCount() - 1;
                        first = this.listview.getFirstVisiblePosition();
                        MGplayer.MyPrintln("livelist down " + first + " " + end + " " + pos);
                        if (pos < first || pos >= end) {
                            if (pos > end) {
                                pos = 0;
                            } else {
                                pos++;
                            }
                            if (pos > end) {
                                pos = 0;
                            }
                            MGplayer.MyPrintln("list pos " + pos);
                            this.listview.setSelection(pos);
                            listFocus();
                            return true;
                        }
                        MGplayer.MyPrintln("list dispatchKeyEvent");
                        this.listview.setSelection(pos + 1);
                        listFocus();
                        return true;
                    }
                    break;
                case 25:
                    if (MGplayer.custom().equals("simba") && isShown()) {
                        pos = this.listview.getSelectedItemPosition();
                        end = this.listview.getCount() - 1;
                        first = this.listview.getFirstVisiblePosition();
                        MGplayer.MyPrintln("livelist up " + first + " " + end + " " + pos);
                        if (pos <= first || pos > end) {
                            if (pos < 0) {
                                pos = end;
                            } else {
                                pos--;
                            }
                            if (pos < 0) {
                                pos = end;
                            }
                            MGplayer.MyPrintln("list pos " + pos);
                            this.listview.setSelection(pos);
                            listFocus();
                            return true;
                        }
                        MGplayer.MyPrintln("list dispatchKeyEvent");
                        this.listview.setSelection(pos - 1);
                        listFocus();
                        return true;
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
