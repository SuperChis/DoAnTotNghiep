package org.example.ezyshop.enums;

import lombok.Getter;

@Getter
public enum ShipmentStatus {
    PENDING("Pending"), // Đơn hàng đang chờ xử lý
    PROCESSING("Processing"), // Đang xử lý đơn hàng
    SHIPPED("Shipped"), // Đã giao cho bên vận chuyển
    IN_TRANSIT("In Transit"), // Đang trên đường vận chuyển
    OUT_FOR_DELIVERY("Out for Delivery"), // Đang giao hàng
    DELIVERED("Delivered"), // Đã giao thành công
    RETURNED("Returned"), // Đã trả hàng
    CANCELED("Canceled"), // Đơn hàng đã bị hủy
    FAILED_DELIVERY("Failed Delivery"); // Giao hàng không thành công

    private final String text;

    ShipmentStatus(String text) {
        this.text = text;
    }
}
