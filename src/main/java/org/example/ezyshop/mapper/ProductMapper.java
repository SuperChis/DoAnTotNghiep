package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.SameProductDTO;
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
    @Mapping(target = "imageURL", ignore = true)
    @Mapping(target="originalPrice",source = "price")
    Product toModel(ProductRequest request);

    /**
     * Chuyển từ Product entity sang ProductDTO.
     */
    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "categoryDTO", source = "category")
    @Mapping(target = "thumbnail", source = "imageURL")
    @Mapping(target = "variantDTOS", source = "variants")
    @Mapping(target="price",source = "originalPrice")
    @Mapping(target="storeId",source = "product.store.id")
    @Mapping(target="storeName",source = "product.store.name")
//    @Mapping(target = "productDTO.variantDTOS.sizeDTOS", source = "product.variants.sizes")
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "categoryDTO", source = "category")
    @Mapping(target = "thumbnail", source = "imageURL")
    @Mapping(target="price",source = "originalPrice")
    @Mapping(target="storeId",source = "product.store.id")
    @Mapping(target="storeName",source = "product.store.name")
    ProductDTO toProductDTOInCart(Product product);

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "categoryDTO", source = "category")
    @Mapping(target = "thumbnail", source = "imageURL")
    @Mapping(target="price",source = "originalPrice")
    @Mapping(target="storeId",source = "product.store.id")
    @Mapping(target="storeName",source = "product.store.name")
    SameProductDTO toSameProductDTO(Product product);

    /**
     * Cập nhật Product entity từ ProductRequest.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "imageURL", ignore = true)
    @Mapping(target="originalPrice",source = "price")
    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);

}
