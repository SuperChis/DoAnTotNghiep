package org.example.ezyshop.service;

import org.example.ezyshop.dto.order.OrderDTO;
import org.example.ezyshop.dto.order.OrderRequest;
import org.example.ezyshop.dto.order.OrderResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderDetails(Long orderId);
    OrderResponse getALlOrderByUser();
    OrderResponse getALlOrderByStore(Pageable pageable);
    void updateOrderStatus(Long orderId, String status);
}
