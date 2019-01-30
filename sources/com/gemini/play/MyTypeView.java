package com.gemini.play;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import com.google.android.exoplayer.hls.HlsChunkSource;
import java.util.ArrayList;
import java.util.HashMap;

public class MyTypeView extends LinearLayout {
    private Context _this;
    private EditText edittext = null;
    private Button find_button = null;
    private boolean forcebutton = false;
    private int forcelist = 0;
    private int gridindex = 0;
    private ListViewInterface iface = null;
    private MySimpleAdapterInputView inputadapter = null;
    private GridView inputgrid = null;
    private LinearLayout inputlayout = null;
    private ArrayList<HashMap<String, Object>> inputlist = new ArrayList();
    private boolean isShow = false;
    private ListView listview = null;
    private boolean passwordOK = false;
    public Handler rHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyTypeView.this.hideTypeList();
                    return;
                case 1:
                    MyTypeView.this.inputPasswordView(msg.getData().getString("data"));
                    return;
                default:
                    return;
            }
        }
    };
    /* renamed from: t */
    String f14t = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    /* renamed from: com.gemini.play.MyTypeView$1 */
    class C04551 implements OnFocusChangeListener {

        /* renamed from: com.gemini.play.MyTypeView$1$1 */
        class C04541 implements Runnable {
            C04541() {
            }

            public void run() {
                MyTypeView.this.listFocus();
            }
        }

        C04551() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            MGplayer.MyPrintln("hasFocus:" + hasFocus);
            if (!hasFocus && MyTypeView.this.isShown()) {
                new Handler().postDelayed(new C04541(), 500);
            }
        }
    }

    /* renamed from: com.gemini.play.MyTypeView$2 */
    class C04562 implements OnItemClickListener {
        C04562() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            String id = (String) ((HashMap) MyTypeView.this.listview.getItemAtPosition(arg2)).get("ItemID");
            String n = LIVEplayer.typeNeedpsGet(id);
            if (id != null && id.equals("2")) {
                MyTypeView.this.gridinput_show(true);
            } else if (n.equals("0") || LIVEplayer.typePasswordOK) {
                MyTypeView.this.iface.callback(0, id);
                MyTypeView.this.gridinput_show(false);
            } else {
                MyTypeView.this.gridinput_show(false);
                if (LIVEplayer.show_ps_playlist) {
                    MyTypeView.this.iface.callback(0, id);
                } else {
                    MyTypeView.this.sendMessage(1, id);
                }
            }
        }
    }

    /* renamed from: com.gemini.play.MyTypeView$3 */
    class C04573 implements OnItemSelectedListener {
        C04573() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyTypeView.this.listFocus();
            MyTypeView.this.showViewTimeout();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            MyTypeView.this.listFocus();
        }
    }

    /* renamed from: com.gemini.play.MyTypeView$4 */
    class C04584 implements OnClickListener {
        C04584() {
        }

        public void onClick(View v) {
            MyTypeView.this.iface.callback(2, MyTypeView.this.edittext.getText().toString());
        }
    }

    /* renamed from: com.gemini.play.MyTypeView$5 */
    class C04595 implements OnItemClickListener {
        C04595() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyTypeView.this.grid_enter(((HashMap) MyTypeView.this.inputgrid.getItemAtPosition(arg2)).get("ItemChar").toString(), arg2);
        }
    }

    /* renamed from: com.gemini.play.MyTypeView$6 */
    class C04606 implements OnItemSelectedListener {
        C04606() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (MyTypeView.this.inputadapter != null) {
                MyTypeView.this.inputadapter.setSeclection(arg2);
                MyTypeView.this.inputadapter.notifyDataSetChanged();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyTypeView$7 */
    class C04617 implements ViewBinder {
        C04617() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyTypeView$8 */
    class C04628 implements ViewBinder {
        C04628() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyTypeView$9 */
    class C04639 implements Runnable {
        C04639() {
        }

        public void run() {
            MyTypeView.this.setVisibility(8);
            MyTypeView.this.iface.callback(1, null);
        }
    }

    public MyTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.typeview, this, true);
        init();
    }

    public MyTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.typeview, this, true);
        init();
    }

    public MyTypeView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.typeview, this, true);
        init();
    }

    private void init() {
        Typeface typeFace = MGplayer.getFontsType(this._this);
        float rate = MGplayer.getFontsRate();
        this.listview = (ListView) findViewById(C0216R.id.listview);
        this.listview.setSelector(getResources().getDrawable(C0216R.mipmap.se));
        TextView listtiew = (TextView) findViewById(C0216R.id.find_text);
        listtiew.setTextSize(8.0f * rate);
        listtiew.setTypeface(typeFace);
        this.edittext = (EditText) findViewById(C0216R.id.find_edit);
        this.edittext.setTextSize(8.0f * rate);
        this.edittext.setTypeface(typeFace);
        this.listview.setOnFocusChangeListener(new C04551());
        this.listview.setOnItemClickListener(new C04562());
        this.listview.setOnItemSelectedListener(new C04573());
        this.inputgrid = (GridView) findViewById(C0216R.id.live_type_inputgrid);
        this.inputlayout = (LinearLayout) findViewById(C0216R.id.live_type_linearLayout);
        this.find_button = (Button) findViewById(C0216R.id.find_button);
        this.find_button.setTextSize(8.0f * rate);
        this.find_button.setTypeface(typeFace);
        this.find_button.setOnClickListener(new C04584());
        this.inputgrid.setOnItemClickListener(new C04595());
        this.inputgrid.setOnItemSelectedListener(new C04606());
        this.inputgrid.setSelector(17170445);
        init_input();
        set_inputlist();
    }

    public void set_list() {
        ArrayList<HashMap<String, Object>> list = new ArrayList();
        HashMap<String, Object> map2 = new HashMap();
        map2.put("ItemID", String.valueOf(2));
        map2.put("ItemIcon", Integer.valueOf(C0216R.mipmap.find));
        map2.put("ItemName", this._this.getString(C0216R.string.typelist_text5).toString());
        list.add(map2);
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemID", null);
        map0.put("ItemIcon", Integer.valueOf(C0216R.mipmap.ti));
        map0.put("ItemName", this._this.getString(C0216R.string.typelist_text1).toString());
        list.add(map0);
        HashMap<String, Object> map1 = new HashMap();
        map1.put("ItemID", String.valueOf(1));
        map1.put("ItemIcon", Integer.valueOf(C0216R.mipmap.lclist));
        map1.put("ItemName", this._this.getString(C0216R.string.typelist_text4).toString());
        list.add(map1);
        HashMap<String, Object> map3 = new HashMap();
        map3.put("ItemID", String.valueOf(3));
        map3.put("ItemIcon", Integer.valueOf(C0216R.mipmap.type));
        map3.put("ItemName", this._this.getString(C0216R.string.typelist_text6).toString());
        list.add(map3);
        for (int i = 0; i < LIVEplayer.typeSize(); i++) {
            HashMap<String, Object> map = new HashMap();
            map.put("ItemID", LIVEplayer.typeIdGet(i));
            map.put("ItemName", LIVEplayer.typeNameGet(i));
            list.add(map);
        }
        MySimpleAdapterTypeListView adapter = new MySimpleAdapterTypeListView(this._this, list, C0216R.layout.typeitem, new String[]{"ItemIcon", "ItemName"}, new int[]{C0216R.id.ItemIcon, C0216R.id.ItemName});
        this.listview.setAdapter(adapter);
        adapter.setViewBinder(new C04617());
    }

    private void init_input() {
        this.inputadapter = new MySimpleAdapterInputView(this._this, this.inputlist, C0216R.layout.inputitem, new String[]{"ItemChar"}, new int[]{C0216R.id.ItemChar});
        this.inputgrid.setAdapter(this.inputadapter);
        this.inputadapter.setViewBinder(new C04628());
    }

    private void set_inputlist() {
        this.inputlist.clear();
        for (int ii = 0; ii < this.f14t.length(); ii++) {
            HashMap<String, Object> map = new HashMap();
            map.put("ItemChar", Character.valueOf(this.f14t.charAt(ii)));
            this.inputlist.add(map);
        }
        HashMap<String, Object> map2 = new HashMap();
        map2.put("ItemChar", "CE");
        this.inputlist.add(map2);
        this.inputadapter.notifyDataSetChanged();
    }

    public void showTypeList() {
        showViewTimeout();
        listFocus();
        if (!isShown()) {
            gridinput_show(false);
            this.isShow = true;
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
            this.isShow = false;
            setFocusable(true);
            new Handler().postDelayed(new C04639(), 500);
        }
    }

    public boolean isShown() {
        return this.isShow;
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

    public void gridFocus() {
        this.inputgrid.setFocusable(true);
        this.inputgrid.setFocusableInTouchMode(true);
        this.inputgrid.requestFocus();
        this.inputgrid.requestFocusFromTouch();
    }

    public void gridNoFocus() {
        this.inputgrid.setFocusable(false);
        this.inputgrid.setFocusableInTouchMode(false);
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

    public void find_button_force() {
        this.find_button.setFocusable(true);
        this.find_button.setFocusableInTouchMode(true);
        this.find_button.requestFocus();
        this.find_button.requestFocusFromTouch();
    }

    public void gridinput_show(boolean show) {
        LinearLayout inputlayout = (LinearLayout) findViewById(C0216R.id.live_type_linearLayout);
        LinearLayout layout = (LinearLayout) findViewById(C0216R.id.layout);
        LayoutParams layoutParams;
        if (show) {
            inputlayout.setVisibility(0);
            layoutParams = (LayoutParams) layout.getLayoutParams();
            layoutParams.width = (int) (((double) MGplayer.screenWidth) * 0.8d);
            layout.setLayoutParams(layoutParams);
            this.forcelist = 1;
        } else {
            inputlayout.setVisibility(8);
            layoutParams = (LayoutParams) layout.getLayoutParams();
            layoutParams.width = (int) (((double) MGplayer.screenWidth) * 0.3d);
            layout.setLayoutParams(layoutParams);
            this.forcelist = 0;
        }
        showViewTimeout();
    }

    public void grid_enter(String t0, int arg2) {
        if (t0.equals("CE")) {
            this.edittext.setText("");
            return;
        }
        this.edittext.setText(this.edittext.getText().toString() + t0);
        if (this.inputadapter != null) {
            this.inputadapter.setSeclection(arg2);
            this.inputadapter.notifyDataSetChanged();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        MGplayer.MyPrintln("typelist dispatchKeyEvent");
        showViewTimeout();
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                    if (this.forcelist == 1) {
                        if (this.gridindex < 5) {
                            this.forcebutton = true;
                            find_button_force();
                            this.find_button.setBackgroundResource(C0216R.mipmap.bf);
                            return true;
                        }
                        this.gridindex -= 5;
                        if (this.gridindex < 0) {
                            this.gridindex = 0;
                        }
                        this.inputgrid.setSelection(this.gridindex);
                        return true;
                    }
                    break;
                case 20:
                    if (this.forcelist == 1) {
                        if (this.forcebutton) {
                            this.forcebutton = false;
                            gridFocus();
                            this.gridindex = 0;
                            this.inputgrid.setSelection(this.gridindex);
                            this.find_button.setBackgroundResource(C0216R.mipmap.bof);
                            break;
                        }
                        this.gridindex += 5;
                        if (this.gridindex > 36) {
                            this.gridindex = 36;
                        }
                        this.inputgrid.setSelection(this.gridindex);
                        return true;
                    }
                    break;
                case 21:
                    if (this.gridindex % 5 == 0 && this.forcelist == 1) {
                        this.gridindex = -1;
                        this.forcelist = 0;
                        listFocus();
                        gridinput_show(false);
                        return true;
                    } else if (this.gridindex > 0) {
                        this.gridindex--;
                        this.inputgrid.setSelection(this.gridindex);
                        return true;
                    }
                    break;
                case 22:
                    if (!LIVEplayer.show_type_find) {
                        hideTypeList();
                        break;
                    } else if (this.forcelist == 1) {
                        this.gridindex++;
                        this.inputgrid.setSelection(this.gridindex);
                        return true;
                    }
                    break;
                case 23:
                case 66:
                    if (this.forcebutton) {
                        this.iface.callback(2, this.edittext.getText().toString());
                        this.forcelist = 0;
                        this.forcebutton = false;
                        return true;
                    } else if (this.forcelist == 1) {
                        if (this.gridindex == 36) {
                            grid_enter("CE", this.gridindex);
                            return true;
                        } else if (this.gridindex >= 36 || this.gridindex < 0) {
                            return true;
                        } else {
                            grid_enter(String.valueOf(this.f14t.charAt(this.gridindex)), this.gridindex);
                            return true;
                        }
                    }
                    break;
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
        builder.setPositiveButton(this._this.getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String password = ((EditText) textEntryView.findViewById(C0216R.id.ps_editText)).getText().toString();
                String type_ps = MGplayer.MyGetSharedPreferences(MyTypeView.this._this, "data", 0).getString("type_password", null);
                if (type_ps == null) {
                    type_ps = MGplayer.type2password;
                }
                if (password.equals(type_ps)) {
                    LIVEplayer.typePasswordOK = true;
                    MyTypeView.this.iface.callback(0, id);
                    return;
                }
                MyToast.makeText(MyTypeView.this._this, MyTypeView.this._this.getString(C0216R.string.typelist_text3).toString(), 0);
                LIVEplayer.typePasswordOK = false;
            }
        });
        builder.setNegativeButton(this._this.getString(C0216R.string.cancel).toString(), null);
        builder.create().show();
    }
}
