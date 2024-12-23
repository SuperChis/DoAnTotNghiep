package org.example.ezyshop.controller;

import jakarta.validation.Valid;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.ProductResponse;
import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreResponse;
import org.example.ezyshop.dto.variant.VariantRequest;
import org.example.ezyshop.dto.variant.VariantResponse;
import org.example.ezyshop.service.ProductService;
import org.example.ezyshop.service.StoreService;
import org.example.ezyshop.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/store")
public class StoreController {
    @Autowired
    private StoreService service;

    @Autowired
    private ProductService productService;

    @Autowired
    private VariantService variantService;

    @GetMapping("/info")
    public ResponseEntity<StoreResponse> getStoreInforByUser() {
        StoreResponse response = service.getStoreInforByUser();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-product")
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request,
                                                      @RequestParam(value = "file", required = false) MultipartFile file ) {

        ProductResponse response = productService.createProduct(request, file);

        return new ResponseEntity<ProductResponse>(response, HttpStatus.CREATED);
    }

    @GetMapping("/variant/{productId}")
    public VariantResponse getAllVariants(@PathVariable Long productId) {
        return variantService.getAllVariants(productId);
    }


    @PostMapping
    public ResponseEntity<VariantResponse> createVariant(@RequestBody VariantRequest request,
                                                         @RequestParam(value = "file", required = false) MultipartFile file) {

        return ResponseEntity.status(201).body(variantService.createVariant(request, file));
    }

    @PutMapping("variant/{id}")
    public ResponseEntity<VariantResponse> updateVariant(@PathVariable Long id,
                                                         @RequestBody VariantRequest request) {

        return ResponseEntity.ok(variantService.updateVariant(id, request));
    }

    @DeleteMapping("/variant/{id}")
    public ResponseEntity<VariantResponse
            > deleteVariant(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.delete(id));
    }
}
