package com.gemini.play;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;

public class MyLiveSettingView {
    private static String current_id = null;
    private static AlertDialog dialog = null;
    private static ListViewInterface iface = null;
    private static AlertDialog menuDialog;
    private static GridView menuGrid;
    private static View menuView;
    public static int[] menu_image_array = new int[]{C0216R.mipmap.lc, C0216R.mipmap.setting, C0216R.mipmap.back2};
    public static int[] menu_name_array = new int[]{C0216R.string.livesetting_text1, C0216R.string.livesetting_text2, C0216R.string.back2};
    public static ListViewInterface onLinePressed = new C09103();

    /* renamed from: com.gemini.play.MyLiveSettingView$1 */
    static class C04331 implements OnKeyListener {
        C04331() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 82) {
                dialog.dismiss();
            }
            return false;
        }
    }

    /* renamed from: com.gemini.play.MyLiveSettingView$3 */
    static class C09103 implements ListViewInterface {
        C09103() {
        }

        public void callback(int cmd, String data) {
            switch (cmd) {
                case 0:
                    MyLiveSettingView.iface.callback(cmd, data);
                    MyLiveSettingView.menuDialog.hide();
                    return;
                default:
                    return;
            }
        }
    }

    public static void init(final Context _this) {
        menuView = View.inflate(_this, C0216R.layout.menuview, null);
        menuDialog = new Builder(_this).create();
        menuDialog.setView(menuView);
        menuDialog.setOnKeyListener(new C04331());
        menuGrid = (GridView) menuView.findViewById(C0216R.id.gridview);
        menuGrid.setAdapter(getMenuAdapter(_this, menu_name_array, menu_image_array));
        menuGrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                switch (arg2) {
                    case 0:
                        MyLiveSettingView.collectActivity(_this);
                        return;
                    case 1:
                        MyLiveSettingView.lineActivity(_this);
                        return;
                    case 2:
                        MyLiveSettingView.menuDialog.hide();
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

    public static void showAlertDialog(Context _this, String id) {
        init(_this);
        current_id = id;
        MGplayer.MyPrintln("MyLiveSettingView current id = " + current_id);
        if (menuDialog == null) {
            menuDialog = new Builder(_this).setView(menuView).show();
        } else {
            menuDialog.show();
        }
    }

    private static void collectActivity(Context _this) {
        if (current_id != null) {
            new MyLiveCollectView().showView(_this, current_id);
        }
    }

    private static void lineActivity(Context _this) {
        if (current_id != null) {
            MyLineView liner = new MyLineView();
            liner.initView(_this);
            liner.setInterface(onLinePressed);
            liner.showView(_this, current_id);
        }
    }

    public static void setInterface(ListViewInterface l) {
        iface = l;
    }
}
