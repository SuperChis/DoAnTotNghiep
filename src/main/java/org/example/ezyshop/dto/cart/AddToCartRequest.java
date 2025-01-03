package org.example.ezyshop.dto.cart;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long productId;
    private Long variantId;
    private Long sizeId;
    private Integer quantity;
}
