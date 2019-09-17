package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.Memory.CartDaoMem;
import com.codecool.shop.dao.implementation.Memory.ProductDaoMem;
import com.codecool.shop.model.Cart;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(urlPatterns = {"/shopping-cart"})
public class CartController extends HttpServlet {

    private void addOrRemoveProduct(HttpServletRequest req){
        List<String> headers = Collections.list(req.getParameterNames());
        ProductDaoMem products = ProductDaoMem.getInstance();
        Cart cart = CartDaoMem.getInstance().find(0);

        if (headers.contains("increase")) {
            int prodId = Integer.parseInt(req.getParameter("increase"));
            cart.addProduct(products.find(prodId));
        }
        else if (headers.contains("decrease")) {
            int prodId = Integer.parseInt(req.getParameter("decrease"));
            cart.removeProduct(products.find(prodId));
        }
    }

    private float getTotalSum(Cart cart) {
        float result = 0;
        List<Float> priceSums = cart.getProductsInCart().stream().map(p -> p.getDefaultPrice() * cart.getQuantityOfProduct(p)).collect(Collectors.toList());
        for (float price : priceSums){
            result += price;
        }
        return result;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cart cart = CartDaoMem.getInstance().find(0);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        addOrRemoveProduct(req);

        context.setVariable("cart" , cart);
        context.setVariable("totalSum", getTotalSum(cart));

        engine.process("product/shopping-cart.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }

}

// // Alternative setting of the template context
// Map<String, Object> params = new HashMap<>();
// params.put("category", productCategoryDataStore.find(1));
// params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
// context.setVariables(params);