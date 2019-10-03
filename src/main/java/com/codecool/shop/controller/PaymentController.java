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

            int addressID = 0;
            UserAddress address = null;
            List<String> headers = Collections.list(req.getParameterNames());
            if (headers.contains("addressID")) {
                if (Integer.parseInt(req.getParameter("addressID")) == 0) {
                    address = createAddresses(req, userID);
                    addressID = address.getId();
                } else {
                    addressID = Integer.parseInt(req.getParameter("addressID"));
                    address = addressDataStore.find(addressID);
                }
            }

            String userName = (String) session.getAttribute("userName");

            User user = userDataStore.find(userID);
            Cart cart = cartDataStore.getCartByUserId(userID);

            if (orderDataStore.find(cart.getId()).getId() == 0) {
                Order newOrder = new Order(user, cart, address, "in progress");
                orderDataStore.add(newOrder);
            }


            context.setVariable("userID", userID);
            context.setVariable("userName", userName);
            context.setVariable("totalPaid", Utils.getTotalSum(cart));
            context.setVariable("products", cart.getProductsInCart());
            context.setVariable("address", address.getOrderFields());
            context.setVariable("addressID", addressID);
            context.setVariable("cart", cart);

            engine.process("product/pay.html", context, resp.getWriter());
        } catch (IOException | SQLException | NullPointerException e) {
            handler.ExceptionOccurred(resp, session, e);
        }
    }

    private UserAddress createAddresses(HttpServletRequest req, int userID) throws SQLException {

        HashMap<String, String> addressDetails = new HashMap<>();
        HashMap<String, String> billingDetails = new HashMap<>();

        UserAddress address;

        addressDetails.put("name", req.getParameter("billingName"));
        addressDetails.put("eMail", req.getParameter("billingEmail"));
        addressDetails.put("phoneNumber", req.getParameter("billingPhone"));
        addressDetails.put("country", req.getParameter("billingCountry"));
        addressDetails.put("city", req.getParameter("billingCity"));
        addressDetails.put("zipCode", req.getParameter("billingZip"));
        addressDetails.put("address", req.getParameter("billingAddress"));

        UserAddress billing = new UserAddress(addressDetails, userID);
        addressDataStore.add(billing);
        address = billing;

        if (!req.getParameter("billingZip").equals(req.getParameter("shippingZip"))) {
            billingDetails.put("name", req.getParameter("billingName"));
            billingDetails.put("eMail", req.getParameter("billingEmail"));
            billingDetails.put("phoneNumber", req.getParameter("billingPhone"));
            billingDetails.put("country", req.getParameter("shippingCountry"));
            billingDetails.put("city", req.getParameter("shippingCity"));
            billingDetails.put("zipCode", req.getParameter("shippingZip"));
            billingDetails.put("address", req.getParameter("shippingAddress"));

            UserAddress shipping = new UserAddress(billingDetails, userID);
            addressDataStore.add(shipping);
            address = shipping;

        }

        return address;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }

}