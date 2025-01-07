package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.user.id = (?1) " +
            "   AND o.isDeleted = false ")
    List<Order> findByUserId(Long userId);
}
