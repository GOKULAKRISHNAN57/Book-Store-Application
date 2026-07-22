package BridgeLabz.Book_Store_Application.feedback.controller;

import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackRequest;
import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackResponse;
import BridgeLabz.Book_Store_Application.feedback.dto.RatingSummary;
import BridgeLabz.Book_Store_Application.feedback.service.FeedbackService;
import BridgeLabz.Book_Store_Application.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class Feedbackcontroller {

    private final FeedbackService feedbackService;

    /**
     * Add feedback (rating + review) for a product.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FeedbackResponse>> addFeedback(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody FeedbackRequest request) {

        FeedbackResponse response =
                feedbackService.addFeedback(userDetails.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Feedback added successfully.",
                        response
                ));
    }

    /**
     * Get all feedback for a product.
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getFeedbackForProduct(
            @PathVariable Long productId) {

        List<FeedbackResponse> response =
                feedbackService.getFeedbackForProduct(productId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Feedback fetched successfully.",
                        response
                )
        );
    }

    /**
     * Get average rating summary for a product.
     */
    @GetMapping("/product/{productId}/summary")
    public ResponseEntity<ApiResponse<RatingSummary>> getRatingSummary(
            @PathVariable Long productId) {

        RatingSummary response =
                feedbackService.getRatingSummary(productId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Rating summary fetched successfully.",
                        response
                )
        );
    }

    /**
     * Delete own feedback.
     */
    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long feedbackId) {

        feedbackService.deleteFeedback(userDetails.getId(), feedbackId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Feedback deleted successfully.",
                        null
                )
        );
    }
}