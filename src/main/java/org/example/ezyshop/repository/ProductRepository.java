package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameLike(String keyword, Pageable pageDetails);

    @Query(value = "SELECT p " +
            "       FROM Product p " +
            "       WHERE LOWER(p.name) like LOWER(?1) " +
            "           AND p.category.id = ?2 " +
            "           AND p.isDeleted = false ")
    Product findByNameAndCategory(String name, Long categoryId);

    Product findByNameAndIsDeletedFalse(String name);

    Product findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.category.id = :categoryId " +
            "AND p.isDeleted = false")
    Page<Product> findByCategoryAndIsDeletedFalse(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "LEFT JOIN StoreEntity st ON p.store.id = st.id " +
            "WHERE p.store.id = :storeId " +
            "AND p.isDeleted = false " +
            "AND st.isDeleted = false")
    Page<Product> findByStoreAndIsDeletedFalse(@Param("storeId") Long storeId, Pageable pageable);

    Page<Product> findByIsDeletedFalse(Pageable pageable);

    @Query(value = "SELECT p " +
            "       FROM Product p " +
            "       LEFT JOIN Category c ON p.category.id = c.id " +
            "       WHERE p.isDeleted = FALSE " +
            "           AND c.isDeleted = FALSE " +
            "           AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "           AND (:minPrice IS NULL OR p.originalPrice >= :minPrice ) " +
            "           AND (:maxPrice IS NULL OR p.originalPrice <= :maxPrice )"
    )
    Page<Product> searchByKeyword(@Param("search") String search,
                                  @Param("minPrice") Long minPrice,
                                  @Param("maxPrice") Long maxPrice,
                                  Pageable pageable);
}
