package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreDTO;
import org.example.ezyshop.entity.Product;
import org.example.ezyshop.entity.StoreEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
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
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "userEmail", source = "user.email")
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
