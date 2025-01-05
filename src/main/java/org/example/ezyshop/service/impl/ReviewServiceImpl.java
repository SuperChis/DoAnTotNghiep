package org.example.ezyshop.service.impl;

import org.example.ezyshop.entity.Review;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.repository.ReviewRepository;
import org.example.ezyshop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository repository;

    public Review getReviewByProductAndUser(Long productId, Long userId) {
        return repository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new NotFoundException(false,404,"Review not found for productId: " + productId + " and userId: " + userId));
    }

    public Review createReview(Review review) {
        return repository.save(review);
    }

    public List<Review> getReviewsByProduct(Long productId) {
        return repository.findByProductId(productId);
    }

    public List<Review> getReviewsByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public Review getReviewById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(false, 404, "Review not found with id: " + id));
    }

    public Review updateReview(Long id, Review updatedReview) {
        Review existingReview = getReviewById(id);
        existingReview.setContent(updatedReview.getContent())
                .setRating(updatedReview.getRating())
                .setVerifiedPurchase(updatedReview.isVerifiedPurchase());
        return repository.save(existingReview);
    }

    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        repository.delete(review);
    }
}
