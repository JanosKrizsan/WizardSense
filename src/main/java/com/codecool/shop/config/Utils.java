package com.codecool.shop.config;

import com.codecool.shop.dao.implementation.JDBC.CartDaoJDBC;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public final class Utils {

    private static CartDaoJDBC cartDataStore = CartDaoJDBC.getInstance();

    static public String hashPass(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    static public boolean checkPass(String candidate, String password) {
        return BCrypt.checkpw(candidate, password);
    }

    public static boolean isJUnitTest() {
        StackTraceElement[] list = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

    public static float getTotalSum(Cart cart) {
        float sum = 0;

        for (Product product : cart.getProductsInCart()) {
            sum += product.getDefaultPrice() * cartDataStore.getCartProductQuantity(cart, product.getId());
        }
        return sum;
    }

    public static void sendEmail(String to) {
        String from = "BizardWizzard@yandex.com";
        final String username = "BizardWizzard";
        final String password = "pwoEkrpo123123";
        String host = "smtp.mailtrap.io";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "2525");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Thank you for your purchase!");
            String content = "Thank ye fer yer purchase, may the powers that be keep thee well! We look forward for yer next purchase!";
            message.setText(content);
            Transport.send(message);

            ErrorHandling.LOGGER.info("E-mail sent upon purchase!");
        } catch (MessagingException e) {
            new ErrorHandling().ExceptionOccurred(e);
            throw new RuntimeException(e);
        }
    }

}
