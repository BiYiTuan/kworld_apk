package com.gemini.play;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.util.Formatter;
import java.util.Locale;

public class MyVodInfoView extends LinearLayout {
    private Context _this = null;
    private ListViewInterface iface = null;
    private ImageView lineview = null;
    public Handler rHandler = new C05245();
    private int seekBarValue;
    private SeekBar seekbar_progress;
    private TextView text_progress;
    private TextView text_tip;
    private TextView text_title;
    private TextView text_total;

    /* renamed from: com.gemini.play.MyVodInfoView$1 */
    class C05201 implements OnSeekBarChangeListener {
        C05201() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            MGplayer.MyPrintln("SeekBar:" + progress);
            MyVodInfoView.this.seekBarValue = progress;
            MyVodInfoView.this.text_progress.setText(MyVodInfoView.this.intToTime(progress));
            MyVodInfoView.this.showViewTimeout();
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (MyVodInfoView.this.seekBarValue > 0) {
                MyVodInfoView.this.iface.callback(0, String.valueOf(MyVodInfoView.this.seekBarValue));
            }
            MyVodInfoView.this.seekBarValue = 0;
        }
    }

    /* renamed from: com.gemini.play.MyVodInfoView$2 */
    class C05212 implements OnClickListener {
        C05212() {
        }

        public void onClick(View v) {
            MGplayer.MyPrintln("lineview.setOnClickListener");
            MyVodInfoView.this.iface.callback(1, null);
        }
    }

    /* renamed from: com.gemini.play.MyVodInfoView$3 */
    class C05223 implements AnimationListener {
        C05223() {
        }

        public void onAnimationEnd(Animation animation) {
            int left = MyVodInfoView.this.getLeft();
            int top = MyVodInfoView.this.getTop();
            int width = MyVodInfoView.this.getWidth();
            int height = MyVodInfoView.this.getHeight();
            MyVodInfoView.this.clearAnimation();
            MyVodInfoView.this.layout(left, top, left + width, top + height);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyVodInfoView$4 */
    class C05234 implements AnimationListener {
        C05234() {
        }

        public void onAnimationEnd(Animation animation) {
            MyVodInfoView.this.setVisibility(8);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyVodInfoView$5 */
    class C05245 extends Handler {
        C05245() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyVodInfoView.this.hideInfoPanal();
                    return;
                case 1:
                    MyVodInfoView.this.showViewTimeout();
                    MyVodInfoView.this.iface.callback(0, String.valueOf(msg.getData().getInt("currentTime")));
                    return;
                default:
                    return;
            }
        }
    }

    public MyVodInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodinfoview, this, true);
        init();
    }

    public MyVodInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodinfoview, this, true);
        init();
    }

    public MyVodInfoView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodinfoview, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        float rate = MGplayer.getFontsRate();
        this.text_title = (TextView) findViewById(C0216R.id.info0_text2);
        this.text_tip = (TextView) findViewById(C0216R.id.info0_text1);
        this.text_progress = (TextView) findViewById(C0216R.id.info1_text0);
        this.text_total = (TextView) findViewById(C0216R.id.info1_text1);
        this.seekbar_progress = (SeekBar) findViewById(C0216R.id.info1_progressBar);
        this.lineview = (ImageView) findViewById(C0216R.id.info_line);
        this.seekbar_progress.setOnSeekBarChangeListener(new C05201());
        this.lineview.setOnClickListener(new C05212());
        this.text_progress.setTextSize(rate * 8.0f);
        this.text_progress.setTypeface(typeFace);
        this.text_total.setTextSize(rate * 8.0f);
        this.text_total.setTypeface(typeFace);
        this.text_title.setTextSize(rate * 8.0f);
        this.text_title.setTypeface(typeFace);
        this.text_tip.setTextSize(rate * 8.0f);
        this.text_tip.setTypeface(typeFace);
    }

    public void setProgress() {
        if (VODplayer.video_url != null) {
            int progress = VODplayer.getProgress(VODplayer.video_url);
            int total = VODplayer.getTotal(VODplayer.video_url);
            if (progress > total) {
                progress = total;
            }
            this.text_progress.setText(intToTime(progress));
            this.text_total.setText(intToTime(total));
            this.seekbar_progress.setMax(total);
            this.seekbar_progress.setProgress(progress);
        }
    }

    public void setTitle(String title) {
        this.text_title.setText(title);
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

    public void showInfoView() {
        if (!isShown()) {
            setProgress();
            showViewTimeout();
            setVisibility(0);
            TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float) (MGplayer.screenHeight / 5), 0.0f);
            animation.setDuration(500);
            animation.setAnimationListener(new C05223());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void hideInfoPanal() {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (MGplayer.screenHeight / 5));
        animation.setDuration(1000);
        animation.setAnimationListener(new C05234());
        setFocusable(true);
        startAnimation(animation);
    }

    private String intToTime(int timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
        return mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
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

    public void backward() {
        int progress = VODplayer.backward(VODplayer.video_url, this.rHandler);
        this.seekbar_progress.setProgress(progress);
        this.text_progress.setText(intToTime(progress));
        showViewTimeout();
    }

    public void forward() {
        int progress = VODplayer.forward(VODplayer.video_url, this.rHandler);
        this.seekbar_progress.setProgress(progress);
        this.text_progress.setText(intToTime(progress));
        showViewTimeout();
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
}
