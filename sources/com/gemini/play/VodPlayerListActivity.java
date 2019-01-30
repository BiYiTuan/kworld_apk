package com.gemini.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.util.ArrayList;
import java.util.HashMap;
import net.lingala.zip4j.util.InternalZipConstants;

public class VodPlayerListActivity extends Activity implements OnGestureListener {
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
    private MyVodFindView findview = null;
    private boolean isexit = false;
    private int lineNum = 2;
    private MyVodTypeView2 listtypeview;
    private MyGridView mVodGrid = null;
    public ListViewInterface onClassifyPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            int select;
            switch (cmd) {
                case 0:
                    VodPlayerListActivity.this.listtypeFocus();
                    VodPlayerListActivity.this.listtypeview.selectIndex();
                    VodPlayerListActivity.this.enable_focus = false;
                    return;
                case 1:
                    select = VodPlayerListActivity.this.listtypeview.getSelectIndex();
                    MGplayer.MyPrintln("onTypePressed:" + select + "VODplayer.url_param:" + VODplayer.url_param);
                    if (select < 4) {
                        VODplayer.list.clear();
                        VODplayer.url_param = "&itype=" + data + "&iyear=0&iarea=0";
                        VodPlayerListActivity.this.cmdMessageAddList(String.valueOf(select), 0, 2);
                        VodPlayerListActivity.this.classifyview.hideClassifyList();
                        if (MGplayer.isNumeric(data)) {
                            VodPlayerListActivity.this.topview.set_vod_top_center_text(VodPlayerListActivity.this.get_top_title(select, Integer.parseInt(data)));
                            return;
                        }
                        return;
                    }
                    return;
                case 2:
                    select = VodPlayerListActivity.this.listtypeview.getSelectIndex();
                    if (select < 4) {
                        VODplayer.list.clear();
                        VODplayer.url_param = data;
                        VodPlayerListActivity.this.cmdMessageAddList(String.valueOf(select), 0, 2);
                        VodPlayerListActivity.this.classifyview.hideClassifyList();
                        String type = data.substring(data.indexOf("&itype=") + 7, data.indexOf("&iyear"));
                        if (type.equals("0")) {
                            VodPlayerListActivity.this.topview.set_vod_top_center_text(VodPlayerListActivity.this.getString(C0216R.string.vodclassify_text5));
                            return;
                        } else if (MGplayer.isNumeric(type)) {
                            VodPlayerListActivity.this.topview.set_vod_top_center_text(VodPlayerListActivity.this.get_top_title(select, Integer.parseInt(type)));
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    };
    public ControlVideoInterface onControlVideo = new ControlVideoInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(VodPlayerListActivity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    VodPlayerListActivity.this.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onFindPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    VodPlayerListActivity.this.cmdMessageAddList(VODplayer.type, VODplayer.page, 2);
                    return;
                case 1:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    VodPlayerListActivity.this.cmdMessageAddList(VODplayer.type, VODplayer.page, 2);
                    return;
                case 2:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    VodPlayerListActivity.this.cmdMessageAddList(VODplayer.type, VODplayer.page, 2);
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
                    int select = VodPlayerListActivity.this.listtypeview.getSelectIndex();
                    MGplayer.MyPrintln("onTypePressed:" + select);
                    if (select < 4) {
                        VodPlayerListActivity.this.classifyview.showClassifyList(select);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onTypePressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VodPlayerListActivity.this.collectlist = 0;
                    VODplayer.listClear();
                    MGplayer.MyPrintln("ItemId data = " + data);
                    VODplayer.page = 0;
                    VODplayer.list.clear();
                    VodPlayerListActivity.this.cmdMessageAddList(data);
                    if (MGplayer.isNumeric(data)) {
                        VodPlayerListActivity.this.topview.set_vod_top_center_text(VodPlayerListActivity.this.get_top_title(Integer.parseInt(data), -1));
                        return;
                    }
                    return;
                case 1:
                    VodPlayerListActivity.this.collectlist = 1;
                    VODplayer.list.clear();
                    VodPlayerListActivity.this.cmdMessageAddList(data, 3);
                    return;
                case 2:
                    VodPlayerListActivity.this.findActivity(VodPlayerListActivity.this);
                    return;
                case 3:
                    VodPlayerListActivity.this.exitActivity();
                    return;
                case 4:
                    VodPlayerListActivity.this.listtypeview.setFocusable(false);
                    VodPlayerListActivity.this.enable_focus = true;
                    VodPlayerListActivity.this.listFocus();
                    return;
                case 5:
                    int select = VodPlayerListActivity.this.listtypeview.getSelectIndex();
                    MGplayer.MyPrintln("onTypePressed:" + select);
                    if (select < 4) {
                        VodPlayerListActivity.this.classifyview.showClassifyList(select);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    public Handler rHandler = new C06407();
    private float rate;
    private int selectItemIndex;
    private MyVodTopView topview = null;
    private Typeface typeFace;
    private ProgressBar vodProgressBar = null;

    /* renamed from: com.gemini.play.VodPlayerListActivity$1 */
    class C06311 implements OnScrollListener {
        boolean isLastRow = false;

        C06311() {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            VodPlayerListActivity.this.set_top_text();
            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                this.isLastRow = true;
            }
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            VodPlayerListActivity.this.set_top_text();
            if (this.isLastRow && scrollState == 0) {
                MGplayer.MyPrintln("load data isLastRow");
                VodPlayerListActivity.this.cmdMessageAddList(VODplayer.type, VODplayer.list.size() / VODplayer.psize, 2);
                this.isLastRow = false;
            }
        }
    }

    /* renamed from: com.gemini.play.VodPlayerListActivity$2 */
    class C06322 implements OnFocusChangeListener {
        C06322() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                VodPlayerListActivity.this.adapter.setSeclection(0);
                VodPlayerListActivity.this.adapter.notifyDataSetChanged();
                return;
            }
            MGplayer.MyPrintln("mVodGrid onfocus");
            VodPlayerListActivity.this.adapter.setSeclection(-1);
            VodPlayerListActivity.this.adapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.gemini.play.VodPlayerListActivity$3 */
    class C06333 implements OnItemClickListener {
        C06333() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            HashMap<String, String> map = (HashMap) VodPlayerListActivity.this.mVodGrid.getItemAtPosition(arg2);
            String id = (String) map.get("ItemId");
            String type = (String) map.get("ItemType");
            MGplayer.MyPrintln("ItemId:" + id);
            VodPlayerListActivity.this.adapter.setSeclection(arg2);
            VodPlayerListActivity.this.adapter.notifyDataSetChanged();
            VodPlayerListActivity.this.openInfoActivity(id, String.valueOf(type));
        }
    }

    /* renamed from: com.gemini.play.VodPlayerListActivity$4 */
    class C06344 implements OnItemSelectedListener {
        C06344() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MGplayer.MyPrintln("onItemSelected:" + arg2);
            VodPlayerListActivity.this.selectItemIndex = arg2;
            if (arg1 != null && VodPlayerListActivity.this.mVodGrid.isFocusable()) {
                if (arg2 >= VODplayer.list.size() - VodPlayerListActivity.this.colnumNum && arg2 > 5) {
                    VodPlayerListActivity.this.cmdMessageAddList(VODplayer.type, VODplayer.list.size() / VODplayer.psize, 2);
                }
                VodPlayerListActivity.this.adapter.setSeclection(arg2);
                VodPlayerListActivity.this.adapter.notifyDataSetChanged();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.VodPlayerListActivity$5 */
    class C06355 implements OnItemLongClickListener {
        C06355() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (VodPlayerListActivity.this.collectlist == 1) {
                String id = (String) ((HashMap) VodPlayerListActivity.this.mVodGrid.getItemAtPosition(arg2)).get("ItemId");
                MGplayer.MyPrintln("setOnItemLongClickListener:" + id);
                if (MGplayer.isNumeric(id)) {
                    VodPlayerListActivity.this.delCollect(Integer.parseInt(id));
                }
            }
            return true;
        }
    }

    /* renamed from: com.gemini.play.VodPlayerListActivity$6 */
    class C06366 implements OnClickListener {
        C06366() {
        }

        public void onClick(View v) {
            if (VodPlayerListActivity.this.findview.isShown()) {
                VodPlayerListActivity.this.findview.hideFindView();
            } else {
                VodPlayerListActivity.this.findview.showFindView(VODplayer.type);
            }
        }
    }

    /* renamed from: com.gemini.play.VodPlayerListActivity$7 */
    class C06407 extends Handler {

        /* renamed from: com.gemini.play.VodPlayerListActivity$7$3 */
        class C06393 extends Thread {
            C06393() {
            }

            public void run() {
                ArrayList<VodListStatus> xlist = VodPlayerListActivity.this.collectvoddber.parseAll();
                if (xlist != null) {
                    for (int ii = 0; ii < xlist.size(); ii++) {
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
                        VODplayer.list.add(map);
                    }
                    VodPlayerListActivity.this.cmdMessageAddVod();
                }
            }
        }

        C06407() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    final String id0 = msg.getData().getString("data");
                    VODplayer.type = id0;
                    VODplayer.url_param = null;
                    new Thread() {
                        public void run() {
                            VODplayer.parseTypeXML(id0);
                        }
                    }.start();
                    break;
                case 1:
                    VodPlayerListActivity.this.adapter.notifyDataSetChanged();
                    VodPlayerListActivity.this.vodProgressBar.setVisibility(8);
                    if (VodPlayerListActivity.list_type_getfocus) {
                        VodPlayerListActivity.list_type_getfocus = false;
                        VodPlayerListActivity.this.listtypeFocus();
                        VodPlayerListActivity.this.enable_focus = false;
                    }
                    VodPlayerListActivity.this.set_top_text();
                    return;
                case 2:
                    break;
                case 3:
                    new C06393().start();
                    return;
                case 4:
                    VodPlayerListActivity.this.mVodGrid.setFocusable(false);
                    VodPlayerListActivity.this.listtypeview.setFocusable(false);
                    VodPlayerListActivity.this.findview.showFindView(VODplayer.type);
                    return;
                case 5:
                    VodPlayerListActivity.this.vodProgressBar.setVisibility(8);
                    return;
                case 6:
                    VodPlayerListActivity.this.adapter.notifyDataSetChanged();
                    return;
                default:
                    return;
            }
            VodPlayerListActivity.this.vodProgressBar.setVisibility(0);
            final String id = VODplayer.type;
            int page = msg.getData().getInt("page");
            String url_tmp = VODplayer.gete() + "/vod_xml.php?type=" + id + "&page=" + page + MGplayer.get_key_value();
            MGplayer.MyPrintln("url_tmp:" + url_tmp);
            if (!(VODplayer.url_param == null || VODplayer.url_param.equals("null"))) {
                url_tmp = url_tmp + VODplayer.url_param;
            }
            final String url = url_tmp;
            MGplayer.MyPrintln("page:" + page + " VODplayer.page:" + VODplayer.page);
            if (page - 1 >= VODplayer.page) {
                VodPlayerListActivity.this.cmdMessageHideProgress();
                return;
            }
            Toast.makeText(VodPlayerListActivity.this, VodPlayerListActivity.this.getString(C0216R.string.vodlist_text7).toString(), 0).show();
            new Thread() {
                public void run() {
                    String type_tmp = VODplayer.type;
                    while (type_tmp == VODplayer.type && !VodPlayerListActivity.this.isDestroyed() && !VodPlayerListActivity.this.isFinishing()) {
                        ArrayList<VodListStatus> xlist = VODplayer.parseXML(id, url);
                        if (xlist == null) {
                            MGplayer.MyPrintln("xlist:NULL");
                        } else {
                            VodPlayerListActivity.this.cmdMessageHideProgress();
                            for (int ii = 0; ii < xlist.size(); ii++) {
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
                                    map.put("ItemType", String.valueOf(VODplayer.type));
                                    map.put("ItemImageUrl", url);
                                    map.put("ItemHaveLoad", Integer.valueOf(0));
                                    VODplayer.list.add(map);
                                }
                            }
                            if (xlist.size() > 0) {
                                VODplayer.page++;
                            }
                            VodPlayerListActivity.this.cmdMessageAddVod();
                            return;
                        }
                    }
                }
            }.start();
        }
    }

    /* renamed from: com.gemini.play.VodPlayerListActivity$8 */
    class C06418 implements ViewBinder {
        C06418() {
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

    /* renamed from: com.gemini.play.VodPlayerListActivity$9 */
    class C06429 implements DialogInterface.OnClickListener {
        C06429() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            VodPlayerListActivity.this.finish();
            if (!MGplayer.boot_launcher) {
                VodPlayerListActivity.this.stopService(new Intent(VodPlayerListActivity.this, LocalService.class));
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.vodplayerlist);
        getWindow().setFlags(1024, 1024);
        MGplayer.mediaplayervodheader(1);
        this.detector = new GestureDetector(this, this);
        this.findview = (MyVodFindView) findViewById(C0216R.id.findview);
        this.collectvoddber = new CollectVodDB(this);
        this.topview = (MyVodTopView) findViewById(C0216R.id.myvodtopview);
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
        this.mVodGrid.setOnScrollListener(new C06311());
        this.mVodGrid.setOnFocusChangeListener(new C06322());
        this.mVodGrid.setOnItemClickListener(new C06333());
        this.mVodGrid.setOnItemSelectedListener(new C06344());
        this.mVodGrid.setOnItemLongClickListener(new C06355());
        init_ui();
        set_list();
        this.listtypeview = (MyVodTypeView2) findViewById(C0216R.id.myvodtypeview);
        this.listtypeview.setInterface(this.onTypePressed);
        if (!MGplayer.isNumeric(VODplayer.type) || VODplayer.columner == null || VODplayer.columner.length <= 0) {
            cmdMessageAddList(VODplayer.type);
        } else {
            for (int ii = 0; ii < VODplayer.columner.length; ii++) {
                if (VODplayer.columner[ii].needps == 0) {
                    VODplayer.type = String.valueOf(ii);
                    this.listtypeview.selectFirstIndex(ii);
                    cmdMessageAddList(VODplayer.type);
                    break;
                }
            }
        }
        MGplayer.video_every_interface(this.onControlVideo);
        this.bitmapCache = new LruBitmapCache();
        listtypeFocus();
        list_type_getfocus = true;
        this.collectlist = 0;
        VODplayer.listClear();
        VODplayer.page = 0;
        VODplayer.list.clear();
        cmdMessageAddList(VODplayer.type);
        if (MGplayer.updatetip_show == 1 && !MGplayer.boot_launcher) {
            MGplayer.showUpdateActivity(this);
        }
    }

    public void init_ui() {
        this.typeFace = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        this.rate = MGplayer.getFontsRate();
        ((ImageButton) findViewById(C0216R.id.findbutton)).setOnClickListener(new C06366());
        this.findview.setInterface(this.onFindPressed);
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
            this.topview.set_vod_top_text(((this.mVodGrid.getFirstVisiblePosition() / (this.lineNum * this.colnumNum)) + 1) + InternalZipConstants.ZIP_FILE_SEPARATOR + pages);
            return;
        }
        pages = VODplayer.vod_number[Integer.parseInt(VODplayer.type)] / (this.lineNum * this.colnumNum);
        if (VODplayer.vod_number[Integer.parseInt(VODplayer.type)] % (this.lineNum * this.colnumNum) > 0) {
            pages++;
        }
        if (pages == 0) {
            pages = 1;
        }
        this.topview.set_vod_top_text(((this.mVodGrid.getFirstVisiblePosition() / (this.lineNum * this.colnumNum)) + 1) + InternalZipConstants.ZIP_FILE_SEPARATOR + pages);
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
        this.adapter.setViewBinder(new C06418());
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

    public void exitActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.vodlist_text10).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new C06429());
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
                Intent intent = new Intent(VodPlayerListActivity.this, VodPlayerInfoActivity.class);
                intent.putExtra(TtmlNode.ATTR_ID, id);
                intent.putExtra("type", type);
                intent.putExtra("collect", VodPlayerListActivity.this.collectlist);
                VodPlayerListActivity.this.startActivity(intent);
            }
        }, 1000);
    }

    protected void onResume() {
        MGplayer.video_every_interface(this.onControlVideo);
        super.onResume();
    }

    protected void onStart() {
        MGplayer.MyPrintln("vod onstart");
        super.onStart();
    }

    private void findActivity(Context _this) {
        dialog = new MyVodFind().init(_this, this.onFindPressed, VODplayer.type);
    }

    public void delCollect(final int id) {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.vodlist_text12).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                VodPlayerListActivity.this.collectvoddber.deleteData(id);
                VodPlayerListActivity.this.collectlist = 1;
                VODplayer.list.clear();
                VodPlayerListActivity.this.cmdMessageAddList("4", 3);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
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
                    this.listtypeview.selectIndex();
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
                if (hideAllView()) {
                    return true;
                }
                if (this.isexit) {
                    this.isexit = false;
                    finish();
                    if (!MGplayer.boot_launcher) {
                        stopService(new Intent(this, LocalService.class));
                        Process.killProcess(Process.myPid());
                        System.exit(0);
                        break;
                    }
                }
                this.isexit = true;
                MyToast.makeText(this, getString(C0216R.string.isexit).toString(), 0);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        VodPlayerListActivity.this.isexit = false;
                    }
                }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                return true;
                break;
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
        if (e1.getX() - e2.getX() > 20.0f) {
            this.findview.showFindView(VODplayer.type);
        } else if (e2.getX() - e1.getX() <= 20.0f && e1.getY() - e2.getY() <= 20.0f && e2.getY() - e1.getY() > 20.0f) {
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
