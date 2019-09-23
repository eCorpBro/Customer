package com.github.ecorpbro;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class Products implements Serializable {
    private List<ProductItem> mProductItemList;

    public List<ProductItem> getProductItemList() {
        return mProductItemList;
    }

    public void setProductItemList(List<ProductItem> productItemList) {
        mProductItemList = productItemList;
    }

    public String toJSON() {
        List<ProductItem> productItems = this.getProductItemList();
        Gson gson = new Gson();
        String json = gson.toJson(productItems);
        String result = "{\"product\":" + json + "}";

        return result;
    }
}
