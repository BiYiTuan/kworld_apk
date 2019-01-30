package com.gemini.play;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyBackInfoView extends LinearLayout {
    private ImageButton FFButton;
    private ImageButton FRButton;
    private Context _this;
    private int currentTime;
    private TextView dateNow;
    private TextView dateTotal;
    private int endTime;
    private ListViewInterface iface;
    private boolean isShow;
    private String[] previewdates;
    private int previewindex;
    private String[] previewweeks;
    public Handler rHandler;
    private SeekBar seekBar;
    private int seekBarValue;
    private int startTime;
    private TextView timeNow;
    private TextView timeTotal;

    /* renamed from: com.gemini.play.MyBackInfoView$1 */
    class C03431 implements OnSeekBarChangeListener {
        C03431() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            MGplayer.MyPrintln("SeekBar:" + progress);
            MyBackInfoView.this.seekBarValue = progress;
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            String data = String.valueOf(MyBackInfoView.this.startTime + (MyBackInfoView.this.seekBarValue / 10));
            MGplayer.MyPrintln("SeekBar startTime:" + MyBackInfoView.this.startTime + " data:" + data);
            MyBackInfoView.this.iface.callback(0, data);
        }
    }

    /* renamed from: com.gemini.play.MyBackInfoView$2 */
    class C03442 implements OnTouchListener {
        C03442() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (MyBackInfoView.this.urlCanBackward()) {
                MyBackInfoView.this.backward();
            }
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyBackInfoView$3 */
    class C03453 implements OnTouchListener {
        C03453() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (MyBackInfoView.this.urlCanBackward()) {
                MyBackInfoView.this.forward();
            }
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyBackInfoView$4 */
    class C03464 implements AnimationListener {
        C03464() {
        }

        public void onAnimationEnd(Animation animation) {
            MyBackInfoView.this.clearAnimation();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyBackInfoView$5 */
    class C03475 implements AnimationListener {
        C03475() {
        }

        public void onAnimationEnd(Animation animation) {
            MyBackInfoView.this.setVisibility(8);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyBackInfoView$6 */
    class C03486 extends Handler {
        C03486() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyBackInfoView.this.hideInfoPanal();
                    return;
                case 1:
                    MyBackInfoView.this.showViewTimeout();
                    MyBackInfoView.this.iface.callback(0, String.valueOf(msg.getData().getInt("currentTime")));
                    return;
                default:
                    return;
            }
        }
    }

    public MyBackInfoView(Context context) {
        super(context);
        this.iface = null;
        this.dateTotal = null;
        this.timeTotal = null;
        this.dateNow = null;
        this.timeNow = null;
        this.seekBar = null;
        this.seekBarValue = 0;
        this.currentTime = 0;
        this.startTime = 0;
        this.endTime = 0;
        this.previewdates = new String[]{null, null, null, null, null, null, null};
        this.previewweeks = new String[]{null, null, null, null, null, null, null};
        this.previewindex = 0;
        this.isShow = false;
        this.FRButton = null;
        this.FFButton = null;
        this.rHandler = new C03486();
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backinfoview, this, true);
        init();
    }

    public MyBackInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.iface = null;
        this.dateTotal = null;
        this.timeTotal = null;
        this.dateNow = null;
        this.timeNow = null;
        this.seekBar = null;
        this.seekBarValue = 0;
        this.currentTime = 0;
        this.startTime = 0;
        this.endTime = 0;
        this.previewdates = new String[]{null, null, null, null, null, null, null};
        this.previewweeks = new String[]{null, null, null, null, null, null, null};
        this.previewindex = 0;
        this.isShow = false;
        this.FRButton = null;
        this.FFButton = null;
        this.rHandler = new C03486();
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backinfoview, this, true);
        init();
    }

    public MyBackInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.iface = null;
        this.dateTotal = null;
        this.timeTotal = null;
        this.dateNow = null;
        this.timeNow = null;
        this.seekBar = null;
        this.seekBarValue = 0;
        this.currentTime = 0;
        this.startTime = 0;
        this.endTime = 0;
        this.previewdates = new String[]{null, null, null, null, null, null, null};
        this.previewweeks = new String[]{null, null, null, null, null, null, null};
        this.previewindex = 0;
        this.isShow = false;
        this.FRButton = null;
        this.FFButton = null;
        this.rHandler = new C03486();
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backinfoview, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        float rate = MGplayer.getFontsRate();
        TextView image_text = (TextView) findViewById(C0216R.id.panal_down_text);
        image_text.setTextSize(rate * 8.0f);
        image_text.setTypeface(typeFace);
        TextView t1 = (TextView) findViewById(C0216R.id.info0_text0);
        TextView t4 = (TextView) findViewById(C0216R.id.info0_text1);
        TextView t5 = (TextView) findViewById(C0216R.id.info0_tip);
        this.dateTotal = (TextView) findViewById(C0216R.id.dateTotal);
        this.timeTotal = (TextView) findViewById(C0216R.id.timeTotal);
        this.dateNow = (TextView) findViewById(C0216R.id.dateNow);
        this.timeNow = (TextView) findViewById(C0216R.id.timeNow);
        t1.setTextSize(rate * 8.0f);
        t1.setTypeface(typeFace);
        t4.setTextSize(rate * 8.0f);
        t4.setTypeface(typeFace);
        t5.setTextSize(rate * 8.0f);
        t5.setTypeface(typeFace);
        this.dateTotal.setTextSize(rate * 4.0f);
        this.dateTotal.setTypeface(typeFace);
        this.timeTotal.setTextSize(rate * 7.0f);
        this.timeTotal.setTypeface(typeFace);
        this.dateNow.setTextSize(rate * 4.0f);
        this.dateNow.setTypeface(typeFace);
        this.timeNow.setTextSize(rate * 7.0f);
        this.timeNow.setTypeface(typeFace);
        this.seekBar = (SeekBar) findViewById(C0216R.id.seekBar);
        this.seekBar.setOnSeekBarChangeListener(new C03431());
        this.FRButton = (ImageButton) findViewById(C0216R.id.FRbutton);
        this.FFButton = (ImageButton) findViewById(C0216R.id.FFbutton);
        this.FRButton.setOnTouchListener(new C03442());
        this.FFButton.setOnTouchListener(new C03453());
        previewDate();
    }

    public void showInfoView() {
        if (!isShown()) {
            this.isShow = true;
            showViewTimeout();
            setVisibility(0);
            if (MGplayer.getCpuName() == null || MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
            } else {
                TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float) (MGplayer.screenHeight / 5), 0.0f);
                animation.setDuration(300);
                animation.setAnimationListener(new C03464());
                setFocusable(true);
                startAnimation(animation);
            }
            if (urlCanBackward()) {
                String statues = MGplayer.mediaplayerstatue("no").trim();
                if (!statues.equals("ERROR")) {
                    String[] statue = statues.split("#");
                    if (statue.length >= 3) {
                        this.seekBar.setMax((Integer.parseInt(statue[2]) * 10) - (Integer.parseInt(statue[0]) * 10));
                        this.dateTotal.setText(MGplayer.fromLongToDateString(Long.parseLong(statue[2]) * 10000, "yyyy-MM-dd"));
                        this.timeTotal.setText(MGplayer.fromLongToDateString(Long.parseLong(statue[2]) * 10000, "HH:mm"));
                        this.seekBar.setProgress((Integer.parseInt(statue[1]) * 10) - (Integer.parseInt(statue[0]) * 10));
                        this.dateNow.setText(MGplayer.fromLongToDateString(Long.parseLong(statue[1]) * 10000, "yyyy-MM-dd"));
                        this.timeNow.setText(MGplayer.fromLongToDateString(Long.parseLong(statue[1]) * 10000, "HH:mm"));
                        this.startTime = Integer.parseInt(statue[0]);
                        this.currentTime = Integer.parseInt(statue[1]);
                        this.endTime = Integer.parseInt(statue[2]);
                    }
                } else {
                    return;
                }
            }
            if (BACKplayer.currentID != null && MGplayer.isNumeric(BACKplayer.currentID)) {
                ImageView image = (ImageView) findViewById(C0216R.id.panal_down_image);
                TextView image_text = (TextView) findViewById(C0216R.id.panal_down_text);
                TextView preview = (TextView) findViewById(C0216R.id.info0_text1);
                String n = BACKplayer.playbackGetVideoName(Integer.parseInt(BACKplayer.currentID));
                String i = BACKplayer.playbackGetVideoImage(Integer.parseInt(BACKplayer.currentID));
                image_text.setText(n);
                String imagePath = this._this.getFilesDir() + "/icon/" + i;
                MGplayer.MyPrintln("imagePath = " + imagePath);
                if (MGplayer.fileIsExists(imagePath)) {
                    image.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                } else {
                    image.setImageResource(C0216R.mipmap.ti);
                }
                String p = previewCurrent();
                if (p == null || p.length() <= 0) {
                    preview.setText(this._this.getString(C0216R.string.infoview_text1).toString());
                } else {
                    preview.setText(p);
                }
            }
            listFocus();
        }
    }

    public void hideInfoPanal() {
        if (isShown()) {
            this.isShow = false;
            if (MGplayer.getCpuName() == null || MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
                setVisibility(8);
                return;
            }
            TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (MGplayer.screenHeight / 5));
            animation.setDuration(1000);
            animation.setAnimationListener(new C03475());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    private String previewCurrent() {
        int ii;
        ArrayList<PreviewsStatus> previewlist = new ArrayList();
        for (ii = 6; ii >= 0; ii--) {
            PreviewsStatus s;
            String previewss = BACKplayer.playbackGetVideoIntroductions(Integer.parseInt(BACKplayer.currentID), ii);
            MGplayer.MyPrintln("previewss:" + previewss + " currentID:" + BACKplayer.currentID);
            if (previewss != null) {
                String[] previews = previewss.split("\\|");
                for (String split : previews) {
                    s = new PreviewsStatus();
                    String[] item = split.split("#");
                    if (item.length >= 2) {
                        s.date = this.previewdates[ii] + " " + item[0] + ":00";
                        s.preivews = item[1];
                    }
                    previewlist.add(s);
                }
            }
        }
        MGplayer.MyPrintln("previewlist size:" + previewlist.size());
        if (previewlist.size() <= 0) {
            return null;
        }
        String p = null;
        for (ii = 0; ii < previewlist.size(); ii++) {
            s = (PreviewsStatus) previewlist.get(ii);
            long time = BACKplayer.fromDateStringToLong(s.date) / 10000;
            MGplayer.MyPrintln("time:" + time + " currentTime:" + this.currentTime);
            if (time < ((long) this.currentTime)) {
                p = s.preivews;
            }
        }
        return p;
    }

    private void previewDate() {
        for (int ii = 0; ii < 7; ii++) {
            Calendar calendar = Calendar.getInstance();
            if (MGplayer.seconds_prc > 0) {
                calendar.setTime(new Date(MGplayer.seconds_prc));
            }
            calendar.set(5, calendar.get(5) - ii);
            int week = calendar.get(7);
            this.previewdates[ii] = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            this.previewweeks[ii] = MGplayer.week(week);
        }
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public void listFocus() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        requestFocusFromTouch();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 21:
                backward();
                break;
            case 22:
                forward();
                break;
        }
        return super.onKeyDown(keyCode, event);
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

    public void showViewNoTimeout() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
    }

    private void forward() {
        showViewNoTimeout();
        this.currentTime += 6;
        if (this.currentTime >= this.endTime || this.currentTime <= 0) {
            this.currentTime = this.endTime;
        }
        this.seekBar.setProgress((this.currentTime * 10) - (this.startTime * 10));
        this.dateNow.setText(MGplayer.fromLongToDateString(((long) this.currentTime) * 10000, "yyyy-MM-dd"));
        this.timeNow.setText(MGplayer.fromLongToDateString(((long) this.currentTime) * 10000, "HH:mm"));
        sendMessage(this.currentTime);
    }

    private void backward() {
        showViewNoTimeout();
        this.currentTime -= 6;
        if (this.currentTime <= this.startTime || this.currentTime <= 0) {
            this.currentTime = this.startTime;
        }
        this.seekBar.setProgress((this.currentTime * 10) - (this.startTime * 10));
        this.dateNow.setText(MGplayer.fromLongToDateString(((long) this.currentTime) * 10000, "yyyy-MM-dd"));
        this.timeNow.setText(MGplayer.fromLongToDateString(((long) this.currentTime) * 10000, "HH:mm"));
        sendMessage(this.currentTime);
    }

    private void sendMessage(int time) {
        if (this.rHandler.hasMessages(1)) {
            this.rHandler.removeMessages(1);
        }
        Message msg = new Message();
        msg.what = 1;
        Bundle data = new Bundle();
        data.putInt("currentTime", time);
        msg.setData(data);
        this.rHandler.sendMessageDelayed(msg, 3000);
    }

    private boolean urlCanBackward() {
        if (BACKplayer.currentURL == null || (!BACKplayer.currentURL.startsWith("gemini://") && !BACKplayer.currentURL.startsWith("gp2p://"))) {
            return false;
        }
        return true;
    }

    public boolean isShown() {
        return this.isShow;
    }
}
