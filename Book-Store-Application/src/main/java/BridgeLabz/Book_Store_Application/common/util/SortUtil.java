package BridgeLabz.Book_Store_Application.common.util;

import BridgeLabz.Book_Store_Application.exception.BadRequestException;
import org.springframework.data.domain.Sort;

import java.util.Set;

public class SortUtil {

    private SortUtil() {
    }

    public static Sort getSort(
            String sortBy,
            String direction,
            Set<String> allowedFields) {

        if (!allowedFields.contains(sortBy)) {
            throw new BadRequestException(
                    "Invalid sort field : " + sortBy
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new BadRequestException(
                    "Sort direction must be 'asc' or 'desc'."
            );
        }

        return direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
    }

}