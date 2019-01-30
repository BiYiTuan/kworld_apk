package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
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
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class MyBackPreviewView extends LinearLayout {
    public static int backpreviewdaytime = 6;
    private Context _this;
    MySimpleAdapterBackPreviewView adapter;
    private ListViewInterface iface;
    private ArrayList<HashMap<String, Object>> list;
    private ListView listview;
    private String[] previewdates;
    private int previewindex;
    private String[] previewweeks;
    public Handler rHandler;
    private TextView[] textdays;

    /* renamed from: com.gemini.play.MyBackPreviewView$1 */
    class C03541 implements OnClickListener {
        C03541() {
        }

        public void onClick(View v) {
            MyBackPreviewView.this.showPreviewListDay(BACKplayer.currentID, 0);
            MyBackPreviewView.this.listFocus();
        }
    }

    /* renamed from: com.gemini.play.MyBackPreviewView$2 */
    class C03552 implements OnClickListener {
        C03552() {
        }

        public void onClick(View v) {
            MyBackPreviewView.this.showPreviewListDay(BACKplayer.currentID, 1);
            MyBackPreviewView.this.listFocus();
        }
    }

    /* renamed from: com.gemini.play.MyBackPreviewView$3 */
    class C03563 implements OnClickListener {
        C03563() {
        }

        public void onClick(View v) {
            MyBackPreviewView.this.showPreviewListDay(BACKplayer.currentID, 2);
            MyBackPreviewView.this.listFocus();
        }
    }

    /* renamed from: com.gemini.play.MyBackPreviewView$4 */
    class C03574 implements OnClickListener {
        C03574() {
        }

        public void onClick(View v) {
            MyBackPreviewView.this.showPreviewListDay(BACKplayer.currentID, 3);
            MyBackPreviewView.this.listFocus();
        }
    }

    /* renamed from: com.gemini.play.MyBackPreviewView$5 */
    class C03585 implements OnClickListener {
        C03585() {
        }

        public void onClick(View v) {
            MyBackPreviewView.this.showPreviewListDay(BACKplayer.currentID, 4);
            MyBackPreviewView.this.listFocus();
        }
    }

    /* renamed from: com.gemini.play.MyBackPreviewView$6 */
    class C03596 implements OnClickListener {
        C03596() {
        }

        public void onClick(View v) {
            MyBackPreviewView.this.showPreviewListDay(BACKplayer.currentID, 5);
            MyBackPreviewView.this.listFocus();
        }
    }

    /* renamed from: com.gemini.play.MyBackPreviewView$7 */
    class C03607 implements OnClickListener {
        C03607() {
        }

        public void onClick(View v) {
            MyBackPreviewView.this.showPreviewListDay(BACKplayer.currentID, 6);
            MyBackPreviewView.this.listFocus();
        }
    }

    /* renamed from: com.gemini.play.MyBackPreviewView$8 */
    class C03618 implements OnItemClickListener {
        C03618() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            String t = (String) ((HashMap) MyBackPreviewView.this.listview.getItemAtPosition(arg2)).get("ItemTime");
            MGplayer.MyPrintln("222 callback:" + MyBackPreviewView.this.previewdates[MyBackPreviewView.this.previewindex] + "  " + t + ":00");
            MyBackPreviewView.this.iface.callback(0, String.valueOf(MGplayer.fromDateStringToLong(MyBackPreviewView.this.previewdates[MyBackPreviewView.this.previewindex] + "  " + t + ":00") / 10000));
        }
    }

    /* renamed from: com.gemini.play.MyBackPreviewView$9 */
    class C03629 implements OnItemSelectedListener {
        C03629() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyBackPreviewView.this.iface.callback(2, (String) ((HashMap) MyBackPreviewView.this.listview.getItemAtPosition(arg2)).get("ItemTime"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public MyBackPreviewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = null;
        this.listview = null;
        this.textdays = new TextView[]{null, null, null, null, null, null, null};
        this.iface = null;
        this.list = new ArrayList();
        this.adapter = null;
        this.previewdates = new String[]{null, null, null, null, null, null, null};
        this.previewweeks = new String[]{null, null, null, null, null, null, null};
        this.previewindex = 0;
        this.rHandler = new Handler() {
            public void handleMessage(Message msg) {
                int i = msg.what;
            }
        };
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backpreviewview, this, true);
        init();
    }

    public MyBackPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = null;
        this.listview = null;
        this.textdays = new TextView[]{null, null, null, null, null, null, null};
        this.iface = null;
        this.list = new ArrayList();
        this.adapter = null;
        this.previewdates = new String[]{null, null, null, null, null, null, null};
        this.previewweeks = new String[]{null, null, null, null, null, null, null};
        this.previewindex = 0;
        this.rHandler = /* anonymous class already generated */;
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backpreviewview, this, true);
        init();
    }

    public MyBackPreviewView(Context context) {
        super(context);
        this._this = null;
        this.listview = null;
        this.textdays = new TextView[]{null, null, null, null, null, null, null};
        this.iface = null;
        this.list = new ArrayList();
        this.adapter = null;
        this.previewdates = new String[]{null, null, null, null, null, null, null};
        this.previewweeks = new String[]{null, null, null, null, null, null, null};
        this.previewindex = 0;
        this.rHandler = /* anonymous class already generated */;
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backpreviewview, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = MGplayer.getFontsType(this._this);
        float rate = MGplayer.getFontsRate();
        this.listview = (ListView) findViewById(C0216R.id.listview);
        this.listview.setSelector(ContextCompat.getDrawable(this._this, C0216R.drawable.gradient9));
        this.textdays[0] = (TextView) findViewById(C0216R.id.daytext0);
        this.textdays[1] = (TextView) findViewById(C0216R.id.daytext1);
        this.textdays[2] = (TextView) findViewById(C0216R.id.daytext2);
        this.textdays[3] = (TextView) findViewById(C0216R.id.daytext3);
        this.textdays[4] = (TextView) findViewById(C0216R.id.daytext4);
        this.textdays[5] = (TextView) findViewById(C0216R.id.daytext5);
        this.textdays[6] = (TextView) findViewById(C0216R.id.daytext6);
        this.textdays[0].setTextSize(4.0f * rate);
        this.textdays[1].setTextSize(4.0f * rate);
        this.textdays[2].setTextSize(4.0f * rate);
        this.textdays[3].setTextSize(4.0f * rate);
        this.textdays[4].setTextSize(4.0f * rate);
        this.textdays[5].setTextSize(4.0f * rate);
        this.textdays[6].setTextSize(4.0f * rate);
        this.textdays[0].setOnClickListener(new C03541());
        this.textdays[1].setOnClickListener(new C03552());
        this.textdays[2].setOnClickListener(new C03563());
        this.textdays[3].setOnClickListener(new C03574());
        this.textdays[4].setOnClickListener(new C03585());
        this.textdays[5].setOnClickListener(new C03596());
        this.textdays[6].setOnClickListener(new C03607());
        this.listview.setOnItemClickListener(new C03618());
        this.listview.setOnItemSelectedListener(new C03629());
        this.listview.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MyBackPreviewView.this.listview.setSelector(ContextCompat.getDrawable(MyBackPreviewView.this._this, C0216R.drawable.gradient9));
                    return;
                }
                MyBackPreviewView.this.listview.setSelector(ContextCompat.getDrawable(MyBackPreviewView.this._this, C0216R.color.transparent));
            }
        });
        init_list();
        previewDate();
        showTextDays(this.previewindex);
    }

    public void init_list() {
        this.adapter = new MySimpleAdapterBackPreviewView(this._this, this.list, C0216R.layout.previewitem, new String[]{"ItemTime", "ItemPriview"}, new int[]{C0216R.id.ItemTime, C0216R.id.ItemPreview});
        this.listview.setAdapter(this.adapter);
        this.adapter.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                    return false;
                }
                ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                return true;
            }
        });
    }

    private void previewDate() {
        for (int ii = 0; ii < 7; ii++) {
            Calendar calendar = Calendar.getInstance();
            if (MGplayer.seconds_prc > 0) {
                calendar.setTime(new Date(MGplayer.seconds_prc));
            }
            if (MGplayer.now_zone_prc != null) {
                calendar.setTimeZone(TimeZone.getTimeZone(MGplayer.now_zone_prc));
            }
            calendar.set(5, calendar.get(5) - ii);
            int week = calendar.get(7);
            this.previewdates[ii] = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            this.textdays[ii].setText(MGplayer.week(week));
        }
    }

    public void set_list(String preview) {
        set_list(preview, true);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void set_list(java.lang.String r19, boolean r20) {
        /*
        r18 = this;
        r0 = r18;
        r14 = r0.list;
        r14.clear();
        if (r19 == 0) goto L_0x0013;
    L_0x0009:
        r14 = "";
        r0 = r19;
        r14 = r0.equals(r14);
        if (r14 == 0) goto L_0x0035;
    L_0x0013:
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = " #";
        r14 = r14.append(r15);
        r0 = r18;
        r15 = r0._this;
        r16 = 2131427377; // 0x7f0b0031 float:1.8476369E38 double:1.0530650436E-314;
        r15 = r15.getString(r16);
        r15 = r15.toString();
        r14 = r14.append(r15);
        r19 = r14.toString();
    L_0x0035:
        r2 = java.util.Calendar.getInstance();
        r14 = com.gemini.play.MGplayer.now_zone_prc;
        if (r14 == 0) goto L_0x0046;
    L_0x003d:
        r14 = com.gemini.play.MGplayer.now_zone_prc;
        r14 = java.util.TimeZone.getTimeZone(r14);
        r2.setTimeZone(r14);
    L_0x0046:
        r14 = com.gemini.play.MGplayer.seconds_prc;
        r16 = 0;
        r14 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1));
        if (r14 <= 0) goto L_0x0058;
    L_0x004e:
        r3 = new java.util.Date;
        r14 = com.gemini.play.MGplayer.seconds_prc;
        r3.<init>(r14);
        r2.setTime(r3);
    L_0x0058:
        r14 = 11;
        r10 = r2.get(r14);
        r14 = 12;
        r11 = r2.get(r14);
        r14 = "\\|";
        r0 = r19;
        r12 = r0.split(r14);
        r14 = r12.length;
        if (r14 > 0) goto L_0x0070;
    L_0x006f:
        return;
    L_0x0070:
        r6 = -1;
        r5 = 0;
    L_0x0072:
        r14 = r12.length;
        if (r5 >= r14) goto L_0x00c6;
    L_0x0075:
        r14 = r12[r5];
        r15 = "#";
        r7 = r14.split(r15);
        r8 = new java.util.HashMap;
        r8.<init>();
        r14 = r7.length;
        r15 = 2;
        if (r14 < r15) goto L_0x00bc;
    L_0x0086:
        r14 = "ItemTime";
        r15 = 0;
        r15 = r7[r15];
        r8.put(r14, r15);
        r14 = 0;
        r14 = r7[r14];
        r15 = ":";
        r13 = r14.split(r15);
        r14 = r13.length;
        r15 = 2;
        if (r14 < r15) goto L_0x00ad;
    L_0x009b:
        r14 = 0;
        r14 = r13[r14];
        r4 = java.lang.Integer.parseInt(r14);
        r14 = 1;
        r14 = r13[r14];
        r9 = java.lang.Integer.parseInt(r14);
        if (r4 >= r10) goto L_0x00bf;
    L_0x00ab:
        r6 = r6 + 1;
    L_0x00ad:
        r14 = "ItemPriview";
        r15 = 1;
        r15 = r7[r15];
        r8.put(r14, r15);
        r0 = r18;
        r14 = r0.list;
        r14.add(r8);
    L_0x00bc:
        r5 = r5 + 1;
        goto L_0x0072;
    L_0x00bf:
        if (r4 != r10) goto L_0x00ad;
    L_0x00c1:
        if (r9 > r11) goto L_0x00ad;
    L_0x00c3:
        r6 = r6 + 1;
        goto L_0x00ad;
    L_0x00c6:
        r14 = 1;
        r0 = r20;
        if (r0 != r14) goto L_0x00da;
    L_0x00cb:
        r0 = r18;
        r14 = r0.adapter;
        r14.setCurrentIndex(r6);
    L_0x00d2:
        r0 = r18;
        r14 = r0.adapter;
        r14.notifyDataSetChanged();
        goto L_0x006f;
    L_0x00da:
        r0 = r18;
        r14 = r0.adapter;
        r15 = -1;
        r14.setCurrentIndex(r15);
        goto L_0x00d2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gemini.play.MyBackPreviewView.set_list(java.lang.String, boolean):void");
    }

    public void showPreviewList(String perview) {
        set_list(perview);
    }

    public void showTextDays(int index) {
        for (int ii = 0; ii < this.textdays.length; ii++) {
            if (index == ii) {
                this.textdays[ii].setBackground(ContextCompat.getDrawable(this._this, C0216R.drawable.gradient9));
            } else if (ii > backpreviewdaytime) {
                this.textdays[ii].setBackgroundColor(Color.rgb(123, 123, 121));
            } else {
                this.textdays[ii].setBackgroundColor(Color.rgb(0, 0, 0));
            }
        }
        this.previewindex = index;
    }

    public void showPreviewListDay(String id, int previewindex) {
        clear_list();
        MGplayer.MyPrintln("previewindex:" + previewindex);
        showTextDays(previewindex);
        String preview = null;
        if (id != null && MGplayer.isNumeric(id)) {
            preview = BACKplayer.playbackGetVideoIntroductions(Integer.parseInt(id), previewindex);
        }
        if (preview == null || preview.length() <= 4) {
            set_list(" #" + this._this.getString(C0216R.string.backlist_text3).toString(), false);
        } else if (previewindex == 0) {
            set_list(preview, true);
        } else {
            set_list(preview, false);
        }
    }

    public void showPreviewListNext(String id) {
        this.previewindex++;
        if (this.previewindex > backpreviewdaytime) {
            this.previewindex = 0;
        }
        clear_list();
        MGplayer.MyPrintln("previewindex:" + this.previewindex);
        showTextDays(this.previewindex);
        String preview = null;
        if (id != null && MGplayer.isNumeric(id)) {
            preview = BACKplayer.playbackGetVideoIntroductions(Integer.parseInt(id), this.previewindex);
        }
        if (preview == null || preview.length() <= 4) {
            set_list(" #" + this._this.getString(C0216R.string.backlist_text3).toString(), false);
        } else if (this.previewindex == 0) {
            set_list(preview, true);
        } else {
            set_list(preview, false);
        }
    }

    public void showPreviewListPre(String id) {
        this.previewindex--;
        if (this.previewindex < 0) {
            this.previewindex = backpreviewdaytime;
        }
        clear_list();
        String preview = BACKplayer.playbackGetVideoIntroductions(Integer.parseInt(id), this.previewindex);
        if (preview == null || preview.length() <= 4) {
            set_list(" #" + this._this.getString(C0216R.string.backlist_text3).toString(), false);
        } else if (this.previewindex == 0) {
            set_list(preview, true);
        } else {
            set_list(preview, false);
        }
    }

    public void clear_list() {
        this.list.clear();
        this.adapter.notifyDataSetChanged();
    }

    public void setSelection(int index) {
        this.listview.setSelection(index);
    }

    public ListView getListView() {
        return this.listview;
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

    public void listFocus() {
        this.listview.setFocusable(true);
        this.listview.setFocusableInTouchMode(true);
        this.listview.requestFocus();
        this.listview.requestFocusFromTouch();
    }

    public void listNoFocus() {
        this.listview.setSelector(ContextCompat.getDrawable(this._this, C0216R.color.transparent));
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                case 20:
                    super.dispatchKeyEvent(event);
                    listFocus();
                    return true;
                case 21:
                    this.iface.callback(3, null);
                    return true;
                case 22:
                    showPreviewListNext(BACKplayer.currentID);
                    listFocus();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
