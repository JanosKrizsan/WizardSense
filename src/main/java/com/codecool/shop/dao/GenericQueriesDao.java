package com.codecool.shop.dao;


import java.sql.SQLException;
import java.util.List;

public interface GenericQueriesDao<T> {

    void add(T object) throws SQLException;
    T find(int id) throws SQLException;
    void remove(int id) throws SQLException;

    List<T> getAll() throws SQLException;
    void removeAll() throws SQLException;

}
