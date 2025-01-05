package org.example.ezyshop.service;

import org.example.ezyshop.dto.cart.AddToCartRequest;
import org.example.ezyshop.dto.cart.CartDTO;
import org.example.ezyshop.dto.cart.CartRequest;
import org.example.ezyshop.dto.cart.CartResponse;
import org.example.ezyshop.dto.category.CategoryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
    CartResponse addProductToCart(AddToCartRequest request);

    CartResponse getAllCarts();

    CartResponse updateCart(CartRequest request);
//    CartDTO getCart(String emailId, Long cartId);
//
//    CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity);
//
//    void updateProductInCarts(Long cartId, Long productId);
//
    CartResponse deleteCartItem(Long cartItemId);
}
