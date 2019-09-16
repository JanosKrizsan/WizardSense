package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;

import java.util.List;

public interface CartDao {

    void add(Cart cart);
    Cart find(int id);
    void remove(int id);

    List<Cart> getAll();
}
