package org.example.ezyshop.controller;

import lombok.RequiredArgsConstructor;
import org.example.ezyshop.dto.payment.PaymentRequest;
import org.example.ezyshop.dto.payment.PaymentResponse;
import org.example.ezyshop.service.VNPayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final VNPayService vnPayService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        try {
            PaymentResponse response = vnPayService.createPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setStatus("99");
            errorResponse.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<String> vnpayReturn(@RequestParam Map<String, String> queryParams) {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String vnp_TxnRef = queryParams.get("vnp_TxnRef");

        if (vnPayService.verifyPayment(queryParams)) {
            if ("00".equals(vnp_ResponseCode)) {
                // Payment successful
                return ResponseEntity.ok("Payment successful for transaction: " + vnp_TxnRef);
            } else {
                // Payment failed
                return ResponseEntity.ok("Payment failed for transaction: " + vnp_TxnRef +
                        " with response code: " + vnp_ResponseCode);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid payment signature");
        }
    }
}
