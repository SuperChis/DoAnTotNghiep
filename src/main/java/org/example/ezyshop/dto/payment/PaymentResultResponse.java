package org.example.ezyshop.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResultResponse {
    private String status;
    private String message;
    private String transactionId;
    private long amount;
    private String orderInfo;
    private String bankCode;
    private String paymentTime;
}
