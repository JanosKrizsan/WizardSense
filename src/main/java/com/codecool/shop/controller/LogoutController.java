package com.codecool.shop.controller;

import com.codecool.shop.config.ErrorHandling;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {

    private ErrorHandling handler = new ErrorHandling();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        try {
            handler.CheckErrors(session, resp);

            req.getSession().removeAttribute("userName");
            req.getSession().removeAttribute("userID");

            resp.sendRedirect("/");
        } catch (IOException e) {
            handler.ExceptionOccurred(session, e);
        }
    }


}