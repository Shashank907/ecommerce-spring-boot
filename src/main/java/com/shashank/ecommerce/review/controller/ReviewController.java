package com.shashank.ecommerce.review.controller;

import com.shashank.ecommerce.review.dto.CreateReviewRequestDto;
import com.shashank.ecommerce.review.dto.ReviewDto;
import com.shashank.ecommerce.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestBody @Valid CreateReviewRequestDto request) {

        ReviewDto review = reviewService.createReview(
                userId,
                productId,
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(review);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getProductReviews(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                reviewService.getProductReviews(productId)
        );
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long userId,
            @PathVariable Long reviewId) {

        reviewService.deleteReview(userId, reviewId);

        return ResponseEntity.noContent().build();
    }
}