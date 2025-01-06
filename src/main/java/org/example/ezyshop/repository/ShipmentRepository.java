package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    @Query(value = "SELECT s " +
            "       FROM Shipment s " +
            "       WHERE s.order.id = (?1) ")
    Shipment findByOrderId(Long orderId);
}
