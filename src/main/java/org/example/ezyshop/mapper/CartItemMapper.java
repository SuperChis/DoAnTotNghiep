package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.cart.CartDTO;
import org.example.ezyshop.dto.cart.CartItemDTO;
import org.example.ezyshop.entity.Cart;
import org.example.ezyshop.entity.CartItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartItemMapper {

    CartItemMapper MAPPER = Mappers.getMapper(CartItemMapper.class);

    @Mapping(target = "cartId", source = "cart.id") // Ánh xạ cartId từ cart
    @Mapping(target = "productDTO", source = "product") // Sử dụng ProductMapper để ánh xạ product -> productDTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CartItemDTO toDTO(CartItem cartItem);

    @Mapping(target = "cart", ignore = true) // Không ánh xạ ngược cart (phải ánh xạ riêng nếu cần)
    @Mapping(target = "product", source = "productDTO") // Sử dụng ProductMapper để ánh xạ productDTO -> product
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CartItem toEntity(CartItemDTO cartItemDTO);
}
