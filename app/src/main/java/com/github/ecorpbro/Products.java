package com.github.ecorpbro;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.ecorpbro.database.ProductBaseHelper;
import com.github.ecorpbro.database.ProductDbSchema.ProductTable;

import java.io.Serializable;
import java.util.List;

public class Products implements Serializable {
    private static Products mProducts;
    private Context mContext;
    private List<ProductItem> mProductItemList;
    private SQLiteDatabase mDatabase;

    public Products() {
    }

    private Products(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ProductBaseHelper(mContext).getWritableDatabase();
    }

    public static Products get(Context context) {
        if (mProducts == null) {
            mProducts = new Products(context);
        }
        return mProducts;
    }

    public List<ProductItem> getProductItemList() {
        return mProductItemList;
    }

    public void setProductItemList(List<ProductItem> productItemList) {
        mProductItemList = productItemList;
    }

    private static ContentValues getContentValues(ProductItem productItem) {
        ContentValues values = new ContentValues();
        values.put(ProductTable.Cols.ID, productItem.getId());
        values.put(ProductTable.Cols.NAME, productItem.getName());
        values.put(ProductTable.Cols.QUANTITY, productItem.getQuantity());
        values.put(ProductTable.Cols.PRICE, productItem.getPrice());
        values.put(ProductTable.Cols.ORDER_PRODUCTS, productItem.getOrder());

        return values;
    }

    public void addProducts(Context context) {
        if (mDatabase == null) {
            mDatabase = new ProductBaseHelper(context).getWritableDatabase();
        }
            mDatabase.delete(ProductTable.DB_TABLE_NAME, null, null);
            List<ProductItem> productItems = this.getProductItemList();
            for (ProductItem product: productItems) {
                ContentValues values = getContentValues(product);
                mDatabase.insert(ProductTable.DB_TABLE_NAME, null, values);
            }
    }
}

