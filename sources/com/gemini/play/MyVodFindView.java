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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodFindView extends LinearLayout {
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
                    MyVodFindView.this.hideFindView();
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

    /* renamed from: com.gemini.play.MyVodFindView$1 */
    class C05101 implements OnFocusChangeListener {
        C05101() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFindView$2 */
    class C05112 implements OnClickListener {
        C05112() {
        }

        public void onClick(View v) {
            MyVodFindView.this.spinner_find_value = ((EditText) MyVodFindView.this.findViewById(C0216R.id.edittext_find)).getText().toString();
            if (MyVodFindView.this.spinner_find_value == null || MyVodFindView.this.spinner_find_value.length() <= 0) {
                Toast.makeText(MyVodFindView.this._this, MyVodFindView.this._this.getString(C0216R.string.vodfind_text11).toString(), 0).show();
                return;
            }
            VODplayer.page = 0;
            String cmd = "&find=" + URLEncoder.encode(MyVodFindView.this.spinner_find_value);
            MGplayer.MyPrintln("find cmd url:" + cmd);
            MyVodFindView.this.iface.callback(0, cmd);
            MyVodFindView.this.hideFindView();
        }
    }

    /* renamed from: com.gemini.play.MyVodFindView$3 */
    class C05123 implements OnFocusChangeListener {
        C05123() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFindView$4 */
    class C05134 implements OnClickListener {
        C05134() {
        }

        public void onClick(View v) {
            VODplayer.page = 0;
            String cmd = "&sort=" + MyVodFindView.this.spinner_sort_value;
            MGplayer.MyPrintln("find cmd url:" + cmd);
            MyVodFindView.this.iface.callback(1, cmd);
            MyVodFindView.this.hideFindView();
        }
    }

    /* renamed from: com.gemini.play.MyVodFindView$5 */
    class C05145 implements OnFocusChangeListener {
        C05145() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFindView$6 */
    class C05156 implements OnClickListener {
        C05156() {
        }

        public void onClick(View v) {
            VODplayer.page = 0;
            String years_value = "0";
            if (MyVodFindView.this.spinner_year_value != 0) {
                years_value = (String) MyVodFindView.this.years_array.get(MyVodFindView.this.spinner_year_value);
            }
            String cmd = "&itype=" + MyVodFindView.this.spinner_type_value + "&iyear=" + years_value + "&iarea=" + MyVodFindView.this.spinner_area_value;
            MGplayer.MyPrintln("find cmd url:" + cmd);
            MyVodFindView.this.iface.callback(2, cmd);
            MyVodFindView.this.hideFindView();
        }
    }

    /* renamed from: com.gemini.play.MyVodFindView$7 */
    class C05167 implements OnFocusChangeListener {
        C05167() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFindView$8 */
    class C05178 implements OnClickListener {
        C05178() {
        }

        public void onClick(View v) {
            MyVodFindView.this.hideFindView();
        }
    }

    /* renamed from: com.gemini.play.MyVodFindView$9 */
    class C05189 implements OnFocusChangeListener {
        C05189() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    public MyVodFindView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodfindview, this, true);
        init();
    }

    public MyVodFindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodfindview, this, true);
        init();
    }

    public MyVodFindView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodfindview, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        final float rate = MGplayer.getFontsRate();
        TextView t3 = (TextView) findViewById(C0216R.id.text_area);
        t3.setTextSize((float) ((int) (7.0f * rate)));
        t3.setTypeface(typeFace);
        TextView t4 = (TextView) findViewById(C0216R.id.text_year);
        t4.setTextSize((float) ((int) (7.0f * rate)));
        t4.setTypeface(typeFace);
        TextView t5 = (TextView) findViewById(C0216R.id.text_type);
        t5.setTextSize((float) ((int) (7.0f * rate)));
        t5.setTypeface(typeFace);
        final EditText e1 = (EditText) findViewById(C0216R.id.edittext_find);
        e1.setTypeface(typeFace);
        e1.setTextSize((float) ((int) (10.0f * rate)));
        e1.setOnFocusChangeListener(new C05101());
        Button b0 = (Button) findViewById(C0216R.id.button_find);
        b0.setTypeface(typeFace);
        b0.setTextSize((float) ((int) (7.0f * rate)));
        b0.setOnClickListener(new C05112());
        b0.setOnFocusChangeListener(new C05123());
        Button b1 = (Button) findViewById(C0216R.id.button_sort);
        b1.setTextSize((float) ((int) (7.0f * rate)));
        b1.setTypeface(typeFace);
        b1.setOnClickListener(new C05134());
        b1.setOnFocusChangeListener(new C05145());
        Button b2 = (Button) findViewById(C0216R.id.button_filter);
        b2.setTextSize((float) ((int) (7.0f * rate)));
        b2.setTypeface(typeFace);
        b2.setOnClickListener(new C05156());
        b2.setOnFocusChangeListener(new C05167());
        Button b3 = (Button) findViewById(C0216R.id.button_back);
        b3.setTextSize((float) ((int) (7.0f * rate)));
        b3.setTypeface(typeFace);
        b3.setOnClickListener(new C05178());
        b3.setOnFocusChangeListener(new C05189());
        MyArrayAdapterFindView adapter = new MyArrayAdapterFindView(this._this, new String[]{this._this.getString(C0216R.string.vodfind_text6).toString(), this._this.getString(C0216R.string.vodfind_text5).toString()});
        adapter.setDropDownViewResource(17367048);
        Spinner spinner_sort = (Spinner) findViewById(C0216R.id.spinner_sort);
        spinner_sort.setAdapter(adapter);
        spinner_sort.setPrompt(this._this.getString(C0216R.string.vodfind_text1).toString());
        spinner_sort.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                MGplayer.MyPrintln("spinner_sort:" + arg2);
                ((TextView) arg1).setTextSize((float) ((int) (rate * 7.0f)));
                MyVodFindView.this.spinner_sort_value = arg2;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ((Spinner) findViewById(C0216R.id.spinner_area)).setAdapter(adapter);
        ((Spinner) findViewById(C0216R.id.spinner_year)).setAdapter(adapter);
        ((Spinner) findViewById(C0216R.id.spinner_type)).setAdapter(adapter);
        this.inputgrid = (GridView) findViewById(C0216R.id.inputgrid);
        this.inputgrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                String t0 = ((HashMap) MyVodFindView.this.inputgrid.getItemAtPosition(arg2)).get("ItemChar").toString();
                if (t0.equals("CE")) {
                    e1.setText("");
                } else {
                    e1.setText(e1.getText().toString() + t0);
                }
                if (MyVodFindView.this.inputadapter != null) {
                    MyVodFindView.this.inputadapter.setSeclection(arg2);
                    MyVodFindView.this.inputadapter.notifyDataSetChanged();
                }
            }
        });
        this.inputgrid.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                if (MyVodFindView.this.inputadapter != null) {
                    MyVodFindView.this.inputadapter.setSeclection(arg2);
                    MyVodFindView.this.inputadapter.notifyDataSetChanged();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.inputgrid.setSelector(17170445);
        init_input();
        set_inputlist();
    }

    private void init_input() {
        this.inputadapter = new MySimpleAdapterInputView(this._this, this.inputlist, C0216R.layout.inputitem, new String[]{"ItemChar"}, new int[]{C0216R.id.ItemChar});
        this.inputgrid.setAdapter(this.inputadapter);
        this.inputadapter.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                    return false;
                }
                ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                return true;
            }
        });
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

    private VodTypeStatus getVodTypeStatus(String type) {
        if (!MGplayer.isNumeric(type) || Integer.parseInt(type) >= 4) {
            return null;
        }
        return VODplayer.typeGet(Integer.parseInt(type));
    }

    private void initTypeSpinner(VodTypeStatus s) {
        ArrayList array;
        final float rate = MGplayer.getFontsRate();
        if (s.areas != null) {
            array = new ArrayList();
            array.add(this._this.getString(C0216R.string.vodfind_text4).toString());
            for (Object add : s.areas) {
                array.add(add);
            }
            MyArrayAdapterFindView adapter_area = new MyArrayAdapterFindView(this._this, array);
            adapter_area.setDropDownViewResource(17367049);
            Spinner spinner_area = (Spinner) findViewById(C0216R.id.spinner_area);
            spinner_area.setAdapter(adapter_area);
            spinner_area.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    MGplayer.MyPrintln("spinner_area:" + arg2);
                    MyVodFindView.this.spinner_area_value = arg2;
                    ((TextView) arg1).setTextSize((float) ((int) (((double) rate) * 6.8d)));
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
        if (s.years != null) {
            this.years_array.clear();
            this.years_array.add(this._this.getString(C0216R.string.vodfind_text4).toString());
            for (Object add2 : s.years) {
                this.years_array.add(add2);
            }
            MyArrayAdapterFindView adapter_year = new MyArrayAdapterFindView(this._this, this.years_array);
            adapter_year.setDropDownViewResource(17367049);
            Spinner spinner_year = (Spinner) findViewById(C0216R.id.spinner_year);
            spinner_year.setAdapter(adapter_year);
            spinner_year.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    MGplayer.MyPrintln("spinner_year:" + arg2);
                    MyVodFindView.this.spinner_year_value = arg2;
                    ((TextView) arg1).setTextSize((float) ((int) (((double) rate) * 6.8d)));
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
        if (s.types != null) {
            array = new ArrayList();
            array.add(this._this.getString(C0216R.string.vodfind_text4).toString());
            for (Object add3 : s.types) {
                array.add(add3);
            }
            MyArrayAdapterFindView adapter_type = new MyArrayAdapterFindView(this._this, array);
            adapter_type.setDropDownViewResource(17367049);
            Spinner spinner_type = (Spinner) findViewById(C0216R.id.spinner_type);
            spinner_type.setAdapter(adapter_type);
            spinner_type.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    MGplayer.MyPrintln("spinner_type:" + arg2);
                    MyVodFindView.this.spinner_type_value = arg2;
                    ((TextView) arg1).setTextSize((float) ((int) (((double) rate) * 6.8d)));
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    public void showFindView(String type) {
        listFocus();
        this.spinner_sort_value = 0;
        this.spinner_area_value = 0;
        this.spinner_year_value = 0;
        this.spinner_type_value = 0;
        if (!isShown()) {
            this.isShow = true;
            VodTypeStatus s = getVodTypeStatus(type);
            if (s != null) {
                initTypeSpinner(s);
            }
            setVisibility(0);
            TranslateAnimation animation = new TranslateAnimation((float) ((MGplayer.screenWidth / 10) * 7), 0.0f, 0.0f, 0.0f);
            animation.setDuration(500);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    MyVodFindView.this.clearAnimation();
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
                    MyVodFindView.this.setVisibility(8);
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
