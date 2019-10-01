package com.codecool.shop.config;

import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import org.mindrot.jbcrypt.BCrypt;

public final class Utils {

    private static CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();

    static public String hashPass(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    static public boolean checkPass(String candidate, String password){
        return BCrypt.checkpw(candidate, password);
    }

    public static boolean isJUnitTest() {
        StackTraceElement[] list = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

    public static float getTotalSum(Cart cart) {
        float sum = 0;

        for (Product product : cart.getProductsInCart()) {
            sum += product.getDefaultPrice() * cartDataStore.getCartProductQuantity(cart, product.getId());
        }
        return sum;
    }

}
