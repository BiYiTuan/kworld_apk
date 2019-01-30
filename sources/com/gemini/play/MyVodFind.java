package com.gemini.play;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodFind {
    private Context _this;
    private AlertDialog dialog;
    private ListViewInterface iface = null;
    private MySimpleAdapterInputView inputadapter = null;
    private GridView inputgrid = null;
    private ArrayList<HashMap<String, Object>> inputlist = new ArrayList();
    private int spinner_area_value = 0;
    private String spinner_find_value = null;
    private int spinner_sort_value = 0;
    private int spinner_type_value = 0;
    private int spinner_year_value = 0;
    private View vodFindActivity = null;
    private ArrayList<String> years_array = new ArrayList();

    /* renamed from: com.gemini.play.MyVodFind$1 */
    class C04831 implements OnFocusChangeListener {
        C04831() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFind$3 */
    class C04853 implements OnFocusChangeListener {
        C04853() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFind$4 */
    class C04864 implements OnClickListener {
        C04864() {
        }

        public void onClick(View v) {
            VODplayer.page = 0;
            String cmd = "&sort=" + MyVodFind.this.spinner_sort_value;
            MGplayer.MyPrintln("find cmd url:" + cmd);
            MyVodFind.this.iface.callback(1, cmd);
            MyVodFind.this.dialog.hide();
        }
    }

    /* renamed from: com.gemini.play.MyVodFind$5 */
    class C04875 implements OnFocusChangeListener {
        C04875() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFind$6 */
    class C04886 implements OnClickListener {
        C04886() {
        }

        public void onClick(View v) {
            VODplayer.page = 0;
            String years_value = "0";
            if (MyVodFind.this.spinner_year_value != 0) {
                years_value = (String) MyVodFind.this.years_array.get(MyVodFind.this.spinner_year_value);
            }
            String cmd = "&itype=" + MyVodFind.this.spinner_type_value + "&iyear=" + years_value + "&iarea=" + MyVodFind.this.spinner_area_value;
            MGplayer.MyPrintln("find cmd url:" + cmd);
            MyVodFind.this.iface.callback(2, cmd);
            MyVodFind.this.dialog.hide();
        }
    }

    /* renamed from: com.gemini.play.MyVodFind$7 */
    class C04897 implements OnFocusChangeListener {
        C04897() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodFind$8 */
    class C04908 implements OnClickListener {
        C04908() {
        }

        public void onClick(View v) {
            MyVodFind.this.dialog.hide();
        }
    }

    /* renamed from: com.gemini.play.MyVodFind$9 */
    class C04919 implements OnFocusChangeListener {
        C04919() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    public AlertDialog init(Context th, ListViewInterface l, String type) {
        this._this = th;
        this.iface = l;
        this.vodFindActivity = LayoutInflater.from(this._this).inflate(C0216R.layout.vodfindview, null);
        Typeface typeFace = MGplayer.getFontsType(this._this);
        final float rate = MGplayer.getFontsRate();
        TextView t3 = (TextView) this.vodFindActivity.findViewById(C0216R.id.text_area);
        t3.setTextSize((float) ((int) (7.0f * rate)));
        t3.setTypeface(typeFace);
        TextView t4 = (TextView) this.vodFindActivity.findViewById(C0216R.id.text_year);
        t4.setTextSize((float) ((int) (7.0f * rate)));
        t4.setTypeface(typeFace);
        TextView t5 = (TextView) this.vodFindActivity.findViewById(C0216R.id.text_type);
        t5.setTextSize((float) ((int) (7.0f * rate)));
        t5.setTypeface(typeFace);
        final EditText e1 = (EditText) this.vodFindActivity.findViewById(C0216R.id.edittext_find);
        e1.setTypeface(typeFace);
        e1.setTextSize((float) ((int) (10.0f * rate)));
        e1.setOnFocusChangeListener(new C04831());
        Button b0 = (Button) this.vodFindActivity.findViewById(C0216R.id.button_find);
        EditText t = (EditText) this.vodFindActivity.findViewById(C0216R.id.edittext_find);
        b0.setTypeface(typeFace);
        b0.setTextSize((float) ((int) (7.0f * rate)));
        final EditText editText = t;
        b0.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MyVodFind.this.spinner_find_value = editText.getText().toString();
                if (MyVodFind.this.spinner_find_value == null || MyVodFind.this.spinner_find_value.length() <= 0) {
                    Toast.makeText(MyVodFind.this._this, MyVodFind.this._this.getString(C0216R.string.vodfind_text11).toString(), 0).show();
                    return;
                }
                VODplayer.page = 0;
                String cmd = "&find=" + URLEncoder.encode(MyVodFind.this.spinner_find_value);
                MGplayer.MyPrintln("find cmd url:" + cmd);
                MyVodFind.this.iface.callback(0, cmd);
                MyVodFind.this.dialog.hide();
            }
        });
        b0.setOnFocusChangeListener(new C04853());
        Button b1 = (Button) this.vodFindActivity.findViewById(C0216R.id.button_sort);
        b1.setTextSize((float) ((int) (7.0f * rate)));
        b1.setTypeface(typeFace);
        b1.setOnClickListener(new C04864());
        b1.setOnFocusChangeListener(new C04875());
        Button b2 = (Button) this.vodFindActivity.findViewById(C0216R.id.button_filter);
        b2.setTextSize((float) ((int) (7.0f * rate)));
        b2.setTypeface(typeFace);
        b2.setOnClickListener(new C04886());
        b2.setOnFocusChangeListener(new C04897());
        Button b3 = (Button) this.vodFindActivity.findViewById(C0216R.id.button_back);
        b3.setTextSize((float) ((int) (7.0f * rate)));
        b3.setTypeface(typeFace);
        b3.setOnClickListener(new C04908());
        b3.setOnFocusChangeListener(new C04919());
        MyArrayAdapterFindView adapter = new MyArrayAdapterFindView(this._this, new String[]{this._this.getString(C0216R.string.vodfind_text6).toString(), this._this.getString(C0216R.string.vodfind_text5).toString()});
        adapter.setDropDownViewResource(17367048);
        Spinner spinner_sort = (Spinner) this.vodFindActivity.findViewById(C0216R.id.spinner_sort);
        spinner_sort.setAdapter(adapter);
        spinner_sort.setPrompt(this._this.getString(C0216R.string.vodfind_text1).toString());
        spinner_sort.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                MGplayer.MyPrintln("spinner_sort:" + arg2);
                ((TextView) arg1).setTextSize((float) ((int) (rate * 7.0f)));
                MyVodFind.this.spinner_sort_value = arg2;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ((Spinner) this.vodFindActivity.findViewById(C0216R.id.spinner_area)).setAdapter(adapter);
        ((Spinner) this.vodFindActivity.findViewById(C0216R.id.spinner_year)).setAdapter(adapter);
        ((Spinner) this.vodFindActivity.findViewById(C0216R.id.spinner_type)).setAdapter(adapter);
        this.inputgrid = (GridView) this.vodFindActivity.findViewById(C0216R.id.inputgrid);
        this.inputgrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                String t0 = ((HashMap) MyVodFind.this.inputgrid.getItemAtPosition(arg2)).get("ItemChar").toString();
                if (t0.equals("CE")) {
                    e1.setText("");
                } else {
                    e1.setText(e1.getText().toString() + t0);
                }
                if (MyVodFind.this.inputadapter != null) {
                    MyVodFind.this.inputadapter.setSeclection(arg2);
                    MyVodFind.this.inputadapter.notifyDataSetChanged();
                }
            }
        });
        this.inputgrid.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                if (MyVodFind.this.inputadapter != null) {
                    MyVodFind.this.inputadapter.setSeclection(arg2);
                    MyVodFind.this.inputadapter.notifyDataSetChanged();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.inputgrid.setSelector(17170445);
        init_input();
        set_inputlist();
        VodTypeStatus s = getVodTypeStatus(type);
        if (s != null) {
            initTypeSpinner(s);
        }
        this.dialog = new Builder(this._this).setView(this.vodFindActivity).show();
        this.dialog.getWindow().setLayout((MGplayer.screenWidth / 5) * 4, (MGplayer.screenHeight / 5) * 4);
        return this.dialog;
    }

    private void init_input() {
        this.inputadapter = new MySimpleAdapterInputView(this._this, this.inputlist, C0216R.layout.inputitem, new String[]{"ItemChar"}, new int[]{C0216R.id.ItemChar});
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
            this.inputlist.add(map);
        }
        HashMap<String, Object> map2 = new HashMap();
        map2.put("ItemChar", "CE");
        this.inputlist.add(map2);
        this.inputadapter.notifyDataSetChanged();
    }

    private VodTypeStatus getVodTypeStatus(String type) {
        if (!MGplayer.isNumeric(type) || Integer.parseInt(type) >= 4) {
            return null;
        }
        return VODplayer.typeGet(Integer.parseInt(type));
    }

    private void initTypeSpinner(VodTypeStatus s) {
        ArrayList array;
        final float rate = MGplayer.getFontsRate();
        if (s.areas != null) {
            array = new ArrayList();
            array.add(this._this.getString(C0216R.string.vodfind_text4).toString());
            for (Object add : s.areas) {
                array.add(add);
            }
            MyArrayAdapterFindView adapter_area = new MyArrayAdapterFindView(this._this, array);
            adapter_area.setDropDownViewResource(17367049);
            Spinner spinner_area = (Spinner) this.vodFindActivity.findViewById(C0216R.id.spinner_area);
            spinner_area.setAdapter(adapter_area);
            spinner_area.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    MGplayer.MyPrintln("spinner_area:" + arg2);
                    MyVodFind.this.spinner_area_value = arg2;
                    ((TextView) arg1).setTextSize((float) ((int) (((double) rate) * 6.8d)));
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
        if (s.years != null) {
            this.years_array.clear();
            this.years_array.add(this._this.getString(C0216R.string.vodfind_text4).toString());
            for (Object add2 : s.years) {
                this.years_array.add(add2);
            }
            MyArrayAdapterFindView adapter_year = new MyArrayAdapterFindView(this._this, this.years_array);
            adapter_year.setDropDownViewResource(17367049);
            Spinner spinner_year = (Spinner) this.vodFindActivity.findViewById(C0216R.id.spinner_year);
            spinner_year.setAdapter(adapter_year);
            spinner_year.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    MGplayer.MyPrintln("spinner_year:" + arg2);
                    MyVodFind.this.spinner_year_value = arg2;
                    ((TextView) arg1).setTextSize((float) ((int) (((double) rate) * 6.8d)));
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
        if (s.types != null) {
            array = new ArrayList();
            array.add(this._this.getString(C0216R.string.vodfind_text4).toString());
            for (Object add3 : s.types) {
                array.add(add3);
            }
            MyArrayAdapterFindView adapter_type = new MyArrayAdapterFindView(this._this, array);
            adapter_type.setDropDownViewResource(17367049);
            Spinner spinner_type = (Spinner) this.vodFindActivity.findViewById(C0216R.id.spinner_type);
            spinner_type.setAdapter(adapter_type);
            spinner_type.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    MGplayer.MyPrintln("spinner_type:" + arg2);
                    MyVodFind.this.spinner_type_value = arg2;
                    ((TextView) arg1).setTextSize((float) ((int) (((double) rate) * 6.8d)));
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }
}
