package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandling;
import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.JDBC.*;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    private ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private UserDaoJDBC userDataStore = UserDaoJDBC.getInstance();
    private SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
    private ProductCategoryDaoJDBC productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private List<Product> defaultProds = null;

    private void filter(GenericQueriesDao<Supplier> sDS, GenericQueriesDao<ProductCategory> pCD, ProductDao pDS, HttpServletRequest req) {
        List<String> headers = Collections.list(req.getParameterNames());

        if (headers.contains("filter")) {
            List<Character> filtered = req.getParameter("filter").chars().mapToObj(e -> (char) e).collect(Collectors.toList());
            int filterId = Integer.parseInt(filtered.get(filtered.size() - 1).toString());
            filtered.remove(filtered.size() - 1);

            StringBuilder sBuilder = new StringBuilder();
            for (Character character : filtered) {
                sBuilder.append(character);
            }

            String filterName = sBuilder.toString().trim();
            List<Supplier> suppliers = sDS.getAll().stream().filter(supplier -> supplier.getName().equals(filterName)).collect(Collectors.toList());


            if (suppliers.size() > 0) {
                defaultProds = pDS.getBy(sDS.find(filterId));
            } else {
                defaultProds = pDS.getBy(pCD.find(filterId));
            }
        } else if (headers.contains("reset")) {
            defaultProds = null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        int cartSize = 0;
        if (session.getAttribute("userID") != null) {
            int userID = (int) session.getAttribute("userID");
            Cart cart = cartDataStore.getCartByUserId(userID);

            cartSize = cart != null ? cartDataStore.getCartByUserId(userID).getSumOfProducts() : 0;
        }


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());


        filter(supplierDataStore, productCategoryDataStore, productDataStore, req);
        context.setVariable("categories", productCategoryDataStore.getAll());
        context.setVariable("suppliers", supplierDataStore.getAll());
        context.setVariable("cartSize", cartSize);
        context.setVariable("products", defaultProds != null ? defaultProds : productDataStore.getAll());
        context.setVariable("userID", session.getAttribute("userID"));
        context.setVariable("userName", session.getAttribute("userName"));


        engine.process("product/index.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<String> headers = Collections.list(req.getParameterNames());

        if (headers.contains("product")) {

            try {
                HttpSession session = req.getSession();

                int userId = (int) session.getAttribute("userID");
                User user = userDataStore.find(userId);

                if (user != null) {
                    int productId = Integer.parseInt(req.getParameter("product"));
                    Product product = productDataStore.find(productId);


                    TreeMap<Product, Integer> products = new TreeMap<>();
                    products.put(product, 1);


                    Cart cartToCheck = cartDataStore.getCartByUserId(userId);

                    Cart newCart = new Cart(products, user);
                    newCart.setId(userId);

                    if (cartToCheck == null) {
                        cartDataStore.add(newCart);
                    } else {

                        if(cartDataStore.getCartProductQuantity(cartToCheck, productId) >= 1){
                            cartDataStore.increaseOrDecreaseQuantity(cartToCheck, productId, true);
                        } else {
                            cartDataStore.add(newCart);
                        }
                    }

                }
            } catch (NumberFormatException e) {
                new ErrorHandling().ExceptionOccurred(e);
            }
        }


        doGet(req, resp);
    }

}