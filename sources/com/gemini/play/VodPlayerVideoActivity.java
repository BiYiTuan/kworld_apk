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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import com.google.android.exoplayer.hls.HlsChunkSource;
import io.vov.vitamio.widget.VideoView;
import net.lingala.zip4j.util.InternalZipConstants;
import org.videolan.vlc.VlcVideoView;

public class VodPlayerVideoActivity extends Activity implements OnGestureListener {
    private LinearLayout backlayout = null;
    private CollectVodDB collectvoddber;
    private GestureDetector detector = null;
    private boolean exited = false;
    private String file_size = null;
    private MyVodImageView imageview;
    private int info_collect;
    private String info_id;
    private String info_num;
    private String info_type;
    private MyVodInfoView infoview;
    private int mVideoViewCurrentDuration = 0;
    public ControlVideoInterface onControlVideo = new ControlVideoInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(VodPlayerVideoActivity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    VodPlayerVideoActivity.this.startActivity(intent);
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
                        VODplayer.playVideo(VodPlayerVideoActivity.this, VODplayer.video_url, seek, VODplayer.video_pw);
                    } else {
                        VODplayer.seekVideo(VODplayer.video_url, seek);
                    }
                    VODplayer.video_current = 0;
                    return;
                default:
                    return;
            }
        }
    };
    public ScrollViewInterface onScrollView = new C09147();
    public ListViewInterface onSelectionsPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VODplayer.video_url = VodPlayerVideoActivity.this.get_video_url_and_pw(VodPlayerVideoActivity.this.getNumUrl(VodPlayerVideoActivity.this.f20s.url, data));
                    VodPlayerVideoActivity.this.info_num = data;
                    VODplayer.playVideo(VodPlayerVideoActivity.this, VODplayer.video_url, VODplayer.video_pw);
                    VodPlayerVideoActivity.this.selectionsview.hideSelectionsView();
                    VodPlayerVideoActivity.this.infoview.setTitle(VodPlayerVideoActivity.this.f20s.name + " " + VodPlayerVideoActivity.this.info_num);
                    return;
                default:
                    return;
            }
        }
    };
    private ScrollTextView onescroller = null;
    private int openDB_postion = 0;
    public Handler rHandler = new C06628();
    private String ratebit = null;
    private int runThread_timeout = 0;
    /* renamed from: s */
    private VodListStatus f20s;
    private MyVodSelectionsView selectionsview;
    private FrameLayout vodProgressLayout = null;
    private PositionVodDB voddber = null;
    private boolean voddbtip = false;
    private VodPositionStatus vodstatus = null;

    /* renamed from: com.gemini.play.VodPlayerVideoActivity$1 */
    class C06531 implements OnClickListener {
        C06531() {
        }

        public void onClick(View v) {
            VodPlayerVideoActivity.this.exitActivity();
        }
    }

    /* renamed from: com.gemini.play.VodPlayerVideoActivity$2 */
    class C06542 implements OnClickListener {
        C06542() {
        }

        public void onClick(View v) {
            MenuView.decodeActivity(VodPlayerVideoActivity.this, VodPlayerVideoActivity.this.rHandler);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerVideoActivity$3 */
    class C06553 implements OnPreparedListener {
        C06553() {
        }

        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }
    }

    /* renamed from: com.gemini.play.VodPlayerVideoActivity$4 */
    class C06574 implements Runnable {

        /* renamed from: com.gemini.play.VodPlayerVideoActivity$4$1 */
        class C06561 extends Thread {
            C06561() {
            }

            public void run() {
                VodPlayerVideoActivity.this.clickrate();
            }
        }

        C06574() {
        }

        public void run() {
            VODplayer.playVideo(VodPlayerVideoActivity.this, VODplayer.video_url, VODplayer.video_pw);
            new C06561().start();
        }
    }

    /* renamed from: com.gemini.play.VodPlayerVideoActivity$8 */
    class C06628 extends Handler {

        /* renamed from: com.gemini.play.VodPlayerVideoActivity$8$1 */
        class C06611 implements Runnable {
            C06611() {
            }

            public void run() {
                VODplayer.playVideo(VodPlayerVideoActivity.this, VODplayer.video_url, VODplayer.video_pw);
            }
        }

        C06628() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String content = msg.getData().getString("content");
                    int times = msg.getData().getInt("times");
                    VodPlayerVideoActivity.this.onescroller.setText("");
                    VodPlayerVideoActivity.this.onescroller.setVisibility(0);
                    VodPlayerVideoActivity.this.onescroller.start_every(VodPlayerVideoActivity.this, content, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", times);
                    return;
                case 12:
                    MGplayer.MyPrintln("restart video 2");
                    VODplayer.stopVideo(msg.getData().getInt("inx"));
                    new Handler().postDelayed(new C06611(), 1500);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.VodPlayerVideoActivity$9 */
    class C06649 implements DialogInterface.OnClickListener {

        /* renamed from: com.gemini.play.VodPlayerVideoActivity$9$1 */
        class C06631 implements Runnable {
            C06631() {
            }

            public void run() {
                VodPlayerVideoActivity.this.finish();
            }
        }

        C06649() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            MyToast.makeText(VodPlayerVideoActivity.this, VodPlayerVideoActivity.this.getString(C0216R.string.liveplayer_text12).toString(), 0);
            VodPlayerVideoActivity.this.exited = true;
            VODplayer.stopVideo();
            VODplayer.mIsPause = true;
            VodPlayerVideoActivity.this.voddber.close();
            new Handler().postDelayed(new C06631(), 3000);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerVideoActivity$7 */
    class C09147 implements ScrollViewInterface {
        C09147() {
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
                    VodPlayerVideoActivity.this.rHandler.sendMessage(msg);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.vodvideo);
        getWindow().setFlags(1024, 1024);
        this.exited = false;
        Typeface typeFace = MGplayer.getFontsType(this);
        float rate = MGplayer.getFontsRate();
        this.voddber = new PositionVodDB(this);
        this.voddber.open();
        this.onescroller = (ScrollTextView) findViewById(C0216R.id.onescrolltext);
        this.infoview = (MyVodInfoView) findViewById(C0216R.id.myinfoview);
        this.infoview.setInterface(this.onInfoPressed);
        this.detector = new GestureDetector(this, this);
        this.collectvoddber = new CollectVodDB(this);
        this.selectionsview = (MyVodSelectionsView) findViewById(C0216R.id.selectionsview);
        this.selectionsview.setInterface(this.onSelectionsPressed);
        MGplayer.video_every_interface(this.onControlVideo);
        this.imageview = (MyVodImageView) findViewById(C0216R.id.imageview);
        this.info_id = getIntent().getStringExtra(TtmlNode.ATTR_ID);
        this.info_type = getIntent().getStringExtra("type");
        this.info_num = getIntent().getStringExtra("num");
        this.info_collect = getIntent().getIntExtra("collect", 0);
        MGplayer.MyPrintln("info id = " + this.info_id);
        if (MGplayer.isNumeric(this.info_id) && MGplayer.isNumeric(this.info_type) && MGplayer.isNumeric(this.info_num) && Integer.parseInt(this.info_num) >= 0) {
            this.vodstatus = this.voddber.get(Integer.parseInt(this.info_id), Integer.parseInt(this.info_type));
            if (this.vodstatus != null) {
                MGplayer.MyPrintln("info num = " + this.info_num + " position = " + this.vodstatus.position);
                this.voddbtip = true;
                openDBActivity();
            }
        }
        this.f20s = getStatus(this.info_id, this.info_type, this.info_collect);
        if (this.f20s == null || this.f20s.url == null) {
            MGplayer.MyPrintln("vodliststatus null");
            return;
        }
        VODplayer.video_url = get_video_url_and_pw(getNumUrl(this.f20s.url, this.info_num));
        MGplayer.MyPrintln("video url = " + VODplayer.video_url);
        if (VODplayer.video_url != null) {
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
                MGplayer.MyPrintln("start onescroll_txt");
                this.onescroller.setText("");
                this.onescroller.setVisibility(0);
                this.onescroller.start(this, MGplayer.onescroll_txt, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF", MGplayer.onescroll_times);
            }
            Button backbutton = (Button) findViewById(C0216R.id.backbutton);
            backbutton.setTextSize(8.0f * rate);
            backbutton.setTypeface(typeFace);
            backbutton.setOnClickListener(new C06531());
            Button decodebutton = (Button) findViewById(C0216R.id.decodebutton);
            decodebutton.setTextSize(8.0f * rate);
            decodebutton.setTypeface(typeFace);
            decodebutton.setOnClickListener(new C06542());
            VODplayer.VideoViewS = (VlcVideoView) findViewById(C0216R.id.VideoViewSoft);
            VODplayer.VideoViewS.setVisibility(0);
            VODplayer.VideoViewS2 = (VideoView) findViewById(C0216R.id.VideoViewSoft2);
            VODplayer.VideoViewS2.setVisibility(0);
            VODplayer.VideoViewH = (VideoView) findViewById(C0216R.id.VideoViewHard);
            VODplayer.VideoViewH.setVisibility(8);
            VODplayer.VideoViewH.setOnPreparedListener(new C06553());
            VODplayer.VideoViewH2 = (ExoPlayerView) findViewById(C0216R.id.VideoViewHard2);
            VODplayer.VideoViewH2.setDefaultControlsEnabled(false);
            VODplayer.VideoViewH2.setVisibility(8);
            if (VODplayer.video_url != null) {
                new Handler().postDelayed(new C06574(), 1000);
            }
            FrameLayout progressLayout = (FrameLayout) findViewById(C0216R.id.vodprogressLayout);
            LayoutParams layoutParams = (LayoutParams) progressLayout.getLayoutParams();
            layoutParams.width = (int) (128.0f * rate);
            layoutParams.height = (int) (48.0f * rate);
            TextView speedView = (TextView) findViewById(C0216R.id.progressSpeed);
            speedView.setTextSize(7.0f * rate);
            speedView.setTypeface(typeFace);
            ProgressBar progressBar = (ProgressBar) findViewById(C0216R.id.vodprogressBar);
            ViewGroup.LayoutParams layoutParams2 = (LayoutParams) progressBar.getLayoutParams();
            layoutParams2.width = (int) (48.0f * rate);
            layoutParams2.height = (int) (48.0f * rate);
            progressBar.setLayoutParams(layoutParams2);
            Handler mHandler = new Handler();
            final FrameLayout frameLayout = progressLayout;
            final TextView textView = speedView;
            final Handler handler = mHandler;
            mHandler.post(new Runnable() {
                public void run() {
                    MGplayer.MyPrintln("VODplayer.isPlaying():" + VODplayer.isPlaying());
                    if (!VodPlayerVideoActivity.this.exited) {
                        if (VODplayer.isPlaying()) {
                            frameLayout.setVisibility(8);
                            VodPlayerVideoActivity.this.imageview.hideView();
                        } else {
                            frameLayout.setVisibility(0);
                            textView.setText(MGplayer.getUidRxBytes() + "K/S");
                        }
                        handler.postDelayed(this, 1000);
                    }
                }
            });
            VODplayer.mIsPause = false;
        }
    }

    private void runThread() {
        final Handler runHandler = new Handler();
        runHandler.postDelayed(new Runnable() {

            /* renamed from: com.gemini.play.VodPlayerVideoActivity$6$1 */
            class C06591 extends Thread {
                C06591() {
                }

                public void run() {
                    VodPlayerVideoActivity.this.getInfo_p2p();
                }
            }

            public void run() {
                if (!VodPlayerVideoActivity.this.exited) {
                    if (VODplayer.video_url != null && VODplayer.video_url.startsWith("p2p://") && ((MGplayer.getCpuName().equals("HIK3V2") || MGplayer.getCpuName().equals("HI3716M")) && VODplayer.p2pCurrentDuration <= 0)) {
                        new C06591().start();
                    }
                    if (VODplayer.isPlaying()) {
                        int position = VODplayer.getProgress(VODplayer.video_url);
                        MGplayer.MyPrintln("#################### vod position: " + position);
                        MGplayer.MyPrintln("#################### vod position: " + VodPlayerVideoActivity.this.info_id + "&" + VodPlayerVideoActivity.this.info_type + "&" + VodPlayerVideoActivity.this.info_num);
                        if (MGplayer.isNumeric(VodPlayerVideoActivity.this.info_id) && MGplayer.isNumeric(VodPlayerVideoActivity.this.info_type) && MGplayer.isNumeric(VodPlayerVideoActivity.this.info_num) && !VodPlayerVideoActivity.this.voddbtip) {
                            VodPlayerVideoActivity.this.voddber.inserDataNoreRepeat(Integer.parseInt(VodPlayerVideoActivity.this.info_type), Integer.parseInt(VodPlayerVideoActivity.this.info_id), VodPlayerVideoActivity.this.info_num, position);
                        }
                    }
                    int total = VODplayer.getTotal(VODplayer.video_url);
                    int progress = VODplayer.getProgress(VODplayer.video_url);
                    MGplayer.MyPrintln("#################### progress: " + progress + " total:" + total);
                    if (progress > 0 && total > 1000 && progress >= total - 6000 && progress <= total + 10000) {
                        String video_url = VodPlayerVideoActivity.this.get_video_url_and_pw(VodPlayerVideoActivity.this.getNextUrl(VodPlayerVideoActivity.this.f20s.url, VodPlayerVideoActivity.this.info_num));
                        if (video_url != null) {
                            VODplayer.video_url = video_url;
                            VODplayer.playVideo(VodPlayerVideoActivity.this, VODplayer.video_url, VODplayer.video_pw);
                        } else {
                            VODplayer.playVideo(VodPlayerVideoActivity.this, VODplayer.video_url, VODplayer.video_pw);
                        }
                    }
                    VodPlayerVideoActivity.this.runThread_timeout = VodPlayerVideoActivity.this.runThread_timeout + 1;
                }
                if (!VodPlayerVideoActivity.this.exited) {
                    runHandler.postDelayed(this, HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS);
                }
            }
        }, HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS);
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

    private VodListStatus getStatus(String id, String itype, int collect) {
        if (collect == 0) {
            return VODplayer.getVodListStatus(id, itype);
        }
        MGplayer.MyPrintln("read db video");
        return this.collectvoddber.get(id, itype);
    }

    private String getNextUrl(String url, String num) {
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
                this.info_num = item2[0];
                return MGplayer.ju(item2[1]);
            }
        }
        return null;
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
        MGplayer.sendServerCmd(VODplayer.gete() + MGplayer.admindir + "/clickrate.php?mac=" + MGplayer.tv.GetMac() + "&type=" + this.info_type + "&id=" + this.info_id + "&chage=0" + MGplayer.get_key_value());
    }

    public void exitActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.vodlist_text10).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new C06649());
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void openDBActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.vodplay_text1).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                VodPlayerVideoActivity.this.voddbtip = false;
                if (VodPlayerVideoActivity.this.vodstatus != null) {
                    VodPlayerVideoActivity.this.info_num = String.valueOf(VodPlayerVideoActivity.this.vodstatus.num);
                    VODplayer.video_url = VodPlayerVideoActivity.this.get_video_url_and_pw(VodPlayerVideoActivity.this.getNumUrl(VodPlayerVideoActivity.this.f20s.url, VodPlayerVideoActivity.this.info_num));
                    VODplayer.playVideo(VodPlayerVideoActivity.this, VODplayer.video_url, VodPlayerVideoActivity.this.vodstatus.position, VODplayer.video_pw);
                    VodPlayerVideoActivity.this.openDB_postion = VodPlayerVideoActivity.this.vodstatus.position;
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                VodPlayerVideoActivity.this.voddbtip = false;
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
                    VodPlayerVideoActivity.this.backlayout.setVisibility(8);
                }
            }, 15000);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != 1) {
            return true;
        }
        if (VODplayer.ppVideo(VODplayer.video_url) == 0) {
            this.imageview.pauseImage();
        } else {
            this.imageview.playImage();
        }
        this.infoview.showInfoView();
        this.infoview.setTitle(this.f20s.name + " " + this.info_num);
        showBacklayout();
        return this.detector.onTouchEvent(event);
    }

    protected void onResume() {
        MGplayer.video_every_interface(this.onControlVideo);
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (this.selectionsview.isShown()) {
                    this.selectionsview.hideSelectionsView();
                    return true;
                }
                exitActivity();
                return super.onKeyDown(keyCode, event);
            case 19:
                this.selectionsview.showSelectionsView(this.f20s, this.info_collect, this.info_num);
                break;
            case 21:
                if (!this.infoview.isShown()) {
                    this.infoview.showInfoView();
                    this.infoview.setTitle(this.f20s.name + " " + this.info_num);
                    showBacklayout();
                    break;
                }
                this.infoview.backward();
                break;
            case 22:
                if (!this.infoview.isShown()) {
                    this.infoview.showInfoView();
                    this.infoview.setTitle(this.f20s.name + " " + this.info_num);
                    showBacklayout();
                    break;
                }
                this.infoview.forward();
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
        return super.onKeyDown(keyCode, event);
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() <= 20.0f && e2.getX() - e1.getX() <= 20.0f) {
            if (e1.getY() - e2.getY() > 20.0f) {
                this.selectionsview.showSelectionsView(this.f20s, this.info_collect);
            } else if (e2.getY() - e1.getY() <= 20.0f) {
                this.infoview.showInfoView();
                this.infoview.setTitle(this.f20s.name + " " + this.info_num);
            }
        }
        return false;
    }

    protected void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public boolean onDown(MotionEvent e) {
        this.infoview.showInfoView();
        this.infoview.setTitle(this.f20s.name + " " + this.info_num);
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
}
