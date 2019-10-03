package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandler;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.config.Utils;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserAddressDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/confirmation"})
public class ConfirmationController extends HttpServlet {
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private UserAddressDaoJDBC addressDataStore = UserAddressDaoJDBC.getInstance();
    private OrderDaoJDBC orderDataStore = OrderDaoJDBC.getInstance();
    private ErrorHandler handler = new ErrorHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        try {
            handler.checkUserLoggedIn(session, resp);

            int userID = (int) session.getAttribute("userID");
            String userName = (String) session.getAttribute("userName");
            int addressID = Integer.parseInt(req.getParameter("addressID"));

            TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
            WebContext context = new WebContext(req, resp, req.getServletContext());

            context.setVariable("userID", userID);
            context.setVariable("userName", userName);

            Cart cart = cartDataStore.getCartByUserId(userID);
            if(req.getParameter("wipe").equals("do")) {
                orderDataStore.setStatus("complete", orderDataStore.find(cart.getId()));
                cartDataStore.remove(cart.getId());
            }

            String addressEmail = addressDataStore.find(addressID).getOrderFields().get("eMail");
            Utils.sendEmail(addressEmail);

            engine.process("product/confirmation.html", context, resp.getWriter());
        } catch (SQLException | IOException | NullPointerException e) {
            handler.ExceptionOccurred(resp, session, e);
            try {
            throw new InvalidParameterException();
            } catch (InvalidParameterException s) {
                ErrorHandler.ExceptionOccurred(s);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}