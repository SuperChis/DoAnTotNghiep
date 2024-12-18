package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "imageURL", source = "image")
    Product toModel(ProductRequest request);

    /**
     * Chuyển từ Product entity sang ProductDTO.
     */
    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "image", source = "imageURL")
    @Mapping(target = "categoryDTO", source = "category")
    ProductDTO toDTO(Product product);

    /**
     * Cập nhật Product entity từ ProductRequest.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "imageURL", source = "image")
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);

}
