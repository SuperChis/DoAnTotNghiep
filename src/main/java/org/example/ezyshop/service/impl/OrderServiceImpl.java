package org.example.ezyshop.service.impl;

import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.order.OrderDTO;
import org.example.ezyshop.dto.order.OrderItemDTO;
import org.example.ezyshop.dto.order.OrderRequest;
import org.example.ezyshop.dto.order.OrderResponse;
import org.example.ezyshop.dto.shipment.ShipmentDTO;
import org.example.ezyshop.entity.*;
import org.example.ezyshop.enums.ShipmentStatus;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.repository.*;
import org.example.ezyshop.service.CartService;
import org.example.ezyshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderItemRepository orderItemRepository;

//    @Autowired
//    private PaymentService paymentService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null ) {
            throw new NotFoundException(false, 404, "Cart is empty");
        }

        // Step 1: Process payment
//        Payment payment = paymentService.processPayment(user, cart.getTotalPrice());

        List<CartItem> cartItems = cartItemRepository.findByCartItemIdIn(request.getCartItemIds());
        if (cartItems.isEmpty()) {
            throw new RequetFailException(false, 400, "Cart is empty");
        }

        double totalPrice = cartItems.stream()
                .mapToDouble(item -> (item.getProductPrice() - item.getDiscount()) * item.getQuantity())
                .sum();

        // Step 2: Create Order
        Order order = new Order();
        order.setEmail(user.getEmail());
        order.setUser(user);
        order.setOrderDate(new Date());
//        order.setPayment(payment);
        order.setTotalAmount(totalPrice);
        order.setStatus("PENDING");
        order.setDeleted(false);
        order.setCreated(new Date());
        order.setLastUpdate(new Date());

        // Step 3: Create Order Items
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem()
                        .setOrder(order)
                        .setProduct(cartItem.getProduct())
                        .setQuantity(cartItem.getQuantity())
                        .setDiscount(cartItem.getDiscount())
                        .setPrice(cartItem.getProductPrice())
                        .setVariantId(cartItem.getVariantId())
                        .setSizeId(cartItem.getSizeId())
                        .setDeleted(false))
                .toList();


        Address address = addressRepository.findDefaultAddressByUserId(user.getId());
        if (address == null) {
            throw new NotFoundException(false, 404, "default address not found");
        }

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setPrice(totalPrice);
        shipment.setName(user.getUsername());
        shipment.setAddress(address.getProvince()+ ", " + address.getDistrict() + ", " + address.getWard());

        repository.save(order);
        orderItemRepository.saveAll(orderItems);
        shipmentRepository.save(shipment);
        // Step 4: Update product stock
//        cart.getCartItemDTOS().forEach(cartItem -> {
//            Product product = cartItem.getProduct();
//            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
//        });

        // Step 5: Clear cart
        cartService.clearCart(request.getCartItemIds());
        List<OrderItemDTO> itemDTOs = orderItems.stream().map(this::mapToOrderItemDTO).toList();
        OrderResponse response = new OrderResponse(true, 200, "Order created successfully");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setEmail(order.getEmail());
//        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setItems(itemDTOs);

        response.setOrder(orderDTO);
        return response;
    }

    @Override
    public OrderResponse getOrderDetails(Long orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(false, 404, "Order not found"));

        List<OrderItem> orderItems = orderItemRepository.findByOrderIdAndIsDeletedFalse(orderId);

        List<OrderItemDTO> itemDTOs = orderItems.stream().map(this::mapToOrderItemDTO).toList();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setEmail(order.getEmail());
//        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setItems(itemDTOs);

        Shipment shipment = shipmentRepository.findByOrderId(orderId);
        ShipmentDTO shipmentDTO = new ShipmentDTO();
        if (shipment != null) {
            shipmentDTO.setName(shipment.getName());
            shipmentDTO.setAddress(shipment.getAddress());
            shipmentDTO.setPrice(shipment.getPrice());
            shipmentDTO.setStatus(shipment.getStatus());
        }

        OrderResponse response = new OrderResponse(true, 200);
        response.setOrder(orderDTO);
        response.setShipment(shipmentDTO);
        return response;
    }

    @Override
    public OrderResponse getALlOrderByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();


        return null;
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setDiscount(item.getDiscount());
        dto.setPrice(item.getPrice());
        return dto;
    }
}

