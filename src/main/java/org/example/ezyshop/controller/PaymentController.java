package org.example.ezyshop.controller;

import lombok.RequiredArgsConstructor;
import org.example.ezyshop.dto.payment.PaymentRequest;
import org.example.ezyshop.dto.payment.PaymentResponse;
import org.example.ezyshop.dto.payment.PaymentResultResponse;
import org.example.ezyshop.service.OrderService;
import org.example.ezyshop.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final VNPayService vnPayService;

    @Autowired
    private OrderService orderService;

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

//    @GetMapping("/vnpay-return")
//    public ResponseEntity<PaymentResultResponse> vnpayReturn(@RequestParam Map<String, String> queryParams) {
//        int paymentStatus =vnPayService.orderReturn(queryParams);
//        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
//        String vnp_TxnRef = queryParams.get("vnp_TxnRef");
//
//        String orderInfo = queryParams.get("vnp_OrderInfo");
//        String paymentTime = queryParams.get("vnp_PayDate");
//        String transactionId = queryParams.get("vnp_TransactionNo");
//        String totalPrice = queryParams.get("vnp_Amount");
//
//        if (paymentStatus == 1) {
//            if ("00".equals(vnp_ResponseCode)) {
//                // Payment successful
//                PaymentResultResponse response =  new PaymentResultResponse();
//                response.setAmount(Integer.parseInt(totalPrice));
//                response.setTransactionId(transactionId);
//                response.setPaymentTime(paymentTime);
//                response.setOrderInfo(orderInfo + " has payment successfully");
//                response.setMessage("success");
//                Long orderId = Long.parseLong(orderInfo);
//                orderService.updateOrderStatus(orderId, "PAID");
//                return ResponseEntity.ok(response);
//            } else {
//                // Payment failed
//                return null;
//            }
//        } else {
//            return null;
//        }
//    }
}
