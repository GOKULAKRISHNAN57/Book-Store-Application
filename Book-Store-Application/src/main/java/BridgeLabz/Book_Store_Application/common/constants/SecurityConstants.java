package BridgeLabz.Book_Store_Application.common.constants;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER = "Authorization";

    public static final long JWT_EXPIRATION = 86400000; // 24 hours

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_USER = "ROLE_USER";
}
