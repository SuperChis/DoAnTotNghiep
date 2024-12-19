package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreDTO;
import org.example.ezyshop.entity.Product;
import org.example.ezyshop.entity.StoreEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

public interface StoreMapper {
    StoreMapper MAPPER = Mappers.getMapper(StoreMapper.class);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "category", ignore = true)
//    @Mapping(target = "cartItems", ignore = true)
//    @Mapping(target = "orderItems", ignore = true)
//    @Mapping(target = "imageURL", source = "image")
//    StoreEntity toModel(CreateStoreRequest request);

    /**
     * Chuyển từ Product entity sang ProductDTO.
     */
    @Mapping(target = "productId", source = "id")
    StoreDTO toDTO(StoreEntity store);

    /**
     * Cập nhật Product entity từ ProductRequest.
     */
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "category", ignore = true)
//    @Mapping(target = "cartItems", ignore = true)
//    @Mapping(target = "orderItems", ignore = true)
//    @Mapping(target = "imageURL", source = "image")
//    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}
