package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodClassifyView extends LinearLayout {
    private Context _this;
    private ListViewInterface iface = null;
    private boolean isShow = false;
    private ListView listview;
    public Handler rHandler = new C04827();
    private String selectid = null;

    /* renamed from: com.gemini.play.MyVodClassifyView$1 */
    class C04761 implements OnFocusChangeListener {
        C04761() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
        }
    }

    /* renamed from: com.gemini.play.MyVodClassifyView$2 */
    class C04772 implements OnItemClickListener {
        C04772() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodClassifyView.this.iface.callback(1, String.valueOf((String) ((HashMap) MyVodClassifyView.this.listview.getItemAtPosition(arg2)).get("ItemID")));
        }
    }

    /* renamed from: com.gemini.play.MyVodClassifyView$3 */
    class C04783 implements OnItemSelectedListener {
        C04783() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyVodClassifyView$4 */
    class C04794 implements ViewBinder {
        C04794() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyVodClassifyView$5 */
    class C04805 implements AnimationListener {
        C04805() {
        }

        public void onAnimationEnd(Animation animation) {
            MyVodClassifyView.this.clearAnimation();
            MyVodClassifyView.this.listFocus();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyVodClassifyView$6 */
    class C04816 implements AnimationListener {
        C04816() {
        }

        public void onAnimationEnd(Animation animation) {
            MyVodClassifyView.this.setVisibility(8);
            MyVodClassifyView.this.iface.callback(0, null);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyVodClassifyView$7 */
    class C04827 extends Handler {
        C04827() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyVodClassifyView.this.hideClassifyList();
                    return;
                default:
                    return;
            }
        }
    }

    public MyVodClassifyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodclassify, this, true);
        init();
    }

    public MyVodClassifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodclassify, this, true);
        init();
    }

    public MyVodClassifyView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodclassify, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        float rate = MGplayer.getFontsRate();
        this.listview = (ListView) findViewById(C0216R.id.listview);
        this.listview.setSelector(getResources().getDrawable(C0216R.mipmap.se));
        this.listview.setOnFocusChangeListener(new C04761());
        this.listview.setOnItemClickListener(new C04772());
        this.listview.setOnItemSelectedListener(new C04783());
    }

    public void set_list(int type) {
        ArrayList<HashMap<String, Object>> list = new ArrayList();
        MGplayer.MyPrintln("columner[" + type + "].type_type = " + VODplayer.columner[type].type_type);
        String[] type_names = null;
        if (VODplayer.columner[type].type_type != null && VODplayer.columner[type].type_type.length() > 1) {
            type_names = VODplayer.columner[type].type_type.split("\\|");
        }
        if (type_names != null) {
            for (int i = 0; i < type_names.length; i++) {
                HashMap<String, Object> map = new HashMap();
                map.put("ItemID", String.valueOf(i + 1));
                map.put("ItemIcon", Integer.valueOf(C0216R.mipmap.ti));
                map.put("ItemName", type_names[i]);
                list.add(map);
            }
        }
        MySimpleAdapterTypeListView adapter = new MySimpleAdapterTypeListView(this._this, list, C0216R.layout.classifyitem, new String[]{"ItemIcon", "ItemName"}, new int[]{C0216R.id.ItemIcon, C0216R.id.ItemName});
        this.listview.setAdapter(adapter);
        adapter.setViewBinder(new C04794());
    }

    public void showClassifyList(int type) {
        showViewTimeout();
        listFocus();
        if (!isShown()) {
            this.isShow = true;
            set_list(type);
            setVisibility(0);
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
                listFocus();
                return;
            }
            TranslateAnimation animation = new TranslateAnimation((float) ((-MGplayer.screenWidth) / 4), 0.0f, 0.0f, 0.0f);
            animation.setDuration(500);
            animation.setAnimationListener(new C04805());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void hideClassifyList() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        listNoFocus();
        if (isShown()) {
            this.isShow = false;
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
                setVisibility(8);
                this.iface.callback(0, null);
                return;
            }
            TranslateAnimation animation = new TranslateAnimation(0.0f, (float) ((-MGplayer.screenWidth) / 4), 0.0f, 0.0f);
            animation.setDuration(1000);
            animation.setAnimationListener(new C04816());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public boolean isShown() {
        return this.isShow;
    }

    public void listFocus() {
        this.listview.setFocusable(true);
        this.listview.setFocusableInTouchMode(true);
        this.listview.requestFocus();
        this.listview.requestFocusFromTouch();
    }

    public void listNoFocus() {
        this.listview.setFocusable(false);
        this.listview.setFocusableInTouchMode(false);
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public void hideViewTimeout() {
        Message msg = new Message();
        msg.what = 0;
        this.rHandler.sendMessageDelayed(msg, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
    }

    public void showViewTimeout() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        hideViewTimeout();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                case 20:
                    MGplayer.MyPrintln("list dispatchKeyEvent");
                    super.dispatchKeyEvent(event);
                    listFocus();
                    showViewTimeout();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
