package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.user.UserDTO;
import org.example.ezyshop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper Mapper = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    User toModel(UserDTO userDTO);

}
