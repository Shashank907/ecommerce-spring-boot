package com.shashank.ecommerce.review.service.impl;

import com.shashank.ecommerce.exception.ResourceNotFoundException;
import com.shashank.ecommerce.order.entity.OrderItem;
import com.shashank.ecommerce.order.repository.OrderItemRepository;
import com.shashank.ecommerce.product.entity.Product;
import com.shashank.ecommerce.product.repository.ProductRepository;
import com.shashank.ecommerce.review.dto.CreateReviewRequestDto;
import com.shashank.ecommerce.review.dto.ReviewDto;
import com.shashank.ecommerce.review.entity.Review;
import com.shashank.ecommerce.review.repository.ReviewRepository;
import com.shashank.ecommerce.review.service.ReviewService;
import com.shashank.ecommerce.user.entity.User;
import com.shashank.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public ReviewDto createReview(
            Long userId,
            Long productId,
            CreateReviewRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new IllegalArgumentException(
                    "User has already reviewed this product");
        }

        boolean purchased = orderItemRepository.findByProductId(productId)
                .stream()
                .anyMatch(orderItem ->
                        orderItem.getOrder()
                                .getUser()
                                .getId()
                                .equals(userId)
                );

        if (!purchased) {
            throw new IllegalArgumentException(
                    "You can review only products you have purchased");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);

        return mapToDto(savedReview);
    }

    @Override
    public List<ReviewDto> getProductReviews(Long productId) {

        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + productId));

        return reviewRepository.findByProductId(productId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Review not found with id: " + reviewId));

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(
                    "Review does not belong to this user");
        }

        reviewRepository.delete(review);
    }

    private ReviewDto mapToDto(Review review) {

        ReviewDto dto = new ReviewDto();

        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setProductId(review.getProduct().getId());
        dto.setProductName(review.getProduct().getName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());

        return dto;
    }
}