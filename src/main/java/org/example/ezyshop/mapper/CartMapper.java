package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.cart.CartDTO;
import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.entity.Cart;
import org.example.ezyshop.entity.CartItem;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = { CartItemMapper.class , ProductMapper.class})
public interface CartMapper {
    CartMapper MAPPER = Mappers.getMapper(CartMapper.class);

//    @Mapping(target = "cartItemDTOS", source = "cartItems")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CartDTO toDTO(Cart cart);

//    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Cart toEntity(CartDTO cartDTO);

//    @IterableMapping(elementTargetType = ProductDTO.class)
//    default List<ProductDTO> mapCartItemsToProductDTOs(List<CartItem> cartItems) {
//        return cartItems.stream()
//                .map(cartItem -> ProductMapper.MAPPER.toDTO(cartItem.getProduct()))
//                .collect(Collectors.toList());
//    }
}
