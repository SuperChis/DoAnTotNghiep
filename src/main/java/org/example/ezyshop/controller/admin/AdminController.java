package org.example.ezyshop.controller.admin;

import org.example.ezyshop.dto.store.StoreResponse;
import org.example.ezyshop.service.StoreService;
import org.example.ezyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {
    @Autowired
    private UserService service;

    @Autowired
    private StoreService storeService;

    @GetMapping("/request/details")
    public ResponseEntity<StoreResponse> getCreateStoreRequest() {
        StoreResponse response = storeService.getCreateStoreRequest();
        return ResponseEntity.ok(response);
    }

    // API để xác nhận yêu cầu tạo store
    @PostMapping("/confirm/{id}")
    public ResponseEntity<StoreResponse> confirmCreateStoreRequest(@PathVariable Long id) {
        StoreResponse response = storeService.confirmCreateStoreRequest(id);
        return ResponseEntity.ok(response);
    }
}
