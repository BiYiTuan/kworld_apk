package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodClassify2View extends LinearLayout {
    private Context _this;
    private MySimpleAdapterTypeListView2 adapter_area;
    private MySimpleAdapterTypeListView2 adapter_type;
    private MySimpleAdapterTypeListView2 adapter_year;
    private Button button_find = null;
    private ListViewInterface iface = null;
    private int index_area = 0;
    private int index_type = 0;
    private int index_year = 0;
    private boolean isShow = false;
    ArrayList<HashMap<String, Object>> list_area;
    ArrayList<HashMap<String, Object>> list_type;
    ArrayList<HashMap<String, Object>> list_year;
    private TextView listtext_area;
    private TextView listtext_type;
    private TextView listtext_year;
    private ListView listview_area;
    private int listview_index = 0;
    private ListView listview_type;
    private ListView listview_year;
    public Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyVodClassify2View.this.hideClassifyList();
                    return;
                default:
                    return;
            }
        }
    };
    private String select_area = "0";
    private String select_cmd = "&itype=0&iyear=0&iarea=0";
    private String select_type = "0";
    private String select_year = "0";
    private String selectid = null;

    /* renamed from: com.gemini.play.MyVodClassify2View$1 */
    class C04671 implements OnItemClickListener {
        C04671() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodClassify2View.this.select_type = (String) ((HashMap) MyVodClassify2View.this.listview_type.getItemAtPosition(arg2)).get("ItemID");
            MyVodClassify2View.this.select_cmd = "&itype=" + MyVodClassify2View.this.select_type + "&iyear=" + MyVodClassify2View.this.select_year + "&iarea=" + MyVodClassify2View.this.select_area;
            MyVodClassify2View.this.adapter_type.setCurrentIndex(arg2);
            MyVodClassify2View.this.adapter_type.notifyDataSetChanged();
        }
    }

    /* renamed from: com.gemini.play.MyVodClassify2View$2 */
    class C04682 implements OnItemSelectedListener {
        C04682() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodClassify2View.this.index_type = arg2;
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyVodClassify2View$3 */
    class C04693 implements OnFocusChangeListener {
        C04693() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                MyVodClassify2View.this.adapter_type.notifyDataSetChanged();
                MyVodClassify2View.this.listview_type.setSelector(MyVodClassify2View.this.getResources().getDrawable(C0216R.mipmap.se));
                return;
            }
            MyVodClassify2View.this.adapter_type.notifyDataSetChanged();
            MyVodClassify2View.this.listview_type.setSelector(C0216R.color.transparent);
        }
    }

    /* renamed from: com.gemini.play.MyVodClassify2View$4 */
    class C04704 implements OnFocusChangeListener {
        C04704() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                MyVodClassify2View.this.adapter_year.notifyDataSetChanged();
                MyVodClassify2View.this.listview_year.setSelector(MyVodClassify2View.this.getResources().getDrawable(C0216R.mipmap.se));
                return;
            }
            MyVodClassify2View.this.adapter_year.notifyDataSetChanged();
            MyVodClassify2View.this.listview_year.setSelector(C0216R.color.transparent);
        }
    }

    /* renamed from: com.gemini.play.MyVodClassify2View$5 */
    class C04715 implements OnItemClickListener {
        C04715() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodClassify2View.this.select_year = (String) ((HashMap) MyVodClassify2View.this.listview_year.getItemAtPosition(arg2)).get("ItemID");
            MyVodClassify2View.this.select_cmd = "&itype=" + MyVodClassify2View.this.select_type + "&iyear=" + MyVodClassify2View.this.select_year + "&iarea=" + MyVodClassify2View.this.select_area;
            MyVodClassify2View.this.adapter_year.setCurrentIndex(arg2);
            MyVodClassify2View.this.adapter_year.notifyDataSetChanged();
        }
    }

    /* renamed from: com.gemini.play.MyVodClassify2View$6 */
    class C04726 implements OnItemSelectedListener {
        C04726() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodClassify2View.this.index_year = arg2;
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyVodClassify2View$7 */
    class C04737 implements OnFocusChangeListener {
        C04737() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                MyVodClassify2View.this.adapter_area.notifyDataSetChanged();
                MyVodClassify2View.this.listview_area.setSelector(MyVodClassify2View.this.getResources().getDrawable(C0216R.mipmap.se));
                return;
            }
            MyVodClassify2View.this.adapter_area.notifyDataSetChanged();
            MyVodClassify2View.this.listview_area.setSelector(C0216R.color.transparent);
        }
    }

    /* renamed from: com.gemini.play.MyVodClassify2View$8 */
    class C04748 implements OnItemClickListener {
        C04748() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodClassify2View.this.select_area = (String) ((HashMap) MyVodClassify2View.this.listview_area.getItemAtPosition(arg2)).get("ItemID");
            MyVodClassify2View.this.select_cmd = "&itype=" + MyVodClassify2View.this.select_type + "&iyear=" + MyVodClassify2View.this.select_year + "&iarea=" + MyVodClassify2View.this.select_area;
            MyVodClassify2View.this.adapter_area.setCurrentIndex(arg2);
            MyVodClassify2View.this.adapter_area.notifyDataSetChanged();
        }
    }

    /* renamed from: com.gemini.play.MyVodClassify2View$9 */
    class C04759 implements OnItemSelectedListener {
        C04759() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodClassify2View.this.index_area = arg2;
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public MyVodClassify2View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodclassify2, this, true);
        init();
    }

    public MyVodClassify2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodclassify2, this, true);
        init();
    }

    public MyVodClassify2View(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodclassify2, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = MGplayer.getFontsType(this._this);
        float rate = MGplayer.getFontsRate();
        this.listview_type = (ListView) findViewById(C0216R.id.listview_type);
        this.listview_year = (ListView) findViewById(C0216R.id.listview_year);
        this.listview_area = (ListView) findViewById(C0216R.id.listview_area);
        this.listtext_type = (TextView) findViewById(C0216R.id.listtext_type);
        this.listtext_year = (TextView) findViewById(C0216R.id.listtext_year);
        this.listtext_area = (TextView) findViewById(C0216R.id.listtext_area);
        this.listtext_type.setTextSize(8.0f * rate);
        this.listtext_type.setTypeface(typeFace);
        this.listtext_year.setTextSize(8.0f * rate);
        this.listtext_year.setTypeface(typeFace);
        this.listtext_area.setTextSize(8.0f * rate);
        this.listtext_area.setTypeface(typeFace);
        this.listview_type.setSelector(getResources().getDrawable(C0216R.mipmap.se));
        this.listview_type.setOnItemClickListener(new C04671());
        this.listview_type.setOnItemSelectedListener(new C04682());
        this.listview_type.setOnFocusChangeListener(new C04693());
        Drawable drawable2 = getResources().getDrawable(C0216R.mipmap.se4);
        this.listview_year.setSelector(drawable2);
        this.listview_year.setOnFocusChangeListener(new C04704());
        this.listview_year.setOnItemClickListener(new C04715());
        this.listview_year.setOnItemSelectedListener(new C04726());
        this.listview_area.setSelector(drawable2);
        this.listview_area.setOnFocusChangeListener(new C04737());
        this.listview_area.setOnItemClickListener(new C04748());
        this.listview_area.setOnItemSelectedListener(new C04759());
        this.button_find = (Button) findViewById(C0216R.id.button_find);
        this.button_find.setTextSize(8.0f * rate);
        this.button_find.setTypeface(typeFace);
        this.button_find.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyVodClassify2View.this.iface.callback(2, MyVodClassify2View.this.select_cmd);
            }
        });
        this.button_find.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MyVodClassify2View.this.button_find.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    MyVodClassify2View.this.button_find.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
    }

    public void set_list_type(int type) {
        this.list_type = new ArrayList();
        MGplayer.MyPrintln("columner[" + type + "].type_year = " + VODplayer.columner[type].type_type);
        String[] type_names = null;
        if (VODplayer.columner[type].type_type != null && VODplayer.columner[type].type_type.length() > 1) {
            type_names = VODplayer.columner[type].type_type.split("\\|");
        }
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemID", "0");
        map0.put("ItemName", this._this.getString(C0216R.string.vodclassify_text5).toString());
        this.list_type.add(map0);
        if (type_names != null) {
            for (int i = 0; i < type_names.length; i++) {
                HashMap<String, Object> map = new HashMap();
                map.put("ItemID", String.valueOf(i + 1));
                map.put("ItemName", type_names[i]);
                this.list_type.add(map);
            }
        }
        this.adapter_type = new MySimpleAdapterTypeListView2(this._this, this.list_type, C0216R.layout.classifyitem2, new String[]{"ItemName"}, new int[]{C0216R.id.ItemName});
        this.listview_type.setAdapter(this.adapter_type);
        this.adapter_type.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                    return false;
                }
                ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                return true;
            }
        });
    }

    public void set_list_year(int type) {
        this.list_year = new ArrayList();
        MGplayer.MyPrintln("columner[" + type + "].type_year = " + VODplayer.columner[type].type_year);
        String[] type_names = null;
        if (VODplayer.columner[type].type_year != null && VODplayer.columner[type].type_year.length() > 1) {
            type_names = VODplayer.columner[type].type_year.split("\\|");
        }
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemID", "0");
        map0.put("ItemName", this._this.getString(C0216R.string.vodclassify_text5).toString());
        this.list_year.add(map0);
        if (type_names != null) {
            for (int i = 0; i < type_names.length; i++) {
                HashMap<String, Object> map = new HashMap();
                map.put("ItemID", String.valueOf(i + 1));
                map.put("ItemName", type_names[i]);
                this.list_year.add(map);
            }
        }
        this.adapter_year = new MySimpleAdapterTypeListView2(this._this, this.list_year, C0216R.layout.classifyitem2, new String[]{"ItemName"}, new int[]{C0216R.id.ItemName});
        this.listview_year.setAdapter(this.adapter_year);
        this.adapter_year.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                    return false;
                }
                ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                return true;
            }
        });
    }

    public void set_list_area(int type) {
        this.list_area = new ArrayList();
        MGplayer.MyPrintln("columner[" + type + "].type_area = " + VODplayer.columner[type].type_area);
        String[] type_names = null;
        if (VODplayer.columner[type].type_area != null && VODplayer.columner[type].type_area.length() > 1) {
            type_names = VODplayer.columner[type].type_area.split("\\|");
        }
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemID", "0");
        map0.put("ItemName", this._this.getString(C0216R.string.vodclassify_text5).toString());
        this.list_area.add(map0);
        if (type_names != null) {
            for (int i = 0; i < type_names.length; i++) {
                HashMap<String, Object> map = new HashMap();
                map.put("ItemID", String.valueOf(i + 1));
                map.put("ItemName", type_names[i]);
                this.list_area.add(map);
            }
        }
        this.adapter_area = new MySimpleAdapterTypeListView2(this._this, this.list_area, C0216R.layout.classifyitem2, new String[]{"ItemName"}, new int[]{C0216R.id.ItemName});
        this.listview_area.setAdapter(this.adapter_area);
        this.adapter_area.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                    return false;
                }
                ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                return true;
            }
        });
    }

    public void showClassifyList(int type) {
        showViewTimeout();
        listFocus();
        if (!isShown()) {
            this.select_type = "0";
            this.select_year = "0";
            this.select_area = "0";
            this.select_cmd = "&itype=0&iyear=0&iarea=0";
            this.index_type = 0;
            this.index_year = 0;
            this.index_area = 0;
            this.isShow = true;
            this.listview_index = 0;
            set_list_type(type);
            set_list_year(type);
            set_list_area(type);
            setVisibility(0);
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
                listFocus();
                return;
            }
            TranslateAnimation animation = new TranslateAnimation((float) ((-MGplayer.screenWidth) / 4), 0.0f, 0.0f, 0.0f);
            animation.setDuration(500);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    MyVodClassify2View.this.clearAnimation();
                    MyVodClassify2View.this.listFocus();
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
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    MyVodClassify2View.this.setVisibility(8);
                    MyVodClassify2View.this.iface.callback(0, null);
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

    public boolean isListLastSelect() {
        if (this.listview_index == 0) {
            if (this.list_type.size() - 1 <= this.index_type) {
                return true;
            }
            return false;
        } else if (this.listview_index == 2) {
            if (this.list_year.size() - 1 > this.index_year) {
                return false;
            }
            return true;
        } else if (this.listview_index != 1) {
            return false;
        } else {
            if (this.list_area.size() - 1 > this.index_area) {
                return false;
            }
            return true;
        }
    }

    public boolean isShown() {
        return this.isShow;
    }

    public void findButtonFocus() {
        this.button_find.setFocusable(true);
        this.button_find.setFocusableInTouchMode(true);
        this.button_find.requestFocus();
        this.button_find.requestFocusFromTouch();
    }

    public void findButtonNoFocus() {
        this.listview_type.setFocusable(false);
        this.listview_type.setFocusableInTouchMode(false);
    }

    public void listFocus() {
        if (this.listview_index == 0) {
            this.listview_type.setFocusable(true);
            this.listview_type.setFocusableInTouchMode(true);
            this.listview_type.requestFocus();
            this.listview_type.requestFocusFromTouch();
        } else if (this.listview_index == 2) {
            this.listview_year.setFocusable(true);
            this.listview_year.setFocusableInTouchMode(true);
            this.listview_year.requestFocus();
            this.listview_year.requestFocusFromTouch();
        } else if (this.listview_index == 1) {
            this.listview_area.setFocusable(true);
            this.listview_area.setFocusableInTouchMode(true);
            this.listview_area.requestFocus();
            this.listview_area.requestFocusFromTouch();
        }
    }

    public void listNoFocus() {
        if (this.listview_index == 0) {
            this.listview_type.setFocusable(false);
            this.listview_type.setFocusableInTouchMode(false);
        } else if (this.listview_index == 2) {
            this.listview_year.setFocusable(false);
            this.listview_year.setFocusableInTouchMode(false);
        } else if (this.listview_index == 1) {
            this.listview_area.setFocusable(false);
            this.listview_area.setFocusableInTouchMode(false);
        }
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
                    MGplayer.MyPrintln("list dispatchKeyEvent");
                    super.dispatchKeyEvent(event);
                    listFocus();
                    showViewTimeout();
                    return true;
                case 20:
                    MGplayer.MyPrintln("list dispatchKeyEvent");
                    if (isListLastSelect()) {
                        findButtonFocus();
                        listNoFocus();
                    } else {
                        listFocus();
                    }
                    showViewTimeout();
                    break;
                case 21:
                    this.listview_index--;
                    if (this.listview_index < 0) {
                        this.listview_index = 2;
                    }
                    listFocus();
                    showViewTimeout();
                    return true;
                case 22:
                    this.listview_index++;
                    if (this.listview_index >= 3) {
                        this.listview_index = 0;
                    }
                    listFocus();
                    showViewTimeout();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
