package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJDBC extends ConnectionHandler implements GenericQueriesDao<Order> {

    private static OrderDaoJDBC instance = null;
    private UserDaoJDBC userDao = UserDaoJDBC.getInstance();
    private CartDaoJDBC cartDao = CartDaoJDBC.getInstance();
    private UserAddressDaoJDBC addressDao = UserAddressDaoJDBC.getInstance();


    private OrderDaoJDBC() {
    }

    public static OrderDaoJDBC getInstance() {
        if (instance == null) {
            instance = new OrderDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Order order) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("INSERT INTO orders (cart_id, user_id, address_id, status) VALUES (? , ?, ?, ?) RETURNING id;");
        statement.setInt(1, order.getCart().getId());
        statement.setInt(2, order.getUser().getId());
        statement.setInt(3, order.getAddress().getId());
            statement.setString(4, order.getStatus());
        ResultSet result = statement.executeQuery();

        int id = 0;
        while (result.next()) {
            id = result.getInt("id");
        }
        order.setId(id);
        statement.close();
    }

    @Override
    public Order find(int cartID) throws SQLException {
        Order order;
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM orders WHERE cart_id = ?;");
        statement.setInt(1, cartID);

        ResultSet results = statement.executeQuery();

        int orderId = 0;
        int cartId = 0;
        int userId = 0;
        int addressId= 0;
            String orderStatus = "";

        while (results.next()) {
            orderId = results.getInt("id");
            cartId = results.getInt("cart_id");
            userId = results.getInt("user_id");
            addressId = results.getInt("address_id");
                orderStatus = results.getString("status");
            }
            order = new Order(userDao.find(userId), cartDao.find(cartId), addressDao.find(addressId), orderStatus);
            order.setId(orderId);

        statement.close();
        return order;
    }

    @Override
    public void remove(int id) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("DELETE FROM orders WHERE id=?;");
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public List<Order> getAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        PreparedStatement statement = getConn().prepareStatement("SELECT id FROM orders");
        ResultSet results = statement.executeQuery();
        while (results.next()) {

            int id = results.getInt("id");
            orders.add(find(id));

        }
        results.close();
        statement.close();
        return orders;
    }

    @Override
    public void removeAll() throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("TRUNCATE orders CASCADE ");
            statement.executeUpdate();
            statement.close();
    }

    public void setStatus(String status, Order order) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("UPDATE orders SET cart_id = 0, status = ? WHERE id=? AND cart_id=? AND user_id=? AND address_id=?;");
        statement.setString(1, status);
        statement.setInt(2, order.getId());
        statement.setInt(3, order.getCart().getId());
        statement.setInt(4, order.getUser().getId());
        statement.setInt(5, order.getAddress().getId());
        statement.close();
    }
}
