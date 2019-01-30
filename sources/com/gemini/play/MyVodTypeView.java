package com.gemini.play;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
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
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodTypeView extends LinearLayout {
    private Context _this = null;
    private Handler channelHandler = new C05391();
    private ListViewInterface iface = null;
    private ListView listview = null;

    /* renamed from: com.gemini.play.MyVodTypeView$1 */
    class C05391 extends Handler {
        C05391() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyVodTypeView.this.iface.callback(msg.getData().getInt("cmd"), msg.getData().getString("data"));
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView$2 */
    class C05402 implements ViewBinder {
        C05402() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView$3 */
    class C05413 implements OnFocusChangeListener {
        C05413() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                MGplayer.MyPrintln("setOnFocusChangeListener = false");
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView$5 */
    class C05435 implements OnItemSelectedListener {
        C05435() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public MyVodTypeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtypeview, this, true);
        init();
    }

    public MyVodTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtypeview, this, true);
        init();
    }

    public MyVodTypeView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtypeview, this, true);
        init();
    }

    private void selectItem(int cmd, String value) {
        this.iface.callback(cmd, value);
    }

    private void init() {
        this.listview = (ListView) findViewById(C0216R.id.listview);
        this.listview.setSelector(getResources().getDrawable(C0216R.drawable.select4));
        final ListView listview = (ListView) findViewById(C0216R.id.listview);
        ArrayList<HashMap<String, Object>> list = new ArrayList();
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemId", "0");
        map0.put("ItemView", Integer.valueOf(C0216R.mipmap.type0));
        if (VODplayer.columner.length < 1 || VODplayer.columner[0].name == null) {
            map0.put("ItemTitle", this._this.getString(C0216R.string.vodlist_text2).toString());
        } else {
            map0.put("ItemTitle", VODplayer.columner[0].name);
        }
        list.add(map0);
        HashMap<String, Object> map1 = new HashMap();
        map1.put("ItemId", "1");
        map1.put("ItemView", Integer.valueOf(C0216R.mipmap.type1));
        if (VODplayer.columner.length < 2 || VODplayer.columner[1].name == null) {
            map1.put("ItemTitle", this._this.getString(C0216R.string.vodlist_text3).toString());
        } else {
            map1.put("ItemTitle", VODplayer.columner[1].name);
        }
        list.add(map1);
        HashMap<String, Object> map2 = new HashMap();
        map2.put("ItemId", "2");
        map2.put("ItemView", Integer.valueOf(C0216R.mipmap.type2));
        if (VODplayer.columner.length < 3 || VODplayer.columner[2].name == null) {
            map2.put("ItemTitle", this._this.getString(C0216R.string.vodlist_text4).toString());
        } else {
            map2.put("ItemTitle", VODplayer.columner[2].name);
        }
        list.add(map2);
        HashMap<String, Object> map3 = new HashMap();
        map3.put("ItemId", "3");
        map3.put("ItemView", Integer.valueOf(C0216R.mipmap.type3));
        if (VODplayer.columner.length < 4 || VODplayer.columner[3].name == null) {
            map3.put("ItemTitle", this._this.getString(C0216R.string.vodlist_text5).toString());
        } else {
            map3.put("ItemTitle", VODplayer.columner[3].name);
        }
        list.add(map3);
        HashMap<String, Object> map4 = new HashMap();
        map4.put("ItemId", "4");
        map4.put("ItemView", Integer.valueOf(C0216R.mipmap.type4));
        map4.put("ItemTitle", this._this.getString(C0216R.string.vodlist_text6).toString());
        list.add(map4);
        HashMap<String, Object> map5 = new HashMap();
        map5.put("ItemId", "5");
        map5.put("ItemView", Integer.valueOf(C0216R.mipmap.type5));
        map5.put("ItemTitle", this._this.getString(C0216R.string.vodlist_text8).toString());
        list.add(map5);
        HashMap<String, Object> map6 = new HashMap();
        map6.put("ItemId", "6");
        map6.put("ItemView", Integer.valueOf(C0216R.mipmap.back));
        map6.put("ItemTitle", this._this.getString(C0216R.string.vodlist_text9).toString());
        list.add(map6);
        MySimpleAdapterVodTypeView adapter = new MySimpleAdapterVodTypeView(this._this, list, C0216R.layout.vodtypeitem, new String[]{"ItemView", "ItemTitle"}, new int[]{C0216R.id.ItemView, C0216R.id.ItemTitle});
        listview.setAdapter(adapter);
        adapter.setViewBinder(new C05402());
        listview.setSelector(getResources().getDrawable(C0216R.drawable.select4));
        listview.setOnFocusChangeListener(new C05413());
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                String id = (String) ((HashMap) listview.getItemAtPosition(arg2)).get("ItemId");
                MGplayer.MyPrintln("ItemId = " + id);
                MyVodTypeView.this.listFocus();
                if (id.equals("0") || id.equals("1") || id.equals("2") || id.equals("3")) {
                    if (VODplayer.columner.length > 0) {
                        int index = Integer.parseInt(id);
                        if (index < VODplayer.columner.length && VODplayer.columner[Integer.parseInt(id)].needps == 1) {
                            MyVodTypeView.this.inputPasswordView(id);
                        }
                        if (index < VODplayer.columner.length && VODplayer.columner[Integer.parseInt(id)].needps == 0) {
                            MyVodTypeView.this.selectItem(0, id);
                            if (MGplayer.isNumeric(VODplayer.type) && Integer.parseInt(VODplayer.type) >= 0) {
                                MGplayer.MyPrintln("VODplayer.type:" + VODplayer.type);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    MyVodTypeView.this.selectItem(0, id);
                    if (MGplayer.isNumeric(VODplayer.type) && Integer.parseInt(VODplayer.type) >= 0) {
                        MGplayer.MyPrintln("VODplayer.type:" + VODplayer.type);
                    }
                } else if (id.equals("4")) {
                    MyVodTypeView.this.iface.callback(1, id);
                } else if (id.equals("5")) {
                    MyVodTypeView.this.iface.callback(2, id);
                } else if (id.equals("6")) {
                    MyVodTypeView.this.iface.callback(3, id);
                }
            }
        });
        listview.setOnItemSelectedListener(new C05435());
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
                String type_ps = MGplayer.MyGetSharedPreferences(MyVodTypeView.this._this, "data", 0).getString("type_password", null);
                if (type_ps == null) {
                    type_ps = VODplayer.columner[Integer.parseInt(id)].password;
                }
                if (password.equals(type_ps)) {
                    VODplayer.columner[Integer.parseInt(id)].needps = 0;
                    MyVodTypeView.this.iface.callback(0, id);
                    return;
                }
                MyToast.makeText(MyVodTypeView.this._this, MyVodTypeView.this._this.getString(C0216R.string.typelist_text3).toString(), 0);
            }
        });
        builder.setNegativeButton(this._this.getString(C0216R.string.cancel).toString(), null);
        builder.create().show();
    }

    public void listFocus() {
        this.listview.setFocusable(true);
        this.listview.setFocusableInTouchMode(true);
        this.listview.requestFocus();
        this.listview.requestFocusFromTouch();
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }
}
