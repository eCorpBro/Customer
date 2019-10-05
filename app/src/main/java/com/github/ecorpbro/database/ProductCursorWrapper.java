package com.github.ecorpbro.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.ecorpbro.ProductItem;
import com.github.ecorpbro.database.ProductDbSchema.ProductTable;

public class ProductCursorWrapper extends CursorWrapper {

    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ProductItem getProduct() {
        int id = Integer.parseInt(getString(getColumnIndex(ProductTable.Cols.ID)));
        String name = getString(getColumnIndex(ProductTable.Cols.NAME));
        String quantity = getString(getColumnIndex(ProductTable.Cols.QUANTITY));
        String price = getString(getColumnIndex(ProductTable.Cols.PRICE));
        String order = getString(getColumnIndex(ProductTable.Cols.ORDER_PRODUCTS));

        ProductItem productItem = new ProductItem();
        productItem.setId(id);
        productItem.setName(name);
        productItem.setQuantity(quantity);
        productItem.setPrice(price);
        productItem.setOrder(order);

        return productItem;
    }
}
