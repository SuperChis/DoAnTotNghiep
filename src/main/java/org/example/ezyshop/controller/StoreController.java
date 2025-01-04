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
@RequestMapping("/api")
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

    @GetMapping("/store/info")
    public ResponseEntity<StoreResponse> getStoreInforByUser() {
        StoreResponse response = service.getStoreInforByUser();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/store/product/add")
    public ResponseEntity<ProductResponse> addProduct(@ModelAttribute ProductRequest request) {

        ProductResponse response = productService.createProduct(request);

        return new ResponseEntity<ProductResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping("/store/product/thumbnail/{productId}")
    public ResponseEntity<ProductResponse> updateThumbnailProduct(@RequestParam(value = "file", required = false) MultipartFile file,
                                                                  @PathVariable("productId") Long producId) {

        ProductResponse response = productService.addThumbnailProduct(producId, file);

        return new ResponseEntity<ProductResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping("/store/product/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@ModelAttribute ProductRequest request,
                                                         @PathVariable Long productId) {
        ProductResponse updatedProduct = productService.updateProduct(productId, request);

        return new ResponseEntity<ProductResponse>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/store/product/{productId}")
    public ProductResponse deleteProductByCategory(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

    @GetMapping("/public/variant/{productId}")
    public VariantResponse getAllVariants(@PathVariable Long productId) {
        return variantService.getAllVariants(productId);
    }

    @PostMapping("/store/variant/add")
    public ResponseEntity<VariantResponse> createVariant(@ModelAttribute VariantRequest request) {

        return ResponseEntity.status(201).body(variantService.createVariant(request));
    }

    @PutMapping("/store/variant/image/{id}")
    public ResponseEntity<VariantResponse> addVariantImg(@RequestParam(value = "file", required = false) MultipartFile file,
                                                         @PathVariable("id") Long id) {

        return ResponseEntity.status(201).body(variantService.addVariantImage(id, file));
    }

    @PutMapping("/store/variant/{id}")
    public ResponseEntity<VariantResponse> updateVariant(@PathVariable Long id,
                                                         @ModelAttribute VariantRequest request) {

        return ResponseEntity.ok(variantService.updateVariant(id, request));
    }

    @DeleteMapping("/store/variant/{id}")
    public ResponseEntity<VariantResponse> deleteVariant(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.delete(id));
    }

    @GetMapping("/public/size/{variantId}")
    public SizeResponse getAllSizes(@PathVariable("variantId") Long variantId) {
        return sizeService.getAllSizes(variantId);
    }

    @PostMapping("/store/size")
    public SizeResponse createSize(@RequestBody SizeRequest sizeRequest) {
        return sizeService.createSize(sizeRequest);
    }

    @PutMapping("/store/size/{id}")
    public SizeResponse updateSize(@PathVariable Long id, @RequestBody SizeRequest sizeRequest) {
        return sizeService.updateSize(id, sizeRequest);
    }

    @DeleteMapping("/store/size/{id}")
    public SizeResponse deleteSize(@PathVariable Long id) {
        return sizeService.deleteSize(id);
    }
}
