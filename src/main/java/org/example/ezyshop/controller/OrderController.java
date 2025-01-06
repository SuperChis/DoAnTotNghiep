package org.example.ezyshop.controller;

import org.example.ezyshop.dto.order.OrderDTO;
import org.example.ezyshop.dto.order.OrderRequest;
import org.example.ezyshop.dto.order.OrderResponse;
import org.example.ezyshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable Long orderId) {
        OrderResponse response = orderService.getOrderDetails(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
