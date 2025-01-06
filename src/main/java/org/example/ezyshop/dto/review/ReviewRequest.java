package org.example.ezyshop.dto.review;

import lombok.Data;

@Data
public class ReviewRequest {
    private String content;

    private int rating;

    private Long productId;

    private boolean verifiedPurchase;
}
