package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.size.SizeDTO;
import org.example.ezyshop.dto.size.SizeRequest;
import org.example.ezyshop.entity.SizeEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SizeMapper {

    SizeMapper INSTANCE = Mappers.getMapper(SizeMapper.class);

    @Mapping(source = "variant.id", target = "variantId")
    SizeDTO toDTO(SizeEntity sizeEntity);

    @Mapping(source = "variantId", target = "variant.id")
    SizeEntity toEntity(SizeRequest sizeRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "variantId", target = "variant.id")
    void updateEntityFromRequest(SizeRequest sizeRequest, @MappingTarget SizeEntity sizeEntity);
}