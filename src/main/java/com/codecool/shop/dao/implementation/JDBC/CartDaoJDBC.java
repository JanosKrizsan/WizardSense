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
    public void add(Cart cart) {
        TreeMap<Product, Integer> products = cart.getProductList();
        try {
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
        } catch (SQLException e) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
        }
    }

    @Override
    public Cart find(int id) {
        Cart cart = null;
        try (PreparedStatement statement = getConn().prepareStatement("SELECT * FROM carts WHERE id = ?;")) {
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

        } catch (SQLException e) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
        }
        return cart;
    }

    @Override
    public void remove(int id) {
        try (PreparedStatement statement = getConn().prepareStatement("DELETE FROM carts WHERE id=?;")) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
        }
    }

    public void clearProductFromCart(int prodID) {
        try (PreparedStatement statement = getConn().prepareStatement("DELETE FROM carts WHERE product_id=?;")) {
            statement.setInt(1, prodID);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
        }
    }

    @Override
    public List<Cart> getAll() {
        List<Cart> carts = new ArrayList<>();
        try (PreparedStatement statement = getConn().prepareStatement("SELECT id FROM carts")) {
            ResultSet results = statement.executeQuery();

            while (results.next()) {

                int id = results.getInt("id");
                carts.add(find(id));

            }

            results.close();

            return carts;
        } catch (SQLException e) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
        }
        return carts;
    }

    @Override
    public void removeAll() {
        try (PreparedStatement statement = getConn().prepareStatement("TRUNCATE carts CASCADE ")) {
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
        }
    }

    public Cart getCartByUserId(int id) {
        try (PreparedStatement statement = getConn().prepareStatement("SELECT id FROM carts WHERE user_id=?;")) {
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();

            Cart cart = null;
            while (results.next()) {

                cart = find(results.getInt("id"));
            }
            results.close();

            return cart;
        } catch (SQLException e) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
        }
        return null;
    }

    public void increaseOrDecreaseQuantity(Cart cart, Integer productId, boolean incOrDec) {

        try {
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

        } catch (SQLException e) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
        }
    }

    public Integer getCartProductQuantity(Cart cart, int productId) {
        try (PreparedStatement statement = getConn().prepareStatement("SELECT product_quantity FROM carts WHERE id=? AND user_id=? AND product_id=?;")) {
            statement.setInt(1, cart.getId());
            statement.setInt(2, cart.getUser().getId());
            statement.setInt(3, productId);

            ResultSet result = statement.executeQuery();

            int quantity = 0;
            while (result.next()) {

                quantity = result.getInt("product_quantity");

            }
            return quantity;
        } catch (SQLException e) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
        }

        return null;
    }
}
