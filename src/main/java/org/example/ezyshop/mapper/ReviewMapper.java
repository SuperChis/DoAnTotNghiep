package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.review.ReviewDTO;
import org.example.ezyshop.dto.review.ReviewRequest;
import org.example.ezyshop.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

//    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "user.id", target = "userId")
    ReviewDTO toDto(Review review);

    Review toEntity(ReviewRequest request);
}
