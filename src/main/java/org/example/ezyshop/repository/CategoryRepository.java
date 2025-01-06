package org.example.ezyshop.repository;

import org.example.ezyshop.dto.category.CategoryDTO;
import org.example.ezyshop.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByNameAndIsDeletedFalse(String name);

    Category findByIdAndIsDeletedFalse(Long id);

    Page<Category> findByIsDeletedFalse(Pageable pageable);

    @Query("SELECT c " +
            "FROM Category c " +
            "WHERE c.isDeleted = false ")
    List<Category> findAll();

    @Query("SELECT new org.example.ezyshop.dto.category.CategoryDTO(c.id, c.name, c.description, COUNT(p.id)) " +
            "FROM Category c " +
            "LEFT JOIN Product p on c.id = p.id " +
            "WHERE c.isDeleted = false " +
            "GROUP BY c.id, c.name, c.description " +
            "ORDER BY COUNT(p.id) DESC")
    List<CategoryDTO> findTopCategoriesByProductCount(Pageable pageable);
}
