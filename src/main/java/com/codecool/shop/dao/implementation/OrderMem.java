package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderMem implements GenericQueriesDao<Order> {

    private static OrderMem instance = null;
    private List<Order> data = new ArrayList<>();

    /* A private Constructor prevents any other class from instantiating.
     */
    private OrderMem() {
    }

    public static OrderMem getInstance() {
        if (instance == null) {
            instance = new OrderMem();
        }
        return instance;
    }

    @Override
    public void add(Order order) {
        data.add(order);
    }

    @Override
    public Order find(int id) {
        return data.get(id);
    }

    @Override
    public void remove(int id) {
        data.remove(id);
    }

    @Override
    public List<Order> getAll() {
        return data;
    }
}
