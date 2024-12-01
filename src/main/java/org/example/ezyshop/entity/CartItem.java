package org.example.ezyshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.example.ezyshop.base.BaseEntity;

@Entity
public class CartItem extends BaseEntity {

    private Integer quantity;

    private double discount;

    private double productPrice;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
