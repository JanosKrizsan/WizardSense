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
    public void add(ProductCategory category) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("INSERT INTO product_categories (name, description, department) VALUES  (?, ?, ?) RETURNING id;");
        statement.setString(1, category.getName());
        statement.setString(2, category.getDescription());
        statement.setString(3, category.getDepartment());
        ResultSet result = statement.executeQuery();
        int cartId = category.getId();
        while (result.next()) {
            cartId = result.getInt("id");
        }
        category.setId(cartId);
        statement.close();

    }

    @Override
    public ProductCategory find(int id) throws SQLException {
        ProductCategory category;
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM product_categories WHERE id = ?;");
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
        statement.close();

        return category;
    }

    @Override
    public void remove(int id) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("DELETE FROM product_categories WHERE id=?;");
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public List<ProductCategory> getAll() throws SQLException {
        List<ProductCategory> categories = new ArrayList<>();
        PreparedStatement statement = getConn().prepareStatement("SELECT id FROM product_categories");
        ResultSet results = statement.executeQuery();

        while (results.next()) {
            int id = results.getInt("id");
            categories.add(find(id));
        }
        results.close();
        statement.close();
        return categories;
    }

    @Override
    public void removeAll() throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("TRUNCATE product_categories CASCADE ");
        statement.executeUpdate();
        statement.close();
    }
}
