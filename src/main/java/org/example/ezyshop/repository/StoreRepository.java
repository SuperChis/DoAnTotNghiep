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

    @Query("SELECT s " +
            "FROM StoreEntity s " +
            "WHERE s.isApproved = false " +
            "and s.id = (?1) ")
    StoreEntity findStoreCanBeUpToStore(Long storeId);

    @Query("SELECT s " +
            "FROM StoreEntity s " +
            "LEFT JOIN User u ON s.user.id = u.id " +
            "WHERE s.isApproved = true " +
            "and s.isDeleted = false " +
            "and u.id = (?1) ")
    StoreEntity findByUserId(Long userId);

    @Query("SELECT s " +
            "FROM StoreEntity s " +
            "LEFT JOIN User u ON s.user.id = u.id " +
            "where u.id = (?1) ")
    StoreEntity findStoreIfExists(Long userId);
}
