package com.gemini.play;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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
import com.gemini.play.MyDialog.Builder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MyLiveCollectView {
    private static final String DB_CREATE = "create table collectlive(_id integer primary key autoincrement, urlid text, image BLOB, url text, name text, password text, type text, source text, introid text)";
    private static final String DB_NAME = "collectlive.db";
    private static final String DB_TABLE = "collectlive";
    private static final int DB_VERSION = 1;
    private static AlertDialog dialog = null;
    private static Context mContext = null;
    private MySimpleAdapterLiveListView adapter = null;
    private ArrayList<HashMap<String, Object>> list = new ArrayList();
    private ListView listview;
    private DatabaseHelper mDatabaseHelper = null;
    private SQLiteDatabase mSQLiteDatabase = null;

    /* renamed from: com.gemini.play.MyLiveCollectView$2 */
    class C04252 implements OnFocusChangeListener {
        C04252() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyLiveCollectView$3 */
    class C04263 implements OnClickListener {
        C04263() {
        }

        public void onClick(View arg0) {
            MyLiveCollectView.dialog.hide();
        }
    }

    /* renamed from: com.gemini.play.MyLiveCollectView$4 */
    class C04274 implements OnFocusChangeListener {
        C04274() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.setBackgroundResource(C0216R.mipmap.bf);
            } else {
                v.setBackgroundResource(C0216R.mipmap.bof);
            }
        }
    }

    /* renamed from: com.gemini.play.MyLiveCollectView$7 */
    class C04327 implements ViewBinder {
        C04327() {
        }

        public boolean setViewValue(View arg0, Object arg1, String textRepresentation) {
            if (((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) == 0) {
                return false;
            }
            ((ImageView) arg0).setImageBitmap((Bitmap) arg1);
            return true;
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(MyLiveCollectView.DB_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists collect");
            onCreate(db);
        }
    }

    public void showView(final Context _this, final String id) {
        mContext = _this;
        View collectActivity = LayoutInflater.from(_this).inflate(C0216R.layout.livecollect, null);
        Button okbutton = (Button) collectActivity.findViewById(C0216R.id.collect_ok);
        Button cancelbutton = (Button) collectActivity.findViewById(C0216R.id.collect_cancel);
        this.listview = (ListView) collectActivity.findViewById(C0216R.id.collect_listView);
        this.listview.setSelector(new ColorDrawable(0));
        Typeface typeFace = MGplayer.getFontsType(_this);
        float rate = MGplayer.getFontsRate();
        okbutton.setTextSize(7.0f * rate);
        okbutton.setTypeface(typeFace);
        cancelbutton.setTextSize(7.0f * rate);
        cancelbutton.setTypeface(typeFace);
        okbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                MyLiveCollectView.this.save_collect(id);
                MyLiveCollectView.this.show_list();
            }
        });
        okbutton.setOnFocusChangeListener(new C04252());
        cancelbutton.setOnClickListener(new C04263());
        cancelbutton.setOnFocusChangeListener(new C04274());
        this.listview.setOnItemClickListener(new OnItemClickListener() {

            /* renamed from: com.gemini.play.MyLiveCollectView$5$2 */
            class C04292 implements DialogInterface.OnClickListener {
                C04292() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }

            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                final String id = ((HashMap) MyLiveCollectView.this.listview.getItemAtPosition(arg2)).get("ItemId").toString();
                Builder builder = new Builder(_this);
                builder.setMessage(_this.getString(C0216R.string.livecollect_text1).toString());
                builder.setPositiveButton(_this.getString(C0216R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (MGplayer.isNumeric(id)) {
                            MGplayer.MyPrintln("collect del num:" + id);
                            MyLiveCollectView.this.deleteData(Integer.parseInt(id));
                            MyLiveCollectView.this.show_list();
                            dialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton(_this.getString(C0216R.string.cancel).toString(), new C04292());
                builder.create().show();
            }
        });
        this.listview.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MyLiveCollectView.this.listview.setSelector(ContextCompat.getDrawable(_this, C0216R.mipmap.se));
                    return;
                }
                MGplayer.MyPrintln("listview onfocus");
                MyLiveCollectView.this.listview.setSelector(new ColorDrawable(0));
            }
        });
        dialog = new AlertDialog.Builder(_this).setView(collectActivity).create();
        dialog.getWindow().setLayout((MGplayer.screenWidth / 5) * 2, (MGplayer.screenHeight / 5) * 4);
        dialog.show();
        init_list();
        show_list();
    }

    private void save_collect(String id) {
        MGplayer.MyPrintln("save collect id = " + id);
        if (MGplayer.isNumeric(id) && insert(LIVEplayer.getStatus(Integer.parseInt(id))) == -1) {
            MyToast.makeText(mContext, mContext.getString(C0216R.string.collect_text2).toString(), 0);
        }
    }

    public void init_list() {
        this.adapter = new MySimpleAdapterLiveListView(mContext, this.list, C0216R.layout.listitem1, new String[]{"ItemView", "ItemId", "ItemTitle", "ItemView2"}, new int[]{C0216R.id.ItemView, C0216R.id.ItemId, C0216R.id.ItemTitle, C0216R.id.ItemView2});
        this.listview.setAdapter(this.adapter);
        this.adapter.setViewBinder(new C04327());
    }

    private void show_list() {
        ArrayList<UrlStatus> xlist = parseAll();
        if (xlist != null) {
            this.list.clear();
            for (int ii = 0; ii < xlist.size(); ii++) {
                UrlStatus s = (UrlStatus) xlist.get(ii);
                HashMap<String, Object> map = new HashMap();
                if (s.imagebit != null) {
                    map.put("ItemView", s.imagebit);
                } else {
                    map.put("ItemView", Integer.valueOf(C0216R.mipmap.ti));
                }
                map.put("ItemTitle", s.name);
                map.put("ItemId", String.valueOf(s.id));
                map.put("ItemUrl", s.url);
                map.put("ItemPassword", s.password);
                this.list.add(map);
            }
            this.adapter.notifyDataSetChanged();
        }
    }

    public void open() {
        this.mDatabaseHelper = new DatabaseHelper(mContext, DB_NAME, null, 1);
        this.mSQLiteDatabase = this.mDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        if (this.mDatabaseHelper != null) {
            this.mDatabaseHelper.close();
            this.mDatabaseHelper = null;
        }
    }

    public long insertData(String urlid, Bitmap image, String url, String name, String password, String type, String source, String introid) {
        ContentValues initialValues = new ContentValues();
        if (image != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            image.compress(CompressFormat.PNG, 100, os);
            initialValues.put("image", os.toByteArray());
        }
        initialValues.put("urlid", urlid);
        initialValues.put("url", url);
        initialValues.put("name", name);
        initialValues.put("password", password);
        initialValues.put("type", type);
        initialValues.put("source", source);
        initialValues.put("introid", introid);
        return this.mSQLiteDatabase.insert(DB_TABLE, "_id", initialValues);
    }

    public long inserDataNoreRepeat(String urlid, Bitmap image, String url, String name, String password, String type, String source, String introid) {
        Cursor cursor = fetchData(urlid);
        if (cursor == null || cursor.getCount() <= 0) {
            return insertData(urlid, image, url, name, password, type, source, introid);
        }
        return -1;
    }

    public boolean deleteData(int id) {
        boolean ret = true;
        open();
        String[] strArr = new String[1];
        strArr[0] = String.format("%03d", new Object[]{Integer.valueOf(id)});
        if (this.mSQLiteDatabase.delete(DB_TABLE, "urlid=?", strArr) <= 0) {
            ret = false;
        }
        close();
        return ret;
    }

    public Cursor fetchAllData() {
        return this.mSQLiteDatabase.query(DB_TABLE, new String[]{"_id", "urlid", "image", "url", "name", "password", "type", "source", "introid"}, null, null, null, null, null);
    }

    public Cursor fetchData(String id) throws SQLException {
        return this.mSQLiteDatabase.query(true, DB_TABLE, new String[]{"_id", "urlid", "image", "url", "name", "password", "type", "source", "introid"}, "urlid=?", new String[]{id}, null, null, null, null);
    }

    public boolean updateData(String urlid, Bitmap image, String url, String name, String password, String type, String source, String introid) {
        ContentValues initialValues = new ContentValues();
        if (image != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            image.compress(CompressFormat.PNG, 100, os);
            initialValues.put("image", os.toByteArray());
        }
        initialValues.put("urlid", urlid);
        initialValues.put("url", url);
        initialValues.put("name", name);
        initialValues.put("password", password);
        initialValues.put("type", type);
        initialValues.put("source", source);
        initialValues.put("introid", introid);
        return this.mSQLiteDatabase.update(DB_TABLE, initialValues, "urlid=?", new String[]{urlid}) > 0;
    }

    public UrlStatus get(Context _this, String id) {
        mContext = _this;
        open();
        Cursor cur = fetchAllData();
        if (cur == null) {
            close();
            return null;
        }
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            UrlStatus s = new UrlStatus();
            s.url = cur.getString(cur.getColumnIndex("url"));
            byte[] in = cur.getBlob(cur.getColumnIndex("image"));
            if (in != null) {
                s.imagebit = BitmapFactory.decodeByteArray(in, 0, in.length);
            }
            String getid = cur.getString(cur.getColumnIndex("urlid"));
            s.id = Integer.parseInt(getid);
            s.name = cur.getString(cur.getColumnIndex("name"));
            s.password = cur.getString(cur.getColumnIndex("password"));
            s.type = cur.getString(cur.getColumnIndex("type"));
            s.source = cur.getString(cur.getColumnIndex("source"));
            s.introid = cur.getString(cur.getColumnIndex("introid"));
            if (Integer.parseInt(getid) == Integer.parseInt(id)) {
                close();
                return s;
            }
            MGplayer.MyPrintln("collect get name " + s.name + " id " + s.id);
            cur.moveToNext();
        }
        close();
        return null;
    }

    public long insert(UrlStatus s) {
        open();
        long ret = inserDataNoreRepeat(String.format("%03d", new Object[]{Integer.valueOf(s.id)}), s.imagebit, s.url, s.name, s.password, s.type, s.source, s.introid);
        close();
        return ret;
    }

    public ArrayList<UrlStatus> parseAll() {
        return parseAll(mContext);
    }

    public ArrayList<UrlStatus> parseAll(Context _this) {
        mContext = _this;
        open();
        ArrayList<UrlStatus> collectArray = new ArrayList();
        Cursor cur = fetchAllData();
        if (cur == null) {
            close();
            return null;
        }
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            UrlStatus s = new UrlStatus();
            s.url = cur.getString(cur.getColumnIndex("url"));
            byte[] in = cur.getBlob(cur.getColumnIndex("image"));
            if (in != null) {
                s.imagebit = BitmapFactory.decodeByteArray(in, 0, in.length);
            }
            s.id = Integer.parseInt(cur.getString(cur.getColumnIndex("urlid")));
            s.name = cur.getString(cur.getColumnIndex("name"));
            s.password = cur.getString(cur.getColumnIndex("password"));
            s.type = cur.getString(cur.getColumnIndex("type"));
            s.source = cur.getString(cur.getColumnIndex("source"));
            s.introid = cur.getString(cur.getColumnIndex("introid"));
            collectArray.add(s);
            cur.moveToNext();
        }
        close();
        return collectArray;
    }
}
