package com.example.bmicalculator;

import static android.provider.BaseColumns._ID;
import static com.example.bmicalculator.Constants.BMI;
import static com.example.bmicalculator.Constants.TABLE_NAME;
import static com.example.bmicalculator.Constants.DATE;
import static com.example.bmicalculator.Constants.TITLE;
import static com.example.bmicalculator.Constants.WIGHT;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "DBHelper.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + _ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE + " INTEGER, "
                + WIGHT + " INTEGER, "
                + BMI + " INTEGER, "
                + TITLE + " TEXT);"  );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }
}
