package com.codecool.shop.config;

import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.JDBC.*;
import com.codecool.shop.model.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@WebListener
public class Initializer implements ServletContextListener {
    private ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private GenericQueriesDao<ProductCategory> productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private GenericQueriesDao<Supplier> supplierDataStore = SupplierDaoJDBC.getInstance();
    private GenericQueriesDao<User> userDataStore = UserDaoJDBC.getInstance();
    private ErrorHandling handler = new ErrorHandling();


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ErrorHandling.LOGGER.info("Trying to connect to database...");
        try {
            ConnectionHandler.connect();
            ErrorHandling.LOGGER.info("Connection Established Successfully!");
        } catch (SQLException e) {
            ErrorHandling.LOGGER.info("Connection Failed. Reason: \n" + e);
        }
        try {
            if (productDataStore.getAll().size() == 0 && userDataStore.getAll().size() == 0) {
                fillMeUp();
            }
        } catch (SQLException e) {
            handler.ExceptionOccurred(e);
        }


    }

    private void fillMeUp() {

        ErrorHandling.LOGGER.info("Filling up database with values...");
        try {
            //adding the admin user
            User admin = new User("admin", "admin");
            userDataStore.add(admin);

            //setting up a new cart

            //setting up a new supplier
            Supplier althiev = new Supplier("Althiev's Smithy", "Armor and Weapons");
            althiev.setId(1);
            supplierDataStore.add(althiev);
            Supplier arnix = new Supplier("Arnix's Scribery", "Scrolls and Books");
            arnix.setId(2);
            supplierDataStore.add(arnix);
            Supplier eldamar = new Supplier("Eldamar's General Goods", "General Goods");
            eldamar.setId(3);
            supplierDataStore.add(eldamar);

            //setting up a new product category
            ProductCategory scroll = new ProductCategory("Scroll", "Consumable", "A rolled up parchment. Contains various things");
            scroll.setId(1);
            productCategoryDataStore.add(scroll);
            ProductCategory weapon = new ProductCategory("Weapon", "Equipment", "Items that a character can use for dealing damage");
            weapon.setId(2);
            productCategoryDataStore.add(weapon);
            ProductCategory potion = new ProductCategory("Potion", "Consumable", "Various liquids enclosed in a bottle.");
            potion.setId(3);
            productCategoryDataStore.add(potion);

            //setting up products and printing it

            List<Product> productList = new ArrayList<>();
            Product PotionOfHealing = new Product("Potion of Healing", 24, "USD", "A magical liquid capable of healing up wounds with accelerated regeneration.", potion, eldamar);
            PotionOfHealing.setImageSrc("https://i.imgur.com/Vjpfgpb.png");
            productList.add(PotionOfHealing);
            Product PotionOfGreaterHealing = new Product("Potion of Greater Healing", 46, "USD", "A more potent version of the healing potion.", potion, eldamar);
            PotionOfGreaterHealing.setImageSrc("https://i.imgur.com/6XEN5i2.png");
            productList.add(PotionOfGreaterHealing);
            Product ScrollOfFrostRay = new Product("Scroll of Frost Ray", 37, "USD", "A scroll of magic containing the 1st level spell: Frost Ray", scroll, arnix);
            ScrollOfFrostRay.setImageSrc("https://i.imgur.com/adNtxWQ.png");
            productList.add(ScrollOfFrostRay);
            Product SwordOfCinders = new Product("Sword of Cinders", 123, "USD", "A finely crafted longsword capable of producing minor flames on it's blade upon speaking the command word.", weapon, althiev);
            SwordOfCinders.setImageSrc("https://i.imgur.com/Y4DUl1u.png");
            productList.add(SwordOfCinders);
            Product SwordOfSands = new Product("Sword of Sands", 170, "USD", "A blade found in the desert, reinforced by Althiev.", weapon, althiev);
            SwordOfSands.setImageSrc("https://i.imgur.com/zB0GNjq.png");
            productList.add(SwordOfSands);
            Product ScrollOfIllusions = new Product("Scroll of Illusions", 76, "USD", "A scroll containing the 3rd level spell: Major Image", scroll, arnix);
            ScrollOfIllusions.setImageSrc("https://i.imgur.com/RyBK1LO.png");
            productList.add(ScrollOfIllusions);
            Product OintmentOfKVaarn = new Product("Ointment of Kvaarn", 87, "USD", "A small flask of iron containing a syrupy liquid. Said to cure almost all common diseases.", potion, eldamar);
            OintmentOfKVaarn.setImageSrc("https://i.imgur.com/m4ACjLh.png");
            productList.add(OintmentOfKVaarn);

            for (Product prod : productList) {
                productDataStore.add(prod);
            }
        } catch (SQLException e) {
            handler.ExceptionOccurred(e);
        }

        ErrorHandling.LOGGER.info("Initialization all done, ready to go!");
    }
}
