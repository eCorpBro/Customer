package com.github.ecorpbro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.ecorpbro.database.ProductDbSchema.ProductTable;

public class ProductBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "productBase.db";

    public ProductBaseHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ProductTable.DB_TABLE_NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductTable.Cols.ID + ", " +
                ProductTable.Cols.NAME + ", " +
                ProductTable.Cols.QUANTITY + ", " +
                ProductTable.Cols.PRICE + ", " +
                ProductTable.Cols.ORDER_PRODUCTS +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductTable.DB_TABLE_NAME);
        onCreate(db);
    }
}
