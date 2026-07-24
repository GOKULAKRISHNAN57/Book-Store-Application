package BridgeLabz.Book_Store_Application.common.constants;

import java.util.Set;

public final class SortConstants {

    private SortConstants() {
    }

    public static final Set<String> PRODUCT_SORT_FIELDS = Set.of(
            "title",
            "author",
            "price",
            "stockQuantity",
            "createdAt"
    );

}