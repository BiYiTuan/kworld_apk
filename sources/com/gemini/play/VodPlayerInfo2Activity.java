package com.gemini.play;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import com.google.android.exoplayer.ExoPlayer.Factory;
import com.google.android.exoplayer.hls.HlsChunkSource;
import io.vov.vitamio.widget.VideoView;
import net.lingala.zip4j.util.InternalZipConstants;
import org.videolan.vlc.VlcVideoView;

public class VodPlayerInfo2Activity extends Activity implements OnGestureListener {
    private Builder DBbuilder = null;
    private TextView area;
    private LinearLayout backlayout = null;
    private Bitmap bimage;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private int button_index;
    private TextView clickrate;
    private CollectVodDB collectvoddber1;
    private CollectVodDB collectvoddber2;
    private GestureDetector detector = null;
    private boolean exited = false;
    private String file_size = null;
    private HistoryVodDB historyvoddber = null;
    private ImageView image;
    private MyVodImageView imageview;
    private int info_collect1;
    private int info_collect2;
    private String info_id1;
    private String info_id2;
    private String info_num2;
    private String info_type1;
    private String info_type2;
    private MyVodInfoView infoview;
    private FrameLayout infoviewactivity;
    private TextView intro3;
    private TextView intro4;
    private boolean isOnInfoView = true;
    private MyVodLineView liner = null;
    private int mIsPause = 0;
    private int mVideoViewCurrentDuration = 0;
    private TextView name;
    private int nextmodel = 0;
    public ControlVideoInterface onControlVideo = new ControlVideoInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(VodPlayerInfo2Activity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    VodPlayerInfo2Activity.this.startActivity(intent);
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
                    int seek = Integer.parseInt(data);
                    VODplayer.video_seek = seek;
                    MGplayer.MyPrintln("url:" + VODplayer.video_url + " onInfoPressed seek:" + seek);
                    if (VODplayer.video_url.startsWith("gp2p://")) {
                        VODplayer.playVideo(VodPlayerInfo2Activity.this, VODplayer.video_url, seek, VODplayer.video_pw);
                    } else {
                        VODplayer.seekVideo(VODplayer.video_url, seek);
                    }
                    VODplayer.video_current = 0;
                    return;
                case 1:
                    String url_line = VodPlayerInfo2Activity.this.getNumUrl(VodPlayerInfo2Activity.this.f18s.url, VodPlayerInfo2Activity.this.info_num2);
                    String[] url_lines = url_line.split("@\\$@");
                    MGplayer.MyPrintln("url_line:" + url_line);
                    if (url_lines.length >= 2) {
                        VodPlayerInfo2Activity.this.liner.showView(VodPlayerInfo2Activity.this, url_line);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onLinePressed = new ListViewInterface() {

        /* renamed from: com.gemini.play.VodPlayerInfo2Activity$25$1 */
        class C05951 implements Runnable {
            C05951() {
            }

            public void run() {
                VODplayer.playVideo(VodPlayerInfo2Activity.this, VODplayer.video_url, VODplayer.video_pw);
            }
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VODplayer.video_url = VodPlayerInfo2Activity.this.get_video_url_and_pw(data);
                    if (VODplayer.video_url != null) {
                        new Handler().postDelayed(new C05951(), 1000);
                    }
                    VODplayer.mIsPause = false;
                    VodPlayerInfo2Activity.this.exited = false;
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
                    VodPlayerInfo2Activity.this.rHandler2.sendMessage(msg);
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onSelectionsPressed1 = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VodPlayerInfo2Activity.this.openVideoActivity(VodPlayerInfo2Activity.this.info_id1, VodPlayerInfo2Activity.this.info_type1, data);
                    VodPlayerInfo2Activity.this.selectionsview1.hideSelectionsView();
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onSelectionsPressed2 = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VODplayer.video_url = VodPlayerInfo2Activity.this.get_video_url_and_pw(VodPlayerInfo2Activity.this.getNumUrl(VodPlayerInfo2Activity.this.f18s.url, data));
                    VodPlayerInfo2Activity.this.info_num2 = data;
                    VODplayer.playVideo(VodPlayerInfo2Activity.this, VODplayer.video_url, VODplayer.video_pw);
                    VodPlayerInfo2Activity.this.selectionsview2.hideSelectionsView();
                    VodPlayerInfo2Activity.this.infoview.setTitle(VodPlayerInfo2Activity.this.f18s.name + " " + VodPlayerInfo2Activity.this.info_num2);
                    return;
                default:
                    return;
            }
        }
    };
    private ScrollTextView onescroller = null;
    private int openDB_postion = 0;
    private ProgressBar progressBar;
    private FrameLayout progressLayout;
    final Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    VodPlayerInfo2Activity.this.image = (ImageView) VodPlayerInfo2Activity.this.findViewById(C0216R.id.image);
                    VodPlayerInfo2Activity.this.image.setImageBitmap(VodPlayerInfo2Activity.this.bimage);
                    return;
                case 1:
                    VodPlayerInfo2Activity.this.init(VodPlayerInfo2Activity.this.info_id1, VodPlayerInfo2Activity.this.info_type1, VodPlayerInfo2Activity.this.info_collect1);
                    return;
                default:
                    return;
            }
        }
    };
    public Handler rHandler2 = new Handler() {

        /* renamed from: com.gemini.play.VodPlayerInfo2Activity$31$1 */
        class C05981 implements Runnable {
            C05981() {
            }

            public void run() {
                VODplayer.playVideo(VodPlayerInfo2Activity.this, VODplayer.video_url, VODplayer.video_pw);
            }
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String content = msg.getData().getString("content");
                    int times = msg.getData().getInt("times");
                    VodPlayerInfo2Activity.this.onescroller.setText("");
                    VodPlayerInfo2Activity.this.onescroller.setVisibility(0);
                    VodPlayerInfo2Activity.this.onescroller.start_every(VodPlayerInfo2Activity.this, content, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", times);
                    return;
                case 12:
                    MGplayer.MyPrintln("restart video 2");
                    VODplayer.stopVideo(msg.getData().getInt("inx"));
                    new Handler().postDelayed(new C05981(), 1500);
                    return;
                default:
                    return;
            }
        }
    };
    private float rate;
    private String ratebit = null;
    private int runThread_timeout = 0;
    /* renamed from: s */
    private VodListStatus f18s;
    private MyVodSelectionsView selectionsview1;
    private MyVodSelectionsView selectionsview2;
    private TextView speedView;
    private TextView total;
    private TextView type;
    private Typeface typeFace;
    private TextView updatetime;
    private FrameLayout videoviewactivity;
    private FrameLayout vodProgressLayout = null;
    private PositionVodDB voddber = null;
    private boolean voddbtip = false;
    private VodPositionStatus vodstatus = null;
    private TextView year;

    /* renamed from: com.gemini.play.VodPlayerInfo2Activity$1 */
    class C05921 extends Thread {
        C05921() {
        }

        public void run() {
            VodListStatus ss = VODplayer.parseCollectXML(VodPlayerInfo2Activity.this.info_type1, Integer.parseInt(VodPlayerInfo2Activity.this.info_id1));
            if (ss != null) {
                VodPlayerInfo2Activity.this.collectvoddber1.insert(ss, ss.imagebit, Integer.parseInt(VodPlayerInfo2Activity.this.info_type1));
                Message msg = new Message();
                msg.what = 1;
                VodPlayerInfo2Activity.this.rHandler.sendMessageDelayed(msg, 500);
            }
        }
    }

    /* renamed from: com.gemini.play.VodPlayerInfo2Activity$3 */
    class C05993 implements OnClickListener {
        C05993() {
        }

        public void onClick(View v) {
            VodPlayerInfo2Activity.this.exitVideoViewActivity();
        }
    }

    /* renamed from: com.gemini.play.VodPlayerInfo2Activity$4 */
    class C06004 implements OnClickListener {
        C06004() {
        }

        public void onClick(View v) {
            MenuView.decodeActivity(VodPlayerInfo2Activity.this, VodPlayerInfo2Activity.this.rHandler);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerInfo2Activity$5 */
    class C06015 implements OnPreparedListener {
        C06015() {
        }

        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    }

    /* renamed from: com.gemini.play.VodPlayerInfo2Activity$8 */
    class C06048 implements OnFocusChangeListener {
        C06048() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                VodPlayerInfo2Activity.this.button0.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                VodPlayerInfo2Activity.this.button0.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.vodplayerinfo2);
        getWindow().setFlags(1024, 1024);
        this.info_id1 = getIntent().getStringExtra(TtmlNode.ATTR_ID);
        this.info_type1 = getIntent().getStringExtra("type");
        this.info_collect1 = getIntent().getIntExtra("collect", 0);
        MGplayer.MyPrintln("info id = " + this.info_id1);
        MGplayer.MyPrintln("info type = " + this.info_type1);
        MGplayer.MyPrintln("info collect = " + this.info_collect1);
        MGplayer.video_every_interface(this.onControlVideo);
        init_ui();
        VodListStatus s = init(this.info_id1, this.info_type1, this.info_collect1);
        String now = MGplayer.fromLongToDateString(MGplayer.seconds_prc, "yyyy-MM-dd HH:mm:ss");
        if (this.info_collect1 == 0) {
            if (VODplayer.columner_needps[Integer.parseInt(this.info_type1)] == 0 && s != null) {
                this.historyvoddber.insert(s, s.imagebit, Integer.parseInt(this.info_type1), now);
            }
        } else if (this.info_collect1 == 1) {
            new C05921().start();
        } else if (this.info_collect1 == 2) {
            final String str = now;
            new Thread() {
                public void run() {
                    VodListStatus ss = VODplayer.parseCollectXML(VodPlayerInfo2Activity.this.info_type1, Integer.parseInt(VodPlayerInfo2Activity.this.info_id1));
                    if (ss != null) {
                        VodPlayerInfo2Activity.this.historyvoddber.insert(ss, ss.imagebit, Integer.parseInt(VodPlayerInfo2Activity.this.info_type1), str);
                        Message msg = new Message();
                        msg.what = 1;
                        VodPlayerInfo2Activity.this.rHandler.sendMessageDelayed(msg, 500);
                    }
                }
            }.start();
        }
        this.infoviewactivity = (FrameLayout) findViewById(C0216R.id.infoview);
        this.videoviewactivity = (FrameLayout) findViewById(C0216R.id.videoview);
        this.isOnInfoView = true;
        this.voddber = new PositionVodDB(this);
        this.voddber.open();
        this.onescroller = (ScrollTextView) findViewById(C0216R.id.onescrolltext);
        this.infoview = (MyVodInfoView) findViewById(C0216R.id.myinfoview);
        this.infoview.setInterface(this.onInfoPressed);
        this.detector = new GestureDetector(this, this);
        this.collectvoddber2 = new CollectVodDB(this);
        this.selectionsview2 = (MyVodSelectionsView) findViewById(C0216R.id.selectionsview2);
        this.selectionsview2.setInterface(this.onSelectionsPressed2);
        this.imageview = (MyVodImageView) findViewById(C0216R.id.imageview);
        this.backlayout = (LinearLayout) findViewById(C0216R.id.backlayout);
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
            this.onescroller.setText("");
            this.onescroller.setVisibility(0);
            this.onescroller.start(this, MGplayer.onescroll_txt, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", MGplayer.onescroll_times);
        }
        Button backbutton = (Button) findViewById(C0216R.id.backbutton);
        backbutton.setTextSize(8.0f * this.rate);
        backbutton.setTypeface(this.typeFace);
        backbutton.setOnClickListener(new C05993());
        Button decodebutton = (Button) findViewById(C0216R.id.decodebutton);
        decodebutton.setTextSize(8.0f * this.rate);
        decodebutton.setTypeface(this.typeFace);
        decodebutton.setOnClickListener(new C06004());
        VODplayer.VideoViewS = (VlcVideoView) findViewById(C0216R.id.VideoViewSoft);
        VODplayer.VideoViewS.setVisibility(0);
        VODplayer.VideoViewS2 = (VideoView) findViewById(C0216R.id.VideoViewSoft2);
        VODplayer.VideoViewS2.setVisibility(0);
        VODplayer.VideoViewH = (VideoView) findViewById(C0216R.id.VideoViewHard);
        VODplayer.VideoViewH.setVisibility(8);
        VODplayer.VideoViewH.setOnPreparedListener(new C06015());
        VODplayer.VideoViewH2 = (ExoPlayerView) findViewById(C0216R.id.VideoViewHard2);
        VODplayer.VideoViewH2.setDefaultControlsEnabled(false);
        VODplayer.VideoViewH2.setVisibility(8);
        this.progressLayout = (FrameLayout) findViewById(C0216R.id.vodprogressLayout);
        LayoutParams layoutParams = (LayoutParams) this.progressLayout.getLayoutParams();
        layoutParams.width = (int) (this.rate * 128.0f);
        layoutParams.height = (int) (this.rate * 48.0f);
        this.speedView = (TextView) findViewById(C0216R.id.progressSpeed);
        this.speedView.setTextSize(7.0f * this.rate);
        this.speedView.setTypeface(this.typeFace);
        this.progressBar = (ProgressBar) findViewById(C0216R.id.vodprogressBar);
        ViewGroup.LayoutParams layoutParams2 = (LayoutParams) this.progressBar.getLayoutParams();
        layoutParams2.width = (int) (this.rate * 48.0f);
        layoutParams2.height = (int) (this.rate * 48.0f);
        this.progressBar.setLayoutParams(layoutParams2);
        VODplayer.mIsPause = false;
        this.exited = false;
        this.nextmodel = 0;
        progressThread();
        runThread();
        createDBActivity();
    }

    private void init_ui() {
        this.selectionsview1 = (MyVodSelectionsView) findViewById(C0216R.id.selectionsview1);
        this.selectionsview1.setInterface(this.onSelectionsPressed1);
        this.collectvoddber1 = new CollectVodDB(this);
        this.historyvoddber = new HistoryVodDB(this);
    }

    private VodListStatus getStatus(String id, String itype, int collect) {
        if (collect == 0) {
            return VODplayer.getVodListStatus(id, itype);
        }
        VodListStatus s;
        Message msg;
        if (collect == 1) {
            MGplayer.MyPrintln("read db info");
            s = this.collectvoddber1.get(id, itype);
            msg = new Message();
            msg.what = 0;
            this.rHandler.sendMessageDelayed(msg, 500);
            return s;
        } else if (collect != 2) {
            return null;
        } else {
            MGplayer.MyPrintln("read history db info");
            VodHistoryStatus s_tmp = this.historyvoddber.get(id, itype);
            s = new VodListStatus();
            if (s_tmp != null) {
                s.name = s_tmp.name;
                s.image = s_tmp.image;
                s.imagebit = s_tmp.imagebit;
                s.url = s_tmp.url;
                s.area = s_tmp.area;
                s.year = s_tmp.year;
                s.type = s_tmp.type;
                s.intro1 = s_tmp.intro1;
                s.intro2 = s_tmp.intro2;
                s.intro3 = s_tmp.intro3;
                s.intro4 = s_tmp.intro4;
                s.id = s_tmp.id;
                s.clickrate = s_tmp.clickrate;
                s.recommend = s_tmp.recommend;
                s.chage = s_tmp.chage;
                s.updatetime = s_tmp.updatetime;
                s.infotype = s_tmp.infotype;
            }
            msg = new Message();
            msg.what = 0;
            this.rHandler.sendMessageDelayed(msg, 500);
            return s;
        }
    }

    private VodListStatus init(String id, String itype, final int collect) {
        if (!MGplayer.isNumeric(itype) || !MGplayer.isNumeric(id) || Integer.parseInt(itype) > 3) {
            return null;
        }
        MGplayer.MyPrintln("vod init 1");
        int index = Integer.parseInt(itype);
        final VodListStatus s = getStatus(id, itype, collect);
        if (s == null) {
            return null;
        }
        this.typeFace = MGplayer.getFontsType(this);
        this.rate = MGplayer.getFontsRate();
        this.button_index = 0;
        new Thread() {
            public void run() {
                MGplayer.MyPrintln("collect = " + collect);
                String url;
                if (collect == 0) {
                    VodPlayerInfo2Activity.this.bimage = VODplayer.listbitmapCache.getBitmap(s.image);
                    if (VodPlayerInfo2Activity.this.bimage == null) {
                        if (s.image == null || !s.image.startsWith("http://")) {
                            url = VODplayer.gete() + "/images/vodpic/" + s.image;
                        } else {
                            url = s.image;
                        }
                        MGplayer.MyPrintln("http bitmap:" + url);
                        if (!url.endsWith("null")) {
                            if (s.imagebit == null || s.imagebit.getHeight() <= 16 || s.imagebit.getWidth() <= 16) {
                                VodPlayerInfo2Activity.this.bimage = VODplayer.listbitmapCache.getBitmap(url);
                                if (VodPlayerInfo2Activity.this.bimage == null) {
                                    VodPlayerInfo2Activity.this.bimage = MGplayer.getHttpBitmap(url);
                                }
                            } else {
                                VodPlayerInfo2Activity.this.bimage = s.imagebit;
                            }
                        }
                    }
                } else {
                    VodListStatus collects = VODplayer.parseCollectXML(VodPlayerInfo2Activity.this.info_type1, s.id);
                    if (!(collects == null || collects.url == null || collects.url.length() <= 0)) {
                        s.url = collects.url;
                    }
                    VodPlayerInfo2Activity.this.bimage = s.imagebit;
                    if (VodPlayerInfo2Activity.this.bimage == null) {
                        if (s.image == null || !s.image.startsWith("http://")) {
                            url = VODplayer.gete() + "/images/vodpic/" + s.image;
                        } else {
                            url = s.image;
                        }
                        if (s.imagebit == null || s.imagebit.getHeight() <= 16 || s.imagebit.getWidth() <= 16) {
                            VodPlayerInfo2Activity.this.bimage = VODplayer.listbitmapCache.getBitmap(url);
                            if (VodPlayerInfo2Activity.this.bimage == null) {
                                VodPlayerInfo2Activity.this.bimage = MGplayer.getHttpBitmap(url);
                            }
                        } else {
                            VodPlayerInfo2Activity.this.bimage = s.imagebit;
                        }
                    }
                }
                Message msg = new Message();
                msg.what = 0;
                VodPlayerInfo2Activity.this.rHandler.sendMessageDelayed(msg, 500);
            }
        }.start();
        this.clickrate = (TextView) findViewById(C0216R.id.clickrate);
        this.clickrate.setTextSize(this.rate * 8.0f);
        this.clickrate.setTypeface(this.typeFace);
        this.clickrate.setText(getString(C0216R.string.playerinfo_text7).toString() + ":" + String.valueOf(s.clickrate));
        if (MGplayer.custom().equals("msiptv")) {
            this.clickrate.setVisibility(8);
        }
        this.total = (TextView) findViewById(C0216R.id.total);
        this.total.setTextSize(this.rate * 8.0f);
        this.total.setTypeface(this.typeFace);
        MGplayer.MyPrintln("s.updatetime:" + s.updatetime);
        String newnum = "1";
        String[] urlss = s.url.split("\\|");
        if (urlss.length > 0) {
            String[] item = urlss[urlss.length - 1].split("#");
            if (item.length >= 2 && item[0] != null) {
                newnum = item[0];
            }
        }
        this.total.setText(getString(C0216R.string.playerinfo_text14).toString() + ":" + urlss.length);
        this.updatetime = (TextView) findViewById(C0216R.id.updatetime);
        this.updatetime.setTextSize(this.rate * 8.0f);
        this.updatetime.setTypeface(this.typeFace);
        MGplayer.MyPrintln("s.updatetime:" + s.updatetime);
        this.updatetime.setText(getString(C0216R.string.playerinfo_text9).toString() + ":" + VODplayer.secondToData2((long) s.updatetime) + InternalZipConstants.ZIP_FILE_SEPARATOR + getString(C0216R.string.playerinfo_text12).toString() + newnum + getString(C0216R.string.playerinfo_text13).toString());
        this.name = (TextView) findViewById(C0216R.id.name);
        this.name.setTextSize(this.rate * 13.0f);
        this.name.setTypeface(this.typeFace);
        MGplayer.MyPrintln("s.name:" + s.name);
        this.name.setText(s.name);
        this.area = (TextView) findViewById(C0216R.id.area);
        this.area.setTextSize(this.rate * 9.0f);
        this.area.setTypeface(this.typeFace);
        this.area.setText(getString(C0216R.string.playerinfo_text5).toString() + ":" + getArea(s.area, index));
        this.type = (TextView) findViewById(C0216R.id.type);
        this.type.setTextSize(this.rate * 9.0f);
        this.type.setTypeface(this.typeFace);
        this.type.setText(getString(C0216R.string.playerinfo_text8).toString() + ":" + getType(s.type, index));
        this.year = (TextView) findViewById(C0216R.id.year);
        this.year.setTextSize(this.rate * 9.0f);
        this.year.setTypeface(this.typeFace);
        this.year.setText(getString(C0216R.string.playerinfo_text6).toString() + ":" + getYears(s.year, index));
        this.intro3 = (TextView) findViewById(C0216R.id.intro3);
        this.intro3.setTextSize(this.rate * 9.0f);
        this.intro3.setTypeface(this.typeFace);
        this.intro3.setText(getString(C0216R.string.playerinfo_text10).toString() + ":" + s.intro3);
        this.intro4 = (TextView) findViewById(C0216R.id.intro4);
        this.intro4.setTextSize(this.rate * 9.0f);
        this.intro4.setTypeface(this.typeFace);
        this.intro4.setText(s.intro4);
        this.intro4.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.button0 = (Button) findViewById(C0216R.id.button0);
        this.button0.setTextSize(this.rate * 10.0f);
        this.button0.setTypeface(this.typeFace);
        this.button0.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (s != null && s.url != null) {
                    String[] urlss = s.url.split("\\|");
                    String num = "1";
                    if (urlss.length > 0) {
                        String[] item = urlss[0].split("#");
                        if (item.length >= 2 && item[0] != null && item[0].length() > 0) {
                            num = item[0];
                        }
                    }
                    VodPlayerInfo2Activity.this.openVideoActivity(VodPlayerInfo2Activity.this.info_id1, VodPlayerInfo2Activity.this.info_type1, num);
                }
            }
        });
        this.button0.setOnFocusChangeListener(new C06048());
        this.button0.setFocusable(true);
        this.button0.setFocusableInTouchMode(true);
        this.button0.requestFocus();
        this.button0.requestFocusFromTouch();
        this.button1 = (Button) findViewById(C0216R.id.button1);
        if (MGplayer.custom().equals("hmiptvkor") || MGplayer.custom().equals("hmiptv6") || MGplayer.custom().equals("hkszx")) {
            this.button1.setText(getString(C0216R.string.playerinfo_text15).toString());
        }
        this.button1.setTextSize(this.rate * 10.0f);
        this.button1.setTypeface(this.typeFace);
        this.button1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VodPlayerInfo2Activity.this.selectionsview1.showSelectionsView(s, VodPlayerInfo2Activity.this.info_collect1);
            }
        });
        this.button1.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    VodPlayerInfo2Activity.this.button1.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    VodPlayerInfo2Activity.this.button1.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
        this.button2 = (Button) findViewById(C0216R.id.button2);
        this.button2.setTextSize(this.rate * 10.0f);
        this.button2.setTypeface(this.typeFace);
        this.button2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (VodPlayerInfo2Activity.this.collectvoddber1.insert(s, VodPlayerInfo2Activity.this.bimage, Integer.parseInt(VodPlayerInfo2Activity.this.info_type1)) == 0) {
                    Toast.makeText(VodPlayerInfo2Activity.this, VodPlayerInfo2Activity.this.getString(C0216R.string.collect_text2).toString(), 0).show();
                } else {
                    Toast.makeText(VodPlayerInfo2Activity.this, VodPlayerInfo2Activity.this.getString(C0216R.string.collect_text1).toString(), 0).show();
                }
            }
        });
        this.button2.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    VodPlayerInfo2Activity.this.button2.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    VodPlayerInfo2Activity.this.button2.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
        this.button3 = (Button) findViewById(C0216R.id.button3);
        this.button3.setTextSize(this.rate * 10.0f);
        this.button3.setTypeface(this.typeFace);
        this.button3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VodPlayerInfo2Activity.this.finish();
            }
        });
        this.button3.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    VodPlayerInfo2Activity.this.button3.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    VodPlayerInfo2Activity.this.button3.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
        return s;
    }

    private String getArea(String area, int index) {
        String v = "";
        if (area != null) {
            String[] areas = area.split("\\|");
            int ii = 0;
            while (ii < areas.length) {
                MGplayer.MyPrintln("areas " + ii + " " + areas[ii]);
                if (MGplayer.isNumeric(areas[ii])) {
                    VodTypeStatus s = VODplayer.typeGet(index);
                    if (!(s == null || s.areas == null || Integer.parseInt(areas[ii]) - 1 < 0)) {
                        int areas_index = Integer.parseInt(areas[ii]) - 1;
                        if (areas_index < s.areas.length) {
                            v = v + s.areas[areas_index];
                        }
                    }
                }
                ii++;
            }
        }
        MGplayer.MyPrintln("areas:" + v);
        return v;
    }

    private String getType(String type, int index) {
        String v = "";
        if (type != null) {
            String[] types = type.split("\\|");
            int ii = 0;
            while (ii < types.length) {
                MGplayer.MyPrintln("types " + ii + " " + types[ii]);
                if (MGplayer.isNumeric(types[ii])) {
                    VodTypeStatus s = VODplayer.typeGet(index);
                    if (!(s == null || s.types == null || Integer.parseInt(types[ii]) - 1 < 0)) {
                        int type_index = Integer.parseInt(types[ii]) - 1;
                        if (type_index < s.types.length) {
                            v = v + s.types[type_index];
                            if (ii < types.length - 1) {
                                v = v + ",";
                            }
                        }
                    }
                }
                ii++;
            }
        }
        MGplayer.MyPrintln("getType:" + v);
        return v;
    }

    private String getYears(String area, int index) {
        String v = "";
        if (area != null) {
            String[] years = area.split("\\|");
            int ii = 0;
            while (ii < years.length) {
                MGplayer.MyPrintln("years " + ii + " " + years[ii]);
                if (MGplayer.isNumeric(years[ii])) {
                    VodTypeStatus s = VODplayer.typeGet(index);
                    if (!(s == null || s.years == null)) {
                        int years_index = Integer.parseInt(years[ii]) - 1;
                        if (years_index < s.years.length) {
                            if (Integer.parseInt(years[ii]) - 1 < 0 || Integer.parseInt(years[ii]) - 1 >= s.years.length) {
                                v = v + years[ii];
                            } else {
                                v = v + s.years[years_index];
                            }
                        }
                    }
                }
                ii++;
            }
        }
        MGplayer.MyPrintln("getYears:" + v);
        return v;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.isOnInfoView && event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 4:
                    if (this.selectionsview1.isShown()) {
                        this.selectionsview1.hideSelectionsView();
                        SelectButton(true, this.button_index);
                        return true;
                    }
                    break;
                case 21:
                    if (!this.selectionsview1.isShown()) {
                        SelectButton(false, -1);
                        return true;
                    }
                    break;
                case 22:
                    if (!this.selectionsview1.isShown()) {
                        SelectButton(true, -1);
                        return true;
                    }
                    break;
                case 82:
                    MenuView.gridMenuInit(this, 0);
                    MenuView.showAlertDialog(this);
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void SelectButton(boolean direct, int index) {
        if (index <= -1) {
            if (this.button0 != null && this.button0.isFocusable()) {
                this.button_index = 0;
            } else if (this.button1 != null && this.button1.isFocusable()) {
                this.button_index = 1;
            } else if (this.button2 != null && this.button2.isFocusable()) {
                this.button_index = 2;
            } else if (this.button3 != null && this.button3.isFocusable()) {
                this.button_index = 3;
            }
            MGplayer.MyPrintln("button index = " + this.button_index);
            if (direct) {
                this.button_index++;
            } else {
                this.button_index--;
            }
            if (this.button_index > 3) {
                this.button_index = 3;
            } else if (this.button_index < 0) {
                this.button_index = 0;
            }
        } else {
            this.button_index = index;
        }
        if (this.button0 != null && this.button1 != null && this.button2 != null && this.button3 != null) {
            switch (this.button_index) {
                case 0:
                    this.button0.setFocusable(true);
                    this.button0.setFocusableInTouchMode(true);
                    this.button0.requestFocus();
                    this.button0.requestFocusFromTouch();
                    this.button1.setFocusable(false);
                    this.button1.setFocusableInTouchMode(false);
                    this.button2.setFocusable(false);
                    this.button2.setFocusableInTouchMode(false);
                    this.button3.setFocusable(false);
                    this.button3.setFocusableInTouchMode(false);
                    return;
                case 1:
                    this.button1.setFocusable(true);
                    this.button1.setFocusableInTouchMode(true);
                    this.button1.requestFocus();
                    this.button1.requestFocusFromTouch();
                    this.button0.setFocusable(false);
                    this.button0.setFocusableInTouchMode(false);
                    this.button2.setFocusable(false);
                    this.button2.setFocusableInTouchMode(false);
                    this.button3.setFocusable(false);
                    this.button3.setFocusableInTouchMode(false);
                    return;
                case 2:
                    this.button2.setFocusable(true);
                    this.button2.setFocusableInTouchMode(true);
                    this.button2.requestFocus();
                    this.button2.requestFocusFromTouch();
                    this.button1.setFocusable(false);
                    this.button1.setFocusableInTouchMode(false);
                    this.button0.setFocusable(false);
                    this.button0.setFocusableInTouchMode(false);
                    this.button3.setFocusable(false);
                    this.button3.setFocusableInTouchMode(false);
                    return;
                case 3:
                    this.button3.setFocusable(true);
                    this.button3.setFocusableInTouchMode(true);
                    this.button3.requestFocus();
                    this.button3.requestFocusFromTouch();
                    this.button1.setFocusable(false);
                    this.button1.setFocusableInTouchMode(false);
                    this.button2.setFocusable(false);
                    this.button2.setFocusableInTouchMode(false);
                    this.button0.setFocusable(false);
                    this.button0.setFocusableInTouchMode(false);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onResume() {
        MGplayer.video_every_interface(this.onControlVideo);
        super.onResume();
    }

    public void openVideoActivity(String id, String type, String num) {
        this.infoviewactivity.setVisibility(8);
        this.videoviewactivity.setVisibility(0);
        this.isOnInfoView = false;
        this.info_num2 = num;
        this.liner = new MyVodLineView();
        this.liner.initView(this);
        this.liner.setInterface(this.onLinePressed);
        open_videoview(id, type, num, this.info_collect1);
    }

    private void open_videoview(String id, String type, String num, int collect) {
        this.info_id2 = id;
        this.info_type2 = type;
        this.info_collect2 = collect;
        this.info_num2 = num;
        this.f18s = getStatus(this.info_id2, this.info_type2, this.info_collect2);
        if (this.f18s == null || this.f18s.url == null) {
            MGplayer.MyPrintln("vodliststatus null");
            return;
        }
        String url_line = get_video_url_and_pw(getNumUrl(this.f18s.url, this.info_num2));
        MGplayer.MyPrintln("url_line:" + url_line + "url_lines:" + url_line.split("@\\$@").length);
        VODplayer.video_url = get_video_url_and_pw(getNumUrl(this.f18s.url, this.info_num2, 0));
        if (MGplayer.isNumeric(this.info_id2) && MGplayer.isNumeric(this.info_type2)) {
            this.vodstatus = this.voddber.get(Integer.parseInt(this.info_id2), Integer.parseInt(this.info_type2));
            if (this.vodstatus != null) {
                MGplayer.MyPrintln("info num = " + this.info_num2 + " position = " + this.vodstatus.position);
                this.voddbtip = true;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        VodPlayerInfo2Activity.this.openDBActivity();
                    }
                }, 2000);
            }
        }
        if (VODplayer.video_url != null) {
            new Handler().postDelayed(new Runnable() {

                /* renamed from: com.gemini.play.VodPlayerInfo2Activity$19$1 */
                class C05911 extends Thread {
                    C05911() {
                    }

                    public void run() {
                        VodPlayerInfo2Activity.this.clickrate();
                    }
                }

                public void run() {
                    VODplayer.playVideo(VodPlayerInfo2Activity.this, VODplayer.video_url, VODplayer.video_pw);
                    new C05911().start();
                }
            }, 1000);
        }
        VODplayer.mIsPause = false;
        this.exited = false;
    }

    private void progressThread() {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                if (!(VodPlayerInfo2Activity.this.exited || VodPlayerInfo2Activity.this.isOnInfoView)) {
                    if (VODplayer.isPlaying()) {
                        VodPlayerInfo2Activity.this.progressLayout.setVisibility(8);
                        VodPlayerInfo2Activity.this.imageview.hideView();
                    } else if (!VODplayer.mIsPause) {
                        VodPlayerInfo2Activity.this.progressLayout.setVisibility(0);
                        VodPlayerInfo2Activity.this.speedView.setText(MGplayer.getUidRxBytes() + "K/S");
                    }
                }
                mHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void runThread() {
        final Handler runHandler = new Handler();
        runHandler.postDelayed(new Runnable() {

            /* renamed from: com.gemini.play.VodPlayerInfo2Activity$21$1 */
            class C05931 extends Thread {
                C05931() {
                }

                public void run() {
                    VodPlayerInfo2Activity.this.getInfo_p2p();
                }
            }

            /* renamed from: com.gemini.play.VodPlayerInfo2Activity$21$2 */
            class C05942 implements Runnable {
                C05942() {
                }

                public void run() {
                    if (VODplayer.isPlaying()) {
                        String video_url = VodPlayerInfo2Activity.this.get_video_url_and_pw(VodPlayerInfo2Activity.this.getNextUrl(VodPlayerInfo2Activity.this.f18s.url, VodPlayerInfo2Activity.this.info_num2));
                        if (video_url != null) {
                            VODplayer.video_url = video_url;
                            VODplayer.playVideo(VodPlayerInfo2Activity.this, VODplayer.video_url, VODplayer.video_pw);
                            return;
                        }
                        VODplayer.playVideo(VodPlayerInfo2Activity.this, VODplayer.video_url, VODplayer.video_pw);
                    }
                }
            }

            public void run() {
                if (!(VodPlayerInfo2Activity.this.exited || VodPlayerInfo2Activity.this.isOnInfoView)) {
                    if (VODplayer.video_url != null && VODplayer.video_url.startsWith("p2p://") && ((MGplayer.getCpuName().equals("HIK3V2") || MGplayer.getCpuName().equals("HI3716M")) && VODplayer.p2pCurrentDuration <= 0)) {
                        new C05931().start();
                    }
                    if (VODplayer.isPlaying()) {
                        int position = VODplayer.getProgress(VODplayer.video_url);
                        MGplayer.MyPrintln("#################### vod position: " + position);
                        MGplayer.MyPrintln("#################### vod position: " + VodPlayerInfo2Activity.this.info_id2 + "&" + VodPlayerInfo2Activity.this.info_type2 + "&" + VodPlayerInfo2Activity.this.info_num2);
                        if (MGplayer.isNumeric(VodPlayerInfo2Activity.this.info_id2) && MGplayer.isNumeric(VodPlayerInfo2Activity.this.info_type2) && !VodPlayerInfo2Activity.this.voddbtip) {
                            VodPlayerInfo2Activity.this.voddber.inserDataNoreRepeat(Integer.parseInt(VodPlayerInfo2Activity.this.info_type2), Integer.parseInt(VodPlayerInfo2Activity.this.info_id2), VodPlayerInfo2Activity.this.info_num2, position);
                        }
                    }
                    int progress = VODplayer.getProgress(VODplayer.video_url);
                    int total = VODplayer.getTotal(VODplayer.video_url);
                    MGplayer.MyPrintln("#################### progress: " + progress + " total:" + total);
                    if (progress <= 0 || total <= 1000 || progress < total - 150000 || progress > total + 10000) {
                        VodPlayerInfo2Activity.this.nextmodel = 0;
                    } else {
                        if (VodPlayerInfo2Activity.this.nextmodel == 0) {
                            new Handler().postDelayed(new C05942(), (long) ((total - progress) + Factory.DEFAULT_MIN_REBUFFER_MS));
                        }
                        VodPlayerInfo2Activity.this.nextmodel = 1;
                    }
                    VodPlayerInfo2Activity.this.runThread_timeout = VodPlayerInfo2Activity.this.runThread_timeout + 1;
                }
                runHandler.postDelayed(this, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
            }
        }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
    }

    private void createDBActivity() {
        if (this.DBbuilder == null) {
            this.DBbuilder = new Builder(this);
            this.DBbuilder.setMessage(getString(C0216R.string.vodplay_text1).toString());
            this.DBbuilder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    VodPlayerInfo2Activity.this.voddbtip = false;
                    if (VodPlayerInfo2Activity.this.vodstatus != null) {
                        VodPlayerInfo2Activity.this.info_num2 = String.valueOf(VodPlayerInfo2Activity.this.vodstatus.num);
                        VODplayer.video_url = VodPlayerInfo2Activity.this.get_video_url_and_pw(VodPlayerInfo2Activity.this.getNumUrl(VodPlayerInfo2Activity.this.f18s.url, VodPlayerInfo2Activity.this.info_num2));
                        VODplayer.playVideo(VodPlayerInfo2Activity.this, VODplayer.video_url, VodPlayerInfo2Activity.this.vodstatus.position, VODplayer.video_pw);
                        VodPlayerInfo2Activity.this.openDB_postion = VodPlayerInfo2Activity.this.vodstatus.position;
                    }
                    dialog.dismiss();
                }
            });
            this.DBbuilder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    VodPlayerInfo2Activity.this.voddbtip = false;
                    dialog.dismiss();
                }
            });
        }
    }

    private void openDBActivity() {
        this.DBbuilder.create().show();
    }

    private String getNextUrl(String url, String num) {
        return getNextUrl(url, num, 0);
    }

    private String getNextUrl(String url, String num, int line) {
        String[] item;
        String[] urlss = url.split("\\|");
        if (urlss.length > 0) {
            item = urlss[urlss.length - 1].split("#");
            if (item.length >= 2 && MGplayer.isNumeric(item[0]) && Integer.parseInt(item[0]) < Integer.parseInt(num)) {
                return null;
            }
        }
        int ii = 0;
        while (ii < urlss.length) {
            item = urlss[ii].split("#");
            if (item.length < 2 || !item[0].equals(num)) {
                ii++;
            } else if (ii >= urlss.length - 1) {
                return null;
            } else {
                String[] item2 = urlss[ii + 1].split("#");
                if (item.length < 2) {
                    return null;
                }
                this.info_num2 = item2[0];
                String gurlss = MGplayer.ju(item2[1]);
                String[] gurls = gurlss.split("@\\$@");
                if (gurls.length > line) {
                    return gurls[line];
                }
                if (gurls.length > 0) {
                    return gurls[0];
                }
                return gurlss;
            }
        }
        return null;
    }

    private void getInfo_p2p() {
        String[] arrs = VODplayer.video_url.split(InternalZipConstants.ZIP_FILE_SEPARATOR);
        MGplayer.MyPrintln("===vod arrs length = " + arrs.length);
        if (arrs.length >= 4) {
            String info = VODplayer.sendHttpRequesttotal(arrs[3].split("\\.")[0], MGplayer.port());
            MGplayer.MyPrintln("speed value info :" + info);
            if (info.length() > 0) {
                int start = info.indexOf("byterate=") + 10;
                if (info.length() > start) {
                    this.ratebit = info.substring(start, start + info.substring(start).indexOf("\""));
                    start = info.indexOf("file_size=") + 11;
                    if (info.length() > start) {
                        this.file_size = info.substring(start, start + info.substring(start).indexOf("\""));
                        if (this.ratebit != null && this.file_size != null && MGplayer.isNumeric(this.ratebit) && MGplayer.isNumeric(this.file_size)) {
                            VODplayer.p2pCurrentDuration = (int) ((Long.parseLong(this.file_size) / Long.parseLong(this.ratebit)) * 1000);
                        }
                    }
                }
            }
        }
    }

    private String getNumUrl(String url, String num) {
        String[] urlss = url.split("\\|");
        for (String split : urlss) {
            String[] item = split.split("#");
            if (item.length >= 2 && item[0].equals(num)) {
                return MGplayer.ju(item[1]);
            }
        }
        return null;
    }

    private String getNumUrl(String url, String num, int line) {
        String[] urlss = url.split("\\|");
        int ii = 0;
        while (ii < urlss.length) {
            String[] item = urlss[ii].split("#");
            if (item.length < 2 || !item[0].equals(num)) {
                ii++;
            } else {
                String gurlss = MGplayer.ju(item[1]);
                String[] gurls = gurlss.split("@\\$@");
                if (gurls.length > line) {
                    return gurls[line];
                }
                if (gurls.length > 0) {
                    return gurls[0];
                }
                return gurlss;
            }
        }
        return null;
    }

    private String get_video_url_and_pw(String get_url_pw) {
        if (get_url_pw == null) {
            return null;
        }
        String[] get_url_pws = get_url_pw.split("geminipwgemini");
        if (get_url_pws.length >= 2 && get_url_pw.indexOf("p2p://") >= 0 && get_url_pw.indexOf("geminipwgemini") >= 0) {
            VODplayer.video_url = get_url_pws[0].replace("geminipwgemini", "");
            if (get_url_pws[1].length() > 0) {
                VODplayer.video_pw = get_url_pws[1];
            } else {
                VODplayer.video_pw = null;
            }
        } else if (get_url_pws.length >= 2) {
            VODplayer.video_url = get_url_pws[0].replace("geminipwgemini", "");
            VODplayer.video_pw = null;
        } else {
            VODplayer.video_url = get_url_pw.replace("geminipwgemini", "");
            VODplayer.video_pw = null;
        }
        return VODplayer.video_url;
    }

    private void clickrate() {
        MGplayer.sendServerCmd(VODplayer.gete() + MGplayer.admindir + "/clickrate.php?mac=" + MGplayer.tv.GetMac() + "&type=" + this.info_type2 + "&id=" + this.info_id2 + "&chage=0" + MGplayer.get_key_value());
    }

    public void exitVideoViewActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.vodlist_text10).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {

            /* renamed from: com.gemini.play.VodPlayerInfo2Activity$27$1 */
            class C05961 implements Runnable {
                C05961() {
                }

                public void run() {
                    VodPlayerInfo2Activity.this.infoviewactivity.setVisibility(0);
                    VodPlayerInfo2Activity.this.videoviewactivity.setVisibility(8);
                    VodPlayerInfo2Activity.this.isOnInfoView = true;
                    if (VodPlayerInfo2Activity.this.button0 != null) {
                        VodPlayerInfo2Activity.this.button0.setFocusable(true);
                        VodPlayerInfo2Activity.this.button0.setFocusableInTouchMode(true);
                        VodPlayerInfo2Activity.this.button0.requestFocus();
                        VodPlayerInfo2Activity.this.button0.requestFocusFromTouch();
                    }
                }
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MyToast.makeText(VodPlayerInfo2Activity.this, VodPlayerInfo2Activity.this.getString(C0216R.string.liveplayer_text12).toString(), 0);
                VodPlayerInfo2Activity.this.exited = true;
                VODplayer.stopVideo();
                VODplayer.mIsPause = true;
                VodPlayerInfo2Activity.this.voddber.close();
                new Handler().postDelayed(new C05961(), 2000);
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showBacklayout() {
        if (this.backlayout != null) {
            this.backlayout.setVisibility(0);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    VodPlayerInfo2Activity.this.backlayout.setVisibility(8);
                }
            }, 15000);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!this.isOnInfoView) {
            switch (keyCode) {
                case 4:
                    if (this.selectionsview2.isShown()) {
                        this.selectionsview2.hideSelectionsView();
                        return true;
                    }
                    exitVideoViewActivity();
                    return super.onKeyDown(keyCode, event);
                case 19:
                    this.selectionsview2.showSelectionsView(this.f18s, this.info_collect2, this.info_num2);
                    break;
                case 21:
                    if (!this.infoview.isShown()) {
                        this.infoview.showInfoView();
                        this.infoview.setTitle(this.f18s.name + " " + this.info_num2);
                        showBacklayout();
                        break;
                    }
                    this.infoview.backward();
                    break;
                case 22:
                    String url_line = getNumUrl(this.f18s.url, this.info_num2);
                    String[] url_lines = url_line.split("@\\$@");
                    if (this.infoview.isShown() || url_lines.length < 2) {
                        if (!this.infoview.isShown()) {
                            this.infoview.showInfoView();
                            this.infoview.setTitle(this.f18s.name + " " + this.info_num2);
                            showBacklayout();
                            break;
                        }
                        this.infoview.forward();
                        break;
                    }
                    this.liner.showView(this, url_line);
                    break;
                    break;
                case 23:
                case 66:
                    if (VODplayer.ppVideo(VODplayer.video_url) != 0) {
                        this.imageview.playImage();
                        break;
                    }
                    this.imageview.pauseImage();
                    break;
                case 82:
                    MenuView.gridMenuInit(this, 0);
                    MenuView.showAlertDialog(this);
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != 1) {
            return true;
        }
        if (!(this.f18s == null || this.f18s.name == null)) {
            if (VODplayer.ppVideo(VODplayer.video_url) == 0) {
                this.imageview.pauseImage();
            } else {
                this.imageview.playImage();
            }
            this.infoview.showInfoView();
            this.infoview.setTitle(this.f18s.name + " " + this.info_num2);
            showBacklayout();
        }
        return this.detector.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent motionEvent) {
        if (!this.isOnInfoView) {
            this.infoview.showInfoView();
            this.infoview.setTitle(this.f18s.name + " " + this.info_num2);
        }
        return false;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!this.isOnInfoView && e1.getX() - e2.getX() <= 20.0f && e2.getX() - e1.getX() <= 20.0f) {
            if (e1.getY() - e2.getY() > 20.0f) {
                this.selectionsview2.showSelectionsView(this.f18s, this.info_collect2);
            } else if (e2.getY() - e1.getY() <= 20.0f) {
                this.infoview.showInfoView();
                this.infoview.setTitle(this.f18s.name + " " + this.info_num2);
            }
        }
        return false;
    }
}
