package org.example.ezyshop.controller;

import jakarta.validation.Valid;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.ProductResponse;
import org.example.ezyshop.dto.size.SizeRequest;
import org.example.ezyshop.dto.size.SizeResponse;
import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreResponse;
import org.example.ezyshop.dto.variant.VariantRequest;
import org.example.ezyshop.dto.variant.VariantResponse;
import org.example.ezyshop.service.ProductService;
import org.example.ezyshop.service.SizeService;
import org.example.ezyshop.service.StoreService;
import org.example.ezyshop.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/store")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StoreController {
    @Autowired
    private StoreService service;

    @Autowired
    private ProductService productService;

    @Autowired
    private VariantService variantService;

    @Autowired
    private SizeService sizeService;

    @GetMapping("/info")
    public ResponseEntity<StoreResponse> getStoreInforByUser() {
        StoreResponse response = service.getStoreInforByUser();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/product/add")
    public ResponseEntity<ProductResponse> addProduct(@RequestPart(value = "file", required = false) MultipartFile file,
                                                      @RequestPart("request") ProductRequest request) {

        ProductResponse response = productService.createProduct(request, file);

        return new ResponseEntity<ProductResponse>(response, HttpStatus.CREATED);
    }

    @GetMapping("/variant/{productId}")
    public VariantResponse getAllVariants(@PathVariable Long productId) {
        return variantService.getAllVariants(productId);
    }


    @PostMapping("/variant/add")
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
    public ResponseEntity<VariantResponse> deleteVariant(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.delete(id));
    }

    @GetMapping("/size/{variantId}")
    public SizeResponse getAllSizes(@PathVariable("variantId") Long variantId) {
        return sizeService.getAllSizes(variantId);
    }

    @PostMapping("/size/")
    public SizeResponse createSize(@RequestBody SizeRequest sizeRequest) {
        return sizeService.createSize(sizeRequest);
    }

    @PutMapping("/size/{id}")
    public SizeResponse updateSize(@PathVariable Long id, @RequestBody SizeRequest sizeRequest) {
        return sizeService.updateSize(id, sizeRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteSize(@PathVariable Long id) {
        sizeService.deleteSize(id);
    }
}
