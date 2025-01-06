package org.example.ezyshop.controller.admin;

import org.example.ezyshop.constant.Constants;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.ProductResponse;
import org.example.ezyshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

//    @PostMapping("/admin/categories")
//    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request,
//                                                      @RequestParam(value = "file", required = false) MultipartFile file ) {
//
//        ProductResponse response = productService.createProduct(request, file);
//
//        return new ResponseEntity<ProductResponse>(response, HttpStatus.CREATED);
//    }

    @GetMapping("/public/highlight")
    public ResponseEntity<ProductResponse> getHighlightProducts() {

        ProductResponse productResponse = productService.getHighlightProducts();

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/all")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = Constants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = Constants.SORT_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/store/{storeId}")
    public ResponseEntity<ProductResponse> getProductsByStore(@PathVariable Long storeId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = Constants.SORT_BY, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchByStore(storeId, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public")
    public ResponseEntity<ProductResponse> getProductsByStore(@RequestParam(name = "id") Long id) {

        ProductResponse productResponse = productService.getById(id);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/search")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@RequestParam(name = "keyword", required = false) String keyword,
                                                                @RequestParam(name = "minPrice", required = false) Long minPrice,
                                                                @RequestParam(name = "maxPrice", required = false) Long maxPrice,
                                                                @RequestParam(name = "categoryId", required = false) Long categoryId,
                                                                @RequestParam(name = "storeId", required = false) Long storeId,
                                                                @RequestParam(name = "pageNumber", defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = Constants.SORT_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchProduct(keyword, minPrice, maxPrice, categoryId, storeId, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest request,
                                                    @PathVariable Long productId) {
        ProductResponse updatedProduct = productService.updateProduct(productId, request);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }


    @DeleteMapping("/admin/products/{productId}")
    public ProductResponse deleteProductByCategory(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }
}
