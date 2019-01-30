package com.gemini.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class VodPlayerMainActivity extends Activity {
    private static AlertDialog findDialog = null;
    private int AreaNumberIndex = 30;
    private ImageView Image20 = null;
    private ImageView Image21 = null;
    private ImageView Image22 = null;
    private ImageView Image23 = null;
    private ImageView Image24 = null;
    private TextView Text10 = null;
    private TextView Text11 = null;
    private TextView Text12 = null;
    private TextView Text20 = null;
    private TextView Text21 = null;
    private TextView Text22 = null;
    private TextView Text23 = null;
    private TextView Text24 = null;
    private TextView Text30 = null;
    private TextView Text31 = null;
    private TextView Text32 = null;
    private TextView Text33 = null;
    private TextView Text34 = null;
    private HistoryVodDB historyvoddber = null;
    private boolean isexit = false;
    private LinearLayout layout10 = null;
    private LinearLayout layout11 = null;
    private LinearLayout layout12 = null;
    private FrameLayout layout20 = null;
    private FrameLayout layout21 = null;
    private FrameLayout layout22 = null;
    private FrameLayout layout23 = null;
    private FrameLayout layout24 = null;
    private LinearLayout layout30 = null;
    private LinearLayout layout31 = null;
    private LinearLayout layout32 = null;
    private LinearLayout layout33 = null;
    private LinearLayout layout34 = null;
    public ListViewInterface onFindPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    MGplayer.MyPrintln("onFindPressed:" + VODplayer.url_param);
                    VodPlayerMainActivity.this.openListActivity("find");
                    return;
                case 1:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    return;
                case 2:
                    VODplayer.list.clear();
                    VODplayer.url_param = data;
                    return;
                default:
                    return;
            }
        }
    };
    public Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (VodPlayerMainActivity.this.f19s != null) {
                        for (int ii = 0; ii < VodPlayerMainActivity.this.f19s.size(); ii++) {
                            switch (ii) {
                                case 0:
                                    VodPlayerMainActivity.this.Image20.setImageBitmap(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).imagebit);
                                    VodPlayerMainActivity.this.Text20.setText(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).name);
                                    break;
                                case 1:
                                    VodPlayerMainActivity.this.Image21.setImageBitmap(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).imagebit);
                                    VodPlayerMainActivity.this.Text21.setText(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).name);
                                    break;
                                case 2:
                                    VodPlayerMainActivity.this.Image22.setImageBitmap(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).imagebit);
                                    VodPlayerMainActivity.this.Text22.setText(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).name);
                                    break;
                                case 3:
                                    VodPlayerMainActivity.this.Image23.setImageBitmap(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).imagebit);
                                    VodPlayerMainActivity.this.Text23.setText(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).name);
                                    break;
                                case 4:
                                    VodPlayerMainActivity.this.Image24.setImageBitmap(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).imagebit);
                                    VodPlayerMainActivity.this.Text24.setText(((VodListStatus) VodPlayerMainActivity.this.f19s.get(ii)).name);
                                    break;
                                default:
                                    break;
                            }
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private float rate;
    /* renamed from: s */
    private ArrayList<VodListStatus> f19s = null;
    private int showIndex = 0;
    private Typeface typeFace;

    /* renamed from: com.gemini.play.VodPlayerMainActivity$1 */
    class C06441 implements OnClickListener {
        C06441() {
        }

        public void onClick(View v) {
            VodPlayerMainActivity.this.EnterArea(10);
            VodPlayerMainActivity.this.EnterEvent(10);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerMainActivity$2 */
    class C06452 implements OnClickListener {
        C06452() {
        }

        public void onClick(View v) {
            VodPlayerMainActivity.this.EnterArea(11);
            VodPlayerMainActivity.this.EnterEvent(11);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerMainActivity$3 */
    class C06463 implements OnClickListener {
        C06463() {
        }

        public void onClick(View v) {
            VodPlayerMainActivity.this.EnterArea(12);
            VodPlayerMainActivity.this.EnterEvent(12);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerMainActivity$4 */
    class C06474 implements OnClickListener {
        C06474() {
        }

        public void onClick(View v) {
            VodPlayerMainActivity.this.EnterArea(20);
            VodPlayerMainActivity.this.EnterEvent(20);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerMainActivity$5 */
    class C06485 implements OnClickListener {
        C06485() {
        }

        public void onClick(View v) {
            VodPlayerMainActivity.this.EnterArea(21);
            VodPlayerMainActivity.this.EnterEvent(21);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerMainActivity$6 */
    class C06496 implements OnClickListener {
        C06496() {
        }

        public void onClick(View v) {
            VodPlayerMainActivity.this.EnterArea(22);
            VodPlayerMainActivity.this.EnterEvent(22);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerMainActivity$7 */
    class C06507 implements OnClickListener {
        C06507() {
        }

        public void onClick(View v) {
            VodPlayerMainActivity.this.EnterArea(23);
            VodPlayerMainActivity.this.EnterEvent(23);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerMainActivity$8 */
    class C06518 implements OnClickListener {
        C06518() {
        }

        public void onClick(View v) {
            VodPlayerMainActivity.this.EnterArea(24);
            VodPlayerMainActivity.this.EnterEvent(24);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerMainActivity$9 */
    class C06529 implements OnClickListener {
        C06529() {
        }

        public void onClick(View v) {
            VodPlayerMainActivity.this.EnterArea(30);
            VodPlayerMainActivity.this.EnterEvent(30);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.vodplayermain);
        getWindow().setFlags(1024, 1024);
        MGplayer.mediaplayervodheader(1);
        this.layout10 = (LinearLayout) findViewById(C0216R.id.layout10);
        this.layout10.setOnClickListener(new C06441());
        this.layout11 = (LinearLayout) findViewById(C0216R.id.layout11);
        this.layout11.setOnClickListener(new C06452());
        this.layout12 = (LinearLayout) findViewById(C0216R.id.layout12);
        this.layout12.setOnClickListener(new C06463());
        this.layout20 = (FrameLayout) findViewById(C0216R.id.layout20);
        this.layout20.setOnClickListener(new C06474());
        this.layout21 = (FrameLayout) findViewById(C0216R.id.layout21);
        this.layout21.setOnClickListener(new C06485());
        this.layout22 = (FrameLayout) findViewById(C0216R.id.layout22);
        this.layout22.setOnClickListener(new C06496());
        this.layout23 = (FrameLayout) findViewById(C0216R.id.layout23);
        this.layout23.setOnClickListener(new C06507());
        this.layout24 = (FrameLayout) findViewById(C0216R.id.layout24);
        this.layout24.setOnClickListener(new C06518());
        this.layout30 = (LinearLayout) findViewById(C0216R.id.layout30);
        this.layout30.setOnClickListener(new C06529());
        this.layout31 = (LinearLayout) findViewById(C0216R.id.layout31);
        this.layout31.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VodPlayerMainActivity.this.EnterArea(31);
                VodPlayerMainActivity.this.EnterEvent(31);
            }
        });
        this.layout32 = (LinearLayout) findViewById(C0216R.id.layout32);
        this.layout32.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VodPlayerMainActivity.this.EnterArea(32);
                VodPlayerMainActivity.this.EnterEvent(32);
            }
        });
        this.layout33 = (LinearLayout) findViewById(C0216R.id.layout33);
        this.layout33.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VodPlayerMainActivity.this.EnterArea(33);
                VodPlayerMainActivity.this.EnterEvent(33);
            }
        });
        this.layout34 = (LinearLayout) findViewById(C0216R.id.layout34);
        this.layout34.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                VodPlayerMainActivity.this.EnterArea(34);
                VodPlayerMainActivity.this.EnterEvent(34);
            }
        });
        EnterArea(this.AreaNumberIndex);
        this.typeFace = MGplayer.getFontsType(this);
        this.rate = MGplayer.getFontsRate();
        this.Text10 = (TextView) findViewById(C0216R.id.Text10);
        this.Text10.setTextSize(this.rate * 5.0f);
        this.Text10.setTypeface(this.typeFace);
        this.Text11 = (TextView) findViewById(C0216R.id.Text11);
        this.Text11.setTextSize(this.rate * 5.0f);
        this.Text11.setTypeface(this.typeFace);
        this.Text12 = (TextView) findViewById(C0216R.id.Text12);
        this.Text12.setTextSize(this.rate * 5.0f);
        this.Text12.setTypeface(this.typeFace);
        this.Text20 = (TextView) findViewById(C0216R.id.Text20);
        this.Text20.setTextSize(this.rate * 6.0f);
        this.Text20.setTypeface(this.typeFace);
        this.Text21 = (TextView) findViewById(C0216R.id.Text21);
        this.Text21.setTextSize(this.rate * 6.0f);
        this.Text21.setTypeface(this.typeFace);
        this.Text22 = (TextView) findViewById(C0216R.id.Text22);
        this.Text22.setTextSize(this.rate * 6.0f);
        this.Text22.setTypeface(this.typeFace);
        this.Text23 = (TextView) findViewById(C0216R.id.Text23);
        this.Text23.setTextSize(this.rate * 6.0f);
        this.Text23.setTypeface(this.typeFace);
        this.Text24 = (TextView) findViewById(C0216R.id.Text24);
        this.Text24.setTextSize(this.rate * 6.0f);
        this.Text24.setTypeface(this.typeFace);
        this.Text30 = (TextView) findViewById(C0216R.id.Text30);
        this.Text30.setTextSize(this.rate * 6.0f);
        this.Text30.setTypeface(this.typeFace);
        if (VODplayer.columner != null && VODplayer.columner.length >= 4) {
            this.Text30.setText(VODplayer.columner[0].name);
        }
        this.Text31 = (TextView) findViewById(C0216R.id.Text31);
        this.Text31.setTextSize(this.rate * 6.0f);
        this.Text31.setTypeface(this.typeFace);
        if (VODplayer.columner != null && VODplayer.columner.length >= 4) {
            this.Text31.setText(VODplayer.columner[1].name);
        }
        this.Text32 = (TextView) findViewById(C0216R.id.Text32);
        this.Text32.setTextSize(this.rate * 6.0f);
        this.Text32.setTypeface(this.typeFace);
        if (VODplayer.columner != null && VODplayer.columner.length >= 4) {
            this.Text32.setText(VODplayer.columner[2].name);
        }
        this.Text33 = (TextView) findViewById(C0216R.id.Text33);
        this.Text33.setTextSize(this.rate * 6.0f);
        this.Text33.setTypeface(this.typeFace);
        if (VODplayer.columner != null && VODplayer.columner.length >= 4) {
            this.Text33.setText(VODplayer.columner[3].name);
        }
        this.Text34 = (TextView) findViewById(C0216R.id.Text34);
        this.Text34.setTextSize(this.rate * 6.0f);
        this.Text34.setTypeface(this.typeFace);
        this.Image20 = (ImageView) findViewById(C0216R.id.Image20);
        this.Image21 = (ImageView) findViewById(C0216R.id.Image21);
        this.Image22 = (ImageView) findViewById(C0216R.id.Image22);
        this.Image23 = (ImageView) findViewById(C0216R.id.Image23);
        this.Image24 = (ImageView) findViewById(C0216R.id.Image24);
        new Thread() {
            public void run() {
                VodPlayerMainActivity.this.f19s = VODplayer.parseMainDB();
                if (VodPlayerMainActivity.this.f19s != null) {
                    VodPlayerMainActivity.this.cmdMessageLoadMain();
                }
            }
        }.start();
        new Handler().postDelayed(new Runnable() {

            /* renamed from: com.gemini.play.VodPlayerMainActivity$15$1 */
            class C06431 extends Thread {
                C06431() {
                }

                public void run() {
                    for (int ii = 0; ii < 3; ii++) {
                        VodPlayerMainActivity.this.f19s = VODplayer.parseMainXML();
                        if (VodPlayerMainActivity.this.f19s != null) {
                            VodPlayerMainActivity.this.cmdMessageLoadMain();
                            return;
                        }
                    }
                }
            }

            public void run() {
                new C06431().start();
            }
        }, 3000);
        ScrollTextView scroller = (ScrollTextView) findViewById(C0216R.id.scrolltext);
        scroller.init(getWindowManager());
        if (MGplayer.scrolltext != null) {
            MGplayer.MyPrintln("start scrolltext");
            scroller.setText("");
            scroller.start(this, MGplayer.scrolltext, 0, 0, 0, 0, 2.5f, (int) (10.0f * MGplayer.getFontsRate()), "FFFFFF");
        }
        timeView();
        String now = MGplayer.fromLongToDateString(MGplayer.seconds_prc - 604800000, "yyyy-MM-dd HH:mm:ss");
        this.historyvoddber = new HistoryVodDB(this);
        this.historyvoddber.deleteDataLittleDate(now);
    }

    public void timeView() {
        final Handler timeHander = new Handler();
        timeHander.postDelayed(new Runnable() {
            public void run() {
                MGplayer.MyPrintln("time now = " + MGplayer.time_now);
                TextView timetext = (TextView) VodPlayerMainActivity.this.findViewById(C0216R.id.timetext);
                timetext.setTextSize((float) ((int) (7.0f * MGplayer.getFontsRate())));
                timetext.setTypeface(MGplayer.getFontsType(VodPlayerMainActivity.this));
                timetext.setText(MGplayer.time_now);
                timeHander.postDelayed(this, HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS);
            }
        }, 1000);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                exitActivity2();
                return true;
            case 19:
                EnterArea(AreaNumber(keyCode));
                return true;
            case 20:
                EnterArea(AreaNumber(keyCode));
                return true;
            case 21:
                EnterArea(AreaNumber(keyCode));
                return true;
            case 22:
                EnterArea(AreaNumber(keyCode));
                return true;
            case 23:
            case 66:
                EnterEvent(this.AreaNumberIndex);
                return true;
            case 82:
                MenuView.gridMenuInit(this, 0);
                MenuView.showAlertDialog(this);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            InputStream is = conn.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            is.close();
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
            return bmp;
        }
    }

    private void TransArea() {
        this.layout10.setBackgroundResource(C0216R.drawable.gradient10);
        this.layout11.setBackgroundResource(C0216R.drawable.gradient10);
        this.layout12.setBackgroundResource(C0216R.drawable.gradient10);
        this.layout20.setBackgroundColor(Color.argb(0, 255, 255, 255));
        this.layout21.setBackgroundColor(Color.argb(0, 255, 255, 255));
        this.layout22.setBackgroundColor(Color.argb(0, 255, 255, 255));
        this.layout23.setBackgroundColor(Color.argb(0, 255, 255, 255));
        this.layout24.setBackgroundColor(Color.argb(0, 255, 255, 255));
        this.layout30.setBackgroundColor(Color.argb(0, 255, 255, 255));
        this.layout31.setBackgroundColor(Color.argb(0, 255, 255, 255));
        this.layout32.setBackgroundColor(Color.argb(0, 255, 255, 255));
        this.layout33.setBackgroundColor(Color.argb(0, 255, 255, 255));
        this.layout34.setBackgroundColor(Color.argb(0, 255, 255, 255));
    }

    private void EnterArea(int index) {
        MGplayer.MyPrintln("EnterArea:" + index);
        TransArea();
        switch (index) {
            case 10:
                this.layout10.setBackgroundResource(C0216R.drawable.gradient15);
                return;
            case 11:
                this.layout11.setBackgroundResource(C0216R.drawable.gradient15);
                return;
            case 12:
                this.layout12.setBackgroundResource(C0216R.drawable.gradient15);
                return;
            case 20:
                this.layout20.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            case 21:
                this.layout21.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            case 22:
                this.layout22.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            case 23:
                this.layout23.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            case 24:
                this.layout24.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            case 30:
                this.layout30.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            case 31:
                this.layout31.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            case 32:
                this.layout32.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            case 33:
                this.layout33.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            case 34:
                this.layout34.setBackgroundResource(C0216R.drawable.gradient14);
                return;
            default:
                return;
        }
    }

    private int AreaNumber(int keyCode) {
        switch (keyCode) {
            case 19:
                if (this.AreaNumberIndex >= 30 && this.AreaNumberIndex < 35) {
                    this.AreaNumberIndex = 10;
                    return this.AreaNumberIndex;
                } else if (this.AreaNumberIndex >= 20 && this.AreaNumberIndex < 25) {
                    return this.AreaNumberIndex;
                } else {
                    if (this.AreaNumberIndex >= 10 && this.AreaNumberIndex < 13) {
                        this.AreaNumberIndex--;
                        if (this.AreaNumberIndex < 10) {
                            this.AreaNumberIndex = 10;
                        }
                        return this.AreaNumberIndex;
                    }
                }
                break;
            case 20:
                if (this.AreaNumberIndex >= 30 && this.AreaNumberIndex < 35) {
                    return this.AreaNumberIndex;
                }
                if (this.AreaNumberIndex >= 20 && this.AreaNumberIndex < 25) {
                    this.AreaNumberIndex = 30;
                    return this.AreaNumberIndex;
                } else if (this.AreaNumberIndex >= 10 && this.AreaNumberIndex < 13) {
                    this.AreaNumberIndex++;
                    if (this.AreaNumberIndex > 12) {
                        this.AreaNumberIndex = 30;
                    }
                    return this.AreaNumberIndex;
                }
                break;
            case 21:
                if (this.AreaNumberIndex >= 30 && this.AreaNumberIndex < 35) {
                    this.AreaNumberIndex--;
                    if (this.AreaNumberIndex < 30) {
                        this.AreaNumberIndex = 30;
                    }
                    return this.AreaNumberIndex;
                } else if (this.AreaNumberIndex >= 20 && this.AreaNumberIndex < 25) {
                    this.AreaNumberIndex--;
                    if (this.AreaNumberIndex < 20) {
                        this.AreaNumberIndex = 10;
                    }
                    return this.AreaNumberIndex;
                } else if (this.AreaNumberIndex < 10 || this.AreaNumberIndex >= 13) {
                    return this.AreaNumberIndex;
                } else {
                    return this.AreaNumberIndex;
                }
            case 22:
                if (this.AreaNumberIndex >= 30 && this.AreaNumberIndex < 35) {
                    this.AreaNumberIndex++;
                    if (this.AreaNumberIndex >= 35) {
                        this.AreaNumberIndex = 34;
                    }
                    return this.AreaNumberIndex;
                } else if (this.AreaNumberIndex >= 20 && this.AreaNumberIndex < 25) {
                    this.AreaNumberIndex++;
                    if (this.AreaNumberIndex >= 25) {
                        this.AreaNumberIndex = 24;
                    }
                    return this.AreaNumberIndex;
                } else if (this.AreaNumberIndex >= 10 && this.AreaNumberIndex < 13) {
                    this.AreaNumberIndex = 20;
                    return this.AreaNumberIndex;
                }
                break;
        }
        return this.AreaNumberIndex;
    }

    public void EnterEvent(int index) {
        switch (index) {
            case 10:
                findActivity(this);
                return;
            case 11:
                historyActivity(this);
                return;
            case 12:
                collectActivity(this);
                return;
            case 20:
                if (this.f19s == null || this.f19s.size() <= 0 || this.f19s.get(0) == null || ((VodListStatus) this.f19s.get(0)).id == 0 || ((VodListStatus) this.f19s.get(0)).url == null) {
                    MyToast.makeText(this, getString(C0216R.string.vodplayermain_text4).toString(), 0);
                    return;
                } else {
                    openInfoActivity(String.valueOf(((VodListStatus) this.f19s.get(0)).id), "0");
                    return;
                }
            case 21:
                if (this.f19s == null || this.f19s.size() <= 1 || this.f19s.get(1) == null || ((VodListStatus) this.f19s.get(1)).id == 0 || ((VodListStatus) this.f19s.get(1)).url == null) {
                    MyToast.makeText(this, getString(C0216R.string.vodplayermain_text4).toString(), 0);
                    return;
                } else {
                    openInfoActivity(String.valueOf(((VodListStatus) this.f19s.get(1)).id), "0");
                    return;
                }
            case 22:
                if (this.f19s == null || this.f19s.size() <= 2 || this.f19s.get(2) == null || ((VodListStatus) this.f19s.get(2)).id == 0 || ((VodListStatus) this.f19s.get(2)).url == null) {
                    MyToast.makeText(this, getString(C0216R.string.vodplayermain_text4).toString(), 0);
                    return;
                } else {
                    openInfoActivity(String.valueOf(((VodListStatus) this.f19s.get(2)).id), "0");
                    return;
                }
            case 23:
                if (this.f19s == null || this.f19s.size() <= 3 || this.f19s.get(3) == null || ((VodListStatus) this.f19s.get(3)).id == 0 || ((VodListStatus) this.f19s.get(3)).url == null) {
                    MyToast.makeText(this, getString(C0216R.string.vodplayermain_text4).toString(), 0);
                    return;
                } else {
                    openInfoActivity(String.valueOf(((VodListStatus) this.f19s.get(3)).id), "0");
                    return;
                }
            case 24:
                if (this.f19s == null || this.f19s.size() <= 4 || this.f19s.get(4) == null || ((VodListStatus) this.f19s.get(4)).id == 0 || ((VodListStatus) this.f19s.get(4)).url == null) {
                    MyToast.makeText(this, getString(C0216R.string.vodplayermain_text4).toString(), 0);
                    return;
                } else {
                    openInfoActivity(String.valueOf(((VodListStatus) this.f19s.get(4)).id), "0");
                    return;
                }
            case 30:
                if (VODplayer.columner[0].needps == 1) {
                    inputPasswordView("0");
                    return;
                } else {
                    openListActivity("0");
                    return;
                }
            case 31:
                if (VODplayer.columner[1].needps == 1) {
                    inputPasswordView("1");
                    return;
                } else {
                    openListActivity("1");
                    return;
                }
            case 32:
                if (VODplayer.columner[2].needps == 1) {
                    inputPasswordView("2");
                    return;
                } else {
                    openListActivity("2");
                    return;
                }
            case 33:
                if (VODplayer.columner[3].needps == 1) {
                    inputPasswordView("3");
                    return;
                } else {
                    openListActivity("3");
                    return;
                }
            case 34:
                exitActivity();
                return;
            default:
                return;
        }
    }

    public void cmdMessageLoadMain() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        Message msg = new Message();
        msg.what = 0;
        this.rHandler.sendMessage(msg);
    }

    public void btnTranslate(View view) {
        TranslateAnimation ta = new TranslateAnimation(0.0f, 200.0f, 0.0f, 300.0f);
        ta.setDuration(1000);
        view.startAnimation(ta);
    }

    public void openListActivity(String type) {
        Intent intent = new Intent(this, VodPlayerList2Activity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void findActivity(Activity _this) {
        findDialog = new MyVodFind3().init(_this, this.onFindPressed);
    }

    private void hotActivity(Context _this) {
        VODplayer.list.clear();
        VODplayer.url_param = "&hot=0";
        openListActivity("hot");
    }

    private void historyActivity(Context _this) {
        MGplayer.MyPrintln("openListActivity historyActivity");
        VODplayer.list.clear();
        VODplayer.url_param = "";
        openListActivity("history");
    }

    private void collectActivity(Context _this) {
        VODplayer.list.clear();
        VODplayer.url_param = "";
        openListActivity("collect");
    }

    public void openInfoActivity(final String id, final String type) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(VodPlayerMainActivity.this, VodPlayerInfoActivity.class);
                intent.putExtra(TtmlNode.ATTR_ID, id);
                intent.putExtra("type", type);
                intent.putExtra("collect", 0);
                VodPlayerMainActivity.this.startActivity(intent);
            }
        }, 300);
    }

    public void exitActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.vodlist_text10).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                VodPlayerMainActivity.this.finish();
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void exitActivity2() {
        if (this.isexit) {
            this.isexit = false;
            finish();
            if (!MGplayer.boot_launcher) {
                stopService(new Intent(this, LocalService.class));
                Process.killProcess(Process.myPid());
                System.exit(0);
                return;
            }
            return;
        }
        this.isexit = true;
        MyToast.makeText(this, getString(C0216R.string.isexit).toString(), 0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                VodPlayerMainActivity.this.isexit = false;
            }
        }, HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS);
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
                String type_ps = MGplayer.MyGetSharedPreferences(VodPlayerMainActivity.this, "data", 0).getString("type_password", null);
                if (type_ps == null && VODplayer.columner != null) {
                    type_ps = VODplayer.columner[Integer.parseInt(id)].password;
                }
                if (!password.equals(type_ps) || VODplayer.columner == null) {
                    MyToast.makeText(VodPlayerMainActivity.this, VodPlayerMainActivity.this.getString(C0216R.string.typelist_text3).toString(), 0);
                    return;
                }
                VODplayer.columner[Integer.parseInt(id)].needps = 0;
                VodPlayerMainActivity.this.openListActivity(id);
            }
        });
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), null);
        builder.create().show();
    }

    public void onResume() {
        this.f19s = VODplayer.parseMainDB();
        if (this.f19s != null) {
            cmdMessageLoadMain();
        }
        super.onResume();
    }
}
