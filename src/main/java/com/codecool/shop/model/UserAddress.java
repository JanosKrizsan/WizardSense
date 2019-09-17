package com.codecool.shop.model;

import java.util.HashMap;

public class UserAddress {
    private int id;
    private HashMap<String, String> orderFields = new HashMap<String, String>() {
        {
            put("name", "");
            put("email", "");
            put("phoneNum", "");
            put("billingCountry", "");
            put("billingCity", "");
            put("billingZipCode", "");
            put("billingAddress", "");
            put("shippingCountry", "");
            put("shippingCity", "");
            put("shippingZipCode", "");
            put("shippingAddress", "");

        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<String, String> getOrderFields() {
        return orderFields;
    }

    public void setOrderFields(HashMap<String, String> orderFields) {
        this.orderFields = orderFields;
    }
}
