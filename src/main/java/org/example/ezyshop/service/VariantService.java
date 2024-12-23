package org.example.ezyshop.service;

import org.example.ezyshop.dto.variant.VariantRequest;
import org.example.ezyshop.dto.variant.VariantResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VariantService {
    public VariantResponse getAllVariants(Long productId);


    public VariantResponse createVariant(VariantRequest request, MultipartFile file);

    public VariantResponse updateVariant(Long id, VariantRequest request);

    public VariantResponse delete(Long id);
}
