package org.example.ezyshop.dto.order;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String email;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private List<OrderItemDTO> items;
}

