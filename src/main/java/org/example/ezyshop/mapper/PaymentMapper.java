package org.example.ezyshop.mapper;

import org.example.ezyshop.dto.payment.PaymentDTO;
import org.example.ezyshop.dto.payment.PaymentRequest;
import org.example.ezyshop.dto.review.ReviewDTO;
import org.example.ezyshop.dto.review.ReviewRequest;
import org.example.ezyshop.entity.Payment;
import org.example.ezyshop.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    //    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "order.id", target = "orderId")
    PaymentDTO toDto(Payment payment);

    Payment toEntity(PaymentRequest request);
}
