package com.gemini.play;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.gemini.kvod2.C0216R;
import com.gemini.play.MyPickerView.onSelectListener;
import java.util.ArrayList;
import java.util.HashMap;

public class MyTypeView2 extends LinearLayout {
    private Context _this;
    private ListViewInterface iface = null;
    private MyPickerView listview;
    public ListViewInterface onTypePressed = new C09122();
    public Handler rHandler = new C04643();
    private String selectid = null;

    /* renamed from: com.gemini.play.MyTypeView2$3 */
    class C04643 extends Handler {
        C04643() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyTypeView2.this.hideTypeList();
                    return;
                case 1:
                    MyTypeView2.this.inputPasswordView(msg.getData().getString("data"));
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.MyTypeView2$1 */
    class C09111 implements onSelectListener {
        C09111() {
        }

        public void onSelect(String text) {
            MyTypeView2.this.selectid = text;
        }
    }

    /* renamed from: com.gemini.play.MyTypeView2$2 */
    class C09122 implements ListViewInterface {
        C09122() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    if (LIVEplayer.typeNeedpsGet(data).equals("0") || LIVEplayer.typePasswordOK) {
                        MyTypeView2.this.iface.callback(0, data);
                        return;
                    } else {
                        MyTypeView2.this.sendMessage(1, data);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public MyTypeView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.typeview2, this, true);
        init();
    }

    public MyTypeView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.typeview2, this, true);
        init();
    }

    public MyTypeView2(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.typeview2, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = Typeface.createFromAsset(this._this.getAssets(), "fonts/Roboto-Bold.ttf");
        float rate = MGplayer.getFontsRate();
        this.listview = (MyPickerView) findViewById(C0216R.id.listview);
        this.listview.setFontSize((int) (8.0f * rate));
        this.listview.setTypeface(typeFace);
        this.listview.setInterface(this.onTypePressed);
    }

    public void set_list() {
        ArrayList<HashMap<String, Object>> list = new ArrayList();
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemID", "0");
        map0.put("ItemName", this._this.getString(C0216R.string.typelist_text1).toString());
        list.add(map0);
        for (int i = 0; i < LIVEplayer.typeSize(); i++) {
            HashMap<String, Object> map = new HashMap();
            map.put("ItemID", LIVEplayer.typeIdGet(i));
            map.put("ItemName", LIVEplayer.typeNameGet(i));
            list.add(map);
        }
        this.listview.setData(list);
        this.listview.setOnSelectListener(new C09111());
        this.listview.getFocus();
        this.listview.setSelected(0);
    }

    public void showTypeList() {
        if (!isShown()) {
            set_list();
            setVisibility(0);
            setFocusable(true);
            listFocus();
        }
    }

    public void hideTypeList() {
        if (isShown()) {
            setFocusable(true);
            setVisibility(8);
            this.iface.callback(1, null);
        }
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public void listFocus() {
        this.listview.setFocusable(true);
        this.listview.setFocusableInTouchMode(true);
        this.listview.requestFocus();
        this.listview.requestFocusFromTouch();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 23:
            case 66:
                MGplayer.MyPrintln("selectid = " + this.selectid);
                this.iface.callback(0, this.selectid);
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    private void sendMessage(int what, String d) {
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("data", d);
        msg.setData(data);
        msg.what = what;
        this.rHandler.sendMessage(msg);
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
                String type_ps = MGplayer.MyGetSharedPreferences(MyTypeView2.this._this, "data", 0).getString("type_password", null);
                if (type_ps == null) {
                    type_ps = MGplayer.type2password;
                }
                if (password.equals(type_ps)) {
                    LIVEplayer.typePasswordOK = true;
                    MyTypeView2.this.iface.callback(0, id);
                    return;
                }
                MyToast.makeText(MyTypeView2.this._this, MyTypeView2.this._this.getString(C0216R.string.typelist_text3).toString(), 0);
                LIVEplayer.typePasswordOK = false;
            }
        });
        builder.setNegativeButton(this._this.getString(C0216R.string.cancel).toString(), null);
        builder.create().show();
    }
}
