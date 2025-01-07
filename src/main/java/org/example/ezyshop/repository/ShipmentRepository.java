package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    @Query(value = "SELECT s " +
            "       FROM Shipment s " +
            "       WHERE s.order.id = (?1) ")
    Shipment findByOrderId(Long orderId);

    @Query("SELECT s " +
            "FROM Shipment s " +
            "Where s.order.id IN (?1) ")
    List<Shipment> findByOrderIdIn(List<Long> orderIds);

    @Query("SELECT s " +
            "FROM Shipment s " +
            "Where s.shipper.id = ?1 ")
    Page<Shipment> findByShipperId(Long shipperId, Pageable pageable);

}
