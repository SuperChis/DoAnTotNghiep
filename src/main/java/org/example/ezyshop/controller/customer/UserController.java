package org.example.ezyshop.controller.customer;

import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.address.AddressRequest;
import org.example.ezyshop.dto.address.AddressResponse;
import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreResponse;
import org.example.ezyshop.dto.user.UserRequest;
import org.example.ezyshop.dto.user.UserResponse;
import org.example.ezyshop.service.AddressService;
import org.example.ezyshop.service.FileStorageService;
import org.example.ezyshop.service.StoreService;
import org.example.ezyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private AddressService addressService;

    @Autowired
    FileStorageService storageService;

    @Autowired
    private StoreService storeService;

    @PutMapping("/profile")
    public UserResponse updateProfile(@RequestBody UserRequest request) {
        return service.updateProfile(request);
    }

    @GetMapping("/profile")
    public UserResponse getListUserByAdmin() {
        return service.getUserProfile();
    }

    @PostMapping("/log-out")
    public BaseResponse logOut() {
        return service.logOut();
    }

    @PutMapping("/update-avatar")
    public UserResponse updateAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // Lưu file và lấy URL
            String fileUrl = storageService.storeFile(file);
            return service.updateAvatar(fileUrl);
        } catch (Exception e) {
            return new UserResponse(false, 400, "Could not upload file: " + e.getMessage());
        }

    }

    @PostMapping("/address")
    public AddressResponse addAddress(@RequestBody AddressRequest request) {
        return addressService.addAddress(request);
    }


    @GetMapping("/addresses")
    public AddressResponse getAddresses() {
        return addressService.getAddresses();
    }


    @PutMapping("/address")
    public BaseResponse updateAddress(@RequestBody AddressRequest request) {
        return addressService.updateAddress(request);
    }


    @DeleteMapping("/address/{id}")
    public BaseResponse deleteAddress(@PathVariable Long id) {
        return addressService.deleteAddress(id);
    }

    @PostMapping("/create-store-request")
    public ResponseEntity<StoreResponse> requestCreateStore(@RequestBody CreateStoreRequest request) {
        StoreResponse response = storeService.requestCreateStore(request);
        return ResponseEntity.ok(response);
    }
}
