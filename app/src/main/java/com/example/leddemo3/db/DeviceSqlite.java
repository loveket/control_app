package com.example.leddemo3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.leddemo3.model.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceSqlite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CXManager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "device";

    public DeviceSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "DEVICENAME TEXT,"
                + "DEVICEIP TEXT,"
                + "STATUS INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 插入数据
    public boolean insertData(String devicename, String deviceip,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DEVICENAME", devicename);
        contentValues.put("DEVICEIP", deviceip);
        contentValues.put("STATUS", status);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // 更新数据
    public boolean updateData(String ip, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("STATUS", status);
        int result = db.update(TABLE_NAME, contentValues, "DEVICEIP = ?", new String[]{String.valueOf(ip)});
        return result > 0;
    }

    // 删除数据
    public boolean deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "DEVICENAME = ?", new String[]{String.valueOf(name)});
        return result > 0;
    }

    // 查询数据
    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = getWritableDatabase().query(TABLE_NAME,null,null,null,null,null,null,null);
        return res;
    }
}
