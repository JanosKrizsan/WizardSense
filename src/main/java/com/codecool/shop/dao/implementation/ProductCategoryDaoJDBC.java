package com.codecool.shop.dao.implementation;

import com.codecool.shop.config.ConnectionHandler;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.model.ProductCategory;

import java.util.List;

public class ProductCategoryDaoJDBC extends ConnectionHandler implements GenericQueriesDao<ProductCategory> {

    private static ProductCategoryDaoJDBC instance = null;


    private ProductCategoryDaoJDBC() {
        super();

    }

    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
    }

    @Override
    public ProductCategory find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<ProductCategory> getAll() {
        return null;
    }
}
