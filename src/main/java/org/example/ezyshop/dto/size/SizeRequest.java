package org.example.ezyshop.dto.size;

import lombok.Data;

@Data
public class SizeRequest {
    private Long variantId;
    private String size;
    private Integer stock;
    private double price;
}
