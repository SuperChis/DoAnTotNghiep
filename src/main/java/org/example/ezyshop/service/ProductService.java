package org.example.ezyshop.service;

import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.ProductResponse;
import org.springframework.data.domain.Pageable;


public interface ProductService {

    ProductResponse createProduct(Long categoryId, ProductRequest request);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                                     String sortOrder);

    ProductResponse updateProduct(Long productId, ProductRequest request);

//    ProductResponse updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
                                           String sortOrder);

    ProductResponse deleteProduct(Long productId);
}
