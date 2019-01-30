package com.gemini.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gemini.custom.custom;
import com.gemini.custom.huanqiu;
import com.gemini.custom.quanxing;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import com.gemini.play.ScreenObserver.ScreenStateListener;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.util.MimeTypes;
import com.loopj.android.http.AsyncHttpClient;
import cz.msebera.android.httpclient.protocol.HttpRequestExecutor;
import io.vov.vitamio.widget.VideoView;
import java.util.Random;
import org.cybergarage.upnp.Device;
import org.videolan.vlc.VlcVideoView;

public class LivePlayerActivity extends Activity {
    private ScrollTextView allwayscrolltext = null;
    private Button backbutton = null;
    private LinearLayout backlayout = null;
    private Handler channelHandler = null;
    private Runnable channelRunnable = null;
    private LinearLayout channellayout = null;
    private TextView channeltext = null;
    private boolean checkVideo_runing = false;
    private int checkVideo_times = 0;
    private Button decodebutton = null;
    private MyInfoView infoview = null;
    private boolean is_longclick = false;
    private boolean isexit = false;
    private MyLineView liner = null;
    private MyListView listview = null;
    private MyListView1 listview1;
    private MyListView2 listview2;
    private ImageButton livebutton = null;
    private ImageView livelist_ad_image = null;
    private FrameLayout mLayout = null;
    private Button menubutton = null;
    private String numChannel = "";
    private Handler numHandler = null;
    private Runnable numRunnable = null;
    public ControlVideoInterface onControlVideo = new ControlVideoInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(LivePlayerActivity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    LivePlayerActivity.this.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onInfoPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    if (MGplayer.isNumeric(data)) {
                        LivePlayerActivity.this.playVideo(LIVEplayer.currentID, LIVEplayer.currentLine, Integer.parseInt(data));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onLinePressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    if (LIVEplayer.currentType == null || !LIVEplayer.currentType.equals("1")) {
                        if (MGplayer.isNumeric(data)) {
                            LivePlayerActivity.this.playVideo(LivePlayerActivity.this.listview.currentID(), Integer.parseInt(data), 0);
                            return;
                        } else {
                            LivePlayerActivity.this.playVideo(LivePlayerActivity.this.listview.currentID(), 0, 0);
                            return;
                        }
                    } else if (MGplayer.isNumeric(data)) {
                        LivePlayerActivity.this.playCollectVideo(LivePlayerActivity.this.listview.currentID(), Integer.parseInt(data), 0);
                        return;
                    } else {
                        LivePlayerActivity.this.playCollectVideo(LivePlayerActivity.this.listview.currentID(), 0, 0);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    public ListViewInterface onLivePressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    if (LivePlayerActivity.this.listview.isShown()) {
                        LivePlayerActivity.this.listview.hidePlayList();
                    }
                    MGplayer.MyPrintln("onLivePressed data:" + data);
                    if (MGplayer.custom().equals("jufeng")) {
                        LivePlayerActivity.this.playVideo(data, 0, 0);
                        LivePlayerActivity.this.infoview.showInfoPanal(data);
                        return;
                    } else if (!LivePlayerActivity.this.isPlaying() || !data.equals(LIVEplayer.currentID)) {
                        if (LIVEplayer.typePasswordOK || !LIVEplayer.isVideoTypePs(Integer.parseInt(data))) {
                            LivePlayerActivity.this.playVideo(data, 0, 0);
                            LivePlayerActivity.this.infoview.showInfoPanal(data);
                            return;
                        }
                        LivePlayerActivity.this.inputPasswordView(data);
                        return;
                    } else {
                        return;
                    }
                case 1:
                    LivePlayerActivity.this.typeview.showTypeList();
                    return;
                case 3:
                    MyLiveSettingView.setInterface(LivePlayerActivity.this.onSettingPressed);
                    MyLiveSettingView.showAlertDialog(LivePlayerActivity.this, data);
                    return;
                case 4:
                    LivePlayerActivity.this.playCollectVideo(data, 0, 0);
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onPreviewPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    if (MGplayer.isNumeric(data)) {
                        LIVEplayer.currentSeek = Integer.parseInt(data);
                        LivePlayerActivity.this.playVideo(LivePlayerActivity.this.listview.currentID(), LIVEplayer.currentLine, LIVEplayer.currentSeek);
                        LivePlayerActivity.this.hideAllView();
                        return;
                    }
                    return;
                case 1:
                    if (LivePlayerActivity.this.listview.isShown()) {
                        LivePlayerActivity.this.listview.listFocus();
                        return;
                    }
                    return;
                case 2:
                    LivePlayerActivity.this.previewview.hidePreviewList();
                    return;
                default:
                    return;
            }
        }
    };
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
                    LivePlayerActivity.this.rHandler.sendMessage(msg);
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onSettingPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onLinePressed data:" + data + " MyListView.currentID:" + MyListView1.currentID);
                    if (LIVEplayer.currentType == null || !LIVEplayer.currentType.equals("1")) {
                        if (MGplayer.isNumeric(data)) {
                            LivePlayerActivity.this.playVideo(LivePlayerActivity.this.listview.currentID(), Integer.parseInt(data), 0);
                            return;
                        } else {
                            LivePlayerActivity.this.playVideo(LivePlayerActivity.this.listview.currentID(), 0, 0);
                            return;
                        }
                    } else if (MGplayer.isNumeric(data)) {
                        LivePlayerActivity.this.playCollectVideo(LivePlayerActivity.this.listview.currentID(), Integer.parseInt(data), 0);
                        return;
                    } else {
                        LivePlayerActivity.this.playCollectVideo(LivePlayerActivity.this.listview.currentID(), 0, 0);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    public ListViewInterface onTypePressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    if (data == null || !(data.equals("2") || data.equals("3"))) {
                        if (data == null) {
                            LivePlayerActivity.this.listview.showPlayList(0);
                        } else if (data.equals("1")) {
                            LivePlayerActivity.this.listview.showPlayList(1);
                        } else {
                            LivePlayerActivity.this.listview.showPlayList(data);
                        }
                        LivePlayerActivity.this.typeview.hideTypeList();
                        return;
                    }
                    return;
                case 1:
                    if (LivePlayerActivity.this.listview.isShown()) {
                        LivePlayerActivity.this.listview.listFocus();
                        return;
                    }
                    return;
                case 2:
                    LivePlayerActivity.this.listview.showFindPlayList(data);
                    LivePlayerActivity.this.typeview.hideTypeList();
                    return;
                default:
                    return;
            }
        }
    };
    private ScrollTextView onescroller = null;
    private int percent_check_time = 0;
    private int percent_tmp = 0;
    private ImageButton previewbutton = null;
    private MyPreviewView previewview = null;
    public Handler rHandler = new Handler() {

        /* renamed from: com.gemini.play.LivePlayerActivity$32$1 */
        class C03001 extends Thread {
            C03001() {
            }

            public void run() {
                MGplayer.sendServerCmd(MGplayer.tv.gete() + "/live_state.php&mac=" + MGplayer.tv.GetMac() + "&cpuid=" + MGplayer.tv.getCpuID() + MGplayer.get_key_value());
            }
        }

        /* renamed from: com.gemini.play.LivePlayerActivity$32$2 */
        class C03012 implements Runnable {
            C03012() {
            }

            public void run() {
                LivePlayerActivity.this.playVideo(LIVEplayer.currentID, LIVEplayer.currentLine, LIVEplayer.currentSeek);
            }
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String content = msg.getData().getString("content");
                    int times = msg.getData().getInt("times");
                    LivePlayerActivity.this.onescroller.setText("");
                    LivePlayerActivity.this.onescroller.setVisibility(0);
                    LivePlayerActivity.this.onescroller.start_every(LivePlayerActivity.this, content, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", times);
                    return;
                case 2:
                    LIVEplayer.playVideoForHard(LIVEplayer.VideoViewH, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                    return;
                case 3:
                    LIVEplayer.playVideoForSoft(LIVEplayer.VideoViewS, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                    return;
                case 4:
                    new C03001().start();
                    return;
                case 5:
                    LIVEplayer.playVideoForHard(LIVEplayer.VideoViewH, LIVEplayer.currentID, LIVEplayer.currentLine, 0, null, null);
                    return;
                case 6:
                    LIVEplayer.playVideoForSoft(LIVEplayer.VideoViewS, LIVEplayer.currentID, LIVEplayer.currentLine, 0, null, null);
                    return;
                case 7:
                    LIVEplayer.playVideoForSoft2(LIVEplayer.VideoViewS2, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                    return;
                case 8:
                    LIVEplayer.playVideoForSoft2(LIVEplayer.VideoViewS2, LIVEplayer.currentID, LIVEplayer.currentLine, 0, null, null);
                    return;
                case 9:
                    LIVEplayer.playVideoForHard2(LIVEplayer.VideoViewH2, LIVEplayer.currentID, LIVEplayer.get_next_line(LIVEplayer.currentID, LIVEplayer.currentLine), 0, null, null);
                    return;
                case 10:
                    LIVEplayer.playVideoForHard2(LIVEplayer.VideoViewH2, LIVEplayer.currentID, LIVEplayer.currentLine, 0, null, null);
                    return;
                case 11:
                    MGplayer.MyPrintln("restart video 1");
                    LivePlayerActivity.this.stopVideo();
                    return;
                case 12:
                    MGplayer.MyPrintln("restart video 2");
                    LivePlayerActivity.this.stopVideo(msg.getData().getInt("inx"));
                    new Handler().postDelayed(new C03012(), 1500);
                    return;
                default:
                    return;
            }
        }
    };
    public ScreenStateListener screenstatelistener = new C09091();
    private boolean showMAC_runing = false;
    private TextView textmac = null;
    private MyTypeView typeview = null;
    private int used_id = 1;
    private ImageView watermark = null;

    /* renamed from: com.gemini.play.LivePlayerActivity$2 */
    class C02992 implements OnCompletionListener {
        C02992() {
        }

        public void onCompletion(MediaPlayer mp) {
            LIVEplayer.check_playing_times = 0;
            LIVEplayer.speed_nothings_times = 0;
            LIVEplayer.check_currentposition = -1;
            MGplayer.MyPrintln("VideoViewH setOnCompletionListener");
            if (LivePlayerActivity.this.rHandler.hasMessages(5)) {
                LivePlayerActivity.this.rHandler.removeMessages(5);
            }
            Message msg = new Message();
            msg.what = 5;
            LivePlayerActivity.this.rHandler.sendMessageDelayed(msg, 3000);
        }
    }

    /* renamed from: com.gemini.play.LivePlayerActivity$3 */
    class C03033 implements OnInfoListener {
        C03033() {
        }

        public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
            MGplayer.MyPrintln("VideoViewH buffer what = " + what);
            return false;
        }
    }

    /* renamed from: com.gemini.play.LivePlayerActivity$4 */
    class C03044 implements OnErrorListener {
        C03044() {
        }

        public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
            LIVEplayer.check_playing_times = 0;
            LIVEplayer.speed_nothings_times = 0;
            LIVEplayer.check_currentposition = -1;
            MGplayer.MyPrintln("VideoViewH onError what = " + what + "," + extra);
            return false;
        }
    }

    /* renamed from: com.gemini.play.LivePlayerActivity$5 */
    class C03055 implements OnPreparedListener {
        C03055() {
        }

        public void onPrepared(MediaPlayer mediaPlayer) {
            MGplayer.MyPrintln("MediaPlayer is prepared and start");
            mediaPlayer.start();
        }
    }

    /* renamed from: com.gemini.play.LivePlayerActivity$6 */
    class C03066 implements OnClickListener {
        C03066() {
        }

        public void onClick(View v) {
            if (LIVEplayer.currentType == null || LIVEplayer.currentType.equals("0")) {
                LivePlayerActivity.this.listview.showPlayList(0);
            } else if (LIVEplayer.currentType == null || !LIVEplayer.currentType.equals("1")) {
                LivePlayerActivity.this.listview.showPlayList(LIVEplayer.currentType, true);
            } else {
                LivePlayerActivity.this.listview.showPlayList(1);
            }
        }
    }

    /* renamed from: com.gemini.play.LivePlayerActivity$7 */
    class C03077 implements OnClickListener {
        C03077() {
        }

        public void onClick(View v) {
            LivePlayerActivity.this.previewview.showPreviewList(Integer.parseInt(LivePlayerActivity.this.listview.getCurrentID()));
        }
    }

    /* renamed from: com.gemini.play.LivePlayerActivity$8 */
    class C03088 implements OnClickListener {
        C03088() {
        }

        public void onClick(View v) {
            LivePlayerActivity.this.exitActivity();
        }
    }

    /* renamed from: com.gemini.play.LivePlayerActivity$9 */
    class C03099 implements OnClickListener {
        C03099() {
        }

        public void onClick(View v) {
            MenuView.gridMenuInit(LivePlayerActivity.this);
            MenuView.showAlertDialog(LivePlayerActivity.this);
        }
    }

    /* renamed from: com.gemini.play.LivePlayerActivity$1 */
    class C09091 implements ScreenStateListener {
        C09091() {
        }

        public void onScreenOn() {
            MGplayer.MyPrintln("onScreenOn");
        }

        public void onScreenOff() {
            MGplayer.MyPrintln("onScreenOff");
            LivePlayerActivity.this.stopService(new Intent(LivePlayerActivity.this, LocalService.class));
            LivePlayerActivity.this.finish();
            Process.killProcess(Process.myPid());
            MGplayer.exit_app();
        }

        public void onUserPresent() {
            MGplayer.MyPrintln("onUserPresent");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(128, 128);
        getWindow().setFlags(1024, 1024);
        LIVEplayer.setContext(this);
        int ui_type = getIntent().getIntExtra("ui_type", 1);
        MGplayer.MyPrintln("ui_type:" + ui_type);
        if (ui_type > 0 && ui_type <= 2) {
            this.used_id = ui_type;
        }
        if (this.used_id == 1) {
            setContentView(C0216R.layout.liveplayer1);
        } else if (this.used_id == 2) {
            setContentView(C0216R.layout.liveplayer2);
        }
        MGplayer.mediaplayervodheader(0);
        this.onescroller = (ScrollTextView) findViewById(C0216R.id.onescrolltext);
        this.allwayscrolltext = (ScrollTextView) findViewById(C0216R.id.allwayscrolltext);
        this.mLayout = (FrameLayout) findViewById(C0216R.id.panal);
        this.backlayout = (LinearLayout) findViewById(C0216R.id.backlayout);
        MGplayer.video_every_interface(this.onControlVideo);
        this.liner = new MyLineView();
        this.liner.initView(this);
        this.liner.setInterface(this.onLinePressed);
        init_ui();
        init();
        checkVideo();
        showMAC();
    }

    public void init_ui() {
        this.listview2 = (MyListView2) findViewById(C0216R.id.mylistview2);
        this.listview1 = (MyListView1) findViewById(C0216R.id.mylistview1);
        this.textmac = (TextView) findViewById(C0216R.id.textmac);
        if (this.used_id == 1) {
            this.listview = new MyListView(this.listview1, this.used_id);
        } else if (this.used_id == 2) {
            this.listview = new MyListView(this.listview2, this.used_id);
        }
        this.listview.setInterface(this.onLivePressed);
        ((MyPreviewView) findViewById(C0216R.id.mypreviewview)).setInterface(this.onPreviewPressed);
        ((MyTypeView) findViewById(C0216R.id.mytypeview)).setInterface(this.onTypePressed);
        if (MGplayer.custom().equals("quanxing")) {
            MGplayer.isShowLefttime = MGplayer.leftdaysshow;
        }
        if (MGplayer.isShowLefttime == 1) {
            TextView timeouttext = (TextView) findViewById(C0216R.id.timeouttext);
            timeouttext.setTextSize(8.0f * MGplayer.getFontsRate());
            timeouttext.setTypeface(MGplayer.getFontsType(this));
            timeouttext.setText("");
            String timeout = "";
            if (Integer.parseInt(MGplayer.leftdays) == -1) {
                timeout = timeout + "    " + getString(C0216R.string.myhomebar_text7).toString() + ":" + getString(C0216R.string.myhomebar_text9).toString();
            } else {
                timeout = timeout + "    " + getString(C0216R.string.myhomebar_text7).toString() + ":" + MGplayer.leftdays + getString(C0216R.string.myhomebar_text8).toString();
            }
            timeouttext.setText(timeout);
        }
        ScrollTextView scroller = (ScrollTextView) findViewById(C0216R.id.scrolltext);
        scroller.init(getWindowManager());
        if (MGplayer.scrolltext != null) {
            if (MGplayer.livescroll_show == 0) {
                MGplayer.MyPrintln("start scrolltext");
                scroller.setText("");
                scroller.start(this, MGplayer.scrolltext, 0, 0, 0, 0, 2.5f, (int) (12.0f * MGplayer.getFontsRate()), "FFFFFF");
            }
            if (MGplayer.livescroll_show == 1) {
                this.allwayscrolltext.setText("");
                this.allwayscrolltext.setVisibility(0);
                this.allwayscrolltext.start_dtime(this, MGplayer.scrolltext, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", MGplayer.livescroll_showtimes);
                LayoutParams lp = (LinearLayout.LayoutParams) this.allwayscrolltext.getLayoutParams();
                if (MGplayer.getDensityDpi() != 480) {
                    lp.height = (int) (16.0f * MGplayer.getFontsRate());
                }
                this.allwayscrolltext.setLayoutParams(lp);
            }
        }
        MGplayer.scroll_every_interface(this.onScrollView);
        if (MGplayer.onescroll_txt != null && MGplayer.onescroll_txt.length() > 0) {
            this.onescroller.init(getWindowManager());
            this.onescroller.setText("");
            this.onescroller.setVisibility(0);
            this.onescroller.start(this, MGplayer.onescroll_txt, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", MGplayer.onescroll_times);
        }
        this.channeltext = (TextView) findViewById(C0216R.id.channeltext);
        this.channellayout = (LinearLayout) findViewById(C0216R.id.channellayout);
        this.watermark = (ImageView) findViewById(C0216R.id.watermark);
        this.watermark.setImageBitmap(BitmapFactory.decodeFile(MGplayer._this.getFilesDir() + "/icon/" + LIVEplayer.watermask));
        LayoutParams layoutParams;
        if (MGplayer.watermark_site == 0) {
            layoutParams = new FrameLayout.LayoutParams(this.watermark.getLayoutParams());
            layoutParams.gravity = 51;
            layoutParams.setMargins(MGplayer.watermark_dip1, MGplayer.watermark_dip2, 0, 0);
            this.watermark.setLayoutParams(layoutParams);
        } else if (MGplayer.watermark_site == 1) {
            layoutParams = new FrameLayout.LayoutParams(this.watermark.getLayoutParams());
            layoutParams.gravity = 83;
            layoutParams.setMargins(MGplayer.watermark_dip1, 0, 0, MGplayer.watermark_dip2);
            this.watermark.setLayoutParams(layoutParams);
        } else if (MGplayer.watermark_site == 3) {
            layoutParams = new FrameLayout.LayoutParams(this.watermark.getLayoutParams());
            layoutParams.gravity = 85;
            layoutParams.setMargins(0, 0, MGplayer.watermark_dip1, MGplayer.watermark_dip2);
            this.watermark.setLayoutParams(layoutParams);
        } else if (MGplayer.watermark_site == 2) {
            layoutParams = new FrameLayout.LayoutParams(this.watermark.getLayoutParams());
            layoutParams.gravity = 53;
            layoutParams.setMargins(0, MGplayer.watermark_dip1, MGplayer.watermark_dip2, 0);
            this.watermark.setLayoutParams(layoutParams);
        } else if (MGplayer.watermark_site == -1) {
            this.watermark.setVisibility(8);
        }
        if (MGplayer.adliveimage_site == 0) {
            this.livelist_ad_image = (ImageView) findViewById(C0216R.id.livelist_ad_image_top);
        } else if (MGplayer.adliveimage_site == 1) {
            this.livelist_ad_image = (ImageView) findViewById(C0216R.id.livelist_ad_image);
        } else if (MGplayer.adliveimage_site == 2) {
            this.livelist_ad_image = (ImageView) findViewById(C0216R.id.livelist_ad_image_bottom);
        }
        this.listview.set_ad_Image(this.livelist_ad_image);
    }

    private void init_screenstatelistener(Context _this) {
        new ScreenObserver(_this).startObserver(this.screenstatelistener);
    }

    private void init() {
        this.infoview = (MyInfoView) findViewById(C0216R.id.myinfoview);
        this.infoview.setVisibility(8);
        this.infoview.setInterface(this.onInfoPressed);
        Typeface typeFace = MGplayer.getFontsType(this);
        float rate = MGplayer.getFontsRate();
        LIVEplayer.VideoViewS2 = (VideoView) findViewById(C0216R.id.VideoViewSoft2);
        if (LIVEplayer.VideoViewS2 != null) {
            LIVEplayer.VideoViewS2.setVisibility(0);
        }
        LIVEplayer.VideoViewS = (VlcVideoView) findViewById(C0216R.id.VideoViewSoft);
        if (LIVEplayer.VideoViewS != null) {
            LIVEplayer.VideoViewS.setVisibility(0);
        }
        LIVEplayer.VideoViewH2 = (ExoPlayerView) findViewById(C0216R.id.VideoViewHard2);
        if (LIVEplayer.VideoViewH2 != null) {
            LIVEplayer.VideoViewH2.setDefaultControlsEnabled(false);
            LIVEplayer.VideoViewH2.setVisibility(8);
        }
        LIVEplayer.VideoViewH = (VideoView) findViewById(C0216R.id.VideoViewHard);
        LIVEplayer.VideoViewH.setVisibility(0);
        LIVEplayer.VideoViewH.setOnCompletionListener(new C02992());
        LIVEplayer.VideoViewH.setOnInfoListener(new C03033());
        LIVEplayer.VideoViewH.setOnErrorListener(new C03044());
        LIVEplayer.VideoViewH.setOnPreparedListener(new C03055());
        this.listview.setVisibility(8);
        this.previewview = (MyPreviewView) findViewById(C0216R.id.mypreviewview);
        this.previewview.setVisibility(8);
        this.typeview = (MyTypeView) findViewById(C0216R.id.mytypeview);
        this.typeview.setVisibility(8);
        this.livebutton = (ImageButton) findViewById(C0216R.id.livebutton);
        this.livebutton.setVisibility(8);
        this.livebutton.setOnClickListener(new C03066());
        this.previewbutton = (ImageButton) findViewById(C0216R.id.previewbutton);
        this.previewbutton.setVisibility(8);
        this.previewbutton.setOnClickListener(new C03077());
        this.backbutton = (Button) findViewById(C0216R.id.backbutton);
        this.backbutton.setTextSize(8.0f * rate);
        this.backbutton.setTypeface(typeFace);
        this.backbutton.setOnClickListener(new C03088());
        if (MGplayer.custom().equals("simba")) {
            this.backbutton.setVisibility(8);
        }
        this.menubutton = (Button) findViewById(C0216R.id.menubutton);
        this.menubutton.setTextSize(8.0f * rate);
        this.menubutton.setTypeface(typeFace);
        this.menubutton.setOnClickListener(new C03099());
        if (MGplayer.custom().equals("simba")) {
            this.menubutton.setVisibility(8);
        }
        if (this.used_id == 1) {
            this.decodebutton = (Button) findViewById(C0216R.id.decodebutton2);
            this.decodebutton.setTextSize(8.0f * rate);
            this.decodebutton.setTypeface(typeFace);
            this.decodebutton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MenuView.decodeActivity(LivePlayerActivity.this, LivePlayerActivity.this.rHandler);
                }
            });
        }
        if (MGplayer.custom().equals("simba")) {
            this.decodebutton.setVisibility(8);
        }
        final TextView speedView = (TextView) findViewById(C0216R.id.progressSpeed);
        ProgressBar progressBar = (ProgressBar) findViewById(C0216R.id.progressBar);
        final FrameLayout progressLayout = (FrameLayout) findViewById(C0216R.id.progressLayout);
        final TextView videoSpeed = (TextView) findViewById(C0216R.id.videoSpeed);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
        layoutParams.width = (int) (48.0f * rate);
        layoutParams.height = (int) (48.0f * rate);
        progressBar.setLayoutParams(layoutParams);
        speedView.setTextSize(7.0f * rate);
        speedView.setTypeface(typeFace);
        if (videoSpeed != null) {
            videoSpeed.setTextSize(7.0f * rate);
            videoSpeed.setTypeface(typeFace);
        }
        if (MGplayer.show_live_speed == 1) {
            if (videoSpeed != null) {
                videoSpeed.setVisibility(0);
            }
            speedView.setVisibility(8);
        } else {
            speedView.setVisibility(0);
            if (videoSpeed != null) {
                videoSpeed.setVisibility(8);
            }
        }
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (LIVEplayer.isPlaying()) {
                    progressLayout.setVisibility(8);
                } else {
                    progressLayout.setVisibility(0);
                }
                if (MGplayer.show_live_speed != 1) {
                    speedView.setText(MGplayer.getUidRxBytes() + "K/S");
                } else if (videoSpeed != null) {
                    videoSpeed.setText(MGplayer.getUidRxBytes() + "kb");
                }
                mHandler.postDelayed(this, 1000);
            }
        }, 2000);
        LIVEplayer.currentLine = 0;
        String id = LIVEplayer.getCurrentID();
        LIVEplayer.currentID = id;
        this.listview.set_currentID(LIVEplayer.currentID);
        LIVEplayer.currentType = String.valueOf(0);
        if (id == null || !MGplayer.isNumeric(id)) {
            if (LIVEplayer.urlSize() <= 0 || !LIVEplayer.isVideoTypePs(LIVEplayer.idGet(0)) || LIVEplayer.typePasswordOK) {
                id = String.valueOf(LIVEplayer.getVideoNoNeedpsNum());
                LIVEplayer.currentID = id;
                this.listview.set_currentID(LIVEplayer.currentID);
                MGplayer.MyPrintln("getVideoNoNeedpsNum" + id);
            } else {
                id = String.valueOf(LIVEplayer.idGet(0));
                LIVEplayer.currentID = id;
                this.listview.set_currentID(LIVEplayer.currentID);
            }
        }
        if (id != null && MGplayer.isNumeric(id)) {
            int delay = AsyncHttpClient.DEFAULT_RETRY_SLEEP_TIME_MILLIS;
            if (MGplayer.custom().equals("huanqiu")) {
                delay = HttpRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE;
            }
            if (!LIVEplayer.isVideoTypePs(Integer.parseInt(id)) || LIVEplayer.typePasswordOK) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        LivePlayerActivity.this.stopVideo();
                        LivePlayerActivity.this.playVideo(LIVEplayer.currentID, 0, 0);
                        if (MGplayer.custom().equals("huanqiu")) {
                            huanqiu.ss();
                        }
                    }
                }, (long) delay);
                this.listview.set_currentID(LIVEplayer.currentID);
                this.infoview.showInfoPanal(LIVEplayer.currentID);
            } else if (LIVEplayer.urlSize() > 0 && !LIVEplayer.isVideoTypePs(Integer.parseInt(id)) && (LIVEplayer.isVideoTypePs(Integer.parseInt(id)) || LIVEplayer.typePasswordOK)) {
                LIVEplayer.currentID = String.valueOf(LIVEplayer.idGet(0));
                this.listview.set_currentID(LIVEplayer.currentID);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        LivePlayerActivity.this.stopVideo();
                        LivePlayerActivity.this.playVideo(LIVEplayer.currentID, 0, 0);
                        if (MGplayer.custom().equals("huanqiu")) {
                            huanqiu.ss();
                        }
                    }
                }, (long) delay);
                this.infoview.showInfoPanal(LIVEplayer.currentID);
            }
        }
        this.channelHandler = new Handler();
        this.channelRunnable = new Runnable() {
            public void run() {
                LivePlayerActivity.this.channellayout.setVisibility(8);
                LIVEplayer.currentID = LivePlayerActivity.this.listview.getCurrentID();
                LivePlayerActivity.this.playVideo(LIVEplayer.currentID, 0, 0);
                LivePlayerActivity.this.infoview.showInfoPanal(LIVEplayer.currentID);
            }
        };
        this.numHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        String num = msg.getData().getString("num");
                        if (!MGplayer.isNumeric(num) || num.length() > 5) {
                            Toast.makeText(LivePlayerActivity.this, LivePlayerActivity.this.getString(C0216R.string.liveplayer_text6).toString(), 0).show();
                        } else if (!LIVEplayer.existVideoId(Integer.parseInt(num))) {
                            Toast.makeText(LivePlayerActivity.this, LivePlayerActivity.this.getString(C0216R.string.liveplayer_text6).toString(), 0).show();
                        } else if (LIVEplayer.typePasswordOK || !LIVEplayer.isVideoTypePs(Integer.parseInt(num))) {
                            LivePlayerActivity.this.channellayout.setVisibility(8);
                            LivePlayerActivity.this.listview.setCurrentID(num);
                            LivePlayerActivity.this.playVideo(num, 0, 0);
                            LivePlayerActivity.this.numChannel = "";
                        } else {
                            LivePlayerActivity.this.inputPasswordView(num);
                        }
                        LivePlayerActivity.this.channellayout.setVisibility(8);
                        LivePlayerActivity.this.numChannel = "";
                        return;
                    default:
                        return;
                }
            }
        };
        this.channeltext.setTextSize(8.0f * rate);
        this.channeltext.setTypeface(typeFace);
        LIVEplayer.checkVideo();
        if (MGplayer.updatetip_show == 1 && !MGplayer.boot_launcher) {
            MGplayer.showUpdateActivity(this);
        }
    }

    private boolean hideAllView() {
        int v = 0;
        if (this.listview.isShown()) {
            this.listview.hidePlayList();
            v = 1;
        }
        if (this.typeview.isShown()) {
            this.typeview.hideTypeList();
            v = 1;
        }
        if (this.previewview.isShown()) {
            this.previewview.hidePreviewList();
            v = 1;
        }
        if (this.infoview.isShown()) {
            this.infoview.hideInfoPanal();
            v = 1;
        }
        if (v > 0) {
            return true;
        }
        return false;
    }

    private void videoSetFocus() {
        if (MGplayer.getDecode() == 0) {
            LIVEplayer.VideoViewH.setFocusable(true);
            LIVEplayer.VideoViewH.setFocusableInTouchMode(true);
            LIVEplayer.VideoViewH.requestFocus();
            LIVEplayer.VideoViewH.requestFocusFromTouch();
        } else if (MGplayer.getDecode() == 1) {
            if (LIVEplayer.VideoViewS != null) {
                LIVEplayer.VideoViewS.setFocusable(true);
                LIVEplayer.VideoViewS.setFocusableInTouchMode(true);
                LIVEplayer.VideoViewS.requestFocus();
                LIVEplayer.VideoViewS.requestFocusFromTouch();
            }
        } else if (MGplayer.getDecode() == 2) {
            if (LIVEplayer.VideoViewS2 != null) {
                LIVEplayer.VideoViewS2.setFocusable(true);
                LIVEplayer.VideoViewS2.setFocusableInTouchMode(true);
                LIVEplayer.VideoViewS2.requestFocus();
                LIVEplayer.VideoViewS2.requestFocusFromTouch();
            }
        } else if (MGplayer.getDecode() == 3 && LIVEplayer.VideoViewH2 != null) {
            LIVEplayer.VideoViewH2.setFocusable(true);
            LIVEplayer.VideoViewH2.setFocusableInTouchMode(true);
            LIVEplayer.VideoViewH2.requestFocus();
            LIVEplayer.VideoViewH2.requestFocusFromTouch();
        }
    }

    private void playCollectVideo(String data, int line, int seek) {
        playCollectVideo(MGplayer.getDecode(), data, line, seek);
    }

    private void playCollectVideo(int inx, String data, int line, int seek) {
        UrlStatus s = new MyLiveCollectView().get(this, data);
        String url = s.url;
        String password = s.password;
        if (url != null) {
            if (MGplayer.custom().equals("quanxing")) {
                url = MGplayer.j2(url, quanxing.urlpassword);
                password = quanxing.hotlink;
            } else {
                String urlss = MGplayer.ju(url);
                String passwordss = MGplayer.j2(password);
                url = LIVEplayer.getVideoUrlFromUrlss(urlss, line);
                password = LIVEplayer.getVideoPassFromPassTmpss(passwordss, line);
            }
            int inx2 = LIVEplayer.selectDecode(url, password);
            if (inx2 >= 0) {
                inx = inx2;
            }
            if (inx == 0) {
                LIVEplayer.VideoViewH.setVisibility(0);
                LIVEplayer.VideoViewH2.setVisibility(8);
                if (LIVEplayer.VideoViewS != null) {
                    LIVEplayer.VideoViewS.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.VideoViewS2.setVisibility(8);
                }
                LIVEplayer.playCollectVideoForHard(this, LIVEplayer.VideoViewH, data, line, seek, url, password);
            } else if (inx == 1) {
                if (LIVEplayer.VideoViewS != null) {
                    LIVEplayer.VideoViewS.setVisibility(0);
                }
                LIVEplayer.VideoViewH.setVisibility(8);
                LIVEplayer.VideoViewH2.setVisibility(8);
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.VideoViewS2.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS != null) {
                    LIVEplayer.playCollectVideoForSoft(this, LIVEplayer.VideoViewS, data, line, seek, url, password);
                }
            } else if (inx == 2) {
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.VideoViewS2.setVisibility(0);
                }
                LIVEplayer.VideoViewH.setVisibility(8);
                LIVEplayer.VideoViewH2.setVisibility(8);
                if (LIVEplayer.VideoViewS != null) {
                    LIVEplayer.VideoViewS.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.playCollectVideoForSoft2(this, LIVEplayer.VideoViewS2, data, line, seek, url, password);
                }
            } else if (inx == 3) {
                if (LIVEplayer.VideoViewH2 != null) {
                    LIVEplayer.VideoViewH2.setVisibility(0);
                }
                LIVEplayer.VideoViewH.setVisibility(8);
                if (LIVEplayer.VideoViewS != null) {
                    LIVEplayer.VideoViewS.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.VideoViewS2.setVisibility(8);
                }
                if (LIVEplayer.VideoViewH2 != null) {
                    LIVEplayer.playCollectVideoForHard2(this, LIVEplayer.VideoViewH2, data, line, seek, url, password);
                }
            }
        }
    }

    private void playVideo(String data, int line, int seek) {
        playVideo(MGplayer.getDecode(), data, line, seek);
    }

    private void playVideo(int inx, String data, int line, int seek) {
        if (MGplayer.custom().equals("jingjimu") || MGplayer.custom().equals("jingjimudev")) {
            custom.jjm_send();
        }
        String url = LIVEplayer.getVideoUrl(Integer.parseInt(data));
        String password = LIVEplayer.getVideoPassword(Integer.parseInt(data));
        if (url != null) {
            if (password.equals("9000")) {
                password = null;
            } else if (MGplayer.custom().equals("quanxing")) {
                MGplayer.MyPrintln("video play:" + url + " password:" + quanxing.urlpassword);
                url = MGplayer.j2(url, quanxing.urlpassword);
                password = quanxing.hotlink;
            } else {
                String urlss = MGplayer.ju(url);
                String passwordss = MGplayer.j2(password);
                url = LIVEplayer.getVideoUrlFromUrlss(urlss, line);
                password = LIVEplayer.getVideoPassFromPassTmpss(passwordss, line);
            }
            int inx2 = LIVEplayer.selectDecode(url, password);
            if (inx2 >= 0) {
                inx = inx2;
            }
            MGplayer.MyPrintln("inx = " + inx);
            if (inx == 0) {
                LIVEplayer.VideoViewH.setVisibility(0);
                if (LIVEplayer.VideoViewH2 != null) {
                    LIVEplayer.VideoViewH2.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS != null) {
                    LIVEplayer.VideoViewS.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.VideoViewS2.setVisibility(8);
                }
                LIVEplayer.playVideoForHard(LIVEplayer.VideoViewH, data, line, seek, url, password);
            } else if (inx == 3) {
                if (LIVEplayer.VideoViewH2.getVisibility() == 8) {
                    LIVEplayer.VideoViewH2.setVisibility(0);
                }
                if (LIVEplayer.VideoViewH != null) {
                    LIVEplayer.VideoViewH.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS != null) {
                    LIVEplayer.VideoViewS.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.VideoViewS2.setVisibility(8);
                }
                LIVEplayer.playVideoForHard2(LIVEplayer.VideoViewH2, data, line, seek, url, password);
            } else if (inx == 1) {
                LIVEplayer.VideoViewH.setVisibility(8);
                if (LIVEplayer.VideoViewH2 != null) {
                    LIVEplayer.VideoViewH2.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.VideoViewS2.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS != null) {
                    LIVEplayer.VideoViewS.setVisibility(0);
                    LIVEplayer.playVideoForSoft(LIVEplayer.VideoViewS, data, line, seek, url, password);
                }
            } else if (inx == 2) {
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.VideoViewS2.setVisibility(0);
                }
                LIVEplayer.VideoViewH.setVisibility(8);
                if (LIVEplayer.VideoViewH2 != null) {
                    LIVEplayer.VideoViewH2.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS != null) {
                    LIVEplayer.VideoViewS.setVisibility(8);
                }
                if (LIVEplayer.VideoViewS2 != null) {
                    LIVEplayer.playVideoForSoft2(LIVEplayer.VideoViewS2, data, line, seek, url, password);
                }
            }
            this.listview.setCurrentID(data);
            LIVEplayer.playWaterMark(data, this.watermark);
            Message msg = new Message();
            msg.what = 4;
            if (this.rHandler.hasMessages(4)) {
                this.rHandler.removeMessages(4);
            }
            this.rHandler.sendMessageDelayed(msg, 36000000);
        }
    }

    private void stopVideo() {
        stopVideo(MGplayer.getDecode());
    }

    private void stopVideo(int inx) {
        if (inx == 0) {
            LIVEplayer.stopVideoForHard(LIVEplayer.VideoViewH);
        } else if (inx == 3) {
            LIVEplayer.stopVideoForHard2(LIVEplayer.VideoViewH2);
        } else if (inx == 1) {
            if (LIVEplayer.VideoViewS != null) {
                LIVEplayer.stopVideoForSoft(LIVEplayer.VideoViewS);
            }
        } else if (inx == 2 && LIVEplayer.VideoViewS2 != null) {
            LIVEplayer.stopVideoForSoft2(LIVEplayer.VideoViewS2);
        }
    }

    private boolean isPlaying() {
        if (MGplayer.getDecode() == 0) {
            return LIVEplayer.VideoViewH.isPlaying();
        }
        if (MGplayer.getDecode() == 1) {
            if (LIVEplayer.VideoViewS != null) {
                return LIVEplayer.VideoViewS.isPlaying();
            }
            return false;
        } else if (MGplayer.getDecode() == 2) {
            if (LIVEplayer.VideoViewS2 != null) {
                return LIVEplayer.VideoViewS2.isPlaying();
            }
            return false;
        } else if (MGplayer.getDecode() != 3 || LIVEplayer.VideoViewH2 == null) {
            return false;
        } else {
            return LIVEplayer.VideoViewH2.isPlaying();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.livebutton.setVisibility(0);
        this.previewbutton.setVisibility(0);
        this.backlayout.setVisibility(0);
        this.checkVideo_times = 0;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                LivePlayerActivity.this.livebutton.setVisibility(8);
                LivePlayerActivity.this.previewbutton.setVisibility(8);
                LivePlayerActivity.this.backlayout.setVisibility(8);
            }
        }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
        this.infoview.showInfoPanal(LIVEplayer.currentID);
        if (MGplayer.custom().equals("simba") && !this.listview.isShown()) {
            if (LIVEplayer.currentType == null || LIVEplayer.currentType.equals("0")) {
                this.listview.showPlayList(0);
            } else if (LIVEplayer.currentType != null && LIVEplayer.currentType.equals("1")) {
                this.listview.showPlayList(1);
            } else if (LIVEplayer.typeNeedpsGet(LIVEplayer.currentType).equals("0") || LIVEplayer.typePasswordOK) {
                this.listview.showPlayList(LIVEplayer.currentType, true);
            } else {
                this.listview.showPlayList(0);
            }
        }
        return super.onTouchEvent(event);
    }

    private void showBacklayout() {
        this.backlayout.setVisibility(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                LivePlayerActivity.this.backlayout.setVisibility(8);
            }
        }, 15000);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 26:
                    MGplayer.MyPrintln("KeyEvent.KEYCODE_POWER");
                    break;
                case 66:
                    if (!(this.listview.isShown() || this.previewview.isShown() || this.typeview.isShown())) {
                        videoSetFocus();
                        break;
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.checkVideo_times = 0;
        if (this.listview.isShown() || this.previewview.isShown() || this.typeview.isShown() || keyCode < 7 || keyCode > 16) {
            int index;
            switch (keyCode) {
                case 4:
                    if (hideAllView()) {
                        return true;
                    }
                    if (this.isexit) {
                        LIVEplayer.typePasswordOK = false;
                        LIVEplayer.stopCheckVideo();
                        stopVideo();
                        this.isexit = false;
                        if (!(MGplayer.custom().equals("quanxing") || quanxing.exit_url == null)) {
                            MGplayer.sendServerCmd(quanxing.exit_url);
                        }
                        if (MGplayer.boot_launcher) {
                            MyToast.makeText(this, getString(C0216R.string.liveplayer_text12).toString(), 0);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    LivePlayerActivity.this.finish();
                                }
                            }, 3000);
                        } else {
                            stopService(new Intent(this, LocalService.class));
                            finish();
                            Process.killProcess(Process.myPid());
                            MGplayer.exit_app();
                        }
                        return true;
                    }
                    this.isexit = true;
                    MyToast.makeText(this, getString(C0216R.string.isexit).toString(), 0);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            LivePlayerActivity.this.isexit = false;
                        }
                    }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
                    return true;
                case 19:
                    if (MGplayer.custom().equals("jufeng")) {
                        if (!(this.listview.isShown() || this.previewview.isShown() || this.typeview.isShown())) {
                            this.channeltext.setText(showPreChannel());
                            this.channelHandler.removeCallbacks(this.channelRunnable);
                            this.channelHandler.postDelayed(this.channelRunnable, 1500);
                            this.channellayout.setVisibility(0);
                            return true;
                        }
                    } else if (!(this.listview.isShown() || this.previewview.isShown() || this.typeview.isShown())) {
                        this.channeltext.setText(showNextChannel());
                        this.channelHandler.removeCallbacks(this.channelRunnable);
                        this.channelHandler.postDelayed(this.channelRunnable, 1500);
                        this.channellayout.setVisibility(0);
                        return true;
                    }
                case 20:
                    if (MGplayer.custom().equals("jufeng")) {
                        if (!(this.listview.isShown() || this.previewview.isShown() || this.typeview.isShown())) {
                            this.channeltext.setText(showNextChannel());
                            this.channelHandler.removeCallbacks(this.channelRunnable);
                            this.channelHandler.postDelayed(this.channelRunnable, 1500);
                            this.channellayout.setVisibility(0);
                            return true;
                        }
                    } else if (!(this.listview.isShown() || this.previewview.isShown() || this.typeview.isShown())) {
                        this.channeltext.setText(showPreChannel());
                        this.channelHandler.removeCallbacks(this.channelRunnable);
                        this.channelHandler.postDelayed(this.channelRunnable, 1500);
                        this.channellayout.setVisibility(0);
                        return true;
                    }
                case 21:
                    if (event.getRepeatCount() > 0) {
                        if (!(this.listview.isShown() || this.previewview.isShown() || this.typeview.isShown())) {
                            this.is_longclick = true;
                            if (!LIVEplayer.isBackplayUrl(LIVEplayer.currentURL)) {
                                AudioManager audio = (AudioManager) getSystemService(MimeTypes.BASE_TYPE_AUDIO);
                                int current = audio.getStreamVolume(3) - 1;
                                if (current < 0) {
                                    current = 0;
                                }
                                audio.setStreamVolume(3, current, 5);
                                break;
                            }
                            showBacklayout();
                            this.infoview.showInfoPanal(LIVEplayer.currentID);
                            break;
                        }
                    } else if (MGplayer.livelist_leftright == 1 && this.listview.isShown()) {
                        MGplayer.MyPrintln("left type_size:" + LIVEplayer.typeSize());
                        if (LIVEplayer.currentType.equals("0")) {
                            if (LIVEplayer.typeNeedpsGet(LIVEplayer.typeIdGet(LIVEplayer.typeSize() - 1)).equals("0") || LIVEplayer.typePasswordOK) {
                                this.listview.showPlayList(LIVEplayer.typeIdGet(LIVEplayer.typeSize() - 1));
                            } else {
                                LIVEplayer.currentType = LIVEplayer.typeIdGet(LIVEplayer.typeSize() - 1);
                                if (LIVEplayer.show_ps_playlist) {
                                    this.listview.showPlayList(LIVEplayer.currentType);
                                } else {
                                    inputPasswordTypeView(LIVEplayer.typeIdGet(LIVEplayer.typeSize() - 1));
                                }
                            }
                        } else if (LIVEplayer.currentType.equals("1")) {
                            this.listview.showPlayList(0);
                        } else {
                            index = LIVEplayer.typeIndexGetFormId(LIVEplayer.currentType);
                            MGplayer.MyPrintln("left index:" + index);
                            if (index < 0) {
                                this.listview.showPlayList(0);
                            } else if (index == 0) {
                                this.listview.showPlayList(1);
                            } else if (LIVEplayer.typeNeedpsGet(LIVEplayer.typeIdGet(index - 1)).equals("0") || LIVEplayer.typePasswordOK) {
                                this.listview.showPlayList(LIVEplayer.typeIdGet(index - 1));
                            } else {
                                LIVEplayer.currentType = LIVEplayer.typeIdGet(index - 1);
                                if (LIVEplayer.show_ps_playlist) {
                                    this.listview.showPlayList(LIVEplayer.currentType);
                                } else {
                                    inputPasswordTypeView(LIVEplayer.typeIdGet(index - 1));
                                }
                            }
                        }
                        this.listview.listFocus();
                        return true;
                    } else if (this.typeview.isShown()) {
                        this.typeview.hideTypeList();
                        this.listview.showViewNoTimeout();
                        return true;
                    } else if (this.listview.isShown() && !this.previewview.isShown()) {
                        this.listview.showViewNoTimeout();
                        this.typeview.listFocus();
                        this.typeview.showTypeList();
                        return true;
                    } else if (!this.listview.isShown() || !this.previewview.isShown()) {
                        return true;
                    } else {
                        this.listview.showViewTimeout();
                        this.listview.listFocus();
                        this.previewview.hidePreviewList();
                        return true;
                    }
                    break;
                case 22:
                    if (event.getRepeatCount() > 0) {
                        if (!(this.listview.isShown() || this.previewview.isShown() || this.typeview.isShown())) {
                            this.is_longclick = true;
                            if (!LIVEplayer.isBackplayUrl(LIVEplayer.currentURL)) {
                                AudioManager audio1 = (AudioManager) getSystemService(MimeTypes.BASE_TYPE_AUDIO);
                                int max1 = audio1.getStreamMaxVolume(3);
                                int current1 = audio1.getStreamVolume(3) + 1;
                                if (current1 > max1) {
                                    current1 = max1;
                                }
                                audio1.setStreamVolume(3, current1, 5);
                                break;
                            }
                            showBacklayout();
                            this.infoview.showInfoPanal(LIVEplayer.currentID);
                            break;
                        }
                    } else if (MGplayer.livelist_leftright == 1 && this.listview.isShown()) {
                        if (LIVEplayer.currentType.equals("0")) {
                            this.listview.showPlayList(1);
                        } else if (!LIVEplayer.currentType.equals("1")) {
                            index = LIVEplayer.typeIndexGetFormId(LIVEplayer.currentType);
                            MGplayer.MyPrintln("right index:" + index);
                            if (index < 0) {
                                this.listview.showPlayList(0);
                            } else if (index == LIVEplayer.typeSize() - 1) {
                                this.listview.showPlayList(0);
                            } else if (LIVEplayer.typeNeedpsGet(LIVEplayer.typeIdGet(index + 1)).equals("0") || LIVEplayer.typePasswordOK) {
                                this.listview.showPlayList(LIVEplayer.typeIdGet(index + 1));
                            } else {
                                LIVEplayer.currentType = LIVEplayer.typeIdGet(index + 1);
                                if (LIVEplayer.show_ps_playlist) {
                                    this.listview.showPlayList(LIVEplayer.currentType);
                                } else {
                                    inputPasswordTypeView(LIVEplayer.typeIdGet(index + 1));
                                }
                            }
                        } else if (LIVEplayer.typeSize() <= 0) {
                            this.listview.showPlayList(0);
                        } else if (LIVEplayer.typeNeedpsGet(LIVEplayer.typeIdGet(0)).equals("0") || LIVEplayer.typePasswordOK) {
                            this.listview.showPlayList(LIVEplayer.typeIdGet(0));
                        } else {
                            LIVEplayer.currentType = LIVEplayer.typeIdGet(0);
                            if (LIVEplayer.show_ps_playlist) {
                                this.listview.showPlayList(LIVEplayer.currentType);
                            } else {
                                inputPasswordTypeView(LIVEplayer.typeIdGet(0));
                            }
                        }
                        this.listview.listFocus();
                        return true;
                    } else {
                        if (this.typeview.isShown()) {
                            this.typeview.hideTypeList();
                            this.listview.showViewNoTimeout();
                        } else if (this.previewview.isShown()) {
                            this.listview.showViewTimeout();
                            this.listview.listFocus();
                            this.previewview.hidePreviewList();
                            return true;
                        } else if (this.listview.isShown() && !this.typeview.isShown()) {
                            this.listview.showViewNoTimeout();
                            this.previewview.showPreviewList(Integer.parseInt(this.listview.getCurrentID()));
                            return true;
                        }
                        return true;
                    }
                    break;
                case 23:
                case 66:
                    if (!this.listview.isShown()) {
                        if (LIVEplayer.currentType == null || LIVEplayer.currentType.equals("0")) {
                            this.listview.showPlayList(0);
                        } else if (LIVEplayer.currentType != null && LIVEplayer.currentType.equals("1")) {
                            this.listview.showPlayList(1);
                        } else if (LIVEplayer.typeNeedpsGet(LIVEplayer.currentType).equals("0") || LIVEplayer.typePasswordOK) {
                            this.listview.showPlayList(LIVEplayer.currentType, true);
                        } else {
                            this.listview.showPlayList(0);
                        }
                        return true;
                    }
                    break;
                case 26:
                    MGplayer.MyPrintln("KeyEvent.KEYCODE_POWER");
                    break;
            }
            return super.onKeyDown(keyCode, event);
        }
        this.numChannel += String.valueOf(keyCode - 7);
        this.channeltext.setText(this.numChannel);
        this.channellayout.setVisibility(0);
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("num", this.numChannel);
        msg.setData(data);
        msg.what = 0;
        if (this.numHandler.hasMessages(0)) {
            this.numHandler.removeMessages(0);
        }
        this.numHandler.sendMessageDelayed(msg, 2000);
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        this.checkVideo_times = 0;
        switch (keyCode) {
            case 21:
                if (!(this.listview.isShown() || this.typeview.isShown() || this.previewview.isShown())) {
                    if (this.is_longclick) {
                        this.is_longclick = false;
                        return true;
                    }
                    this.infoview.showInfoPanal(LIVEplayer.currentID);
                    return true;
                }
            case 22:
                if (!(this.listview.isShown() || this.typeview.isShown() || this.previewview.isShown())) {
                    if (this.is_longclick) {
                        this.is_longclick = false;
                        return true;
                    }
                    this.liner.showView(this, this.listview.currentID(), LIVEplayer.currentLine);
                    return true;
                }
            case 82:
                MenuView.gridMenuInit(this);
                MenuView.showAlertDialog(this);
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void exitActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.myhomebar_text6).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {

            /* renamed from: com.gemini.play.LivePlayerActivity$27$1 */
            class C02981 implements Runnable {
                C02981() {
                }

                public void run() {
                    LivePlayerActivity.this.finish();
                }
            }

            public void onClick(DialogInterface dialog, int which) {
                LIVEplayer.stopCheckVideo();
                dialog.dismiss();
                LivePlayerActivity.this.stopVideo();
                LIVEplayer.typePasswordOK = false;
                if (MGplayer.boot_launcher) {
                    LIVEplayer.resumeDecode();
                    MyToast.makeText(LivePlayerActivity.this, LivePlayerActivity.this.getString(C0216R.string.liveplayer_text12).toString(), 0);
                    new Handler().postDelayed(new C02981(), 2000);
                    return;
                }
                LIVEplayer.resumeDecode();
                LivePlayerActivity.this.stopService(new Intent(LivePlayerActivity.this, LocalService.class));
                LivePlayerActivity.this.finish();
                Process.killProcess(Process.myPid());
                MGplayer.exit_app();
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private String showNextChannel() {
        UrlStatus status = LIVEplayer.getVideoStatusNext(Integer.parseInt(this.listview.getCurrentID()), !LIVEplayer.typePasswordOK);
        if (status == null) {
            return "";
        }
        int id = status.id;
        String name = status.name;
        if (id < 0) {
            return "";
        }
        this.listview.setCurrentID(id);
        return id + " - " + name;
    }

    private String showPreChannel() {
        UrlStatus status = LIVEplayer.getVideoStatusForward(Integer.parseInt(this.listview.getCurrentID()), !LIVEplayer.typePasswordOK);
        if (status == null) {
            return "";
        }
        int id = status.id;
        String name = status.name;
        if (id < 0) {
            return "";
        }
        this.listview.setCurrentID(id);
        return id + " - " + name;
    }

    private void inputPasswordTypeView(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View textEntryView = LayoutInflater.from(this).inflate(C0216R.layout.inputpasswordview, null);
        builder.setIcon(17301659);
        builder.setTitle(getString(C0216R.string.typelist_text2).toString());
        builder.setView(textEntryView);
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String password = ((EditText) textEntryView.findViewById(C0216R.id.ps_editText)).getText().toString();
                String type_ps = MGplayer.MyGetSharedPreferences(LivePlayerActivity.this, "data", 0).getString("type_password", null);
                if (type_ps == null) {
                    type_ps = MGplayer.type2password;
                }
                if (password.equals(type_ps)) {
                    LIVEplayer.typePasswordOK = true;
                    LivePlayerActivity.this.listview.showPlayList(id);
                    return;
                }
                MyToast.makeText(LivePlayerActivity.this, LivePlayerActivity.this.getString(C0216R.string.typelist_text3).toString(), 0);
                LIVEplayer.typePasswordOK = false;
                LIVEplayer.currentType = id;
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                LIVEplayer.typePasswordOK = false;
                LIVEplayer.currentType = id;
            }
        });
        builder.create().show();
    }

    private void inputPasswordView(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View textEntryView = LayoutInflater.from(this).inflate(C0216R.layout.inputpasswordview, null);
        builder.setIcon(17301659);
        builder.setTitle(getString(C0216R.string.typelist_text2).toString());
        builder.setView(textEntryView);
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String password = ((EditText) textEntryView.findViewById(C0216R.id.ps_editText)).getText().toString();
                String type_ps = MGplayer.MyGetSharedPreferences(LivePlayerActivity.this, "data", 0).getString("type_password", null);
                if (type_ps == null) {
                    type_ps = MGplayer.type2password;
                }
                if (password.equals(type_ps)) {
                    LIVEplayer.typePasswordOK = true;
                    LivePlayerActivity.this.channellayout.setVisibility(8);
                    LIVEplayer.currentID = id;
                    LivePlayerActivity.this.playVideo(LIVEplayer.currentID, 0, 0);
                    LivePlayerActivity.this.numChannel = "";
                    return;
                }
                MyToast.makeText(LivePlayerActivity.this, LivePlayerActivity.this.getString(C0216R.string.typelist_text3).toString(), 0);
                LIVEplayer.typePasswordOK = false;
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), null);
        builder.create().show();
    }

    public void showMAC() {
        if (!this.showMAC_runing && MGplayer.live_showmac > 0) {
            this.showMAC_runing = true;
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {

                /* renamed from: com.gemini.play.LivePlayerActivity$33$1 */
                class C03021 implements Runnable {
                    C03021() {
                    }

                    public void run() {
                        LivePlayerActivity.this.textmac.setVisibility(8);
                    }
                }

                public void run() {
                    int max = MGplayer.screenWidth;
                    Random random = new Random();
                    int w = random.nextInt(max);
                    int h = random.nextInt(MGplayer.screenHeight);
                    int s = random.nextInt(10) + 10;
                    MGplayer.MyPrintln("w:" + w + " h:" + h + " m:" + (random.nextInt(Device.DEFAULT_LEASE_TIME) + 1) + " s:" + s);
                    LivePlayerActivity.this.textmac.setVisibility(0);
                    LivePlayerActivity.this.textmac.setText(MGplayer.tv.GetMac());
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) LivePlayerActivity.this.textmac.getLayoutParams();
                    params.setMargins(w, h, 0, 0);
                    LivePlayerActivity.this.textmac.setLayoutParams(params);
                    new Handler().postDelayed(new C03021(), HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS);
                    mHandler.postDelayed(this, 120000);
                }
            }, 10000);
        }
    }

    public void checkVideo() {
        MGplayer.MyPrintln("checkVideo_runing:" + this.checkVideo_runing + " checkVideo:" + MGplayer.checkVideo_times);
        if (!this.checkVideo_runing && MGplayer.checkVideo_times > 0) {
            this.checkVideo_runing = true;
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (LivePlayerActivity.this.checkVideo_times > 0 && LivePlayerActivity.this.checkVideo_times > MGplayer.checkVideo_times) {
                        LIVEplayer.resumeDecode();
                        LivePlayerActivity.this.stopService(new Intent(LivePlayerActivity.this, LocalService.class));
                        LivePlayerActivity.this.finish();
                        Process.killProcess(Process.myPid());
                        MGplayer.exit_app();
                    }
                    LivePlayerActivity.this.checkVideo_times = LivePlayerActivity.this.checkVideo_times + 1;
                    MGplayer.MyPrintln("checkVideo:" + LivePlayerActivity.this.checkVideo_times);
                    mHandler.postDelayed(this, 10000);
                }
            }, 10000);
        }
    }

    protected void onResume() {
        MGplayer.video_every_interface(this.onControlVideo);
        super.onResume();
    }
}
