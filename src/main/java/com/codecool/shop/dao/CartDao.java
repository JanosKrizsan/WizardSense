package com.codecool.shop.dao;

import com.codecool.shop.model.Product;

import java.util.List;

public interface CartDao {

    void add(Product product);
    Product find(int id);
    void remove(int id);

    List<Product> getAll();
}
