package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter.ViewBinder;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;

public class MyVodSelectionsView extends LinearLayout {
    private Context _this = null;
    private MySimpleAdapterVodSelectionsView adapter = null;
    private ListViewInterface iface = null;
    private ArrayList<HashMap<String, Object>> list = new ArrayList();
    private GridView selgrid = null;

    /* renamed from: com.gemini.play.MyVodSelectionsView$1 */
    class C05301 implements OnItemClickListener {
        C05301() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            MyVodSelectionsView.this.iface.callback(0, (String) ((HashMap) MyVodSelectionsView.this.selgrid.getItemAtPosition(arg2)).get("ItemId"));
            MyVodSelectionsView.this.hideSelectionsView();
            if (MyVodSelectionsView.this.adapter != null) {
                MyVodSelectionsView.this.adapter.setSeclection(arg2);
                MyVodSelectionsView.this.adapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodSelectionsView$2 */
    class C05312 implements OnItemSelectedListener {
        C05312() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (MyVodSelectionsView.this.adapter != null) {
                MyVodSelectionsView.this.adapter.setSeclection(arg2);
                MyVodSelectionsView.this.adapter.notifyDataSetChanged();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyVodSelectionsView$3 */
    class C05323 implements AnimationListener {
        C05323() {
        }

        public void onAnimationEnd(Animation animation) {
            MyVodSelectionsView.this.clearAnimation();
            MyVodSelectionsView.this.listFocus();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyVodSelectionsView$4 */
    class C05334 implements AnimationListener {
        C05334() {
        }

        public void onAnimationEnd(Animation animation) {
            MyVodSelectionsView.this.setVisibility(8);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyVodSelectionsView$5 */
    class C05345 implements ViewBinder {
        C05345() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    public MyVodSelectionsView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.selectionsview, this, true);
        init();
    }

    public MyVodSelectionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.selectionsview, this, true);
        init();
    }

    public MyVodSelectionsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.selectionsview, this, true);
        init();
    }

    private void init() {
        this.selgrid = (GridView) findViewById(C0216R.id.selgrid);
        this.selgrid.setOnItemClickListener(new C05301());
        this.selgrid.setOnItemSelectedListener(new C05312());
        this.selgrid.setSelector(17170445);
        init_list();
    }

    public void showSelectionsView(VodListStatus s, int collect) {
        showSelectionsView(s, collect, null);
    }

    public void showSelectionsView(VodListStatus s, int collect, String num) {
        if (!isShown()) {
            set_list(s.url, num);
            setVisibility(0);
            if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
                setFocusable(true);
                listFocus();
                return;
            }
            TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, (float) (MGplayer.screenHeight / 3), 0.0f);
            animation.setDuration(500);
            animation.setAnimationListener(new C05323());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void hideSelectionsView() {
        if (MGplayer.getCpuName().equals("A20") || !MGplayer.need_scroll_list) {
            setFocusable(true);
            setVisibility(8);
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (MGplayer.screenHeight / 3));
        animation.setDuration(300);
        animation.setAnimationListener(new C05334());
        setFocusable(true);
        startAnimation(animation);
    }

    public void init_list() {
        this.adapter = new MySimpleAdapterVodSelectionsView(this._this, this.list, C0216R.layout.selectionsitem, new String[]{"ItemView", "ItemTitle"}, new int[]{C0216R.id.ItemView, C0216R.id.ItemTitle});
        this.selgrid.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C05345());
    }

    public void set_list(String urls, String num) {
        String[] urlss = urls.split("\\|");
        this.list.clear();
        int selectindex = 0;
        for (int ii = 0; ii < urlss.length; ii++) {
            String[] item = urlss[ii].split("#");
            if (item.length >= 2) {
                HashMap<String, Object> map = new HashMap();
                map.put("ItemId", item[0]);
                map.put("ItemUrl", item[1]);
                map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti));
                map.put("ItemTitle", this._this.getString(C0216R.string.playerinfo_text12).toString() + item[0] + this._this.getString(C0216R.string.playerinfo_text13).toString());
                this.list.add(map);
            }
            if (num != null && item[0].equals(num)) {
                selectindex = ii;
            }
        }
        this.adapter.setSeclection(selectindex);
        this.adapter.notifyDataSetChanged();
    }

    public void listFocus() {
        this.selgrid.setFocusable(true);
        this.selgrid.setFocusableInTouchMode(true);
        this.selgrid.requestFocus();
        this.selgrid.requestFocusFromTouch();
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }
}
