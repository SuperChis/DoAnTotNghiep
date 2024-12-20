package org.example.ezyshop.controller;

import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreResponse;
import org.example.ezyshop.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
public class StoreController {
    @Autowired
    private StoreService service;

    @GetMapping("/info")
    public ResponseEntity<StoreResponse> getStoreInforByUser() {
        StoreResponse response = service.getStoreInforByUser();
        return ResponseEntity.ok(response);
    }
}
