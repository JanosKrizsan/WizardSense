package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.dao.implementation.OrderMem;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;


@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderMem details = OrderMem.getInstance();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());


        context.setVariable("details", details);

        engine.process("product/checkout.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderMem order = OrderMem.getInstance();

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phoneNum = req.getParameter("phoneNum");
        String billCountry = req.getParameter("billCountry");
        String billCity = req.getParameter("billCity");
        String billZip = req.getParameter("billZip");
        String billAddress = req.getParameter("billAddress");
        String shipCountry = req.getParameter("shipCountry");
        String shipCity = req.getParameter("shipCity");
        String shipZip = req.getParameter("shipZip");
        String shipAddress = req.getParameter("shipAddress");

        order.setOrderInfo(name, email, phoneNum, billCountry, billCity, billZip, billAddress, shipCountry, shipCity, shipZip, shipAddress);

        resp.sendRedirect("/pay");
    }
}

// // Alternative setting of the template context
// Map<String, Object> params = new HashMap<>();
// params.put("category", productCategoryDataStore.find(1));
// params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
// context.setVariables(params);