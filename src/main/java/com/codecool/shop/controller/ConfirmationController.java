package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandling;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.config.Utils;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserAddressDaoJDBC;
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
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/confirmation"})
public class ConfirmationController extends HttpServlet {
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private OrderDaoJDBC orderDataStore = OrderDaoJDBC.getInstance();
    private UserAddressDaoJDBC addressDataStore = UserAddressDaoJDBC.getInstance();
    private ErrorHandling handler = new ErrorHandling();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        try {
            handler.CheckErrors(session, resp);

            int userID = (int) session.getAttribute("userID");
            String userName = (String) session.getAttribute("userName");

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

            int addressLocation = addressDataStore.getAddressByUserId(userID).size() - 1;
            String emailAddress = addressDataStore.getAddressByUserId(userID).get(addressLocation).getOrderFields().get("eMail");
            Utils.sendEmail(emailAddress);

            engine.process("product/confirmation.html", context, resp.getWriter());
        } catch (SQLException | IOException e) {
            handler.ExceptionOccurred(session, e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}