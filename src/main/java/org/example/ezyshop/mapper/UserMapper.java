package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.user.UserDTO;
import org.example.ezyshop.dto.user.UserRequest;
import org.example.ezyshop.entity.Product;
import org.example.ezyshop.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper Mapper = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    User toModel(UserDTO userDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", ignore = true)
    void updateUserFromRequest(UserRequest request, @MappingTarget User user);
}
