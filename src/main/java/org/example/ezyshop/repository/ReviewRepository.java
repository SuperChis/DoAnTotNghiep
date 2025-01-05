package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByProductIdAndUserId(Long productId, Long userId);
}
