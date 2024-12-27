package org.example.ezyshop.service;


import org.example.ezyshop.dto.category.CategoryRequest;
import org.example.ezyshop.dto.category.CategoryResponse;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryResponse getAllCategory();

    CategoryResponse updateCategory(CategoryRequest request, Long categoryId);

    CategoryResponse deleteCategory(Long categoryId);

}
