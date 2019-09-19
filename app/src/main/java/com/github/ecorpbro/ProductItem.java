package com.github.ecorpbro;

public class ProductItem {
    private int mId;
    private String mName;
    private String mQuantity;
    private String mPrice;

    public ProductItem(int id, String name, String quantity, String price) {
        this.mId = id;
        this.mName = name;
        this.mQuantity = quantity;
        this.mPrice = price;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        this.mQuantity = quantity;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        this.mPrice = price;
    }
}
