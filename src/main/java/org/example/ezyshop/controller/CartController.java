package org.example.ezyshop.controller;

import org.example.ezyshop.constant.Constants;
import org.example.ezyshop.dto.cart.AddToCartRequest;
import org.example.ezyshop.dto.cart.CartDTO;
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

    @GetMapping("/user/")
    public ResponseEntity<CartResponse> getCarts() {

        return new ResponseEntity<CartResponse>(service.getAllCarts(), HttpStatus.FOUND);
    }
//
//    @GetMapping("/public/users/{emailId}/carts/{cartId}")
//    public ResponseEntity<CartDTO> getCartById(@PathVariable String emailId, @PathVariable Long cartId) {
//        CartDTO cartDTO = service.getCart(emailId, cartId);
//
//        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.FOUND);
//    }
//
//    @PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
//    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
//        CartDTO cartDTO = service.updateProductQuantityInCart(cartId, productId, quantity);
//
//        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/public/carts/{cartId}/product/{productId}")
//    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
//        String status = service.deleteProductFromCart(cartId, productId);
//
//        return new ResponseEntity<String>(status, HttpStatus.OK);
//    }
}
