package com.yusion.shanghai.yusion4s.ubt.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ice on 2017/9/18.
 */

public class SqlLiteUtil {

    public static MyDatabaseHelper myDatabaseHelper;

    public static void init(Context context) {
        myDatabaseHelper = getMyDatabaseHelper(context);
    }

    public static MyDatabaseHelper getMyDatabaseHelper(Context context) {
        if (myDatabaseHelper == null) {
            myDatabaseHelper = new MyDatabaseHelper(context, "yusion");
        }
        return myDatabaseHelper;
    }

    public static void insert(ContentValues contentValues) {
        SQLiteDatabase database = myDatabaseHelper.getWritableDatabase();
        database.insert(MyDatabaseHelper.TABLE, null, contentValues);
    }

    public static void delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase database = myDatabaseHelper.getWritableDatabase();
        database.delete(MyDatabaseHelper.TABLE, whereClause, whereArgs);
    }

    public static Cursor query(String[] columns, String selection, String[] selectionArgs, String limit) {
        SQLiteDatabase database = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = database.query(MyDatabaseHelper.TABLE, columns, selection, selectionArgs, null, null, null, limit);
        return cursor;
    }
}
