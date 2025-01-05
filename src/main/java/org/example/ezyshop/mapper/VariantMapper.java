package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.variant.VariantDTO;
import org.example.ezyshop.dto.variant.VariantRequest;
import org.example.ezyshop.entity.Product;
import org.example.ezyshop.entity.Variant;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VariantMapper {

    VariantMapper MAPPER = Mappers.getMapper(VariantMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "product", ignore = true)
    Variant toModel(VariantRequest request);

    /**
     * Chuyển từ Product entity sang ProductDTO.
     */
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "sizeDTOS", source = "variant.sizes")
    VariantDTO toDTO(Variant variant);

    /**
     * Cập nhật Product entity từ ProductRequest.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imageUrl", ignore = true)
    void updateVariantFromRequest(VariantRequest request, @MappingTarget Variant variant);
}
