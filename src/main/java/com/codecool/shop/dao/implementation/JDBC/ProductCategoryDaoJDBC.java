package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.ProductCategory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJDBC extends ConnectionHandler implements GenericQueriesDao<ProductCategory> {

    private static ProductCategoryDaoJDBC instance = null;


    private ProductCategoryDaoJDBC() {
    }

    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        try (PreparedStatement statement = getConn().prepareStatement("INSERT INTO product_categories (name, description, department) VALUES  (?, ?, ?) RETURNING id;")) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setString(3, category.getDepartment());
            ResultSet result = statement.executeQuery();
            int cartId = category.getId();
            while (result.next()) {
                cartId = result.getInt("id");
            }
            category.setId(cartId);

        } catch (SQLException e) {
            ExceptionOccurred(e);
        }
    }

    @Override
    public ProductCategory find(int id) {
        ProductCategory category = null;
        try (PreparedStatement statement = getConn().prepareStatement("SELECT * FROM product_categories WHERE id = ?;")) {
            statement.setInt(1, id);

            ResultSet results = statement.executeQuery();

            String name = "";
            String department = "";
            String description = "";

            while (results.next()) {
                name = results.getString("name");
                department = results.getString("department");
                description = results.getString("description");
            }

            category = new ProductCategory(name, department, description);
            category.setId(id);

        } catch (SQLException e) {
            ExceptionOccurred(e);
        }
        return category;
    }

    @Override
    public void remove(int id) {
        try (PreparedStatement statement = getConn().prepareStatement("DELETE FROM product_categories WHERE id=?;")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            ExceptionOccurred(e);
        }
    }

    @Override
    public List<ProductCategory> getAll() {
        List<ProductCategory> categories = new ArrayList<>();
        try (PreparedStatement statement = getConn().prepareStatement("SELECT id FROM product_categories");
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                int id = results.getInt("id");
                categories.add(find(id));
            }
            results.close();

            return categories;
        } catch (SQLException e) {
            ExceptionOccurred(e);
        }
        return categories;
    }

    @Override
    public void removeAll() {
        try (PreparedStatement statement = getConn().prepareStatement("TRUNCATE product_categories CASCADE ")) {
            statement.executeUpdate();
        } catch (
                SQLException e) {
            ExceptionOccurred(e);
        }
    }
}
