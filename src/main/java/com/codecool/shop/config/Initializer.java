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
import java.util.HashMap;

@WebListener
public class Initializer implements ServletContextListener {
    private ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private GenericQueriesDao<ProductCategory> productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private GenericQueriesDao<Supplier> supplierDataStore = SupplierDaoJDBC.getInstance();
    private GenericQueriesDao<User> userDataStore = UserDaoJDBC.getInstance();
    private GenericQueriesDao<UserAddress> addressDataStore = UserAddressDaoJDBC.getInstance();
    private ErrorHandler handler = new ErrorHandler();


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ErrorHandler.LOGGER.info("Trying to connect to database...");
        try {
            ConnectionHandler.connect();
            ErrorHandler.LOGGER.info("Connection Established Successfully!");
        } catch (SQLException e) {
            ErrorHandler.LOGGER.info("Connection Failed. Reason: \n" + e);
        }
        try {
            if (productDataStore.getAll().size() == 0 && userDataStore.getAll().size() == 0) {
                fillMeUp();
            }
        } catch (SQLException e) {
            ErrorHandler.ExceptionOccurred(e);
        }


    }

    private void fillMeUp() {

        ErrorHandler.LOGGER.info("Filling up database with values...");
        try {
            //adding the admin user
            User admin = new User("admin", "admin");
            userDataStore.add(admin);

            //setting up a new cart

            //setting up a new supplier
            Supplier althiev = new Supplier("Althiev's Smithy", "Armor and Weapons");
            althiev.setId(1);
            supplierDataStore.add(althiev);
            Supplier arnix = new Supplier("Arnix's Scribery", "Scrolls and Magical Scripts");
            arnix.setId(2);
            supplierDataStore.add(arnix);
            Supplier eldamar = new Supplier("Eldamar's General Goods", "General Goods");
            eldamar.setId(3);
            supplierDataStore.add(eldamar);
            Supplier reinar = new Supplier("Reinar's Tomes", "Tomes Shoppe");
            reinar.setId(4);
            supplierDataStore.add(reinar);

            //setting up a new product category
            ProductCategory scroll = new ProductCategory("Scroll", "Consumable", "A rolled up parchment. Contains various things");
            scroll.setId(1);
            productCategoryDataStore.add(scroll);
            ProductCategory weapon = new ProductCategory("Weapon", "Equipment", "Items that a character can use for dealing damage");
            weapon.setId(2);
            productCategoryDataStore.add(weapon);
            ProductCategory potion = new ProductCategory("Potion", "Equipment", "Various liquids enclosed in a bottle.");
            potion.setId(3);
            productCategoryDataStore.add(potion);
            ProductCategory armor = new ProductCategory("Armor", "Consumable", "Things of defence, for a frail body.");
            armor.setId(4);
            productCategoryDataStore.add(armor);
            ProductCategory staff = new ProductCategory("Staff", "Equipment", "For those wanting to hold a big staff instead of two small ones.");
            staff.setId(5);
            productCategoryDataStore.add(staff);
            ProductCategory wand = new ProductCategory("Wand", "Equipment", "One-handed staves for the lazy mage.");
            wand.setId(6);
            productCategoryDataStore.add(wand);
            ProductCategory rod = new ProductCategory("Rod", "Equipment", "Like a wand, but pointier.");
            rod.setId(6);
            productCategoryDataStore.add(rod);
            ProductCategory trinket = new ProductCategory("Trinket", "Equipment", "Stylish and deadly at the same time.");
            trinket.setId(8);
            productCategoryDataStore.add(trinket);
            ProductCategory book = new ProductCategory("Book", "Consumable", "A tome full of knowledge and mastery.");
            weapon.setId(9);
            productCategoryDataStore.add(book);


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

            Product BookOfExaltedDeeds = new Product("Book of Exalted Deeds", 266, "USD", "description", book, reinar);
            BookOfExaltedDeeds.setImageSrc("https://imgur.com/oi5hYDD");
            productList.add(BookOfExaltedDeeds);

            Product BookOfVileDarkness = new Product("Book of Vile Darkness", 412, "USD", "description", book, reinar);
            BookOfVileDarkness.setImageSrc("https://imgur.com/K3rdBE1");
            productList.add(BookOfVileDarkness);

//            Product AmultetOfHealth = new Product("Amulet of Health");
//            AmultetOfHealth.setImageSrc();
//            productList.add(AmultetOfHealth);
//
//            Product AmuletOfPlanes = new Product("Amulet of Planes");
//            AmuletOfPlanes.setImageSrc();
//            productList.add(AmuletOfPlanes);
//
//            Product AnimatedShield = new Product("Animated Shield");
//            AnimatedShield.setImageSrc();
//            productList.add(AnimatedShield);
//
//            Product AnstruthHarp = new Product("Anstruth's Harp");
//            AnstruthHarp.setImageSrc();
//            productList.add(AnstruthHarp);
//
//            Product ArmorOfInvulerability = new Product("Armor of Invulnerability");
//            ArmorOfInvulerability.setImageSrc();
//            productList.add(ArmorOfInvulerability);
//
//            Product ArmorOfResistance = new Product("Armor of Resistance");
//            ArmorOfResistance.setImageSrc();
//            productList.add(ArmorOfResistance);
//
//            Product AxeOfDwarvishLords = new Product("Axe Of Dwarvish Lords");
//            AxeOfDwarvishLords.setImageSrc();
//            productList.add(AxeOfDwarvishLords);
//
//            Product BadgeOfTheWatch = new Product("Badge of The Watch");
//            BadgeOfTheWatch.setImageSrc();
//            productList.add(BadgeOfTheWatch);
//
//            Product BagOfBeans = new Product("Bag of Beans");
//            BagOfBeans.setImageSrc();
//            productList.add(BagOfBeans);
//
//            Product BeltOfDwarvenkind = new Product("Belt of Dwarvenkind");
//            BeltOfDwarvenkind.setImageSrc();
//            productList.add(BeltOfDwarvenkind);
//
//            Product BeltOfGiantStrength = new Product("Belt of Giant Strength");
//            BeltOfGiantStrength.setImageSrc();
//            productList.add(BeltOfGiantStrength);
//
//            Product CubeOfForce = new Product("Cube of Force");
//            CubeOfForce.setImageSrc();
//            productList.add(CubeOfForce);
//
//            Product DragonSlayer = new Product("Dragonslayer");
//            DragonSlayer.setImageSrc();
//            productList.add(DragonSlayer);
//
//            Product Drown = new Product("Drown");
//            Drown.setImageSrc();
//            productList.add(Drown);
//
//            Product ElvenChain = new Product("Elven Chainmail");
//            ElvenChain.setImageSrc();
//            productList.add(ElvenChain);
//
//            Product IronFang = new Product("Ironfang");
//            IronFang.setImageSrc();
//            productList.add(IronFang);
//
//            Product Matalotok = new Product("Matalotok");
//            Matalotok.setImageSrc();
//            productList.add(Matalotok);
//
//            Product OrbOfDragonKind = new Product("Orb of Dragonkind");
//            OrbOfDragonKind.setImageSrc();
//            productList.add(OrbOfDragonKind);
//
//            Product RingOfAirElementalCommand = new Product("Ring of Air Elemental Command");
//            RingOfAirElementalCommand.setImageSrc();
//            productList.add(RingOfAirElementalCommand);
//
//            Product StaffOfMagi = new Product("Staff of Magi");
//            StaffOfMagi.setImageSrc();
//            productList.add(StaffOfMagi);
//
//            Product StoneOfGolorr = new Product("Stone of Golorr");
//            StoneOfGolorr.setImageSrc();
//            productList.add(StoneOfGolorr);
//
//            Product VorpalSword = new Product("Vorpal Sword");
//            VorpalSword.setImageSrc();
//            productList.add(VorpalSword);
//
//            Product WandOfOrcus = new Product("Wand of Orcus");
//            WandOfOrcus.setImageSrc();
//            productList.add(WandOfOrcus);
//
//            Product WindVane = new Product("Windvane");
//            WindVane.setImageSrc();
//            productList.add(WindVane);

            for (Product prod : productList) {
                productDataStore.add(prod);
            }

            HashMap<String, String> adminAddress = new HashMap<>();
            adminAddress.put("name", "Admin Adminovich von Adminsky");
            adminAddress.put("eMail", "adminsky@adminmail.com");
            adminAddress.put("phoneNumber", "+36-420-0000");
            adminAddress.put("country", "Adminia");
            adminAddress.put("city", "Adminville");
            adminAddress.put("zipCode", "1337");
            adminAddress.put("address", "Adamant Street 42");

            addressDataStore.add(new UserAddress(adminAddress, admin.getId()));

        } catch (SQLException e) {
            ErrorHandler.LOGGER.info("Initialization failed!");
            ErrorHandler.ExceptionOccurred(e);
        }

        ErrorHandler.LOGGER.info("Initialization all done, ready to go!");


    }
}
