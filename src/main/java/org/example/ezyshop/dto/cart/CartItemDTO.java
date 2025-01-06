package org.example.ezyshop.dto.cart;

import lombok.Data;
import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.variant.VariantDTO;

@Data
public class CartItemDTO {
    private Long id;
    private Long cartId;
    private ProductDTO productDTO;
    private Integer quantity;
    private double discount;
    private double productPrice;
    private String size;
    private String attribute;
    private Long sizeId;
    private Long sizeStock;
    private String urlImage;
    private VariantDTO variantDTO;
}
