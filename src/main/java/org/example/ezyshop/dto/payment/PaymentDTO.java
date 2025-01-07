package org.example.ezyshop.dto.payment;

import lombok.Data;
import org.example.ezyshop.entity.Order;

@Data
public class PaymentDTO {
    private Long orderId;

    private String paymentMethod;
}
