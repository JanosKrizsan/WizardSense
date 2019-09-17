package com.codecool.shop.model;

import java.util.HashMap;

public class UserAddress {
    private int id;
    private int userId;
    private HashMap<String, String> orderFields;

    public UserAddress(HashMap<String, String> orderFields, int userId) {
        this.orderFields = orderFields;
        this.userId = userId;
    }

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
