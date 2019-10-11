package com.github.ecorpbro.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.ecorpbro.ProductItem;
import com.github.ecorpbro.database.ProductDbSchema.ProductTable;

public class ProductCursorWrapper extends CursorWrapper {

    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ProductItem getProductItem() {
        int idIndex = this.getColumnIndex(ProductTable.Cols.ID);
        int nameIndex = this.getColumnIndex(ProductTable.Cols.NAME);
        int quantityIndex = this.getColumnIndex(ProductTable.Cols.QUANTITY);
        int priceIndex = this.getColumnIndex(ProductTable.Cols.PRICE);
        int order_productsIndex = this.getColumnIndex(ProductTable.Cols.ORDER_PRODUCTS);


        int id = this.getInt(idIndex);
        String name = this.getString(nameIndex);
        String quantity = this.getString(quantityIndex);
        String price = this.getString(priceIndex);
        String order = this.getString(order_productsIndex);

        ProductItem productItem = new ProductItem();
        productItem.setId(id);
        productItem.setName(name);
        productItem.setQuantity(quantity);
        productItem.setPrice(price);
        productItem.setOrder(order);

        return productItem;
    }
}
