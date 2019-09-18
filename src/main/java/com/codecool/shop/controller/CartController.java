package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(urlPatterns = {"/shopping-cart"})
public class CartController extends HttpServlet {
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private ProductDaoJDBC productDataStore = ProductDaoJDBC.getInstance();

    private void addOrRemoveProduct(HttpServletRequest req){
        List<String> headers = Collections.list(req.getParameterNames());

        HttpSession session = req.getSession();

        int userId = (int)session.getAttribute("userID");
        Cart cart = cartDataStore.getCartByUserId(userId);

        if (headers.contains("increase")) {
            int prodId = Integer.parseInt(req.getParameter("increase"));
            cart.addProduct(productDataStore.find(prodId));
            cartDataStore.add(cart);
        }
        else if (headers.contains("decrease")) {
            int prodId = Integer.parseInt(req.getParameter("decrease"));
            session.setAttribute("decrease", prodId);
            cart.removeProduct(productDataStore.find(prodId));
            cartDataStore.remove(cart.getId());
            session.removeAttribute("decrease");
        }
    }

    private float getTotalSum(Cart cart) {
        float sum = 0;

        for (Product product : cart.getProductList().keySet()) {
            sum += product.getDefaultPrice() * cart.getProductList().get(product);
        }
        return sum;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession();
        //TODO user nonexistent case
        int userId = (int)session.getAttribute("userID");
        Cart cart = cartDataStore.getCartByUserId(userId);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        addOrRemoveProduct(req);

        context.setVariable("cart" , cart);
        context.setVariable("totalSum", getTotalSum(cart));
        context.setVariable("userID", session.getAttribute("userID"));
        context.setVariable("userName", session.getAttribute("userName"));

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