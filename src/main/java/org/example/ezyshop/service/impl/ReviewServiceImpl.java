package org.example.ezyshop.service.impl;

import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.review.ReviewDTO;
import org.example.ezyshop.dto.review.ReviewRequest;
import org.example.ezyshop.dto.review.ReviewResponse;
import org.example.ezyshop.entity.Product;
import org.example.ezyshop.entity.Review;
import org.example.ezyshop.entity.User;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.mapper.ReviewMapper;
import org.example.ezyshop.repository.ProductRepository;
import org.example.ezyshop.repository.ReviewRepository;
import org.example.ezyshop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository repository;
    @Autowired
    private ProductRepository productRepository;

    public Review getReviewByProductAndUser(Long productId, Long userId) {
        return repository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new NotFoundException(false,404,"Review not found for productId: " + productId + " and userId: " + userId));
    }

    public ReviewResponse createReview(ReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Review review = ReviewMapper.INSTANCE.toEntity(request);
        Product product = productRepository.findByIdAndIsDeletedFalse(review.getId());
        if (product == null) {
            throw new NotFoundException(false, 404, "product is reviewed not exists");
        }
        review.setUser(user);
        review.setProduct(product);
        repository.save(review);
        ReviewDTO dto = ReviewMapper.INSTANCE.toDto(review);
        ReviewResponse response = new ReviewResponse(true, 200).setReviewDTO(dto);
        return response;
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
