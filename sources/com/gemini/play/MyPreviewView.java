package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class MyPreviewView extends LinearLayout {
    private Context _this;
    private MySimpleAdapterPreviewView adapter;
    private ListViewInterface iface;
    private boolean isShow;
    private ArrayList<HashMap<String, Object>> list;
    private ImageButton listdright;
    private TextView listtext;
    private ListView listview;
    private String[] previewdates;
    private int previewindex;
    private String[] previewweeks;
    public Handler rHandler;

    /* renamed from: com.gemini.play.MyPreviewView$1 */
    class C04371 implements OnItemClickListener {
        C04371() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            HashMap<String, String> map = (HashMap) MyPreviewView.this.listview.getItemAtPosition(arg2);
            String PreviewBack = (String) map.get("ItemBack");
            final long time = MGplayer.fromDateStringToLong(MyPreviewView.this.previewdates[MyPreviewView.this.previewindex] + "  " + ((String) map.get("ItemTime")) + ":00") / 10000;
            if (PreviewBack.equals("0")) {
                MyToast.makeText(MyPreviewView.this._this, MyPreviewView.this._this.getString(C0216R.string.preview_text1).toString(), 0);
            } else if (PreviewBack.equals("1")) {
                new Thread() {
                    public void run() {
                        String url = "";
                        if (MGplayer.custom().equals("quanxing")) {
                            url = MGplayer.j2(LIVEplayer.getVideoUrl(Integer.parseInt(MyListView1.currentID)));
                        } else {
                            url = LIVEplayer.getVideoUrlFromUrlss(MGplayer.ju(LIVEplayer.getVideoUrl(Integer.parseInt(MyListView1.currentID))), LIVEplayer.currentLine);
                        }
                        String statues = MyGemini.getStatue(url).trim();
                        MGplayer.MyPrintln("setOnItemClickListener statues:" + statues + " url:" + url);
                        MyPreviewView.this.showViewTimeout();
                        if (statues.equals("ERROR")) {
                            if (MyPreviewView.this.rHandler.hasMessages(3)) {
                                MyPreviewView.this.rHandler.removeMessages(3);
                            }
                            Message msg = new Message();
                            msg.what = 3;
                            Bundle data = new Bundle();
                            data.putString("data", String.valueOf(time));
                            msg.setData(data);
                            MyPreviewView.this.rHandler.sendMessageDelayed(msg, 100);
                            return;
                        }
                        String[] statue = statues.split("#");
                        if (statue.length >= 3) {
                            long time_start = Long.parseLong(statue[0]);
                            long time_end = Long.parseLong(statue[2]);
                            if (time > time_start && time < time_end) {
                                if (MyPreviewView.this.rHandler.hasMessages(3)) {
                                    MyPreviewView.this.rHandler.removeMessages(3);
                                }
                                msg = new Message();
                                msg.what = 3;
                                data = new Bundle();
                                data.putString("data", String.valueOf(time));
                                msg.setData(data);
                                MyPreviewView.this.rHandler.sendMessageDelayed(msg, 100);
                            } else if (time <= time_start) {
                                MyToast.makeText(MyPreviewView.this._this, MyPreviewView.this._this.getString(C0216R.string.preview_text1).toString(), 0);
                            } else if (time >= time_end) {
                                MyToast.makeText(MyPreviewView.this._this, MyPreviewView.this._this.getString(C0216R.string.preview_text2).toString(), 0);
                            }
                        }
                    }
                }.start();
            }
        }
    }

    /* renamed from: com.gemini.play.MyPreviewView$2 */
    class C04382 implements OnScrollListener {
        C04382() {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            MyPreviewView.this.showViewTimeout();
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            MyPreviewView.this.showViewTimeout();
        }
    }

    /* renamed from: com.gemini.play.MyPreviewView$3 */
    class C04393 implements OnItemSelectedListener {
        C04393() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            HashMap<String, String> map = (HashMap) MyPreviewView.this.listview.getItemAtPosition(arg2);
            MyPreviewView.this.showViewTimeout();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyPreviewView$4 */
    class C04404 implements OnClickListener {
        C04404() {
        }

        public void onClick(View v) {
            MyPreviewView.this.iface.callback(2, null);
            MyPreviewView.this.showViewTimeout();
        }
    }

    /* renamed from: com.gemini.play.MyPreviewView$5 */
    class C04415 implements OnClickListener {
        C04415() {
        }

        public void onClick(View v) {
            MyPreviewView.this.iface.callback(2, null);
            MyPreviewView.this.showViewTimeout();
        }
    }

    /* renamed from: com.gemini.play.MyPreviewView$6 */
    class C04426 implements ViewBinder {
        C04426() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyPreviewView$7 */
    class C04437 implements AnimationListener {
        C04437() {
        }

        public void onAnimationEnd(Animation animation) {
            MyPreviewView.this.clearAnimation();
            MyPreviewView.this.listFocus();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyPreviewView$8 */
    class C04448 implements AnimationListener {
        C04448() {
        }

        public void onAnimationEnd(Animation animation) {
            MyPreviewView.this.setVisibility(8);
            MyPreviewView.this.iface.callback(1, null);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyPreviewView$9 */
    class C04469 extends Handler {
        C04469() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyPreviewView.this.hidePreviewList();
                    return;
                case 1:
                    MyPreviewView.this.set_list(msg.getData().getString("preview"), false);
                    return;
                case 2:
                    final String url = msg.getData().getString("url");
                    final String id = msg.getData().getString(TtmlNode.ATTR_ID);
                    new Thread() {
                        public void run() {
                            int index = MyPreviewView.this.previewindex;
                            String preview = MGplayer.sendServerCmd(url, 1000);
                            if (preview.length() > 4) {
                                LIVEplayer.setVideoIntroductions(Integer.parseInt(id), index, preview);
                            }
                            if (preview.length() > 4 && index == MyPreviewView.this.previewindex) {
                                if (MyPreviewView.this.rHandler.hasMessages(1)) {
                                    MyPreviewView.this.rHandler.removeMessages(1);
                                }
                                Message msg = new Message();
                                msg.what = 1;
                                Bundle data = new Bundle();
                                data.putString("preview", preview);
                                msg.setData(data);
                                MyPreviewView.this.rHandler.sendMessageDelayed(msg, 100);
                            }
                        }
                    }.start();
                    return;
                case 3:
                    MyPreviewView.this.iface.callback(0, msg.getData().getString("data"));
                    return;
                default:
                    return;
            }
        }
    }

    public MyPreviewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = null;
        this.listview = null;
        this.listtext = null;
        this.listdright = null;
        this.iface = null;
        this.previewdates = new String[]{null, null, null, null, null, null, null};
        this.previewweeks = new String[]{null, null, null, null, null, null, null};
        this.previewindex = 0;
        this.adapter = null;
        this.list = new ArrayList();
        this.isShow = false;
        this.rHandler = new C04469();
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.previewview, this, true);
        init();
    }

    public MyPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = null;
        this.listview = null;
        this.listtext = null;
        this.listdright = null;
        this.iface = null;
        this.previewdates = new String[]{null, null, null, null, null, null, null};
        this.previewweeks = new String[]{null, null, null, null, null, null, null};
        this.previewindex = 0;
        this.adapter = null;
        this.list = new ArrayList();
        this.isShow = false;
        this.rHandler = new C04469();
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.previewview, this, true);
        init();
    }

    public MyPreviewView(Context context) {
        super(context);
        this._this = null;
        this.listview = null;
        this.listtext = null;
        this.listdright = null;
        this.iface = null;
        this.previewdates = new String[]{null, null, null, null, null, null, null};
        this.previewweeks = new String[]{null, null, null, null, null, null, null};
        this.previewindex = 0;
        this.adapter = null;
        this.list = new ArrayList();
        this.isShow = false;
        this.rHandler = new C04469();
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.previewview, this, true);
        init();
    }

    private void init() {
        previewDate();
        Typeface typeFace = MGplayer.getFontsType(this._this);
        float rate = MGplayer.getFontsRate();
        this.listview = (ListView) findViewById(C0216R.id.listview);
        this.listtext = (TextView) findViewById(C0216R.id.listtext);
        this.listdright = (ImageButton) findViewById(C0216R.id.dright);
        this.listview.setOnItemClickListener(new C04371());
        this.listview.setOnScrollListener(new C04382());
        this.listview.setOnItemSelectedListener(new C04393());
        this.listdright.setOnClickListener(new C04404());
        this.listtext.setOnClickListener(new C04415());
        this.listview.setSelector(ContextCompat.getDrawable(this._this, C0216R.drawable.gradient9));
        this.listtext.setTextSize(8.0f * rate);
        this.listtext.setTypeface(typeFace);
        this.adapter = new MySimpleAdapterPreviewView(this._this, this.list, C0216R.layout.previewitem, new String[]{"ItemTime", "ItemPriview", "ItemImage"}, new int[]{C0216R.id.ItemTime, C0216R.id.ItemPreview, C0216R.id.ItemImage});
        this.listview.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C04426());
    }

    public void clear_list() {
        this.list.clear();
        this.adapter.notifyDataSetChanged();
    }

    public void set_list(String preview) {
        set_list(preview, true);
    }

    public void set_list(String preview, boolean timeout) {
        if (preview != null && !preview.equals("")) {
            Calendar calendar = Calendar.getInstance();
            if (MGplayer.now_zone_prc != null) {
                calendar.setTimeZone(TimeZone.getTimeZone(MGplayer.now_zone_prc));
            }
            if (MGplayer.seconds_prc > 0) {
                calendar.setTime(new Date(MGplayer.seconds_prc));
            }
            int nhour = calendar.get(11);
            int nminute = calendar.get(12);
            String[] previews = preview.split("\\|");
            if (previews.length > 0) {
                String url;
                int index = -1;
                if (MGplayer.custom().equals("quanxing")) {
                    url = MGplayer.j2(LIVEplayer.getVideoUrl(Integer.parseInt(MyListView1.currentID)));
                } else {
                    url = LIVEplayer.getVideoUrlFromUrlss(MGplayer.ju(LIVEplayer.getVideoUrl(Integer.parseInt(MyListView1.currentID))), 0);
                }
                String statues = null;
                if (url != null && url.equals(LIVEplayer.currentURL)) {
                    statues = MGplayer.mediaplayerstatue("no").trim();
                } else if (url != null) {
                    String day = MyGemini.getUrlParam(url, "day");
                    MGplayer.MyPrintln("day:" + day);
                    if (day != null) {
                        statues = (((calendar.getTimeInMillis() / 1000) - ((long) ((Integer.parseInt(day) * 24) * 2600))) / 10) + "#" + 0 + "#" + (calendar.getTimeInMillis() / 10000);
                    }
                }
                MGplayer.MyPrintln("status:" + statues + "url:" + url);
                this.list.clear();
                for (String split : previews) {
                    String[] items = split.split("#");
                    HashMap<String, Object> map = new HashMap();
                    if (items.length >= 2) {
                        map.put("ItemTime", items[0]);
                        String[] times = items[0].split(":");
                        if (times.length >= 2) {
                            int hour = Integer.parseInt(times[0]);
                            int minute = Integer.parseInt(times[1]);
                            if (hour < nhour) {
                                index++;
                            } else if (hour == nhour && minute <= nminute) {
                                index++;
                            }
                        }
                        map.put("ItemPriview", items[1]);
                        if (statues != null) {
                            String[] statue = statues.split("#");
                            if (statue.length >= 3 || (MGplayer.isNumeric(statue[0]) && MGplayer.isNumeric(statue[2]))) {
                                long time = MGplayer.fromDateStringToLong(this.previewdates[this.previewindex] + " " + times[0] + ":" + times[1] + ":00") / 10000;
                                long time_start = Long.parseLong(statue[0]);
                                long time_end = Long.parseLong(statue[2]);
                                if (time <= time_start || time >= time_end) {
                                    map.put("ItemBack", "0");
                                } else {
                                    map.put("ItemImage", Integer.valueOf(C0216R.mipmap.bp));
                                    map.put("ItemBack", "1");
                                }
                            } else {
                                map.put("ItemBack", "-1");
                            }
                        } else {
                            map.put("ItemBack", "-1");
                        }
                        this.list.add(map);
                    }
                }
                if (timeout) {
                    this.adapter.setCurrentIndex(index);
                    this.listview.setSelection(index);
                } else {
                    this.adapter.setCurrentIndex(-1);
                }
                this.adapter.notifyDataSetChanged();
                listFocus();
            }
        }
    }

    public void showPreviewList(int id) {
        this.previewindex = 0;
        String preview = LIVEplayer.getVideoIntroduction(id);
        clear_list();
        scrollPreviewList(this.previewindex);
        set_list(preview);
    }

    public void showPreviewListNext(String id) {
    }

    public void scrollPreviewList(int date) {
        this.listtext.setText(this._this.getString(C0216R.string.liveplayer_text3) + " " + this.previewdates[date] + " " + this.previewweeks[date] + " ");
        showViewTimeout();
        if (!isShown()) {
            this.isShow = true;
            setVisibility(0);
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
                listFocus();
                return;
            }
            TranslateAnimation animation = new TranslateAnimation((float) ((MGplayer.screenWidth / 3) * 2), 0.0f, 0.0f, 0.0f);
            animation.setDuration(500);
            animation.setAnimationListener(new C04437());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void hidePreviewList() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        if (isShown()) {
            this.isShow = false;
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
                setVisibility(8);
                this.iface.callback(1, null);
                return;
            }
            TranslateAnimation animation = new TranslateAnimation(0.0f, (float) ((MGplayer.screenWidth / 3) * 2), 0.0f, 0.0f);
            animation.setDuration(1000);
            animation.setAnimationListener(new C04448());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void setSelection(int index) {
        this.listview.setSelection(index);
    }

    public ListView getListView() {
        return this.listview;
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
            this.previewweeks[ii] = MGplayer.week(week);
        }
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
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
