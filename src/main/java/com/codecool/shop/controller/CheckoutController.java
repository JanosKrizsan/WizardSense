package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;

import com.codecool.shop.dao.implementation.Memory.OrderMem;
import com.codecool.shop.model.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderMem.getInstance().add(new Order());
        HashMap details = OrderMem.getInstance().find(0).getOrderFields();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());


        context.setVariable("details", details);

        engine.process("product/checkout.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = OrderMem.getInstance().find(0);

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

        order.setOrderFields(new HashMap<String, String>() {
            {
                put("name", req.getParameter("name"));
                put("email", req.getParameter("email"));
                put("phoneNum", req.getParameter("phoneNum"));
                put("billingCountry", req.getParameter("billCountry"));
                put("billingCity", req.getParameter("billCity"));
                put("billingZipCode", req.getParameter("billZip"));
                put("billingAddress", req.getParameter("billAddress"));
                put("shippingCountry", req.getParameter("shipCountry"));
                put("shippingCity", req.getParameter("shipCity"));
                put("shippingZipCode", req.getParameter("shipZip"));
                put("shippingAddress", req.getParameter("shipAddress"));

            }

        });

        resp.sendRedirect("/pay");
    }
}

// // Alternative setting of the template context
// Map<String, Object> params = new HashMap<>();
// params.put("category", productCategoryDataStore.find(1));
// params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
// context.setVariables(params);