package org.example.ezyshop.dto.cart;

import lombok.Data;
import org.example.ezyshop.dto.product.ProductDTO;

@Data
public class CartItemDTO {
    private Long id;
    private Long cartId;
    private ProductDTO productDTO;
    private Integer quantity;
    private double discount;
    private double productPrice;
}
