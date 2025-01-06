package org.example.ezyshop.service;

import org.example.ezyshop.dto.order.OrderDTO;
import org.example.ezyshop.dto.order.OrderRequest;
import org.example.ezyshop.dto.order.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderDetails(Long orderId);
    OrderResponse getALlOrderByUser();
}
