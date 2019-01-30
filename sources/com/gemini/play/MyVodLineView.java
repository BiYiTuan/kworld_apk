package com.gemini.play;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter.ViewBinder;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodLineView {
    private MySimpleAdapterLiveLineView adapter = null;
    private AlertDialog dialog = null;
    private ListViewInterface iface = null;
    private LayoutInflater layoutInflater = null;
    private View lineActivity = null;
    private ArrayList<HashMap<String, Object>> list = new ArrayList();
    private ListView listview;
    private Context mContext = null;

    /* renamed from: com.gemini.play.MyVodLineView$1 */
    class C05251 implements OnClickListener {
        C05251() {
        }

        public void onClick(View arg0) {
            MyVodLineView.this.dialog.hide();
        }
    }

    /* renamed from: com.gemini.play.MyVodLineView$2 */
    class C05262 implements OnFocusChangeListener {
        C05262() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodLineView$3 */
    class C05273 implements OnItemClickListener {
        C05273() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodLineView.this.iface.callback(0, ((HashMap) MyVodLineView.this.listview.getItemAtPosition(arg2)).get("ItemLine").toString());
            if (MyVodLineView.this.dialog != null) {
                MyVodLineView.this.dialog.hide();
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodLineView$5 */
    class C05295 implements ViewBinder {
        C05295() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    public void initView(final Context _this) {
        this.mContext = _this;
        this.layoutInflater = LayoutInflater.from(_this);
        this.lineActivity = this.layoutInflater.inflate(C0216R.layout.vodline, null);
        this.listview = (ListView) this.lineActivity.findViewById(C0216R.id.line_listView);
        this.listview.setSelector(new ColorDrawable(0));
        Button cancelbutton = (Button) this.lineActivity.findViewById(C0216R.id.line_cancel);
        cancelbutton.setOnClickListener(new C05251());
        cancelbutton.setOnFocusChangeListener(new C05262());
        Typeface typeFace = MGplayer.getFontsType(_this);
        cancelbutton.setTextSize(7.0f * MGplayer.getFontsRate());
        cancelbutton.setTypeface(typeFace);
        this.listview.setOnItemClickListener(new C05273());
        this.listview.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MyVodLineView.this.listview.setSelector(ContextCompat.getDrawable(_this, C0216R.drawable.gradient9));
                    return;
                }
                MGplayer.MyPrintln("listview onfocus");
                MyVodLineView.this.listview.setSelector(new ColorDrawable(0));
            }
        });
        this.dialog = new Builder(_this).setView(this.lineActivity).create();
        this.dialog.getWindow().setLayout((MGplayer.screenWidth / 5) * 2, MGplayer.screenHeight);
        init_list();
    }

    public void showView(Context _this, String urls) {
        show_list(urls);
        this.dialog.show();
    }

    public void init_list() {
        this.adapter = new MySimpleAdapterLiveLineView(this.mContext, this.list, C0216R.layout.lineitem, new String[]{"ItemTitle"}, new int[]{C0216R.id.ItemTitle});
        this.listview.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C05295());
    }

    public void hide() {
        if (this.dialog != null) {
            this.dialog.hide();
        }
    }

    private void show_list(String urls) {
        this.list.clear();
        String[] url_lines = urls.split("@\\$@");
        for (int ii = 0; ii < url_lines.length; ii++) {
            HashMap<String, Object> map = new HashMap();
            map.put("ItemTitle", this.mContext.getString(C0216R.string.liveline_text2) + " " + this.mContext.getString(C0216R.string.liveline_text1) + "-" + (ii + 1));
            map.put("ItemLine", url_lines[ii]);
            this.list.add(map);
        }
        this.adapter.notifyDataSetChanged();
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }
}
