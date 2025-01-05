package org.example.ezyshop.controller;

import lombok.RequiredArgsConstructor;
import org.example.ezyshop.entity.Review;
import org.example.ezyshop.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @GetMapping("/product/{productId}/user/{userId}")
    public ResponseEntity<Review> getReviewByProductAndUser(@PathVariable Long productId, @PathVariable Long userId) {
        Review review = service.getReviewByProductAndUser(productId, userId);
        return ResponseEntity.ok(review);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review savedReview = service.createReview(review);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable Long productId) {
        List<Review> reviews = service.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable Long userId) {
        List<Review> reviews = service.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = service.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review updatedReview) {
        Review review = service.updateReview(id, updatedReview);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        service.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
