package com.gemini.play;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import com.gemini.kvod2.C0216R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppsActivity extends Activity {
    private List<ResolveInfo> mApps = null;
    private GridView mGrid = null;
    private OnItemClickListener mListener = null;

    /* renamed from: com.gemini.play.AppsActivity$1 */
    class C02401 implements OnItemClickListener {
        C02401() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int pint, long plong) {
            ResolveInfo info = (ResolveInfo) AppsActivity.this.mApps.get(pint);
            ComponentName name = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
            Intent intent = new Intent();
            intent.setComponent(name);
            AppsActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.gemini.play.AppsActivity$2 */
    class C02412 implements ViewBinder {
        C02412() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0216R.layout.appslayout);
        GridView gridview = (GridView) findViewById(C0216R.id.gridview);
        Intent intent = new Intent("android.intent.action.MAIN", null);
        intent.addCategory("android.intent.category.LAUNCHER");
        this.mApps = getPackageManager().queryIntentActivities(intent, 0);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList();
        for (int i = 0; i < this.mApps.size(); i++) {
            HashMap<String, Object> map = new HashMap();
            ResolveInfo info = (ResolveInfo) this.mApps.get(i);
            map.put("ItemImage", ((BitmapDrawable) info.activityInfo.loadIcon(getPackageManager())).getBitmap());
            map.put("ItemText", info.activityInfo.loadLabel(getPackageManager()));
            lstImageItem.add(map);
        }
        SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem, C0216R.layout.appsitem, new String[]{"ItemImage", "ItemText"}, new int[]{C0216R.id.ItemImage, C0216R.id.ItemText});
        this.mListener = new C02401();
        gridview.setOnItemClickListener(this.mListener);
        saImageItems.setViewBinder(new C02412());
        gridview.setAdapter(saImageItems);
    }
}
