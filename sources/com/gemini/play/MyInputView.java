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

public class MyInputView extends LinearLayout {
    private Context _this;
    private MySimpleAdapterInputView adapter = null;
    private ListViewInterface iface = null;
    private GridView inputgrid = null;
    private ArrayList<HashMap<String, Object>> list = new ArrayList();

    /* renamed from: com.gemini.play.MyInputView$1 */
    class C04011 implements OnItemClickListener {
        C04011() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (MyInputView.this.adapter != null) {
                MyInputView.this.adapter.setSeclection(arg2);
                MyInputView.this.adapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: com.gemini.play.MyInputView$2 */
    class C04022 implements OnItemSelectedListener {
        C04022() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (MyInputView.this.adapter != null) {
                MyInputView.this.adapter.setSeclection(arg2);
                MyInputView.this.adapter.notifyDataSetChanged();
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* renamed from: com.gemini.play.MyInputView$3 */
    class C04033 implements AnimationListener {
        C04033() {
        }

        public void onAnimationEnd(Animation animation) {
            MyInputView.this.clearAnimation();
            MyInputView.this.listFocus();
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyInputView$4 */
    class C04044 implements AnimationListener {
        C04044() {
        }

        public void onAnimationEnd(Animation animation) {
            MyInputView.this.setVisibility(8);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* renamed from: com.gemini.play.MyInputView$5 */
    class C04055 implements ViewBinder {
        C04055() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    public MyInputView(Context context) {
        super(context);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.inputview, this, true);
        init();
    }

    public MyInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.inputview, this, true);
        init();
    }

    public MyInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.inputview, this, true);
        init();
    }

    private void init() {
        this.inputgrid = (GridView) findViewById(C0216R.id.inputgrid);
        this.inputgrid.setOnItemClickListener(new C04011());
        this.inputgrid.setOnItemSelectedListener(new C04022());
        this.inputgrid.setSelector(17170445);
        init_list();
    }

    public void showInputView() {
        if (!isShown()) {
            set_list();
            setVisibility(0);
            TranslateAnimation animation = new TranslateAnimation((float) ((MGplayer.screenWidth / 10) * 3), 0.0f, 0.0f, 0.0f);
            animation.setDuration(500);
            animation.setAnimationListener(new C04033());
            setFocusable(true);
            startAnimation(animation);
        }
    }

    public void hideInputView() {
        TranslateAnimation animation = new TranslateAnimation(0.0f, (float) ((MGplayer.screenWidth / 10) * 3), 0.0f, 0.0f);
        animation.setDuration(300);
        animation.setAnimationListener(new C04044());
        setFocusable(true);
        startAnimation(animation);
    }

    public void init_list() {
        this.adapter = new MySimpleAdapterInputView(this._this, this.list, C0216R.layout.inputitem, new String[]{"ItemChar"}, new int[]{C0216R.id.ItemChar});
        this.inputgrid.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C04055());
    }

    public void set_list() {
        this.list.clear();
        String t = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        for (int ii = 0; ii < t.length(); ii++) {
            HashMap<String, Object> map = new HashMap();
            map.put("ItemChar", Character.valueOf(t.charAt(ii)));
            this.list.add(map);
        }
        this.adapter.notifyDataSetChanged();
    }

    public void listFocus() {
        this.inputgrid.setFocusable(true);
        this.inputgrid.setFocusableInTouchMode(true);
        this.inputgrid.requestFocus();
        this.inputgrid.requestFocusFromTouch();
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }
}
