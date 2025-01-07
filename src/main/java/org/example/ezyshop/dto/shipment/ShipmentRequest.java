package org.example.ezyshop.dto.shipment;

import lombok.Data;
import org.example.ezyshop.enums.ShipmentStatus;

@Data
public class ShipmentRequest {
    private Long orderId;
    private Long shipmentId;
    private ShipmentStatus status;
    private String orderStatus;
}
