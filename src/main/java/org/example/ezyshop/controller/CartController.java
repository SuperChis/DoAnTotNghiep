package org.example.ezyshop.controller;

import org.example.ezyshop.constant.Constants;
import org.example.ezyshop.dto.cart.AddToCartRequest;
import org.example.ezyshop.dto.cart.CartDTO;
import org.example.ezyshop.dto.cart.CartRequest;
import org.example.ezyshop.dto.cart.CartResponse;
import org.example.ezyshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService service;

    @PostMapping("/user/add")
    public ResponseEntity<CartResponse> addProductToCart(@RequestBody AddToCartRequest request) {
        CartResponse response = service.addProductToCart(request);

        return new ResponseEntity<CartResponse>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<CartResponse> getCarts() {
        return new ResponseEntity<CartResponse>(service.getAllCarts(), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<CartResponse> updateCart(@RequestBody CartRequest request) {
        CartResponse response = service.updateCart(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/user")
    public ResponseEntity<CartResponse> deleteCartItem(@RequestParam("cartItemId") Long cartItemId) {
        CartResponse status = service.deleteCartItem(cartItemId);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
