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
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodFind3View extends LinearLayout {
    private Context _this = null;
    private ListViewInterface iface = null;
    private MySimpleAdapterInputView inputadapter = null;
    private GridView inputgrid = null;
    private ArrayList<HashMap<String, Object>> inputlist = new ArrayList();
    private boolean isShow = false;
    public Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyVodFind3View.this.hideFindView();
                    return;
                default:
                    return;
            }
        }
    };
    private int spinner_area_value = 0;
    private String spinner_find_value = null;
    private int spinner_sort_value = 0;
    private int spinner_type_value = 0;
    private int spinner_year_value = 0;
    private ArrayList<String> years_array = new ArrayList();

    /* renamed from: com.gemini.play.MyVodFind3View$1 */
    class C05011 implements OnFocusChangeListener {
        C05011() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3View$2 */
    class C05022 implements OnClickListener {
        C05022() {
        }

        public void onClick(View v) {
            MyVodFind3View.this.spinner_find_value = ((EditText) MyVodFind3View.this.findViewById(C0216R.id.edittext_find)).getText().toString();
            if (MyVodFind3View.this.spinner_find_value == null || MyVodFind3View.this.spinner_find_value.length() <= 0) {
                Toast.makeText(MyVodFind3View.this._this, MyVodFind3View.this._this.getString(C0216R.string.vodfind_text11).toString(), 0).show();
                return;
            }
            VODplayer.page = 0;
            String cmd = "&find=" + URLEncoder.encode(MyVodFind3View.this.spinner_find_value);
            MGplayer.MyPrintln("find cmd url:" + cmd);
            MyVodFind3View.this.iface.callback(0, cmd);
            MyVodFind3View.this.hideFindView();
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3View$3 */
    class C05033 implements OnFocusChangeListener {
        C05033() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3View$4 */
    class C05044 implements OnClickListener {
        C05044() {
        }

        public void onClick(View v) {
            VODplayer.page = 0;
            String cmd = "&sort=" + MyVodFind3View.this.spinner_sort_value;
            MGplayer.MyPrintln("find cmd url:" + cmd);
            MyVodFind3View.this.iface.callback(1, cmd);
            MyVodFind3View.this.hideFindView();
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3View$5 */
    class C05055 implements OnFocusChangeListener {
        C05055() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3View$7 */
    class C05077 implements OnItemSelectedListener {
        C05077() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (MyVodFind3View.this.inputadapter != null) {
                MyVodFind3View.this.inputadapter.setSeclection(arg2);
                MyVodFind3View.this.inputadapter.notifyDataSetChanged();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3View$8 */
    class C05088 implements ViewBinder {
        C05088() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3View$9 */
    class C05099 implements AnimationListener {
        C05099() {
        }

        public void onAnimationEnd(Animation animation) {
            MyVodFind3View.this.clearAnimation();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    public MyVodFind3View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodfindview3, this, true);
        init();
    }

    public MyVodFind3View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodfindview3, this, true);
        init();
    }

    public MyVodFind3View(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodfindview3, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        float rate = MGplayer.getFontsRate();
        final EditText e1 = (EditText) findViewById(C0216R.id.edittext_find);
        e1.setTypeface(typeFace);
        e1.setTextSize((float) ((int) (10.0f * rate)));
        e1.setOnFocusChangeListener(new C05011());
        Button button_find = (Button) findViewById(C0216R.id.button_find);
        button_find.setTypeface(typeFace);
        button_find.setTextSize((float) ((int) (rate * 7.0f)));
        button_find.setOnClickListener(new C05022());
        button_find.setOnFocusChangeListener(new C05033());
        Button button_back = (Button) findViewById(C0216R.id.button_back);
        button_back.setTextSize((float) ((int) (rate * 7.0f)));
        button_back.setTypeface(typeFace);
        button_back.setOnClickListener(new C05044());
        button_back.setOnFocusChangeListener(new C05055());
        this.inputgrid = (GridView) findViewById(C0216R.id.inputgrid);
        this.inputgrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                String t0 = ((HashMap) MyVodFind3View.this.inputgrid.getItemAtPosition(arg2)).get("ItemChar").toString();
                if (t0.equals("CE")) {
                    e1.setText("");
                } else {
                    e1.setText(e1.getText().toString() + t0);
                }
                if (MyVodFind3View.this.inputadapter != null) {
                    MyVodFind3View.this.inputadapter.setSeclection(arg2);
                    MyVodFind3View.this.inputadapter.notifyDataSetChanged();
                }
            }
        });
        this.inputgrid.setOnItemSelectedListener(new C05077());
        this.inputgrid.setSelector(17170445);
        init_input();
        set_inputlist();
    }

    private void init_input() {
        this.inputadapter = new MySimpleAdapterInputView(this._this, this.inputlist, C0216R.layout.inputitem, new String[]{"ItemChar"}, new int[]{C0216R.id.ItemChar});
        this.inputgrid.setAdapter(this.inputadapter);
        this.inputadapter.setViewBinder(new C05088());
    }

    private void set_inputlist() {
        this.inputlist.clear();
        String t = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        for (int ii = 0; ii < t.length(); ii++) {
            HashMap<String, Object> map = new HashMap();
            map.put("ItemChar", Character.valueOf(t.charAt(ii)));
            this.inputlist.add(map);
        }
        HashMap<String, Object> map2 = new HashMap();
        map2.put("ItemChar", "CE");
        this.inputlist.add(map2);
        this.inputadapter.notifyDataSetChanged();
    }

    public void showFindView(String type) {
        listFocus();
        if (!isShown()) {
            this.isShow = true;
            setVisibility(0);
            TranslateAnimation animation = new TranslateAnimation((float) ((MGplayer.screenWidth / 10) * 7), 0.0f, 0.0f, 0.0f);
            animation.setDuration(500);
            animation.setAnimationListener(new C05099());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void hideFindView() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        if (isShown()) {
            this.isShow = false;
            TranslateAnimation animation = new TranslateAnimation(0.0f, (float) ((MGplayer.screenWidth / 10) * 7), 0.0f, 0.0f);
            animation.setDuration(1000);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    MyVodFind3View.this.setVisibility(8);
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

    public boolean isShown() {
        return this.isShow;
    }

    public void listFocus() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        requestFocusFromTouch();
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MGplayer.MyPrintln("onKeyDown keyCode = " + keyCode);
        switch (keyCode) {
            case 21:
                super.onKeyDown(keyCode, event);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }
}
