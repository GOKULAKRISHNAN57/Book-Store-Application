package BridgeLabz.Book_Store_Application.util;



import java.util.regex.Pattern;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final String PHONE_REGEX =
            "^[6-9][0-9]{9}$";

    public static boolean isValidEmail(String email) {
        return email != null &&
                Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPhone(String phone) {
        return phone != null &&
                Pattern.matches(PHONE_REGEX, phone);
    }
}