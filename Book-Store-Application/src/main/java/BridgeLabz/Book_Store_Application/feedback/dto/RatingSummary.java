package BridgeLabz.Book_Store_Application.feedback.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingSummary {

    private Long productId;

    private String productTitle;

    private Double averageRating;

    private Long totalReviews;

}