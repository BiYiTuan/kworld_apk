package com.gemini.play;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import io.vov.vitamio.provider.MediaStore.Audio.AudioColumns;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CollectVodDB {
    private static String DB_CREATE = ("create table " + DB_TABLE + "(_id integer primary key autoincrement, image BLOB, imageurl text, url text, name text, area text, year text, type text, intro1 text, intro2 text, intro3 text, intro4 text, vodid integer, clickrate integer, recommend integer, chage real, updatetime int, infotype int)");
    private static String DB_NAME = "collectvod.db";
    private static String DB_TABLE = "collectvod";
    private static final int DB_VERSION = 1;
    private Context mContext = null;
    private DatabaseHelper mDatabaseHelper = null;
    private SQLiteDatabase mSQLiteDatabase = null;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CollectVodDB.DB_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists collect");
            onCreate(db);
        }
    }

    public CollectVodDB(Context context) {
        if (MGplayer.custom().equals("msiptv")) {
            DB_NAME = "collectvod_msiptv.db";
            DB_TABLE = "collectvod_msiptv";
            DB_CREATE = "create table " + DB_TABLE + "(_id integer primary key autoincrement, image BLOB, imageurl text, url text, name text, area text, year text, type text, intro1 text, intro2 text, intro3 text, intro4 text, vodid integer, clickrate integer, recommend integer, chage real, updatetime int, infotype int)";
        }
        this.mContext = context;
    }

    public void open() {
        if (this.mDatabaseHelper == null) {
            this.mDatabaseHelper = new DatabaseHelper(this.mContext, DB_NAME, null, 1);
            this.mSQLiteDatabase = this.mDatabaseHelper.getWritableDatabase();
        }
    }

    public void close() {
        if (this.mDatabaseHelper != null) {
            this.mDatabaseHelper.close();
            this.mDatabaseHelper = null;
        }
    }

    public void clear() {
        open();
        if (this.mSQLiteDatabase != null) {
            this.mSQLiteDatabase.execSQL("delete from " + DB_TABLE);
        }
        close();
    }

    public long insertData(Bitmap image, String imageurl, String url, String name, String area, String year, String type, String intro1, String intro2, String intro3, String intro4, int id, int clickrate, int recommend, float chage, int updatetime, int info_type) {
        ContentValues initialValues = new ContentValues();
        if (image != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            image.compress(CompressFormat.PNG, 100, os);
            initialValues.put("image", os.toByteArray());
        }
        initialValues.put("imageurl", imageurl);
        initialValues.put("url", url);
        initialValues.put("name", name);
        initialValues.put("area", area);
        initialValues.put(AudioColumns.YEAR, year);
        initialValues.put("type", type);
        initialValues.put("intro1", intro1);
        initialValues.put("intro2", intro2);
        initialValues.put("intro3", intro3);
        initialValues.put("intro4", intro4);
        initialValues.put("vodid", Integer.valueOf(id));
        initialValues.put("clickrate", Integer.valueOf(clickrate));
        initialValues.put("recommend", Integer.valueOf(recommend));
        initialValues.put("chage", Float.valueOf(chage));
        initialValues.put("updatetime", Integer.valueOf(updatetime));
        initialValues.put("infotype", Integer.valueOf(info_type));
        return this.mSQLiteDatabase.insert(DB_TABLE, "_id", initialValues);
    }

    public long inserDataNoreRepeat(Bitmap image, String imageurl, String url, String name, String area, String year, String type, String intro1, String intro2, String intro3, String intro4, int id, int clickrate, int recommend, float chage, int updatetime, int infotype) {
        Cursor cursor = fetchData(id, infotype);
        if (cursor != null && cursor.getCount() > 0) {
            return 0;
        }
        long ret = insertData(image, imageurl, url, name, area, year, type, intro1, intro2, intro3, intro4, id, clickrate, recommend, chage, updatetime, infotype);
        if (this.mSQLiteDatabase == null) {
            return ret;
        }
        this.mSQLiteDatabase.execSQL("delete from " + DB_TABLE + " where (select count(name) from " + DB_TABLE + ")> 100 and name in (select name from " + DB_TABLE + " order by _id desc limit (select count(name) from " + DB_TABLE + ") offset 100)");
        return ret;
    }

    public boolean deleteData(int id) {
        boolean value = true;
        open();
        if (this.mSQLiteDatabase.delete(DB_TABLE, "vodid=?", new String[]{String.valueOf(id)}) <= 0) {
            value = false;
        }
        close();
        return value;
    }

    public Cursor fetchAllData() {
        return this.mSQLiteDatabase.query(DB_TABLE, new String[]{"_id", "image", "imageurl", "url", "name", "area", AudioColumns.YEAR, "type", "intro1", "intro2", "intro3", "intro4", "vodid", "clickrate", "recommend", "chage", "updatetime", "infotype"}, null, null, null, null, null);
    }

    public Cursor fetchData(int id, int infotype) throws SQLException {
        Cursor mCursor = this.mSQLiteDatabase.query(true, DB_TABLE, new String[]{"_id", "image", "imageurl", "url", "name", "area", AudioColumns.YEAR, "type", "intro1", "intro2", "intro3", "intro4", "vodid", "clickrate", "recommend", "chage", "updatetime", "infotype"}, "vodid=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                if (mCursor.getInt(mCursor.getColumnIndex("infotype")) == infotype) {
                    return mCursor;
                }
                mCursor.moveToNext();
            }
        }
        return null;
    }

    public boolean updateData(Bitmap image, String imageurl, String url, String name, String area, String year, String type, String intro1, String intro2, String intro3, String intro4, int id, int clickrate, int recommend, float chage, int updatetime) {
        ContentValues initialValues = new ContentValues();
        if (image != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            image.compress(CompressFormat.PNG, 100, os);
            initialValues.put("image", os.toByteArray());
        }
        initialValues.put("imageurl", imageurl);
        initialValues.put("url", url);
        initialValues.put("name", name);
        initialValues.put("area", area);
        initialValues.put(AudioColumns.YEAR, year);
        initialValues.put("type", type);
        initialValues.put("intro1", intro1);
        initialValues.put("intro2", intro2);
        initialValues.put("intro3", intro3);
        initialValues.put("intro4", intro4);
        initialValues.put("vodid", Integer.valueOf(id));
        initialValues.put("clickrate", Integer.valueOf(clickrate));
        initialValues.put("recommend", Integer.valueOf(recommend));
        initialValues.put("chage", Float.valueOf(chage));
        initialValues.put("updatetime", Integer.valueOf(updatetime));
        return this.mSQLiteDatabase.update(DB_TABLE, initialValues, "vodid=?", new String[]{String.valueOf(id)}) > 0;
    }

    public VodListStatus get(String id, String infotype) {
        return get(Integer.parseInt(id), Integer.parseInt(infotype));
    }

    public VodListStatus get(int id, int infotype) {
        VodListStatus s = new VodListStatus();
        open();
        Cursor cur = fetchData(id, infotype);
        if (cur == null) {
            close();
            return null;
        }
        s.url = cur.getString(cur.getColumnIndex("url"));
        byte[] in = cur.getBlob(cur.getColumnIndex("image"));
        if (in != null) {
            s.imagebit = BitmapFactory.decodeByteArray(in, 0, in.length);
        }
        s.image = cur.getString(cur.getColumnIndex("imageurl"));
        s.name = cur.getString(cur.getColumnIndex("name"));
        s.area = cur.getString(cur.getColumnIndex("area"));
        s.year = cur.getString(cur.getColumnIndex(AudioColumns.YEAR));
        s.type = cur.getString(cur.getColumnIndex("type"));
        s.intro1 = cur.getString(cur.getColumnIndex("intro1"));
        s.intro2 = cur.getString(cur.getColumnIndex("intro2"));
        s.intro3 = cur.getString(cur.getColumnIndex("intro3"));
        s.intro4 = cur.getString(cur.getColumnIndex("intro4"));
        s.id = cur.getInt(cur.getColumnIndex("vodid"));
        s.clickrate = cur.getInt(cur.getColumnIndex("clickrate"));
        s.recommend = cur.getInt(cur.getColumnIndex("recommend"));
        s.chage = cur.getFloat(cur.getColumnIndex("chage"));
        s.updatetime = cur.getInt(cur.getColumnIndex("updatetime"));
        s.infotype = cur.getInt(cur.getColumnIndex("infotype"));
        close();
        return s;
    }

    public long insert(VodListStatus s, Bitmap bit, int t) {
        open();
        long ret = inserDataNoreRepeat(bit, s.image, s.url, s.name, s.area, s.year, s.type, s.intro1, s.intro2, s.intro3, s.intro4, s.id, s.clickrate, s.recommend, s.chage, s.updatetime, t);
        close();
        return ret;
    }

    public ArrayList<VodListStatus> parseAll() {
        open();
        ArrayList<VodListStatus> VodCollectArray = new ArrayList();
        Cursor cur = fetchAllData();
        if (cur == null) {
            close();
            return null;
        }
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            String url = cur.getString(cur.getColumnIndex("url"));
            byte[] in = cur.getBlob(cur.getColumnIndex("image"));
            Bitmap image = null;
            if (in != null) {
                image = BitmapFactory.decodeByteArray(in, 0, in.length);
            }
            String imageurl = cur.getString(cur.getColumnIndex("imageurl"));
            String name = cur.getString(cur.getColumnIndex("name"));
            String area = cur.getString(cur.getColumnIndex("area"));
            String year = cur.getString(cur.getColumnIndex(AudioColumns.YEAR));
            String type = cur.getString(cur.getColumnIndex("type"));
            String intro1 = cur.getString(cur.getColumnIndex("intro1"));
            String intro2 = cur.getString(cur.getColumnIndex("intro2"));
            String intro3 = cur.getString(cur.getColumnIndex("intro3"));
            String intro4 = cur.getString(cur.getColumnIndex("intro4"));
            int id = cur.getInt(cur.getColumnIndex("vodid"));
            int clickrate = cur.getInt(cur.getColumnIndex("clickrate"));
            int recommend = cur.getInt(cur.getColumnIndex("recommend"));
            float chage = cur.getFloat(cur.getColumnIndex("chage"));
            int updatetime = cur.getInt(cur.getColumnIndex("updatetime"));
            int infotype = cur.getInt(cur.getColumnIndex("infotype"));
            VodListStatus s = new VodListStatus();
            s.name = name;
            s.imagebit = image;
            s.image = imageurl;
            s.url = url;
            s.area = area;
            s.year = year;
            s.type = type;
            s.intro1 = intro1;
            s.intro2 = intro2;
            s.intro3 = intro3;
            s.intro4 = intro4;
            s.id = id;
            s.clickrate = clickrate;
            s.recommend = recommend;
            s.chage = chage;
            s.updatetime = updatetime;
            s.infotype = infotype;
            VodCollectArray.add(s);
            cur.moveToNext();
        }
        close();
        return VodCollectArray;
    }

    public int parseSize() {
        open();
        Cursor cur = fetchAllData();
        if (cur == null) {
            close();
            return 0;
        }
        int size = cur.getCount();
        close();
        return size;
    }
}
