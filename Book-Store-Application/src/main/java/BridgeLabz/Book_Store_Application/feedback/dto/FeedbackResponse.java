package BridgeLabz.Book_Store_Application.feedback.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {

    private Long feedbackId;

    private Long userId;

    private String userName;

    private Long productId;

    private String productTitle;

    private Integer rating;

    private String review;

    private LocalDateTime createdAt;

}