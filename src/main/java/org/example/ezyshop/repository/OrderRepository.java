package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.user.id = (?1) " +
            "   AND o.isDeleted = false " +
            "ORDER BY o.created desc ")
    List<Order> findByUserId(Long userId);

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "JOIN OrderItem oi ON o.id = oi.order.id " +
            "JOIN Product p ON oi.product.id = p.id " +
            "WHERE p.store.id = :storeId AND o.isDeleted = false " +
            "     AND o.isDeleted = false " +
            "     AND p.isDeleted = false " +
            "ORDER BY o.created desc ")
    Page<Order> findOrdersByStoreId(@Param("storeId") Long storeId, Pageable pageable);
}
