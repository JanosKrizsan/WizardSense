package JDBC;

import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;
import com.codecool.shop.dao.implementation.Memory.*;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private Cart testCart = new Cart(new HashMap<>(0), null);

    private ProductCategory fakeCategory = new ProductCategory("Fake Category", "Fake Department", "Fake Description");
    private Supplier fakeSupplier = new Supplier("Fake Supplier", "Fake Description");
    private Product fakeProduct = new Product("FakeProd", 0, "USD", "Just a fake", fakeCategory, fakeSupplier);


    static Connection setConnectionData(PGSimpleDataSource dataSource) throws SQLException {
        dataSource.setDatabaseName("wizardsensetest");
        dataSource.setUser("postgres");
        dataSource.setPassword("19980114");

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

    @AfterEach
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
        ProductCategory supposedCategory = productCategoryDataStore.find(1);
        assertEquals(testCategory, supposedCategory);
    }

    @Test
    void testRemoveProductCategory() {
        productCategoryDataStore.add(testCategory);
        productCategoryDataStore.remove(1);
        assertEquals(productCategoryDataStore.getAll(), new ArrayList<ProductCategory>());
    }

    @Test
    void testAddSupplier() {
        supplierDataStore.add(testSupplier);
        testSupplier.setId(1);
        Supplier supposedSupplier = supplierDataStore.find(1);
        assertEquals(testSupplier, supposedSupplier);
    }

    @Test
    void testRemoveSupplier() {
        supplierDataStore.add(testSupplier);
        supplierDataStore.remove(1);
        assertEquals(supplierDataStore.getAll(), new ArrayList<Supplier>());
    }

    @Test
    void testAddProduct() {
        supplierDataStore.add(testSupplier);
        productCategoryDataStore.add(testCategory);
        productDataStore.add(testProduct);
        Product supposedProduct = productDataStore.find(1);
        assertEquals(testProduct, supposedProduct);
    }

    @Test
    void testFilterBySupplier() {
        productDataStore.add(testProduct);
        productDataStore.add(fakeProduct);
        List<Product> supposedProducts = new ArrayList<Product>() {{
            add(testProduct);
        }};
        assertEquals(productDataStore.getBy(testSupplier), supposedProducts);
        productDataStore.remove(2);
    }

    @Test
    void testFilterByCategory() {
        productDataStore.add(testProduct);
        productDataStore.add(fakeProduct);
        List<Product> supposedProducts = new ArrayList<Product>() {{
            add(testProduct);
        }};
        assertEquals(productDataStore.getBy(testCategory), supposedProducts);
        productDataStore.remove(2);
    }

    @Test
    void testRemoveProduct() {
        productDataStore.add(testProduct);
        productDataStore.remove(1);
        assertEquals(productDataStore.getAll(), new ArrayList<Product>());
    }

    @Test
    void testAddCart() {
        cartDataStore.add(testCart);
        Cart supposedCart = cartDataStore.find(1);
        assertEquals(testCart, supposedCart);
    }

    @Test
    void testRemoveCart() {
        cartDataStore.add(testCart);
        cartDataStore.remove(1);
        assertEquals(cartDataStore.getAll(), new ArrayList<Cart>());
    }

    @Test
    void testAddToCart() {
        cartDataStore.add(testCart);
        Cart cartToAddTo = cartDataStore.find(1);
        cartToAddTo.addProduct(testProduct);
        HashMap<Product, Integer> supposedMap = new HashMap<Product, Integer>() {{
            put(testProduct, 1);
        }};
        assertEquals(cartToAddTo.getProductList(), supposedMap);

    }

    @Test
    void testRemoveFromCart() {
        cartDataStore.add(testCart);
        Cart cartToAddTo = cartDataStore.find(1);
        cartToAddTo.addProduct(testProduct);
        cartToAddTo.removeProduct(testProduct);
        HashMap<Product, Integer> supposedMap = new HashMap<>();
        assertEquals(cartToAddTo.getProductList(), supposedMap);
    }


}