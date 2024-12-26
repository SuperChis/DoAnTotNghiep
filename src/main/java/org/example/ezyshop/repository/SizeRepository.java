package org.example.ezyshop.repository;

import org.example.ezyshop.entity.SizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<SizeEntity, Long> {
    @Query("SELECT s " +
            "FROM SizeEntity s " +
            "WHERE s.variant.id = (?1) " +
            "ORDER BY s.created")
    List<SizeEntity> findAllByVariantId(Long variantId);
}
