package org.example.ezyshop.dto.order;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private double price;
    private double discount;
}

