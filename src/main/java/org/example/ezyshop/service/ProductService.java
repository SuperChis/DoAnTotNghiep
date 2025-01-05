package org.example.ezyshop.service;

import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;


public interface ProductService {

    ProductResponse createProduct( ProductRequest request);

    ProductResponse addThumbnailProduct(Long productId, MultipartFile file);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                                     String sortOrder);

    ProductResponse searchByStore(Long storeId, Integer pageNumber, Integer pageSize, String sortBy,
                                     String sortOrder);

    ProductResponse getById(Long id);

    ProductResponse updateProduct(Long productId, ProductRequest request);

//    ProductResponse updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse searchProduct(String keyword, Long minPrice, Long maxPrice, Long categoryId, Long storeId,
                                  Integer pageNumber, Integer pageSize, String sortBy,
                                  String sortOrder);

    ProductResponse deleteProduct(Long productId);
}
