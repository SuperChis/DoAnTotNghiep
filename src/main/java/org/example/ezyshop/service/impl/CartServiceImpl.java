package org.example.ezyshop.service.impl;

import jakarta.transaction.Transactional;
import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.cart.*;
import org.example.ezyshop.entity.*;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.mapper.ProductMapper;
import org.example.ezyshop.mapper.VariantMapper;
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
    private SizeRepository sizeRepository;

    private CartDTO toDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        List<Long> sizeIds = cartItems.stream().map(CartItem::getSizeId).toList();
        List<SizeEntity> sizes = sizeRepository.findByIdIn(sizeIds);
        Map<Long, SizeEntity> mapSizeById = sizes.stream()
                .collect(Collectors.toMap(
                        SizeEntity::getId, // Sử dụng sizeId làm key
                        Function.identity() // Giá trị là chính SizeEntity
                ));
//        List<CartItemDTO> cartItemDTOS = cartItems.stream()
//                .map(this::mapToCartItemDTO)
//                .collect(Collectors.toList());

        List<CartItemDTO> cartItemDTOS = cartItems.stream()
                .map(cartItem -> {
                    CartItemDTO cartItemDTO = mapToCartItemDTO(cartItem); // Hàm ánh xạ cơ bản
                    SizeEntity sizeEntity = mapSizeById.get(cartItem.getSizeId());
                    if (sizeEntity != null) {
                        cartItemDTO.setSize(sizeEntity.getSize()); // Ánh xạ sizeName
                        cartItemDTO.setSizeStock((long) sizeEntity.getStock());
                        cartItemDTO.setUrlImage(sizeEntity.getVariant().getImageUrl());// Ánh xạ stock
                    }
                    return cartItemDTO;
                })
                .toList();

        dto.setCartItemDTOS(cartItemDTOS);
        dto.setId(cart.getId());
        dto.setTotalPrice(cart.getTotalPrice());
        return dto;
    }

    private CartItemDTO mapToCartItemDTO(CartItem item) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductDTO(ProductMapper.MAPPER.toProductDTO(item.getProduct()));
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

        if (size.getStock() == null || size.getStock() <= 0 || size.getStock() < request.getQuantity()) {
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

        cartItemRepository.save(cartItem);

        size.setStock(size.getStock() - request.getQuantity());
        sizeRepository.save(size);
        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepository.save(product);

        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * request.getQuantity()));
        repository.save(cart);

        CartDTO dto = toDTO(cart);
        return new CartResponse(true, 200, "add product to cart successfully")
                .setDto(dto);
    }

    @Override
    public CartResponse getAllCarts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Cart cart = repository.findByUserId(user.getId());
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
                    dto.setVariantDTO(VariantMapper.MAPPER.toDTO(sizeEntity.getVariant()));
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
        Cart cart = repository.findByUserId(user.getId());
        if (cart == null) {
            throw new NotFoundException(false, 4004, "cart is not found");
        }

        CartItem cartItem = cartItemRepository.findByCartItemId(request.getCartItemId());

        if (request.getSizeId() != null) cartItem.setSizeId(request.getSizeId());

        if (request.getQuantity() != null) {
            SizeEntity size = sizeRepository.findById(cartItem.getSizeId())
                    .orElseThrow(() -> new NotFoundException(false, 404, request.getSizeId() + " not exists"));
            int oldQuantity = cartItem.getQuantity();
            if (size.getStock() == null || size.getStock() <= 0 || size.getStock() + oldQuantity < request.getQuantity()) {
                throw new RequetFailException(false, 400, "Product quantity is not enough");
            }
            size.setStock(size.getStock() + oldQuantity - request.getQuantity());
            double specialPrice = size.getPrice() - ((cartItem.getDiscount() * 0.01) * size.getPrice());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setProductPrice(specialPrice);
            cartItem.setLastUpdate(new Date());
            cart.setTotalPrice(cart.getTotalPrice() + cartItem.getProductPrice() * cartItem.getQuantity() - cartItem.getProductPrice() * oldQuantity);
            if(request.getQuantity() == 0) cartItem.setDeleted(true);
            sizeRepository.save(size);
        }

        cartItemRepository.save(cartItem);
        repository.save(cart);
        return new CartResponse(true, 200, "update cart successfully");
    }

    @Override
    public CartResponse deleteCartItem(Long cartItemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Cart cart = repository.findByUserId(user.getId());
        if (cart == null) {
            throw new NotFoundException(false, 4004, "cart is not found");
        }

        CartItem cartItem = cartItemRepository.findByCartItemId(cartItemId);

        if (cartItem == null) {
            throw new NotFoundException(false, 404, "Cart item not exists");
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItem.setDeleted(true);
        cartItemRepository.save(cartItem);
        repository.save(cart);

        SizeEntity size = sizeRepository.findById(cartItem.getSizeId())
                .orElseThrow(() -> new NotFoundException(false, 404, " not exists"));
        size.setStock(size.getStock() + cartItem.getQuantity());
        sizeRepository.save(size);

        return new CartResponse(true, 200, "delete cart item succesfully");
    }

    @Override
    public void clearCart(List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemRepository.findByCartItemIdIn(cartItemIds);
        cartItems.forEach(item -> item.setDeleted(true)); // Xóa mềm các CartItem
        cartItemRepository.saveAll(cartItems);
    }
}

