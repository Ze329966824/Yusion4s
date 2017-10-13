package com.yusion.shanghai.yusion4s.ubt.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ice on 2017/9/18.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    // 创建Book数据表语法
    public static final String TABLE = "UBTEvents";
    public static final String CREATE_TABLE = "create table " + TABLE + " ("
            + "id integer primary key autoincrement, "
            + "object text, "
            + "action text, "
            + "ts text, "
            + "page_cn text, "
            + "page text)";

    // 通过构造方法创建数据库，其中name为数据库名称
    public MyDatabaseHelper(Context context, String name) {
        super(context, name, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行创建数据表的语法
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; ++i) {
            switch (i) {
                case 1:
                    db.execSQL("ALTER TABLE " + TABLE + " ADD COLUMN page_cn TEXT");
                    break;
            }
        }
    }

}

