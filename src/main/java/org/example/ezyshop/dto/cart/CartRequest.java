package org.example.ezyshop.dto.cart;

import lombok.Data;

@Data
public class CartRequest {
    private Long cartItemId;
    private Long sizeId;
    private Integer quantity;
}
