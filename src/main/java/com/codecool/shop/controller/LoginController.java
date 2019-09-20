package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.config.Utils;
import com.codecool.shop.dao.implementation.JDBC.*;
import com.codecool.shop.model.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("userID", req.getSession().getAttribute("userID"));
        context.setVariable("userName", req.getSession().getAttribute("userName"));
        context.setVariable("errorMessage", req.getAttribute("errorMessage"));

        engine.process("product/login.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDaoJDBC userDataStore = UserDaoJDBC.getInstance();
        HttpSession session = req.getSession();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User userToLogin = userDataStore.find(username);

        if (userToLogin.getPassword() == null || !Utils.checkPass(password, userToLogin.getPassword())){
            req.setAttribute("errorMessage", "Your alias or the spell is improper!");
            doGet(req, resp);
        } else {
            session.setAttribute("userID", userToLogin.getId());
            session.setAttribute("userName", userToLogin.getUsername());
            resp.sendRedirect("/");
        }


    }

}