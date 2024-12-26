package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ezyshop.base.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {
    @Column(nullable = false)
    private String email;

    private LocalDate orderDate;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private Double totalAmount;

    private String status;

    private boolean isDeleted;
}
