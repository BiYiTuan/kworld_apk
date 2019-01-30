package com.gemini.play;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PositionVodDB {
    private static final String DB_CREATE = "create table positionvod2(_id integer primary key autoincrement, type int, vodid int, num text, position int)";
    private static final String DB_NAME = "positionvod2.db";
    private static final String DB_TABLE = "positionvod2";
    private static final int DB_VERSION = 1;
    private Context mContext = null;
    private DatabaseHelper mDatabaseHelper = null;
    private SQLiteDatabase mSQLiteDatabase = null;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            if (!PositionVodDB.tabbleIsExist(db, PositionVodDB.DB_CREATE)) {
                db.execSQL(PositionVodDB.DB_CREATE);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists collect");
            onCreate(db);
        }
    }

    public PositionVodDB(Context context) {
        this.mContext = context;
    }

    public void open() {
        if (this.mDatabaseHelper == null) {
            this.mDatabaseHelper = new DatabaseHelper(this.mContext, DB_NAME, null, 1);
            this.mSQLiteDatabase = this.mDatabaseHelper.getWritableDatabase();
        }
    }

    public void close() {
    }

    public static boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        try {
            Cursor cursor = db.rawQuery("select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ", null);
            if (cursor.moveToNext() && cursor.getInt(0) > 0) {
                result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }

    public long insertData(int type, int vodid, String num, int position) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("type", Integer.valueOf(type));
        initialValues.put("vodid", Integer.valueOf(vodid));
        initialValues.put("num", num);
        initialValues.put("position", Integer.valueOf(position));
        return this.mSQLiteDatabase.insert(DB_TABLE, "_id", initialValues);
    }

    public long inserDataNoreRepeat(int type, int vodid, String num, int position) {
        open();
        Cursor cursor = fetchData(vodid, type);
        if (cursor == null || cursor.getCount() <= 0) {
            long ret = insertData(type, vodid, num, position);
            if (cursor == null) {
                return ret;
            }
            cursor.close();
            return ret;
        }
        updateData(type, vodid, num, position);
        cursor.close();
        return 1;
    }

    public boolean deleteData(int id) {
        return this.mSQLiteDatabase.delete(DB_TABLE, "vodid=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor fetchAllData() {
        return this.mSQLiteDatabase.query(DB_TABLE, new String[]{"_id", "type", "vodid", "num", "position"}, null, null, null, null, null);
    }

    public Cursor fetchData(int id, int infotype) throws SQLException {
        Cursor cursor = this.mSQLiteDatabase.rawQuery("select * from positionvod2 where vodid=? and type=?", new String[]{String.valueOf(id), String.valueOf(infotype)});
        return (cursor == null || !cursor.moveToNext()) ? null : cursor;
    }

    public boolean updateData(int type, int vodid, String num, int position) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("type", Integer.valueOf(type));
        initialValues.put("vodid", Integer.valueOf(vodid));
        initialValues.put("num", num);
        initialValues.put("position", Integer.valueOf(position));
        if (this.mSQLiteDatabase.update(DB_TABLE, initialValues, "vodid=? and type=?", new String[]{String.valueOf(vodid), String.valueOf(type)}) > 0) {
            return true;
        }
        return false;
    }

    public VodPositionStatus get(int id, int infotype) {
        open();
        VodPositionStatus s = new VodPositionStatus();
        Cursor cur = fetchData(id, infotype);
        if (cur == null) {
            return null;
        }
        int position = cur.getInt(cur.getColumnIndex("position"));
        String num = cur.getString(cur.getColumnIndex("num"));
        s.position = position;
        s.num = num;
        cur.close();
        return s;
    }
}
