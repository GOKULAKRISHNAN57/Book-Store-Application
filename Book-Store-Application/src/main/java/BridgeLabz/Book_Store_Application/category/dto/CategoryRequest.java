package BridgeLabz.Book_Store_Application.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 100,
            message = "Category name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500,
            message = "Description cannot exceed 500 characters")
    private String description;
}