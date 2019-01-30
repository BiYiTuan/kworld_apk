package com.gemini.play;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import com.google.android.exoplayer.hls.HlsChunkSource;
import org.videolan.vlc.VlcVideoView;

public class BackPlayerVideoActivity extends Activity implements OnGestureListener {
    private String back_id = null;
    private String back_time = null;
    private Button backbutton = null;
    private LinearLayout backlayout = null;
    private GestureDetector detector = null;
    private MyBackInfoView infoview = null;
    private boolean isexit = false;
    public ControlVideoInterface onControlVideo = new C09015();
    public ListViewInterface onInfoPressed = new C09026();
    public ScrollViewInterface onScrollView = new C09039();
    private ScrollTextView onescroller = null;
    public Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String content = msg.getData().getString("content");
                    int times = msg.getData().getInt("times");
                    BackPlayerVideoActivity.this.onescroller.setText("");
                    BackPlayerVideoActivity.this.onescroller.setVisibility(0);
                    BackPlayerVideoActivity.this.onescroller.start_every(BackPlayerVideoActivity.this, content, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", times);
                    return;
                default:
                    return;
            }
        }
    };

    /* renamed from: com.gemini.play.BackPlayerVideoActivity$1 */
    class C02601 implements Runnable {
        C02601() {
        }

        public void run() {
            if (BackPlayerVideoActivity.this.back_time == null) {
                BACKplayer.playVideo(BackPlayerVideoActivity.this, BackPlayerVideoActivity.this.back_id, false, 0);
                return;
            }
            BACKplayer.playVideo(BackPlayerVideoActivity.this, BackPlayerVideoActivity.this.back_id, false, Integer.parseInt(BackPlayerVideoActivity.this.back_time));
        }
    }

    /* renamed from: com.gemini.play.BackPlayerVideoActivity$3 */
    class C02623 implements OnClickListener {
        C02623() {
        }

        public void onClick(View v) {
            BackPlayerVideoActivity.this.exitActivity();
        }
    }

    /* renamed from: com.gemini.play.BackPlayerVideoActivity$4 */
    class C02634 implements Runnable {
        C02634() {
        }

        public void run() {
            BackPlayerVideoActivity.this.backlayout.setVisibility(8);
        }
    }

    /* renamed from: com.gemini.play.BackPlayerVideoActivity$7 */
    class C02647 implements Runnable {
        C02647() {
        }

        public void run() {
            BackPlayerVideoActivity.this.finish();
        }
    }

    /* renamed from: com.gemini.play.BackPlayerVideoActivity$8 */
    class C02658 implements Runnable {
        C02658() {
        }

        public void run() {
            BackPlayerVideoActivity.this.isexit = false;
        }
    }

    /* renamed from: com.gemini.play.BackPlayerVideoActivity$5 */
    class C09015 implements ControlVideoInterface {
        C09015() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(BackPlayerVideoActivity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    BackPlayerVideoActivity.this.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.BackPlayerVideoActivity$6 */
    class C09026 implements ListViewInterface {
        C09026() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    if (MGplayer.isNumeric(data) && BACKplayer.currentID != null) {
                        BACKplayer.playVideo(BackPlayerVideoActivity.this, BACKplayer.currentID, false, Integer.parseInt(data));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.BackPlayerVideoActivity$9 */
    class C09039 implements ScrollViewInterface {
        C09039() {
        }

        public void callback(int cmd, String content, int times) {
            switch (cmd) {
                case 0:
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("content", content);
                    data.putInt("times", times);
                    msg.setData(data);
                    msg.what = 1;
                    BackPlayerVideoActivity.this.rHandler.sendMessage(msg);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.backvideo);
        getWindow().setFlags(1024, 1024);
        setResult(-1, getIntent());
        this.back_id = getIntent().getStringExtra(TtmlNode.ATTR_ID);
        this.back_time = getIntent().getStringExtra("time");
        this.detector = new GestureDetector(this, this);
        BACKplayer.VideoViewS = (VlcVideoView) findViewById(C0216R.id.VideoViewSoft);
        BACKplayer.VideoViewH = (VideoView) findViewById(C0216R.id.VideoViewHard);
        this.infoview = (MyBackInfoView) findViewById(C0216R.id.myinfoview);
        this.infoview.setInterface(this.onInfoPressed);
        MGplayer.video_every_interface(this.onControlVideo);
        this.backlayout = (LinearLayout) findViewById(C0216R.id.backlayout);
        this.onescroller = (ScrollTextView) findViewById(C0216R.id.onescrolltext);
        init_ui();
        if (this.back_id != null) {
            new Handler().postDelayed(new C02601(), 1000);
        }
        BACKplayer.checkVideo(this);
        videoSetFocus();
    }

    private void init_ui() {
        Typeface typeFace = MGplayer.getFontsType(this);
        float rate = MGplayer.getFontsRate();
        FrameLayout progressBar = (FrameLayout) findViewById(C0216R.id.progressBar);
        LayoutParams layoutParams = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
        layoutParams.width = (int) (54.0f * rate);
        layoutParams.height = (int) (38.0f * rate);
        progressBar.setLayoutParams(layoutParams);
        TextView speedView = (TextView) findViewById(C0216R.id.progressSpeed);
        speedView.setTextSize(7.0f * rate);
        speedView.setTypeface(typeFace);
        Handler mHandler = new Handler();
        final FrameLayout frameLayout = progressBar;
        final TextView textView = speedView;
        final Handler handler = mHandler;
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (BACKplayer.isPlaying()) {
                    frameLayout.setVisibility(8);
                } else {
                    frameLayout.setVisibility(0);
                }
                textView.setText(MGplayer.getUidRxBytes() + "K/S");
                handler.postDelayed(this, 1000);
            }
        }, 1000);
        this.backbutton = (Button) findViewById(C0216R.id.backbutton);
        this.backbutton.setTextSize(8.0f * rate);
        this.backbutton.setTypeface(typeFace);
        this.backbutton.setOnClickListener(new C02623());
        ScrollTextView scroller = (ScrollTextView) findViewById(C0216R.id.scrolltext);
        scroller.init(getWindowManager());
        if (MGplayer.scrolltext != null) {
            MGplayer.MyPrintln("start scrolltext");
            scroller.setText("");
            scroller.start(this, MGplayer.scrolltext, 0, 0, 0, 0, 2.5f, (int) (12.0f * MGplayer.getFontsRate()), "FFFFFF");
        }
        MGplayer.scroll_every_interface(this.onScrollView);
        if (MGplayer.onescroll_txt != null && MGplayer.onescroll_txt.length() > 0) {
            this.onescroller.init(getWindowManager());
            MGplayer.MyPrintln("start onescroll_txt");
            this.onescroller.setText("");
            this.onescroller.setVisibility(0);
            this.onescroller.start(this, MGplayer.onescroll_txt, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", MGplayer.onescroll_times);
        }
    }

    protected void onDestroy() {
        MGplayer.MyPrintln("onDestroy");
        stopVideo();
        super.onDestroy();
    }

    protected void onRestart() {
        MGplayer.MyPrintln("onRestart");
        super.onRestart();
    }

    protected void onPause() {
        MGplayer.MyPrintln("onPause");
        stopVideo();
        super.onPause();
    }

    private void showBacklayout() {
        if (!this.backlayout.isShown()) {
            this.backlayout.setVisibility(0);
            new Handler().postDelayed(new C02634(), HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        showBacklayout();
        this.infoview.showInfoView();
        return this.detector.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {
        this.infoview.showInfoView();
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() <= 20.0f && e2.getX() - e1.getX() <= 20.0f && e1.getY() - e2.getY() <= 20.0f && e2.getY() - e1.getY() > 20.0f) {
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

    private boolean hideAllView() {
        int v = 0;
        if (this.infoview.isShown()) {
            this.infoview.hideInfoPanal();
            v = 1;
        }
        if (v <= 0) {
            return false;
        }
        videoSetFocus();
        return true;
    }

    protected void onResume() {
        MGplayer.video_every_interface(this.onControlVideo);
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case 4:
                if (hideAllView()) {
                    return true;
                }
                if (this.isexit) {
                    stopVideo();
                    BACKplayer.stopCheckVideo();
                    this.isexit = false;
                    new Handler().postDelayed(new C02647(), 1000);
                    return true;
                }
                this.isexit = true;
                MyToast.makeText(this, getString(C0216R.string.isexit).toString(), 0);
                new Handler().postDelayed(new C02658(), HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                return true;
            case 21:
                this.infoview.showInfoView();
                showBacklayout();
                break;
            case 22:
                this.infoview.showInfoView();
                showBacklayout();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void stopVideoForHard(VideoView mVideoView) {
        MGplayer.mediaplayerunload();
        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
        mVideoView.pause();
    }

    public static void stopVideoForSoft(VlcVideoView mVideoView) {
        MGplayer.mediaplayerunload();
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        } else {
            mVideoView.pause();
        }
    }

    public void stopVideo() {
        if (MGplayer.getDecode() == 0) {
            stopVideoForHard(BACKplayer.VideoViewH);
        } else {
            stopVideoForSoft(BACKplayer.VideoViewS);
        }
    }

    private void videoSetFocus() {
        if (MGplayer.getDecode() == 0) {
            BACKplayer.VideoViewH.setFocusable(true);
            BACKplayer.VideoViewH.setFocusableInTouchMode(true);
            BACKplayer.VideoViewH.requestFocus();
            BACKplayer.VideoViewH.requestFocusFromTouch();
        } else if (BACKplayer.VideoViewS != null) {
            BACKplayer.VideoViewS.setFocusable(true);
            BACKplayer.VideoViewS.setFocusableInTouchMode(true);
            BACKplayer.VideoViewS.requestFocus();
            BACKplayer.VideoViewS.requestFocusFromTouch();
        }
    }

    public void exitActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.myhomebar_text6).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BackPlayerVideoActivity.this.stopVideo();
                BACKplayer.stopCheckVideo();
                BackPlayerVideoActivity.this.finish();
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
