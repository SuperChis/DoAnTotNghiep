package org.example.ezyshop.service;

import org.example.ezyshop.dto.shipment.ShipmentRequest;
import org.example.ezyshop.dto.shipment.ShipmentResponse;
import org.springframework.data.domain.Pageable;

public interface ShipperService {
    ShipmentResponse updateShipment(ShipmentRequest request);

    ShipmentResponse getAllShipment(Pageable pageable);
}
