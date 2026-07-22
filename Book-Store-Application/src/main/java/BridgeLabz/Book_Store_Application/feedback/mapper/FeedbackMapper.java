package BridgeLabz.Book_Store_Application.feedback.mapper;

import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackResponse;
import BridgeLabz.Book_Store_Application.feedback.entity.Feedback;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public FeedbackResponse toResponse(Feedback feedback) {

        return FeedbackResponse.builder()
                .id(feedback.getId())
                .userId(feedback.getUser().getId())
                .userFullName(feedback.getUser().getFullName())
                .productId(feedback.getProduct().getId())
                .productTitle(feedback.getProduct().getTitle())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdatedAt())
                .build();
    }
}