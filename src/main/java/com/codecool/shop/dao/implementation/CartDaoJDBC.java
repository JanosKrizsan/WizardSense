package com.codecool.shop.dao.implementation;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.Cart;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CartDaoJDBC extends ConnectionHandler implements GenericQueriesDao<Cart> {

    private static CartDaoJDBC instance = null;
    private PreparedStatement statement;


    private CartDaoJDBC() {
        super();
    }

    public static CartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new CartDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Cart cart) {
        try {
            getConn().rollback();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public Cart find(int id) {
        try {
            getConn().rollback();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void remove(int id) {
        try {
            getConn().rollback();
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    @Override
    public List<Cart> getAll() {
        try {
            getConn().rollback();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }


}
