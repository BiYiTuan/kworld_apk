package com.gemini.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import cz.msebera.android.httpclient.protocol.HttpRequestExecutor;
import java.util.ArrayList;
import java.util.HashMap;
import net.lingala.zip4j.util.InternalZipConstants;

public class VodPlayerList2Activity extends Activity implements OnGestureListener {
    private static AlertDialog dialog = null;
    private static boolean list_type_getfocus = false;
    private MySimpleAdapterVodView adapter;
    private LruBitmapCache bitmapCache;
    private MyVodClassify2View classifyview;
    private int collectlist = 0;
    private CollectVodDB collectvoddber;
    private int colnumNum = 5;
    private GestureDetector detector = null;
    private boolean enable_focus = true;
    private AlertDialog findDialog = null;
    private MyVodFindView findview = null;
    private HistoryVodDB historyvoddber = null;
    private boolean isexit = false;
    private int lineNum = 2;
    private MyVodTypeView3 listtypeview;
    private MyGridView mVodGrid = null;
    public ListViewInterface onClassifyPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VodPlayerList2Activity.this.listtypeFocus();
                    VodPlayerList2Activity.this.enable_focus = false;
                    return;
                case 1:
                    VODplayer.list.clear();
                    VODplayer.url_param = "&itype=" + data + "&iyear=0&iarea=0";
                    VodPlayerList2Activity.this.cmdMessageAddList(VODplayer.type, 0, 2);
                    return;
                case 2:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    VodPlayerList2Activity.this.cmdMessageAddList(VODplayer.type, 0, 2);
                    VodPlayerList2Activity.this.classifyview.hideClassifyList();
                    return;
                default:
                    return;
            }
        }
    };
    public ControlVideoInterface onControlVideo = new C09139();
    public ListViewInterface onFindPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    MGplayer.MyPrintln("onFindPressed:" + VODplayer.url_param);
                    VodPlayerList2Activity.this.openListActivity("find");
                    return;
                case 1:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    return;
                case 2:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onTopPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VodPlayerList2Activity.this.isexit = false;
                    VodPlayerList2Activity.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onTypePressed = new ListViewInterface() {

        /* renamed from: com.gemini.play.VodPlayerList2Activity$11$1 */
        class C06151 implements OnClickListener {
            C06151() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                VODplayer.listClear();
                if (VodPlayerList2Activity.this.collectvoddber == null) {
                    VodPlayerList2Activity.this.collectvoddber = new CollectVodDB(VodPlayerList2Activity.this);
                }
                VodPlayerList2Activity.this.collectvoddber.clear();
                VodPlayerList2Activity.this.isexit = false;
                VodPlayerList2Activity.this.finish();
            }
        }

        /* renamed from: com.gemini.play.VodPlayerList2Activity$11$2 */
        class C06162 implements OnClickListener {
            C06162() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        /* renamed from: com.gemini.play.VodPlayerList2Activity$11$3 */
        class C06173 implements OnClickListener {
            C06173() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                VODplayer.listClear();
                if (VodPlayerList2Activity.this.historyvoddber == null) {
                    VodPlayerList2Activity.this.historyvoddber = new HistoryVodDB(VodPlayerList2Activity.this);
                }
                VodPlayerList2Activity.this.historyvoddber.clear();
                VodPlayerList2Activity.this.isexit = false;
                VodPlayerList2Activity.this.finish();
            }
        }

        /* renamed from: com.gemini.play.VodPlayerList2Activity$11$4 */
        class C06184 implements OnClickListener {
            C06184() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    Builder builder;
                    if (data.equals("collectclear")) {
                        builder = new Builder(VodPlayerList2Activity.this);
                        builder.setMessage(VodPlayerList2Activity.this.getString(C0216R.string.vodhistoryview_text1).toString());
                        builder.setPositiveButton(VodPlayerList2Activity.this.getString(C0216R.string.ok).toString(), new C06151());
                        builder.setNegativeButton(VodPlayerList2Activity.this.getString(C0216R.string.cancel).toString(), new C06162());
                        builder.create().show();
                        return;
                    } else if (data.equals("historyclear")) {
                        builder = new Builder(VodPlayerList2Activity.this);
                        builder.setMessage(VodPlayerList2Activity.this.getString(C0216R.string.vodhistoryview_text1).toString());
                        builder.setPositiveButton(VodPlayerList2Activity.this.getString(C0216R.string.ok).toString(), new C06173());
                        builder.setNegativeButton(VodPlayerList2Activity.this.getString(C0216R.string.cancel).toString(), new C06184());
                        builder.create().show();
                        return;
                    } else if (VODplayer.type.equals("find")) {
                        VODplayer.findtype = "&findtype=" + data;
                        VODplayer.listClear();
                        VODplayer.page = 0;
                        if (VODplayer.columner[Integer.parseInt(data)].needps == 1) {
                            VodPlayerList2Activity.this.inputPasswordView(Integer.parseInt(data), 2);
                            return;
                        } else {
                            VodPlayerList2Activity.this.cmdMessageAddVod(2);
                            return;
                        }
                    } else if (!VODplayer.type.equals("find") && !VODplayer.type.equals("history") && !VODplayer.type.equals("collect") && !VODplayer.type.equals("hot")) {
                        if (data.equals("-1")) {
                            VodPlayerList2Activity.this.findActivity(VodPlayerList2Activity.this);
                            VODplayer.findtype = "&findtype=" + VODplayer.type;
                        } else if (data.equals("000") || data.equals("0")) {
                            VODplayer.url_param = "";
                            VODplayer.findtype = "&findtype=0";
                        } else {
                            VODplayer.url_param = "&itype=" + data + "&iyear=0&iarea=0";
                            VODplayer.findtype = "&findtype=0";
                        }
                        VodPlayerList2Activity.this.collectlist = 0;
                        VODplayer.listClear();
                        VODplayer.page = 0;
                        VodPlayerList2Activity.this.cmdMessageAddVod(2);
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    public Handler rHandler = new C06286();
    private float rate;
    private int selectItemIndex;
    private MyVodTop3View topview = null;
    private Typeface typeFace;
    private ProgressBar vodProgressBar = null;

    /* renamed from: com.gemini.play.VodPlayerList2Activity$1 */
    class C06191 implements OnScrollListener {
        boolean isLastRow = false;

        C06191() {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            VodPlayerList2Activity.this.set_top_text();
            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                this.isLastRow = true;
            }
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            VodPlayerList2Activity.this.set_top_text();
            if (this.isLastRow && scrollState == 0) {
                MGplayer.MyPrintln("load data isLastRow");
                VodPlayerList2Activity.this.cmdMessageAddList(VODplayer.type, VODplayer.list.size() / VODplayer.psize, 2);
                this.isLastRow = false;
            }
        }
    }

    /* renamed from: com.gemini.play.VodPlayerList2Activity$2 */
    class C06202 implements OnFocusChangeListener {
        C06202() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                VodPlayerList2Activity.this.adapter.setSeclection(0);
                VodPlayerList2Activity.this.adapter.notifyDataSetChanged();
                return;
            }
            MGplayer.MyPrintln("mVodGrid onfocus");
            VodPlayerList2Activity.this.adapter.setSeclection(-1);
            VodPlayerList2Activity.this.adapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.gemini.play.VodPlayerList2Activity$3 */
    class C06213 implements OnItemClickListener {
        C06213() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            HashMap<String, String> map = (HashMap) VodPlayerList2Activity.this.mVodGrid.getItemAtPosition(arg2);
            String id = (String) map.get("ItemId");
            String type = (String) map.get("ItemType");
            MGplayer.MyPrintln("ItemId:" + id + " ItemType:" + type);
            VodPlayerList2Activity.this.adapter.setSeclection(arg2);
            VodPlayerList2Activity.this.adapter.notifyDataSetChanged();
            VodPlayerList2Activity.this.openInfoActivity(id, String.valueOf(type));
        }
    }

    /* renamed from: com.gemini.play.VodPlayerList2Activity$4 */
    class C06224 implements OnItemSelectedListener {
        C06224() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MGplayer.MyPrintln("onItemSelected:" + arg2);
            VodPlayerList2Activity.this.selectItemIndex = arg2;
            if (arg1 != null && VodPlayerList2Activity.this.mVodGrid.isFocusable()) {
                if (arg2 >= VODplayer.list.size() - VodPlayerList2Activity.this.colnumNum && arg2 >= 5) {
                    VodPlayerList2Activity.this.cmdMessageAddList(VODplayer.type, VODplayer.list.size() / VODplayer.psize, 2);
                }
                VodPlayerList2Activity.this.adapter.setSeclection(arg2);
                VodPlayerList2Activity.this.adapter.notifyDataSetChanged();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.VodPlayerList2Activity$5 */
    class C06235 implements OnItemLongClickListener {
        C06235() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (VodPlayerList2Activity.this.collectlist == 1) {
                String id = (String) ((HashMap) VodPlayerList2Activity.this.mVodGrid.getItemAtPosition(arg2)).get("ItemId");
                MGplayer.MyPrintln("setOnItemLongClickListener:" + id);
                if (MGplayer.isNumeric(id)) {
                    VodPlayerList2Activity.this.delCollect(Integer.parseInt(id));
                }
            }
            return true;
        }
    }

    /* renamed from: com.gemini.play.VodPlayerList2Activity$6 */
    class C06286 extends Handler {

        /* renamed from: com.gemini.play.VodPlayerList2Activity$6$3 */
        class C06263 extends Thread {
            C06263() {
            }

            public void run() {
                ArrayList<VodListStatus> xlist = VodPlayerList2Activity.this.collectvoddber.parseAll();
                if (xlist != null) {
                    for (int ii = 0; ii < xlist.size(); ii++) {
                        String url;
                        VodListStatus s = (VodListStatus) xlist.get(ii);
                        HashMap<String, Object> map = new HashMap();
                        map.put("ItemId", String.valueOf(s.id));
                        if (s.imagebit != null) {
                            map.put("ItemImageBit", s.imagebit);
                        }
                        map.put("ItemInfo", Integer.valueOf(s.clickrate));
                        map.put("ItemTitle", s.name);
                        map.put("ItemType", String.valueOf(s.infotype));
                        map.put("ItemHaveLoad", Integer.valueOf(0));
                        if (s.image == null || !s.image.startsWith("http://")) {
                            url = VODplayer.gete() + "/images/vodpic/" + s.image;
                        } else {
                            url = s.image;
                        }
                        MGplayer.MyPrintln("collect image url:" + url);
                        map.put("ItemView", url);
                        map.put("ItemImageUrl", url);
                        VODplayer.list.add(map);
                    }
                    VodPlayerList2Activity.this.cmdMessageAddVod();
                }
            }
        }

        /* renamed from: com.gemini.play.VodPlayerList2Activity$6$4 */
        class C06274 extends Thread {
            C06274() {
            }

            public void run() {
                ArrayList<VodHistoryStatus> xlist = VodPlayerList2Activity.this.historyvoddber.parseAll();
                if (xlist != null) {
                    for (int ii = 0; ii < xlist.size(); ii++) {
                        String url;
                        VodHistoryStatus s = (VodHistoryStatus) xlist.get(ii);
                        HashMap<String, Object> map = new HashMap();
                        map.put("ItemId", String.valueOf(s.id));
                        if (s.imagebit != null) {
                            map.put("ItemImageBit", s.imagebit);
                        }
                        map.put("ItemInfo", Integer.valueOf(s.clickrate));
                        map.put("ItemTitle", s.name);
                        map.put("ItemType", String.valueOf(s.infotype));
                        map.put("ItemHaveLoad", Integer.valueOf(0));
                        if (s.image == null || !s.image.startsWith("http://")) {
                            url = VODplayer.gete() + "/images/vodpic/" + s.image;
                        } else {
                            url = s.image;
                        }
                        MGplayer.MyPrintln("history image url:" + url);
                        map.put("ItemView", url);
                        map.put("ItemImageUrl", url);
                        VODplayer.list.add(map);
                    }
                    VodPlayerList2Activity.this.cmdMessageAddVod();
                }
            }
        }

        C06286() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    final String id0 = msg.getData().getString("data");
                    VODplayer.type = id0;
                    if (!(VODplayer.type.equals("history") || VODplayer.type.equals("hot") || VODplayer.type.equals("collect"))) {
                        VODplayer.url_param = null;
                    }
                    new Thread() {
                        public void run() {
                            VODplayer.parseTypeXML(id0);
                        }
                    }.start();
                    break;
                case 1:
                    VodPlayerList2Activity.this.adapter.notifyDataSetChanged();
                    VodPlayerList2Activity.this.vodProgressBar.setVisibility(8);
                    if (VodPlayerList2Activity.list_type_getfocus) {
                        VodPlayerList2Activity.list_type_getfocus = false;
                        VodPlayerList2Activity.this.listtypeFocus();
                        VodPlayerList2Activity.this.enable_focus = false;
                    }
                    VodPlayerList2Activity.this.set_top_text();
                    return;
                case 2:
                    break;
                case 3:
                    new C06263().start();
                    return;
                case 4:
                    VodPlayerList2Activity.this.mVodGrid.setFocusable(false);
                    VodPlayerList2Activity.this.listtypeview.setFocusable(false);
                    VodPlayerList2Activity.this.findview.showFindView(VODplayer.type);
                    return;
                case 5:
                    VodPlayerList2Activity.this.vodProgressBar.setVisibility(8);
                    return;
                case 6:
                    VodPlayerList2Activity.this.adapter.notifyDataSetChanged();
                    return;
                case 7:
                    new C06274().start();
                    return;
                default:
                    return;
            }
            VodPlayerList2Activity.this.vodProgressBar.setVisibility(0);
            final String id = VODplayer.type;
            int page = msg.getData().getInt("page");
            String url_tmp = VODplayer.gete() + "/vod_xml.php?type=" + id + "&page=" + page + MGplayer.get_key_value();
            MGplayer.MyPrintln("url_tmp:" + url_tmp);
            MGplayer.MyPrintln("url_param:" + VODplayer.url_param);
            if (!(VODplayer.url_param == null || VODplayer.url_param.equals("null"))) {
                url_tmp = url_tmp + VODplayer.url_param;
            }
            if (VODplayer.type.equals("find")) {
                url_tmp = url_tmp + VODplayer.findtype;
            }
            final String url = url_tmp;
            MGplayer.MyPrintln("page:" + page + " VODplayer.page:" + VODplayer.page);
            MGplayer.MyPrintln("parseXML:" + url);
            if (page - 1 >= VODplayer.page) {
                VodPlayerList2Activity.this.cmdMessageHideProgress();
                return;
            }
            Toast.makeText(VodPlayerList2Activity.this, VodPlayerList2Activity.this.getString(C0216R.string.vodlist_text7).toString(), 0).show();
            new Thread() {
                public void run() {
                    ArrayList<VodListStatus> xlist = null;
                    String type_tmp = VODplayer.type;
                    int ii = 0;
                    while (ii < 50) {
                        if (type_tmp == VODplayer.type && !VodPlayerList2Activity.this.isDestroyed() && !VodPlayerList2Activity.this.isFinishing()) {
                            xlist = VODplayer.parseXML(id, url);
                            if (xlist != null) {
                                break;
                            }
                            MGplayer.MyPrintln("xlist:NULL");
                            MGplayer.sleep(HttpRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE);
                            ii++;
                        } else {
                            return;
                        }
                    }
                    VodPlayerList2Activity.this.cmdMessageHideProgress();
                    if (xlist != null) {
                        for (ii = 0; ii < xlist.size(); ii++) {
                            VodListStatus s = (VodListStatus) xlist.get(ii);
                            boolean checkok = false;
                            for (int kk = 0; kk < VODplayer.list.size(); kk++) {
                                if (Integer.parseInt(((HashMap) VODplayer.list.get(kk)).get("ItemId").toString()) == s.id) {
                                    checkok = true;
                                    MGplayer.MyPrintln("checkok = true");
                                    break;
                                }
                            }
                            if (!checkok) {
                                String url;
                                HashMap<String, Object> map = new HashMap();
                                if (s.image == null || !s.image.startsWith("http://")) {
                                    url = VODplayer.gete() + "/images/vodpic/" + s.image;
                                } else {
                                    url = s.image;
                                }
                                map.put("ItemView", url);
                                map.put("ItemId", String.valueOf(s.id));
                                map.put("ItemInfo", Integer.valueOf(s.clickrate));
                                map.put("ItemTitle", s.name);
                                map.put("ItemHaveLoad", Integer.valueOf(0));
                                if (VODplayer.type.equals("history") || VODplayer.type.equals("find") || VODplayer.type.equals("collect") || VODplayer.type.equals("hot")) {
                                    map.put("ItemType", "0");
                                } else {
                                    map.put("ItemType", VODplayer.type);
                                }
                                map.put("ItemImageUrl", url);
                                VODplayer.ptotal = s.total;
                                VODplayer.list.add(map);
                            }
                        }
                        if (xlist.size() > 0) {
                            VODplayer.page++;
                        }
                        VodPlayerList2Activity.this.cmdMessageAddVod();
                    }
                }
            }.start();
        }
    }

    /* renamed from: com.gemini.play.VodPlayerList2Activity$7 */
    class C06297 implements ViewBinder {
        C06297() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) != 0) {
                ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                return true;
            } else if (((arg0 instanceof ImageView) & (arg1 instanceof String)) == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* renamed from: com.gemini.play.VodPlayerList2Activity$9 */
    class C09139 implements ControlVideoInterface {
        C09139() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(VodPlayerList2Activity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    VodPlayerList2Activity.this.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.vodplayerlist2);
        getWindow().setFlags(1024, 1024);
        VODplayer.type = getIntent().getStringExtra("type");
        int findtype = getIntent().getIntExtra("findtype", -1);
        this.detector = new GestureDetector(this, this);
        this.findview = (MyVodFindView) findViewById(C0216R.id.findview);
        this.collectvoddber = new CollectVodDB(this);
        this.topview = (MyVodTop3View) findViewById(C0216R.id.myvodtopview);
        this.mVodGrid = (MyGridView) findViewById(C0216R.id.vodgrid);
        this.mVodGrid.setSelector(new ColorDrawable(0));
        this.topview.setInterface(this.onTopPressed);
        this.classifyview = (MyVodClassify2View) findViewById(C0216R.id.myvodclassifyview);
        this.classifyview.setInterface(this.onClassifyPressed);
        this.vodProgressBar = (ProgressBar) findViewById(C0216R.id.progressBar);
        LayoutParams progressBarlayoutParams = (LayoutParams) this.vodProgressBar.getLayoutParams();
        progressBarlayoutParams.width = MGplayer.screenWidth / 6;
        progressBarlayoutParams.height = MGplayer.screenHeight / 6;
        this.vodProgressBar.setLayoutParams(progressBarlayoutParams);
        this.historyvoddber = new HistoryVodDB(this);
        this.mVodGrid.setOnScrollListener(new C06191());
        this.mVodGrid.setOnFocusChangeListener(new C06202());
        this.mVodGrid.setOnItemClickListener(new C06213());
        this.mVodGrid.setOnItemSelectedListener(new C06224());
        this.mVodGrid.setOnItemLongClickListener(new C06235());
        init_ui();
        set_list();
        this.listtypeview = (MyVodTypeView3) findViewById(C0216R.id.myvodtypeview);
        this.listtypeview.setInterface(this.onTypePressed);
        if (MGplayer.isNumeric(VODplayer.type)) {
            this.listtypeview.set_init(Integer.parseInt(VODplayer.type));
        } else if (VODplayer.type.equals("find")) {
            this.listtypeview.set_init(4, findtype);
        } else if (VODplayer.type.equals("hot")) {
            this.listtypeview.set_init(5);
        } else if (VODplayer.type.equals("collect")) {
            this.listtypeview.set_init(6);
        } else if (VODplayer.type.equals("history")) {
            this.listtypeview.set_init(7);
        }
        MGplayer.video_every_interface(this.onControlVideo);
        this.bitmapCache = new LruBitmapCache();
        listtypeFocus();
        list_type_getfocus = true;
        if (findtype < 0) {
            VODplayer.findtype = "&findtype=0";
        } else {
            VODplayer.findtype = "&findtype=" + findtype;
        }
        if (VODplayer.type.equals("history")) {
            this.collectlist = 2;
            VODplayer.list.clear();
            cmdMessageAddList(null, 7);
        } else if (VODplayer.type.equals("collect")) {
            this.collectlist = 1;
            VODplayer.list.clear();
            cmdMessageAddList(null, 3);
        } else if (VODplayer.type.equals("find")) {
            this.collectlist = 0;
            VODplayer.listClear();
            VODplayer.page = 0;
            VODplayer.list.clear();
            if (VODplayer.columner[0].needps == 1) {
                inputPasswordView(0, 2);
            } else {
                cmdMessageAddList(VODplayer.type, 2);
            }
        } else {
            VODplayer.url_param = "&itype=001&iyear=0&iarea=0";
            this.collectlist = 0;
            VODplayer.listClear();
            VODplayer.page = 0;
            VODplayer.list.clear();
            cmdMessageAddList(VODplayer.type, 0);
        }
        if (MGplayer.isNumeric(VODplayer.type)) {
            this.topview.set_vod_top_center_text(get_top_title(Integer.parseInt(VODplayer.type), -1));
        } else if (VODplayer.type.equals("find")) {
            this.topview.set_vod_top_center_text(getString(C0216R.string.vodplayermain_text1));
        } else if (VODplayer.type.equals("hot")) {
            this.topview.set_vod_top_center_text(getString(C0216R.string.vodplayermain_text2));
        } else if (VODplayer.type.equals("collect")) {
            this.topview.set_vod_top_center_text(getString(C0216R.string.vodplayermain_text3));
        } else if (VODplayer.type.equals("history")) {
            this.topview.set_vod_top_center_text(getString(C0216R.string.vodplayermain_text5));
        }
        for (int ii = 0; ii < VODplayer.columner.length; ii++) {
            VODplayer.parseTypeMem(String.valueOf(ii));
        }
    }

    public void init_ui() {
        this.typeFace = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        this.rate = MGplayer.getFontsRate();
    }

    public void cmdMessageAddList(String id) {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        Bundle data = new Bundle();
        data.putString("data", id);
        Message msg = new Message();
        msg.setData(data);
        msg.what = 0;
        this.rHandler.sendMessageDelayed(msg, 500);
    }

    public void cmdMessageAddList(String id, int what) {
        if (this.rHandler.hasMessages(what)) {
            this.rHandler.removeMessages(what);
        }
        Bundle data = new Bundle();
        data.putString("data", id);
        Message msg = new Message();
        msg.setData(data);
        msg.what = what;
        this.rHandler.sendMessageDelayed(msg, 500);
    }

    public void cmdMessageAddList(String id, int page, int what) {
        if (this.rHandler.hasMessages(what)) {
            this.rHandler.removeMessages(what);
        }
        Bundle data = new Bundle();
        data.putString("data", id);
        data.putInt("page", page);
        Message msg = new Message();
        msg.setData(data);
        msg.what = what;
        this.rHandler.sendMessageDelayed(msg, 500);
    }

    public void cmdMessageAddVod() {
        Message msg = new Message();
        msg.what = 1;
        this.rHandler.sendMessage(msg);
    }

    public void cmdMessageAddVod(int what) {
        Message msg = new Message();
        msg.what = what;
        this.rHandler.sendMessage(msg);
    }

    public void cmdMessageHideProgress() {
        if (this.rHandler.hasMessages(5)) {
            this.rHandler.removeMessages(5);
        }
        Message msg = new Message();
        msg.what = 5;
        this.rHandler.sendMessage(msg);
    }

    public void set_collect() {
    }

    public void set_top_text() {
        if (!MGplayer.isNumeric(VODplayer.type)) {
            return;
        }
        int pages;
        if (this.collectlist == 1) {
            int size = this.collectvoddber.parseSize();
            pages = size / (this.lineNum * this.colnumNum);
            if (size % (this.lineNum * this.colnumNum) > 0) {
                pages++;
            }
            if (pages == 0) {
                pages = 1;
            }
            this.topview.set_vod_top_text((((this.mVodGrid.getFirstVisiblePosition() + 5) / (this.lineNum * this.colnumNum)) + 1) + InternalZipConstants.ZIP_FILE_SEPARATOR + pages);
            return;
        }
        if (VODplayer.ptotal == 0) {
            pages = VODplayer.vod_number[Integer.parseInt(VODplayer.type)] / (this.lineNum * this.colnumNum);
            if (VODplayer.vod_number[Integer.parseInt(VODplayer.type)] % (this.lineNum * this.colnumNum) > 0) {
                pages++;
            }
        } else {
            pages = VODplayer.ptotal / (this.lineNum * this.colnumNum);
            if (VODplayer.ptotal % (this.lineNum * this.colnumNum) > 0) {
                pages++;
            }
        }
        if (pages == 0) {
            pages = 1;
        }
        this.topview.set_vod_top_text((((this.mVodGrid.getFirstVisiblePosition() + 5) / (this.lineNum * this.colnumNum)) + 1) + InternalZipConstants.ZIP_FILE_SEPARATOR + pages);
    }

    public String get_top_title(int type, int classify) {
        String text = "";
        if (VODplayer.columner == null || VODplayer.columner.length < 1 || VODplayer.columner[type].name == null) {
            switch (type) {
                case 0:
                    text = getString(C0216R.string.vodlist_text2).toString();
                    break;
                case 1:
                    text = getString(C0216R.string.vodlist_text3).toString();
                    break;
                case 2:
                    text = getString(C0216R.string.vodlist_text4).toString();
                    break;
                case 3:
                    text = getString(C0216R.string.vodlist_text5).toString();
                    break;
                default:
                    break;
            }
        }
        text = VODplayer.columner[type].name;
        String[] type_names = null;
        if (VODplayer.columner[type].type_type != null && VODplayer.columner[type].type_type.length() > 1) {
            type_names = VODplayer.columner[type].type_type.split("\\|");
        }
        if (classify - 1 < 0 || type_names == null || type_names.length <= classify - 1) {
            return text;
        }
        return text + "→" + type_names[classify - 1];
    }

    public void set_list() {
        this.adapter = new MySimpleAdapterVodView(this, VODplayer.list, C0216R.layout.voditem, new String[]{"ItemView", "ItemInfo", "ItemTitle"}, new int[]{C0216R.id.ItemView, C0216R.id.ItemInfo, C0216R.id.ItemTitle});
        this.mVodGrid.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C06297());
    }

    private boolean hideAllView() {
        int v = 0;
        if (this.findview.isShown()) {
            this.findview.hideFindView();
            v = 1;
        }
        if (this.classifyview.isShown()) {
            this.classifyview.hideClassifyList();
            v = 1;
        }
        if (v > 0) {
            return true;
        }
        return false;
    }

    public void listFocus() {
        this.mVodGrid.setFocusable(true);
        this.mVodGrid.setFocusableInTouchMode(true);
        this.mVodGrid.requestFocus();
        this.mVodGrid.requestFocusFromTouch();
    }

    public void listtypeFocus() {
        this.listtypeview.setFocusable(true);
        this.listtypeview.setFocusableInTouchMode(true);
        this.listtypeview.requestFocus();
        this.listtypeview.requestFocusFromTouch();
    }

    public void openInfoActivity(final String id, final String type) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(VodPlayerList2Activity.this, VodPlayerInfo2Activity.class);
                intent.putExtra(TtmlNode.ATTR_ID, id);
                intent.putExtra("type", type);
                intent.putExtra("collect", VodPlayerList2Activity.this.collectlist);
                VodPlayerList2Activity.this.startActivity(intent);
            }
        }, 300);
    }

    protected void onResume() {
        MGplayer.video_every_interface(this.onControlVideo);
        super.onResume();
    }

    protected void onStart() {
        MGplayer.MyPrintln("vod onstart");
        super.onStart();
    }

    public void openListActivity(String type) {
        finish();
        Intent intent = new Intent(this, VodPlayerList2Activity.class);
        intent.putExtra("type", type);
        if (MGplayer.isNumeric(VODplayer.type)) {
            int findtype = Integer.parseInt(VODplayer.type);
            if (findtype >= 0 && findtype <= 3) {
                intent.putExtra("findtype", Integer.parseInt(VODplayer.type));
            }
        }
        startActivity(intent);
    }

    private void findActivity(Activity _this) {
        dialog = new MyVodFind3().init(_this, this.onFindPressed);
    }

    private void inputPasswordView(final int index, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View textEntryView = LayoutInflater.from(this).inflate(C0216R.layout.inputpasswordview, null);
        builder.setIcon(17301659);
        builder.setTitle(getString(C0216R.string.typelist_text2).toString());
        builder.setView(textEntryView);
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String password = ((EditText) textEntryView.findViewById(C0216R.id.ps_editText)).getText().toString();
                String type_ps = MGplayer.MyGetSharedPreferences(VodPlayerList2Activity.this, "data", 0).getString("type_password", null);
                if (type_ps == null && VODplayer.columner != null) {
                    type_ps = VODplayer.columner[index].password;
                }
                if (!password.equals(type_ps) || VODplayer.columner == null) {
                    MyToast.makeText(VodPlayerList2Activity.this, VodPlayerList2Activity.this.getString(C0216R.string.typelist_text3).toString(), 0);
                    return;
                }
                VODplayer.columner[index].needps = 0;
                VodPlayerList2Activity.this.cmdMessageAddList(VODplayer.type, id);
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), null);
        builder.create().show();
    }

    public void delCollect(final int id) {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.vodlist_text12).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                VodPlayerList2Activity.this.collectvoddber.deleteData(id);
                VodPlayerList2Activity.this.collectlist = 1;
                VODplayer.list.clear();
                VodPlayerList2Activity.this.cmdMessageAddList("4", 3);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 21:
                    if (!this.enable_focus || this.selectItemIndex % this.colnumNum != 0) {
                        if ((this.enable_focus || !this.classifyview.isShown()) && !this.enable_focus) {
                            break;
                        }
                    }
                    listtypeFocus();
                    this.enable_focus = false;
                    return true;
                    break;
                case 82:
                    MenuView.gridMenuInit(this, 0);
                    MenuView.showAlertDialog(this);
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (!hideAllView()) {
                    this.isexit = false;
                    finish();
                    break;
                }
                return true;
            case 19:
            case 20:
                if (this.enable_focus) {
                    super.onKeyUp(keyCode, event);
                    listFocus();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() <= 20.0f && e2.getX() - e1.getX() <= 20.0f && e1.getY() - e2.getY() <= 20.0f && e2.getY() - e1.getY() > 20.0f) {
        }
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
