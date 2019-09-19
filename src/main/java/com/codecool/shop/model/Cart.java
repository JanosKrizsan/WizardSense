package com.codecool.shop.model;

import java.util.HashMap;
import java.util.Set;

public class Cart {

    private int id;
    private User user;
    private HashMap<Product, Integer> productList;

    public Cart(HashMap<Product, Integer> productList, User user) {
        this.productList = productList;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Product, Integer> getProductList() {
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

    public Integer getQuantityOfProduct(Product productToSearch) {
        return productList.get(productToSearch);
    }

    public Set<Product> getProductsInCart() {
        return productList.keySet();
    }

    public Integer getSumOfProducts(){

        int products = 0;
        for (Product product :
                getProductsInCart()) {
            products += productList.get(product);
        }
        return products;
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

