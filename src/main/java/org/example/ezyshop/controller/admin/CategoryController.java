package org.example.ezyshop.controller.admin;

import jakarta.validation.Valid;
import org.example.ezyshop.constant.Constants;
import org.example.ezyshop.dto.category.CategoryRequest;
import org.example.ezyshop.dto.category.CategoryResponse;
import org.example.ezyshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @PostMapping("/admin")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse savedCategoryDTO = service.createCategory(request);

        return new ResponseEntity<CategoryResponse>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = Constants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_DIR, required = false) String sortOrder) {

        CategoryResponse categoryResponse = service.getCategories(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<CategoryResponse> getAllCategory() {

        CategoryResponse categoryResponse = service.getAllCategory();

        return new ResponseEntity<CategoryResponse>(categoryResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody CategoryRequest request,
                                                      @PathVariable Long categoryId) {
        CategoryResponse categoryDTO = service.updateCategory(request, categoryId);

        return new ResponseEntity<CategoryResponse>(categoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/{categoryId}")
    public CategoryResponse deleteCategory(@PathVariable Long categoryId) {
        return service.deleteCategory(categoryId);
    }
}
