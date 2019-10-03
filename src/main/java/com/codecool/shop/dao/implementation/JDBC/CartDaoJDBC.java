package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
    public void add(Cart cart) throws SQLException {
        TreeMap<Product, Integer> products = cart.getProductList();
        for (Product product : products.keySet()) {
            statement = getConn().prepareStatement("INSERT INTO carts (id, user_id, product_id, product_quantity) VALUES (? , ? , ?, ?) RETURNING id;");
            statement.setInt(1, cart.getId());
            statement.setInt(2, cart.getUser().getId());
            statement.setInt(3, product.getId());
            for (Integer quantity : products.values()) {
                statement.setInt(4, quantity);
            }
            ResultSet result = statement.executeQuery();
            int cartId = cart.getId();
            while (result.next()) {
                cartId = result.getInt("id");
            }
            cart.setId(cartId);
            statement.close();
        }
    }

    @Override
    public Cart find(int id) throws SQLException {
        Cart cart;
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM carts WHERE id = ?;");
        statement.setInt(1, id);

        ResultSet results = statement.executeQuery();

        int productId;
        int productQuantity;
        User user = null;
        TreeMap<Product, Integer> productMap = new TreeMap<>();

        while (results.next()) {

            productId = results.getInt("product_id");
            productQuantity = results.getInt("product_quantity");
            user = UserDaoJDBC.getInstance().find(results.getInt("user_id"));
            productMap.put(productDao.find(productId), productQuantity);
        }

        cart = new Cart(productMap, user);
        cart.setId(id);
        results.close();

        return cart;
    }

    @Override
    public void remove(int id) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("DELETE FROM carts WHERE id=?;");
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
    }

    public void clearProductFromCart(int prodID) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("DELETE FROM carts WHERE product_id=?;");
        statement.setInt(1, prodID);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public List<Cart> getAll() throws SQLException {
        List<Cart> carts = new ArrayList<>();
        PreparedStatement statement = getConn().prepareStatement("SELECT id FROM carts");
        ResultSet results = statement.executeQuery();

        while (results.next()) {

            int id = results.getInt("id");
            carts.add(find(id));

        }

        results.close();
        statement.close();
        return carts;
    }

    @Override
    public void removeAll() throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("TRUNCATE carts CASCADE ");
        statement.executeUpdate();
        statement.close();
    }

    public Cart getCartByUserId(int id) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("SELECT id FROM carts WHERE user_id=?;");
        statement.setInt(1, id);
        ResultSet results = statement.executeQuery();

        Cart cart = null;
        while (results.next()) {

            cart = find(results.getInt("id"));
        }
        results.close();
        statement.close();

        return cart;
    }

    public void increaseOrDecreaseQuantity(Cart cart, Integer productId, boolean incOrDec) throws SQLException {
        if (incOrDec) {
            statement = getConn().prepareStatement("UPDATE carts " +
                    "SET product_quantity = product_quantity + 1 " +
                    "WHERE id=? AND user_id=? AND product_id=?;");
        } else {
            statement = getConn().prepareStatement("UPDATE carts " +
                    "SET product_quantity = product_quantity - 1 " +
                    "WHERE id=? AND user_id=? AND product_id=?;");
        }

        statement.setInt(1, cart.getId());
        statement.setInt(2, cart.getUser().getId());
        statement.setInt(3, productId);
        statement.executeUpdate();
        statement.close();
    }

    public Integer getCartProductQuantity(Cart cart, int productId) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("SELECT product_quantity FROM carts WHERE id=? AND user_id=? AND product_id=?;");
        statement.setInt(1, cart.getId());
        statement.setInt(2, cart.getUser().getId());
        statement.setInt(3, productId);

        ResultSet result = statement.executeQuery();

        int quantity = 0;
        while (result.next()) {

            quantity = result.getInt("product_quantity");

        }
        return quantity;
    }
}
