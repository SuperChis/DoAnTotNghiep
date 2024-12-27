package org.example.ezyshop.controller.admin;

import jakarta.validation.Valid;
import org.example.ezyshop.constant.Constants;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.ProductResponse;
import org.example.ezyshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/public")
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

    @GetMapping("/public/search/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = Constants.SORT_BY, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy,
                sortOrder);

        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest request,
                                                    @PathVariable Long productId) {
        ProductResponse updatedProduct = productService.updateProduct(productId, request);

        return new ResponseEntity<ProductResponse>(updatedProduct, HttpStatus.OK);
    }


    @DeleteMapping("/admin/products/{productId}")
    public ProductResponse deleteProductByCategory(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }
}
