package org.example.ezyshop.service;

import org.example.ezyshop.entity.Review;
import org.example.ezyshop.exception.NotFoundException;

import java.util.List;

public interface ReviewService {
    public Review getReviewByProductAndUser(Long productId, Long userId);

    public Review createReview(Review review);

    public List<Review> getReviewsByProduct(Long productId);

    public List<Review> getReviewsByUser(Long userId);

    public Review getReviewById(Long id);

    public Review updateReview(Long id, Review updatedReview);

    public void deleteReview(Long id);
}
