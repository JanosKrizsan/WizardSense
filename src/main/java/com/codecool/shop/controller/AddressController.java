package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandling;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserAddressDaoJDBC;
import com.codecool.shop.model.UserAddress;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet(urlPatterns = {"/address-list"})
public class AddressController extends HttpServlet {

    private UserAddressDaoJDBC addressDataStore = UserAddressDaoJDBC.getInstance();
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private ErrorHandling handler = new ErrorHandling();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session = req.getSession();

        try {
            handler.checkUserLoggedIn(session, resp);
            Integer userID = (Integer) session.getAttribute("userID");
            String userName = (String) session.getAttribute("userName");

            List<UserAddress> addressList;
            Integer cartSize;

            addressList = addressDataStore.getAddressByUserId(userID);
            cartSize = cartDataStore.getCartByUserId(userID).getSumOfProducts();


            TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
            WebContext context = new WebContext(req, resp, req.getServletContext());

            context.setVariable("cartSize", cartSize);
            context.setVariable("addresses", addressList);
            context.setVariable("userID", userID);
            context.setVariable("userName", userName);
            engine.process("product/address_list.html", context, resp.getWriter());
        } catch (IOException | SQLException e) {
            handler.ExceptionOccurred(resp, session, e);
        }
    }
}
