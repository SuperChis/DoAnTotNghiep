package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true) // Bỏ qua ánh xạ cho quan hệ ManyToOne
    @Mapping(target = "products", ignore = true) // Bỏ qua ánh xạ cho quan hệ OneToMany
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "imageURL", source = "image")
    Product toModel(ProductRequest request);

    /**
     * Chuyển từ Product entity sang ProductDTO.
     */
    @Mapping(target = "categoryId", source = "category.id") // Lấy ID của category
    ProductDTO toDTO(Product product);

    /**
     * Cập nhật Product entity từ ProductRequest.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "imageURL", source = "image")
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);

}