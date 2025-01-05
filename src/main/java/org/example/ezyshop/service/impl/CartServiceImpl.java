package org.example.ezyshop.service.impl;

import jakarta.transaction.Transactional;
import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.cart.*;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        cartItemDTO.setProductDTO(ProductMapper.MAPPER.toProductDTOInCart(item.getProduct()));
        cartItemDTO.setId(item.getId());
        cartItemDTO.setCartId(item.getCart().getId());
        cartItemDTO.setProductPrice(item.getProductPrice());
        cartItemDTO.setDiscount(item.getDiscount());
        cartItemDTO.setQuantity(item.getQuantity());
        cartItemDTO.setSizeId(item.getSizeId());
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

        SizeEntity size = sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new NotFoundException(false, 404, request.getSizeId() + " not exists"));

        if (size.getStock() == null || size.getStock() <= 0) {
            throw new RequetFailException(false, 400, "Product quantity is not enough");
        }

//        if (request.getSizeId() != null) {
//            size = sizeRepository.findById(request.getSizeId())
//                    .orElseThrow(() -> new NotFoundException(false, 404, request.getSizeId() + " not exists"));
//            if (size.getStock() == null || size.getStock() <= 0) {
//                throw new RequetFailException(false, 400, "Product quantity is not enough");
//            }
//        }

        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(cart.getId(), request.getProductId(), request.getSizeId());

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setDiscount(product.getDiscount());
            cartItem.setSizeId(size.getId());
            double specialPrice = size.getPrice() - ((product.getDiscount() * 0.01) * size.getPrice());
            cartItem.setProductPrice(specialPrice);
            cartItem.setDeleted(false);
            cartItem.setCreated(new Date());
            cartItem.setLastUpdate(new Date());

        } else {
            double specialPrice = size.getPrice() - ((product.getDiscount() * 0.01) * size.getPrice());
            cartItem.setQuantity(request.getQuantity() + cartItem.getQuantity());
            cartItem.setDiscount(product.getDiscount());
            cartItem.setProductPrice(specialPrice);
            cartItem.setLastUpdate(new Date());
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

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        Map<Long, CartItem> mapCartItemBySizeId = cartItems.stream()
                .collect(Collectors.toMap(
                        CartItem::getSizeId, // Sử dụng sizeId làm key
                        Function.identity(), // Giá trị là chính cartItem
                        (existing, replacement) -> existing // Xử lý trường hợp trùng key: giữ lại item đã tồn tại
                ));
        List<Long> sizeIds = cartItems.stream().map(CartItem::getSizeId).toList();
        List<SizeEntity> sizes = sizeRepository.findByIdIn(sizeIds);
        Map<Long, SizeEntity> mapSizeById = sizes.stream()
                .collect(Collectors.toMap(
                        SizeEntity::getId, // Sử dụng sizeId làm key
                        Function.identity() // Giá trị là chính SizeEntity
                ));

        if (cartItems.isEmpty()) {
            throw new NotFoundException(true, 200, "Cart is empty");
        }

//        List<CartItemDTO> cartItemDTOS = cartItems.stream()
//                .map(this::mapToCartItemDTO)
//                .toList();
        List<CartItemDTO> cartItemDTOS = cartItems.stream()
                .map(cartItem -> {
                    CartItemDTO dto = mapToCartItemDTO(cartItem); // Hàm ánh xạ cơ bản
                    SizeEntity sizeEntity = mapSizeById.get(cartItem.getSizeId());
                    if (sizeEntity != null) {
                        dto.setSize(sizeEntity.getSize()); // Ánh xạ sizeName
                        dto.setSizeStock((long) sizeEntity.getStock());
                        dto.setUrlImage(sizeEntity.getVariant().getImageUrl());// Ánh xạ stock
                    }
                    return dto;
                })
                .toList();
        CartDTO dto = new CartDTO().setCartItemDTOS(cartItemDTOS)
                .setTotalPrice(cart.getTotalPrice())
                .setId(cart.getId());
        return new CartResponse(true, 200)
                .setDto(dto);
    }

    @Override
    public CartResponse updateCart(CartRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            throw new NotFoundException(false, 4004, "cart is not found");
        }

        CartItem cartItem = cartItemRepository.findByCartItemId(request.getCartItemId());

        if (request.getSizeId() != null) cartItem.setSizeId(request.getSizeId());

        if (request.getQuantity() != null) {
            SizeEntity size = sizeRepository.findById(request.getSizeId())
                    .orElseThrow(() -> new NotFoundException(false, 404, request.getSizeId() + " not exists"));
            double specialPrice = size.getPrice() - ((cartItem.getDiscount() * 0.01) * size.getPrice());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setProductPrice(specialPrice);
            cartItem.setLastUpdate(new Date());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * cartItem.getQuantity()));
        }

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
        return new CartResponse(true, 200, "update cart successfully");
    }
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

