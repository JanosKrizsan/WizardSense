package com.codecool.shop.controller;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserAddressDaoJDBC;
import com.codecool.shop.dao.implementation.JDBC.UserDaoJDBC;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.UserAddress;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    private static UserAddressDaoJDBC addressDataStore = UserAddressDaoJDBC.getInstance();



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> headers = Collections.list(req.getParameterNames());
        HttpSession session = req.getSession();
        if (session.getAttribute("userID") == null) {
            resp.sendError(401, "Unauthorized access!");
        }

        int userId = (int) session.getAttribute("userID");
        String userName = (String) session.getAttribute("userName");

        UserAddress address = new UserAddress(new HashMap<>(), userId);

        Integer addressID = null;
        if(headers.contains("selection")) {

            addressID = Integer.parseInt(req.getParameter("selection"));
            address = addressDataStore.find(addressID);

        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("addressID", addressID);
        context.setVariable("userID", userId);
        context.setVariable("userName", userName);
        context.setVariable("details", address);

        engine.process("product/checkout.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}