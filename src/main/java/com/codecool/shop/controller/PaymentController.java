package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandler;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.config.Utils;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserAddressDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserDaoJDBC;
import com.codecool.shop.model.*;
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


@WebServlet(urlPatterns = {"/pay"})
public class PaymentController extends HttpServlet {

    private UserAddressDaoJDBC addressDataStore = UserAddressDaoJDBC.getInstance();
    private OrderDaoJDBC orderDataStore = OrderDaoJDBC.getInstance();
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private UserDaoJDBC userDataStore = UserDaoJDBC.getInstance();
    private ErrorHandler handler = new ErrorHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        try {
            handler.checkUserLoggedIn(session, resp);

            int userID = (int) session.getAttribute("userID");


            List<String> headers = Collections.list(req.getParameterNames());
            if (headers.contains("addressID")) {
                if (req.getParameter("addressID") == null) {
                    createAddresses(req, userID);
                }
            }

            int addressId = Integer.parseInt(req.getParameter("addressID"));
            String userName = (String) session.getAttribute("userName");

            User user = userDataStore.find(userID);
            Cart cart = cartDataStore.getCartByUserId(userID);
            UserAddress address = addressDataStore.find(addressId);
            orderDataStore.add(new Order(user, cart, address, "in progress"));
            Order order = orderDataStore.find(cart.getId());


            context.setVariable("userID", userID);
            context.setVariable("userName", userName);
            context.setVariable("totalPaid", Utils.getTotalSum(cart));
            context.setVariable("products", order.getCart().getProductsInCart());
            context.setVariable("address", order.getAddress().getOrderFields());
            context.setVariable("cart", order.getCart());

            engine.process("product/pay.html", context, resp.getWriter());
        } catch (IOException | SQLException e) {
            handler.ExceptionOccurred(resp, session, e);
        }
    }

    private void createAddresses(HttpServletRequest req, int userID) throws SQLException {

        HashMap<String, String> addressDetails = new HashMap<>();
        HashMap<String, String> billingDetails = new HashMap<>();

        addressDetails.put("name", req.getParameter("billingName"));
        addressDetails.put("eMail", req.getParameter("billingEmail"));
        addressDetails.put("phoneNumber", req.getParameter("billingPhone"));
        addressDetails.put("country", req.getParameter("billingCountry"));
        addressDetails.put("city", req.getParameter("billingCity"));
        addressDetails.put("zipCode", req.getParameter("billingZip"));
        addressDetails.put("address", req.getParameter("billingAddress"));

        UserAddress address = new UserAddress(addressDetails, userID);
        addressDataStore.add(address);

        if (!req.getParameter("billingZip").equals(req.getParameter("shippingZip"))) {
            billingDetails.put("name", req.getParameter("shippingName"));
            billingDetails.put("eMail", req.getParameter("shippingEmail"));
            billingDetails.put("phoneNumber", req.getParameter("shippingPhone"));
            billingDetails.put("country", req.getParameter("shippingCountry"));
            billingDetails.put("city", req.getParameter("shippingCity"));
            billingDetails.put("zipCode", req.getParameter("shippingZip"));
            billingDetails.put("address", req.getParameter("shippingAddress"));

            UserAddress billing = new UserAddress(billingDetails, userID);
            addressDataStore.add(billing);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }

}