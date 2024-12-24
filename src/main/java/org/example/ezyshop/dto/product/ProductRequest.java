package org.example.ezyshop.dto.product;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    private Long categoryId;
//    private String attribute; //init variant
//    private String size; //init sizeEntity
//    private Integer stock;
}
