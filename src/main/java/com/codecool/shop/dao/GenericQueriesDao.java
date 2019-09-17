package com.codecool.shop.dao;

import com.codecool.shop.model.User;

import java.util.List;

public interface GenericQueriesDao<T> {

    void add(T object);
    T find(int id);
    void remove(int id);

    List<T> getAll();

}
