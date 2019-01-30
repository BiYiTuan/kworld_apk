package com.gemini.play;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;
import net.lingala.zip4j.util.InternalZipConstants;

public class VodPlayerInfoActivity extends Activity {
    private TextView area;
    private Bitmap bimage;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private int button_index;
    private TextView clickrate;
    private CollectVodDB collectvoddber;
    private ImageView image;
    private int info_collect;
    private String info_id;
    private String info_type;
    private TextView intro3;
    private TextView intro4;
    private TextView name;
    public ControlVideoInterface onControlVideo = new ControlVideoInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(VodPlayerInfoActivity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    VodPlayerInfoActivity.this.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    };
    public ListViewInterface onSelectionsPressed = new ListViewInterface() {
        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    VodPlayerInfoActivity.this.openVideoActivity(VodPlayerInfoActivity.this.info_id, VodPlayerInfoActivity.this.info_type, data);
                    VodPlayerInfoActivity.this.selectionsview.hideSelectionsView();
                    return;
                default:
                    return;
            }
        }
    };
    final Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    VodPlayerInfoActivity.this.image = (ImageView) VodPlayerInfoActivity.this.findViewById(C0216R.id.image);
                    VodPlayerInfoActivity.this.image.setImageBitmap(VodPlayerInfoActivity.this.bimage);
                    return;
                case 1:
                    VodPlayerInfoActivity.this.init(VodPlayerInfoActivity.this.info_id, VodPlayerInfoActivity.this.info_type, VodPlayerInfoActivity.this.info_collect);
                    return;
                default:
                    return;
            }
        }
    };
    private float rate;
    private MyVodSelectionsView selectionsview;
    private TextView total;
    private TextView type;
    private Typeface typeFace;
    private TextView updatetime;
    private TextView year;

    /* renamed from: com.gemini.play.VodPlayerInfoActivity$1 */
    class C06061 extends Thread {
        C06061() {
        }

        public void run() {
            VodListStatus ss = VODplayer.parseCollectXML(VodPlayerInfoActivity.this.info_type, Integer.parseInt(VodPlayerInfoActivity.this.info_id));
            VodPlayerInfoActivity.this.collectvoddber.insert(ss, ss.imagebit, Integer.parseInt(VodPlayerInfoActivity.this.info_type));
            Message msg = new Message();
            msg.what = 1;
            VodPlayerInfoActivity.this.rHandler.sendMessageDelayed(msg, 500);
        }
    }

    /* renamed from: com.gemini.play.VodPlayerInfoActivity$4 */
    class C06094 implements OnFocusChangeListener {
        C06094() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                VodPlayerInfoActivity.this.button0.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                VodPlayerInfoActivity.this.button0.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.VodPlayerInfoActivity$6 */
    class C06116 implements OnFocusChangeListener {
        C06116() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                VodPlayerInfoActivity.this.button1.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                VodPlayerInfoActivity.this.button1.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.VodPlayerInfoActivity$8 */
    class C06138 implements OnFocusChangeListener {
        C06138() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                VodPlayerInfoActivity.this.button2.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                VodPlayerInfoActivity.this.button2.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.VodPlayerInfoActivity$9 */
    class C06149 implements OnClickListener {
        C06149() {
        }

        public void onClick(View v) {
            VodPlayerInfoActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.vodplayerinfo);
        getWindow().setFlags(1024, 1024);
        this.info_id = getIntent().getStringExtra(TtmlNode.ATTR_ID);
        this.info_type = getIntent().getStringExtra("type");
        this.info_collect = getIntent().getIntExtra("collect", 0);
        MGplayer.MyPrintln("info id = " + this.info_id);
        MGplayer.MyPrintln("info type = " + this.info_type);
        MGplayer.MyPrintln("info collect = " + this.info_collect);
        MGplayer.video_every_interface(this.onControlVideo);
        init_ui();
        init(this.info_id, this.info_type, this.info_collect);
        if (this.info_collect != 0) {
            new C06061().start();
        }
    }

    private void init_ui() {
        this.selectionsview = (MyVodSelectionsView) findViewById(C0216R.id.selectionsview);
        this.selectionsview.setInterface(this.onSelectionsPressed);
        this.collectvoddber = new CollectVodDB(this);
    }

    private VodListStatus getStatus(String id, String itype, int collect) {
        if (collect == 0) {
            return VODplayer.getVodListStatus(id, itype);
        }
        MGplayer.MyPrintln("read db info");
        VodListStatus s = this.collectvoddber.get(id, itype);
        Message msg = new Message();
        msg.what = 0;
        this.rHandler.sendMessageDelayed(msg, 500);
        return s;
    }

    private void init(String id, String itype, final int collect) {
        if (MGplayer.isNumeric(itype) && MGplayer.isNumeric(id) && Integer.parseInt(itype) <= 3) {
            MGplayer.MyPrintln("vod init 1");
            int index = Integer.parseInt(itype);
            final VodListStatus s = getStatus(id, itype, collect);
            if (s != null) {
                this.typeFace = MGplayer.getFontsType(this);
                this.rate = MGplayer.getFontsRate();
                this.button_index = 0;
                new Thread() {
                    public void run() {
                        MGplayer.MyPrintln("collect = " + collect);
                        String url;
                        if (collect == 0) {
                            VodPlayerInfoActivity.this.bimage = VODplayer.listbitmapCache.getBitmap(s.image);
                            if (VodPlayerInfoActivity.this.bimage == null) {
                                if (s.image == null || !s.image.startsWith("http://")) {
                                    url = VODplayer.gete() + "/images/vodpic/" + s.image;
                                } else {
                                    url = s.image;
                                }
                                MGplayer.MyPrintln("http bitmap:" + url);
                                if (s.imagebit != null) {
                                    VodPlayerInfoActivity.this.bimage = s.imagebit;
                                } else {
                                    VodPlayerInfoActivity.this.bimage = MGplayer.getHttpBitmap(url);
                                }
                            }
                        } else {
                            VodListStatus collects = VODplayer.parseCollectXML(VodPlayerInfoActivity.this.info_type, s.id);
                            if (collects != null && collects.url != null && collects.url.length() <= 0) {
                                MGplayer.MyPrintln("collects.url:" + collects.url);
                                s.url = collects.url;
                                VodPlayerInfoActivity.this.bimage = s.imagebit;
                                if (VodPlayerInfoActivity.this.bimage == null) {
                                    if (s.image == null || !s.image.startsWith("http://")) {
                                        url = VODplayer.gete() + "/images/vodpic/" + s.image;
                                    } else {
                                        url = s.image;
                                    }
                                    if (s.imagebit != null) {
                                        VodPlayerInfoActivity.this.bimage = s.imagebit;
                                    } else {
                                        VodPlayerInfoActivity.this.bimage = MGplayer.getHttpBitmap(url);
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        VodPlayerInfoActivity.this.rHandler.sendMessageDelayed(msg, 500);
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
                            VodPlayerInfoActivity.this.openVideoActivity(VodPlayerInfoActivity.this.info_id, VodPlayerInfoActivity.this.info_type, num);
                        }
                    }
                });
                this.button0.setOnFocusChangeListener(new C06094());
                this.button0.setFocusable(true);
                this.button0.setFocusableInTouchMode(true);
                this.button0.requestFocus();
                this.button0.requestFocusFromTouch();
                this.button1 = (Button) findViewById(C0216R.id.button1);
                this.button1.setTextSize(this.rate * 10.0f);
                this.button1.setTypeface(this.typeFace);
                this.button1.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        VodPlayerInfoActivity.this.selectionsview.showSelectionsView(s, VodPlayerInfoActivity.this.info_collect);
                    }
                });
                this.button1.setOnFocusChangeListener(new C06116());
                this.button2 = (Button) findViewById(C0216R.id.button2);
                this.button2.setTextSize(this.rate * 10.0f);
                this.button2.setTypeface(this.typeFace);
                this.button2.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (VodPlayerInfoActivity.this.collectvoddber.insert(s, VodPlayerInfoActivity.this.bimage, Integer.parseInt(VodPlayerInfoActivity.this.info_type)) == 0) {
                            Toast.makeText(VodPlayerInfoActivity.this, VodPlayerInfoActivity.this.getString(C0216R.string.collect_text2).toString(), 0).show();
                        } else {
                            Toast.makeText(VodPlayerInfoActivity.this, VodPlayerInfoActivity.this.getString(C0216R.string.collect_text1).toString(), 0).show();
                        }
                    }
                });
                this.button2.setOnFocusChangeListener(new C06138());
                this.button3 = (Button) findViewById(C0216R.id.button3);
                this.button3.setTextSize(this.rate * 10.0f);
                this.button3.setTypeface(this.typeFace);
                this.button3.setOnClickListener(new C06149());
                this.button3.setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            VodPlayerInfoActivity.this.button3.setBackgroundResource(C0216R.mipmap.bf);
                        } else {
                            VodPlayerInfoActivity.this.button3.setBackgroundResource(C0216R.mipmap.bof);
                        }
                    }
                });
            }
        }
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
                        v = v + s.areas[Integer.parseInt(areas[ii]) - 1];
                    }
                }
                ii++;
            }
        }
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
                        v = v + s.types[Integer.parseInt(types[ii]) - 1];
                        if (ii < types.length - 1) {
                            v = v + ",";
                        }
                    }
                }
                ii++;
            }
        }
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
                        if (Integer.parseInt(years[ii]) - 1 < 0 || Integer.parseInt(years[ii]) - 1 >= s.years.length) {
                            v = v + years[ii];
                        } else {
                            v = v + s.years[Integer.parseInt(years[ii]) - 1];
                        }
                    }
                }
                ii++;
            }
        }
        return v;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 4:
                    if (this.selectionsview.isShown()) {
                        this.selectionsview.hideSelectionsView();
                        SelectButton(true, this.button_index);
                        return true;
                    }
                    break;
                case 21:
                    if (!this.selectionsview.isShown()) {
                        SelectButton(false, -1);
                        return true;
                    }
                    break;
                case 22:
                    if (!this.selectionsview.isShown()) {
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
        Intent intent = new Intent(this, VodPlayerVideoActivity.class);
        intent.putExtra(TtmlNode.ATTR_ID, id);
        intent.putExtra("type", type);
        intent.putExtra("num", num);
        intent.putExtra("collect", this.info_collect);
        startActivity(intent);
    }
}
