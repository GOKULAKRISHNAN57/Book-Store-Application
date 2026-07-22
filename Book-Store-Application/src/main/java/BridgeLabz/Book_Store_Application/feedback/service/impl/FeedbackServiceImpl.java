package BridgeLabz.Book_Store_Application.feedback.service.impl;

import BridgeLabz.Book_Store_Application.exception.AccessDeniedException;
import BridgeLabz.Book_Store_Application.exception.BadRequestException;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;
import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackRequest;
import BridgeLabz.Book_Store_Application.feedback.dto.FeedbackResponse;
import BridgeLabz.Book_Store_Application.feedback.dto.RatingSummary;
import BridgeLabz.Book_Store_Application.feedback.entity.Feedback;
import BridgeLabz.Book_Store_Application.feedback.mapper.FeedbackMapper;
import BridgeLabz.Book_Store_Application.feedback.repository.FeedbackRepository;
import BridgeLabz.Book_Store_Application.feedback.service.FeedbackService;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import BridgeLabz.Book_Store_Application.product.repository.ProductRepository;
import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public FeedbackResponse addFeedback(Long userId, FeedbackRequest request) {

        // Find the logged-in user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        // Find active product
        Product product = productRepository.findByIdAndActiveTrue(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + request.getProductId()));

        // Prevent duplicate feedback from the same user for the same product
        if (feedbackRepository.existsByUserAndProduct(user, product)) {
            throw new BadRequestException("You have already reviewed this product.");
        }

        // Create feedback
        Feedback feedback = Feedback.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        feedbackRepository.save(feedback);

        return feedbackMapper.toResponse(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getFeedbackForProduct(Long productId) {

        // Ensure product exists
        productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID: " + productId));

        return feedbackRepository.findByProductId(productId)
                .stream()
                .map(feedbackMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RatingSummary getRatingSummary(Long productId) {

        // Ensure product exists
        productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID: " + productId));

        Double averageRating = feedbackRepository.findAverageRatingByProductId(productId);
        long totalReviews = feedbackRepository.countByProductId(productId);

        return RatingSummary.builder()
                .productId(productId)
                .averageRating(averageRating == null ? 0.0 : Math.round(averageRating * 10.0) / 10.0)
                .totalReviews(totalReviews)
                .build();
    }

    @Override
    public void deleteFeedback(Long userId, Long feedbackId) {

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Feedback not found with ID: " + feedbackId));

        // Only the author of the feedback can delete it
        if (!feedback.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to delete this feedback.");
        }

        feedbackRepository.delete(feedback);
    }
}