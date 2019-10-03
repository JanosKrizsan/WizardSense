package com.codecool.shop.config;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ErrorHandler {

    public static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getName());

    public void ExceptionOccurred(HttpServletResponse resp, HttpSession sess, Exception e) {

        if (e instanceof SQLException) {
            LOGGER.warning(String.format("SQLException occurred: %s", e));
            sess.setAttribute("error", "SQLException");
        } else if (e instanceof IOException) {
            LOGGER.warning(String.format("IOException occurred: %s", e));
            sess.setAttribute("error", "IOException");
        } else if (e instanceof IllegalArgumentException) {
            LOGGER.warning(String.format("IllegalArgumentException occurred: %s", e));
            sess.setAttribute("error", "IllegalArgumentException");
        } else if (e instanceof InterruptedException) {
            LOGGER.warning(String.format("InterruptedException occurred: %s", e));
            sess.setAttribute("error", "InterruptedException");
        } else {
            LOGGER.warning(String.format("Unknown Exception occurred: %s", e));
            sess.setAttribute("error", "UnknownException");
        }
        respondToExceptions(sess, resp);
    }

    public static void ExceptionOccurred(Exception e) {
        LOGGER.severe(String.format("Internal server error occurred: %s", e));
    }

    private void respondToExceptions(HttpSession session, HttpServletResponse resp) {
        int errorMessage;

        try {
            if (session.getAttribute("userID") == null) {
                errorMessage = HttpServletResponse.SC_UNAUTHORIZED;
                resp.sendError(errorMessage, "Unauthorized access, please Register or Log into your profile.");

            } else {
                if (session.getAttribute("error") != null) {
                    switch (session.getAttribute("error").toString()) {
                        case "SQLException":
                            errorMessage = HttpServletResponse.SC_NO_CONTENT;
                            resp.sendError(errorMessage, "Database error occurred, please contact the Arch Wizard!");
                            Thread.sleep(3000);
                            resp.sendRedirect("/");

                            break;
                        case "IOException":
                            errorMessage = HttpServletResponse.SC_NOT_FOUND;
                            resp.sendError(errorMessage, "Resource not found, please contact your Admin Wizard.");
                            Thread.sleep(3000);
                            resp.sendRedirect("/");

                            break;
                        case "IllegalArgumentException":
                            errorMessage = 418;
                            resp.sendError(errorMessage, "Inappropriate argument passed, please contact your Wizard Mod to brew your coffee.");
                            Thread.sleep(3000);
                            resp.sendRedirect("/");

                            break;
                        case "InterruptedException":
                            errorMessage = HttpServletResponse.SC_GATEWAY_TIMEOUT;
                            resp.sendError(errorMessage, "Time out, please try again.");
                            Thread.sleep(3000);
                            resp.sendRedirect("/");
                            break;
                        case "UnknownException":
                            errorMessage = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                            resp.sendError(errorMessage, "Unknown error occurred, please contact the Arch Wizard!");
                            Thread.sleep(3000);
                            resp.sendRedirect("/");
                            break;
                    }
                }
            }

        } catch (InterruptedException | IOException e) {
            LOGGER.warning(String.format("Unknown Exception occurred: %s", e));
        }
    }

    public void checkUserLoggedIn(HttpSession session, HttpServletResponse resp) {
        respondToExceptions(session, resp);
    }
}
