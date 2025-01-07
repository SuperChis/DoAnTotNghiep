package org.example.ezyshop.dto.order;

import lombok.Data;
import org.example.ezyshop.dto.payment.PaymentDTO;
import org.example.ezyshop.dto.shipment.ShipmentDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String email;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private PaymentDTO paymentDTO;
    private List<OrderItemDTO> items;
    private ShipmentDTO shipment;
}

