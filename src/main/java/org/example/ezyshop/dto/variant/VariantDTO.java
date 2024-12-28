package org.example.ezyshop.dto.variant;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.dto.size.SizeDTO;

import java.util.List;

@Data
@Accessors(chain = true)
public class VariantDTO {
    private Long id;
    private String attribute;
    private Long productId;
    private String productName;
    private String imageUrl;
    private List<SizeDTO> sizeDTOS;
}
