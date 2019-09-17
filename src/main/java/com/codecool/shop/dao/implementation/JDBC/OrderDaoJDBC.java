package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJDBC extends ConnectionHandler implements GenericQueriesDao<Order> {

    private static OrderDaoJDBC instance = null;
    private PreparedStatement statement;
    private UserDaoJDBC userDao = UserDaoJDBC.getInstance();
    private CartDaoJDBC cartDao = CartDaoJDBC.getInstance();


    private OrderDaoJDBC() {
    }

    public static OrderDaoJDBC getInstance() {
        if (instance == null) {
            instance = new OrderDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Order order) {

    }

    @Override
    public Order find(int id) {
        Order order = null;
        try {
            statement = getConn().prepareStatement("SELECT * FROM orders WHERE id = ?;");
            statement.setInt(1, id);

            ResultSet results = statement.executeQuery();

            int orderId = 0;
            int cartId = 0;
            int userId = 0;
            String orderStatus = "";

            while (results.next()) {
                orderId = results.getInt("id");
                cartId = results.getInt("cart_id");
                userId = results.getInt("user_id");
                orderStatus = results.getString("status");
            }

            order = new Order(userDao.find(userId), cartDao.find(cartId));
            order.setId(orderId);
            order.setStatus(orderStatus);

            statement.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
        return order;
    }

    @Override
    public void remove(int id) {
        try {
            statement = getConn().prepareStatement("DELETE FROM orders WHERE id=?;");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try {
            statement = getConn().prepareStatement("SELECT id FROM orders");
            ResultSet results = statement.executeQuery();

            while (results.next()) {

                int id = results.getInt("id");
                orders.add(find(id));

            }

            statement.close();
            results.close();

            return orders;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return orders;
    }
}
