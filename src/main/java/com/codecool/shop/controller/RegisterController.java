package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandler;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.JDBC.UserDaoJDBC;
import com.codecool.shop.model.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {

    private ErrorHandler handler = new ErrorHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("errorMessage", req.getAttribute("errorMessage"));

        try {
            engine.process("product/register.html", context, resp.getWriter());
        } catch (IOException e) {
            handler.ExceptionOccurred(resp, session, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        UserDaoJDBC userDataStore = UserDaoJDBC.getInstance();
        HttpSession session = req.getSession();

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String rePassword = req.getParameter("rePassword");

        try {
            if (!password.equals(rePassword)) {
                req.setAttribute("errorMessage", "Your spellwords are mismatching!");
                doGet(req, resp);
            } else if (userDataStore.find(username).getUsername() != null) {
                req.setAttribute("errorMessage", "Thou hast chosen an alias already in existence!");
                doGet(req, resp);
            } else {
                userDataStore.add(new User(username, password));
                resp.sendRedirect("/");
            }
        } catch (SQLException | IOException e) {
            handler.ExceptionOccurred(resp, session, e);
        }

    }


}