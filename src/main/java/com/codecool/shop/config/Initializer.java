package com.codecool.shop.config;

import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.JDBC.*;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.dao.implementation.Memory.SupplierDaoMem;
import com.codecool.shop.model.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;

@WebListener
public class Initializer implements ServletContextListener {
    private ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private GenericQueriesDao<ProductCategory> productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private GenericQueriesDao<Supplier> supplierDataStore = SupplierDaoJDBC.getInstance();
    private GenericQueriesDao<Cart> cartDataStore = CartDaoJDBC.getInstance();
    private GenericQueriesDao<User> userDataStore = UserDaoJDBC.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if(productDataStore.getAll().size() != 0 && userDataStore.getAll().size() > 0){
            fillMeUp();
        }

    }

    private void fillMeUp(){

        //adding the admin user
        User admin = new User("admin", "admin");
        userDataStore.add(admin);

        //setting up a new cart
        Cart cart = new Cart(new HashMap<>(0));
        cartDataStore.add(cart);

        //setting up a new supplier
        Supplier althiev = new Supplier("Althiev's Smithy", "Armor and Weapons");
        supplierDataStore.add(althiev);
        Supplier arnix = new Supplier("Arnix's Scribery", "Scrolls and Books");
        supplierDataStore.add(arnix);
        Supplier eldamar = new Supplier("Eldamar's General Goods", "General Goods");
        supplierDataStore.add(eldamar);

        //setting up a new product category
        ProductCategory scroll = new ProductCategory("Scroll", "Consumable", "A rolled up parchment. Contains various things");
        productCategoryDataStore.add(scroll);
        ProductCategory weapon = new ProductCategory("Weapon", "Equipment", "Items that a character can use for dealing damage");
        productCategoryDataStore.add(weapon);
        ProductCategory potion = new ProductCategory("Potion", "Consumable", "Various liquids enclosed in a bottle.");
        productCategoryDataStore.add(potion);

        //setting up products and printing it
        productDataStore.add(new Product("Potion of Healing", 24, "USD", "A magical liquid capable of healing up wounds with accelerated regeneration.", potion, eldamar));
        productDataStore.add(new Product("Potion of Greater Healing", 46, "USD", "A more potent version of the healing potion.", potion, eldamar));
        productDataStore.add(new Product("Scroll of Frost Ray", 37, "USD", "A scroll of magic containing the 1st level spell: Frost Ray", scroll, arnix));
        productDataStore.add(new Product("Sword of Cinders", 123, "USD", "A finely crafted longsword capable of producing minor flames on it's blade upon speaking the command word.", weapon, althiev));
        productDataStore.add(new Product("Sword of Sands", 170, "USD", "A blade found in the desert, reinforced by Althiev.", weapon, althiev));
        productDataStore.add(new Product("Scroll of Illusions", 76, "USD", "A scroll containing the 3rd level spell: Major Image", scroll, arnix));
        productDataStore.add(new Product("Ointment of Kvaarn", 87, "USD", "A small flask of iron containing a syrupy liquid. Said to cure almost all common diseases.", potion, eldamar));

    }
}
