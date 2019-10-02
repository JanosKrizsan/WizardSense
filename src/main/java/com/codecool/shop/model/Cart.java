package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeMap;

public class Cart {

    private int id;
    private User user;
    private TreeMap<Product, Integer> productList;

    public Cart(TreeMap<Product, Integer> productList, User user) {
        this.productList = productList;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TreeMap<Product, Integer> getProductList() {
        return productList;
    }

    public void addProduct(Product product) {
        if (!productList.containsKey(product)) {
            productList.put(product, 1);
        } else {
            productList.put(product, productList.get(product) + 1);
        }
    }

    public void removeProduct(Product product) {
        if (productList.get(product) <= 1) {
            productList.remove(product);
        } else {
            productList.put(product, productList.get(product) - 1);
        }
    }

    public Integer getQuantityOfProduct(Product productToSearch) throws SQLException {
        return (CartDaoJDBC.getInstance().getCartProductQuantity(this, productToSearch.getId()));
    }

    public Set<Product> getProductsInCart() {
        return productList.keySet();
    }

    public Integer getSumOfProducts(){

        int productCount = 0;
        for (Integer value : productList.values()) {
            productCount += value;

        }
        return productCount;
    }


    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                "product list: %2$s", id, productList);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }
}

