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
            Supplier trickstertrinkets = new Supplier("Trickster Trinkets", "Wearables and Stylables");
            trickstertrinkets.setId(5);
            supplierDataStore.add(trickstertrinkets);
            Supplier aldakar = new Supplier("Aldakar & Sons", "Music and Noise Makers");
            aldakar.setId(6);
            supplierDataStore.add(aldakar);
            Supplier adventurers = new Supplier("Adventurers' Guild", "All Things From Beyond The Pale");
            adventurers.setId(6);
            supplierDataStore.add(adventurers);
            Supplier rodolpho = new Supplier("Wands of Rodolpho", "Wands, Staves, Rods, for The Lovers of Magic");
            rodolpho.setId(6);
            supplierDataStore.add(rodolpho);

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
            ProductCategory wondrous = new ProductCategory("Wondrous", "All", "Items of the weird sort.");
            wondrous.setId(10);
            productCategoryDataStore.add(wondrous);
            ProductCategory instrument = new ProductCategory("Instruments", "Equipment", "For those wishing to play a tune, deadly or not.");
            instrument.setId(11);
            productCategoryDataStore.add(instrument);


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

            Product BookOfExaltedDeeds = new Product("Book of Exalted Deeds", 266, "USD", "A tome of magic, recants and enacts deeds of good and mighty.", book, reinar);
            BookOfExaltedDeeds.setImageSrc("https://i.imgur.com/oi5hYDD.png");
            productList.add(BookOfExaltedDeeds);

            Product BookOfVileDarkness = new Product("Book of Vile Darkness", 412, "USD", "Another tome of magic, quite opposite to its brother \"The Book of Exalted Deeds\"", book, reinar);
            BookOfVileDarkness.setImageSrc("https://i.imgur.com/K3rdBE1.png");
            productList.add(BookOfVileDarkness);

            Product AmultetOfHealth = new Product("Amulet of Health", 229, "USD", "While wearing this amulet, your life-essence is increased.", trinket, trickstertrinkets);
            AmultetOfHealth.setImageSrc("https://i.imgur.com/kwhXG90.png");
            productList.add(AmultetOfHealth);

            Product AmuletOfPlanes = new Product("Amulet of Planes", 99, "USD", "While wearing this, you may *or accidentally not* travel to another plane of existence.", trinket, trickstertrinkets);
            AmuletOfPlanes.setImageSrc("https://i.imgur.com/OGEtebD.png");
            productList.add(AmuletOfPlanes);

            Product AnimatedShield = new Product("Animated Shield", 228, "USD", "A living shield, which protects you automatically.", armor, althiev);
            AnimatedShield.setImageSrc("https://i.imgur.com/iW5nQ4D.png");
            productList.add(AnimatedShield);

            Product AnstruthHarp = new Product("Anstruth's Harp", 349, "USD", "Perfect to lull the mind and cull the weary enemy. For the experienced Bard!", instrument, aldakar);
            AnstruthHarp.setImageSrc("https://i.imgur.com/e8pHAXe.png");
            productList.add(AnstruthHarp);

            Product ArmorOfInvulerability = new Product("Armor of Invulnerability", 1229, "USD", "While wearing this, you cannot be hurt by physical means. Words and magic still hurt.", armor, adventurers);
            ArmorOfInvulerability.setImageSrc("https://i.imgur.com/DbMX8tc.png");
            productList.add(ArmorOfInvulerability);

            Product ArmorOfResistance = new Product("Armor of Resistance", 349, "USD", "Wearing this makes you resistant to a type of environmental condition, although you never know which one.", armor, adventurers);
            ArmorOfResistance.setImageSrc("https://i.imgur.com/6zpMnlX.png");
            productList.add(ArmorOfResistance);

            Product AxeOfDwarvishLords = new Product("Axe Of Dwarvish Lords", 750, "USD", "And axe from the first age of Dwarves, forged with lava from the Earth's core.", weapon, althiev);
            AxeOfDwarvishLords.setImageSrc("https://i.imgur.com/XgxYhPQ.png");
            productList.add(AxeOfDwarvishLords);

            Product BadgeOfTheWatch = new Product("Badge of The Watch", 55, "USD", "A badge which transforms to the badge of local peacekeeping officers, wherever you may be.", trinket, adventurers);
            BadgeOfTheWatch.setImageSrc("https://i.imgur.com/coICyM2.png");
            productList.add(BadgeOfTheWatch);

            Product BagOfBeans = new Product("Bag of Beans", 50, "USD", "A pack of exploding beans.", wondrous, adventurers);
            BagOfBeans.setImageSrc("https://i.imgur.com/X6lxpzL.png");
            productList.add(BagOfBeans);

            Product BeltOfDwarvenkind = new Product("Belt of Dwarvenkind", 182, "USD", "You can speak Dwarven, see in the dark, and will gain a bit of strength relative to yours.", armor, althiev);
            BeltOfDwarvenkind.setImageSrc("https://i.imgur.com/tpNW4Qa.png");
            productList.add(BeltOfDwarvenkind);

            Product BeltOfGiantStrength = new Product("Belt of Giant Strength", 222, "USD", "", armor, adventurers);
            BeltOfGiantStrength.setImageSrc("https://i.imgur.com/YwTNfNn.png");
            productList.add(BeltOfGiantStrength);

            Product CubeOfForce = new Product("Cube of Force", 340, "USD", "Can create a barrier of force dependant on the side pushed.", wondrous, adventurers);
            CubeOfForce.setImageSrc("https://i.imgur.com/55DgG92.png");
            productList.add(CubeOfForce);

            Product DragonSlayer = new Product("Dragonslayer", 199, "USD", "Can pass through a dragon's or dragonkin's scales with ease, inflicting ethereal damage as well.", weapon, adventurers);
            DragonSlayer.setImageSrc("https://i.imgur.com/gtz0xVb.png");
            productList.add(DragonSlayer);

            Product Drown = new Product("Drown", 120, "USD", "A Trident forged by the waterlord Ozayr, for his three-headed son Tritraties. Can command fishes.", weapon, adventurers);
            Drown.setImageSrc("https://i.imgur.com/CQzfcV8.png");
            productList.add(Drown);

            Product ElvenChain = new Product("Elven Chainmail", 100, "USD", "An elven chainmail armor infused with magic resistance.", armor, althiev);
            ElvenChain.setImageSrc("https://i.imgur.com/UsambcB.png");
            productList.add(ElvenChain);

            Product IronFang = new Product("Ironfang", 116, "USD", "A warpick, with the power of Ogremoch. Also useful for mining.", weapon, althiev);
            IronFang.setImageSrc("https://i.imgur.com/m5Z0xmN.png");
            productList.add(IronFang);

            Product Matalotok = new Product("Matalotok", 210, "USD", "A nether-realm warhammer, surrounded by mist and cold.", weapon, althiev);
            Matalotok.setImageSrc("https://i.imgur.com/tbIVFnD.png");
            productList.add(Matalotok);

            Product OrbOfDragonKind = new Product("Orb of Dragonkind", 1499, "USD", "An orb with an Evil Dragon's essence. It is quite useful for many deeds revealed or not.", trinket, adventurers);
            OrbOfDragonKind.setImageSrc("https://i.imgur.com/v6ipte1.png");
            productList.add(OrbOfDragonKind);

            Product RingOfAirElementalCommand = new Product("Ring of Air Elemental Command", 480, "USD", "As the name states, you can control an Air Elemental. Only one at a time, mind you!", trinket, trickstertrinkets);
            RingOfAirElementalCommand.setImageSrc("https://i.imgur.com/fWfCuix.png");
            productList.add(RingOfAirElementalCommand);

            Product StaffOfMagi = new Product("Staff of Magi", 100, "USD", "Provides invisibility while held, at the cost of your life-force. Or Mana, whichever you prefer.", staff, rodolpho);
            StaffOfMagi.setImageSrc("https://i.imgur.com/JmQMwFB.png");
            productList.add(StaffOfMagi);

            Product StoneOfGolorr = new Product("Stone of Golorr", 7899, "USD", "With the usage of this stone, you can recall things long gone from the ages, by the eternal mind of Golorr.", wondrous, adventurers);
            StoneOfGolorr.setImageSrc("https://i.imgur.com/Z8KqJDD.png");
            productList.add(StoneOfGolorr);

            Product VorpalSword = new Product("Vorpal Sword", 140, "USD", "Slashes not just the physical body, but mind and soul as well", weapon, althiev);
            VorpalSword.setImageSrc("https://i.imgur.com/zJeSYrk.png");
            productList.add(VorpalSword);

            Product WandOfOrcus = new Product("Wand of Orcus", 380, "USD", "A black obsidian and iron rod topped with the skull of a human hero once slain by Orcus.", wand, rodolpho);
            WandOfOrcus.setImageSrc("https://i.imgur.com/Vv2eR2R.png");
            productList.add(WandOfOrcus);

            Product WindVane = new Product("Windvane", 88, "USD", "A spear, also can open portals to other realms.", weapon, adventurers);
            WindVane.setImageSrc("https://i.imgur.com/ILPUuJn.png");
            productList.add(WindVane);

            Product ImmovableRod = new Product("Immovable Rod", 255, "USD", "With the push of a button, you can make this rod immovable, defying even physical laws.", rod, rodolpho);
            ImmovableRod.setImageSrc("https://i.imgur.com/3W2HbA2.png");
            productList.add(ImmovableRod);

            Product RodOfRulership = new Product("Rod of Rulership", 192, "USD", "Enables the wielder to impose his/her will on others.", rod, rodolpho);
            RodOfRulership.setImageSrc("https://i.imgur.com/RFbKq2p.png");
            productList.add(RodOfRulership);

            Product ShieldOfTheHiddenLord = new Product("Shield of The Hidden Lord", 399, "USD", "Of gems and mithril. The shield may or may not speak.", armor, adventurers);
            ShieldOfTheHiddenLord.setImageSrc("https://i.imgur.com/Vs2NyHj.png");
            productList.add(ShieldOfTheHiddenLord);

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
