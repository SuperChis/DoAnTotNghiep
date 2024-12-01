package org.example.ezyshop.dto.product;

import lombok.Getter;

@Getter
public class ProductRequest {
    private String name;
    private String image;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
}
