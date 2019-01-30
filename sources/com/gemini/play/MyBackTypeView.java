package com.gemini.play;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.util.ArrayList;
import java.util.HashMap;

public class MyBackTypeView extends LinearLayout {
    private Context _this;
    private ListViewInterface iface = null;
    private ListView listview = null;
    private boolean passwordOK = false;
    public Handler rHandler = new C03685();

    /* renamed from: com.gemini.play.MyBackTypeView$1 */
    class C03641 implements OnFocusChangeListener {

        /* renamed from: com.gemini.play.MyBackTypeView$1$1 */
        class C03631 implements Runnable {
            C03631() {
            }

            public void run() {
                MyBackTypeView.this.listFocus();
            }
        }

        C03641() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            MGplayer.MyPrintln("hasFocus:" + hasFocus);
            if (!hasFocus && MyBackTypeView.this.isShown()) {
                new Handler().postDelayed(new C03631(), 500);
            }
        }
    }

    /* renamed from: com.gemini.play.MyBackTypeView$2 */
    class C03652 implements OnItemClickListener {
        C03652() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            String id = (String) ((HashMap) MyBackTypeView.this.listview.getItemAtPosition(arg2)).get("ItemID");
            if (BACKplayer.playbackTypeNeedpsGet(id).equals("0") || BACKplayer.typePasswordOK) {
                MyBackTypeView.this.iface.callback(0, id);
            } else {
                MyBackTypeView.this.sendMessage(1, id);
            }
        }
    }

    /* renamed from: com.gemini.play.MyBackTypeView$3 */
    class C03663 implements OnItemSelectedListener {
        C03663() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyBackTypeView.this.listFocus();
            MyBackTypeView.this.showViewTimeout();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            MyBackTypeView.this.listFocus();
        }
    }

    /* renamed from: com.gemini.play.MyBackTypeView$4 */
    class C03674 implements ViewBinder {
        C03674() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyBackTypeView$5 */
    class C03685 extends Handler {
        C03685() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyBackTypeView.this.hideTypeList();
                    return;
                case 1:
                    MyBackTypeView.this.inputPasswordView(msg.getData().getString("data"));
                    return;
                default:
                    return;
            }
        }
    }

    public MyBackTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backtypeview, this, true);
        init();
    }

    public MyBackTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backtypeview, this, true);
        init();
    }

    public MyBackTypeView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.backtypeview, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        float rate = MGplayer.getFontsRate();
        this.listview = (ListView) findViewById(C0216R.id.listview);
        this.listview.setSelector(ContextCompat.getDrawable(this._this, C0216R.drawable.gradient9));
        this.listview.setOnFocusChangeListener(new C03641());
        this.listview.setOnItemClickListener(new C03652());
        this.listview.setOnItemSelectedListener(new C03663());
    }

    public void set_list() {
        ArrayList<HashMap<String, Object>> list = new ArrayList();
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemID", null);
        map0.put("ItemIcon", Integer.valueOf(C0216R.mipmap.ti));
        map0.put("ItemName", this._this.getString(C0216R.string.typelist_text1).toString());
        list.add(map0);
        for (int i = 0; i < BACKplayer.playbackTypeSize(); i++) {
            HashMap<String, Object> map = new HashMap();
            map.put("ItemID", BACKplayer.playbackTypeIdGet(i));
            map.put("ItemIcon", Integer.valueOf(C0216R.mipmap.ti));
            map.put("ItemName", BACKplayer.playbackTypeNameGet(i));
            list.add(map);
        }
        MySimpleAdapterTypeListView adapter = new MySimpleAdapterTypeListView(this._this, list, C0216R.layout.typeitem, new String[]{"ItemIcon", "ItemName"}, new int[]{C0216R.id.ItemIcon, C0216R.id.ItemName});
        this.listview.setAdapter(adapter);
        adapter.setViewBinder(new C03674());
    }

    public void showTypeList() {
        showViewTimeout();
        listFocus();
        if (!isShown()) {
            set_list();
            setVisibility(0);
            setFocusable(true);
            listFocus();
        }
    }

    public void hideTypeList() {
        if (this.rHandler.hasMessages(0)) {
            this.rHandler.removeMessages(0);
        }
        listNoFocus();
        if (isShown()) {
            setFocusable(true);
            setVisibility(8);
        }
    }

    public void listFocus() {
        this.listview.setFocusable(true);
        this.listview.setFocusableInTouchMode(true);
        this.listview.requestFocus();
        this.listview.requestFocusFromTouch();
    }

    public void listNoFocus() {
        this.listview.setFocusable(false);
        this.listview.setFocusableInTouchMode(false);
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
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

    private void sendMessage(int what, String d) {
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("data", d);
        msg.setData(data);
        msg.what = what;
        this.rHandler.sendMessage(msg);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MGplayer.MyPrintln("TypeView onKeyDown");
        switch (keyCode) {
            case 19:
            case 20:
                boolean ret = super.onKeyDown(keyCode, event);
                listFocus();
                return true;
            case 21:
            case 22:
                hideTypeList();
                this.iface.callback(1, null);
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                case 20:
                    boolean ret = super.dispatchKeyEvent(event);
                    listFocus();
                    return true;
                case 21:
                case 22:
                    hideTypeList();
                    this.iface.callback(1, null);
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void inputPasswordView(final String id) {
        Builder builder = new Builder(this._this);
        final View textEntryView = LayoutInflater.from(this._this).inflate(C0216R.layout.inputpasswordview, null);
        builder.setIcon(17301659);
        builder.setTitle(this._this.getString(C0216R.string.typelist_text2).toString());
        builder.setView(textEntryView);
        builder.setPositiveButton(this._this.getString(C0216R.string.ok).toString(), new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String password = ((EditText) textEntryView.findViewById(C0216R.id.ps_editText)).getText().toString();
                String type_ps = MGplayer.MyGetSharedPreferences(MyBackTypeView.this._this, "data", 0).getString("type_password", null);
                if (type_ps == null) {
                    type_ps = BACKplayer.playbackTypePasswordGet(id);
                }
                if (password.equals(type_ps)) {
                    BACKplayer.typePasswordOK = true;
                    MyBackTypeView.this.iface.callback(0, id);
                    return;
                }
                MyToast.makeText(MyBackTypeView.this._this, MyBackTypeView.this._this.getString(C0216R.string.typelist_text3).toString(), 0);
                BACKplayer.typePasswordOK = false;
            }
        });
        builder.setNegativeButton(this._this.getString(C0216R.string.cancel).toString(), null);
        builder.create().show();
    }
}
