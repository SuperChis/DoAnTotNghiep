package org.example.ezyshop.repository;

import org.example.ezyshop.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    @Query("SELECT s " +
            "FROM StoreEntity s " +
            "WHERE s.isDeleted = false ")
    StoreEntity findByName(String name);

    @Query("select s " +
            "from StoreEntity s " +
            "where s.isApproved = false ")
    List<StoreEntity> findAllRequestCreatedStore();
}
