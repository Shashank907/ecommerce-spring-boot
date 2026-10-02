package com.shashank.ecommerce.review.service;

import com.shashank.ecommerce.review.dto.CreateReviewRequestDto;
import com.shashank.ecommerce.review.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(
            Long userId,
            Long productId,
            CreateReviewRequestDto request
    );

    List<ReviewDto> getProductReviews(Long productId);

    void deleteReview(Long userId, Long reviewId);
}