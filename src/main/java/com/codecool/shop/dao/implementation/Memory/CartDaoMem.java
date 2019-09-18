package com.codecool.shop.dao.implementation.Memory;

import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartDaoMem implements GenericQueriesDao<Cart> {

    private List<Cart> data = new ArrayList<>();
    private static CartDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private CartDaoMem() {
    }

    public static CartDaoMem getInstance() {
        if (instance == null) {
            instance = new CartDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Cart cart) {
        data.add(cart);
    }

    @Override
    public Cart find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<Cart> getAll() {
        return data;
    }

    @Override
    public void removeAll() {
        data.clear();
    }

}
