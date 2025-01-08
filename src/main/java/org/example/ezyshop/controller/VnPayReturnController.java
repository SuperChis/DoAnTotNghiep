package org.example.ezyshop.controller;

import lombok.RequiredArgsConstructor;
import org.example.ezyshop.service.OrderService;
import org.example.ezyshop.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class VnPayReturnController {
    private final VNPayService vnPayService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/vnpay-return")
    public String vnpayReturn(@RequestParam Map<String, String> queryParams, Model model) {
        int paymentStatus = vnPayService.orderReturn(queryParams);
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String orderInfo = queryParams.get("vnp_OrderInfo");
        String paymentTime = queryParams.get("vnp_PayDate");
        String transactionId = queryParams.get("vnp_TransactionNo");
        String totalPrice = queryParams.get("vnp_Amount");

        // Thêm thông tin vào model để hiển thị trong giao diện
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        if (paymentStatus == 1 && "00".equals(vnp_ResponseCode)) {
            // Thanh toán thành công
            Long orderId = Long.parseLong(orderInfo);
            orderService.updateOrderStatus(orderId, "PAID");
            return "ordersuccess"; // Tên của file ordersuccess.html trong thư mục templates
        } else {
            // Thanh toán thất bại
            return "orderFail"; // Tên của file orderFail.html trong thư mục templates
        }
    }
}
