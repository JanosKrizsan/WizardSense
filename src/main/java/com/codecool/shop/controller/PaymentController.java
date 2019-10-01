package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.config.Utils;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserAddressDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.User;
import com.codecool.shop.model.UserAddress;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@WebServlet(urlPatterns = {"/pay"})
public class PaymentController extends HttpServlet {

    private UserAddressDaoJDBC addressDataStore = UserAddressDaoJDBC.getInstance();
    private OrderDaoJDBC orderDataStore = OrderDaoJDBC.getInstance();
    private CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();
    private UserDaoJDBC userDataStore = UserDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("userID") == null) {
            resp.sendError(401, "Unauthorized access!");
        }

        int userID = (int) session.getAttribute("userID");


        List<String> headers = Collections.list(req.getParameterNames());
        if (headers.contains("addressID")) {
            if (req.getParameter("addressID") == null) {
                createAddresses(req, userID);
            }
        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        User user = userDataStore.find(userID);
        Cart cart = cartDataStore.getCartByUserId(userID);
        orderDataStore.add(new Order(user, cart, "in progress"));

        float totalSum = Utils.getTotalSum(cartDataStore.find(cart.getId()));

        context.setVariable("totalSum", totalSum);
        context.setVariable("userID", req.getSession().getAttribute("userID"));
        context.setVariable("userName", req.getSession().getAttribute("userName"));


        engine.process("product/pay.html", context, resp.getWriter());
    }

    private void createAddresses(HttpServletRequest req, int userID) {

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

}