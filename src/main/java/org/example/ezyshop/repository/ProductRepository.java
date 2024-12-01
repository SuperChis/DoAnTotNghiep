package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByProductNameLike(String keyword, Pageable pageDetails);

    @Query(value = "SELECT p " +
            "       FROM Product p " +
            "       WHERE LOWER(p.name) like LOWER(?1) " +
            "           AND p.category.id = ?2 " +
            "           AND p.isDeleted = false ")
    Product findByNameAndCategory(String name, Long categoryId);
}
