package BridgeLabz.Book_Store_Application.feedback.service;

import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackRequest;
import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackResponse;
import BridgeLabz.Book_Store_Application.feedback.dto.RatingSummary;

import java.util.List;

public interface FeedbackService {

    /**
     * Add feedback for a product.
     */
    FeedbackResponse addFeedback(
            Long userId,
            FeedbackRequest request
    );

    /**
     * Update existing feedback.
     */
    FeedbackResponse updateFeedback(
            Long userId,
            Long feedbackId,
            FeedbackRequest request
    );

    /**
     * Delete feedback.
     */
    void deleteFeedback(
            Long userId,
            Long feedbackId
    );

    /**
     * Get all feedback for a product.
     */
    List<FeedbackResponse> getProductFeedbacks(
            Long productId
    );

    /**
     * Get rating summary of a product.
     */
    RatingSummary getRatingSummary(
            Long productId
    );

}