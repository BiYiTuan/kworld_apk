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
import java.util.Calendar;
import java.util.Date;

public class MyInfoView extends LinearLayout {
    private ImageButton FFButton = null;
    private ImageButton FRButton = null;
    private Context _this;
    private int currentTime = 0;
    private TextView dateNow = null;
    private TextView dateTotal = null;
    private int endTime = 0;
    private ListViewInterface iface = null;
    private String image = null;
    public boolean isOnLongClick = false;
    private boolean isShow = false;
    boolean miusEnable = false;
    MiusThread miusThread;
    private String name = null;
    private OnTouchListener onTouchListener = new C04007();
    PlusThread plusThread;
    private String preview = null;
    public Handler rHandler = new C03996();
    private SeekBar seekBar = null;
    private int seekBarValue = 0;
    private int startTime = 0;
    private TextView timeNow = null;
    private TextView timeTotal = null;

    /* renamed from: com.gemini.play.MyInfoView$1 */
    class C03941 implements OnSeekBarChangeListener {
        C03941() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            MGplayer.MyPrintln("SeekBar:" + progress);
            MyInfoView.this.seekBarValue = progress;
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            String data = String.valueOf(MyInfoView.this.startTime + (MyInfoView.this.seekBarValue / 10));
            MGplayer.MyPrintln("SeekBar startTime:" + MyInfoView.this.startTime + " data:" + data);
            MyInfoView.this.iface.callback(0, data);
        }
    }

    /* renamed from: com.gemini.play.MyInfoView$2 */
    class C03952 implements OnTouchListener {
        C03952() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (MyInfoView.this.urlCanBackward()) {
                MyInfoView.this.backward();
            }
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyInfoView$3 */
    class C03963 implements OnTouchListener {
        C03963() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (MyInfoView.this.urlCanBackward()) {
                MyInfoView.this.forward();
            }
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyInfoView$4 */
    class C03974 implements AnimationListener {
        C03974() {
        }

        public void onAnimationEnd(Animation animation) {
            MyInfoView.this.showViewTimeout();
            MyInfoView.this.clearAnimation();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyInfoView$5 */
    class C03985 implements AnimationListener {
        C03985() {
        }

        public void onAnimationEnd(Animation animation) {
            MyInfoView.this.setVisibility(8);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyInfoView$6 */
    class C03996 extends Handler {
        C03996() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyInfoView.this.hideInfoPanal();
                    return;
                case 1:
                    MyInfoView.this.showViewTimeout();
                    MyInfoView.this.iface.callback(0, String.valueOf(msg.getData().getInt("currentTime")));
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.MyInfoView$7 */
    class C04007 implements OnTouchListener {
        C04007() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            MGplayer.MyPrintln("Button onTouch");
            switch (v.getId()) {
                case C0216R.id.FFbutton:
                    MyInfoView.this.onTouchChange("plus", event.getAction());
                    break;
                case C0216R.id.FRbutton:
                    MyInfoView.this.onTouchChange("mius", event.getAction());
                    break;
            }
            return true;
        }
    }

    class MiusThread extends Thread {
        MiusThread() {
        }

        public void run() {
            while (MyInfoView.this.isOnLongClick) {
                try {
                    Thread.sleep(200);
                    MGplayer.MyPrintln("---");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }
    }

    class PlusThread extends Thread {
        PlusThread() {
        }

        public void run() {
            while (MyInfoView.this.isOnLongClick) {
                try {
                    Thread.sleep(200);
                    MGplayer.MyPrintln("+++");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }
    }

    public MyInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.infoview, this, true);
        init_ui();
    }

    public MyInfoView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.infoview, this, true);
        init_ui();
    }

    public MyInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.infoview, this, true);
        init_ui();
    }

    private void init_ui() {
        Typeface typeFace = MGplayer.getFontsType(this._this);
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
        this.dateTotal.setTextSize(rate * 5.0f);
        this.dateTotal.setTypeface(typeFace);
        this.timeTotal.setTextSize(rate * 8.0f);
        this.timeTotal.setTypeface(typeFace);
        this.dateNow.setTextSize(rate * 5.0f);
        this.dateNow.setTypeface(typeFace);
        this.timeNow.setTextSize(rate * 8.0f);
        this.timeNow.setTypeface(typeFace);
        this.seekBar = (SeekBar) findViewById(C0216R.id.seekBar);
        this.seekBar.setOnSeekBarChangeListener(new C03941());
        this.FRButton = (ImageButton) findViewById(C0216R.id.FRbutton);
        this.FFButton = (ImageButton) findViewById(C0216R.id.FFbutton);
        this.FRButton.setOnTouchListener(new C03952());
        this.FFButton.setOnTouchListener(new C03963());
    }

    public void showInfoPanal(String id) {
        if (!isShown()) {
            this.isShow = true;
            setVisibility(0);
            if (MGplayer.getCpuName() == null || MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
                showViewTimeout();
            } else {
                TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float) (MGplayer.screenHeight / 3), 0.0f);
                animation.setDuration(500);
                animation.setAnimationListener(new C03974());
                setFocusable(true);
                startAnimation(animation);
            }
            if (id != null && MGplayer.isNumeric(id)) {
                ImageView image = (ImageView) findViewById(C0216R.id.panal_down_image);
                TextView image_text = (TextView) findViewById(C0216R.id.panal_down_text);
                TextView preview = (TextView) findViewById(C0216R.id.info0_text1);
                String n = LIVEplayer.getVideoName(Integer.parseInt(id));
                String i = LIVEplayer.getVideoImage(Integer.parseInt(id));
                image_text.setText(n);
                String imagePath = "";
                if (MGplayer.custom().equals("quanxing")) {
                    imagePath = "/data/data/" + MGplayer.tv.getPackName() + "/temp/icon/" + i;
                } else {
                    imagePath = this._this.getFilesDir() + "/icon/" + i;
                }
                if (MGplayer.fileIsExists(imagePath)) {
                    image.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                } else {
                    image.setImageResource(C0216R.mipmap.ti);
                }
                String p = previewCurrent(LIVEplayer.getVideoIntroduction(Integer.parseInt(id)));
                if (p == null || p.length() <= 0) {
                    preview.setText(this._this.getString(C0216R.string.infoview_text1).toString());
                } else {
                    preview.setText(p);
                }
            }
            LinearLayout layout = (LinearLayout) findViewById(C0216R.id.info0_Layout_tip);
            if (LIVEplayer.currentURL == null || !(LIVEplayer.currentURL.startsWith("gemini://") || LIVEplayer.currentURL.startsWith("gp2p://"))) {
                layout.setVisibility(8);
            } else {
                layout.setVisibility(0);
            }
            if (urlCanBackward()) {
                String statues = MGplayer.mediaplayerstatue("no");
                MGplayer.MyPrintln("statues=" + statues);
                if (statues != null) {
                    statues = statues.trim();
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
                return;
            }
            Calendar calendar = Calendar.getInstance();
            if (MGplayer.seconds_prc > 0) {
                calendar.setTime(new Date(MGplayer.seconds_prc));
            }
            int year = calendar.get(1);
            int month = calendar.get(2) + 1;
            int day = calendar.get(5);
            int hour = calendar.get(11);
            int minute = calendar.get(12);
            this.seekBar.setMax(100);
            this.dateTotal.setText(year + "-" + month + "-" + day);
            this.timeTotal.setText(hour + ":" + minute);
            this.seekBar.setProgress(100);
            this.dateNow.setText(year + "-" + month + "-" + day);
            this.timeNow.setText(hour + ":" + minute);
            listFocus();
        }
    }

    public void hideInfoPanal() {
        if (isShown()) {
            this.isShow = false;
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
                setVisibility(8);
                return;
            }
            TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (MGplayer.screenHeight / 3));
            animation.setDuration(1000);
            animation.setAnimationListener(new C03985());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    private String previewCurrent(String preview) {
        if (preview != null) {
            if (!preview.equals("")) {
                Calendar calendar = Calendar.getInstance();
                if (MGplayer.seconds_prc > 0) {
                    calendar.setTime(new Date(MGplayer.seconds_prc));
                }
                int nhour = calendar.get(11);
                int nminute = calendar.get(12);
                MGplayer.MyPrintln("preview =" + preview);
                String[] previews = preview.split("\\|");
                if (previews.length <= 0) {
                    return null;
                }
                String[] items;
                int index = -1;
                for (String split : previews) {
                    items = split.split("#");
                    if (items.length >= 2) {
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
                    }
                }
                if (index < 0) {
                    return null;
                }
                items = previews[index].split("#");
                if (items.length < 2) {
                    return null;
                }
                MGplayer.MyPrintln("preview =" + items[1]);
                return items[1];
            }
        }
        return null;
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
                if (urlCanBackward()) {
                    backward();
                    break;
                }
                break;
            case 22:
                if (urlCanBackward()) {
                    forward();
                    break;
                }
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

    public boolean isShown() {
        return this.isShow;
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
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
        if (LIVEplayer.currentURL == null || (!LIVEplayer.currentURL.startsWith("gemini://") && !LIVEplayer.currentURL.startsWith("gp2p://"))) {
            return false;
        }
        return true;
    }

    private void onTouchChange(String methodName, int eventAction) {
        if ("mius".equals(methodName)) {
            if (eventAction == 0) {
                this.miusThread = new MiusThread();
                this.isOnLongClick = true;
                this.miusThread.start();
            } else if (eventAction == 1) {
                if (this.miusThread != null) {
                    this.isOnLongClick = false;
                }
            } else if (eventAction == 2 && this.miusThread != null) {
                this.isOnLongClick = true;
            }
        } else if (!"plus".equals(methodName)) {
        } else {
            if (eventAction == 0) {
                this.plusThread = new PlusThread();
                this.isOnLongClick = true;
                this.plusThread.start();
            } else if (eventAction == 1) {
                if (this.plusThread != null) {
                    this.isOnLongClick = false;
                }
            } else if (eventAction == 2 && this.plusThread != null) {
                this.isOnLongClick = true;
            }
        }
    }
}
