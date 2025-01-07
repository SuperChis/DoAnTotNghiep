package org.example.ezyshop.repository;

import org.example.ezyshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = "SELECT od " +
            "       FROM OrderItem od " +
            "       WHERE od.id = (?1) " +
            "       AND od.isDeleted = false ")
    OrderItem findByIdAndIsDeletedFalse(Long orderItemId);

    @Query(value = "SELECT od " +
            "       FROM OrderItem od " +
            "       LEFT JOIN Order o ON od.order.id = o.id " +
            "       WHERE o.id = (?1) " +
            "       AND o.isDeleted = false ")
    List<OrderItem> findByOrderIdAndIsDeletedFalse(Long orderId);

    @Query(value = "SELECT od " +
            "       FROM OrderItem od " +
            "       LEFT JOIN Order o ON od.order.id = o.id " +
            "       WHERE o.id IN (?1) " +
            "       AND o.isDeleted = false ")
    List<OrderItem> findByOrderIdIn(List<Long> orderId);

    @Query(value = "SELECT od " +
            "       FROM OrderItem od " +
            "       LEFT JOIN Order o ON od.order.id = o.id " +
            "       WHERE o.id = (?1) " +
            "       AND o.isDeleted = false ")
    List<OrderItem> findByOrderId(Long orderId);
}
