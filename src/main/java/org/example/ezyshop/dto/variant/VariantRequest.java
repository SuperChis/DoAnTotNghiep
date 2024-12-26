package org.example.ezyshop.dto.variant;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class VariantRequest {

    private String attribute;

    private Long productId;

    private MultipartFile file;
}
