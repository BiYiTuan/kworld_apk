package com.gemini.play;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import com.google.android.exoplayer.hls.HlsChunkSource;
import org.videolan.vlc.VlcVideoView;

public class BackPlayer2Activity extends Activity implements OnGestureListener {
    private String back_id = null;
    private String back_time = null;
    private Button backbutton = null;
    private LinearLayout backlayout = null;
    private LinearLayout backlistlayout = null;
    private GestureDetector detector = null;
    private MyBackInfoView infoview = null;
    private boolean isexit = false;
    private MyBackListView listview = null;
    public ControlVideoInterface onControlVideo = new C08969();
    public ListViewInterface onInfoPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    if (MGplayer.isNumeric(data) && BACKplayer.currentID != null) {
                        BACKplayer.playVideo(BackPlayer2Activity.this, BACKplayer.currentID, false, Integer.parseInt(data));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onListPressed = new C08935();
    public ListViewInterface onPreviewPressed = new C08946();
    public ScrollViewInterface onScrollView = new ScrollViewInterface() {
        public void callback(int cmd, String content, int times) {
            switch (cmd) {
                case 0:
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("content", content);
                    data.putInt("times", times);
                    msg.setData(data);
                    msg.what = 1;
                    BackPlayer2Activity.this.rHandler.sendMessage(msg);
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onTypePressed = new C08957();
    private ScrollTextView onescroller = null;
    private MyBackPreviewView previewview = null;
    public Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    BackPlayer2Activity.this.hideBackListLayout();
                    return;
                case 1:
                    String content = msg.getData().getString("content");
                    int times = msg.getData().getInt("times");
                    BackPlayer2Activity.this.onescroller.setText("");
                    BackPlayer2Activity.this.onescroller.setVisibility(0);
                    BackPlayer2Activity.this.onescroller.start_every(BackPlayer2Activity.this, content, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", times);
                    return;
                default:
                    return;
            }
        }
    };
    private ImageButton typebutton = null;
    private MyBackTypeView typeview = null;
    private int view_index = 0;

    /* renamed from: com.gemini.play.BackPlayer2Activity$1 */
    class C02501 implements OnPreparedListener {
        C02501() {
        }

        public void onPrepared(MediaPlayer mediaPlayer) {
            MGplayer.MyPrintln("MediaPlayer is prepared and start");
            mediaPlayer.start();
        }
    }

    /* renamed from: com.gemini.play.BackPlayer2Activity$2 */
    class C02512 implements Runnable {
        C02512() {
        }

        public void run() {
            BackPlayer2Activity.this.showBackListLayout();
        }
    }

    /* renamed from: com.gemini.play.BackPlayer2Activity$4 */
    class C02534 implements OnClickListener {
        C02534() {
        }

        public void onClick(View v) {
            BackPlayer2Activity.this.exitActivity();
        }
    }

    /* renamed from: com.gemini.play.BackPlayer2Activity$8 */
    class C02548 implements Runnable {
        C02548() {
        }

        public void run() {
            BackPlayer2Activity.this.backlayout.setVisibility(8);
        }
    }

    /* renamed from: com.gemini.play.BackPlayer2Activity$5 */
    class C08935 implements ListViewInterface {
        C08935() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    BACKplayer.currentID = data;
                    BACKplayer.playVideoFull(BackPlayer2Activity.this, null);
                    break;
                case 1:
                    BackPlayer2Activity.this.typeview.showTypeList();
                    return;
                case 2:
                    break;
                case 3:
                    BackPlayer2Activity.this.previewview.listFocus();
                    MGplayer.MyPrintln("onPreviewPressed listFocus");
                    return;
                default:
                    return;
            }
            MGplayer.MyPrintln("onPreviewPressed onListPressed cmd = " + cmd);
            if (MGplayer.isNumeric(data)) {
                String preview = BACKplayer.playbackGetVideoIntroduction(Integer.parseInt(data));
                BACKplayer.currentID = data;
                BackPlayer2Activity.this.previewview.showTextDays(0);
                BackPlayer2Activity.this.previewview.showPreviewList(preview);
                MGplayer.MyPrintln("onPreviewPressed preview = " + preview);
            }
        }
    }

    /* renamed from: com.gemini.play.BackPlayer2Activity$6 */
    class C08946 implements ListViewInterface {
        C08946() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    if (MGplayer.isNumeric(data)) {
                        MGplayer.MyPrintln("111 callback:" + data);
                        BACKplayer.playVideo(BackPlayer2Activity.this, BACKplayer.currentID, false, Integer.parseInt(data));
                        BackPlayer2Activity.this.hideBackListLayout();
                        BackPlayer2Activity.this.typeview.hideTypeList();
                        return;
                    }
                    return;
                case 3:
                    BackPlayer2Activity.this.listFocus();
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.BackPlayer2Activity$7 */
    class C08957 implements ListViewInterface {
        C08957() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    BackPlayer2Activity.this.listview.showListView(data);
                    BackPlayer2Activity.this.typeview.hideTypeList();
                    return;
                case 1:
                    BackPlayer2Activity.this.listview.listFocus();
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.BackPlayer2Activity$9 */
    class C08969 implements ControlVideoInterface {
        C08969() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(BackPlayer2Activity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    BackPlayer2Activity.this.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.backvideo2);
        getWindow().setFlags(1024, 1024);
        setResult(-1, getIntent());
        this.back_id = BACKplayer.getCurrentID();
        MGplayer.mediaplayervodheader(0);
        this.detector = new GestureDetector(this, this);
        BACKplayer.VideoViewS = (VlcVideoView) findViewById(C0216R.id.VideoViewSoft);
        BACKplayer.VideoViewH = (VideoView) findViewById(C0216R.id.VideoViewHard);
        BACKplayer.VideoViewH.setOnPreparedListener(new C02501());
        this.infoview = (MyBackInfoView) findViewById(C0216R.id.myinfoview);
        this.infoview.setInterface(this.onInfoPressed);
        MGplayer.video_every_interface(this.onControlVideo);
        this.backlayout = (LinearLayout) findViewById(C0216R.id.backlayout);
        this.backlistlayout = (LinearLayout) findViewById(C0216R.id.backlistlayout);
        this.onescroller = (ScrollTextView) findViewById(C0216R.id.onescrolltext);
        init_ui();
        BACKplayer.checkVideo(this);
        new Handler().postDelayed(new C02512(), 1000);
    }

    private void init_ui() {
        Typeface typeFace = MGplayer.getFontsType(this);
        float rate = MGplayer.getFontsRate();
        ProgressBar progressBar = (ProgressBar) findViewById(C0216R.id.progressBar);
        LayoutParams layoutParams = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
        layoutParams.width = (int) (48.0f * rate);
        layoutParams.height = (int) (48.0f * rate);
        progressBar.setLayoutParams(layoutParams);
        TextView speedView = (TextView) findViewById(C0216R.id.progressSpeed);
        speedView.setTextSize(7.0f * rate);
        speedView.setTypeface(typeFace);
        FrameLayout progressLayout = (FrameLayout) findViewById(C0216R.id.progressLayout);
        Handler mHandler = new Handler();
        final FrameLayout frameLayout = progressLayout;
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
        this.backbutton.setOnClickListener(new C02534());
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
        this.previewview = (MyBackPreviewView) findViewById(C0216R.id.mybackpreviewview);
        this.previewview.setInterface(this.onPreviewPressed);
        this.previewview.listNoFocus();
        this.listview = (MyBackListView) findViewById(C0216R.id.mybacklistview);
        this.listview.setInterface(this.onListPressed);
        this.listview.listFocus();
        this.typeview = (MyBackTypeView) findViewById(C0216R.id.mytypeview);
        this.typeview.setInterface(this.onTypePressed);
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
            new Handler().postDelayed(new C02548(), HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        showBackListLayout();
        this.infoview.setVisibility(8);
        return this.detector.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {
        this.infoview.showInfoView();
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 20.0f) {
            this.typeview.showTypeList();
        } else if (e2.getX() - e1.getX() > 20.0f) {
            this.typeview.showTypeList();
        } else if (e1.getY() - e2.getY() <= 20.0f && e2.getY() - e1.getY() > 20.0f) {
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
        if (this.typeview.isShown()) {
            this.typeview.setVisibility(8);
            listFocus();
            this.view_index = 0;
            v = 1;
        } else if (this.backlistlayout.isShown()) {
            hideBackListLayout();
            v = 1;
        }
        if (v > 0) {
            return true;
        }
        return false;
    }

    protected void onResume() {
        MGplayer.video_every_interface(this.onControlVideo);
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MGplayer.MyPrintln("onKeyDown code = " + event.getKeyCode());
        switch (event.getKeyCode()) {
            case 4:
                if (hideAllView()) {
                    return true;
                }
                if (this.isexit) {
                    BACKplayer.stopCheckVideo();
                    stopVideo();
                    this.isexit = false;
                    MyToast.makeText(this, getString(C0216R.string.liveplayer_text12).toString(), 1);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            BackPlayer2Activity.this.finish();
                        }
                    }, 3000);
                    return true;
                }
                this.isexit = true;
                MyToast.makeText(this, getString(C0216R.string.isexit).toString(), 0);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        BackPlayer2Activity.this.isexit = false;
                    }
                }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                return true;
            case 21:
                if (!this.backlistlayout.isShown()) {
                    this.infoview.showInfoView();
                    break;
                }
                showBackListLayout();
                if (!this.typeview.isShown()) {
                    if (this.view_index != 2) {
                        if (this.view_index == 0) {
                            this.typeview.showTypeList();
                            this.view_index = 1;
                            break;
                        }
                    }
                    listFocus();
                    this.view_index = 0;
                    break;
                }
                this.typeview.setVisibility(8);
                listFocus();
                this.view_index = 0;
                break;
                break;
            case 22:
                if (this.backlistlayout.isShown()) {
                    if (!this.typeview.isShown()) {
                        showBackListLayout();
                        if (this.view_index != 1) {
                            if (this.view_index == 0) {
                                previewFocus();
                                this.view_index = 2;
                                break;
                            }
                        }
                        listFocus();
                        this.view_index = 0;
                        break;
                    }
                    this.typeview.setVisibility(8);
                    listFocus();
                    this.view_index = 0;
                    break;
                }
                this.infoview.showInfoView();
                break;
                break;
            case 23:
            case 66:
                showBackListLayout();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void stopVideoForHard(VideoView mVideoView) {
        BACKplayer.player_isexit = true;
        MGplayer.mediaplayerunload();
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        }
    }

    public static void stopVideoForSoft(VlcVideoView mVideoView) {
        BACKplayer.player_isexit = true;
        MGplayer.mediaplayerunload();
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            mVideoView.onDestory();
        } else {
            mVideoView.pause();
            mVideoView.onDestory();
        }
    }

    public void stopVideo() {
        if (MGplayer.getDecode() == 0) {
            stopVideoForHard(BACKplayer.VideoViewH);
        } else {
            stopVideoForSoft(BACKplayer.VideoViewS);
        }
    }

    public void hideBackListLayout() {
        this.backlistlayout.setVisibility(8);
    }

    public void hideViewTimeout() {
        Message msg = new Message();
        msg.what = 0;
        this.rHandler.sendMessageDelayed(msg, 30000);
    }

    public void setListviewSelection() {
        boolean z = false;
        if (MGplayer.isNumeric(BACKplayer.getCurrentID())) {
            int num;
            if (LIVEplayer.show_ps_playlist) {
                num = BACKplayer.playbackGetVideoNum(Integer.parseInt(BACKplayer.getCurrentID()), false);
            } else {
                int parseInt = Integer.parseInt(BACKplayer.getCurrentID());
                if (!BACKplayer.typePasswordOK) {
                    z = true;
                }
                num = BACKplayer.playbackGetVideoNum(parseInt, z);
            }
            final int i = num;
            MGplayer.MyPrintln("1 LIVEplayer.getCurrentID():" + LIVEplayer.getCurrentID() + "setSelection:" + i);
            this.listview.post(new Runnable() {
                public void run() {
                    BackPlayer2Activity.this.listview.requestFocusFromTouch();
                    BackPlayer2Activity.this.listview.setSelection(i);
                }
            });
        } else if (MGplayer.isNumeric(LIVEplayer.getCurrentID())) {
            this.listview.setSelection(0);
        }
    }

    public void showBackListLayout() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        this.backlistlayout.setVisibility(0);
        listFocus();
        hideViewTimeout();
        setListviewSelection();
    }

    private void listFocus() {
        this.view_index = 0;
        this.listview.setFocusable(true);
        this.listview.setFocusableInTouchMode(true);
        this.listview.requestFocus();
        this.listview.requestFocusFromTouch();
        this.listview.listFocus();
    }

    private void previewFocus() {
        this.previewview.listFocus();
    }

    public void exitActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.myhomebar_text6).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {

            /* renamed from: com.gemini.play.BackPlayer2Activity$16$1 */
            class C02491 implements Runnable {
                C02491() {
                }

                public void run() {
                    BackPlayer2Activity.this.finish();
                }
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BackPlayer2Activity.this.stopVideo();
                BACKplayer.stopCheckVideo();
                MyToast.makeText(BackPlayer2Activity.this, BackPlayer2Activity.this.getString(C0216R.string.liveplayer_text12).toString(), 0);
                new Handler().postDelayed(new C02491(), 2000);
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
