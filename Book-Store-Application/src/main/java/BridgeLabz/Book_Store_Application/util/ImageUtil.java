package BridgeLabz.Book_Store_Application.util;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public final class ImageUtil {

    private ImageUtil() {
    }

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/jpg"
    );

    private static final long MAX_SIZE = 5 * 1024 * 1024;

    public static boolean isValidImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return false;
        }

        return ALLOWED_TYPES.contains(file.getContentType())
                && file.getSize() <= MAX_SIZE;
    }
}
