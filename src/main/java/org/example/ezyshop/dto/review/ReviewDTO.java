package org.example.ezyshop.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private String content;
    private int rating;
    private boolean verifiedPurchase;
    private Long productId;
    private Long userId;
}
