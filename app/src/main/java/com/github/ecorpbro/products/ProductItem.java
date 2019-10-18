package com.github.ecorpbro.products;

import java.util.Date;
import java.util.UUID;

public class ProductItem {
    private UUID id; //id товара
    private String name;
    private String quantity;
    private String price;
    private String order;

    public ProductItem() {
        this(UUID.randomUUID());
    }

    public ProductItem(UUID id) {
        this.id = id;
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
