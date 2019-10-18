package com.github.ecorpbro.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.ecorpbro.products.ProductItem;
import com.github.ecorpbro.products.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductBaseManager {

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public ProductBaseManager(Context context) {
        mContext = context;
    }

    public ProductCursorWrapper getCursorWrapper(String whereClause, String[] whereArgs) {
        mDatabase = new ProductBaseHelper(mContext).getWritableDatabase();
        Cursor cursor = mDatabase.query(
                ProductDbSchema.ProductTable.DB_TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);
        return cursorWrapper;
    }

    public Products getProductsFromBase() {
        ProductCursorWrapper cursorWrapper = getCursorWrapper(null,null);
        List<ProductItem> mProductItemList = new ArrayList<>();
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                mProductItemList.add(cursorWrapper.getProductItem());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        Products products = new Products();
        products.setProductItemList(mProductItemList);
        return products;
    }
}
