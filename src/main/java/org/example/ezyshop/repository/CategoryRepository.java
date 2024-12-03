package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByNameAndIsDeletedFalse(String name);

    Category findByIdAndIsDeletedFalse(Long id);

    Page<Category> findByIsDeletedFalse(Pageable pageable);
}
