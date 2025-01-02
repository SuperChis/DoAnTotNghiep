package org.example.ezyshop.dto.payment;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderInfo;
    private Long amount;
    private String bankCode;
    private String language;
}
