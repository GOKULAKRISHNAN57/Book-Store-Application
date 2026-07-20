package BridgeLabz.Book_Store_Application.util;


import java.util.regex.Pattern;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    private static final String PASSWORD_REGEX =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$";

    public static boolean isStrongPassword(String password) {

        if (password == null) {
            return false;
        }

        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
