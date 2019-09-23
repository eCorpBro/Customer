package com.github.ecorpbro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONDownloader {

    private static final String TAG = "JSONDownloader";

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();


        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public static Products jsonStringToProducts(String jsonText) throws JSONException {
        List<ProductItem> productItemList = new ArrayList<>();

        JSONObject jsonRoot = new JSONObject(jsonText);

        JSONArray jsonArray = jsonRoot.getJSONArray("products");

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

}