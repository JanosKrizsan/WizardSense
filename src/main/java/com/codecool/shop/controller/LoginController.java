package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandling;
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
import java.sql.SQLException;


@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
    private UserDaoJDBC userDataStore = UserDaoJDBC.getInstance();
    private ErrorHandling handler = new ErrorHandling();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("userID", req.getSession().getAttribute("userID"));
        context.setVariable("userName", req.getSession().getAttribute("userName"));
        context.setVariable("errorMessage", req.getAttribute("errorMessage"));

        try {
            engine.process("product/login.html", context, resp.getWriter());
        } catch (IOException e) {
            handler.ExceptionOccurred(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            User userToLogin = userDataStore.find(username);

            if (userToLogin.getPassword() == null || !Utils.checkPass(password, userToLogin.getPassword())) {
                req.setAttribute("errorMessage", "Your alias or the spell is improper!");
                doGet(req, resp);
            } else {
                session.setAttribute("userID", userToLogin.getId());
                session.setAttribute("userName", userToLogin.getUsername());
                resp.sendRedirect("/");
            }
        } catch (IOException | SQLException e) {
            handler.ExceptionOccurred(e);
        }
    }
}