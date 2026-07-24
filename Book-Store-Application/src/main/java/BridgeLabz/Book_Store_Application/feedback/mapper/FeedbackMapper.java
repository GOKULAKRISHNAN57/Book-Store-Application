package BridgeLabz.Book_Store_Application.feedback.mapper;

import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackResponse;
import BridgeLabz.Book_Store_Application.feedback.entity.Feedback;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class FeedbackMapper {

    /**
     * Convert Feedback Entity to FeedbackResponse DTO
     */
    public FeedbackResponse toResponse(Feedback feedback) {

        if (feedback == null) {
            return null;
        }

        return FeedbackResponse.builder()
                .feedbackId(feedback.getId())
                .userId(feedback.getUser().getId())
                .userName(feedback.getUser().getFullName())
                .productId(feedback.getProduct().getId())
                .productTitle(feedback.getProduct().getTitle())
                .rating(feedback.getRating())
                .review(feedback.getReview())
                .createdAt(feedback.getCreatedAt())
                .build();
    }

    /**
     * Convert List<Feedback> to List<FeedbackResponse>
     */
    public List<FeedbackResponse> toResponseList(List<Feedback> feedbackList) {

        if (feedbackList == null || feedbackList.isEmpty()) {
            return Collections.emptyList();
        }

        return feedbackList.stream()
                .map(this::toResponse)
                .toList();
    }

}