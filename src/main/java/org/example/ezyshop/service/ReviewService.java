package org.example.ezyshop.service;

import org.example.ezyshop.dto.review.ReviewRequest;
import org.example.ezyshop.dto.review.ReviewResponse;
import org.example.ezyshop.entity.Review;
import org.example.ezyshop.exception.NotFoundException;

import java.util.List;

public interface ReviewService {
    Review getReviewByProductAndUser(Long productId, Long userId);

    ReviewResponse createReview(ReviewRequest request);

    List<Review> getReviewsByProduct(Long productId);

    List<Review> getReviewsByUser(Long userId);

    Review getReviewById(Long id);

    Review updateReview(Long id, Review updatedReview);

    void deleteReview(Long id);
}
