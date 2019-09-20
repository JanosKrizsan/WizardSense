package com.codecool.shop.config;

import org.mindrot.jbcrypt.BCrypt;

public class Utils {

    static public String hashPass(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    static public boolean checkPass(String candidate, String password){
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

}
