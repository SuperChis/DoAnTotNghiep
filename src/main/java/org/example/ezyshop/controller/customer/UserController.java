package org.example.ezyshop.controller.customer;

import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.address.AddressRequest;
import org.example.ezyshop.dto.address.AddressResponse;
import org.example.ezyshop.dto.store.CreateStoreRequest;
import org.example.ezyshop.dto.store.StoreRequestState;
import org.example.ezyshop.dto.store.StoreResponse;
import org.example.ezyshop.dto.user.UserRequest;
import org.example.ezyshop.dto.user.UserResponse;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.service.*;
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

    @Autowired
    private AmazonClient amazonClient;

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
            String url = amazonClient.uploadFile(file);
            return service.updateAvatar(url);
        } catch (Exception e) {
            throw new RequetFailException("upload error");
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

    @GetMapping("/store-request-state")
    public ResponseEntity<StoreRequestState> getStoreRequestState() {
        StoreRequestState response = storeService.getStoreRequestState();
        return ResponseEntity.ok(response);
    }
}
