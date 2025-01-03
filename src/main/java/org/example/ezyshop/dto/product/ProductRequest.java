package org.example.ezyshop.dto.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductRequest {
    private String name;
    private String description;
//    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    private String summary;
    private Long categoryId;
    private MultipartFile file;
}
