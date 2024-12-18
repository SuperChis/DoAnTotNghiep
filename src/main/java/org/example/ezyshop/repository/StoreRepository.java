package org.example.ezyshop.repository;

import org.example.ezyshop.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    @Query("SELECT s " +
            "FROM StoreEntity s " +
            "WHERE s.isDeleted = false ")
    StoreEntity findByName(String name);
}
