package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.address.AddressDTO;
import org.example.ezyshop.dto.address.AddressRequest;
import org.example.ezyshop.dto.cart.CartDTO;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.entity.Address;
import org.example.ezyshop.entity.Cart;
import org.example.ezyshop.entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface AddressMapper {
    AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AddressDTO toDTO(Address address);

    //    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Address toEntity(AddressDTO addressDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateAddressFromRequest(AddressRequest request, @MappingTarget Address address);
}
