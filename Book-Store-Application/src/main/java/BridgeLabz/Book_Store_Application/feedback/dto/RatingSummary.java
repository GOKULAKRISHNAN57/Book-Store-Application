package BridgeLabz.Book_Store_Application.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingSummary {

    private Long productId;

    private Double averageRating;

    private Long totalReviews;
}