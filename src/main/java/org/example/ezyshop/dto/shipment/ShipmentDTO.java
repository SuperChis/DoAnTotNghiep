package org.example.ezyshop.dto.shipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ezyshop.dto.order.OrderDTO;
import org.example.ezyshop.enums.ShipmentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDTO {
    private String name;
    private String address;
    private double price;
    private ShipmentStatus status;
    private OrderDTO orderDTO;
}
