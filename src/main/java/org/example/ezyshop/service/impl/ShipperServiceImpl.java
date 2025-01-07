package org.example.ezyshop.service.impl;

import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.order.OrderDTO;
import org.example.ezyshop.dto.order.OrderItemDTO;
import org.example.ezyshop.dto.pagination.PageDto;
import org.example.ezyshop.dto.shipment.ShipmentDTO;
import org.example.ezyshop.dto.shipment.ShipmentRequest;
import org.example.ezyshop.dto.shipment.ShipmentResponse;
import org.example.ezyshop.entity.*;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.mapper.PaymentMapper;
import org.example.ezyshop.repository.*;
import org.example.ezyshop.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShipperServiceImpl implements ShipperService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private PaymentRepository paymentRepository;

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

    public ShipmentResponse updateShipment(ShipmentRequest request) {
        Order order = orderRepository.findByIdAndIsDeletedFalse(request.getOrderId());
        if (request.getOrderStatus() != null) {
            order.setStatus(request.getOrderStatus());
        }
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdAndIsDeletedFalse(order.getId());
        List<OrderItemDTO> itemDTOs = orderItems.stream().map(this::mapToOrderItemDTO).toList();
        Shipment shipment = shipmentRepository.findByOrderId(request.getOrderId());
        if (request.getStatus() != null) {
            shipment.setStatus(request.getStatus());
        }
        Payment payment = paymentRepository.findByOrderId(order.getId());
        if (payment == null ) {
            throw new NotFoundException(false, 404, "payment not found");
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setEmail(order.getEmail());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setItems(itemDTOs);
        orderDTO.setPaymentDTO(PaymentMapper.INSTANCE.toDto(payment));

        ShipmentDTO shipmentDTO = new ShipmentDTO();
        if (shipment != null) {
            shipmentDTO.setName(shipment.getName());
            shipmentDTO.setAddress(shipment.getAddress());
            shipmentDTO.setPrice(shipment.getPrice());
            shipmentDTO.setStatus(shipment.getStatus());
        }
        shipmentDTO.setOrderDTO(orderDTO);
        orderRepository.save(order);
        shipmentRepository.save(shipment);
        ShipmentResponse response = new ShipmentResponse(true, 200);
        return response.setDto(shipmentDTO);
    }

    public ShipmentResponse getAllShipment(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        Page<Shipment> shipmentPage = shipmentRepository.findByShipperId(user.getId(), pageable);
        List<Shipment> shipments = shipmentPage.getContent();
        List<ShipmentDTO> shipmentDTOS = shipments.stream().map(shipment -> {
            ShipmentDTO shipmentDTO = new ShipmentDTO();
            if (shipment != null) {
                shipmentDTO.setName(shipment.getName());
                shipmentDTO.setAddress(shipment.getAddress());
                shipmentDTO.setPrice(shipment.getPrice());
                shipmentDTO.setStatus(shipment.getStatus());
            }
            Order order = shipment.getOrder();
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setEmail(order.getEmail());
            orderDTO.setTotalAmount(order.getTotalAmount());
            orderDTO.setStatus(order.getStatus());
//            orderDTO.setItems(itemDTOs);
//            orderDTO.setPaymentDTO(PaymentMapper.INSTANCE.toDto(payment));
            shipmentDTO.setOrderDTO(orderDTO);
            return shipmentDTO;
        }).toList();

        List<Long> orderIds = shipments.stream().map(shipment -> shipment.getOrder().getId()).toList();
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdIn(orderIds);
        List<OrderItemDTO> orderItemDTOS = orderItems.stream().map(this::mapToOrderItemDTO).toList();
        Map<Long, List<OrderItemDTO>> mapOrderItemDTOByOrderId = orderItemDTOS.stream()
                .collect(Collectors.groupingBy(OrderItemDTO::getOrderId));
        for (ShipmentDTO shipmentDTO : shipmentDTOS) {
            shipmentDTO.getOrderDTO().setItems(mapOrderItemDTOByOrderId.get(shipmentDTO.getOrderDTO().getId()));
        }
        return new ShipmentResponse(true, 200).setDtoList(shipmentDTOS).setPageDto(PageDto.populatePageDto(shipmentPage));
    }


}
