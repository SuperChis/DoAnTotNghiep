package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p " +
            "FROM Payment p " +
            "WHERE p.order.id = (?1) ")
    Payment findByOrderId(Long orderId);
}
