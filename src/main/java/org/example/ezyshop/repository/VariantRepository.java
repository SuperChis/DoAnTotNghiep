package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    Variant findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT v " +
            "FROM Variant v " +
            "WHERE v.isDeleted = false " +
            "   AND v.product.id = (?1) " +
            "   AND v.product.isDeleted = false ")
    List<Variant> findByProductId(Long productId);

    @Query("SELECT v " +
            "FROM Variant v " +
            "WHERE v.isDeleted = false " +
            "   AND v.product.id IN (:productIds) " +
            "   AND v.product.isDeleted = false")
    List<Variant> findByProductIdIn(@Param("productIds") List<Long> productIds);

}
