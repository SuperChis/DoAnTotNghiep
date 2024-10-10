package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.ezyshop.base.BaseEntity;

@Entity
@Data
public class Cart extends BaseEntity {

    private Integer quantity;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User account;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orderEntity_id")
    private OrderEntity orderEntity;

    private boolean isDeleted;
}
