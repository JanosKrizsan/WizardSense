package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandler;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserAddressDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.User;
import com.codecool.shop.model.UserAddress;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@WebServlet(urlPatterns = {"/pay"})
public class PaymentController extends HttpServlet {

    private UserAddressDaoJDBC addressDataStore = UserAddressDaoJDBC.getInstance();
    private OrderDaoJDBC orderDataStore = OrderDaoJDBC.getInstance();
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private UserDaoJDBC userDataStore = UserDaoJDBC.getInstance();
    private ErrorHandler handler = new ErrorHandler();
    private UserAddressDaoJDBC userAddressDataStore = UserAddressDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        try {
            handler.checkUserLoggedIn(session, resp);

            int userID = (int) session.getAttribute("userID");


            List<String> headers = Collections.list(req.getParameterNames());
            if (headers.contains("addressID")) {
                if (req.getParameter("addressID") == null) {
                    createAddresses(req, userID);
                }
            }

            int lastItem = addressDataStore.getAddressByUserId(userID).size() - 1;
            int addressId = addressDataStore.getAddressByUserId(userID).get(lastItem).getId();

            User user = userDataStore.find(userID);
            Cart cart = cartDataStore.getCartByUserId(userID);
            UserAddress address = addressDataStore.find(addressId);
        orderDataStore.add(new Order(user, cart, address, "in progress"));


        resp.sendRedirect("/confirmation");
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