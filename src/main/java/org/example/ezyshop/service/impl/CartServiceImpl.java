package org.example.ezyshop.service.impl;

import jakarta.transaction.Transactional;
import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.cart.AddToCartRequest;
import org.example.ezyshop.dto.cart.CartDTO;
import org.example.ezyshop.dto.cart.CartItemDTO;
import org.example.ezyshop.dto.cart.CartResponse;
import org.example.ezyshop.dto.pagination.PageDto;
import org.example.ezyshop.entity.*;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.mapper.ProductMapper;
import org.example.ezyshop.repository.CartItemRepository;
import org.example.ezyshop.repository.CartRepository;
import org.example.ezyshop.repository.ProductRepository;
import org.example.ezyshop.repository.SizeRepository;
import org.example.ezyshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private SizeRepository sizeRepository;

    private CartDTO toDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        List<CartItemDTO> cartItemDTOS = cartItems.stream()
                .map(this::mapToCartItemDTO)
                .collect(Collectors.toList());

        dto.setCartItemDTOS(cartItemDTOS);
        dto.setId(cart.getId());
        dto.setTotalPrice(cart.getTotalPrice());
        return dto;
    }

    private CartItemDTO mapToCartItemDTO(CartItem item) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductDTO(ProductMapper.MAPPER.toDTO(item.getProduct()));
        cartItemDTO.setId(item.getId());
        cartItemDTO.setCartId(item.getCart().getId());
        cartItemDTO.setProductPrice(item.getProductPrice());
        cartItemDTO.setDiscount(item.getDiscount());
        cartItemDTO.setQuantity(item.getQuantity());
        return cartItemDTO;
    }

    @Override
    @Transactional
    public CartResponse addProductToCart(AddToCartRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Cart cart = repository.findByUserId(user.id);

        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId());
        if (product == null) {
            throw new NotFoundException(false, 4004 , request.getProductId() + " not exists");
        }

        if (product.getQuantity() == 0) {
            throw new RequetFailException(false, 400, product.getName() + " is not available");
        }

        if (product.getQuantity() < request.getQuantity()) {
            throw new RequetFailException(false, 400, "Please, make an order of the " + product.getName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(cart.getId(), request.getProductId());
        SizeEntity size = null;
        if (request.getSizeId() != null) {
            size = sizeRepository.findById(request.getSizeId())
                    .orElseThrow(() -> new NotFoundException(false, 404, request.getSizeId() + " not exists"));
            if (size.getStock() == null || size.getStock() <= 0) {
                throw new RequetFailException(false, 400, "Product quantity is not enough");
            }
        }

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setDiscount(product.getDiscount());
            cartItem.setDeleted(false);

        } else {
            cartItem.setQuantity(request.getQuantity() + cartItem.getQuantity());
            cartItem.setDiscount(product.getDiscount());
        }

        if (size != null) {
            cartItem.setProductPrice(size.getPrice());
            size.setStock(size.getStock() - request.getQuantity());
            cartItem.setSizeId(size.getId());
            sizeRepository.save(size);
        } else {
            cartItem.setProductPrice(product.getOriginalPrice());
        }

        product.setQuantity(product.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);

        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * request.getQuantity()));
        cartRepository.save(cart);

        CartDTO dto = toDTO(cart);
        return new CartResponse(true, 200, "add product to cart successfully")
                .setDto(dto);
    }

    @Override
    public CartResponse getAllCarts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            throw new NotFoundException(false, 4004, "cart is not found");
        }

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new NotFoundException(true, 200, "Cart is empty");
        }
        List<CartItemDTO> cartItemDTOS = cartItems.stream()
                .map(this::mapToCartItemDTO)
                .toList();
        CartDTO dto = new CartDTO().setCartItemDTOS(cartItemDTOS);
        return new CartResponse(true, 200)
                .setDto(dto);
    }
//
//    @Override
//    public CartDTO getCart(String emailId, Long cartId) {
//        Cart cart = repository.findCartByEmailAndCartId(emailId, cartId);
//
//        if (cart == null) {
//            throw new ResourceNotFoundException("Cart", "cartId", cartId);
//        }
//
//        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//        List<ProductDTO> products = cart.getCartItems().stream()
//                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
//
//        cartDTO.setProducts(products);
//
//        return cartDTO;
//    }
//
//    @Override
//    public void updateProductInCarts(Long cartId, Long productId) {
//        Cart cart = repository.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
//
//        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(cartId, productId);
//
//        if (cartItem == null) {
//            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
//        }
//
//        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
//
//        cartItem.setProductPrice(product.getSpecialPrice());
//
//        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
//
//        cartItem = cartItemRepository.save(cartItem);
//    }
//
//    @Override
//    public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
//        Cart cart = repository.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
//
//        if (product.getQuantity() == 0) {
//            throw new APIException(product.getProductName() + " is not available");
//        }
//
//        if (product.getQuantity() < quantity) {
//            throw new APIException("Please, make an order of the " + product.getProductName()
//                    + " less than or equal to the quantity " + product.getQuantity() + ".");
//        }
//
//        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(cartId, productId);
//
//        if (cartItem == null) {
//            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
//        }
//
//        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
//
//        product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);
//
//        cartItem.setProductPrice(product.getSpecialPrice());
//        cartItem.setQuantity(quantity);
//        cartItem.setDiscount(product.getDiscount());
//
//        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * quantity));
//
//        cartItem = cartItemRepository.save(cartItem);
//
//        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//        List<ProductDTO> productDTOs = cart.getCartItems().stream()
//                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
//
//        cartDTO.setProducts(productDTOs);
//
//        return cartDTO;
//
//    }
//
//    @Override
//    public String deleteProductFromCart(Long cartId, Long productId) {
//        Cart cart = repository.findById(cartId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
//
//        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(cartId, productId);
//
//        if (cartItem == null) {
//            throw new ResourceNotFoundException("Product", "productId", productId);
//        }
//
//        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
//
//        Product product = cartItem.getProduct();
//        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
//
//        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
//
//        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
//    }

}

