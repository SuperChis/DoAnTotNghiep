package org.example.ezyshop.dto.product;

import lombok.Data;
import org.example.ezyshop.dto.category.CategoryDTO;
import org.example.ezyshop.dto.variant.VariantDTO;

import java.util.List;

@Data
public class SameProductDTO {
    private Long productId;
    private String productName;
    private String thumbnail;
    private String description;
    private String summary;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    private CategoryDTO categoryDTO;
//    private List<VariantDTO> variantDTOS;
    private Long storeId;
    private String storeName;
    private Long reviewNumber;
    private Double averageRating;
}
