package org.example.ezyshop.repository;

import org.example.ezyshop.entity.SizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<SizeEntity, Long> {
    List<SizeEntity> findAllByVariantId(Long variantId);
}
