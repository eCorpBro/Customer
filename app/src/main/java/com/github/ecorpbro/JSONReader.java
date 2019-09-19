package com.github.ecorpbro;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {

//Получаем объект Products из текстовой строки
    public static Products readProductsJSONFile(Context context) throws IOException, JSONException {
        String jsonText = readText(context, R.raw.products);
        List<ProductItem> productItemList = new ArrayList<>();

        JSONObject jsonRoot = new JSONObject(jsonText);

        JSONArray jsonArray = jsonRoot.getJSONArray("products");
        ProductItem[] productItems = new ProductItem[jsonArray.length()];

        for (int i = 0; i < jsonArray.length();i++) {
            JSONObject productJsonObject = jsonArray.getJSONObject(i);

            int id = productJsonObject.getInt("id");
            String name = productJsonObject.getString("name");
            String quantity = productJsonObject.getString("quantity");
            String price = productJsonObject.getString("price");

            ProductItem productItem = new ProductItem(id,name,quantity,price);

            productItemList.add(productItem);
        }

        Products products = new Products();
        products.setProductItemList(productItemList);

        return products;
    }

//Считывание файла raw ресурса(в нашем случае *.json) и формирование его в виде строки
    private static String readText(Context context,int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = br.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();

    }
}
