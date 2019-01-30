package com.gemini.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.gemini.kvod2.C0216R;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MenuView {
    private static AlertDialog dialog = null;
    public static int getUnbuildling = -1;
    private static boolean isMore = false;
    static final Handler m_handler = new C03374();
    private static AlertDialog menuDialog;
    private static GridView menuGrid;
    private static View menuView;
    public static int[] menu_image_array = new int[]{C0216R.mipmap.collect, C0216R.mipmap.input, C0216R.mipmap.update, C0216R.mipmap.net, C0216R.mipmap.font, C0216R.mipmap.apps, C0216R.mipmap.num, C0216R.mipmap.about, C0216R.mipmap.feedback, C0216R.mipmap.recharge, C0216R.mipmap.back2};
    public static int[] menu_image_array_quanxing = new int[]{C0216R.mipmap.input, C0216R.mipmap.update, C0216R.mipmap.net, C0216R.mipmap.font, C0216R.mipmap.apps, C0216R.mipmap.about, C0216R.mipmap.back2};
    public static int[] menu_name_array = new int[]{C0216R.string.menu_text1, C0216R.string.menu_text2, C0216R.string.menu_text3, C0216R.string.menu_text4, C0216R.string.menu_text5, C0216R.string.menu_text8, C0216R.string.menu_text9, C0216R.string.menu_text6, C0216R.string.menu_text11, C0216R.string.menu_text14, C0216R.string.menu_text7};
    public static int[] menu_name_array_quanxing = new int[]{C0216R.string.menu_text2, C0216R.string.menu_text3, C0216R.string.menu_text4, C0216R.string.menu_text5, C0216R.string.menu_text8, C0216R.string.menu_text6, C0216R.string.menu_text7};
    private final int ITEM_0 = 0;
    private final int ITEM_1 = 1;
    private final int ITEM_2 = 2;
    private final int ITEM_3 = 3;
    private final int ITEM_4 = 4;

    /* renamed from: com.gemini.play.MenuView$1 */
    static class C03311 implements OnKeyListener {
        C03311() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 82) {
                dialog.dismiss();
            }
            return false;
        }
    }

    /* renamed from: com.gemini.play.MenuView$4 */
    static class C03374 extends Handler {
        C03374() {
        }

        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                Date d = new Date();
                try {
                    MGplayer.upLoadByAsyncHttpClient("http://www.gemini-iptv.com/debug/debug.php", "debug-" + MGplayer.tv.GetMac().replace(":", "") + "-" + new SimpleDateFormat("yyyy-MM-dd").format(d) + ".txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.gemini.play.MenuView$5 */
    static class C03385 extends Thread {
        C03385() {
        }

        public void run() {
            Message msg = MenuView.m_handler.obtainMessage();
            msg.arg1 = 0;
            MenuView.m_handler.sendMessage(msg);
        }
    }

    public static void gridMenuInit(Context _this) {
        gridMenuInit(_this, 1);
    }

    public static void gridMenuInit(final Context _this, int setkey) {
        menuView = View.inflate(_this, C0216R.layout.menuview, null);
        menuDialog = new Builder(_this).create();
        menuDialog.setView(menuView);
        if (setkey == 1) {
            menuDialog.setOnKeyListener(new C03311());
        }
        menuGrid = (GridView) menuView.findViewById(C0216R.id.gridview);
        if (MGplayer.custom().equals("quanxing")) {
            menuGrid.setAdapter(getMenuAdapter(_this, menu_name_array_quanxing, menu_image_array_quanxing));
            menuGrid.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    switch (arg2) {
                        case 0:
                            MenuView.decodeActivity(_this);
                            return;
                        case 1:
                            MenuView.updateActivity(_this);
                            return;
                        case 2:
                            _this.startActivity(new Intent("android.settings.SETTINGS"));
                            return;
                        case 3:
                            MenuView.fontActivity(_this);
                            return;
                        case 4:
                            MenuView.appsActivity(_this);
                            return;
                        case 5:
                            MenuView.aboutActivity(_this);
                            return;
                        case 6:
                            MenuView.menuDialog.hide();
                            return;
                        default:
                            return;
                    }
                }
            });
            return;
        }
        menuGrid.setAdapter(getMenuAdapter(_this, menu_name_array, menu_image_array));
        menuGrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                switch (arg2) {
                    case 0:
                        final EditText input = new EditText(_this);
                        input.setInputType(129);
                        new Builder(_this).setTitle("").setView(input).setPositiveButton(_this.getString(C0216R.string.ok), new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (input.getText().toString().equals("11223300")) {
                                    MenuView.serverActivity(_this);
                                } else {
                                    Toast.makeText(_this, "", 0).show();
                                }
                            }
                        }).setNegativeButton(_this.getString(C0216R.string.cancel), null).create().show();
                        return;
                    case 1:
                        MenuView.decodeActivity(_this);
                        return;
                    case 2:
                        MenuView.updateActivity(_this);
                        return;
                    case 3:
                        _this.startActivity(new Intent("android.settings.SETTINGS"));
                        return;
                    case 4:
                        MenuView.fontActivity(_this);
                        return;
                    case 5:
                        MenuView.appsActivity(_this);
                        return;
                    case 6:
                        MenuView.unbundlingActivity(_this);
                        return;
                    case 7:
                        MenuView.aboutActivity(_this);
                        return;
                    case 8:
                        MenuView.feedbackActivity(_this);
                        return;
                    case 9:
                        MenuView.reChargeActivity(_this);
                        return;
                    case 10:
                        MenuView.menuDialog.hide();
                        return;
                    default:
                        return;
                }
            }
        });
    }

    private static SimpleAdapter getMenuAdapter(Context _this, int[] menuNameArray, int[] imageResourceArray) {
        ArrayList<HashMap<String, Object>> data = new ArrayList();
        for (int i = 0; i < menuNameArray.length; i++) {
            HashMap<String, Object> map = new HashMap();
            map.put("itemImage", Integer.valueOf(imageResourceArray[i]));
            map.put("itemText", _this.getString(menuNameArray[i]).toString());
            data.add(map);
        }
        return new SimpleAdapter(_this, data, C0216R.layout.menuitem, new String[]{"itemImage", "itemText"}, new int[]{C0216R.id.item_image, C0216R.id.item_text});
    }

    public static void feedbackActivity(Context _this) {
        new C03385().start();
        MyToast.makeText(_this, "Upload Successful", 0);
    }

    public static void showAlertDialog(Context _this) {
        if (menuDialog == null) {
            menuDialog = new Builder(_this).setView(menuView).show();
        } else {
            menuDialog.show();
        }
    }

    public static boolean isShow() {
        return menuDialog.isShowing();
    }

    private static void updateActivity(Context _this) {
        UpdateActivity.startUpdate((Activity) _this, null);
    }

    private static void fontActivity(Context _this) {
        int progressValue = MGplayer.MyGetSharedPreferences(_this, "data", 0).getInt("fontsize", 100);
        View fontActivity = LayoutInflater.from(_this).inflate(C0216R.layout.fontview, null);
        final SeekBar fontSeekBar = (SeekBar) fontActivity.findViewById(C0216R.id.fontSeekBar);
        fontSeekBar.setMax(200);
        fontSeekBar.setProgress(progressValue);
        final TextView fontTextView = (TextView) fontActivity.findViewById(C0216R.id.fontText);
        final TextView sizeTextView = (TextView) fontActivity.findViewById(C0216R.id.sizeText);
        fontTextView.setTypeface(MGplayer.getFontsType(_this));
        final float rate = MGplayer.getFontsRate();
        fontTextView.setTextSize((10.0f * rate) * (((float) progressValue) / 100.0f));
        sizeTextView.setText(progressValue + "%");
        sizeTextView.setTextSize(10.0f * rate);
        final Context context = _this;
        fontSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MGplayer.MyPrintln("fontsize:" + progress);
                if (progress < 30) {
                    fontSeekBar.setProgress(30);
                }
                Editor editor = MGplayer.MyGetSharedPreferences(context, "data", 0).edit();
                editor.putInt("fontsize", progress);
                editor.commit();
                fontTextView.setTextSize((12.0f * rate) * (((float) progress) / 100.0f));
                sizeTextView.setText(progress + "%");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        dialog = new Builder(_this).setView(fontActivity).show();
        Window window = dialog.getWindow();
        MGplayer mGplayer = MGplayer.tv;
        int i = (MGplayer.screenWidth / 5) * 2;
        MGplayer mGplayer2 = MGplayer.tv;
        window.setLayout(i, (MGplayer.screenHeight / 5) * 2);
    }

    private static void serverActivity(final Context _this) {
        View serverActivity = LayoutInflater.from(_this).inflate(C0216R.layout.serverview, null);
        final EditText serverEditText = (EditText) serverActivity.findViewById(C0216R.id.serverset_edit);
        TextView serverTextView = (TextView) serverActivity.findViewById(C0216R.id.serverset_text);
        Typeface typeFace = MGplayer.getFontsType(_this);
        float rate = MGplayer.getFontsRate();
        serverEditText.setTextSize(12.0f * rate);
        serverEditText.setTypeface(typeFace);
        serverTextView.setTextSize(12.0f * rate);
        serverTextView.setTypeface(typeFace);
        final Button okButton = (Button) serverActivity.findViewById(C0216R.id.positiveButton);
        final Button cancelButton = (Button) serverActivity.findViewById(C0216R.id.negativeButton);
        okButton.setTextSize(8.0f * rate);
        okButton.setTypeface(typeFace);
        cancelButton.setTextSize(8.0f * rate);
        cancelButton.setTypeface(typeFace);
        okButton.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    okButton.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    okButton.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
        cancelButton.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cancelButton.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    cancelButton.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
        String inx = MGplayer.MyGetSharedPreferences(_this, "data", 0).getString("server", null);
        if (inx == null) {
            serverEditText.setText(MGplayer.tv.gete());
        } else {
            serverEditText.setText(inx);
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inx = serverEditText.getText().toString();
                Editor editor = MGplayer.MyGetSharedPreferences(_this, "data", 0).edit();
                editor.putString("server", inx);
                editor.commit();
                MenuView.dialog.hide();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MenuView.dialog.hide();
            }
        });
        dialog = new Builder(_this).setView(serverActivity).show();
        Window window = dialog.getWindow();
        MGplayer mGplayer = MGplayer.tv;
        int i = (MGplayer.screenWidth / 5) * 4;
        MGplayer mGplayer2 = MGplayer.tv;
        window.setLayout(i, (MGplayer.screenHeight / 5) * 4);
    }

    private static void aboutActivity(Context _this) {
        if (MGplayer.custom().equals("quanxing")) {
            Intent intent = new Intent();
            intent.setClass(_this, SetWebview.class);
            _this.startActivity(intent);
            return;
        }
        intent = new Intent();
        intent.setClass(_this, AboutActivity.class);
        _this.startActivity(intent);
    }

    private static void appsActivity(Context _this) {
        Intent intent = new Intent();
        intent.setClass(_this, AppsActivity.class);
        _this.startActivity(intent);
    }

    public static void decodeActivity(Context _this) {
        decodeActivity(_this, null);
    }

    public static void decodeActivity(final Context _this, final Handler rHandler) {
        View decodeActivity = LayoutInflater.from(_this).inflate(C0216R.layout.decodeview, null);
        RadioGroup radiogroup = (RadioGroup) decodeActivity.findViewById(C0216R.id.RadioGroup);
        RadioButton radiobutton1 = (RadioButton) decodeActivity.findViewById(C0216R.id.RadioDecode1);
        RadioButton radiobutton4 = (RadioButton) decodeActivity.findViewById(C0216R.id.RadioDecode4);
        RadioButton radiobutton2 = (RadioButton) decodeActivity.findViewById(C0216R.id.RadioDecode2);
        RadioButton radiobutton3 = (RadioButton) decodeActivity.findViewById(C0216R.id.RadioDecode3);
        Typeface typeFace = MGplayer.getFontsType(_this);
        float rate = MGplayer.getFontsRate();
        radiobutton1.setTextSize(8.0f * rate);
        radiobutton1.setTypeface(typeFace);
        radiobutton2.setTextSize(8.0f * rate);
        radiobutton2.setTypeface(typeFace);
        radiobutton3.setTextSize(8.0f * rate);
        radiobutton3.setTypeface(typeFace);
        radiobutton4.setTextSize(8.0f * rate);
        radiobutton4.setTypeface(typeFace);
        final int inx = MGplayer.MyGetSharedPreferences(_this, "data", 0).getInt("decode", 0);
        if (inx == 0 || inx == 3) {
            radiobutton1.setChecked(true);
            radiobutton1.setFocusable(true);
            radiobutton1.requestFocus();
        } else if (inx == 1) {
            radiobutton2.setChecked(true);
            radiobutton2.setFocusable(true);
            radiobutton2.requestFocus();
        } else if (inx == 2) {
            radiobutton3.setChecked(true);
            radiobutton3.setFocusable(true);
            radiobutton3.requestFocus();
        }
        radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int radioButtonId = arg0.getCheckedRadioButtonId();
                Editor editor = MGplayer.MyGetSharedPreferences(_this, "data", 0).edit();
                if (rHandler != null) {
                    MyToast.makeText(_this.getApplicationContext(), _this.getString(C0216R.string.menu_text12), 1);
                } else {
                    MyToast.makeText(_this.getApplicationContext(), _this.getString(C0216R.string.menu_text10), 1);
                }
                switch (radioButtonId) {
                    case C0216R.id.RadioDecode1:
                        editor.putInt("decode", 0);
                        editor.commit();
                        break;
                    case C0216R.id.RadioDecode2:
                        editor.putInt("decode", 1);
                        editor.commit();
                        break;
                    case C0216R.id.RadioDecode3:
                        editor.putInt("decode", 2);
                        editor.commit();
                        break;
                    case C0216R.id.RadioDecode4:
                        editor.putInt("decode", 3);
                        editor.commit();
                        break;
                }
                if (rHandler != null) {
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putInt("inx", inx);
                    msg.setData(data);
                    msg.what = 12;
                    rHandler.sendMessageDelayed(msg, 500);
                }
            }
        });
        dialog = new Builder(_this).setView(decodeActivity).show();
        Window window = dialog.getWindow();
        MGplayer mGplayer = MGplayer.tv;
        int i = (MGplayer.screenWidth / 4) * 3;
        MGplayer mGplayer2 = MGplayer.tv;
        window.setLayout(i, (MGplayer.screenHeight / 5) * 4);
    }

    private static void unbundlingActivity(Context _this) {
        View unbundlingActivity = LayoutInflater.from(_this).inflate(C0216R.layout.unbundlingview, null);
        TextView title = (TextView) unbundlingActivity.findViewById(C0216R.id.unbundling_title);
        TextView text1 = (TextView) unbundlingActivity.findViewById(C0216R.id.unbundling_text1);
        TextView text2 = (TextView) unbundlingActivity.findViewById(C0216R.id.unbundling_text2);
        TextView text3 = (TextView) unbundlingActivity.findViewById(C0216R.id.unbundling_text3);
        final TextView text4 = (TextView) unbundlingActivity.findViewById(C0216R.id.unbundling_text4);
        Typeface typeFace = MGplayer.getFontsType(_this);
        float rate = MGplayer.getFontsRate();
        title.setTextSize(8.0f * rate);
        title.setTypeface(typeFace);
        text1.setTextSize(8.0f * rate);
        text1.setTypeface(typeFace);
        text2.setTextSize(8.0f * rate);
        text2.setTypeface(typeFace);
        text2.setText(MGplayer.number);
        text3.setTextSize(8.0f * rate);
        text3.setTypeface(typeFace);
        text4.setTextSize(8.0f * rate);
        text4.setTypeface(typeFace);
        final Button okButton = (Button) unbundlingActivity.findViewById(C0216R.id.positiveButton);
        final Button cancelButton = (Button) unbundlingActivity.findViewById(C0216R.id.negativeButton);
        okButton.setTextSize(8.0f * rate);
        okButton.setTypeface(typeFace);
        cancelButton.setTextSize(8.0f * rate);
        cancelButton.setTypeface(typeFace);
        cancelButton.setFocusable(true);
        cancelButton.setFocusableInTouchMode(true);
        final Context context = _this;
        final Handler pHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        String ret = msg.getData().getString("ret");
                        MGplayer.MyPrintln("1 unbundling:" + ret);
                        if (ret.trim().indexOf("unbundling") >= 0) {
                            MGplayer.MyPrintln("2 unbundling:" + ret);
                            text4.setText(context.getString(C0216R.string.unbundling_text6));
                            okButton.setText(context.getString(C0216R.string.unbundling_text9));
                            MenuView.getUnbuildling = 0;
                            return;
                        } else if (ret.trim().indexOf("cannot") >= 0) {
                            MGplayer.MyPrintln("3 unbundling:" + ret);
                            text4.setText(context.getString(C0216R.string.unbundling_text10));
                            okButton.setText(context.getString(C0216R.string.unbundling_text6));
                            MenuView.getUnbuildling = 0;
                            return;
                        } else {
                            text4.setText(context.getString(C0216R.string.unbundling_text9));
                            okButton.setText(context.getString(C0216R.string.unbundling_text6));
                            MenuView.getUnbuildling = 1;
                            return;
                        }
                    default:
                        return;
                }
            }
        };
        okButton.setOnClickListener(new View.OnClickListener() {

            /* renamed from: com.gemini.play.MenuView$13$1 */
            class C03291 extends Thread {
                C03291() {
                }

                public void run() {
                    String cmd = null;
                    if (MenuView.getUnbuildling == 0) {
                        cmd = MGplayer.key(MGplayer.tv.GetMac() + "#" + MGplayer.tv.getCpuID() + "#" + 1 + "#" + MGplayer.seconds_prc);
                    } else if (MenuView.getUnbuildling == 1) {
                        cmd = MGplayer.key(MGplayer.tv.GetMac() + "#" + MGplayer.tv.getCpuID() + "#" + 0 + "#" + MGplayer.seconds_prc);
                    }
                    String ret = MGplayer.sendServerCmd(MGplayer.tv.gete() + MGplayer.admindir + "/unbundling_send.php?cmd=" + cmd + MGplayer.get_key_value());
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("ret", ret);
                    msg.setData(data);
                    msg.what = 0;
                    pHandler.sendMessage(msg);
                }
            }

            public void onClick(View v) {
                new C03291().start();
            }
        });
        okButton.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    okButton.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    okButton.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MenuView.dialog.hide();
            }
        });
        cancelButton.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cancelButton.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    cancelButton.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
        new Thread() {
            public void run() {
                String ret = MGplayer.sendServerCmd(MGplayer.tv.gete() + MGplayer.admindir + "/unbundling_send.php?cmd=" + MGplayer.key(MGplayer.tv.GetMac() + "#" + MGplayer.tv.getCpuID() + "#" + 2 + "#" + MGplayer.seconds_prc) + MGplayer.get_key_value());
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("ret", ret);
                msg.setData(data);
                msg.what = 0;
                pHandler.sendMessage(msg);
            }
        }.start();
        dialog = new Builder(_this).setView(unbundlingActivity).show();
        dialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == 0) {
                    MGplayer.MyPrintln("KeyEvent KEYCODE_DPAD");
                    switch (keyCode) {
                        case 21:
                            MGplayer.MyPrintln("KeyEvent KEYCODE_DPAD_LEFT");
                            okButton.setFocusable(true);
                            okButton.setFocusableInTouchMode(true);
                            okButton.requestFocus();
                            okButton.requestFocusFromTouch();
                            break;
                        case 22:
                            MGplayer.MyPrintln("KeyEvent KEYCODE_DPAD_RIGHT");
                            cancelButton.setFocusable(true);
                            cancelButton.setFocusableInTouchMode(true);
                            cancelButton.requestFocus();
                            cancelButton.requestFocusFromTouch();
                            break;
                    }
                }
                return false;
            }
        });
        Window window = dialog.getWindow();
        MGplayer mGplayer = MGplayer.tv;
        int i = (MGplayer.screenWidth / 5) * 4;
        MGplayer mGplayer2 = MGplayer.tv;
        window.setLayout(i, (MGplayer.screenHeight / 5) * 4);
    }

    private static void reChargeActivity(final Context _this) {
        View rechargeActivity = LayoutInflater.from(_this).inflate(C0216R.layout.recharge, null);
        TextView rechargeText = (TextView) rechargeActivity.findViewById(C0216R.id.rechargeText);
        final EditText rechargeEdit = (EditText) rechargeActivity.findViewById(C0216R.id.rechargeEdit);
        TextView leftdatext = (TextView) rechargeActivity.findViewById(C0216R.id.leftdatext);
        final TextView leftday = (TextView) rechargeActivity.findViewById(C0216R.id.leftday);
        Typeface typeFace = MGplayer.getFontsType(_this);
        float rate = MGplayer.getFontsRate();
        rechargeText.setTextSize(8.0f * rate);
        rechargeText.setTypeface(typeFace);
        rechargeEdit.setTextSize(8.0f * rate);
        rechargeEdit.setTypeface(typeFace);
        leftdatext.setTextSize(8.0f * rate);
        leftdatext.setTypeface(typeFace);
        leftday.setTextSize(8.0f * rate);
        leftday.setTypeface(typeFace);
        if (MGplayer.isShowLefttime == 1) {
            String timeout = "";
            if (Integer.parseInt(MGplayer.leftdays) == -1) {
                timeout = timeout + "    " + _this.getString(C0216R.string.myhomebar_text9).toString();
            } else {
                timeout = timeout + "    " + MGplayer.leftdays + _this.getString(C0216R.string.myhomebar_text8).toString();
            }
            leftday.setText(timeout);
        }
        final Button okButton = (Button) rechargeActivity.findViewById(C0216R.id.positiveButton);
        final Button cancelButton = (Button) rechargeActivity.findViewById(C0216R.id.negativeButton);
        okButton.setTextSize(8.0f * rate);
        okButton.setTypeface(typeFace);
        cancelButton.setTextSize(8.0f * rate);
        cancelButton.setTypeface(typeFace);
        cancelButton.setFocusable(true);
        cancelButton.setFocusableInTouchMode(true);
        final Handler pHandler = new Handler() {
            String ret = null;

            /* renamed from: com.gemini.play.MenuView$19$1 */
            class C03301 implements OnClickListener {
                C03301() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        this.ret = _this.getString(C0216R.string.menu_text15).toString();
                        break;
                    case 1:
                        this.ret = _this.getString(C0216R.string.menu_text16).toString();
                        break;
                    case 2:
                        this.ret = _this.getString(C0216R.string.menu_text17).toString();
                        break;
                    case 3:
                        this.ret = _this.getString(C0216R.string.menu_text18).toString();
                        break;
                    case 4:
                        this.ret = _this.getString(C0216R.string.menu_text19).toString();
                        break;
                    case 5:
                        this.ret = _this.getString(C0216R.string.menu_text22).toString();
                        break;
                    case 6:
                        this.ret = _this.getString(C0216R.string.menu_text20).toString();
                        break;
                    case 7:
                        this.ret = _this.getString(C0216R.string.menu_text21).toString();
                        break;
                }
                String timeout = "";
                if (Integer.parseInt(MGplayer.leftdays) == -1) {
                    timeout = timeout + "    " + _this.getString(C0216R.string.myhomebar_text9).toString();
                } else {
                    timeout = timeout + "    " + MGplayer.leftdays + _this.getString(C0216R.string.myhomebar_text8).toString();
                }
                leftday.setText(timeout);
                if (MGplayer.boot_launcher) {
                    Launcher2Activity.setLeftView();
                }
                new MyDialog2.Builder(_this).setTitle(_this.getString(C0216R.string.validate_text1).toString()).setMessage(this.ret).setPositiveButton(17039370, new C03301()).create().show();
            }
        };
        okButton.setOnClickListener(new View.OnClickListener() {

            /* renamed from: com.gemini.play.MenuView$20$1 */
            class C03321 implements OnClickListener {
                C03321() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }

            public void onClick(View v) {
                final String number = rechargeEdit.getText().toString();
                if (number.length() != 16) {
                    new MyDialog2.Builder(_this).setTitle(_this.getString(C0216R.string.validate_text1).toString()).setMessage(_this.getString(C0216R.string.menu_text23).toString()).setPositiveButton(17039370, new C03321()).create().show();
                } else {
                    new Thread() {
                        public void run() {
                            String ret = MGplayer.sendServerCmd(MGplayer.tv.gete() + "recharge.php?mac=" + MGplayer.tv.GetMac() + "&cpuid=" + MGplayer.tv.getCpuID() + "&number=" + number);
                            Message msg = new Message();
                            if (ret.contains("recharge memcached close")) {
                                msg.what = 0;
                            } else if (ret.contains("recharge wait 30s")) {
                                msg.what = 1;
                            } else if (ret.contains("recharge number noexist")) {
                                msg.what = 2;
                            } else if (ret.contains("recharge number used")) {
                                msg.what = 3;
                            } else if (ret.contains("recharge terminal noexist")) {
                                msg.what = 4;
                            } else if (ret.contains("recharge playlist different")) {
                                msg.what = 5;
                            } else if (ret.contains("recharge allow no")) {
                                msg.what = 6;
                            } else if (ret.contains("recharge ok left")) {
                                msg.what = 7;
                                String leftdays = ret.substring(ret.indexOf(":") + 1).trim();
                                if (MGplayer.isNumeric(leftdays)) {
                                    MGplayer.leftdays = leftdays;
                                }
                                MGplayer.MyPrintln("left:" + MGplayer.leftdays);
                            }
                            pHandler.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });
        okButton.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    okButton.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    okButton.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MenuView.dialog.hide();
            }
        });
        cancelButton.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cancelButton.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    cancelButton.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        });
        dialog = new Builder(_this).setView(rechargeActivity).show();
        dialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == 0) {
                    MGplayer.MyPrintln("KeyEvent KEYCODE_DPAD");
                    switch (keyCode) {
                        case 21:
                            MGplayer.MyPrintln("KeyEvent KEYCODE_DPAD_LEFT");
                            okButton.setFocusable(true);
                            okButton.setFocusableInTouchMode(true);
                            okButton.requestFocus();
                            okButton.requestFocusFromTouch();
                            break;
                        case 22:
                            MGplayer.MyPrintln("KeyEvent KEYCODE_DPAD_RIGHT");
                            cancelButton.setFocusable(true);
                            cancelButton.setFocusableInTouchMode(true);
                            cancelButton.requestFocus();
                            cancelButton.requestFocusFromTouch();
                            break;
                    }
                }
                return false;
            }
        });
        Window window = dialog.getWindow();
        MGplayer mGplayer = MGplayer.tv;
        int i = (MGplayer.screenWidth / 5) * 4;
        MGplayer mGplayer2 = MGplayer.tv;
        window.setLayout(i, (MGplayer.screenHeight / 5) * 4);
    }
}
