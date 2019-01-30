package com.gemini.play;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyDialog.Builder;

public class BackPlayerActivity extends Activity {
    private MyBackListView listview = null;
    public ControlVideoInterface onControlVideo = new C08984();
    public ListViewInterface onListPressed = new C08995();
    public ListViewInterface onPreviewPressed = new C09006();
    public ListViewInterface onTypePressed = new C08973();
    Handler pHandler = new C02577();
    private MyBackPreviewView previewview = null;
    private ImageButton typebutton = null;
    private MyBackTypeView typeview = null;

    /* renamed from: com.gemini.play.BackPlayerActivity$1 */
    class C02551 implements OnClickListener {
        C02551() {
        }

        public void onClick(View v) {
            BackPlayerActivity.this.typeview.showTypeList();
        }
    }

    /* renamed from: com.gemini.play.BackPlayerActivity$2 */
    class C02562 implements OnClickListener {
        C02562() {
        }

        public void onClick(View v) {
            BackPlayerActivity.this.exitActivity();
        }
    }

    /* renamed from: com.gemini.play.BackPlayerActivity$7 */
    class C02577 extends Handler {
        C02577() {
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
        }
    }

    /* renamed from: com.gemini.play.BackPlayerActivity$8 */
    class C02588 implements DialogInterface.OnClickListener {
        C02588() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            BackPlayerActivity.this.finish();
        }
    }

    /* renamed from: com.gemini.play.BackPlayerActivity$9 */
    class C02599 implements DialogInterface.OnClickListener {
        C02599() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    /* renamed from: com.gemini.play.BackPlayerActivity$3 */
    class C08973 implements ListViewInterface {
        C08973() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    BackPlayerActivity.this.listview.showListView(data);
                    BackPlayerActivity.this.typeview.hideTypeList();
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.BackPlayerActivity$4 */
    class C08984 implements ControlVideoInterface {
        C08984() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MGplayer.MyPrintln("onControlVideo:" + data);
                    Intent intent = new Intent();
                    intent.setClass(BackPlayerActivity.this, ControlPlayerActivity.class);
                    intent.putExtra("vod_url", data);
                    BackPlayerActivity.this.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.BackPlayerActivity$5 */
    class C08995 implements ListViewInterface {
        C08995() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    BACKplayer.currentID = data;
                    BACKplayer.playVideoFull(BackPlayerActivity.this, null);
                    break;
                case 2:
                    break;
                case 3:
                    BackPlayerActivity.this.previewview.listFocus();
                    MGplayer.MyPrintln("onPreviewPressed listFocus");
                    return;
                default:
                    return;
            }
            MGplayer.MyPrintln("onPreviewPressed onListPressed cmd = " + cmd);
            if (MGplayer.isNumeric(data)) {
                String preview = BACKplayer.playbackGetVideoIntroduction(Integer.parseInt(data));
                BACKplayer.currentID = data;
                BackPlayerActivity.this.previewview.showPreviewList(preview);
                MGplayer.MyPrintln("onPreviewPressed preview = " + preview);
                Message msg = new Message();
                Bundle d = new Bundle();
                d.putString("data", data);
                msg.setData(d);
                msg.what = 0;
                if (BackPlayerActivity.this.pHandler.hasMessages(0)) {
                    BackPlayerActivity.this.pHandler.removeMessages(0);
                }
                BackPlayerActivity.this.pHandler.sendMessageDelayed(msg, 2000);
            }
        }
    }

    /* renamed from: com.gemini.play.BackPlayerActivity$6 */
    class C09006 implements ListViewInterface {
        C09006() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    BACKplayer.playVideoFull(BackPlayerActivity.this, data);
                    return;
                case 3:
                    BackPlayerActivity.this.listview.listFocus();
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.backplayer);
        getWindow().setFlags(1024, 1024);
        MGplayer.mediaplayervodheader(0);
        this.previewview = (MyBackPreviewView) findViewById(C0216R.id.mybackpreviewview);
        this.previewview.setInterface(this.onPreviewPressed);
        this.previewview.listNoFocus();
        this.listview = (MyBackListView) findViewById(C0216R.id.mybacklistview);
        this.listview.setInterface(this.onListPressed);
        this.listview.listFocus();
        this.typeview = (MyBackTypeView) findViewById(C0216R.id.mytypeview);
        this.typeview.setInterface(this.onTypePressed);
        MGplayer.video_every_interface(this.onControlVideo);
        this.typebutton = (ImageButton) findViewById(C0216R.id.typebutton);
        this.typebutton.setOnClickListener(new C02551());
        Typeface typeFace = MGplayer.getFontsType(this);
        Button backbutton = (Button) findViewById(C0216R.id.backbutton);
        backbutton.setTextSize(8.0f * MGplayer.getFontsRate());
        backbutton.setTypeface(typeFace);
        backbutton.setOnClickListener(new C02562());
        ScrollTextView scroller = (ScrollTextView) findViewById(C0216R.id.scrolltext);
        scroller.init(getWindowManager());
        if (MGplayer.scrolltext != null) {
            MGplayer.MyPrintln("start scrolltext");
            scroller.setText("");
            scroller.start(this, MGplayer.scrolltext, 0, 0, 0, 0, 2.5f, (int) (12.0f * MGplayer.getFontsRate()), "FFFFFF");
        }
    }

    protected void onDestroy() {
        MGplayer.MyPrintln("backplayer onDestroy");
        super.onDestroy();
    }

    protected void onRestart() {
        MGplayer.MyPrintln("backplayer onRestart");
        super.onRestart();
    }

    protected void onResume() {
        MGplayer.MyPrintln("backplayer onResume");
        MGplayer.video_every_interface(this.onControlVideo);
        super.onResume();
    }

    protected void onPause() {
        MGplayer.MyPrintln("backplayer onPause");
        super.onPause();
    }

    private boolean hideAllView() {
        int v = 0;
        if (this.typeview.isShown()) {
            this.typeview.hideTypeList();
            v = 1;
        }
        if (v > 0) {
            return true;
        }
        return false;
    }

    public void exitActivity() {
        Builder builder = new Builder(this);
        builder.setMessage(getString(C0216R.string.myhomebar_text6).toString());
        builder.setPositiveButton(getString(C0216R.string.ok).toString(), new C02588());
        builder.setNegativeButton(getString(C0216R.string.cancel).toString(), new C02599());
        builder.create().show();
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MGplayer.MyPrintln("backplayer onKeyDown");
        switch (event.getKeyCode()) {
            case 4:
                if (!hideAllView()) {
                    exitActivity();
                    break;
                }
                return true;
            case 21:
                this.typeview.showTypeList();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 82:
                MenuView.gridMenuInit(this);
                MenuView.showAlertDialog(this);
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void hideListAll() {
        this.previewview.setVisibility(8);
        this.listview.setVisibility(8);
    }

    public void showListAll() {
        this.previewview.setVisibility(0);
        this.listview.setVisibility(0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case -1:
                this.previewview.listNoFocus();
                MGplayer.MyPrintln("onActivityResult");
                return;
            default:
                return;
        }
    }
}
