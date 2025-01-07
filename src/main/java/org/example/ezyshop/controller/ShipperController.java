package org.example.ezyshop.controller;

import org.example.ezyshop.constant.Constants;
import org.example.ezyshop.dto.order.OrderRequest;
import org.example.ezyshop.dto.order.OrderResponse;
import org.example.ezyshop.dto.shipment.ShipmentRequest;
import org.example.ezyshop.dto.shipment.ShipmentResponse;
import org.example.ezyshop.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipper")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShipperController {

    @Autowired
    private ShipperService service;

    @PutMapping("")
    public ResponseEntity<ShipmentResponse> updateShipment(@RequestBody ShipmentRequest request) {
        ShipmentResponse response = service.updateShipment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable Long orderId) {
//        OrderResponse response = orderService.getOrderDetails(orderId);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @GetMapping("/all")
    public ResponseEntity<ShipmentResponse> geAllShipment(@RequestParam(name = "pageNumber", defaultValue = Constants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                       @RequestParam(name = "pageSize", defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize) {
        ShipmentResponse response = service.getAllShipment(PageRequest.of(pageNumber, pageSize));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
