package org.example.ezyshop.service.impl;

import jakarta.transaction.Transactional;
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
import org.example.ezyshop.mapper.PaymentMapper;
import org.example.ezyshop.repository.*;
import org.example.ezyshop.service.CartService;
import org.example.ezyshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderItemRepository orderItemRepository;

//    @Autowired
//    private PaymentService PaymentService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null ) {
            throw new NotFoundException(false, 404, "Cart is empty");
        }

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


        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new NotFoundException(false, 404, "address not found"));

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setPrice(totalPrice);
        shipment.setName(user.getUsername());
        shipment.setAddress(address.getProvince()+ ", " + address.getDistrict() + ", " + address.getWard());
//        order.setShipment(shipment);

        try {
            Optional<User> shipper = userRepository.findByEmailAndIsDeletedFalse("shipperTokoo01@yopmail.com");
            shipment.setShipper(shipper.get());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Step 4: Update product stock
//        cart.getCartItemDTOS().forEach(cartItem -> {
//            Product product = cartItem.getProduct();
//            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
//        });

        Payment payment = new Payment().setPaymentMethod(request.getPaymentMethod()).setOrder(order);
        order.setPayment(payment);
        repository.save(order);
        orderItemRepository.saveAll(orderItems);
        shipmentRepository.save(shipment);
        paymentRepository.save(payment);

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
        orderDTO.setPaymentDTO(PaymentMapper.INSTANCE.toDto(payment));
        response.setOrder(orderDTO);
        return response;
    }

    @Override
    public OrderResponse getOrderDetails(Long orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(false, 404, "Order not found"));

        List<OrderItem> orderItems = orderItemRepository.findByOrderIdAndIsDeletedFalse(orderId);

        List<OrderItemDTO> itemDTOs = orderItems.stream().map(this::mapToOrderItemDTO).toList();

        Payment payment = paymentRepository.findByOrderId(orderId);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setEmail(order.getEmail());
//        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setItems(itemDTOs);
        orderDTO.setPaymentDTO(PaymentMapper.INSTANCE.toDto(payment));

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
        List<Order> orders = repository.findByUserId(user.getId());
        List<Long> orderIds = orders.stream().map(Order::getId).toList();
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdIn(orderIds);
        List<OrderItemDTO> itemDTOs = orderItems.stream().map(this::mapToOrderItemDTO).toList();
        Map<Long, List<OrderItemDTO>> mapItemDTOSByOrderId = itemDTOs.stream()
                .collect(Collectors.groupingBy(OrderItemDTO::getOrderId));

        List<Shipment> shipments = shipmentRepository.findByOrderIdIn(orderIds);
        Map<Long, Shipment> mapShipmentByOrderId = shipments.stream()
                .collect(Collectors.toMap(
                        shipment -> shipment.getOrder().getId(), // Key: orderId
                        shipment -> shipment // Value: Shipment object
                ));
        List<OrderDTO> dtos = new ArrayList<>();
        for (Order order: orders) {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setEmail(order.getEmail());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus());
            dto.setPaymentDTO(PaymentMapper.INSTANCE.toDto(order.getPayment()));
            dto.setItems(mapItemDTOSByOrderId.get(order.getId()));
            Shipment shipment = mapShipmentByOrderId.get(order.getId());
            ShipmentDTO shipmentDTO = new ShipmentDTO();
            if (shipment != null) {
                shipmentDTO.setName(shipment.getName());
                shipmentDTO.setAddress(shipment.getAddress());
                shipmentDTO.setPrice(shipment.getPrice());
                shipmentDTO.setStatus(shipment.getStatus());
            }
            dto.setShipment(shipmentDTO);
            dtos.add(dto);
        }
        return new OrderResponse(true, 200).setDtoList(dtos);
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = repository.findByIdAndIsDeletedFalse(orderId);
        if (order == null) {
            throw new NotFoundException(false, 404, "Order not exists");
        }
        order.setStatus(status);
        repository.save(order);
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderId(item.getOrder().getId());
        dto.setOrderItemId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setDiscount(item.getDiscount());
        dto.setPrice(item.getPrice());
        return dto;
    }
}

