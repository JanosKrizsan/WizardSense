package com.codecool.shop.dao;//            for (Product product : cartDataStore.getAll()) {
//                if (product.getId() == productId) {
//                    product.setQuantity(1);
//                    found = true;
//                    break;
//                }
//            }
//
//            if (!found) {
//            }

import com.codecool.shop.model.Product;

import java.util.List;

public interface CartDao {

    void add(Product product);
    Product find(int id);
    void remove(int id);

    List<Product> getAll();
}
