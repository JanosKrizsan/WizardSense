package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoJDBC extends ConnectionHandler implements ProductDao {

    private static ProductDaoJDBC instance = null;
    private PreparedStatement statement;

    private ProductDaoJDBC() {
    }

    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        try {
            statement = getConn().prepareStatement("INSERT INTO products (name, description, default_price, " +
                    "default_currency, product_category_id, supplier_id, image_src) VALUES (?, ?, ?, ?, ?, ?, ?);");
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setFloat(3, product.getDefaultPrice());
            statement.setString(4, product.getDefaultCurrency().getCurrencyCode());
            statement.setInt(5, product.getProductCategory().getId());
            statement.setInt(6, product.getSupplier().getId());
            statement.setString(7, product.getImageSrc());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public Product find(int id) {
        try {
            statement = getConn().prepareStatement("SELECT * FROM products WHERE id=?;");
            statement.setInt(1, id);

            ResultSet results = statement.executeQuery();

            int productId = 0;
            String prodName = "";
            String prodDesc = "";
            float defPrice = 0;
            String defCurrency = "";
            int categoryId = 0;
            int supplierId = 0;

            while (results.next()){

                productId = results.getInt("id");
                prodName = results.getString("name");
                prodDesc = results.getString("description");
                defPrice = results.getFloat("default_price");
                defCurrency = results.getString("default_currency");
                categoryId= results.getInt("product_category_id");
                supplierId= results.getInt("supplier_id");
            }

           ProductCategory category = ProductCategoryDaoJDBC.getInstance().find(categoryId);
           Supplier supplier = SupplierDaoJDBC.getInstance().find(supplierId);

            Product found = new Product(prodName, defPrice, defCurrency, prodDesc, category, supplier);
            found.setId(productId);

            statement.closeOnCompletion();
            results.close();

            return found;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void remove(int id) {
        try {
            statement = getConn().prepareStatement("DELETE FROM products WHERE id=?;");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public List<Product> getAll() {
        try {
            statement = getConn().prepareStatement("SELECT id FROM products;");
            ResultSet results = statement.executeQuery();

            List<Product> products = new ArrayList<>();

            while (results.next()) {

                int id = results.getInt("id");
                products.add(find(id));

            }

            statement.close();
            results.close();

            return products;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {

        List<Product> product = getAll();
        return product.stream().filter(prod -> prod.getSupplier().getId() == supplier.getId()).collect(Collectors.toList());
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {

        List<Product> product = getAll();
        return product.stream().filter(prod -> prod.getProductCategory().getId() == productCategory.getId()).collect(Collectors.toList());
    }
}
