package JDBC;

import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class JDBCDaoTest {

    // Getting the memory DAOs
    private static ProductCategoryDaoJDBC productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private static SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
    private static ProductDaoJDBC productDataStore = ProductDaoJDBC.getInstance();
    private static CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();

    private ProductCategory testCategory = new ProductCategory("Test Category", "Test Department", "Test Description");
    private Supplier testSupplier = new Supplier("Test Supplier", "Test Description");
    private Product testProduct = new Product("TestProd", 0, "USD", "Just a test", testCategory, testSupplier);
    private Cart testCart = new Cart(new TreeMap<>(), null);

    private ProductCategory fakeCategory = new ProductCategory("Fake Category", "Fake Department", "Fake Description");
    private Supplier fakeSupplier = new Supplier("Fake Supplier", "Fake Description");
    private Product fakeProduct = new Product("FakeProd", 0, "USD", "Just a fake", fakeCategory, fakeSupplier);


    static Connection setConnectionData(PGSimpleDataSource dataSource) throws SQLException {

        try {
            ClassLoader cl = Class.forName("com.codecool.shop.config.ConnectionHandler").getClassLoader();

            InputStream inputs = cl.getResourceAsStream("test.properties");

            Properties prop = new Properties();

            if (inputs != null) {
                prop.load(new InputStreamReader(inputs, StandardCharsets.UTF_8));
            }

            dataSource.setURL(prop.getProperty("url"));
            dataSource.setDatabaseName(prop.getProperty("name"));
            dataSource.setUser(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }


        return dataSource.getConnection();
    }

    @BeforeAll
    static void establishConnection(){
        try {
            productCategoryDataStore.setConn(setConnectionData(productCategoryDataStore.getDataSource()));
            supplierDataStore.setConn(setConnectionData(supplierDataStore.getDataSource()));
            productDataStore.setConn(setConnectionData(productDataStore.getDataSource()));
            cartDataStore.setConn(setConnectionData(cartDataStore.getDataSource()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void cleanTheSlate() {
        productDataStore.removeAll();
        supplierDataStore.removeAll();
        productCategoryDataStore.removeAll();
        cartDataStore.removeAll();
    }


    @Test
    void testAddProductCategory() {
        productCategoryDataStore.add(testCategory);
        ProductCategory supposedCategory = productCategoryDataStore.find(testCategory.getId());
        assertEquals(testCategory, supposedCategory);
    }

    @Test
    void testRemoveProductCategory() {
        productCategoryDataStore.add(testCategory);
        productCategoryDataStore.remove(testCategory.getId());
        assertEquals(productCategoryDataStore.getAll(), new ArrayList<ProductCategory>());
    }

    @Test
    void testAddSupplier() {
        supplierDataStore.add(testSupplier);
        Supplier supposedSupplier = supplierDataStore.find(testSupplier.getId());
        assertEquals(testSupplier, supposedSupplier);
    }

    @Test
    void testRemoveSupplier() {
        supplierDataStore.add(testSupplier);
        supplierDataStore.remove(testSupplier.getId());
        assertEquals(supplierDataStore.getAll(), new ArrayList<Supplier>());
    }

    @Test
    void testAddProduct() {
        supplierDataStore.add(testSupplier);
        productCategoryDataStore.add(testCategory);
        productDataStore.add(testProduct);
        Product supposedProduct = productDataStore.find(testProduct.getId());
        assertEquals(testProduct, supposedProduct);
    }

    @Test
    void testFilterBySupplier() {

        supplierDataStore.add(testSupplier);
        productCategoryDataStore.add(testCategory);

        supplierDataStore.add(fakeSupplier);
        productCategoryDataStore.add(fakeCategory);

        productDataStore.add(testProduct);
        productDataStore.add(fakeProduct);
        List<Product> supposedProducts = new ArrayList<Product>() {{
            add(testProduct);
        }};
        assertEquals(productDataStore.getBy(testSupplier), supposedProducts);
    }

    @Test
    void testFilterByCategory() {

        supplierDataStore.add(testSupplier);
        productCategoryDataStore.add(testCategory);

        supplierDataStore.add(fakeSupplier);
        productCategoryDataStore.add(fakeCategory);

        productDataStore.add(testProduct);
        productDataStore.add(fakeProduct);
        List<Product> supposedProducts = new ArrayList<Product>() {{
            add(testProduct);
        }};
        assertEquals(productDataStore.getBy(testCategory), supposedProducts);
    }

    @Test
    void testRemoveProduct() {
        productDataStore.add(testProduct);
        productDataStore.remove(testProduct.getId());
        assertEquals(productDataStore.getAll(), new ArrayList<Product>());
    }

    @Test
    void testAddCart() {
        cartDataStore.add(testCart);
        Cart supposedCart = cartDataStore.find(testCart.getId());
        assertEquals(testCart, supposedCart);
    }

    @Test
    void testRemoveCart() {
        cartDataStore.add(testCart);
        cartDataStore.clearProductFromCart(testCart.getId());
        assertEquals(cartDataStore.getAll(), new ArrayList<Cart>());
    }

    @Test
    void testAddToCart() {
        cartDataStore.add(testCart);
        Cart cartToAddTo = cartDataStore.find(testCart.getId());
        cartToAddTo.addProduct(testProduct);
        HashMap<Product, Integer> supposedMap = new HashMap<Product, Integer>() {{
            put(testProduct, 1);
        }};
        assertEquals(cartToAddTo.getProductList(), supposedMap);

    }

    @Test
    void testRemoveFromCart() {
        cartDataStore.add(testCart);
        Cart cartToAddTo = cartDataStore.find(testCart.getId());
        cartToAddTo.addProduct(testProduct);
        cartToAddTo.removeProduct(testProduct);
        HashMap<Product, Integer> supposedMap = new HashMap<>();
        assertEquals(cartToAddTo.getProductList(), supposedMap);
    }


}