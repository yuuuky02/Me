package com.example.meee;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDBHelper extends SQLiteOpenHelper {

    public MemoDBHelper(Context context) {
        super(context, "memo.db", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE memo (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, category TEXT, content TEXT," +
                "address TEXT, camera TEXT, album TEXT, emotion TEXT);");

        db.execSQL("CREATE TABLE exercise (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, etime INTEGER, edistance INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS memo");
        db.execSQL("DROP TABLE IF EXISTS exercise");
        onCreate(db);
    }
}
