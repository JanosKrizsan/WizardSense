package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartDaoJDBC extends ConnectionHandler implements GenericQueriesDao<Cart> {

    private static CartDaoJDBC instance = null;
    private PreparedStatement statement;
    private ProductDaoJDBC productDao = ProductDaoJDBC.getInstance();


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
        HashMap<Product, Integer> products = cart.getProductList();
        try {
            for (Product product : products.keySet()) {
                statement = getConn().prepareStatement("INSERT INTO carts (id, user_id, product_id, product_quantity) VALUES (? , ? , ?, ?);");
                statement.setInt(1, cart.getId());
                statement.setInt(2, cart.getUser().getId());
                statement.setInt(3, product.getId());
                statement.setInt(4, products.get(product));
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public Cart find(int id) {
        Cart cart = null;
        try {
            statement = getConn().prepareStatement("SELECT * FROM carts WHERE id = ?;");
            statement.setInt(1, id);

            ResultSet results = statement.executeQuery();

            int productId = 0;
            int productQuantity = 0;
            User user = null;
            HashMap<Product, Integer> productMap = new HashMap<>();

            while (results.next()) {
                productId = results.getInt("product_id");
                productQuantity = results.getInt("product_quantity");
                user = UserDaoJDBC.getInstance().find(results.getInt("user_id"));
                productMap.put(productDao.find(productId), productQuantity);
            }

            cart = new Cart(productMap, user);
            cart.setId(id);
            statement.close();
            results.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
        return cart;
    }

    @Override
    public void remove(int id) {
        try {
            statement = getConn().prepareStatement("DELETE FROM carts WHERE id=?;");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    @Override
    public List<Cart> getAll() {
        List<Cart> carts = new ArrayList<>();
        try {
            statement = getConn().prepareStatement("SELECT id FROM carts");
            ResultSet results = statement.executeQuery();

            while (results.next()) {

                int id = results.getInt("id");
                carts.add(find(id));

            }

            statement.close();
            results.close();

            return carts;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return carts;
    }

    public Cart getCartByUserId(int id) {
        try {
            statement = getConn().prepareStatement("SELECT * FROM carts WHERE user_id=?;");
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();

            Cart cart = null;
            while (results.next()) {

                cart = find(results.getInt("id"));
            }

            statement.close();
            results.close();

            return cart;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public void increaseProductQuantity(Cart cart, Product product) {
        try {
            statement = getConn().prepareStatement("UPDATE carts " +
                    "SET product_quantity = product_quantity + 1 " +
                    "WHERE id=? AND user_id=? AND product_id=?;");
            statement.setInt(1, cart.getId());
            statement.setInt(2, cart.getUser().getId());
            statement.setInt(3, product.getId());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
