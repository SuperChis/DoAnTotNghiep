package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ezyshop.base.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {
    @Column(nullable = false)
    private String email;

    private Date orderDate;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private Double totalAmount;

    private String status;
    /*
    PENDING: Chờ thanh toán.

    PAID: Đã thanh toán.
    CANCELLED: Đơn hàng đã bị hủy.
    SHIPPED: Đơn hàng đã được gửi đi.
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shipment_id")
//    private Shipment shipment;

    private boolean isDeleted;
}
