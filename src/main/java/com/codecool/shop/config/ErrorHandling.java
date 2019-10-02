package com.codecool.shop.config;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class ErrorHandling {

    public static final Logger LOGGER = Logger.getLogger(ErrorHandling.class.getName());

    public void ExceptionOccurred(Exception e) {
        LOGGER.warning(String.format("Exception occurred: %s", e));
    }

    public Integer ExecuteResponse(HttpSession sess) {
        Integer errorCode = null;

        if (sess.getAttribute("userID") == null) {
            return HttpServletResponse.SC_UNAUTHORIZED;
        }

        return errorCode;
    }

    public void CheckErrors(HttpSession session, HttpServletResponse resp) throws IOException {
        Integer errorMessage = ExecuteResponse(session);
        if (errorMessage != null) {
            resp.sendError(errorMessage);
            try {
                resp.wait(5);
            } catch (InterruptedException e) {
                ExceptionOccurred(e);
            }
            resp.sendRedirect("/");
        }
    }

}
