package org.example.ezyshop.dto.payment;

import lombok.Data;

@Data
public class PaymentResponse {
    private String status;
    private String message;
    private String paymentUrl;
    private String transactionId;
}