package BridgeLabz.Book_Store_Application.feedback.service;

import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackRequest;
import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackResponse;
import BridgeLabz.Book_Store_Application.feedback.dto.RatingSummary;

import java.util.List;

public interface FeedbackService {

    FeedbackResponse addFeedback(Long userId, FeedbackRequest request);

    List<FeedbackResponse> getFeedbackForProduct(Long productId);

    RatingSummary getRatingSummary(Long productId);

    void deleteFeedback(Long userId, Long feedbackId);
}