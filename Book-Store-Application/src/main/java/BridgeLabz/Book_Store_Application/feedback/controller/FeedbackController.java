package BridgeLabz.Book_Store_Application.feedback.controller;

import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackRequest;
import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackResponse;
import BridgeLabz.Book_Store_Application.feedback.dto.RatingSummary;
import BridgeLabz.Book_Store_Application.feedback.service.FeedbackService;
import BridgeLabz.Book_Store_Application.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     * Add feedback
     */
    @PostMapping
    public ApiResponse<FeedbackResponse> addFeedback(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody FeedbackRequest request) {

        FeedbackResponse response = feedbackService.addFeedback(
                userDetails.getId(),
                request
        );

        return ApiResponse.success(
                "Feedback submitted successfully.",
                response
        );
    }

    /**
     * Update feedback
     */
    @PutMapping("/{feedbackId}")
    public ApiResponse<FeedbackResponse> updateFeedback(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long feedbackId,
            @Valid @RequestBody FeedbackRequest request) {

        FeedbackResponse response = feedbackService.updateFeedback(
                userDetails.getId(),
                feedbackId,
                request
        );

        return ApiResponse.success(
                "Feedback updated successfully.",
                response
        );
    }

    /**
     * Delete feedback
     */
    @DeleteMapping("/{feedbackId}")
    public ApiResponse<Void> deleteFeedback(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long feedbackId) {

        feedbackService.deleteFeedback(
                userDetails.getId(),
                feedbackId
        );

        return ApiResponse.success(
                "Feedback deleted successfully.",
                null
        );
    }

    /**
     * Get all feedback for a product
     */
    @GetMapping("/product/{productId}")
    public ApiResponse<List<FeedbackResponse>> getProductFeedbacks(
            @PathVariable Long productId) {

        List<FeedbackResponse> response =
                feedbackService.getProductFeedbacks(productId);

        return ApiResponse.success(
                "Product feedback retrieved successfully.",
                response
        );
    }

    /**
     * Get rating summary of a product
     */
    @GetMapping("/product/{productId}/rating")
    public ApiResponse<RatingSummary> getRatingSummary(
            @PathVariable Long productId) {

        RatingSummary response =
                feedbackService.getRatingSummary(productId);

        return ApiResponse.success(
                "Product rating summary retrieved successfully.",
                response
        );
    }

}