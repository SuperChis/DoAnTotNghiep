package org.example.ezyshop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ezyshop.dto.category.CategoryDTO;
import org.example.ezyshop.dto.review.ReviewDTO;
import org.example.ezyshop.dto.variant.VariantDTO;
import org.example.ezyshop.entity.Review;
import org.example.ezyshop.entity.Variant;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
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
    private List<VariantDTO> variantDTOS;
    List<ReviewDTO> reviewDTOS;
}
