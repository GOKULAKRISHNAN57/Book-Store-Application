package BridgeLabz.Book_Store_Application.feedback.service.impl;

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

    private final FeedbackMapper feedbackMapper;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        ));
    }

    private Product getProduct(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + productId
                        ));
    }

    @Override
    public FeedbackResponse addFeedback(
            Long userId,
            FeedbackRequest request) {

        User user = getUser(userId);

        Product product = getProduct(request.getProductId());

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new BadRequestException(
                    "Product is not available."
            );
        }

        if (feedbackRepository.existsByUserAndProduct(user, product)) {
            throw new BadRequestException(
                    "You have already submitted feedback for this product."
            );
        }

        Feedback feedback = Feedback.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .review(request.getReview())
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return feedbackMapper.toResponse(savedFeedback);
    }
    @Override
    public FeedbackResponse updateFeedback(
            Long userId,
            Long feedbackId,
            FeedbackRequest request) {

        User user = getUser(userId);

        Feedback feedback = feedbackRepository
                .findByIdAndUser(feedbackId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Feedback not found."
                        ));

        feedback.setRating(request.getRating());
        feedback.setReview(request.getReview());

        Feedback updatedFeedback = feedbackRepository.save(feedback);

        return feedbackMapper.toResponse(updatedFeedback);
    }

    @Override
    public void deleteFeedback(
            Long userId,
            Long feedbackId) {

        User user = getUser(userId);

        Feedback feedback = feedbackRepository
                .findByIdAndUser(feedbackId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Feedback not found."
                        ));

        feedbackRepository.delete(feedback);
    }
    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getProductFeedbacks(
            Long productId) {

        getProduct(productId);

        return feedbackMapper.toResponseList(
                feedbackRepository.findByProductId(productId)
        );
    }
    @Override
    @Transactional(readOnly = true)
    public RatingSummary getRatingSummary(
            Long productId) {

        Product product = getProduct(productId);

        Double averageRating =
                feedbackRepository.getAverageRating(productId);

        if (averageRating == null) {
            averageRating = 0.0;
        }

        long totalReviews =
                feedbackRepository.countByProduct(product);

        return RatingSummary.builder()
                .productId(product.getId())
                .productTitle(product.getTitle())
                .averageRating(
                        Math.round(averageRating * 10.0) / 10.0
                )
                .totalReviews(totalReviews)
                .build();
    }

}