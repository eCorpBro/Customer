package com.github.ecorpbro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.ecorpbro.database.ProductBaseHelper;
import com.github.ecorpbro.database.ProductCursorWrapper;
import com.github.ecorpbro.database.ProductDbSchema.ProductTable;

import java.util.ArrayList;
import java.util.List;

public class Products implements Parcelable {

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

    protected Products(Parcel in) {
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

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

    public void setProductItemListFromBase() {
        List<ProductItem> productItems = new ArrayList<>();
        ProductCursorWrapper cursor = queryProducts(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                productItems.add(cursor.getProductItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        mProductItemList = productItems;
    }


        public ProductItem getProductItem(int id) {
        ProductItem productItem = new ProductItem();
        for (ProductItem product : mProducts.getProductItemList()) {
            if (product.getId() == id) {
                productItem = product;
            }
        }
        return productItem;
    }

    public void updateProductItem(ProductItem productItem) {
        String id = String.valueOf(productItem.getId());
        ContentValues values = getContentValues(productItem);
        mDatabase.update(ProductTable.DB_TABLE_NAME,
                values,
                ProductTable.Cols.ID + " = ?",
                new  String[]{id});

    }

    private ProductCursorWrapper queryProducts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ProductTable.DB_TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ProductCursorWrapper(cursor);
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

    public void deleteProducts() {
        mDatabase.delete(ProductTable.DB_TABLE_NAME, null, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}

