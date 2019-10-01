package com.codecool.shop.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getSession().getAttribute("userID") == null) {
            resp.sendError(401, "Unauthorized access!");
        }
        req.getSession().removeAttribute("userName");
        req.getSession().removeAttribute("userID");

        resp.sendRedirect("/");
    }


}