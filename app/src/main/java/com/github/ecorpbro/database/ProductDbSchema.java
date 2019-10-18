package com.github.ecorpbro.database;

public class ProductDbSchema {
    public static final class ProductTable {
        public static final String DB_TABLE_NAME = "products";

        public static final class Cols{
            public static final String UUID = "id";
            public static final String NAME = "name";
            public static final String QUANTITY ="quantity";
            public static final String PRICE = "price";
            public static final String ORDER_PRODUCTS = "order_products";
        }
    }
}
