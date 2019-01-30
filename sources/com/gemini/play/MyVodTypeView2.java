package com.gemini.play;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;

public class MyVodTypeView2 extends LinearLayout {
    private Context _this;
    private boolean enable_focus;
    private ListViewInterface iface;
    private ImageView[] imager;
    private int index;
    private LinearLayout[] layouter;
    private float rate;
    private int showIndex;
    private TextView[] texter;
    private Typeface typeFace;

    /* renamed from: com.gemini.play.MyVodTypeView2$1 */
    class C05451 implements OnClickListener {
        C05451() {
        }

        public void onClick(View arg0) {
            MyVodTypeView2.this.selectIndex(0);
            if (VODplayer.columner != null && MyVodTypeView2.this.index < VODplayer.columner.length && VODplayer.columner[0].needps == 1) {
                MyVodTypeView2.this.inputPasswordView("0", -1);
            } else if (VODplayer.columner != null && MyVodTypeView2.this.index < VODplayer.columner.length && VODplayer.columner[0].needps == 0) {
                MyVodTypeView2.this.iface.callback(0, "0");
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView2$2 */
    class C05462 implements OnClickListener {
        C05462() {
        }

        public void onClick(View arg0) {
            MyVodTypeView2.this.selectIndex(1);
            if (VODplayer.columner != null && MyVodTypeView2.this.index < VODplayer.columner.length && VODplayer.columner[1].needps == 1) {
                MyVodTypeView2.this.inputPasswordView("1", -1);
            } else if (VODplayer.columner == null || MyVodTypeView2.this.index >= VODplayer.columner.length || VODplayer.columner[1].needps != 0) {
                MyVodTypeView2.this.iface.callback(0, "1");
            } else {
                MyVodTypeView2.this.iface.callback(0, "1");
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView2$3 */
    class C05473 implements OnClickListener {
        C05473() {
        }

        public void onClick(View arg0) {
            MyVodTypeView2.this.selectIndex(2);
            if (VODplayer.columner != null && MyVodTypeView2.this.index < VODplayer.columner.length && VODplayer.columner[2].needps == 1) {
                MyVodTypeView2.this.inputPasswordView("2", -1);
            } else if (VODplayer.columner == null || MyVodTypeView2.this.index >= VODplayer.columner.length || VODplayer.columner[2].needps != 0) {
                MyVodTypeView2.this.iface.callback(0, "2");
            } else {
                MyVodTypeView2.this.iface.callback(0, "2");
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView2$4 */
    class C05484 implements OnClickListener {
        C05484() {
        }

        public void onClick(View arg0) {
            MyVodTypeView2.this.selectIndex(3);
            if (VODplayer.columner != null && MyVodTypeView2.this.index < VODplayer.columner.length && VODplayer.columner[3].needps == 1) {
                MyVodTypeView2.this.inputPasswordView("3", -1);
            } else if (VODplayer.columner == null || MyVodTypeView2.this.index >= VODplayer.columner.length || VODplayer.columner[3].needps != 0) {
                MyVodTypeView2.this.iface.callback(0, "3");
            } else {
                MyVodTypeView2.this.iface.callback(0, "3");
            }
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView2$5 */
    class C05495 implements OnClickListener {
        C05495() {
        }

        public void onClick(View arg0) {
            MyVodTypeView2.this.selectIndex(4);
            MyVodTypeView2.this.iface.callback(1, "4");
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView2$6 */
    class C05506 implements OnClickListener {
        C05506() {
        }

        public void onClick(View arg0) {
            MyVodTypeView2.this.selectIndex(5);
            MyVodTypeView2.this.iface.callback(2, String.valueOf(MyVodTypeView2.this.showIndex));
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView2$7 */
    class C05517 implements OnClickListener {
        C05517() {
        }

        public void onClick(View arg0) {
            MyVodTypeView2.this.selectIndex(6);
            MyVodTypeView2.this.iface.callback(3, "6");
        }
    }

    /* renamed from: com.gemini.play.MyVodTypeView2$8 */
    class C05528 implements OnFocusChangeListener {
        C05528() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                MyVodTypeView2.this.selectIndexNoFocus(MyVodTypeView2.this.showIndex);
            }
        }
    }

    public MyVodTypeView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._this = null;
        this.iface = null;
        this.imager = new ImageView[7];
        this.texter = new TextView[7];
        this.layouter = new LinearLayout[7];
        this.index = 0;
        this.showIndex = 0;
        this.enable_focus = false;
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtypeview2, this, true);
        init();
    }

    public MyVodTypeView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._this = null;
        this.iface = null;
        this.imager = new ImageView[7];
        this.texter = new TextView[7];
        this.layouter = new LinearLayout[7];
        this.index = 0;
        this.showIndex = 0;
        this.enable_focus = false;
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtypeview2, this, true);
        init();
    }

    public MyVodTypeView2(Context context) {
        super(context);
        this._this = null;
        this.iface = null;
        this.imager = new ImageView[7];
        this.texter = new TextView[7];
        this.layouter = new LinearLayout[7];
        this.index = 0;
        this.showIndex = 0;
        this.enable_focus = false;
        this._this = context;
        LayoutInflater.from(context).inflate(C0216R.layout.vodtypeview2, this, true);
        init();
    }

    private void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
        this.imager[0] = (ImageView) findViewById(C0216R.id.ItemView0);
        this.texter[0] = (TextView) findViewById(C0216R.id.ItemTitle0);
        this.layouter[0] = (LinearLayout) findViewById(C0216R.id.ItemLayout0);
        this.layouter[0].setOnClickListener(new C05451());
        if (VODplayer.columner == null || VODplayer.columner.length < 1 || VODplayer.columner[0].name == null) {
            this.texter[0].setText(this._this.getString(C0216R.string.vodlist_text2).toString());
        } else {
            this.texter[0].setText(VODplayer.columner[0].name);
        }
        this.imager[1] = (ImageView) findViewById(C0216R.id.ItemView1);
        this.texter[1] = (TextView) findViewById(C0216R.id.ItemTitle1);
        this.layouter[1] = (LinearLayout) findViewById(C0216R.id.ItemLayout1);
        this.layouter[1].setOnClickListener(new C05462());
        if (VODplayer.columner == null || VODplayer.columner.length < 2 || VODplayer.columner[1].name == null) {
            this.texter[1].setText(this._this.getString(C0216R.string.vodlist_text3).toString());
        } else {
            this.texter[1].setText(VODplayer.columner[1].name);
        }
        this.imager[2] = (ImageView) findViewById(C0216R.id.ItemView2);
        this.texter[2] = (TextView) findViewById(C0216R.id.ItemTitle2);
        this.layouter[2] = (LinearLayout) findViewById(C0216R.id.ItemLayout2);
        this.layouter[2].setOnClickListener(new C05473());
        if (VODplayer.columner == null || VODplayer.columner.length < 3 || VODplayer.columner[2].name == null) {
            this.texter[2].setText(this._this.getString(C0216R.string.vodlist_text4).toString());
        } else {
            this.texter[2].setText(VODplayer.columner[2].name);
        }
        this.imager[3] = (ImageView) findViewById(C0216R.id.ItemView3);
        this.texter[3] = (TextView) findViewById(C0216R.id.ItemTitle3);
        this.layouter[3] = (LinearLayout) findViewById(C0216R.id.ItemLayout3);
        this.layouter[3].setOnClickListener(new C05484());
        if (VODplayer.columner == null || VODplayer.columner.length < 4 || VODplayer.columner[3].name == null) {
            this.texter[3].setText(this._this.getString(C0216R.string.vodlist_text5).toString());
        } else {
            this.texter[3].setText(VODplayer.columner[3].name);
        }
        this.imager[4] = (ImageView) findViewById(C0216R.id.ItemView4);
        this.texter[4] = (TextView) findViewById(C0216R.id.ItemTitle4);
        this.layouter[4] = (LinearLayout) findViewById(C0216R.id.ItemLayout4);
        this.layouter[4].setOnClickListener(new C05495());
        if (VODplayer.columner == null || VODplayer.columner.length < 5 || VODplayer.columner[4].name == null) {
            this.texter[4].setText(this._this.getString(C0216R.string.vodlist_text6).toString());
        } else {
            this.texter[4].setText(VODplayer.columner[4].name);
        }
        this.imager[5] = (ImageView) findViewById(C0216R.id.ItemView5);
        this.texter[5] = (TextView) findViewById(C0216R.id.ItemTitle5);
        this.layouter[5] = (LinearLayout) findViewById(C0216R.id.ItemLayout5);
        this.layouter[5].setOnClickListener(new C05506());
        if (VODplayer.columner == null || VODplayer.columner.length < 6 || VODplayer.columner[5].name == null) {
            this.texter[5].setText(this._this.getString(C0216R.string.vodlist_text8).toString());
        } else {
            this.texter[5].setText(VODplayer.columner[5].name);
        }
        this.imager[6] = (ImageView) findViewById(C0216R.id.ItemView6);
        this.texter[6] = (TextView) findViewById(C0216R.id.ItemTitle6);
        this.layouter[6] = (LinearLayout) findViewById(C0216R.id.ItemLayout6);
        this.layouter[6].setOnClickListener(new C05517());
        if (VODplayer.columner == null || VODplayer.columner.length < 7 || VODplayer.columner[6].name == null) {
            this.texter[6].setText(this._this.getString(C0216R.string.vodlist_text9).toString());
        } else {
            this.texter[6].setText(VODplayer.columner[6].name);
        }
        for (int ii = 0; ii < this.texter.length; ii++) {
            this.texter[ii].setTextSize(this.rate * 8.0f);
            this.texter[ii].setTypeface(this.typeFace);
        }
        setOnFocusChangeListener(new C05528());
        this.enable_focus = true;
        this.showIndex = this.index;
        String timeout = "";
        if (MGplayer.isShowLefttime == 1) {
            if (Integer.parseInt(MGplayer.leftdays) == -1) {
                timeout = timeout + this._this.getString(C0216R.string.myhomebar_text7).toString() + ":" + this._this.getString(C0216R.string.myhomebar_text9).toString();
            } else {
                timeout = timeout + this._this.getString(C0216R.string.myhomebar_text7).toString() + ":" + MGplayer.leftdays + this._this.getString(C0216R.string.myhomebar_text8).toString();
            }
        }
        TextView leftdaytext = (TextView) findViewById(C0216R.id.leftday);
        leftdaytext.setTextSize(this.rate * 6.0f);
        leftdaytext.setTypeface(this.typeFace);
        leftdaytext.setText(timeout);
    }

    public void setInterface(ListViewInterface l) {
        this.iface = l;
    }

    public void selectIndex() {
        this.enable_focus = true;
        selectIndex(this.showIndex);
        this.index = this.showIndex;
    }

    public void selectIndex(int iex) {
        if (this.enable_focus) {
            for (int ii = 0; ii < this.texter.length; ii++) {
                if (iex == ii) {
                    this.layouter[ii].setBackgroundResource(C0216R.mipmap.se3);
                } else {
                    this.layouter[ii].setBackgroundColor(0);
                }
            }
            this.index = iex;
        }
    }

    public void selectFirstIndex(int iex) {
        this.index = iex;
        this.showIndex = iex;
        for (int ii = 0; ii < this.texter.length; ii++) {
            if (iex == ii) {
                this.layouter[ii].setBackgroundResource(C0216R.mipmap.se3);
            } else {
                this.layouter[ii].setBackgroundColor(0);
            }
        }
    }

    private void selectIndexNoFocus(int index) {
        if (this.enable_focus) {
            for (int ii = 0; ii < this.texter.length; ii++) {
                if (index == ii) {
                    this.layouter[ii].setBackgroundResource(C0216R.mipmap.se4);
                } else {
                    this.layouter[ii].setBackgroundColor(0);
                }
            }
        }
    }

    private void EnterEvent(int index) {
        if (this.enable_focus) {
            switch (index) {
                case 0:
                    if (VODplayer.columner != null && index < VODplayer.columner.length && VODplayer.columner[0].needps == 1) {
                        inputPasswordView("0", index);
                        return;
                    } else if (VODplayer.columner != null && index < VODplayer.columner.length && VODplayer.columner[0].needps == 0) {
                        this.showIndex = index;
                        this.iface.callback(0, "0");
                        return;
                    } else {
                        return;
                    }
                case 1:
                    if (VODplayer.columner != null && index < VODplayer.columner.length && VODplayer.columner[1].needps == 1) {
                        inputPasswordView("1", index);
                        return;
                    } else if (VODplayer.columner != null && index < VODplayer.columner.length && VODplayer.columner[1].needps == 0) {
                        this.showIndex = index;
                        this.iface.callback(0, "1");
                        return;
                    } else {
                        return;
                    }
                case 2:
                    if (VODplayer.columner != null && index < VODplayer.columner.length && VODplayer.columner[2].needps == 1) {
                        inputPasswordView("2", index);
                        return;
                    } else if (VODplayer.columner != null && index < VODplayer.columner.length && VODplayer.columner[2].needps == 0) {
                        this.showIndex = index;
                        this.iface.callback(0, "2");
                        return;
                    } else {
                        return;
                    }
                case 3:
                    if (VODplayer.columner != null && index < VODplayer.columner.length && VODplayer.columner[3].needps == 1) {
                        inputPasswordView("3", index);
                        return;
                    } else if (VODplayer.columner != null && index < VODplayer.columner.length && VODplayer.columner[3].needps == 0) {
                        this.showIndex = index;
                        this.iface.callback(0, "3");
                        return;
                    } else {
                        return;
                    }
                case 4:
                    this.showIndex = index;
                    this.iface.callback(1, "4");
                    return;
                case 5:
                    this.iface.callback(2, String.valueOf(this.showIndex));
                    return;
                case 6:
                    this.iface.callback(3, "6");
                    return;
                default:
                    return;
            }
        } else if (this.iface != null) {
            this.iface.callback(4, null);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MGplayer.MyPrintln("onKeyDown keyCode = " + keyCode);
        if (!this.enable_focus) {
            return true;
        }
        switch (keyCode) {
            case 19:
                this.index--;
                if (this.index < 0) {
                    this.index = this.layouter.length - 1;
                }
                selectIndex(this.index);
                break;
            case 20:
                this.index++;
                if (this.index >= this.layouter.length) {
                    this.index = 0;
                }
                selectIndex(this.index);
                break;
            case 21:
                MGplayer.MyPrintln("TypeView2 DPAD LEFT");
                if (this.index < 4) {
                    if (VODplayer.columner != null && this.index < VODplayer.columner.length && VODplayer.columner[this.index].needps == 1) {
                        inputClassifyPasswordView(5, this.index);
                        break;
                    }
                    this.iface.callback(5, null);
                    this.enable_focus = false;
                    break;
                }
                break;
            case 22:
                selectIndexNoFocus(this.showIndex);
                this.iface.callback(4, null);
                this.enable_focus = false;
                break;
            case 23:
            case 66:
                EnterEvent(this.index);
                break;
            case 82:
                MenuView.gridMenuInit(this._this);
                MenuView.showAlertDialog(this._this);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setFocus() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        requestFocusFromTouch();
    }

    public int getSelectIndex() {
        return this.index;
    }

    private void inputClassifyPasswordView(final int cmd, final int index) {
        Builder builder = new Builder(this._this);
        final View textEntryView = LayoutInflater.from(this._this).inflate(C0216R.layout.inputpasswordview, null);
        builder.setIcon(17301659);
        builder.setTitle(this._this.getString(C0216R.string.typelist_text2).toString());
        builder.setView(textEntryView);
        builder.setPositiveButton(this._this.getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String password = ((EditText) textEntryView.findViewById(C0216R.id.ps_editText)).getText().toString();
                String type_ps = MGplayer.MyGetSharedPreferences(MyVodTypeView2.this._this, "data", 0).getString("type_password", null);
                if (type_ps == null && VODplayer.columner != null) {
                    type_ps = VODplayer.columner[index].password;
                }
                if (!password.equals(type_ps) || VODplayer.columner == null) {
                    MyToast.makeText(MyVodTypeView2.this._this, MyVodTypeView2.this._this.getString(C0216R.string.typelist_text3).toString(), 0);
                    return;
                }
                MyVodTypeView2.this.iface.callback(cmd, null);
                VODplayer.columner[index].needps = 0;
                MyVodTypeView2.this.enable_focus = false;
                MyVodTypeView2.this.selectIndex(index);
            }
        });
        builder.setNegativeButton(this._this.getString(C0216R.string.cancel).toString(), null);
        builder.create().show();
    }

    private void inputPasswordView(final String id, final int index) {
        Builder builder = new Builder(this._this);
        final View textEntryView = LayoutInflater.from(this._this).inflate(C0216R.layout.inputpasswordview, null);
        builder.setIcon(17301659);
        builder.setTitle(this._this.getString(C0216R.string.typelist_text2).toString());
        builder.setView(textEntryView);
        builder.setPositiveButton(this._this.getString(C0216R.string.ok).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String password = ((EditText) textEntryView.findViewById(C0216R.id.ps_editText)).getText().toString();
                String type_ps = MGplayer.MyGetSharedPreferences(MyVodTypeView2.this._this, "data", 0).getString("type_password", null);
                if (type_ps == null && VODplayer.columner != null) {
                    type_ps = VODplayer.columner[Integer.parseInt(id)].password;
                }
                if (!password.equals(type_ps) || VODplayer.columner == null) {
                    MyToast.makeText(MyVodTypeView2.this._this, MyVodTypeView2.this._this.getString(C0216R.string.typelist_text3).toString(), 0);
                    return;
                }
                if (index >= 0) {
                    MyVodTypeView2.this.showIndex = index;
                }
                VODplayer.columner[Integer.parseInt(id)].needps = 0;
                MyVodTypeView2.this.iface.callback(0, id);
            }
        });
        builder.setNegativeButton(this._this.getString(C0216R.string.cancel).toString(), null);
        builder.create().show();
    }
}
