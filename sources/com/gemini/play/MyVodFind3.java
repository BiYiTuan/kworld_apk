package com.gemini.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodFind3 {
    private Activity _this;
    private MySimpleAdapterTypeListView2 adapter_area;
    private MySimpleAdapterTypeListView2 adapter_item;
    private MySimpleAdapterTypeListView2 adapter_type;
    private MySimpleAdapterTypeListView2 adapter_year;
    private Button button_find;
    private AlertDialog dialog;
    private ListViewInterface iface = null;
    private MySimpleAdapterVodFindView inputadapter = null;
    private GridView inputgrid = null;
    private ArrayList<HashMap<String, Object>> inputlist = new ArrayList();
    private int item_index = 0;
    ArrayList<HashMap<String, Object>> list_area = new ArrayList();
    ArrayList<HashMap<String, Object>> list_item = new ArrayList();
    ArrayList<HashMap<String, Object>> list_type = new ArrayList();
    ArrayList<HashMap<String, Object>> list_year = new ArrayList();
    private TextView listtext_area;
    private TextView listtext_item;
    private TextView listtext_type;
    private TextView listtext_year;
    private ListView listview_area;
    private ListView listview_item;
    private ListView listview_type;
    private ListView listview_year;
    private String select_area = "0";
    private String select_cmd = "&itype=0&iyear=0&iarea=0";
    private String select_item = "0";
    private String select_type = "0";
    private String select_year = "0";
    private int spinner_area_value = 0;
    private String spinner_find_value = null;
    private int spinner_sort_value = 0;
    private int spinner_type_value = 0;
    private int spinner_year_value = 0;
    private View vodFindActivity = null;
    private ArrayList<String> years_array = new ArrayList();

    /* renamed from: com.gemini.play.MyVodFind3$1 */
    class C04921 implements OnFocusChangeListener {
        C04921() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3$3 */
    class C04943 implements OnItemSelectedListener {
        C04943() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (MyVodFind3.this.inputadapter != null) {
                MyVodFind3.this.inputadapter.setSeclection(arg2);
                MyVodFind3.this.inputadapter.notifyDataSetChanged();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3$5 */
    class C04965 implements OnClickListener {
        C04965() {
        }

        public void onClick(View v) {
            MGplayer.MyPrintln("button find select_cmd:" + MyVodFind3.this.select_cmd);
            VODplayer.type = MyVodFind3.this.select_item;
            MyVodFind3.this.iface.callback(0, MyVodFind3.this.select_cmd);
            MyVodFind3.this.dialog.hide();
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3$6 */
    class C04976 implements OnFocusChangeListener {
        C04976() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                MyVodFind3.this.button_find.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                MyVodFind3.this.button_find.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3$7 */
    class C04987 implements OnItemClickListener {
        C04987() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodFind3.this.select_item = (String) ((HashMap) MyVodFind3.this.listview_item.getItemAtPosition(arg2)).get("ItemID");
            if (MGplayer.isNumeric(MyVodFind3.this.select_item)) {
                int item = Integer.parseInt(MyVodFind3.this.select_item);
                MyVodFind3.this.set_list_type(item, 1);
                MyVodFind3.this.set_list_area(item, 1);
                MyVodFind3.this.set_list_year(item, 1);
            }
            String select_year_text = "0";
            if (MGplayer.isNumeric(MyVodFind3.this.select_item) && MGplayer.isNumeric(MyVodFind3.this.select_year) && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year != null && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.length() > 1) {
                String[] type_names = VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.split("\\|");
                if (type_names != null && type_names.length > Integer.parseInt(MyVodFind3.this.select_year) && Integer.parseInt(MyVodFind3.this.select_year) - 1 >= 0) {
                    select_year_text = type_names[Integer.parseInt(MyVodFind3.this.select_year) - 1];
                }
            }
            MyVodFind3.this.select_cmd = "&item=" + MyVodFind3.this.select_item + "&itype=" + MyVodFind3.this.select_type + "&iyear=" + select_year_text + "&iarea=" + MyVodFind3.this.select_area;
        }
    }

    /* renamed from: com.gemini.play.MyVodFind3$9 */
    class C05009 implements OnItemSelectedListener {
        C05009() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodFind3.this.select_item = (String) ((HashMap) MyVodFind3.this.listview_item.getItemAtPosition(arg2)).get("ItemID");
            if (MGplayer.isNumeric(MyVodFind3.this.select_item)) {
                MyVodFind3.this.select_type = "0";
                MyVodFind3.this.select_year = "0";
                MyVodFind3.this.select_area = "0";
                int item = Integer.parseInt(MyVodFind3.this.select_item);
                MyVodFind3.this.set_list_type(item, 1);
                MyVodFind3.this.set_list_area(item, 1);
                MyVodFind3.this.set_list_year(item, 1);
            }
            String select_year_text = "0";
            if (MGplayer.isNumeric(MyVodFind3.this.select_item) && MGplayer.isNumeric(MyVodFind3.this.select_year) && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year != null && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.length() > 1) {
                String[] type_names = VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.split("\\|");
                if (type_names != null && type_names.length > Integer.parseInt(MyVodFind3.this.select_year) && Integer.parseInt(MyVodFind3.this.select_year) - 1 >= 0) {
                    select_year_text = type_names[Integer.parseInt(MyVodFind3.this.select_year) - 1];
                }
            }
            MyVodFind3.this.select_cmd = "&item=" + MyVodFind3.this.select_item + "&itype=" + MyVodFind3.this.select_type + "&iyear=" + select_year_text + "&iarea=" + MyVodFind3.this.select_area;
            MyVodFind3.this.adapter_item.setCurrentIndex(arg2);
            MyVodFind3.this.adapter_item.notifyDataSetChanged();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public AlertDialog init(Activity th, ListViewInterface l) {
        this._this = th;
        this.iface = l;
        this.vodFindActivity = LayoutInflater.from(this._this).inflate(C0216R.layout.vodfindview3, null);
        Typeface typeFace = MGplayer.getFontsType(this._this);
        float rate = MGplayer.getFontsRate();
        final EditText e1 = (EditText) this.vodFindActivity.findViewById(C0216R.id.edittext_find);
        e1.setTypeface(typeFace);
        e1.setTextSize((float) ((int) (9.0f * rate)));
        e1.setOnFocusChangeListener(new C04921());
        final LinearLayout linearlayout_find = (LinearLayout) this.vodFindActivity.findViewById(C0216R.id.linearlayout_find);
        final EditText t = (EditText) this.vodFindActivity.findViewById(C0216R.id.edittext_find);
        this.inputgrid = (GridView) this.vodFindActivity.findViewById(C0216R.id.inputgrid);
        this.inputgrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                HashMap<String, Object> map = (HashMap) MyVodFind3.this.inputgrid.getItemAtPosition(arg2);
                String t0 = map.get("ItemChar").toString();
                String te = map.get("ItemEvent").toString();
                if (te.equals("CE")) {
                    e1.setText("");
                } else if (te.equals("OK")) {
                    String value = t.getText().toString();
                    if (value == null || value.length() <= 0) {
                        Toast.makeText(MyVodFind3.this._this, MyVodFind3.this._this.getString(C0216R.string.vodfind_text11).toString(), 0).show();
                        return;
                    }
                    String cmd = "&find=" + URLEncoder.encode(value);
                    MGplayer.MyPrintln("find cmd url:" + cmd);
                    MyVodFind3.this.iface.callback(0, cmd);
                    MyVodFind3.this.dialog.hide();
                } else if (te.equals("BACK")) {
                    MyVodFind3.this.dialog.hide();
                } else {
                    e1.setText(e1.getText().toString() + t0);
                }
                if (MyVodFind3.this.inputadapter != null) {
                    MyVodFind3.this.inputadapter.setSeclection(arg2);
                    MyVodFind3.this.inputadapter.notifyDataSetChanged();
                }
            }
        });
        this.inputgrid.setOnItemSelectedListener(new C04943());
        this.inputgrid.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    linearlayout_find.setBackgroundResource(C0216R.drawable.gradient_find);
                } else {
                    linearlayout_find.setBackgroundResource(C0216R.color.transparent);
                }
            }
        });
        this.inputgrid.setSelector(17170445);
        init_input();
        set_inputlist();
        this.listview_item = (ListView) this.vodFindActivity.findViewById(C0216R.id.listview_item);
        this.listview_type = (ListView) this.vodFindActivity.findViewById(C0216R.id.listview_type);
        this.listview_year = (ListView) this.vodFindActivity.findViewById(C0216R.id.listview_year);
        this.listview_area = (ListView) this.vodFindActivity.findViewById(C0216R.id.listview_area);
        this.listtext_item = (TextView) this.vodFindActivity.findViewById(C0216R.id.listtext_item);
        this.listtext_type = (TextView) this.vodFindActivity.findViewById(C0216R.id.listtext_type);
        this.listtext_year = (TextView) this.vodFindActivity.findViewById(C0216R.id.listtext_year);
        this.listtext_area = (TextView) this.vodFindActivity.findViewById(C0216R.id.listtext_area);
        this.button_find = (Button) this.vodFindActivity.findViewById(C0216R.id.button_find);
        this.listtext_item.setTextSize(6.0f * rate);
        this.listtext_item.setTypeface(typeFace);
        this.listtext_type.setTextSize(6.0f * rate);
        this.listtext_type.setTypeface(typeFace);
        this.listtext_year.setTextSize(6.0f * rate);
        this.listtext_year.setTypeface(typeFace);
        this.listtext_area.setTextSize(6.0f * rate);
        this.listtext_area.setTypeface(typeFace);
        this.button_find.setTextSize(7.0f * rate);
        this.button_find.setTypeface(typeFace);
        this.button_find.setOnClickListener(new C04965());
        this.button_find.setOnFocusChangeListener(new C04976());
        final Drawable drawable = this._this.getResources().getDrawable(C0216R.mipmap.se);
        Drawable drawable2 = this._this.getResources().getDrawable(C0216R.mipmap.se4);
        this.listview_item.setSelector(drawable);
        this.listview_item.setOnItemClickListener(new C04987());
        this.listview_item.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MyVodFind3.this.listview_item.setSelection(Integer.parseInt(MyVodFind3.this.select_item));
                    MyVodFind3.this.listview_item.setBackgroundResource(C0216R.drawable.gradient_find);
                    MyVodFind3.this.adapter_item.notifyDataSetChanged();
                    MyVodFind3.this.listview_item.setSelector(drawable);
                    return;
                }
                MyVodFind3.this.listview_item.setBackgroundResource(C0216R.color.transparent);
                MyVodFind3.this.adapter_item.notifyDataSetChanged();
                MyVodFind3.this.listview_item.setSelector(C0216R.color.transparent);
            }
        });
        this.listview_item.setOnItemSelectedListener(new C05009());
        this.listview_type.setSelector(drawable2);
        this.listview_type.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                MyVodFind3.this.select_type = (String) ((HashMap) MyVodFind3.this.listview_type.getItemAtPosition(arg2)).get("ItemID");
                String select_year_text = "0";
                if (MGplayer.isNumeric(MyVodFind3.this.select_item) && MGplayer.isNumeric(MyVodFind3.this.select_year) && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year != null && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.length() > 1) {
                    String[] type_names = VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.split("\\|");
                    if (type_names != null && type_names.length > Integer.parseInt(MyVodFind3.this.select_year) && Integer.parseInt(MyVodFind3.this.select_year) - 1 >= 0) {
                        select_year_text = type_names[Integer.parseInt(MyVodFind3.this.select_year) - 1];
                    }
                }
                MyVodFind3.this.select_cmd = "&item=" + MyVodFind3.this.select_item + "&itype=" + MyVodFind3.this.select_type + "&iyear=" + select_year_text + "&iarea=" + MyVodFind3.this.select_area;
                MyVodFind3.this.adapter_type.setCurrentIndex(arg2);
                MyVodFind3.this.adapter_type.notifyDataSetChanged();
            }
        });
        this.listview_type.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MyVodFind3.this.listview_type.setBackgroundResource(C0216R.drawable.gradient_find);
                    MyVodFind3.this.adapter_type.notifyDataSetChanged();
                    MyVodFind3.this.listview_type.setSelector(drawable);
                    return;
                }
                MyVodFind3.this.listview_type.setBackgroundResource(C0216R.color.transparent);
                MyVodFind3.this.adapter_type.notifyDataSetChanged();
                MyVodFind3.this.listview_type.setSelector(C0216R.color.transparent);
            }
        });
        this.listview_area.setSelector(drawable2);
        this.listview_area.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                MyVodFind3.this.select_area = (String) ((HashMap) MyVodFind3.this.listview_area.getItemAtPosition(arg2)).get("ItemID");
                String select_year_text = "0";
                if (MGplayer.isNumeric(MyVodFind3.this.select_item) && MGplayer.isNumeric(MyVodFind3.this.select_year) && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year != null && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.length() > 1) {
                    String[] type_names = VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.split("\\|");
                    if (type_names != null && type_names.length > Integer.parseInt(MyVodFind3.this.select_year) && Integer.parseInt(MyVodFind3.this.select_year) - 1 >= 0) {
                        select_year_text = type_names[Integer.parseInt(MyVodFind3.this.select_year) - 1];
                    }
                }
                MyVodFind3.this.select_cmd = "&item=" + MyVodFind3.this.select_item + "&itype=" + MyVodFind3.this.select_type + "&iyear=" + select_year_text + "&iarea=" + MyVodFind3.this.select_area;
                MyVodFind3.this.adapter_area.setCurrentIndex(arg2);
                MyVodFind3.this.adapter_area.notifyDataSetChanged();
            }
        });
        this.listview_area.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MyVodFind3.this.listview_area.setBackgroundResource(C0216R.drawable.gradient_find);
                    MyVodFind3.this.adapter_area.notifyDataSetChanged();
                    MyVodFind3.this.listview_area.setSelector(drawable);
                    return;
                }
                MyVodFind3.this.listview_area.setBackgroundResource(C0216R.color.transparent);
                MyVodFind3.this.adapter_area.notifyDataSetChanged();
                MyVodFind3.this.listview_area.setSelector(C0216R.color.transparent);
            }
        });
        this.listview_year.setSelector(drawable2);
        this.listview_year.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                String id = (String) ((HashMap) MyVodFind3.this.listview_year.getItemAtPosition(arg2)).get("ItemID");
                if (MGplayer.isNumeric(id)) {
                    MyVodFind3.this.select_year = id;
                }
                String select_year_text = "0";
                if (MGplayer.isNumeric(MyVodFind3.this.select_item) && MGplayer.isNumeric(MyVodFind3.this.select_year) && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year != null && VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.length() > 1) {
                    String[] type_names = VODplayer.columner[Integer.parseInt(MyVodFind3.this.select_item)].type_year.split("\\|");
                    if (type_names != null && type_names.length > Integer.parseInt(MyVodFind3.this.select_year) && Integer.parseInt(MyVodFind3.this.select_year) - 1 >= 0) {
                        select_year_text = type_names[Integer.parseInt(MyVodFind3.this.select_year) - 1];
                    }
                }
                MyVodFind3.this.select_cmd = "&item=" + MyVodFind3.this.select_item + "&itype=" + MyVodFind3.this.select_type + "&iyear=" + select_year_text + "&iarea=" + MyVodFind3.this.select_area;
                MyVodFind3.this.adapter_year.setCurrentIndex(arg2);
                MyVodFind3.this.adapter_year.notifyDataSetChanged();
            }
        });
        this.listview_year.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MyVodFind3.this.listview_year.setBackgroundResource(C0216R.drawable.gradient_find);
                    MyVodFind3.this.adapter_year.notifyDataSetChanged();
                    MyVodFind3.this.listview_year.setSelector(drawable);
                    return;
                }
                MyVodFind3.this.listview_year.setBackgroundResource(C0216R.color.transparent);
                MyVodFind3.this.adapter_year.notifyDataSetChanged();
                MyVodFind3.this.listview_year.setSelector(C0216R.color.transparent);
            }
        });
        set_list_item();
        set_list_type(this.item_index, 0);
        set_list_area(this.item_index, 0);
        set_list_year(this.item_index, 0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MyVodFind3.this.inputadapter.notifyDataSetChanged();
            }
        }, 100);
        this.dialog = new Builder(this._this).setView(this.vodFindActivity).show();
        this.dialog.getWindow().setLayout((MGplayer.screenWidth / 8) * 7, (MGplayer.screenHeight / 8) * 7);
        return this.dialog;
    }

    private void init_input() {
        this.inputadapter = new MySimpleAdapterVodFindView(this._this, this.inputlist, C0216R.layout.inputitem, new String[]{"ItemChar"}, new int[]{C0216R.id.ItemChar});
        this.inputgrid.setAdapter(this.inputadapter);
        this.inputadapter.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                    return false;
                }
                ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                return true;
            }
        });
    }

    private void set_inputlist() {
        this.inputlist.clear();
        String t = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        for (int ii = 0; ii < t.length(); ii++) {
            HashMap<String, Object> map = new HashMap();
            map.put("ItemChar", Character.valueOf(t.charAt(ii)));
            map.put("ItemEvent", Character.valueOf(t.charAt(ii)));
            this.inputlist.add(map);
        }
        HashMap<String, Object> map2 = new HashMap();
        map2.put("ItemChar", "CE");
        map2.put("ItemEvent", "CE");
        this.inputlist.add(map2);
        HashMap<String, Object> map3 = new HashMap();
        map3.put("ItemChar", this._this.getString(C0216R.string.ok));
        map3.put("ItemEvent", "OK");
        this.inputlist.add(map3);
        HashMap<String, Object> map4 = new HashMap();
        map4.put("ItemChar", this._this.getString(C0216R.string.back));
        map4.put("ItemEvent", "BACK");
        this.inputlist.add(map4);
        this.inputadapter.notifyDataSetChanged();
    }

    public void set_list_item() {
        this.list_item = new ArrayList();
        for (int i = 0; i < VODplayer.columner.length; i++) {
            if (VODplayer.columner_needps[i] == 0) {
                HashMap<String, Object> map = new HashMap();
                map.put("ItemID", String.valueOf(i));
                map.put("ItemName", VODplayer.columner[i].name);
                MGplayer.MyPrintln("item id:" + String.valueOf(i));
                this.list_item.add(map);
            }
        }
        this.adapter_item = new MySimpleAdapterTypeListView2(this._this, this.list_item, C0216R.layout.classifyitem2, new String[]{"ItemName"}, new int[]{C0216R.id.ItemName});
        this.listview_item.setAdapter(this.adapter_item);
        this.adapter_item.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                    return false;
                }
                ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                return true;
            }
        });
    }

    public void set_list_type(int type, int reset) {
        this.list_type.clear();
        MGplayer.MyPrintln("columner[" + type + "].type_year = " + VODplayer.columner[type].type_type);
        String[] type_names = null;
        if (VODplayer.columner[type].type_type != null && VODplayer.columner[type].type_type.length() > 1) {
            type_names = VODplayer.columner[type].type_type.split("\\|");
        }
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemID", "0");
        map0.put("ItemName", this._this.getString(C0216R.string.vodclassify_text5).toString());
        this.list_type.add(map0);
        if (type_names != null) {
            for (int i = 0; i < type_names.length; i++) {
                HashMap<String, Object> map = new HashMap();
                map.put("ItemID", String.valueOf(i + 1));
                map.put("ItemName", type_names[i]);
                this.list_type.add(map);
            }
        }
        if (reset == 0) {
            this.adapter_type = new MySimpleAdapterTypeListView2(this._this, this.list_type, C0216R.layout.classifyitem2, new String[]{"ItemName"}, new int[]{C0216R.id.ItemName});
            this.listview_type.setAdapter(this.adapter_type);
            this.adapter_type.setViewBinder(new ViewBinder() {
                public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                    if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                        return false;
                    }
                    ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                    return true;
                }
            });
            return;
        }
        if (MGplayer.isNumeric(this.select_type)) {
            this.select_type = "0";
            int item = Integer.parseInt(this.select_type);
            this.adapter_type.setCurrentIndex(item);
            this.listview_type.setSelection(item);
        }
        this.adapter_type.notifyDataSetChanged();
    }

    public void set_list_year(int type, int reset) {
        this.list_year.clear();
        MGplayer.MyPrintln("columner[" + type + "].type_year = " + VODplayer.columner[type].type_year);
        String[] type_names = null;
        if (VODplayer.columner[type].type_year != null && VODplayer.columner[type].type_year.length() > 1) {
            type_names = VODplayer.columner[type].type_year.split("\\|");
        }
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemID", "0");
        map0.put("ItemName", this._this.getString(C0216R.string.vodclassify_text5).toString());
        this.list_year.add(map0);
        if (type_names != null) {
            for (int i = 0; i < type_names.length; i++) {
                HashMap<String, Object> map = new HashMap();
                map.put("ItemID", String.valueOf(i + 1));
                map.put("ItemName", type_names[i]);
                this.list_year.add(map);
            }
        }
        if (reset == 0) {
            this.adapter_year = new MySimpleAdapterTypeListView2(this._this, this.list_year, C0216R.layout.classifyitem2, new String[]{"ItemName"}, new int[]{C0216R.id.ItemName});
            this.listview_year.setAdapter(this.adapter_year);
            this.adapter_year.setViewBinder(new ViewBinder() {
                public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                    if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                        return false;
                    }
                    ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                    return true;
                }
            });
            return;
        }
        if (MGplayer.isNumeric(this.select_year)) {
            this.select_year = "0";
            int item = Integer.parseInt(this.select_year);
            this.adapter_year.setCurrentIndex(item);
            this.listview_year.setSelection(item);
        }
        this.adapter_year.notifyDataSetChanged();
    }

    public void set_list_area(int type, int reset) {
        this.list_area.clear();
        MGplayer.MyPrintln("columner[" + type + "].type_area = " + VODplayer.columner[type].type_area);
        String[] type_names = null;
        if (VODplayer.columner[type].type_area != null && VODplayer.columner[type].type_area.length() > 1) {
            type_names = VODplayer.columner[type].type_area.split("\\|");
        }
        HashMap<String, Object> map0 = new HashMap();
        map0.put("ItemID", "0");
        map0.put("ItemName", this._this.getString(C0216R.string.vodclassify_text5).toString());
        this.list_area.add(map0);
        if (type_names != null) {
            for (int i = 0; i < type_names.length; i++) {
                HashMap<String, Object> map = new HashMap();
                map.put("ItemID", String.valueOf(i + 1));
                map.put("ItemName", type_names[i]);
                this.list_area.add(map);
            }
        }
        if (reset == 0) {
            this.adapter_area = new MySimpleAdapterTypeListView2(this._this, this.list_area, C0216R.layout.classifyitem2, new String[]{"ItemName"}, new int[]{C0216R.id.ItemName});
            this.listview_area.setAdapter(this.adapter_area);
            this.adapter_area.setViewBinder(new ViewBinder() {
                public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
                    if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                        return false;
                    }
                    ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
                    return true;
                }
            });
            return;
        }
        if (MGplayer.isNumeric(this.select_area)) {
            this.select_area = "0";
            int item = Integer.parseInt(this.select_area);
            this.adapter_area.setCurrentIndex(item);
            this.listview_area.setSelection(item);
        }
        this.adapter_area.notifyDataSetChanged();
    }
}
