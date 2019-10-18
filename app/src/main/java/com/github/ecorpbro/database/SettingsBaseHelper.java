package com.github.ecorpbro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.ecorpbro.database.SettingsDbSchema.DefaultUrlTable;
import com.github.ecorpbro.database.SettingsDbSchema.SourceUrlTable;

public class SettingsBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "settings.db";

    public SettingsBaseHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SourceUrlTable.DB_TABLE_NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SourceUrlTable.Cols.NAME + ", " +
                SourceUrlTable.Cols.URL +
                ")"
        );
        db.execSQL("CREATE TABLE " + DefaultUrlTable.DB_TABLE_NAME + "(" +
                SourceUrlTable.Cols.URL +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SourceUrlTable.DB_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DefaultUrlTable.DB_TABLE_NAME);
        onCreate(db);
    }
}

