package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.config.Utils;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.OrderDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/confirmation"})
public class ConfirmationController extends HttpServlet {
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private OrderDaoJDBC orderDataStore = OrderDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("userID") == null) {
            resp.sendError(401, "Unauthorized access!");
        } else {

            int userID = (int)session.getAttribute("userID");
            String userName = (String)session.getAttribute("userName");

            TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
            WebContext context = new WebContext(req, resp, req.getServletContext());

            Cart cart = cartDataStore.getCartByUserId(userID);
            context.setVariable("totalPaid", Utils.getTotalSum(cart));
            context.setVariable("cart", cart);
            context.setVariable("userID", userID);
            context.setVariable("userName", userName);

            Order order = orderDataStore.find(cart.getId());
            orderDataStore.setStatus("complete", order);
            cartDataStore.remove(cart.getId());

            engine.process("product/confirmation.html", context, resp.getWriter());

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}