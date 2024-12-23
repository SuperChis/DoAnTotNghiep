package org.example.ezyshop.dto.variant;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VariantDTO {
    private String attribute;
    private Long productId;
    private String productName;
    private String imageUrl;
}
