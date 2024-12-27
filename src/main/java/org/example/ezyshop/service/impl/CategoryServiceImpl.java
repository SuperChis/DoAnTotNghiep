package org.example.ezyshop.service.impl;

import jakarta.transaction.Transactional;
import org.example.ezyshop.dto.category.CategoryDTO;
import org.example.ezyshop.dto.category.CategoryRequest;
import org.example.ezyshop.dto.category.CategoryResponse;
import org.example.ezyshop.dto.pagination.PageDto;
import org.example.ezyshop.entity.Category;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.mapper.CategoryMapper;
import org.example.ezyshop.repository.CategoryRepository;
import org.example.ezyshop.service.CategoryService;
import org.example.ezyshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category savedCategory = repository.findByNameAndIsDeletedFalse(request.getName());

        if (savedCategory != null) {
            throw new RequetFailException(false, 400, "Category with the name '" + request.getName() + "' already exists !!!");
        }

        Category category = CategoryMapper.MAPPER.toModel(request);

        repository.save(category);

        CategoryDTO categoryDTO = CategoryMapper.MAPPER.toDTO(category);

        return new CategoryResponse(true, 200, "create category successfully")
                .setDto(categoryDTO);
    }

    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Category> pageCategories = repository.findByIsDeletedFalse(pageable);

        List<CategoryDTO> dtoList = pageCategories.getContent().stream()
                .map(CategoryMapper.MAPPER::toDTO)
                .collect(Collectors.toList());

        return new CategoryResponse(true, 200)
                .setDtoList(dtoList)
                .setPageDto(PageDto.populatePageDto(pageCategories));
    }

    @Override
    public CategoryResponse getAllCategory() {
        List<Category> categories = repository.findAll();

        List<CategoryDTO> dtoList = categories.stream()
                .map(CategoryMapper.MAPPER::toDTO)
                .collect(Collectors.toList());

        return new CategoryResponse(true, 200)
                .setDtoList(dtoList);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(CategoryRequest request, Long categoryId) {
        Category savedCategory = repository.findByIdAndIsDeletedFalse(categoryId);
        if (savedCategory == null) {
            throw new NotFoundException(false, 400, "Category");
        }

        CategoryMapper.MAPPER.updateCategoryFromRequest(request, savedCategory);

        return new CategoryResponse(true, 200)
                .setDto(CategoryMapper.MAPPER.toDTO(savedCategory));
    }

    @Override
    @Transactional
    public CategoryResponse deleteCategory(Long categoryId) {
        Category savedCategory = repository.findByIdAndIsDeletedFalse(categoryId);
        if (savedCategory == null) {
            throw new NotFoundException(false, 400, "Category");
        }

        savedCategory.setDeleted(true);
        repository.save(savedCategory);
        return new CategoryResponse(true, 200);
    }


}
