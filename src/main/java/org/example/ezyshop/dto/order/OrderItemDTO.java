package org.example.ezyshop.dto.order;

import lombok.Data;
import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.size.SizeDTO;
import org.example.ezyshop.dto.variant.VariantDTO;

@Data
public class OrderItemDTO {
    private Long orderId;
    private Long orderItemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private double price;
    private double discount;
    private SizeDTO sizeDTO;
    private VariantDTO variantDTO;
}

