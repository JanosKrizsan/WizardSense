package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandling;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.config.Utils;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserDaoJDBC;
import com.codecool.shop.model.Cart;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


@WebServlet(urlPatterns = {"/shopping-cart"})
public class CartController extends HttpServlet {
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private UserDaoJDBC userDataStore = UserDaoJDBC.getInstance();
    private ErrorHandling handler = new ErrorHandling();


    private void addOrRemoveProduct(HttpServletRequest req) throws SQLException {
        List<String> headers = Collections.list(req.getParameterNames());

        HttpSession session = req.getSession();


        int userId = (int)session.getAttribute("userID");

        Cart cart = cartDataStore.getCartByUserId(userId);

        if (headers.contains("increase")) {
            int prodId = Integer.parseInt(req.getParameter("increase"));

            cartDataStore.increaseOrDecreaseQuantity(cart, prodId, true);
        }
        else if (headers.contains("decrease")) {
            int prodId = Integer.parseInt(req.getParameter("decrease"));

            if(cartDataStore.getCartProductQuantity(cart, prodId) <= 1) {
                cartDataStore.clearProductFromCart(prodId);
            } else {
                cartDataStore.increaseOrDecreaseQuantity(cart, prodId, false);
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<String> headers = Collections.list(req.getParameterNames());

        HttpSession session = req.getSession();

        try {
            handler.checkUserLoggedIn(session, resp);

            if (headers.contains("increase") || headers.contains("decrease")) {
                addOrRemoveProduct(req);
            }

            int userId = (int) session.getAttribute("userID");

            Cart cart = cartDataStore.getCartByUserId(userId);
            if (cart == null) {
                cart = new Cart(new TreeMap<>(), userDataStore.find(userId));
            }

            TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
            WebContext context = new WebContext(req, resp, req.getServletContext());

            context.setVariable("cart", cart);
            context.setVariable("totalSum", Utils.getTotalSum(cart));
            context.setVariable("userID", session.getAttribute("userID"));
            context.setVariable("userName", session.getAttribute("userName"));

            engine.process("product/shopping-cart.html", context, resp.getWriter());
        } catch (SQLException | IOException e) {
            handler.ExceptionOccurred(resp, session, e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }

}