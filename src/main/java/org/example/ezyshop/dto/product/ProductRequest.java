package org.example.ezyshop.dto.product;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    private Long categoryId;
    private String color; //init variant
    private String size; //init sizeEntity
    private Integer stock;
}
