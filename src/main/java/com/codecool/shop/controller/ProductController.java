package com.codecool.shop.controller;

import com.codecool.shop.dao.GenericQueriesDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductCategoryDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;

import com.codecool.shop.config.TemplateEngineUtil;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLDataException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

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
        ProductDao productDataStore = ProductDaoJDBC.getInstance();
        SupplierDaoJDBC supplierDataStore = SupplierDaoJDBC.getInstance();
        ProductCategoryDaoJDBC productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();

        int cartSize = CartDaoJDBC.getInstance().find(1).getSumOfProducts();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());


        filter(supplierDataStore ,productCategoryDataStore, productDataStore, req);
        context.setVariable("categories", productCategoryDataStore.getAll());
        context.setVariable("suppliers", supplierDataStore.getAll());
        context.setVariable("cartSize", cartSize);
        context.setVariable("products", defaultProds != null ? defaultProds : productDataStore.getAll());


        engine.process("product/index.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<String> headers = Collections.list(req.getParameterNames());

        if (headers.contains("product")){
            ;
            try {
                ProductDao productDataStore = ProductDaoJDBC.getInstance();
                CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
                int productId = Integer.parseInt(req.getParameter("product"));
                Product product = productDataStore.find(productId);

                HttpSession session = req.getSession();

                HashMap<Product, Integer> products = new HashMap<>();
                products.put(product, 1);

                if(session.getAttribute("user") != null) {

                    List<Cart> carts = cartDataStore.getAll().stream()
                            .filter(cart -> cart.getProductList()
                                    .containsKey(product))
                            .filter(cart -> cart.getUser().equals(session.getAttribute("user")))
                            .collect(Collectors.toList());

                    if(carts.size() == 1) {
                        cartDataStore.increaseProductQuantity(carts.get(0), product);
                    } else if (carts.size() < 1) {
                        Cart cart = new Cart(products, session.getAttribute("user"));
                    } else {
                        throw new SQLDataException("Duplicate data in database, please revise");
                    }
                }



            } catch (NumberFormatException | SQLDataException e) {
                System.out.println(e);
            }
        }


        doGet(req, resp);
    }

}

// // Alternative setting of the template context
// Map<String, Object> params = new HashMap<>();
// params.put("category", productCategoryDataStore.find(1));
// params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
// context.setVariables(params);